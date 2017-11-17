package me.wuwenbin.modules.repository.provider.find;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.repository.annotation.field.CountSQL;
import me.wuwenbin.modules.repository.annotation.field.FindSQL;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.exception.MethodParamException;
import me.wuwenbin.modules.repository.exception.MethodTypeMissMatch;
import me.wuwenbin.modules.repository.provider.crud.AbstractProvider;
import me.wuwenbin.modules.repository.provider.find.param.SelectQuery;
import me.wuwenbin.modules.repository.provider.find.support.Condition;
import me.wuwenbin.modules.repository.provider.find.support.Constraint;
import me.wuwenbin.modules.repository.util.MethodUtils;
import me.wuwenbin.tools.sqlgen.util.SQLDefineUtils;

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
        String count = "count";
        if (count.equals(methodName)) {
            return executeCount(returnType, args);
        } else {
            String countByRouter = "countBy$Router";
            if (methodName.startsWith(countByRouter)) {
                return executeCountByRouter(methodName, countByRouter.length(), args);
            } else {
                String countBySql = "count$BySql";
                if (methodName.startsWith(countBySql)) {
                    return executeCountBySql(args);
                } else {
                    String exists = "exists";
                    if (methodName.equals(exists)) {
                        return executeExists(args);
                    } else {
                        String findOne = "findOne";
                        if (methodName.equals(findOne)) {
                            return executeFindOne(args);
                        } else {
                            String findAll = "findAll";
                            if (methodName.equals(findAll)) {
                                return executeFindAll(args);
                            } else {
                                String findByRouter = "findBy$Router";
                                if (methodName.startsWith(findByRouter)) {
                                    int[] routers = MethodUtils.getRouters(findByRouter, 13);
                                    String sql = super.sbb.selectAllByRoutersAnd(routers);
                                    return executeFindCustomByParamAndReturn(args, methodName, returnType, sql);
                                } else {
                                    String findBy = "findBy";
                                    if (methodName.startsWith(findBy)) {
                                        return executeFindBy(methodName, args);
                                    } else {
                                        String findBySql = "find$BySql";
                                        if (methodName.startsWith(findBySql)) {
                                            if (super.getMethod().isAnnotationPresent(FindSQL.class)) {
                                                String sql = super.getMethod().getAnnotation(FindSQL.class).value();
                                                return executeFindCustomByParamAndReturn(args, methodName, returnType, sql);
                                            } else {
                                                throw new MethodExecuteException("方法:「" + methodName + "」之上必须有@FindSQL注解指定相关sql");
                                            }
                                        } else {
                                            throw new MethodExecuteException("方法:「" + methodName + "」命名有误，请参考命名规则重新命名！");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 执行「count」方法
     *
     * @param returnType
     * @param args
     * @return
     * @throws MethodTypeMissMatch
     * @throws MethodParamException
     */
    private long executeCount(Class<?> returnType, Object[] args) throws MethodTypeMissMatch, MethodParamException {
        if (args == null) {
            String sql = super.sbb.countAll();
            return getJdbcTemplate().queryNumberByArray(sql, Long.class);
        } else if (args[0].getClass().equals(SelectQuery.class)) {
            String longStr = "long";
            if (returnType.getSimpleName().equals(longStr)) {
                return executeCountBySelectQuery(args);
            } else {
                throw new MethodTypeMissMatch("「count」方法返回类型必须为「long」");
            }
        } else {
            throw new MethodParamException("方法:「" + super.getMethod().getName() + "」参数类型有误，请参考命名方法规则重新命名！");
        }
    }

    /**
     * 执行「countByRouter」方法
     *
     * @param methodName
     * @param prefix
     * @param args
     * @return
     * @throws Exception
     */
    private long executeCountByRouter(String methodName, int prefix, Object[] args) throws Exception {
        int[] routers = MethodUtils.getRouters(methodName, 14);
        String and = "And", or = "Or";
        String newMethodName = methodName.substring(prefix);
        if (newMethodName.startsWith(and)) {
            String sql = super.sbb.countAndByRouters(routers);
            return executeByParamType(args, sql);
        } else if (newMethodName.startsWith(or)) {
            String sql = super.sbb.countOrByRouters(routers);
            return executeByParamType(args, sql);
        } else {
            throw new MethodTypeMissMatch("方法:「" + methodName + "」命名出错，请参考命名规则！");
        }
    }

    /**
     * 执行「count$BySql」方法
     *
     * @param args
     * @return
     * @throws MethodExecuteException
     * @throws MethodParamException
     */
    private long executeCountBySql(Object[] args) throws MethodExecuteException, MethodParamException {
        if (getMethod().isAnnotationPresent(CountSQL.class)) {
            String sql = super.getMethod().getAnnotation(CountSQL.class).value();
            return executeByParamType(args, sql);
        } else {
            throw new MethodExecuteException("方法:「" + super.getMethod().getName() + "」上必须含有@CountSQL注解指定相关sql");
        }
    }

    /**
     * 执行「exists」方法
     *
     * @param args
     * @return
     */
    private boolean executeExists(Object[] args) {
        if (args[0].getClass().equals(SelectQuery.class)) {
            return executeCountBySelectQuery(args) > 0;
        } else {
            String sql = "select count(0) from ".concat(super.tableName).concat(" where ").concat(super.pkDbName).concat(" = ?");
            return getJdbcTemplate().queryNumberByArray(sql, Long.class, args[0]) > 0;
        }
    }

    /**
     * 执行「findOne」方法
     *
     * @param args
     * @return
     */
    private T executeFindOne(Object[] args) {
        if (args[0].getClass().equals(SelectQuery.class)) {
            SelectQuery selectQuery = (SelectQuery) args[0];
            List<Condition> conditions = selectQuery.getConditions();
            List<String> targetRouters = selectQuery.getSelectTargets();
            if (targetRouters != null && targetRouters.size() > 0) {
                //查询部分字段
                StringBuilder sql = new StringBuilder("select ");
                for (String targetRouter : targetRouters) {
                    sql.append(" ".concat(targetRouter).concat(","));
                }
                StringBuilder newSql = new StringBuilder(sql.substring(0, sql.length() - 1));
                for (Condition c : conditions) {
                    newSql.append(" ".concat(c.getPreJoin().name()).concat(" ").concat(c.getConstraint().getPart(c.getField())));
                }
                return getJdbcTemplate().findBeanByMap(newSql.toString(), super.getClazz(), selectQuery.getParamMap());
            } else {
                //查询所有字段
                StringBuilder sql = new StringBuilder("select * from ".concat(super.tableName).concat(" where 1=1"));
                for (Condition c : conditions) {
                    sql.append(" ".concat(c.getPreJoin().name()).concat(" ").concat(c.getConstraint().getPart(c.getField())));
                }
                return getJdbcTemplate().findBeanByMap(sql.toString(), super.getClazz(), selectQuery.getParamMap());
            }
        } else {
            //参数为主键类型
            String sql = "select * from ".concat(super.tableName).concat(" where ").concat(super.pkDbName).concat(" = ?");
            return getJdbcTemplate().findBeanByArray(sql, super.getClazz(), args[0]);
        }
    }

    /**
     * 执行「findAll」方法
     *
     * @param args
     * @return
     */
    private List<T> executeFindAll(Object[] args) {
        if (args == null) {
            String sql = super.sbb.selectAll();
            return getJdbcTemplate().findListBeanByArray(sql, super.getClazz());
        } else if (args[0].getClass().equals(SelectQuery.class)) {
            SelectQuery selectQuery = (SelectQuery) args[0];
            List<Condition> conditions = selectQuery.getConditions();
            List<String> targetRouters = selectQuery.getSelectTargets();
            if (targetRouters != null && targetRouters.size() > 0) {
                //查询部分字段
                StringBuilder sql = new StringBuilder("select ");
                for (String targetRouter : targetRouters) {
                    sql.append(" ".concat(targetRouter).concat(","));
                }
                StringBuilder newSql = new StringBuilder(sql.substring(0, sql.length() - 1));
                for (Condition c : conditions) {
                    newSql.append(" ".concat(c.getPreJoin().name()).concat(" ").concat(c.getConstraint().getPart(c.getField())));
                }
                return getJdbcTemplate().findListBeanByMap(newSql.toString(), super.getClazz(), selectQuery.getParamMap());
            } else {
                //查询所有字段
                StringBuilder sql = new StringBuilder("select * from ".concat(super.tableName).concat(" where 1=1"));
                for (Condition c : conditions) {
                    sql.append(" ".concat(c.getPreJoin().name()).concat(" ").concat(c.getConstraint().getPart(c.getField())));
                }
                return getJdbcTemplate().findListBeanByMap(sql.toString(), super.getClazz(), selectQuery.getParamMap());
            }
        } else {
            String sql = super.sbb.selectAll();
            return getJdbcTemplate().findListBeanByArray(sql, super.getClazz());
        }
    }

    /**
     * 执行「findBy」方法
     *
     * @param methodName
     * @param args
     * @return
     * @throws MethodExecuteException
     * @throws MethodParamException
     */
    private Object executeFindBy(String methodName, Object[] args) throws MethodExecuteException, MethodParamException {
        String joinStr = "And|Or";
        String fieldStr = methodName.substring(6);
        String[] fields = fieldStr.split(joinStr);
        StringBuilder sqlBuilder = new StringBuilder("select * from ".concat(super.tableName).concat(" where "));
        for (int i = 0; i < fields.length; i++) {
            String fieldPart = fields[i];
            Constraint constraint = Constraint.getFromEndsWith(fieldPart);
            String field = fieldPart.substring(0, fieldPart.length() - constraint.toString().length());
            String column = SQLDefineUtils.java2SQL("", field);
            if (i == 0) {
                sqlBuilder.append(constraint.getPart(column, field));
            } else {
                String tempAnd = fields[i - 1].concat("And").concat(fieldPart);
                String tempOr = fields[i - 1].concat("Or").concat(fieldPart);
                if (fieldStr.contains(tempAnd)) {
                    sqlBuilder.append(" and ").append(constraint.getPart(column, field));
                } else if (fieldStr.contains(tempOr)) {
                    sqlBuilder.append(" or ").append(constraint.getPart(column, field));
                } else {
                    throw new MethodExecuteException("方法:「" + methodName + "」命名有误，请参考命名规则！");
                }
            }
        }
        String sql = sqlBuilder.toString();
        return executeFindCustomByParamAndReturn(args, methodName, super.getMethod().getReturnType(), sql);
    }


    /**
     * 根据方法参数类型判断执行方法
     *
     * @param args
     * @param sql
     * @return
     * @throws MethodParamException
     */
    @SuppressWarnings("unchecked")
    private Long executeByParamType(Object[] args, String sql) throws MethodParamException {
        if (MethodUtils.paramTypeMapOrSub(args[0])) {
            return getJdbcTemplate().queryNumberByMap(sql, Long.class, (Map<String, Object>) args[0]);
        } else if (MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
            return getJdbcTemplate().queryNumberByBean(sql, Long.class, args[0]);
        } else {
            throw new MethodParamException("方法:「" + super.getMethod().getName() + "」参数类型有误，请参考命名规则！");
        }
    }

    /**
     * 有selectQuery对象的参数时执行的方法
     *
     * @param args
     * @return
     */
    private long executeCountBySelectQuery(Object[] args) {
        SelectQuery selectQuery = (SelectQuery) args[0];
        List<Condition> conditions = selectQuery.getConditions();
        StringBuilder sql = new StringBuilder(super.sbb.countAll().concat(" and 1=1"));
        for (Condition c : conditions) {
            sql.append(" ".concat(c.getPreJoin().name()).concat(" ").concat(c.getConstraint().getPart(c.getField())));
        }
        return getJdbcTemplate().queryNumberByMap(sql.toString(), Long.class, selectQuery.getParamMap());
    }

    /**
     * 执行「find」的方法，根据返回类型以及参数类型来判断具体执行的那个方法
     *
     * @param args
     * @param methodName
     * @param returnType
     * @param sql
     * @return
     * @throws MethodExecuteException
     * @throws MethodParamException
     */
    @SuppressWarnings("unchecked")
    private Object executeFindCustomByParamAndReturn(Object[] args, String methodName, Class returnType, String sql) throws MethodExecuteException, MethodParamException {
        if (MethodUtils.paramTypeMapOrSub(args[0])) {
            if (returnType.getClass().equals(super.getClazz()) || super.getClazz().isAssignableFrom(returnType.getClass())) {
                return getJdbcTemplate().findBeanByMap(sql, super.getClazz(), (Map<String, Object>) args[0]);
            } else if (Collection.class.isAssignableFrom(super.getClazz())) {
                return getJdbcTemplate().findListBeanByMap(sql, super.getClazz(), (Map<String, Object>) args[0]);
            } else {
                throw new MethodExecuteException("方法:「" + methodName + "」返回类型有误，请参考命名规则！");
            }
        } else if (MethodUtils.paramTypeJavaBeanOrSub(args[0], super.getClazz())) {
            if (returnType.getClass().equals(super.getClazz()) || super.getClazz().isAssignableFrom(returnType.getClass())) {
                return getJdbcTemplate().findBeanByBean(sql, super.getClazz(), args[0]);
            } else if (Collection.class.isAssignableFrom(super.getClazz())) {
                return getJdbcTemplate().findListBeanByBean(sql, super.getClazz(), args[0]);
            } else {
                throw new MethodExecuteException("方法:「" + methodName + "」返回类型有误，请参考命名规则！");
            }
        } else {
            throw new MethodParamException("方法:「" + methodName + "」参数类型有误，请参考命名规则！");
        }
    }
}
