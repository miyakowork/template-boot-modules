package me.wuwenbin.modules.repository.provider.save;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.pagination.util.StringUtils;
import me.wuwenbin.modules.repository.annotation.field.Routers;
import me.wuwenbin.modules.repository.annotation.field.SQL;
import me.wuwenbin.modules.repository.constant.Parametric;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.exception.MethodParamException;
import me.wuwenbin.modules.repository.exception.MethodTypeMismatchException;
import me.wuwenbin.modules.repository.function.BiFunction;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repository.provider.save.annotation.SaveSQL;
import me.wuwenbin.modules.repository.util.BeanUtils;
import me.wuwenbin.modules.sql.support.Symbol;
import me.wuwenbin.modules.sql.util.SQLDefineUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * insert/save方法的执行提供者
 * created by Wuwenbin on 2017/11/1 at 11:32
 *
 * @author Wuwenbin
 */
@SuppressWarnings("unchecked")
public class SaveProvider<T> extends AbstractProvider<T> {

    public SaveProvider(Method method, AncestorDao jdbcTemplate, Class<T> clazz) {
        super(method, jdbcTemplate, clazz);
    }

    @Override
    public Object execute(Object[] args) throws Exception {
        return executeByParamAndReturnType(getSql(), args);
    }

    private String getSql() throws Exception {
        String save = "save", savePk = "savePk";
        if (super.getMethod().isAnnotationPresent(SQL.class)) {
            return super.getMethod().getAnnotation(SQL.class).value();
        } else if (super.getMethod().isAnnotationPresent(Routers.class)) {
            int[] routers = super.getMethod().getAnnotation(Routers.class).value();
            return super.isPkInsert ? super.sbb.insertRoutersWithPk(Symbol.COLON, routers) : super.sbb.insertRoutersWithoutPk(Symbol.COLON, routers);
        } else if (super.getMethod().isAnnotationPresent(SaveSQL.class)) {
            StringBuilder inserts = new StringBuilder();
            StringBuilder values = new StringBuilder();
            String[] insertFields = super.getMethod().getAnnotation(SaveSQL.class).columns();
            for (String insertField : insertFields) {
                inserts.append(insertField).append(", ");
                if (super.getMethod().getAnnotation(SaveSQL.class).type().equals(Parametric.Colon)) {
                    values.append(":").append(SQLDefineUtils.underline2Camel(insertField)).append(", ");
                }
                if (super.getMethod().getAnnotation(SaveSQL.class).type().equals(Parametric.Doubt)) {
                    values.append("?, ");
                }
            }
            String finalInserts = inserts.substring(0, inserts.length() - 2);
            String finalValues = values.substring(0, values.length() - 2);
            return "insert into ".concat(super.tableName).concat("(").concat(finalInserts).concat(")").concat(" values (").concat(finalValues).concat(")");
        } else if (super.getMethod().getName().equalsIgnoreCase(save) || super.getMethod().getName().equalsIgnoreCase(savePk)) {
            return super.isPkInsert ? super.sbb.insertAllWithPk(Symbol.COLON) : super.sbb.insertAllWithoutPk(Symbol.COLON);
        } else {
            throw new MethodExecuteException("方法「" + super.getMethod().getName() + "」暂不支持！");
        }
    }


    private Object executeByParamAndReturnType(String sql, Object[] args) throws Exception {
        Class returnType = super.getMethod().getReturnType();
        if (args == null) {
            throw new MethodParamException("方法「" + super.getMethod().getName() + "」无参数！");
        } else if (args.length == 1) {
            String intName = "int", longName = "long";
            Object arg = args[0];
            if (BeanUtils.paramTypeJavaBeanOrSub(arg, super.getClazz())) {
                if (returnType.getSimpleName().equals(intName)) {
                    return getJdbcTemplate().executeBean(sql, arg);
                } else if (returnType.getSimpleName().equals(longName)) {
                    return getJdbcTemplate().insertBeanAutoGenKeyReturnKey(sql, arg);
                } else if (BeanUtils.paramTypeMapOrSub(returnType)) {
                    return getJdbcTemplate().insertBeanAutoGenKeyReturnMap(sql, arg, super.pkDbName);
                } else if (BeanUtils.paramTypeJavaBeanOrSub(arg, super.getClazz())) {
                    return getJdbcTemplate().insertBeanAutoGenKeyReturnBean(sql, arg, super.getClazz(), super.pkDbName);
                } else {
                    throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型类型暂不支持！");
                }
            } else if (BeanUtils.paramTypeMapOrSub(arg)) {
                Map<String, Object> mapArg = (Map<String, Object>) arg;
                if (returnType.getSimpleName().equals(intName)) {
                    return getJdbcTemplate().executeMap(sql, mapArg);
                } else if (returnType.getSimpleName().equals(longName)) {
                    return getJdbcTemplate().insertMapAutoGenKeyReturnKey(sql, mapArg);
                } else if (BeanUtils.paramTypeMapOrSub(returnType)) {
                    return getJdbcTemplate().insertMapAutoGenKeyReturnMap(sql, mapArg, super.pkDbName);
                } else if (BeanUtils.paramTypeJavaBeanOrSub(arg, super.getClazz())) {
                    return getJdbcTemplate().insertMapAutoGenKeyReturnBean(sql, mapArg, super.getClazz(), super.pkDbName);
                } else {
                    throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型类型暂不支持！");
                }
            } else if (BeanUtils.isPrimitive(arg)) {
                if (returnType.getSimpleName().equals(intName)) {
                    return getJdbcTemplate().executeArray(sql, arg);
                } else {
                    throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型类型暂不支持！");
                }
            } else if (BeanUtils.paramTypeCollectionOrSub(arg)) {
                Collection<T> paramCollection = (Collection<T>) args[0];
                if (StringUtils.isEmpty(super.pkDbName)) {
                    return getJdbcTemplate().executeBatchByCollectionBeans(sql, paramCollection);
                } else {
                    List<T> result = new ArrayList<>(paramCollection.size());
                    for (T p : paramCollection) {
                        result.add(getJdbcTemplate().insertBeanAutoGenKeyReturnBean(sql, p, super.getClazz(), super.pkDbName));
                    }
                    return result;
                }
            } else if (BeanUtils.paramTypeArray(arg)) {
                Object[] objects = (Object[]) arg;
                return executeByArray(sql, objects);
            } else {
                throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型暂不支持！");
            }
        } else {
            String intName = "int", longName = "long";
            if (isMultiParamPrimitive(args)) {
                if (returnType.getSimpleName().equals(intName)) {
                    return getJdbcTemplate().executeArray(sql, args);
                } else if (returnType.getSimpleName().equals(longName)) {
                    return executePrimitive(sql, args, (s, m) -> getJdbcTemplate().insertMapAutoGenKeyReturnKey(s, m));
                } else if (BeanUtils.paramTypeJavaBeanOrSub(returnType, super.getClazz())) {
                    return executePrimitive(sql, args, (s, m) -> getJdbcTemplate().insertMapAutoGenKeyReturnBean(s, m, super.getClazz(), super.pkDbName));
                } else if ((BeanUtils.paramTypeMapOrSub(returnType))) {
                    return executePrimitive(sql, args, (s, m) -> getJdbcTemplate().insertMapAutoGenKeyReturnMap(s, m, super.pkDbName));
                } else {
                    throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型类型暂不支持！");
                }
            } else {
                throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型暂不支持！");
            }
        }
    }


    /**
     * 执行参数为数组的save方法
     *
     * @param sql
     * @param objects
     * @return
     * @throws Exception
     */
    private Object executeByArray(String sql, Object[] objects) throws Exception {
        List<T> result = new ArrayList<>();
        if (objects[0] instanceof Map) {
            //noinspection unchecked
            Map<String, Object>[] paramMap = (Map<String, Object>[]) objects;
            for (Map<String, Object> p : paramMap) {
                result.add(getJdbcTemplate().insertMapAutoGenKeyReturnBean(sql, p, getClazz(), super.pkDbName));
            }
            return result;
        } else if (objects[0].getClass().equals(super.getClazz())) {
            for (Object p : objects) {
                result.add(getJdbcTemplate().insertBeanAutoGenKeyReturnBean(sql, p, getClazz(), super.pkDbName));
            }
            return result;
        } else {
            throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型暂不支持！");
        }
    }

    /**
     * 多参数的情况只有一种，那就是多个基本类型
     * 所以此处来判断下每个参数是否都为基本类型，方可执行下一步
     *
     * @param args
     * @return
     */
    private boolean isMultiParamPrimitive(Object[] args) {
        for (Object arg : args) {
            if (!BeanUtils.isPrimitive(arg)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行参数为基本类型的方法
     *
     * @param sql
     * @param args
     * @param execute
     * @return
     */
    private Object executePrimitive(String sql, Object[] args, BiFunction<String, Map<String, Object>, Object> execute) throws Exception {
        sql = sql.toLowerCase();
        sql = sql.substring(0, sql.indexOf("values") + 6);
        Map<String, Object> map = new HashMap<>(args.length);
        StringBuilder mapValues = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            mapValues.append(":p").append(i).append(", ");
            map.put("p" + i, args[i]);
        }
        String temp = mapValues.substring(0, mapValues.length() - 2);
        sql = sql.concat(" (").concat(temp).concat(")");
        return execute.apply(sql, map);
    }
}
