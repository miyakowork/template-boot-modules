package me.wuwenbin.modules.utils.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by Wuwenbin on 2017/12/20 at 下午3:39
 */
public class R extends ConcurrentHashMap<String, Object> {

    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";

    public static final int SUCCESS = 200;
    public static final int SERVER_ERROR = 500;

    public R() {
        put(CODE, SUCCESS);
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static R ok() {
        return new R();
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("message", msg == null || "".equals(msg) ? "success!" : msg);
        return r;
    }

    public static R ok(Object data) {
        return ok(null, data);
    }

    public static R ok(String msg, Object data) {
        return ok(msg).put("data", data);
    }

    public static R ok(Map<String, Object> jsonMap) {
        R r = new R();
        r.putAll(jsonMap);
        return r;
    }

    public static <T> R error(String message, T data) {
        return error(message).put("data", data);
    }

    public static R error(String message) {
        return ok().put("code", SERVER_ERROR).put("message", message);
    }

    public static R custom(int code, String message) {
        return ok().put("code", code).put("message", message);
    }

    public static <T> R custom(int code, String message, T data) {
        return custom(code, message).put("data", data);
    }
}
