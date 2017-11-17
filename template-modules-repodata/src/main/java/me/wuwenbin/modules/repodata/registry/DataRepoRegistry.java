package me.wuwenbin.modules.repodata.registry;

import me.wuwenbin.modules.jdbc.factory.DaoFactory;
import me.wuwenbin.modules.repodata.annotation.type.DataRepo;
import me.wuwenbin.modules.repodata.proxy.DataRepoProxyFactory;
import me.wuwenbin.modules.repodata.util.ClassScanUtils;
import me.wuwenbin.tools.sqlgen.util.SQLDefineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * created by Wuwenbin on 2017/10/30 at 12:03
 *
 * @author Wuwenbin
 */
@Configuration
public class DataRepoRegistry implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DataRepoRegistry.class);

    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    private DaoFactory daoFactory;

    /**
     * spring开始启动将扫描好的bean的潜开始注入到容器中的前置操作处理，在此可以动态注入我们生成的class
     *
     * @param beanDefinitionRegistry
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        logger.info("注册BootService开始");
        try {
            Set<Class<?>> scannedClass = scanBootServiceInterfaces();
            for (Class bootService : scannedClass) {
                //noinspection unchecked
                registerBean(beanDefinitionRegistry,
                        SQLDefineUtils.java2SQL("", bootService.getSimpleName()),
                        new DataRepoProxyFactory<>(this.daoFactory, bootService).newInstance().getClass());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("扫描BootService类失败", e);
        }
        logger.info("注册BootService完毕");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        //没有什么后置处理
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.daoFactory = applicationContext.getBean(DaoFactory.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.daoFactory, "未检测到DaoFactory注入，请先完成此Bean的注入！");
    }

    /**
     * 手动注册动态生成的bean
     *
     * @param registry
     * @param name
     * @param beanClass
     */
    private void registerBean(BeanDefinitionRegistry registry, String name, Class<?> beanClass) {
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
        ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
        abd.setScope(scopeMetadata.getScopeName());
        // 可以自动生成name
        String beanName = (!StringUtils.isEmpty(name) ? name : this.beanNameGenerator.generateBeanName(abd, registry));
        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }

    /**
     * 扫描class
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Set<Class<?>> scanBootServiceInterfaces() throws IOException, ClassNotFoundException {
        Set<Class<? extends Annotation>> classSet = new HashSet<>();
        classSet.add(DataRepo.class);
        return ClassScanUtils.scan("", classSet, null);
    }

}
