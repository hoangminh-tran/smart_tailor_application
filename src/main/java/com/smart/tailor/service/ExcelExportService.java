package com.smart.tailor.service;

import com.smart.tailor.utils.response.ExpertTailoringResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface ExcelExportService {
    void exportExpertTailoringData(List<ExpertTailoringResponse> expertTailoringResponse, HttpServletResponse response) throws IOException;

    void exportCategoryMaterialForBrand(List<MaterialResponse> materialResponses, HttpServletResponse response) throws IOException;

    void exportSampleExpertTailoring(HttpServletResponse response) throws IOException;

    void exportSampleCategoryMaterial(HttpServletResponse response, String[] categoryNames) throws IOException;

    void exportSampleSizeExpertTailoring(HttpServletResponse response, String[] expertTailoringNames, String[] sizeNames) throws IOException;

    void exportSampleExpertTailoringMaterial(HttpServletResponse response, List<MaterialResponse> materialResponses) throws IOException;
}
