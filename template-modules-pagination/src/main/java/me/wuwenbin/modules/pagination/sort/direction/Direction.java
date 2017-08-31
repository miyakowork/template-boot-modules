package me.wuwenbin.modules.pagination.sort.direction;

/**
 * 排序的方向
 * created by Wuwenbin on 2017/8/29 at 21:54
 */
public enum Direction {

    ASC("asc"), DESC("desc");

    private String direction;

    Direction(String direction) {
        this.direction = direction;
    }

    /**
     * 得到排序方向的文本
     *
     * @return
     */
    public String getDirectionString() {
        return this.direction;
    }

    /**
     * 根据输入的文本，来得到排序方向的枚举值，默认asc
     *
     * @param direction
     * @return
     */
    public static Direction getDirectionByString(String direction) {
        return "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
    }
}
