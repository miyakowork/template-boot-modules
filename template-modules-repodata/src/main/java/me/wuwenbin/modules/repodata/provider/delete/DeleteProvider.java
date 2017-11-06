package me.wuwenbin.modules.repodata.provider.delete;

import me.wuwenbin.modules.jdbc.ancestor.AncestorDao;
import me.wuwenbin.modules.repodata.annotation.field.DeleteSQL;
import me.wuwenbin.modules.repodata.exception.MethodExecuteException;
import me.wuwenbin.modules.repodata.exception.MethodParamException;
import me.wuwenbin.modules.repodata.exception.MethodTypeMissMatch;
import me.wuwenbin.modules.repodata.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repodata.util.MethodUtils;

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
        String returnTypeName = "void";
        if (returnTypeName.equals(returnType.getSimpleName())) {
            if (args.length != 1) {
                throw new MethodParamException("参数种类只能为一种，详情请参考命名规则");
            } else {
                String delete = "delete";
                if (delete.equals(methodName)) {
                    String sql = super.sbb.deleteByPk();
                    executePredefineDeleteMethod(sql, args);
                } else {
                    String deleteByRouter = "deleteBy$Router";
                    if (methodName.startsWith(deleteByRouter)) {
                        int[] routers = MethodUtils.getRouters(methodName, 15);
                        String sql = super.sbb.deleteByRouters(routers);
                        executeCustomDeleteMethod(sql, args);
                    } else {
                        String deleteBy = "deleteBy";
                        if (methodName.startsWith(deleteBy)) {
                            String sql = getDeleteBySql(methodName);
                            executeCustomDeleteMethod(sql, args);
                        } else {
                            String deleteBySql = "delete$BySql";
                            if (methodName.startsWith(deleteBySql)) {
                                if (super.getMethod().isAnnotationPresent(DeleteSQL.class)) {
                                    String sql = super.getMethod().getAnnotation(DeleteSQL.class).value();
                                    executeCustomDeleteMethod(sql, args);
                                } else {
                                    throw new MethodExecuteException("「delete$BySql」必须使用@DeleteSQL指定先关掉的sql");
                                }
                            }
                        }
                    }
                }
            }
            //方法返回值为void，无论什么情况都是此处都是返回null
            return null;
        } else {
            throw new MethodTypeMissMatch("「delete...」方法返回值必须为「void」类型");
        }
    }

    /**
     * 获取自定义方法的sql
     *
     * @param methodName
     * @return
     */
    private String getDeleteBySql(String methodName) {
        StringBuilder sql = new StringBuilder("delete from ".concat(super.tableName).concat(" where "));
        String fields = methodName.substring(8);
        String and = "And";
        String or = "Or";
        if (fields.contains(and)) {
            String[] fieldNames = fields.split(and);
            for (String fieldName : fieldNames) {
                String name = fieldName.substring(0, 1).concat(fieldName.substring(1));
                sql.append(name).append(" = :").append(name).append(" and ");
            }
        } else if (fields.contains(or)) {
            String[] fieldNames = fields.split(or);
            for (String fieldName : fieldNames) {
                String name = fieldName.substring(0, 1).concat(fieldName.substring(1));
                sql.append(name).append(" = :").append(name).append(" or ");
            }
        } else {
            String name = fields.substring(0, 1).concat(fields.substring(1));
            sql.append(name).append(" = :").append(name);
        }
        String finalSql = sql.toString();
        String endWithAnd = "and ";
        String endWithOr = "or ";
        if (sql.toString().endsWith(endWithAnd)) {
            finalSql = finalSql.substring(0, finalSql.length() - 4);
        }
        if (sql.toString().endsWith(endWithOr)) {
            finalSql = finalSql.substring(0, finalSql.length() - 3);
        }
        return finalSql;
    }


    //===============================内置方法==============================

    /**
     * 执行预定义好的delete方法
     *
     * @param sql
     * @param args
     * @throws Exception
     */
    private void executePredefineDeleteMethod(String sql, Object[] args) throws Exception {
        if (MethodUtils.paramTypeArray(args[0])) {
            Object[] pks = (Object[]) args[0];
            List<Map<String, Object>> paramList = new ArrayList<>(pks.length);
            Arrays.stream(pks).forEach(p -> {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put(super.pkFiledName, p);
                paramList.add(paramMap);
            });
            getJdbcTemplate().executeBatchByCollectionMaps(sql, paramList);
        } else if (MethodUtils.paramTypeCollectionOrSub(args[0])) {
            //noinspection unchecked
            Collection<Object> paramCollection = (Collection<Object>) args[0];
            List<Map<String, Object>> paramList = new ArrayList<>(paramCollection.size());
            paramCollection.forEach(p -> {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put(super.pkFiledName, p);
                paramList.add(paramMap);
            });
            getJdbcTemplate().executeBatchByCollectionMaps(sql, paramList);
        } else {
            Map<String, Object> paramMap = new HashMap<>(1);
            paramMap.put(super.pkFiledName, args[0]);
            getJdbcTemplate().executeMap(sql, paramMap);
        }
    }

    /**
     * 执行deleteBy$Router方法
     *
     * @param sql
     * @param args
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void executeCustomDeleteMethod(String sql, Object[] args) throws Exception {
        if (MethodUtils.paramTypeMapOrSub(args[0])) {
            getJdbcTemplate().executeMap(sql, (Map<String, Object>) args[0]);
        } else if (MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
            getJdbcTemplate().executeBean(sql, args[0]);
        } else if (MethodUtils.paramTypeCollectionOrSub(args[0])) {
            Collection<T> paramCollection = (Collection<T>) args[0];
            getJdbcTemplate().executeBatchByCollectionBeans(sql, paramCollection);
        } else if (MethodUtils.paramTypeArray(args[0])) {
            Object[] objects = (Object[]) args[0];
            if (objects[0] instanceof Map) {
                Map<String, Object>[] paramMap = (Map<String, Object>[]) objects;
                getJdbcTemplate().executeBatchByArrayMaps(sql, paramMap);
            } else if (objects[0].getClass().equals(super.getClazz())) {
                getJdbcTemplate().executeBatchByArrayBeans(sql, objects);
            }
        }
    }


}
