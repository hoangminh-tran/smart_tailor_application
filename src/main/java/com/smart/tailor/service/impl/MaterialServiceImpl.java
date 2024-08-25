package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Material;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.MaterialMapper;
import com.smart.tailor.repository.MaterialRepository;
import com.smart.tailor.service.CategoryService;
import com.smart.tailor.service.ExcelExportService;
import com.smart.tailor.service.ExcelImportService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.CategoryResponse;
import com.smart.tailor.utils.response.ErrorDetail;
import com.smart.tailor.utils.response.MaterialResponse;
import com.smart.tailor.utils.response.MaterialWithPriceResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final Logger logger = LoggerFactory.getLogger(MaterialServiceImpl.class);
    private final CategoryService categoryService;
    private final MaterialMapper materialMapper;
    private final ExcelImportService excelImportService;
    private final ExcelExportService excelExportService;

    @Override
    public Optional<Material> findByMaterialNameAndCategory_CategoryName(String materialName, String categoryName) {
        return materialRepository.findByMaterialNameIgnoreCaseAndCategory_CategoryNameIgnoreCase(materialName, categoryName);
    }

    @Override
    @Transactional
    public void createMaterial(MaterialRequest materialRequest) {
        var category = categoryService.findByCategoryName(materialRequest.getCategoryName())
                .orElseThrow(() -> new ItemNotFoundException("Can Not Find Category with Category Name: " + materialRequest.getCategoryName()));

        Optional<Material> categoryMaterialOptional = findByMaterialNameAndCategory_CategoryName(materialRequest.getMaterialName(), materialRequest.getCategoryName());
        Optional<Material> materialOptional = findByMaterialName(materialRequest.getMaterialName());

        if (materialOptional.isPresent() || categoryMaterialOptional.isPresent()) {
            throw new ItemAlreadyExistException("Material Information with Material Name " + materialRequest.getMaterialName() + " is existed");
        }

        materialRepository.save(
                Material
                        .builder()
                        .materialName(materialRequest.getMaterialName())
                        .category(category)
                        .hsCode(materialRequest.getHsCode())
                        .unit(materialRequest.getUnit())
                        .basePrice(materialRequest.getBasePrice())
                        .status(true)
                        .build()
        );
    }

    @Override
    public Boolean isExistedMaterial(MaterialRequest materialRequest) {
        return materialRepository.existsByMaterialNameIgnoreCaseAndCategory_CategoryNameIgnoreCaseAndHsCodeAndUnitIgnoreCaseAndBasePrice(
                materialRequest.getMaterialName(),
                materialRequest.getCategoryName(),
                materialRequest.getHsCode(),
                materialRequest.getUnit(),
                materialRequest.getBasePrice()
        );
    }


    @Override
    public void createMaterialByExcelFile(MultipartFile file) {
        if (!excelImportService.isValidExcelFile(file)) {
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            List<Pair<Integer, MaterialRequest>> materialRequests = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            Set<MaterialRequest> duplicateExcelData = new HashSet<>();
            XSSFSheet sheet = workbook.getSheet("Category and Material");

            if (sheet == null) {
                throw new ExcelFileNotSupportException(MessageConstant.WRONG_TYPE_OF_CATEGORY_AND_MATERIAL_EXCEL_FILE);
            }
            logger.info("Inside getCategoryMaterialDataFromExcel Method");

            boolean inValidData = false;
            List<ErrorDetail> errorFields = new ArrayList<>();

            int rowIndex = 3;
            while (rowIndex <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowCompletelyEmptyForCategoryMaterial(row)) {
                    rowIndex++;
                    continue;
                }

                List<String> errors = new ArrayList<>();
                MaterialRequest materialRequest = new MaterialRequest();
                boolean rowDataValid = true;
                boolean isValid = false;
                String message = "";
                for (int cellIndex = 0; cellIndex < 5; cellIndex++) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        inValidData = true;
                        rowDataValid = false;
                        errors.add(getCellNameForCategoryMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " is empty");
                    } else {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    var categoryName = cell.getStringCellValue();
                                    if (categoryName.length() > 50) {
                                        errors.add(getCellNameForCategoryMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must not exceed 50 characters");
                                        inValidData = true;
                                        rowDataValid = false;
                                    } else {
                                        materialRequest.setCategoryName(categoryName);
                                    }
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForCategoryMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must be data type string");
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    var materialName = cell.getStringCellValue();
                                    if (materialName.length() > 50) {
                                        errors.add(getCellNameForCategoryMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must not exceed 50 characters");
                                        inValidData = true;
                                        rowDataValid = false;
                                    } else {
                                        materialRequest.setMaterialName(materialName);
                                    }
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForCategoryMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must be data type string");
                                }
                                break;
                            case 2:
                                isValid = false;
                                BigInteger bigIntegerValue = null;
                                message = " must be a valid positive integer";

                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        try {
                                            double numericValue = cell.getNumericCellValue();
                                            bigIntegerValue = BigDecimal.valueOf(numericValue).toBigInteger();
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                        }
                                        break;
                                    case STRING:
                                        try {
                                            String stringValue = cell.getStringCellValue();
                                            bigIntegerValue = new BigInteger(stringValue);
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                        }
                                        break;
                                    default:
                                        isValid = false;
                                        break;
                                }

                                if (isValid && bigIntegerValue != null && bigIntegerValue.compareTo(BigInteger.ZERO) >= 0) {
                                    materialRequest.setHsCode(bigIntegerValue);
                                } else {
                                    if (bigIntegerValue != null && bigIntegerValue.compareTo(BigInteger.ZERO) < 0) {
                                        message = " must be a positive integer";
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForCategoryMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + message);
                                }
                                break;
                            case 3:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    var unit = cell.getStringCellValue();
                                    if (unit.length() > 50) {
                                        errors.add(getCellNameForCategoryMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must not exceed 50 characters");
                                        inValidData = true;
                                        rowDataValid = false;
                                    } else {
                                        materialRequest.setUnit(unit);
                                    }
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForCategoryMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + " must be data type string");
                                }
                                break;
                            case 4:
                                isValid = false;
                                Integer basePrice = null;
                                message = " must be data type integer";
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        try {
                                            double numericValue = cell.getNumericCellValue();
                                            if (numericValue >= Integer.MIN_VALUE && numericValue <= Integer.MAX_VALUE) {
                                                basePrice = (int) numericValue;
                                                isValid = true;
                                            } else {
                                                isValid = false;
                                            }
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                        }
                                        break;
                                    case STRING:
                                        try {
                                            basePrice = Integer.parseInt(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                        }
                                        break;
                                }
                                if (isValid && basePrice != null && basePrice >= 0) {
                                    materialRequest.setBasePrice(basePrice);
                                } else {
                                    if (isValid && basePrice != null && basePrice < 0) {
                                        message = " must be positive integer";
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForCategoryMaterial(cellIndex) + " at row Index " + (rowIndex + 1) + message);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }

                if (!duplicateExcelData.add(materialRequest)) {
                    errors.add("Duplicate Material Request Data at row Index " + (rowIndex + 1) + " in excel file");
                }

                var category = categoryService.findByCategoryName(materialRequest.getCategoryName());
                if (category.isEmpty()) {
                    errors.add("Category_Name at row Index " + (rowIndex + 1) + " not found");
                }

                if (rowDataValid) {
                    if (isExistedMaterial(materialRequest)) {
                        errors.add("Material_Name at row Index " + (rowIndex + 1) + " is existed");
                    }

                    if (errors.isEmpty()) {
                        var material = materialRepository.findByMaterialNameIgnoreCaseAndCategory_CategoryNameIgnoreCase(materialRequest.getMaterialName(), materialRequest.getCategoryName());
                        var materialID = material.isPresent() ? material.get().getMaterialID() : Utilities.generateCustomPrimaryKey();
                        materialRepository.save(
                                Material
                                        .builder()
                                        .materialID(materialID)
                                        .materialName(materialRequest.getMaterialName())
                                        .category(category.get())
                                        .hsCode(materialRequest.getHsCode())
                                        .unit(materialRequest.getUnit())
                                        .basePrice(materialRequest.getBasePrice())
                                        .status(true)
                                        .build()
                        );
                        materialRequests.add(Pair.of(rowIndex + 1, materialRequest));
                    }
                }
                if (!errors.isEmpty())
                    errorFields.add(new ErrorDetail(errors));
                rowIndex++;
            }

            if (materialRequests.isEmpty() && errorFields.isEmpty()) {
                throw new BadRequestException("Material Request Excel File Has Empty Data");
            }

            if (!errorFields.isEmpty()) {
                throw new ExcelFileInvalidDataTypeException("The Material Excel File have " + materialRequests.size() + " create Success and " + errorFields.size() + " create Failure", errorFields);
            }
        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
    }

    private boolean isRowCompletelyEmptyForCategoryMaterial(Row row) {
        for (int cellIndex = 0; cellIndex < 5; cellIndex++) {
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellNameForCategoryMaterial(int cellIndex) {
        switch (cellIndex) {
            case 0:
                return "Category_Name";
            case 1:
                return "Material_Name";
            case 2:
                return "HS_Code";
            case 3:
                return "Unit";
            case 4:
                return "Base_Price";
            default:
                return "Unknown";
        }
    }

    @Override
    public List<MaterialResponse> findAllMaterials() {
        return materialRepository
                .findAll()
                .stream()
                .map(materialMapper::mapperToMaterialResponseWithoutPrices)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findAllActiveMaterials() {
        return materialRepository
                .findAll()
                .stream()
                .filter(material -> material.getStatus())
                .map(materialMapper::mapperToMaterialResponseWithoutPrices)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialResponse findByMaterialNameAndCategoryName(String materialName, String categoryName) {
        var materialOptional = findByMaterialNameAndCategory_CategoryName(materialName, categoryName);
        if (materialOptional.isPresent()) {
            return materialMapper.mapperToMaterialResponseWithoutPrices(materialOptional.get());
        }

        return null;
    }

    @Override
    public List<MaterialResponse> exportCategoryMaterialForBrandByExcel(HttpServletResponse response) throws IOException {
        var materialResponses = findAllActiveMaterials();
        excelExportService.exportCategoryMaterialForBrand(materialResponses, response);
        return materialResponses;
    }

    @Override
    public MaterialResponse findByMaterialID(String materialID) {
        if (Utilities.isStringNotNullOrEmpty(materialID.toString())) {
            var material = materialRepository.findById(materialID)
                    .orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + materialID));

            return materialMapper.mapperToMaterialResponseWithoutPrices(material);
        }
        return null;
    }

    @Transactional
    @Override
    public void updateMaterial(String materialID, MaterialRequest materialRequest) {
        var material = materialRepository.findById(materialID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + materialID));

        var categoryOptional = categoryService.findByCategoryName(materialRequest.getCategoryName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Category with CategoryName: " + materialRequest.getCategoryName()));

        materialRepository.save(
                Material
                        .builder()
                        .materialID(materialID)
                        .materialName(materialRequest.getMaterialName())
                        .category(categoryOptional)
                        .hsCode(materialRequest.getHsCode())
                        .unit(materialRequest.getUnit())
                        .basePrice(materialRequest.getBasePrice())
                        .status(material.getStatus())
                        .build()
        );
    }

    @Transactional
    @Override
    public void updateStatusMaterial(String materialID) {
        var material = materialRepository.findByMaterialID(materialID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + materialID));

        material.setStatus(!material.getStatus());
        materialRepository.save(material);
    }

    @Override
    public void generateSampleCategoryMaterialByExportExcel(HttpServletResponse response) throws IOException {
        String[] categoryNames = categoryService
                .findAllCatgories()
                .stream()
                .filter(categoryResponse -> categoryResponse.getStatus())
                .map(CategoryResponse::getCategoryName)
                .toList()
                .toArray(String[]::new);

        excelExportService.exportSampleCategoryMaterial(response, categoryNames);
    }

    @Override
    public Optional<Material> findByMaterialName(String materialName) {
        return materialRepository.findByMaterialName(materialName);
    }

    @Override
    public Optional<Material> findMaterialByID(String materialID) {
        return materialRepository.findById(materialID);
    }

    @Override
    public List<MaterialResponse> findListMaterialByCategoryID(String categoryID) {
        var category = categoryService.findCategoryOptionalByID(categoryID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Category with CategoryID: " + categoryID));

        return materialRepository
                .findListMaterialByCategoryID(categoryID)
                .stream()
                .map(materialMapper::mapperToMaterialResponseWithoutPrices)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findListMaterialByCategoryName(String categoryName) {
        var category = categoryService.findByCategoryName("Can not find Category with CategoryName: " + categoryName)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_CATEGORY));

        return materialRepository
                .findListMaterialByCategoryName("%" + categoryName + "%")
                .stream()
                .map(materialMapper::mapperToMaterialResponseWithoutPrices)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialWithPriceResponse> findAllMaterialByExpertTailoringIDAndCategoryID(String expertTailoringID, String categoryID) {
        return materialRepository
                .findAllMaterialByExpertTailoringIDAndCategoryID(expertTailoringID, categoryID)
                .stream()
                .map(material -> {
                    var minPrice = materialRepository.getMinPriceByMaterialID(material.getMaterialID());
                    var maxPrice = materialRepository.getMaxPriceByMaterialID(material.getMaterialID());
                    return materialMapper.mapperToMaterialResponseWithPrices(material, minPrice, maxPrice);
                })
                .toList();
    }

    @Override
    public List<Material> findMaterialsByExpertTailoringIDAndCategoryName(String expertTailoringID, String categoryName) {
        return materialRepository
                .findMaterialsByExpertTailoringIDAndCategoryName(expertTailoringID, categoryName)
                .stream()
                .toList();
    }
}
