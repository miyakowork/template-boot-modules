package me.wuwenbin.modules.repository.api.open.crud;

import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;

import java.util.Collection;
import java.util.Map;

/**
 * created by Wuwenbin on 2018/2/20 at 13:53
 */
@Repository
public interface IDeleteRepository<T, PK> extends IRepository<T, PK> {
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
    void delete(Map<String, PK> deleteParam) throws Exception;

    /**
     * 根据主键集合删除多条记录
     *
     * @param pks
     * @throws Exception
     */
    void delete(Collection<PK> pks) throws Exception;


    //==============================删除操作  结束==============================//
}
