package me.wuwenbin.modules.pagination.query.support.params;

import me.wuwenbin.modules.pagination.query.support.operator.Operator;


/**
 * 除了基本的字段(pageNo/pageSize等)，还有一些表格的查询字段，
 * 这些字段也是组成查询sql语句一部分
 * created by Wuwenbin on 2017/8/30 at 11:02
 */
public class QueryParams {

    private String filedName;
    private String filedValue;
    private String columnName;
    private Operator operator;

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String getFiledValue() {
        return filedValue;
    }

    public void setFiledValue(String filedValue) {
        this.filedValue = filedValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

}
