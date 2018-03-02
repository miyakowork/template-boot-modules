package me.wuwenbin.modules.repository.api.open.crud;

import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;

import java.util.Collection;
import java.util.Map;

/**
 * created by Wuwenbin on 2018/2/20 at 13:53
 */
@Repository
public interface ICreateRepository<T, PK> extends IRepository<T, PK> {
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
     * 主键不是自增长的时候，返回插入影响条目
     *
     * @param entityMap
     * @return
     * @throws Exception
     */
    int savePk(Map<String, Object> entityMap) throws Exception;

    /**
     * 插入一条实体记录，主键插不插入一局@GeneralType的value属性值而定
     *
     * @param entity
     * @return
     * @throws Exception
     */
    T save(T entity) throws Exception;

    /**
     * 主键不是自增长的时候，返回插入影响条目
     *
     * @param entity
     * @return
     * @throws Exception
     */
    int savePk(T entity) throws Exception;


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

//    int saveTemplate(T entity, String... saveColumns) throws Exception;
//
//    int saveTemplate(Map<String, Object> entityMap, String... saveColumns) throws Exception;
//
//    int saveTemplate(Object[] params, String... saveColumns) throws Exception;


    //==============================增加/插入操作  结束==============================//
    //===============================以下为jdk8新特性操作，暂时不可用。现在只是接口形式=============================//
//    int save8(Supplier<String> sqlSupplier, T entity) throws Exception;
//
//    int save8(Supplier<String> sqlSupplier, Map<String, Object> entityMap) throws Exception;
//
//    int save8(Supplier<String> sqlSupplier, Object... params) throws Exception;
}
