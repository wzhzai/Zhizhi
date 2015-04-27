package com.cleverzone.zhizhi;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cleverzone.zhizhi.sqlite.DBManager;
import com.cleverzone.zhizhi.util.Const;


public class RecordItemDetail extends BaseActivity {

    private static final String TAG = "RecordItemDetail";

    private Context mContext;
    private String mMainClassify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_item_detail);
        mContext = this;
        mMainClassify = mContext.getString(Const.RECORD_CLASSIFIES_TEXT[getIntent().getIntExtra("which", 0)]);
        setTitle(R.mipmap.title_bar_icon_record);

        Log.e(TAG, "count=" + DBManager.getInstance(mContext).getProductInfoCount(mMainClassify));
        DBManager.getInstance(mContext).getAllProductInfoByMainClassify(mMainClassify);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
