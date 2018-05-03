package me.wuwenbin.modules.repository.util;

import me.wuwenbin.modules.jpa.exception.DataSourceKeyNotExistException;
import me.wuwenbin.modules.jpa.factory.DaoFactory;
import me.wuwenbin.modules.repository.constant.MethodType;
import me.wuwenbin.modules.repository.exception.MethodExecuteException;
import me.wuwenbin.modules.repository.exception.MethodTypeMismatchException;
import me.wuwenbin.modules.repository.provider.crud.ICrudProvider;
import me.wuwenbin.modules.repository.provider.delete.DeleteProvider;
import me.wuwenbin.modules.repository.provider.find.ReadProvider;
import me.wuwenbin.modules.repository.provider.find.support.Constraint;
import me.wuwenbin.modules.repository.provider.save.CreateProvider;
import me.wuwenbin.modules.repository.provider.select.page.PageProvider;
import me.wuwenbin.modules.repository.provider.select.random.RandProvider;
import me.wuwenbin.modules.repository.provider.update.UpdateProvider;
import me.wuwenbin.modules.sql.annotation.SQLColumn;
import me.wuwenbin.modules.sql.constant.Router;
import me.wuwenbin.modules.sql.util.SQLBuilderUtils;
import me.wuwenbin.modules.sql.util.SQLDefineUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * created by Wuwenbin on 2017/11/1 at 12:08
 *
 * @author Wuwenbin
 */
public class BeanUtils {

    /**
     * 根据传入的方法名来判断该方法属于哪一类型
     *
     * @param method
     * @return
     * @throws MethodTypeMismatchException
     */
    public static ICrudProvider getProvider(Method method, DaoFactory daoFactory, Class<?> clazz, String dataSourceKey) throws MethodTypeMismatchException, DataSourceKeyNotExistException {
        String methodName = method.getName();
        if (StringUtils.isEmpty(methodName)) {
            throw new RuntimeException("方法名为空！？");
        } else {
            if (methodName.equalsIgnoreCase(MethodType.PAGE.getName())) {
                return new PageProvider<>(method, daoFactory, clazz, dataSourceKey);
            } else if (methodName.startsWith(MethodType.SAVE.getName())) {
                return new CreateProvider<>(method, daoFactory, clazz, dataSourceKey);
            } else if (methodName.startsWith(MethodType.DELETE.getName())) {
                return new DeleteProvider<>(method, daoFactory, clazz, dataSourceKey);
            } else if (methodName.startsWith(MethodType.COUNT.getName()) || methodName.startsWith(MethodType.FIND.getName())) {
                return new ReadProvider<>(method, daoFactory, clazz, dataSourceKey);
            } else if (methodName.startsWith(MethodType.UPDATE.getName())) {
                return new UpdateProvider<>(method, daoFactory, clazz, dataSourceKey);
            } else if (methodName.startsWith(MethodType.RAND.getName())) {
                return new RandProvider<>(method, daoFactory, clazz, dataSourceKey);
            } else {
                throw new MethodTypeMismatchException();
            }
        }
    }


    /**
     * 根据方法名获取routers数组
     *
     * @param methodName
     * @return
     * @throws Exception
     */
    public static int[] getRouters(String methodName, int startIndex) throws Exception {
        String routerText = methodName.substring(startIndex);
        routerText = routerText.toLowerCase();
        char[] routerChars = routerText.toCharArray();
        if (routerText.split("").length != routerChars.length) {
            throw new Exception("方法命名中的Router部分有误，请修正！");
        } else {
            int[] routers = new int[routerChars.length];
            for (int i = 0; i < routerChars.length; i++) {
                routers[i] = Router.DEFAULT + routerChars[i];
            }
            return routers;
        }
    }

    /**
     * 判断参数类型是否为Map类型或者为Map的子类
     *
     * @param param
     * @return
     */
    public static boolean paramTypeMapOrSub(Object param) {
        return param instanceof Map;
    }

    /**
     * 判断是否为javaBean或者为此类的子类
     *
     * @param param
     * @param javaBeanClass
     * @return
     */
    public static boolean paramTypeJavaBeanOrSub(Object param, Class<?> javaBeanClass) {
        return param.getClass().equals(javaBeanClass) || javaBeanClass.isAssignableFrom(param.getClass());
    }

    /**
     * 判断参数是否为集合类型
     *
     * @param param
     * @return
     */
    public static boolean paramTypeCollectionOrSub(Object param) {
        return param instanceof Collection;
    }

    /**
     * 判断参数是否为数组类型
     *
     * @param param
     * @return
     */
    public static boolean paramTypeArray(Object param) {
        return param.getClass().isArray();
    }

    /**
     * 判断类型是不是基础类型
     *
     * @param clazz
     * @return
     */
    public static boolean isPrimitive(Class clazz) {
        return (
                clazz.equals(String.class) ||
                        clazz.equals(Integer.class) ||
                        clazz.equals(Byte.class) ||
                        clazz.equals(Long.class) ||
                        clazz.equals(Double.class) ||
                        clazz.equals(Float.class) ||
                        clazz.equals(Character.class) ||
                        clazz.equals(Short.class) ||
                        clazz.equals(BigDecimal.class) ||
                        clazz.equals(BigInteger.class) ||
                        clazz.equals(Boolean.class) ||
                        clazz.equals(Date.class) ||
                        clazz.equals(LocalDate.class) ||
                        clazz.equals(LocalDateTime.class) ||
                        clazz.isPrimitive()
        );
    }

    /**
     * 判断对象数据是不是基础类型
     *
     * @param param
     * @return
     */
    public static boolean isPrimitive(Object param) {
        return param instanceof Integer || param instanceof String || param instanceof Double || param instanceof Float || param instanceof Long || param instanceof Boolean || param instanceof Date;
    }

    /**
     * 根据router获取当前的属性字段
     *
     * @param clazz
     * @param router
     * @return
     */
    public static List<Field> getFieldsByRouter(Class clazz, int router) {
        Field[] fields = SQLBuilderUtils.getAllFieldsExceptObject(clazz);
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            int[] routers = field.isAnnotationPresent(SQLColumn.class) ? field.getAnnotation(SQLColumn.class).routers() : new int[]{Router.DEFAULT};
            if (SQLBuilderUtils.fieldRoutersInParamRouters(routers, new int[]{router})) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    /**
     * 自定义方法名处理成where部分的sql
     *
     * @param methodName
     * @param fieldStr
     * @param fields
     * @param sqlBuilder
     * @param colon      参数形式是否为冒号
     * @throws MethodExecuteException
     */
    public static void getWherePart(String methodName, String fieldStr, String[] fields, StringBuilder sqlBuilder, boolean colon) throws MethodExecuteException {
        for (int i = 0; i < fields.length; i++) {
            String fieldPart = fields[i];
            Constraint constraint = Constraint.getFromEndsWith(fieldPart);
            String field = fieldPart.endsWith(constraint.name()) ? fieldPart : fieldPart.concat(constraint.name());
            field = field.substring(0, field.length() - constraint.toString().length());
            String column = SQLDefineUtils.java2SQL("", field);
            if (i == 0) {
                field = field.substring(0, 1).toLowerCase().concat(field.substring(1, field.length()));
                if (colon) {
                    sqlBuilder.append(constraint.getPart(column, field));
                } else {
                    String temp = constraint.getPart(column, field);
                    sqlBuilder.append(temp.substring(0, temp.indexOf(":"))).append(" ?");
                }
            } else {
                String tempAnd = fields[i - 1].concat("And").concat(fieldPart);
                String tempOr = fields[i - 1].concat("Or").concat(fieldPart);
                if (fieldStr.contains(tempAnd)) {
                    field = field.substring(0, 1).toLowerCase().concat(field.substring(1, field.length()));
                    if (colon) {
                        sqlBuilder.append(" and ").append(constraint.getPart(column, field));
                    } else {
                        String temp = constraint.getPart(column, field);
                        sqlBuilder.append(" and ").append(temp.substring(0, temp.indexOf(":"))).append(" ?");
                    }
                } else if (fieldStr.contains(tempOr)) {
                    if (colon) {
                        sqlBuilder.append(" or ").append(constraint.getPart(column, field));
                    } else {
                        String temp = constraint.getPart(column, field);
                        sqlBuilder.append(" or ").append(temp.substring(0, temp.indexOf(":"))).append(" ?");
                    }
                } else {
                    throw new MethodExecuteException("方法:「" + methodName + "」命名有误，请参考命名规则！");
                }
            }
        }
    }
}
