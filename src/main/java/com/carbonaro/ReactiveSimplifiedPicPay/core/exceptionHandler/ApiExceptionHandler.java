package com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler;

import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

@RestControllerAdvice
public class ApiExceptionHandler extends ApiExceptionsConstants {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private void getPrivateStackTrace(Exception e) {

        if (e instanceof EmptyException) {
            log.warn("WARN           ====> {}", e.getMessage());
            log.warn("WARNING CLASS  ====> {}", e.getClass());
            log.warn("STACKTRACE     ====> {}", (Object) e.getStackTrace());
        } else {
            log.error("ERROR        ====> {}", e.getMessage());
            log.error("ERROR CLASS  ====> {}", e.getClass());
            log.error("STACKTRACE   ====> {}", (Object) e.getStackTrace());
        }
    }

    @ExceptionHandler({EmptyException.class})
    private ResponseEntity<ErrorEmptyResponse> noContentExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorEmptyResponse response = ErrorEmptyResponse.builder()
                .error(NO_CONTENT)
                .timestamp(TIMESTEMP)
                .path(getPath(request))
                .status(NO_CONTENT_STATUS.value())
                .warningMessage(NO_CONTENT_WARNING_MESSAGE)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MissingRequestValueException.class, ServerWebInputException.class,
    EmptyOrNullObjectException.class, DataIntegrityViolationException.class, TransactionValidationException.class, BadRequestException.class})
    private ResponseEntity<ErrorResponse> badRequestExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .error(BAD_REQUEST)
                .timestamp(TIMESTEMP)
                .path(getPath(request))
                .status(BAD_REQUEST_STATUS.value())
                .errorMessage(BAD_REQUEST_ERROR_MESSAGE)
                .build();

        return new ResponseEntity<>(response, BAD_REQUEST_STATUS);
    }

    @ExceptionHandler({NotFoundException.class})
    private ResponseEntity<ErrorResponse> notFoundExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .error(NOT_FOUND)
                .timestamp(TIMESTEMP)
                .path(getPath(request))
                .status(NOT_FOUND_STATUS.value())
                .errorMessage(NOT_FOUND_ERROR_MESSAGE)
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
}
