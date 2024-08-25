package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.OrderStage;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.mapper.OrderStageMapper;
import com.smart.tailor.repository.OrderRepository;
import com.smart.tailor.repository.OrderStageRepository;
import com.smart.tailor.service.OrderStageService;
import com.smart.tailor.utils.request.OrderStageRequest;
import com.smart.tailor.utils.response.OrderStageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class OrderStageServiceImpl implements OrderStageService {
    private final OrderStageRepository orderStageRepository;
    private final OrderRepository orderRepository;
    private final OrderStageMapper orderStageMapper;

    @Override
    public OrderStageResponse createOrderStage(OrderStageRequest orderStageRequest) {
        try {
            if (orderStageRequest == null) {
                throw new RuntimeException(MessageConstant.MISSING_ARGUMENT);
            }

            String orderID = orderStageRequest.getOrderID() != null ? orderStageRequest.getOrderID() : null;
            if (orderID == null) {
                throw new RuntimeException(MessageConstant.MISSING_ARGUMENT);
            }
            var checkOrder = orderRepository.findById(orderID);
            if (checkOrder.isEmpty()) {
                throw new RuntimeException(MessageConstant.MISSING_ARGUMENT);
            }
            var order = checkOrder.get();

            OrderStatus stage = orderStageRequest.getStage() != null ? orderStageRequest.getStage() : null;
            if (stage == null) {
                throw new RuntimeException(MessageConstant.MISSING_ARGUMENT);
            }

            Integer currentQuantity = orderStageRequest.getCurrentQuantity() != null ? orderStageRequest.getCurrentQuantity() : 0;

            Integer remainingQuantity = order.getQuantity() - currentQuantity;

            Boolean status = orderStageRequest.isStatus();

            OrderStage orderStage = OrderStage
                    .builder()
                    .stage(stage)
                    .currentQuantity(currentQuantity)
                    .remainingQuantity(remainingQuantity)
                    .order(order)
                    .status(status)
                    .build();
            var savedStage = orderStageRepository.save(orderStage);
            if (savedStage != null) {
                return orderStageMapper.mapToOrderStageResponse(savedStage);
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<OrderStageResponse> getOrderStageByOrderID(String orderID) {
        return orderStageRepository.findAll()
                .stream()
                .filter(stage -> stage.getOrder().getOrderID().equals(orderID))
                .map(orderStageMapper::mapToOrderStageResponse)
                .toList();
    }

    @Override
    public OrderStage getOrderStageByID(String stageID) {
        return orderStageRepository.findById(stageID).orElse(null);
    }

    @Override
    public void updateStage(OrderStage orderStage) {
        orderStageRepository.save(orderStage);
    }
}
