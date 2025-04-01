package com.carbonaro.ReactiveSimplifiedPicPay.services.helper;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExportingBuilderExcelHelper {

    private static final String TITLE = "Excel Extraction - date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    public <T> byte[] buildExcel(List<T> data) {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            createHeader(workbook, sheet);
            createDataRows(workbook, sheet, data);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error while building Excel file", e);
        }
    }

    private void createHeader(XSSFWorkbook workbook, Sheet sheet) {

        CellStyle headerStyle = createHeaderStyle(workbook);
        createTitleRow(sheet, headerStyle);
        createInfoRow(sheet, headerStyle);
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        return headerStyle;
    }

    private void createTitleRow(Sheet sheet, CellStyle headerStyle) {

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(TITLE);
        titleCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
    }

    private void createInfoRow(Sheet sheet, CellStyle headerStyle) {

        Row infoRow = sheet.createRow(1);
        Cell infoCell = infoRow.createCell(0);
        infoCell.setCellValue("Informações da Extração");
        infoCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
    }

    private <T> void createDataRows(XSSFWorkbook workbook, Sheet sheet, List<T> data) {

        if (data == null || data.isEmpty()) {
            return;
        }

        CellStyle headerStyle = createDataHeaderStyle(workbook);
        createDataHeaderRow(sheet, data.getFirst(), headerStyle);
        createDataContentRows(sheet, data);
        autoSizeColumns(sheet, data.getFirst().getClass().getDeclaredFields().length);
    }

    private CellStyle createDataHeaderStyle(XSSFWorkbook workbook) {

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        return headerStyle;
    }

    private <T> void createDataHeaderRow(Sheet sheet, T firstItem, CellStyle headerStyle) {

        Row headerRow = sheet.createRow(2);
        Field[] fields = firstItem.getClass().getDeclaredFields();
        int colIndex = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(field.getName());
            cell.setCellStyle(headerStyle);
        }
    }

    private <T> void createDataContentRows(Sheet sheet, List<T> data) {

        int rowIndex = 3;
        for (T item : data) {
            Row dataRow = sheet.createRow(rowIndex++);
            createDataContentRow(dataRow, item);
        }
    }

    private <T> void createDataContentRow(Row dataRow, T item) {

        Field[] fields = item.getClass().getDeclaredFields();
        int colIndex = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            Cell cell = dataRow.createCell(colIndex++);
            try {
                Object value = field.get(item);
                if (value != null) {
                    if (value instanceof BigDecimal) {
                        cell.setCellValue(formatCurrency((BigDecimal) value));
                    } else if (value instanceof List) {
                        cell.setCellValue(formatList((List<?>) value));
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error while accessing field value", e);
            }
        }
    }

    private void autoSizeColumns(Sheet sheet, int numberOfColumns) {

        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private String formatCurrency(BigDecimal value) {
        return "R$ " + String.format("%,.2f", value);
    }

    private String formatList(List<?> list) {

        StringBuilder formattedList = new StringBuilder();
        for (Object item : list) {
            formattedList.append(item.toString()).append(", ");
        }

        return !formattedList.isEmpty() ? formattedList.substring(0, formattedList.length() - 2) : "";
    }

}
