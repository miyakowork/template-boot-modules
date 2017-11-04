package me.wuwenbin.modules.repodata.exception;

/**
 * created by Wuwenbin on 2017/11/2 at 23:06
 */
public class MethodParamException extends Exception {

    public MethodParamException() {
        super("参数仅能有一个，且为Map或者JavaBean类型");
    }

    public MethodParamException(String message) {
        super(message);
    }
}
