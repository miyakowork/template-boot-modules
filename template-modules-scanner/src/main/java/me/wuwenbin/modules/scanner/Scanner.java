package me.wuwenbin.modules.scanner;

import me.wuwenbin.modules.scanner.annotation.ResourceScan;
import me.wuwenbin.modules.scanner.config.ScannerConfig;
import me.wuwenbin.modules.scanner.enumerate.ScannerType;
import me.wuwenbin.modules.scanner.persistence.ScannerRepository;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger LOG = LoggerFactory.getLogger(Scanner.class);

    /**
     * 主扫描方法，在项目中需要实现IScanConfig、IScan等接口，
     * 并且此静态方法是用于 Listener并且继承接口ApplicationListener<ContextRefreshedEvent>，使用位置放于onApplicationEvent方法中
     *
     * @param scannerRepository
     * @param config
     * @param event
     * @throws Exception
     */
    public static void scan(ScannerRepository scannerRepository, ScannerConfig config, ContextRefreshedEvent event) throws Exception {
        if (config.getScannerType() != ScannerType.NONE) {
            LOG.info("扫描类型为 [{}]，开始扫描步骤的后续操作", config.getScannerType().name());
            if (config.getScannerType() == ScannerType.CREATE || config.getScannerType() == ScannerType.DROP) {
                scannerRepository.deleteResources(config.getSystemModuleCode());
            }
            if (config.getScannerType() != ScannerType.DROP) {
                if (event.getApplicationContext().getParent() != null) {
                    Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Controller.class);
                    beans.putAll(event.getApplicationContext().getBeansWithAnnotation(RestController.class));
                    int cnt = 0;
                    for (Object bean : beans.values()) {
                        if (isControllerPresent(bean)) {
                            String[] prefixes = getPrefixUrl(bean);
                            for (String prefix : prefixes) {
                                Method[] methods = AopProxyUtils.ultimateTargetClass(bean).getDeclaredMethods();
                                for (Method method : methods) {
                                    String[] lasts = getLastUrl(method);
                                    for (String last : lasts) {
                                        String url = getCompleteUrl(prefix, last);
                                        if (method.isAnnotationPresent(ResourceScan.class)) {
                                            LOG.info("资源 url：[{}] 扫描到@ResourceScan，准备插入数据库", url);
                                            deleteRoleResource(config, scannerRepository, url);
                                            cnt = insertResourceAndRoleResource(method, config, scannerRepository, url, cnt);
                                        } else {
                                            LOG.info("资源 url：[{}] 未扫描到@ResourceScan，略过插入数据库步骤", url);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    LOG.info("扫描资源完毕，共计插入资源数目：[{}]", cnt);
                }
            }
        } else {
            LOG.info("扫描类型为 [{}]，略过资源扫描步骤", ScannerType.NONE.name());
        }
    }


    private static boolean isEmpty(Object str) {
        return str != null && !"".equals(str);
    }

    /**
     * 是否包含Controller或者RestController
     * 此处的ultimateTargetClass方法是用来获取被spring的cglib代理类的原始类，这样才能获取到类上面的 注解（因为cglib代理类的原理是继承原始的类成成一个子类来操作）
     *
     * @param bean
     * @return
     */
    private static boolean isControllerPresent(Object bean) {
        return AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RestController.class) || AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(Controller.class);
    }

    /**
     * 获取Controller上的RequestMapping中的value值，即url的前缀部分
     *
     * @param bean
     * @return
     */
    private static String[] getPrefixUrl(Object bean) {
        String[] prefixes;
        if (AopProxyUtils.ultimateTargetClass(bean).isAnnotationPresent(RequestMapping.class)) {
            prefixes = AopProxyUtils.ultimateTargetClass(bean).getAnnotation(RequestMapping.class).value();
        } else {
            prefixes = new String[]{""};
        }
        return prefixes.length == 0 ? new String[]{""} : prefixes;
    }

    /**
     * 获取Controller内的方法上的RequestMapping的value值，即url的后缀部分
     *
     * @param method
     * @return
     */
    private static String[] getLastUrl(Method method) {
        String[] lasts;
        if (method.isAnnotationPresent(RequestMapping.class)) {
            lasts = method.getAnnotation(RequestMapping.class).value();
        } else {
            lasts = new String[]{""};
        }
        return lasts.length == 0 ? new String[]{""} : lasts;
    }

    /**
     * 根据prefix和last获取完整url
     *
     * @param prefix
     * @param last
     * @return
     */
    private static String getCompleteUrl(String prefix, String last) {
        last = last.startsWith("/") ? last : "/".concat(last);
        return (prefix.startsWith("/") ? prefix : "/".concat(prefix)).concat("/".equals(last) ? "" : last);
    }

    /**
     * 删除role_resource对照关系记录
     *
     * @param config
     * @param scannerRepository
     * @param url
     * @throws Exception
     */
    private static void deleteRoleResource(ScannerConfig config, ScannerRepository scannerRepository, String url) throws Exception {
        for (long roleId : config.getRoleIds()) {
            scannerRepository.deleteRoleResource(roleId, config.getSystemModuleCode(), url);
        }
    }

    /**
     * 插入role_resource对照关系记录
     *
     * @param config
     * @param scannerRepository
     * @param resId
     * @throws Exception
     */
    private static void insertRoleResource(ScannerConfig config, ScannerRepository scannerRepository, long resId) throws Exception {
        for (long roleId : config.getRoleIds()) {
            scannerRepository.insertRoleResource(roleId, resId);
        }
    }

    /**
     * 最后处理插入资源的操作
     *
     * @param method
     * @param config
     * @param scannerRepository
     * @param url
     * @throws Exception
     */
    private static int insertResourceAndRoleResource(Method method, ScannerConfig config, ScannerRepository scannerRepository, String url, int cnt) throws Exception {
        String name = method.getAnnotation(ResourceScan.class).value();
        boolean enabled = method.getAnnotation(ResourceScan.class).enabled();
        int orderIndex = method.getAnnotation(ResourceScan.class).orderIndex();
        String systemCode = config.getSystemModuleCode();
        if (isEmpty(systemCode)) {
            throw new RuntimeException("系统模块代码为空！");
        }
        String remark = method.getAnnotation(ResourceScan.class).remark();
        if (method.isAnnotationPresent(RequiresPermissions.class)) {
            String[] permissionMarks = method.getAnnotation(RequiresPermissions.class).value();
            for (String permissionMark : permissionMarks) {
                //可做修改，前提是url不能修改否则会当做是个全新的资源插入
                if (config.getScannerType() == ScannerType.UPDATE) {
                    if (scannerRepository.isResourceExist(url, systemCode)) {
                        cnt++;
                        long resId = scannerRepository.insertResources(name, url, permissionMark, enabled, orderIndex, systemCode, remark);
                        insertRoleResource(config, scannerRepository, resId);
                    }
                } else {
                    cnt++;
                    long resId = scannerRepository.insertResources(name, url, permissionMark, enabled, orderIndex, systemCode, remark);
                    insertRoleResource(config, scannerRepository, resId);
                }
            }
        }
        return cnt;
    }
}
