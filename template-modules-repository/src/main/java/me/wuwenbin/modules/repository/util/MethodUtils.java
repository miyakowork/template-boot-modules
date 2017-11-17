package me.wuwenbin.modules.repository.util;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.repository.constant.MethodType;
import me.wuwenbin.modules.repository.exception.MethodTypeMissMatch;
import me.wuwenbin.modules.repository.provider.crud.ICrudProvider;
import me.wuwenbin.modules.repository.provider.delete.DeleteProvider;
import me.wuwenbin.modules.repository.provider.find.FindProvider;
import me.wuwenbin.modules.repository.provider.page.PageProvider;
import me.wuwenbin.modules.repository.provider.save.SaveProvider;
import me.wuwenbin.modules.repository.provider.update.UpdateProvider;
import me.wuwenbin.tools.sqlgen.constant.Router;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/11/1 at 12:08
 *
 * @author Wuwenbin
 */
public class MethodUtils {

    /**
     * 根据传入的方法名来判断该方法属于哪一类型
     *
     * @param method
     * @return
     * @throws MethodTypeMissMatch
     */
    public static <T> ICrudProvider getProvider(Method method, AncestorDao jdbcTemplate, Class<T> clazz) throws MethodTypeMissMatch {
        String methodName = method.getName();
        if (StringUtils.isEmpty(methodName)) {
            throw new RuntimeException("方法名为空！？");
        } else {
            String pagination = "findPagination";
            if (methodName.equals(pagination)) {
                return new PageProvider<>(method, jdbcTemplate, clazz);
            } else if (methodName.startsWith(MethodType.SAVE.toString())) {
                return new SaveProvider<>(method, jdbcTemplate, clazz);
            } else if (methodName.startsWith(MethodType.DELETE.toString())) {
                return new DeleteProvider<>(method, jdbcTemplate, clazz);
            } else if (methodName.startsWith(MethodType.COUNT.toString()) || methodName.startsWith(MethodType.FIND.toString())) {
                return new FindProvider<>(method, jdbcTemplate, clazz);
            } else if (methodName.startsWith(MethodType.UPDATE.toString())) {
                return new UpdateProvider<>(method, jdbcTemplate, clazz);
            } else {
                throw new MethodTypeMissMatch();
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
}
