package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.SystemImageAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.SystemImageService;
import com.smart.tailor.utils.request.SystemImageRequest;
import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(SystemImageAPI.SYSTEM_IMAGE)
@RequiredArgsConstructor
@Validated
public class SystemImageController {

    private final SystemImageService systemImageService;

    @GetMapping(SystemImageAPI.GET_ALL_SYSTEM_IMAGE)
    public ResponseEntity<ObjectNode> getAllSystemImage() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var systemImageList = systemImageService.getAllSystemImage();
        if (!systemImageList.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_SYSTEM_IMAGE_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(systemImageList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SYSTEM_IMAGE);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(SystemImageAPI.GET_ALL_SYSTEM_IMAGE_BY_ID + "/{systemImageID}")
    public ResponseEntity<ObjectNode> getSystemImageById( @PathVariable("systemImageID") String systemImageID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var systemImageList = systemImageService.getSystemImageById(systemImageID);
        if (systemImageList != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_SYSTEM_IMAGE_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(systemImageList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SYSTEM_IMAGE);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(SystemImageAPI.GET_ALL_SYSTEM_IMAGE_BY_IMAGE_TYPE)
    public ResponseEntity<ObjectNode> getAllSystemImageByImageType(@RequestParam("imageType") String imageType) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var systemImageList = systemImageService.getAllSystemImageByImageType(imageType);
        if (systemImageList != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_SYSTEM_IMAGE_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(systemImageList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SYSTEM_IMAGE);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(SystemImageAPI.GET_ALL_SYSTEM_IMAGE_BY_IMAGE_TYPE_AND_PREMIUM)
    public ResponseEntity<ObjectNode> getSystemImageByTypeAndIsPremium(@RequestParam("imageType") String imageType, @RequestParam("isPremium") Boolean isPremium) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var systemImageList = systemImageService.getSystemImageByTypeAndIsPremium(imageType, isPremium);
        if (systemImageList != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_SYSTEM_IMAGE_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(systemImageList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SYSTEM_IMAGE);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(SystemImageAPI.ADD_NEW_SYSTEM_IMAGE)
    public ResponseEntity<ObjectNode> addNewSystemImage(@Valid @RequestBody SystemImageRequest systemImageRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var systemImageList = systemImageService.addNewSystemImage(systemImageRequest);
        if (systemImageList != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.ADD_NEW_SYSTEM_IMAGE_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(systemImageList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.ADD_NEW_SYSTEM_IMAGE_FAIL);
        }
        return ResponseEntity.ok(response);
    }
}
