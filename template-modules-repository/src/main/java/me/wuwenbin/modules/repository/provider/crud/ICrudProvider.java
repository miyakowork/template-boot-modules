package me.wuwenbin.modules.repository.provider.crud;

/**
 * created by Wuwenbin on 2017/11/1 at 11:30
 *
 * @author Wuwenbin
 */
public interface ICrudProvider {

    /**
     * 接口核心方法。代理类执行接口中的方法
     *
     * @param args
     * @return
     * @throws Exception
     */
    Object execute(Object[] args) throws Exception;

}
