package me.wuwenbin.modules.repository.api.repository;

import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

/**
 * created by Wuwenbin on 2017/11/18 at 22:29
 */
public final class RepositoryFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RepositoryFactory.applicationContext = applicationContext;
    }

    public static <T extends IRepository> T get(Class<T> repository) {
        Repository repo = repository.getAnnotation(Repository.class);
        String name = repo.value();
        String simpleName = repository.getSimpleName();
        simpleName = simpleName.substring(0, 1).toLowerCase().concat(simpleName.substring(1, simpleName.length()));
        String beanName = StringUtils.isEmpty(name) ? simpleName : name;
        return applicationContext.getBean(beanName, repository);
    }

}
