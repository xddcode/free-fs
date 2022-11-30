package com.free.fs.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author hao.ding@insentek.com
 * @date 2022-04-22 13:50
 */
@Slf4j
public class DateTimeUtils {

    public static final long MILLIS_SECOND = 1000;

    public static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    public static final long MILLIS_HOUR = 60 * MILLIS_MINUTE;
    public static final long MILLIS_DAY = 24 * MILLIS_HOUR;
    public static final long MILLIS_WEEK = 7 * MILLIS_DAY;
    public static final long MILLIS_MONTH = 30 * MILLIS_DAY;

    public static final String DATE_TIME_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_STR = "yyyy-MM-dd";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_STR);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_STR);

    /**
     * 时间增加
     *
     * @param date
     *            日期
     * @param seconds
     *            秒
     * @return date
     */
    public static Date add(Date date, long seconds) {
        long time = date.getTime();
        return new Date(time + seconds * 1000);
    }

    public static String getFormatTime(String oldDate, String oldFormat, String newFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
            Date date = sdf.parse(oldDate);
            sdf = new SimpleDateFormat(newFormat);
            return sdf.format(date);
        } catch (ParseException ex) {
            log.error("Date format conversion failure! {} {} -> {}", ex);
            return null;
        }
    }

    /**
     * 字符串转时间戳到毫秒级
     * 
     * @param time
     *            时间串
     * @return long 毫秒级时间戳
     */
    public static Long toLongDateStr(String time) {
        if (time.length() == DATE_TIME_STR.length()) {
            LocalDateTime dateTime = stringToDateTime(time);
            return dateTimeToEpochMilli(dateTime);
        }
        LocalDate date = stringToDate(time);
        return dateToEpochMilli(date);

    }

    /**
     * 获取到毫秒级时间戳
     * 
     * @param localDate
     *            具体时间
     * @return long 毫秒级时间戳
     */
    public static long dateToEpochMilli(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
    }

    /**
     * 获取到毫秒级时间戳
     * 
     * @param localDateTime
     *            具体时间
     * @return long 毫秒级时间戳
     */
    public static long dateTimeToEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 时间字符串 转LocalDateTime
     * 
     * @param localDateTime
     *            时间字符串
     * @return LocalDateTime
     */
    public static LocalDateTime stringToDateTime(String localDateTime) {
        return LocalDateTime.parse(localDateTime, DATE_TIME_FORMATTER);
    }

    /**
     * 时间字符串 转LocalDateTime
     * 
     * @param localDateTime
     *            时间字符串
     * @return LocalDateTime
     */
    public static LocalDate stringToDate(String localDateTime) {
        return LocalDate.parse(localDateTime, DATE_FORMATTER);
    }

    /**
     * 转换带T的日期的格式
     * 
     * @param timet
     *            时间串
     */
    public static String formatIncludeTTime(String timet) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat(DATE_TIME_STR);
            Date date = sdf1.parse(timet);
            return sdf2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timet;
    }

    /**
     * 判断是否是时间格式
     * 
     * @param dateString
     *            时间串
     * @return boolean 是否是时间格式
     */
    public static boolean isValidDateStr(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_STR);
        try {
            df.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 字符串时间戳转日期格式
     * 
     * @param timestamp
     *            字符串时间戳转
     * @return String 日期格式
     */
    public static String formatTimeStamp(String timestamp) {
        // Instant.ofEpochMilli 转 毫秒， Instant.ofEpochSecond 转 秒
        LocalDateTime localDateTime =
            Instant.ofEpochMilli(Long.parseLong(timestamp)).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * 判断字符串内容是否为时间串
     * 
     * @param timestamp
     *            字符串时间戳转
     * @return boolean 是否为日期格式
     */
    public static boolean isValidTimeStamp(String timestamp) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_STR);
        try {
            df.format(new Date(Long.parseLong(String.valueOf(timestamp))));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long formatTime(String dataStr, String simpleDateFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(simpleDateFormat);
            return df.parse(dataStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatTime(Date dataStr, String simpleDateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(simpleDateFormat);
        return df.format(dataStr);
    }

    public static Date formatTime(Long time, String simpleDateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(simpleDateFormat);
        //设置时区为 UTC
        String format = df.format(time);
        Date parse;
        try {
            parse = df.parse(format);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return parse;
    }

    /**
     * 由过去的某一时间,计算距离当前的时间
     */
    public static String calculateTime(Date time) {
        if (time == null) {
            return null;
        }
        // 获取当前时间的毫秒数
        long nowTime = System.currentTimeMillis();
        String msg = null;
        // 获取指定时间的毫秒数
        long reset = time.getTime();
        long dateDiff = nowTime - reset;
        if (dateDiff < 0) {
            msg = "输入的时间不对";
        } else {
            // 秒
            long dateTemp1 = dateDiff / 1000;
            // 分钟
            long dateTemp2 = dateTemp1 / 60;
            // 小时
            long dateTemp3 = dateTemp2 / 60;
            // 天数
            long dateTemp4 = dateTemp3 / 24;
            // 月数
            long dateTemp5 = dateTemp4 / 30;
            // 年数
            long dateTemp6 = dateTemp5 / 12;
            if (dateTemp6 > 0) {
                msg = dateTemp6 + " year ago";
            } else if (dateTemp5 > 0) {
                msg = dateTemp5 + " month ago";
            } else if (dateTemp4 > 0) {
                msg = dateTemp4 + " day ago";
            } else if (dateTemp3 > 0) {
                msg = dateTemp3 + " hour ago";
            } else if (dateTemp2 > 0) {
                msg = dateTemp2 + " minute ago";
            } else if (dateTemp1 > 0) {
                msg = "just";
            }
        }
        return msg;
    }

    /**
     * 格式化日期
     * 
     * @param dateStr
     *            String 字符型日期
     * @param format
     *            String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception ignored) {
        }
        return date;
    }
}
