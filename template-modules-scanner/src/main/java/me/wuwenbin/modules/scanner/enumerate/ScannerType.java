package me.wuwenbin.modules.scanner.enumerate;

/**
 * 扫描资源的几种方式
 * created by Wuwenbin on 2017/9/19 at 18:11
 * @author wuwenbin
 */
public enum ScannerType {
    /**
     * 仅仅删除数据库中已存在的资源。
     */
    DROP,
    /**
     * 先删除然后在扫描所有资源存入数据库
     */
    CREATE,
    /**
     * 对数据库中已存在的资源不做操作，把数据库中未包含的扫描到的资源插入（即作比对后更新数据库）
     */
    UPDATE,
    /**
     * 字面意思，啥都不做
     */
    NONE
}
