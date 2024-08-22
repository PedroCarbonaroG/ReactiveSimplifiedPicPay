package com.carbonaro.ReactiveSimplifiedPicPay.core.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Configuration;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class MessageHelper implements MessageSource {

    public String getMessage(String message) {

        return getMessage(message, new Object[0], Locale.getDefault());
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {

        return "Not Implemented";
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {

        return "Not Implemented";
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {

        return "Not Implemented";
    }
}
