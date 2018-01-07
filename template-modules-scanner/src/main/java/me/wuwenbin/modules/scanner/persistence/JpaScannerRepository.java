package me.wuwenbin.modules.scanner.persistence;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.jpa.factory.DaoFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/12/26 at 12:11
 */
public class JpaScannerRepository implements ScannerRepository {

    private AncestorDao jdbcTemplate;

    public JpaScannerRepository(DaoFactory daoFactory) {
        this.jdbcTemplate = daoFactory.dynamicDao;
    }

    public AncestorDao getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(AncestorDao jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long insertResources(String name, String url, String permissionMark, boolean enabled, int orderIndex, String systemCode, String remark) throws Exception {
        Map<String, Object> paramMap = new HashMap<>(7);
        paramMap.put("name", name);
        paramMap.put("url", url);
        paramMap.put("permissionMark", permissionMark);
        paramMap.put("enabled", enabled);
        paramMap.put("orderIndex", orderIndex);
        paramMap.put("systemCode", systemCode);
        paramMap.put("remark", remark);
        String sql = "insert into t_oauth_resource(url,permission_mark,enabled,order_index,system_code,remark) values (:url,:permissionMark,:enabled,:orderIndex,:systemCode,:remark)";
        return jdbcTemplate.insertMapAutoGenKeyReturnKey(sql, paramMap);
    }

    @Override
    public void deleteResources(String sysModuleCode) throws Exception {
        String delSql = "delete from  t_oauth_resource where system_code = ?";
        jdbcTemplate.executeArray(delSql, sysModuleCode);
    }

    @Override
    public boolean isResourceExist(String url, String sysModuleCode) {
        String existSql = "select count(0) from t_oauth_resource where url = ? and system_code = ? ";
        return jdbcTemplate.queryNumberByArray(existSql, Long.class, url, sysModuleCode) >= 1;
    }

    @Override
    public void deleteRoleResource(long roleId, String systemCode, String url) throws Exception {
        String resSql = "select id from t_oauth_resource where url = ? and system_code = ?";
        String resId = jdbcTemplate.findPrimitiveByArray(resSql, String.class, url, systemCode);
        String delSql = "delete from t_oauth_role_resource where role_id = ? and resource_id = ?";
        jdbcTemplate.executeArray(delSql, roleId, resId);
    }

    @Override
    public void insertRoleResource(long roleId, long resourceId) throws Exception {
        String sql = "insert into t_oauth_role_resource(role_id,resource_id,enabled) values(?,?,1)";
        jdbcTemplate.executeArray(sql, roleId, resourceId);
    }
}
