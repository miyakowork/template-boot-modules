package me.wuwenbin.modules.sql.exception;

/**
 * 异常类
 * <p>
 * Created by wuwenbin on 2017/1/10.
 */
public class TableNameNullException extends RuntimeException {

    public TableNameNullException() {
        super("tableName 为空!");
    }
}
