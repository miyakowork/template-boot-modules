package me.wuwenbin.modules.sql.util;


import me.wuwenbin.modules.sql.annotation.MappedSuper;
import me.wuwenbin.modules.sql.annotation.SQLColumn;
import me.wuwenbin.modules.sql.annotation.SQLTable;
import me.wuwenbin.modules.sql.annotation.not.NotInsert;
import me.wuwenbin.modules.sql.annotation.not.NotSelect;
import me.wuwenbin.modules.sql.annotation.not.NotUpdate;
import me.wuwenbin.modules.sql.constant.Router;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


/**
 * some methods of sql building
 * <p>
 * Created by wuwenbin on 2017/1/9.
 */
public class SQLBuilderUtils {

    private static final String SINGLE_SPACE = " ";
    private static final String DOUBLE_SPACE = "  ";

    private static final String AND = "AND";
    private static final String ANDSPACE = "AND ";
    private static final String COMMA = ",";
    private static final String COMMASPACE = ", ";

    /**
     * 最后生成sql时候,做一些字符串处理
     *
     * @param sql to transfer
     * @return {@link String}
     */
    public static String dealSQL(String sql) {
        sql = sql.replace(", FROM ", " FROM ").replace(",  FROM ", " FROM ");
        sql = sql.replace(", WHERE ", " WHERE ").replace(",  WHERE ", " WHERE ");
        sql = sql.replace(", )", ")");
        sql = sql.replace(DOUBLE_SPACE, SINGLE_SPACE);
        if (sql.endsWith(AND)) {
            sql = sql.substring(0, sql.length() - 3);
        }
        if (sql.endsWith(ANDSPACE)) {
            sql = sql.substring(0, sql.length() - 4);
        }
        if (sql.endsWith(COMMA)) {
            sql = sql.substring(0, sql.length() - 1);
        }
        if (sql.endsWith(COMMASPACE)) {
            sql = sql.substring(0, sql.length() - 2);
        }
        return sql;
    }

    /**
     * bean上是否存在@SQLTable
     *
     * @param beanClass the obj of sql building
     * @return {@link Boolean}
     */
    public static boolean SQLTableIsExist(Class<?> beanClass) {
        return beanClass.isAnnotationPresent(SQLTable.class);
    }

    /**
     * 检查字段上的routers是否是方法参数中指定的
     *
     * @param filedRouters 字段上的routers
     * @param paramRouters 方法参数中的routers
     * @return {@link Boolean}
     */
    public static boolean fieldRoutersInParamRouters(int[] filedRouters, int[] paramRouters) {
        List<Integer> paramRouterList = new ArrayList<>();
        for (int paramRouter : paramRouters) {
            paramRouterList.add(paramRouter);
        }

        for (int filedRouter : filedRouters) {
            if (paramRouterList.contains(filedRouter)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取类中所有字段包括父类的protected字段
     *
     * @param clazz
     * @return
     */
    public static Field[] getAllFieldsExceptObject(Class<?> clazz) {
        List<Field> fields = new Vector<>();
        Class tempClass = clazz;
        while (tempClass != Object.class) {
            fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
            while (!isMappedSuperClass(tempClass)) {
                if (tempClass == Object.class) {
                    break;
                }
                tempClass = tempClass.getSuperclass();
            }
        }
        Field[] newField = new Field[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            newField[i] = field;
        }
        return newField;
    }

    /**
     * routers条件判断是否为不为空
     *
     * @param routers
     * @return
     */
    public static boolean routerIsNotEmpty(int... routers) {
        return routers != null && routers.length > 0;
    }

    /**
     * 判断是否需要把父类的字段也一起囊括
     *
     * @param clazz
     * @return
     */
    private static boolean isMappedSuperClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(MappedSuper.class) && clazz.getAnnotation(MappedSuper.class).value();
    }

    /**
     * 判断类中属性字段是否有含有不能select的
     *
     * @param fields
     * @return
     */
    public static boolean hasNoSelectField(Field[] fields) {
        for (Field field : fields) {
            if (!canBeSelect(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断类中属性字段是否有含有不能insert的
     *
     * @param fields
     * @return
     */
    public static boolean hasNoInsertField(Field[] fields) {
        for (Field field : fields) {
            if (!canBeInsert(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断类中属性字段是否有含有不能update的
     *
     * @param fields
     * @return
     */
    public static boolean hasNoUpdateField(Field[] fields) {
        for (Field field : fields) {
            if (!canBeUpdate(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前列是否可以被select
     *
     * @param field
     * @return
     */
    public static boolean canBeSelect(Field field) {
        return !field.isAnnotationPresent(NotSelect.class) && (!field.isAnnotationPresent(SQLColumn.class) || field.getAnnotation(SQLColumn.class).select());
    }

    /**
     * 判断当前列是否可以被insert
     *
     * @param field
     * @return
     */
    public static boolean canBeInsert(Field field) {
        return !field.isAnnotationPresent(NotInsert.class) && (!field.isAnnotationPresent(SQLColumn.class) || field.getAnnotation(SQLColumn.class).insert());
    }

    /**
     * 判断当前列是否可以被update
     *
     * @param field
     * @return
     */
    public static boolean canBeUpdate(Field field) {
        return !field.isAnnotationPresent(NotUpdate.class) && (!field.isAnnotationPresent(SQLColumn.class) || field.getAnnotation(SQLColumn.class).update());
    }


    /**
     * 获取当前列上的router集合
     * 有注解并且有声明router的话，返回此字段，没有则是返回默认的
     *
     * @param field
     * @return
     */
    public static int[] getRouterInField(Field field) {
        if (field.isAnnotationPresent(SQLColumn.class)) {
            return field.getAnnotation(SQLColumn.class).routers();
        } else {
            return new int[]{Router.DEFAULT};
        }
    }
}
