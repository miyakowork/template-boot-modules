package me.wuwenbin.modules.repository.proxy;


import me.wuwenbin.modules.jpa.factory.DaoFactory;
import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.provider.crud.ICrudProvider;
import me.wuwenbin.modules.repository.util.BeanUtils;
import me.wuwenbin.modules.repository.util.ExceptionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
@Component
public class RepositoryProxyFactory<T> implements InvocationHandler, InitializingBean, ApplicationContextAware {

    private DaoFactory daoFactory;
    private ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public T newInstance(Class<T> targetClass) {
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
        //noinspection unchecked
        String dataSourceKey = ((Class<T>) proxy.getClass().getGenericInterfaces()[0]).getAnnotation(Repository.class).key();
        ICrudProvider provider = BeanUtils.getProvider(method, this.daoFactory, currentClass, dataSourceKey);
        return provider.execute(args);
    }


    @Override
    public void afterPropertiesSet() {
        this.daoFactory = applicationContext.getBean(DaoFactory.class);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
