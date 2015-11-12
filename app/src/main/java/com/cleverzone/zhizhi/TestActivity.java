package com.cleverzone.zhizhi;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

/**
 * Created by wzhz on 2015/11/12.
 */
public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 10, 11);
        calendarView.setDateSelected(calendar, true);
        calendar.set(2015, 10, 10);
        calendarView.setDateSelected(calendar, true);
        calendar.set(2015, 10, 9);
        calendarView.setDateSelected(calendar, true);
        calendar.set(2015, 10, 8);
        calendarView.setDateSelected(calendar, true);
    }
}
