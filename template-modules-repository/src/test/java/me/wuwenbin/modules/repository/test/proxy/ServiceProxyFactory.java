package me.wuwenbin.modules.repository.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/10/28 at 11:21
 */
public class ServiceProxyFactory<T> implements InvocationHandler {

    private Class<T> javaInterface;

    public ServiceProxyFactory(Class<T> javaInterface) {
        super();
        this.javaInterface = javaInterface;
    }

    public T newInstance() {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{javaInterface}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ParameterizedType pt = (ParameterizedType) method.getGenericReturnType();
        Class clazz = (Class) pt.getActualTypeArguments()[0];
        System.out.println(Arrays.toString(clazz.getDeclaredFields()));
        System.out.println("=========================");
        System.out.println("代理类:" + proxy.getClass());
        System.out.println("方法名:" + method.getName());
        System.out.println("注解:" + Arrays.toString(method.getDeclaredAnnotations()));
        System.out.println("返回类型:" + method.getReturnType().getSimpleName());
        System.out.println(method.getReturnType().equals(List.class));
        System.out.println("返回类型是否为基本类型:" + method.getReturnType().isPrimitive());
        System.out.println("参数:" + args);
        System.out.println("参数类型:" + (args != null ? args.getClass() : "无参数"));
        if (args != null) {
            System.out.println("参数个数L:" + args.length);
            for (Object o : args) {
                System.out.println("是否为数组:" + o.getClass().isArray());
                System.out.println("!~~:" + (o instanceof Map));
            }
        }
        //针对不同的方法进行不同的操作
        if (method.getReturnType().isPrimitive())
            return 0;
        else return null;
    }
}
