package me.wuwenbin.modules.utils.security.factory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * url解码/编码
 * Created by wuwenbin on 2017/2/15.
 *
 * @author wuwenbin
 * @since 1.0.
 */
public class UrlFactory {

    /**
     * url解码,UTF-8
     *
     * @param input
     * @return
     */
    public String urlDecode(String input) {
        return urlDecode(input, Charset.defaultCharset().displayName());
    }

    /**
     * URL解码
     *
     * @param input
     * @param encoding
     * @return
     */
    public String urlDecode(String input, String encoding) {
        try {
            return URLDecoder.decode(input, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }

    /**
     * URL编码,UTF-8模式
     *
     * @param input
     * @return
     */
    public String urlEncode(String input) {
        return urlEncode(input, Charset.defaultCharset().displayName());
    }

    /**
     * URL编码
     *
     * @param input
     * @param encoding
     * @return
     */
    public String urlEncode(String input, String encoding) {
        try {
            return URLEncoder.encode(input, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }
}
