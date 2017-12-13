package me.wuwenbin.modules.jdbc.test;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.jpa.factory.DaoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/9/4 at 23:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:demo.xml"})
public class TestCase {

//    @Autowired
//    private PublicService publicService;


    AncestorDao dao;

    @Autowired
    public void setDao(DaoFactory daoFactory) {
        this.dao = daoFactory.dynamicDao;
    }

    @Test
    public void testJDBC() {
//        System.out.println(publicService.templateItemsOauth2());
//        System.out.println(publicService.campusTalk());
//        publicService.sout();
        String sql = "select addr from t_company where scale like :scale";
//        List<String> r = dao.getJdbcTemplateObj().queryForList(sql, String.class, " 规模：1000-5000人");
        Map<String, Object> m = new HashMap<>();
        m.put("scale", " 规模：%");
//        String r1 = dao.getJdbcTemplateObj().queryForObject(sql,String.class," 规模：");
//        r.forEach(System.out::println);
//        System.out.println(r1);
        List<String> r2 = dao.getNamedParameterJdbcTemplateObj().queryForList(sql, m, String.class);
        r2.forEach(System.out::println);
    }
}
