package com.cleverzone.zhizhi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.cleverzone.zhizhi.application.ZApp;
import com.cleverzone.zhizhi.bean.NetScanResult;
import com.cleverzone.zhizhi.bean.ProductBean;
import com.cleverzone.zhizhi.capture.CaptureActivity;
import com.cleverzone.zhizhi.comui.BaseListChooseDialog;
import com.cleverzone.zhizhi.sqlite.DBManager;
import com.cleverzone.zhizhi.util.Const;
import com.cleverzone.zhizhi.util.SoftInputController;
import com.cleverzone.zhizhi.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;


public class AddProductActivity extends BaseActivity {

    public static final int MODE_ADD = 0;
    public static final int MODE_SHOW = 1;

    private Context mContext;
    private EditText mEtShelfLife;
    private RadioGroup mRgShelfLife;
    private EditText mEtName;
    private EditText mEtQuantity;
    private EditText mEtPosition;
    private EditText mEtAdvance;
    private EditText mEtClassify;
    private EditText mEtFrequency;
    private Calendar mChooseCalendar;
    private int mMainClassifyResId;
    private int mId = -1;
    private Button mBtDel;
    private ArrayList<View> mViewList;
    private ImageView mImageView;
    private String mImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int which = getIntent().getIntExtra("which", 0);
        mMainClassifyResId = Const.RECORD_CLASSIFIES_TEXT[which];
        setContentView(R.layout.activity_add_product);
        mContext = this;
        int title = 0;
        switch (which) {
            case 0:
                title = R.mipmap.icon_makeup_title;
                break;
            case 1:
                title = R.mipmap.icon_food_title;
                break;
            case 2:
                title = R.mipmap.icon_medicine_title;
                break;
            case 3:
                title = R.mipmap.icon_others_title;
                break;
        }
        setTitle(title);
        mChooseCalendar = Calendar.getInstance();
        mViewList = new ArrayList<>();
        int mode = getIntent().getIntExtra("mode", MODE_ADD);
        if (mode == MODE_SHOW) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        initAllViews();
        if (mode == MODE_SHOW) {
            showInfo((ProductBean)getIntent().getSerializableExtra("bean"));
            setEditButton();
        } else {
            setSaveButton();
        }
        setCancelButton();
    }

    private void setCancelButton() {
        setLeftButton(R.string.cancel_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setEditButton() {
        setRightButton(R.string.add_product_edit_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (View view : mViewList) {
                    view.setEnabled(true);
                }
                setSaveButton();
            }
        });
    }

    private void setSaveButton() {
        setRightButton(R.string.add_product_save_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEtName.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, R.string.add_product_name_no_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEtShelfLife.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, R.string.add_product_shelf_life_no_null, Toast.LENGTH_SHORT).show();
                    return;
                }

                ProductBean productBean = new ProductBean();
                productBean.id = mId;
                productBean.picPath = mImagePath;
                productBean.name = mEtName.getText().toString();
                if (mRgShelfLife.getCheckedRadioButtonId() == R.id.add_product_radio_day) {
                    productBean.shelfLifeDay = Integer.parseInt(mEtShelfLife.getText().toString());
                    mChooseCalendar.add(Calendar.DAY_OF_MONTH, productBean.shelfLifeDay);
                } else {
                    productBean.shelfLifMonth = Integer.parseInt(mEtShelfLife.getText().toString());
                    mChooseCalendar.add(Calendar.MONTH, productBean.shelfLifMonth);
                }
                productBean.exDate = Const.NORMAL_SIMPLE_DATE_FORMAT.format(mChooseCalendar.getTime());
                productBean.position = mEtPosition.getText().toString();
                if (mEtAdvance.getText().toString().isEmpty()) {
                    productBean.advance = 0;
                } else {
                    productBean.advance = Integer.parseInt(mEtAdvance.getText().toString());
                }
                mChooseCalendar.add(Calendar.DAY_OF_MONTH, productBean.advance);
                productBean.hintDate = Const.NORMAL_SIMPLE_DATE_FORMAT.format(mChooseCalendar.getTime());
                if (mEtQuantity.getText().toString().isEmpty()) {
                    productBean.count = 0;
                } else {
                    productBean.count = Integer.parseInt(mEtQuantity.getText().toString());
                }
                productBean.mainClassify = mContext.getString(mMainClassifyResId);
                productBean.subClassify = mEtClassify.getText().toString();
                // TODO: 2015/7/30  saveProduct
//                DBManager.getInstance(mContext).saveProduct(productBean);
                Toast.makeText(mContext, getString(R.string.product_add_success), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showInfo(ProductBean bean) {
        for (View view : mViewList) {
            view.setEnabled(false);
        }
        try {
            mChooseCalendar.setTime(Const.NORMAL_SIMPLE_DATE_FORMAT.parse(bean.prDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mId = bean.id;
        ImageLoader.getInstance().displayImage(bean.picPath, mImageView, Utils.getDefaultImageLoaderOptions());
        mEtName.setText(bean.name);
        if (bean.shelfLifeDay != 0) {
            mRgShelfLife.check(R.id.add_product_radio_day);
            mEtShelfLife.setText(String.valueOf(bean.shelfLifeDay));
        } else {
            mEtShelfLife.setText(String.valueOf(bean.shelfLifMonth));
        }
        mEtQuantity.setText(String.valueOf(bean.count));
        mEtPosition.setText(bean.position);
        mEtAdvance.setText(String.valueOf(bean.advance));
        mEtClassify.setText(bean.subClassify);
        mBtDel.setVisibility(View.VISIBLE);
        mBtDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle(R.string.add_product_del_text)
                        .setMessage("确认要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2015/7/30 deleteProduct
//                        DBManager.getInstance(mContext).deldetProduct(mId);
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

    }

    private void initAllViews() {
        mBtDel = (Button) findViewById(R.id.add_product_bt_del);
        mImageView = (ImageView) findViewById(R.id.add_product_iv_image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInputController.hideSoftInput(mContext, mImageView);
                new BaseListChooseDialog.CBuilder(mContext).setTitle(R.string.add_product_choose_image_text).setContents(R.array.image_choose_text)
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 2:
                                        if (!Utils.isNetConnect(mContext)) {
                                            return;
                                        }
                                        Intent intent = new Intent(mContext, CaptureActivity.class);
                                        AddProductActivity.this.startActivityForResult(intent, 0);
                                        break;
                                }
                            }
                        }).show();
            }
        });
        mViewList.add(mImageView);
        mEtShelfLife = (EditText) findViewById(R.id.add_product_et_shelf_lift);
        mViewList.add(mEtShelfLife);
        mRgShelfLife = (RadioGroup) findViewById(R.id.add_product_rg_shelf_lift);
        mViewList.add(mRgShelfLife);
        mViewList.add(findViewById(R.id.add_product_radio_day));
        mViewList.add(findViewById(R.id.add_product_radio_month));
        mRgShelfLife.check(R.id.add_product_radio_month);
        mEtName = (EditText) findViewById(R.id.add_product_et_name);
        mViewList.add(mEtName);
        mEtQuantity = (EditText) findViewById(R.id.add_product_et_quantity);
        mViewList.add(mEtQuantity);
        mEtPosition = (EditText) findViewById(R.id.add_product_et_position);
        mViewList.add(mEtPosition);
        mEtAdvance = (EditText) findViewById(R.id.add_product_et_advance);
        mViewList.add(mEtAdvance);
        mEtFrequency = (EditText) findViewById(R.id.add_product_et_frequency);
        mViewList.add(mEtFrequency);
        mEtClassify = (EditText) findViewById(R.id.add_product_et_classify);
        mViewList.add(mEtClassify);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == CaptureActivity.SCAN_SUCCESS_RESULT_CODE) {
            NetScanResult result = (NetScanResult) data.getSerializableExtra("result");
            mEtName.setText(result.name);
            ZApp.getInstance().mImageLoader.displayImage(result.url, mImageView, Utils.getDefaultImageLoaderOptions());
            if (TextUtils.isEmpty(result.url)) {
                mImagePath = "";
            } else {
                mImagePath = result.url;
            }
        }
    }

    private void createDatePickDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mChooseCalendar.set(year, monthOfYear, dayOfMonth);
                mEtShelfLife.requestFocus();
            }
        }, year, month, day);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mEtShelfLife.requestFocus();
            }
        });
        datePickerDialog.show();
    }

}
