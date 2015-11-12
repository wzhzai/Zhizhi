package com.cleverzone.zhizhi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cleverzone.zhizhi.application.ZApp;
import com.cleverzone.zhizhi.bean.NetScanResult;
import com.cleverzone.zhizhi.bean.NewProductBean;
import com.cleverzone.zhizhi.capture.CaptureActivity;
import com.cleverzone.zhizhi.comui.BaseListChooseDialog;
import com.cleverzone.zhizhi.sqlite.DBManager;
import com.cleverzone.zhizhi.util.Const;
import com.cleverzone.zhizhi.util.DateUtil;
import com.cleverzone.zhizhi.util.SoftInputController;
import com.cleverzone.zhizhi.util.Utils;
import com.cleverzone.zhizhi.util.ViewEnableController;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class AddProductActivity extends BaseActivity {

    public static final int MODE_ADD = 0;

    public static final int MODE_SHOW = 1;

    private Context mContext;

    private int mMainClassifyResId;

    private ArrayList<View> mViewList;

    private String mImagePath = "";

    private ViewEnableController mViewEnableController;

    private HashMap<Integer, View> mViewHashMap;

    private int mPrTimestamp = 0;

    private int mExTimestamp = 0;

    private int mLifeType = Const.SHELF_LIFE_TYPE_DAY;

    private DBManager mDBManager;

    private int mMode;

    private NewProductBean mNewProductBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewEnableController = new ViewEnableController();
        mViewHashMap = new HashMap<>();
        int which = getIntent().getIntExtra("which", 0);
        mMode = getIntent().getIntExtra("mode", 0);
        if (which != -100 && which != -200) {
            mMainClassifyResId = Const.RECORD_CLASSIFIES_TEXT[which];
        }
        setContentView(R.layout.activity_add_product);
        mContext = this;
        if (mMode == MODE_SHOW) {
            mNewProductBean = (NewProductBean) getIntent().getSerializableExtra("bean");
            if (mNewProductBean == null) {
                Toast.makeText(mContext, "数据错误，请重试", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        mDBManager = DBManager.getInstance(mContext);
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
        mViewList = new ArrayList<>();
        int mode = getIntent().getIntExtra("mode", MODE_ADD);
        if (mode == MODE_SHOW) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        initAllViews();
        if (mode == MODE_SHOW) {
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
                mViewEnableController.setAllEnable();
                setSaveButton();
            }
        });
    }

    private void setSaveButton() {
        setRightButton(R.string.add_product_save_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {
        NewProductBean productBean = new NewProductBean();
        if (mMode == MODE_SHOW) {
            productBean.id = mNewProductBean.id;
        }
        String name = ((EditText) mViewHashMap.get(R.id.add_product_et_name)).getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, "请输入宝贝名字", Toast.LENGTH_SHORT).show();
            return;
        }
        String life = ((EditText) mViewHashMap.get(R.id.add_product_et_shelf_lift)).getText().toString();
        if (mPrTimestamp == 0 || mExTimestamp == 0 || TextUtils.isEmpty(life)) {
            Toast.makeText(mContext, "请输入保质期", Toast.LENGTH_SHORT).show();
            return;
        }
        productBean.name = name;
        productBean.picPath = mImagePath;
        productBean.prDate = mPrTimestamp;
        productBean.shelfLife = Integer.parseInt(life);
        productBean.shelfLifeType = mLifeType;
        productBean.exDate = mExTimestamp;
        String count = ((EditText) mViewHashMap.get(R.id.add_product_et_quantity)).getText().toString();
        productBean.count = TextUtils.isEmpty(count) ? 1 : Integer.parseInt(count);
        productBean.position = ((EditText) mViewHashMap.get(R.id.add_product_et_position)).getText().toString();
        if (mMode == MODE_ADD) {
            productBean.mainClassify = getString(mMainClassifyResId);
        } else {
            productBean.mainClassify = mNewProductBean.mainClassify;
        }
        productBean.subClassify = ((EditText) mViewHashMap.get(R.id.add_product_et_classify)).getText().toString();
        productBean.backup = ((EditText) mViewHashMap.get(R.id.add_product_et_backup)).getText().toString();
        String advance = ((EditText) mViewHashMap.get(R.id.add_product_et_advance)).getText().toString();
        productBean.advance = TextUtils.isEmpty(advance) ? 0 : Integer.parseInt(advance);
        String frequency = ((EditText) mViewHashMap.get(R.id.add_product_et_frequency)).getText().toString();
        productBean.frequency = TextUtils.isEmpty(frequency) ? 0 : Integer.parseInt(frequency);
        productBean.hintDate = DateUtil.add(productBean.exDate, 0, 0, (0 - productBean.advance));
        mDBManager.saveProduct(productBean);
        Toast.makeText(mContext, R.string.add_product_save_success_text, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initAllViews() {
        final ImageView imageView = (ImageView) findViewById(R.id.add_product_iv_image);
        mViewHashMap.put(R.id.add_product_iv_image, imageView);
        mViewEnableController.registerView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInputController.hideSoftInput(mContext, imageView);
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

        EditText etName = (EditText) findViewById(R.id.add_product_et_name);
        mViewHashMap.put(R.id.add_product_et_name, etName);
        mViewEnableController.registerView(etName);

        View.OnClickListener dateChooseClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickerDialog();
            }
        };

        TextView tvPrDate = (TextView) findViewById(R.id.add_product_et_pr);
        tvPrDate.setOnClickListener(dateChooseClickListener);
        mViewHashMap.put(R.id.add_product_et_pr, tvPrDate);
        mViewEnableController.registerView(tvPrDate);

        TextView tvExDate = (TextView) findViewById(R.id.add_product_et_ex);
        mViewHashMap.put(R.id.add_product_et_ex, tvExDate);
        mViewEnableController.registerView(tvExDate);

        ImageView ivCalendar = (ImageView) findViewById(R.id.add_product_pr_iv);
        ivCalendar.setOnClickListener(dateChooseClickListener);
        mViewHashMap.put(R.id.add_product_pr_iv, ivCalendar);
        mViewEnableController.registerView(ivCalendar);

        EditText etLife = (EditText) findViewById(R.id.add_product_et_shelf_lift);
        etLife.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = s.toString();
                if (!num.isEmpty()) {
                    TextView tvPrDate = (TextView) mViewHashMap.get(R.id.add_product_et_pr);
                    if (!tvPrDate.getText().toString().isEmpty()) {
                        int n = Integer.parseInt(num);
                        setExDate(mPrTimestamp, n, mLifeType);
                    }
                }
            }
        });
        mViewHashMap.put(R.id.add_product_et_shelf_lift, etLife);
        mViewEnableController.registerView(etLife);

        RadioGroup rgType = (RadioGroup) findViewById(R.id.add_product_rg_shelf_lift);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.add_product_radio_day:
                        mLifeType = Const.SHELF_LIFE_TYPE_DAY;
                        break;
                    case R.id.add_product_radio_month:
                        mLifeType = Const.SHELF_LIFE_TYPE_MONTH;
                        break;
                    case R.id.add_product_radio_year:
                        mLifeType = Const.SHELF_LIFE_TYPE_YEAR;
                        break;
                    default:
                        mLifeType = Const.SHELF_LIFE_TYPE_DAY;
                        break;
                }
                checkLife();
            }
        });
        mViewHashMap.put(R.id.add_product_rg_shelf_lift, rgType);
        mViewEnableController.registerView(rgType);

        mViewEnableController.registerView(rgType.findViewById(R.id.add_product_radio_day));
        mViewEnableController.registerView(rgType.findViewById(R.id.add_product_radio_month));
        mViewEnableController.registerView(rgType.findViewById(R.id.add_product_radio_year));

        EditText etCount = (EditText) findViewById(R.id.add_product_et_quantity);
        mViewHashMap.put(R.id.add_product_et_quantity, etCount);
        mViewEnableController.registerView(etCount);

        EditText etPosition = (EditText) findViewById(R.id.add_product_et_position);
        mViewHashMap.put(R.id.add_product_et_position, etPosition);
        mViewEnableController.registerView(etPosition);

        EditText etSubClassify = (EditText) findViewById(R.id.add_product_et_classify);
        mViewHashMap.put(R.id.add_product_et_classify, etSubClassify);
        mViewEnableController.registerView(etSubClassify);

        EditText etBackup = (EditText) findViewById(R.id.add_product_et_backup);
        mViewHashMap.put(R.id.add_product_et_backup, etBackup);
        mViewEnableController.registerView(etBackup);

        EditText etAdvance = (EditText) findViewById(R.id.add_product_et_advance);
        mViewHashMap.put(R.id.add_product_et_advance, etAdvance);
        mViewEnableController.registerView(etAdvance);

        EditText etFrequency = (EditText) findViewById(R.id.add_product_et_frequency);
        mViewHashMap.put(R.id.add_product_et_frequency, etFrequency);
        mViewEnableController.registerView(etFrequency);

        if (mMode == MODE_SHOW) {
            if (!TextUtils.isEmpty(mNewProductBean.picPath)) {
                ImageLoader.getInstance().displayImage(mNewProductBean.picPath, imageView);
            }
            etName.setText(mNewProductBean.name);
            mPrTimestamp = mNewProductBean.prDate;
            tvPrDate.setText(DateUtil.getDateString(mNewProductBean.prDate));
            etLife.setText(String.valueOf(mNewProductBean.shelfLife));
            mLifeType = mNewProductBean.shelfLifeType;
            checkLife();
            etCount.setText(String.valueOf(mNewProductBean.count));
            etPosition.setText(mNewProductBean.position);
            etSubClassify.setText(mNewProductBean.subClassify);
            etBackup.setText(mNewProductBean.backup);
            etAdvance.setText(String.valueOf(mNewProductBean.advance));
            etFrequency.setText(String.valueOf(mNewProductBean.frequency));
            mViewEnableController.setAllDisable();
        }

    }

    private void checkLife() {
        EditText etLife = (EditText) mViewHashMap.get(R.id.add_product_et_shelf_lift);
        final String num = etLife.getText().toString();
        if (!num.isEmpty()) {
            int n = Integer.parseInt(num);
            setExDate(mPrTimestamp, n, mLifeType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == CaptureActivity.SCAN_SUCCESS_RESULT_CODE) {
            NetScanResult result = (NetScanResult) data.getSerializableExtra("result");
            EditText etName = (EditText) mViewHashMap.get(R.id.add_product_et_name);
            etName.setText(result.name);
            ImageView imageView = (ImageView) mViewHashMap.get(R.id.add_product_iv_image);
            ZApp.getInstance().mImageLoader.displayImage(result.url, imageView, Utils.getDefaultImageLoaderOptions());
            if (TextUtils.isEmpty(result.url)) {
                mImagePath = "";
            } else {
                mImagePath = result.url;
            }
        }
    }

    private void createDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                TextView tvPrDate = (TextView) mViewHashMap.get(R.id.add_product_et_pr);
                tvPrDate.setText(DateUtil.getDateStringWithGivenParams(year, monthOfYear, dayOfMonth));
                mPrTimestamp = DateUtil.getTenTimestampWithGivenParams(year, monthOfYear, dayOfMonth);

                checkLife();
            }
        }, DateUtil.getYear(), DateUtil.getMonth(), DateUtil.getDayOfMonth());
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        dialog.show();
    }

    private void setExDate(int prTimestamp, int life, int type) {
        TextView textView = (TextView) mViewHashMap.get(R.id.add_product_et_ex);
        switch (type) {
            case Const.SHELF_LIFE_TYPE_DAY:
                mExTimestamp = DateUtil.add(prTimestamp, 0, 0, life);
                break;
            case Const.SHELF_LIFE_TYPE_MONTH:
                mExTimestamp = DateUtil.add(prTimestamp, 0, life, 0);
                break;
            case Const.SHELF_LIFE_TYPE_YEAR:
                mExTimestamp = DateUtil.add(prTimestamp, life, 0, 0);
                break;
            default:
                mExTimestamp = DateUtil.add(prTimestamp, 0, 0, life);
                break;
        }
        textView.setText(DateUtil.getDateString(mExTimestamp));
    }

}
