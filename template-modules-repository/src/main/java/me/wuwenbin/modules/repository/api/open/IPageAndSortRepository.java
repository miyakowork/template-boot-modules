package me.wuwenbin.modules.repository.api.open;


import me.wuwenbin.modules.jpa.support.Page;
import me.wuwenbin.modules.pagination.query.TableQuery;
import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;
import me.wuwenbin.modules.repository.provider.select.page.support.IPageExtra;

/**
 * 分页信息获取接口
 * created by Wuwenbin on 2017/10/29 at 11:12
 *
 * @author Wuwenbin
 */
@Repository
public interface IPageAndSortRepository<T, PK> extends IRepository<T, PK> {

    /**
     * 根据PageModel中的字段属性上的注解来自动生成查询的sql
     * PageModel为T的子类，作为补充表实体中有些冗余的字段
     *
     * @param page
     * @param clazz
     * @param tableQuery
     * @param <PageModel>
     * @return
     */
    <PageModel extends T> Page<PageModel> findPagination(Page<PageModel> page, Class<PageModel> clazz, TableQuery tableQuery);

    /**
     * 当自动sql还需要添加额外的查询时，使用此方法即可
     *
     * @param pageExtra
     * @param page
     * @param clazz
     * @param tableQuery
     * @param <PageModel>
     * @return
     */
    <PageModel extends T> Page<PageModel> findPagination(IPageExtra pageExtra, Page<PageModel> page, Class<PageModel> clazz, TableQuery tableQuery);

    /**
     * 当自动生成的sql不能满足的时候，请使用此方法的自定义的sql
     *
     * @param sql
     * @param page
     * @param clazz
     * @param tableQuery
     * @param <PageModel>
     * @return
     */
    <PageModel extends T> Page<PageModel> findPagination(String sql, Page<PageModel> page, Class<PageModel> clazz, TableQuery tableQuery);



}
