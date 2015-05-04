package com.cleverzone.zhizhi;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class LoginActivity extends BaseActivity implements Handler.Callback {

    private static final String TAG = "LoginActivity";

    private EditText mEtName;
    private EditText mEtPassword;
    private Button mBtLogin;
    private String mMode = "2";
    private Handler mHandler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setLeftButtonHide();
        setRightButton(R.string.login_register, onRegisterClickListener);
        initAllViews();
    }

    private View.OnClickListener onRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setRightButton(R.string.login_text, onLoginClickListener);
            mBtLogin.setText(R.string.login_register);
            mMode = "1";
        }
    };

    private View.OnClickListener onLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setRightButton(R.string.login_register, onRegisterClickListener);
            mBtLogin.setText(R.string.login_text);
            mMode = "2";
        }
    };

    private void initAllViews() {
        mEtName = (EditText) findViewById(R.id.login_et_user_name);
        mEtPassword = (EditText) findViewById(R.id.login_et_password);
        mBtLogin = (Button) findViewById(R.id.login_button);
    }

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

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
                    String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                    Log.e(TAG, "result = " + json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
