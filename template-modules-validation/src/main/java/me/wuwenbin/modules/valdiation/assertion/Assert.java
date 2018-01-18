package me.wuwenbin.modules.valdiation.assertion;

import me.wuwenbin.modules.valdiation.template.Template;
import org.springframework.validation.BindingResult;

/**
 * created by Wuwenbin on 2018/1/16 at 12:13
 */
public class Assert {

    /**
     * 检查方法入口
     *
     * @param messageTemplate
     * @param result
     * @param <T>
     * @return
     */
    public static <T> T check(Template<T> messageTemplate, BindingResult result) {
        return messageTemplate.getConvert().apply(result.getFieldErrors());
    }

}
