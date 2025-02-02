package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper.ApiExceptionHandlerHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper.MessageHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.NotFoundException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.TransactionValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ApiExceptionHandlerHelper {

    private final MessageHelper messageHelper;

    private static final String SEPARATOR = " • ";

    private void getPrivateStackTrace(Exception e) {

        if (e instanceof EmptyException) {

            log.warn("WARN           ====> {}", messageHelper.getMessage(e.getMessage()));
            log.warn("LOCALIZED WARN ====> {}", e.getLocalizedMessage());
            log.warn("WARNING CLASS  ====> {}", e.getClass());
            log.warn("STACKTRACE     ====> {}", (Object) e.getStackTrace());
        } else {
            log.error("ERROR            ====> {}", messageHelper.getMessage(e.getMessage()));
            log.error("LOCALIZED ERROR  ====> {}", e.getLocalizedMessage());
            log.error("ERROR CLASS      ====> {}", e.getClass());

            for (int i = 0; i < e.getStackTrace().length; i++) {
                System.out.println(e.getStackTrace()[i]);
            }
//            log.error("STACKTRACE       ====> {}", (Object) e.getStackTrace());
        }
    }

    @ExceptionHandler({EmptyException.class})
    private ResponseEntity<ErrorEmptyResponse> noContentExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorEmptyResponse response = ErrorEmptyResponse.builder()
                .error(NO_CONTENT)
                .timestamp(TIMESTAMP)
                .path(getPath(request))
                .status(NO_CONTENT_STATUS.value())
                .warningMessage(messageHelper.getMessage(e.getMessage()).concat(SEPARATOR).concat(messageHelper.getMessage(HANDLER_NO_CONTENT_WARNING_MESSAGE)))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler({TransactionValidationException.class, BadRequestException.class})
    private ResponseEntity<ErrorResponse> badRequestExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .error(BAD_REQUEST)
                .timestamp(TIMESTAMP)
                .path(getPath(request))
                .status(BAD_REQUEST_STATUS.value())
                .errorMessage(messageHelper.getMessage(e.getMessage()).concat(SEPARATOR).concat(messageHelper.getMessage(HANDLER_BAD_REQUEST_ERROR_MESSAGE)))
                .build();

        return new ResponseEntity<>(response, BAD_REQUEST_STATUS);
    }

    @ExceptionHandler({NotFoundException.class})
    private ResponseEntity<ErrorResponse> notFoundExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .error(NOT_FOUND)
                .timestamp(TIMESTAMP)
                .path(getPath(request))
                .status(NOT_FOUND_STATUS.value())
                .errorMessage(messageHelper.getMessage(e.getMessage()).concat(SEPARATOR).concat(messageHelper.getMessage(HANDLER_NOT_FOUND_ERROR_MESSAGE)))
                .build();

        return new ResponseEntity<>(response, NOT_FOUND_STATUS);
    }

   @ExceptionHandler({Exception.class})
    private ResponseEntity<ErrorResponse> internalServerErrorExceptionHandler(Exception e, ServerWebExchange request) {

        getPrivateStackTrace(e);
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(TIMESTAMP)
                .path(getPath(request))
                .error(INTERNAL_SERVER_ERROR)
                .status(INTERNAL_SERVER_STATUS.value())
                .errorMessage(messageHelper.getMessage(e.getMessage()).concat(SEPARATOR).concat(messageHelper.getMessage(HANDLER_INTERNAL_SERVER_ERROR_MESSAGE)))
                .build();

        return new ResponseEntity<>(response, INTERNAL_SERVER_STATUS);
    }

}

