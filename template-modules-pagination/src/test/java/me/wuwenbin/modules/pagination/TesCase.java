package me.wuwenbin.modules.pagination;

import org.junit.Test;

/**
 * created by Wuwenbin on 2017/8/30 at 22:46
 */
public class TesCase {

    @Test
    public void testPage()   {
        String sql="select * from a left join b on a.b_id = b.id where a.n = 2";
        TestBo testBo = new TestBo();
        testBo.setLoginDate("2015-07-08 11:22:33");
        testBo.setRegDate("2015-08-09 12:02:33 ~ 2016-01-25 16:42:53");
        testBo.setUsername("wuw");
        testBo.setLimit(30);
        testBo.setMultiSort("id,asc;username,desc");
        testBo.setSort("name");
        testBo.setOrder("desc");
        testBo.setOffset(60);
        System.out.println(Pagination.getSql(sql,testBo));
        System.out.println(Pagination.getParamsMap(testBo));
    }
}
