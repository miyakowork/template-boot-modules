package me.wuwenbin.modules.pagination;

import me.wuwenbin.modules.pagination.query.TableQuery;
import me.wuwenbin.modules.pagination.query.support.annotation.QueryColumn;
import me.wuwenbin.modules.pagination.query.support.annotation.QueryTable;
import me.wuwenbin.modules.pagination.query.support.operator.Operator;
import me.wuwenbin.modules.pagination.sort.Sorting;
import me.wuwenbin.modules.pagination.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * 使用者调用方法集合
 * created by Wuwenbin on 2017/8/30 at 11:40
 */
public class Pagination {

    private static final String EMPTY = "";

    /**
     * 获取l页面当前页码
     *
     * @param tableQuery
     * @return
     */
    public static int getPageNo(TableQuery tableQuery) {
        return tableQuery.getPageNo();
    }

    /**
     * 获取当前页面的大小
     *
     * @param tableQuery
     * @return
     */
    public static int getPageSize(TableQuery tableQuery) {
        return tableQuery.getPageSize();
    }


    /**
     * 获取查询的参数Map集合
     *
     * @param tableQuery
     * @return
     */
    public static Map<String, Object> getParamsMap(TableQuery tableQuery) {
        Map<String, Object> paramsMap = new TreeMap<>();

        //通过Java的反射机制，获取QueryBO中的属性，同时把属性中不为空的值放到查询的param中
        Field[] fields = tableQuery.getClass().getDeclaredFields();
        for (Field field : fields) {
            //设置为true以获取属性的值
            field.setAccessible(true);
            try {
                if (StringUtils.isNotEmpty(field.get(tableQuery))) {
                    String fieldName = field.getName();
                    //默认比较逻辑为like
                    Operator operator = Operator.LIKE;
                    if (field.isAnnotationPresent(QueryColumn.class)) {
                        operator = field.getAnnotation(QueryColumn.class).operator();
                    }
                    //between and逻辑，常用于时间查询
                    if (operator.equals(Operator.BETWEEN_AND)) {
                        String split = "~";
                        if (field.isAnnotationPresent(QueryColumn.class)) {
                            split = field.getAnnotation(QueryColumn.class).split();
                        }
                        String[] fieldValues = field.get(tableQuery).toString().split(split);
                        String startKey = fieldName.concat("$Start");
                        String endKey = fieldName.concat("$End");
                        String startFieldValue = StringUtils.trimEnd(fieldValues[0]);
                        String endFieldValue = StringUtils.trimStart(fieldValues[1]);
                        paramsMap.put(startKey, startFieldValue);
                        paramsMap.put(endKey, endFieldValue);
                    }
                    //其他的逻辑
                    else {
                        Object fieldValue;
                        //like逻辑
                        if (operator.equals(Operator.LIKE)) {
                            fieldValue = StringUtils.format("%{}%", field.get(tableQuery));
                        } else if (operator.equals(Operator.LEFT_LIKE)) {
                            fieldValue = StringUtils.format("%{}", field.get(tableQuery));
                        } else if (operator.equals(Operator.RIGHT_LIKE)) {
                            fieldValue = StringUtils.format("{}%", field.get(tableQuery));
                        }
                        //非like、非自定义逻辑的存入参数Map中的值则不需要%%包裹起来
                        else {
                            fieldValue = field.get(tableQuery);
                        }
                        paramsMap.put(fieldName, fieldValue);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return paramsMap;
    }

    /**
     * 获取查询部分的sql
     *
     * @param mainSql
     * @param tableQuery
     * @return
     */
    public static String getSql(String mainSql, TableQuery tableQuery) {
        StringBuilder sql = new StringBuilder(mainSql);
        if (!mainSql.contains("WHERE") && !mainSql.contains("where")) {
            sql = sql.append(" WHERE 1=1");
        }
        String tableName = "";
        if (tableQuery.getClass().isAnnotationPresent(QueryTable.class)) {
            String tableNameInClass = tableQuery.getClass().getAnnotation(QueryTable.class).name();
            String tableAliasNameInClass = tableQuery.getClass().getAnnotation(QueryTable.class).aliasName();
            //获取表名，原则是：类上的表别名>类上的表名>""
            tableName = StringUtils.isNotEmpty(tableAliasNameInClass) ? tableAliasNameInClass : StringUtils.isNotEmpty(tableNameInClass) ? tableNameInClass : "";
        }
        //通过Java的反射机制，获取QueryBO中的属性，同时把属性中不为空的值放到查询的param中
        Field[] fields = tableQuery.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(QueryColumn.class)) {
                String tableNameInField = field.getAnnotation(QueryColumn.class).tableName();
                String tableAliasNameInField = field.getAnnotation(QueryColumn.class).tableAlias();
                //获取表名，原则是：字段属性上的表别名>字段属性表名>类上的表别名>类上的表名>""
                tableName = StringUtils.isNotEmpty(tableAliasNameInField) ? tableAliasNameInField : StringUtils.isNotEmpty(tableNameInField) ? tableNameInField : tableName;
            }
            //设置为true以获取属性的值
            field.setAccessible(true);

            //开始拼装SQL语句，以及生成语句中的参数对应的参数map
            try {
                if (StringUtils.isNotEmpty(field.get(tableQuery))) {
                    String fieldName = field.getName();
                    //默认属性和字段是一致的
                    String columnNameInDb = fieldName;
                    //默认比较逻辑为like
                    Operator operator = Operator.LIKE;
                    String constraint = "AND";
                    if (field.isAnnotationPresent(QueryColumn.class)) {
                        columnNameInDb = StringUtils.java2SQL(field.getAnnotation(QueryColumn.class).column(), field.getName());
                        operator = field.getAnnotation(QueryColumn.class).operator();
                        constraint = field.getAnnotation(QueryColumn.class).constraint();
                    }

                    //between and逻辑，常用于时间查询
                    if (operator.equals(Operator.BETWEEN_AND)) {
                        String startKey = fieldName.concat("$Start");
                        String endKey = fieldName.concat("$End");
                        String temp;
                        if (StringUtils.isNotEmpty(tableName)) {
                            temp = StringUtils.format(" {} ({}.`{}` BETWEEN  :{} AND :{})", constraint, tableName, columnNameInDb, startKey, endKey);
                        } else {
                            temp = StringUtils.format(" {} (`{}` BETWEEN :{} AND :{})", constraint, columnNameInDb, startKey, endKey);
                        }
                        sql.append(temp);
                    }
                    //其他的逻辑
                    else {
                        String temp;
                        if (StringUtils.isNotEmpty(tableName)) {
                            temp = StringUtils.format(" {} {}.`{}` {} :{}", constraint, tableName, columnNameInDb, operator.getOperator(), fieldName);
                        } else {
                            temp = StringUtils.format(" {} `{}` {} :{}", constraint, columnNameInDb, operator.getOperator(), fieldName);
                        }
                        sql.append(temp);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //组装order部分
        String orderSql = " ORDER BY";
        //  先检查下多列的一起排序的是否有，有就按照这个来
        if (tableQuery.getSortingInformation() != null) {
            for (Sorting sort : tableQuery.getSortingInformation()) {
                String sortName = sort.getSortName();
                String sortDirection = sort.getSortDirection().getDirectionString();
                orderSql = orderSql.concat(StringUtils.format(" `{}` {}", sortName, sortDirection)).concat(",");
            }
            //处理一下sql，去除多余的符号
            orderSql = orderSql.substring(0, orderSql.length() - 1);
        }
        //然后查下只有单列排序的是否存在，没有多列的话就按照单列的排序来
        else if (tableQuery.getSortingInfo() != null) {
            Sorting sort = tableQuery.getSortingInfo();
            String sortName = sort.getSortName();
            String sortDirection = sort.getSortDirection().getDirectionString();
            orderSql = orderSql.concat(StringUtils.format(" `{}` {}", sortName, sortDirection));
        }
        //然后就是都没有，则不需要排序
        else {
            orderSql = EMPTY;
        }
        sql.append(orderSql);
        return sql.toString();
    }
}
