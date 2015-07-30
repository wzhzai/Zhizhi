package com.cleverzone.zhizhi.util;

import com.cleverzone.zhizhi.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by WANGZHENGZE on 2015/4/27.
 */
public class Const {
    public static final int[] RECORD_CLASSIFIES_ICON = {R.mipmap.icon_makeup, R.mipmap.icon_food, R.mipmap.icon_medicine, R.mipmap.icon_others};
    public static final int[] RECORD_CLASSIFIES_TEXT = {R.string.record_makeup_text, R.string.record_food_text, R.string.record_medicine_text, R.string.record_others_text};
    public static final SimpleDateFormat NORMAL_SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static final int SHELF_LIFE_TYPE_DAY = 0;
    public static final int SHELF_LIFE_TYPE_MONTH = 1;
    public static final int SHELF_LIFE_TYPE_YEAR = 2;

}
