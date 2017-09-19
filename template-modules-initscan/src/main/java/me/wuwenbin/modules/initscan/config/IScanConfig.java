package me.wuwenbin.modules.initscan.config;

import me.wuwenbin.modules.initscan.enumerate.ScanType;

/**
 * created by Wuwenbin on 2017/9/19 at 16:00
 */
public interface IScanConfig {

    /**
     * 扫描资源的类型
     *
     * @return
     */
    ScanType getType();

    /**
     * 资源所属系统的代号（因为肯能有多个系统）
     *
     * @return
     */
    String getSysModuleCode();
}
