package me.wuwenbin.modules.mongodb.dao;

import com.mongodb.BasicDBList;
import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import me.wuwenbin.modules.mongodb.pojo.MongoStatic;
import me.wuwenbin.modules.mongodb.pojo.PageParam;
import me.wuwenbin.modules.mongodb.support.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Mongo数据库 dao操作类，基于spring-data-mongodb 1.10.1.RELEASE版本
 * <p>
 * 官方文档：http://docs.spring.io/spring-data/data-mongo/docs/1.10.1.RELEASE/reference/html/
 * 官方API：http://docs.spring.io/spring-data/data-mongo/docs/1.10.1.RELEASE/api/
 * <p>
 * Created by wuwenbin on 2017/4/22.
 */
public abstract class MongoAccessor {


    protected static Logger LOG = LoggerFactory.getLogger(MongoAccessor.class);

    protected MongoTemplate mongoTemplate;

    public MongoAccessor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    /**
     * 获取分页分页信息
     *
     * @param page
     * @return
     */
    protected abstract PageParam getPageParam(Page page);


    /**
     * 获取mongoTemplate实例
     *
     * @return
     */
    public MongoTemplate getMongoTemplate() {
        return this.mongoTemplate;
    }

    /**
     * 查询满足条件的数目
     *
     * @param query
     * @param clazz
     * @return
     */
    public <T> long findCountByQuery(Query query, Class<T> clazz) {
        long count = mongoTemplate.count(query, clazz);
        LOG.info("- COUNT查询，结果数：:" + count);
        return count;
    }

    /**
     * 通过query查询条件来查询Bean
     *
     * @param query
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findBeanByQuery(Query query, Class<T> clazz) {
        LOG.info("- 查询Bean :" + (query != null ? query.toString() : "无条件查询"));
        return mongoTemplate.findOne(query, clazz);
    }

    /**
     * 根据mongodb 的object_id查询，不是我们自定义的id
     *
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findBeanById(Object id, Class<T> clazz) {
        return mongoTemplate.findById(id, clazz);
    }

    /**
     * 根据MongoDB的主键ID查找Bean
     *
     * @param mongoId
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findBeanByMongoId(String mongoId, Class<T> clazz) {
        Query query = new Query(Criteria.where(MongoStatic.MongoID).is(mongoId));
        return findBeanByQuery(query, clazz);
    }

    /**
     * 正则表达式条件查找Bean
     *
     * @param fieldName
     * @param regex
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findBeanByRegxTxt(String fieldName, String regex, Class<T> clazz) {
        Query query = new Query(Criteria.where(fieldName).regex(regex));
        return findBeanByQuery(query, clazz);
    }

    /**
     * 正则表达式查找Bean
     *
     * @param fieldName
     * @param pattern
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findBeanByRegxPattern(String fieldName, Pattern pattern, Class<T> clazz) {
        Query query = new Query(Criteria.where(fieldName).regex(pattern));
        return findBeanByQuery(query, clazz);
    }

    /**
     * 根据某个字段来查找Bean，如果有多个此方法匹配的查询结果是返回第一条匹配的结果
     *
     * @param fieldName
     * @param fieldValue
     * @param clazz
     * @return
     */
    public <T> T findBeanByField(String fieldName, Object fieldValue, Class<T> clazz) {
        Query query = new Query(Criteria.where(fieldName).is(fieldValue));
        return findBeanByQuery(query, clazz);
    }

    /**
     * 根据多列数据来查询bean
     *
     * @param fieldMap
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findBeanByFields(TreeMap<String, Object> fieldMap, Class<T> clazz) {
        if (fieldMap == null || fieldMap.isEmpty() || fieldMap.size() == 0) {
            return null;
        } else if (fieldMap.size() == 1) {
            Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
            Map.Entry<String, Object> entry = iterator.next();
            return findBeanByField(entry.getKey(), entry.getValue(), clazz);
        } else {
            Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
            Map.Entry<String, Object> entry = iterator.next();
            Criteria criteria = Criteria.where(entry.getKey()).is(entry.getValue());
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry2 = iterator.next();
                criteria.and(entry2.getKey().toString()).is(entry2.getValue());
            }
            Query query = new Query(criteria);
            return findBeanByQuery(query, clazz);
        }
    }

    /**
     * 通过query查询出bean集合
     *
     * @param query
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findListBeanByQuery(Query query, Class<T> clazz) {
        LOG.info("- 查询ListBean :" + (query != null ? query.toString() : "没有查询条件"));
        return mongoTemplate.find(query, clazz);
    }

    /**
     * 查找所有Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findListBean(Class<T> clazz) {
        return findListBeanByQuery(null, clazz);
    }

    /**
     * 查询排序集合ListBean
     *
     * @param sortField
     * @param direction
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findListBeanBySort(String sortField, Sort.Direction direction, Class<T> clazz) {
        Query query = new Query().with(new Sort(direction, sortField));
        LOG.info("- 查询Bean :" + (query != null ? query.toString() : "无条件查询"));
        return mongoTemplate.find(query, clazz);
    }

    /**
     * 根据某个字段查询符合的ListBean
     *
     * @param fieldName
     * @param fieldValue
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findListBeanByField(String fieldName, Object fieldValue, Class<T> clazz) {
        Query query = new Query(Criteria.where(fieldName).is(fieldValue));
        return findListBeanByQuery(query, clazz);
    }

    /**
     * 根据多列数据来查询ListBean
     *
     * @param fieldMap
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> findListBeanByFields(TreeMap<String, Object> fieldMap, Class<T> clazz) {
        if (fieldMap == null || fieldMap.isEmpty() || fieldMap.size() == 0) {
            return null;
        } else if (fieldMap.size() == 1) {
            Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
            Map.Entry<String, Object> entry = iterator.next();
            return findListBeanByField(entry.getKey(), entry.getValue(), clazz);
        } else {
            Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
            Map.Entry<String, Object> entry = iterator.next();
            Criteria criteria = Criteria.where(entry.getKey()).is(entry.getValue());
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry2 = iterator.next();
                criteria.and(entry2.getKey().toString()).is(entry2.getValue());
            }
            Query query = new Query(criteria);
            return findListBeanByQuery(query, clazz);
        }
    }

    /**
     * 此方法默认是查找count(*) 和 用户定义的相关字段groupByFields 查询的结果类似
     * [{"name" : "智慧百年" , "count" : 1} , { "name" : "百年建设" , "count" :2}]
     * 这种集合list<br>
     *
     * @param criteria
     * @param clazz
     * @param groupByFields
     * @param <T>
     * @return
     */
    public <T> List findListMapByCriteriaWithGroupBy(Criteria criteria, Class<T> clazz, String... groupByFields) {
        String inputCollectionName = clazz.getAnnotation(Document.class).collection();
        GroupBy groupBy = GroupBy.key(groupByFields).initialDocument("{count:0}").reduceFunction("function(doc, prev){prev.count+=1}");
        GroupByResults<T> groupByResults = mongoTemplate.group(criteria, inputCollectionName, groupBy, clazz);
        BasicDBList list = (BasicDBList) groupByResults.getRawResults().get(MongoStatic.RAW_RESULT);
        LOG.info("- GroupBy查询，返回list集合 :" + list);
        return list;
    }

    /**
     * 类似于select distinct [column] from table，输出结果类似 ["结果1" , "结果2"]<br>
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List findListWithDistinct(String key, Class<T> clazz) {
        String collectionName = clazz.getAnnotation(Document.class).collection();
        CommandResult result = mongoTemplate.executeCommand("{distinct:'" + collectionName + "', key:'" + key + "'}");
        BasicDBList list = (BasicDBList) result.get("values");
        LOG.info("- Distinct查询，返回list集合 :" + list);
        return list;
    }

    /**
     * 类似于select distinct [column] from table，输出结果类似 ["结果1" , "结果2"]<br>
     *
     * @param key
     * @param collectionName
     * @return
     */
    public List findListWithDistinct(String key, String collectionName) {
        CommandResult result = mongoTemplate.executeCommand("{distinct:'" + collectionName + "', key:'" + key + "'}");
        BasicDBList list = (BasicDBList) result.get("values");
        LOG.info("- Distinct查询，返回list集合 :" + list);
        return list;
    }

    /**
     * 执行js
     *
     * @param json
     * @return
     */
    public CommandResult runCommand(String json) {
        LOG.info("- 执行JSON :" + json.toString());
        return mongoTemplate.executeCommand(json);
    }

    /**
     * 根据Update条件更新数据
     *
     * @param query
     * @param update
     * @param clazz
     * @return
     */
    public <T> int update(Query query, Update update, Class<T> clazz) {
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, clazz);
        int n = writeResult.getN();
        LOG.info("- 更新所有匹配文档集合 ，满足的更新条数:" + n);
        return n;
    }

    /**
     * 根据某个条件更新所有符合条件的某些字段
     *
     * @param filedName
     * @param filedValue
     * @param fieldMap
     * @param clazz
     * @return
     */
    public <T> int updateAllByField(String filedName, Object filedValue, TreeMap<String, Object> fieldMap, Class<T> clazz) {
        Query query = new Query(Criteria.where(filedName).is(filedValue));
        Update update = new Update();
        Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            update.set(entry.getKey(), entry.getValue());
        }
        return update(query, update, clazz);
    }

    /**
     * 根据id更新某些符合条件的数据字段
     *
     * @param idVal
     * @param fieldMap
     * @param clazz
     * @return
     */
    public <T> int updateAllById(String idVal, TreeMap<String, Object> fieldMap, Class<T> clazz) {
        return updateAllByField(MongoStatic.MongoID, idVal, fieldMap, clazz);
    }

    /**
     * 根据某个条件更新某个符合条件的数据字段vv
     *
     * @param condName
     * @param condVal
     * @param fieldName
     * @param fieldVlaue
     * @param clazz
     * @return
     */
    public <T> int updateAllByField(String condName, Object condVal, String fieldName, Object fieldVlaue, Class<T> clazz) {
        Query query = new Query(Criteria.where(condName).is(condVal));
        Update update = new Update().set(fieldName, fieldVlaue);
        return update(query, update, clazz);
    }

    /**
     * 根据id条件更新某个符合条件的数据字段
     *
     * @param id
     * @param fieldName
     * @param fieldVlaue
     * @param clazz
     * @return
     */
    public <T> int updateAllById(String id, String fieldName, Object fieldVlaue, Class<T> clazz) {
        Query query = new Query(Criteria.where(MongoStatic.MongoID).is(id));
        Update update = new Update().set(fieldName, fieldVlaue);
        return update(query, update, clazz);
    }

    /**
     * 插入多个文档
     *
     * @param objectsToSave
     * @param <T>
     */
    public <T> void insertAll(Collection<T> objectsToSave) {
        LOG.info("- 插入多个文档");
        mongoTemplate.insertAll(objectsToSave);
    }

    /**
     * 插入一条bean
     *
     * @param insertBean
     */
    public void insert(Object insertBean) {
        LOG.info("- 插入数据 :" + insertBean);
        mongoTemplate.insert(insertBean);

    }

    /**
     * 删除指定的collection名
     *
     * @param collectionName
     */
    public void dropCollectionByName(String collectionName) {
        LOG.info("- 删除文档，根据collection的名字");
        mongoTemplate.dropCollection(collectionName);
    }

    /**
     * 更具查询条件删除对应数据库中对应的文档中的实体类
     *
     * @param query
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> int removeBeanByQueryAndClass(Query query, Class<T> clazz) {
        WriteResult writeResult = mongoTemplate.remove(query, clazz);
        int N = writeResult.getN();
        LOG.info("- 删除文档[" + clazz.getAnnotation(Document.class).collection() + "]，返回成功删除数 :" + N);
        return N;
    }

    /**
     * 无查询条件删除某个实体类在数据库中对应的，如果实体类上@Document指定collection，则值删除指定的collection的数据
     *
     * @param removeBean
     * @return
     */
    public int removeBean(Object removeBean) {
        WriteResult writeResult = mongoTemplate.remove(removeBean);
        int n = writeResult.getN();
        LOG.info("- 删除所有匹配文档集合 ，满足的删除条数:" + n);
        return n;
    }

    /**
     * 删除数据库中指定文档中的相对应的实体类的数据
     *
     * @param query
     * @param collectionName
     * @return
     */
    public int removeBeanByQueryAndCollectionName(Query query, String collectionName) {
        WriteResult writeResult = mongoTemplate.remove(query, collectionName);
        int n = writeResult.getN();
        LOG.info("- 删除所有匹配文档集合 ，满足的删除条数:" + n);
        return n;
    }

    /**
     * 删除数据库中指定文档中的相对应的实体类的数据
     *
     * @param removeBean
     * @param collectionName
     * @return
     */
    public int removeBeanByBeanAndCollectionName(Object removeBean, String collectionName) {
        WriteResult writeResult = mongoTemplate.remove(removeBean, collectionName);
        int n = writeResult.getN();
        LOG.info("- 删除所有匹配文档集合 ，满足的删除条数:" + n);
        return n;
    }
}
