package cn.org.opendfl.tasktool.utils;


/**
 * 常用工具类
 * @author chenjh
 */
public class CommUtils {
    private CommUtils() {

    }

    /**
     * 去除url指定参数
     *
     * @param url
     * @param name
     * @return
     */
    public static String removeParam(String url, String... name) {
        for (String s : name) {
            // 使用replaceAll正则替换,replace不支持正则
            url = url.replaceAll("[&|?]" + s + "=[^&]*", "");
        }
        return url;
    }

    /**
     * 取数据前maxLength位
     *
     * @param str
     * @param maxLength
     * @return
     */
    public static String getStringLimit(String str, int maxLength) {
        return str != null && str.length() > maxLength ? str.substring(0, maxLength) : str;
    }

    public static String getStringFirst(String str, String split) {
        if (str == null) {
            return null;
        }
        int idx = str.indexOf(split);
        if (idx > 0) {
            return str.substring(0, idx);
        }
        return str;
    }

    public static String getString(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            return (String) obj;
        } else {
            return "" + obj;
        }
    }

    public static Long getLong(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else {
            return Long.parseLong("" + obj);
        }
    }

    /**
     * @return
     */
    public static boolean startWithChar(String str, char ch) {
        if (str == null || str.length() < 1) {
            return false;
        }
        return str.charAt(0) == ch;
    }

    /**
     * @return
     */
    public static boolean endWithChar(String str, char ch) {
        if (str == null || str.length() < 1) {
            return false;
        }
        return str.charAt(str.length() - 1) == ch;
    }


    public static String appendUrl(String url, String path) {
        if (url == null) {
            return null;
        }
        if (path == null) {
            path = "";
        }
        if (endWithChar(url, '/') && startWithChar(path, '/')) {//相当于startsWith
            return url + path.substring(1);
        } else if (endWithChar(url, '/') || startWithChar(path, '/')) {//相当于startsWith
            return url + path;
        }

        return url + "/" + path;
    }


    public static String join(String[] arrays, String split) {
        StringBuilder bld = new StringBuilder();
        for (String array : arrays) {
            bld.append(array).append(split);
        }
        String str = bld.toString();
        if (str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
