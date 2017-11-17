package me.wuwenbin.modules.repository.proxy;


import me.wuwenbin.modules.jpa.factory.DaoFactory;
import me.wuwenbin.modules.repository.provider.DataRepoProvider;
import me.wuwenbin.modules.repository.util.ExceptionUtils;

import java.lang.reflect.*;

/**
 * created by Wuwenbin on 2017/10/29 at 21:40
 *
 * @author Wuwenbin
 */
public class DataRepoProxyFactory implements InvocationHandler {


    private DaoFactory daoFactory;
    private DataRepoProvider provider;

    public DataRepoProxyFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public DataRepoProvider getProvider() {
        return provider;
    }

    public <T> T newInstance(Class<T> targetClass) {
        Type paramClass = ((ParameterizedType) targetClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        this.provider = new DataRepoProvider<>(daoFactory, (Class<?>) paramClass);
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[]{targetClass}, this);
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
