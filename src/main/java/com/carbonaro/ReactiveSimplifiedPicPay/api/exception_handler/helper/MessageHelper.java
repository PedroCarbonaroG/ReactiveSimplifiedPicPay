package com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.GENERAL_MESSAGE_HELPER_DEFAULT_MESSAGE;

@Configuration
@RequiredArgsConstructor
public class MessageHelper implements MessageSource {

    private final MessageSource messageSourceHelper;

    @SneakyThrows
    public String getMessage(String code) {

        try { return messageSourceHelper.getMessage(code, new Object[0], LocaleContextHolder.getLocale());}
        catch (NoSuchMessageException e) { return messageSourceHelper.getMessage(GENERAL_MESSAGE_HELPER_DEFAULT_MESSAGE, new Object[0], LocaleContextHolder.getLocale()); }
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {

        return messageSourceHelper.getMessage(code, args, defaultMessage, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {

        return messageSourceHelper.getMessage(code, args, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {

        return messageSourceHelper.getMessage(resolvable, locale);
    }

}
