package me.wuwenbin.modules.jdbc.test;

import me.wuwenbin.modules.jpa.factory.DaoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * created by Wuwenbin on 2017/9/4 at 23:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:demo.xml"})
public class TestCase {


    @Autowired
    DaoFactory daoFactory;


    @Test
    public void testJDBC() {
        String sql = "SELECT count(0) FROM t_oauth_user";
        int a = daoFactory.defaultDao.queryNumberByArray(sql, Integer.class);
        System.out.println("====:" + a);
    }
}
