package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.ExpertTailoringMaterialService;
import com.smart.tailor.utils.request.ExpertTailoringMaterialListRequest;
import com.smart.tailor.validate.ValidCustomKey;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping(APIConstant.ExpertTailoringMaterialAPI.EXPERT_TAILORING_MATERIAL)
@RequiredArgsConstructor
@Slf4j
@Validated
public class ExpertTailoringMaterialController {
    private final ExpertTailoringMaterialService expertTailoringMaterialService;
    private final Logger logger = LoggerFactory.getLogger(ExpertTailoringMaterialController.class);

    @GetMapping(APIConstant.ExpertTailoringMaterialAPI.GET_ALL_EXPERT_TAILORING_MATERIAL_BY_EXPERT_TAILORING_ID + "/{expertTailoringID}")
    public ResponseEntity<ObjectNode> getAllExpertTailoringMaterialByExpertTailoringID( @PathVariable("expertTailoringID") String expertTailoringID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var expertTailoringMaterialResponses = expertTailoringMaterialService.findAllActiveExpertTailoringMaterialByExpertTailoringID(expertTailoringID);
        if (!expertTailoringMaterialResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_EXPERT_TAILORING_MATERIAL_BY_EXPERT_TAILORING_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(expertTailoringMaterialResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING_MATERIAL);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.ExpertTailoringMaterialAPI.GET_ALL_EXPERT_TAILORING_MATERIAL_BY_EXPERT_TAILORING_NAME + "/{expertTailoringName}")
    public ResponseEntity<ObjectNode> getAllExpertTailoringMaterialByExpertTailoringName(@PathVariable("expertTailoringName") String expertTailoringName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var expertTailoringMaterialResponses = expertTailoringMaterialService.findAllActiveExpertTailoringMaterialByExpertTailoringName(expertTailoringName);
        if (!expertTailoringMaterialResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_EXPERT_TAILORING_MATERIAL_BY_EXPERT_TAILORING_NAME);
            response.set("data", objectMapper.valueToTree(expertTailoringMaterialResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING_MATERIAL);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.ExpertTailoringMaterialAPI.GET_ALL_EXPERT_TAILORING_MATERIAL)
    public ResponseEntity<ObjectNode> getAllExpertTailoringMaterial() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var expertTailoringMaterialResponses = expertTailoringMaterialService.findAllExpertTailoringMaterial();
        if (!expertTailoringMaterialResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_EXPERT_TAILORING_MATERIAL_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(expertTailoringMaterialResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING_MATERIAL);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.ExpertTailoringMaterialAPI.ADD_NEW_EXPERT_TAILORING_MATERIAL)
    public ResponseEntity<ObjectNode> addNewExpertTailoringMaterial(@Valid @RequestBody ExpertTailoringMaterialListRequest expertTailoringMaterialListRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        expertTailoringMaterialService.createExpertTailoringMaterial(expertTailoringMaterialListRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_EXPERT_TAILORING_MATERIAL_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.ExpertTailoringMaterialAPI.CHANGE_STATUS_EXPERT_TAILORING_MATERIAL)
    public ResponseEntity<ObjectNode> changeStatusExpertTailoringMaterial( @RequestParam("expertTailoringID") String expertTailoringID,
                                                                           @RequestParam("materialID") String materialID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        expertTailoringMaterialService.changeStatusExpertTailoringMaterial(expertTailoringID, materialID);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.UPDATE_EXPERT_TAILORING_MATERIAL_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.ExpertTailoringMaterialAPI.GENERATE_SAMPLE_CATEGORY_MATERIAL_EXPERT_TAILORING_BY_EXCEL_FILE)
    public ResponseEntity<ObjectNode> generateSampleExpertTailoringMaterial(HttpServletResponse httpServletResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = Generate_Sample_Expert_Tailoring_Material.xlsx";
        httpServletResponse.setHeader(headerKey, headerValue);
        expertTailoringMaterialService.generateSampleExpertTailoringMaterial(httpServletResponse);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.GENERATE_SAMPLE_EXPERT_TAILORING_MATERIAL_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.ExpertTailoringMaterialAPI.ADD_NEW_EXPERT_TAILORING_MATERIAL_BY_EXCEL_FILE)
    public ResponseEntity<ObjectNode> addNewExpertTailoringMaterialByExcelFile(@RequestParam("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        expertTailoringMaterialService.createExpertTailoringMaterialByExcelFile(file);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_EXPERT_TAILORING_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}
