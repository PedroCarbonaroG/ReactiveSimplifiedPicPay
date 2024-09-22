package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.GENERAL_UNKNOWN_EXCEPTION_ERROR;

@Configuration
@RequiredArgsConstructor
public class MessageHelper implements MessageSource {

    private final MessageSource messageSource;

    public String getMessage(String code) {

        try { return messageSource.getMessage(code, new Object[0], LocaleContextHolder.getLocale()); }
        catch (NoSuchMessageException e) { return messageSource.getMessage(GENERAL_UNKNOWN_EXCEPTION_ERROR, new Object[0], LocaleContextHolder.getLocale()); }
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {

        try { return messageSource.getMessage(code, args, defaultMessage, locale); }
        catch (NoSuchMessageException e) { return messageSource.getMessage(GENERAL_UNKNOWN_EXCEPTION_ERROR, args, defaultMessage, locale); }
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) {

        try { return messageSource.getMessage(code, args, locale); }
        catch (NoSuchMessageException e) { return messageSource.getMessage(GENERAL_UNKNOWN_EXCEPTION_ERROR, args, locale); }
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) {

        return messageSource.getMessage(resolvable, locale);
    }

}
