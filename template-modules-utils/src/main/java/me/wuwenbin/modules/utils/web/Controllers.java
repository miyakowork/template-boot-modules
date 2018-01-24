package me.wuwenbin.modules.utils.web;

import me.wuwenbin.modules.utils.http.R;
import me.wuwenbin.modules.utils.util.function.TemplateConsumer;
import me.wuwenbin.modules.utils.util.function.TemplateSupplier;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;


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
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @return
     */
    public R exec(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier) {
        if (preOperate.get()) {
            return exec(mainBody);
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容，mainBody可以带额外的操作的（例如在执行成功之后给R在put额外的参数）
     *
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @param successOperate
     * @param failureOperate
     * @param exceptionOperate
     * @return
     */
    public R exec(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier,
                  UnaryOperator<R> successOperate, UnaryOperator<R> failureOperate, UnaryOperator<R> exceptionOperate) {
        if (preOperate.get()) {
            return exec(mainBody, successOperate, failureOperate, exceptionOperate);
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容，mainBody可以带额外的操作的（例如在执行成功之后给R在put额外的参数）
     *
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @param successOperate
     * @param failureOperate
     * @return
     */
    public R execWrapSuccessFailure(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier,
                                    UnaryOperator<R> successOperate, UnaryOperator<R> failureOperate) {
        if (preOperate.get()) {
            return exec(mainBody, successOperate, failureOperate, UnaryOperator.identity());
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容，mainBody可以带额外的操作的（例如在执行成功之后给R在put额外的参数）
     *
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @param successOperate
     * @param exceptionOperate
     * @return
     */
    public R execWrapSuccessException(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier,
                                      UnaryOperator<R> successOperate, UnaryOperator<R> exceptionOperate) {
        if (preOperate.get()) {
            return exec(mainBody, successOperate, UnaryOperator.identity(), exceptionOperate);
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容，mainBody可以带额外的操作的（例如在执行成功之后给R在put额外的参数）
     *
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @param failureException
     * @param exceptionOperate
     * @return
     */
    public R execWrapFailureException(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier,
                                      UnaryOperator<R> failureException, UnaryOperator<R> exceptionOperate) {
        if (preOperate.get()) {
            return exec(mainBody, UnaryOperator.identity(), failureException, exceptionOperate);
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容，mainBody可以带额外的操作的（例如在执行成功之后给R在put额外的参数）
     *
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @param successOperate
     * @return
     */
    public R execWrapSuccess(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier,
                             UnaryOperator<R> successOperate) {
        if (preOperate.get()) {
            return exec(mainBody, successOperate, UnaryOperator.identity(), UnaryOperator.identity());
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容，mainBody可以带额外的操作的（例如在执行成功之后给R在put额外的参数）
     *
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @param failureOperate
     * @return
     */
    public R execWrapFailure(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier,
                             UnaryOperator<R> failureOperate) {
        if (preOperate.get()) {
            return exec(mainBody, UnaryOperator.identity(), failureOperate, UnaryOperator.identity());
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * 带操作之前判断的，判断为真则执行主要内容，mainBody可以带额外的操作的（例如在执行成功之后给R在put额外的参数）
     *
     * @param preOperate
     * @param mainBody
     * @param elseSupplier
     * @param exceptionOperate
     * @return
     */
    public R execWrapException(Supplier<Boolean> preOperate, TemplateSupplier<Boolean> mainBody, Supplier<R> elseSupplier,
                               UnaryOperator<R> exceptionOperate) {
        if (preOperate.get()) {
            return exec(mainBody, UnaryOperator.identity(), UnaryOperator.identity(), exceptionOperate);
        } else {
            return elseSupplier.get();
        }
    }

    /**
     * try catch且带有if条件判断的
     *
     * @param ifCondition
     * @return
     */
    public R exec(TemplateSupplier<Boolean> ifCondition) {
        try {
            boolean res = ifCondition.get();
            if (res) {
                String msg = operationName == null ? successMsg : operationName.concat("成功！");
                return R.ok(msg);
            } else {
                String msg = operationName == null ? errorMsg : operationName.concat("失败！");
                return R.error(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            return R.error(msg);
        }
    }


    /**
     * try catch且带有if条件判断的，成功之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param successOperate
     * @return
     */
    public R execWrapSuccess(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> successOperate) {
        return exec(ifCondition, successOperate, UnaryOperator.identity(), UnaryOperator.identity());
    }

    /**
     * try catch且带有if条件判断的，失败之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param failureOperate
     * @return
     */
    public R execWrapFailure(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> failureOperate) {
        return exec(ifCondition, UnaryOperator.identity(), failureOperate, UnaryOperator.identity());
    }

    /**
     * try catch且带有if条件判断的，异常之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param exceptionOperate
     * @return
     */
    public R execWrapException(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> exceptionOperate) {
        return exec(ifCondition, UnaryOperator.identity(), UnaryOperator.identity(), exceptionOperate);
    }

    /**
     * try catch且带有if条件判断的，成功或失败之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param successOperate
     * @param failureOperate
     * @return
     */
    public R execWrapSuccessFailure(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> successOperate, UnaryOperator<R> failureOperate) {
        return exec(ifCondition, successOperate, failureOperate, UnaryOperator.identity());
    }

    /**
     * try catch且带有if条件判断的，成功或异常之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param successOperate
     * @param exceptionOperate
     * @return
     */
    public R execWrapSuccessException(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> successOperate, UnaryOperator<R> exceptionOperate) {
        return exec(ifCondition, successOperate, UnaryOperator.identity(), exceptionOperate);
    }

    /**
     * try catch且带有if条件判断的，失败或异常之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @param failureOperate
     * @param exceptionOperate
     * @return
     */
    public R execWrapFailureException(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> failureOperate, UnaryOperator<R> exceptionOperate) {
        return exec(ifCondition, UnaryOperator.identity(), failureOperate, exceptionOperate);
    }

    /**
     * try catch且带有if条件判断的，成功/失败/异常之后可以给R添加额外参数的
     *
     * @param ifCondition
     * @return
     */
    public R exec(TemplateSupplier<Boolean> ifCondition, UnaryOperator<R> successOperate, UnaryOperator<R> failureOperate, UnaryOperator<R> exceptionOperate) {
        try {
            boolean res = ifCondition.get();
            if (res) {
                String msg = operationName == null ? successMsg : operationName.concat("成功！");
                R r = R.ok(msg);
                return successOperate.apply(r);
            } else {
                String msg = operationName == null ? errorMsg : operationName.concat("失败！");
                R r = R.error(msg);
                return failureOperate.apply(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            R r = R.error(msg);
            return exceptionOperate.apply(r);
        }
    }

    /**
     * 在删除之前需要做if操作判断的
     *
     * @param ifCondition
     * @param t
     * @param tryOperate
     * @param <T>
     * @return
     */
    public <T> R execLight(TemplateSupplier<Boolean> ifCondition, T t, TemplateConsumer<T> tryOperate) {
        try {
            boolean flag = ifCondition.get();
            if (flag) {
                return execLight(t, tryOperate);
            } else {
                String msg = operationName == null ? errorMsg : operationName.concat("失败！");
                return R.error(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            return R.error(msg);
        }
    }

    /**
     * 在删除之前需要做if操作判断的，并且else失败条件自定义
     *
     * @param ifCondition
     * @param t
     * @param tryOperate
     * @param <T>
     * @return
     */
    public <T> R execLight(TemplateSupplier<Boolean> ifCondition, T t, TemplateConsumer<T> tryOperate, String message) {
        try {
            boolean flag = ifCondition.get();
            if (flag) {
                return execLight(t, tryOperate);
            } else {
                String msg = message == null ? errorMsg : message;
                return R.error(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            return R.error(msg);
        }
    }

    /**
     * 无需要判断的，仅需要try catch即可
     *
     * @param <T>
     * @param t
     * @param tryOperate
     * @return
     */
    public <T> R execLight(T t, TemplateConsumer<T> tryOperate) {
        return execLight(t, tryOperate, UnaryOperator.identity(), UnaryOperator.identity());
    }

    /**
     * 无需要判断的，仅需要try catch即可，获取结果之后可以给R赋予额外的参数
     *
     * @param <T>
     * @param t
     * @param tryOperate
     * @return
     */
    public <T> R execLight(T t, TemplateConsumer<T> tryOperate, UnaryOperator<R> tryResultOperate, UnaryOperator<R> exceptionOperate) {
        try {
            tryOperate.accept(t);
            String msg = operationName == null ? successMsg : operationName.concat("成功！");
            R r = R.ok(msg);
            return tryResultOperate.apply(r);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = operationName == null ? exceptionMsg : operationName.concat("异常，原因：").concat(e.getMessage());
            R r = R.error(msg);
            return exceptionOperate.apply(r);
        }
    }

    /**
     * 无需要判断的，仅需要try catch即可，可以给try中的r结果添加额外的参数或者操作方法
     *
     * @param t
     * @param tryOperate
     * @param tryResultOperate
     * @param <T>
     * @return
     */
    public <T> R execLightWrapTry(T t, TemplateConsumer<T> tryOperate, UnaryOperator<R> tryResultOperate) {
        return execLight(t, tryOperate, tryResultOperate, UnaryOperator.identity());
    }

    /**
     * 无需要判断的，仅需要try catch即可，可以给try中的r结果添加额外的参数或者操作方法
     *
     * @param t
     * @param tryOperate
     * @param catchOperate
     * @param <T>
     * @return
     */
    public <T> R execLightWrapCatch(T t, TemplateConsumer<T> tryOperate, UnaryOperator<R> catchOperate) {
        return execLight(t, tryOperate, UnaryOperator.identity(), catchOperate);
    }
}
