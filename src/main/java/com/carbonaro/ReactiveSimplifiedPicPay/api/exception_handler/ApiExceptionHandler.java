package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper.ApiExceptionsHandlerHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper.MessageHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ApiExceptionsHandlerHelper implements ErrorWebExceptionHandler {

    private final MessageHelper messageHelper;

    private static final String SEPARATOR = " • ";

    private void getPrivateStackTrace(Exception e) {

        if (e instanceof EmptyReturnException) {

            log.warn("WARN           ====> {}", messageHelper.getMessage(e.getMessage()));
            log.warn("WARN MESSAGE   ====> {}", e.getLocalizedMessage());
            log.warn("WARN CLASS     ====> {}", e.getClass());
            log.warn("STACKTRACE     ====> {}", (Object) e.getStackTrace());
        } else {

            log.error("ERROR         ====> {}", messageHelper.getMessage(e.getMessage()));
            log.error("ERROR MESSAGE ====> {}", e.getLocalizedMessage());
            log.error("ERROR CLASS   ====> {}", e.getClass());
            log.error("STACKTRACE    ====> {}", (Object) e.getStackTrace());
        }
    }
    private void getPrivateInterceptorStackTrace(Exception e) {

        log.error("ERROR MESSAGE  ====> {}", e.getMessage());
        log.error("ERROR CLASS    ====> {}", e.getClass());
        log.error("STACKTRACE     ====> {}", (Object) e.getStackTrace());
    }

    @ExceptionHandler({EmptyReturnException.class})
    private ResponseEntity<ErrorEmptyResponse> noContentExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorEmptyResponse response = ErrorEmptyResponse.builder()
                .error(NO_CONTENT)
                .timestamp(TIMESTEMP)
                .path(getPath(request))
                .status(NO_CONTENT_STATUS.value())
                .warningMessage(messageHelper.getMessage(e.getMessage()).concat(SEPARATOR).concat(NO_CONTENT_WARNING_MESSAGE))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler({TransactionValidationException.class, BadRequestException.class})
    private ResponseEntity<ErrorResponse> badRequestExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .error(BAD_REQUEST)
                .timestamp(TIMESTEMP)
                .path(getPath(request))
                .status(BAD_REQUEST_STATUS.value())
                .errorMessage(messageHelper.getMessage(e.getMessage()).concat(SEPARATOR).concat(BAD_REQUEST_ERROR_MESSAGE))
                .build();

        return new ResponseEntity<>(response, BAD_REQUEST_STATUS);
    }

    @ExceptionHandler({NoSuchElementException.class})
    private ResponseEntity<ErrorResponse> notFoundExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .error(NOT_FOUND)
                .timestamp(TIMESTEMP)
                .path(getPath(request))
                .status(NOT_FOUND_STATUS.value())
                .errorMessage(messageHelper.getMessage(e.getMessage()).concat(SEPARATOR).concat(NOT_FOUND_ERROR_MESSAGE))
                .build();

        return new ResponseEntity<>(response, NOT_FOUND_STATUS);
    }

    @ExceptionHandler({Exception.class})
    private ResponseEntity<ErrorResponse> internalServerErrorExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(TIMESTEMP)
                .path(getPath(request))
                .error(INTERNAL_SERVER_ERROR)
                .status(INTERNAL_SERVER_STATUS.value())
                .errorMessage(INTERNAL_SERVER_ERROR_MESSAGE)
                .build();

        return new ResponseEntity<>(response, INTERNAL_SERVER_STATUS);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        byte[] responseBytes = buildExceptionResponse(exchange, ex);
        return exchange
                .getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBytes)));
    }
    private byte[] buildExceptionResponse(ServerWebExchange exchange, Throwable exception) {

        HttpStatus status = UNAUTHORIZED_STATUS;
        String error = UNAUTHORIZED;
        String message = validateException(exception);

        if (Objects.isNull(message)) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            error = INTERNAL_SERVER_ERROR;
            message = INTERNAL_SERVER_ERROR_MESSAGE;
        }

        getPrivateInterceptorStackTrace((Exception) exception);
        ErrorResponse response = ErrorResponse
                .builder()
                .error(error)
                .timestamp(TIMESTEMP)
                .path(getPath(exchange))
                .status(status.value())
                .errorMessage(message)
                .build();

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Json.pretty(response).getBytes(StandardCharsets.UTF_8);
    }
    private String validateException(Throwable ex) {

        String defaultMessage = UNAUTHORIZED_ERROR_MESSAGE;
        if (ex instanceof ExpiredJwtException) return messageHelper.getMessage(TOKEN_EXPIRED_ERROR).concat(SEPARATOR).concat(defaultMessage);
        else if (ex instanceof SignatureException) return messageHelper.getMessage(TOKEN_SIGNATURE_ERROR).concat(SEPARATOR).concat(defaultMessage);
        else if (ex instanceof MalformedJwtException) return messageHelper.getMessage(TOKEN_STRUCTURE_ERROR).concat(SEPARATOR).concat(defaultMessage);
        else if (ex instanceof IllegalArgumentException) return messageHelper.getMessage(TOKEN_SCOPES_ERROR).concat(SEPARATOR).concat(defaultMessage);
        else return null;

    }

}
