package me.wuwenbin.modules.pagination.query.model.layui;

import me.wuwenbin.modules.pagination.query.TableQuery;
import me.wuwenbin.modules.pagination.sort.Sorting;
import me.wuwenbin.modules.pagination.sort.direction.Direction;
import me.wuwenbin.modules.pagination.util.StringUtils;

import java.util.List;


/**
 * created by Wuwenbin on 2017/8/30 at 12:09
 */
public class LayTableQuery extends TableQuery {

    private int page;//当前页码
    private int limit;//页面大小
    private String order;//排序顺序 desc asc
    private String sort;//排序字段

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * 当前的页码
     *
     * @return
     */
    @Override
    public int getPageNo() {
        int pageNo = getPage();
        return pageNo > 0 ? pageNo : 1;
    }

    /**
     * 当前分页的大小
     *
     * @return
     */
    @Override
    public int getPageSize() {
        int size = getLimit();
        return size > 0 ? size : 10;
    }

    /**
     * 是否支持服务器排序
     *
     * @return
     */
    @Override
    public boolean isSupportServerSort() {
        return true;
    }



    /**
     * 如果{@link #isSupportMultiSort()}为真，则返回null
     * 如果{@link #isSupportMultiSort()}为假，则返回排序信息
     *
     * @return
     */
    @Override
    public Sorting getSortingInfo() {
        if (StringUtils.isNotEmpty(getSort()) && StringUtils.isNotEmpty(getOrder())) {
            Sorting sort = new Sorting();
            sort.setSortName(getSort());
            sort.setSortDirection(Direction.getDirectionByString(getOrder()));
            return sort;
        }
        return null;
    }

    /**
     * 此方法的返回结果和上面的{@link #getSortingInfo()}情况相反
     *
     * @return
     */
    @Override
    public List<Sorting> getSortingInformation() {
        return null;
    }
}
