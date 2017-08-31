package me.wuwenbin.modules.pagination.model.layui;

import me.wuwenbin.modules.pagination.model.Table;

import java.util.List;

/**
 * layui2.x版本推出的dataTable插件
 * created by Wuwenbin on 2017/8/30 at 11:41
 */
public class LayTable<T> implements Table<T> {

    private int code; //状态码，0代表成功，其它失败
    private String message;//状态信息，一般可为空
    private long count;//数据总量
    private List<T> data; //数据，字段是任意的。如：[{"id":1,"username":"贤心"}, {"id":2,"username":"佟丽娅"}]

    public LayTable(int code, String message, long count, List<T> data) {
        this.code = code;
        this.message = message;
        this.count = count;
        this.data = data;
    }

    public LayTable(long count, List<T> data) {
        this.code = 0;
        this.message = "获取数据成功！";
        this.count = count;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 表格的当前页数据
     *
     * @return
     */
    @Override
    public List<T> getCurrentPageData() {
        return data;
    }
}
