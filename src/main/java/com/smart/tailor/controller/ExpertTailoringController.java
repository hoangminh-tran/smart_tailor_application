package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.ExpertTailoringService;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
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
@RequestMapping(APIConstant.ExpertTailoringAPI.EXPERT_TAILORING)
@RequiredArgsConstructor
@Slf4j
@Validated
public class ExpertTailoringController {
    private final ExpertTailoringService expertTailoringService;
    private final Logger logger = LoggerFactory.getLogger(ExpertTailoringController.class);
    private final ObjectMapper objectMapper;

    @GetMapping(APIConstant.ExpertTailoringAPI.GET_ALL_EXPERT_TAILORING)
    public ResponseEntity<ObjectNode> getAllExpertTailoring() {
        ObjectNode response = objectMapper.createObjectNode();
        var expertTailoring = expertTailoringService.getAllExpertTailoring();
        if (!expertTailoring.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_EXPERT_TAILORING_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(expertTailoring));
            return ResponseEntity.ok(response);
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.ExpertTailoringAPI.GET_ALL_EXPERT_TAILORING_BY_EXPERT_TAILORING_NAME + "/{expertTailoringName}")
    public ResponseEntity<ObjectNode> getExpertTailoringByName(@PathVariable("expertTailoringName") String expertTailoringName) {
        ObjectNode response = objectMapper.createObjectNode();
        var expertTailoring = expertTailoringService.getExpertTailoringResponseByExpertTailoringName(expertTailoringName);
        if (expertTailoring != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_EXPERT_TAILORING_BY_NAME_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(expertTailoring));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.ExpertTailoringAPI.ADD_NEW_EXPERT_TAILORING)
    public ResponseEntity<ObjectNode> addNewExpertTailoring(@Valid @RequestBody ExpertTailoringRequest expertTailoringRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        expertTailoringService.createExpertTailoring(expertTailoringRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_NEW_EXPERT_TAILORING_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }


    @PostMapping(APIConstant.ExpertTailoringAPI.ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE)
    public ResponseEntity<ObjectNode> addNewExpertTailoringByExcelFile(@RequestParam("file") MultipartFile file) {
        ObjectNode response = objectMapper.createObjectNode();
        expertTailoringService.createExpertTailoringByExcelFile(file);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_NEW_EXPERT_TAILORING_BY_EXCEL_FILE_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

//    @GetMapping(APIConstant.ExpertTailoringAPI.GET_ALL_EXPERT_TAILORING_BY_EXCEL_FILE)
//    public ResponseEntity<ObjectNode> getAllExpertTailoringByExcelFile(HttpServletResponse httpServletResponse) throws IOException {
//        ObjectNode response = objectMapper.createObjectNode();
//        try {
//            httpServletResponse.setContentType("application/octet-stream");
//            String headerKey = "Content-Disposition";
//            String headerValue = "attachment; filename = Expert_Tailoring_List.xlsx";
//            httpServletResponse.setHeader(headerKey, headerValue);
//            var materials = expertTailoringService.getAllExpertTailoringByExportExcelData(httpServletResponse);
//            if (!materials.isEmpty()) {
//                response.put("status", HttpStatus.OK.value());
//                response.put("message", MessageConstant.GET_ALL_BRAND_MATERIAL_SUCCESSFULLY);
//                response.set("data", objectMapper.valueToTree(materials));
//            } else {
//                response.put("status", HttpStatus.NOT_FOUND.value());
//                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_BRAND_MATERIAL);
//            }
//            return ResponseEntity.ok(response);
//        } catch (Exception ex) {
//            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
//            logger.error("ERROR IN GET ALL EXPERT TAILORING BY EXCEL FILE. ERROR MESSAGE: {}", ex.getMessage());
//            return ResponseEntity.ok(response);
//        }
//    }

    @GetMapping(APIConstant.ExpertTailoringAPI.GENERATE_SAMPLE_EXPERT_TAILORING_BY_EXCEL_FILE)
    public ResponseEntity<ObjectNode> generateSampleExpertTailoringByExcelFile(HttpServletResponse httpServletResponse) throws IOException {
        ObjectNode response = objectMapper.createObjectNode();
        httpServletResponse.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = Generate_Sample_Expert_Tailoring.xlsx";
        httpServletResponse.setHeader(headerKey, headerValue);
        expertTailoringService.generateSampleExpertTailoringByExportExcel(httpServletResponse);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.GENERATE_SAMPLE_EXPERT_TAILORING_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.ExpertTailoringAPI.GET_EXPERT_TAILORING_BY_ID + "/{expertTailoringID}")
    public ResponseEntity<ObjectNode> getExpertTailoringByID( @PathVariable("expertTailoringID") String expertTailoringID) {
        ObjectNode response = objectMapper.createObjectNode();
        var materials = expertTailoringService.findByExpertTailoringID(expertTailoringID);
        if (materials != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_EXPERT_TAILORING_BY_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(materials));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.ExpertTailoringAPI.UPDATE_STATUS_EXPERT_TAILORING + "/{expertTailoringID}")
    public ResponseEntity<ObjectNode> changeStatusExpertTailoring( @PathVariable("expertTailoringID") String expertTailoringID) {
        ObjectNode response = objectMapper.createObjectNode();
        expertTailoringService.updateStatusExpertTailoring(expertTailoringID);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.CHANGE_STATUS_EXPERT_TAILORING_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.ExpertTailoringAPI.UPDATE_EXPERT_TAILORING + "/{expertTailoringID}")
    public ResponseEntity<ObjectNode> updateExpertTailoring( @PathVariable("expertTailoringID") String expertTailoringID,
                                                            @Valid @RequestBody ExpertTailoringRequest expertTailoringRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        expertTailoringService.updateExpertTailoring(expertTailoringID, expertTailoringRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.UPDATE_EXPERT_TAILORING_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}
