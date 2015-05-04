package com.cleverzone.zhizhi.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cleverzone.zhizhi.AddProductActivity;
import com.cleverzone.zhizhi.R;
import com.cleverzone.zhizhi.comui.BaseListChooseDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

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

    public static boolean isNetConnect(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) (context)
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connManager.getActiveNetworkInfo();
            if ((netInfo == null || !netInfo.isConnected())) {
            } else if (netInfo.getTypeName().toUpperCase().equals("MOBILE")
                    && netInfo.getExtraInfo() != null
                    && netInfo.getExtraInfo().equals("cmwap")) {
                return true;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(context, R.string.no_net_hint, Toast.LENGTH_SHORT).show();
        return false;
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

    public static DisplayImageOptions getDeafultImageLoaderOptions() {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();
        return options;

    }

}
