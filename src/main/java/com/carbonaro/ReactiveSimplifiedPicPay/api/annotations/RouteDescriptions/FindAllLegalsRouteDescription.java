package com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteDescriptions;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.LegalPersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Responsible route to return all LegalPersons.",
        description = "If everything went right, returns all legal persons as a json list",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "If everything went right, returns OK.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LegalPersonResponse.class))),
                @ApiResponse(
                        responseCode = "204",
                        description = "If everything went right however don't find any data, returns no content.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorEmptyResponse.class))),
                @ApiResponse(
                        responseCode = " 401 • 404  500",
                        description = "If something goes wrong if data or application resources, returns treated error.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))})
public @interface FindAllLegalsRouteDescription {
}
