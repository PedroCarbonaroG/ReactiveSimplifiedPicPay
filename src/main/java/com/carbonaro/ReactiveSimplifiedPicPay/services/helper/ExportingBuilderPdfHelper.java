package com.carbonaro.ReactiveSimplifiedPicPay.services.helper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ExportingBuilderPdfHelper {

    private ExportingServiceHelper exportingServiceHelper;

    private static final String TRANSACTION_TEMPLATE = "transaction_templates/transaction_header.html";

    @SneakyThrows
    public <T> Mono<byte[]> buildPdf(T report) {

        StringBuilder sb = new StringBuilder();

        List<Field> fields = Stream.concat(
                Arrays.stream(report.getClass().getDeclaredFields()),
                Arrays.stream(report.getClass().getSuperclass().getDeclaredFields()))
                .toList();
        fields.forEach(self -> self.setAccessible(true));

        sb.append(exportingServiceHelper.processHtmlTemplate(TRANSACTION_TEMPLATE, object));

        return ExportingServiceHelper.convertHtmlToPdf(sb.toString());
    }

}
