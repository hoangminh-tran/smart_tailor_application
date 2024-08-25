package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.entities.Material;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.enums.PrintType;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.ItemMaskMapper;
import com.smart.tailor.repository.ItemMaskRepository;
import com.smart.tailor.service.ItemMaskService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.ItemMaskRequest;
import com.smart.tailor.utils.response.ItemMaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemMaskServiceImpl implements ItemMaskService {
    private final ItemMaskRepository itemMaskRepository;
    private final ItemMaskMapper itemMaskMapper;
    private final MaterialService materialService;
    private final Logger logger = LoggerFactory.getLogger(ItemMaskServiceImpl.class);

    @Override
    @Transactional
    public List<ItemMask> createItemMask(PartOfDesign partOfDesign, List<ItemMaskRequest> itemMaskRequestList) {
        List<ItemMask> itemMaskList = new ArrayList<>();

        for (ItemMaskRequest itemMaskRequest : itemMaskRequestList) {
            if (!Utilities.isValidBoolean(itemMaskRequest.getIsSystemItem())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " isSystemItem");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getPositionX())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " positionX");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getPositionY())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " positionY");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getScaleX())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " scaleX");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getScaleY())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " scaleY");
            }

            if (!Utilities.isValidInteger(itemMaskRequest.getIndexZ())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " indexZ");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getRotate())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " rotate");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getTopLeftRadius())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " topLeftRadius");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getTopRightRadius())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " topRightRadius");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getBottomLeftRadius())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " bottomLeftRadius");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getBottomRightRadius())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " bottomRightRadius");
            }

            byte[] base64ImageUrl = Optional.ofNullable(itemMaskRequest.getImageUrl())
                    .map(Utilities::encodeStringToBase64)
                    .orElse(null);

            String itemMaskName = Optional.ofNullable(itemMaskRequest.getItemMaskName()).orElse(null);
            String typeOfItem = Optional.ofNullable(itemMaskRequest.getTypeOfItem()).orElse(null);

            var itemMask = ItemMask
                    .builder()
                    .partOfDesign(partOfDesign)
                    .itemMaskName(itemMaskName)
                    .typeOfItem(typeOfItem)
                    .isSystemItem(itemMaskRequest.getIsSystemItem())
                    .positionX(itemMaskRequest.getPositionX())
                    .positionY(itemMaskRequest.getPositionY())
                    .scaleX(itemMaskRequest.getScaleX())
                    .scaleY(itemMaskRequest.getScaleY())
                    .indexZ(itemMaskRequest.getIndexZ())
                    .rotate(itemMaskRequest.getRotate())
                    .bottomLeftRadius(itemMaskRequest.getBottomLeftRadius())
                    .bottomRightRadius(itemMaskRequest.getBottomRightRadius())
                    .topRightRadius(itemMaskRequest.getTopRightRadius())
                    .topLeftRadius(itemMaskRequest.getTopLeftRadius())
                    .status(true)
                    .imageUrl(base64ImageUrl)
                    .printType(PrintType.valueOf(itemMaskRequest.getPrintType()))
                    .build();

            Material material = null;
            if (Utilities.isStringNotNullOrEmpty(itemMaskRequest.getMaterialID())) {
                if (!Utilities.isValidCustomKey(itemMaskRequest.getMaterialID())) {
                    throw new BadRequestException("Invalid Type String of MaterialID: " + itemMaskRequest.getMaterialID());
                }

                material = materialService.findMaterialByID(itemMaskRequest.getMaterialID())
                        .orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + itemMaskRequest.getMaterialID()));

                itemMask.setMaterial(material);
            }

            var savedItemMask = itemMaskRepository.save(itemMask);
            itemMaskList.add(savedItemMask);
        }
        return itemMaskList;
    }

    @Override
    public List<ItemMaskResponse> getListItemMaskByPartOfDesignID(String partOfDesignID) {
        return itemMaskRepository
                .findAll()
                .stream()
                .filter(itemMask -> itemMask.getPartOfDesign().getPartOfDesignID().toString().equals(partOfDesignID.toString()))
                .filter(itemMask -> itemMask.getStatus())
                .map(itemMaskMapper::mapperToItemMaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemMaskResponse getItemMaskByItemMaskID(String itemMaskID) {
        var itemMask = itemMaskRepository.findById(itemMaskID);
        if (itemMask.isPresent() && itemMask.get().getStatus()) {
            return itemMaskMapper.mapperToItemMaskResponse(itemMask.get());
        }
        return null;
    }

    @Override
    public List<ItemMaskResponse> getAllItemMask() {
        return itemMaskRepository
                .findAll()
                .stream()
                .filter(itemMask -> itemMask.getStatus())
                .map(itemMaskMapper::mapperToItemMaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemMask> getAllItemMaskByPartOfDesignID(String partOfDesignID) {
        return itemMaskRepository.getAllItemMaskByPartOfDesignID(partOfDesignID);
    }

    @Transactional
    @Override
    public void changeStatusItemMask(ItemMask itemMask, Boolean status) {
        itemMask.setStatus(status);
        itemMaskRepository.save(itemMask);
    }

    @Transactional
    @Override
    public List<ItemMask> updateItemMask(PartOfDesign partOfDesign, List<ItemMaskRequest> itemMaskRequestList) {
        List<ItemMask> itemMaskList = new ArrayList<>();

        for (ItemMaskRequest itemMaskRequest : itemMaskRequestList) {
            if (!Utilities.isValidBoolean(itemMaskRequest.getIsSystemItem())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " isSystemItem");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getPositionX())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " positionX");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getPositionY())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " positionY");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getScaleX())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " scaleX");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getScaleY())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " scaleY");
            }

            if (!Utilities.isValidInteger(itemMaskRequest.getIndexZ())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " indexZ");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getRotate())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " rotate");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getTopLeftRadius())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " topLeftRadius");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getTopRightRadius())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " topRightRadius");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getBottomLeftRadius())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " bottomLeftRadius");
            }

            if (!Utilities.isValidFloat(itemMaskRequest.getBottomRightRadius())) {
                throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " bottomRightRadius");
            }

            // Check Whether ImageUrl is existed or not. Then Convert It to Base64
            byte[] base64ImageUrl = Optional.ofNullable(itemMaskRequest.getImageUrl())
                    .map(Utilities::encodeStringToBase64)
                    .orElse(null);

            String itemMaskName = Optional.ofNullable(itemMaskRequest.getItemMaskName()).orElse(null);
            String typeOfItem = Optional.ofNullable(itemMaskRequest.getTypeOfItem()).orElse(null);

            Material material = null;
            if (Utilities.isStringNotNullOrEmpty(itemMaskRequest.getMaterialID())) {
                if (!Utilities.isValidCustomKey(itemMaskRequest.getMaterialID())) {
                    throw new BadRequestException("Invalid Type String of MaterialID: " + itemMaskRequest.getMaterialID());
                }

                material = materialService.findMaterialByID(itemMaskRequest.getMaterialID())
                        .orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + itemMaskRequest.getMaterialID()));
            }

            var savedItemMask = itemMaskRepository.save(
                    ItemMask
                        .builder()
                        .partOfDesign(partOfDesign)
                        .material(material)
                        .itemMaskName(itemMaskName)
                        .typeOfItem(typeOfItem)
                        .isSystemItem(itemMaskRequest.getIsSystemItem())
                        .positionX(itemMaskRequest.getPositionX())
                        .positionY(itemMaskRequest.getPositionY())
                        .scaleX(itemMaskRequest.getScaleX())
                        .scaleY(itemMaskRequest.getScaleY())
                        .indexZ(itemMaskRequest.getIndexZ())
                        .rotate(itemMaskRequest.getRotate())
                        .bottomLeftRadius(itemMaskRequest.getBottomLeftRadius())
                        .bottomRightRadius(itemMaskRequest.getBottomRightRadius())
                        .topRightRadius(itemMaskRequest.getTopRightRadius())
                        .topLeftRadius(itemMaskRequest.getTopLeftRadius())
                        .status(true)
                        .imageUrl(base64ImageUrl)
                        .printType(PrintType.valueOf(itemMaskRequest.getPrintType()))
                        .build()
            );

            itemMaskList.add(savedItemMask);
        }
        return itemMaskList;
    }

    @Transactional
    @Override
    public List<ItemMask> createCloneItemMask(PartOfDesign clonePartOfDesign, List<ItemMask> baseItemMaskList) {
        List<ItemMask> itemMaskList = new ArrayList<>();

        for (ItemMask baseItemMask : baseItemMaskList) {

            var cloneItemMask = itemMaskRepository.save(
                    ItemMask
                            .builder()
                            .partOfDesign(clonePartOfDesign)
                            .itemMaskName(baseItemMask.getItemMaskName())
                            .typeOfItem(baseItemMask.getTypeOfItem())
                            .isSystemItem(baseItemMask.getIsSystemItem())
                            .positionX(baseItemMask.getPositionX())
                            .positionY(baseItemMask.getPositionY())
                            .scaleX(baseItemMask.getScaleX())
                            .scaleY(baseItemMask.getScaleY())
                            .indexZ(baseItemMask.getIndexZ())
                            .rotate(baseItemMask.getRotate())
                            .bottomLeftRadius(baseItemMask.getBottomLeftRadius())
                            .bottomRightRadius(baseItemMask.getBottomRightRadius())
                            .topRightRadius(baseItemMask.getTopRightRadius())
                            .topLeftRadius(baseItemMask.getTopLeftRadius())
                            .status(true)
                            .imageUrl(baseItemMask.getImageUrl())
                            .material(baseItemMask.getMaterial())
                            .printType(baseItemMask.getPrintType())
                            .build()
            );
            itemMaskList.add(cloneItemMask);
        }
        return itemMaskList;
    }
}
