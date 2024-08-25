package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.SizeExpertTailoringService;
import com.smart.tailor.utils.request.SizeExpertTailoringRequest;
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
@RequestMapping(APIConstant.SizeExpertTailoringAPI.SIZE_EXPERT_TAILORING)
@RequiredArgsConstructor
@Slf4j
@Validated
public class SizeExpertTailoringController {
    private final SizeExpertTailoringService sizeExpertTailoringService;
    private final Logger logger = LoggerFactory.getLogger(SizeExpertTailoringController.class);

    @GetMapping(APIConstant.SizeExpertTailoringAPI.GET_ALL_SIZE_EXPERT_TAILORING)
    public ResponseEntity<ObjectNode> findAllSizeExpertTailoring() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var sizeExpertTailoringResponses = sizeExpertTailoringService.findAllSizeExpertTailoring();
        if (!sizeExpertTailoringResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_SIZE_EXPERT_TAILORING_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(sizeExpertTailoringResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SIZE_EXPERT_TAILORING);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.SizeExpertTailoringAPI.GET_ALL_SIZE_BY_EXPERT_TAILORING + "/{expectTailoringID}")
    public ResponseEntity<ObjectNode> findAllSizeByExpectTailoringID(@PathVariable("expectTailoringID") String expectTailoringID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var sizeExpertTailoringResponses = sizeExpertTailoringService.findAllSizeExpertTailoringID(expectTailoringID);
        if (!sizeExpertTailoringResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_SIZE_EXPERT_TAILORING_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(sizeExpertTailoringResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SIZE_EXPERT_TAILORING);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.SizeExpertTailoringAPI.ADD_NEW_SIZE_EXPERT_TAILORING)
    public ResponseEntity<ObjectNode> addNewSize(@Valid @RequestBody SizeExpertTailoringRequest sizeExpertTailoringRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        sizeExpertTailoringService.createSizeExpertTailoring(sizeExpertTailoringRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_SIZE_EXPERT_TAILORING_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.SizeExpertTailoringAPI.UPDATE_SIZE_EXPERT_TAILORING)
    public ResponseEntity<ObjectNode> updateSize(@Valid @RequestBody SizeExpertTailoringRequest sizeExpertTailoringRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        sizeExpertTailoringService.updateSizeExpertTailoring(sizeExpertTailoringRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.UPDATE_SIZE_EXPERT_TAILORING_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.SizeExpertTailoringAPI.GENERATE_SAMPLE_SIZE_EXPERT_TAILORING_BY_EXCEL_FILE)
    public ResponseEntity<ObjectNode> generateSampleSizeExpertTailoringByExcelFile(HttpServletResponse httpServletResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
//        httpServletResponse.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Generate_Size_Expert_Tailoring.xlsx";
        httpServletResponse.setHeader(headerKey, headerValue);

        sizeExpertTailoringService.generateSampleSizeExpertTailoringByExcelFile(httpServletResponse);

        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.GENERATE_SAMPLE_SIZE_EXPERT_TAILORING_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.SizeExpertTailoringAPI.ADD_NEW_SIZE_EXPERT_TAILORING_BY_EXCEL_FILE)
    public ResponseEntity<ObjectNode> addNewSizeExpertTailoringByExcelFile(@RequestParam("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        sizeExpertTailoringService.createSizeExpertTailoringByExcelFile(file);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_SIZE_EXPERT_TAILORING_BY_EXCEL_FILE_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}
