package com.smart.tailor.service;

import com.smart.tailor.utils.request.ExpertTailoringMaterialListRequest;
import com.smart.tailor.utils.request.ExpertTailoringRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ExcelImportService {
    boolean isValidExcelFile(MultipartFile file);

    List<ExpertTailoringRequest> getExpertTailoringDataFromExcel(InputStream inputStream);

    List<ExpertTailoringMaterialListRequest> getExpertTailoringMaterialDataFromExcel(InputStream inputStream);
}
