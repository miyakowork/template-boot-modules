package me.wuwenbin.modules.repository.provider.find;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.repository.annotation.field.Routers;
import me.wuwenbin.modules.repository.annotation.field.SQL;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.exception.MethodParamException;
import me.wuwenbin.modules.repository.exception.MethodTypeMismatchException;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repository.provider.find.annotation.*;
import me.wuwenbin.modules.repository.provider.find.param.SelectQuery;
import me.wuwenbin.modules.repository.util.BeanUtils;
import me.wuwenbin.modules.sql.support.Symbol;
import me.wuwenbin.modules.sql.util.SQLBuilderUtils;
import me.wuwenbin.modules.sql.util.SQLDefineUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        String methodName = super.getMethod().getName();
        Class returnType = super.getMethod().getReturnType();
        int argsLength = args == null ? 0 : args.length;
        String count = "count", exist = "exists", find = "find", findOne = "findOne", findAll = "findAll";
        String countBy = "countBy", findBy = "findBy";

        //无参数的情况
        //支持count()统计所有数据、findAll()查询数据库中所有数据， 其他情况暂时不支持
        if (argsLength == 0) {
            if (methodName.equals(count)) {
                String sql = super.sbb.countAll();
                return getJdbcTemplate().queryNumberByArray(sql, Long.class);
            } else if (methodName.equals(findAll)) {
                String sql = super.sbb.selectAll();
                if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                    String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                    String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                    sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
                }
                return getJdbcTemplate().findListBeanByArray(sql, super.getClazz());
            } else if (super.getMethod().isAnnotationPresent(SQL.class)) {
                String sql = super.getMethod().getAnnotation(SQL.class).value();
                if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                    String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                    String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                    sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
                }
                return executeWithMultiParam(sql, returnType, null);
            } else {
                throw new MethodExecuteException("方法「" + methodName + "」为不支持的类型，请参考命名规则");
            }
        }


        //参数为一种类型的情况
        //返回非多个结果的情况，参数有Map、T、Map[]/T[]、List<T>/List<Map>、SelectQuery这么几种情况
        else if (argsLength == 1) {
            //注解优先，存在注解的情况下方法名是无意义的，随意命名都可，符合规范
            //@CountSQL注解的情况
            if (super.getMethod().isAnnotationPresent(SQL.class)) {
                SQL sqlAnnotation = super.getMethod().getAnnotation(SQL.class);
                String sql = sqlAnnotation.value();
                sql = getOrderSql(methodName, sql);
                return executeWithSingleParam(sql, args, returnType);
            }
            //@Routers注解情况
            else if (super.getMethod().isAnnotationPresent(Routers.class)) {
                Routers routerAnnotation = super.getMethod().getAnnotation(Routers.class);
                int[] routers = routerAnnotation.value();
                if (SQLBuilderUtils.getFieldsByRouters(super.getClazz(), routers).size() == 1) {
                    //count语句
                    if (methodName.startsWith(count)) {
                        String sql = super.sbb.countAndByRouters(Symbol.COLON, routers);
                        sql = sql.substring(0, sql.indexOf(":")).concat("?");
                        return executeWithSingleParam(sql, args, returnType);
                    }
                    //find
                    else if (methodName.startsWith(find)) {
                        String sql = super.sbb.selectAllByRoutersAnd(Symbol.COLON, routers);
                        sql = sql.substring(0, sql.indexOf(":")).concat("?");
                        if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                            String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                            String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                            sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
                        }
                        return executeWithSingleParam(sql, args, returnType);
                    } else {
                        throw new MethodExecuteException("方法「" + methodName + "」不支持，请参考命名规则！");
                    }
                } else {
                    throw new MethodExecuteException("方法「" + methodName + "」有误。检测到一个参数，但有多个router条件，请参考命名规则！");
                }
            }
            //以下是无注解的情况，为预定义方法名的情形
            //执行预定义的exists或者count方法
            else if (methodName.equals(exist) || methodName.equals(count)) {
                if (args[0].getClass().equals(SelectQuery.class)) {
                    SelectQuery selectQuery = (SelectQuery) args[0];
                    String sql = "select count(0) from ".concat(super.tableName).concat(selectQuery.getWhereSqlPart());
                    return executeWithSingleParam(sql, args, returnType);
                } else {
                    String sql = super.stb.countAndByColumns(super.tableName, super.pkDbName);
                    return executeWithSingleParam(sql, args, returnType);
                }
            }
            //预定义的findOne方法
            else if (methodName.equals(findOne)) {
                if (args[0].getClass().equals(SelectQuery.class)) {
                    SelectQuery selectQuery = (SelectQuery) args[0];
                    String sql = "select * from ".concat(super.tableName).concat(selectQuery.getWhereSqlPart());
                    return executeWithSingleParam(sql, args, returnType);
                } else {
                    String sql = super.stb.selectAllByColumns(super.tableName, super.pkDbName);
                    return executeWithSingleParam(sql, args, returnType);
                }
            }
            //预定义的findAll方法
            else if (methodName.equals(findAll)) {
                if (args[0].getClass().equals(SelectQuery.class)) {
                    SelectQuery selectQuery = (SelectQuery) args[0];
                    String sql = "select * from ".concat(super.tableName).concat(selectQuery.getWhereSqlPart());
                    if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                        String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                        String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                        sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
                    }
                    return executeWithSingleParam(sql, args, returnType);
                } else {
                    throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型不支持，请参考命名规则！");
                }
            }
            //以下也是无注解的情况，为自定义的方法名的情形
            //自定义countBy语句
            else if (methodName.startsWith(countBy)) {
                String preSql = "select count(0) from ".concat(super.tableName).concat(" where ");
                String sql;
                if (methodName.substring(7).split("And|Or").length > 1) {
                    sql = getSql(methodName, 7, preSql, true);
                } else {
                    sql = getSql(methodName, 7, preSql, false);
                }
                return executeWithSingleParam(sql, args, returnType);
            }
            //自定义findBy语句
            else if (methodName.startsWith(findBy)) {
                String preSql = "select * from ".concat(super.tableName).concat(" where ");
                String sql;
                if (methodName.substring(6).split("And|Or").length > 1) {
                    sql = getSql(methodName, 6, preSql, true);
                } else {
                    sql = getSql(methodName, 6, preSql, false);
                }
                if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                    String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                    String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                    sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
                }
                return executeWithSingleParam(sql, args, returnType);
            }
            //自定义findXxxBy语句
            else if (methodName.substring(0, methodName.indexOf("By") + 2).matches("^find.*By$")) {
                String field = methodName.substring(methodName.indexOf("find") + 4, methodName.indexOf("By"));
                field = field.substring(0, 1).toLowerCase().concat(field.substring(1));
                String preSql = "select ".concat(SQLDefineUtils.java2SQL("", field)).concat(" from ").concat(super.tableName).concat(" where ");
                String sql;
                if (methodName.substring(methodName.indexOf("By") + 2).split("And|Or").length == 1) {
                    sql = getSql(methodName, methodName.indexOf("By") + 2, preSql, false);
                } else {
                    sql = getSql(methodName, methodName.indexOf("By") + 2, preSql, true);
                }
                if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                    String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                    String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                    sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
                }
                return executeWithSingleParam(sql, args, returnType);
            }
            //不支持的方法
            else {
                throw new MethodExecuteException("方法「" + methodName + "」不支持，请参考命名规则！");
            }
        }

        //其他情况为多种参数的情况
        else {
            //注解@SQL
            if (super.getMethod().isAnnotationPresent(SQL.class)) {
                SQL sqlAnnotation = super.getMethod().getAnnotation(SQL.class);
                String sql = sqlAnnotation.value();
                sql = getOrderSql(methodName, sql);
                return executeWithMultiParam(sql, returnType, args);
            }
            //@Routers注解情况
            else if (super.getMethod().isAnnotationPresent(Routers.class)) {
                Routers routerAnnotation = super.getMethod().getAnnotation(Routers.class);
                int[] routers = routerAnnotation.value();
                if (SQLBuilderUtils.getFieldsByRouters(super.getClazz(), routers).size() > 1) {
                    //count语句
                    if (methodName.startsWith(count)) {
                        String sql = super.sbb.countAndByRouters(Symbol.QUESTION_MARK, routers);
                        return executeWithMultiParam(sql, returnType, args);
                    }
                    //find
                    else if (methodName.startsWith(find)) {
                        String sql = super.sbb.selectAllByRoutersAnd(Symbol.QUESTION_MARK, routers);
                        if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                            String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                            String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                            sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
                        }
                        return executeWithMultiParam(sql, returnType, args);
                    } else {
                        throw new MethodExecuteException("方法「" + methodName + "」不支持，请参考命名规则！");
                    }
                } else {
                    throw new MethodExecuteException("方法「" + methodName + "」有误。检测到一个router条件，但有多个参数，请参考命名规则！");
                }
            }
            //自定义countBy语句
            else if (methodName.startsWith(countBy)) {
                String preSql = "select count(0) from ".concat(super.tableName).concat(" where ");
                String sql = getSql(methodName, 7, preSql, false);
                return executeWithMultiParam(sql, returnType, args);
            }
            //自定义findBy语句
            else if (methodName.startsWith(findBy)) {
                String preSql = "select * from ".concat(super.tableName).concat(" where ");
                String sql = getSql(methodName, 6, preSql, false);
                if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                    String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                    String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                    sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
                }
                return executeWithMultiParam(sql, returnType, args);
            }
            //自定义findXxxBy语句
            else if (methodName.substring(0, methodName.indexOf("By") + 2).matches("^find.*By$")) {
                String field = methodName.substring(methodName.indexOf("find") + 4, methodName.indexOf("By"));
                field = field.substring(0, 1).toLowerCase().concat(field.substring(1));
                String preSql = "select ".concat(SQLDefineUtils.java2SQL("", field)).concat(" from ").concat(super.tableName).concat(" where ");
                String sql = getSql(methodName, methodName.indexOf("By") + 2, preSql, false);
                if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                    String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                    String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                    sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
                }
                return executeWithMultiParam(sql, returnType, args);
            }
            //不支持的方法
            else {
                throw new MethodExecuteException("方法「" + methodName + "」不支持，请参考命名规则！");
            }
        }

    }


    private String getOrderSql(String methodName, String sql) {
        if (methodName.startsWith("find")) {
            if (super.getMethod().isAnnotationPresent(OrderBy.class)) {
                String orderField = super.getMethod().getAnnotation(OrderBy.class).value();
                String order = super.getMethod().getAnnotation(OrderBy.class).order().name();
                sql = sql.concat(" order by ").concat(orderField).concat(" ").concat(order);
            }
            if (super.getMethod().isAnnotationPresent(OrderBys.class)) {
                String[] orderFields = super.getMethod().getAnnotation(OrderBys.class).value();
                String[] order = Arrays.stream(super.getMethod().getAnnotation(OrderBys.class).order()).map(Enum::name).toArray(String[]::new);
                if (orderFields.length == order.length && orderFields.length > 0) {
                    StringBuilder temp = new StringBuilder();
                    for (int i = 0; i < orderFields.length; i++) {
                        temp.append(orderFields[i].concat(" ").concat(order[i]).concat(","));
                    }
                    temp = new StringBuilder(temp.substring(0, temp.length() - 1));
                    sql = sql.concat(" order by ").concat(temp.toString());
                }
            }
        }
        return sql;
    }


    /**
     * 执行单个参数的方法
     *
     * @param sql
     * @param args
     * @param returnType
     * @return
     * @throws MethodTypeMismatchException
     * @throws MethodParamException
     */
    @SuppressWarnings("unchecked")
    private Object executeWithSingleParam(String sql, Object[] args, Class returnType) throws MethodTypeMismatchException, MethodParamException, MethodExecuteException {
        if (BeanUtils.paramTypeMapOrSub(args[0])) {
            Map<String, Object> mapArg = (Map<String, Object>) args[0];
            if (returnType.equals(super.getClazz()) || "findOne".equals(super.getMethod().getName())) {
                return getJdbcTemplate().findBeanByMap(sql, super.getClazz(), mapArg);
            } else if (returnType.equals(List.class) || List.class.isAssignableFrom(returnType)) {
                if (super.getMethod().isAnnotationPresent(PrimitiveCollection.class)) {
                    Class<?> genericClass = super.getMethod().getAnnotation(PrimitiveCollection.class).value();
                    return getJdbcTemplate().findListPrimitiveByMap(sql, genericClass, mapArg);
                } else if (super.getMethod().isAnnotationPresent(ListMap.class)) {
                    return getJdbcTemplate().findListMapByMap(sql, mapArg);
                } else {
                    return getJdbcTemplate().findListBeanByMap(sql, super.getClazz(), mapArg);
                }
            } else if (returnType.getSimpleName().equals("long")) {
                return getJdbcTemplate().queryNumberByMap(sql, Long.class, mapArg);
            } else if (returnType.getSimpleName().equals("int")) {
                return getJdbcTemplate().queryNumberByMap(sql, Integer.class, mapArg);
            } else if (returnType.getSimpleName().equals("boolean")) {
                return getJdbcTemplate().queryNumberByMap(sql, Long.class, mapArg) > 0;
            } else if (returnType.equals(Map.class) || Map.class.isAssignableFrom(returnType)) {
                return getJdbcTemplate().findMapByMap(sql, mapArg);
            } else if (BeanUtils.isPrimitive(returnType)) {
                if (super.getMethod().isAnnotationPresent(Primitive.class)) {
                    Class<?> genericClass = super.getMethod().getAnnotation(Primitive.class).value();
                    return getJdbcTemplate().findPrimitiveByMap(sql, genericClass, mapArg);
                } else {
                    throw new MethodExecuteException("方法「" + super.getMethod().getName() + "」返回类型为基本类型，必须使用注解@PrimitiveCollection或者@Primitive标明泛型类型");
                }
            } else {
                throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型不规范，请参考命名规则！");
            }
        } else if (BeanUtils.paramTypeArray(args[0])) {
            Object[] objArg = (Object[]) args[0];
            return executeWithMultiParam(sql, returnType, objArg);
        } else if (BeanUtils.paramTypeCollectionOrSub(args[0])) {
            Collection collectionArg = (Collection) args[0];
            Object[] objArg = collectionArg.toArray();
            return executeWithSingleParam(sql, new Object[]{objArg}, returnType);
        } else if (BeanUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
            if (returnType.equals(super.getClazz()) || "findOne".equals(super.getMethod().getName())) {
                return getJdbcTemplate().findBeanByBean(sql, super.getClazz(), args[0]);
            } else if (returnType.equals(List.class) || List.class.isAssignableFrom(returnType)) {
                if (super.getMethod().isAnnotationPresent(PrimitiveCollection.class)) {
                    Class<?> genericClass = super.getMethod().getAnnotation(PrimitiveCollection.class).value();
                    return getJdbcTemplate().findListPrimitiveByBean(sql, genericClass, args[0]);
                } else if (super.getMethod().isAnnotationPresent(ListMap.class)) {
                    return getJdbcTemplate().findListMapByBean(sql, args[0]);
                } else {
                    return getJdbcTemplate().findListBeanByBean(sql, super.getClazz(), args[0]);
                }
            } else if (returnType.getSimpleName().equals("long")) {
                return getJdbcTemplate().queryNumberByBean(sql, Long.class, args[0]);
            } else if (returnType.getSimpleName().equals("int")) {
                return getJdbcTemplate().queryNumberByArray(sql, Integer.class, args[0]);
            } else if (returnType.getSimpleName().equals("boolean")) {
                return getJdbcTemplate().queryNumberByBean(sql, Long.class, args[0]) > 0;
            } else {
                throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型不规范，请参考命名规则！");
            }
        } else if (args[0].getClass().equals(SelectQuery.class)) {
            SelectQuery selectQuery = (SelectQuery) args[0];
            if (returnType.equals(super.getClazz()) || "findOne".equals(super.getMethod().getName())) {
                return getJdbcTemplate().findBeanByMap(sql, super.getClazz(), selectQuery.getParamMap());
            } else if (returnType.equals(List.class) || List.class.isAssignableFrom(returnType)) {
                if (super.getMethod().isAnnotationPresent(PrimitiveCollection.class)) {
                    Class<?> genericClass = super.getMethod().getAnnotation(PrimitiveCollection.class).value();
                    return getJdbcTemplate().findListPrimitiveByMap(sql, genericClass, selectQuery.getParamMap());
                } else if (super.getMethod().isAnnotationPresent(ListMap.class)) {
                    return getJdbcTemplate().findListMapByMap(sql, selectQuery.getParamMap());
                } else {
                    return getJdbcTemplate().findListBeanByMap(sql, super.getClazz(), selectQuery.getParamMap());
                }
            } else if (returnType.getSimpleName().equals("long")) {
                return getJdbcTemplate().queryNumberByMap(sql, Long.class, selectQuery.getParamMap());
            } else if (returnType.getSimpleName().equals("int")) {
                return getJdbcTemplate().queryNumberByMap(sql, Integer.class, selectQuery.getParamMap());
            } else if (returnType.getSimpleName().equals("boolean")) {
                return getJdbcTemplate().queryNumberByMap(sql, Long.class, selectQuery.getParamMap()) > 0;
            } else if (returnType.equals(Map.class) || Map.class.isAssignableFrom(returnType)) {
                return getJdbcTemplate().findMapByMap(sql, selectQuery.getParamMap());
            } else if (BeanUtils.isPrimitive(returnType)) {
                if (super.getMethod().isAnnotationPresent(Primitive.class)) {
                    Class<?> genericClass = super.getMethod().getAnnotation(Primitive.class).value();
                    return getJdbcTemplate().findPrimitiveByMap(sql, genericClass, selectQuery.getParamMap());
                } else {
                    throw new MethodExecuteException("方法「" + super.getMethod().getName() + "」返回类型为基本类型，必须使用注解@PrimitiveCollection或者@Primitive标明泛型类型");
                }
            } else {
                throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型不规范，请参考命名规则！");
            }
        } else if (args[0].getClass().isPrimitive() || BeanUtils.isPrimitive(args[0])) {
            Object objArg = args[0];
            return executeWithMultiParam(sql, returnType, new Object[]{objArg});
        } else {
            throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型有误，请参考命名规则！");
        }
    }

    /**
     * 执行多个参数的方法
     *
     * @param sql
     * @param returnType
     * @param objArg
     * @return
     * @throws MethodTypeMismatchException
     */
    private Object executeWithMultiParam(String sql, Class returnType, Object[] objArg) throws MethodTypeMismatchException, MethodExecuteException {
        if (returnType.equals(super.getClazz()) || "findOne".equals(super.getMethod().getName())) {
            return getJdbcTemplate().findBeanByArray(sql, super.getClazz(), objArg);
        } else if (returnType.equals(List.class) || List.class.isAssignableFrom(returnType)) {
            if (super.getMethod().isAnnotationPresent(PrimitiveCollection.class)) {
                Class<?> genericClass = super.getMethod().getAnnotation(PrimitiveCollection.class).value();
                return getJdbcTemplate().findListPrimitiveByArray(sql, genericClass, objArg);
            } else if (super.getMethod().isAnnotationPresent(ListMap.class)) {
                return getJdbcTemplate().findListMapByArray(sql, objArg);
            } else {
                return getJdbcTemplate().findListBeanByArray(sql, super.getClazz(), objArg);
            }
        } else if (returnType.getSimpleName().equals("long")) {
            return getJdbcTemplate().queryNumberByArray(sql, Long.class, objArg);
        } else if (returnType.getSimpleName().equals("int")) {
            return getJdbcTemplate().queryNumberByArray(sql, Integer.class, objArg);
        } else if (returnType.getSimpleName().equals("boolean")) {
            return getJdbcTemplate().queryNumberByArray(sql, Long.class, objArg) > 0;
        } else if (returnType.equals(Map.class) || Map.class.isAssignableFrom(returnType)) {
            return getJdbcTemplate().findMapByArray(sql, objArg);
        } else if (BeanUtils.isPrimitive(returnType)) {
            if (super.getMethod().isAnnotationPresent(Primitive.class)) {
                Class<?> genericClass = super.getMethod().getAnnotation(Primitive.class).value();
                return getJdbcTemplate().findPrimitiveByArray(sql, genericClass, objArg);
            } else {
                throw new MethodExecuteException("方法「" + super.getMethod().getName() + "」返回类型为基本类型，必须使用注解@PrimitiveCollection或者@Primitive标明泛型类型！");
            }
        } else {
            throw new MethodTypeMismatchException("方法「" + super.getMethod().getName() + "」返回类型不规范，请参考命名规则！");
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
        String[] fields = fieldStr.split(joinStr);
        StringBuilder sqlBuilder = new StringBuilder(preSql);
        BeanUtils.getWherePart(methodName, fieldStr, fields, sqlBuilder, colon);
        return sqlBuilder.toString();
    }

}
