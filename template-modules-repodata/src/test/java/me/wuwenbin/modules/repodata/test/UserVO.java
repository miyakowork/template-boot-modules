package me.wuwenbin.modules.repodata.test;

import me.wuwenbin.modules.repodata.annotation.sql.SQLPkRefer;
import me.wuwenbin.modules.repodata.annotation.sql.SQLRefer;

/**
 * created by Wuwenbin on 2017/10/27 at 11:49
 */
public class UserVO extends User {

    @SQLRefer(targetClass = Role.class, targetColumn = "name", column = "role_id", referColumn = "id")
    private String defaultRoleName;
    @SQLPkRefer(targetClass = Department.class, targetColumn = "name", column = "dept_id")
    private String deptName;

    public String getDefaultRoleName() {
        return defaultRoleName;
    }

    public void setDefaultRoleName(String defaultRoleName) {
        this.defaultRoleName = defaultRoleName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

}
