package me.wuwenbin.modules.repository.registry;

import me.wuwenbin.modules.repository.annotation.type.DataRepo;
import me.wuwenbin.modules.repository.proxy.DataRepoProxyFactory;
import me.wuwenbin.modules.repository.util.ClassScanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.*;
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
//@Configuration
public class DataRepoRegistry implements BeanDefinitionRegistryPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DataRepoRegistry.class);

    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    private DataRepoProxyFactory dataRepoProxyFactory;

    public DataRepoRegistry(DataRepoProxyFactory dataRepoProxyFactory) {
        this.dataRepoProxyFactory = dataRepoProxyFactory;
    }

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
                String simpleBeanName = bootService.getSimpleName().substring(0, 1).toLowerCase().concat(bootService.getSimpleName().substring(1));
                //noinspection unchecked
                Class clazz = dataRepoProxyFactory.newInstance(bootService).getClass();
                registerBean(beanDefinitionRegistry, simpleBeanName, clazz);
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
