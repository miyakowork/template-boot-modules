package me.wuwenbin.modules.pagination.sort;

import me.wuwenbin.modules.pagination.sort.direction.Direction;

/**
 * 排序的对象
 * created by Wuwenbin on 2017/8/29 at 21:53
 *
 * @author Wuwenbin
 */
public class Sorting {

    private String sortName;
    private Direction sortDirection;

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public static Sorting asc(String sortName) {
        Sorting sorting = new Sorting();
        sorting.setSortName(sortName);
        sorting.setSortDirection(Direction.ASC);
        return sorting;
    }

    public static Sorting desc(String sortName) {
        Sorting sorting = new Sorting();
        sorting.setSortName(sortName);
        sorting.setSortDirection(Direction.DESC);
        return sorting;
    }

}
