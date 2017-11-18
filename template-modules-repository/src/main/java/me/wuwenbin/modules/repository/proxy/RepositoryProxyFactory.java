package me.wuwenbin.modules.repository.proxy;


import me.wuwenbin.modules.jpa.factory.DaoFactory;
import me.wuwenbin.modules.repository.provider.RepositoryProvider;
import me.wuwenbin.modules.repository.util.ExceptionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

/**
 * 使用JDK动态代理Repository接口的工厂类
 * created by Wuwenbin on 2017/10/29 at 21:40
 *
 * @author Wuwenbin
 */
public class RepositoryProxyFactory<T> implements InvocationHandler {


    private DaoFactory daoFactory;
    private RepositoryProvider provider;

    public RepositoryProxyFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public RepositoryProvider getProvider() {
        return provider;
    }

    @SuppressWarnings("unchecked")
    public T newInstance(Class<T> targetClass) {
        this.provider = new RepositoryProvider(daoFactory);
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
        //获取当前接口所对应操作的实体泛型类
        //noinspection unchecked
        Class<T> currentClass = (Class<T>) ((ParameterizedType) ((Class<T>) proxy.getClass().getGenericInterfaces()[0]).getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return this.provider.execute(method, args, currentClass);
    }


}
