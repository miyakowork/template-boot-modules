package me.wuwenbin.modules.sql.exception;

/**
 * 未识别的主键，不存在或没标识注解
 * <p>
 * Created by wuwenbin on 2017/1/12.
 */
public class UpdatePkNotExistException extends RuntimeException {
    public UpdatePkNotExistException() {
        super("主键不存在或没有使用注解标识 !");
    }
}
