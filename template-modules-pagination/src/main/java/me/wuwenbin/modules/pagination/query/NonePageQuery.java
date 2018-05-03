package me.wuwenbin.modules.pagination.query;

import me.wuwenbin.modules.pagination.sort.Sorting;

import java.util.List;

/**
 * created by Wuwenbin on 2018/5/2 at 下午3:14
 */
public class NonePageQuery implements Query {

    @Override
    public int getPageNo() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return 0;
    }

    @Override
    public boolean isSupportServerSort() {
        return false;
    }

    @Override
    public boolean isSupportMultiSort() {
        return false;
    }

    @Override
    public boolean isCurrentMultiSort() {
        return false;
    }

    @Override
    public Sorting getSortingInfo() {
        return null;
    }

    @Override
    public List<Sorting> getSortingInformation() {
        return null;
    }

    @Override
    public boolean isGroupBy() {
        return false;
    }

    @Override
    public boolean isHaving() {
        return false;
    }

    @Override
    public String groupByExpression() {
        return null;
    }

    @Override
    public String havingSearchCondition() {
        return null;
    }
}
