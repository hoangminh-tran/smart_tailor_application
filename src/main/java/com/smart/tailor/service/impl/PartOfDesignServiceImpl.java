package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.entities.Material;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.PartOfDesignMapper;
import com.smart.tailor.repository.PartOfDesignRepository;
import com.smart.tailor.service.ItemMaskService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.service.PartOfDesignService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.response.PartOfDesignResponse;
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
public class PartOfDesignServiceImpl implements PartOfDesignService {
    private final PartOfDesignRepository partOfDesignRepository;
    private final ItemMaskService itemMaskService;
    private final MaterialService materialService;
    private final PartOfDesignMapper partOfDesignMapper;
    private final Logger logger = LoggerFactory.getLogger(PartOfDesignServiceImpl.class);

    @Transactional
    @Override
    public List<PartOfDesign> createPartOfDesign(Design design, List<PartOfDesignRequest> partOfDesignRequestList) {
        List<PartOfDesign> partOfDesignList = new ArrayList<>();
        for (PartOfDesignRequest partOfDesignRequest : partOfDesignRequestList) {

            // Check Whether ImageUrl is existed or not. Then Convert It to Base64
            byte[] base64ImageUrl = Optional.ofNullable(partOfDesignRequest.getImageUrl())
                    .map(Utilities::encodeStringToBase64)
                    .orElse(null);

            byte[] base64SuccessImageUrl = Optional.ofNullable(partOfDesignRequest.getSuccessImageUrl())
                    .map(Utilities::encodeStringToBase64)
                    .orElse(null);

            byte[] base64RealPartImageUrl = Optional.ofNullable(partOfDesignRequest.getRealPartImageUrl())
                    .map(Utilities::encodeStringToBase64)
                    .orElse(null);

            var partOfDesign = PartOfDesign
                    .builder()
                    .design(design)
                    .partOfDesignName(partOfDesignRequest.getPartOfDesignName())
                    .imageUrl(base64ImageUrl)
                    .successImageUrl(base64SuccessImageUrl)
                    .realPartImageUrl(base64RealPartImageUrl)
                    .width(partOfDesignRequest.getWidth())
                    .height(partOfDesignRequest.getHeight())
                    .build();


            Material material = null;
            if (Utilities.isStringNotNullOrEmpty(partOfDesignRequest.getMaterialID())) {
                if (!Utilities.isValidCustomKey(partOfDesignRequest.getMaterialID())) {
                    throw new BadRequestException("Invalid Type String of MaterialID: " + partOfDesignRequest.getMaterialID());
                }

                material = materialService.findMaterialByID(partOfDesignRequest.getMaterialID()).
                        orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + partOfDesignRequest.getMaterialID()));

                partOfDesign.setMaterial(material);
            } else {
                var expertTailoring = design.getExpertTailoring();
                var materials = materialService.findMaterialsByExpertTailoringIDAndCategoryName(expertTailoring.getExpertTailoringID(), "Fabric");
                partOfDesign.setMaterial(materials.get(0));
            }

            var savedPartOfDesign = partOfDesignRepository.save(partOfDesign);

//            if (Optional.ofNullable(partOfDesignRequest.getItemMask()).isEmpty()) {
//                continue;
//            }
//            List<ItemMask> itemMaskList = null;
//            try {
//                itemMaskList = itemMaskService.createItemMask(savedPartOfDesign, partOfDesignRequest.getItemMask());
//            } catch (BadRequestException ex) {
//                logger.error("Bad Request Exception in create Item Mask {}", ex.getMessage());
//                throw new BadRequestException(ex.getMessage());
//            } catch (ItemNotFoundException ex) {
//                logger.error("Item Not Found Exception in create Item Mask {}", ex.getMessage());
//                throw new ItemNotFoundException(ex.getMessage());
//            }
//
//            // Set List Of ItemMask belong to PartOfDesign
//            savedPartOfDesign.setItemMaskList(itemMaskList);
//            // Add Correct PartOfDesign to ListPartOfDesign
            partOfDesignList.add(savedPartOfDesign);
        }
        return partOfDesignList;

    }

    @Override
    public List<PartOfDesignResponse> getListPartOfDesignByDesignID(String designID) {
        return partOfDesignRepository
                .findAll()
                .stream()
                .filter(part -> part.getDesign().getDesignID().toString().equals(designID.toString()))
                .map(partOfDesignMapper::mapperToPartOfDesignResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartOfDesign> getListPartOfDesignObjectByDesignID(String designID) {
        return partOfDesignRepository
                .findAll()
                .stream()
                .filter(part -> part.getDesign().getDesignID().equals(designID))
                .collect(Collectors.toList());
    }

    @Override
    public PartOfDesignResponse getPartOfDesignByPartOfDesignID(String partOfDesignID) {
        var partOfDesign = partOfDesignRepository.findById(partOfDesignID);
        if (partOfDesign.isPresent()) {
            return partOfDesignMapper.mapperToPartOfDesignResponse(partOfDesign.get());
        }
        return null;
    }

    @Override
    public List<PartOfDesignResponse> getAllPartOfDesign() {
        return partOfDesignRepository
                .findAll()
                .stream()
                .map(partOfDesignMapper::mapperToPartOfDesignResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<PartOfDesign> savePartOfDesign(List<PartOfDesign> partOfDesignList) {
        return partOfDesignRepository.saveAll(partOfDesignList);
    }

    @Transactional
    @Override
    public List<PartOfDesign> updatePartOfDesign(Design design, List<PartOfDesignRequest> partOfDesignRequestList) {
        List<PartOfDesign> partOfDesignList = new ArrayList<>();
        for (PartOfDesignRequest partOfDesignRequest : partOfDesignRequestList) {

            // Find PartOfDesign By Design ID and PartOfDesignName
            var existedPartOfDesign = partOfDesignRepository.getPartOfDesignByDesignIDAndPartOfDesignName(
                    design.getDesignID(),
                    partOfDesignRequest.getPartOfDesignName()
            ).orElseThrow(() -> new ItemNotFoundException("Can not find PartOfDesign By DesignID and PartOfDesignName"));

            // Check Whether ImageUrl is existed or not. Then Convert It to Base64
            byte[] base64ImageUrl = Optional.ofNullable(partOfDesignRequest.getImageUrl())
                    .map(Utilities::encodeStringToBase64)
                    .orElse(null);

            byte[] base64SuccessImageUrl = Optional.ofNullable(partOfDesignRequest.getSuccessImageUrl())
                    .map(Utilities::encodeStringToBase64)
                    .orElse(null);

            byte[] base64RealPartImageUrl = Optional.ofNullable(partOfDesignRequest.getRealPartImageUrl())
                    .map(Utilities::encodeStringToBase64)
                    .orElse(null);

            Material material = null;
            if (Utilities.isStringNotNullOrEmpty(partOfDesignRequest.getMaterialID())) {
                if (!Utilities.isValidCustomKey(partOfDesignRequest.getMaterialID())) {
                    throw new BadRequestException("Invalid Type String of MaterialID: " + partOfDesignRequest.getMaterialID());
                }

                material = materialService.findMaterialByID(partOfDesignRequest.getMaterialID()).
                        orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + partOfDesignRequest.getMaterialID()));
            }

            if (Optional.ofNullable(partOfDesignRequest.getItemMask()).isEmpty()) {
                continue;
            }

            logger.info("Before Remove old ItemMask {}",  existedPartOfDesign.getItemMaskList().size());
            for(ItemMask itemMask : existedPartOfDesign.getItemMaskList()){
               logger.info("Item Mask from ExistedPartOfDesign {}", itemMask.getItemMaskID());
            }

            var oldItemMasks = itemMaskService.getAllItemMaskByPartOfDesignID(existedPartOfDesign.getPartOfDesignID());
            for(var oldItemMask : oldItemMasks){
                itemMaskService.changeStatusItemMask(oldItemMask, false);
            }
            existedPartOfDesign.getItemMaskList().clear();

            logger.info("After Remove old ItemMask {}",  existedPartOfDesign.getItemMaskList().size());
            for(ItemMask itemMask :  existedPartOfDesign.getItemMaskList()){
                logger.warn("Item Mask from ExistedPartOfDesign {}", itemMask.getItemMaskID());
            }

            try {
                existedPartOfDesign.getItemMaskList().addAll(itemMaskService.updateItemMask(existedPartOfDesign, partOfDesignRequest.getItemMask()));
            } catch (BadRequestException ex) {
                logger.error("Bad Request Exception in create Item Mask {}", ex.getMessage());
                throw new BadRequestException(ex.getMessage());
            } catch (ItemNotFoundException ex) {
                logger.error("Item Not Found Exception in create Item Mask {}", ex.getMessage());
                throw new ItemNotFoundException(ex.getMessage());
            }

            logger.info("After Add New ItemMask {}",  existedPartOfDesign.getItemMaskList().size());
            for(ItemMask itemMask :  existedPartOfDesign.getItemMaskList()){
                logger.warn("Item Mask from ExistedPartOfDesign {}", itemMask.getItemMaskID());
            }
            var updatedPartOfDesign = partOfDesignRepository.save(
                    PartOfDesign
                        .builder()
                        .partOfDesignID(existedPartOfDesign.getPartOfDesignID())
                        .design(design)
                        .partOfDesignName(partOfDesignRequest.getPartOfDesignName())
                        .imageUrl(base64ImageUrl)
                        .successImageUrl(base64SuccessImageUrl)
                        .realPartImageUrl(base64RealPartImageUrl)
                        .width(partOfDesignRequest.getWidth())
                        .height(partOfDesignRequest.getHeight())
                        .material(material)
                        .itemMaskList(existedPartOfDesign.getItemMaskList())
                        .build()
            );

            // Add Correct PartOfDesign to ListPartOfDesign
            partOfDesignList.add(updatedPartOfDesign);
        }
        return partOfDesignList;

    }

    @Transactional
    @Override
    public List<PartOfDesign> createClonePartOfDesign(Design cloneDesign, List<PartOfDesign> basePartOfDesignList) {
        List<PartOfDesign> partOfDesignList = new ArrayList<>();
        for (PartOfDesign basePartOfDesign : basePartOfDesignList) {
            var clonePartOfDesign = partOfDesignRepository.save(
                    PartOfDesign
                            .builder()
                            .design(cloneDesign)
                            .partOfDesignName(basePartOfDesign.getPartOfDesignName())
                            .imageUrl(basePartOfDesign.getImageUrl())
                            .successImageUrl(basePartOfDesign.getSuccessImageUrl())
                            .realPartImageUrl(basePartOfDesign.getRealPartImageUrl())
                            .width(basePartOfDesign.getWidth())
                            .height(basePartOfDesign.getHeight())
                            .material(basePartOfDesign.getMaterial())
                            .build()
            );

            List<ItemMask> itemMaskList = new ArrayList<>();
            if(!basePartOfDesign.getItemMaskList().isEmpty()){
                try {
                    itemMaskList = itemMaskService.createCloneItemMask(clonePartOfDesign, basePartOfDesign.getItemMaskList());
                } catch (BadRequestException ex) {
                    logger.error("Bad Request Exception in create Item Mask {}", ex.getMessage());
                    throw new BadRequestException(ex.getMessage());
                } catch (ItemNotFoundException ex) {
                    logger.error("Item Not Found Exception in create Item Mask {}", ex.getMessage());
                    throw new ItemNotFoundException(ex.getMessage());
                }
                clonePartOfDesign.setItemMaskList(itemMaskList);
                var clonePartOfDesignUpdated = partOfDesignRepository.save(clonePartOfDesign);
                // Add Correct PartOfDesign to ListPartOfDesign
                partOfDesignList.add(clonePartOfDesignUpdated);
            }
            else partOfDesignList.add(clonePartOfDesign);
        }
        return partOfDesignList;
    }

}
