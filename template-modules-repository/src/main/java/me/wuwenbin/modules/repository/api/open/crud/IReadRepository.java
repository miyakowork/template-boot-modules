package me.wuwenbin.modules.repository.api.open.crud;

import me.wuwenbin.modules.pagination.query.NonePageQuery;
import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;
import me.wuwenbin.modules.repository.provider.find.annotation.ListMap;
import me.wuwenbin.modules.repository.provider.find.param.SelectQuery;

import java.util.List;
import java.util.Map;

/**
 * created by Wuwenbin on 2018/2/20 at 13:53
 */
@Repository
public interface IReadRepository<T, PK> extends IRepository<T, PK> {
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


    /**
     * 通过Query条件查询记录
     *
     * @param mainSql    主sql
     * @param clazz
     * @param tableQuery
     * @return
     */
    List<T> findByQuery(String mainSql, Class<T> clazz, NonePageQuery tableQuery);

    /**
     * 通过Query条件查询记录
     *
     * @param mainSql
     * @param nonePageQuery
     * @return
     */
    @ListMap
    List<Map<String, Object>> findByQuery(String mainSql, NonePageQuery nonePageQuery);

    //==============================查询操作  结束==============================//
}
