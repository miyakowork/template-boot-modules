package me.wuwenbin.modules.sql;


import me.wuwenbin.modules.sql.constant.Router;
import me.wuwenbin.modules.sql.factory.SQLBeanBuilder;
import me.wuwenbin.modules.sql.support.Symbol;
import org.junit.Test;

/**
 * Created by wuwenbin on 2017/7/8.
 */
public class UserTest {

    @Test
    public void main() {
        SQLBeanBuilder sbb = SQLGen.builder(User.class);
//        SQLTextBuilder ssb = SQLGen.builder();
//        String sql = sbb.insertAllWithoutPk();
//        String sql = sbb.selectPartByRoutersAnd(Symbol.COLON, new int[]{Router.DEFAULT}, Router.B);
//        System.out.println(sql);
//        Field[] fields = sbb.getAllFieldExceptObject();
//        System.out.println(sbb.getPkField());
//        for (Field field : fields) {
//            System.out.println(field.getName());
//        }
//        System.out.println(sbb.updateRoutersByPk(Symbol.QUESTION_MARK, Router.DEFAULT));
//        System.out.println(sbb.insertAllWithoutPk(Symbol.QUESTION_MARK));
        System.out.println(sbb.insertRoutersWithoutPk(Symbol.COLON,Router.DEFAULT));
    }

}
