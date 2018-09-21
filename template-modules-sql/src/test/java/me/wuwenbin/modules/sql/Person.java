package me.wuwenbin.modules.sql;

import me.wuwenbin.modules.sql.annotation.SQLColumn;
import me.wuwenbin.modules.sql.annotation.SQLMappedSuper;
import me.wuwenbin.modules.sql.annotation.not.NotInsert;

/**
 * created by Wuwenbin on 2017/8/16 at 14:10
 */
@SQLMappedSuper(false)
public class Person extends Human {
    private String age;
    @SQLColumn(select = false)
    @NotInsert
    private String address;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
