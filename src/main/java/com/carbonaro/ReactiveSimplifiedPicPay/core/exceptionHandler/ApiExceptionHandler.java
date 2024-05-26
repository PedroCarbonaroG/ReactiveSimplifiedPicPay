package com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler;

import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
public class ApiExceptionHandler extends ApiExceptionsConstants {

    @ExceptionHandler(EmptyException.class)
    private ResponseEntity<ErrorEmptyResponse> emptyExceptionHandler(EmptyException e) {

        getPrivateStackTrace(e);
        ErrorEmptyResponse response = ErrorEmptyResponse.builder()
                .path("NOT DEFINED YET")
                .timestemp(LocalDateTime.now())
                .error(NO_CONTENT)
                .warningMessage(NO_CONTENT_WARNING_MESSAGE)
                .status(HttpStatus.NO_CONTENT.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundException e) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .path("NOT DEFINED YET")
                .timestemp(LocalDateTime.now())
                .error(NOT_FOUND)
                .errorMessage(NOT_FOUND_ERROR_MESSAGE)
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> internalServerErrorExceptionHandler(Exception e) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .path("NOT DEFINED YET")
                .timestemp(LocalDateTime.now())
                .error(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR_MESSAGE)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void getPrivateStackTrace(Exception e) {
        System.out.println("FINAL ERROR IN THE STACK TRACE ==============> " + e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(System.out::println);
    }
}
