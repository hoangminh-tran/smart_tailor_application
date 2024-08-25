package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.SizeExpertTailoring;
import com.smart.tailor.entities.SizeExpertTailoringKey;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.SizeExpertTailoringMapper;
import com.smart.tailor.repository.SizeExpertTailoringRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.request.SizeExpertTailoringRequest;
import com.smart.tailor.utils.response.ErrorDetail;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import com.smart.tailor.utils.response.SizeExpertTailoringResponse;
import com.smart.tailor.utils.response.SizeResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SizeExpertTailoringServiceImpl implements SizeExpertTailoringService {
    private final SizeExpertTailoringRepository sizeExpertTailoringRepository;
    private final SizeService sizeService;
    private final ExpertTailoringService expertTailoringService;
    private final SizeExpertTailoringMapper sizeExpertTailoringMapper;
    private final ExcelImportService excelImportService;
    private final ExcelExportService excelExportService;
    private final Logger logger = LoggerFactory.getLogger(SizeExpertTailoringServiceImpl.class);

    @Transactional
    @Override
    public void createSizeExpertTailoring(SizeExpertTailoringRequest sizeExpertTailoringRequest) {
        var size = sizeService.findBySizeName(sizeExpertTailoringRequest.getSizeName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Size with SizeName:" + sizeExpertTailoringRequest.getSizeName()));

        var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(sizeExpertTailoringRequest.getExpertTailoringName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Expert Tailoring with ExpertTailoringName: " + sizeExpertTailoringRequest.getExpertTailoringName()));

        var sizeExpertTailoringExisted = sizeExpertTailoringRepository.existsByExpertTailoringExpertTailoringNameAndSizeSizeNameAndRatio(
                sizeExpertTailoringRequest.getExpertTailoringName(),
                sizeExpertTailoringRequest.getSizeName(),
                sizeExpertTailoringRequest.getRatio()
        );

        if (sizeExpertTailoringExisted) {
            throw new ItemAlreadyExistException("Size Expert Tailoring is existed");
        }

        SizeExpertTailoringKey sizeExpertTailoringKey = SizeExpertTailoringKey
                .builder()
                .expertTailoringID(expertTailoring.getExpertTailoringID())
                .sizeID(size.getSizeID())
                .build();

        sizeExpertTailoringRepository.save(
                SizeExpertTailoring
                        .builder()
                        .sizeExpertTailoringKey(sizeExpertTailoringKey)
                        .size(size)
                        .expertTailoring(expertTailoring)
                        .ratio(sizeExpertTailoringRequest.getRatio())
                        .status(true)
                        .build()
        );
    }

    @Override
    public List<SizeExpertTailoringResponse> findAllSizeExpertTailoring() {
        return sizeExpertTailoringRepository
                .findAll()
                .stream()
                .map(sizeExpertTailoringMapper::mapperToSizeExpertTailoringResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SizeExpertTailoringResponse> findAllSizeExpertTailoringID(String expectTailoringID) {
        return sizeExpertTailoringRepository
                .findAll()
                .stream()
                .filter(sizeExpertTailoring -> {
                    return sizeExpertTailoring.getExpertTailoring().getExpertTailoringID().equals(expectTailoringID);
                })
                .map(sizeExpertTailoringMapper::mapperToSizeExpertTailoringResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SizeExpertTailoringResponse findSizeExpertTailoringByExpertTailoringIDAndSizeID(String expertTailoringID, String sizeID) {
        return sizeExpertTailoringRepository
                .findSizeExpertTailoringByExpertTailoringExpertTailoringIDAndSizeSizeID(expertTailoringID, sizeID)
                .map(sizeExpertTailoringMapper::mapperToSizeExpertTailoringResponse)
                .orElse(null);
    }

    @Transactional
    @Override
    public void updateSizeExpertTailoring(SizeExpertTailoringRequest sizeExpertTailoringRequest) {
        var size = sizeService.findBySizeName(sizeExpertTailoringRequest.getSizeName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Size with SizeName:" + sizeExpertTailoringRequest.getSizeName()));

        var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(sizeExpertTailoringRequest.getExpertTailoringName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Expert Tailoring with ExpertTailoringName: " + sizeExpertTailoringRequest.getExpertTailoringName()));

        var sizeExpertTailoringExisted = sizeExpertTailoringRepository.existsByExpertTailoringExpertTailoringNameAndSizeSizeNameAndRatio(
                sizeExpertTailoringRequest.getExpertTailoringName(),
                sizeExpertTailoringRequest.getSizeName(),
                sizeExpertTailoringRequest.getRatio()
        );

        if (sizeExpertTailoringExisted) {
            throw new ItemAlreadyExistException("Size Expert Tailoring is existed");
        }

        SizeExpertTailoringKey sizeExpertTailoringKey = SizeExpertTailoringKey
                .builder()
                .expertTailoringID(expertTailoring.getExpertTailoringID())
                .sizeID(size.getSizeID())
                .build();

        sizeExpertTailoringRepository.save(
                SizeExpertTailoring
                        .builder()
                        .sizeExpertTailoringKey(sizeExpertTailoringKey)
                        .size(size)
                        .expertTailoring(expertTailoring)
                        .ratio(sizeExpertTailoringRequest.getRatio())
                        .status(true)
                        .build()
        );
    }

    @Override
    public void createSizeExpertTailoringByExcelFile(MultipartFile file) {
        if (!excelImportService.isValidExcelFile(file)) {
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            List<Pair<Integer, SizeExpertTailoringRequest>> sizeExpertTailoringRequests = new ArrayList<>();
            List<ErrorDetail> errorFields = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheet("Size Expert Tailoring");

            if (sheet == null) {
                throw new ExcelFileNotSupportException(MessageConstant.WRONG_TYPE_OF_CATEGORY_AND_MATERIAL_EXCEL_FILE);
            }
            logger.info("Inside getSizeExpertTailoringRequestFromExcel Method");

            boolean inValidData = false;
            Set<SizeExpertTailoringRequest> duplicateExcelData = new HashSet<>();

            int rowIndex = 2;
            while (rowIndex <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowCompletelyEmptyForSizeExpertTailoring(row)) {
                    rowIndex++;
                    continue;
                }

                List<String> errors = new ArrayList<>();
                SizeExpertTailoringRequest sizeExpertTailoringRequest = new SizeExpertTailoringRequest();
                boolean rowDataValid = true;
                boolean isValid = false;
                String message = "";
                for (int cellIndex = 0; cellIndex < 3; cellIndex++) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        inValidData = true;
                        rowDataValid = false;
                        errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + " is empty!");
                    } else {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    var expertTailoringName = cell.getStringCellValue();
                                    if (expertTailoringName.length() > 50) {
                                        errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + " must not exceed 50 characters");
                                        inValidData = true;
                                        rowDataValid = false;
                                    } else {
                                        sizeExpertTailoringRequest.setExpertTailoringName(expertTailoringName);
                                    }
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + " must be data type string");
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                                    var sizeName = cell.getStringCellValue();
                                    if (sizeName.length() > 50) {
                                        errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + " must not exceed 5 characters");
                                        inValidData = true;
                                        rowDataValid = false;
                                    } else {
                                        sizeExpertTailoringRequest.setSizeName(sizeName);
                                    }
                                } else {
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + " must be data type string");
                                }
                                break;
                            case 2:
                                isValid = false;
                                double ratio = -1;
                                message = " must be data type numeric";
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        ratio = (double) cell.getNumericCellValue();
                                        isValid = true;
                                        break;
                                    case STRING:
                                        try {
                                            ratio = Double.parseDouble(cell.getStringCellValue());
                                            isValid = true;
                                        } catch (NumberFormatException e) {
                                            isValid = false;
                                        }
                                        break;
                                }
                                if (isValid && ratio >= 0) {
                                    sizeExpertTailoringRequest.setRatio(ratio);
                                } else {
                                    if (isValid && ratio < 0) {
                                        message = " must be positive numeric";
                                    }
                                    inValidData = true;
                                    rowDataValid = false;
                                    errors.add(getCellNameForSizeExpertTailoring(cellIndex) + " at row Index " + (rowIndex + 1) + message);
                                }
                                break;
                        }

                    }
                }

                if (!duplicateExcelData.add(sizeExpertTailoringRequest)) {
                    errors.add("Duplicate Material Request Data at row Index " + (rowIndex + 1) + " in excel file");
                }

                var size = sizeService.findBySizeName(sizeExpertTailoringRequest.getSizeName());
                if (size.isEmpty()) {
                    errors.add("Size_Name at row Index " + (rowIndex + 1) + " Not Found!");
                }

                var expertTailoring = expertTailoringService.getExpertTailoringByExpertTailoringName(sizeExpertTailoringRequest.getExpertTailoringName());
                if (expertTailoring.isEmpty()) {
                    errors.add("Expert_Tailoring_Name at row Index " + (rowIndex + 1) + " Not Found!");
                }

                if (rowDataValid) {
                    var sizeExpertTailoringExisted = sizeExpertTailoringRepository.existsByExpertTailoringExpertTailoringNameAndSizeSizeNameAndRatio(
                            sizeExpertTailoringRequest.getExpertTailoringName(),
                            sizeExpertTailoringRequest.getSizeName(),
                            sizeExpertTailoringRequest.getRatio()
                    );

                    if (sizeExpertTailoringExisted) {
                        errors.add("Size Expert Tailoring is Existed");
                    }

                    if (errors.isEmpty()) {
                        SizeExpertTailoringKey sizeExpertTailoringKey = SizeExpertTailoringKey
                                .builder()
                                .expertTailoringID(expertTailoring.get().getExpertTailoringID())
                                .sizeID(size.get().getSizeID())
                                .build();

                        sizeExpertTailoringRepository.save(
                                SizeExpertTailoring
                                        .builder()
                                        .sizeExpertTailoringKey(sizeExpertTailoringKey)
                                        .size(size.get())
                                        .expertTailoring(expertTailoring.get())
                                        .ratio(sizeExpertTailoringRequest.getRatio())
                                        .status(true)
                                        .build()
                        );
                        sizeExpertTailoringRequests.add(Pair.of(rowIndex + 1, sizeExpertTailoringRequest));
                    }
                }

                if (!errors.isEmpty())
                    errorFields.add(new ErrorDetail(errors));
                rowIndex++;
            }

            if (sizeExpertTailoringRequests.isEmpty() && errorFields.isEmpty()) {
                throw new BadRequestException("Material Request Excel File Has Empty Data");
            }

            if (!errorFields.isEmpty()) {
                throw new ExcelFileInvalidDataTypeException("The Size Expert Tailoring Excel File have " + sizeExpertTailoringRequests.size() + " create Success and " + errorFields.size() + " create Failure", errorFields);
            }

        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
    }

    private boolean isRowCompletelyEmptyForSizeExpertTailoring(Row row) {
        for (int cellIndex = 0; cellIndex < 3; cellIndex++) {
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellNameForSizeExpertTailoring(int cellIndex) {
        switch (cellIndex) {
            case 0:
                return "Expert_Tailoring_Name";
            case 1:
                return "Size_Name";
            case 2:
                return "Ratio";
            default:
                return "Unknown";
        }
    }

    @Override
    public void generateSampleSizeExpertTailoringByExcelFile(HttpServletResponse response) throws IOException {
        String[] expertTailoringNames = expertTailoringService
                .getAllExpertTailoring()
                .stream()
                .map(ExpertTailoringResponse::getExpertTailoringName)
                .toList()
                .toArray(String[]::new);

        String[] sizeNames = sizeService
                .findAllSizeResponse()
                .stream()
                .map(SizeResponse::getSizeName)
                .toList()
                .toArray(String[]::new);

        excelExportService.exportSampleSizeExpertTailoring(response, expertTailoringNames, sizeNames);
    }
}
