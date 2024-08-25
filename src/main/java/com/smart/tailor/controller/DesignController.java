package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.utils.request.CloneDesignRequest;
import com.smart.tailor.utils.request.DesignRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(APIConstant.DesignAPI.DESIGN)
@RequiredArgsConstructor
@Slf4j
@Validated
public class DesignController {
    private final DesignService designService;
    private final Logger logger = LoggerFactory.getLogger(DesignController.class);
    private final ObjectMapper objectMapper;

    @PostMapping(APIConstant.DesignAPI.ADD_NEW_DESIGN)
    public ResponseEntity<ObjectNode> addNewDesign(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                   @Valid @RequestBody DesignRequest designRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        var apiResponse = designService.createDesign(jwtToken, designRequest);
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN_BY_USER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getAllDesignByUserID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                           @PathVariable("userID") String userID) {
        var designResponseList = designService.getAllDesignByUserID(jwtToken, userID);
        ObjectNode response = objectMapper.createObjectNode();
        if (designResponseList.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_DESIGN_BY_USER_ID);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_DESIGN_BY_USER_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(designResponseList));
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN)
    public ResponseEntity<ObjectNode> getAllDesign() {
        var designResponseList = designService.getAllDesign();
        ObjectNode response = objectMapper.createObjectNode();
        if (designResponseList.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_DESIGN);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_DESIGN_BY_USER_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(designResponseList));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.DesignAPI.GET_DESIGN_BY_ID + "/{designID}")
    public ResponseEntity<ObjectNode> getDesignByID(@PathVariable("designID") String designID) {
        var design = designService.getDesignResponseByID(designID);
        ObjectNode response = objectMapper.createObjectNode();
        if (design == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_DESIGN);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_DESIGN_BY_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(design));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN_BY_CUSTOMER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getAllDesignByCustomerID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                               @PathVariable("userID") String userID) {
        var apiResponse = designService.getAllDesignByUserIDAndRoleName(jwtToken, userID, RoleType.CUSTOMER.name());
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getAllDesignByBrandID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                            @PathVariable("brandID") String brandID) {
        var apiResponse = designService.getAllDesignByUserIDAndRoleName(jwtToken, brandID, RoleType.BRAND.name());
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.DesignAPI.UPDATE_PUBLIC_STATUS_BY_DESIGN_ID + "/{designID}")
    public ResponseEntity<ObjectNode> updatePublicStatusByDesignID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                                   @PathVariable("designID") String designID) {
        designService.updatePublicStatusDesign(jwtToken, designID);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.UPDATE_PUBLIC_STATUS_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.DesignAPI.ADD_NEW_CLONE_DESIGN)
    public ResponseEntity<ObjectNode> addNewCloneDesign(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                        @Valid @RequestBody CloneDesignRequest cloneDesignRequest) {
        designService.addNewCloneDesignFromBrandDesign(jwtToken, cloneDesignRequest);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_NEW_CLONE_DESIGN_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.DesignAPI.UPDATE_DESIGN + "/{designID}")
    public ResponseEntity<ObjectNode> updateDesign(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                   @PathVariable("designID") String designID,
                                                   @Valid @RequestBody DesignRequest designRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        var apiResponse = designService.updateDesign(jwtToken, designID, designRequest);
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.DesignAPI.CREATE_CLONE_DESIGN_FROM_BASE_DESIGN + "/{baseDesignID}")
    public ResponseEntity<ObjectNode> createCloneDesignFromBaseDesign(@PathVariable("baseDesignID") String baseDesignID) {
        var designResponse = designService.createCloneDesignFromBaseDesign(baseDesignID);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Create Clone Design From Base Design Successfully");
        response.set("data", objectMapper.valueToTree(designResponse));
        return ResponseEntity.ok(response);
    }
}
