package com.cleverzone.zhizhi.comui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.cleverzone.zhizhi.R;

/**
 * Created by WANGZHENGZE on 2015/7/24.
 */
public class LoadingDialog extends ProgressDialog {
    public LoadingDialog(Context context) {
        super(context);
        init();
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        setCancelable(false);
    }

    public static LoadingDialog createDialog(Context context) {
        return new LoadingDialog(context, R.style.CircleProgressTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }
}
