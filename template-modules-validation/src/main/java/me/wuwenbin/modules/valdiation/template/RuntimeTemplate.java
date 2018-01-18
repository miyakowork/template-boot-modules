package me.wuwenbin.modules.valdiation.template;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * created by Wuwenbin on 2018/1/16 at 12:59
 */
public class RuntimeTemplate extends Template<RuntimeException> {

    public RuntimeTemplate(MessageSource messageSource) {
        super(messageSource, LocaleContextHolder.getLocale(), fieldErrors -> {
            List<String> message = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                String errorMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                message.add(fieldError.getField().concat(":").concat(errorMessage));
            }
            throw new RuntimeException(String.join(",", message));
        });
    }

    public RuntimeTemplate(MessageSource messageSource, Locale currentLocale, Function<List<FieldError>, RuntimeException> convert) {
        super(messageSource, currentLocale, convert);
    }

    public RuntimeTemplate(MessageSource messageSource, Locale currentLocale) {
        super(messageSource, currentLocale, fieldErrors -> {
            List<String> message = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                String errorMessage = messageSource.getMessage(fieldError, currentLocale);
                message.add(fieldError.getField().concat(":").concat(errorMessage));
            }
            throw new RuntimeException(String.join(",", message));
        });
    }

    public RuntimeTemplate(MessageSource messageSource, Function<List<FieldError>, RuntimeException> convert) {
        super(messageSource, LocaleContextHolder.getLocale(), convert);
    }

}
