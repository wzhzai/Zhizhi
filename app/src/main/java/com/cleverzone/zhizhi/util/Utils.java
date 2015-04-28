package com.cleverzone.zhizhi.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WANGZHENGZE on 2015/4/16.
 * 工具类
 */
public class Utils {
    /**
     * 判断sd卡是否存在
     * @return 存在返回true
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static Calendar parseStringToCalendar(String s) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Const.NORMAL_SIMPLE_DATE_FORMAT.parse(s));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static int getDayDifference(String stringDate) {
        Calendar nowCalendar = Calendar.getInstance();
        long l = parseStringToCalendar(stringDate).getTimeInMillis() - nowCalendar.getTimeInMillis();
        return Long.valueOf(l / (1000 * 60 * 60 * 24)).intValue();
    }

}
