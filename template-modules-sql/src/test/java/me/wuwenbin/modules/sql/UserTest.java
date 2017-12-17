package me.wuwenbin.modules.sql;


import me.wuwenbin.modules.sql.constant.Router;
import me.wuwenbin.modules.sql.factory.SQLBeanBuilder;
import me.wuwenbin.modules.sql.factory.SQLTextBuilder;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created by wuwenbin on 2017/7/8.
 */
public class UserTest {

    @Test
    public void main() {
        SQLBeanBuilder sbb = SQLGen.builder(User.class);
        SQLTextBuilder ssb = SQLGen.builder();
//        String sql = sbb.insertAllWithoutPk();
        String sql = sbb.selectPartByRoutersAnd(new int[]{Router.DEFAULT});
        System.out.println(sql);
        Field[] fields = sbb.getAllFieldExceptObject();
//        for (Field field : fields) {
//            System.out.println(field.getName());
//        }
    }

}
