package me.wuwenbin.modules.jdbc.test;

import me.wuwenbin.modules.jpa.ancestor.AncestorDao;
import me.wuwenbin.modules.jpa.annotation.DynamicDataSource;
import me.wuwenbin.modules.jpa.factory.DaoFactory;
import me.wuwenbin.modules.jpa.util.Calls;
import me.wuwenbin.modules.jpa.util.ReObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/11/29 at 17:26
 */
@Service
public class PublicService {


    private AncestorDao dao;

    @Autowired
    public void setAncestorDao(DaoFactory daoFactory) {
        this.dao = daoFactory.dynamicDao;
    }

    @Autowired
    private DaoFactory daoFactory;

    @DynamicDataSource("ct")
    public List<Map<String, Object>> campusTalk() {
        System.out.println("------------- campusTalk -----------------");
        String sql = "select * from t_user";
//        return dao.findListMapByArray(sql);
        return daoFactory.dynamicDao.findListMapByArray(sql);
    }

    @DynamicDataSource("tp")
    public List<Map<String, Object>> templateItemsOauth2() {
        System.out.println("------------- templateItemOauth2 -----------------");
        String sql = "select * from t_oauth_user";
//        return dao.findListMapByArray(sql);
        return daoFactory.dynamicDao.findListMapByArray(sql);
    }

    @DynamicDataSource("tp")
    public List<Map<String, Object>> test(String sql) {
        return daoFactory.dynamicDao.findListMapByArray(sql);
    }

    public void sout() {
//        PublicService publicService = ((PublicService) AopContext.currentProxy());
        System.out.println(Calls.get(PublicService::templateItemsOauth2));
        List<Map<String, Object>> r = Calls.get((ReObject<PublicService, List<Map<String, Object>>>) bean -> bean.test("select * from t_oauth_role"));
        System.out.println(r);
        System.out.println(Calls.get(PublicService::campusTalk));
//        System.out.println(publicService.templateItemsOauth2());
//        System.out.println(publicService.campusTalk());
    }
}
