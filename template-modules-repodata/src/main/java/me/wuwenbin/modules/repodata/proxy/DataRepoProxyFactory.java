package me.wuwenbin.modules.repodata.proxy;

import me.wuwenbin.modules.jdbc.factory.DaoFactory;

import java.lang.reflect.Proxy;

/**
 * created by Wuwenbin on 2017/10/31 at 14:19
 *
 * @author Wuwenbin
 */
public class DataRepoProxyFactory<T> {

    private final Class<T> dataInterface;
    private DaoFactory daoFactory;

    public DataRepoProxyFactory(DaoFactory daoFactory, Class<T> dataInterface) {
        this.daoFactory = daoFactory;
        this.dataInterface = dataInterface;
    }

    public Class<T> getDataInterface() {
        return dataInterface;
    }


    private T newInstance(DataRepoProxy<T> dataRepoProxy) {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(dataInterface.getClassLoader(), new Class[]{dataInterface}, dataRepoProxy);
    }

    public T newInstance() {
        final DataRepoProxy<T> dataRepoProxy = new DataRepoProxy<>(this.daoFactory, this.dataInterface);
        return newInstance(dataRepoProxy);
    }

}
