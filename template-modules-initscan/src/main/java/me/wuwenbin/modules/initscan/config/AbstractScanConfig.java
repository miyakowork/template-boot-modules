package me.wuwenbin.modules.initscan.config;

import me.wuwenbin.modules.initscan.enumerate.ScanType;

/**
 * created by Wuwenbin on 2017/9/19 at 22:49
 */
public abstract class AbstractScanConfig implements IScanConfig {

    /**
     * 扫描资源的类型
     *
     * @return
     */
    @Override
    public ScanType getType() {
        return ScanType.CREATE;
    }

}
