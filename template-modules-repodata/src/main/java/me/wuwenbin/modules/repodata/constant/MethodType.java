package me.wuwenbin.modules.repodata.constant;

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
     * update方法
     */
    UPDATE;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
