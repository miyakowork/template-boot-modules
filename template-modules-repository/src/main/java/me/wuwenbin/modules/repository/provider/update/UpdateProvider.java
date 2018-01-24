package me.wuwenbin.modules.repository.provider.update;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.repository.annotation.field.Routers;
import me.wuwenbin.modules.repository.annotation.field.SQL;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.exception.MethodParamException;
import me.wuwenbin.modules.repository.exception.MethodTypeMismatchException;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repository.provider.update.annotation.Modify;
import me.wuwenbin.modules.repository.util.BeanUtils;
import me.wuwenbin.modules.sql.support.Symbol;
import me.wuwenbin.modules.sql.util.SQLDefineUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
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
        String updateBy = "updateBy";
        //无参数
        if (args == null) {
            throw new MethodParamException("方法「" + methodName + "」参数不规范，请参考命名规则！");
        }
        //参数种类为一个
        else if (args.length == 1) {
            if (super.getMethod().isAnnotationPresent(SQL.class)) {
                String sql = super.getMethod().getAnnotation(SQL.class).value();
                return executeWithSingleParam(sql, args, returnType);
            }
            //如果使用@Modify则方法命名关系到where条件语句
            else if (super.getMethod().isAnnotationPresent(Modify.class)) {
                if (methodName.startsWith(updateBy)) {
                    int[] updateRouters = super.getMethod().getAnnotation(Modify.class).value();
                    String preSql = super.sbb.updateRoutersByPk(Symbol.COLON, updateRouters);
                    preSql = preSql.substring(0, preSql.toLowerCase().indexOf("where"));
                    String finalSql = preSql.concat(" where ");
                    String sql = (getSql(methodName, 8, finalSql, true));
                    return executeWithSingleParam(sql, args, returnType);
                } else {
                    throw new MethodExecuteException("方法「" + methodName + "」命名不规范（此处应为updateByXxx），请参考命名规则！");
                }
            }
            //如果使用@Routers注解，则无关方法命名，更新的字段为routers，条件为主键 = ？
            else if (super.getMethod().isAnnotationPresent(Routers.class)) {
                int[] updateRouters = super.getMethod().getAnnotation(Routers.class).value();
                String sql = super.sbb.updateRoutersByPk(Symbol.COLON, updateRouters);
                return executeWithSingleParam(sql, args, returnType);
            } else if (methodName.substring(0, methodName.indexOf("By") + 2).matches("^update.*By$")) {
                String fields = methodName.substring(methodName.indexOf("update") + 6, methodName.indexOf("By"));
                String sql = "update ".concat(super.tableName).concat(" set");
                if (!StringUtils.isEmpty(fields)) {
                    String[] fieldArray = fields.split("And");
                    for (String s : fieldArray) {
                        s = s.substring(0, 1).toLowerCase().concat(s.substring(1, s.length()));
                        sql = sql.concat(" ").concat(s).concat(" = :").concat(s);
                    }
                }
                String finalSql = sql.concat(" where ");
                finalSql = getSql(methodName, methodName.indexOf("By") + 2, finalSql, true);
                return executeWithSingleParam(finalSql, args, returnType);
            } else {
                throw new MethodExecuteException("方法「" + methodName + "」不符合规范，请参考命名规则！");
            }
        }
        //参数为多个/多种，sql语句为问号形式
        else {
            //如果使用@Modify则方法命名关系到where条件语句
            if (super.getMethod().isAnnotationPresent(Modify.class)) {
                if (methodName.startsWith(updateBy)) {
                    int[] updateRouters = super.getMethod().getAnnotation(Modify.class).value();
                    String preSql = super.sbb.updateRoutersByPk(Symbol.QUESTION_MARK, updateRouters);
                    preSql = preSql.substring(0, preSql.toLowerCase().indexOf("where"));
                    String finalSql = preSql.concat(" where ");
                    String sql = getSql(methodName, 8, finalSql, false);
                    return executeWithMultiParam(sql, args, returnType);
                } else {
                    throw new MethodExecuteException("方法「" + methodName + "」命名不规范，请参考命名规则！");
                }
            } else if (methodName.substring(0, methodName.indexOf("By") + 2).matches("^update.*By$")) {
                String fields = methodName.substring(methodName.indexOf("update") + 6, methodName.indexOf("By"));
                String sql = "update ".concat(super.tableName).concat(" set");
                if (!StringUtils.isEmpty(fields)) {
                    String[] fieldArray = fields.split("And");
                    for (String s : fieldArray) {
                        s = SQLDefineUtils.java2SQL("", s);
                        sql = sql.concat(" ").concat(s).concat(" = ?");
                    }
                }
                String finalSql = sql.concat(" where ");
                finalSql = getSql(methodName, methodName.indexOf("By") + 2, finalSql, false);
                return executeWithMultiParam(finalSql, args, returnType);
            } else {
                throw new MethodExecuteException("方法「" + methodName + "」为不支持的类型，请参考命名规则！");
            }
        }

    }

    /**
     * 执行单参数的方法
     *
     * @param sql
     * @param args
     * @param returnTye
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Object executeWithSingleParam(String sql, Object[] args, Class returnTye) throws Exception {
        switch (returnTye.getSimpleName()) {
            case "int":
                if (BeanUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
                    return getJdbcTemplate().executeBean(sql, args[0]);
                } else if (BeanUtils.paramTypeMapOrSub(args[0])) {
                    return getJdbcTemplate().executeMap(sql, (Map<String, Object>) args[0]);
                } else if (args[0].getClass().isPrimitive() || BeanUtils.isPrimitive(args[0])) {
                    return getJdbcTemplate().executeArray(sql, args[0]);
                } else {
                    throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型不符合规范，请查看命名规则！");
                }
            case "int[]":
                if (BeanUtils.paramTypeArray(args[0])) {
                    Object obj = args[0];
                    if (obj instanceof Map) {
                        return getJdbcTemplate().executeBatchByArrayMaps(sql, (Map<String, Object>) args[0]);
                    } else if (BeanUtils.paramTypeJavaBeanOrSub(obj, super.getClazz())) {
                        return getJdbcTemplate().executeBatchByArrayBeans(sql, args);
                    } else {
                        throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型不符合规范，请查看命名规则！");
                    }
                } else if (BeanUtils.paramTypeCollectionOrSub(args[0])) {
                    Collection paramCollection = (Collection) args[0];
                    Object temp = paramCollection.iterator().next();
                    if (temp instanceof Map) {
                        return getJdbcTemplate().executeBatchByCollectionMaps(sql, paramCollection);
                    } else if (temp.getClass().equals(super.getClazz())) {
                        return getJdbcTemplate().executeBatchByCollectionBeans(sql, paramCollection);
                    } else {
                        throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型不符合规范，请查看命名规则！");
                    }
                } else {
                    throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型不符合规范，请查看命名规则！");
                }
            default:
                throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型不符合规范，请查看命名规则！");
        }
    }

    /**
     * 执行多参数的方法
     *
     * @param sql
     * @param args
     * @param returnType
     * @throws Exception
     * @returnreturnType
     */
    private int executeWithMultiParam(String sql, Object[] args, Class returnType) throws Exception {
        if (returnType.getSimpleName().equals("int")) {
            return getJdbcTemplate().executeArray(sql, args);
        } else {
            throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型不符合规范，请查看命名规则！");
        }
    }

    /**
     * 获取「By...」的sql语句
     *
     * @param methodName
     * @param colon      是否为冒号形式
     * @return
     */
    private String getSql(String methodName, int subLength, String preSql, boolean colon) throws MethodExecuteException {
        String joinStr = "And|Or";
        String fieldStr = methodName.substring(subLength);
        StringBuilder sqlBuilder = new StringBuilder(preSql);
        String[] fields = fieldStr.split(joinStr);
        BeanUtils.getWherePart(methodName, fieldStr, fields, sqlBuilder, colon);
        return sqlBuilder.toString();
    }
}
