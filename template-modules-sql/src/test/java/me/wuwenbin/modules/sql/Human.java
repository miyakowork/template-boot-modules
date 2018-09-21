package me.wuwenbin.modules.sql;

import me.wuwenbin.modules.sql.annotation.MappedSuper;
import me.wuwenbin.modules.sql.annotation.SQLMappedSuper;

/**
 * created by Wuwenbin on 2017/12/12 at 12:01
 */
@SQLMappedSuper
public class Human {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
