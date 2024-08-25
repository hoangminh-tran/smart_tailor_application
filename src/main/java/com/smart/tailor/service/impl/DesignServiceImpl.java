package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.mapper.DesignMapper;
import com.smart.tailor.repository.DesignRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.CloneDesignRequest;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.request.ItemMaskInformation;
import com.smart.tailor.utils.request.PartOfDesignInformation;
import com.smart.tailor.utils.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DesignServiceImpl implements DesignService {
    private final DesignRepository designRepository;
    private final PartOfDesignService partOfDesignService;
    private final ExpertTailoringService expertTailoringService;
    private final BrandMaterialService brandMaterialService;
    private final JwtService jwtService;
    private final UserService userService;
    private final DesignMapper designMapper;
    private final Logger logger = LoggerFactory.getLogger(DesignServiceImpl.class);

    @Override
    public APIResponse createDesign(String jwtToken, DesignRequest designRequest) {
        var userID = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!userID.equals(designRequest.getUserID())){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        if (!Utilities.isValidBoolean(designRequest.getPublicStatus())) {
            throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " publicStatus");
        }

        var user = userService.getUserByUserID(designRequest.getUserID())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND));

        var expertTailoringResponse = expertTailoringService.findExpertTailoringByID(designRequest.getExpertTailoringID())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING));

        String color = Optional.ofNullable(designRequest.getColor()).orElse(null);

        byte[] base64ImageUrl = Optional.ofNullable(designRequest.getImageUrl())
                .map(Utilities::encodeStringToBase64)
                .orElse(null);

        Design design = designRepository.save(
                Design
                        .builder()
                        .user(user)
                        .expertTailoring(expertTailoringResponse)
                        .titleDesign(designRequest.getTitleDesign())
                        .publicStatus(designRequest.getPublicStatus())
                        .minWeight(designRequest.getMinWeight())
                        .maxWeight(designRequest.getMaxWeight())
                        .color(color)
                        .imageUrl(base64ImageUrl)
                        .build()
        );

        if (Optional.ofNullable(designRequest.getPartOfDesign()).isEmpty()) {
            throw new BadRequestException(MessageConstant.PART_OF_DESIGN_LIST_REQUEST_IS_EMPTY);
        }

        List<PartOfDesign> partOfDesignList = null;
        try {
            partOfDesignList = partOfDesignService.createPartOfDesign(design, designRequest.getPartOfDesign());
        } catch (BadRequestException ex) {
            logger.error("Bad Request Exception in create Part Of Design {}", ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        } catch (ItemNotFoundException ex) {
            logger.error("Item Not Found Exception in Part Of Design {}", ex.getMessage());
            throw new ItemNotFoundException(ex.getMessage());
        }


        byte[] imageUrl = Utilities.encodeStringToBase64(designRequest.getImageUrl());
        // Set ImageUrl From Front PartOfDesign to Design
        design.setImageUrl(imageUrl);

        // Update List PartOfDesign belong to Design
        design.setPartOfDesignList(partOfDesignList);

        var saveDesign = designRepository.save(design);

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.ADD_NEW_DESIGN_SUCCESSFULLY)
                .data(designMapper.mapperToDesignCustomResponse(saveDesign))
                .build();
    }

    @Override
    public Design getDesignByID(String designID) {
        return designRepository.findById(designID).orElse(null);
    }

    @Override
    public DesignResponse getDesignByOrderID(String orderID) {
//        return designMapper.mapperToDesignResponse(designRepository.findByOrderID(orderID));
        return getDesignResponseByID(designRepository.findByOrderID(orderID).getDesignID());
    }

    @Override
    public Design getDesignObjectByOrderID(String orderID) {
        return designRepository.findByOrderID(orderID);
    }

    @Override
    public List<DesignResponse> getAllDesignByUserID(String jwtToken, String userID) {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!userID.equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }
        return designRepository
                .findAll()
                .stream()
                .filter(design -> design.getUser().getUserID().toString().equals(userID.toString()))
                .map(designMapper::mapperToDesignResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public DesignResponse getDesignResponseByID(String designID) {
        var designOptional = designRepository.findById(designID);
        if (designOptional.isPresent()) {
            DesignResponse designResponse = designMapper.mapperToDesignResponse(designOptional.get());
            List<DesignMaterialDetail> materialDetailList = new ArrayList<>();

            List<PartOfDesignResponse> partOfDesignList = designResponse.getPartOfDesign();
            for (PartOfDesignResponse partOfDesign : partOfDesignList) {
                MaterialResponse materialResponse = partOfDesign.getMaterial();
                DesignMaterialDetail designMaterialDetail = DesignMaterialDetail
                        .builder()
                        .materialResponse(materialResponse)
                        .quantity(1)
                        .maxPrice(brandMaterialService.getMaxPriceByMaterialID(materialResponse.getMaterialID()))
                        .minPrice(brandMaterialService.getMinPriceByMaterialID(materialResponse.getMaterialID()))
                        .build();
                boolean constain = false;
                for (int index = 0; index < materialDetailList.size(); index++) {
                    if (materialDetailList.get(index).getMaterialResponse().getMaterialID().equals(materialResponse.getMaterialID())) {
                        designMaterialDetail.setMaxPrice(
                                designMaterialDetail.getMaxPrice() + materialDetailList.get(index).getMaxPrice()
                        );
                        designMaterialDetail.setMinPrice(
                                designMaterialDetail.getMinPrice() + materialDetailList.get(index).getMinPrice()
                        );
                        designMaterialDetail.setQuantity(1 + materialDetailList.get(index).getQuantity());
                        materialDetailList.set(index, designMaterialDetail);
                        constain = true;
                    }
                }
                if (!constain) {
                    materialDetailList.add(designMaterialDetail);
                }

                List<ItemMaskResponse> itemMaskResponseList = partOfDesign.getItemMasks();
//                logger.error("itemMaskResponseList: {}", itemMaskResponseList);
                if (itemMaskResponseList != null) {
                    for (ItemMaskResponse itemMaskResponse : itemMaskResponseList) {
                        if (itemMaskResponse.getMaterial() != null && itemMaskResponse.getStatus()) {
                            materialResponse = itemMaskResponse.getMaterial();
                            designMaterialDetail = DesignMaterialDetail
                                    .builder()
                                    .materialResponse(materialResponse)
                                    .quantity(1)
                                    .maxPrice(brandMaterialService.getMaxPriceByMaterialID(materialResponse.getMaterialID()))
                                    .minPrice(brandMaterialService.getMinPriceByMaterialID(materialResponse.getMaterialID()))
                                    .build();
                            constain = false;
                            for (int index = 0; index < materialDetailList.size(); index++) {
                                if (materialDetailList.get(index).getMaterialResponse().getMaterialID().equals(materialResponse.getMaterialID())) {
                                    designMaterialDetail.setMaxPrice(
                                            designMaterialDetail.getMaxPrice() + materialDetailList.get(index).getMaxPrice()
                                    );
                                    designMaterialDetail.setMinPrice(
                                            designMaterialDetail.getMinPrice() + materialDetailList.get(index).getMinPrice()
                                    );
                                    designMaterialDetail.setQuantity(1 + materialDetailList.get(index).getQuantity());
                                    materialDetailList.set(index, designMaterialDetail);
                                    constain = true;
                                }
                            }
                            if (!constain) {
                                materialDetailList.add(designMaterialDetail);
                            }
                        }
                    }
                }
            }

            designResponse.setMaterialDetail(
                    materialDetailList
            );
            return designResponse;
        }
        return null;
    }

    @Override
    public List<DesignResponse> getAllDesign() {
        return designRepository
                .findAll()
                .stream()
                .map(designMapper::mapperToDesignResponse)
                .collect(Collectors.toList());
    }

    @Override
    public APIResponse getAllDesignByUserIDAndRoleName(String jwtToken, String userID, String roleName) {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!userID.equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        if (!Utilities.isStringNotNullOrEmpty(roleName)) {
            throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " roleName");
        }

        var userExisted = userService.getUserByUserID(userID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND));

        if (!userExisted.getRoles().getRoleName().contains(roleName)) {
            throw new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ROLE);
        }

        var designResponse = designRepository
                .findAll()
                .stream()
                .filter(design -> {
                    var user = design.getUser();
                    if (user.getUserID().toString().equals(userID.toString()) && user.getRoles().getRoleName().contains(roleName)) {
                        return true;
                    }
                    return false;
                })
                .map(designMapper::mapperToDesignResponse)
                .collect(Collectors.toList());

        String message = roleName.equals(RoleType.CUSTOMER.name()) ? MessageConstant.GET_ALL_DESIGN_BY_CUSTOMER_ID_SUCCESSFULLY : MessageConstant.GET_ALL_DESIGN_BY_BRAND_ID_SUCCESSFULLY;

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(designResponse)
                .build();
    }

    @Transactional
    @Override
    public void updatePublicStatusDesign(String jwtToken, String designID) {
        var designExisted = designRepository.findById(designID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN));

        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!designExisted.getUser().getUserID().equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        designExisted.setPublicStatus(!designExisted.getPublicStatus());
        designRepository.save(designExisted);
    }

    @Override
    public Design saveDesign(Design design) {
        return designRepository.save(design);
    }

    @Transactional
    @Override
    public void addNewCloneDesignFromBrandDesign(String jwtToken, CloneDesignRequest cloneDesignRequest) {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!cloneDesignRequest.getUserID().equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        var user = userService.getUserByUserID(cloneDesignRequest.getUserID())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND));

        var brandDesign = designRepository.findById(cloneDesignRequest.getDesignID())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN_BY_BRAND_ID));


        Design cloneDesign = designRepository.save(
                Design
                        .builder()
                        .user(user)
                        .expertTailoring(brandDesign.getExpertTailoring())
                        .titleDesign(brandDesign.getTitleDesign())
                        .publicStatus(brandDesign.getPublicStatus())
                        .minWeight(brandDesign.getMinWeight())
                        .maxWeight(brandDesign.getMaxWeight())
                        .color(brandDesign.getColor())
                        .imageUrl(brandDesign.getImageUrl())
                        .build()
        );
        logger.info("Create Clone Design {}", cloneDesign);

        if (Optional.ofNullable(cloneDesignRequest.getPartOfDesign()).isEmpty()) {
            throw new BadRequestException(MessageConstant.PART_OF_DESIGN_LIST_REQUEST_IS_EMPTY);
        }

        List<PartOfDesign> partOfDesignList = null;
        try {
            partOfDesignList = partOfDesignService.createPartOfDesign(cloneDesign, cloneDesignRequest.getPartOfDesign());
        } catch (BadRequestException ex) {
            logger.error("Bad Request Exception in create Part Of Design {}", ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        } catch (ItemNotFoundException ex) {
            logger.error("Item Not Found Exception in Part Of Design {}", ex.getMessage());
            throw new ItemNotFoundException(ex.getMessage());
        }

        // Update List PartOfDesign belong to Design
        cloneDesign.setPartOfDesignList(partOfDesignList);

        designRepository.save(cloneDesign);
    }

    @Transactional
    @Override
    public APIResponse updateDesign(String jwtToken, String designID, DesignRequest designRequest) {
        var userID = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!userID.equals(designRequest.getUserID())){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        if (!Utilities.isValidBoolean(designRequest.getPublicStatus())) {
            throw new BadRequestException(MessageConstant.INVALID_DATA_TYPE + " publicStatus");
        }

        var user = userService.getUserByUserID(designRequest.getUserID())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND));

        var expertTailoringResponse = expertTailoringService.findExpertTailoringByID(designRequest.getExpertTailoringID())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING));

        String color = Optional.ofNullable(designRequest.getColor()).orElse(null);

        var design = designRepository.findById(designID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN));

        List<PartOfDesign> partOfDesignList = null;
        try {
            partOfDesignList = partOfDesignService.updatePartOfDesign(design, designRequest.getPartOfDesign());
        } catch (BadRequestException ex) {
            logger.error("Bad Request Exception in Update Part Of Design {}", ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        } catch (ItemNotFoundException ex) {
            logger.error("Item Not Found Exception in Update Part Of Design {}", ex.getMessage());
            throw new ItemNotFoundException(ex.getMessage());
        }

        byte[] base64ImageUrl = Optional.ofNullable(designRequest.getImageUrl())
                .map(Utilities::encodeStringToBase64)
                .orElse(null);

        var updateDesign = designRepository.save(
                Design
                        .builder()
                        .designID(designID)
                        .user(user)
                        .expertTailoring(expertTailoringResponse)
                        .titleDesign(designRequest.getTitleDesign())
                        .publicStatus(designRequest.getPublicStatus())
                        .color(color)
                        .minWeight(designRequest.getMinWeight())
                        .maxWeight(designRequest.getMaxWeight())
                        .imageUrl(base64ImageUrl)
                        .partOfDesignList(partOfDesignList)
                        .build()
        );

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.UPDATE_DESIGN_SUCCESSFULLY)
                .data(designMapper.mapperToDesignCustomResponse(updateDesign))
                .build();
    }

    @Transactional
    @Override
    public Design createCloneDesignFromBaseDesign(String baseDesignID) {
        var design = designRepository.findById(baseDesignID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN));

        var cloneDesign = designRepository.save(
                Design
                        .builder()
                        .user(design.getUser())
                        .expertTailoring(design.getExpertTailoring())
                        .titleDesign(design.getTitleDesign())
                        .publicStatus(false)
                        .color(design.getColor())
                        .minWeight(design.getMinWeight())
                        .maxWeight(design.getMaxWeight())
                        .imageUrl(design.getImageUrl())
                        .build()
        );

        List<PartOfDesign> partOfDesignList = null;
        try {
            partOfDesignList = partOfDesignService.createClonePartOfDesign(cloneDesign, design.getPartOfDesignList());
        } catch (BadRequestException ex) {
            logger.error("Bad Request Exception in Update Part Of Design {}", ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        } catch (ItemNotFoundException ex) {
            logger.error("Item Not Found Exception in Update Part Of Design {}", ex.getMessage());
            throw new ItemNotFoundException(ex.getMessage());
        }

        cloneDesign.setPartOfDesignList(partOfDesignList);
        var cloneDesignUpdate = designRepository.save(cloneDesign);

//        return designMapper.mapperToDesignResponse(cloneDesignUpdate);
        return cloneDesignUpdate;
    }
}
