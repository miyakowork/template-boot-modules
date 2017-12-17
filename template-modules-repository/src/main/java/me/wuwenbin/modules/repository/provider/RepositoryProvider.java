package me.wuwenbin.modules.repository.provider;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.jpa.factory.DaoFactory;
import me.wuwenbin.modules.repository.provider.crud.ICrudProvider;
import me.wuwenbin.modules.repository.util.BeanUtils;

import java.lang.reflect.Method;

/**
 * 此类为解析@Repository接口的方法所对应的执行步骤
 * created by Wuwenbin on 2017/11/1 at 0:02
 *
 * @author Wuwenbin
 */
public class RepositoryProvider {


    private AncestorDao jdbcTemplate;

    public RepositoryProvider(DaoFactory daoFactory) {
        this.jdbcTemplate = daoFactory.dynamicDao;
    }

    public <T> Object execute(Method method, Object[] args, Class<T> clazz) throws Exception {
        ICrudProvider provider = BeanUtils.getProvider(method, this.jdbcTemplate, clazz);
        return provider.execute(args);
    }
}
