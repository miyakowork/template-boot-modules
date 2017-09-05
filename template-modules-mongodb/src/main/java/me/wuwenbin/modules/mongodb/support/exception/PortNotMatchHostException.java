package me.wuwenbin.modules.mongodb.support.exception;

/**
 * Created by wuwenbin on 2017/4/23.
 */
public class PortNotMatchHostException extends RuntimeException {
    public PortNotMatchHostException() {
        super();
    }

    public PortNotMatchHostException(String message) {
        super(message);
    }
}
