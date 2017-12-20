package me.wuwenbin.modules.utils;


import me.wuwenbin.modules.utils.http.R;
import me.wuwenbin.modules.utils.lang.Maps;
import me.wuwenbin.modules.utils.rest.Rests;
import org.junit.Test;
import org.nutz.dao.entity.annotation.Id;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * created by Wuwenbin on 2017/12/19 at 10:45
 */
public class Testx {


    public static void main(String[] args) throws NoSuchFieldException {
        Field field = Testx.class.getDeclaredField("id");

        System.out.println(field.isAnnotationPresent(Id.class));
        Map<String, Object> map = Maps.hashMap("k1", 1, "k2", "ssas1", "ksss", 2);
        for (String key : map.keySet()) {
            System.out.println("key:" + key + ",value:" + map.get(key));
        }
    }

    private int addUser(String mss) throws Exception {
        if (!mss.equals("a")) {
            throw new Exception("测试错误！");
        }
        return 1;
    }

    private boolean prev(boolean f) {
        return f;
    }

    @Test
    public void testRests() {
//        R r = Rests.builder("添加用户").exec(() -> addUser("aa") > 1);
//        System.out.println(r);
        R r1 = Rests.builder("添加用户").exec(() -> false, () -> addUser("a") == 1, () -> R.ok("啦啦啦啦"));
        System.out.println(r1);
    }

}
