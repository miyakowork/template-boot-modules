package me.wuwenbin.modules.pagination.sort;

import me.wuwenbin.modules.pagination.sort.direction.Direction;

/**
 * 排序的对象
 * created by Wuwenbin on 2017/8/29 at 21:53
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
}
