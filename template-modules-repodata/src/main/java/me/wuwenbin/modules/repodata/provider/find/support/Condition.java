package me.wuwenbin.modules.repodata.provider.find.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * created by Wuwenbin on 2017/11/7 at 22:08
 *
 * @author Wuwenbin
 */
public class Condition {

    private PreJoin preJoin = PreJoin.AND;
    private String field;
    private Object value;
    private Constraint constraint = Constraint.Equal;

    public Condition() {
    }

    public Condition(PreJoin preJoin, String field, Object value, Constraint constraint) {
        this.preJoin = preJoin;
        this.field = field;
        this.value = value;
        this.constraint = constraint;
    }

    public Condition(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public Condition(String field, Object value, Constraint constraint) {
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

    public static Condition builder(PreJoin preJoin, String field, Object value, Constraint constraint) {
        return new Condition(preJoin, field, value, constraint);
    }

    public static Condition builderDefault(String field, Object value) {
        return new Condition(field, value);
    }

    public static Condition builderSimple(String field, Constraint constraint) {
        return new Condition(field, constraint);
    }

    public static List<Condition> defaultList(Param... params) {
        List<Condition> conditions = new ArrayList<>(params.length);
        for (Param param : params) {
            Condition condition = Condition.builderDefault(param.getField(), param.getValue());
            conditions.add(condition);
        }
        return conditions;
    }

    public static List<Condition> list(Condition... conditions) {
        List<Condition> conditionList = new ArrayList<>(conditions.length);
        conditionList.addAll(Arrays.asList(conditions));
        return conditionList;
    }


}