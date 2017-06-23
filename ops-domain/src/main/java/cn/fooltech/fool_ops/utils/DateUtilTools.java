package cn.fooltech.fool_ops.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author lgk
 * @version V1.0
 * @date 2015年10月19日上午10:59:56
 */
public class DateUtilTools {
    /**
     * yyyy-MM-dd
     */
    public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * yyyyMM
     */
    public static final String DATE_PATTERN_YYYYMM = "yyyyMM";
    /**
     * yyyyMMddHHmmss
     */
    public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String DATE_PATTERN_YYYYMMDDHHMMssSSS = "yyyyMMddHHmmssSSS";
    /**
     * yyyyMMddHHmm
     */
    public static final String DATE_PATTERN_YYYYMMDDHHMM = "yyyyMMddHHmm";
    /**
     * HHmmss
     */
    public static final String DATE_PATTERN_HHMMSS = "HHmmss";
    /**
     * yyyyMMddHHmm
     */
    public static final String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_PATTERN_YYYY_MM_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String DATE_PATTERN_YYYY_MM_DDHHMM = "yyyy-MM-dd HH:mm";
    /**
     * 秒的毫秒数
     */
    public final static long SECOND_MILLIS = 1000;
    /**
     * 分钟的毫秒数
     */
    public final static long MIN_MILLIS = 60 * SECOND_MILLIS;
    /**
     * 小时的毫秒数
     */
    public final static long HOUR_MILLIS = 60 * MIN_MILLIS;
    /**
     * 天的毫秒数
     */
    public final static long DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final Log log = LogFactory.getLog(DateUtilTools.class);
    private static final DateFormat FROMATYYYY_MM_DDHHMMSS = new SimpleDateFormat(
            DATE_PATTERN_YYYY_MM_DDHHMMSS);
    private static final DateFormat FROMATYYYY_MM_DD = new SimpleDateFormat(
            DATE_PATTERN_YYYY_MM_DD);

    /**
     * 日期转换成字符串格式
     *
     * @param theDate     待转换的日期
     * @param datePattern 日期格式
     * @return 日期字符串
     */
    public static String date2String(Date theDate, String datePattern) {
        if (theDate == null) {
            return "";
        }

        DateFormat format = new SimpleDateFormat(datePattern);
        try {
            return format.format(theDate);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 日期转换成字符串格式
     *
     * @param theDate 待转换的日期
     * @return 日期字符串 YYYY-MM-DD
     */
    public static String date2String(Date theDate) {
        if (theDate == null) {
            return "";
        }

        try {
            return FROMATYYYY_MM_DD.format(theDate);
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 日期转换成字符串格式
     *
     * @param theDate 待转换的日期
     * @return 日期字符串 YYYY-MM-DD HH:mm:ss
     */
    public static String time2String(Date theDate) {
        if (theDate == null) {
            return "";
        }

        try {
            return FROMATYYYY_MM_DDHHMMSS.format(theDate);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 字符串转换成日期格式
     *
     * @param dateString  待转换的日期字符串
     * @param datePattern 日期格式
     * @return 转换后的日期
     */
    public static Date string2Date(String dateString, String datePattern) {
        if (dateString == null || dateString.trim().length() == 0) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(datePattern);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            log.error("ParseException in Converting String to date: "
                    + e.getMessage());
        }
        return null;
    }

    /**
     * 字符串转换成日期格式
     *
     * @param dateString yyyy-MM-dd
     *                   待转换的日期字符串
     * @return 转换后的日期
     */
    public static Date string2Date(String dateString) {
        if (dateString == null || dateString.trim().length() == 0) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(DATE_PATTERN_YYYY_MM_DD);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            log.error("ParseException in Converting String to date: "
                    + e.getMessage());
        }
        return null;
    }

    /**
     * 字符串转换成时间格式
     *
     * @param dateString yyyy-MM-dd HH:mm:ss
     *                   待转换的时间字符串
     * @return 转换后的日期
     */
    public static Date string2Time(String dateString) {
        if (dateString == null || dateString.trim().length() == 0) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(DATE_PATTERN_YYYY_MM_DDHHMMSS);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            log.error("ParseException in Converting String to date: "
                    + e.getMessage());
        }
        return null;
    }

    /**
     * 修改日期
     *
     * @param theDate  待修改的日期
     * @param addMonth 加减月数
     * @param addDays  加减的天数
     * @param hour     设置的小时
     * @param minute   设置的分
     * @param second   设置的秒
     * @return 修改后的日期
     */
    public static Date changeDateTime(Date theDate, int addMonth, int addDays,
                                      int hour, int minute, int second) {
        if (theDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        cal.add(Calendar.MONTH, addMonth);
        cal.add(Calendar.DAY_OF_MONTH, addDays);
        if (hour >= 0 && hour <= 24) {
            cal.set(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute >= 0 && minute <= 60) {
            cal.set(Calendar.MINUTE, minute);
        }
        if (second >= 0 && second <= 60) {
            cal.set(Calendar.SECOND, second);
        }
        return cal.getTime();
    }

    /**
     * 获取月初时间
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);// 年
        cal.set(Calendar.MONTH, month - 1);// 月，因为Calendar里的月是从0开始，所以要减1
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DATE, 1);// 日，设为一号
        return cal.getTime();// 获得月末是几号
    }

    /**
     * 获取月末时间
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);// 年
        cal.set(Calendar.MONTH, month - 1);// 月，因为Calendar里的月是从0开始，所以要减1
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.DATE, 1);// 日，设为一号
        cal.add(Calendar.MONTH, 1);// 月份加一，得到下个月的一号
        cal.add(Calendar.DATE, -1);// 下一个月减一为本月最后一天
        return cal.getTime();// 获得月末是几号
    }


    /**
     * 计算指定时间日期的开始时间
     *
     * @param date
     * @return
     */
    public static long getDateStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * 计算指定时间日期的结束时间
     *
     * @param date
     * @return
     */
    public static long getDateEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * 将当前时间加值
     *
     * @param theDate
     * @param addDays   天
     * @param addHour   小时
     * @param addMinute 分
     * @param addSecond 秒
     * @return
     */
    public static Date addDateTime(Date theDate, int addDays, int addHour,
                                   int addMinute, int addSecond) {
        Date result = new Date();
        result.setTime(theDate.getTime());
        long addTime = (addDays * 3600 * 24 + addHour * 3600 + addMinute * 60 + addSecond) * 1000;
        result.setTime(result.getTime() + addTime);
        return result;
    }

    /**
     * 将当前时间加值
     *
     * @param currentTime 当前时间
     * @param addDays     天
     * @param addHour     小时
     * @param addMinute   分
     * @param addSecond   秒
     * @return
     */
    public static long addDateTime(long currentTime, int addDays, int addHour,
                                   int addMinute, int addSecond) {
        return currentTime + addDays * DAY_MILLIS + addHour * HOUR_MILLIS
                + addMinute * MIN_MILLIS + addSecond * SECOND_MILLIS;
    }

    /**
     * 获取当前时间的毫秒数值
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 计算两个时间的天数差
     *
     * @param date1 减数
     * @param date2 被减数
     * @return 返回天数
     */
    public static int subDateDay(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / DAY_MILLIS);
    }

    /**
     * 计算两个时间的天数差
     *
     * @param time1 减数
     * @param time2 被减数
     * @return 返回天数
     */
    public static int subTimeDay(long time1, long time2) {
        return (int) ((time1 - time2) / DAY_MILLIS);
    }

    /**
     * 计算更新时间:result=((currentTime-lastTime)/intervalTime)*intervalTime+lastTime
     *
     * @param lastTime     上次时间
     * @param currentTime  当前时间
     * @param intervalTime 间隔时间
     * @return
     */
    public static long computeTime(long lastTime, long currentTime,
                                   long intervalTime) {
        return ((currentTime - lastTime) / intervalTime) * intervalTime
                + lastTime;
    }

    /**
     * 计算本周星期几开始时间
     *
     * @return
     */
    public static long getWeekDayTime(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


    /**
     * 获取时间元素
     *
     * @return
     */
    public static int getDateElement(int type) {
        return Calendar.getInstance().get(type);
    }

    /**
     * 获取时间元素
     *
     * @return
     */
    public static int getDateElement(int type, Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(type);
    }

    /**
     * 获取当年的开始时间
     *
     * @return
     */
    public static Date changeToYearStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    /**
     * 获取上一年的开始时间
     *
     * @return
     */
    public static Date changeToPreYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        return calendar.getTime();
    }

    /**
     * 目标日期是否今日
     *
     * @param target
     * @return
     */
    public static boolean isToday(Date target) {

        Calendar targetc = Calendar.getInstance();
        targetc.setTime(target);

        Calendar today = Calendar.getInstance();
        if (targetc.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
                && targetc.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 获得今天的日期
     *
     * @return
     */
    public static Date getToday() {
        return changeDateTime(now(), 0, 0, 0, 0, 0);
    }

    /**
     * 获得明天的日期
     *
     * @return
     */
    public static Date getTomorrow() {
        return changeDateTime(now(), 0, 1, 0, 0, 0);
    }

    /**
     * 获得昨天的日期
     *
     * @return
     */
    public static Date getYesterday(Date date) {
        return changeDateTime(date, 0, -1, 0, 0, 0);
    }

    public static long getFixedTime(int runAtTime) {
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, runAtTime);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if (c.getTimeInMillis() < now.getTime()) {
            c.setTimeInMillis(c.getTimeInMillis() + 1000 * 3600 * 24 * 1);
        }
        return c.getTimeInMillis() - now.getTime();
    }

    /**
     * 获取两个日期相隔的天数
     *
     * @param d1
     * @param d2
     * @return
     * @throws Exception
     */
    public static int daysBetween(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(d2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Math.abs(Integer.parseInt(String.valueOf(between_days)));
    }

    /**
     * 获得较小的日期
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        if (d1.compareTo(d2) < 0) {
            return d1;
        }
        return d2;
    }

    /**
     * 获得较大的日期
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        if (d1.compareTo(d2) > 0) {
            return d1;
        }
        return d2;
    }

//	public static void main(String[] args) {
//		System.out.println(date2String(new Date(), "yyyyMM"));
//	}

    /**
     * 判断今天是否当月的1号
     */
    public static boolean todayIsFirstInMonth() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (day == 1) return true;
        return false;
    }
}
