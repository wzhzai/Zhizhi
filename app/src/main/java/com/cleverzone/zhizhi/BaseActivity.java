package com.cleverzone.zhizhi;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by WANGZHENGZE on 2015/4/26.
 */
public class BaseActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void setTitle(int resId) {
        ImageView ivTitle = (ImageView) findViewById(R.id.main_title_center_bottom);
        ivTitle.setImageResource(resId);
    }

    public void setLeftButton() {
        TextView tvLeft = (TextView) findViewById(R.id.main_title_left_bottom);
    }

    public void setRightButton() {
        TextView tvRight = (TextView) findViewById(R.id.main_title_right_bottom);

    }

}
