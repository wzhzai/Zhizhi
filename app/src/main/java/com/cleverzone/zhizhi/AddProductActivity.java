package com.cleverzone.zhizhi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cleverzone.zhizhi.bean.ProductBean;
import com.cleverzone.zhizhi.sqlite.DBManager;
import com.cleverzone.zhizhi.util.Const;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddProductActivity extends BaseActivity {

    private Context mContext;
    private EditText mEtPrDate;
    private EditText mEtShelfLife;
    private RadioGroup mRgShelfLife;
    private EditText mEtName;
    private EditText mEtQuantity;
    private EditText mEtPosition;
    private EditText mEtAdvance;
    private EditText mEtClassify;
    private Button mBtSave;
    private Calendar mChooseCalendar;
    private int mMainClassifyResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainClassifyResId = Const.RECORD_CLASSIFIES_TEXT[getIntent().getIntExtra("which", 0)];
        setContentView(R.layout.activity_add_product);
        mContext = this;
        setTitle(R.mipmap.title_bar_icon_record);
        initAllViews();
    }

    private void initAllViews() {
        mEtPrDate = (EditText) findViewById(R.id.add_product_et_pr);
        mEtPrDate.setInputType(InputType.TYPE_NULL);
        mEtShelfLife = (EditText) findViewById(R.id.add_product_et_shelf_lift);
        mEtPrDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    createDatePickDialog();
                }
            }
        });
        mRgShelfLife = (RadioGroup) findViewById(R.id.add_product_rg_shelf_lift);
        mRgShelfLife.check(R.id.add_product_radio_month);
        mEtName = (EditText) findViewById(R.id.add_product_et_name);
        mEtQuantity = (EditText) findViewById(R.id.add_product_et_quantity);
        mEtPosition = (EditText) findViewById(R.id.add_product_et_position);
        mEtAdvance = (EditText) findViewById(R.id.add_product_et_advance);
        mEtClassify = (EditText) findViewById(R.id.add_product_et_classify);
        mBtSave = (Button) findViewById(R.id.add_product_bt_save);

        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductBean productBean = new ProductBean();
                productBean.name = mEtName.getText().toString();
                productBean.prDate = mEtPrDate.getText().toString();
                if (mRgShelfLife.getCheckedRadioButtonId() == R.id.add_product_radio_day) {
                    productBean.shelfLifeDay = Integer.parseInt(mEtShelfLife.getText().toString());
                    mChooseCalendar.add(Calendar.DAY_OF_MONTH, productBean.shelfLifeDay);
                } else {
                    productBean.shelfLifMonth = Integer.parseInt(mEtShelfLife.getText().toString());
                    mChooseCalendar.add(Calendar.MONTH, productBean.shelfLifMonth);
                }
                productBean.exDate = Const.NORMAL_SIMPLE_DATE_FORMAT.format(mChooseCalendar.getTime());
                productBean.position = mEtPosition.getText().toString();
                productBean.advance = Integer.parseInt(mEtAdvance.getText().toString());
                mChooseCalendar.add(Calendar.DAY_OF_MONTH, productBean.advance);
                productBean.hintDate = Const.NORMAL_SIMPLE_DATE_FORMAT.format(mChooseCalendar.getTime());
                productBean.count = Integer.parseInt(mEtQuantity.getText().toString());
                productBean.mainClassify = mContext.getString(mMainClassifyResId);
                productBean.subClassify = mEtClassify.getText().toString();
                DBManager.getInstance(mContext).saveProduct(productBean);
                Toast.makeText(mContext, getString(R.string.product_add_success), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void createDatePickDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mChooseCalendar = Calendar.getInstance();
                mChooseCalendar.set(year, monthOfYear, dayOfMonth);
                mEtPrDate.setText(Const.NORMAL_SIMPLE_DATE_FORMAT.format(mChooseCalendar.getTime()));
                mEtShelfLife.requestFocus();
            }
        }, year, month, day);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mEtPrDate.clearFocus();
                mEtShelfLife.requestFocus();
            }
        });
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
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
