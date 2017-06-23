package cn.fooltech.fool_ops.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:字符串处理的类。 所有的字符串处理都使用这个类。
 * 暂时继承了commons的StringUtils类
 * 可以方便的扩展其他的实用方法
 * @author: 戴铁坚
 * @data: 2007-8-3
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private StringUtils() {
    }

    public static int stringToInt(String str) {
        if ("".equals(str) || str == null) return -1;
        int ret;
        ret = Integer.valueOf(str);
        return ret;
    }

    /**
     * 将String类型的数据转换为其他类型
     *
     * @param type 支持 int/float/double/ling/short/bigdecimal/date
     * @param str
     * @return
     */
    public static Object StringToOtherType(String type, String str) {
        if (str == null) {
            return null;
        }
        if ("string".equalsIgnoreCase(type)) {
            return str;
        } else if ("int".equalsIgnoreCase(type) || "integer".equalsIgnoreCase(type)) {
            return Integer.valueOf(str);
        } else if ("float".equalsIgnoreCase(type)) {
            return Float.valueOf(str);
        } else if ("double".equalsIgnoreCase(type)) {
            return Double.valueOf(str);
        } else if ("long".equalsIgnoreCase(type)) {
            return Long.valueOf(str);
        } else if ("short".equalsIgnoreCase(type)) {
            return Short.valueOf(str);
        } else if ("bigdecimal".equalsIgnoreCase(type) || "decimal".equalsIgnoreCase(type) || "money".equals(type)) {
            return new BigDecimal(str);
        } else if ("date".equalsIgnoreCase(type) || "time".equalsIgnoreCase(type)) {
            return DateUtils.getDateFromString(str);
        }
        return str;
    }

    /**
     * 字符串的Encode编码
     *
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
     *
     * @param  c 需要判断的字符
     * @return boolean, 返回true,Ascill字符
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param  s ,需要得到长度的字符串
     * @return int, 得到的字符串长度
     */
    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
     *
     * @param origin, 原始字符串
     * @param  len, 截取长度(一个汉字长度按2算的)
     * @return String, 返回的字符串
     */
    public static String substring(String origin, int len) {
        if (origin == null || origin.equals("") || len < 1)
            return "";
        byte[] strByte = new byte[len];
        if (len > StringUtils.length(origin)) {
            return origin;
        }
        System.arraycopy(origin.getBytes(), 0, strByte, 0, len);
        int count = 0;
        for (int i = 0; i < len; i++) {
            int value = (int) strByte[i];
            if (value < 0) {
                count++;
            }
        }
        if (count % 2 != 0) {
            len = (len == 1) ? ++len : --len;
        }
        return new String(strByte, 0, len);
    }

    /**
     * 判断是否拥有前缀
     *
     * @param preStr
     * @param str
     * @return
     */
    public static boolean checkPreStr(String[] preStr, String str) {
        for (int ii = 0; ii < preStr.length; ii++) {
            if (str.startsWith(preStr[ii])) return true;
        }
        return false;
    }

    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    /**
     * 按模板替换字符串
     * @param template
     * @param data
     * @return
     * @throws Exception
     */
    public static String composeMessage(String template, Map<String, String> data) {
        String regex = "\\$\\{(.+?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(template);
        /*
         * sb用来存储替换过的内容，它会把多次处理过的字符串按源字符串序
         * 存储起来。
         */
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1);//键名
            String value = data.get(name);//键值
            if (value == null) {
                value = "";
            } else {
                /*
                 * 由于$出现在replacement中时，表示对捕获组的反向引用，所以要对上面替换内容
                 * 中的 $ 进行替换，让它们变成 "\$1000.00" 或 "\$1000000000.00" ，这样
                 * 在下面使用 matcher.appendReplacement(sb, value) 进行替换时就不会把
                 * $1 看成是对组的反向引用了，否则会使用子匹配项值amount 或 balance替换 $1
                 * ，最后会得到错误结果：
                 *
                 * 尊敬的客户刘明你好！本次消费金额amount000.00，您帐户888888888上的余额
                 * 为balance000000.00，欢迎下次光临！
                 *
                 * 要把 $ 替换成 \$ ，则要使用 \\\\\\& 来替换，因为一个 \ 要使用 \\\ 来进
                 * 行替换，而一个 $ 要使用 \\$ 来进行替换，因 \ 与  $ 在作为替换内容时都属于
                 * 特殊字符：$ 字符表示反向引用组，而 \ 字符又是用来转义 $ 字符的。
                 */
                value = value.replaceAll("\\$", "\\\\\\$");
                //System.out.println("value=" + value);
            }
            /*
             * 经过上面的替换操作，现在的 value 中含有 $ 特殊字符的内容被换成了"\$1000.00"
             * 或 "\$1000000000.00" 了，最后得到下正确的结果：
             *
             * 尊敬的客户刘明你好！本次消费金额$1000.00，您帐户888888888上的
             * 余额为$1000000.00，欢迎下次光临！
             *
             * 另外，我们在这里使用Matcher对象的appendReplacement()方法来进行替换操作，而
             * 不是使用String对象的replaceAll()或replaceFirst()方法来进行替换操作，因为
             * 它们都能只能进行一次性简单的替换操作，而且只能替换成一样的内容，而这里则是要求每
             * 一个匹配式的替换值都不同，所以就只能在循环里使用appendReplacement方式来进行逐
             * 个替换了。
             */
            matcher.appendReplacement(sb, value);
        }
        //最后还得要把尾串接到已替换的内容后面去，这里尾串为“，欢迎下次光临！”
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取指定最大长度，超出截断
     * @param paramater
     * @return
     */
    public static String maxLength(String paramater, int maxLength){
        if(paramater==null)return "";//确保不返回null
        if(paramater.length()>maxLength){
            return paramater.substring(0, maxLength);
        }
        return paramater;
    }
}
