package me.wuwenbin.modules.repository.provider.find.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * created by Wuwenbin on 2017/11/7 at 22:08
 *
 * @author Wuwenbin
 */
public class Condition {

    private PreJoin preJoin;
    private String field;
    private Constraint constraint;
    private Object value;

    private Condition(PreJoin preJoin, String field, Constraint constraint, Object value) {
        this.preJoin = preJoin;
        this.field = field;
        this.value = value;
        this.constraint = constraint;
    }

    public PreJoin getPreJoin() {
        return preJoin;
    }

    public void setPreJoin(PreJoin preJoin) {
        this.preJoin = preJoin;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }


    public static Condition build(PreJoin preJoin, String field, Object value, Constraint constraint) {
        return new Condition(preJoin, field, constraint, value);
    }

    public static Condition build(String field, Object value) {
        return new Condition(PreJoin.AND, field, Constraint.Equal, value);
    }

    public static Condition build(String field, Constraint constraint, Object value) {
        return new Condition(PreJoin.AND, field, constraint, value);
    }

    public static List<Condition> buildList(FiledValue... filedValues) {
        List<Condition> conditions = new ArrayList<>(filedValues.length);
        for (FiledValue filedValue : filedValues) {
            Condition condition = Condition.build(filedValue.getField(), filedValue.getValue());
            conditions.add(condition);
        }
        return conditions;
    }

    public static List<Condition> buildList(Condition... conditions) {
        List<Condition> conditionList = new ArrayList<>(conditions.length);
        conditionList.addAll(Arrays.asList(conditions));
        return conditionList;
    }


}