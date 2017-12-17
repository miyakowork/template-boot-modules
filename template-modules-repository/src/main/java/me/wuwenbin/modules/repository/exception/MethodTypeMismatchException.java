package me.wuwenbin.modules.repository.exception;

/**
 * created by Wuwenbin on 2017/11/1 at 15:20
 */
public class MethodTypeMismatchException extends Exception {

    public MethodTypeMismatchException() {
        super("方法名命名有误，未找到此方法匹配的类型！");
    }

    public MethodTypeMismatchException(String message) {
        super(message);
    }
}
