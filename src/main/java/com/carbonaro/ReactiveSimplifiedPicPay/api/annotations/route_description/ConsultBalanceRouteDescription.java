package com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.http.MediaType;

@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Responsible route to get user balance.",
        description = "If everything went right, returns balance from user document",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "If everything went right, returns OK."),
                @ApiResponse(
                        responseCode = "204",
                        description = "If everything went right however don't find any data about user, returns no content."),
                @ApiResponse(
                        responseCode = " 400 • 404  500",
                        description = "If something goes wrong if data or application resources, returns treated error.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))})
public @interface ConsultBalanceRouteDescription {
}
