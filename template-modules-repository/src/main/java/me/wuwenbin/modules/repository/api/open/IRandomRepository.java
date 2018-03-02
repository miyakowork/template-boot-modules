package me.wuwenbin.modules.repository.api.open;

import me.wuwenbin.modules.repository.annotation.type.Repository;
import me.wuwenbin.modules.repository.api.base.IRepository;

import java.util.List;

/**
 * 随机查询接口
 * Base结尾的表示仅仅使用rand() 函数作为随机条件，适用于少数记录的数据库
 * Effect结尾的随机查询效率较高
 * created by Wuwenbin on 2018/2/20 at 14:01
 */
@Repository
public interface IRandomRepository<T, PK> extends IRepository<T, PK> {

    /**
     * 随机一条数据
     *
     * @return
     */
    T randBase();

    /**
     * 随机N条数据
     *
     * @param randCount 随机记录数
     * @return
     */
    List<T> randBases(int randCount);

    /**
     * 随机一条数据
     *
     * @return
     */
    T randEffect();

    /**
     * 随机N条数据
     *
     * @param randCount
     * @return
     */
    List<T> randEffects(int randCount);


}
