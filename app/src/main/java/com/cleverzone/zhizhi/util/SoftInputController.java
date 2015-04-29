package com.cleverzone.zhizhi.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by WANGZHENGZE on 2015/4/29.
 */
public class SoftInputController {
    public static void hideSoftInput(Context mContext,View v){
        if (mContext == null || v == null) {
            return;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showSoftInput(Context mContext,View v){
        if (mContext == null || v == null) {
            return;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(v, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
