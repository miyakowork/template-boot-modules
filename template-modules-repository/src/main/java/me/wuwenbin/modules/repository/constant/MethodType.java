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
    SAVE,

    /**
     * 删除方法
     */
    DELETE,

    /**
     * 查找方法
     */
    FIND,

    /**
     * 计数
     */
    COUNT,

    /**
     * update方法
     */
    UPDATE;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
