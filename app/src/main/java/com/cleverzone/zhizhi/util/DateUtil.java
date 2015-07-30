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

    public static int getTenTimestamp() {
        Calendar calendar = Calendar.getInstance();
        Long l = calendar.getTimeInMillis() / 1000;
        return l.intValue();
    }
}
