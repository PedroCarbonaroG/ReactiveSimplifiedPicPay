package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IExportingAPI;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;

@RestController
public class IExportingImpl implements IExportingAPI {

    @Override
    public Mono<ResponseEntity<byte[]>> getPdfExtraction() {

        return Mono
                .just(extractionPDF());
    }

    @Override
    public Mono<ResponseEntity<byte[]>> getExcelExtraction() {
        return null;
    }

    @SneakyThrows
    private ResponseEntity<byte[]> extractionPDF() {

        var workbook = createExcel();
        var baos = new ByteArrayOutputStream();

        workbook.write(baos);

        byte[] excelBytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exemplo.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(excelBytes);
    }
    private Workbook createExcel() {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Exemplo");

        // Cria linhas e células com alguns dados fictícios
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nome");
        headerRow.createCell(2).setCellValue("Email");

        Row dataRow1 = sheet.createRow(1);
        dataRow1.createCell(0).setCellValue(1);
        dataRow1.createCell(1).setCellValue("Fulano de Tal");
        dataRow1.createCell(2).setCellValue("fulano@example.com");

        Row dataRow2 = sheet.createRow(2);
        dataRow2.createCell(0).setCellValue(2);
        dataRow2.createCell(1).setCellValue("Ciclano da Silva");
        dataRow2.createCell(2).setCellValue("ciclano@example.com");

        return workbook;
    }

}
