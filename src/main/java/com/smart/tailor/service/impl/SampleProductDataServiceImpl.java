package com.smart.tailor.service.impl;

import com.smart.tailor.entities.SampleProductData;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.SampleProductDataMapper;
import com.smart.tailor.repository.SampleProductDataRepository;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.service.OrderStageService;
import com.smart.tailor.service.SampleProductDataService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.SampleProductDataRequest;
import com.smart.tailor.utils.response.SampleProductDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SampleProductDataServiceImpl implements SampleProductDataService {
    private final SampleProductDataRepository sampleProductDataRepository;
    private final BrandService brandService;
    private final OrderService orderService;
    private final OrderStageService stageService;
    private final SampleProductDataMapper sampleProductDataMapper;

    @Transactional
    @Override
    public void addNewSampleProductData(SampleProductDataRequest sampleProductDataRequest) {
        String subOrderID = sampleProductDataRequest.getOrderID();
        String stageID = sampleProductDataRequest.getOrderStageID();
        String brandID = sampleProductDataRequest.getBrandID();

        var orderStage = stageService.getOrderStageByID(stageID);
        var order = orderService.getOrderById(subOrderID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Order with OrderID: " + subOrderID));

        var brand = brandService.findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with BrandID: " + brandID));

        byte[] base64ImageUrl = null;
        if (Optional.ofNullable(sampleProductDataRequest.getImageUrl()).isPresent()) {
            base64ImageUrl = Utilities.encodeStringToBase64(sampleProductDataRequest.getImageUrl());
        }

        byte[] base64Video = null;
        if (Optional.ofNullable(sampleProductDataRequest.getVideo()).isPresent()) {
            base64Video = Utilities.encodeStringToBase64(sampleProductDataRequest.getVideo());
        }

        sampleProductDataRepository.save(SampleProductData
                .builder()
                .order(order)
                .orderStage(orderStage)
                .brand(brand)
                .imageUrl(base64ImageUrl)
                .video(base64Video)
                .description(sampleProductDataRequest.getDescription())
                .status(false)
                .build()
        );
    }

    @Transactional
    @Override
    public void updateSampleProductData(String sampleModelID, SampleProductDataRequest sampleProductDataRequest) {
        String stageID = sampleProductDataRequest.getOrderStageID();
        String orderID = sampleProductDataRequest.getOrderID();
        String brandID = sampleProductDataRequest.getBrandID();

        var sampleProductData = sampleProductDataRepository.findById(sampleModelID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Sample Product Data with SampleModelID: " + sampleModelID));
        var order = orderService.getOrderById(orderID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Order with orderID: " + orderID));
        var orderStage = stageService.getOrderStageByID(stageID);
        if (orderStage == null)
            throw new ItemNotFoundException("Can not find Order Stage with ID: " + stageID);

        var brand = brandService.findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with BrandID: " + brandID));

        byte[] base64ImageUrl = null;
        if (Optional.ofNullable(sampleProductDataRequest.getImageUrl()).isPresent()) {
            base64ImageUrl = Utilities.encodeStringToBase64(sampleProductDataRequest.getImageUrl());
        }

        byte[] base64Video = null;
        if (Optional.ofNullable(sampleProductDataRequest.getVideo()).isPresent()) {
            base64Video = Utilities.encodeStringToBase64(sampleProductDataRequest.getVideo());
        }

        sampleProductDataRepository.save(SampleProductData
                .builder()
                .sampleModelID(sampleModelID)
                .order(order)
                .orderStage(orderStage)
                .brand(brand)
                .imageUrl(base64ImageUrl)
                .video(base64Video)
                .description(sampleProductDataRequest.getDescription())
                .status(sampleProductDataRequest.getStatus())
                .build()
        );
    }

    @Override
    public SampleProductDataResponse getSampleProductDataByID(String sampleModelID) {
        return sampleProductDataRepository
                .findById(sampleModelID)
                .map(sampleProductDataMapper::mapperToSampleProductDataResponse)
                .orElse(null);
    }

    @Override
    public List<SampleProductDataResponse> getSampleProductDataByParentOrderID(String orderID) {
        return orderService
                .getSubOrderByParentID(orderID)
                .stream()
                .flatMap(subOrder -> sampleProductDataRepository.findSampleProductDataByOrderID(subOrder.getOrderID()).stream())
                .map(sampleProductDataMapper::mapperToSampleProductDataResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SampleProductDataResponse> getSampleProductDataByParentOrderIDAndStageID(String orderID, String stageID) {
        var order = orderService.getOrderById(orderID).orElseThrow(() -> new ItemNotFoundException("Can not found order"));
        if (order.getOrderType().equals("PARENT_ORDER")) {
            var stage = stageService.getOrderStageByID(stageID);
            if (stage.getStage().equals(OrderStatus.DEPOSIT)) {
                return orderService
                        .getSubOrderByParentID(orderID)
                        .stream()
                        .flatMap(
                                subOrder -> sampleProductDataRepository.findSampleProductDataByOrderID(subOrder.getOrderID()).stream()
                                        .filter(f -> f.getOrderStage().getStage().equals(OrderStatus.START_PRODUCING)
                                        )
                        )
                        .map(sampleProductDataMapper::mapperToSampleProductDataResponse)
                        .collect(Collectors.toList());
            } else {
                if (stage.getStage().equals(OrderStatus.PROCESSING)) {
                    var sampleInStage2 = orderService
                            .getSubOrderByParentID(orderID)
                            .stream()
                            .flatMap(
                                    subOrder -> sampleProductDataRepository.findSampleProductDataByOrderID(subOrder.getOrderID()).stream()
                                            .filter(f -> f.getOrderStage().getStage().equals(OrderStatus.FINISH_SECOND_STAGE)
                                            )
                            )
                            .map(sampleProductDataMapper::mapperToSampleProductDataResponse)
                            .collect(Collectors.toList());
                    if (sampleInStage2.isEmpty()) {
                        sampleInStage2 = orderService
                                .getSubOrderByParentID(orderID)
                                .stream()
                                .flatMap(
                                        subOrder -> sampleProductDataRepository.findSampleProductDataByOrderID(subOrder.getOrderID()).stream()
                                                .filter(f -> f.getOrderStage().getStage().equals(OrderStatus.FINISH_FIRST_STAGE)
                                                )
                                )
                                .map(sampleProductDataMapper::mapperToSampleProductDataResponse)
                                .collect(Collectors.toList());
                        return sampleInStage2;
                    } else {
                        return sampleInStage2;
                    }
                } else {
                    return orderService
                            .getSubOrderByParentID(orderID)
                            .stream()
                            .flatMap(
                                    subOrder -> sampleProductDataRepository.findSampleProductDataByOrderID(subOrder.getOrderID()).stream()
                                            .filter(f -> f.getOrderStage().getStage().equals(OrderStatus.COMPLETED)
                                            )
                            )
                            .map(sampleProductDataMapper::mapperToSampleProductDataResponse)
                            .collect(Collectors.toList());
                }
            }
        } else {
            return orderService
                    .getSubOrderByParentID(orderID)
                    .stream()
                    .flatMap(
                            subOrder -> sampleProductDataRepository.findSampleProductDataByOrderID(subOrder.getOrderID()).stream()
                                    .filter(f -> f.getOrderStage().getStageId().equals(stageID))
                    )
                    .map(sampleProductDataMapper::mapperToSampleProductDataResponse)
                    .collect(Collectors.toList());
        }
    }
}
