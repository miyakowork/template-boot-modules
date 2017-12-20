package me.wuwenbin.modules.utils.rest;

import me.wuwenbin.modules.utils.http.R;
import me.wuwenbin.modules.utils.support.MySupplier;

import java.util.function.Supplier;


/**
 * created by Wuwenbin on 2017/12/20 at 下午3:28
 *
 * @author wuwenbin
 */
public final class Rests {

    private String operationName;

    private String successMsg;
    private String errorMsg;
    private String exceptionMsg;

    private Rests(String operationName) {
        this.operationName = operationName;
    }

    private Rests(String successMsg, String errorMsg, String exceptionMsg) {
        this.successMsg = successMsg;
        this.errorMsg = errorMsg;
        this.exceptionMsg = exceptionMsg;
    }

    public static Rests builder(String operationName) {
        return new Rests(operationName);
    }

    public static Rests builder(String successMsg, String errorMsg, String exceptionMsg) {
        return new Rests(successMsg, errorMsg, exceptionMsg);
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容
     *
     * @param preOperation
     * @param mainBody
     * @param elseSupplier
     * @return
     */
    public R exec(Supplier<Boolean> preOperation, MySupplier<Boolean> mainBody, Supplier<R> elseSupplier) {
        if (preOperation.get()) {
            return exec(mainBody);
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 有if条件判断的
     *
     * @param mySupplier
     * @return
     */
    public R exec(MySupplier<Boolean> mySupplier) {
        try {
            boolean res = mySupplier.get();
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
     * @param mySupplier
     * @return
     */
    public R execLight(MySupplier<Object> mySupplier) {
        try {
            mySupplier.get();
            String msg = operationName == null ? successMsg : operationName.concat("成功！");
            return R.ok(msg);
        } catch (Exception e) {
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            return R.error(msg);
        }
    }
}
