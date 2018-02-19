package me.wuwenbin.modules.repository.provider.select.page.support;

import me.wuwenbin.modules.repository.provider.select.page.support.fake.column.FakeColPageExtra;

/**
 * created by Wuwenbin on 2018/2/7 at 13:06
 */
public final class PageExtraFactory {

    @SuppressWarnings("unchecked")
    public static <T extends AbstractPageExtra> T getPageExtra(Class<T> clazz, Object... args) {
        if (clazz.equals(FakeColPageExtra.class)) {
            if (args != null) {
                if (args.length == 1) {
                    return (T) new FakeColPageExtra(args[0].toString(), "");
                } else {
                    return (T) new FakeColPageExtra(args[0].toString(), args[1].toString());
                }
            } else {
                throw new RuntimeException("参数不能为空");
            }
        }
        throw new RuntimeException("未找到符合的 PageExtra");
    }

    /**
     * 计算出最终的sql语句，此处仅有伪列的功能，其余功能后续版本跟进
     *
     * @param pageExtra
     * @param sql
     * @return
     */
    public static String finalSql(IPageExtra pageExtra, String sql) {
        String finalSql = sql;
        if (pageExtra.isSupportFakeColumn()) {
            finalSql = pageExtra.fakeColumnDeal().apply(finalSql);
        }
        return finalSql;
    }
}
