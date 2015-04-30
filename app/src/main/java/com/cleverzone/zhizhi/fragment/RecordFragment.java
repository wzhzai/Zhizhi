package com.cleverzone.zhizhi.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleverzone.zhizhi.R;
import com.cleverzone.zhizhi.RecordItemDetail;
import com.cleverzone.zhizhi.comui.KCalendar;
import com.cleverzone.zhizhi.sqlite.DBManager;
import com.cleverzone.zhizhi.util.Const;
import com.cleverzone.zhizhi.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "RecordFragment";

    private Context mContext;
    private String mDate = null;

    private String mParam1;
    private String mParam2;

    private ArrayList<TextView> mHintTextViewList;
    private KCalendar mKCalendar;
    private List<String> mMarkList;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHintTextViewList = new ArrayList<>();
        initCalendar(view);
        initOtherViews(view);
        Log.e(TAG, "onViewCreated");

    }

    private View.OnClickListener mOnAddProductClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.addProductChooseClassify(mContext);
        }
    };

    private void initOtherViews(View view) {
        Button btAddProduct = (Button) view.findViewById(R.id.record_bt_add_product);
        btAddProduct.setOnClickListener(mOnAddProductClickListener);
        ArrayList<View> itemList = new ArrayList<>();
        itemList.add(view.findViewById(R.id.record_makeup));
        itemList.add(view.findViewById(R.id.record_food));
        itemList.add(view.findViewById(R.id.record_medicine));
        itemList.add(view.findViewById(R.id.record_others));
        for (int i = 0; i < itemList.size(); i++) {
            setEachItemClickListener(itemList.get(i), i);
        }

        mHintTextViewList.add((TextView)view.findViewById(R.id.record_makeup_recent_hint));
        mHintTextViewList.add((TextView) view.findViewById(R.id.record_food_recent_hint));
        mHintTextViewList.add((TextView) view.findViewById(R.id.record_medicine_recent_hint));
        mHintTextViewList.add((TextView) view.findViewById(R.id.record_others_recent_hint));

    }

    private void setEachItemClickListener(View view, final int which) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countIsZero(which)) {
                    Toast.makeText(mContext, "还没有记录哦~~", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, RecordItemDetail.class);
                    intent.putExtra("which", which);
                    startActivity(intent);
                }
            }
        };
        view.setOnClickListener(onClickListener);
    }

    private boolean countIsZero(int which) {
        return DBManager.getInstance(mContext).getProductInfoCount(mContext.getString(Const.RECORD_CLASSIFIES_TEXT[which])) == 0;
    }


    private void initCalendar(View view) {
        final TextView popupwindow_calendar_month = (TextView) view
                .findViewById(R.id.popupwindow_calendar_month);
        mKCalendar = (KCalendar) view
                .findViewById(R.id.popupwindow_calendar);

        popupwindow_calendar_month.setText(mKCalendar.getCalendarYear() + "年"
                + mKCalendar.getCalendarMonth() + "月");

        if (null != mDate) {

            int years = Integer.parseInt(mDate.substring(0,
                    mDate.indexOf("-")));
            int month = Integer.parseInt(mDate.substring(
                    mDate.indexOf("-") + 1, mDate.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");

            mKCalendar.showCalendar(years, month);
            mKCalendar.setCalendarDayBgColor(mDate, R.drawable.calendar_date_focused);
        }

//        mMarkList = DBManager.getInstance(mContext).getAllRecentHintDate();
//        mKCalendar.addMarks(mMarkList, 0);

        //监听所选中的日期
        mKCalendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                int month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf("-") + 1,
                        dateFormat.lastIndexOf("-")));

                if (mKCalendar.getCalendarMonth() - month == 1//跨年跳转
                        || mKCalendar.getCalendarMonth() - month == -11) {
                    mKCalendar.lastMonth();

                } else if (month - mKCalendar.getCalendarMonth() == 1 //跨年跳转
                        || month - mKCalendar.getCalendarMonth() == -11) {
                    mKCalendar.nextMonth();

                } else {
                    mKCalendar.removeAllBgColor();
                    mKCalendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
                    mDate = dateFormat;//最后返回给全局 mDate
                }
            }
        });

        //监听当前月份
        mKCalendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month
                        .setText(year + "年" + month + "月");
            }
        });

        //上月监听按钮
        RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_last_month);
        popupwindow_calendar_last_month
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        mKCalendar.lastMonth();
                    }

                });

        //下月监听按钮
        RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        mKCalendar.nextMonth();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        mMarkList = DBManager.getInstance(mContext).getAllRecentHintDate();
        mKCalendar.addMarks(mMarkList, 0);
        int index = 0;
        for (int i : Const.RECORD_CLASSIFIES_TEXT) {
            String mainClassify = mContext.getString(i);
            String hintDate = DBManager.getInstance(mContext).getRecentHintDateByMainClassify(mainClassify);
            if (TextUtils.isEmpty(hintDate)) {
                mHintTextViewList.get(index).setText(mContext.getString(R.string.record_recent_hint_no_day_text));
            } else {
                int differentDay = Utils.getDayDifference(hintDate);
                mHintTextViewList.get(index).setText(Html.fromHtml(mContext.getString(R.string.record_recent_hint_text, differentDay)));
                Log.e(TAG, "differentDay = " + differentDay);
            }
            index ++;
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
}
