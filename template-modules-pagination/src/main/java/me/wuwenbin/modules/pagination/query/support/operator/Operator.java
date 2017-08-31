package me.wuwenbin.modules.pagination.query.support.operator;

import java.util.Objects;

/**
 * SQL中运算操作符
 * Created by Wuwenbin on 2017/7/22.
 */
public enum Operator {


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
    BETWEEN_AND("BETWEEN", "AND");

    private String operation;
    private String subOperation;
    private String split;

    Operator(String operation) {
        this.operation = operation;
    }

    Operator(String prefix, String suffix) {
        this.operation = prefix;
        this.subOperation = suffix;

    }

    public boolean isDoubleOperator() {
        return !Objects.equals(operation, "") && !Objects.equals(subOperation, "");
    }

    public String getOperation() {
        return this.operation;
    }

    public String[] getOperations() {
        return new String[]{this.operation, this.subOperation};
    }

}
