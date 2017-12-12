package me.wuwenbin.modules.sql;


import me.wuwenbin.modules.sql.annotation.SQLColumn;
import me.wuwenbin.modules.sql.annotation.SQLTable;
import me.wuwenbin.modules.sql.annotation.not.NotInsert;
import me.wuwenbin.modules.sql.annotation.not.NotSelect;
import me.wuwenbin.modules.sql.constant.Router;

/**
 * Created by wuwenbin on 2017/7/8.
 */
@SQLTable
public class User extends Person {

    @SQLColumn(pk = true)
    private Long id;

    @SQLColumn(routers = Router.B, update = false)
    private String username;

    @SQLColumn(routers = Router.B)
    @NotSelect
    private String password;

    @SQLColumn(routers = Router.C)
    private String cnName;

    @NotInsert
    private String email;

    private Long deptId;
    private Long roleId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
