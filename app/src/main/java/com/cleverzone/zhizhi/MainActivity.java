package com.cleverzone.zhizhi;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleverzone.zhizhi.fragment.BaseFragment;
import com.cleverzone.zhizhi.fragment.MeFragment;
import com.cleverzone.zhizhi.fragment.MessageFragment;
import com.cleverzone.zhizhi.fragment.RecordFragment;
import com.cleverzone.zhizhi.fragment.ZhizhiFragment;


public class MainActivity extends ActionBarActivity {

    private final static String TAG = "MainActivity";
    private final static int MAX_TAB_COUNT = 4;

    private ViewPager mViewPager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAllViews();
    }

    private void initAllViews() {
        mContext = this;
        initViewPager();
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        final TabStateBean tabStateBean = initTab();
        setTabSelected(0, tabStateBean);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabStateBean.ivNormalArray[position].setAlpha(positionOffset);
                tabStateBean.ivSelectedArray[position].setAlpha(1 - positionOffset);
                int normalR = (int) (84 * positionOffset + 69);
                int normalG = (int) (-39 * positionOffset + 192);
                int normalB = (int) (127 * positionOffset + 26);
                tabStateBean.tvTabNameArray[position].setTextColor(Color.rgb(normalR, normalG, normalB));
                if (position != MAX_TAB_COUNT - 1) {
                    int selectedR = (int) (84 * (1 - positionOffset) + 69);
                    int selectedG = (int) (-39 * (1 - positionOffset) + 192);
                    int selectedB = (int) (127 * (1 - positionOffset) + 26);
                    tabStateBean.ivNormalArray[position + 1].setAlpha(1 - positionOffset);
                    tabStateBean.ivSelectedArray[position + 1].setAlpha(positionOffset);
                    tabStateBean.tvTabNameArray[position + 1].setTextColor(Color.rgb(selectedR, selectedG, selectedB));
                }
            }

            @Override
            public void onPageSelected(int position) {
                setTabSelected(position, tabStateBean);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTabSelected(int position, TabStateBean tabStateBean) {
        for (int i = 0; i < MAX_TAB_COUNT; i++) {
            if (i == position) {
                tabStateBean.ivSelectedArray[i].setAlpha(1f);
                tabStateBean.ivNormalArray[i].setAlpha(0f);
                tabStateBean.tvTabNameArray[i].setTextColor(getResources().getColor(R.color.main_tab_text_selected_color));
            } else {
                tabStateBean.ivSelectedArray[i].setAlpha(0f);
                tabStateBean.ivNormalArray[i].setAlpha(1f);
                tabStateBean.tvTabNameArray[i].setTextColor(getResources().getColor(R.color.main_tab_text_normal_color));
            }
        }
    }

    private class TabStateBean {
        public ImageView[] ivSelectedArray;
        public ImageView[] ivNormalArray;
        public TextView[] tvTabNameArray;
        public View[] tabViewArray;
    }

    private TabStateBean initTab() {
        TabStateBean tabStateBean = new TabStateBean();
        tabStateBean.ivSelectedArray = new ImageView[MAX_TAB_COUNT];
        tabStateBean.ivNormalArray = new ImageView[MAX_TAB_COUNT];
        tabStateBean.tvTabNameArray = new TextView[MAX_TAB_COUNT];
        tabStateBean.tabViewArray = new View[MAX_TAB_COUNT];

        tabStateBean.ivSelectedArray[0] = (ImageView) findViewById(R.id.tab_iv_zhizhi_fake);
        tabStateBean.ivSelectedArray[1] = (ImageView) findViewById(R.id.tab_iv_record_fake);
        tabStateBean.ivSelectedArray[2] = (ImageView) findViewById(R.id.tab_iv_message_fake);
        tabStateBean.ivSelectedArray[3] = (ImageView) findViewById(R.id.tab_iv_me_fake);

        tabStateBean.ivNormalArray[0] = (ImageView) findViewById(R.id.tab_iv_zhizhi);
        tabStateBean.ivNormalArray[1] = (ImageView) findViewById(R.id.tab_iv_record);
        tabStateBean.ivNormalArray[2] = (ImageView) findViewById(R.id.tab_iv_message);
        tabStateBean.ivNormalArray[3] = (ImageView) findViewById(R.id.tab_iv_me);

        tabStateBean.tvTabNameArray[0] = (TextView) findViewById(R.id.tab_tv_zhizhi);
        tabStateBean.tvTabNameArray[1] = (TextView) findViewById(R.id.tab_tv_record);
        tabStateBean.tvTabNameArray[2] = (TextView) findViewById(R.id.tab_tv_message);
        tabStateBean.tvTabNameArray[3] = (TextView) findViewById(R.id.tab_tv_me);

        tabStateBean.tabViewArray[0] = findViewById(R.id.tab_zhizhi);
        tabStateBean.tabViewArray[1] = findViewById(R.id.tab_record);
        tabStateBean.tabViewArray[2] = findViewById(R.id.tab_message);
        tabStateBean.tabViewArray[3] = findViewById(R.id.tab_me);

        for (int i = 0; i < tabStateBean.tabViewArray.length; i++) {
            View view = tabStateBean.tabViewArray[i];
            view.setOnClickListener(new OnTabClickListener(i));
        }

        return tabStateBean;
    }

    private class OnTabClickListener implements View.OnClickListener {
        int mPosition;

        public OnTabClickListener(int p) {
            mPosition = p;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(mPosition, false);
        }
    }

    private class MainViewPagerAdapter extends FragmentPagerAdapter {

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = null;
            switch (position) {
                case 0:
                    fragment = ZhizhiFragment.newInstance("", "");
                    break;
                case 1:
                    fragment = RecordFragment.newInstance("", "");
                    break;
                case 2:
                    fragment = MessageFragment.newInstance("", "");
                    break;
                case 3:
                    fragment = MeFragment.newInstance("", "");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return MAX_TAB_COUNT;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
