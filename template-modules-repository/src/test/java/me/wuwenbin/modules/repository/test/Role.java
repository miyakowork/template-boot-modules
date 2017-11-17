package me.wuwenbin.modules.repository.test;

import me.wuwenbin.tools.sqlgen.annotation.SQLTable;

/**
 * created by Wuwenbin on 2017/10/6 at 10:59
 */
@SQLTable
public class Role {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
