package me.wuwenbin.modules.repository.provider.find;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.repository.annotation.field.Routers;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.exception.MethodParamException;
import me.wuwenbin.modules.repository.exception.MethodTypeMissMatch;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repository.provider.find.annotation.CountSQL;
import me.wuwenbin.modules.repository.provider.find.annotation.ExistSQL;
import me.wuwenbin.modules.repository.provider.find.annotation.FindSQL;
import me.wuwenbin.modules.repository.provider.find.param.SelectQuery;
import me.wuwenbin.modules.repository.util.BeanUtils;
import me.wuwenbin.modules.repository.util.MethodUtils;

import java.lang.reflect.Method;
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
                return getJdbcTemplate().findListBeanByArray(sql, super.getClazz());
            } else {
                throw new MethodExecuteException("方法「" + methodName + "」为不支持的类型，请参考命名规则");
            }
        }


        //参数为一种类型的情况
        //返回非多个结果的情况，参数有Map、T、Map[]/T[]、List<T>/List<Map>、SelectQuery这么几种情况
        else if (argsLength == 1) {
            //注解优先，存在注解的情况下方法名是无意义的，随意命名都可，符合规范
            //@CountSQL注解的情况
            if (super.getMethod().isAnnotationPresent(CountSQL.class)) {
                CountSQL countSQL = super.getMethod().getAnnotation(CountSQL.class);
                String sql = countSQL.value();
                return executeWithSingleParam(sql, args, returnType);
            }
            //@ExistSQL:注解情况
            else if (super.getMethod().isAnnotationPresent(ExistSQL.class)) {
                ExistSQL existSQL = super.getMethod().getAnnotation(ExistSQL.class);
                String sql = existSQL.value();
                //方法名需规范，且唯一的参数只能为基本类型
                return executeWithSingleParam(sql, args, returnType);
            }
            //@FindSQL注解情况
            else if (super.getMethod().isAnnotationPresent(FindSQL.class)) {
                FindSQL findSQL = super.getMethod().getAnnotation(FindSQL.class);
                String sql = findSQL.value();
                //方法名规范，且唯一的参数只能为基本类型即可
                return executeWithSingleParam(sql, args, returnType);
            }
            //@Routers注解情况
            else if (super.getMethod().isAnnotationPresent(Routers.class)) {
                Routers routerAnnotation = super.getMethod().getAnnotation(Routers.class);
                int[] routers = routerAnnotation.value();
                if (routers.length == 1) {
                    //count语句
                    if (methodName.startsWith(count)) {
                        String sql = super.sbb.countAndByRouters(routers);
                        sql = sql.substring(0, sql.indexOf(":")).concat("?");
                        return executeWithSingleParam(sql, args, returnType);
                    }
                    //find
                    else if (methodName.startsWith(find)) {
                        String sql = super.sbb.selectAllByRoutersAnd(routers);
                        sql = sql.substring(0, sql.indexOf(":")).concat("?");
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
                    return executeWithSingleParam(sql, args, returnType);
                } else {
                    throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型不支持，请参考命名规则！");
                }
            }
            //以下也是无注解的情况，为自定义的方法名的情形
            //自定义countBy语句
            else if (methodName.startsWith(countBy)) {
                String preSql = "select count(0) from ".concat(super.tableName).concat(" where ");
                String sql = getSql(methodName, 7, preSql, true);
                return executeWithSingleParam(sql, args, returnType);
            }
            //自定义findBy语句
            else if (methodName.startsWith(findBy)) {
                String preSql = "select * from ".concat(super.tableName).concat(" where ");
                String sql = getSql(methodName, 6, preSql, true);
                return executeWithSingleParam(sql, args, returnType);
            }
            //自定义findXxxBy语句
            else if (methodName.substring(0, methodName.indexOf("By") + 2).matches("^find.*By$")) {
                String field = methodName.substring(methodName.indexOf("find") + 4, methodName.indexOf("By"));
                field = field.substring(0, 1).toLowerCase().concat(field.substring(1));
                String preSql = "select ".concat(field).concat(" from ").concat(super.tableName).concat(" where ");
                String sql = getSql(methodName, methodName.indexOf("By") + 2, preSql, false);
                return executeWithSingleParam(sql, args, returnType);
            }
            //不支持的方法
            else {
                throw new MethodExecuteException("方法「" + methodName + "」不支持，请参考命名规则！");
            }
        }

        //其他情况为多种参数的情况
        else {
            //注解@CountSQL，参数形式为问号
            if (super.getMethod().isAnnotationPresent(CountSQL.class)) {
                CountSQL countSQL = super.getMethod().getAnnotation(CountSQL.class);
                String sql = countSQL.value();
                return executeWithMultiParam(sql, returnType, args);
            }
            //注解@ExistSQL
            else if (super.getMethod().isAnnotationPresent(ExistSQL.class)) {
                ExistSQL existSQL = super.getMethod().getAnnotation(ExistSQL.class);
                String sql = existSQL.value();
                return executeWithMultiParam(sql, returnType, args);
            }
            //注解@FindSQL
            else if (super.getMethod().isAnnotationPresent(FindSQL.class)) {
                FindSQL findSQL = super.getMethod().getAnnotation(FindSQL.class);
                String sql = findSQL.value();
                return executeWithMultiParam(sql, returnType, args);
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
                return executeWithMultiParam(sql, returnType, args);
            }
            //自定义findXxxBy语句
            else if (methodName.substring(0, methodName.indexOf("By") + 2).matches("^find.*By$")) {
                String field = methodName.substring(methodName.indexOf("find") + 4, methodName.indexOf("By"));
                field = field.substring(0, 1).toLowerCase().concat(field.substring(1));
                String preSql = "select ".concat(field).concat(" from ").concat(super.tableName).concat(" where ");
                String sql = getSql(methodName, methodName.indexOf("By") + 2, preSql, false);
                return executeWithMultiParam(sql, returnType, args);
            }
            //不支持的方法
            else {
                throw new MethodExecuteException("方法「" + methodName + "」不支持，请参考命名规则！");
            }
        }

    }


    /**
     * 执行单个参数的方法
     *
     * @param sql
     * @param args
     * @param returnType
     * @return
     * @throws MethodTypeMissMatch
     * @throws MethodParamException
     */
    @SuppressWarnings("unchecked")
    private Object executeWithSingleParam(String sql, Object[] args, Class returnType) throws MethodTypeMissMatch, MethodParamException, MethodExecuteException {
        if (MethodUtils.paramTypeMapOrSub(args[0])) {
            Map<String, Object> mapArg = (Map<String, Object>) args[0];
            if (returnType.equals(super.getClazz())) {
                return getJdbcTemplate().findBeanByMap(sql, super.getClazz(), mapArg);
            } else if (returnType.equals(List.class) || List.class.isAssignableFrom(returnType)) {
                return getJdbcTemplate().findListBeanByMap(sql, super.getClazz(), mapArg);
            } else if (returnType.getSimpleName().equals("long")) {
                return getJdbcTemplate().queryNumberByMap(sql, Long.class, mapArg);
            } else if (returnType.getSimpleName().equals("int")) {
                return getJdbcTemplate().queryNumberByArray(sql, Integer.class, mapArg);
            } else if (returnType.getSimpleName().equals("boolean")) {
                return getJdbcTemplate().queryNumberByMap(sql, Long.class, mapArg) > 0;
            } else if (returnType.equals(Map.class) || Map.class.isAssignableFrom(returnType)) {
                return getJdbcTemplate().findMapByMap(sql, mapArg);
            } else if (returnType.isPrimitive() || BeanUtils.isPrimitive(args)) {
                Map<String, Object> result = getJdbcTemplate().findMapByMap(sql, mapArg);
                return getByIterator(returnType, result);
            } else {
                throw new MethodTypeMissMatch("方法「" + super.getMethod().getName() + "」返回类型不规范，请参考命名规则！");
            }
        } else if (MethodUtils.paramTypeArray(args[0])) {
            Object[] objArg = (Object[]) args[0];
            return executeWithMultiParam(sql, returnType, objArg);
        } else if (MethodUtils.paramTypeCollectionOrSub(args[0])) {
            Collection collectionArg = (Collection) args[0];
            Object[] objArg = collectionArg.toArray();
            return executeWithSingleParam(sql, new Object[]{objArg}, returnType);
        } else if (MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
            if (returnType.equals(super.getClazz())) {
                return getJdbcTemplate().findBeanByBean(sql, super.getClazz(), args[0]);
            } else if (returnType.equals(List.class) || List.class.isAssignableFrom(returnType)) {
                return getJdbcTemplate().findListBeanByBean(sql, super.getClazz(), args[0]);
            } else if (returnType.getSimpleName().equals("long")) {
                return getJdbcTemplate().queryNumberByBean(sql, Long.class, args[0]);
            } else if (returnType.getSimpleName().equals("int")) {
                return getJdbcTemplate().queryNumberByArray(sql, Integer.class, args[0]);
            } else if (returnType.getSimpleName().equals("boolean")) {
                return getJdbcTemplate().queryNumberByBean(sql, Long.class, args[0]) > 0;
            } else {
                throw new MethodTypeMissMatch("方法「" + super.getMethod().getName() + "」返回类型不规范，请参考命名规则！");
            }
        } else if (args[0].getClass().equals(SelectQuery.class)) {
            SelectQuery selectQuery = (SelectQuery) args[0];
            if (returnType.equals(super.getClazz())) {
                return getJdbcTemplate().findBeanByMap(sql, super.getClazz(), selectQuery.getParamMap());
            } else if (returnType.equals(List.class) || List.class.isAssignableFrom(returnType)) {
                return getJdbcTemplate().findListBeanByMap(sql, super.getClazz(), selectQuery.getParamMap());
            } else if (returnType.getSimpleName().equals("long")) {
                return getJdbcTemplate().queryNumberByMap(sql, Long.class, selectQuery.getParamMap());
            } else if (returnType.getSimpleName().equals("int")) {
                return getJdbcTemplate().queryNumberByArray(sql, Integer.class, selectQuery.getParamMap());
            } else if (returnType.getSimpleName().equals("boolean")) {
                return getJdbcTemplate().queryNumberByMap(sql, Long.class, selectQuery.getParamMap()) > 0;
            } else if (returnType.equals(Map.class) || Map.class.isAssignableFrom(returnType)) {
                return getJdbcTemplate().findMapByMap(sql, selectQuery.getParamMap());
            } else if (returnType.isPrimitive() || BeanUtils.isPrimitive(args)) {
                Map<String, Object> result = getJdbcTemplate().findMapByMap(sql, selectQuery.getParamMap());
                return getByIterator(returnType, result);
            } else {
                throw new MethodTypeMissMatch("方法「" + super.getMethod().getName() + "」返回类型不规范，请参考命名规则！");
            }
        } else if (args[0].getClass().isPrimitive() || BeanUtils.isPrimitive(args[0])) {
            Object objArg = args[0];
            return executeWithMultiParam(sql, returnType, new Object[]{objArg});
        } else {
            throw new MethodParamException("方法「" + super.getMethod().getName() + "」参数类型有误，请参考命名规则！");
        }
    }

    private Object getByIterator(Class returnType, Map<String, Object> result) throws MethodExecuteException {
        if (result != null) {
            return result.get(result.keySet().iterator().next());
        } else {
            if (BeanUtils.isPrimitive(returnType)) {
                return null;
            } else {
                throw new MethodExecuteException("方法「" + super.getMethod().getName() + "」返回类型有误，请参考命名规则！");
            }
        }
    }

    /**
     * 执行多个参数的方法
     *
     * @param sql
     * @param returnType
     * @param objArg
     * @return
     * @throws MethodTypeMissMatch
     */
    private Object executeWithMultiParam(String sql, Class returnType, Object[] objArg) throws MethodTypeMissMatch, MethodExecuteException {
        if (returnType.equals(super.getClazz())) {
            return getJdbcTemplate().findBeanByArray(sql, super.getClazz(), objArg);
        } else if (returnType.equals(List.class) || List.class.isAssignableFrom(returnType)) {
            return getJdbcTemplate().findListBeanByArray(sql, super.getClazz(), objArg);
        } else if (returnType.getSimpleName().equals("long")) {
            return getJdbcTemplate().queryNumberByArray(sql, Long.class, objArg);
        } else if (returnType.getSimpleName().equals("int")) {
            return getJdbcTemplate().queryNumberByArray(sql, Integer.class, objArg);
        } else if (returnType.getSimpleName().equals("boolean")) {
            return getJdbcTemplate().queryNumberByArray(sql, Long.class, objArg) > 0;
        } else if (returnType.equals(Map.class) || Map.class.isAssignableFrom(returnType)) {
            return getJdbcTemplate().findMapByArray(sql, objArg);
        } else if (returnType.isPrimitive() || BeanUtils.isPrimitive(objArg)) {
            Map<String, Object> result = getJdbcTemplate().findMapByArray(sql, objArg);
            return getByIterator(returnType, result);
        } else {
            throw new MethodTypeMissMatch("方法「" + super.getMethod().getName() + "」返回类型不规范，请参考命名规则！");
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
        MethodUtils.getWherePart(methodName, fieldStr, fields, sqlBuilder, colon);
        return sqlBuilder.toString();
    }

}
