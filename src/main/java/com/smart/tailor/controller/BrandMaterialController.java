package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.service.BrandMaterialService;
import com.smart.tailor.service.JwtService;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.validate.ValidCustomKey;
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
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping(APIConstant.BrandMaterialAPI.BRAND_MATERIAL)
@RequiredArgsConstructor
@Slf4j
@Validated
public class BrandMaterialController {
    private final BrandMaterialService brandMaterialService;
    private final Logger logger = LoggerFactory.getLogger(BrandMaterialController.class);
    private final ObjectMapper objectMapper;

    @GetMapping(APIConstant.BrandMaterialAPI.GET_ALL_BRAND_MATERIAL)
    public ResponseEntity<ObjectNode> getAllBrandMaterials() {
        ObjectNode response = objectMapper.createObjectNode();
        var materials = brandMaterialService.getAllBrandMaterial();
        if (!materials.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_BRAND_MATERIAL_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(materials));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_BRAND_MATERIAL);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.BrandMaterialAPI.GET_ALL_BRAND_MATERIAL_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getAllBrandMaterialsByBrandID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                                    @PathVariable("brandID") String brandID) {
        ObjectNode response = objectMapper.createObjectNode();
        var materials = brandMaterialService.getAllBrandMaterialByBrandID(jwtToken, brandID);
        if (materials != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_BRAND_MATERIAL_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(materials));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_BRAND_MATERIAL);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.BrandMaterialAPI.ADD_NEW_BRAND_MATERIAL)
    public ResponseEntity<ObjectNode> addNewBrandMaterial(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                          @Valid @RequestBody BrandMaterialRequest brandMaterialRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        brandMaterialService.createBrandMaterial(jwtToken, brandMaterialRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_NEW_BRAND_MATERIAL_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.BrandMaterialAPI.UPDATE_BRAND_MATERIAL)
    public ResponseEntity<ObjectNode> updateBrandMaterial(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                          @Valid @RequestBody BrandMaterialRequest brandMaterialRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        brandMaterialService.updateBrandMaterial(jwtToken, brandMaterialRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.UPDATE_BRAND_MATERIAL_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }


    @PostMapping(APIConstant.BrandMaterialAPI.ADD_NEW_BRAND_MATERIAL_BY_EXCEL_FILE)
    public ResponseEntity<ObjectNode> addNewBrandMaterialByExcelFile(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                                     @RequestParam("file") MultipartFile file,
                                                                     @RequestParam("brandID") String brandID) {
        ObjectNode response = objectMapper.createObjectNode();
        brandMaterialService.createBrandMaterialByImportExcelData(jwtToken, file, brandID);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_NEW_BRAND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

}
