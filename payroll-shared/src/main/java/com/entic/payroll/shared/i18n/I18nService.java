package com.entic.payroll.shared.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class I18nService {

    private static final Logger log = LoggerFactory.getLogger(I18nService.class);

    private final MessageSource messageSource;

    public I18nService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String code, Object... args) {
        return resolve(code, LocaleContextHolder.getLocale(), args);
    }

    public String get(String code, Locale locale, Object... args) {
        return resolve(code, locale, args);
    }

    private String resolve(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, code, locale);
    }
}
