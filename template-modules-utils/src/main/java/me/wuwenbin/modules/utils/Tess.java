package me.wuwenbin.modules.utils;


import me.wuwenbin.modules.utils.lang.Maps;

import java.util.Map;

/**
 * created by Wuwenbin on 2017/12/19 at 10:45
 */
public class Tess {

    public static void main(String[] args) {
        Map<String, Object> map = Maps.hashMap("k1", 1, "k2", "ssas1", "ksss", 2);
        for (String key : map.keySet()) {
            System.out.println("key:" + key + ",value:" + map.get(key));
        }
    }

}
