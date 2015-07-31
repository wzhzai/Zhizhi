package com.cleverzone.zhizhi.util;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by WANGZHENGZE on 2015/7/31.
 */
public class ViewEnableController {
    private ArrayList<View> mViewArrayList;

    public ViewEnableController() {
        mViewArrayList = new ArrayList<>();
    }

    public void registerView(View view) {
        mViewArrayList.add(view);
    }

    public void unRegisterView(View view) {
        mViewArrayList.remove(view);
    }

    public void clearAll() {
        mViewArrayList.clear();
    }

    public void setAllEnable() {
        for (View view : mViewArrayList) {
            view.setEnabled(true);
        }
    }

    public void setAllDisable() {
        for (View view : mViewArrayList) {
            view.setEnabled(false);
        }
    }
}
