package me.wuwenbin.modules.repodata.api.open;


import me.wuwenbin.modules.repodata.annotation.type.DataRepo;

/**
 * created by Wuwenbin on 2017/10/29 at 11:12
 */
@DataRepo
public interface IPageAndSortDataRepo<T, PK> extends IBaseCrudDataRepo<T, PK> {
}
