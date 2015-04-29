package com.cleverzone.zhizhi;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleverzone.zhizhi.comui.ClassifyChooseDialog;
import com.cleverzone.zhizhi.fragment.BaseFragment;
import com.cleverzone.zhizhi.fragment.MeFragment;
import com.cleverzone.zhizhi.fragment.MessageFragment;
import com.cleverzone.zhizhi.fragment.RecordFragment;
import com.cleverzone.zhizhi.fragment.ZhizhiFragment;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends BaseActivity {

    private final static String TAG = "MainActivity";
    private final static int MAX_TAB_COUNT = 4;

    private ViewPager mViewPager;
    private Context mContext;
    private HashMap<Integer, BaseFragment> mFragmentMap;
    private MainTitleBarController mTitleBarController;
    public View.OnClickListener mOnAddProductClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new ClassifyChooseDialog(mContext).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentMap = new HashMap<>();
        initAllViews();
    }

    private void initAllViews() {
        mContext = this;
        mTitleBarController = new MainTitleBarController();
        initViewPager();
    }

    private class ColorEquationBean {
        public int k;
        public int b;
    }

    /***
     * list contains params r, g, b
     * @param startColorInt the changing color start value
     * @param endColorInt the changing color end value
     * @return ArrayList<ColorEquationBean>
     */
    private ArrayList<ColorEquationBean> getAllColorParams(int startColorInt, int endColorInt) {
        ArrayList<ColorEquationBean> list = new ArrayList<>();
        list.add(getEachColorParams(Color.red(startColorInt), Color.red(endColorInt)));
        list.add(getEachColorParams(Color.green(startColorInt), Color.green(endColorInt)));
        list.add(getEachColorParams(Color.blue(startColorInt), Color.blue(endColorInt)));
        return list;
    }

    /***
     * calculate equation's k and b about r or g or b from start to end
     * @param start start r or g or b
     * @param end end r or g or b
     * @return ColorEquationBean
     */
    private ColorEquationBean getEachColorParams(int start, int end) {
        ColorEquationBean colorEquationBean = new ColorEquationBean();
        colorEquationBean.b = start;
        colorEquationBean.k = end - colorEquationBean.b;
        return colorEquationBean;
    }

    private class MainTitleBarController {

        private ImageView mIvTitle;

        public MainTitleBarController() {
            mIvTitle = (ImageView) findViewById(R.id.main_title_center_bottom);
        }

        public void pageChanged(int position) {
            switch (position) {
                case 0:
                    mIvTitle.setImageResource(R.mipmap.title_bar_icon_zhizhi);
                    setLeftButtonHide();
                    setRightButtonHide();
                    break;
                case 1:
                    mIvTitle.setImageResource(R.mipmap.title_bar_icon_record);
                    setRightButton(R.string.record_add_product_text, mOnAddProductClickListener);
                    setLeftButtonHide();
                    break;
                case 2:
                    mIvTitle.setImageResource(R.mipmap.title_bar_icon_message);
                    setLeftButtonHide();
                    setRightButtonHide();
                    break;
                case 3:
                    mIvTitle.setImageResource(R.mipmap.title_bar_icon_me);
                    setLeftButtonHide();
                    setRightButtonHide();
                    break;
            }
        }

    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mViewPager.setOffscreenPageLimit(MAX_TAB_COUNT);
        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        final TabStateBean tabStateBean = initTab();
        setTabSelected(0, tabStateBean);
        setLeftButtonHide();
        setRightButtonHide();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mNormalColor = getResources().getColor(R.color.main_tab_text_normal_color);
            private int mSelectedColor = getResources().getColor(R.color.main_tab_text_selected_color);

            private ArrayList<ColorEquationBean> mColorEquationBeanArrayList = getAllColorParams(mSelectedColor, mNormalColor);

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabStateBean.ivNormalArray[position].setAlpha(positionOffset);
                tabStateBean.ivSelectedArray[position].setAlpha(1 - positionOffset);
                int currentR = (int) (mColorEquationBeanArrayList.get(0).k * positionOffset + mColorEquationBeanArrayList.get(0).b);
                int currentG = (int) (mColorEquationBeanArrayList.get(1).k * positionOffset + mColorEquationBeanArrayList.get(1).b);
                int currentB = (int) (mColorEquationBeanArrayList.get(2).k * positionOffset + mColorEquationBeanArrayList.get(2).b);
                tabStateBean.tvTabNameArray[position].setTextColor(Color.rgb(currentR, currentG, currentB));
                if (position != MAX_TAB_COUNT - 1) {
                    int nextR = (int) (mColorEquationBeanArrayList.get(0).k * (1 - positionOffset) + mColorEquationBeanArrayList.get(0).b);
                    int nextG = (int) (mColorEquationBeanArrayList.get(1).k * (1 - positionOffset) + mColorEquationBeanArrayList.get(1).b);
                    int nextB = (int) (mColorEquationBeanArrayList.get(2).k * (1 - positionOffset) + mColorEquationBeanArrayList.get(2).b);
                    tabStateBean.ivNormalArray[position + 1].setAlpha(1 - positionOffset);
                    tabStateBean.ivSelectedArray[position + 1].setAlpha(positionOffset);
                    tabStateBean.tvTabNameArray[position + 1].setTextColor(Color.rgb(nextR, nextG, nextB));
                }
            }

            @Override
            public void onPageSelected(int position) {
                setTabSelected(position, tabStateBean);
                mTitleBarController.pageChanged(position);
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
                    if (mFragmentMap.containsKey(position)) {
                        fragment = mFragmentMap.get(position);
                    } else {
                        fragment = ZhizhiFragment.newInstance("", "");
                        mFragmentMap.put(position, fragment);
                    }
                    break;
                case 1:
                    if (mFragmentMap.containsKey(position)) {
                        fragment = mFragmentMap.get(position);
                    } else {
                        fragment = RecordFragment.newInstance("", "");
                        mFragmentMap.put(position, fragment);
                    }
                    break;
                case 2:
                    if (mFragmentMap.containsKey(position)) {
                        fragment = mFragmentMap.get(position);
                    } else {
                        fragment = MessageFragment.newInstance("", "");
                        mFragmentMap.put(position, fragment);
                    }
                    break;
                case 3:
                    if (mFragmentMap.containsKey(position)) {
                        fragment = mFragmentMap.get(position);
                    } else {
                        fragment = MeFragment.newInstance("", "");
                        mFragmentMap.put(position, fragment);
                    }
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
