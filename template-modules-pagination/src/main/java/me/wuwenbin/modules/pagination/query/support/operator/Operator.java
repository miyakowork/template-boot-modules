package me.wuwenbin.modules.pagination.query.support.operator;

import java.util.Objects;

/**
 * SQL中运算操作符
 *
 * @author Wuwenbin
 * @date 2017/7/22
 */
public enum Operator {

    /**
     * 运算符级别
     */
    EQ("="),
    NE("!="),
    GLT("<>"),
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    NLT("!<"),
    NGT("!>"),
    LIKE("LIKE"),
    LEFT_LIKE("LIKE"),
    RIGHT_LIKE("LIKE"),
    BETWEEN_AND("BETWEEN", "AND");

    private String operator;
    private String subOperation;

    Operator(String operator) {
        this.operator = operator;
    }

    Operator(String prefix, String suffix) {
        this.operator = prefix;
        this.subOperation = suffix;

    }

    public boolean isDoubleOperator() {
        return !Objects.equals(operator, "") && !Objects.equals(subOperation, "");
    }

    public String getOperator() {
        return this.operator;
    }

    public String[] getOperations() {
        return new String[]{this.operator, this.subOperation};
    }


}
