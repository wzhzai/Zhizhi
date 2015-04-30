package com.cleverzone.zhizhi.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cleverzone.zhizhi.AddProductActivity;
import com.cleverzone.zhizhi.R;
import com.cleverzone.zhizhi.comui.BaseListChooseDialog;

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

    public static AdapterView.OnItemClickListener getOnClassifyItemClickListener(final Context context) {
        return new AbsListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, AddProductActivity.class);
                intent.putExtra("which", position);
                intent.putExtra("mode", AddProductActivity.MODE_ADD);
                context.startActivity(intent);
            }
        };
    }

    public static void addProductChooseClassify(Context context) {
        new BaseListChooseDialog.CBuilder(context).setTitle(R.string.record_choose_classify_text)
                .setContents(Const.RECORD_CLASSIFIES_TEXT).setIcons(Const.RECORD_CLASSIFIES_ICON)
                .setOnItemClickListener(getOnClassifyItemClickListener(context)).show();
    }

}
