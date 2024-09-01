package com.carbonaro.ReactiveSimplifiedPicPay.core.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class MessageHelper implements MessageSource {

    private final MessageSource messageSource;

    public String getMessage(String code) {

        return messageSource.getMessage(code, new Object[0], LocaleContextHolder.getLocale());
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {

        return messageSource.getMessage(code, args, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {

        return messageSource.getMessage(resolvable, locale);
    }
}
