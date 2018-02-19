package me.wuwenbin.modules.repository.provider.select.page.support;

import java.util.function.UnaryOperator;

/**
 * page分页查询额外的条件
 * created by Wuwenbin on 2018/2/7 at 12:13
 */
public interface IPageExtra {

    /**
     * 是否支持伪列查询
     *
     * @return
     */
    boolean isSupportFakeColumn();

    UnaryOperator<String> fakeColumnDeal();

}
