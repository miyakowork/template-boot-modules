package me.wuwenbin.modules.repository.api.open;


import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;
import me.wuwenbin.modules.repository.api.open.crud.ICreateRepository;
import me.wuwenbin.modules.repository.api.open.crud.IDeleteRepository;
import me.wuwenbin.modules.repository.api.open.crud.IReadRepository;
import me.wuwenbin.modules.repository.api.open.crud.IUpdateRepository;

/**
 * created by Wuwenbin on 2017/10/28 at 10:44
 *
 * @author Wuwenbin
 */
@Repository
public interface IBaseCrudRepository<T, PK> extends ICreateRepository<T, PK>, IDeleteRepository<T, PK>, IReadRepository<T, PK>, IUpdateRepository<T, PK>, IRepository<T, PK> {

}
