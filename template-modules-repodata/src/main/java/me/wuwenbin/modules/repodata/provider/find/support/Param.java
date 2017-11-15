package me.wuwenbin.modules.repodata.provider.find.support;

/**
 * created by Wuwenbin on 2017/11/12 at 12:48
 *
 * @author Wuwenbin
 */
public class Param {

    private String field;
    private Object value;

    public static Param builder(String field, Object value) {
        Param param = new Param();
        param.setField(field);
        param.setValue(value);
        return param;
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
}
