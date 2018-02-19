package me.wuwenbin.modules.pagination;

import me.wuwenbin.modules.pagination.query.model.bootstrap.BootstrapTableQuery;
import me.wuwenbin.modules.pagination.query.support.annotation.QueryColumn;
import me.wuwenbin.modules.pagination.query.support.annotation.QueryTable;
import me.wuwenbin.modules.pagination.query.support.operator.Operator;

/**
 * created by Wuwenbin on 2017/8/30 at 15:46
 */
@QueryTable(name = "t_system")
public class TestBo extends BootstrapTableQuery {

    private String username;

    @QueryColumn(operator = Operator.EQ, column = "l_d")
    private String loginDate;

    @QueryColumn(operator = Operator.BETWEEN_AND)
    private String regDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
