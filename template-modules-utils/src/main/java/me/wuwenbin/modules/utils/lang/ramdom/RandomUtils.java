package me.wuwenbin.modules.utils.lang.ramdom;

import java.util.UUID;

/**
 * created by Wuwenbin on 2018/1/5 at 12:12
 *
 * @author wuwenbin
 */
public final class RandomUtils {

    /**
     * 清爽uuid，无中间线
     *
     * @return
     */
    public String uuidCool() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 完整uuid，带中间线
     *
     * @return
     */
    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
