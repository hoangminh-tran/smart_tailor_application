package com.smart.tailor.service;

import com.smart.tailor.utils.request.SizeExpertTailoringRequest;
import com.smart.tailor.utils.response.SizeExpertTailoringResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface SizeExpertTailoringService {
    void createSizeExpertTailoring(SizeExpertTailoringRequest sizeExpertTailoringRequest);

    List<SizeExpertTailoringResponse> findAllSizeExpertTailoring();

    List<SizeExpertTailoringResponse> findAllSizeExpertTailoringID(String expertTailoringID);

    SizeExpertTailoringResponse findSizeExpertTailoringByExpertTailoringIDAndSizeID(String expertTailoringID, String sizeID);

    void updateSizeExpertTailoring(SizeExpertTailoringRequest sizeExpertTailoringRequest);

    void createSizeExpertTailoringByExcelFile(MultipartFile file);

    void generateSampleSizeExpertTailoringByExcelFile(HttpServletResponse response) throws IOException;
}
