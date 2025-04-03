package com.carbonaro.ReactiveSimplifiedPicPay.api;

import static com.carbonaro.ReactiveSimplifiedPicPay.api.helper.BaseHelper.disableSecurity;
import static com.carbonaro.ReactiveSimplifiedPicPay.api.helper.BaseHelper.getOptionalField;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import com.carbonaro.ReactiveSimplifiedPicPay.services.ExportingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@AutoConfigureWebTestClient
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExportingApiTest {

    private static final String URI_TRANSACTIONS = "/extraction/transactions";
    private static final String URI_NATURALS = "/extraction/person/naturals";
    private static final String URI_LEGALS = "/extraction/person/legals";

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_SORT = "sort";
    private static final String PARAM_FILE_TYPE = "fileType";
    private static final String PARAM_SENDER_DOCUMENT = "senderDocument";
    private static final String PARAM_RECEIVER_DOCUMENT = "receiverDocument";
    private static final String PARAM_INITIAL_DATE = "initialDate";
    private static final String PARAM_FINAL_DATE = "finalDate";
    private static final String PARAM_INITIAL_TRANSACTION_VALUE = "initialTransactionValue";
    private static final String PARAM_FINAL_TRANSACTION_VALUE = "finalTransactionValue";
    private static final String PARAM_BIRTH_DATE = "birthDate";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_CNPJ = "cnpj";
    private static final String PARAM_COMPANY_SIZE = "companySize";
    private static final String PARAM_EMPLOYEES_NUMBER = "employeesNumber";
    private static final String PARAM_INITIAL_MONTHLY_BILLING = "initialMonthlyBilling";
    private static final String PARAM_FINAL_MONTHLY_BILLING = "finalMonthlyBilling";
    private static final String PARAM_INITIAL_ANNUAL_BILLING = "initialAnnualBilling";
    private static final String PARAM_FINAL_ANNUAL_BILLING = "initialAnnualBilling";

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ApplicationContext context;

    @MockBean
    private ExportingService exportingService;

    @Test
    @DisplayName("Test Transactions To Extraction")
    void testTransactionsToExtraction() {

        var fileType = FileTypeEnum.PDF;
        var filter = TransactionFilterRequest.builder().build();

        byte[] mockResponse = new byte[]{1, 2, 3};
        when(exportingService.getTransactionsExtraction(filter, fileType))
                .thenReturn(Mono.just(mockResponse));

        webTestClient
                .mutateWith(disableSecurity())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_TRANSACTIONS)
                        .queryParamIfPresent(PARAM_FILE_TYPE, getOptionalField(fileType))
                        .queryParamIfPresent(PARAM_PAGE, getOptionalField(filter.getPage()))
                        .queryParamIfPresent(PARAM_SORT, getOptionalField(filter.getSort()))
                        .queryParamIfPresent(PARAM_SIZE, getOptionalField(filter.getSize()))
                        .queryParamIfPresent(PARAM_SENDER_DOCUMENT, getOptionalField(filter.getSenderDocument()))
                        .queryParamIfPresent(PARAM_RECEIVER_DOCUMENT, getOptionalField(filter.getReceiverDocument()))
                        .queryParamIfPresent(PARAM_INITIAL_DATE, getOptionalField(filter.getInitialDate()))
                        .queryParamIfPresent(PARAM_FINAL_DATE, getOptionalField(filter.getFinalDate()))
                        .queryParamIfPresent(PARAM_INITIAL_TRANSACTION_VALUE, getOptionalField(filter.getInitialTransactionValue()))
                        .queryParamIfPresent(PARAM_FINAL_TRANSACTION_VALUE, getOptionalField(filter.getFinalTransactionValue()))
                        .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(byte[].class)
                .value(response ->
                        StepVerifier.create(Mono.just(response))
                                .assertNext(self -> {
                                    Assertions.assertNotNull(self);
                                    Assertions.assertArrayEquals(mockResponse, self);})
                                .verifyComplete());
    }

    @Test
    @DisplayName("Test Naturals To Extraction")
    void testNaturalsToExtraction() {

        var fileType = FileTypeEnum.EXCEL;
        var filter = NaturalPersonFilterRequest.builder().build();

        byte[] mockResponse = new byte[]{1, 2, 3};
        when(exportingService.getNaturalsToExtraction(filter, fileType))
                .thenReturn(Mono.just(mockResponse));

        webTestClient
                .mutateWith(disableSecurity())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_NATURALS)
                        .queryParam(PARAM_FILE_TYPE, fileType)
                        .queryParamIfPresent(PARAM_PAGE, getOptionalField(filter.getPage()))
                        .queryParamIfPresent(PARAM_SORT, getOptionalField(filter.getSort()))
                        .queryParamIfPresent(PARAM_SIZE, getOptionalField(filter.getSize()))
                        .queryParamIfPresent(PARAM_BIRTH_DATE, getOptionalField(filter.getBirthDate()))
                        .queryParamIfPresent(PARAM_NAME, getOptionalField(filter.getName()))
                        .queryParamIfPresent(PARAM_EMAIL, getOptionalField(filter.getEmail()))
                        .queryParamIfPresent(PARAM_ADDRESS, getOptionalField(filter.getAddress()))
                        .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(byte[].class)
                .value(response ->
                        StepVerifier.create(Mono.just(response))
                                .assertNext(self -> {
                                    Assertions.assertNotNull(self);
                                    Assertions.assertArrayEquals(mockResponse, self);})
                                .verifyComplete());
    }

    @Test
    @DisplayName("Test Legals To Extraction")
    void testLegalsToExtraction() {

        var fileType = FileTypeEnum.EXCEL;
        var filter = LegalPersonFilterRequest.builder().build();

        byte[] mockResponse = new byte[]{1, 2, 3};
        when(exportingService.getLegalsToExtraction(any(), any()))
                .thenReturn(Mono.just(mockResponse));

        webTestClient
                .mutateWith(disableSecurity())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_LEGALS)
                        .queryParam(PARAM_FILE_TYPE, fileType)
                        .queryParamIfPresent(PARAM_PAGE, getOptionalField(filter.getPage()))
                        .queryParamIfPresent(PARAM_SORT, getOptionalField(filter.getSort()))
                        .queryParamIfPresent(PARAM_SIZE, getOptionalField(filter.getSize()))
                        .queryParamIfPresent(PARAM_CNPJ, getOptionalField(filter.getCnpj()))
                        .queryParamIfPresent(PARAM_NAME, getOptionalField(filter.getName()))
                        .queryParamIfPresent(PARAM_ADDRESS, getOptionalField(filter.getAddress()))
                        .queryParamIfPresent(PARAM_COMPANY_SIZE, getOptionalField(filter.getCompanySize()))
                        .queryParamIfPresent(PARAM_EMPLOYEES_NUMBER, getOptionalField(filter.getEmployeesNumber()))
                        .queryParamIfPresent(PARAM_INITIAL_MONTHLY_BILLING, getOptionalField(filter.getInitialMonthlyBilling()))
                        .queryParamIfPresent(PARAM_FINAL_MONTHLY_BILLING, getOptionalField(filter.getFinalMonthlyBilling()))
                        .queryParamIfPresent(PARAM_INITIAL_ANNUAL_BILLING, getOptionalField(filter.getInitialAnnualBilling()))
                        .queryParamIfPresent(PARAM_FINAL_ANNUAL_BILLING, getOptionalField(filter.getFinalAnnualBilling()))
                        .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(byte[].class)
                .value(response ->
                        StepVerifier.create(Mono.just(response))
                                .assertNext(self -> {
                                    Assertions.assertNotNull(self);
                                    Assertions.assertArrayEquals(mockResponse, self);})
                                .verifyComplete());
    }

}
