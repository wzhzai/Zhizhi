package com.cleverzone.zhizhi.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * Created by WANGZHENGZE on 2015/4/16.
 * ������
 */
public class Utils {
    /**
     * �ж�sd���Ƿ����
     * @return ���ڷ���true
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
