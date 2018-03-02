package me.wuwenbin.modules.repository.provider.crud;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.sql.SQLGen;
import me.wuwenbin.modules.sql.annotation.GeneralType;
import me.wuwenbin.modules.sql.annotation.SQLColumn;
import me.wuwenbin.modules.sql.annotation.SQLPk;
import me.wuwenbin.modules.sql.annotation.SQLTable;
import me.wuwenbin.modules.sql.annotation.support.PkGenType;
import me.wuwenbin.modules.sql.exception.PkFieldNotFoundException;
import me.wuwenbin.modules.sql.factory.SQLBeanBuilder;
import me.wuwenbin.modules.sql.factory.SQLTextBuilder;
import me.wuwenbin.modules.sql.util.SQLDefineUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 此处准备好一些需要用的值，提供给实现类用
 * created by Wuwenbin on 2017/11/1 at 20:02
 *
 * @author Wuwenbin
 */
public abstract class AbstractProvider<T> implements ICrudProvider {

    private Class<T> clazz;
    private Method method;
    private AncestorDao jdbcTemplate;

    protected SQLBeanBuilder sbb;
    protected SQLTextBuilder stb;
    protected String tableName;
    protected boolean isPkInsert;
    protected String pkFiledName;
    protected String pkDbName;


    public AbstractProvider(Method method, AncestorDao jdbcTemplate, Class<T> clazz) {
        this.method = method;
        this.clazz = clazz;
        this.jdbcTemplate = jdbcTemplate;
        init();
    }

    protected Class<T> getClazz() {
        return clazz;
    }

    protected AncestorDao getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    protected Method getMethod() {
        return method;
    }

    private void init() {
        SQLTable sqlTable;
        try {
            sqlTable = getClazz().getAnnotation(SQLTable.class);
        } catch (NullPointerException e) {
            throw new RuntimeException("未指定@SQLTable", e);
        }
        Field pk;
        try {
            pk = SQLGen.builder(getClazz()).getPkField();
            this.pkFiledName = pk.getName();
            this.isPkInsert = pk.isAnnotationPresent(GeneralType.class) && pk.getAnnotation(GeneralType.class).value().equals(PkGenType.DEFINITION);
            if (pk.isAnnotationPresent(SQLColumn.class)) {
                this.pkDbName = SQLDefineUtils.java2SQL(pk.getAnnotation(SQLColumn.class).value(), this.pkFiledName);
            } else {
                this.pkDbName = SQLDefineUtils.java2SQL(pk.getAnnotation(SQLPk.class).value(), this.pkFiledName);
            }
        } catch (PkFieldNotFoundException e) {
            this.pkFiledName = "";
            this.isPkInsert = false;
            this.pkDbName = "";
        }
        this.tableName = SQLDefineUtils.java2SQL(sqlTable.value(), getClazz().getSimpleName());
        this.sbb = SQLGen.builder(getClazz());
        this.stb = SQLGen.builder();
    }

}
