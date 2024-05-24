package com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public final Mono<ErrorResponse> genericExceptionHandler(Exception ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                request.getContextPath(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error!",
                ex.getMessage())
        ;

        return Mono.just(error);
    }
}
