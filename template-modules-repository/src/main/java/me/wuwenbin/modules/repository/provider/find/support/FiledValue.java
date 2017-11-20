package me.wuwenbin.modules.repository.provider.find.support;

/**
 * created by Wuwenbin on 2017/11/12 at 12:48
 *
 * @author Wuwenbin
 */
public class FiledValue {

    private String field;
    private Object value;

    private FiledValue(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public static FiledValue build(String field, Object value) {
        return new FiledValue(field, value);
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
