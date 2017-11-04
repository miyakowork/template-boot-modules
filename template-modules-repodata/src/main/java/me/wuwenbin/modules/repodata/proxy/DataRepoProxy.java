package me.wuwenbin.modules.repodata.proxy;


import me.wuwenbin.modules.jdbc.factory.DaoFactory;
import me.wuwenbin.modules.repodata.provider.DataRepoProvider;
import me.wuwenbin.modules.repodata.util.ExceptionUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * created by Wuwenbin on 2017/10/29 at 21:40
 *
 * @author Wuwenbin
 */
public class DataRepoProxy<T> implements InvocationHandler, Serializable {


    private Class<T> dataInterface;
    private DataRepoProvider<T> provider;

    public DataRepoProxy(DaoFactory daoFactory, Class<T> dataInterface) {
        super();
        this.provider = new DataRepoProvider<>(daoFactory, dataInterface);
        this.dataInterface = dataInterface;
    }

    public Class<T> getDataInterface() {
        return dataInterface;
    }

    public DataRepoProvider<T> getProvider() {
        return provider;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw ExceptionUtils.unwrapThrowable(t);
            }
        }
        return this.provider.execute(method, args);
//        System.out.println("=========================");
//        System.out.println("代理类:" + proxy.getClass());
//        System.out.println("方法名:" + method.getName());
//        System.out.println("注解:" + Arrays.toString(method.getDeclaredAnnotations()));
//        System.out.println("返回类型:" + method.getReturnType());
//        System.out.println("返回类型是否为基本类型:" + method.getReturnType().isPrimitive());
//        //针对不同的方法进行不同的操作
//        if (method.getReturnType().isPrimitive()) {
//            return 0;
//        } else {
//            return null;
//        }
    }
}
