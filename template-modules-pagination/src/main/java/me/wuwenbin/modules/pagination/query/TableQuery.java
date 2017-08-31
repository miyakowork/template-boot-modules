package me.wuwenbin.modules.pagination.query;

import me.wuwenbin.modules.pagination.sort.Sorting;

import java.util.List;

/**
 * 默认实现类都是继承此类，默认不支持服务器排序以及多列排序的，如果需要支持，请自行重写相关方法
 * created by Wuwenbin on 2017/8/29 at 22:48
 */
public abstract class TableQuery implements Query {

    /**
     * 当前的页码
     *
     * @return
     */
    @Override
    public abstract int getPageNo();

    /**
     * 当前分页的大小
     *
     * @return
     */
    @Override
    public abstract int getPageSize();

    /**
     * 是否支持服务器排序
     *
     * @return
     */
    @Override
    public boolean isSupportServerSort() {
        return false;
    }

    /**
     * 有些前端的分页表格插件式不支持多列一起排序的，所以需要有个方法判断下
     *
     * @return
     */
    @Override
    public boolean isSupportMultiSort() {
        return false;
    }

    /**
     * 如果支持多列一起排序，那么现在是不是多列排序呢，所以又有一个方法来做判断
     *
     * @return
     */
    @Override
    public boolean isCurrentMultiSort() {
        return false;
    }

    /**
     * 如果{@link #isSupportMultiSort()}为真，则返回null
     * 如果{@link #isSupportMultiSort()}为假，则返回排序信息
     *
     * @return
     */
    @Override
    public abstract Sorting getSortingInfo();

    /**
     * 此方法的返回结果和上面的{@link #getSortingInfo()}情况相反
     *
     * @return
     */
    @Override
    public abstract List<Sorting> getSortingInformation();

}
