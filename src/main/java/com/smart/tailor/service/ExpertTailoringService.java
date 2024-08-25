package com.smart.tailor.service;

import com.smart.tailor.config.CustomExeption;
import com.smart.tailor.entities.ExpertTailoring;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface ExpertTailoringService {
    Optional<ExpertTailoring> getExpertTailoringByID(String expectID) throws CustomExeption;

    void createExpertTailoring(ExpertTailoringRequest expertTailoringRequest);

    ExpertTailoringResponse mapperToExpertTailoringResponse(ExpertTailoring expertTailoring);

    List<ExpertTailoringResponse> getAllExpertTailoring();

    ExpertTailoringResponse getExpertTailoringResponseByExpertTailoringName(String expertTailoringName);

    Optional<ExpertTailoring> getExpertTailoringByExpertTailoringName(String expertTailoringName);

    void createExpertTailoringByExcelFile(MultipartFile file);

    List<ExpertTailoringResponse> getAllExpertTailoringByExportExcelData(HttpServletResponse response) throws IOException;

    void generateSampleExpertTailoringByExportExcel(HttpServletResponse response) throws IOException;

    ExpertTailoringResponse findByExpertTailoringID(String expertTailoringID);

    void updateExpertTailoring(String expertTailoringID, ExpertTailoringRequest expertTailoringRequest);

    void updateStatusExpertTailoring(String expertTailoringID);

    Optional<ExpertTailoring> findExpertTailoringByID(String expertTailoringID);
}
