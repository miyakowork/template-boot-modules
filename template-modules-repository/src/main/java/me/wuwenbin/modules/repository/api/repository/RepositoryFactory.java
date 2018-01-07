package me.wuwenbin.modules.repository.api.repository;

import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

/**
 * 可以以工厂方式获取所有已注册的Repository，配置此Bean即可
 * created by Wuwenbin on 2017/11/18 at 22:29
 * @author wuwenbin
 */
public final class RepositoryFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RepositoryFactory.applicationContext = applicationContext;
    }

    /**
     * 获取定义的Repository接口
     *
     * @param repository
     * @param <T>
     * @return
     */
    public static <T extends IRepository> T get(Class<T> repository) {
        Repository repo = repository.getAnnotation(Repository.class);
        String name = repo.value();
        String repositoryBeanName = repository.getName();
        repositoryBeanName = repositoryBeanName.substring(0, 1).toLowerCase().concat(repositoryBeanName.substring(1, repositoryBeanName.length()));
        String beanName = StringUtils.isEmpty(name) ? repositoryBeanName : name;
        return applicationContext.getBean(beanName, repository);
    }

}
