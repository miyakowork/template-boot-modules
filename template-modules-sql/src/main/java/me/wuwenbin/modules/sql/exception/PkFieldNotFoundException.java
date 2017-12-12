package me.wuwenbin.modules.sql.exception;

/**
 * 字面意思
 * Created by wuwenbin on 2017/4/17.
 */
public class PkFieldNotFoundException extends RuntimeException {
    public PkFieldNotFoundException() {
        super("没有设置主键的Annotation标识，即@SQLColumn(pk=true)");
    }

}
