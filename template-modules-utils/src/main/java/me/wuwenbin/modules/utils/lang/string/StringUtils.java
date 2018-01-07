package me.wuwenbin.modules.utils.lang.string;

/**
 * created by Wuwenbin on 2018/1/4 at 11:14
 *
 * @author wuwenbin
 */
public final class StringUtils {

    /**
     * 查询第一个字符串不为空的并返回
     *
     * @param strings
     * @return
     */
    public String firstNotEmpty(String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        } else if (strings.length == 1) {
            return strings[0];
        } else {
            for (String s : strings) {
                if (s != null && !s.equals("")) {
                    return s;
                }
            }
            return "";
        }
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 字符串是否为空白 空白的定义如下： <br>
     * 1、为null <br>
     * 2、为不可见字符（如空格）<br>
     * 3、""<br>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public boolean isBlank(CharSequence str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (!Character.isWhitespace(str.charAt(i)) || Character.isSpaceChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否补位空字符串
     *
     * @param str
     * @return
     */
    public boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

}
