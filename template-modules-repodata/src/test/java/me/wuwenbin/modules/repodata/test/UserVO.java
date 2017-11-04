package me.wuwenbin.modules.repodata.test;

import me.wuwenbin.modules.repodata.annotation.field.SQLPkRefer;
import me.wuwenbin.modules.repodata.annotation.field.SQLRefer;

import java.util.List;

/**
 * created by Wuwenbin on 2017/10/27 at 11:49
 */
public class UserVO extends User {

    @SQLRefer(target = Role.class, column = "id")
    private Long roleName;
    @SQLPkRefer(target = Department.class)
    private Long deptName;

    private List<Role> roles;

    public Long getRoleName() {
        return roleName;
    }

    public void setRoleName(Long roleName) {
        this.roleName = roleName;
    }

    public Long getDeptName() {
        return deptName;
    }

    public void setDeptName(Long deptName) {
        this.deptName = deptName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
