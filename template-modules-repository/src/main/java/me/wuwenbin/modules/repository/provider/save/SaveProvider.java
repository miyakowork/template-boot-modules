package me.wuwenbin.modules.repository.provider.save;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.repository.annotation.field.Routers;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.exception.MethodParamException;
import me.wuwenbin.modules.repository.exception.MethodTypeMissMatch;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repository.provider.save.annotation.SaveSQL;
import me.wuwenbin.modules.repository.util.MethodUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

/**
 * insert/save方法的执行提供者
 * created by Wuwenbin on 2017/11/1 at 11:32
 *
 * @author Wuwenbin
 */
public class SaveProvider<T> extends AbstractProvider<T> {

    public SaveProvider(Method method, AncestorDao jdbcTemplate, Class<T> clazz) {
        super(method, jdbcTemplate, clazz);
    }


    @Override
    public Object execute(Object[] args) throws Exception {
        String methodName = super.getMethod().getName();
        Type returnType = super.getMethod().getGenericReturnType();

        //插入全部实体字段
        String save = "save";
        if (save.equals(methodName)) {
            boolean paramIsCorrect = args.length == 1 && (MethodUtils.paramTypeMapOrSub(args[0]) || MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz()));
            if (paramIsCorrect) {
                String sql = super.isPkInsert ? super.sbb.insertAllWithPk() : super.sbb.insertAllWithoutPk();
                return executeByParamType(args, returnType, sql);
            } else {
                throw new MethodParamException("方法「" + methodName + "」的参数形式（个数或类型）不正确，请参考命名规则！");
            }
        }

        //指定自定义insert SQL语句，方法参数视sql语句而定（可以为多个，每个依次对应sql语句中的参数）
        else if (methodName.startsWith(save) && super.getMethod().isAnnotationPresent(SaveSQL.class)) {
            String saveSql = super.getMethod().getAnnotation(SaveSQL.class).value();
            return executeByParamType(args, returnType, saveSql);
        }

        //指定插入的字段Router条件，根据router生成插入的sql
        else if (methodName.startsWith(save) && super.getMethod().isAnnotationPresent(Routers.class)) {
            int[] routers = super.getMethod().getAnnotation(Routers.class).value();
            String sql = super.isPkInsert ? super.sbb.insertRoutersWithPk(routers) : super.sbb.insertRoutersWithoutPk(routers);
            return executeByParamType(args, returnType, sql);
        }

        //暂不支持的方法
        else {
            throw new MethodExecuteException("不支持的方法「" + super.getMethod().getName() + "」，请使用「AncestorDao」接口！");
        }

    }


    /**
     * 根据参数的类型来执行对应的sql方法
     *
     * @param args
     * @param returnType
     * @param sql
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Object executeByParamType(Object[] args, Type returnType, String sql) throws Exception {
        if (args.length > 1) {
            String intName = "int", paramSign = ":";
            if (intName.equals(returnType.getTypeName())) {
                String considerSql = sql;
                if (considerSql.contains(paramSign)) {
                    String values = considerSql.substring(considerSql.lastIndexOf("(") + 1, considerSql.lastIndexOf(")"));
                    StringBuilder q = new StringBuilder();
                    Arrays.stream(values.split(",")).forEach(p -> q.append("?").append(", "));
                    considerSql = considerSql.substring(0, considerSql.indexOf(")") + 8).concat(" (").concat(q.substring(0, q.length() - 2)).concat(")");
                    return getJdbcTemplate().executeArray(considerSql, args);
                }
                return getJdbcTemplate().executeArray(sql, args);
            } else {
                throw new MethodParamException("方法「" + super.getMethod().getName() + "」返回类型不正确，请参考命名规则！");
            }
        } else if (MethodUtils.paramTypeMapOrSub(args[0])) {
            return getJdbcTemplate().insertMapAutoGenKeyOutBean(sql, (Map<String, Object>) args[0], super.getClazz(), super.tableName);
        } else if (MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
            return getJdbcTemplate().insertBeanAutoGenKeyOutBean(sql, (T) args[0], super.getClazz(), super.tableName);
        } else if (MethodUtils.paramTypeCollectionOrSub(args[0])) {
            Collection<T> paramCollection = (Collection<T>) args[0];
            List<T> result = new ArrayList<>(paramCollection.size());
            paramCollection.forEach(p -> {
                try {
                    result.add(getJdbcTemplate().insertBeanAutoGenKeyOutBean(sql, p, super.getClazz(), super.tableName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return result;
        } else if (MethodUtils.paramTypeArray(args[0])) {
            Object[] objects = (Object[]) args[0];
            return execSaveMethodWithParamArray(sql, objects);
        } else {
            throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型不符合要求，请参考命名规则！");
        }
    }


    //===================内部execute方法============================


    /**
     * 执行save方法
     *
     * @param sql
     * @param objects
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Object execSaveMethodWithParamArray(String sql, Object[] objects) throws Exception {
        List<T> result = new ArrayList<>();
        if (objects[0] instanceof Map) {
            Map<String, Object>[] paramMap = (Map<String, Object>[]) objects;
            Stream.of(paramMap).forEach(p -> {
                try {
                    result.add(getJdbcTemplate().insertMapAutoGenKeyOutBean(sql, p, getClazz(), tableName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return result;
        } else if (objects[0].getClass().equals(super.getClazz())) {
            Stream.of(objects).forEach(p -> {
                try {
                    result.add(getJdbcTemplate().insertBeanAutoGenKeyOutBean(sql, (T) p, getClazz(), tableName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return result;
        } else {
            throw new MethodTypeMissMatch("方法「" + super.getMethod().getName() + "」返回类型不规范，请参考命名规则！");
        }
    }


}
