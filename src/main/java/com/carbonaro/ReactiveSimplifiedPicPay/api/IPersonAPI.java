package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.Description;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Tag(name = "Person API - Natural and Legal Do's")
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IPersonAPI {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "Neither LegalPerson was found."),
            @ApiResponse(responseCode = "404", description = "Not Found. Resources are not found to complete the search for all LegalPersons."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error. Something went wrong with API, contact the administration."),
    })
    @Description(value = "Responsible route to return all LegalPersons in DataBase.")
    @GetMapping(value = "/legals")
    Flux<LegalPerson> findAllLegals();

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "Neither NaturalPerson was found."),
            @ApiResponse(responseCode = "404", description = "Not Found. Resources are not found to complete the search for all NaturalPersons."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error. Something went wrong with API, contact the administration."),
    })
    @Description(value = "Responsible route to return all NaturalPersons in DataBase.")
    @GetMapping(value = "/naturals")
    Flux<NaturalPerson> findAllNaturals();
}
