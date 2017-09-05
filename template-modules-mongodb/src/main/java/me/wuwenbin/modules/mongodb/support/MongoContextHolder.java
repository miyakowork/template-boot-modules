package me.wuwenbin.modules.mongodb.support;


import me.wuwenbin.modules.mongodb.support.bo.MongoContext;

/**
 * 处理同一线程下的切换数据源的问题
 * Created by wuwenbin on 2017/5/4.
 */
public class MongoContextHolder {

    private static final ThreadLocal<MongoContext> mongoContextHolder = new ThreadLocal<>();

    public static void setHolder(String key, String database) {
        mongoContextHolder.set(MongoContext.create(key, database));
    }

    public static MongoContext getHolder() {
        return mongoContextHolder.get();
    }

    public static void clearHolder() {
        mongoContextHolder.remove();
    }
}
