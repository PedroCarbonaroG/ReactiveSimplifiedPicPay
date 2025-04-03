package com.carbonaro.ReactiveSimplifiedPicPay.api;

import static com.carbonaro.ReactiveSimplifiedPicPay.api.helper.BaseHelper.disableSecurity;
import static com.carbonaro.ReactiveSimplifiedPicPay.api.helper.BaseHelper.getOptionalField;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.NaturalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.services.LegalPersonService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.NaturalPersonService;
import java.util.Collections;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonApiTest {

    private static final String URI_LIST_ALL_LEGALS = "/person/list-all/legals";
    private static final String URI_LEGAL_BY_CNPJ = "/person/legal/cnpj";
    private static final String URI_LEGAL_BY_ID = "/person/legal/id";
    private static final String URI_SAVE_LEGAL = "/person/legal/save";
    private static final String URI_SAVE_PARTNER = "/person/legal/save/partner";
    private static final String URI_UPDATE_LEGAL = "/person/legal/update";
    private static final String URI_DELETE_LEGAL = "/person/legal/delete";
    private static final String URI_DELETE_PARTNER = "/person/legal/partner/delete";
    private static final String URI_LIST_ALL_NATURALS = "/person/list-all/naturals";
    private static final String URI_NATURAL_BY_CPF = "/person/natural/cpf";
    private static final String URI_NATURAL_BY_ID = "/person/natural/id";
    private static final String URI_SAVE_NATURAL = "/person/natural/save";
    private static final String URI_UPDATE_NATURAL = "/person/natural/update";
    private static final String URI_DELETE_NATURAL = "/person/natural/delete";

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_SORT = "sort";
    private static final String PARAM_ID = "id";
    private static final String PARAM_CNPJ = "cnpj";
    private static final String PARAM_CPF = "cpf";

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ApplicationContext context;

    @MockBean
    private NaturalPersonService naturalPersonService;
    @MockBean
    private LegalPersonService legalPersonService;

    @Test
    @DisplayName("Teste findAllLegals")
    void testFindAllLegals() {

        var filter = LegalPersonFilterRequest.builder().page(0).size(20).build();
        var pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
        var legalPerson = LegalPerson
                .builder()
                .cnpj("12345678901234")
                .name("Test-Company")
                .build();
        var page = new PageImpl<>(Collections.singletonList(legalPerson));

        when(legalPersonService.findAllLegals(pageRequest, filter)).thenReturn(Mono.just(page));

        webTestClient
                .mutateWith(disableSecurity())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_LIST_ALL_LEGALS)
                        .queryParamIfPresent(PARAM_PAGE, getOptionalField(filter.getPage()))
                        .queryParamIfPresent(PARAM_SIZE, getOptionalField(filter.getSize()))
                        .queryParamIfPresent(PARAM_SIZE, getOptionalField(filter.getSort()))
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PageResponse.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    Assertions.assertIterableEquals(page.getContent(), response.getContent());
                });
    }

    @Test
    @DisplayName("Teste findLegalByCNPJ")
    void testFindLegalByCNPJ() {
        // Preparação
        String cnpj = "12345678901234";
        LegalPerson legalPerson = new LegalPerson();
        legalPerson.setCnpj(cnpj);

        when(legalPersonService.findLegalByCNPJ(cnpj)).thenReturn(Mono.just(legalPerson));

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .get()
                .uri(URI_LEGAL_BY_CNPJ)
                .header("companyCNPJ", cnpj)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(LegalPersonResponse.class)
                .value(response -> Assertions.assertNotNull(response));
    }

    @Test
    @DisplayName("Teste findLegalById")
    void testFindLegalById() {
        // Preparação
        String id = "123";
        LegalPerson legalPerson = new LegalPerson();
        legalPerson.setId(id);

        when(legalPersonService.findLegalById(id)).thenReturn(Mono.just(legalPerson));

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_LEGAL_BY_ID)
                        .queryParamIfPresent(PARAM_ID, getOptionalField(id))
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(LegalPersonResponse.class)
                .value(response -> Assertions.assertNotNull(response));
    }

    @Test
    @DisplayName("Teste saveLegalPerson")
    void testSaveLegalPerson() {
        // Preparação
        LegalPersonRequest request = new LegalPersonRequest();
        request.setCnpj("12345678901234");

        when(legalPersonService.saveLegalPerson(any())).thenReturn(Mono.empty());

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_SAVE_LEGAL)
                        .queryParamIfPresent(PARAM_CNPJ, getOptionalField(request.getCnpj()))
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("Teste savePartnerToLegalPerson")
    void testSavePartnerToLegalPerson() {
        // Preparação
        String cnpj = "12345678901234";
        String cpf = "12345678901";

        when(legalPersonService.savePartner(cnpj, cpf)).thenReturn(Mono.empty());

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .post()
                .uri(URI_SAVE_PARTNER)
                .header("companyCNPJ", cnpj)
                .header("partnerCPF", cpf)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("Teste updateLegalPerson")
    void testUpdateLegalPerson() {
        // Preparação
        String cnpj = "12345678901234";
        LegalPersonRequest request = new LegalPersonRequest();
        request.setCnpj(cnpj);

        when(legalPersonService.updateLegalPerson(anyString(), any())).thenReturn(Mono.empty());

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_UPDATE_LEGAL)
                        .queryParamIfPresent(PARAM_CNPJ, getOptionalField(request.getCnpj()))
                        .build())
                .header("companyCNPJ", cnpj)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("Teste deleteLegalPerson")
    void testDeleteLegalPerson() {
        // Preparação
        String cnpj = "12345678901234";

        when(legalPersonService.deleteLegalPerson(cnpj)).thenReturn(Mono.empty());

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .delete()
                .uri(URI_DELETE_LEGAL)
                .header("companyCNPJ", cnpj)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("Teste deletePartnerByLegalPerson")
    void testDeletePartnerByLegalPerson() {
        // Preparação
        String cnpj = "12345678901234";
        String cpf = "12345678901";

        when(legalPersonService.deletePartner(cnpj, cpf)).thenReturn(Mono.empty());

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .delete()
                .uri(URI_DELETE_PARTNER)
                .header("companyCNPJ", cnpj)
                .header("partnerCPF", cpf)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("Teste findAllNaturals")
    void testFindAllNaturals() {
        // Preparação
        NaturalPersonFilterRequest filter = NaturalPersonFilterRequest.builder()
                .page(0)
                .size(10)
                .build();

        NaturalPerson naturalPerson = new NaturalPerson();
        naturalPerson.setCpf("12345678901");

        PageImpl<NaturalPerson> page = new PageImpl<>(Collections.singletonList(naturalPerson));

        when(naturalPersonService.findAllNaturals(any(PageRequest.class), any(NaturalPersonFilterRequest.class)))
                .thenReturn(Mono.just(page));

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_LIST_ALL_NATURALS)
                        .queryParamIfPresent(PARAM_PAGE, getOptionalField(filter.getPage()))
                        .queryParamIfPresent(PARAM_SIZE, getOptionalField(filter.getSize()))
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PageResponse.class)
                .value(response -> Assertions.assertNotNull(response));
    }

    @Test
    @DisplayName("Teste findNaturalByCPF")
    void testFindNaturalByCPF() {
        // Preparação
        String cpf = "12345678901";
        NaturalPerson naturalPerson = new NaturalPerson();
        naturalPerson.setCpf(cpf);

        when(naturalPersonService.findNaturalByCPF(cpf)).thenReturn(Mono.just(naturalPerson));

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .get()
                .uri(URI_NATURAL_BY_CPF)
                .header("cpf", cpf)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(NaturalPersonResponse.class)
                .value(response -> Assertions.assertNotNull(response));
    }

    @Test
    @DisplayName("Teste findNaturalByID")
    void testFindNaturalByID() {
        // Preparação
        String id = "123";
        NaturalPerson naturalPerson = new NaturalPerson();
        naturalPerson.setId(id);

        when(naturalPersonService.findNaturalById(id)).thenReturn(Mono.just(naturalPerson));

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_NATURAL_BY_ID)
                        .queryParamIfPresent(PARAM_ID, getOptionalField(id))
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(NaturalPersonResponse.class)
                .value(response -> Assertions.assertNotNull(response));
    }

    @Test
    @DisplayName("Teste saveNaturalPerson")
    void testSaveNaturalPerson() {
        // Preparação
        NaturalPersonRequest request = new NaturalPersonRequest();
        request.setCpf("12345678901");

        when(naturalPersonService.saveNaturalPerson(any())).thenReturn(Mono.empty());

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_SAVE_NATURAL)
                        .queryParamIfPresent(PARAM_CPF, getOptionalField(request.getCpf()))
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("Teste updateNaturalPerson")
    void testUpdateNaturalPerson() {
        // Preparação
        String cpf = "12345678901";
        NaturalPersonRequest request = new NaturalPersonRequest();
        request.setCpf(cpf);

        when(naturalPersonService.updateNaturalPerson(any(), eq(cpf))).thenReturn(Mono.empty());

        // Execução e verificação
        webTestClient
                .mutateWith(disableSecurity())
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_UPDATE_NATURAL)
                        .queryParamIfPresent(PARAM_CPF, getOptionalField(request.getCpf()))
                        .build())
                .header("cpf", cpf)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("Teste deleteNaturalPerson")
    void testDeleteNaturalPerson() {

        String cpf = "12345678901";

        when(naturalPersonService.deleteNatural(cpf)).thenReturn(Mono.empty());

        webTestClient
                .mutateWith(disableSecurity())
                .delete()
                .uri(URI_DELETE_NATURAL)
                .header("cpf", cpf)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
