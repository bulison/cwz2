package cn.fooltech.fool_ops.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期转换操作工具类
 *
 * @author
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
    private DateUtils() {
    }


    /**
     * 获得当前字符串类型的日期时间值（格式：2004-05-20 00:00:00）
     *
     * @return 日期时间值
     */
    public static String getCurrentDateTime() {
        Date d = new Date();
        Timestamp t = new Timestamp(d.getTime());
        return t.toString().substring(0, 19);
    }

    /**
     * 获得当前字符串类型的日期值（格式：2004-05-20）
     *
     * @return 日期值
     */
    public static String getCurrentDate() {
        Date d = new Date();
        Timestamp t = new Timestamp(d.getTime());
        return t.toString().substring(0, 10);
    }

    /**
     * 获得当前字符串类型的时间值
     *
     * @return 时间值（格式为"00:00:00"）
     */
    public static String getCurrentTime() {
        Date d = new Date();
        Timestamp t = new Timestamp(d.getTime());
        return t.toString().substring(11, 19);
    }

    /**
     * 将字符串转换为时间类型
     * 支持格式如下：
     * Thu May 15 00:00:00 CST 2008 或者	yyyy-MM-dd  或者 yyyy-MM-dd HH:mm  或者 yyyy-MM-dd HH:mm:ss
     *
     * @param str
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date getDateFromString(String str) {
        if (str == null || str.trim().equals(""))
            return null;
        SimpleDateFormat dateFormat = null;
        Date d = null;
        try {
            d = new Date(str);
        } catch (Exception e) {
        }
        if (d != null) return d;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            d = dateFormat.parse(str);
        } catch (ParseException e) {
        }
        if (d != null) return d;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            d = dateFormat.parse(str);
        } catch (ParseException e) {
        }
        if (d != null) return d;
        dateFormat = new SimpleDateFormat("yy-M-d");
        try {
            d = dateFormat.parse(str);
        } catch (ParseException e) {
        }
        if (d != null) return d;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d = dateFormat.parse(str);
        } catch (ParseException e) {
        }
        if (d != null) return d;
        throw new IllegalArgumentException("时间字符串格式错误");

    }

    /**
     * 取得日期格式的字符串 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 取得时间格式的字符串 yyyy-MM-dd HH:mm
     *
     * @param date
     * @return
     */
    public static String getTimeString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(date);
    }

    /**
     * 取得指定格式的字符串
     *
     * @param date
     * @return
     */
    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 计算日期差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date now, Date returnDate) {
        Calendar cNow = Calendar.getInstance();
        Calendar cReturnDate = Calendar.getInstance();
        cNow.setTime(now);
        cReturnDate.setTime(returnDate);
        setTimeToMidnight(cNow);
        setTimeToMidnight(cReturnDate);
        long todayMs = cNow.getTimeInMillis();
        long returnMs = cReturnDate.getTimeInMillis();
        long intervalMs = todayMs - returnMs;
        return millisecondsToDays(intervalMs);
    }

    /**
     * 将日期转化为大小写
     *
     * @param date
     * @return
     */
    public static String toUpperDateString(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        return numberToUpper(year) + "年" + monthToUppder(month) + "月" + dayToUppder(day) + "日";
    }

    // 将数字转化为大写
    private static String numberToUpper(int num) {
        String u[] = {"○", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        char[] str = String.valueOf(num).toCharArray();
        String rstr = "";
        for (int i = 0; i < str.length; i++) {
            rstr = rstr + u[Integer.parseInt(str[i] + "")];
        }
        return rstr;
    }

    // 月转化为大写
    private static String monthToUppder(int month) {
        if (month < 10) {
            return numberToUpper(month);
        } else if (month == 10) {
            return "十";
        } else {
            return "十" + numberToUpper(month - 10);
        }
    }

    // 日转化为大写
    private static String dayToUppder(int day) {
        if (day < 20) {
            return monthToUppder(day);
        } else {
            char[] str = String.valueOf(day).toCharArray();
            if (str[1] == '0') {
                return numberToUpper(Integer.parseInt(str[0] + "")) + "十";
            } else {
                return numberToUpper(Integer.parseInt(str[0] + "")) + "十"
                        + numberToUpper(Integer.parseInt(str[1] + ""));
            }
        }
    }

    private static int millisecondsToDays(long intervalMs) {
        return (int) (intervalMs / (1000 * 86400));
    }

    private static void setTimeToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

//	public static void main(String[] args) throws ParseException {
//
//		//System.out.println(toUpperDateString(getDateFromString("2012-05-31")));
//		//DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd ");
//		Date fd=new Date();
//		Date end=new Date();
//		int days=DateUtils.daysBetween(fd, end);
//		System.out.println(days);
//		//System.out.println(dateFormat.parse("Thu May 15 00:00:00 CST 2008"));
//
//		//Date d1 = DateUtils.getDateFromString("2004-5-6 09:59:59");
//		//Date d2 = DateUtils.getDateFromString("2004-5-6");
//		//System.out.println(daysBetween(d1,d2));
//		System.out.println(getStringByFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
//	}
}
