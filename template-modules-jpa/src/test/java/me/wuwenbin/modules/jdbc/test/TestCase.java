package me.wuwenbin.modules.jdbc.test;

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
    private PublicService publicService;

    @Test
    public void testJDBC() {
//        System.out.println(publicService.templateItemsOauth2());
//        System.out.println(publicService.campusTalk());
        publicService.sout();
    }
}
