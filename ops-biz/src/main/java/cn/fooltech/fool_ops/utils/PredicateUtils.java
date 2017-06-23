package cn.fooltech.fool_ops.utils;

public class PredicateUtils {

    /**
     * 获得模糊匹配（任意位置）
     *
     * @param key
     * @return
     */
    public static String getAnyLike(String key) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("%");
        buffer.append(key);
        buffer.append("%");
        return buffer.toString();
    }

    /**
     * 获得模糊匹配（左位置）
     *
     * @param key
     * @return
     */
    public static String getLeftLike(String key) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("%");
        buffer.append(key);
        return buffer.toString();
    }

    /**
     * 获得模糊匹配（右位置）
     *
     * @param key
     * @return
     */
    public static String getRightLike(String key) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(key);
        buffer.append("%");
        return buffer.toString();
    }
}
