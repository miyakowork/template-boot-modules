package me.wuwenbin.modules.repodata.provider.crud;

import me.wuwenbin.modules.jdbc.ancestor.AncestorDao;
import me.wuwenbin.tools.sqlgen.SQLGen;
import me.wuwenbin.tools.sqlgen.annotation.GeneralType;
import me.wuwenbin.tools.sqlgen.annotation.SQLColumn;
import me.wuwenbin.tools.sqlgen.annotation.SQLTable;
import me.wuwenbin.tools.sqlgen.annotation.support.PkGenType;
import me.wuwenbin.tools.sqlgen.factory.SQLBeanBuilder;
import me.wuwenbin.tools.sqlgen.factory.SQLTextBuilder;
import me.wuwenbin.tools.sqlgen.util.SQLDefineUtils;

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
    private AncestorDao jdbcTemplate;
    private Method method;

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
        return jdbcTemplate;
    }

    protected Method getMethod() {
        return method;
    }

    private void init() {
        SQLTable sqlTable = this.clazz.getAnnotation(SQLTable.class);
        Field pk = SQLGen.builder(this.clazz).getPkField();
        this.tableName = SQLDefineUtils.java2SQL(sqlTable.value(), this.clazz.getSimpleName());
        this.pkFiledName = pk.getName();
        this.isPkInsert = pk.isAnnotationPresent(GeneralType.class) && pk.getAnnotation(GeneralType.class).value().equals(PkGenType.DEFINITION);
        this.pkDbName = SQLDefineUtils.java2SQL(pk.getAnnotation(SQLColumn.class).value(), this.pkFiledName);
        this.sbb = SQLGen.builder(this.clazz);
        this.stb = SQLGen.builder();
    }
}
