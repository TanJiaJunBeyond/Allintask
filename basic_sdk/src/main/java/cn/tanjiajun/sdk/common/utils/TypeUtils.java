package cn.tanjiajun.sdk.common.utils;

import java.util.List;
import java.util.Map;

/**
 * 类型转换工具类
 *
 * @author TanJiaJun
 * @version 0.0.2
 */
public class TypeUtils {

    public static String getString(final Object object) {
        return getString(object, null);
    }

    public static String getString(final Object object, final String defaultValue) {
        String str = defaultValue;
        if (null != object && object instanceof String) {
            str = (String) object;
        } else if (null != object && object instanceof Number) {
            str = object + "";
        }
        return str;
    }

    public static boolean getBoolean(final Object object) {
        return getBoolean(object, false);
    }

    public static boolean getBoolean(final Object object, final boolean defaultValue) {
        boolean b = defaultValue;
        if (null != object && object instanceof Boolean) {
            b = (Boolean) object;
        }
        return b;
    }

    public static Integer getInteger(final Object object) {
        return getInteger(object, null);
    }

    public static Integer getInteger(final Object object, final Integer defaultValue) {
        Integer i = defaultValue;
        if (null != object && object instanceof Integer) {
            i = (Integer) object;
        }
        return i;
    }

    public static Float getFloat(final Object object) {
        return getFloat(object, 0.0f);
    }

    public static Float getFloat(final Object object, final float defaultValue) {
        float f = defaultValue;
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }
        return f;
    }

    public static Double getDouble(final Object object) {
        return getDouble(object, 0);
    }

    public static Double getDouble(final Object object, final double defaultValue) {
        double f = defaultValue;
        if (object instanceof Number) {
            return ((Double) object).doubleValue();
        }
        return f;
    }

    public static Long getLong(final Object object) {
        return getLong(object, null);
    }

    public static Long getLong(final Object object, final Long defaultValue) {
        Long l = defaultValue;
        if (null != object) {

            if (object instanceof Long) {
                l = (Long) object;
            } else if (object instanceof Integer) {
                l = Long.parseLong(((Integer) object).toString());
            }
        }
        return l;
    }

    public static Map<String, Object> getMap(final Object object) {
        return getMap(object, null);
    }

    public static Map<String, Object> getMap(final Object object, final Map<String, Object> defaultValue) {
        Map<String, Object> map = defaultValue;
        if (null != object && object instanceof Map<?, ?>) {
            map = (Map<String, Object>) object;
        }
        return map;
    }

    public static List<Map<String, Object>> getList(final Object object) {
        return getList(object, null);
    }

    public static List<Map<String, Object>> getList(final Object object, final List<Map<String, Object>> defaultValue) {
        List<Map<String, Object>> list = defaultValue;
        if (null != object && object instanceof List<?>) {
            list = (List<Map<String, Object>>) object;
        }

        return list;
    }

    public static String getId(Object object) {
        return object != null ? object.toString() : "";
    }

}
