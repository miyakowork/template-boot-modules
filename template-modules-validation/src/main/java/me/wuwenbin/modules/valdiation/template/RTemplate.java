package me.wuwenbin.modules.valdiation.template;

import me.wuwenbin.modules.utils.http.R;
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
public class RTemplate extends Template<R> {

    public RTemplate(MessageSource messageSource) {
        super(messageSource, fieldErrors -> {
            List<String> message = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                String errorMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                message.add(fieldError.getField().concat(":").concat(errorMessage));
            }
            return R.error(String.join(",", message));
        });
    }

    public RTemplate(MessageSource messageSource, Locale currentLocale, Function<List<FieldError>, R> convert) {
        super(messageSource, currentLocale, convert);
    }

    public RTemplate(MessageSource messageSource, Locale currentLocale) {
        super(messageSource, currentLocale, fieldErrors -> {
            List<String> message = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                String errorMessage = messageSource.getMessage(fieldError, currentLocale);
                message.add(fieldError.getField().concat(":").concat(errorMessage));
            }
            return R.error(String.join(",", message));
        });
    }

    public RTemplate(MessageSource messageSource, Function<List<FieldError>, R> convert) {
        super(messageSource, LocaleContextHolder.getLocale(), convert);
    }

}
