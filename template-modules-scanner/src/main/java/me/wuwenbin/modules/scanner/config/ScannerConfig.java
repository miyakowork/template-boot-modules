package me.wuwenbin.modules.scanner.config;

import me.wuwenbin.modules.scanner.enumerate.ScannerType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 扫描配置类
 * created by Wuwenbin on 2017/9/19 at 16:00
 *
 * @author wuwenbin
 */
public class ScannerConfig {

    /**
     * 扫描类型
     */
    private ScannerType scannerType;

    /**
     * 系统代码
     */
    private String systemModuleCode;

    /**
     * 角色id
     */
    private List<Long> roleIds;


    public ScannerType getScannerType() {
        return scannerType;
    }

    public void setScannerType(ScannerType scannerType) {
        this.scannerType = scannerType;
    }

    public String getSystemModuleCode() {
        return systemModuleCode;
    }

    public void setSystemModuleCode(String systemModuleCode) {
        this.systemModuleCode = systemModuleCode;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long... roleIds) {
        this.roleIds = Arrays.asList(roleIds);
    }

    public void setRoleIds(String roleIdStr) {
        List<Long> roleIds = new ArrayList<>();
        Arrays.stream(roleIdStr.split(",")).forEach(roleId -> roleIds.add(Long.valueOf(roleId)));
        this.roleIds = roleIds;
    }
}
