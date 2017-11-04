package me.wuwenbin.modules.repodata.test.proxy;

/**
 * created by Wuwenbin on 2017/10/30 at 10:13
 */
public interface PublicService<T> {
    int test();

    T saveTest(String... entities);
}
