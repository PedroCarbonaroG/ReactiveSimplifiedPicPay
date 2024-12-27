package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.DEFAULT_MESSAGE;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class MessageHelper implements MessageSource {

    private final MessageSource messagehelper;

    @SneakyThrows
    public String getMessage(String code) {

        try { return messagehelper.getMessage(code, new Object[0], LocaleContextHolder.getLocale());}
        catch (NoSuchMessageException e) { return messagehelper.getMessage(DEFAULT_MESSAGE, new Object[0], LocaleContextHolder.getLocale()); }
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {

        return messagehelper.getMessage(code, args, defaultMessage, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {

        return messagehelper.getMessage(code, args, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {

        return messagehelper.getMessage(resolvable, locale);
    }

}
