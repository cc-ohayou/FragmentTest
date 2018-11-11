package space.cc.com.fragmenttest.domain.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import space.cc.com.fragmenttest.domain.BuildConfig;

/**
 * 时间相关工具类
 * Created by yaojian on 2017/9/20 20:16
 */

public class TimeUtils {

    /**
     * 毫秒与毫秒的倍数
     */
    public static final int MSEC = 1;
    /**
     * 秒与毫秒的倍数
     */
    public static final int SEC  = 1000;
    /**
     * 分与毫秒的倍数
     */
    public static final int MIN  = 60000;
    /**
     * 时与毫秒的倍数
     */
    public static final int HOUR = 3600000;
    /**
     * 天与毫秒的倍数
     */
    public static final int DAY  = 86400000;


    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final DateFormat SIMULAT_FORMAT = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());//模拟策略的时间戳

    private TimeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    public static String millis2String(final long millis) {
        return millis2String(millis, DEFAULT_FORMAT);
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为format</p>
     *
     * @param millis 毫秒时间戳
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String millis2String(final long millis, final DateFormat format) {
        return format.format(new Date(millis));
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    public static long string2Millis(final String time) {
        return string2Millis(time, DEFAULT_FORMAT);
    }


    public static String simulatTime(final String time) {
//        long shijianchuo= string2Millis(time, DEFAULT_FORMAT);
//        return millis2String(shijianchuo, SIMULAT_FORMAT);
        return  time;
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为format</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Millis(final String time, final DateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将时间字符串转为Date类型
     * <p>time格式为format</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return Date类型
     */
    public static Date string2Date(final String time, final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取合适型两个时间差
     *
     * @param millis0   毫秒时间戳1
     * @param millis1   毫秒时间戳2
     * @param precision 精度
     *                  <p>precision = 0，返回null</p>
     *                  <p>precision = 1，返回天</p>
     *                  <p>precision = 2，返回天和小时</p>
     *                  <p>precision = 3，返回天、小时和分钟</p>
     *                  <p>precision = 4，返回天、小时、分钟和秒</p>
     *                  <p>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</p>
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(final long millis0, final long millis1, final int precision) {
        return millis2FitTimeSpan(Math.abs(millis0 - millis1), precision);
    }

    /**
     * 获取当前毫秒时间戳
     *
     * @return 毫秒时间戳
     */
    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @return 时间字符串
     */
    public static String getNowString() {
        return millis2String(System.currentTimeMillis(), DEFAULT_FORMAT);
    }

    /**
     * 获取当前时间字符串
     * <p>格式为format</p>
     *
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String getNowString(final DateFormat format) {
        return millis2String(System.currentTimeMillis(), format);
    }

    private static String millis2FitTimeSpan(long millis, int precision) {
        if (millis < 0 || precision <= 0) return null;
        precision = Math.min(precision, 5);
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        if (millis == 0) return 0 + units[precision - 1];
        StringBuilder sb = new StringBuilder();
        int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 判断现在是否是股票交易时间
     * 9:30-11:30、13:00-15:00
     * 除去双休日
     */
    public static boolean isStockTime(){
        if(BuildConfig.DEBUG)
            return true;

        Calendar calendar = Calendar.getInstance();
        long systemTime = System.currentTimeMillis();
        calendar.setTimeInMillis(systemTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == 1 || day == 7)
            return false;

        if (hour == 11 && min <= 30)
            return true;

        if (hour == 9 && min >= 30)
            return true;

        if (hour == 10)
            return true;

        if (hour > 12 && hour < 15)
            return true;

        return false;
    }

    /**
     * 判断现在是否是股票交易时间
     * 9:00-14:40
     * 除去双休日
     */
    public static boolean isStockTime2(){
        if(BuildConfig.DEBUG)
            return true;

        Calendar calendar = Calendar.getInstance();
        long systemTime = System.currentTimeMillis();
        calendar.setTimeInMillis(systemTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == 1 || day == 7)
            return false;

        if (hour == 14 && min <= 40)
            return true;

        if (hour >= 9 && hour < 14)
            return true;

        return false;
    }

    /**
     * 将日期转化为毫秒
     * @param date		200612241200	,20061224	,2006-12-24,
     * 					2006年12月24日"	,12月24日" ;
     * 					2006-12-24 12:00:59 星期四	,2006年12月24日 12时00分59秒 星期四,
     *
     * @param format	"yyyyMMddhhmm" 	,"yyyyMMdd"	,"yyyy-MM-dd",
     * 					"yyyy年MM月dd日"	,"MM月dd日" ;
     * 					"yyyy-MM-dd HH:mm:ss E"		,"yyyy年MM月dd日 HH时mm分ss秒 E ",
     *
     * @return
     */
    public static long getMillisFromDate(String date , String format) {

        if (StringUtils.isEmpty(date) || "-1".equals(date)) {
            return 0;
        }
        long millionSeconds = -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            millionSeconds = sdf.parse(date).getTime() ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millionSeconds;

    }

}
