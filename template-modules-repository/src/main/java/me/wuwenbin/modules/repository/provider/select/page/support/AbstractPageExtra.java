package me.wuwenbin.modules.repository.provider.select.page.support;

import java.util.function.UnaryOperator;

/**
 * 如果只需某个模块可以继承此类
 * created by Wuwenbin on 2018/2/7 at 12:14
 */
public abstract class AbstractPageExtra implements IPageExtra {

    @Override
    public boolean isSupportFakeColumn() {
        return false;
    }

    @Override
    public UnaryOperator<String> fakeColumnDeal() {
        return null;
    }

}
