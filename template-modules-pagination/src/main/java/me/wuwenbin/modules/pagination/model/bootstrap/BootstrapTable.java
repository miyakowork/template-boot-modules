package me.wuwenbin.modules.pagination.model.bootstrap;

import me.wuwenbin.modules.pagination.model.Table;

import java.util.List;

/**
 * bootstrapTable的表格数据模型
 * created by Wuwenbin on 2017/8/30 at 11:41
 * @author wuwenbin
 */
public class BootstrapTable<T> implements Table<T> {

    /**
     * 数据总量，提供页脚的分页显示参数
     */
    private long total;
    /**
     * 当前页的数据集合
     */
    private List<T> rows;

    public BootstrapTable(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    /**
     * 表格的当前页数据
     *
     * @return
     */
    @Override
    public List<T> getCurrentPageData() {
        return rows;
    }
}
