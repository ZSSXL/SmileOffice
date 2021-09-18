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

    /**
     * 获取当前时间戳
     *
     * @return 13位时间戳
     */
    public static String currentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
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
