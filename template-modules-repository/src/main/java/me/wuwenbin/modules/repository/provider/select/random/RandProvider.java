package me.wuwenbin.modules.repository.provider.select.random;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.utils.lang.LangUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Wuwenbin on 2018/2/21 at 14:15
 */
public class RandProvider<T> extends AbstractProvider<T> {

    public RandProvider(Method method, AncestorDao jdbcTemplate, Class<T> clazz) {
        super(method, jdbcTemplate, clazz);
    }

    @Override
    public Object execute(Object[] args) throws Exception {
        String randBase = "randBase", randBases = "randBases", randEffect = "randEffect", randEffects = "randEffects";
        String methodName = super.getMethod().getName();
        String limitPart = " LIMIT {}";
        String baseSQL = "SELECT * FROM `{}` ORDER BY RAND()";
        String effectSQL = "SELECT * FROM `{}` AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX({}) FROM `{}`)-(SELECT MIN({}) FROM `{}`))+(SELECT MIN({}) FROM `{}`)) AS id) AS t2 WHERE t1.{} >= t2.id ORDER BY t1.{} LIMIT 1";
        if (methodName.equalsIgnoreCase(randBase)) {
            String sql = baseSQL + limitPart;
            sql = LangUtils.string.placeholder(sql, 1);
            return super.getJdbcTemplate().findBeanByArray(sql, super.getClazz());
        } else if (methodName.equalsIgnoreCase(randBases)) {
            String sql = baseSQL + limitPart;
            sql = LangUtils.string.placeholder(sql, args[0]);
            return super.getJdbcTemplate().findListBeanByArray(sql, super.getClazz());
        } else if (methodName.equalsIgnoreCase(randEffect)) {
            String t = super.tableName;
            String p = super.pkDbName;
            String sql = LangUtils.string.placeholder(effectSQL, t, p, t, p, t, p, t, p, p);
            return super.getJdbcTemplate().findBeanByArray(sql, super.getClazz());
        } else if (methodName.equalsIgnoreCase(randEffects)) {
            String t = super.tableName;
            String p = super.pkDbName;
            String sql = LangUtils.string.placeholder(effectSQL, t, p, t, p, t, p, t, p, p);
            int n = (int) args[0];
            List<T> r = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                r.add(super.getJdbcTemplate().findBeanByArray(sql, super.getClazz()));
            }
            return r;
        } else {
            throw new MethodExecuteException("方法「" + methodName + "」为不支持的类型，请不要使用「rand」开头命名方法");
        }
    }
}
