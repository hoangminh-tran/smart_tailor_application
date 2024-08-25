package com.smart.tailor.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.*;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.enums.PaymentMethod;
import com.smart.tailor.enums.PaymentType;
import com.smart.tailor.event.CreateOrderEvent;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.mapper.DesignDetailMapper;
import com.smart.tailor.mapper.OrderMapper;
import com.smart.tailor.mapper.PaymentMapper;
import com.smart.tailor.repository.DesignDetailRepository;
import com.smart.tailor.repository.OrderRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.*;
import com.smart.tailor.utils.response.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BrandService brandService;
    private final DesignService designService;
    private final CustomerService customerService;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;
    private final DesignDetailMapper detailMapper;
    private final BrandMaterialService brandMaterialService;
    private final DesignDetailRepository detailRepository;
    private final PaymentService paymentService;
    private final SystemPropertiesService systemPropertiesService;
    private final BrandPropertiesService brandPropertiesService;
    private final EmployeeService employeeService;
    private final OrderStageService stageService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GHTKShippingService ghtkShippingService;
    private final JwtService jwtService;
    private final NotificationService notificationService;
    private final SizeExpertTailoringService sizeExpertTailoringService;
    private final BrandLaborQuantityService brandLaborQuantityService;
    private final PayOSService payOSService;
    private static final BigDecimal PIXEL_TO_CENTIMETER = new BigDecimal("0.0264583");
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
//    private final Map<String, OrderDetailPriceResponse> calculatePrice = new HashMap<>();

    @Value("${client.server.link}")
    private String clientServerLink;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) throws Exception {
        if (!Utilities.isStringNotNullOrEmpty(orderRequest.getDesignID())) {
            throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": designID");
        }
        String designID = orderRequest.getDesignID();

        DesignResponse designResponse = designService.getDesignResponseByID(designID);
        if (designResponse == null) {
            throw new BadRequestException(MessageConstant.INVALID_INPUT + ": designID");
        }

        Integer quantity = orderRequest.getQuantity() != null && Utilities.isValidNumber(orderRequest.getQuantity().toString()) ? orderRequest.getQuantity() : 0;

        UserResponse userResponse = designResponse.getUser();
        CustomerResponse customerResponse = customerService.getCustomerByUserID(userResponse.getUserID());

        String address;
        String province;
        String district;
        String ward;
        if (Utilities.isNonNullOrEmpty(orderRequest.getAddress()) && Utilities.isNonNullOrEmpty(orderRequest.getProvince()) && Utilities.isNonNullOrEmpty(orderRequest.getDistrict()) && Utilities.isNonNullOrEmpty(orderRequest.getWard())) {
            address = orderRequest.getAddress();
            province = orderRequest.getProvince();
            district = orderRequest.getDistrict();
            ward = orderRequest.getWard();
        } else {
            address = customerResponse.getAddress();
            province = customerResponse.getProvince();
            district = customerResponse.getDistrict();
            ward = customerResponse.getWard();
        }

        String phone;
        if (!Utilities.isStringNotNullOrEmpty(orderRequest.getPhone())) {
            phone = customerResponse.getPhoneNumber();
        } else if (!Utilities.isValidVietnamesePhoneNumber(orderRequest.getPhone())) {
            phone = customerResponse.getPhoneNumber();
        } else {
            phone = orderRequest.getPhone();
        }

        String buyerName;
        if (Utilities.isNonNullOrEmpty(orderRequest.getBuyerName())) {
            buyerName = orderRequest.getBuyerName();
        } else {
            buyerName = customerResponse.getFullName();
        }

        String orderType = orderRequest.getOrderType();
        if (orderRequest.getParentOrderID() != null) {
            var parentOrderID = orderRequest.getParentOrderID();
            Optional<Order> parentOrder = orderRepository.findById(parentOrderID);
            if (parentOrder.isPresent()) {
                Order order = Order.builder()
                        .quantity(quantity)
                        .address(address)
                        .province(province)
                        .district(district)
                        .ward(ward)
                        .phone(phone)
                        .buyerName(buyerName)
                        .orderStatus(orderRequest.getOrderStatus())
                        .orderType("SUB_ORDER")
                        .parentOrder(parentOrder.get())
                        .totalPrice(0)
                        .expectedStartDate(LocalDateTime.now().plusDays(1))
                        .employee(parentOrder.get().getEmployee())
                        .build();
                var orderResponse = orderRepository.save(order);
                return orderMapper.mapToOrderResponse(orderResponse);
            } else {
                throw new RuntimeException("Parent order not found");
            }
        }
        Order order = Order.builder()
                .quantity(quantity)
                .address(address)
                .province(province)
                .district(district)
                .ward(ward)
                .orderType(orderType)
                .phone(phone)
                .buyerName(buyerName)
                .orderStatus(orderRequest.getOrderStatus())
                .totalPrice(0)
                .orderType("PARENT_ORDER")
                .employee(getSuitableEmp())
                .expectedStartDate(LocalDateTime.now().plusDays(1))
                .build();
        logger.error("EMP: {}", getSuitableEmp());
        var orderResponse = orderRepository.save(order);
        return orderMapper.mapToOrderResponse(orderResponse);
    }

    @Override
    public List<OrderResponse> getParentOrderByDesignID(String designID) throws Exception {
        List<Order> orderList = orderRepository.getParentOrderByDesignID(designID);
        List<OrderResponse> orderResponse = new ArrayList<>();
        for (Order order : orderList) {
            var response = orderMapper.mapToOrderResponse(order);
            orderResponse.add(response);
        }
        return orderResponse.stream().sorted(Comparator.comparing(OrderResponse::getCreateDate).reversed()).toList();
    }

//    @Override
//    public void updateOrderStatus(String orderID, String orderStatus) {
//        var order = getOrderById(orderID).isPresent() ? getOrderById(orderID).get() : null;
//        if (order == null) {
//            throw new BadRequestException(MessageConstant.RESOURCE_NOT_FOUND);
//        }
//        order.setOrderStatus(OrderStatus.valueOf(orderStatus));
//        updateOrder(order);
//    }

    private OrderCustomResponse convertToOrderCustomResponse(Order order, List<DesignDetail> designDetails, List<DesignMaterialDetailResponse> designMaterialDetailResponseList) {
        return OrderCustomResponse
                .builder()
                .designResponse(designService.getDesignByOrderID(order.getOrderID()))
                .parentOrderID(order.getParentOrder() != null ? order.getParentOrder().getOrderID() : null)
                .orderType(order.getOrderType())
                .orderID(order.getOrderID())
                .quantity(order.getQuantity())
                .orderStatus(order.getOrderStatus())
                .rating(order.getRating())
                .address(order.getAddress())
                .province(order.getProvince())
                .labelID(order.getLabelID())
                .district(order.getDistrict())
                .ward(order.getWard())
                .phone(order.getPhone())
                .buyerName(order.getBuyerName())
                .totalPrice(order.getTotalPrice())
                .employeeID(order.getEmployee().getEmployeeID())
                .expectedStartDate(Utilities.convertLocalDateTimeToString(order.getExpectedStartDate()))
                .expectedProductCompletionDate(Utilities.convertLocalDateTimeToString(order.getExpectedProductCompletionDate()))
                .estimatedDeliveryDate(Utilities.convertLocalDateTimeToString(order.getEstimatedDeliveryDate()))
                .productionStartDate(Utilities.convertLocalDateTimeToString(order.getProductionStartDate()))
                .productionCompletionDate(Utilities.convertLocalDateTimeToString(order.getProductionCompletionDate()))
                .createDate(order.getCreateDate() != null ? Utilities.convertLocalDateTimeToString(order.getCreateDate()) : null)
                .detailList(
                        designDetails
                                .stream()
                                .map(detailMapper::mapperToDesignDetailResponse)
                                .toList()
                )
                .paymentList(
                        paymentService.findAllByOrderID(order.getOrderID())
                                .stream()
                                .filter(p -> !p.getPaymentType().equals(PaymentType.ORDER_REFUND))
                                .map(paymentMapper::mapperToPaymentResponse)
                                .sorted(Comparator.comparing(PaymentResponse::getCreateDate).reversed())
                                .toList()
                )
                .designMaterialDetailResponseList(designMaterialDetailResponseList)
                .build();
    }

    private Pair<BigDecimal, BigDecimal> calculateItemMaskByBrandMaterial(ItemMaskInformation itemMaskInfo, String brandID, BigDecimal pixelToCentimeter, Float pixelRatioRealFromWeb) {
        // Convert scales from pixels to centimeters
        BigDecimal scaleX_Centimeter = BigDecimal.valueOf(Math.abs(itemMaskInfo.getScaleX())).multiply(pixelToCentimeter);
        BigDecimal scaleY_Centimeter = BigDecimal.valueOf(Math.abs(itemMaskInfo.getScaleY())).multiply(pixelToCentimeter);
        BigDecimal actual_ScaleX_Centimeter = scaleX_Centimeter.multiply(BigDecimal.valueOf(pixelRatioRealFromWeb));
        BigDecimal actual_ScaleY_Centimeter = scaleY_Centimeter.multiply(BigDecimal.valueOf(pixelRatioRealFromWeb));

        String materialID = itemMaskInfo.getMaterialID();
        BigDecimal brandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getBrandPriceByBrandIDAndMaterialID(brandID, materialID));
        // Calculate area in square meters
        BigDecimal area = actual_ScaleX_Centimeter.multiply(actual_ScaleY_Centimeter).divide(BigDecimal.valueOf(10000), 10, RoundingMode.HALF_UP); // Rounding to 10 decimal places

        // Calculate price, rounding up to the nearest whole number
        BigDecimal price = area.multiply(brandPriceMaterial).setScale(0, RoundingMode.CEILING);

        return Pair.of(area, price);
    }

    private Pair<BigDecimal, BigDecimal> calculatePartOfDesignByBrandMaterial(PartOfDesignInformation partInfo, String brandID, BigDecimal ratio) {
        BigDecimal width = BigDecimal.valueOf(partInfo.getWidth()).multiply(ratio); // in Centimeter
        BigDecimal height = BigDecimal.valueOf(partInfo.getHeight()).multiply(ratio); // in Centimeter

        String materialID = partInfo.getMaterialID();
        BigDecimal brandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getBrandPriceByBrandIDAndMaterialID(brandID, materialID));

        // Correct calculation
        BigDecimal area = width.multiply(height).divide(BigDecimal.valueOf(10000), 10, RoundingMode.HALF_UP); // Keeping 10 decimal places for precision
        BigDecimal price = area.multiply(brandPriceMaterial).setScale(0, RoundingMode.CEILING); // Rounding up to nearest whole number

        return Pair.of(area, price);
    }

    private Triple<BigDecimal, BigDecimal, BigDecimal> calculatePartOfDesignWithoutBrand(PartOfDesignInformation partInfo, BigDecimal ratio) {
        BigDecimal width = BigDecimal.valueOf(partInfo.getWidth()).multiply(ratio); // in Centimeter
        BigDecimal height = BigDecimal.valueOf(partInfo.getHeight()).multiply(ratio); // in Centimeter

        String materialID = partInfo.getMaterialID();
        BigDecimal minBrandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getMinPriceByMaterialID(materialID));
        BigDecimal maxBrandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getMaxPriceByMaterialID(materialID));

        // Correct calculation
        BigDecimal area = width.multiply(height).divide(BigDecimal.valueOf(10000), 10, RoundingMode.HALF_UP); // Keeping 10 decimal places for precision
        BigDecimal minPrice = area.multiply(minBrandPriceMaterial).setScale(0, RoundingMode.CEILING);
        BigDecimal maxPrice = area.multiply(maxBrandPriceMaterial).setScale(0, RoundingMode.CEILING);

        return Triple.of(area, minPrice, maxPrice);
    }

    private Triple<BigDecimal, BigDecimal, BigDecimal> calculateItemMaskWithoutBrand(ItemMaskInformation itemMaskInfo, BigDecimal pixelToCentimeter, Float pixelRatioRealFromWeb) {
        // Convert scales from pixels to centimeters
        BigDecimal scaleX_Centimeter = BigDecimal.valueOf(Math.abs(itemMaskInfo.getScaleX())).multiply(pixelToCentimeter);
        BigDecimal scaleY_Centimeter = BigDecimal.valueOf(Math.abs(itemMaskInfo.getScaleY())).multiply(pixelToCentimeter);
        BigDecimal actual_ScaleX_Centimeter = scaleX_Centimeter.multiply(BigDecimal.valueOf(pixelRatioRealFromWeb));
        BigDecimal actual_ScaleY_Centimeter = scaleY_Centimeter.multiply(BigDecimal.valueOf(pixelRatioRealFromWeb));

        String materialID = itemMaskInfo.getMaterialID();
        BigDecimal minBrandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getMinPriceByMaterialID(materialID));
        BigDecimal maxBrandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getMaxPriceByMaterialID(materialID));

        // Calculate area in square meters
        BigDecimal area = actual_ScaleX_Centimeter.multiply(actual_ScaleY_Centimeter).divide(BigDecimal.valueOf(10000), 10, RoundingMode.HALF_UP); // Rounding to 10 decimal places
        BigDecimal minPrice = area.multiply(minBrandPriceMaterial).setScale(0, RoundingMode.CEILING);
        BigDecimal maxPrice = area.multiply(maxBrandPriceMaterial).setScale(0, RoundingMode.CEILING);

        return Triple.of(area, minPrice, maxPrice);
    }


    public OrderDetailPriceResponse calculateTotalPriceForSpecificOrder(String parentOrderID) throws Exception {
        var orderCustomResponse = getOrderById(parentOrderID).get();
        var listSubOrders = getSubOrderByParentIDNOTCHECKSTATUS(parentOrderID);
        if (listSubOrders.isEmpty()) {
            listSubOrders = List.of(safeMapToOrderResponse(orderCustomResponse));
        }
        var designResponse = designService.getDesignByOrderID(parentOrderID);
        var expertTailoring = designResponse.getExpertTailoring();
        List<PartOfDesignInformation> partOfDesignInformationList = new ArrayList<>();
        List<ItemMaskInformation> itemMaskInformationList = new ArrayList<>();

        designResponse.getPartOfDesign().forEach(partOfDesignResponse -> {
            partOfDesignInformationList.add(PartOfDesignInformation
                    .builder()
                    .partOfDesignName(partOfDesignResponse.getPartOfDesignName())
                    .width(partOfDesignResponse.getWidth())
                    .height(partOfDesignResponse.getHeight())
                    .materialID(partOfDesignResponse.getMaterial().getMaterialID())
                    .materialName(partOfDesignResponse.getMaterial().getMaterialName())
                    .build());
            partOfDesignResponse.getItemMasks().forEach(itemMaskResponse -> {
                itemMaskInformationList.add(ItemMaskInformation
                        .builder()
                        .itemMaskID(itemMaskResponse.getItemMaskID())
                        .itemMaskName(itemMaskResponse.getItemMaskName())
                        .scaleX(itemMaskResponse.getScaleX())
                        .scaleY(itemMaskResponse.getScaleY())
                        .materialID(itemMaskResponse.getMaterial().getMaterialID())
                        .materialName(itemMaskResponse.getMaterial().getMaterialName())
                        .build());
            });
        });
        // Get Min Weight and Max Weight of each Design
        var minWeightParentOrder = designResponse.getMinWeight() * orderCustomResponse.getQuantity();
        var maxWeightParentOrder = designResponse.getMaxWeight() * orderCustomResponse.getQuantity();
        var averageWeightParentOrder = (float) (minWeightParentOrder + maxWeightParentOrder) / 2;
        var maximumShippingWeight = Integer.parseInt(systemPropertiesService.getByName("MAX_SHIPPING_WEIGHT").getPropertyValue());

        BigDecimal shippingFee = BigDecimal.valueOf(-1);

        // Calculate Shipping fee based on Address and Weight of Customer
        if (orderCustomResponse.getAddress() != null && orderCustomResponse.getProvince() != null &&
                orderCustomResponse.getDistrict() != null && orderCustomResponse.getWard() != null && averageWeightParentOrder < maximumShippingWeight) {
            var smartTailorAddress = systemPropertiesService.getByName("SMART_TAILOR_ADDRESS").getPropertyValue();
            var smartTailorProvince = systemPropertiesService.getByName("SMART_TAILOR_PROVINCE").getPropertyValue();
            var smartTailorDistrict = systemPropertiesService.getByName("SMART_TAILOR_DISTRICT").getPropertyValue();
            var smartTailorWard = systemPropertiesService.getByName("SMART_TAILOR_WARD").getPropertyValue();
            OrderShippingRequest.OrderShippingDetailRequest orderShippingDetailRequest =
                    new OrderShippingRequest.OrderShippingDetailRequest(
                            smartTailorAddress,
                            smartTailorProvince,
                            smartTailorDistrict,
                            smartTailorWard,
                            orderCustomResponse.getAddress(),
                            orderCustomResponse.getProvince(),
                            orderCustomResponse.getDistrict(),
                            orderCustomResponse.getWard(),
                            averageWeightParentOrder
                    );


            var orderShippingRequest =
                    OrderShippingRequest
                            .builder()
                            .order(orderShippingDetailRequest)
                            .build();

            var feeResponse = ghtkShippingService.calculateShippingFee(orderShippingRequest);
            if (feeResponse != null && feeResponse.getSuccess()) {
                shippingFee = Utilities.roundToNearestThousand(BigDecimal.valueOf(feeResponse.getFee()));
            }
        }

        BigDecimal totalPriceOfParentOrder = BigDecimal.ZERO;
        var divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());
        var pixelToCentimeter = BigDecimal.valueOf(Double.parseDouble(systemPropertiesService.getByName("PIXEL_TO_CENTIMETER").getPropertyValue()));
        var pixelRatioRealFromWeb = Float.parseFloat(systemPropertiesService.getByName("PIXEL_RATIO_REAL_FROM_WEB").getPropertyValue());
        var maxQuantity = Integer.MIN_VALUE;
        var minQuantity = Integer.MAX_VALUE;
        List<BigDecimal> subOrderIncludeTwoStage = new ArrayList<>();
        List<List<BigDecimal>> subOrderIncludeFourStage = new ArrayList<>();
        Map<String, DesignMaterialDetailResponse> designMaterialDetailResponseMap = new HashMap<>();
        List<BrandDetailPriceResponse> brandDetailPriceResponseList = new ArrayList<>();
        for (var subOrder : listSubOrders) {
            List<DesignDetail> designDetailList = detailRepository.getDesignDetailBySubOrderID(subOrder.getOrderID());
            if (subOrder.getBrand() != null) {
                int totalQuantityOfSubOrder = designDetailList
                        .stream()
                        .mapToInt(DesignDetail::getQuantity)
                        .sum();

                BigDecimal totalPriceOfEachSubOrder = BigDecimal.ZERO;
                maxQuantity = Math.max(maxQuantity, totalQuantityOfSubOrder);
                minQuantity = Math.min(minQuantity, totalQuantityOfSubOrder);

                Brand brand = null;
                for (DesignDetail designDetail : designDetailList) {
                    brand = designDetail.getBrand();
                    var designDetailQuantity = BigDecimal.valueOf(designDetail.getQuantity());
                    var size = designDetail.getSize();

                    var sizeExpertTailoring = sizeExpertTailoringService.findSizeExpertTailoringByExpertTailoringIDAndSizeID(
                            expertTailoring.getExpertTailoringID(),
                            size.getSizeID()
                    );

                    var ratio = BigDecimal.valueOf(sizeExpertTailoring.getRatio());

                    // Calculate Total Price Of PartOfDesign of SubOrder
                    BigDecimal totalPricePartOfDesignOfSubOrder = BigDecimal.ZERO;
                    for (PartOfDesignInformation partOfDesignInformation : partOfDesignInformationList) {
                        var calculatePartOfDesignResponse = calculatePartOfDesignByBrandMaterial(partOfDesignInformation, brand.getBrandID(), ratio);
                        var partOfDesignName = partOfDesignInformation.getPartOfDesignName();
                        var areaPartOfDesign = calculatePartOfDesignResponse.getFirst();
                        var pricePartOfDesign = calculatePartOfDesignResponse.getSecond();
                        totalPricePartOfDesignOfSubOrder = totalPricePartOfDesignOfSubOrder.add(pricePartOfDesign);
                        updateDesignMaterialDetailMap(designMaterialDetailResponseMap, partOfDesignName, areaPartOfDesign, pricePartOfDesign);
                    }

                    // Calculate Total Price Of ItemMask of SubOrder
                    BigDecimal totalPriceItemMaskOfSubOrder = BigDecimal.ZERO;
                    for (ItemMaskInformation itemMaskInformation : itemMaskInformationList) {
                        var calculateItemMaskResponse = calculateItemMaskByBrandMaterial(itemMaskInformation, brand.getBrandID(), pixelToCentimeter, pixelRatioRealFromWeb);
                        var itemMaskID = itemMaskInformation.getItemMaskID();
                        var itemMaskName = itemMaskInformation.getItemMaskName();
                        var areaItemMask = calculateItemMaskResponse.getFirst();
                        var priceItemMask = calculateItemMaskResponse.getSecond();
                        totalPriceItemMaskOfSubOrder = totalPriceItemMaskOfSubOrder.add(priceItemMask);
                        updateDesignMaterialDetailMap(designMaterialDetailResponseMap, itemMaskID + " " + itemMaskName, areaItemMask, priceItemMask);
                    }

                    // Get Labor Quantity of Each Brand for SubOrder
                    var brandLaborQuantityOfSubOrder = brandLaborQuantityService.findLaborQuantityByBrandIDAndBrandQuantity(brand.getBrandID(), totalQuantityOfSubOrder);
                    BigDecimal brandLaborCostPerQuantity = BigDecimal.valueOf(brandLaborQuantityOfSubOrder.getLaborCostPerQuantity());
                    updateDesignMaterialDetailMap(designMaterialDetailResponseMap, "Brand Labor Quantity", null, brandLaborCostPerQuantity);
                    totalPriceOfEachSubOrder = totalPriceOfEachSubOrder.add(totalPricePartOfDesignOfSubOrder.add(totalPriceItemMaskOfSubOrder).add(brandLaborCostPerQuantity).multiply(designDetailQuantity));
                }
                totalPriceOfEachSubOrder = Utilities.roundToNearestThousand(totalPriceOfEachSubOrder);
                BigDecimal brandDepositStage = BigDecimal.valueOf(-1);
                BigDecimal brandFirstStage = BigDecimal.valueOf(-1);
                BigDecimal brandSecondStage = BigDecimal.valueOf(-1);
                if (maxQuantity < divideNumber) {
                    brandDepositStage = Utilities.roundToNearestThousand(totalPriceOfEachSubOrder);
                    subOrderIncludeTwoStage.add(brandDepositStage);
                } else {
                    brandDepositStage = totalPriceOfEachSubOrder.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP);
                    brandFirstStage = totalPriceOfEachSubOrder.subtract(brandDepositStage).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP);
                    brandSecondStage = totalPriceOfEachSubOrder.subtract(brandDepositStage).subtract(brandFirstStage).setScale(0, RoundingMode.HALF_UP);

                    // Round Brand Price at Deposit, First, Second Stage to nearest Thousand
                    brandDepositStage = Utilities.roundToNearestThousand(brandDepositStage);
                    brandFirstStage = Utilities.roundToNearestThousand(brandFirstStage);
                    brandSecondStage = Utilities.roundToNearestThousand(brandSecondStage);

                    // Update Total Price Of SubOrder with new Nearest Stage of Deposit, First, Second Stage
                    totalPriceOfEachSubOrder = brandDepositStage.add(brandFirstStage).add(brandSecondStage);

                    List<BigDecimal> fourStage = new ArrayList<>();
                    fourStage.add(brandDepositStage);
                    fourStage.add(brandFirstStage);
                    fourStage.add(brandSecondStage);
                    subOrderIncludeFourStage.add(fourStage);
                }
                BrandDetailPriceResponse brandDetailPriceResponse = BrandDetailPriceResponse
                        .builder()
                        .brandID(brand.getBrandID())
                        .subOrderID(subOrder.getOrderID())
                        .brandPriceDeposit(brandDepositStage.toString())
                        .brandPriceFirstStage(brandFirstStage.toString())
                        .brandPriceSecondStage(brandSecondStage.toString())
                        .build();
                brandDetailPriceResponseList.add(brandDetailPriceResponse);

                // Add Total Price Of SubOrder to ParentOrder
                totalPriceOfParentOrder = totalPriceOfParentOrder.add(totalPriceOfEachSubOrder);

            } else {
                for (DesignDetail designDetail : designDetailList) {
                    var size = designDetail.getSize();

                    var sizeExpertTailoring = sizeExpertTailoringService.findSizeExpertTailoringByExpertTailoringIDAndSizeID(
                            expertTailoring.getExpertTailoringID(),
                            size.getSizeID()
                    );

                    var ratio = BigDecimal.valueOf(sizeExpertTailoring.getRatio());

                    // Calculate Total Price Of PartOfDesign of SubOrder
                    BigDecimal totalPricePartOfDesignOfSubOrder = BigDecimal.ZERO;
                    for (PartOfDesignInformation partOfDesignInformation : partOfDesignInformationList) {
                        var calculatePartOfDesignWithoutBrand = calculatePartOfDesignWithoutBrand(partOfDesignInformation, ratio);
                        var partOfDesignName = partOfDesignInformation.getPartOfDesignName();
                        var areaPartOfDesign = calculatePartOfDesignWithoutBrand.getLeft();
                        var minPricePartOfDesign = calculatePartOfDesignWithoutBrand.getMiddle();
                        var maxPricePartOfDesign = calculatePartOfDesignWithoutBrand.getRight();
                        updateDesignMaterialDetailWithoutBrandMap(designMaterialDetailResponseMap, partOfDesignName, areaPartOfDesign, minPricePartOfDesign, maxPricePartOfDesign);
                    }

                    // Calculate Total Price Of ItemMask of SubOrder
                    BigDecimal totalPriceItemMaskOfSubOrder = BigDecimal.ZERO;
                    for (ItemMaskInformation itemMaskInformation : itemMaskInformationList) {
                        var calculateItemMaskWithoutBrand = calculateItemMaskWithoutBrand(itemMaskInformation, pixelToCentimeter, pixelRatioRealFromWeb);
                        var itemMaskID = itemMaskInformation.getItemMaskID();
                        var itemMaskName = itemMaskInformation.getItemMaskName();
                        var areaItemMask = calculateItemMaskWithoutBrand.getLeft();
                        var minPriceItemMask = calculateItemMaskWithoutBrand.getMiddle();
                        var maxPriceItemMask = calculateItemMaskWithoutBrand.getRight();
                        updateDesignMaterialDetailWithoutBrandMap(designMaterialDetailResponseMap, itemMaskID + " " + itemMaskName, areaItemMask, minPriceItemMask, maxPriceItemMask);
                    }

                    // Get Labor Quantity of Each Brand for SubOrder
                    var minLaborQuantityCost = BigDecimal.valueOf(brandLaborQuantityService.getMinBrandLaborQuantityCostByQuantity(subOrder.getQuantity()));
                    var maxLaborQuantityCost = BigDecimal.valueOf(brandLaborQuantityService.getMaxBrandLaborQuantityCostByQuantity(subOrder.getQuantity()));
                    updateDesignMaterialDetailWithoutBrandMap(designMaterialDetailResponseMap, "Brand Labor Quantity", null, minLaborQuantityCost, maxLaborQuantityCost);
                }
            }
        }
        List<DesignMaterialDetailResponse> designMaterialDetailResponseList = new ArrayList<>();
        for (DesignMaterialDetailResponse response : designMaterialDetailResponseMap.values()) {
            designMaterialDetailResponseList.add(
                    DesignMaterialDetailResponse
                            .builder()
                            .detailName(
                                    Utilities.isNonNullOrEmpty(response.getDetailName()) ? response.getDetailName() : null
                            )
                            .minMeterSquare(
                                    response.getMinMeterSquare() != null ? ((BigDecimal) response.getMinMeterSquare()).setScale(4, RoundingMode.HALF_UP).toString() : null
                            )
                            .maxMeterSquare(
                                    response.getMaxMeterSquare() != null ? ((BigDecimal) response.getMaxMeterSquare()).setScale(4, RoundingMode.HALF_UP).toString() : null
                            )
                            .minPriceMaterial(
                                    response.getMinPriceMaterial() != null ? response.getMinPriceMaterial().toString() : null
                            )
                            .maxPriceMaterial(
                                    response.getMaxPriceMaterial() != null ? response.getMaxPriceMaterial().toString() : null
                            )
                            .build()
            );
        }

        // Get Order Fee Percentage to calculate Commission for Each Order
        var orderFeePercentage = Integer.parseInt(systemPropertiesService.getByName("ORDER_FEE_PERCENTAGE").getPropertyValue());

        // Calculate Commission by divide 100
        BigDecimal commission = totalPriceOfParentOrder.multiply(BigDecimal.valueOf(orderFeePercentage).divide(BigDecimal.valueOf(100)));
        commission = Utilities.roundToNearestThousand(commission);

        BigDecimal customerDepositStage = BigDecimal.valueOf(-1);
        BigDecimal customerFirstStage = BigDecimal.valueOf(-1);
        BigDecimal customerSecondStage = BigDecimal.valueOf(-1);

        // Case all subOrder have only two stage.
        // CustomerDepositStage = sum of All Deposit Stage of SubOrder
        if (minQuantity < divideNumber && maxQuantity < divideNumber) {
            customerDepositStage = subOrderIncludeTwoStage
                    .stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Case at least one SubOrder has two stage and one SubOrder has four stage
            // CustomerDepositStage = sum of All Deposit Two Stage of SubOrder and all of 1/3 Deposit Four Stage of SubOrder
            // CustomerFirstStage = CustomerSecondStage = sum all  1/3 Deposit Four Stage of SubOrder
        } else if (minQuantity < divideNumber && maxQuantity >= divideNumber) {
            customerDepositStage = subOrderIncludeTwoStage
                    .stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            customerFirstStage = BigDecimal.ZERO;
            customerSecondStage = BigDecimal.ZERO;
            for (List<BigDecimal> stage : subOrderIncludeFourStage) {
                customerDepositStage = customerDepositStage.add(stage.get(0));
                customerFirstStage = customerFirstStage.add(stage.get(1));
                customerSecondStage = customerSecondStage.add(stage.get(2));
            }

            // Case all subOrder have 4 stage
            // CustomerDepositStage = CustomerFirstStage = CustomerSecondStage = 1/3 Total Price
        } else if (minQuantity >= divideNumber && maxQuantity >= divideNumber) {
            customerDepositStage = totalPriceOfParentOrder.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP);
            customerFirstStage = totalPriceOfParentOrder.subtract(customerDepositStage).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP);
            customerSecondStage = totalPriceOfParentOrder.subtract(customerDepositStage).subtract(customerFirstStage).setScale(0, RoundingMode.HALF_UP);

            // Round Brand Price at Deposit, First, Second Stage to nearest Thousand
            customerDepositStage = Utilities.roundToNearestThousand(customerDepositStage);
            customerFirstStage = Utilities.roundToNearestThousand(customerFirstStage);
            customerSecondStage = Utilities.roundToNearestThousand(customerSecondStage);

            // Update Total Price Of SubOrder with new Nearest Stage of Deposit, First, Second Stage
            totalPriceOfParentOrder = customerDepositStage.add(customerFirstStage).add(customerSecondStage);
        }

        BigDecimal customerPriceDeposit = customerDepositStage.add(commission);
        BigDecimal adjustedTotalPriceOfParentOrder = totalPriceOfParentOrder.add(commission);

        logger.error("CHECK: {}", OrderDetailPriceResponse
                .builder()
                .totalPriceOfParentOrder(adjustedTotalPriceOfParentOrder.toString())
                .customerCommissionFee(commission.toString())
                .customerPriceDeposit(customerPriceDeposit.toString())
                .customerPriceFirstStage(customerFirstStage.toString())
                .customerSecondStage(customerSecondStage.toString())
                .customerShippingFee(shippingFee.toString())
                .brandDetailPriceResponseList(brandDetailPriceResponseList)
                .designMaterialDetailResponseList(designMaterialDetailResponseList)
                .build());

        return OrderDetailPriceResponse
                .builder()
                .totalPriceOfParentOrder(adjustedTotalPriceOfParentOrder.toString())
                .customerCommissionFee(commission.toString())
                .customerPriceDeposit(customerPriceDeposit.toString())
                .customerPriceFirstStage(customerFirstStage.toString())
                .customerSecondStage(customerSecondStage.toString())
                .customerShippingFee(shippingFee.toString())
                .brandDetailPriceResponseList(brandDetailPriceResponseList)
                .designMaterialDetailResponseList(designMaterialDetailResponseList)
                .build();
    }

    private void updateDesignMaterialDetailMap(Map<String, DesignMaterialDetailResponse> designMaterialDetailResponseMap, String detailName, BigDecimal area, BigDecimal price) {
        DesignMaterialDetailResponse response = designMaterialDetailResponseMap.get(detailName);
        if (response != null) {
            if (price != null) {
                response.setMinPriceMaterial(((BigDecimal) response.getMinPriceMaterial()).min(price));
                response.setMaxPriceMaterial(((BigDecimal) response.getMaxPriceMaterial()).max(price));
            }
            if (area != null) {
                response.setMinMeterSquare(((BigDecimal) response.getMinMeterSquare()).min(area));
                response.setMaxMeterSquare(((BigDecimal) response.getMaxMeterSquare()).max(area));
            }
        } else {
            response = DesignMaterialDetailResponse
                    .builder()
                    .detailName(detailName)
                    .minMeterSquare(area)
                    .maxMeterSquare(area)
                    .minPriceMaterial(price)
                    .maxPriceMaterial(price)
                    .build();
        }
        designMaterialDetailResponseMap.put(detailName, response);
    }

    private void updateDesignMaterialDetailWithoutBrandMap(Map<String, DesignMaterialDetailResponse> designMaterialDetailResponseMap, String detailName, BigDecimal area, BigDecimal minPrice, BigDecimal maxPrice) {
        DesignMaterialDetailResponse response = designMaterialDetailResponseMap.get(detailName);
        if (response != null) {
            if (minPrice != null && maxPrice != null) {
                BigDecimal minOfBoth = minPrice.min(maxPrice);
                BigDecimal maxOfBoth = minPrice.max(maxPrice);

                response.setMinPriceMaterial(((BigDecimal) response.getMinPriceMaterial()).min(minOfBoth));
                response.setMaxPriceMaterial(((BigDecimal) response.getMaxPriceMaterial()).max(maxOfBoth));
            }
            if (area != null) {
                response.setMinMeterSquare(((BigDecimal) response.getMinMeterSquare()).min(area));
                response.setMaxMeterSquare(((BigDecimal) response.getMaxMeterSquare()).max(area));
            }
        } else {
            response = DesignMaterialDetailResponse
                    .builder()
                    .detailName(detailName)
                    .minMeterSquare(area)
                    .maxMeterSquare(area)
                    .minPriceMaterial(minPrice)
                    .maxPriceMaterial(maxPrice)
                    .build();
        }
        designMaterialDetailResponseMap.put(detailName, response);
    }

    @Override
    public OrderCustomResponse getOrderByOrderID(String orderID) throws Exception {
        try {

            var order = orderRepository.findById(orderID).isPresent() ? orderRepository.findById(orderID).get() : null;
            if (order == null) {
                throw new BadRequestException(MessageConstant.RESOURCE_NOT_FOUND + " with orderID: " + orderID);
            }
            if (order.getOrderType().equals("PARENT_ORDER")) {

                var calculatedPrice = calculateTotalPriceForSpecificOrder(orderID);
                logger.error("BUG HERE: {}", calculatedPrice);
                List<DesignDetail> designDetailList = detailRepository.findAllByOrderID(orderID);
                List<DesignDetail> detailList = null;
                if (!designDetailList.isEmpty()) {
                    for (DesignDetail detail : designDetailList) {
                        if (detailList == null) {
                            detailList = new ArrayList<>();
                        }
                        detailList.add(detail);
                    }
                }
//                order.setDetailList(detailList);
                var status = order.getOrderStatus();
                var paymentList = paymentService.findAllByOrderID(orderID);

                switch (status) {
                    case DEPOSIT -> {
                        logger.error("INCASE DEPOSIT");
                        if (!paymentList.isEmpty()) {
                            var checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.DEPOSIT) && p.getPaymentStatus()).findFirst();

                            // check if deposited?
                            if (checkDeposited.isPresent()) {
                                // change status of parent order to PROCESSING
                                logger.info("Change Status PROCESSING Order");
                                changeOrderStatus(OrderStatusUpdateRequest.builder().orderID(orderID).status(OrderStatus.PREPARING.name()).build());

                                // change status of sub order to START_PRODUCING
                                var subOrderList = getSubOrderByParentID(orderID);
                                for (OrderResponse subOrder : subOrderList) {
                                    var subOrderObject = getOrderById(subOrder.getOrderID()).get();
                                    subOrderObject.setOrderStatus(OrderStatus.CHECKING_SAMPLE_DATA);
                                    updateOrder(subOrderObject);
                                }
                            }
                        }
                    }
                    case PREPARING -> {
                        var subOrderList = getSubOrderByParentID(orderID);
                        boolean isStart = true;
                        for (var subResponse : subOrderList) {
                            if (!subResponse.getOrderStatus().equals(OrderStatus.START_PRODUCING)) {
                                isStart = false;
                            }
                        }
                        if (isStart) {
                            logger.info("Change Status PROCESSING Order");
                            changeOrderStatus(OrderStatusUpdateRequest.builder().orderID(orderID).status(OrderStatus.PROCESSING.name()).build());
                        }
                    }
                    case PROCESSING -> {
                        logger.error("INCASE PROCESSING");

                        int divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());
                        var subOrderList = getSubOrderByParentID(orderID);
                        Integer maxSubQuantity = 0;
                        for (var subOrder : subOrderList) {
                            maxSubQuantity = max(maxSubQuantity, subOrder.getQuantity());
                        }
                        var sender = designDetailList.get(0).getDesign().getUser();
                        var recipient = userService.getUserByEmail("accountantsmarttailor123@gmail.com");
                        if (maxSubQuantity >= divideNumber) {

                            // CHECK CURRENT STAGE
                            var stage = -1;
                            if (!paymentList.isEmpty()) {
                                var checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_2)
                                        && p.getOrder().getOrderID().equals(orderID)).findFirst();
                                if (checkDeposited.isEmpty()) {
                                    checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_1)
                                            && p.getOrder().getOrderID().equals(orderID)).findFirst();
                                    if (checkDeposited.isPresent()) {
                                        if (checkDeposited.get().getPaymentStatus()) {
                                            stage = 1;
                                        } else {
                                            stage = 0;
                                        }
                                    } else {
                                        stage = 0;
                                    }
                                } else {
                                    if (checkDeposited.get().getPaymentStatus()) stage = 2;
                                }
                            }

                            boolean isFinish;


                            switch (stage) {
                                case 0 -> {
                                    isFinish = true;
                                    for (OrderResponse subOrder : subOrderList) {
                                        if (!subOrder.getOrderStatus().equals(OrderStatus.FINISH_FIRST_STAGE) && !subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                            isFinish = false;
                                            break;
                                        }
                                    }
                                    if (isFinish && isOrderCompletelyPicked(orderID)) {
                                        var checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_1)
                                                && p.getOrder().getOrderID().equals(orderID)
                                        ).findFirst();
                                        if (checkDeposited.isEmpty()) {
                                            logger.error("CREATE STAGE_1");
                                            paymentService.createPayOSPayment(
                                                    PaymentRequest
                                                            .builder()
                                                            .orderID(orderID)
                                                            .paymentSenderID(sender.getUserID())
                                                            .paymentSenderName(order.getBuyerName())
                                                            .paymentSenderBankCode("")
                                                            .paymentSenderBankNumber("")
                                                            .paymentRecipientID(recipient.getUserID())
                                                            .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                            .paymentRecipientBankCode("OCB")
                                                            .paymentRecipientBankNumber("0163100007285002")
                                                            .paymentType(PaymentType.STAGE_1)
                                                            .paymentAmount(Integer.valueOf(calculatedPrice.getCustomerPriceFirstStage()))
                                                            .itemList(null)
                                                            .build()
                                            );
//                                            for (OrderResponse subOrderResponse : subOrderList) {
//                                                var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                                if (!subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
//                                                    subOrder.setOrderStatus(OrderStatus.CHECKING_SAMPLE_DATA);
//                                                    updateOrder(subOrder);
//                                                }
//                                            }
                                        }
                                    }
                                }
                                case 1 -> {
                                    isFinish = true;
                                    for (OrderResponse subOrder : subOrderList) {
                                        if (!subOrder.getOrderStatus().equals(OrderStatus.FINISH_SECOND_STAGE) && !subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                            isFinish = false;
                                            break;
                                        }
                                    }
                                    if (isFinish && isOrderCompletelyPicked(orderID)) {
                                        var checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_2)
                                                && p.getOrder().getOrderID().equals(orderID)).findFirst();
                                        if (checkDeposited.isEmpty()) {
                                            logger.error("CREATE STAGE_2");
                                            paymentService.createPayOSPayment(
                                                    PaymentRequest
                                                            .builder()
                                                            .orderID(orderID)
                                                            .paymentSenderID(sender.getUserID())
                                                            .paymentSenderName(order.getBuyerName())
                                                            .paymentSenderBankCode("")
                                                            .paymentSenderBankNumber("")
                                                            .paymentRecipientID(recipient.getUserID())
                                                            .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                            .paymentRecipientBankCode("OCB")
                                                            .paymentRecipientBankNumber("0163100007285002")
                                                            .paymentType(PaymentType.STAGE_2)
                                                            .paymentAmount(Integer.valueOf(calculatedPrice.getCustomerSecondStage()))
                                                            .itemList(null)
                                                            .build()
                                            );
//                                            for (OrderResponse subOrderResponse : subOrderList) {
//                                                var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                                if (!subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
//                                                    subOrder.setOrderStatus(OrderStatus.CHECKING_SAMPLE_DATA);
//                                                    updateOrder(subOrder);
//                                                }
//                                            }
                                        }
                                    }
                                }
                                case 2 -> {
                                    isFinish = true;
                                    for (OrderResponse subOrder : subOrderList) {
                                        if (!subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                            isFinish = false;
                                            break;
                                        }
                                    }
                                    if (isFinish && isOrderCompletelyPicked(orderID)) {
                                        var maxDateTime = LocalDateTime.parse(subOrderList.get(0).getProductionCompletionDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                                        for (OrderResponse subOrder : subOrderList) {
                                            var completionDate = LocalDateTime.parse(subOrder.getProductionCompletionDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                                            maxDateTime = maxDateTime.isAfter(completionDate) ? maxDateTime : completionDate;
                                        }
                                        order.setProductionCompletionDate(maxDateTime);
                                        updateOrder(order);
                                        changeOrderStatus(OrderStatusUpdateRequest.builder().orderID(orderID).status(OrderStatus.COMPLETED.name()).build());
                                    }
                                }
                            }
                        } else {
                            boolean isFinish;
                            isFinish = true;
                            for (OrderResponse subOrder : subOrderList) {
                                if (!subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                    isFinish = false;
                                    break;
                                }
                            }
                            if (isFinish && isOrderCompletelyPicked(orderID)) {
//                                var checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.COMPLETED_ORDER)
//                                        && p.getOrder().getOrderID().equals(orderID)).findFirst();
//                                if (checkDeposited.isEmpty()) {
//                                    logger.error("CREATE COMPLETE_ORDER");
//                                    paymentService.createPayOSPayment(
//                                            PaymentRequest
//                                                    .builder()
//                                                    .orderID(orderID)
//                                                    .paymentSenderID(sender.getUserID())
//                                                    .paymentSenderName(order.getBuyerName())
//                                                    .paymentSenderBankCode("")
//                                                    .paymentSenderBankNumber("")
//                                                    .paymentRecipientID(recipient.getUserID())
//                                                    .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
//                                                    .paymentRecipientBankCode("OCB")
//                                                    .paymentRecipientBankNumber("0163100007285002")
//                                                    .paymentType(PaymentType.COMPLETED_ORDER)
//                                                    .paymentAmount(
//                                                            Integer.parseInt(calculatedPrice.getTotalPriceOfParentOrder()) - Integer.parseInt(calculatedPrice.getCustomerPriceDeposit())
//                                                    )
//                                                    .itemList(null)
//                                                    .build()
//                                    );
//                                }
//                                logger.info("Change Status PROCESSING Order");
                                changeOrderStatus(OrderStatusUpdateRequest.builder().orderID(orderID).status(OrderStatus.COMPLETED.name()).build());
                            }
                        }
                    }
                    case DELIVERED -> {
                        var subOrderList = getSubOrderByParentID(orderID);
                        for (var subOrderResponse : subOrderList) {
                            var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
                            var subPrice = calculatedPrice.getBrandDetailPriceResponseList()
                                    .stream()
                                    .filter(brandPrice -> brandPrice.getSubOrderID().equals(subOrderResponse.getOrderID()))
                                    .findFirst()
                                    .orElseThrow(() -> {
                                        return new ItemNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
                                    });
//                            paymentService.createManualPayment(
//                                    PaymentRequest
//                                            .builder()
//                                            .paymentSenderID(userService.getUserByEmail("accountantsmarttailor123@gmail.com").getUserID())
//                                            .paymentSenderName("NGUYEN VAN A")
//                                            .paymentSenderBankCode("NCB")
//                                            .paymentSenderBankNumber("9704198526191432198")
//                                            .paymentRecipientID(subOrder.getDetailList().get(0).getBrand().getBrandID())
//                                            .paymentRecipientName(subOrder.getDetailList().get(0).getBrand().getBrandName())
//                                            .paymentRecipientBankCode(subOrder.getDetailList().get(0).getBrand().getBankName())
//                                            .paymentRecipientBankNumber(subOrder.getDetailList().get(0).getBrand().getAccountNumber())
//                                            .orderID(subOrder.getOrderID())
//                                            .paymentAmount(subOrder.getTotalPrice())
//                                            .paymentMethod(PaymentMethod.CREDIT_CARD)
//                                            .paymentType(PaymentType.BRAND_INVOICE)
//                                            .build()
//                            );
//                            if(order.getOrderStatus().name().equals(OrderStatus.DELIVERED.name()) &&
//                                    Optional.ofNullable(order.getLabelID()).isEmpty() && order.getOrderType().equals("PARENT_ORDER")) {
//                                var design = designService.getDesignByOrderID(subOrder.getOrderID());
//                                logger.info("Design Information: {}", design);
//
//                                var minWeightParentOrder = design.getMinWeight() * order.getQuantity();
//                                logger.info("Minimum Weight for Parent Order (Quantity {}): {}", order.getQuantity(), minWeightParentOrder);
//
//                                var maxWeightParentOrder = design.getMaxWeight() * order.getQuantity();
//                                logger.info("Maximum Weight for Parent Order (Quantity {}): {}", order.getQuantity(), maxWeightParentOrder);
//
//                                var averageWeightParentOrder = (float) (minWeightParentOrder + maxWeightParentOrder) / 2;
//                                logger.info("Average Weight for Parent Order: {}", averageWeightParentOrder);
//
//                                var maximumShippingWeight = Integer.parseInt(systemPropertiesService.getByName("MAX_SHIPPING_WEIGHT").getPropertyValue());
//                                if(averageWeightParentOrder < maximumShippingWeight){
//                                    OrderShippingRequest.OrderShippingDetailRequest orderShippingDetailRequest =
//                                            new OrderShippingRequest.OrderShippingDetailRequest(
//                                                    order.getOrderID() + " " + LocalDateTime.now(),
//                                                    "Smart Tailor Services",
//                                                    "344 Lê Văn Việt",
//                                                    "Hồ Chí Minh",
//                                                    "Thủ Đức",
//                                                    "Tăng Nhơn Phú B",
//                                                    "0926733445",
//                                                    order.getPhone(),
//                                                    order.getBuyerName(),
//                                                    order.getAddress(),
//                                                    order.getProvince(),
//                                                    order.getDistrict(),
//                                                    order.getWard(),
//                                                    "Khác",
//                                                    "1",
//                                                    "2024/10/08",
//                                                    0,
//                                                    averageWeightParentOrder,
//                                                    1
//                                            );
//
//                                    var orderShippingRequest =
//                                            OrderShippingRequest
//                                                    .builder()
//                                                    .order(orderShippingDetailRequest)
//                                                    .build();
//
//                                    var createShippingOrder = ghtkShippingService.createShippingOrder(orderShippingRequest);
//                                    if(createShippingOrder != null){
//                                        logger.info("Create Shipping Order Successfully {}", createShippingOrder);
//                                    }
//                                }
//                            }
                            if (subOrderResponse.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                paymentService.createPayOSPayment(PaymentRequest.builder().orderID(subOrder.getOrderID())

                                        .paymentSenderID(
                                                userService.getUserByEmail("accountantsmarttailor123@gmail.com")
                                                        .getUserID()
                                        )
                                        .paymentSenderName("")
                                        .paymentSenderBankCode("")
                                        .paymentSenderBankNumber("")

                                        .paymentRecipientID(
                                                subOrder.getDetailList().get(0)
                                                        .getBrand().getBrandID()
                                        )
                                        .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                        .paymentRecipientBankCode("OCB")
                                        .paymentRecipientBankNumber("0163100007285002")
                                        .paymentType(PaymentType.BRAND_INVOICE)
                                        .paymentAmount(
                                                Integer.parseInt(subPrice.getBrandPriceDeposit())
                                                        +
                                                        Integer.parseInt(subPrice.getBrandPriceFirstStage())
                                                        +
                                                        Integer.parseInt(subPrice.getBrandPriceSecondStage())
                                        )
                                        .itemList(null)
                                        .build()
                                );
                            }
                        }
                    }
                    case CANCEL -> {
                        // CHECK CURRENT STAGE
                        var stage = -1;
                        if (!paymentList.isEmpty()) {
                            var checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_2)).findFirst();
                            if (checkDeposited.isEmpty()) {
                                checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_1)).findFirst();
                                if (checkDeposited.isPresent()) {
                                    if (checkDeposited.get().getPaymentStatus()) {
                                        stage = 1;
                                    } else {
                                        stage = 0;
                                    }
                                } else {
                                    stage = 0;
                                }
                            } else {
                                if (checkDeposited.get().getPaymentStatus()) stage = 2;
                            }
                        }
                        var subOrderList = getSubOrderByParentID(orderID);
                        switch (stage) {
                            case 0 -> {
                                for (OrderResponse subOrderResponse : subOrderList) {
                                    var subPrice = calculatedPrice.getBrandDetailPriceResponseList()
                                            .stream()
                                            .filter(brandPrice -> brandPrice.getSubOrderID().equals(subOrderResponse.getOrderID()))
                                            .findFirst()
                                            .orElseThrow(() -> {
                                                return new ItemNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
                                            });
                                    if (
                                            subOrderResponse.getOrderStatus().equals(OrderStatus.FINISH_FIRST_STAGE)
                                                    ||
                                                    subOrderResponse.getOrderStatus().equals(OrderStatus.COMPLETED)
                                    ) {
                                        var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                        paymentService.createManualPayment(
//                                                PaymentRequest
//                                                        .builder()
//                                                        .paymentSenderID(userService.getUserByEmail("accountantsmarttailor123@gmail.com").getUserID())
//                                                        .paymentSenderName("NGUYEN VAN A")
//                                                        .paymentSenderBankCode("NCB")
//                                                        .paymentSenderBankNumber("9704198526191432198")
//                                                        .paymentRecipientID(subOrder.getDetailList().get(0).getBrand().getBrandID())
//                                                        .paymentRecipientName(subOrder.getDetailList().get(0).getBrand().getBrandName())
//                                                        .paymentRecipientBankCode(subOrder.getDetailList().get(0).getBrand().getBankName())
//                                                        .paymentRecipientBankNumber(subOrder.getDetailList().get(0).getBrand().getAccountNumber())
//                                                        .orderID(subOrder.getOrderID())
//                                                        .paymentAmount(subOrder.getTotalPrice())
//                                                        .paymentMethod(PaymentMethod.CREDIT_CARD)
//                                                        .paymentType(PaymentType.BRAND_INVOICE)
//                                                        .build()
//                                        );
                                        if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                            var payOSResponse = paymentService.createPayOSPayment(PaymentRequest.builder().orderID(subOrder.getOrderID())

                                                    .paymentSenderID(
                                                            userService.getUserByEmail("accountantsmarttailor123@gmail.com")
                                                                    .getUserID()
                                                    )
                                                    .paymentSenderName("")
                                                    .paymentSenderBankCode("")
                                                    .paymentSenderBankNumber("")
                                                    .paymentRecipientID(
                                                            subOrder.getDetailList().get(0)
                                                                    .getBrand().getBrandID()
                                                    )
                                                    .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                    .paymentRecipientBankCode("OCB")
                                                    .paymentRecipientBankNumber("0163100007285002")
                                                    .paymentType(PaymentType.BRAND_INVOICE)
                                                    .paymentAmount(Integer.valueOf(subPrice.getBrandPriceDeposit()))
                                                    .itemList(null)
                                                    .build()
                                            );
                                        }
                                        changeOrderStatus(OrderStatusUpdateRequest.builder().orderID(String.valueOf(subOrder.getOrderID())).status(OrderStatus.CANCEL.name()).build());
                                    }
                                }
                            }
                            case 1 -> {
                                for (OrderResponse subOrderResponse : subOrderList) {
                                    var subPrice = calculatedPrice.getBrandDetailPriceResponseList()
                                            .stream()
                                            .filter(brandPrice -> brandPrice.getSubOrderID().equals(subOrderResponse.getOrderID()))
                                            .findFirst()
                                            .orElseThrow(() -> {
                                                return new ItemNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
                                            });
                                    if (subOrderResponse.getOrderStatus().equals(OrderStatus.FINISH_SECOND_STAGE) ||
                                            subOrderResponse.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                        var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                        paymentService.createManualPayment(
//                                                PaymentRequest
//                                                        .builder()
//                                                        .paymentSenderID(userService.getUserByEmail("accountantsmarttailor123@gmail.com").getUserID())
//                                                        .paymentSenderName("NGUYEN VAN A")
//                                                        .paymentSenderBankCode("NCB")
//                                                        .paymentSenderBankNumber("9704198526191432198")
//                                                        .paymentRecipientID(subOrder.getDetailList().get(0).getBrand().getBrandID())
//                                                        .paymentRecipientName(subOrder.getDetailList().get(0).getBrand().getBrandName())
//                                                        .paymentRecipientBankCode(subOrder.getDetailList().get(0).getBrand().getBankName())
//                                                        .paymentRecipientBankNumber(subOrder.getDetailList().get(0).getBrand().getAccountNumber())
//                                                        .orderID(subOrder.getOrderID())
//                                                        .paymentAmount(subOrder.getTotalPrice())
//                                                        .paymentMethod(PaymentMethod.CREDIT_CARD)
//                                                        .paymentType(PaymentType.BRAND_INVOICE)
//                                                        .build()
//                                        );
                                        if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                            var payOSResponse = paymentService.createPayOSPayment(PaymentRequest.builder().orderID(subOrder.getOrderID())

                                                    .paymentSenderID(
                                                            userService.getUserByEmail("accountantsmarttailor123@gmail.com")
                                                                    .getUserID()
                                                    )
                                                    .paymentSenderName("")
                                                    .paymentSenderBankCode("")
                                                    .paymentSenderBankNumber("")
                                                    .paymentRecipientID(
                                                            subOrder.getDetailList().get(0)
                                                                    .getBrand().getBrandID()
                                                    )
                                                    .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                    .paymentRecipientBankCode("OCB")
                                                    .paymentRecipientBankNumber("0163100007285002")
                                                    .paymentType(PaymentType.BRAND_INVOICE)
                                                    .paymentAmount(Integer.valueOf(subPrice.getBrandPriceFirstStage()))
                                                    .itemList(null)
                                                    .build()
                                            );
                                        }
                                        changeOrderStatus(OrderStatusUpdateRequest.builder().orderID(String.valueOf(subOrder.getOrderID())).status(OrderStatus.CANCEL.name()).build());
                                    }
                                }
                            }
                            case 2 -> {
                                for (OrderResponse subOrderResponse : subOrderList) {
                                    var subPrice = calculatedPrice.getBrandDetailPriceResponseList()
                                            .stream()
                                            .filter(brandPrice -> brandPrice.getSubOrderID().equals(subOrderResponse.getOrderID()))
                                            .findFirst()
                                            .orElseThrow(() -> {
                                                return new ItemNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
                                            });
                                    if (subOrderResponse.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                        var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                        paymentService.createManualPayment(
//                                                PaymentRequest
//                                                        .builder()
//                                                        .paymentSenderID(userService.getUserByEmail("accountantsmarttailor123@gmail.com").getUserID())
//                                                        .paymentSenderName("NGUYEN VAN A")
//                                                        .paymentSenderBankCode("NCB")
//                                                        .paymentSenderBankNumber("9704198526191432198")
//                                                        .paymentRecipientID(subOrder.getDetailList().get(0).getBrand().getBrandID())
//                                                        .paymentRecipientName(subOrder.getDetailList().get(0).getBrand().getBrandName())
//                                                        .paymentRecipientBankCode(subOrder.getDetailList().get(0).getBrand().getBankName())
//                                                        .paymentRecipientBankNumber(subOrder.getDetailList().get(0).getBrand().getAccountNumber())
//                                                        .orderID(subOrder.getOrderID())
//                                                        .paymentAmount(subOrder.getTotalPrice())
//                                                        .paymentMethod(PaymentMethod.CREDIT_CARD)
//                                                        .paymentType(PaymentType.BRAND_INVOICE)
//                                                        .build()
//                                        );
                                        if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                            var payOSResponse = paymentService.createPayOSPayment(PaymentRequest.builder().orderID(subOrder.getOrderID())

                                                    .paymentSenderID(
                                                            userService.getUserByEmail("accountantsmarttailor123@gmail.com")
                                                                    .getUserID()
                                                    )
                                                    .paymentSenderName("")
                                                    .paymentSenderBankCode("")
                                                    .paymentSenderBankNumber("")
                                                    .paymentRecipientID(
                                                            subOrder.getDetailList().get(0)
                                                                    .getBrand().getBrandID()
                                                    ).paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                    .paymentRecipientBankCode("OCB")
                                                    .paymentRecipientBankNumber("0163100007285002")
                                                    .paymentType(PaymentType.BRAND_INVOICE)
                                                    .paymentAmount(Integer.valueOf(subPrice.getBrandPriceSecondStage()))
                                                    .itemList(null)
                                                    .build()
                                            );
                                        }
                                        changeOrderStatus(OrderStatusUpdateRequest.builder().orderID(String.valueOf(subOrder.getOrderID())).status(OrderStatus.CANCEL.name()).build());
                                    }
                                }
                            }
                        }
                    }
                }
                return convertToOrderCustomResponse(order, detailList, calculatedPrice.getDesignMaterialDetailResponseList().isEmpty() ? new ArrayList<>() : calculatedPrice.getDesignMaterialDetailResponseList());
            } else {
                List<DesignDetail> designDetailList = detailRepository.findAllBySubOrderID(orderID);
                List<DesignDetail> detailList = null;
                if (!designDetailList.isEmpty()) {
                    for (DesignDetail detail : designDetailList) {
                        if (detailList == null) {
                            detailList = new ArrayList<>();
                        }
                        detailList.add(detail);
                    }
                }
//                order.setDetailList(detailList);
                return convertToOrderCustomResponse(order, detailList, new ArrayList<>());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public OrderCustomResponse getOrderDetailByOrderID(String jwtToken, String orderID) throws Exception {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        var parentOrderList = orderRepository.getParentOrderByUserID(userIDFromJwtToken);

        var listPayment = paymentService.findAllByOrderID(orderID);
        for (var payment : listPayment) {
            PayOSResponse payOS = null;
            if (payment.getPaymentType().equals(PaymentType.BRAND_INVOICE)) {
                payOS = payOSService.getBrandPaymentInfo(payment.getPaymentCode());
            } else {
                if (payment.getPaymentType().equals(PaymentType.ORDER_REFUND)) {
                    payOS = payOSService.getRefundPaymentInfo(payment.getPaymentCode());
                } else
                    payOS = payOSService.getPaymentInfo(payment.getPaymentCode());
            }
            if (payOS != null) {
                payment.setPaymentStatus(payOS.getData().getStatus().equals("PAID"));
                paymentService.updatePayment(payment);
            }
        }

        boolean isAuthorized = parentOrderList
                .stream()
                .anyMatch(order -> order.getOrderID().equals(orderID));

        if (!isAuthorized) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to the order");
        }

        var response = getOrderByOrderID(orderID);


        var paymentNewest = response.getPaymentList().stream().max(Comparator.comparing(PaymentResponse::getCreateDate));

        List<PaymentResponse> paymentList = new ArrayList<>();
        if (paymentNewest.isPresent() && !paymentNewest.get().getPaymentStatus())
            paymentList.add(paymentNewest.get());

        response.setPaymentList(paymentList);
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> getOrderById(String orderID) {
        return orderRepository.findById(orderID);
    }

    @Override
    public List<OrderCustomResponse> getOrderByBrandID(String jwtToken, String brandID) throws Exception {
        var userID = jwtService.extractUserIDFromJwtToken(jwtToken);
        if (!userID.equals(brandID)) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }
        var listOrder = orderRepository.getOrderByBrandID(brandID);
        List<OrderCustomResponse> responseList = new ArrayList<>();
        for (Order o : listOrder) {
            responseList.add(getOrderByOrderID(o.getOrderID()));
        }
        return responseList.stream().sorted(Comparator.comparing(OrderCustomResponse::getCreateDate).reversed()).toList();
    }


    @Override
    public List<OrderCustomResponse> getOrderByUserID(String jwtToken, String userID) throws Exception {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if (!userID.equals(userIDFromJwtToken)) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }
        var orderList = orderRepository.getParentOrderByUserID(userID);
        List<OrderCustomResponse> responseList = new ArrayList<>();
        for (Order o : orderList) {
//            responseList.add(getOrderByOrderID(o.getOrderID()));
            responseList.add(orderMapper.mapToOrderCustomResponse(o));
        }
        return responseList.stream().sorted(Comparator.comparing(OrderCustomResponse::getCreateDate).reversed()).toList();
    }

    @Override
    public List<OrderResponse> getSubOrderByParentID(String parentOrderID) {
        return orderRepository
                .findAll()
                .stream()
                .filter(order -> order.getParentOrder() != null && order.getParentOrder().getOrderID().equals(parentOrderID) && !order.getOrderStatus().equals(OrderStatus.CANCEL))
                .map(this::safeMapToOrderResponse)
                .sorted(Comparator.comparing(OrderResponse::getCreateDate).reversed())
                .toList();
    }

    public List<OrderResponse> getSubOrderByParentIDNOTCHECKSTATUS(String parentOrderID) {
        return orderRepository
                .findAll()
                .stream()
                .filter(order -> order.getParentOrder() != null && order.getParentOrder().getOrderID().equals(parentOrderID))
                .map(this::safeMapToOrderResponse)
                .sorted(Comparator.comparing(OrderResponse::getCreateDate).reversed())
                .toList();
    }

    private OrderResponse safeMapToOrderResponse(Order order) {
        try {
            return orderMapper.mapToOrderResponse(order);
        } catch (Exception e) {
            // Log the exception
            logger.error("Error mapping order to response: {}", order, e);
            // Return a default or error response
            return null;
        }
    }

    @Override
    @Caching
    public List<OrderResponse> getAllOrder() {
        return orderRepository.findAll().stream()
                .map(this::safeMapToOrderResponse)
                .filter(Objects::nonNull) // Loại bỏ các giá trị null nếu cần
                .sorted(Comparator.comparing(OrderResponse::getCreateDate).reversed())
                .toList();
    }

    @Override
    public OrderResponse changeOrderStatus(OrderStatusUpdateRequest orderRequest) throws Exception {
        String orderID = orderRequest.getOrderID();

        // Retrieve the order by its ID
        var order = getOrderById(orderID);
        if (order.isEmpty()) {
            throw new ItemNotFoundException("Cannot find order with OrderID: " + orderID);
        }

        // Get the existing order object
        var existedOrder = order.get();

        // Retrieve the new status from the request
        var orderStatus = OrderStatus.valueOf(orderRequest.getStatus());

        switch (orderStatus) {
            case CANCEL -> {
                // Get the current status of the order
                var currentStatus = existedOrder.getOrderStatus();

                /**
                 * If the order is a parent order (PARENT_ORDER)
                 */
                if (existedOrder.getOrderType().equals("PARENT_ORDER")) {

                    // Step 1: Retrieve the list of all sub-orders associated with the parent order
                    var subOrderList = getSubOrderByParentID(existedOrder.getOrderID());

                    // Step 2: Update the status of each sub-order
                    for (var subOrderResponse : subOrderList) {

                        // Fetch the sub-order based on its ID
                        var subOrder = getOrderById(subOrderResponse.getOrderID()).orElseThrow(() -> new ItemNotFoundException("Cannot find sub-order with ID: " + subOrderResponse.getOrderID()));

                        // Change the status of the sub-order to CANCEL
                        subOrder.setOrderStatus(OrderStatus.CANCEL);

                        // Save the updated sub-order status to the repository
                        orderRepository.save(subOrder);

                        var sender = subOrderResponse.getEmployeeID();
                        var brand = subOrderResponse.getBrand().getBrandID();

                        notificationService.sendPrivateNotification(
                                NotificationRequest
                                        .builder()
                                        .senderID(sender)
                                        .recipientID(brand)
                                        .action("CHANGE ORDER STATUS")
                                        .type("REQUEST VIEW")
                                        .targetID(subOrderResponse.getOrderID())
                                        .message("Order status has been changed! Status: " + orderStatus.name())
                                        .build()
                        );
                    }

                    // Handle different current statuses of the parent order
                    switch (currentStatus) {
                        case NOT_VERIFY, PENDING, REJECTED, DELIVERED, COMPLETED -> {
                            /**
                             * TODO
                             * Create a report for the employee
                             */
                            logger.warn("Create a report for the employee line 850: {}", currentStatus);
                        }
                        case SUSPENDED -> {
                            var depositPayment = paymentService.findAllByOrderID(orderID).stream().filter(p -> p.getPaymentType().equals(PaymentType.DEPOSIT)).findFirst().orElse(null);
                            if (depositPayment != null) {

                                var depositPaymentStatus = depositPayment.getPaymentStatus();
                                if (depositPaymentStatus) {

                                    int price = (int) (depositPayment.getPaymentAmount());
                                    var orderResponse = orderMapper.mapToOrderCustomResponse(existedOrder);
                                    var sender = userService.getUserByEmail("accountantsmarttailor123@gmail.com");
                                    var recipient = orderResponse.getDesignResponse().getUser();
                                    paymentService.createPayOSPayment(
                                            PaymentRequest.builder()
                                                    .paymentAmount(price)
                                                    .paymentMethod(PaymentMethod.BANK_TRANSFER)
                                                    .paymentType(PaymentType.ORDER_REFUND)

                                                    .paymentSenderID(sender.getUserID())
                                                    .paymentSenderName(sender.getFullName())
                                                    .paymentSenderBankCode("OCB")
                                                    .paymentSenderBankNumber("0163100007285002")
                                                    .paymentRecipientID(
                                                            recipient.getUserID()
                                                    )
                                                    .paymentRecipientName("")
                                                    .paymentRecipientBankNumber("")
                                                    .paymentRecipientBankCode("")
                                                    .orderID(orderID)
                                                    .build()
                                    );
                                    logger.error("CREATE CUS REFUND SUCCESSFULLY");

                                    depositPayment = paymentService.findAllByOrderID(orderID).stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_1)).findFirst().orElse(null);
                                    if (depositPayment != null) {

                                        depositPaymentStatus = depositPayment.getPaymentStatus();
                                        if (depositPaymentStatus) {
                                            price = (int) (depositPayment.getPaymentAmount());
                                            orderResponse = orderMapper.mapToOrderCustomResponse(existedOrder);
                                            sender = userService.getUserByEmail("accountantsmarttailor123@gmail.com");
                                            recipient = orderResponse.getDesignResponse().getUser();
                                            paymentService.createPayOSPayment(
                                                    PaymentRequest.builder()
                                                            .paymentAmount(price)
                                                            .paymentMethod(PaymentMethod.BANK_TRANSFER)
                                                            .paymentType(PaymentType.ORDER_REFUND)

                                                            .paymentSenderID(sender.getUserID())
                                                            .paymentSenderName(sender.getFullName())
                                                            .paymentSenderBankCode("OCB")
                                                            .paymentSenderBankNumber("0163100007285002")
                                                            .paymentRecipientID(
                                                                    recipient.getUserID()
                                                            )
                                                            .paymentRecipientName("")
                                                            .paymentRecipientBankNumber("")
                                                            .paymentRecipientBankCode("")
                                                            .orderID(orderID)
                                                            .build()
                                            );
                                            logger.error("CREATE CUS REFUND SUCCESSFULLY");
                                            depositPayment = paymentService.findAllByOrderID(orderID).stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_2)).findFirst().orElse(null);
                                            if (depositPayment != null) {

                                                depositPaymentStatus = depositPayment.getPaymentStatus();
                                                if (depositPaymentStatus) {
                                                    price = (int) (depositPayment.getPaymentAmount());
                                                    orderResponse = orderMapper.mapToOrderCustomResponse(existedOrder);
                                                    sender = userService.getUserByEmail("accountantsmarttailor123@gmail.com");
                                                    recipient = orderResponse.getDesignResponse().getUser();
                                                    paymentService.createPayOSPayment(
                                                            PaymentRequest.builder()
                                                                    .paymentAmount(price)
                                                                    .paymentMethod(PaymentMethod.BANK_TRANSFER)
                                                                    .paymentType(PaymentType.ORDER_REFUND)

                                                                    .paymentSenderID(sender.getUserID())
                                                                    .paymentSenderName(sender.getFullName())
                                                                    .paymentSenderBankCode("OCB")
                                                                    .paymentSenderBankNumber("0163100007285002")
                                                                    .paymentRecipientID(
                                                                            recipient.getUserID()
                                                                    )
                                                                    .paymentRecipientName("")
                                                                    .paymentRecipientBankNumber("")
                                                                    .paymentRecipientBankCode("")
                                                                    .orderID(orderID)
                                                                    .build()
                                                    );
                                                    logger.error("CREATE CUS REFUND SUCCESSFULLY");
                                                }
                                            }
                                        }
                                    }
                                }
                            }

//                            for (var subOrderResponse : subOrderList) {
//                                var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
//                                    logger.error("CREATE BRAND TRANS");
//
//                                    paymentService.createPayOSPayment(
//                                            PaymentRequest
//                                                    .builder()
//                                                    .orderID(subOrder.getOrderID())
//
//                                                    .paymentSenderID(
//                                                            userService.getUserByEmail("accountantsmarttailor123@gmail.com")
//                                                                    .getUserID()
//                                                    )
//                                                    .paymentSenderName("")
//                                                    .paymentSenderBankCode("")
//                                                    .paymentSenderBankNumber("")
//
//                                                    .paymentRecipientID(
//                                                            subOrder.getDetailList().get(0)
//                                                                    .getBrand().getBrandID()
//                                                    )
//                                                    .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
//                                                    .paymentRecipientBankCode("OCB")
//                                                    .paymentRecipientBankNumber("0163100007285002")
//
//                                                    .paymentType(PaymentType.BRAND_INVOICE)
//                                                    .paymentAmount(
//                                                            subOrder.getTotalPrice()
//                                                    )
//                                                    .itemList(null)
//                                                    .build()
//                                    );
//                                }
//                            }

                        }
                        case DEPOSIT, PREPARING -> {
                            /**
                             * Check if payment has been made
                             */
                            // Get deposit payments associated with the order
                            var depositPayment = paymentService.findAllByOrderID(orderID).stream().filter(p -> p.getPaymentType().equals(PaymentType.DEPOSIT)).findFirst().orElse(null);

                            if (depositPayment == null) {
                                /**
                                 * TODO
                                 * Create a report for the employee
                                 */
                                logger.warn("Create a report: NONE DEPOSIT {}", currentStatus);
                            } else {
                                // Get the status of the deposit payment
                                var depositPaymentStatus = depositPayment.getPaymentStatus();

                                // If payment has been made
                                if (depositPaymentStatus) {
                                    /**
                                     * TODO
                                     * Create a report for the employee
                                     */
                                    logger.warn("Create a report: DEPOSIT: {}", "PAID");

                                    /**
                                     * TODO
                                     * Update numberOfViolations
                                     */
                                    logger.warn("Update numberOfViolations: DEPOSIT: {}", "PAID");

                                    /**
                                     * TODO
                                     * Refund 80% of the deposited payment
                                     */
                                    Integer property = Integer.valueOf(systemPropertiesService.getByName("FEE_CANCEL_IN_DURATION").getPropertyValue());
                                    int price = (int) (depositPayment.getPaymentAmount() * (1 - property));
                                    var orderResponse = orderMapper.mapToOrderCustomResponse(existedOrder);
                                    var sender = userService.getUserByEmail("accountantsmarttailor123@gmail.com");
                                    var recipient = orderResponse.getDesignResponse().getUser();
                                    paymentService.createPayOSPayment(
                                            PaymentRequest.builder()
                                                    .paymentAmount(price)
                                                    .paymentMethod(PaymentMethod.BANK_TRANSFER)
                                                    .paymentType(PaymentType.ORDER_REFUND)

                                                    .paymentSenderID(sender.getUserID())
                                                    .paymentSenderName(sender.getFullName())
                                                    .paymentSenderBankCode("OCB")
                                                    .paymentSenderBankNumber("0163100007285002")
                                                    .paymentRecipientID(
                                                            recipient.getUserID()
                                                    )
                                                    .paymentRecipientName("")
                                                    .paymentRecipientBankNumber("")
                                                    .paymentRecipientBankCode("")
                                                    .orderID(orderID)
                                                    .build()
                                    );
                                    logger.error("CREATE CUS REFUND SUCCESSFULLY");
                                    logger.error("sender: {}", sender.getUserID());
                                    logger.error("recipient: {}", recipient.getUserID());
                                } else {
                                    /**
                                     * TODO
                                     * Create a report for the employee
                                     */
                                    logger.warn("Create a report: DEPOSIT: {}", "PENDING");
                                }
                            }
                        }
                        case PROCESSING -> {
                            var stage = -1;
                            var paymentList = paymentService.findAllByOrderID(orderID);
                            if (!paymentList.isEmpty()) {
                                var checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_2)).findFirst();
                                if (checkDeposited.isEmpty()) {
                                    checkDeposited = paymentList.stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_1)).findFirst();
                                    if (checkDeposited.isPresent()) {
                                        if (checkDeposited.get().getPaymentStatus()) {
                                            stage = 1;
                                        } else {
                                            stage = 0;
                                        }
                                    } else {
                                        stage = 0;
                                    }
                                } else {
                                    if (checkDeposited.get().getPaymentStatus()) {
                                        stage = 2;
                                    } else {
                                        stage = 1;
                                    }
                                }
                            }

                            for (var subOrderResponse : subOrderList) {

                                // Fetch the sub-order based on its ID
                                var subOrder = getOrderById(subOrderResponse.getOrderID()).orElseThrow(() -> new ItemNotFoundException("Cannot find sub-order with ID: " + subOrderResponse.getOrderID()));

                                // Change the status of the sub-order to CANCEL
                                subOrder.setOrderStatus(OrderStatus.CANCEL);

                                // Save the updated sub-order status to the repository
                                orderRepository.save(subOrder);
                            }

                            boolean isFinish;
                            switch (stage) {
                                case 0 -> {

                                    var isStart = false;
                                    for (var subOrderResponse : subOrderList) {
                                        var started = stageService.getOrderStageByOrderID(
                                                        subOrderResponse.getOrderID()
                                                )
                                                .stream()
                                                .anyMatch(
                                                        s -> s.getStage().equals(OrderStatus.START_PRODUCING)
                                                                && s.getStatus()
                                                );
                                        if (started) {
                                            isStart = true;
                                            break;
                                        }
                                    }

                                    if (isStart) {
                                        var calculatedPrice = calculateTotalPriceForSpecificOrder(orderID);
                                        for (var subOrderResponse : subOrderList) {
                                            var subPrice = calculatedPrice.getBrandDetailPriceResponseList()
                                                    .stream()
                                                    .filter(brandPrice -> brandPrice.getSubOrderID().equals(subOrderResponse.getOrderID()))
                                                    .findFirst()
                                                    .orElseThrow(() -> {
                                                        return new ItemNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
                                                    });
                                            var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
                                            if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                                logger.error("CREATE BRAND TRANS");

                                                paymentService.createPayOSPayment(
                                                        PaymentRequest
                                                                .builder()
                                                                .orderID(subOrder.getOrderID())

                                                                .paymentSenderID(
                                                                        userService.getUserByEmail("accountantsmarttailor123@gmail.com")
                                                                                .getUserID()
                                                                )
                                                                .paymentSenderName("")
                                                                .paymentSenderBankCode("")
                                                                .paymentSenderBankNumber("")

                                                                .paymentRecipientID(
                                                                        subOrder.getDetailList().get(0)
                                                                                .getBrand().getBrandID()
                                                                )
                                                                .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                                .paymentRecipientBankCode("OCB")
                                                                .paymentRecipientBankNumber("0163100007285002")

                                                                .paymentType(PaymentType.BRAND_INVOICE)
                                                                .paymentAmount(
                                                                        Integer.valueOf(subPrice.getBrandPriceDeposit())
                                                                )
                                                                .itemList(null)
                                                                .build()
                                                );
                                            }
                                        }
                                    } else {
                                        logger.error("CREATE CUS REFUND");
                                        var depositPayment = paymentService.findAllByOrderID(orderID).stream().filter(p -> p.getPaymentType().equals(PaymentType.DEPOSIT)).findFirst().orElse(null);
                                        Integer property = Integer.valueOf(systemPropertiesService.getByName("FEE_CANCEL_IN_DURATION").getPropertyValue());
                                        int price = (int) (depositPayment.getPaymentAmount() * (1 - property));
                                        var orderResponse = orderMapper.mapToOrderCustomResponse(existedOrder);
                                        var sender = userService.getUserByEmail("accountantsmarttailor123@gmail.com");
                                        var recipient = orderResponse.getDesignResponse().getUser();
                                        paymentService.createPayOSPayment(
                                                PaymentRequest.builder()
                                                        .paymentAmount(price)
                                                        .paymentMethod(PaymentMethod.BANK_TRANSFER)
                                                        .paymentType(PaymentType.ORDER_REFUND)

                                                        .paymentSenderID(sender.getUserID())
                                                        .paymentSenderName(sender.getFullName())
                                                        .paymentSenderBankCode("OCB")
                                                        .paymentSenderBankNumber("0163100007285002")
                                                        .paymentRecipientID(
                                                                recipient.getUserID()
                                                        )
                                                        .paymentRecipientName("")
                                                        .paymentRecipientBankNumber("")
                                                        .paymentRecipientBankCode("")
                                                        .orderID(orderID)
                                                        .build()
                                        );
                                        logger.error("CREATE CUS REFUND SUCCESSFULLY");
                                        logger.error("sender: {}", sender.getUserID());
                                        logger.error("recipient: {}", recipient.getUserID());
                                    }
                                }
                                case 1 -> {
                                    var calculatedPrice = calculateTotalPriceForSpecificOrder(orderID);
                                    for (var subOrderResponse : subOrderList) {
                                        var subPrice = calculatedPrice.getBrandDetailPriceResponseList()
                                                .stream()
                                                .filter(brandPrice -> brandPrice.getSubOrderID().equals(subOrderResponse.getOrderID()))
                                                .findFirst()
                                                .orElseThrow(() -> {
                                                    return new ItemNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
                                                });

                                        var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
                                        if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                            paymentService.createPayOSPayment(
                                                    PaymentRequest
                                                            .builder()
                                                            .orderID(subOrder.getOrderID())

                                                            .paymentSenderID(
                                                                    userService.getUserByEmail("accountantsmarttailor123@gmail.com")
                                                                            .getUserID()
                                                            )
                                                            .paymentSenderName("")
                                                            .paymentSenderBankCode("")
                                                            .paymentSenderBankNumber("")

                                                            .paymentRecipientID(
                                                                    subOrder.getDetailList().get(0)
                                                                            .getBrand().getBrandID()
                                                            )
                                                            .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                            .paymentRecipientBankCode("OCB")
                                                            .paymentRecipientBankNumber("0163100007285002")

                                                            .paymentType(PaymentType.BRAND_INVOICE)
                                                            .paymentAmount(
                                                                    Integer.valueOf(subPrice.getBrandPriceFirstStage())
                                                            )
                                                            .itemList(null)
                                                            .build()
                                            );
                                        }
                                    }
                                }
                                case 2 -> {
                                    var calculatedPrice = calculateTotalPriceForSpecificOrder(orderID);
                                    for (var subOrderResponse : subOrderList) {
                                        var subPrice = calculatedPrice.getBrandDetailPriceResponseList()
                                                .stream()
                                                .filter(brandPrice -> brandPrice.getSubOrderID().equals(subOrderResponse.getOrderID()))
                                                .findFirst()
                                                .orElseThrow(() -> {
                                                    return new ItemNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
                                                });

                                        var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
                                        if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                            paymentService.createPayOSPayment(
                                                    PaymentRequest
                                                            .builder()
                                                            .orderID(subOrder.getOrderID())

                                                            .paymentSenderID(
                                                                    userService.getUserByEmail("accountantsmarttailor123@gmail.com")
                                                                            .getUserID()
                                                            )
                                                            .paymentSenderName("")
                                                            .paymentSenderBankCode("")
                                                            .paymentSenderBankNumber("")

                                                            .paymentRecipientID(
                                                                    subOrder.getDetailList().get(0)
                                                                            .getBrand().getBrandID()
                                                            )
                                                            .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                            .paymentRecipientBankCode("OCB")
                                                            .paymentRecipientBankNumber("0163100007285002")

                                                            .paymentType(PaymentType.BRAND_INVOICE)
                                                            .paymentAmount(
                                                                    Integer.valueOf(subPrice.getBrandPriceSecondStage())
                                                            )
                                                            .itemList(null)
                                                            .build()
                                            );
                                        }
                                    }
                                }
                            }
//                            // Step 1: Get current stage
//                            var depositStage = subOrderList.stream().allMatch(o -> o.getOrderStatus().equals(OrderStatus.COMPLETED) || o.getOrderStatus().equals(OrderStatus.FINISH_FIRST_STAGE));
//
//                            var firstStage = subOrderList.stream().allMatch(o -> o.getOrderStatus().equals(OrderStatus.COMPLETED) || o.getOrderStatus().equals(OrderStatus.FINISH_SECOND_STAGE));
//
//                            if (depositStage) {
//                                var depositPayment = paymentService.findAllByOrderID(orderID).stream().filter(p -> p.getPaymentType().equals(PaymentType.DEPOSIT)).findFirst().orElseThrow(() -> new ItemNotFoundException("Deposit payment not found for OrderID: " + orderID));
//
//                                // Get the status of the deposit payment
//                                var depositPaymentStatus = depositPayment.getPaymentStatus();
//
//                                // If payment has been made
//                                if (depositPaymentStatus) {
//                                    /**
//                                     * TODO
//                                     * Create a report for the employee
//                                     */
//                                    logger.warn("Create a report: DEPOSIT: {}", "PAID");
//
//                                    /**
//                                     * TODO
//                                     * Update numberOfViolations
//                                     */
//                                    logger.warn("Update numberOfViolations: DEPOSIT: {}", "PAID");
//                                } else {
//                                    /**
//                                     * TODO
//                                     * Create a report for the employee
//                                     */
//                                    logger.warn("Create a report: DEPOSIT: {}", "PENDING");
//                                }
//                                /**
//                                 * TODO
//                                 * Create Transaction For Brand
//                                 */
//                                for (var subOrderResponse : subOrderList) {
//                                    var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                    if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
//                                        paymentService.createPayOSPayment(PaymentRequest.builder().orderID(subOrder.getOrderID())
//
//                                                .paymentSenderID(null).paymentSenderName("").paymentSenderBankCode("").paymentSenderBankNumber("")
//
//                                                .paymentRecipientID(null).paymentRecipientName("NGUYEN HOANG LAM TRUONG").paymentRecipientBankCode("OCB").paymentRecipientBankNumber("0163100007285002")
//
//                                                .paymentType(PaymentType.BRAND_INVOICE).paymentAmount(subOrder.getTotalPrice()).itemList(null).build());
//                                    }
//                                }
//                            }
//                            else
//                                if (firstStage) {
//                                var stage1Payment = paymentService.findAllByOrderID(orderID).stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_1)).findFirst().orElseThrow(() -> new ItemNotFoundException("Stage 1 payment not found for OrderID: " + orderID));
//
//                                // Get the status of the deposit payment
//                                var stage1PaymentStatus = stage1Payment.getPaymentStatus();
//
//                                // If payment has been made
//                                if (stage1PaymentStatus) {
//                                    /**
//                                     * TODO
//                                     * Create a report for the employee
//                                     */
//                                    logger.warn("Create a report: FINISH_STAGE_1: {}", "PAID");
//
//                                    /**
//                                     * TODO
//                                     * Update numberOfViolations
//                                     */
//                                    logger.warn("Update numberOfViolations: FINISH_STAGE_1: {}", "PAID");
//                                } else {
//                                    /**
//                                     * TODO
//                                     * Create a report for the employee
//                                     */
//                                    logger.warn("Create a report: FINISH_STAGE_1: {}", "PENDING");
//                                }
//                                /**
//                                 * TODO
//                                 * Create Transaction For Brand
//                                 */
//                                for (var subOrderResponse : subOrderList) {
//                                    var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                    if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
//                                        paymentService.createPayOSPayment(PaymentRequest.builder().orderID(subOrder.getOrderID())
//
//                                                .paymentSenderID(null).paymentSenderName("").paymentSenderBankCode("").paymentSenderBankNumber("")
//
//                                                .paymentRecipientID(null).paymentRecipientName("NGUYEN HOANG LAM TRUONG").paymentRecipientBankCode("OCB").paymentRecipientBankNumber("0163100007285002")
//
//                                                .paymentType(PaymentType.BRAND_INVOICE).paymentAmount(subOrder.getTotalPrice()).itemList(null).build());
//                                    }
//                                }
//                            }
//                            else {
//                                var stage2Payment = paymentService.findAllByOrderID(orderID).stream().filter(p -> p.getPaymentType().equals(PaymentType.STAGE_2)).findFirst().orElseThrow(() -> new ItemNotFoundException("Stage 2 payment not found for OrderID: " + orderID));
//
//                                // Get the status of the deposit payment
//                                var stage2PaymentStatus = stage2Payment.getPaymentStatus();
//
//                                // If payment has been made
//                                if (stage2PaymentStatus) {
//                                    /**
//                                     * TODO
//                                     * Create a report for the employee
//                                     */
//                                    logger.warn("Create a report: FINISH_STAGE_2: {}", "PAID");
//
//                                    /**
//                                     * TODO
//                                     * Update numberOfViolations
//                                     */
//                                    logger.warn("Update numberOfViolations: FINISH_STAGE_2: {}", "PAID");
//                                } else {
//                                    /**
//                                     * TODO
//                                     * Create a report for the employee
//                                     */
//                                    logger.warn("Create a report: FINISH_STAGE_2: {}", "PENDING");
//                                }
//                                /**
//                                 * TODO
//                                 * Create Transaction For Brand
//                                 */
//                                for (var subOrderResponse : subOrderList) {
//                                    var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                    if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
//                                        paymentService.createPayOSPayment(PaymentRequest.builder().orderID(subOrder.getOrderID())
//
//                                                .paymentSenderID(null).paymentSenderName("").paymentSenderBankCode("").paymentSenderBankNumber("")
//
//                                                .paymentRecipientID(null).paymentRecipientName("NGUYEN HOANG LAM TRUONG").paymentRecipientBankCode("OCB").paymentRecipientBankNumber("0163100007285002")
//
//                                                .paymentType(PaymentType.BRAND_INVOICE).paymentAmount(subOrder.getTotalPrice()).itemList(null).build());
//                                    }
//                                }
//                            }
                        }
                    }
                }
                /**
                 * THIS ORDER IS SUB_ORDER
                 */
                else {
                    var parentOrder = getOrderById(existedOrder.getParentOrder().getOrderID()).get();
//                    parentOrder.setOrderStatus(OrderStatus.PENDING);
//                    orderRepository.save(parentOrder);

                    var detailList = existedOrder.getDetailList();
                    Brand curBrand = null;
                    for (var detail : detailList) {
                        detail.setOrder(parentOrder);
                        curBrand = detail.getBrand();
                        detail.setBrand(null);
                        detail.setDetailStatus(false);
                        detailRepository.save(detail);
                    }

                    if (!existedOrder.getOrderStatus().equals(OrderStatus.PENDING)) {
                        logger.error("SUB ORDER IS NOT PENDING....");

                        int price = (int) (parentOrder.getTotalPrice());
//                        var orderResponse = orderMapper.mapToOrderCustomResponse(existedOrder);
                        var sender = curBrand;
                        var recipient = userService.getUserByEmail("accountantsmarttailor123@gmail.com");
                        /*
                            UPDATE BRAND RATING WHEN BRAND CANCEL ORDER AFTER ORDER START AND PENALTY
                        */
                        var ratingReductionAfterStart = Float.parseFloat(systemPropertiesService.getByName("RATING_REDUCTION_AFTER_START").getPropertyValue());
                        brandService.ratingBrandCancelOrder(curBrand.getBrandID(), ratingReductionAfterStart);

                        paymentService.createPayOSPayment(
                                PaymentRequest.builder()
                                        .paymentAmount(price)
                                        .paymentMethod(PaymentMethod.BANK_TRANSFER)
                                        .paymentType(PaymentType.FINED)

                                        .paymentSenderID(sender.getBrandID())
                                        .paymentSenderName(sender.getBrandName())
                                        .paymentSenderBankCode("")
                                        .paymentSenderBankNumber("")
                                        .paymentRecipientID(
                                                recipient.getUserID()
                                        )
                                        .paymentRecipientName("OCB")
                                        .paymentRecipientBankNumber("0163100007285002")
                                        .paymentRecipientBankCode("")
                                        .orderID(orderID)
                                        .build()
                        );
                        logger.error("CREATE FINED SUCCESSFULLY");
                    } else {
                           /*
                            UPDATE BRAND RATING WHEN BRAND CANCEL ORDER BEFORE ORDER START TO AVOID SPAM PICK ORDER
                        */
                        var ratingReductionBeforeStart = Float.parseFloat(systemPropertiesService.getByName("RATING_REDUCTION_BEFORE_START").getPropertyValue());
                        brandService.ratingBrandCancelOrder(curBrand.getBrandID(), ratingReductionBeforeStart);
                    }

                    existedOrder.setDetailList(null);
                    existedOrder.setParentOrder(null);
                    var orderResponse = safeMapToOrderResponse(parentOrder);
                    applicationEventPublisher.publishEvent(new CreateOrderEvent(orderResponse));
                }
            }
            case START_PRODUCING -> {
                existedOrder.setProductionStartDate(LocalDateTime.now());
            }
            case COMPLETED -> {
                existedOrder.setProductionCompletionDate(LocalDateTime.now());
            }
            case SUSPENDED -> {
//                var orderResponse = safeMapToOrderResponse(existedOrder);
//                applicationEventPublisher.publishEvent(new CreateOrderEvent(orderResponse));

                if (existedOrder.getOrderStatus().equals(OrderStatus.SUSPENDED)) {
                    var orderResponse = safeMapToOrderResponse(existedOrder);
                    applicationEventPublisher.publishEvent(new CreateOrderEvent(orderResponse));
                }
            }
        }

        existedOrder.setOrderStatus(orderStatus);
        var stageResponseList = stageService.getOrderStageByOrderID(orderID);
        for (var stageResponse : stageResponseList) {
            if (stageResponse.getStage().equals(orderStatus)) {
                var stage = stageService.getOrderStageByID(stageResponse.getStageId());
                stage.setStatus(true);
                stageService.updateStage(stage);
            }
        }

        var updatedOrder = orderRepository.save(existedOrder);

        if (orderStatus.equals(OrderStatus.FINISH_FIRST_STAGE) || orderStatus.equals(OrderStatus.FINISH_SECOND_STAGE)) {
            var stage = stageService.getOrderStageByID(
                    (
                            stageService.getOrderStageByOrderID(
                                            existedOrder.getParentOrder().getOrderID()
                                    ).stream()
                                    .filter(s -> s.getStage().equals(OrderStatus.PROCESSING))
                                    .findFirst()
                                    .get()
                                    .getStageId()
                    )
            );
            var subStage = stageService.getOrderStageByID(
                    (
                            stageService.getOrderStageByOrderID(existedOrder.getOrderID())
                                    .stream()
                                    .filter(s -> s.getStage().equals(existedOrder.getOrderStatus()))
                                    .findFirst()
                                    .get()
                                    .getStageId()
                    )
            );
            if (stage != null) {
                stage.setCurrentQuantity(stage.getCurrentQuantity() + subStage.getCurrentQuantity());
                stageService.updateStage(stage);
            }
        }

        if (existedOrder.getOrderType().equals("PARENT_ORDER")) {
//            getOrderByOrderID(existedOrder.getOrderID());
            var sender = existedOrder.getEmployee().getEmployeeID();
            var cus = designService.getDesignObjectByOrderID(orderID).getUser().getUserID();

            notificationService.sendPrivateNotification(
                    NotificationRequest
                            .builder()
                            .senderID(sender)
                            .recipientID(cus)
                            .action("CHANGE ORDER STATUS")
                            .type("REQUEST VIEW")
                            .targetID(existedOrder.getOrderID())
                            .message("Order status has been changed! Status: " + orderStatus.name())
                            .build()
            );

            sender = designService.getDesignObjectByOrderID(orderID).getUser().getUserID();
            cus = existedOrder.getEmployee().getEmployeeID();

            notificationService.sendPrivateNotification(
                    NotificationRequest
                            .builder()
                            .senderID(sender)
                            .recipientID(cus)
                            .action("CHANGE ORDER STATUS")
                            .type("REQUEST VIEW")
                            .targetID(existedOrder.getOrderID())
                            .message("Order status has been changed! Status: " + orderStatus.name())
                            .build()
            );

        }
//        else {
//            if (existedOrder.getParentOrder() != null)
//                getOrderByOrderID(existedOrder.getParentOrder().getOrderID());
//        }
        return orderMapper.mapToOrderResponse(updatedOrder);
    }

    @Override
    public void updateOrder(Order order) throws Exception {
        orderRepository.save(order);
//        getOrderByOrderID(order.getOrderID());
    }

    @Override
    public OrderResponse brandPickOrder(OrderPickingRequest orderPickingRequest) throws Exception {
        try {
            if (orderPickingRequest == null) {
                return null;
            }

            String brandID = orderPickingRequest.getBrandID();
            String basedOrderID = orderPickingRequest.getOrderID();
            List<String> detailList = orderPickingRequest.getDetailList();

            /**
             * TODO
             * check if detail is picked or not
             */

            var checkExistBrand = brandService.getBrandById(brandID);
            if (checkExistBrand.isEmpty()) {
                throw new BadRequestException(MessageConstant.CAN_NOT_FIND_BRAND + "with brandID: " + brandID);
            }
            var existedBrand = checkExistBrand.get();

            var checkBasedOrder = getOrderById(basedOrderID);
            if (checkBasedOrder.isEmpty()) {
                throw new BadRequestException(MessageConstant.RESOURCE_NOT_FOUND + "with orderID: " + basedOrderID);
            }
            var basedOrder = checkBasedOrder.get();
            if (basedOrder.getOrderStatus().equals(OrderStatus.PENDING) || basedOrder.getOrderStatus().equals(OrderStatus.SUSPENDED)) {
                Design baseDesign = null;
                for (String detailID : detailList) {
                    var checkDetail = detailRepository.getDesignDetailByDesignDetailID(detailID);
                    if (checkDetail.isEmpty()) {
                        throw new BadRequestException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN_DETAIL + " with detailID: " + detailID);
                    }
                    if (!orderRepository.getOrderByDetailID(detailID).getOrderID().equals(basedOrderID)) {
                        throw new BadRequestException("This detail " + detailID + " is inside another order!");
                    }
                    var detail = detailRepository.getDesignDetailByDesignDetailID(detailID).get();
                    if (baseDesign == null) {
                        baseDesign = detail.getDesign();
                    } else {
                        if (baseDesign.getDesignID() != detail.getDesign().getDesignID()) {
                            throw new BadRequestException("This detail " + detailID + " is not in the same design!");
                        }
                    }
                }
                int price = baseDesign.getPartOfDesignList().stream().flatMap(partOfDesign -> Stream.concat(Stream.of(partOfDesign.getMaterial()), partOfDesign.getItemMaskList().stream().map(ItemMask::getMaterial))).filter(Objects::nonNull) // Lọc bỏ các vật liệu bị null
                        .collect(Collectors.toMap(material -> material, material -> 1, Integer::sum)).entrySet().stream().mapToInt(entry -> {
                            var checkBrandMaterial = brandMaterialService.getPriceByID(BrandMaterialKey.builder().brandID(brandID).materialID(entry.getKey().getMaterialID()).build());
                            return checkBrandMaterial.map(brandMaterial -> brandMaterial.getBrandPrice() * entry.getValue()).orElse(0);
                        }).sum();

                Integer quantity = 0;

                var existedBrandOrder = detailRepository.getDetailOfOrderBaseOnBrandID(basedOrderID, brandID);
                if (existedBrandOrder != null) {
                    logger.info("Existed Order is updating...");
                    var orderResponse = existedBrandOrder.getOrder();
                    List<DesignDetail> detailResponse = new ArrayList<>();
                    for (String detailID : detailList) {
                        var detail = detailRepository.getDesignDetailByDesignDetailID(detailID).get();
                        detail.setOrder(orderResponse);
                        detail.setBrand(existedBrand);
                        detail.setDetailStatus(true);
                        detailRepository.save(detail);

                        quantity += detail.getQuantity();
                        price *= detail.getQuantity();

                        detailResponse.add(detail);
                    }
                    orderResponse = existedBrandOrder.getOrder();
                    orderResponse.setTotalPrice(price);
                    orderResponse.setQuantity(quantity);
                    var wageProperty = systemPropertiesService.getByName("BRAND_PRODUCTIVITY");
                    var dayCompleted = Integer.parseInt(brandPropertiesService.getByBrandIDAndPropertyID(brandID, wageProperty.getPropertyID()).getBrandPropertyValue());
                    var oldCompleteDate = orderResponse.getExpectedProductCompletionDate();
                    var newCompleteDate = orderResponse.getExpectedStartDate().plusDays((long) Math.ceil(quantity / dayCompleted));
                    orderResponse.setExpectedProductCompletionDate(oldCompleteDate.isAfter(newCompleteDate) ? oldCompleteDate : newCompleteDate);
                    updateOrder(orderResponse);
                    orderRepository.save(orderResponse);

                    basedOrder.setTotalPrice(basedOrder.getTotalPrice() + price);
//                    basedOrder.setTotalPrice(10000);
                    orderRepository.save(basedOrder);

                    orderResponse.setDetailList(detailResponse);
                    return orderMapper.mapToOrderResponse(orderResponse);
                } else {
                    logger.info("New Order is created...");
                    var detail = detailRepository.getDesignDetailByDesignDetailID(detailList.get(0)).get();
                    var design = detail.getDesign();
                    OrderResponse createdOrder = createOrder(
                            OrderRequest.builder()
                                    .parentOrderID(basedOrderID)
                                    .designID(design.getDesignID())
                                    .orderType(basedOrder.getOrderType())
                                    .quantity(0)
                                    .orderStatus(basedOrder.getOrderStatus().equals(OrderStatus.PENDING) ? OrderStatus.PENDING : OrderStatus.CHECKING_SAMPLE_DATA)
                                    .address(basedOrder.getAddress()).province(basedOrder.getProvince())
                                    .district(basedOrder.getDistrict())
                                    .ward(basedOrder.getWard()).phone(basedOrder.getPhone()).buyerName(basedOrder.getBuyerName()).build());
                    var orderResponse = getOrderById(createdOrder.getOrderID()).get();
                    List<DesignDetail> detailResponse = new ArrayList<>();
                    for (String detailID : detailList) {
                        detail = detailRepository.getDesignDetailByDesignDetailID(detailID).get();
                        detail.setOrder(orderResponse);
                        detail.setBrand(existedBrand);
                        detail.setDetailStatus(true);
                        detailRepository.save(detail);

                        quantity += detail.getQuantity();
                        price *= detail.getQuantity();

                        detailResponse.add(detail);
                    }

                    var wageProperty = systemPropertiesService.getByName("BRAND_PRODUCTIVITY");
                    var dayCompleted = Integer.parseInt(brandPropertiesService.getByBrandIDAndPropertyID(brandID, wageProperty.getPropertyID()).getBrandPropertyValue());
                    logger.info("Day Completed Line 641 {}", dayCompleted);
                    logger.info("Order Quantity Line 642 {}", quantity);
                    orderResponse.setExpectedProductCompletionDate(orderResponse.getExpectedStartDate().plusDays((long) Math.ceil(quantity / dayCompleted)));
                    updateOrder(orderResponse);

                    orderResponse = getOrderById(createdOrder.getOrderID()).get();
                    orderResponse.setTotalPrice(price);
                    orderResponse.setQuantity(quantity);
                    orderRepository.save(orderResponse);

                    basedOrder.setTotalPrice(basedOrder.getTotalPrice() + price);
//                    basedOrder.setTotalPrice(10000);

                    orderRepository.save(basedOrder);

                    int divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());
                    stageService.createOrderStage(OrderStageRequest.builder().orderID(createdOrder.getOrderID()).stage(OrderStatus.START_PRODUCING).currentQuantity(0).status(false).build());

                    if (quantity >= divideNumber) {
                        int eachPhase = Utilities.roundToNearestHalf(quantity * 1.0 / 3);
                        stageService.createOrderStage(OrderStageRequest.builder().orderID(createdOrder.getOrderID()).stage(OrderStatus.FINISH_FIRST_STAGE).currentQuantity(eachPhase).status(false).build());
                        stageService.createOrderStage(OrderStageRequest.builder().orderID(createdOrder.getOrderID()).stage(OrderStatus.FINISH_SECOND_STAGE).currentQuantity(eachPhase).status(false).build());
                        stageService.createOrderStage(OrderStageRequest.builder().orderID(createdOrder.getOrderID()).stage(OrderStatus.COMPLETED).currentQuantity(orderResponse.getQuantity() - (eachPhase * 2)).status(false).build());
                    } else {
                        stageService.createOrderStage(OrderStageRequest.builder().orderID(createdOrder.getOrderID()).stage(OrderStatus.COMPLETED).currentQuantity(orderResponse.getQuantity()).status(false).build());
                    }

                    orderResponse.setDetailList(detailResponse);
//                    getOrderByOrderID(basedOrderID);
                    return orderMapper.mapToOrderResponse(orderResponse);
                }
            } else {
                throw new RuntimeException("This order is expire!");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Order getOrderByDetailID(String detailID) {
        return orderRepository.getOrderByDetailID(detailID);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isOrderCompletelyPicked(String orderID) {
        try {
            logger.info("Inside method isOrderCompletelyPicked");
            var order = orderRepository.findById(orderID).get();
            var detailList = order.getDetailList();
            for (DesignDetail detail : detailList) {
                if (!detail.getDetailStatus()) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isOrderExpireTime(String orderID) {
        var systemPropertiesExpirationTime = systemPropertiesService.getByName("MATCHING_TIME");
        var order = orderRepository.findById(orderID).get();
        logger.info("Inside Method isOrderExpireTime with orderID {}", orderID);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime orderExpiredDateTime = order.getCreateDate().plusMinutes(Integer.parseInt(systemPropertiesExpirationTime.getPropertyValue()));
        logger.info("CurrentDateTime {}", currentDateTime);
        logger.info("OrderExpiredDateTime {}", orderExpiredDateTime);
        return currentDateTime.isAfter(orderExpiredDateTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllParentOrder() {
        return orderRepository.findAll().stream().filter(orderResponse -> orderResponse.getOrderType().equals("PARENT_ORDER")).map(this::safeMapToOrderResponse).toList();
    }

    @Override
    public List<String> filterBrandForSpecificOrderBaseOnDesign(String designID) {
        logger.info("DesignID {}", designID);
        var designResponse = designService.getDesignByID(designID);
        if (designResponse == null) {
            throw new ItemNotFoundException("Can not find Design By Design ID: " + designID);
        }

        // Find all brands by Expert Tailoring ID of the Design
        var expertTailoring = designResponse.getExpertTailoring();
        var brandExpertTailoringSelected = brandService.findAllBrandByExpertTailoringID(expertTailoring.getExpertTailoringID());

        brandExpertTailoringSelected.forEach(brand -> logger.info("Brand Selected by Expert Tailoring: {}", brand.getUser().getEmail()));


        // Get all material IDs of the design
        Set<String> designMaterialIDs = new HashSet<>();
        designResponse.getPartOfDesignList().forEach(partOfDesign -> {
            var materialPartOfDesign = partOfDesign.getMaterial();
            if (materialPartOfDesign != null) {
                designMaterialIDs.add(materialPartOfDesign.getMaterialID());
            }
            partOfDesign.getItemMaskList().forEach(itemMask -> {
                var materialItemMask = itemMask.getMaterial();
                if (materialItemMask != null) {
                    designMaterialIDs.add(materialItemMask.getMaterialID());
                }
            });
        });

        designMaterialIDs.forEach(designMaterial -> logger.info("Design Material ID {}", designMaterial));

        // Filter brands that have all materials used in the design
        List<String> brandResponses = new ArrayList<>();
        for (var brand : brandExpertTailoringSelected) {
            var brandMaterials = brandMaterialService.getAllBrandMaterialByBrandID(brand.getBrandID());
            long matchingMaterialCount = designMaterialIDs.stream().filter(designMaterialID -> brandMaterials.stream().anyMatch(brandMaterial -> brandMaterial.getMaterialID().toString().equals(designMaterialID.toString()))).count();
            if (matchingMaterialCount == designMaterialIDs.size()) {
                brandResponses.add(brand.getUser().getEmail());
            }
        }
        return brandResponses;
    }


    @Override
    public void confirmOrder(String orderID) {
        try {
            var checkOrder = getOrderById(orderID);
            if (checkOrder.isEmpty()) {
                throw new Exception(MessageConstant.RESOURCE_NOT_FOUND);
            }
            var order = checkOrder.get();

            var subOrderList = getSubOrderByParentID(orderID);
            String maxDate = subOrderList.get(0).getExpectedProductCompletionDate();
            var convertMaxDate = LocalDateTime.parse(maxDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
//            LocalDateTime maxDate = subOrderList.get(0).getExpectedProductCompletionDate();
            int quantity = 0;
            var calculatePrice = calculateTotalPriceForSpecificOrder(orderID);
            for (var subOrder : subOrderList) {
                var expectedCompleteDate = subOrder.getExpectedProductCompletionDate();
                var convertExpectedCompleteDate = LocalDateTime.parse(expectedCompleteDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                maxDate = convertExpectedCompleteDate.isAfter(convertMaxDate) ? expectedCompleteDate : maxDate;
                quantity += subOrder.getQuantity();

                var brandPrice = calculatePrice.getBrandDetailPriceResponseList().stream()
                        .filter(bp -> bp.getSubOrderID().equals(subOrder.getOrderID()))
                        .findFirst().get();
                var subOrderObject = getOrderById(subOrder.getOrderID()).get();
                subOrderObject.setTotalPrice(
                        Integer.parseInt(brandPrice.getBrandPriceDeposit())
                                +
                                Integer.parseInt(brandPrice.getBrandPriceFirstStage())
                                +
                                Integer.parseInt(brandPrice.getBrandPriceSecondStage())
                );
            }
            order.setTotalPrice(Integer.valueOf(calculatePrice.getTotalPriceOfParentOrder()));
            order.setExpectedProductCompletionDate(LocalDateTime.parse(maxDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            updateOrder(order);

            var oldStage = stageService.getOrderStageByOrderID(orderID);
            if (oldStage.isEmpty()) {
                int divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());
                stageService.createOrderStage(OrderStageRequest.builder().orderID(orderID).stage(OrderStatus.DEPOSIT).currentQuantity(0).status(false).build());

                stageService.createOrderStage(OrderStageRequest.builder().orderID(orderID).stage(OrderStatus.PROCESSING).currentQuantity(0).status(false).build());

                stageService.createOrderStage(OrderStageRequest.builder().orderID(orderID).stage(OrderStatus.COMPLETED).currentQuantity(quantity).status(false).build());
            }
//            calculatePrice.put(orderID, calculateTotalPriceForSpecificOrder(orderID));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderStageResponse> getOrderStageByOrderID(String orderID) {
        return stageService.getOrderStageByOrderID(orderID).stream()
                .sorted(Comparator.comparing(OrderStageResponse::getCreatedDate).reversed())
                .toList();
    }

    private Employee getSuitableEmp() {
        try {
            var empList = employeeService.getAll();
            if (empList.isEmpty()) {
                return null;
            }
            empList.sort(Comparator.comparingInt(Employee::getPendingTask).thenComparingInt(Employee::getTotalTask).thenComparing(Employee::getFailTask, Comparator.reverseOrder()));
            return empList.get(0);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Transactional
    @Override
    public void ratingOrder(String jwtToken, RatingOrderRequest ratingOrderRequest) {
        var userID = jwtService.extractUserIDFromJwtToken(jwtToken);
        if (!userID.equals(ratingOrderRequest.getUserID())) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        var user = userService.getUserByUserID(ratingOrderRequest.getUserID())
                .orElseThrow(() -> new ItemNotFoundException("Cannot find User with UserID: " + ratingOrderRequest.getUserID()));

        var parentOrder = orderRepository.findById(ratingOrderRequest.getParentOrderID())
                .orElseThrow(() -> new ItemNotFoundException("Cannot find Order with OrderID: " + ratingOrderRequest.getParentOrderID()));

        var orderRating = (float) ratingOrderRequest.getRating();
        parentOrder.setRating(orderRating);

        // Update Rating for Order
        orderRepository.save(parentOrder);

        // Rating Brand Contribute to Order
        var subOrderList = getSubOrderByParentID(parentOrder.getOrderID());
        var estimateOrderTimeLine = getOrderTimeLineByParentOrderID(parentOrder.getOrderID());

        // Define the format for parsing and formatting dates
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Convert estimated dates to LocalDateTime
        LocalDateTime estimatedDateFinishFirstStage = null;
        if (estimateOrderTimeLine.getEstimatedDateFinishFirstStage() != null) {
            logger.error("Inside estimatedDateFinishFirstStage Line 2258 {}", estimateOrderTimeLine.getEstimatedDateFinishFirstStage());
            estimatedDateFinishFirstStage = LocalDateTime.parse(estimateOrderTimeLine.getEstimatedDateFinishFirstStage(), outputFormatter);
        }

        LocalDateTime estimatedDateFinishSecondStage = null;
        if (estimateOrderTimeLine.getEstimatedDateFinishSecondStage() != null) {
            estimatedDateFinishSecondStage = LocalDateTime.parse(estimateOrderTimeLine.getEstimatedDateFinishSecondStage(), outputFormatter);
        }

        LocalDateTime estimatedDateCompletion = null;
        if (estimateOrderTimeLine.getEstimatedDateFinishCompleteStage() != null) {
            estimatedDateCompletion = LocalDateTime.parse(estimateOrderTimeLine.getEstimatedDateFinishCompleteStage(), outputFormatter);
        }

        logger.info("Estimated Date Finish First Stage: {}", estimatedDateFinishFirstStage);
        logger.info("Estimated Date Finish Second Stage: {}", estimatedDateFinishSecondStage);
        logger.info("Estimated Date Completion: {}", estimatedDateCompletion);
        for (var subOrder : subOrderList) {
            var designDetail = detailRepository.getDesignDetailBySubOrderID(subOrder.getOrderID());
            var brand = designDetail.stream().map(DesignDetail::getBrand).findFirst();
            var subOrderRating = subOrder.getRating();
            var completedAheadOfSchedule = 0;
            var completedLate = 0;
            var subOrderStageList = stageService.getOrderStageByOrderID(subOrder.getOrderID());
            var brandOrderRating = orderRating;

            for (var subOrderStage : subOrderStageList) {

                if (subOrderStage.getLastModifiedDate() == null) continue;

                LocalDateTime lastModifiedDateTimeFormatted = LocalDateTime.parse(subOrderStage.getLastModifiedDate(), inputFormatter);

                // Compare the stages and calculate counters based on dates
                if (subOrderStage.getStage().equals(OrderStatus.FINISH_FIRST_STAGE)) {
                    if (estimatedDateFinishFirstStage != null) {
                        if (lastModifiedDateTimeFormatted.isBefore(estimatedDateFinishFirstStage)) {
                            completedAheadOfSchedule++;
                        } else if (lastModifiedDateTimeFormatted.isAfter(estimatedDateFinishFirstStage)) {
                            completedLate++;
                        }
                    }
                    logger.error("Last Modified Date Time At Finish First Stage: {}", lastModifiedDateTimeFormatted);
                } else if (subOrderStage.getStage().equals(OrderStatus.FINISH_SECOND_STAGE)) {
                    if (estimatedDateFinishSecondStage != null) {
                        if (lastModifiedDateTimeFormatted.isBefore(estimatedDateFinishSecondStage)) {
                            completedAheadOfSchedule++;
                        } else if (lastModifiedDateTimeFormatted.isAfter(estimatedDateFinishSecondStage)) {
                            completedLate++;
                        }
                    }
                    logger.error("Last Modified Date Time At Finish Second Stage: {}", lastModifiedDateTimeFormatted);
                } else if (subOrderStage.getStage().equals(OrderStatus.COMPLETED)) {
                    if (estimatedDateCompletion != null) {
                        if (lastModifiedDateTimeFormatted.isBefore(estimatedDateCompletion)) {
                            completedAheadOfSchedule++;
                        } else if (lastModifiedDateTimeFormatted.isAfter(estimatedDateCompletion)) {
                            completedLate++;
                        }
                    }
                    logger.error("Last Modified Date Time At Finish Complete Stage: {}", lastModifiedDateTimeFormatted);
                }
            }

            // Update orderRating based on the completion status
            var systemPropertyRateAHeadSchedule = systemPropertiesService.getByName("RATE_AHEAD_SCHEDULE");
            var rateAHeadSchedule = Double.parseDouble(systemPropertyRateAHeadSchedule.getPropertyValue());

            var systemPropertyRateLateSchedule = systemPropertiesService.getByName("RATE_LATE_SCHEDULE");
            var rateLateSchedule = Double.parseDouble(systemPropertyRateLateSchedule.getPropertyValue());

            brandOrderRating += (completedAheadOfSchedule > 0) ? (float) (completedAheadOfSchedule * rateAHeadSchedule) : 0;
            brandOrderRating += (completedLate > 0) ? (float) (completedLate * -rateLateSchedule) : 0;
            if (brandOrderRating <= 0) brandOrderRating = 0.0f;
            else if (brandOrderRating > 5) brandOrderRating = 5.0f;

            logger.error("Brand Infor Joining Order {} {}", brand.get().getUser().getEmail(), brand.get().getUser().getUserID());
            logger.warn("Complete A Head of Schedule {}", completedAheadOfSchedule);
            logger.warn("Complete Late {}", completedLate);
            logger.warn("Order Rating {}", brandOrderRating);

            // Update the rating for the brand
            brandService.ratingBrand(brand.get().getBrandID(), brandOrderRating, subOrderRating);

            // Update Rating for SubOrder
            var existedSubOrder = orderRepository.findById(subOrder.getOrderID()).get();
            existedSubOrder.setRating(brandOrderRating);
            orderRepository.save(existedSubOrder);
        }
    }

    @Override
    public OrderTimeLineResponse getOrderTimeLineByParentOrderID(String parentOrderID) {
        var parentOrder = orderRepository.findById(parentOrderID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Order with Parent Order ID: " + parentOrderID));

        var subOrderList = getSubOrderByParentID(parentOrderID);
        var maximumDateAtFirstStage = Integer.MIN_VALUE;
        var maximumDateAtSecondStage = Integer.MIN_VALUE;
        var maximumDateAtCompleteStage = Integer.MIN_VALUE;
        var maximumQuantityOfSubOrder = Integer.MIN_VALUE;
        var divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());

        for (var subOrder : subOrderList) {
            var designDetail = detailRepository.getDesignDetailBySubOrderID(subOrder.getOrderID());
            var brand = designDetail.stream().map(DesignDetail::getBrand).findFirst();

            var systemPropertiesResponse = systemPropertiesService.getByName("BRAND_PRODUCTIVITY");
            var brandProductivity = brandPropertiesService.getByBrandIDAndPropertyID(brand.get().getBrandID(), systemPropertiesResponse.getPropertyID());

            maximumQuantityOfSubOrder = max(maximumQuantityOfSubOrder, subOrder.getQuantity());
            // Get All Stage Of SubOrder
            // Calculate All Quantity At FirstStage, SecondStage, CompleteStage
            // Find the Maximum Date At FirstStage, SecondStage, CompleteStage
            maximumDateAtFirstStage = max(maximumDateAtFirstStage, (int) Math.ceil((subOrder.getQuantity() * 1.0 / 3) / Integer.parseInt(brandProductivity.getBrandPropertyValue())));
            maximumDateAtSecondStage = max(maximumDateAtSecondStage, (int) Math.ceil((subOrder.getQuantity() * 2.0 / 3) / Integer.parseInt(brandProductivity.getBrandPropertyValue())));
            maximumDateAtCompleteStage = max(maximumDateAtCompleteStage, (int) Math.ceil((subOrder.getQuantity() * 1.0) / Integer.parseInt(brandProductivity.getBrandPropertyValue())));
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        if (maximumQuantityOfSubOrder < divideNumber) {
            return OrderTimeLineResponse
                    .builder()
                    .estimatedDateStartDepositStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate()))
                    .estimatedQuantityFinishFirstStage(0)
                    .estimatedDateFinishFirstStage(null)
                    .estimatedQuantityFinishSecondStage(0)
                    .estimatedDateFinishSecondStage(null)
                    .estimatedQuantityFinishCompleteStage(parentOrder.getQuantity())
                    .estimatedDateFinishCompleteStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtCompleteStage)))
                    .build();
        }

        return OrderTimeLineResponse
                .builder()
                .estimatedDateStartDepositStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate()))
                .estimatedQuantityFinishFirstStage(Utilities.roundToNearestHalf(parentOrder.getQuantity() * 1.0 / 3))
                .estimatedDateFinishFirstStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtFirstStage)))
                .estimatedQuantityFinishSecondStage(Utilities.roundToNearestHalf(parentOrder.getQuantity() * 2.0 / 3))
                .estimatedDateFinishSecondStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtSecondStage)))
                .estimatedQuantityFinishCompleteStage(parentOrder.getQuantity())
                .estimatedDateFinishCompleteStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtCompleteStage)))
                .build();
    }

    @Override
    public OrderTimeLineResponse getOrderTimeLineBySubOrderID(String subOrderID) {
        var subOrder = getOrderById(subOrderID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find SubOrder with SubOrderID: " + subOrderID));

        var parentOrder = orderRepository.getParentOrderBySubOrderID(subOrderID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find ParentOrder with SubOrderID: " + subOrderID));

        var listPayment = paymentService.findAllByOrderID(parentOrder.getOrderID());

        // Compare SubOrder Quantity and Divide Number Quantity
        var divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());
        var subOrderQuantity = subOrder.getQuantity();

        // Get Information Brand take part in SubOrder
        var designDetail = detailRepository.getDesignDetailBySubOrderID(subOrder.getOrderID());
        var brand = designDetail
                .stream()
                .map(DesignDetail::getBrand)
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with SubOrderID: " + subOrderID));

        // Get Brand Productivity Per Day
        var systemPropertiesResponse = systemPropertiesService.getByName("BRAND_PRODUCTIVITY");
        var brandProductivity = brandPropertiesService.getByBrandIDAndPropertyID(brand.getBrandID(), systemPropertiesResponse.getPropertyID());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        if (subOrderQuantity < divideNumber) {
            var maximumDateAtCompleteStage = (int) Math.ceil((subOrder.getQuantity() * 1.0) / Integer.parseInt(brandProductivity.getBrandPropertyValue()));
            if (listPayment.isEmpty()) {
                return OrderTimeLineResponse
                        .builder()
                        .estimatedDateStartDepositStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate()))
                        .estimatedQuantityFinishFirstStage(0)
                        .estimatedDateFinishFirstStage(null)
                        .estimatedQuantityFinishSecondStage(0)
                        .estimatedDateFinishSecondStage(null)
                        .estimatedQuantityFinishCompleteStage(parentOrder.getQuantity())
                        .estimatedDateFinishCompleteStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtCompleteStage)))
                        .build();
            } else {
                // Update TimeLine Along With Latest Payment of Customer
                // Check Whether Customer Payment Before The Estimated Date at Deposit Stage or not?
                // If Before Expected Start Deposit Stage, The TimeLine Of SubOrder still remains
                var customerDepositPayment = listPayment
                        .stream()
                        .filter(payment -> payment.getPaymentType().toString().equals(PaymentType.DEPOSIT.name()))
                        .findFirst();

                if (customerDepositPayment.get().getCreateDate().isBefore(parentOrder.getExpectedStartDate())) {
                    return OrderTimeLineResponse
                            .builder()
                            .estimatedDateStartDepositStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate()))
                            .estimatedQuantityFinishFirstStage(0)
                            .estimatedDateFinishFirstStage(null)
                            .estimatedQuantityFinishSecondStage(0)
                            .estimatedDateFinishSecondStage(null)
                            .estimatedQuantityFinishCompleteStage(parentOrder.getQuantity())
                            .estimatedDateFinishCompleteStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtCompleteStage)))
                            .build();
                }
                // If After Expected Start Deposit Stage, The TimeLine Of SubOrder Have to Start With DateTime Customer Payment
                else {
                    return OrderTimeLineResponse
                            .builder()
                            .estimatedDateStartDepositStage(dateTimeFormatter.format(customerDepositPayment.get().getCreateDate()))
                            .estimatedQuantityFinishFirstStage(0)
                            .estimatedDateFinishFirstStage(null)
                            .estimatedQuantityFinishSecondStage(0)
                            .estimatedDateFinishSecondStage(null)
                            .estimatedQuantityFinishCompleteStage(parentOrder.getQuantity())
                            .estimatedDateFinishCompleteStage(dateTimeFormatter.format(customerDepositPayment.get().getCreateDate().plusDays(maximumDateAtCompleteStage)))
                            .build();
                }
            }
        } else {
            var maximumDateAtFirstStage = (int) Math.ceil((subOrder.getQuantity() * 1.0 / 3) / Integer.parseInt(brandProductivity.getBrandPropertyValue()));
            var maximumDateAtSecondStage = (int) Math.ceil((subOrder.getQuantity() * 2.0 / 3) / Integer.parseInt(brandProductivity.getBrandPropertyValue()));
            var maximumDateAtCompleteStage = (int) Math.ceil((subOrder.getQuantity() * 1.0) / Integer.parseInt(brandProductivity.getBrandPropertyValue()));

            if (listPayment.isEmpty()) {
                return OrderTimeLineResponse
                        .builder()
                        .estimatedDateStartDepositStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate()))
                        .estimatedQuantityFinishFirstStage(Utilities.roundToNearestHalf(subOrder.getQuantity() * 1.0 / 3))
                        .estimatedDateFinishFirstStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtFirstStage)))
                        .estimatedQuantityFinishSecondStage(Utilities.roundToNearestHalf(subOrder.getQuantity() * 2.0 / 3))
                        .estimatedDateFinishSecondStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtSecondStage)))
                        .estimatedQuantityFinishCompleteStage(subOrder.getQuantity())
                        .estimatedDateFinishCompleteStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtCompleteStage)))
                        .build();
            } else {
                var customerDepositPayment = listPayment
                        .stream()
                        .filter(payment -> payment.getPaymentType().toString().equals(PaymentType.DEPOSIT.name()))
                        .findFirst();

                var customerFirstStagePayment = listPayment
                        .stream()
                        .filter(payment -> payment.getPaymentType().toString().equals(PaymentType.STAGE_1.name()))
                        .findFirst();

                var customerSecondStagePayment = listPayment
                        .stream()
                        .filter(payment -> payment.getPaymentType().toString().equals(PaymentType.STAGE_2.name()))
                        .findFirst();

                var expectedStartDepositSubOrder = parentOrder.getExpectedStartDate();
                // Check Customer Payment before Expected Start Date of Order
                // Customer Deposit Payment for Period of Deposit -> Stage 1
                if (customerDepositPayment.isPresent()) {
                    if (customerDepositPayment.get().getCreateDate().isAfter(parentOrder.getExpectedStartDate())) {
                        expectedStartDepositSubOrder = customerDepositPayment.get().getCreateDate();
                    }
                }
                var expectedStartFirstStageSubOrder = expectedStartDepositSubOrder.plusDays(maximumDateAtFirstStage);

                // Customer First Stage Payment for Period of Stage 1 -> Stage 2
                if (customerFirstStagePayment.isPresent()) {
                    if (customerFirstStagePayment.get().getCreateDate().isAfter(expectedStartFirstStageSubOrder)) {
                        expectedStartFirstStageSubOrder = customerFirstStagePayment.get().getCreateDate();
                    }
                }
                var expectedStartSecondStageSubOrder = expectedStartFirstStageSubOrder.plusDays(maximumDateAtSecondStage - maximumDateAtFirstStage);

                // Customer Second Stage Payment for Period of Stage 2 -> Complete Stage
                if (customerSecondStagePayment.isPresent()) {
                    if (customerSecondStagePayment.get().getCreateDate().isAfter(expectedStartSecondStageSubOrder)) {
                        expectedStartSecondStageSubOrder = customerSecondStagePayment.get().getCreateDate();
                    }
                }
                var expectedCompleteStageSubOrder = expectedStartSecondStageSubOrder.plusDays(maximumDateAtCompleteStage - maximumDateAtSecondStage);

                return OrderTimeLineResponse
                        .builder()
                        .estimatedDateStartDepositStage(dateTimeFormatter.format(expectedStartDepositSubOrder))
                        .estimatedQuantityFinishFirstStage(Utilities.roundToNearestHalf(subOrder.getQuantity() * 1.0 / 3))
                        .estimatedDateFinishFirstStage(dateTimeFormatter.format(expectedStartFirstStageSubOrder))
                        .estimatedQuantityFinishSecondStage(Utilities.roundToNearestHalf(subOrder.getQuantity() * 2.0 / 3))
                        .estimatedDateFinishSecondStage(dateTimeFormatter.format(expectedStartSecondStageSubOrder))
                        .estimatedQuantityFinishCompleteStage(subOrder.getQuantity())
                        .estimatedDateFinishCompleteStage(dateTimeFormatter.format(expectedCompleteStageSubOrder))
                        .build();
            }
        }
    }

    @Override
    public List<FullOrderResponse> getFullProp() throws JsonProcessingException {
        try {
            var listOrder = orderRepository.getAllParentOrder().stream()
//                    .filter(order -> order.getOrderStatus() == OrderStatus.CANCEL
//                            || order.getOrderStatus() == OrderStatus.DELIVERED)
                    .toList();

            List<FullOrderResponse> response = listOrder.stream()
                    .filter(fullOrderResponse -> fullOrderResponse.getPaymentList() != null
                            && !fullOrderResponse.getPaymentList().isEmpty())
                    .map(order -> {
                        try {
                            return orderMapper.mapToFullOrderResponse(order);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .sorted(Comparator.comparing(FullOrderResponse::getCreateDate).reversed())
                    .toList();

            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<FullOrderResponse> getFullPropByBrandID(String brandID) throws JsonProcessingException {
        try {
            var listOrder = orderRepository
                    .findAll()
                    .stream()
                    .filter(order -> order.getOrderType().equals("SUB_ORDER")
                            && (order.getOrderStatus() == OrderStatus.CANCEL
                            || order.getOrderStatus() == OrderStatus.COMPLETED)
                            && order.getDetailList() != null
                            && !order.getDetailList().isEmpty()
                            && order.getDetailList().get(0).getBrand() != null
                            && order.getDetailList().get(0).getBrand().getBrandID().equals(brandID)
                            && order.getPaymentList() != null
                            && !order.getPaymentList().isEmpty()
                    )
                    .toList();
            List<FullOrderResponse> response = new ArrayList<>();
            for (Order order : listOrder) {
                FullOrderResponse fullOrderResponse = orderMapper.mapToFullOrderResponse(order);
                response.add(fullOrderResponse);
            }
            return response.stream().sorted(Comparator.comparing(FullOrderResponse::getCreateDate).reversed()).toList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public OrderDetailShippingResponse getOrderDetailShippingResponseByLabelID(String labelID) {
        return ghtkShippingService.getOrderDetailShippingResponseByLabelID(labelID);
    }

    @Override
    public Optional<Order> getParentOrderByOrderIDAndUserID(String orderID, String userID) {
        return orderRepository.getParentOrderByOrderIDAndUserID(orderID, userID);
    }

    @Override
    public Optional<Order> getSubOrderByOrderIDAndBrandID(String orderID, String brandID) {
        return orderRepository.getSubOrderByOrderIDAndBrandID(orderID, brandID);
    }

    public void updatePayOS() throws Exception {
        logger.info("Inside Method updatePayOS");
        var paymentList = paymentService.getAllPayment();
        for (Payment payment : paymentList) {
            if (payment != null) {
                if (payment.getPaymentType() != null) {
                    PayOSResponse payOS = null;
                    if (payment.getPaymentType().equals(PaymentType.BRAND_INVOICE)) {
                        payOS = payOSService.getBrandPaymentInfo(payment.getPaymentCode());
                    } else {
                        if (payment.getPaymentType().equals(PaymentType.ORDER_REFUND)) {
                            payOS = payOSService.getRefundPaymentInfo(payment.getPaymentCode());
                        } else
                            payOS = payOSService.getPaymentInfo(payment.getPaymentCode());
                    }
                    if (payOS != null) {
                        payment.setPaymentStatus(payOS.getData().getStatus().equals("PAID"));
                        paymentService.updatePayment(payment);
                    }
                }
            }
        }
    }

    @Override
    public OrderStatusDetailResponse getAllOrderStatusDetailResponse() {
        var totalParentOrder = orderRepository.getAllParentOrder();

        long totalPreOrder = totalParentOrder.stream()
                .filter(order ->
                        order.getOrderStatus() == OrderStatus.NOT_VERIFY ||
                                order.getOrderStatus() == OrderStatus.PENDING
                )
                .count();

        long totalProcessingOrder = totalParentOrder.stream()
                .filter(order ->
                        order.getOrderStatus() == OrderStatus.DEPOSIT ||
                                order.getOrderStatus() == OrderStatus.PREPARING ||
                                order.getOrderStatus() == OrderStatus.PROCESSING ||
                                order.getOrderStatus() == OrderStatus.SUSPENDED ||
                                order.getOrderStatus() == OrderStatus.COMPLETED
                )
                .count();

        long totalFullyCompletedOrder = totalParentOrder.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.DELIVERED)
                .count();

        long totalCancelOrder = totalParentOrder.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.CANCEL)
                .count();

        List<Pair<String, Long>> orderStatusDetailList = List.of(
                Pair.of("Total Parent Order", (long) totalParentOrder.size()),
                Pair.of("Total Pre Order", totalPreOrder),
                Pair.of("Total Processing Order", totalProcessingOrder),
                Pair.of("Total Fully Completed Order", totalFullyCompletedOrder),
                Pair.of("Total Cancel Order", totalCancelOrder)
        );

        return OrderStatusDetailResponse.builder()
                .orderStatusDetailList(orderStatusDetailList)
                .build();
    }

    @Override
    public GrowthPercentageResponse calculateOrderGrowthPercentageForCurrentAndPreviousMonth() {
        var totalParentOrder = orderRepository.getAllParentOrder();

        LocalDateTime now = LocalDateTime.now();

        YearMonth currentMonth = YearMonth.from(now);
        LocalDateTime startOfCurrentMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfCurrentMonth = now;

        YearMonth previousMonth = currentMonth.minusMonths(1);
        LocalDateTime startOfPreviousMonth = previousMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfPreviousMonth = previousMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        long currentMonthOrderCount = totalParentOrder
                .stream()
                .filter(order -> {
                    LocalDateTime createDate = order.getCreateDate();
                    return !createDate.isBefore(startOfCurrentMonth) && !createDate.isAfter(endOfCurrentMonth);
                })
                .count();

        long previousMonthOrderCount = totalParentOrder
                .stream()
                .filter(order -> {
                    LocalDateTime createDate = order.getCreateDate();
                    return !createDate.isBefore(startOfPreviousMonth) && !createDate.isAfter(endOfPreviousMonth);
                })
                .count();

        if (previousMonthOrderCount == 0) {
            return GrowthPercentageResponse
                    .builder()
                    .currentData(currentMonthOrderCount)
                    .previousData(previousMonthOrderCount)
                    .growthPercentage(currentMonthOrderCount > 0 ? 100.0f : 0.0f)
                    .build();
        }

        float growthPercentage = ((float) (currentMonthOrderCount - previousMonthOrderCount) / previousMonthOrderCount) * 100.0f;

        var groundGrowthPercentage = BigDecimal
                .valueOf(growthPercentage)
                .setScale(1, RoundingMode.HALF_UP)
                .floatValue();

        return GrowthPercentageResponse
                .builder()
                .currentData(currentMonthOrderCount)
                .previousData(previousMonthOrderCount)
                .growthPercentage(groundGrowthPercentage)
                .build();
    }

    @Override
    public List<Pair<Object, Integer>> getAllTotalOrderOfEachBrand() {
        List<Pair<Object, Integer>> totalSubOrderDetails = new ArrayList<>();
        var brandList = brandService.getAllBrandInformation();

        for (var brandInformation : brandList) {
            var totalOrderForBrand = orderRepository.getOrderByBrandID(brandInformation.getBrandID()).size();
            totalSubOrderDetails.add(Pair.of(brandInformation.getBrandName(), totalOrderForBrand));
        }

        return totalSubOrderDetails;
    }

    @Override
    public SubOrderInvoice getSubOrderInvoiceBySubOrderID(String subOrderID) throws Exception {
        var orderCustomResponse = getOrderByOrderID(subOrderID);
        ;

        var subOrderQuantity = orderCustomResponse.getQuantity();
        var designDetails = detailRepository.getDesignDetailBySubOrderID(subOrderID);
        var brand = designDetails
                .stream()
                .map(DesignDetail::getBrand)
                .findFirst()
                .orElse(null);

        var brandLaborQuantity = brandLaborQuantityService.findLaborQuantityByBrandIDAndBrandQuantity(
                brand.getBrandID(),
                subOrderQuantity
        ).getLaborCostPerQuantity();

        var designResponse = orderCustomResponse.getDesignResponse();
        List<BrandMaterialResponse> brandMaterialResponseList = new ArrayList<>();
        for (var materialDetail : designResponse.getMaterialDetail()) {
            brandMaterialResponseList.add(
                    brandMaterialService.getBrandMaterialResponseByBrandIDAndMaterialID(
                            brand.getBrandID(),
                            materialDetail.getMaterialResponse().getMaterialID())
            );
        }

        return SubOrderInvoice
                .builder()
                .orderCustomResponse(orderCustomResponse)
                .brandMaterialResponseList(brandMaterialResponseList)
                .brandLaborQuantity(brandLaborQuantity)
                .build();
    }
}
