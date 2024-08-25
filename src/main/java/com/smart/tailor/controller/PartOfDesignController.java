package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.PartOfDesignService;
import com.smart.tailor.validate.ValidCustomKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(APIConstant.PartOfDesignAPI.PART_OF_DESIGN)
@RequiredArgsConstructor
@Slf4j
@Validated
public class PartOfDesignController {
    private final PartOfDesignService partOfDesignService;
    private final Logger logger = LoggerFactory.getLogger(MaterialController.class);
    private final ObjectMapper objectMapper;

    @GetMapping(APIConstant.PartOfDesignAPI.GET_ALL_PART_OF_DESIGN_BY_DESIGN_ID + "/{designID}")
    public ResponseEntity<ObjectNode> getAllPartOfDesignByDesignID( @PathVariable("designID") String designID) {
        ObjectNode response = objectMapper.createObjectNode();
        var partOfDesignResponses = partOfDesignService.getListPartOfDesignByDesignID(designID);
        if (partOfDesignResponses.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_PART_OF_DESIGN_BY_DESIGN_ID);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_PART_OF_DESIGN_BY_DESIGN_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(partOfDesignResponses));
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping(APIConstant.PartOfDesignAPI.GET_ALL_PART_OF_DESIGN)
    public ResponseEntity<ObjectNode> getAllPartOfDesign() {
        ObjectNode response = objectMapper.createObjectNode();
        var partOfDesignResponses = partOfDesignService.getAllPartOfDesign();
        if (partOfDesignResponses.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_PART_OF_DESIGN);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_PART_OF_DESIGN_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(partOfDesignResponses));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.PartOfDesignAPI.GET_PART_OF_DESIGN_BY_ID + "/{partOfDesignID}")
    public ResponseEntity<ObjectNode> getDesignByID( @PathVariable("partOfDesignID") String partOfDesignID) {
        ObjectNode response = objectMapper.createObjectNode();
        var partOfDesignResponse = partOfDesignService.getPartOfDesignByPartOfDesignID(partOfDesignID);
        if (partOfDesignResponse == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_PART_OF_DESIGN);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_PART_OF_DESIGN_BY_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(partOfDesignResponse));
        }
        return ResponseEntity.ok(response);
    }
}
