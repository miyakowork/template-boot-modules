package me.wuwenbin.modules.pagination.query.model;

import me.wuwenbin.modules.pagination.query.TableQuery;
import me.wuwenbin.modules.pagination.sort.Sorting;

import java.util.List;

/**
 * 非BootstrapTable和LayuiTable时，可用的查询参数基础对象
 * 如果需要额外参数可继承此类
 * created by Wuwenbin on 2018/2/8 at 17:03
 */
public class BasicTableQuery extends TableQuery {

    private int pageNo = 1;
    private int pageSize = 10;

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public int getPageNo() {
        return this.pageNo;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public Sorting getSortingInfo() {
        return null;
    }

    @Override
    public List<Sorting> getSortingInformation() {
        return null;
    }
}
