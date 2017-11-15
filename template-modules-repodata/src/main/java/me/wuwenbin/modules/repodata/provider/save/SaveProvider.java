package me.wuwenbin.modules.repodata.provider.save;

import me.wuwenbin.modules.jdbc.ancestor.AncestorDao;
import me.wuwenbin.modules.repodata.annotation.field.SaveSQL;
import me.wuwenbin.modules.repodata.exception.MethodExecuteException;
import me.wuwenbin.modules.repodata.exception.MethodParamException;
import me.wuwenbin.modules.repodata.exception.MethodTypeMissMatch;
import me.wuwenbin.modules.repodata.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repodata.provider.save.support.ExecuteResult;
import me.wuwenbin.modules.repodata.util.MethodUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
        Class<?> returnType = super.getMethod().getReturnType();
        //如果方法名就是save，则直接执行插入该类的全部字段
        String save = "save";
        if (save.equals(methodName)) {
            String sql = this.produceAllFieldSql(args);
            return executeByParamType(args, returnType, sql);
        } else {
            String saveBySql = "save$BySql";
            if (methodName.startsWith(saveBySql)) {
                if (getMethod().isAnnotationPresent(SaveSQL.class)) {
                    String sql = getMethod().getAnnotation(SaveSQL.class).value();
                    if (args.length > 1) {
                        throw new MethodParamException("「save$BySql...」方法的参数个数有且仅能有一种类型且仅能有一个");
                    } else {
                        return executeByParamType(args, returnType, sql);
                    }
                } else {
                    throw new MethodExecuteException("「save$BySql」方法上必须含有@SaveSQL注解指定sql语句");
                }
            } else {
                //接下来此处是处理自定义的方法以及自定义插入Router的方法，如：saveIdName(long id,String name)/save$RouterA(...)
                //如果是指定Router则只能使用预定义的Router(从A~Z)
                String saveRouter = "save$Router";
                if (methodName.startsWith(saveRouter)) {
                    int[] routers = MethodUtils.getRouters(methodName, 11);
                    String sql = super.isPkInsert ? super.sbb.insertRoutersWithPk(routers) : super.sbb.insertRoutersWithoutPk(routers);
                    return executeByParamType(args, returnType, sql);
                } else {
                    throw new MethodExecuteException("方法命名有误，请参考命名规则！");
                }
            }
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
    private Object executeByParamType(Object[] args, Class<?> returnType, String sql) throws Exception {
        if (MethodUtils.paramTypeMapOrSub(args[0])) {
            return execSaveMethodWithParamMapOrJavaBean(returnType,
                    () -> getJdbcTemplate().insertMapAutoGenKeyOutBean(sql, (Map<String, Object>) args[0], super.getClazz(), super.tableName));
        } else if (MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
            return execSaveMethodWithParamMapOrJavaBean(returnType,
                    () -> getJdbcTemplate().insertBeanAutoGenKeyOutBean(sql, (T) args[0], super.getClazz(), super.tableName));
        } else if (MethodUtils.paramTypeCollectionOrSub(args[0])) {
            return execSaveMethodWithParamCollection(returnType, () -> {
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
            });
        } else if (MethodUtils.paramTypeArray(args[0])) {
            Object[] objects = (Object[]) args[0];
            return execSaveMethodWithParamArray(sql, objects);
        } else {
            throw new MethodParamException("定义的方法中参数类型不符合要求，参数类型只能为：JavaBean的泛型类、JavaBean的泛型集合类以及数组");
        }
    }


    //==============================生成sql语句方法=============================

    /**
     * 插入所有字段，主键id根据注解判断是否插入
     *
     * @param args
     * @return
     * @throws MethodParamException
     */
    private String produceAllFieldSql(Object[] args) throws MethodParamException {
        //判断该参数的返回类型是否为Map或者为T或T的子类
        boolean correctReturn = MethodUtils.paramTypeMapOrSub(args[0]) || MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz());
        if (args.length == 1 && correctReturn) {
            return super.isPkInsert ? super.sbb.insertAllWithPk() : super.sbb.insertAllWithoutPk();
        } else {
            throw new MethodParamException();
        }
    }


    //===================内部execute方法============================

    /**
     * 执行save方法
     *
     * @param returnType
     * @param result
     * @return
     * @throws MethodTypeMissMatch
     */
    private Object execSaveMethodWithParamMapOrJavaBean(Class<?> returnType, ExecuteResult result) throws Exception {
        if (returnType.equals(super.getClazz())) {
            return result.execute();
        } else {
            throw new MethodTypeMissMatch("参数为Map/JavaBean的「save...」方法，其返回类型必须为JavaBean");
        }
    }

    /**
     * 执行save方法
     *
     * @param returnType
     * @param result
     * @return
     * @throws MethodTypeMissMatch
     */
    private Object execSaveMethodWithParamCollection(Class<?> returnType, ExecuteResult result) throws Exception {
        //如果方法返回类型为List或者List的子类
        if (List.class.isAssignableFrom(returnType)) {
            return result.execute();
        } else {
            throw new MethodTypeMissMatch("参数为Collection/子类的「save...」方法，其返回类型必须为List<JavaBean>类型或者List的子类");
        }
    }

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
            throw new MethodTypeMissMatch("参数为数组类型的「save...」方法，其返回类型必须类List<JavaBean>类型或者List的子类");
        }
    }


}
