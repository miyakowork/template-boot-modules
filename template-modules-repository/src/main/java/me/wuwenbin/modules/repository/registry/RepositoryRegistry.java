package me.wuwenbin.modules.repository.registry;

import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.proxy.RepositoryProxyFactory;
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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 注册Repository代理类至Spring容器中
 * created by Wuwenbin on 2017/10/30 at 12:03
 *
 * @author Wuwenbin
 */
@Component
public class RepositoryRegistry implements BeanDefinitionRegistryPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryRegistry.class);

    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    private RepositoryProxyFactory repositoryProxyFactory = new RepositoryProxyFactory();


    /**
     * spring开始启动将扫描好的bean的潜开始注入到容器中的前置操作处理，在此可以动态注入我们生成的class
     *
     * @param beanDefinitionRegistry
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        int cnt = 0;
        try {
            logger.info("========== 开始扫描含有 [@Repository] 的 Bean ==========");
            Set<Class<?>> repositories = scanBootServiceInterfaces();
            logger.info("========== 扫描结束，以供扫描到含有 [@Repository] 的 Bean {} 个 ==========", repositories.size());


            logger.info("========== 开始注册扫描到的 [Repositories] 至 Spring 容器中 ==========");

            for (Class repository : repositories) {
                Repository repositoryClass = (Repository) repository.getAnnotation(Repository.class);
                String beanName = repositoryClass.value();
                String simpleBeanName = StringUtils.isEmpty(beanName) ? repository.getName().substring(0, 1).toLowerCase().concat(repository.getName().substring(1)) : beanName;
                //noinspection unchecked
                Class clazz = repositoryProxyFactory.newInstance(repository).getClass();

                logger.info("---------- 开始注册 [Repository，BeanClass : {} ，BeanName：{}] ----------", repository, simpleBeanName);
                registerBean(beanDefinitionRegistry, simpleBeanName, clazz);
                logger.info("---------- 注册结束 [Repository，BeanClass : {} ，BeanName：{}] ----------", repository, simpleBeanName);

                cnt++;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("！！！扫描Repository类失败！！！", e);
        }

        logger.info("========== 注册结束，总计注册成功的 [Repositories] 个数为 {} 个 ==========", cnt);
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
        classSet.add(Repository.class);
        return ClassScanUtils.scan("", classSet, null);
    }

}
