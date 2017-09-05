package me.wuwenbin.modules.mongodb.support.exception;

/**
 * Created by wuwenbin on 2017/4/22.
 */
public class DataSourceKeyNotExistException extends RuntimeException {

    public DataSourceKeyNotExistException() {
        super("传入的key不在数据源map中");
    }
}
