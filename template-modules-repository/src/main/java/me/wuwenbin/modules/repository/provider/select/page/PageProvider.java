package me.wuwenbin.modules.repository.provider.select.page;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.jpa.support.Page;
import me.wuwenbin.modules.pagination.Pagination;
import me.wuwenbin.modules.pagination.query.TableQuery;
import me.wuwenbin.modules.pagination.util.StringUtils;
import me.wuwenbin.modules.repository.annotation.sql.SQLPkRefer;
import me.wuwenbin.modules.repository.annotation.sql.SQLRefer;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repository.provider.select.page.support.IPageExtra;
import me.wuwenbin.modules.repository.provider.select.page.support.PageExtraFactory;
import me.wuwenbin.modules.sql.SQLGen;
import me.wuwenbin.modules.sql.annotation.SQLColumn;
import me.wuwenbin.modules.sql.annotation.SQLTable;
import me.wuwenbin.modules.sql.factory.SQLBeanBuilder;
import me.wuwenbin.modules.sql.util.SQLDefineUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/11/15 at 15:00
 *
 * @author Wuwenbin
 */
public class PageProvider<T> extends AbstractProvider<T> {

    public PageProvider(Method method, AncestorDao jdbcTemplate, Class<T> clazz) {
        super(method, jdbcTemplate, clazz);
    }

    @Override
    public Object execute(Object[] args) throws Exception {
        int paramLength = args.length;
        if (paramLength == 3) {
            Page page = (Page) args[0];
            Class clazz = (Class) args[1];
            TableQuery tableQuery = (TableQuery) args[2];
            StringBuilder sqlBuilder = new StringBuilder("select");
            StringBuilder joinSqlBuilder = new StringBuilder();
            String mainTableName = getTableName(clazz);
            sqlBuilder.append(" ").append(mainTableName).append(".*");
            Field[] fields = clazz.getDeclaredFields();
            handleFields(sqlBuilder, joinSqlBuilder, mainTableName, fields);
            sqlBuilder.append(" from ").append(mainTableName).append(joinSqlBuilder);
            String mainSql = sqlBuilder.toString();
            return execute(page, clazz, tableQuery, mainSql);
        } else if (paramLength == 4) {
            Object firstArg = args[0];
            if (IPageExtra.class.isAssignableFrom(firstArg.getClass())) {
                IPageExtra pageExtra = (IPageExtra) args[0];
                Page page = (Page) args[1];
                Class clazz = (Class) args[2];
                TableQuery tableQuery = (TableQuery) args[3];
                StringBuilder sqlBuilder = new StringBuilder("select");
                StringBuilder joinSqlBuilder = new StringBuilder();
                String mainTableName = getTableName(clazz);
                sqlBuilder.append(" ").append(mainTableName).append(".*");
                Field[] fields = clazz.getDeclaredFields();
                handleFields(sqlBuilder, joinSqlBuilder, mainTableName, fields);
                sqlBuilder.append(" from ").append(mainTableName).append(joinSqlBuilder);
                String mainSql = sqlBuilder.toString();
                mainSql = PageExtraFactory.finalSql(pageExtra, mainSql);
                return execute(page, clazz, tableQuery, mainSql);
            } else {
                String mainSql = (String) args[0];
                Page page = (Page) args[1];
                Class clazz = (Class) args[2];
                TableQuery tableQuery = (TableQuery) args[3];
                return execute(page, clazz, tableQuery, mainSql);
            }
        } else {
            throw new MethodExecuteException("方法:「" + super.getMethod().getName() + "」方法命名有误，请参考命名规则！");
        }
    }

    private void handleFields(StringBuilder sqlBuilder, StringBuilder joinSqlBuilder, String mainTableName, Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(SQLPkRefer.class)) {
                String targetTableAliasName = field.getAnnotation(SQLPkRefer.class).targetTableAlias();
                String minorTableName = getTableName(field.getAnnotation(SQLPkRefer.class).targetClass());
                SQLBeanBuilder tempSbb = SQLGen.builder(field.getAnnotation(SQLPkRefer.class).targetClass());
                String minorPkName = SQLDefineUtils.java2SQL(tempSbb.getPkField().getAnnotation(SQLColumn.class).value(), tempSbb.getPkField().getName());
                String selfJoinColumn = field.getAnnotation(SQLPkRefer.class).joinColumn();
                String minorSelectColumn = field.getAnnotation(SQLPkRefer.class).targetColumn();
                handlerBuilderByAliasIsEmpty(sqlBuilder, joinSqlBuilder, mainTableName, field, targetTableAliasName, minorTableName, minorPkName, selfJoinColumn, minorSelectColumn);
            }
            if (field.isAnnotationPresent(SQLRefer.class)) {
                String targetTableAliasName = field.getAnnotation(SQLRefer.class).targetTableAlias();
                String minorTableName = getTableName(field.getAnnotation(SQLRefer.class).targetClass());
                String minorPkName = field.getAnnotation(SQLRefer.class).referColumn();
                String selfJoinColumn = field.getAnnotation(SQLRefer.class).joinColumn();
                String minorSelectColumn = field.getAnnotation(SQLRefer.class).targetColumn();
                handlerBuilderByAliasIsEmpty(sqlBuilder, joinSqlBuilder, mainTableName, field, targetTableAliasName, minorTableName, minorPkName, selfJoinColumn, minorSelectColumn);
            }
        }
    }

    private void handlerBuilderByAliasIsEmpty(StringBuilder sqlBuilder, StringBuilder joinSqlBuilder,
                                              String mainTableName, Field field, String targetTableAliasName,
                                              String minorTableName, String minorPkName, String selfJoinColumn, String minorSelectColumn) {
        if (StringUtils.isEmpty(targetTableAliasName)) {
            sqlBuilder.append(",").append(minorTableName).append(".")
                    .append(minorSelectColumn).append(" as ").append(field.getName());
            joinSqlBuilder.append(" left join").append(" ")
                    .append(minorTableName).append(" on ").append(minorTableName)
                    .append(".").append(minorPkName).append(" = ").append(mainTableName)
                    .append(".").append(selfJoinColumn);
        } else {
            sqlBuilder.append(",").append(targetTableAliasName)
                    .append(".").append(minorSelectColumn).append(" as ").append(field.getName());
            joinSqlBuilder.append(" left join").append(" ").append(minorTableName)
                    .append(" ").append(targetTableAliasName).append(" on ").append(targetTableAliasName)
                    .append(".").append(minorPkName).append(" = ").append(mainTableName)
                    .append(".").append(selfJoinColumn);
        }
    }

    /**
     * 执行分页方法
     *
     * @param page
     * @param clazz
     * @param tableQuery
     * @param mainSql
     * @return
     */
    @SuppressWarnings("unchecked")
    private Object execute(Page page, Class clazz, TableQuery tableQuery, String mainSql) {
        String finalSQL = Pagination.getSql(mainSql, tableQuery);
        Map<String, Object> paramsMap = Pagination.getParamsMap(tableQuery);
        //因为此处的Page中的排序OrderDirection和OrderField与前台BootstrapTable中所传的参数(sort,order)不一致
        //所以此处需要从QueryBO中拿取(sort和order)来设置到page中，以及分页大小和所要查询的页码
        page.setPageNo(Pagination.getPageNo(tableQuery));
        page.setPageSize(Pagination.getPageSize(tableQuery));
        return getJdbcTemplate().findPageListBeanByMap(finalSQL, clazz, page, paramsMap);
    }


    /**
     * 根据注解和类型来获取最后的表名
     *
     * @param javaBean
     * @return
     */
    private String getTableName(Class<?> javaBean) {
        String tableName;
        if (javaBean.isAnnotationPresent(SQLTable.class)) {
            tableName = SQLDefineUtils.java2SQL(javaBean.getAnnotation(SQLTable.class).value(), javaBean.getSimpleName());
        } else {
            tableName = SQLDefineUtils.java2SQL("", javaBean.getSimpleName());
        }
        return tableName;
    }

}
