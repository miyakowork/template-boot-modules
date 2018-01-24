package me.wuwenbin.modules.utils.http;

import me.wuwenbin.modules.utils.lang.LangUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by Wuwenbin on 2018/1/5 at 19:51
 */
public class WebUtils {

    /**
     * 所有img标签的src属性
     */
    private static Pattern IMG_SRC = Pattern.compile("<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    /**
     * 所有以<开头,以>结尾的标签
     */
    private final static String regexpForHtml = "<([^>]*)>";

    /**
     * 判断是否为ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return LangUtils.string.isNotBlank(request.getHeader("x-requested-with")) && request.getHeader("x-requested-with").equals("XMLHttpRequest");
    }

    /**
     * 获取IP地址
     * <p>
     * 使用nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     *
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddress;
        try {
            remoteAddress = request.getHeader("x-forwarded-for");
            if (LangUtils.string.isEmpty(remoteAddress) || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("Proxy-Client-IP");
            }
            if (LangUtils.string.isEmpty(remoteAddress) || remoteAddress.length() == 0 || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (LangUtils.string.isEmpty(remoteAddress) || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (LangUtils.string.isEmpty(remoteAddress) || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (LangUtils.string.isEmpty(remoteAddress) || "unknown".equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            remoteAddress = request.getRemoteAddr();
        }
        return remoteAddress;
    }

    /**
     * 根据获取到的html得到所有包含&lt;img&gt;的文本 不包含标签，只得到img的src属性
     *
     * @param htmlTxt html文本内容
     * @return img的src属性
     */
    public static List<String> get(String htmlTxt) {
        Matcher matcher = IMG_SRC.matcher(htmlTxt);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (group == null) {
                continue;
            }
            // 这里可能还需要更复杂的判断,用以处理src="...."内的一些转义符
            if (group.startsWith("'")) {
                list.add(group.substring(1, group.indexOf("'", 1)));
            } else if (group.startsWith("\"")) {
                list.add(group.substring(1, group.indexOf("\"", 1)));
            } else {
                list.add(group.split("\\s")[0]);
            }
        }
        return list;
    }

    /**
     * 获取所有img地址，结果作为ListString集合
     * 根据获取到的html得到所有包含&lt;img&gt;的文本 不包含标签，只得到img的src属性
     *
     * @param htmlTxt 获取的html文本
     * @return
     */
    public static List<String> getImageList(String htmlTxt) {
        return get(htmlTxt);
    }

    /**
     * 获取第一个img的地址
     *
     * @param htmlTxt 获取的html文本
     * @return
     * @throws IndexOutOfBoundsException
     */
    public static String getFirstImage(String htmlTxt) throws IndexOutOfBoundsException {
        return getImageByIndex(htmlTxt, 0);
    }

    /**
     * 获取指定序号的img地址
     *
     * @param htmlTxt 获取的html文本
     * @param index   指定的序号
     * @return
     * @throws IndexOutOfBoundsException
     */
    public static String getImageByIndex(String htmlTxt, int index) throws IndexOutOfBoundsException {
        if (get(htmlTxt).size() <= index) {
            throw new IndexOutOfBoundsException("the index you custom is out of the image size");
        } else {
            return getImageList(htmlTxt).get(index);
        }
    }

    /**
     * 过滤所有以"<"开头，以">"结尾的标签
     *
     * @param str
     * @return
     */
    public static String filterHtml(String str) {
        Pattern pattern = Pattern.compile(regexpForHtml);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 过滤指定标签
     *
     * @param str
     * @param tag
     * @return
     */
    public static String fiterHtmlTag(String str, String tag) {
        String regexp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 替换指定的标签
     *
     * @param str
     * @param beforeTag    要替换的标签
     * @param tagAttribute 要替换的标签属性值
     * @param startTag     新标签开始标记
     * @param endTag       新标签结束标记
     * @return 替换img标签的src属性值为[img]属性值[/img]
     */
    public static String replaceHtmlTag(String str, String beforeTag, String tagAttribute, String startTag, String endTag) {
        String regexpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
        String regexpForTagAttribute = tagAttribute + "=\"([^\"]+)\"";
        Pattern patternForTag = Pattern.compile(regexpForTag);
        Pattern patternForAttribute = Pattern.compile(regexpForTagAttribute);
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbReplace = new StringBuffer();
            Matcher matcherForAttribute = patternForAttribute.matcher(matcherForTag.group(1));
            if (matcherForAttribute.find()) {
                matcherForAttribute.appendReplacement(sbReplace, startTag + matcherForAttribute.group(1) + endTag);
            }
            matcherForTag.appendReplacement(sb, sbReplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

}
