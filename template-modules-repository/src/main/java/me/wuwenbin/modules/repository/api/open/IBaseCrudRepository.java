package me.wuwenbin.modules.repository.api.open;


import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;
import me.wuwenbin.modules.repository.provider.find.param.SelectQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/10/28 at 10:44
 *
 * @author Wuwenbin
 */
@Repository
public interface IBaseCrudRepository<T, PK> extends IRepository<T, PK> {

    //==============================增加/插入操作  开始==============================//

    /**
     * 插入一条记录，参数为Map集合
     *
     * @param entityMap
     * @return
     * @throws Exception
     */
    T save(Map<String, Object> entityMap) throws Exception;

    /**
     * 插入一条实体记录，主键插不插入一局@GeneralType的value属性值而定
     *
     * @param entity
     * @return
     * @throws Exception
     */
    T save(T entity) throws Exception;


    /**
     * 插入多条实体记录，参数为集合的形式
     *
     * @param entities
     * @return
     * @throws Exception
     */
    int[] save(Collection<T> entities) throws Exception;


    /**
     * 插入多条实体记录，参数为不定形式的实体参数/数组形式的实体参数
     *
     * @param entities
     * @return
     */
    int[] save(T... entities) throws Exception;

    /**
     * 插入多条实体记录，参数为不定类型的数组map
     *
     * @param entityMaps
     * @return
     * @throws Exception
     */
    int[] save(Map<String, Object>... entityMaps) throws Exception;


    //==============================增加/插入操作  结束==============================//
    //==============================删除操作  开始==============================//

    /**
     * 根据主键删除一条记录
     *
     * @param pk
     * @throws Exception
     */
    void delete(PK pk) throws Exception;

    /**
     * 根据多个主键删除多条记录
     *
     * @param pks
     * @throws Exception
     */
    void delete(PK... pks) throws Exception;

    /**
     * 删除数据，参数为map形式
     *
     * @param deleteParam
     * @throws Exception
     */
    void delete(Map<String, Object> deleteParam) throws Exception;

    /**
     * 根据主键集合删除多条记录
     *
     * @param pks
     * @throws Exception
     */
    void delete(Collection<PK> pks) throws Exception;


    //==============================删除操作  结束==============================//
    //==============================查询操作  开始==============================//

    /**
     * 查询
     *
     * @return
     */
    long count();

    /**
     * 根据条件统计
     *
     * @param selectQuery
     * @return
     */
    long count(SelectQuery selectQuery);

    /**
     * 是否存在
     *
     * @param pk
     * @return
     */
    boolean exists(PK pk);

    /**
     * 根据条件判断是否存在该条记录
     *
     * @param selectQuery
     * @return
     */
    boolean exists(SelectQuery selectQuery);

    /**
     * 根据主键查找一条记录
     *
     * @param pk
     * @return
     */
    T findOne(PK pk);

    /**
     * 根据条件查询出这条记录
     *
     * @param selectQuery
     * @return
     */
    T findOne(SelectQuery selectQuery);

    /**
     * 查找所有记录
     *
     * @return
     */
    List<T> findAll();

    /**
     * 查询所有，根据条件来
     *
     * @param selectQuery
     * @return
     */
    List<T> findAll(SelectQuery selectQuery);


    //==============================查询操作  结束==============================//
    //==============================修改操作  开始==============================//

    //根据app的需求而自定义方法即可，详情请参照命名规则
    //==============================修改操作  结束==============================//
}
