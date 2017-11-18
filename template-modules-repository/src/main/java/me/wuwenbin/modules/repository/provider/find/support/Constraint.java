package me.wuwenbin.modules.repository.provider.find.support;

import org.springframework.util.StringUtils;

/**
 * created by Wuwenbin on 2017/11/7 at 12:17
 *
 * @author Wuwenbin
 */
public enum Constraint {
    /**
     * 约束的条件
     */
    Equal("="),
    Eq("="),
    NotEqual("<>"),
    Ne("<>"),
    Between,
    LessThan("<"),
    Lt("<"),
    LessThanEqual("<="),
    Lte("<="),
    GreaterThan(">"),
    Gt(">"),
    GreaterThanEqual(">="),
    Gte(">="),
    Like("like"),
    NotLike("not like"),
    In("in"),
    NotIn("not in"),
    Not("not"),
    IsNull,
    IsNotNull;

    private String constraint;

    Constraint() {
    }

    Constraint(String constraint) {
        this.constraint = constraint;
    }

    public String getConstraint() {
        return this.constraint;
    }

    /**
     * 判断是不是关键字约束
     *
     * @return
     */
    public boolean keyword() {
        return !StringUtils.isEmpty(getConstraint());
    }

    /**
     * 拼装部分的sql语句
     *
     * @param field
     * @return
     */
    public String getPart(String field) {
        String text = "";
        text = getString(field, field, text);
        return text;
    }

    /**
     * 拼装部分的sql语句，提供给字段和参数名不一样的方法
     *
     * @param column
     * @param field
     * @return
     */
    public String getPart(String column, String field) {
        String text = "";
        text = getString(column, field, text);
        return text;
    }

    private String getString(String column, String field, String text) {
        if (keyword()) {
            text = column.concat(" ").concat(this.constraint).concat(" :").concat(field);
        } else if (this.equals(Constraint.Between)) {
            text = column.concat(" ").concat("between :prefix").concat(field).concat(" and :suffix").concat(field);
        } else if (this.equals(Constraint.IsNull)) {
            text = column.concat(" ").concat("is null");
        } else if (this.equals(Constraint.IsNotNull)) {
            text = column.concat(" ").concat("is not null");
        }
        return text;
    }

    /**
     * 从名字判断返回Constraint
     *
     * @param name
     * @return
     */
    public Constraint getFromName(String name) {
        if (StringUtils.isEmpty(name) || name.equals(Equal.toString())) {
            return Equal;
        } else if (name.equals(Eq.toString())) {
            return Eq;
        } else if (name.equals(NotEqual.toString())) {
            return NotEqual;
        } else if (name.equals(Ne.toString())) {
            return Ne;
        } else if (name.equals(Between.toString())) {
            return Between;
        } else if (name.equals(LessThan.toString())) {
            return LessThan;
        } else if (name.equals(Lt.toString())) {
            return Lt;
        } else if (name.equals(LessThanEqual.toString())) {
            return LessThanEqual;
        } else if (name.equals(Lte.toString())) {
            return Lte;
        } else if (name.equals(GreaterThan.toString())) {
            return GreaterThan;
        } else if (name.equals(Gt.toString())) {
            return Gt;
        } else if (name.equals(GreaterThanEqual.toString())) {
            return GreaterThanEqual;
        } else if (name.equals(Gte.toString())) {
            return Gte;
        } else if (name.equals(Like.toString())) {
            return Like;
        } else if (name.equals(NotLike.toString())) {
            return NotLike;
        } else if (name.equals(In.toString())) {
            return In;
        } else if (name.equals(NotIn.toString())) {
            return NotIn;
        } else if (name.equals(Not.toString())) {
            return Not;
        } else if (name.equals(IsNull.toString())) {
            return IsNull;
        } else if (name.equals(IsNotNull.toString())) {
            return IsNotNull;
        } else {
            return Equal;
        }
    }

    /**
     * 从字符串尾部判断返回Constraint
     *
     * @param filedPart
     * @return
     */
    public static Constraint getFromEndsWith(String filedPart) {
        if (filedPart.endsWith(Equal.toString())) {
            return Equal;
        } else if (filedPart.endsWith(Eq.toString())) {
            return Eq;
        } else if (filedPart.endsWith(NotEqual.toString())) {
            return NotEqual;
        } else if (filedPart.endsWith(Ne.toString())) {
            return Ne;
        } else if (filedPart.endsWith(Between.toString())) {
            return Between;
        } else if (filedPart.endsWith(LessThan.toString())) {
            return LessThan;
        } else if (filedPart.endsWith(Lt.toString())) {
            return Lt;
        } else if (filedPart.endsWith(LessThanEqual.toString())) {
            return LessThanEqual;
        } else if (filedPart.endsWith(Lte.toString())) {
            return Lte;
        } else if (filedPart.endsWith(GreaterThan.toString())) {
            return GreaterThan;
        } else if (filedPart.endsWith(Gt.toString())) {
            return Gt;
        } else if (filedPart.endsWith(GreaterThanEqual.toString())) {
            return GreaterThanEqual;
        } else if (filedPart.endsWith(Gte.toString())) {
            return Gte;
        } else if (filedPart.endsWith(Like.toString())) {
            return Like;
        } else if (filedPart.endsWith(NotLike.toString())) {
            return NotLike;
        } else if (filedPart.endsWith(In.toString())) {
            return In;
        } else if (filedPart.endsWith(NotIn.toString())) {
            return NotIn;
        } else if (filedPart.endsWith(Not.toString())) {
            return Not;
        } else if (filedPart.endsWith(IsNull.toString())) {
            return IsNull;
        } else if (filedPart.endsWith(IsNotNull.toString())) {
            return IsNotNull;
        } else {
            return Equal;
        }
    }
}