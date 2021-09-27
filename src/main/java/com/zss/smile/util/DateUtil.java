package com.zss.smile.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author ZSS
 * @date 2021/9/6 15:30
 * @desc 时间工具类
 */
@SuppressWarnings("unused")
public class DateUtil {

    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String DATE_FORMAT_COMPLETE = "yyyy-MM-dd HH:mm:ss";
    private static final Long THIRTY_DAYS_TIMESTAMP = 30L * 24L * 60 * 60L * 1000L;
    private static final Long ONE_DAY = 1000L * 60L * 60L * 24L;

    /**
     * 获取当前时间戳
     *
     * @return 13位时间戳
     */
    public static String currentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 30天前的时间戳
     *
     * @return 13位时间戳
     */
    public static String thirtyDaysAgo() {
        return String.valueOf(System.currentTimeMillis() - THIRTY_DAYS_TIMESTAMP);
    }

    /**
     * 与现在相隔的天数
     *
     * @param timestamp 时间戳
     * @return 先查天数
     */
    public static Integer daysFromNow(String timestamp) {
        long days = System.currentTimeMillis() - Long.parseLong(timestamp);
        return (int) (days / ONE_DAY);
    }

    /**
     * 获取当前时间(完整模式)
     *
     * @return String
     */
    public static String getDateComplete() {
        DateTime dateTime = new DateTime((new Date()));
        return dateTime.toString(DATE_FORMAT_COMPLETE);
    }

    /**
     * 时间戳转String
     *
     * @param timestamp 时间戳
     * @return String
     */
    public static String timestampToDate(String timestamp) {
        Date date = new Date(Long.parseLong(timestamp));
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(DATE_FORMAT_COMPLETE);
    }
}
