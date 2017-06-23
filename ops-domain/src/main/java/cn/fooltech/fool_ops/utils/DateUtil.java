package cn.fooltech.fool_ops.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author lzf
 * @version 1.0
 * @date 下午4:19:22
 */
public class DateUtil {
    public final static String dateFormate = "yyyy-MM-dd";
    public final static String dateTime = "yyyyMMddHHmmssSSS";

    /**
     * 把日期字符串转换成日期格式
     *
     * @param firstDateIn
     * @param getDateIn
     * @param getMoney
     * @param payDateList
     * @param payMoneyList
     * @return
     */
    public static boolean dateformVerify(String firstDateIn, String getDateIn,
                                         String getMoney, String[] payDateList, String[] payMoneyList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 设置lenient为false.
        // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(firstDateIn);
            dateFormat.parse(getDateIn);
            for (int i = 0; i < payDateList.length; i++) {
                dateFormat.parse(payDateList[i]);
            }
            Long.parseLong(getMoney);
            for (int i = 0; i < payDateList.length; i++) {
                Long.parseLong(payMoneyList[i]);
            }
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static Date str2Date(String dateStr, String formatStr)
            throws Exception {
        SimpleDateFormat format = getDateFomat(formatStr);// new
        // SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式化日期
     *
     * @param date
     */
    public static String format(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormate);
        return format.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 根据传入的日期格式获取SimpleDateFormat对象
     *
     * @param formatStr 格式化字符串,如果该值为空则使用系统默认格式
     * @return
     * @throws Exception
     * @2012-9-6 下午01:32:01
     */
    public static SimpleDateFormat getDateFomat(String formatStr)
            throws Exception {
        formatStr = dateFormate;
        return new SimpleDateFormat(formatStr);
    }

    /**
     * 获取昨天的日期
     *
     * @param today 今天
     * @return
     * @author rqh
     */
    public static Date getYesterday(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获取日期(忽略日期的时分秒)
     *
     * @param date
     * @return
     * @author rqh
     */
    public static Date getSimpleDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 日期比较(忽略日期的时分秒)
     *
     * @param date1
     * @param date2
     * @return
     * @author rqh
     */
    public static int compareDate(Date date1, Date date2) {
        date1 = getSimpleDate(date1);
        date2 = getSimpleDate(date2);
        return date1.compareTo(date2);
    }

    /**
     * 计算两个日期之间相差的天数(忽略日期的时分秒)
     *
     * @param smdate 开始日期
     * @param bdate  结束日期
     * @return
     * @throws Exception
     * @author rqh
     */
    public static int daysBetween(Date smdate, Date bdate) throws Exception {
        smdate = getSimpleDate(smdate);
        bdate = getSimpleDate(bdate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 调整日期
     *
     * @param date
     * @param day
     */
    public static Date addDays(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 时间戳转换Date
     *
     * @param unix
     * @return
     * @throws Exception
     */
    public static Date unix2Date(String unix) {
        if (StringUtils.isBlank(unix))
            return null;
        long _l = Long.valueOf(unix);
        return unix2Date(_l);
    }

    /**
     * 时间戳转换Date
     *
     * @param unix
     * @return
     */
    public static Date unix2Date(long unix) {
        return unix2Date(unix, null);
    }

    /**
     * 时间戳转换Date
     *
     * @param unix
     * @return
     */
    public static Date unix2Date(long unix, String formatStr) {
        formatStr = StringUtils.isBlank(formatStr) ? "yyyy-MM-dd HH:mm:ss"
                : formatStr;
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String d = format.format(unix);
        try {
            Date date = format.parse(d);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间戳转为日期字符串
     *
     * @param unix
     * @return
     * @throws Exception
     */
    public static String unix2Str(long unix, String formatStr) throws Exception {
        Date d = unix2Date(unix, formatStr);
        return DateUtils.getStringByFormat(d, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 时间戳转为日期字符串
     *
     * @param unix
     * @return
     * @throws Exception
     */
    public static String unix2Str(String unix) throws Exception {
        if (StringUtils.isBlank(unix))
            return null;
        long _l = Long.valueOf(unix);
        return unix2Str(_l);
    }

    /**
     * 时间戳转为日期字符串
     *
     * @param unix
     * @return
     * @throws Exception
     */
    public static String unix2Str(long unix) throws Exception {
        Date d = unix2Date(unix);
        return DateUtils.getStringByFormat(d, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 时间戳转日期
     *
     * @param str_num
     * @return
     */
    public static String timestamp2Date(String str_num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (str_num.length() == 13) {
            String date = sdf.format(new Date(Long.parseLong(str_num)));
            //LogUtil.d(Constant.TAG + "将13位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        } else {
            String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
            //LogUtil.d(Constant.TAG + "将10位时间戳:" + str_num + "转化为字符串:", date);
            return date;
        }
    }

    public static Date getYear(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);
        return cal.getTime();
    }

    //获取当天时间0点
    public static Date getDate2Zero() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return cal.getTime();
    }

}
