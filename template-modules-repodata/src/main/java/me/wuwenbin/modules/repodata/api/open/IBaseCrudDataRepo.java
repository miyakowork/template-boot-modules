package me.wuwenbin.modules.repodata.api.open;


import me.wuwenbin.modules.repodata.annotation.type.DataRepo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/10/28 at 10:44
 *
 * @author Wuwenbin
 */
@DataRepo
public interface IBaseCrudDataRepo<T, PK> {

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
    List<T> save(Collection<T> entities) throws Exception;


    /**
     * 插入多条实体记录，参数为不定形式的实体参数/数组形式的实体参数
     *
     * @param entities
     * @return
     */
    List<T> save(T... entities) throws Exception;

    /**
     * 插入多条实体记录，参数为不定类型的数组map
     *
     * @param entityMaps
     * @return
     * @throws Exception
     */
    List<T> save(Map<String, Object>... entityMaps) throws Exception;


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
     * 根据主键集合删除多条记录
     *
     * @param pks
     * @throws Exception
     */
    void delete(Collection<PK> pks) throws Exception;

}
