package me.wuwenbin.modules.mongodb.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.templateproject.test.MongoService;

/**
 * Created by wuwenbin on 2017/4/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:demo.xml"})
public class MongoTest {


    @Autowired
    MongoService mongoService;

    @Test
    public void test() throws Exception {
//        System.err.println(mongoFactory.defaultMongoDao.getMongoTemplate().getDb());
//        List<Idea> idea = mongoFactory.defaultMongoDao.findListBean(Idea.class);
//        System.err.println(idea.toString());
//        mongoService.findAdmin();
//        mongoService.findIdea();
        mongoService.addTest();
    }


}
