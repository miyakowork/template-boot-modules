package me.wuwenbin.modules.repository.provider.update;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.repository.annotation.field.UpdateSQL;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.exception.MethodParamException;
import me.wuwenbin.modules.repository.exception.MethodTypeMissMatch;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repository.util.MethodUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/11/1 at 19:39
 *
 * @author Wuwenbin
 */
public class UpdateProvider<T> extends AbstractProvider<T> {

    public UpdateProvider(Method method, AncestorDao jdbcTemplate, Class<T> clazz) {
        super(method, jdbcTemplate, clazz);
    }

    @Override
    public Object execute(Object[] args) throws Exception {
        String methodName = super.getMethod().getName();
        Class returnType = super.getMethod().getReturnType();
        String updateRouterByPk = "updateRouter";
        if (methodName.startsWith(updateRouterByPk)) {
            int[] routers = MethodUtils.getRouters(methodName, updateRouterByPk.length());
            String sql = super.sbb.updateRoutersByPk(routers);
            return executeUpdateByParamAndReturnType(args, methodName, returnType, sql);
        } else {
            String updateRouterByRouters = "updateRouter";
            String byRouters = "ByRouter";
            if (methodName.startsWith(updateRouterByRouters) && methodName.contains(byRouters)) {
                String[] tempArray = methodName.split(byRouters);
                //noinspection AlibabaUndefineMagicConstant
                if (tempArray.length != 2) {
                    throw new MethodExecuteException("方法「" + methodName + "」命名有误，请参考命名规则！");
                } else {
                    int[] updateRouters = MethodUtils.getRouters(tempArray[0], updateRouterByRouters.length());
                    int[] conditionRouters = MethodUtils.getRouters(tempArray[1], 0);
                    String sql = super.sbb.updateRoutersByRouterArray(updateRouters, conditionRouters);
                    return executeUpdateByParamAndReturnType(args, methodName, returnType, sql);
                }
            } else {
                String updateBySql = "updateBySql";
                if (methodName.startsWith(updateBySql)) {
                    if (super.getMethod().isAnnotationPresent(UpdateSQL.class)) {
                        String sql = super.getMethod().getAnnotation(UpdateSQL.class).value();
                        return executeUpdateByParamAndReturnType(args, methodName, returnType, sql);
                    } else {
                        throw new MethodExecuteException("方法「" + methodName + "」之上必须有@UpdateSQL注解指定相关SQL！");
                    }
                } else {
                    throw new MethodExecuteException("方法「" + methodName + "」命名有误，请参考命名规则！");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Object executeUpdateByParamAndReturnType(Object[] args, String methodName, Class returnType, String sql) throws Exception {
        if (MethodUtils.paramTypeMapOrSub(args[0])) {
            Map<String, Object> param = (Map<String, Object>) args[0];
            int n = getJdbcTemplate().executeMap(sql, param);
            String bool = "boolean";
            String voidName = "void";
            if (returnType.equals(super.getClazz())) {
                if (n > 0) {
                    String findSql = super.sbb.selectAllByPk();
                    return getJdbcTemplate().findBeanByMap(findSql, super.getClazz(), param);
                } else {
                    return null;
                }
            } else if (returnType.getSimpleName().equals(bool)) {
                return n > 0;
            } else if (returnType.getSimpleName().equals(voidName)) {
                return null;
            } else {
                throw new MethodTypeMissMatch("方法「" + methodName + "」返回类型有误，请参考命名规则！");
            }
        } else if (MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
            int n = getJdbcTemplate().executeBean(sql, args[0]);
            if (n > 0) {
                String findSql = super.sbb.selectAllByPk();
                return getJdbcTemplate().findBeanByBean(findSql, super.getClazz(), args[0]);
            } else {
                return null;
            }
        } else {
            throw new MethodParamException("方法「" + methodName + "」参数类型有误，请参考命名规则！");
        }
    }


}
