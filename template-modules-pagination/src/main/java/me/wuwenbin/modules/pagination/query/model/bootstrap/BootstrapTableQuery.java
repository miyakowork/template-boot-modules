package me.wuwenbin.modules.pagination.query.model.bootstrap;

import me.wuwenbin.modules.pagination.query.TableQuery;
import me.wuwenbin.modules.pagination.sort.Sorting;
import me.wuwenbin.modules.pagination.sort.direction.Direction;
import me.wuwenbin.modules.pagination.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Wuwenbin on 2017/8/30 at 11:51
 */
public abstract class BootstrapTableQuery extends TableQuery {

    private int limit;//当前的页面大小，即每页显示多少条数据
    private int offset;//当前页的最后一条数据的序号
    private String order;//排序顺序 desc asc
    private String sort;//排序字段
    private List<Sorting> multiSorts;//多列排序

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
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

    public List<Sorting> getMultiSorts() {
        return multiSorts;
    }

    public void setMultiSort(String multiSorts) {
        if (StringUtils.isEmpty(multiSorts)) {
            this.multiSorts = null;
        } else {
            String[] sorts = multiSorts.split(";");
            List<Sorting> multiSortList = new ArrayList<>(sorts.length);
            for (String sort : sorts) {
                String[] singleSort = sort.split(",");
                Sorting ms = new Sorting();
                ms.setSortName(singleSort[0]);
                ms.setSortDirection(Direction.getDirectionByString(singleSort[1]));
                multiSortList.add(ms);
            }
            this.multiSorts = multiSortList;
        }
    }

    /**
     * 当前的页码
     *
     * @return
     */
    @Override
    public int getPageNo() {
        int pageNo = getOffset() / getLimit() + 1;
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
     * 有些前端的分页表格插件式不支持多列一起排序的，所以需要有个方法判断下
     *
     * @return
     */
    @Override
    public boolean isSupportMultiSort() {
        return isSupportServerSort();
    }

    /**
     * 如果支持多列一起排序，那么现在是不是多列排序呢，所以又有一个方法来做判断
     *
     * @return
     */
    @Override
    public boolean isCurrentMultiSort() {
        return isSupportMultiSort() && StringUtils.isNotEmpty(multiSorts);
    }

    /**
     * 如果{@link #isSupportMultiSort()#isCurrentMultiSort()}二者为真，则返回null
     * 如果{@link #isSupportMultiSort()#isCurrentMultiSort()}二者其中之一为假，则返回排序信息
     *
     * @return
     */
    @Override
    public Sorting getSortingInfo() {
        if (!isCurrentMultiSort()) {
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
        if (isCurrentMultiSort()) {
            return getMultiSorts();
        }
        return null;
    }


}
