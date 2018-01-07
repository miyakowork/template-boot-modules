package me.wuwenbin.modules.utils.http;

import me.wuwenbin.modules.utils.lang.LangUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Wuwenbin on 2018/1/5 at 19:51
 */
public class HttpUtils {

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
}
