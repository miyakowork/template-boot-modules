package me.wuwenbin.modules.initscan.persistence;

/**
 * 扫描之后对数据库的操作方法接口
 * created by Wuwenbin on 2017/9/19 at 21:00
 */
public interface IScan {

    void insertScanRes(String sql, String name, String url, String permissionMark, boolean enabled, int orderIndex, String systemCode, String remark) throws Exception;

    void updateScanRes(String sql, String sysModuleCode) throws Exception;

    void deleteBatchScanRes(String sql, String sysModuleCode) throws Exception;

    boolean isExistRes(String sql, String url, String sysModuleCode);
}
