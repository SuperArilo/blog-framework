package com.tty.common.utils;
import java.util.Calendar;

/**
 * 时间工具类
 * @version 1.0
 */
public class DateUtil {

    private DateUtil() {
        throw new AssertionError("No com.superarilo.util.DateUtil instances for you!");
    }

    /**
     * 时区 GMT+8
     */
    public static final String TIMEZONE_GMT_8 = "GMT+8";

    /**
     * 横线 -
     */
    private static final String TRANSVERSE_LINE = "-";

    /**
     * 斜线 /
     */
    private static final String OBLIQUE_LINE  = "/";

    /**
     * 冒号分隔符
     */
    private static final String TIME_SEPARATOR_COLON = ":";

    private static final String A_SPACE = " ";

    /**
     * 月
     */
    private static final String MM = "MM";

    /**
     * 日
     */
    private static final String DD = "dd";

    /**
     * 12小时制
     */
    private static final String HOUR_12 = "hh";

    /**
     * 24小时制
     */
    private static final String HOUR_24 = "HH";

    /**
     * 分钟
     */
    private static final String MINUTE = "mm";

    /**
     * 秒
     */
    private static final String SECOND = "ss";

    /**
     * 时分
     */
    private static final String HOUR_MINUTE_TIME_SEPARATOR_COLON = HOUR_24 + TIME_SEPARATOR_COLON + MINUTE;

    /**
     * 时分秒
     */
    private static final String HOUR_MINUTE_SECOND_TIME_SEPARATOR_COLON = HOUR_MINUTE_TIME_SEPARATOR_COLON
        + TIME_SEPARATOR_COLON + SECOND;

    /**
     * 年份（后两位）
     */
    public static final String YY_PATTERN = "yy";

    /**
     * 年份（四位）
     */
    public static final String YYYY_PATTERN = YY_PATTERN + YY_PATTERN;

    /**
     * 年月(以 短横线 ‘-’ 分割)
     */
    public static final String YYYY_MM_PATTERN = YYYY_PATTERN + TRANSVERSE_LINE + MM;

    /**
     * 年月(以 斜线 ‘/’ 分割)
     */
    public static final String YYYY_MM_BIAS_PATTERN = YYYY_PATTERN + OBLIQUE_LINE + MM;
    /**
     * 年月日(以 短横线 ‘-’ 分割)
     */
    public static final String YYYY_MM_DD_PATTERN = YYYY_MM_PATTERN + TRANSVERSE_LINE + DD;

    /**
     * 年月日(以 斜线 ‘/’ 分割)
     */
    public static final String YYYY_MM_DD_BIAS_PATTERN = YYYY_MM_BIAS_PATTERN + OBLIQUE_LINE + DD;

    /**
     * 年月日时分秒(以 短横线 ‘-’ 分割)
     */
    public static final String YYYY_MM_DD_HH_MM_SS_PATTERN = YYYY_MM_DD_PATTERN + A_SPACE
        + HOUR_MINUTE_SECOND_TIME_SEPARATOR_COLON;

    /**
     * 年月日时分秒(以 斜线 ‘/’ 分割)
     */
    public static final String YYYY_MM_DD_HH_MM_SS_BIAS_PATTERN = YYYY_MM_DD_BIAS_PATTERN + A_SPACE
        + HOUR_MINUTE_SECOND_TIME_SEPARATOR_COLON;

    /**
     * 一天二十四小时的秒数
     * @return 返回秒数
     */
    public static long theNumberOfSecondsInADay() {
        return 24 * 60 * 60L;
    }

    /**
     * 计算当天剩余秒数 如果当天时间小于两小时，则在追加一天时间
     * @return 返回秒数
     */
    public static long theRestOfTheDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endTime = calendar.getTime().getTime();
        long currentTime = System.currentTimeMillis();
        long time = endTime - currentTime;
        return time / 1000 > 2 * 60 * 60 ? time : time + theNumberOfSecondsInADay();
    }
}
