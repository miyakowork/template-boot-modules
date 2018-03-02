package me.wuwenbin.modules.repository.api.open.crud;

import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;

import java.util.Map;

/**
 * created by Wuwenbin on 2018/2/20 at 13:53
 */
@Repository
public interface IUpdateRepository<T, PK> extends IRepository<T, PK> {

    //==============================修改操作  开始==============================//

    int update(T t) throws Exception;

    int update(Map<String, Object> map) throws Exception;

    //更复杂的方法请自行根据项目的需求而自定义方法即可，详情请参照命名规则
    //==============================修改操作  结束==============================//
}
