package me.wuwenbin.modules.repository.provider.delete;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.repository.annotation.field.Routers;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.exception.MethodParamException;
import me.wuwenbin.modules.repository.exception.MethodTypeMissMatch;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repository.provider.delete.annotation.DeleteSQL;
import me.wuwenbin.modules.repository.util.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * created by Wuwenbin on 2017/11/1 at 19:38
 *
 * @author Wuwenbin
 */
public class DeleteProvider<T> extends AbstractProvider<T> {

    public DeleteProvider(Method method, AncestorDao jdbcTemplate, Class<T> clazz) {
        super(method, jdbcTemplate, clazz);
    }

    @Override
    public Object execute(Object[] args) throws Exception {
        String methodName = super.getMethod().getName();
        Class<?> returnType = super.getMethod().getReturnType();
        String returnVoid = "void", deleteBy = "deleteBy";
        if (returnVoid.equals(returnType.getSimpleName())) {

            //方法名为delete且没有参数，则为删除表中所有数据
            //并且仅支持参数个数为一种类型或一个数量
            String delete = "delete";
            if (args.length == 0) {
                if (methodName.equals(delete)) {
                    String sql = "delete from ".concat(super.tableName);
                    getJdbcTemplate().executeArray(sql);
                } else {
                    throw new MethodParamException("方法「」参数类型或个数有误，请参考命名规则！");
                }
            }


            //这种条件为IBaseCrudRepository预定义好的四种情况，
            //其中三种是根据主键条件来删除对应的数据库中的数据，参数类型有三种情况单个主键、多个主键数组、多个主键的集合
            //还有一种是自定义命名的且不是使用注解的方式如：deleteBySessionId或者是deleteByNameLike（此种情形是包含约束条件 - like），其中sessionId和name与列名一一对应
            //并且仅支持参数个数为一种类型或一个数量
            else if (args.length == 1) {
                //包含@Routers注解的方法
                if (super.getMethod().isAnnotationPresent(Routers.class)) {
                    Routers routerAnnotation = super.getMethod().getAnnotation(Routers.class);
                    int[] routers = routerAnnotation.value();
                    if (routers.length > 1) {
                        throw new MethodExecuteException("方法「" + super.getMethod().getName() + "」参数个数与sql语句不匹配，请参考命名规则！");
                    } else {
                        String sql = super.sbb.deleteByRouters(routers);
                        List<Field> field = MethodUtils.getFieldsByRouter(super.getClazz(), routers[0]);
                        executeWithSingleField(sql, args, field.get(0).getName());
                    }
                }
                //包含@DeleteSQL注解的方法
                else if (super.getMethod().isAnnotationPresent(DeleteSQL.class)) {
                    DeleteSQL deleteSQL = super.getMethod().getAnnotation(DeleteSQL.class);
                    String sql = deleteSQL.value();
                    sql = sql.replace("?", ":wuwenbin");//任意一个字符串做占位符
                    executeWithSingleField(sql, args, "wuwenbin");
                }
                //自定义方法名的「deleteBy」方法
                else if (methodName.startsWith(deleteBy)) {
                    String sql = getDeleteBySql(methodName, true);
                    if (sql.indexOf(":") != sql.lastIndexOf(":")) {
                        executeWithSingleField(sql, args, "");
                    } else {
                        String fieldName = sql.substring(sql.indexOf(":") + 1, sql.length());
                        executeWithSingleField(sql, args, fieldName);
                    }
                }
                //依据主键条件删除数据的「delete」方法
                else if (methodName.equals(delete)) {
                    String sql = super.sbb.deleteByPk();
                    executeWithSingleField(sql, args, super.pkFiledName);
                }
                //暂不支持的方法
                else {
                    throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型或个数有误，请参考命名规则！");
                }
            }


            //以下的条件则是为参数为两个或者两个以上的情况
            //此条件下不支持@Routers注解，因为在这种情况下无法正确匹配到参数名与参数值
            else {
                //此条件是表示有@DeleteSQL注解的情况，方法名此时无关，只要符合语法即可，且参数形式为问号形式
                if (super.getMethod().isAnnotationPresent(DeleteSQL.class)) {
                    DeleteSQL deleteSql = super.getMethod().getAnnotation(DeleteSQL.class);
                    String sql = deleteSql.value();
                    getJdbcTemplate().executeArray(sql, args);
                }
                //此条件为自定义方法名的情况
                else if (methodName.startsWith(deleteBy)) {
                    String sql = getDeleteBySql(methodName, false);
                    getJdbcTemplate().executeArray(sql, args);
                }
                //其余情况都是不支持多参数的，请直接使用「AncestorDao」接口进行操作
                else {
                    throw new MethodExecuteException("方法「" + super.getMethod().getName() + "」为不支持的类型，请参考命名规则！");
                }
            }

            //方法返回值为void，无论什么情况都是此处都是返回null
            return null;
        }

        //返回类型只能为 void
        else {
            throw new MethodTypeMissMatch("方法「" + super.getMethod().getName() + "」返回值只能是「void」类型！");
        }

    }

    /**
     * 获取「deleteBy」的sql语句
     *
     * @param methodName
     * @param colon      是否为冒号形式
     * @return
     */
    private String getDeleteBySql(String methodName, boolean colon) throws MethodExecuteException {
        String joinStr = "And|Or";
        String fieldStr = methodName.substring(8);
        String[] fields = fieldStr.split(joinStr);
        StringBuilder sqlBuilder = new StringBuilder("delete from ".concat(super.tableName).concat(" where "));
        MethodUtils.getWherePart(methodName, fieldStr, fields, sqlBuilder, colon);
        return sqlBuilder.toString();
    }


    //===============================内置方法==============================

    /**
     * 执行预定义好的delete方法
     *
     * @param sql
     * @param args
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void executeWithSingleField(String sql, Object[] args, String filedName) throws Exception {
        //Map类型以及Map的子类/子接口类型
        if (MethodUtils.paramTypeMapOrSub(args[0])) {
            getJdbcTemplate().executeMap(sql, (Map<String, Object>) args[0]);
        }

        //T实体对象类型
        else if (MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
            getJdbcTemplate().executeBean(sql, args[0]);
        }
        //数组类型
        else if (MethodUtils.paramTypeArray(args[0])) {
            Object[] pks = (Object[]) args[0];
            //Map数组
            if (pks[0] instanceof Map) {
                getJdbcTemplate().executeBatchByArrayMaps(sql, (Map<String, Object>[]) pks);
            }
            //实体数组
            else if (pks[0].getClass().equals(super.getClazz())) {
                getJdbcTemplate().executeBatchByArrayBeans(sql, pks);
            }
            //单个参数集合
            else {
                List<Map<String, Object>> paramMapList = new ArrayList<>(pks.length);
                for (Object o : pks) {
                    Map<String, Object> paramMap = new HashMap<>(1);
                    paramMap.put(filedName, o);
                    paramMapList.add(paramMap);
                }
                getJdbcTemplate().executeBatchByCollectionMaps(sql, paramMapList);
            }
        }

        //集合类型
        else if (MethodUtils.paramTypeCollectionOrSub(args[0])) {
            Collection paramCollection = (Collection) args[0];
            Object temp = paramCollection.iterator().next();
            //Map集合
            if (temp instanceof Map) {
                getJdbcTemplate().executeBatchByCollectionMaps(sql, paramCollection);
            }
            //实体集合
            else if (temp.getClass().equals(super.getClazz())) {
                getJdbcTemplate().executeBatchByCollectionBeans(sql, paramCollection);
            }
            //单个参数集合
            else {
                List<Map<String, Object>> paramMapList = new ArrayList<>(paramCollection.size());
                for (Object o : paramCollection) {
                    Map<String, Object> paramMap = new HashMap<>(1);
                    paramMap.put(filedName, o);
                    paramMapList.add(paramMap);
                }
                getJdbcTemplate().executeBatchByCollectionMaps(sql, paramMapList);
            }
        }

        //单独的一个基本类型参数
        else {
            Map<String, Object> paramMap = new HashMap<>(1);
            paramMap.put(filedName, args[0]);
            getJdbcTemplate().executeMap(sql, paramMap);
        }
    }


}
