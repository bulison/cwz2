package cn.fooltech.fool_ops.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类
 *
 * @author tjr
 * @ClassName: StrUtil
 * @date 2011-11-25 下午10:39:58
 */
public class StrUtil {
    //private final static Logger log = Logger.getLogger(StrUtil.class);

    /**
     * 补位，通常情况用在生成随机数时不足几位以某个字符补足
     *
     * @param str      要补位的字符串
     * @param len      补位后返回的字符串长度，如果传入的字符串大于该长度，则返回源串
     * @param flag     要补的字符串，如果为空，则返回源串
     * @param position 在前面补还是后面
     * @return
     * @throws Exception
     */
    public static String fill(String str, int len, String flag, boolean position) throws Exception {
        if (isEmpty(flag) || len <= str.length()) return str;
        String tmp = "".intern();
        for (int i = 0; i < len - str.length(); i++)
            tmp += flag;
        if (position)
            str = tmp + str;
        else
            str += tmp;
        return str;
    }

    /**
     * 获得随机数
     *
     * @param seed 随机种子
     * @return
     * @throws Exception
     */
    public static String random(int seed) throws Exception {
        int random = (int) (Math.random() * seed);
        return String.valueOf(random);
    }

    /**
     * 把传入的英文单词首字母转换为大写/小写
     * 传入的如果为NULL或空字符串则返回传入值
     *
     * @param src
     * @param b   true 为大写 false为小写 默认大写
     * @return
     * @throws Exception
     * @auth tjr
     * @2012-12-13 上午12:24:10
     */
    public static String firstLetter2UpOrLower(String src, boolean b) {
        if (!isEmpty(src)) {
            char[] char_ = null;
            char_ = src.toCharArray();
            char_[0] = b ? Character.toUpperCase(char_[0]) : Character.toLowerCase(char_[0]);
            return String.valueOf(char_);
        }
        return src;
    }

    /**
     * 比较三个字符串是否一样
     *
     * @param source
     * @param a
     * @param b
     * @return
     * @throws Exception
     * @Title: isSameAAndB
     * @author tjr
     * @date 2013-10-9 下午5:31:34
     */
    public static boolean isSameAAndB(String source, String a, String b) throws Exception {
        return isSame(source, a) && isSame(source, b);
    }

    /**
     * 比较源字符串是否等于a或b其中一个字符串
     *
     * @param source
     * @param a
     * @param b
     * @return
     * @throws Exception
     * @Title: isSameAOrB
     * @author tjr
     * @date 2013-10-9 下午5:31:49
     */
    public static boolean isSameAOrB(String source, String a, String b) throws Exception {
        return isSame(source, a) || isSame(source, b);
    }

    /**
     * 判断str1是否跟str2相同，
     * 该方法会先把传进来的字符串转大写后再对比
     *
     * @param str1
     * @param str2
     * @return
     * @author tjr
     * 2012-1-19 上午10:16:32
     */
    public static boolean isSameA(String str1, String str2) {
        return isSame(str1.toUpperCase(), str2.toUpperCase());
    }

    /**
     * 判断str1是否str2相同
     *
     * @param str1
     * @param str2
     * @return true表示相同
     */
    public static boolean isSame(String str1, String str2) {
        if (null == str1 && null == str2) return true;
        else if (null == str1 || null == str2) return false;
        if (str1.trim().equals(str2.trim()))
            return true;
        return false;
    }

    /**
     * 把空格转换为指定字符
     *
     * @param sourceStr  源字符
     * @param replaceStr 代替字符
     * @return 处理后的字符串, 如果传进来的字符串为空，则返回空字符串
     * @Title: replaceSpace
     * @auth tjr
     * @date 2011-11-25下午10:49:45
     */
    public static String replaceSpace(String sourceStr, String replaceStr) {
        if (isEmpty(sourceStr)) return "".intern();
        sourceStr = sourceStr.trim();
        if (0 == sourceStr.length())
            return "".intern();
        return replace(sourceStr, " ".intern(), replaceStr);
    }

    /**
     * 把字符串里的特殊字符转换成HTML标签
     *
     * @param str 原字符串
     * @return 处理后的字符串, 如果传进来的字符串为空，则返回空字符串
     * @Title: toHtmlInput
     * @auth tjr
     * @date 2011-11-25下午10:48:59
     */
    public static String toHtmlInput(String str) {
        if (isEmpty(str))
            return "".intern();
        String html = new String(str);
        html = replace(html, "&", "&amp;");
        html = replace(html, "<", "&lt;");
        html = replace(html, ">", "&gt;");
        html = replaceSpace(html, "&nbsp;");
        html = replace(html, "\n", "<br/>");
        return html;
    }

    /**
     * @param source    源字符串
     * @param oldString 被替换的字符串
     * @param newString 代替替换的字符串
     * @return 转换后的字符
     * @Title: replace
     * @auth tjr
     * @date 2011-11-25下午10:48:11
     */
    public static String replace(String source, String oldString,
                                 String newString) {
        StringBuffer output = new StringBuffer();
        int lengthOfSource = source.length();// 源字符串长度
        int lengthOfOld = oldString.length();// 旧字符串长度
        int posStart = 0;// 开始搜索位置
        int pos;

        while ((pos = source.indexOf(oldString, posStart)) >= 0) {
            output.append(source.substring(posStart, pos));
            output.append(newString);
            posStart = pos + lengthOfOld;
        }
        if (posStart < lengthOfSource) {
            output.append(source.substring(posStart));
        }
        return output.toString();
    }

    /**
     * @param source       要转换字符
     * @param sourceCoding 源编码
     * @param coding       要转换的编码
     * @return 转换后的字符
     * @Title:toCoding
     * @auth tjr
     * @date 2011-11-25下午10:45:55
     */
    public static String toCoding(String source, String sourceCoding, String coding) {
        sourceCoding = isEmpty(sourceCoding) ? "ISO-8859-1".intern() : sourceCoding;
        try {
            if (isEmpty(source))
                source = "".intern();
            else
                source = new String(source.getBytes(sourceCoding), coding);
        } catch (Exception e) {
            //System.out.println("StrTool#toCoding(String)运行时出错：错误为:" + e);
            //log.error("转码失败",e);
        }
        return source;
    }

    /**
     * @param source 要转换字符
     * @return 转换后的字符
     * @Title:toCoding
     * @auth tjr
     * @date 2011-11-25下午10:46:55
     */
    public static String toCoding(String source) {
        return toCoding(source, "utf-8");
    }

    /**
     * @param source 要转换字符
     * @param coding 要转换的编码
     * @return 转换后的字符
     * @Title:toCoding
     * @auth tjr
     * @date 2011-11-25下午10:45:55
     */
    public static String toCoding(String source, String coding) {
        return toCoding(source, null, coding);
    }

    /**
     * 如果存入的字符串为空，则返回空字符串
     * 否则返回去掉前后空格的源字符串
     *
     * @param str
     * @return
     * @auth tjr
     * @2012-12-2 下午05:07:43
     */
    public static String empty2Trim(String str) {
        return isEmpty(str) ? "".intern() : str.trim();
    }

    /**
     * @param str 要判断的字符串
     * @return 返回false表示为空
     * @Title: notEmpty
     * @auth tjr
     * @date 2013-11-27 下午10:41:58
     */
    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * @param str 要判断的字符串
     * @return 返回false表示不为空
     * @Title: isEmpty
     * @auth tjr
     * @date 2011-11-25下午10:41:58
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".intern().equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符数组是否为空
     *
     * @param strs
     * @return
     * @author tjr
     * Jan 11, 2012 11:53:56 AM
     */
    public static boolean isEmpty(String[] strs) {
        if (null == strs || 0 == strs.length)
            return true;
        return false;
    }

    /**
     * 分割字符串
     *
     * @param str要分割的字符串
     * @param 分割的符号
     * @add by xjh @date 2015-5-25
     * <p>
     * str="a,b,c,," return result[]={"a","b","c"}
     */
    public static String[] split(String str, String splitter) {
        if (null == str || null == splitter) return new String[0];
        String results[] = str.split(splitter);
        List<String> list = new ArrayList<String>();
        for (String result : results) {
            if ("".equals(result)) {
                continue;
            } else {
                list.add(result);
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}
