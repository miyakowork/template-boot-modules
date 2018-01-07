package me.wuwenbin.modules.scanner.persistence;

/**
 * 扫描之后对数据库的操作方法接口
 * created by Wuwenbin on 2017/9/19 at 21:00
 *
 * @author wuwenbin
 */
public interface ScannerRepository {

    /**
     * 插入资源
     *
     * @param name
     * @param url
     * @param permissionMark
     * @param enabled
     * @param orderIndex
     * @param systemCode
     * @param remark
     * @throws Exception
     */
    long insertResources(String name, String url, String permissionMark, boolean enabled, int orderIndex, String systemCode, String remark) throws Exception;

    /**
     * 删除资源
     *
     * @param sysModuleCode
     * @throws Exception
     */
    void deleteResources(String sysModuleCode) throws Exception;

    /**
     * 资源是否已存在，根据url和系统代码判断
     *
     * @param url
     * @param sysModuleCode
     * @return
     */
    boolean isResourceExist(String url, String sysModuleCode);

    /**
     * 删除role_resource表中的记录关系对照
     *
     * @param roleId
     * @param systemCode
     * @param url
     * @throws Exception
     */
    void deleteRoleResource(long roleId, String systemCode, String url) throws Exception;

    /**
     * 插入role_resource对照关系记录到表中
     *
     * @param roleId
     * @param resourceId
     * @throws Exception
     */
    void insertRoleResource(long roleId, long resourceId) throws Exception;
}
