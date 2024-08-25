package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.*;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.mapper.DesignDetailMapper;
import com.smart.tailor.mapper.DesignMapper;
import com.smart.tailor.mapper.OrderMapper;
import com.smart.tailor.repository.DesignDetailRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.*;
import com.smart.tailor.utils.response.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@Service
@RequiredArgsConstructor
public class DesignDetailServiceImpl implements DesignDetailService {
    private final DesignDetailRepository designDetailRepository;
    private final DesignDetailMapper designDetailMapper;
    private final DesignMapper designMapper;
    private final OrderMapper orderMapper;
    private final PartOfDesignService partOfDesignService;
    private final BrandService brandService;
    private final BrandMaterialService brandMaterialService;
    private final SizeExpertTailoringService sizeExpertTailoringService;
    private final SystemPropertiesService systemPropertiesService;
    private final GHTKShippingService ghtkShippingService;
    private final DesignService designService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final SizeService sizeService;
    private final JwtService jwtService;
    private final BrandLaborQuantityService brandLaborQuantityService;
    private final Logger logger = LoggerFactory.getLogger(DesignDetailServiceImpl.class);

    @Transactional(readOnly = true)
    @Override
    public DesignDetailCustomResponse findAllByOrderID(String orderID) {
        try {
            if (orderID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }

            DesignDetailCustomResponse responseList = new DesignDetailCustomResponse();
//            List<DesignDetailResponse> detailList = null;
//            if (!designDetailList.isEmpty()) {
//                for (DesignDetail detail : designDetailList) {
//                    if (detailList == null) {
//                        detailList = new ArrayList<>();
//                    }
//                    detailList.add(designDetailMapper.mapperToDesignDetailResponse(detail));
//                }
//            }
            var designID = designService.getDesignObjectByOrderID(orderID).getDesignID();
            var designResponse = designService.getDesignResponseByID(designID);
            responseList.setDesign(designResponse);
            var order = orderService.getOrderById(orderID).get();
            responseList.setOrder(orderMapper.mapToOrderResponse(order));
            List<DesignDetail> detailList = designDetailRepository.findAllByOrderID(orderID);
            if (detailList != null) {
                responseList.setDesignDetail(detailList.stream().map(designDetailMapper::mapperToDesignDetailResponse).toList());
            }
            return responseList;
        } catch (Exception ex) {
            logger.error("ERROR IN DESIGN DETAIL SERVICE: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public DesignDetailResponse findByID(String orderID) {
        return designDetailMapper.mapperToDesignDetailResponse(
                designDetailRepository.getDesignDetailByDesignDetailID(orderID).get()
        );
    }

    //    @Transactional(readOnly = true)
    @Override
    public APIResponse createDesignDetail(String jwtToken, DesignDetailRequest designDetailRequest) {
        try {
            if (designDetailRequest == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }

            if (designDetailRequest.getDesignId() == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": designId");
            }
            Design baseDesign = designService.getDesignByID(designDetailRequest.getDesignId());

            var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
            if(!baseDesign.getUser().getUserID().equals(userIDFromJwtToken)){
                throw new UnauthorizedAccessException("You are not authorized to access this resource.");
            }

//            Design clone = new Design();
//            clone.setUser(baseDesign.getUser());
//            clone.setExpertTailoring(baseDesign.getExpertTailoring());
//            clone.setTitleDesign(baseDesign.getTitleDesign());
//            clone.setPublicStatus(false);
//            clone.setMinWeight(baseDesign.getMinWeight());
//            clone.setMaxWeight(baseDesign.getMaxWeight());
//            clone.setImageUrl(baseDesign.getImageUrl());
//            clone.setColor(baseDesign.getColor());
//
//            var basePartOfDesign = partOfDesignService.getListPartOfDesignObjectByDesignID(designId);
//
//            List<PartOfDesignRequest> clonePart = new ArrayList<>();
//            for (var partOfDesign : basePartOfDesign) {
//                var itemMaskList = partOfDesign
//                        .getItemMaskList()
//                        .stream()
//                        .filter(ItemMask::getStatus)
//                        .toList();
//
//                List<ItemMaskRequest> cloneItemMark = new ArrayList<>();
//
//                for (var itemMask : itemMaskList) {
//                    cloneItemMark.add(
//                            ItemMaskRequest
//                                    .builder()
//                                    .itemMaskName(itemMask.getItemMaskName())
//                                    .typeOfItem(itemMask.getTypeOfItem())
//                                    .materialID(itemMask.getMaterial().getMaterialID())
//                                    .isSystemItem(itemMask.getIsSystemItem())
//                                    .positionX(itemMask.getPositionX())
//                                    .positionY(itemMask.getPositionY())
//                                    .scaleX(itemMask.getScaleX())
//                                    .scaleY(itemMask.getScaleY())
//                                    .rotate(itemMask.getRotate())
//                                    .topLeftRadius(itemMask.getTopLeftRadius())
//                                    .topRightRadius(itemMask.getTopRightRadius())
//                                    .bottomLeftRadius(itemMask.getBottomLeftRadius())
//                                    .bottomRightRadius(itemMask.getBottomRightRadius())
//                                    .indexZ(itemMask.getIndexZ())
//                                    .imageUrl(Utilities.decodeBase64ToString(itemMask.getImageUrl()))
//                                    .printType(itemMask.getPrintType().name())
//                                    .build()
//                    );
//                }
//
//                clonePart.add(
//                        PartOfDesignRequest
//                                .builder()
//                                .partOfDesignName(partOfDesign.getPartOfDesignName())
//                                .imageUrl(Utilities.decodeBase64ToString(partOfDesign.getImageUrl()))
//                                .materialID(partOfDesign.getMaterial().getMaterialID())
//                                .successImageUrl(Utilities.decodeBase64ToString(partOfDesign.getSuccessImageUrl()))
//                                .realPartImageUrl(Utilities.decodeBase64ToString(partOfDesign.getRealPartImageUrl()))
//                                .width(partOfDesign.getWidth())
//                                .height(partOfDesign.getHeight())
//                                .itemMask(cloneItemMark)
//                                .build()
//                );
//            }
//
//            var design = designService.saveDesign(clone);
//            if (design == null) {
//                throw new BadRequestException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN + " with id: " + designId);
//            }
//            var newPart = partOfDesignService.createPartOfDesign(design, clonePart);
//            design.setPartOfDesignList(newPart);
//
//            designService.saveDesign(design);
//
//            designId = design.getDesignID();
//            logger.error("DESIGN ID: {}", designId);

            Order parentOrder = null;
            Design design = designService.createCloneDesignFromBaseDesign(designDetailRequest.getDesignId());
            CustomerResponse customerResponse = customerService.getCustomerByUserID(design.getUser().getUserID());
            String address = "";
            String province = "";
            String district = "";
            String ward = "";
            if (Utilities.isNonNullOrEmpty(designDetailRequest.getAddress())
                    && Utilities.isNonNullOrEmpty(designDetailRequest.getProvince())
                    && Utilities.isNonNullOrEmpty(designDetailRequest.getDistrict())
                    && Utilities.isNonNullOrEmpty(designDetailRequest.getWard())) {
                address = designDetailRequest.getAddress();
                province = designDetailRequest.getProvince();
                district = designDetailRequest.getDistrict();
                ward = designDetailRequest.getWard();
            } else {
                address = customerResponse.getAddress();
                province = customerResponse.getProvince();
                district = customerResponse.getDistrict();
                ward = customerResponse.getWard();
            }

            String phone = "";
            if (!Utilities.isNonNullOrEmpty(designDetailRequest.getPhone())) {
                phone = customerResponse.getPhoneNumber();
            } else if (!Utilities.isValidVietnamesePhoneNumber(designDetailRequest.getPhone())) {
                phone = customerResponse.getPhoneNumber();
            } else {
                phone = designDetailRequest.getPhone();
            }

            String buyerName;
            if (Utilities.isNonNullOrEmpty(designDetailRequest.getBuyerName())) {
                buyerName = designDetailRequest.getBuyerName();
            } else {
                buyerName = customerResponse.getFullName();
            }
            List<DesignDetailSize> sizeList = designDetailRequest.getSizeList();
            int index = -1;
            int totalQuantity = 0;
            for (DesignDetailSize sizeRequest : sizeList) {
                index++;
                if (sizeRequest.getSizeID() == null) {
                    throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + " at [" + index + "]: sizeID");
                }
                var size = sizeService.findByID(sizeRequest.getSizeID())
                        .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_SIZE));

                if (sizeRequest.getQuantity() == null) {
                    throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + " at [" + index + "]: quantity");
                }
                Integer quantity = sizeRequest.getQuantity();
                if (!Utilities.isValidNumber(quantity.toString()) || quantity <= 0) {
                    throw new BadRequestException(MessageConstant.INVALID_INPUT + " at [" + index + "]: quantity");
                }
                totalQuantity += quantity;
            }
            /**
             * Create Parent Order
             */
            logger.info("Line 245 Inside code DesignDetail");
            OrderResponse parentOrderResponse = orderService.createOrder(
                    OrderRequest
                            .builder()
                            .designID(design.getDesignID())
                            .quantity(totalQuantity)
                            .parentOrderID(null)
                            .orderType("PARENT_ORDER")
                            .address(address)
                            .province(province)
                            .district(district)
                            .ward(ward)
                            .phone(phone)
                            .buyerName(buyerName)
                            .orderStatus(OrderStatus.NOT_VERIFY)
                            .build()
            );
            logger.info("CREATE NEW ORDER SUCCESSFULLY!: {}", parentOrderResponse);
            parentOrder = orderService.getOrderById(parentOrderResponse.getOrderID()).get();

            List<DesignDetail> designDetailList = new ArrayList<>();
            for (DesignDetailSize sizeRequest : sizeList) {
                index++;
                if (sizeRequest.getSizeID() == null) {
                    throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + " at [" + index + "]: sizeID");
                }
                var size = sizeService.findByID(sizeRequest.getSizeID())
                        .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_SIZE));

                if (sizeRequest.getQuantity() == null) {
                    throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + " at [" + index + "]: quantity");
                }
                Integer quantity = sizeRequest.getQuantity();
                if (!Utilities.isValidNumber(quantity.toString()) || quantity <= 0) {
                    throw new BadRequestException(MessageConstant.INVALID_INPUT + " at [" + index + "]: quantity");
                }

                Brand existedBrand = null;

//                parentOrder.setQuantity(parentOrder.getQuantity() + quantity);
//                orderService.updateOrder(parentOrder);

                designDetailList.add(
                        DesignDetail
                                .builder()
                                .design(design)
                                .brand(null)
                                .order(parentOrder)
                                .size(size)
                                .quantity(quantity)
                                .detailStatus(false)
                                .build()
                );
            }
            designDetailRepository.saveAll(designDetailList);

            return APIResponse
                    .builder()
                    .status(HttpStatus.OK.value())
                    .message(MessageConstant.ADD_NEW_DESIGN_DETAIL_SUCCESSFULLY)
                    .data(
                            OrderDetailResponse.builder()
                                    .sizeList(sizeList)
                                    .orderID(parentOrder.getOrderID())
                                    .employeeID(parentOrder.getEmployee().getEmployeeID())
                                    .build()
                    )
                    .build();

        } catch (
                Exception ex) {
            logger.error("ERROR IN DESIGN DETAIL SERVICE: {}", ex.getMessage());
        }
        return null;
    }

    @Override
    public DesignDetailResponse updateDesignDetail(DesignDetail designDetail) {
        return designDetailMapper.mapperToDesignDetailResponse(designDetailRepository.save(designDetail));
    }

    @Override
    public DesignDetail updateDetailByID(DesignDetail designDetail) {
        return designDetailRepository.save(designDetail);
    }

    @Override
    public Optional<DesignDetail> getDesignDetailObjectByID(String detailID) {
        return designDetailRepository.findById(detailID);
    }

    @Override
    public DesignDetail getDetailOfOrderBaseOnBrandID(String orderID, String brandID) {
        return designDetailRepository.getDetailOfOrderBaseOnBrandID(orderID, brandID);
    }

    @Override
    public List<DesignDetail> getDesignDetailBySubOrderID(String subOrderID) {
        return designDetailRepository.getDesignDetailBySubOrderID(subOrderID);
    }

//    @Override
//    public OrderDetailPriceResponse calculateTotalPriceForSpecificOrder(String parentOrderID) throws Exception {
//        var orderCustomResponse = orderService.getOrderByOrderID(parentOrderID);
//        var listSubOrders = orderService.getSubOrderByParentID(parentOrderID);
//        var designResponse = orderCustomResponse.getDesignResponse();
//        var expertTailoring = designResponse.getExpertTailoring();
//        List<PartOfDesignInformation> partOfDesignInformationList = new ArrayList<>();
//        List<ItemMaskInformation> itemMaskInformationList = new ArrayList<>();
//
//        designResponse.getPartOfDesign().forEach(partOfDesignResponse -> {
//            partOfDesignInformationList.add(PartOfDesignInformation
//                    .builder()
//                    .width(partOfDesignResponse.getWidth())
//                    .height(partOfDesignResponse.getHeight())
//                    .materialID(partOfDesignResponse.getMaterial().getMaterialID())
//                    .materialName(partOfDesignResponse.getMaterial().getMaterialName())
//                    .build());
//            partOfDesignResponse.getItemMasks()
//                    .stream()
//                    .filter(ItemMaskResponse::getStatus)
//                    .forEach(itemMaskResponse -> {
//                        itemMaskInformationList.add(ItemMaskInformation
//                                .builder()
//                                .scaleX(itemMaskResponse.getScaleX())
//                                .scaleY(itemMaskResponse.getScaleY())
//                                .materialID(itemMaskResponse.getMaterial().getMaterialID())
//                                .materialName(itemMaskResponse.getMaterial().getMaterialName())
//                                .build());
//                    });
//        });
//
//        // Get Min Weight and Max Weight of each Design
//        var minWeightParentOrder = designResponse.getMinWeight() * orderCustomResponse.getQuantity();
//        var maxWeightParentOrder = designResponse.getMaxWeight() * orderCustomResponse.getQuantity();
//        var averageWeightParentOrder = (float) (minWeightParentOrder + maxWeightParentOrder) / 2;
//        var maximumShippingWeight = Integer.parseInt(systemPropertiesService.getByName("MAX_SHIPPING_WEIGHT").getPropertyValue());
//
//        BigDecimal shippingFee = BigDecimal.valueOf(-1);
//
//        // Calculate Shipping fee based on Address and Weight of Customer
//        if (orderCustomResponse.getAddress() != null && orderCustomResponse.getProvince() != null &&
//                orderCustomResponse.getDistrict() != null && orderCustomResponse.getWard() != null && averageWeightParentOrder < maximumShippingWeight) {
//
//            OrderShippingRequest.OrderShippingDetailRequest orderShippingDetailRequest =
//                    new OrderShippingRequest.OrderShippingDetailRequest(
//                            "344 Lê Văn Việt",
//                            "Hồ Chí Minh",
//                            "Thủ Đức",
//                            "Tăng Nhơn Phú B",
//                            orderCustomResponse.getAddress(),
//                            orderCustomResponse.getProvince(),
//                            orderCustomResponse.getDistrict(),
//                            orderCustomResponse.getWard(),
//                            averageWeightParentOrder
//                    );
//
//            var orderShippingRequest =
//                    OrderShippingRequest
//                            .builder()
//                            .order(orderShippingDetailRequest)
//                            .build();
//
//            var feeResponse = ghtkShippingService.calculateShippingFee(orderShippingRequest);
//            if (feeResponse != null && feeResponse.getSuccess()) {
//                shippingFee = Utilities.roundToNearestThousand(BigDecimal.valueOf(feeResponse.getFee()));
//            }
//        }
//
//        BigDecimal totalPriceOfParentOrder = BigDecimal.ZERO;
//        var divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());
//        var maxQuantity = Integer.MIN_VALUE;
//        var minQuantity = Integer.MAX_VALUE;
//        List<BigDecimal> subOrderIncludeTwoStage = new ArrayList<>();
//        List<List<BigDecimal>> subOrderIncludeFourStage = new ArrayList<>();
//
//        List<BrandDetailPriceResponse> brandDetailPriceResponseList = new ArrayList<>();
//        for (var subOrder : listSubOrders) {
//            List<DesignDetail> designDetailList = getDesignDetailBySubOrderID(subOrder.getOrderID());
//
//            int totalQuantityOfSubOrder = designDetailList
//                    .stream()
//                    .mapToInt(DesignDetail::getQuantity)
//                    .sum();
//
//            BigDecimal totalPriceOfEachSubOrder = BigDecimal.ZERO;
//            maxQuantity = Math.max(maxQuantity, totalQuantityOfSubOrder);
//            minQuantity = Math.min(minQuantity, totalQuantityOfSubOrder);
//
//            Brand brand = null;
//            for (DesignDetail designDetail : designDetailList) {
//                brand = designDetail.getBrand();
//                var designDetailQuantity = BigDecimal.valueOf(designDetail.getQuantity());
//                var size = designDetail.getSize();
//
//                var sizeExpertTailoring = sizeExpertTailoringService.findSizeExpertTailoringByExpertTailoringIDAndSizeID(
//                        expertTailoring.getExpertTailoringID(),
//                        size.getSizeID()
//                );
//
//                var ratio = BigDecimal.valueOf(sizeExpertTailoring.getRatio());
//
//                // Calculate Total Price Of PartOfDesign of SubOrder
//                BigDecimal totalPricePartOfDesignOfSubOrder = BigDecimal.ZERO;
//                for (PartOfDesignInformation partOfDesignInformation : partOfDesignInformationList) {
//                    totalPricePartOfDesignOfSubOrder = totalPricePartOfDesignOfSubOrder.add(calculatePartOfDesignByBrandMaterial(partOfDesignInformation, brand.getBrandID(), ratio));
//                }
//
//                // Calculate Total Price Of ItemMask of SubOrder
//                BigDecimal totalPriceItemMaskOfSubOrder = BigDecimal.ZERO;
//                for (ItemMaskInformation itemMaskInformation : itemMaskInformationList) {
//                    totalPriceItemMaskOfSubOrder = totalPriceItemMaskOfSubOrder.add(calculateItemMaskByBrandMaterial(itemMaskInformation, brand.getBrandID()));
//                }
//
//                // Get Labor Quantity of Each Brand for SubOrder
//                var brandLaborQuantityOfSubOrder = brandLaborQuantityService.findLaborQuantityByBrandIDAndBrandQuantity(brand.getBrandID(), totalQuantityOfSubOrder);
//                BigDecimal brandLaborCostPerQuantity = BigDecimal.valueOf(brandLaborQuantityOfSubOrder.getLaborCostPerQuantity());
//
//                totalPriceOfEachSubOrder = totalPriceOfEachSubOrder.add(totalPricePartOfDesignOfSubOrder.add(totalPriceItemMaskOfSubOrder).add(brandLaborCostPerQuantity).multiply(designDetailQuantity));
//            }
//            totalPriceOfEachSubOrder = Utilities.roundToNearestThousand(totalPriceOfEachSubOrder);
//            BigDecimal brandDepositStage = BigDecimal.valueOf(-1);
//            BigDecimal brandFirstStage = BigDecimal.valueOf(-1);
//            BigDecimal brandSecondStage = BigDecimal.valueOf(-1);
//            if (maxQuantity < divideNumber) {
//                brandDepositStage = Utilities.roundToNearestThousand(totalPriceOfEachSubOrder);
//                subOrderIncludeTwoStage.add(brandDepositStage);
//            } else {
//                brandDepositStage = totalPriceOfEachSubOrder.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP);
//                brandFirstStage = totalPriceOfEachSubOrder.subtract(brandDepositStage).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP);
//                brandSecondStage = totalPriceOfEachSubOrder.subtract(brandDepositStage).subtract(brandFirstStage).setScale(0, RoundingMode.HALF_UP);
//
//                // Round Brand Price at Deposit, First, Second Stage to nearest Thousand
//                brandDepositStage = Utilities.roundToNearestThousand(brandDepositStage);
//                brandFirstStage = Utilities.roundToNearestThousand(brandFirstStage);
//                brandSecondStage = Utilities.roundToNearestThousand(brandSecondStage);
//
//                // Update Total Price Of SubOrder with new Nearest Stage of Deposit, First, Second Stage
//                totalPriceOfEachSubOrder = brandDepositStage.add(brandFirstStage).add(brandSecondStage);
//
//                List<BigDecimal> fourStage = new ArrayList<>();
//                fourStage.add(brandDepositStage);
//                fourStage.add(brandFirstStage);
//                fourStage.add(brandSecondStage);
//                subOrderIncludeFourStage.add(fourStage);
//            }
//
//            BrandDetailPriceResponse brandDetailPriceResponse = BrandDetailPriceResponse
//                    .builder()
//                    .brandID(brand.getBrandID())
//                    .subOrderID(subOrder.getOrderID())
//                    .brandPriceDeposit(brandDepositStage.toString())
//                    .brandPriceFirstStage(brandFirstStage.toString())
//                    .brandPriceSecondStage(brandSecondStage.toString())
//                    .build();
//            brandDetailPriceResponseList.add(brandDetailPriceResponse);
//
//            // Add Total Price Of SubOrder to ParentOrder
//            totalPriceOfParentOrder = totalPriceOfParentOrder.add(totalPriceOfEachSubOrder);
//        }
//
//        // Get Order Fee Percentage to calculate Commission for Each Order
//        var orderFeePercentage = Integer.parseInt(systemPropertiesService.getByName("ORDER_FEE_PERCENTAGE").getPropertyValue());
//
//        // Calculate Commission by divide 100
//        BigDecimal commission = totalPriceOfParentOrder.multiply(BigDecimal.valueOf(orderFeePercentage).divide(BigDecimal.valueOf(100)));
//        commission = Utilities.roundToNearestThousand(commission);
//
//        BigDecimal customerDepositStage = BigDecimal.valueOf(-1);
//        BigDecimal customerFirstStage = BigDecimal.valueOf(-1);
//        BigDecimal customerSecondStage = BigDecimal.valueOf(-1);
//
//        // Case all subOrder have only two stage.
//        // CustomerDepositStage = sum of All Deposit Stage of SubOrder
//        if (minQuantity < divideNumber && maxQuantity < divideNumber) {
//            customerDepositStage = subOrderIncludeTwoStage
//                    .stream()
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            // Case at least one SubOrder has two stage and one SubOrder has four stage
//            // CustomerDepositStage = sum of All Deposit Two Stage of SubOrder and all of 1/3 Deposit Four Stage of SubOrder
//            // CustomerFirstStage = CustomerSecondStage = sum all  1/3 Deposit Four Stage of SubOrder
//        } else if (minQuantity < divideNumber && maxQuantity >= divideNumber) {
//            customerDepositStage = subOrderIncludeTwoStage
//                    .stream()
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            customerFirstStage = BigDecimal.ZERO;
//            customerSecondStage = BigDecimal.ZERO;
//            for (List<BigDecimal> stage : subOrderIncludeFourStage) {
//                customerDepositStage = customerDepositStage.add(stage.get(0));
//                customerFirstStage = customerFirstStage.add(stage.get(1));
//                customerSecondStage = customerSecondStage.add(stage.get(2));
//            }
//
//            // Case all subOrder have 4 stage
//            // CustomerDepositStage = CustomerFirstStage = CustomerSecondStage = 1/3 Total Price
//        } else if (minQuantity >= divideNumber && maxQuantity >= divideNumber) {
//            customerDepositStage = totalPriceOfParentOrder.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP);
//            customerFirstStage = totalPriceOfParentOrder.subtract(customerDepositStage).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP);
//            customerSecondStage = totalPriceOfParentOrder.subtract(customerDepositStage).subtract(customerFirstStage).setScale(0, RoundingMode.HALF_UP);
//
//            // Round Brand Price at Deposit, First, Second Stage to nearest Thousand
//            customerDepositStage = Utilities.roundToNearestThousand(customerDepositStage);
//            customerFirstStage = Utilities.roundToNearestThousand(customerFirstStage);
//            customerSecondStage = Utilities.roundToNearestThousand(customerSecondStage);
//
//            // Update Total Price Of SubOrder with new Nearest Stage of Deposit, First, Second Stage
//            totalPriceOfParentOrder = customerDepositStage.add(customerFirstStage).add(customerSecondStage);
//        }
//
//        BigDecimal customerPriceDeposit = customerDepositStage.add(commission);
//        BigDecimal adjustedTotalPriceOfParentOrder = totalPriceOfParentOrder.add(commission);
//
//        return OrderDetailPriceResponse
//                .builder()
//                .totalPriceOfParentOrder(adjustedTotalPriceOfParentOrder.toString())
//                .customerCommissionFee(commission.toString())
//                .customerPriceDeposit(customerPriceDeposit.toString())
//                .customerPriceFirstStage(customerFirstStage.toString())
//                .customerSecondStage(customerSecondStage.toString())
//                .customerShippingFee(shippingFee.toString())
//                .brandDetailPriceResponseList(brandDetailPriceResponseList)
//                .build();
//    }
//
//
//    private BigDecimal calculatePartOfDesignByBrandMaterial(PartOfDesignInformation partInfo, String brandID, BigDecimal ratio) {
//        BigDecimal width = BigDecimal.valueOf(partInfo.getWidth()).multiply(ratio); // in Centimeter
//        BigDecimal height = BigDecimal.valueOf(partInfo.getHeight()).multiply(ratio); // in Centimeter
//
//        String materialID = partInfo.getMaterialID();
//        BigDecimal brandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getBrandPriceByBrandIDAndMaterialID(brandID, materialID));
//
//        // Correct calculation
//        BigDecimal area = width.multiply(height).divide(BigDecimal.valueOf(10000), 10, RoundingMode.HALF_UP); // Keeping 10 decimal places for precision
//        BigDecimal price = area.multiply(brandPriceMaterial).setScale(0, RoundingMode.CEILING); // Rounding up to nearest whole number
//
//        return price;
//    }
//
//    private BigDecimal calculateItemMaskByBrandMaterial(ItemMaskInformation itemMaskInfo, String brandID) {
//        // Convert scales from pixels to centimeters
//        BigDecimal scaleX_Centimeter = BigDecimal.valueOf(Math.abs(itemMaskInfo.getScaleX())).multiply(PIXEL_TO_CENTIMETER);
//        BigDecimal scaleY_Centimeter = BigDecimal.valueOf(Math.abs(itemMaskInfo.getScaleY())).multiply(PIXEL_TO_CENTIMETER);
//
//        String materialID = itemMaskInfo.getMaterialID();
//        BigDecimal brandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getBrandPriceByBrandIDAndMaterialID(brandID, materialID));
//
//        // Calculate area in square meters
//        BigDecimal area = scaleX_Centimeter.multiply(scaleY_Centimeter).divide(BigDecimal.valueOf(10000), 10, RoundingMode.HALF_UP); // Rounding to 10 decimal places
//
//        // Calculate price, rounding up to the nearest whole number
//        BigDecimal price = area.multiply(brandPriceMaterial).setScale(0, RoundingMode.CEILING);
//
//        return price;
//    }

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

    public OrderDetailPriceResponse calculateTotalPriceForSpecificOrder(String parentOrderID) throws Exception {
        var orderCustomResponse = orderService.getOrderByOrderID(parentOrderID);
        var listSubOrders = orderService.getSubOrderByParentID(parentOrderID);
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
            List<DesignDetail> designDetailList = getDesignDetailBySubOrderID(subOrder.getOrderID());

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
        }
        List<DesignMaterialDetailResponse> designMaterialDetailResponseList = new ArrayList<>();
        for(DesignMaterialDetailResponse response : designMaterialDetailResponseMap.values()){
            designMaterialDetailResponseList.add(
                    DesignMaterialDetailResponse
                            .builder()
                            .detailName(
                                    !response.getDetailName().isEmpty() ? response.getDetailName().toString() : null
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
        if(response != null){
            response.setMinPriceMaterial(((BigDecimal)response.getMinPriceMaterial()).min(price));
            response.setMaxPriceMaterial(((BigDecimal)response.getMaxPriceMaterial()).max(price));
            if(area != null){
                response.setMinMeterSquare(((BigDecimal)response.getMinMeterSquare()).min(area));
                response.setMaxMeterSquare(((BigDecimal)response.getMaxMeterSquare()).max(area));
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
}
