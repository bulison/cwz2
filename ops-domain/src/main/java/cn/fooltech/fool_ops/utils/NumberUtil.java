package cn.fooltech.fool_ops.utils;

import com.google.common.base.Strings;
import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>数字处理工具类</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月18日
 */
public class NumberUtil extends NumberUtils {

    /**
     * 精度
     */
    public static final int PRECISION_SCALE_2 = 2;
    public static final int PRECISION_SCALE_4 = 4;

    /**
     * 字符串转BigDecimal
     *
     * @param num
     * @return
     */
    public static BigDecimal toBigDeciaml(String num) {
        if (StringUtils.isNotBlank(num)) {
            return new BigDecimal(num);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 字符串转BigDecimal
     *
     * @param num
     * @return
     */
    public static BigDecimal toBigDeciaml(String num, int scale) {
        if (StringUtils.isNotBlank(num)) {
            BigDecimal result = new BigDecimal(num);
            result.setScale(scale, BigDecimal.ROUND_HALF_UP);
            return result;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 字符串转BigDecimal
     *
     * @param num
     * @return
     */
    public static BigDecimal toBigDeciaml(double num, int scale) {
        BigDecimal result = new BigDecimal(num);
        result.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return result;
    }

    /**
     * BigDecimal转字符串
     *
     * @param num
     * @return
     */
    public static String bigDecimalToStr(BigDecimal num) {
        if (num != null) {
            num = num.setScale(PRECISION_SCALE_2, BigDecimal.ROUND_HALF_UP);
            return stripTrailingZeros(num.toPlainString());
        }
        return null;
    }

    /**
     * BigDecimal转字符串
     *
     * @param num
     * @return
     */
    public static String bigDecimalToStr(BigDecimal num, int scale) {
        if (num != null) {
            num = num.setScale(scale, BigDecimal.ROUND_HALF_UP);
            return stripTrailingZeros(num.toPlainString());
        }
        return null;
    }

    /**
     * 字符串转BigDecimal
     *
     * @param num
     * @return
     */
    public static BigDecimal strToBigDecimal(String num) {
        if (num != null && num.length() > 0) {
            BigDecimal numVal = new BigDecimal(num);
            return numVal;
        }
        return null;
    }

    /**
     * 数字去除多余的0
     */
    public static String stripTrailingZeros(String number) {
        if (number == null) return "0";
        BigDecimal result = BigDecimal.valueOf(Double.parseDouble(number)).stripTrailingZeros();
        return result.toString().equals("0.0") ? "0" : result.toPlainString();
    }

    /**
     * 数字去除多余的0, 保留scale位小数
     */
    public static String stripTrailingZeros(String number, int scale) {
        if (Strings.isNullOrEmpty(number)) return "0";
        BigDecimal result = BigDecimal.valueOf(Double.parseDouble(number)).stripTrailingZeros();
        if (result.precision() > 2) result = result.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return result.toString().equals("0.0") ? "0" : result.toPlainString();
    }

    /**
     * 数字去除多余的0, 保留scale位小数
     */
    public static String stripTrailingZeros(Object number, int scale) {
        if (number == null) return "0";
        BigDecimal result = BigDecimal.valueOf(Double.parseDouble(number.toString())).stripTrailingZeros();
        if (result.precision() > 2) result = result.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return result.toString().equals("0.0") ? "0" : result.toPlainString();
    }

    /**
     * 数字去除多余的0
     */
    public static BigDecimal stripTrailingZeros(BigDecimal number) {
        if (number == null) return BigDecimal.ZERO;
        BigDecimal result = BigDecimal.valueOf(Double.parseDouble(number.stripTrailingZeros().toPlainString()));
        if (BigDecimal.ZERO.compareTo(result) == 0) return new BigDecimal(0);
        return result;
    }

    /**
     * 数字四舍五入
     */
    public static BigDecimal scale(BigDecimal number, int scale) {
        if (number == null) return BigDecimal.ZERO;
        number = number.setScale(scale, RoundingMode.HALF_UP);
        return number;
    }

    /**
     * 两数相加，防止空
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
        if (num1 == null) num1 = BigDecimal.ZERO;
        if (num2 == null) num2 = BigDecimal.ZERO;
        return num2.add(num1);
    }

    /**
     * 两数相减，防止空
     */
    public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
        if (num1 == null) num1 = BigDecimal.ZERO;
        if (num2 == null) num2 = BigDecimal.ZERO;
        return num1.subtract(num2);
    }

    /**
     * 两数相除，防止空
     */
//    public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
//        if (num1 == null) return BigDecimal.ZERO;
//        if (num2 == null) return BigDecimal.ZERO;
//        if (num2.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
//        return num1.divide(num2);
//    }

    /**
     * 两数相除，防止空
     */
    public static BigDecimal divide(BigDecimal num1, BigDecimal num2, int scale) {
        if (num1 == null) return BigDecimal.ZERO;
        if (num2 == null) return BigDecimal.ZERO;
        if (num2.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return num1.divide(num2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 两数相乘，有空则返回0
     */
    public static BigDecimal multiply(BigDecimal ...nums) {
        BigDecimal result = BigDecimal.ONE;
        if(nums==null || nums.length==0)return BigDecimal.ZERO;
        for(BigDecimal num:nums){
            if(num==null||num.equals(BigDecimal.ZERO))return BigDecimal.ZERO;
            result = result.multiply(num);
        }
        return result;
    }

    /**
     * 两数相乘，防止空
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
        if (num1 == null) return BigDecimal.ZERO;
        if (num2 == null) return BigDecimal.ZERO;
        return num1.multiply(num2);
    }

    /**
     * 两数相除，防止空
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2, int scale) {
        if (num1 == null) return BigDecimal.ZERO;
        if (num2 == null) return BigDecimal.ZERO;
        return scale(num1.multiply(num2), scale);
    }

    public static BigDecimal ifNull(BigDecimal num) {
        return num == null ? BigDecimal.ZERO : num;
    }

    public static BigDecimal ifNull(BigDecimal num, BigDecimal defaultValue) {
        return num == null ? defaultValue : num;
    }

    /**
     * 取最大值(两个值都未空时返回0)
     *
     * @param num1
     * @param num2
     * @return
     */
    public static BigDecimal max(BigDecimal num1, BigDecimal num2) {
        if (num1 == null && num2 == null) return BigDecimal.ZERO;
        if (num1 == null) return num2;
        if (num2 == null) return num1;
        if (num1.compareTo(num2) > 0) {
            return num1;
        }
        return num2;
    }

    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
}
