package com.carbonaro.ReactiveSimplifiedPicPay.core.annotations.RouteDescriptions;

import com.carbonaro.ReactiveSimplifiedPicPay.core.exception_handler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.exception_handler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.NaturalPersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

@Operation(
        summary = "Responsible route to return all NaturalPersons.",
        description = "Nothing yet",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NaturalPersonResponse.class))),
                @ApiResponse(
                        responseCode = "204",
                        description = "Neither NaturalPerson was found.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorEmptyResponse.class))),
                @ApiResponse(
                        responseCode = "404 • 500",
                        description = "If something goes wrong if data or application resources, returns treated error.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),})
public @interface FindAllNaturalsRouteDescription {
}
