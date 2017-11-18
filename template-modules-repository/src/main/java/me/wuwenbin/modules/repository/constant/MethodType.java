package me.wuwenbin.modules.repository.constant;

/**
 * created by Wuwenbin on 2017/11/1 at 15:05
 *
 * @author Wuwenbin
 */
public enum MethodType {

    /**
     * insert方法
     */
    SAVE("save"),

    /**
     * 删除方法
     */
    DELETE("delete"),

    /**
     * 查找方法
     */
    FIND("find"),

    /**
     * 计数
     */
    COUNT("count"),

    /**
     * 分页方法
     */
    PAGE("findPagination"),

    /**
     * update方法
     */
    UPDATE("update");

    private String name;

    MethodType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
