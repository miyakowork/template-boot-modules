package me.wuwenbin.modules.utils.rest;

import me.wuwenbin.modules.utils.http.R;
import me.wuwenbin.modules.utils.util.function.TemplateSupplier;

import java.util.function.Supplier;


/**
 * Controller层经常要做的操作，这里把基本操作给函数式化了
 * 方便同意调用，省的重复写try catch代码段
 * created by Wuwenbin on 2017/12/20 at 下午3:28
 *
 * @author wuwenbin
 * @since 1.10.5.RELEASE
 */
public final class Controllers {

    private String operationName;

    private String successMsg;
    private String errorMsg;
    private String exceptionMsg;

    private Controllers(String operationName) {
        this.operationName = operationName;
    }

    private Controllers(String successMsg, String errorMsg, String exceptionMsg) {
        this.successMsg = successMsg;
        this.errorMsg = errorMsg;
        this.exceptionMsg = exceptionMsg;
    }

    public static Controllers builder(String operationName) {
        return new Controllers(operationName);
    }

    public static Controllers builder(String successMsg, String errorMsg, String exceptionMsg) {
        return new Controllers(successMsg, errorMsg, exceptionMsg);
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容
     *
     * @param preOperation
     * @param mainBody
     * @param elseSupplier
     * @return
     */
    public R exec(Supplier<Boolean> preOperation, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier) {
        if (preOperation.get()) {
            return exec(mainBody);
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 有if条件判断的
     *
     * @param templateSupplier
     * @return
     */
    public R exec(TemplateSupplier<Boolean> templateSupplier) {
        try {
            boolean res = templateSupplier.get();
            if (res) {
                String msg = operationName == null ? successMsg : operationName.concat("成功！");
                return R.ok(msg);
            } else {
                String msg = operationName == null ? errorMsg : operationName.concat("失败！");
                return R.error(msg);
            }
        } catch (Exception e) {
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            return R.error(msg);
        }
    }

    /**
     * 无需要判断的，仅需要try catch即可
     *
     * @param templateSupplier
     * @return
     */
    public R execLight(TemplateSupplier<Object> templateSupplier) {
        try {
            templateSupplier.get();
            String msg = operationName == null ? successMsg : operationName.concat("成功！");
            return R.ok(msg);
        } catch (Exception e) {
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            return R.error(msg);
        }
    }
}
