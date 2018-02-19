package me.wuwenbin.modules.utils.lang.string;

import me.wuwenbin.modules.utils.lang.LangUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * created by Wuwenbin on 2018/1/4 at 11:14
 *
 * @author wuwenbin
 */
public final class StringUtils {

    private static final String EMPTY_JSON = "{}";
    private static final String UNDERLINE = "_";
    private static final char C_BACKSLASH = '\\';
    private static final char C_DELIMITER_START = '{';


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
     * 是否包含空字符串
     *
     * @param strings 字符串列表
     * @return 是否包含空字符串
     */
    public boolean hasEmpty(CharSequence... strings) {
        if (strings == null || strings.length == 0)
            return true;
        for (CharSequence str : strings) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否全部为空字符串
     *
     * @param strings 字符串列表
     * @return 是否全部为空字符串
     */
    public boolean isAllEmpty(CharSequence... strings) {
        if (strings == null || strings.length == 0)
            return true;
        for (CharSequence str : strings) {
            if (isNotEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public boolean isNotEmpty(CharSequence str) {
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


    /**
     * 是否包含空字符串
     *
     * @param strings 字符串列表
     * @return 是否包含空字符串
     */
    public boolean hasBlank(CharSequence... strings) {
        if (strings == null || strings.length == 0)
            return true;
        for (CharSequence str : strings) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 给定所有字符串是否为空白
     *
     * @param strings 字符串
     * @return 所有字符串是否为空白
     */
    public boolean isAllBlank(CharSequence... strings) {
        if (strings == null || strings.length == 0)
            return true;
        for (CharSequence str : strings) {
            if (isNotBlank(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 除去字符串头尾部的空白，如果字符串是<code>null</code>，依然返回<code>null</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * trim(null)          = null
     * trim(&quot;&quot;)            = &quot;&quot;
     * trim(&quot;     &quot;)       = &quot;&quot;
     * trim(&quot;abc&quot;)         = &quot;abc&quot;
     * trim(&quot;    abc    &quot;) = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public String trim(String str) {
        return (null == str) ? null : trim(str, 0);
    }

    /**
     * 给定字符串数组全部做去首尾空格
     *
     * @param strs 字符串数组
     */
    public void trim(String[] strs) {
        if (null == strs) {
            return;
        }
        String str;
        for (int i = 0; i < strs.length; i++) {
            str = strs[i];
            if (null != str) {
                strs[i] = str.trim();
            }
        }
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
    public String trimStart(String str) {
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
    public String trimEnd(String str) {
        return trim(str, 1);
    }

    /**
     * 除去字符串头尾部的空白符，如果字符串是<code>null</code>，依然返回<code>null</code>。
     *
     * @param str  要处理的字符串
     * @param mode <code>-1</code>表示trimStart，<code>0</code>表示trim全部， <code>1</code>表示trimEnd
     * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public String trim(String str, int mode) {
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

    /**
     * 移除字符串中所有给定字符串<br>
     * 例：removeAll("aa-bb-cc-dd", "-")  ->  aabbccdd
     *
     * @param str         字符串
     * @param strToRemove 被移除的字符串
     * @return 移除后的字符串
     */
    public String removeAll(String str, CharSequence strToRemove) {
        return str.replace(strToRemove, "");
    }


    /**
     * 清理空白字符
     *
     * @param str 被清理的字符串
     * @return 清理后的字符串
     */
    public String cleanBlank(String str) {
        if (str == null) {
            return null;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        char c;
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合
     */
    public String[] splitToArray(String str, char separator) {
        List<String> result = split(str, separator);
        return result.toArray(new String[result.size()]);
    }

    /**
     * 切分字符串<br>
     * a#b#c -> [a,b,c] <br>
     * a##b#c -> [a,"",b,c]
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合
     */
    public List<String> split(String str, char separator) {
        return split(str, separator, 0);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数
     * @return 切分后的集合
     */
    public String[] splitToArray(String str, char separator, int limit) {
        List<String> result = split(str, separator, limit);
        return result.toArray(new String[result.size()]);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数
     * @return 切分后的集合
     */
    public List<String> split(String str, char separator, int limit) {
        if (str == null) {
            return null;
        }
        List<String> list = new ArrayList<String>(limit == 0 ? 16 : limit);
        if (limit == 1) {
            list.add(str);
            return list;
        }

        boolean isNotEnd = true; // 未结束切分的标志
        int strLen = str.length();
        StringBuilder sb = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            char c = str.charAt(i);
            if (isNotEnd && c == separator) {
                list.add(sb.toString());
                // 清空StringBuilder
                sb.delete(0, sb.length());

                // 当达到切分上限-1的量时，将所剩字符全部作为最后一个串
                if (limit != 0 && list.size() == limit - 1) {
                    isNotEnd = false;
                }
            } else {
                sb.append(c);
            }
        }
        list.add(sb.toString());// 加入尾串
        return list;
    }

    /**
     * 给定字符串是否被字符包围
     *
     * @param str    字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 是否包围，空串不包围
     */
    public boolean isSurround(CharSequence str, char prefix, char suffix) {
        return !isBlank(str) && str.length() >= 2 && str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix;
    }

    /**
     * 重复某个字符
     *
     * @param c     被重复的字符
     * @param count 重复的数目
     * @return 重复字符字符串
     */
    public String repeat(char c, int count) {
        char[] result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }

    /**
     * 重复某个字符串
     *
     * @param str   被重复的字符
     * @param count 重复的数目
     * @return 重复字符字符串
     */
    public String repeat(String str, int count) {

        // 检查
        final int len = str.length();
        final long longSize = (long) len * (long) count;
        final int size = (int) longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
        }

        final char[] array = new char[size];
        str.getChars(0, len, array, 0);
        int n;
        for (n = len; n < size - n; n <<= 1) {// n <<= 1相当于n *2
            System.arraycopy(array, 0, array, n, n);
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }


    /**
     * 有序的格式化文本，使用{number}做为占位符<br>
     * 例：<br>
     * 通常使用：format("this is {0} for {1}", "a", "b") -> this is a for b<br>
     *
     * @param pattern   文本格式
     * @param arguments 参数
     * @return 格式化后的文本
     */
    public String indexedFormat(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public String format(String template, Object... params) {
        if (params == null || params.length == 0 || isBlank(template)) {
            return template;
        }
        return placeholder(template, params);
    }

    /**
     * 格式化文本，使用 {varName} 占位<br>
     * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ----> aValue and bValue
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param map      参数值对
     * @return 格式化后的文本
     */
    public String format(String template, Map<?, ?> map) {
        if (null == map || map.isEmpty()) {
            return template;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", utf8Str(entry.getValue()));
        }
        return template;
    }

    /**
     * 编码字符串，编码为UTF-8
     *
     * @param str 字符串
     * @return 编码后的字节码
     */
    public byte[] utf8Bytes(String str) {
        return bytes(str, StandardCharsets.UTF_8);
    }

    /**
     * 编码字符串<br>
     * 使用系统默认编码
     *
     * @param str 字符串
     * @return 编码后的字节码
     */
    public byte[] bytes(String str) {
        return bytes(str, Charset.defaultCharset());
    }

    /**
     * 编码字符串
     *
     * @param str     字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public byte[] bytes(String str, String charset) {
        return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 编码字符串
     *
     * @param str     字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public byte[] bytes(String str, Charset charset) {
        if (str == null) {
            return null;
        }

        if (null == charset) {
            return str.getBytes();
        }
        return str.getBytes(charset);
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    public String utf8Str(Object obj) {
        return str(obj, StandardCharsets.UTF_8);
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj         对象
     * @param charsetName 字符集
     * @return 字符串
     */
    public String str(Object obj, String charsetName) {
        return str(obj, Charset.forName(charsetName));
    }

    /**
     * 将对象转为字符串<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj     对象
     * @param charset 字符集
     * @return 字符串
     */
    public String str(Object obj, Charset charset) {
        if (null == obj)
            return null;
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return str((byte[]) obj, charset);
        } else if (obj instanceof Byte[]) {
            return str((Byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer) obj, charset);
        } else if (obj.getClass().isArray()) {
            try {
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception e) {
                final String className = obj.getClass().getComponentType().getName();
                switch (className) {
                    case "long":
                        return Arrays.toString((long[]) obj);
                    case "int":
                        return Arrays.toString((int[]) obj);
                    case "short":
                        return Arrays.toString((short[]) obj);
                    case "char":
                        return Arrays.toString((char[]) obj);
                    case "byte":
                        return Arrays.toString((byte[]) obj);
                    case "boolean":
                        return Arrays.toString((boolean[]) obj);
                    case "float":
                        return Arrays.toString((float[]) obj);
                    case "double":
                        return Arrays.toString((double[]) obj);
                    default:
                        throw new RuntimeException(e);
                }
            }
        }

        return obj.toString();
    }

    /**
     * 将byte数组转为字符串
     *
     * @param bytes   byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public String str(byte[] bytes, String charset) {
        return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 解码字节码
     *
     * @param data    字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * 将Byte数组转为字符串
     *
     * @param bytes   byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public String str(Byte[] bytes, String charset) {
        return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 解码字节码
     *
     * @param data    字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public String str(Byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        byte[] bytes = new byte[data.length];
        Byte dataByte;
        for (int i = 0; i < data.length; i++) {
            dataByte = data[i];
            bytes[i] = (null == dataByte) ? -1 : dataByte;
        }

        return str(bytes, charset);
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public String str(ByteBuffer data, String charset) {
        if (data == null) {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public String str(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    /**
     * 字符串转换为byteBuffer
     *
     * @param str     字符串
     * @param charset 编码
     * @return byteBuffer
     */
    public ByteBuffer byteBuffer(String str, String charset) {
        return ByteBuffer.wrap(bytes(str, charset));
    }


    /**
     * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->hello_world
     *
     * @param camelCaseStr 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }

        final int length = camelCaseStr.length();
        StringBuilder sb = new StringBuilder();
        char c;
        boolean isPreUpperCase = false;
        for (int i = 0; i < length; i++) {
            c = camelCaseStr.charAt(i);
            boolean isNextUpperCase = true;
            if (i < (length - 1)) {
                isNextUpperCase = Character.isUpperCase(camelCaseStr.charAt(i + 1));
            }
            if (Character.isUpperCase(c)) {
                if (!isPreUpperCase || !isNextUpperCase) {
                    if (i > 0) sb.append(UNDERLINE);
                }
                isPreUpperCase = true;
            } else {
                isPreUpperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
     * 例如：hello_world->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public String toCamelCase(String name) {
        if (name == null) {
            return null;
        }
        if (name.contains(UNDERLINE)) {
            name = name.toLowerCase();

            StringBuilder sb = new StringBuilder(name.length());
            boolean upperCase = false;
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);

                if (c == '_') {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else
            return name;
    }

    /**
     * 统计指定内容中包含指定字符串的数量<br>
     * 参数为 {@code null} 或者 "" 返回 {@code 0}.
     * <p>
     * <pre>
     * StringX.count(null, *)       = 0
     * StringX.count("", *)         = 0
     * StringX.count("abba", null)  = 0
     * StringX.count("abba", "")    = 0
     * StringX.count("abba", "a")   = 2
     * StringX.count("abba", "ab")  = 1
     * StringX.count("abba", "xxx") = 0
     * </pre>
     *
     * @param content      被查找的字符串
     * @param strForSearch 需要查找的字符串
     * @return 查找到的个数
     */
    public int count(final String content, final String strForSearch) {
        if (hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
            return 0;
        }

        int count = 0;
        int idx = 0;
        while ((idx = content.indexOf(strForSearch, idx)) > -1) {
            count++;
            idx += strForSearch.length();
        }
        return count;
    }

    /**
     * 统计指定内容中包含指定字符的数量
     *
     * @param content       内容
     * @param charForSearch 被统计的字符
     * @return 包含数量
     */
    public int count(String content, char charForSearch) {
        int count = 0;
        if (isEmpty(content)) {
            return 0;
        }
        int contentLength = content.length();
        for (int i = 0; i < contentLength; i++) {
            if (charForSearch == content.charAt(i)) {
                count++;
            }
        }
        return count;
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
    public String placeholder(final String needReplaceString, final Object... argArray) {
        if (LangUtils.string.isBlank(needReplaceString) || (argArray == null || argArray.length == 0)) {
            return needReplaceString;
        }
        final int strPatternLength = needReplaceString.length();

        //初始化定义好的长度以获得更好的性能
        StringBuilder sb = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;//记录已经处理到的位置
        int delimiterIndex;//占位符所在位置
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            delimiterIndex = needReplaceString.indexOf(EMPTY_JSON, handledPosition);
            if (delimiterIndex == -1) {//剩余部分无占位符
                if (handledPosition == 0) { //不带占位符的模板直接返回
                    return needReplaceString;
                } else { //字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                    sb.append(needReplaceString, handledPosition, strPatternLength);
                    return sb.toString();
                }
            } else {
                if (delimiterIndex > 0 && needReplaceString.charAt(delimiterIndex - 1) == C_BACKSLASH) {//转义符
                    if (delimiterIndex > 1 && needReplaceString.charAt(delimiterIndex - 2) == C_BACKSLASH) {//双转义符
                        //转义符之前还有一个转义符，占位符依旧有效
                        sb.append(needReplaceString, handledPosition, delimiterIndex - 1);
                        sb.append(LangUtils.string.utf8Str(argArray[argIndex]));
                        handledPosition = delimiterIndex + 2;
                    } else {
                        //占位符被转义
                        argIndex--;
                        sb.append(needReplaceString, handledPosition, delimiterIndex - 1);
                        sb.append(C_DELIMITER_START);
                        handledPosition = delimiterIndex + 1;
                    }
                } else {//正常占位符
                    sb.append(needReplaceString, handledPosition, delimiterIndex);
                    sb.append(LangUtils.string.utf8Str(argArray[argIndex]));
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
     * 包装指定字符串
     *
     * @param str    被包装的字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 包装后的字符串
     */
    public String wrap(String str, String prefix, String suffix) {
        return format("{}{}{}", prefix, str, suffix);
    }

    /**
     * 统计汉子数目
     *
     * @param charSequence
     * @return
     */
    public int chineseWords(CharSequence charSequence) {
        if (charSequence == null) return 0;
        char[] t1 = charSequence.toString().toCharArray();
        int count = 0;
        for (char aT1 : t1) {
            if (Character.toString(aT1).matches("[\\u4E00-\\u9FA5]+")) {
                count++;
            }
        }
        return count;
    }

    /**
     * 判断全角的字符串，包括全角汉字以及全角标点
     *
     * @param charSequence
     * @return
     */
    public int fullAngelWords(CharSequence charSequence) {
        if (charSequence == null) return 0;
        char[] t1 = charSequence.toString().toCharArray();
        int count = 0;
        for (char aT1 : t1) {
            if (Character.toString(aT1).matches("[^\\x00-\\xff]")) {
                System.out.println(aT1);
                count++;
            }
        }
        return count;
    }
}
