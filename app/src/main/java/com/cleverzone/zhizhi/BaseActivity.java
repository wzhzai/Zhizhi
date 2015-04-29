package com.cleverzone.zhizhi;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by WANGZHENGZE on 2015/4/26.
 */
public class BaseActivity extends FragmentActivity {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void setTitle(int resId) {
        ImageView ivTitle = (ImageView) findViewById(R.id.main_title_center_bottom);
        ivTitle.setImageResource(resId);
    }

    public void setLeftButtonHide() {
        setLeftButton(null, null, View.GONE);
    }

    public void setLeftButton(int resId, View.OnClickListener l) {
        setLeftButton(getString(resId), l, View.VISIBLE);
    }

    public void setLeftButton(String s, View.OnClickListener l, int visibility) {
        TextView tvLeft = (TextView) findViewById(R.id.main_title_left_bottom);
        if (visibility == View.GONE) {
            tvLeft.setVisibility(View.GONE);
        } else {
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(s);
            tvLeft.setOnClickListener(l);
        }
    }

    public void setRightButtonHide() {
        setRightButton(null, null, View.GONE);
    }

    public void setRightButton(int resId, View.OnClickListener l) {
        setRightButton(getString(resId), l, View.VISIBLE);
    }

    public void setRightButton(String s, View.OnClickListener l, int visibility) {
        TextView tvRight = (TextView) findViewById(R.id.main_title_right_bottom);
        if (visibility == View.GONE) {
            tvRight.setVisibility(View.GONE);
        } else {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(s);
            tvRight.setOnClickListener(l);
        }
    }

}
