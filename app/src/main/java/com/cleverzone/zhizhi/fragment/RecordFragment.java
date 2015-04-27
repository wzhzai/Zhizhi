package com.cleverzone.zhizhi.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cleverzone.zhizhi.AddProductActivity;
import com.cleverzone.zhizhi.R;
import com.cleverzone.zhizhi.RecordItemDetail;
import com.cleverzone.zhizhi.comui.ClassifyChooseDialog;
import com.cleverzone.zhizhi.comui.KCalendar;

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
        initCalendar(view);
        initOtherViews(view);

    }

    private void initOtherViews(View view) {
        Button btAddProduct = (Button) view.findViewById(R.id.record_bt_add_product);
        btAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClassifyChooseDialog(mContext).show();
            }
        });
        ArrayList<View> itemList = new ArrayList<>();
        itemList.add(view.findViewById(R.id.record_makeup));
        itemList.add(view.findViewById(R.id.record_food));
        itemList.add(view.findViewById(R.id.record_medicine));
        itemList.add(view.findViewById(R.id.record_others));
        for (int i = 0; i < itemList.size(); i++) {
            setEachItemClickListener(itemList.get(i), i);
        }
    }

    private void setEachItemClickListener(View view, final int which) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecordItemDetail.class);
                intent.putExtra("which", which);
                startActivity(intent);
            }
        };
        view.setOnClickListener(onClickListener);
    }


    private void initCalendar(View view) {
        final TextView popupwindow_calendar_month = (TextView) view
                .findViewById(R.id.popupwindow_calendar_month);
        final KCalendar calendar = (KCalendar) view
                .findViewById(R.id.popupwindow_calendar);

        popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
                + calendar.getCalendarMonth() + "月");

        if (null != mDate) {

            int years = Integer.parseInt(mDate.substring(0,
                    mDate.indexOf("-")));
            int month = Integer.parseInt(mDate.substring(
                    mDate.indexOf("-") + 1, mDate.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");

            calendar.showCalendar(years, month);
            calendar.setCalendarDayBgColor(mDate, R.drawable.calendar_date_focused);
        }

        List<String> list = new ArrayList<String>(); //设置标记列表
        list.add("2014-04-01");
        list.add("2014-04-02");
        calendar.addMarks(list, 0);

        //监听所选中的日期
        calendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                int month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf("-") + 1,
                        dateFormat.lastIndexOf("-")));

                if (calendar.getCalendarMonth() - month == 1//跨年跳转
                        || calendar.getCalendarMonth() - month == -11) {
                    calendar.lastMonth();

                } else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
                        || month - calendar.getCalendarMonth() == -11) {
                    calendar.nextMonth();

                } else {
                    calendar.removeAllBgColor();
                    calendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
                    mDate = dateFormat;//最后返回给全局 mDate
                }
            }
        });

        //监听当前月份
        calendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
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
                        calendar.lastMonth();
                    }

                });

        //下月监听按钮
        RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        calendar.nextMonth();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
}
