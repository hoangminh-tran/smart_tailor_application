package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.SizeService;
import com.smart.tailor.utils.request.ListSizeRequest;
import com.smart.tailor.utils.request.SizeRequest;
import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(APIConstant.SizeAPI.SIZE)
@RequiredArgsConstructor
@Slf4j
@Validated
public class SizeController {
    private final SizeService sizeService;
    private final Logger logger = LoggerFactory.getLogger(SizeController.class);

    @GetMapping(APIConstant.SizeAPI.GET_ALL_SIZE)
    public ResponseEntity<ObjectNode> getAllSize() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var sizeResponses = sizeService.findAllSizeResponse();
        if (!sizeResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_SIZE_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(sizeResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SIZE);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.SizeAPI.ADD_NEW_SIZE)
    public ResponseEntity<ObjectNode> addNewSize(@Valid @RequestBody ListSizeRequest listSizeRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        sizeService.createSize(listSizeRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_SIZE_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.SizeAPI.UPDATE_SIZE + "/{sizeID}")
    public ResponseEntity<ObjectNode> updateSize( @PathVariable("sizeID") String sizeID,
                                                 @Valid @RequestBody SizeRequest sizeRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        sizeService.updateSize(sizeID, sizeRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.UPDATE_SIZE_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}
