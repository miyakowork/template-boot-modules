package me.wuwenbin.modules.sql.util;

import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

/**
 * some utils of sql building
 * <p>
 *
 * @author wuwenbin
 * @date 2017/1/9
 */
public class SQLDefineUtils {

    /**
     * 驼峰命名转下划线
     *
     * @param param to transfer
     * @return {@link String}
     */
    private static String camelToUnderline(String param) {
        if (param != null && !"".equals(param.trim())) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                char c = param.charAt(i);
                if (!Character.isUpperCase(c)) {
                    sb.append(c);
                } else {
                    sb.append('_').append(Character.toLowerCase(c));
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 下划线转驼峰命名
     *
     * @param param to transfer
     * @return {@link String}
     */
    public static String underline2Camel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder(param);
            Matcher mc = compile("_").matcher(param);
            int i = 0;
            while (mc.find()) {
                int position = mc.end() - (i++);
                sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
            }
            return sb.toString();
        }
    }

    /**
     * 如果用户指定了column的列名就按用户指定的来，没有则驼峰命名转化为下划线命名
     *
     * @param charName  the charName of db
     * @param fieldName the custom name of charName
     * @return {@link String}
     */
    public static String java2SQL(String charName, String fieldName) {
        if (charName == null || "".equals(charName)) {
            String newName = camelToUnderline(fieldName);
            if (!"_".equals(newName.substring(0, 1))) {
                return newName;
            } else {
                return newName.substring(1, newName.length());
            }
        } else {
            return charName;
        }

    }

}
