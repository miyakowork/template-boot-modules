package me.wuwenbin.modules.mongodb.aop;

import me.wuwenbin.modules.mongodb.annotation.DynamicMongoSource;
import me.wuwenbin.modules.mongodb.factory.MongoFactory;
import me.wuwenbin.modules.mongodb.support.MongoContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by wuwenbin on 2017/4/22.
 */
@Aspect
@Component
@Order(1)
public class MongoSourceAspect {

    private MongoFactory mongoFactory;

    @Autowired
    public MongoSourceAspect(MongoFactory mongoFactory) {
        this.mongoFactory = mongoFactory;
    }


    /**
     * 定义切入点
     */
    @Pointcut("@annotation(me.wuwenbin.modules.mongodb.annotation.DynamicMongoSource)")
    public void DynamicDataSourceAspect() {
    }

    /**
     * 切换
     *
     * @param joinPoint
     */
    @Before("DynamicDataSourceAspect()")
    public void switchMongoSource(JoinPoint joinPoint) throws NoSuchMethodException {
        Class clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Class[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        //noinspection unchecked
        Method method = clazz.getMethod(methodName, argClass);
        if (method.isAnnotationPresent(DynamicMongoSource.class)) {
            String key = method.getAnnotation(DynamicMongoSource.class).key();
            String db = method.getAnnotation(DynamicMongoSource.class).db();
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(db)) {
                MongoContextHolder.setHolder(key, db);
                mongoFactory.determineDynamicMongoDao();
            }

            if (StringUtils.isEmpty(key) && !StringUtils.isEmpty(db)) {
                MongoContextHolder.setHolder(null, db);
                mongoFactory.determineDynamicMongoDaoByDatabase();
            }

            if (StringUtils.isEmpty(db) && !StringUtils.isEmpty(key)) {
                MongoContextHolder.setHolder(key, null);
                mongoFactory.determineDynamicMongoDaoByKey();
            }

        }
    }

    /**
     * 方法执行完毕后回滚回原来的数据源
     *
     * @param joinPoint
     */
    @After("DynamicDataSourceAspect()")
    public void rollbackMongoSource2Default(JoinPoint joinPoint) throws NoSuchMethodException {
        Class clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Class[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        //noinspection unchecked
        Method method = clazz.getMethod(methodName, argClass);
        if (method.isAnnotationPresent(DynamicMongoSource.class)) {
            mongoFactory.dynamicMongoDao = mongoFactory.defaultMongoDao;
            MongoContextHolder.clearHolder();
        }
    }
}
