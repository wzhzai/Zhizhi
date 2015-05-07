package com.cleverzone.zhizhi;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cleverzone.zhizhi.util.SharedPreferencesUtil;
import com.cleverzone.zhizhi.util.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class LoginActivity extends BaseActivity implements Handler.Callback {

    private static final String TAG = "LoginActivity";
    public static final int LOGIN_SUCCESS = 200;

    private EditText mEtName;
    private EditText mEtPassword;
    private Button mBtLogin;
    private String mMode = "2";
    private Handler mHandler = new Handler(this);
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_login);
        setLeftButtonHide();
        setRightButton(R.string.login_register, onRegisterClickListener);
        setTitle(R.mipmap.icon_login);
        initAllViews();
    }

    private View.OnClickListener onRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setRightButton(R.string.login_text, onLoginClickListener);
            mBtLogin.setText(R.string.login_register);
            mMode = "1";
            setTitle(R.mipmap.icon_register);
        }
    };

    private View.OnClickListener onLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setRightButton(R.string.login_register, onRegisterClickListener);
            mBtLogin.setText(R.string.login_text);
            mMode = "2";
            setTitle(R.mipmap.icon_login);
        }
    };

    private void initAllViews() {
        mEtName = (EditText) findViewById(R.id.login_et_user_name);
        mEtPassword = (EditText) findViewById(R.id.login_et_password);
        mBtLogin = (Button) findViewById(R.id.login_button);
        mBtLogin.setOnClickListener(onButtonClickListener);
    }

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!Utils.isNetConnect(mContext)) {
                return;
            }
            startLogin(getHttpPost());
        }
    };

    private HttpPost getHttpPost() {
        ArrayList<NameValuePair> paramList = new ArrayList<>();
        BasicNameValuePair param;
        param = new BasicNameValuePair("user", mEtName.getText().toString());
        paramList.add(param);
        param = new BasicNameValuePair("pwd", mEtPassword.getText().toString());
        paramList.add(param);
        param = new BasicNameValuePair("mode", mMode);
        paramList.add(param);
        HttpPost httpPost = new HttpPost("http://59.67.152.71/wangzhengze/ZhizhiServer/Login.php");
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpPost;
    }

    private void startLogin(final HttpPost httpPost) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                try {
                    HttpResponse response = client.execute(httpPost);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
                        Log.e(TAG, "result = " + data);
                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject msgObject = jsonObject.getJSONObject("message");
                        int code = 0;
                        if (jsonObject.getInt("status") == 0) {
                            code = msgObject.getInt("error_code");
                        } else {
                            code = msgObject.getInt("result");
                        }
                        mHandler.sendEmptyMessage(code);
                    } else {
                        mHandler.sendEmptyMessage(-1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(-1);
                }
            }
        }).start();
    }

    private static class MyResult {
        public static final int POST_PARAMS_ERROR = 110;
        public static final int DB_ERROR = 111;
        public static final int USER_EXIST = 112;
        public static final int USER_NO_EXIST = 113;
        public static final int REGISTER_SUCCESS = 200;
        public static final int LOGIN_SUCCESS = 201;

    }

    public void handleSuccess() {
        Intent intent = new Intent();
        SharedPreferencesUtil.saveString(mContext, "user_name", mEtName.getText().toString());
        intent.putExtra("user_name", mEtName.getText().toString());
        setResult(LOGIN_SUCCESS, intent);
        finish();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MyResult.LOGIN_SUCCESS:
                handleSuccess();
                Toast.makeText(mContext, R.string.login_success, Toast.LENGTH_SHORT).show();
                break;
            case MyResult.REGISTER_SUCCESS:
                handleSuccess();
                Toast.makeText(mContext, R.string.register_success, Toast.LENGTH_SHORT).show();
                break;
            case MyResult.USER_EXIST:
                Toast.makeText(mContext, R.string.user_name_exist, Toast.LENGTH_SHORT).show();
                break;
            case MyResult.USER_NO_EXIST:
                Toast.makeText(mContext, R.string.user_no_exist, Toast.LENGTH_SHORT).show();
                break;
            case 0:
            case MyResult.DB_ERROR:
            case MyResult.POST_PARAMS_ERROR:
                Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT).show();
                break;
            case -1:
                Toast.makeText(mContext, R.string.net_time_out, Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
