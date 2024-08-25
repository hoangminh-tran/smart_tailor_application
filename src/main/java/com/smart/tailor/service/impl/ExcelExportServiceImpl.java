package com.smart.tailor.service.impl;


import com.smart.tailor.service.ExcelExportService;
import com.smart.tailor.service.SystemPropertiesService;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelExportServiceImpl implements ExcelExportService {
    private final SystemPropertiesService systemPropertiesService;

    private void createCell(Row row, int columnIndex, Object value, CellStyle style, XSSFSheet sheet) {
        Cell cell = row.createCell(columnIndex);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDateTime localDateTime) {
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            cell.setCellValue(date);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDate localDate) {
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            cell.setCellValue(date);
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        }  else if (value instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) value;
            // Check if the BigDecimal fits into an Integer or Long
            if (bigDecimal.scale() <= 0 && bigDecimal.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) <= 0) {
                if (bigDecimal.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) <= 0) {
                    cell.setCellValue(bigDecimal.intValue());
                } else {
                    cell.setCellValue(bigDecimal.longValue());
                }
            } else {
                // Convert to String if it's too large
                cell.setCellValue(bigDecimal.toString());
            }
        } else if (value instanceof BigInteger) {
            BigInteger bigInteger = (BigInteger) value;
            // Check if the BigInteger fits into an Integer or Long
            if (bigInteger.bitLength() <= 31) {
                cell.setCellValue(bigInteger.intValue());
            } else if (bigInteger.bitLength() <= 63) {
                cell.setCellValue(bigInteger.longValue());
            } else {
                // Convert to String if it's too large
                cell.setCellValue(bigInteger.toString());
            }
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(style);
        sheet.autoSizeColumn(columnIndex);
    }

    @Override
    public void exportExpertTailoringData(List<ExpertTailoringResponse> expertTailoringResponses, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Expert Tailoring List");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Expert Tailoring List", style, sheet);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        font.setFontHeightInPoints((short) 10);

        // Create Header of Excel Sheet
        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Expert_Tailoring_Name", style, sheet);
        createCell(row, 1, "Size_Image_Url", style, sheet);
        createCell(row, 2, "Create_Date", style, sheet);
        createCell(row, 3, "Last_Modified_Date", style, sheet);

        // Write Data from DB to Excel Sheet
        int rowIndex = 2;
        CellStyle styleData = workbook.createCellStyle();
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);
        styleData.setFont(fontData);
        styleData.setAlignment(HorizontalAlignment.CENTER);

        // Format CreateDate and LastModifiedDate
        CellStyle dateTimeCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateTimeCellStyle.setFont(fontData);
        dateTimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss.00"));
        dateTimeCellStyle.setAlignment(HorizontalAlignment.CENTER);

        for (var expertTailoring : expertTailoringResponses) {
            Row rowSheet = sheet.createRow(rowIndex++);
            int countIndex = 0;
            createCell(rowSheet, countIndex++, expertTailoring.getExpertTailoringName(), styleData, sheet);
            createCell(rowSheet, countIndex++, expertTailoring.getSizeImageUrl(), styleData, sheet);
            createCell(rowSheet, countIndex++, expertTailoring.getCreateDate(), dateTimeCellStyle, sheet);
            createCell(rowSheet, countIndex++, expertTailoring.getLastModifiedDate(), dateTimeCellStyle, sheet);
        }

        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    @Override
    public void exportCategoryMaterialForBrand(List<MaterialResponse> materialResponses, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Brand Material");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setLocked(true);
        createCell(row, 0, "Category and Material List", titleStyle, sheet);
        CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(rangeAddress);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, rangeAddress, sheet);
        font.setFontHeightInPoints((short) 10);

        // Create Explanation Row
        row = sheet.createRow(1);
        XSSFFont explanationFont = workbook.createFont();
        explanationFont.setBold(false);
        explanationFont.setFontHeight(14);
        CellStyle explanationStyle = workbook.createCellStyle();
        explanationStyle.setBorderTop(BorderStyle.MEDIUM);
        explanationStyle.setBorderBottom(BorderStyle.MEDIUM);
        explanationStyle.setBorderLeft(BorderStyle.MEDIUM);
        explanationStyle.setBorderRight(BorderStyle.MEDIUM);
        explanationStyle.setAlignment(HorizontalAlignment.LEFT);
        explanationStyle.setWrapText(true);
        explanationStyle.setFont(explanationFont);
//        explanationStyle.setLocked(true);

        var priceVariationPercentageForMaterial = Double.parseDouble(systemPropertiesService.getByName("PRICE_VARIATION_PERCENTAGE_FOR_MATERIAL").getPropertyValue());
        var lowerBrandPricePercentage = (100 - priceVariationPercentageForMaterial * 100);
        var upperBrandPricePercentage = (100 + priceVariationPercentageForMaterial * 100);
        String explanation = "Explanation:\n"
                + "• Category_Name: Only values from the predefined list of categories in the Smart Tailor application are allowed.\n"
                + "• Base_Price: Only positive integer values are allowed, and the currency unit is VND.\n"
                + "• HS_Code: Only positive integer values are allowed.\n"
                + "• Brand_Price: Only positive integer values are allowed, and the currency unit is VND. Brand_Price must be between " + lowerBrandPricePercentage + "% and " + upperBrandPricePercentage + "% of Base_Price.\n" +
                "If the brand does not have a material for this entry, the field may be left blank.\n";

        // Create a cell and set the style
        Cell explanationCell = row.createCell(0);
        explanationCell.setCellValue(explanation);
        explanationCell.setCellStyle(explanationStyle);

        // Merge cells for the explanation row
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

        // Apply borders to the merged region
        CellRangeAddress explanationRange = new CellRangeAddress(1, 1, 0, 5);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, explanationRange, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, explanationRange, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, explanationRange, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, explanationRange, sheet);

        // Set Row Height for Explanation Row
        row.setHeightInPoints(107); // Adjust height as needed

        // Create Header of Excel Sheet
        row = sheet.createRow(2);
        font.setBold(true);
        font.setFontHeight(16);
        titleStyle.setFont(font);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.cloneStyleFrom(titleStyle);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);

        createCell(row, 0, "Category_Name", headerStyle, sheet);
        createCell(row, 1, "Material_Name", headerStyle, sheet);
        createCell(row, 2, "HS_Code", headerStyle, sheet);
        createCell(row, 3, "Unit", headerStyle, sheet);
        createCell(row, 4, "Base_Price", headerStyle, sheet);
        createCell(row, 5, "Brand_Price", headerStyle, sheet);

        // Write Data from DB to Excel Sheet
        int rowIndex = 3;
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);

        CellStyle lockedData = workbook.createCellStyle();
        lockedData.setLocked(true);
        lockedData.setFont(fontData);
        lockedData.setAlignment(HorizontalAlignment.CENTER);
        lockedData.setBorderTop(BorderStyle.MEDIUM);
        lockedData.setBorderBottom(BorderStyle.MEDIUM);
        lockedData.setBorderLeft(BorderStyle.MEDIUM);
        lockedData.setBorderRight(BorderStyle.MEDIUM);

        CellStyle styleData = workbook.createCellStyle();
        styleData.setLocked(false);
        styleData.setFont(fontData);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setBorderTop(BorderStyle.MEDIUM);
        styleData.setBorderBottom(BorderStyle.MEDIUM);
        styleData.setBorderLeft(BorderStyle.MEDIUM);
        styleData.setBorderRight(BorderStyle.MEDIUM);

        for (var materialResponse : materialResponses) {
            Row rowSheet = sheet.createRow(rowIndex++);
            int countIndex = 0;
            createCell(rowSheet, countIndex++, materialResponse.getCategoryName(), lockedData, sheet);
            createCell(rowSheet, countIndex++, materialResponse.getMaterialName(), lockedData, sheet);
            createCell(rowSheet, countIndex++, materialResponse.getHsCode(), lockedData, sheet);
            createCell(rowSheet, countIndex++, materialResponse.getUnit(), lockedData, sheet);
            createCell(rowSheet, countIndex++, materialResponse.getBasePrice(), lockedData, sheet);
            createCell(rowSheet, countIndex++, null, styleData, sheet);
        }

        // Auto Size Column to fit content
        for (int i = 0; i < 6; ++i) {
            sheet.autoSizeColumn(i);
        }

        // Set Password to Unlock Columns and Rows
        sheet.protectSheet("Aa@123456");

        // Apply data validation for Brand Price column
        int lastRow = sheet.getLastRowNum();
        String brandPriceFormula = "AND(F3>=1, E3>=F3*" + (1 - priceVariationPercentageForMaterial) + ", E3<=F3*" + (1 + priceVariationPercentageForMaterial) + ")";
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();

        CellRangeAddressList brandPriceAddressList = new CellRangeAddressList(2, lastRow, 5, 5);

        DataValidationConstraint brandPriceConstraint = validationHelper.createCustomConstraint(brandPriceFormula);

        DataValidation brandPriceValidation = validationHelper.createValidation(brandPriceConstraint, brandPriceAddressList);

        brandPriceValidation.setShowErrorBox(true);
        brandPriceValidation.createErrorBox("Invalid Input", "Brand Price must be between " + (100 - priceVariationPercentageForMaterial * 100) + "% and " + (100 + priceVariationPercentageForMaterial * 100) + "% of Base Price.");

        sheet.addValidationData(brandPriceValidation);

        // Set column widths
        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 55 * 256);
        sheet.setColumnWidth(2, 21 * 256);
        sheet.setColumnWidth(3, 18 * 256);
        sheet.setColumnWidth(4, 18 * 256);
        sheet.setColumnWidth(5, 18 * 256);

        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


    @Override
    public void exportSampleExpertTailoring(HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Expert Tailoring");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setLocked(true);
        createCell(row, 0, "Expert Tailoring List", style, sheet);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        font.setFontHeightInPoints((short) 10);

        // Create Header of Excel Sheet
        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Expert_Tailoring_Name", style, sheet);
        createCell(row, 1, "Size_Image_Url", style, sheet);

        int rowIndex = 2;
        CellStyle styleData = workbook.createCellStyle();
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);
        styleData.setFont(fontData);
        styleData.setLocked(false);
        styleData.setAlignment(HorizontalAlignment.CENTER);

        // Set Password to Unlock Columns and Rows
        sheet.protectSheet("Aa@123456");

        // Unlocked For Specific Cells
        for (int i = 2; i <= 200; ++i) {
            row = sheet.createRow(i);
            Cell cellA = row.createCell(0);
            Cell cellB = row.createCell(1);
            cellA.setCellStyle(styleData);
            cellB.setCellStyle(styleData);
        }

        // Create Data Validation for String
        DataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheet);

        // Apply Constraint to Cell 0 Which is Expert Tailoring Name
        DataValidationConstraint constraint = dataValidationHelper.createCustomConstraint("ISTEXT(A3)");
        CellRangeAddressList expertTailoringNameRange = new CellRangeAddressList(2, 200, 0, 0);
        DataValidation expertTailoringNameValidation = dataValidationHelper.createValidation(constraint, expertTailoringNameRange);
        expertTailoringNameValidation.setShowErrorBox(true);
        expertTailoringNameValidation.createErrorBox("Invalid Input", "Expert Tailoring Name must be Type String");
        sheet.addValidationData(expertTailoringNameValidation);

        // Apply Constraint to Cell 0 Which is Expert Tailoring Name
        constraint = dataValidationHelper.createCustomConstraint("ISTEXT(B3)");
        CellRangeAddressList expertTailoringUrlRange = new CellRangeAddressList(2, 200, 1, 1);
        DataValidation expertTailoringUrlValidation = dataValidationHelper.createValidation(constraint, expertTailoringUrlRange);
        expertTailoringUrlValidation.setShowErrorBox(true);
        expertTailoringUrlValidation.createErrorBox("Invalid Input", "Expert Tailoring Url must be Type String");
        sheet.addValidationData(expertTailoringUrlValidation);

        // Set Width for Specific Column
        sheet.setColumnWidth(0, 30 * 256);
        sheet.setColumnWidth(1, 160 * 256);

        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    @Override
    public void exportSampleCategoryMaterial(HttpServletResponse response, String[] categoryNames) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Category and Material");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setLocked(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Category and Material", style, sheet);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        font.setFontHeightInPoints((short) 10);

        // Create Explanation Row
        row = sheet.createRow(1);
        XSSFFont explanationFont = workbook.createFont();
        explanationFont.setBold(false);
        explanationFont.setFontHeight(14);
        CellStyle explanationStyle = workbook.createCellStyle();
        explanationStyle.setAlignment(HorizontalAlignment.LEFT);
        explanationStyle.setWrapText(true);
        font.setBold(false);
        font.setFontHeight(14);
        explanationStyle.setFont(explanationFont);
//        explanationStyle.setLocked(true);

        String explanation = "Explanation:\n"
                + "• Category_Name: Only values from the predefined list of categories in the Smart Tailor application are allowed.\n"
                + "• Base_Price: Only positive integer values are allowed, and the currency unit is VND.\n"
                + "• HS_Code: Only positive integer values are allowed.";

        createCell(row, 0, explanation, explanationStyle, sheet);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

        // Set Row Height for Explanation Row
        row.setHeightInPoints(72); // Adjust height as needed

        // Create Header of Excel Sheet
        row = sheet.createRow(2);
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight(16);
        style.setFont(headerFont);
        createCell(row, 0, "Category_Name", style, sheet);
        createCell(row, 1, "Material_Name", style, sheet);
        createCell(row, 2, "HS_Code", style, sheet);
        createCell(row, 3, "Unit", style, sheet);
        createCell(row, 4, "Base_Price", style, sheet);

        int rowIndex = 3;
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);
        CellStyle styleData = workbook.createCellStyle();
        styleData.setLocked(false);
        styleData.setFont(fontData);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        // Set Password to Unlock Columns and Rows
        sheet.protectSheet("Aa@123456");

        // Unlocked For Specific Cells
        for (int i = rowIndex; i <= 200; ++i) {
            row = sheet.createRow(i);
            Cell cellA = row.createCell(0);
            Cell cellB = row.createCell(1);
            Cell cellC = row.createCell(2);
            Cell cellD = row.createCell(3);
            Cell cellE = row.createCell(4);
            cellA.setCellStyle(styleData);
            cellB.setCellStyle(styleData);
            cellC.setCellStyle(styleData);
            cellD.setCellStyle(styleData);
            cellE.setCellStyle(styleData);
        }

        // Create Data Validation for String
        DataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheet);

        // Apply Constraint to Cell 0 <=> CategoryName
        DataValidationConstraint constraint = dataValidationHelper.createExplicitListConstraint(categoryNames);
        CellRangeAddressList categoryNameRange = new CellRangeAddressList(3, 200, 0, 0);
        DataValidation categoryNameValidation = dataValidationHelper.createValidation(constraint, categoryNameRange);
        categoryNameValidation.setShowErrorBox(true);
        categoryNameValidation.createErrorBox("Invalid Input", "Category Name must be one of predefined values");
        sheet.addValidationData(categoryNameValidation);

        // Apply Constraint to Cell 1 <=> MaterialName
        constraint = dataValidationHelper.createCustomConstraint("ISTEXT(B4)");
        CellRangeAddressList materialNameRange = new CellRangeAddressList(3, 200, 1, 1);
        DataValidation materialNameValidation = dataValidationHelper.createValidation(constraint, materialNameRange);
        materialNameValidation.setShowErrorBox(true);
        materialNameValidation.createErrorBox("Invalid Input", "Material Name must be Type String");
        sheet.addValidationData(materialNameValidation);

        // Apply Constraint to Cell 2 <=> HSCode
        constraint = dataValidationHelper.createCustomConstraint("AND(ISNUMBER(C4), C4 >= 0)");
        CellRangeAddressList hsCodeRange = new CellRangeAddressList(3, 200, 2, 2);
        DataValidation hsCodeValidation = dataValidationHelper.createValidation(constraint, hsCodeRange);
        hsCodeValidation.setShowErrorBox(true);
        hsCodeValidation.createErrorBox("Invalid Input", "HS Code must be Type Number And Non-Negative Number");
        sheet.addValidationData(hsCodeValidation);

        // Apply Constraint to Cell 3 <=> Unit
        constraint = dataValidationHelper.createCustomConstraint("ISTEXT(D4)");
        CellRangeAddressList unitRange = new CellRangeAddressList(3, 200, 3, 3);
        DataValidation unitValidation = dataValidationHelper.createValidation(constraint, unitRange);
        unitValidation.setShowErrorBox(true);
        unitValidation.createErrorBox("Invalid Input", "Unit must be Type String");
        sheet.addValidationData(unitValidation);

        // Apply Constraint to Cell 4 <=> BasePrice
        constraint = dataValidationHelper.createCustomConstraint("AND(ISNUMBER(E4), E4 >= 0)");
        CellRangeAddressList basePriceRange = new CellRangeAddressList(3, 200, 4, 4);
        DataValidation basePriceValidation = dataValidationHelper.createValidation(constraint, basePriceRange);
        basePriceValidation.setShowErrorBox(true);
        basePriceValidation.createErrorBox("Invalid Input", "Base Price must be Type Number And Non-Negative Number");
        sheet.addValidationData(basePriceValidation);

        // Set Width for Specific Columns
        sheet.setColumnWidth(0, 25 * 256); // Adjust width of the column with explanation
        sheet.setColumnWidth(1, 55 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 18 * 256);
        sheet.setColumnWidth(4, 18 * 256);

        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    @Override
    public void exportSampleSizeExpertTailoring(HttpServletResponse response, String[] expertTailoringNames, String[] sizeNames) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Size Expert Tailoring");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setLocked(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Size Expert Tailoring", style, sheet);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        font.setFontHeightInPoints((short) 10);

        // Create Header of Excel Sheet
        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Expert_Tailoring_Name", style, sheet);
        createCell(row, 1, "Size_Name", style, sheet);
        createCell(row, 2, "Ratio", style, sheet);

        int rowIndex = 2;
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);
        CellStyle styleData = workbook.createCellStyle();
        styleData.setLocked(false);
        styleData.setFont(fontData);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        // Set Password to Unlock Columns and Rows
        sheet.protectSheet("Aa@123456");

        // Unlocked For Specific Cells
        for (int i = 2; i <= 300; ++i) {
            row = sheet.createRow(i);
            Cell cellA = row.createCell(0);
            Cell cellB = row.createCell(1);
            Cell cellC = row.createCell(2);
            Cell cellD = row.createCell(3);
            Cell cellE = row.createCell(4);
            cellA.setCellStyle(styleData);
            cellB.setCellStyle(styleData);
            cellC.setCellStyle(styleData);
            cellD.setCellStyle(styleData);
            cellE.setCellStyle(styleData);
        }

        // Create Data Validation for String
        DataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheet);

        // Apply Constraint to Cell 0 <=> expertTailoringName
        DataValidationConstraint constraint = dataValidationHelper.createExplicitListConstraint(expertTailoringNames);
        CellRangeAddressList expertTailoringNameRange = new CellRangeAddressList(2, 300, 0, 0);
        DataValidation expertTailoringNameValidation = dataValidationHelper.createValidation(constraint, expertTailoringNameRange);
        expertTailoringNameValidation.setShowErrorBox(true);
        expertTailoringNameValidation.createErrorBox("Invalid Input", "Expert Tailoring Name must be one of the predefined values");
        sheet.addValidationData(expertTailoringNameValidation);

        // Apply Constraint to Cell 1 <=> sizeName
        constraint = dataValidationHelper.createExplicitListConstraint(sizeNames);
        CellRangeAddressList sizeNameRange = new CellRangeAddressList(2, 300, 1, 1);
        DataValidation sizeNameValidation = dataValidationHelper.createValidation(constraint, sizeNameRange);
        sizeNameValidation.setShowErrorBox(true);
        sizeNameValidation.createErrorBox("Invalid Input", "Size Name must be one of the predefined values");
        sheet.addValidationData(sizeNameValidation);

        // Apply Constraint to Cell 2 <=> Ratio
        constraint = dataValidationHelper.createCustomConstraint("AND(ISNUMBER(C3), C3 >= 0)");
        CellRangeAddressList ratioRange = new CellRangeAddressList(2, 300, 2, 2);
        DataValidation ratioValidation = dataValidationHelper.createValidation(constraint, ratioRange);
        ratioValidation.setShowErrorBox(true);
        ratioValidation.createErrorBox("Invalid Input", "Ratio must be Type Number And Non-Negative Number");
        sheet.addValidationData(ratioValidation);

        // Set Width for Specific Column
        sheet.setColumnWidth(0, 35 * 256);
        sheet.setColumnWidth(1, 35 * 256);
        sheet.setColumnWidth(2, 18 * 256);

        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    @Override
    public void exportSampleExpertTailoringMaterial(HttpServletResponse response, List<MaterialResponse> materialResponses) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Expert Tailoring Material");

        // Create Title Row of Excel Sheet
        Row row = sheet.createRow(0);
        CellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Expert Tailoring Material (use comma to separate multiple values)", titleStyle, sheet);
        CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0, 2);
        sheet.addMergedRegion(rangeAddress);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, rangeAddress, sheet);
        font.setFontHeightInPoints((short) 10);

        // Create Header of Excel Sheet
        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        titleStyle.setFont(font);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.cloneStyleFrom(titleStyle);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);

        createCell(row, 0, "Category_Name", headerStyle, sheet);
        createCell(row, 1, "Material_Name", headerStyle, sheet);
        createCell(row, 2, "Expert_Tailoring_Name", headerStyle, sheet);

        // Write Data from DB to Excel Sheet
        int rowIndex = 2;
        XSSFFont fontData = workbook.createFont();
        fontData.setBold(false);
        fontData.setFontHeight(14);

        CellStyle lockedData = workbook.createCellStyle();
        lockedData.setLocked(true);
        lockedData.setFont(fontData);
        lockedData.setAlignment(HorizontalAlignment.CENTER);
        lockedData.setBorderTop(BorderStyle.MEDIUM);
        lockedData.setBorderBottom(BorderStyle.MEDIUM);
        lockedData.setBorderLeft(BorderStyle.MEDIUM);
        lockedData.setBorderRight(BorderStyle.MEDIUM);

        CellStyle styleData = workbook.createCellStyle();
        styleData.setLocked(false);
        styleData.setFont(fontData);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setBorderTop(BorderStyle.MEDIUM);
        styleData.setBorderBottom(BorderStyle.MEDIUM);
        styleData.setBorderLeft(BorderStyle.MEDIUM);
        styleData.setBorderRight(BorderStyle.MEDIUM);

        for (var materialResponse : materialResponses) {
            Row rowSheet = sheet.createRow(rowIndex++);
            int countIndex = 0;
            createCell(rowSheet, countIndex++, materialResponse.getCategoryName(), lockedData, sheet);
            createCell(rowSheet, countIndex++, materialResponse.getMaterialName(), lockedData, sheet);
            createCell(rowSheet, countIndex++, null, styleData, sheet);
        }

        // Set Password to Unlock Columns and Rows
        sheet.protectSheet("Aa@123456");

        // Apply Constraint to Cell 3 <=> Expert Tailoring
        DataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint constraint = dataValidationHelper.createCustomConstraint("AND(ISTEXT(C3))");

        CellRangeAddressList expertTailoringNameRange = new CellRangeAddressList(2, 300, 2, 2);
        DataValidation expertTailoringNameValidation = dataValidationHelper.createValidation(constraint, expertTailoringNameRange);
        expertTailoringNameValidation.setShowErrorBox(true);
        expertTailoringNameValidation.createErrorBox("Invalid Input",
                "Expert Tailoring Name must be Type String. If multiple values, they have to be separated by commas."
        );
        sheet.addValidationData(expertTailoringNameValidation);

        // Set Width for Specific Column
        sheet.setColumnWidth(0, 28 * 256);
        sheet.setColumnWidth(1, 50 * 256);
        sheet.setColumnWidth(2, 50 * 256);


        // Export Data to Excel
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
