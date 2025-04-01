package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@Slf4j
@RequiredArgsConstructor
public abstract class ApiExceptionHandlerHelper {

    @Autowired
    protected MessageHelper messageHelper;
    private static final String SEPARATOR = " • ";

    protected void logException(Throwable e, boolean isWarning) {

        if (isWarning) {
            log.warn("⚠ WARN: {}", getInternalErrorMessage(e));
            log.warn("⚠ LOCALIZED: {}", e.getLocalizedMessage());
        } else {
            log.error("❌ ERROR: {}", getInternalErrorMessage(e));
            log.error("❌ LOCALIZED: {}", e.getLocalizedMessage());
        }
        log.error("CLASS: {}", e.getClass());
        //DISCOMMENT THAT TO SEE ALL ERROR STACKTRACE LOG log.error("STACKTRACE:", e);
    }

    private String getInternalErrorMessage(Throwable ex) {

        if (messageHelper.getMessage(ex.getMessage()).equals(messageHelper.getMessage(GENERAL_MESSAGE_HELPER_DEFAULT_MESSAGE))) {
            return ex.getMessage();
        }

        return messageHelper.getMessage(ex.getMessage());
    }

    protected String determineErrorMessage(Throwable ex) {

        return switch (ex) {
            case IllegalArgumentException ignored -> HANDLER_ILLEGAL_ARGUMENT_ERROR_MESSAGE;
            case MalformedJwtException ignored -> HANDLER_MALFORMED_JWT_ERROR_MESSAGE;
            case ExpiredJwtException ignored -> HANDLER_EXPIRED_JWT_ERROR_MESSAGE;
            case SignatureException ignored -> HANDLER_SIGNATURE_ERROR_MESSAGE;
            case AuthenticationServiceException ignored -> HANDLER_AUTHENTICATION_SERVICE_EXCEPTION;
            case AuthenticationException ignored -> HANDLER_AUTHENTICATION_EXCEPTION;
            default -> HANDLER_INTERNAL_SERVER_ERROR_MESSAGE;
        };
    }

    protected ErrorResponse buildSecurityErrorResponse(ServerWebExchange exchange, HttpStatus status, String errorMessage) {

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(status.name())
                .status(status.value())
                .errorMessage(StringUtils.capitalize(status.name().toLowerCase()).concat(SEPARATOR).concat(messageHelper.getMessage(errorMessage)))
                .path(exchange.getRequest().getPath().value())
                .build();
    }

    protected ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus status, ServerWebExchange request) {

        var response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .path(getPath(request))
                .error(status.getReasonPhrase())
                .status(status.value())
                .errorMessage(messageHelper.getMessage(getDefaultMessageByStatus(status.value()))
                        .concat(SEPARATOR).concat(messageHelper.getMessage(e.getMessage())))
                .build();

        return new ResponseEntity<>(response, status);
    }

    protected String getDefaultMessageByStatus(int status) {
        return switch (status) {
            case 400 -> HANDLER_BAD_REQUEST_ERROR_MESSAGE;
            case 401 -> HANDLER_UNAUTHORIZED_ACCESS_DENIED_ERROR_MESSAGE;
            case 404 -> HANDLER_NOT_FOUND_ERROR_MESSAGE;
            case 204 -> HANDLER_NO_CONTENT_WARNING_MESSAGE;
            default -> HANDLER_INTERNAL_SERVER_ERROR_MESSAGE;
        };
    }

    protected String getPath(ServerWebExchange request) {
        return UriUtils.decode(request.getRequest().getPath().toString(), "UTF-8");
    }

    @SneakyThrows
    protected Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatus status, ErrorResponse errorResponse) {
        var bytes = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsBytes(errorResponse);
        var buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

}