package me.wuwenbin.modules.repository.exception;

/**
 * created by Wuwenbin on 2017/11/1 at 15:20
 */
public class MethodTypeMissMatch extends Exception {

    public MethodTypeMissMatch() {
        super("方法名命名有误，未找到此方法匹配的类型！");
    }

    public MethodTypeMissMatch(String message) {
        super(message);
    }
}
