package com.cleverzone.zhizhi.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

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

}
