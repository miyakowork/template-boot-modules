package me.wuwenbin.modules.repodata.provider.find;

import me.wuwenbin.modules.jdbc.ancestor.AncestorDao;
import me.wuwenbin.modules.repodata.provider.crud.AbstractProvider;

import java.lang.reflect.Method;

/**
 * created by Wuwenbin on 2017/11/1 at 19:39
 *
 * @author Wuwenbin
 */
public class FindProvider<T> extends AbstractProvider<T> {

    public FindProvider(Method method, AncestorDao jdbcTemplate, Class<T> clazz) {
        super(method, jdbcTemplate, clazz);
    }

    @Override
    public Object execute(Object[] args) throws Exception {
        return null;
    }

}
