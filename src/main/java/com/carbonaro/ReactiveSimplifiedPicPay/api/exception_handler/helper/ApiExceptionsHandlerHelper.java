package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;
import java.time.LocalDateTime;

public abstract class ApiExceptionsHandlerHelper {

    protected final String getPath(ServerWebExchange request) { return UriUtils.decode(request.getRequest().getPath().toString(), "UTF-8"); }
    protected static final LocalDateTime TIMESTEMP = LocalDateTime.now();

    // <=====================================================================================================================================================> //

    protected static final String NOT_FOUND = "Not Found";
    protected static final String NO_CONTENT = "No Content";
    protected static final String BAD_REQUEST = "Bad Request";
    protected static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    // <=====================================================================================================================================================> //

    protected static final String NOT_FOUND_ERROR_MESSAGE = "Not found, some resources are not available";
    protected static final String BAD_REQUEST_ERROR_MESSAGE = "Bad request, param(s) could not match, something is missing or wrong, revise that and try again.";
    protected static final String NO_CONTENT_WARNING_MESSAGE = "No content, the search was done with success but non Object(s) was found!";
    protected static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error, contact the administration for solve the problem!";

    // <=====================================================================================================================================================> //

    protected static final HttpStatus NOT_FOUND_STATUS = HttpStatus.NOT_FOUND;
    protected static final HttpStatus NO_CONTENT_STATUS = HttpStatus.NO_CONTENT;
    protected static final HttpStatus BAD_REQUEST_STATUS = HttpStatus.BAD_REQUEST;
    protected static final HttpStatus INTERNAL_SERVER_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

}
