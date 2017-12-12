package me.wuwenbin.modules.sql.exception;

/**
 * 异常类
 * <p>
 * Created by wuwenbin on 2017/1/12.
 */
public class DeletePkNotExistException extends RuntimeException {
    public DeletePkNotExistException() {
        super("主键不存在或没有使用注解标识 !");
    }
}
