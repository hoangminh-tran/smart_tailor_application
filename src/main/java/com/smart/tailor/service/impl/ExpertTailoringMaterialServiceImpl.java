package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.ExpertTailoringMaterial;
import com.smart.tailor.entities.ExpertTailoringMaterialKey;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.ExpertTailoringMaterialMapper;
import com.smart.tailor.repository.ExpertTailoringMaterialRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.ExpertTailoringMaterialListRequest;
import com.smart.tailor.utils.request.ExpertTailoringMaterialRequest;
import com.smart.tailor.utils.response.ErrorDetail;
import com.smart.tailor.utils.response.ExpertTailoringMaterialResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpertTailoringMaterialServiceImpl implements ExpertTailoringMaterialService {
    private final ExpertTailoringMaterialRepository expertTailoringMaterialRepository;
    private final ExpertTailoringService expertTailoringService;
    private final MaterialService materialService;
    private final ExpertTailoringMaterialMapper expertTailoringMaterialMapper;
    private final ExcelExportService excelExportService;
    private final ExcelImportService excelImportService;
    private final Logger logger = LoggerFactory.getLogger(ExpertTailoringMaterialServiceImpl.class);

    @Override
    public void createExpertTailoringMaterial(ExpertTailoringMaterialListRequest expertTailoringMaterialListRequest) {
        String categoryName = expertTailoringMaterialListRequest.getCategoryName();
        String materialName = expertTailoringMaterialListRequest.getMaterialName();

        List<Object> errorDetails = new ArrayList<>();
        String errorMaterial = null;

        var material = materialService.findByMaterialNameAndCategory_CategoryName(
                materialName, categoryName);
        if (material.isEmpty()) {
            errorMaterial = "Can not find Material with Category Name: " + categoryName + " and Material Name: " + materialName;
        }

        for (String expertTailoringName : expertTailoringMaterialListRequest.getExpertTailoringNames()) {
            List<String> errors = new ArrayList<>();
            var expertTailoringMaterialRequest = ExpertTailoringMaterialRequest
                    .builder()
                    .materialName(materialName)
                    .categoryName(categoryName)
                    .expertTailoringName(expertTailoringName)
                    .build();

            var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(expertTailoringName);
            if (expertTailoring.isEmpty()) {
                errors.add("Can not find Expert Tailoring with Expert Tailoring Name: " + expertTailoringName);
            }

            var expertTailoringMaterialExisted = findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(
                    expertTailoring.get().getExpertTailoringID(), material.get().getMaterialID());

            if (!errorMaterial.isEmpty()) {
                errors.add(errorMaterial);
            }

            if (expertTailoringMaterialExisted.isPresent()) {
                errors.add("Expert Tailoring Material is Existed");
            }

            if (!errors.isEmpty()) {
                errorDetails.add(new ErrorDetail(expertTailoringMaterialRequest, errors));
            } else {
                ExpertTailoringMaterialKey expertTailoringMaterialKey = ExpertTailoringMaterialKey
                        .builder()
                        .expertTailoringID(expertTailoring.get().getExpertTailoringID())
                        .materialID(material.get().getMaterialID())
                        .build();

                ExpertTailoringMaterial expertTailoringMaterial = ExpertTailoringMaterial
                        .builder()
                        .expertTailoringMaterialKey(expertTailoringMaterialKey)
                        .expertTailoring(expertTailoring.get())
                        .material(material.get())
                        .status(true)
                        .build();

                expertTailoringMaterialRepository.save(expertTailoringMaterial);
            }
        }

        if (!errorDetails.isEmpty()) {
            throw new MultipleErrorException(HttpStatus.BAD_REQUEST, "Error occur When Create Size", errorDetails);
        }
    }

    @Override
    public Optional<ExpertTailoringMaterial> findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(String expertTailoringID, String materialID) {
        return expertTailoringMaterialRepository.findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(expertTailoringID, materialID);
    }

    @Transactional
    @Override
    public void changeStatusExpertTailoringMaterial(String expertTailoringID, String materialID) {
        var material = materialService.findMaterialByID(materialID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL));

        var expertTailoring = expertTailoringService.findExpertTailoringByID(expertTailoringID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING));

        var expertTailoringMaterialExisted = findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(
                expertTailoring.getExpertTailoringID(), material.getMaterialID())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_EXPERT_TAILORING_MATERIAL));

        expertTailoringMaterialExisted.setStatus(!expertTailoringMaterialExisted.getStatus());
        expertTailoringMaterialRepository.save(expertTailoringMaterialExisted);
    }

    @Override
    public List<ExpertTailoringMaterialResponse> findAllExpertTailoringMaterial() {
        return expertTailoringMaterialRepository
                .findAll()
                .stream()
                .map(expertTailoringMaterialMapper::mapperToExpertTailoringMaterialResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpertTailoringMaterialResponse> findAllActiveExpertTailoringMaterialByExpertTailoringID(String expertTailoringID) {
        return expertTailoringMaterialRepository
                .findAll()
                .stream()
                .filter(expertTailoringMaterial ->
                        expertTailoringMaterial.getExpertTailoringMaterialKey().getExpertTailoringID().toString().equals(expertTailoringID.toString()) &&
                                expertTailoringMaterial.getStatus())
                .map(expertTailoringMaterialMapper::mapperToExpertTailoringMaterialResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpertTailoringMaterialResponse> findAllActiveExpertTailoringMaterialByExpertTailoringName(String expertTailoringName) {
        return expertTailoringMaterialRepository
                .findAll()
                .stream()
                .filter(expertTailoringMaterial ->
                        expertTailoringMaterial.getExpertTailoring().getExpertTailoringName().equalsIgnoreCase(expertTailoringName) &&
                                expertTailoringMaterial.getStatus())
                .map(expertTailoringMaterialMapper::mapperToExpertTailoringMaterialResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void generateSampleExpertTailoringMaterial(HttpServletResponse response) throws IOException {
        var materialResponse = materialService.findAllActiveMaterials();
        excelExportService.exportSampleExpertTailoringMaterial(response, materialResponse);
    }

    @Transactional
    @Override
    public void createExpertTailoringMaterialByExcelFile(MultipartFile file) {
        if (!excelImportService.isValidExcelFile(file)) {
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            var excelData = excelImportService.getExpertTailoringMaterialDataFromExcel(file.getInputStream());

            if (excelData.isEmpty()) {
                throw new BadRequestException("Category and Material Excel File Has Empty Data");
            }

            Set<ExpertTailoringMaterialListRequest> excelNames = new HashSet<>();
            List<ExpertTailoringMaterialListRequest> uniqueExcelData = new ArrayList<>();
            List<Object> duplicateExcelData = new ArrayList<>();

            for (ExpertTailoringMaterialListRequest request : excelData) {
                if (!excelNames.add(request)) {
                    duplicateExcelData.add(request);
                } else {
                    uniqueExcelData.add(request);
                }
            }

            if (!duplicateExcelData.isEmpty()) {
                throw new ExcelFileDuplicateDataException(MessageConstant.DUPLICATE_EXPERT_TAILORING_MATERIAL_IN_EXCEL_FILE, duplicateExcelData);
            }

            List<Object> invalidData = new ArrayList<>();
            for (ExpertTailoringMaterialListRequest materialRequest : uniqueExcelData) {
                try {
                    createExpertTailoringMaterial(materialRequest);
                } catch (ItemNotFoundException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.CAN_NOT_FIND_ANY_MATERIAL;
                    logger.error("Error creating Expert Tailoring Material: Item not found - {}", errorMessage, ex);
                    invalidData.add(new ErrorDetail(materialRequest, errorMessage));
                } catch (DuplicateDataException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.EXPERT_TAILORING_MATERIAL_IS_EXISTED;
                    logger.error("Error creating Expert Tailoring Material: Already exists - {}", errorMessage, ex);
                    invalidData.add(new ErrorDetail(ex.getErrors(), errorMessage));
                } catch (Exception ex) {
                    logger.error("Error creating Expert Tailoring Material - {}", ex.getMessage());
                    invalidData.add(new ErrorDetail(materialRequest, ex.getMessage()));
                }
            }

            if (!invalidData.isEmpty()) {
                throw new ExcelFileInvalidDataTypeException("Some Data could not be processed correctly", invalidData);
            }
        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
    }
}
