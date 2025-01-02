package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;
import java.time.LocalDateTime;

public abstract class ApiExceptionHandlerHelper {

    protected final String getPath(ServerWebExchange request) { return UriUtils.decode(request.getRequest().getPath().toString(), "UTF-8"); }
    protected static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    protected static final String NOT_FOUND = "Not Found";
    protected static final String NO_CONTENT = "No Content";
    protected static final String BAD_REQUEST = "Bad Request";
    protected static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    protected static final HttpStatus NOT_FOUND_STATUS = HttpStatus.NOT_FOUND;
    protected static final HttpStatus NO_CONTENT_STATUS = HttpStatus.NO_CONTENT;
    protected static final HttpStatus BAD_REQUEST_STATUS = HttpStatus.BAD_REQUEST;
    protected static final HttpStatus INTERNAL_SERVER_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

}
