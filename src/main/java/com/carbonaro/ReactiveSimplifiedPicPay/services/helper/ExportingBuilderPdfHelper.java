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
    public Mono<byte[]> buildPdf(Object object) {

        StringBuilder sb = new StringBuilder();

        List<Field> fields = Stream.concat(
                Arrays.stream(object.getClass().getDeclaredFields()),
                Arrays.stream(object.getClass().getSuperclass().getDeclaredFields()))
                .toList();

        for (Field field : fields) {
            field.setAccessible(true);
        }

        sb.append(exportingServiceHelper.processHtmlTemplate(TRANSACTION_TEMPLATE, object));

        return ExportingServiceHelper.convertHtmlToPdf(sb.toString());
    }

}
