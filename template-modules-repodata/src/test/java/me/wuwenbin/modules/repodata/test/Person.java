package me.wuwenbin.modules.repodata.test;

import me.wuwenbin.tools.sqlgen.annotation.MappedSuper;
import me.wuwenbin.tools.sqlgen.annotation.SQLColumn;
import me.wuwenbin.tools.sqlgen.annotation.not.NotInsert;

/**
 * created by Wuwenbin on 2017/8/16 at 14:10
 */
@MappedSuper
public class Person {
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
