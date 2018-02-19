package me.wuwenbin.modules.pagination.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * created by Wuwenbin on 2017/8/30 at 11:10
 */
public class StringUtils {

    /**
     * 驼峰命名转下划线
     *
     * @param param to transfer
     * @return {@link String}
     */
    private static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        } else {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append('_').append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
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
            if ("_".equals(newName.substring(0, 1))) {
                return newName.substring(1, newName.length());
            } else {
                return newName;
            }
        } else {
            return charName;
        }
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
    private static boolean isBlank(CharSequence str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check whether the given {@code String} is empty.
     * <p>This method accepts any Object as an argument, comparing it to
     * {@code null} and the empty String. As a consequence, this method
     * will never return {@code true} for a non-null non-String object.
     * <p>The Object signature is useful for general attribute handling code
     * that commonly deals with Strings but generally has to iterate over
     * Objects since attributes may e.g. be primitive value objects as well.
     *
     * @param str the candidate String
     * @since 3.2.1
     */
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    private static String utf8Str(Object obj) {
        return str(obj);
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    private static String str(Object obj) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return str(obj);
        } else if (obj instanceof Byte[]) {
            return str(obj);
        } else if (obj instanceof ByteBuffer) {
            return str(obj);
        } else if (obj.getClass().isArray()) {
            try {
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception e) {
                final String className = obj.getClass().getComponentType().getName();
                if ("long".equals(className)) {
                    return Arrays.toString((long[]) obj);
                } else if ("int".equals(className)) {
                    return Arrays.toString((int[]) obj);
                } else if ("short".equals(className)) {
                    return Arrays.toString((short[]) obj);
                } else if ("char".equals(className)) {
                    return Arrays.toString((char[]) obj);
                } else if ("byte".equals(className)) {
                    return Arrays.toString((byte[]) obj);
                } else if ("boolean".equals(className)) {
                    return Arrays.toString((boolean[]) obj);
                } else if ("float".equals(className)) {
                    return Arrays.toString((float[]) obj);
                } else if ("double".equals(className)) {
                    return Arrays.toString((double[]) obj);
                } else {
                    throw new RuntimeException(e);
                }
            }
        }

        return obj.toString();
    }

    /**
     * 不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： 	format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\：		format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param needReplaceString 字符串模板
     * @param argArray          参数列表
     * @return 结果
     */
    public static String format(final String needReplaceString, final Object... argArray) {
        if (isBlank(needReplaceString) || (argArray == null || argArray.length == 0)) {
            return needReplaceString;
        }
        final int strPatternLength = needReplaceString.length();

        //初始化定义好的长度以获得更好的性能
        StringBuilder sb = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;//记录已经处理到的位置
        int delimiterIndex;//占位符所在位置
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            delimiterIndex = needReplaceString.indexOf("{}", handledPosition);
            if (delimiterIndex == -1) {//剩余部分无占位符
                if (handledPosition == 0) { //不带占位符的模板直接返回
                    return needReplaceString;
                } else { //字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                    sb.append(needReplaceString, handledPosition, strPatternLength);
                    return sb.toString();
                }
            } else {
                if (delimiterIndex > 0 && needReplaceString.charAt(delimiterIndex - 1) == '\\') {//转义符
                    if (delimiterIndex > 1 && needReplaceString.charAt(delimiterIndex - 2) == '\\') {//双转义符
                        //转义符之前还有一个转义符，占位符依旧有效
                        sb.append(needReplaceString, handledPosition, delimiterIndex - 1);
                        sb.append(utf8Str(argArray[argIndex]));
                        handledPosition = delimiterIndex + 2;
                    } else {
                        //占位符被转义
                        argIndex--;
                        sb.append(needReplaceString, handledPosition, delimiterIndex - 1);
                        sb.append('{');
                        handledPosition = delimiterIndex + 1;
                    }
                } else {//正常占位符
                    sb.append(needReplaceString, handledPosition, delimiterIndex);
                    sb.append(utf8Str(argArray[argIndex]));
                    handledPosition = delimiterIndex + 2;
                }
            }
        }
        // append the characters following the last {} pair.
        //加入最后一个占位符后所有的字符
        sb.append(needReplaceString, handledPosition, needReplaceString.length());

        return sb.toString();
    }

    /**
     * 除去字符串头部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * trimStart(null)         = null
     * trimStart(&quot;&quot;)           = &quot;&quot;
     * trimStart(&quot;abc&quot;)        = &quot;abc&quot;
     * trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
     * trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
     * trimStart(&quot; abc &quot;)      = &quot;abc &quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回 <code>null</code>
     */
    public static String trimStart(String str) {
        return trim(str, -1);
    }

    /**
     * 除去字符串尾部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * trimEnd(null)       = null
     * trimEnd(&quot;&quot;)         = &quot;&quot;
     * trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
     * trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
     * trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
     * trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回 <code>null</code>
     */
    public static String trimEnd(String str) {
        return trim(str, 1);
    }

    /**
     * 除去字符串头尾部的空白符，如果字符串是<code>null</code>，依然返回<code>null</code>。
     *
     * @param str  要处理的字符串
     * @param mode <code>-1</code>表示trimStart，<code>0</code>表示trim全部， <code>1</code>表示trimEnd
     * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    private static String trim(String str, int mode) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        int start = 0;
        int end = length;

        // 扫描字符串头部
        if (mode <= 0) {
            while ((start < end) && (Character.isWhitespace(str.charAt(start)))) {
                start++;
            }
        }

        // 扫描字符串尾部
        if (mode >= 0) {
            while ((start < end) && (Character.isWhitespace(str.charAt(end - 1)))) {
                end--;
            }
        }

        if ((start > 0) || (end < length)) {
            return str.substring(start, end);
        }

        return str;
    }
}
