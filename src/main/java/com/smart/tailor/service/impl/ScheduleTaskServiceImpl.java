package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Payment;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.enums.PaymentType;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.OrderShippingRequest;
import com.smart.tailor.utils.request.OrderStatusUpdateRequest;
import com.smart.tailor.utils.request.PaymentRequest;
import com.smart.tailor.utils.response.OrderResponse;
import com.smart.tailor.utils.response.PayOSResponse;
import com.smart.tailor.utils.response.PayOSResponseData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(ScheduleTaskServiceImpl.class);
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PayOSDataService payOSDataService;
    private final PayOSService payOSService;
    private final DesignDetailService detailService;
    private final SystemPropertiesService systemPropertiesService;
    private final GHTKShippingService ghtkShippingService;

    @Scheduled(cron = "0 0 * * * *") // second - minute - hour - dayOfMonth - month - dayOfWeek   // Run every hour
    @Override
    public void deleteUserWithEmailUnverifiedSchedule() {
        logger.info("Scheduled task is running at {}", LocalDateTime.now());
        var listUnverifiedUser = userService.findAllUnverifiedUser();
        var timeBeforeAccountDelete = Integer.parseInt(systemPropertiesService.getByName("TIME_BEFORE_ACCOUNT_DELETION").getPropertyValue());
        for (User user : listUnverifiedUser) {
            LocalDateTime userCreateDateTime = user.getCreateDate();
            // Config System Properties about Time Expire For Each UnVerified User
            // Using Default Expire Time
            LocalDateTime userExpiredDateTime = userCreateDateTime.plusHours(timeBeforeAccountDelete);
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (currentDateTime.isAfter(userExpiredDateTime)) {
                logger.info("Delete User {}", user);
                userService.deleteUnverifiedUser(user.getUserID());
            }
        }
    }

    @Scheduled(cron = "0 * * * * *") // Run every minute
    @Override
    @Transactional
    public void checkValidOrderAfterExpirationTimeOrder() throws Exception {
        logger.info("Inside Method checkValidOrderAfterExpirationTimeOrder");
        var orders = orderService.getAllParentOrder();
        for (OrderResponse orderResponse : orders) {
            var checkOrderExpireTime = orderService.isOrderExpireTime(orderResponse.getOrderID());
            var orderStatus = orderResponse.getOrderStatus();
            if (checkOrderExpireTime) {
                var status = orderService.isOrderCompletelyPicked(orderResponse.getOrderID());
                var order = orderService.getOrderById(orderResponse.getOrderID()).get();
                if (status) {
                    var detail = detailService.findAllByOrderID(orderResponse.getOrderID());
                    var design = detail.getDesign();
                    var sender = design.getUser();
                    var recipient = userService.getUserByEmail("accountantsmarttailor123@gmail.com");

//                    var paymentList = paymentService.findAllByOrderID(orderResponse.getOrderID());
//                    if (paymentList != null && !paymentList.isEmpty()) {
//                        for (Payment p : paymentList) {
//                            var orderCode = p.getPaymentCode();
//                            PayOSResponseData onlinePayOS;
//                            if (p.getPaymentType().equals(PaymentType.BRAND_INVOICE)) {
//                                onlinePayOS = payOSService.getBrandPaymentInfo(orderCode).getData();
//                            } else {
//                                if (p.getPaymentType().equals(PaymentType.ORDER_REFUND)) {
//                                    onlinePayOS = payOSService.getRefundPaymentInfo(orderCode).getData();
//                                } else {
//                                    onlinePayOS = payOSService.getPaymentInfo(orderCode).getData();
//                                }
//                            }
//                            var checkPayOSData = payOSDataService.findByOrderCode(orderCode);
//                            if (checkPayOSData.isPresent()) {
//                                var payOSData = checkPayOSData.get();
//                                payOSData.setStatus(onlinePayOS.getStatus());
//                                payOSDataService.save(payOSData);
//                                p.setPaymentStatus(payOSData.getStatus().equals("PAID"));
//                                paymentService.updatePayment(p);
//                            }
//                        }
//                    }
//                    logger.error("PAYMENT LIST: {}", paymentList);
                    switch (orderStatus) {
                        case PENDING -> {
                            logger.error("INCASE PENDING");
                            /**
                             * UPDATE STATUS TO DEPOSIT
                             */
                            logger.info("Change Status Start Order");
                            var price = detailService.calculateTotalPriceForSpecificOrder(orderResponse.getOrderID());
                            order.setTotalPrice(
                                    Integer.valueOf(price.getTotalPriceOfParentOrder())
                            );
                            orderService.updateOrder(order);
                            orderService.changeOrderStatus(
                                    OrderStatusUpdateRequest
                                            .builder()
                                            .orderID(order.getOrderID().toString())
                                            .status(OrderStatus.DEPOSIT.name())
                                            .build()
                            );
                            orderService.confirmOrder(order.getOrderID());
                            var payOSResponse = paymentService.createPayOSPayment(
                                    PaymentRequest
                                            .builder()
                                            .orderID(orderResponse.getOrderID())

                                            .paymentSenderID(sender.getUserID())
                                            .paymentSenderName(sender.getFullName())
                                            .paymentSenderBankCode("")
                                            .paymentSenderBankNumber("")

                                            .paymentRecipientID(recipient.getUserID())
                                            .paymentRecipientName(recipient.getFullName())
                                            .paymentRecipientBankCode("OCB")
                                            .paymentRecipientBankNumber("0163100007285002")

                                            .paymentType(PaymentType.DEPOSIT)
                                            .paymentAmount(Integer.valueOf(price.getCustomerPriceDeposit()))
                                            .itemList(null)
                                            .build()
                            );
                        }
                        case SUSPENDED -> {
                            logger.error("INCASE SUSPENDED");
                            /**
                             * UPDATE STATUS TO DEPOSIT
                             */
                            logger.info("Change Status PROCESSING Order");
                            orderService.confirmOrder(order.getOrderID());
                            orderService.changeOrderStatus(
                                    OrderStatusUpdateRequest
                                            .builder()
                                            .orderID(order.getOrderID().toString())
                                            .status(OrderStatus.PROCESSING.name())
                                            .build()
                            );
//                            orderService.confirmOrder(order.getOrderID());
                        }
                    }
                } else {
                    if (orderStatus.equals(OrderStatus.PROCESSING)) {
                        logger.info("Change Status PENDING Order");
                        orderService.changeOrderStatus(
                                OrderStatusUpdateRequest
                                        .builder()
                                        .orderID(order.getOrderID().toString())
                                        .status(OrderStatus.SUSPENDED.name())
                                        .build()
                        );
                    } else if (orderStatus.equals(OrderStatus.SUSPENDED)) {
                        logger.info("Continue Status SUSPENDED");
                    } else if (!orderStatus.equals(OrderStatus.CANCEL)) {
                        logger.info("Change Status Delete Order");
                        orderService.changeOrderStatus(
                                OrderStatusUpdateRequest
                                        .builder()
                                        .orderID(order.getOrderID().toString())
                                        .status(OrderStatus.CANCEL.name())
                                        .build()
                        );
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Run every minute
    @Override
    @Transactional
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

    @Scheduled(cron = "0/30 * * * * *") // Run every minute
    @Override
    public void createOrderDelivery() throws Exception {
        logger.info("Inside Create Order Delivery");
        var orders = orderService.getAllParentOrder();
        for (OrderResponse orderResponse : orders) {
            if(orderResponse.getOrderStatus().name().equals(OrderStatus.DELIVERED.name()) &&
                    Optional.ofNullable(orderResponse.getLabelID()).isEmpty()){
                var order = orderService.getOrderById(orderResponse.getOrderID()).get();
                logger.info("Retrieved Order: {}", order);

                var detail = detailService.findAllByOrderID(orderResponse.getOrderID());
                logger.info("Retrieved Order Details for OrderID {}: {}", orderResponse.getOrderID(), detail);

                var design = detail.getDesign();
                logger.info("Design Information: {}", design);

                var minWeightParentOrder = design.getMinWeight() * order.getQuantity();
                logger.info("Minimum Weight for Parent Order (Quantity {}): {}", order.getQuantity(), minWeightParentOrder);

                var maxWeightParentOrder = design.getMaxWeight() * order.getQuantity();
                logger.info("Maximum Weight for Parent Order (Quantity {}): {}", order.getQuantity(), maxWeightParentOrder);

                var averageWeightParentOrder = (float) (minWeightParentOrder + maxWeightParentOrder) / 2;
                logger.info("Average Weight for Parent Order: {}", averageWeightParentOrder);

                var maximumShippingWeight = Integer.parseInt(systemPropertiesService.getByName("MAX_SHIPPING_WEIGHT").getPropertyValue());
                if(averageWeightParentOrder < maximumShippingWeight){
                    OrderShippingRequest.OrderShippingDetailRequest orderShippingDetailRequest =
                            new OrderShippingRequest.OrderShippingDetailRequest(
                                     order.getOrderID(),
                                    "Smart Tailor Services",
                                    "344 Lê Văn Việt",
                                    "Hồ Chí Minh",
                                    "Thủ Đức",
                                    "Tăng Nhơn Phú B",
                                    "0926733445",
                                    order.getPhone(),
                                    order.getBuyerName(),
                                    order.getAddress(),
                                    order.getProvince(),
                                    order.getDistrict(),
                                    order.getWard(),
                                    "Khác",
                                    "1",
                                    "2024/10/08",
                                    0,
                                    averageWeightParentOrder,
                                    1
                            );

                    var orderShippingRequest =
                            OrderShippingRequest
                                    .builder()
                                    .order(orderShippingDetailRequest)
                                    .build();

                    var createShippingOrder = ghtkShippingService.createShippingOrder(orderShippingRequest);
                }
            }
        }
    }
}
