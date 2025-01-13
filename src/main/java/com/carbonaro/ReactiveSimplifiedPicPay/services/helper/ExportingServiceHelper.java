package com.carbonaro.ReactiveSimplifiedPicPay.services.helper;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ExportingServiceHelper {

    private TemplateEngine templateEngine;

    private static final String PDF_EXP = "pdf";
    private static final String XLSX_EXP = "xlsx";
    private static final String BASE_ATTACHMENT = "attachment; filename={0} - {1}.{2}";
    private static final String APPLICATION_XLSX_VALUE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";

    public static Mono<ResponseEntity<byte[]>> exportingResponse(byte[] exportingBytes, FileTypeEnum fileType, Class<?> clazz) {

        HttpHeaders header = new HttpHeaders();

        String fileName = clazz.getSimpleName().concat("_").concat("Extraction");
        String extractionDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        if (fileType.equals(FileTypeEnum.EXCEL)) {
            header.setContentType(MediaType.parseMediaType(APPLICATION_XLSX_VALUE));
            header.set(HttpHeaders.CONTENT_DISPOSITION, MessageFormat.format(BASE_ATTACHMENT, fileName, extractionDate, XLSX_EXP));
        } else if (fileType.equals(FileTypeEnum.PDF)) {
            header.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE));
            header.set(HttpHeaders.CONTENT_DISPOSITION, MessageFormat.format(BASE_ATTACHMENT, fileName, extractionDate, PDF_EXP));
        }

        header.setContentLength(exportingBytes.length);
        return Mono.just(new ResponseEntity<>(exportingBytes, header, HttpStatus.OK));
    }

    public static Mono<byte[]> convertHtmlToPdf(String contentHtml) {

        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(contentHtml, StringUtils.EMPTY);
        builder.toStream(actual);
        builder.useFastMode();
        try {
            builder.run();
        } catch (IOException e) {
            return Mono.error(e);
        }

        return Mono.just(actual.toByteArray());
    }

    public String processHtmlTemplate(String template, Object object) {

        Map<String, Object> parameters = generateMapFieldValue(object);
        return processHtmlTemplate(template, parameters);
    }

    public String processHtmlTemplate(String template, Map<String, Object> object) {

        Context context = new Context();
        context.setVariables(object);
        return templateEngine.process(template, context);
    }

    public static <T> Collection<List<T>> partitionBasedOnSize(List<T> inputList, int size) {

        final AtomicInteger counter = new AtomicInteger(0);
        return inputList.stream()
                .collect(Collectors.groupingBy(s -> counter.getAndIncrement()/size))
                .values();
    }

    private static Map<String, Object> generateMapFieldValue(Object object) {

        Map<String, Object> parameters = new HashMap<>();

        ReflectionUtils.doWithFields(object.getClass(), field -> {
            field.setAccessible(true);
            parameters.put(
                    field.getName(),
                    field.get(object) == null ? "" : field.get(object)
            );
        });

        return parameters;
    }

}
