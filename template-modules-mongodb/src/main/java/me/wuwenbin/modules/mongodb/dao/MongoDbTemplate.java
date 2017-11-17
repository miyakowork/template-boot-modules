package me.wuwenbin.modules.mongodb.dao;

import me.wuwenbin.modules.mongodb.pojo.PageParam;
import me.wuwenbin.modules.mongodb.support.page.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @see {@link MongoAccessor}
 * Created by wuwenbin on 2017/4/23.
 */
public class MongoDbTemplate extends MongoAccessor {

    public MongoDbTemplate(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    protected PageParam getPageParam(Page page) {
        if (page.isFirstSetted() && page.isPageSizeSetted()) {
            return new PageParam(page.getFirst(), page.getPageSize());
        }
        return new PageParam(0, 1);
    }


    /**
     * 根据查询条件来查找分页信息
     *
     * @param query
     * @param page
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Page<T> findPageListBeanByQuery(Query query, Page page, Class<T> clazz) {
        Assert.notNull(page, "分页信息不能为空");
        PageParam pageParam = getPageParam(page);
        query.skip(pageParam.getSkip()).limit(pageParam.getLimit());
        long count;
        if (page.isAutoCount()) {
            count = mongoTemplate.count(query, clazz);
            page.setTotalCount((int) count);
        }
        List<T> list = findListBeanByQuery(query, clazz);
        page.setResult(list);
        return page;
    }

    /**
     * 按某字段排序查出page分页信息
     *
     * @param sortField
     * @param direction
     * @param page
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Page findPageListBeanBySort(String sortField, Sort.Direction direction, Page page, Class<T> clazz) {
        Query query = new Query().with(new Sort(direction, sortField));
        return findPageListBeanByQuery(query, page, clazz);
    }

}
