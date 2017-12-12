package me.wuwenbin.modules.sql;


import me.wuwenbin.modules.sql.factory.SQLBeanBuilder;
import me.wuwenbin.modules.sql.factory.SQLTextBuilder;

/**
 * 使用入口方法
 * Created by wuwenbin on 2017/1/12.
 *
 * @author wuwenbin
 * @since 1.1.0
 */
public final class SQLGen {

    private volatile static SQLBeanBuilder sqlBeanBuilder;
    private volatile static SQLTextBuilder sqlTextBuilder;

    private SQLGen() {
    }

    /**
     * 生成 {@link SQLBeanBuilder} 实例
     *
     * @param #targetBeanClass 对应实体类
     * @return {@link SQLBeanBuilder}
     */
    public synchronized static SQLBeanBuilder builder(Class<?> targetBeanClass) {
        if (sqlBeanBuilder == null) {
            synchronized (SQLBeanBuilder.class) {
                if (sqlBeanBuilder == null) {
                    sqlBeanBuilder = new SQLBeanBuilder(targetBeanClass);
                }
            }
        } else if (targetBeanClass != sqlBeanBuilder.getBeanClass()) {
            sqlBeanBuilder = new SQLBeanBuilder(targetBeanClass);
        }
        return sqlBeanBuilder;
    }

    /**
     * 生成 {@link SQLTextBuilder} instance
     *
     * @return {@link SQLTextBuilder}
     */
    public static SQLTextBuilder builder() {
        if (sqlTextBuilder == null) {
            synchronized (SQLTextBuilder.class) {
                if (sqlTextBuilder == null) {
                    sqlTextBuilder = new SQLTextBuilder();
                }
            }
        }
        return sqlTextBuilder;
    }

}
