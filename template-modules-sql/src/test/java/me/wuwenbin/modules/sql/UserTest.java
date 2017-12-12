package me.wuwenbin.modules.sql;


import me.wuwenbin.modules.sql.factory.SQLBeanBuilder;
import me.wuwenbin.modules.sql.factory.SQLTextBuilder;
import org.junit.Test;

/**
 * Created by wuwenbin on 2017/7/8.
 */
public class UserTest {

    @Test
    public void main() {
        SQLBeanBuilder sbb = SQLGen.builder(User.class);
        SQLTextBuilder ssb = SQLGen.builder();
        String sql = sbb.insertAllWithoutPk();
//        sql = sbb.selectPartByPk(Router.B, Router.C);
        System.out.println(sql);
    }

}
