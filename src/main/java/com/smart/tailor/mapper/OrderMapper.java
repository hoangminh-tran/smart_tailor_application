package com.smart.tailor.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.tailor.entities.Order;
import com.smart.tailor.repository.OrderRepository;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.service.PaymentService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.response.FullOrderResponse;
import com.smart.tailor.utils.response.OrderCustomResponse;
import com.smart.tailor.utils.response.OrderResponse;
import com.smart.tailor.utils.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

public interface OrderMapper {
    OrderResponse mapToOrderResponse(Order order) throws JsonProcessingException;

    OrderCustomResponse mapToOrderCustomResponse(Order order) throws JsonProcessingException;

    FullOrderResponse mapToFullOrderResponse(Order order) throws JsonProcessingException;
}

@Component
@RequiredArgsConstructor
class OrderMapperImpl implements OrderMapper {
    private final DesignService designService;
    private final DesignDetailMapper detailMapper;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final BrandMapper brandMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderMapperImpl.class);

    @Transactional(readOnly = true)
    @Override
    public OrderResponse mapToOrderResponse(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder()
                .parentOrderID(order.getParentOrder() != null ? order.getParentOrder().getOrderID() : null)
                .orderType(order.getOrderType())
                .orderID(order.getOrderID())
                .quantity(order.getQuantity())
                .orderStatus(order.getOrderStatus())
                .address(order.getAddress())
                .province(order.getProvince())
                .district(order.getDistrict())
                .rating(order.getRating())
                .labelID(order.getLabelID())
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
                .lastModifiedDate(order.getLastModifiedDate() != null ? Utilities.convertLocalDateTimeToString(order.getLastModifiedDate()) : null)
                .detailList(order.getDetailList() != null ?
                        order.getDetailList().stream()
                                .map(detailMapper::mapperToDesignDetailResponse)
                                .toList() : null);

        try {
            List<PaymentResponse> paymentResponseList = paymentService.findAllByOrderID(order.getOrderID())
                    .stream()
                    .map(paymentMapper::mapperToPaymentResponse)
                    .sorted(Comparator.comparing(PaymentResponse::getCreateDate).reversed())
                    .toList();

            if (!paymentResponseList.isEmpty()) {
                orderResponse.paymentList(paymentResponseList);
            }
        } catch (Exception ex) {
            logger.error("Lỗi khi ánh xạ thanh toán cho đơn hàng ID: " + order.getOrderID(), ex);
            throw ex;
        }
//        try {
//            List<PaymentResponse> paymentResponseList = paymentService.findAllByOrderID(order.getOrderID())
//                    .stream()
//                    .map(paymentMapper::mapperToPaymentResponse)
//                    .toList();
//
//            if (!paymentResponseList.isEmpty()) {
//                orderResponse.paymentList(paymentResponseList);
//            }
//        } catch (Exception ex) {
//            logger.error("Lỗi khi ánh xạ thanh toán cho đơn hàng ID: " + order.getOrderID(), ex);
//            throw ex;
//        }
        try {
            if (order.getOrderType().equals("SUB_ORDER")) {
                if (order.getDetailList() != null && !order.getDetailList().isEmpty()) {
                    if (order.getDetailList().get(0) != null) {
                        if (order.getDetailList().get(0).getBrand() != null) {
                            var brand = order.getDetailList().get(0).getBrand();
                            if (brand != null) {
                                orderResponse.brand(brandMapper.mapperToBrandResponse(brand));
                            }
                        }
                    }
                }
            } else {
                orderResponse.brand(null);
            }
        } catch (Exception ex) {
            logger.error("Lỗi khi ánh xạ thanh toán cho đơn hàng ID: " + order.getOrderID(), ex);
            throw ex;
        }
        return orderResponse.build();
    }

    @Transactional(readOnly = true)
    @Override
    public OrderCustomResponse mapToOrderCustomResponse(Order order) {
        if (order == null) {
            return null;
        }

        OrderCustomResponse.OrderCustomResponseBuilder orderResponse = OrderCustomResponse.builder()
                .designResponse(designService.getDesignByOrderID(order.getOrderID()))
                .parentOrderID(order.getParentOrder() != null ? order.getParentOrder().getOrderID() : null)
                .orderType(order.getOrderType())
                .orderID(order.getOrderID())
                .quantity(order.getQuantity())
                .orderStatus(order.getOrderStatus())
                .address(order.getAddress())
                .province(order.getProvince())
                .district(order.getDistrict())
                .ward(order.getWard())
                .phone(order.getPhone())
                .labelID(order.getLabelID())
                .rating(order.getRating())
                .buyerName(order.getBuyerName())
                .totalPrice(order.getTotalPrice())
                .employeeID(order.getEmployee().getEmployeeID())
                .expectedStartDate(Utilities.convertLocalDateTimeToString(order.getExpectedStartDate()))
                .expectedProductCompletionDate(Utilities.convertLocalDateTimeToString(order.getExpectedProductCompletionDate()))
                .estimatedDeliveryDate(Utilities.convertLocalDateTimeToString(order.getEstimatedDeliveryDate()))
                .productionStartDate(Utilities.convertLocalDateTimeToString(order.getProductionStartDate()))
                .productionCompletionDate(Utilities.convertLocalDateTimeToString(order.getProductionCompletionDate()))
                .createDate(order.getCreateDate() != null ? Utilities.convertLocalDateTimeToString(order.getCreateDate()) : null)
                .lastModifiedDate(order.getLastModifiedDate() != null ? Utilities.convertLocalDateTimeToString(order.getLastModifiedDate()) : null)
                .detailList(order.getDetailList() != null ?
                        order.getDetailList().stream()
                                .map(detailMapper::mapperToDesignDetailResponse)
                                .toList() : null);

        try {
            List<PaymentResponse> paymentResponseList = paymentService.findAllByOrderID(order.getOrderID())
                    .stream()
                    .map(paymentMapper::mapperToPaymentResponse)
                    .sorted(Comparator.comparing(PaymentResponse::getCreateDate).reversed())
                    .toList();

            if (!paymentResponseList.isEmpty()) {
                orderResponse.paymentList(paymentResponseList);
            }
        } catch (Exception ex) {
            logger.error("Lỗi khi ánh xạ thanh toán cho đơn hàng ID: " + order.getOrderID(), ex);
            throw ex;
        }

        return orderResponse.build();
    }

    @Override
    public FullOrderResponse mapToFullOrderResponse(Order order) throws JsonProcessingException {
        if (order == null) {
            return null;
        }

        FullOrderResponse.FullOrderResponseBuilder orderResponse = FullOrderResponse.builder()
                .designResponse(designService.getDesignByOrderID(order.getOrderID()))
                .orderType(order.getOrderType())
                .orderID(order.getOrderID())
                .quantity(order.getQuantity())
                .orderStatus(order.getOrderStatus())
                .address(order.getAddress())
                .province(order.getProvince())
                .district(order.getDistrict())
                .ward(order.getWard())
                .labelID(order.getLabelID())
                .phone(order.getPhone())
                .rating(order.getRating())
                .buyerName(order.getBuyerName())
                .totalPrice(order.getTotalPrice())
                .employeeID(order.getEmployee().getEmployeeID())
                .expectedStartDate(Utilities.convertLocalDateTimeToString(order.getExpectedStartDate()))
                .expectedProductCompletionDate(Utilities.convertLocalDateTimeToString(order.getExpectedProductCompletionDate()))
                .estimatedDeliveryDate(Utilities.convertLocalDateTimeToString(order.getEstimatedDeliveryDate()))
                .productionStartDate(Utilities.convertLocalDateTimeToString(order.getProductionStartDate()))
                .productionCompletionDate(Utilities.convertLocalDateTimeToString(order.getProductionCompletionDate()))
                .createDate(order.getCreateDate() != null ? Utilities.convertLocalDateTimeToString(order.getCreateDate()) : null)
                .lastModifiedDate(order.getLastModifiedDate() != null ? Utilities.convertLocalDateTimeToString(order.getLastModifiedDate()) : null)
                .detailList(order.getDetailList() != null ?
                        order.getDetailList().stream()
                                .map(detailMapper::mapperToDesignDetailResponse)
                                .toList() : null);
        orderResponse.paymentStatus(false);
        var orderPayment = paymentService.findAllByOrderID(order.getOrderID());
        orderResponse.paymentList(
                orderPayment.stream()
                        .map(paymentMapper::mapperToPaymentResponse)
                        .sorted(Comparator.comparing(PaymentResponse::getCreateDate).reversed())
                        .toList()
        );
//        try {
//            List<PaymentResponse> paymentResponseList = Optional.ofNullable(order)
//                    .map(Order::getOrderID)
//                    .map(orderID -> paymentService.findAllByOrderID(orderID))
//                    .orElse(Collections.emptyList()) // Trả về danh sách rỗng nếu không có giá trị
//                    .stream()
//                    .map(paymentMapper::mapperToPaymentResponse)
//                    .toList();
//
//            if (!paymentResponseList.isEmpty()) {
//                orderResponse.paymentList(paymentResponseList);
//            }
//        } catch (Exception ex) {
//            logger.error("Lỗi khi ánh xạ thanh toán cho đơn hàng ID: " + order.getOrderID(), ex);
//            throw ex;
//        }
        try {
            if (!order.getOrderType().equals("SUB_ORDER")) {
                List<OrderResponse> subOrderList = orderRepository.findAll().stream()
                        .filter(o -> "SUB_ORDER".equals(o.getOrderType()) &&
                                o.getParentOrder() != null &&
                                o.getParentOrder().getOrderID().equals(order.getOrderID())
                        )
                        .map(this::mapToOrderResponse)
                        .sorted(Comparator.comparing(OrderResponse::getCreateDate).reversed())
                        .toList();

                orderResponse.subOrderList(subOrderList);

                boolean isFinish = subOrderList.stream()
                        .allMatch(subOrder -> {
                            var paymentList = subOrder.getPaymentList();
                            if (paymentList == null) {
                                return false;
                            }
                            return paymentList.stream()
                                    .allMatch(PaymentResponse::getPaymentStatus);
                        });
                orderResponse.paymentStatus(isFinish);
            } else {
                orderResponse.subOrderList(null);
            }
        } catch (Exception ex) {
            logger.error("Lỗi khi ánh xạ thanh toán cho đơn hàng ID: " + order.getOrderID(), ex);
            throw ex;
        }
        return orderResponse.build();
    }
}