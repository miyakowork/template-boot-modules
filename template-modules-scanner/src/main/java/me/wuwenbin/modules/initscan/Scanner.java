package me.wuwenbin.modules.initscan;

import me.wuwenbin.modules.initscan.annotation.TemplateScan;
import me.wuwenbin.modules.initscan.config.IScanConfig;
import me.wuwenbin.modules.initscan.enumerate.ScanType;
import me.wuwenbin.modules.initscan.persistence.IScan;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/9/19 at 9:44
 */
public final class Scanner {

    /**
     * 主扫描方法，在项目中需要实现IScanConfig、IScan等接口，
     * 并且此静态方法是用于 Listener并且继承接口ApplicationListener<ContextRefreshedEvent>，使用位置放于onApplicationEvent方法中
     *
     * @param scanDao
     * @param config
     * @param event
     * @throws Exception
     */
    public static void doIt(IScan scanDao, IScanConfig config, ContextRefreshedEvent event) throws Exception {
        if (config.getType() != ScanType.NONE) {
            if (config.getType() == ScanType.CREATE || config.getType() == ScanType.DROP) {
                String delSql = "delete from  t_oauth2_resource where system_code = ?";
                scanDao.deleteBatchScanRes(delSql, config.getSysModuleCode());
            }
            if (event.getApplicationContext().getParent() != null) {
                Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Controller.class);
                beans.putAll(event.getApplicationContext().getBeansWithAnnotation(RestController.class));
                for (Object bean : beans.values()) {
                    //此处的ultimateTargetClass方法是用来获取被spring的cglib代理类的原始类，这样才能获取到类上面的 注解（因为cglib代理类的原理是继承原始的类成成一个子类来操作）
                    if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RestController.class) || AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(Controller.class)) {
                        String[] prefixes;
                        if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RequestMapping.class)) {
                            prefixes = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(RequestMapping.class).value();
                        } else {
                            prefixes = new String[]{""};
                        }
                        prefixes = prefixes.length == 0 ? new String[]{""} : prefixes;
                        for (String prefix : prefixes) {
                            Method[] methods = AopProxyUtils.ultimateTargetClass(bean).getDeclaredMethods();
                            for (Method method : methods) {
                                String[] lasts;
                                if (method.isAnnotationPresent(RequestMapping.class)) {
                                    lasts = method.getAnnotation(RequestMapping.class).value();
                                } else {
                                    lasts = new String[]{""};
                                }
                                lasts = lasts.length == 0 ? new String[]{""} : lasts;
                                for (String last : lasts) {
                                    last = last.startsWith("/") ? last : "/".concat(last);
                                    String url = (prefix.startsWith("/") ? prefix : "/".concat(prefix)).concat("/".equals(last) ? "" : last);
                                    if (method.isAnnotationPresent(TemplateScan.class)) {
                                        String name = method.getAnnotation(TemplateScan.class).name();
                                        boolean enabled = method.getAnnotation(TemplateScan.class).enabled();
                                        int orderIndex = method.getAnnotation(TemplateScan.class).orderIndex();
                                        String systemCode = config.getSysModuleCode();
                                        if (isEmpty(systemCode)) {
                                            throw new Exception("系统模块代码为空！");
                                        }
                                        String remark = method.getAnnotation(TemplateScan.class).remark();

                                        if (method.isAnnotationPresent(RequiresPermissions.class)) {
                                            String[] permissionMarks = method.getAnnotation(RequiresPermissions.class).value();
                                            for (String permissionMark : permissionMarks) {
                                                if (config.getType() == ScanType.UPDATE) {//可做修改，前提是url不能修改否则会当做是个全新的资源插入
                                                    String existSql = "select count(0) from t_oauth_resource where url = ? ";
                                                    if (scanDao.isExistRes(existSql, url, systemCode)) {
                                                        String sql = "insert into t_oauth_resource(name,url,permission_mark,enabled,order_index,system_code,remark) values (?,?,?,?,?,?,?)";
                                                        scanDao.insertScanRes(sql, name, url, permissionMark, enabled, orderIndex, systemCode, remark);
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
            }
        }
    }


    private static boolean isEmpty(Object str) {
        return str != null && !"".equals(str);
    }

}
