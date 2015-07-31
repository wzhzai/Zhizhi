package com.cleverzone.zhizhi.util;

import java.util.Calendar;

/**
 * Created by WANGZHENGZE on 2015/7/30.
 */
public class DateUtil {
    public static int getTenTimestampWithGivenParams(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        Long l = calendar.getTimeInMillis() / 1000;
        return l.intValue();
    }

    public static String getDateStringWithGivenParams(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        return Const.NORMAL_SIMPLE_DATE_FORMAT.format(calendar.getTime());
    }

    public static String getDateString(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return Const.NORMAL_SIMPLE_DATE_FORMAT.format(calendar.getTime());
    }

    public static String getDateString(int timestamp) {
        return getDateString(timestamp * 1000l);
    }

    public static String getDateString() {
        return getDateString(Calendar.getInstance().getTimeInMillis());
    }

    public static int getTenTimestamp() {
        Calendar calendar = Calendar.getInstance();
        Long l = calendar.getTimeInMillis() / 1000;
        return l.intValue();
    }

    public static int getTenTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Long l = calendar.getTimeInMillis() / 1000;
        return l.intValue();
    }

    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static int getDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int add(int timestamp, int year, int month, int day) {
        return add(timestamp * 1000l, year, month, day);
    }

    public static int add(long timestamp, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.YEAR, year);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return getTenTimestamp(calendar.getTimeInMillis());
    }
}
