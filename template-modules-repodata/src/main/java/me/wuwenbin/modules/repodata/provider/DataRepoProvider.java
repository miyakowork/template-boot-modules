package me.wuwenbin.modules.repodata.provider;

import me.wuwenbin.modules.jdbc.ancestor.AncestorDao;
import me.wuwenbin.modules.jdbc.factory.DaoFactory;
import me.wuwenbin.modules.repodata.provider.crud.ICrudProvider;
import me.wuwenbin.modules.repodata.util.MethodUtils;

import java.lang.reflect.Method;

/**
 * 此类为解析@DataRepo接口的方法所对应的执行步骤
 * created by Wuwenbin on 2017/11/1 at 0:02
 *
 * @author Wuwenbin
 */
public class DataRepoProvider<T> {


    private AncestorDao jdbcTemplate;
    private Class<T> clazz;

    public DataRepoProvider(DaoFactory daoFactory, Class<T> clazz) {
        this.jdbcTemplate = daoFactory.dynamicDao;
        this.clazz = clazz;
    }

    public Object execute(Method method, Object[] args) throws Exception {
        ICrudProvider provider = MethodUtils.getProvider(method, this.jdbcTemplate, this.clazz);
        return provider.execute(args);
    }
}