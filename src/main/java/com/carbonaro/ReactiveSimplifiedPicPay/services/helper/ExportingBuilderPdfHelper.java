package com.carbonaro.ReactiveSimplifiedPicPay.services.helper;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class ExportingBuilderPdfHelper {

    private final TemplateEngine templateEngine;

    public <T> byte[] buildPdf(List<T> report) {
        String htmlContent = generateHtmlContent(report);
        return convertHtmlToPdf(htmlContent);
    }

    private <T> String generateHtmlContent(List<T> report) {
        Context context = new Context();
        context.setVariable("title", "Relatório de Extração");
        context.setVariable("headers", getHeaders(report));
        context.setVariable("rows", getRows(report));
        return templateEngine.process("report", context);
    }

    private <T> List<String> getHeaders(List<T> report) {
        if (report.isEmpty()) {
            return List.of();
        }
        return Stream.of(getAllFields(report.getFirst().getClass()))
                .map(field -> formatFieldName(field.getName()))
                .toList();
    }

    private <T> List<List<String>> getRows(List<T> report) {
        return report.stream()
                .map(item -> Stream.of(getAllFields(item.getClass()))
                        .map(field -> {
                            field.setAccessible(true);
                            try {
                                Object value = field.get(item);
                                if (value instanceof BigDecimal) {
                                    return formatCurrency((BigDecimal) value);
                                } else if (value instanceof List) {
                                    return formatList((List<?>) value);
                                } else {
                                    return value != null ? value.toString() : "";
                                }
                            } catch (IllegalAccessException e) {
                                return "";
                            }
                        })
                        .toList())
                .toList();
    }

    private Field[] getAllFields(Class<?> type) {
        if (type.getSuperclass() != null) {
            Field[] parentFields = getAllFields(type.getSuperclass());
            Field[] currentFields = type.getDeclaredFields();
            Field[] allFields = new Field[parentFields.length + currentFields.length];
            System.arraycopy(parentFields, 0, allFields, 0, parentFields.length);
            System.arraycopy(currentFields, 0, allFields, parentFields.length, currentFields.length);
            return allFields;
        }
        return type.getDeclaredFields();
    }

    private String formatFieldName(String fieldName) {
        StringBuilder formattedName = new StringBuilder();
        for (char c : fieldName.toCharArray()) {
            if (Character.isUpperCase(c)) {
                formattedName.append(" ");
            }
            formattedName.append(c);
        }
        return formattedName.toString().trim();
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

    private byte[] convertHtmlToPdf(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.useFastMode();
            builder.run();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error while generating PDF", e);
        }
    }
}
