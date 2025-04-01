package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper.ApiExceptionHandlerHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.SecurityException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Description;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Order(-2)
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ApiExceptionHandlerHelper implements WebExceptionHandler {

    @ExceptionHandler({EmptyException.class})
    private ResponseEntity<Void> handleNoContentException(Exception e, ServerWebExchange request) {
        logException(e, true);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({TransactionValidationException.class, BadRequestException.class})
    private ResponseEntity<ErrorResponse> handleBadRequestException(Exception e, ServerWebExchange request) {
        logException(e, false);
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({AccessDeniedException.class, SecurityException.class})
    private ResponseEntity<ErrorResponse> handleSecurityException(Exception e, ServerWebExchange request) {
        logException(e, false);
        return buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
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

    @Override
    @SneakyThrows
    @Description("External system exception handler")
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        logException(ex, false);
        var status = HttpStatus.UNAUTHORIZED;
        var errorMessage = determineErrorMessage(ex);
        var errorResponse = buildSecurityErrorResponse(exchange, status, errorMessage);
        return writeErrorResponse(exchange, status, errorResponse);
    }

}
