package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper.MessageHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.NotFoundException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.TransactionValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@Slf4j
@Order(-2)
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final MessageHelper messageHelper;
    private static final String SEPARATOR = " • ";

    private void logException(Throwable e, boolean isWarning) {
        if (isWarning) {
            log.warn("⚠ WARN: {}", messageHelper.getMessage(e.getMessage()));
            log.warn("⚠ LOCALIZED: {}", messageHelper.getMessage(e.getLocalizedMessage()));
        } else {
            log.error("❌ ERROR: {}", messageHelper.getMessage(e.getMessage()));
            log.error("❌ LOCALIZED: {}", messageHelper.getMessage(e.getLocalizedMessage()));
        }
        log.error("CLASS: {}", e.getClass());

//        log.error("STACKTRACE:", e);
    }

    @ExceptionHandler({EmptyException.class})
    private ResponseEntity<ErrorResponse> handleNoContentException(Exception e, ServerWebExchange request) {
        logException(e, true);
        return buildErrorResponse(e, HttpStatus.NO_CONTENT, request);
    }

    @ExceptionHandler({TransactionValidationException.class, BadRequestException.class})
    private ResponseEntity<ErrorResponse> handleBadRequestException(Exception e, ServerWebExchange request) {
        logException(e, false);
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({NotFoundException.class})
    private ResponseEntity<ErrorResponse> handleNotFoundException(Exception e, ServerWebExchange request) {
        logException(e, false);
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handleInternalServerError(Exception e, ServerWebExchange request) {
        logException(e, false);
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus status, ServerWebExchange request) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .path(getPath(request))
                .error(status.getReasonPhrase())
                .status(status.value())
                .errorMessage(messageHelper.getMessage(e.getMessage()).concat(SEPARATOR)
                        .concat(messageHelper.getMessage(getDefaultMessageByStatus(status.value()))))
                .build();

        return new ResponseEntity<>(response, status);
    }

    private String getDefaultMessageByStatus(int status) {

        return switch (status / 100) {
            case 4 -> switch (status) {
                case 400 -> HANDLER_BAD_REQUEST_ERROR_MESSAGE;
                case 404 -> HANDLER_NOT_FOUND_ERROR_MESSAGE;
                default -> HANDLER_INTERNAL_SERVER_ERROR_MESSAGE;};
            case 2 -> HANDLER_NO_CONTENT_WARNING_MESSAGE;
            default -> HANDLER_INTERNAL_SERVER_ERROR_MESSAGE;
        };
    }

    private String getPath(ServerWebExchange request) {
        return UriUtils.decode(request.getRequest().getPath().toString(), "UTF-8");
    }

    @Override
    @SneakyThrows
    @Description("External system exception handler")
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        logException(ex, false);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String errorMessage = switch (ex) {
            case IllegalArgumentException ignored -> HANDLER_ILLEGAL_ARGUMENT_ERROR_MESSAGE;
            case MalformedJwtException ignored -> HANDLER_MALFORMED_JWT_ERROR_MESSAGE;
            case ExpiredJwtException ignored -> HANDLER_EXPIRED_JWT_ERROR_MESSAGE;
            case SignatureException ignored -> HANDLER_SIGNATURE_ERROR_MESSAGE;
            case AuthenticationServiceException ignored -> HANDLER_AUTHENTICATION_SERVICE_EXCEPTION;
            case AuthenticationException ignored -> HANDLER_AUTHENTICATION_EXCEPTION;
            default -> HANDLER_INTERNAL_SERVER_ERROR_MESSAGE;
        };

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(status.name())
                .status(status.value())
                .errorMessage(status.name().concat(SEPARATOR).concat(messageHelper.getMessage(errorMessage)))
                .path(exchange.getRequest().getPath().value())
                .build();

        var bytes = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsBytes(errorResponse);
        var buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

}
