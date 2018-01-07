package me.wuwenbin.modules.utils.bean;

import java.util.Arrays;

/**
 * created by Wuwenbin on 2017/12/26 at 14:53
 */
public final class ArrayConverts {

    /**
     * String数组转换为double类型数组
     *
     * @param strArray
     * @return
     */
    public static double[] toDoubleArray(String[] strArray) {
        return Arrays.stream(strArray).mapToDouble(Double::valueOf).toArray();
    }

    /**
     * String数组转换为int类型数组
     *
     * @param strArray
     * @return
     */
    public static int[] toIntegerArray(String[] strArray) {
        return Arrays.stream(strArray).mapToInt(Integer::valueOf).toArray();
    }

    /**
     * String数组转换为int类型数组
     *
     * @param strArray
     * @return
     */
    public static long[] toLongArray(String[] strArray) {
        return Arrays.stream(strArray).mapToLong(Long::valueOf).toArray();
    }


}
