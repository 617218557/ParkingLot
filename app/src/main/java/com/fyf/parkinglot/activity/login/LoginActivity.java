package com.fyf.parkinglot.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.main.MainActivity;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.DataOperateUtils;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.utils.Utils;
import com.fyf.parkinglot.view.CustomToast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout warpper_phoneNum;
    private TextInputLayout warpper_password;
    private EditText et_phoneNum; // 手机号
    private EditText et_password; // 密码
    private TextView tv_or;// 或者创建账户
    private CircularProgressButton btn_login;// 登录按钮

    private final int FLAG_LOGIN = 0;
    private final int FLAG_REGIST = 1;
    private int flag = FLAG_LOGIN;// 标记,0为登录,1为注册

    private Animation animIn;
    private Animation animOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        setListener();
        init();
    }

    // 初始化
    private void init() {
        animIn = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_in);
        animOut = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_out);
        // 读取用户名密码
        Map<String, String> map = DataOperateUtils.readLoginInfo(this);
        if (map.size() == 2) {
            et_phoneNum.setText(map.get(SQLWord.USER_PHONENUM));
            et_password.setText(map.get(SQLWord.USER_PASSWORD));
        }
    }

    private void findView() {
        warpper_phoneNum = (TextInputLayout) findViewById(R.id.activity_login_warpper_phoneNum);
        warpper_password = (TextInputLayout) findViewById(R.id.activity_login_warpper_password);
        et_phoneNum = (EditText) findViewById(R.id.activity_login_et_phoneNum);
        et_password = (EditText) findViewById(R.id.activity_login_et_password);
        tv_or = (TextView) findViewById(R.id.activity_login_tv_or);
        btn_login = (CircularProgressButton) findViewById(R.id.activity_login_btn_login);
        btn_login.setIndeterminateProgressMode(true); // 设置为不确定模式
    }

    private void setListener() {
        tv_or.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickAbled(false);
                animOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        btn_login.startAnimation(animOut);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (flag == FLAG_LOGIN) {
                            //设置成注册
                            tv_or.setText(R.string.activity_login_orLogin);
                            btn_login.setIdleText(getString(R.string.activity_login_regist));
                            btn_login.setText(getString(R.string.activity_login_regist));
                            flag = FLAG_REGIST;
                        } else {
                            //设置成登录
                            tv_or.setText(R.string.activity_login_orRegist);
                            btn_login.setIdleText(getString(R.string.activity_login_login));
                            btn_login.setText(getString(R.string.activity_login_login));
                            flag = FLAG_LOGIN;
                        }
                        btn_login.invalidate();
                        // 开始淡入动画
                        tv_or.startAnimation(animIn);
                        btn_login.startAnimation(animIn);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setClickAbled(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                tv_or.startAnimation(animOut);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                // 检查手机号码合法性
                if (!Utils.isMobileNum(et_phoneNum.getText().toString())) {
                    warpper_phoneNum.setError("手机号码不正确");
                    return;
                }
                warpper_phoneNum.setError("");
                if (et_password.getText().toString().equals("")) {
                    warpper_password.setError("密码不能为空");
                    return;
                }
                warpper_password.setError("");
                if (et_password.getText().toString().length() < 6) {
                    warpper_password.setError("密码长度需大于6位");
                    return;
                }
                warpper_password.setError("");
                setClickAbled(false);
                if (flag == FLAG_LOGIN) {
                    // 登录状态
                    LoginTask loginTask = new LoginTask();
                    loginTask.execute();
                } else {
                    // 注册状态
                    RegistTask registTask = new RegistTask();
                    registTask.execute();
                }
            }
        });
    }

    // 隐藏键盘
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //登陆的task
    class LoginTask extends AsyncTask {
        String user_phoneNum;
        String user_password;

        @Override
        protected void onPreExecute() {
            btn_login.setProgress(50);
            user_phoneNum = et_phoneNum.getText().toString();
            user_password = et_password.getText().toString();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            DataOperateUtils.saveLoginInfo(LoginActivity.this, user_phoneNum, user_password);
            RequestBody body = new FormEncodingBuilder()
                    .add("user_phoneNum", user_phoneNum)
                    .add("user_password", user_password)
                    .build();
            return HttpUtils.httpPost(URLAddress.loginURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            setLoginInfo(o.toString());
            setClickAbled(true);
            super.onPostExecute(o);
        }
    }

    // 解析登录结果,并处理
    private void setLoginInfo(String json) {
        if (json == null || json.equals("")) {
            btn_login.setProgress(0);
            CustomToast.showToast(getApplicationContext(), "登录失败", 1000);
            return;
        }
        if (JsonUtils.getResultCode(json) < 1) {
            // 登录失败,并显示原因
            btn_login.setProgress(0);
            CustomToast.showToast(getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
        } else {
            // 登陆成功,解析用户信息
            btn_login.setProgress(100);
            UserInfoInCache.clear();
            JsonUtils.getUserInfo(LoginActivity.this, JsonUtils.getResultMsgString(json));
            startMainActivity();
        }
    }

    //  注册的task
    class RegistTask extends AsyncTask {
        String user_phoneNum;
        String user_password;

        @Override
        protected void onPreExecute() {
            btn_login.setProgress(50);
            user_phoneNum = et_phoneNum.getText().toString();
            user_password = et_password.getText().toString();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            DataOperateUtils.saveLoginInfo(LoginActivity.this, user_phoneNum, user_password);
            RequestBody body = new FormEncodingBuilder()
                    .add("user_phoneNum", user_phoneNum)
                    .add("user_password", user_password)
                    .add("user_name", "")
                    .add("user_age", "0")
                    .add("user_gender", "1")
                    .add("user_img", "")
                    .build();
            return HttpUtils.httpPost(URLAddress.registURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            setRegistInfo(o.toString());
            setClickAbled(true);
            super.onPostExecute(o);
        }
    }

    // 解析注册结果,并处理
    private void setRegistInfo(String json) {
        if (json == null || json.equals("")) {
            btn_login.setProgress(0);
            CustomToast.showToast(getApplicationContext(), "注册失败", 1000);
            return;
        }
        if (JsonUtils.getResultCode(json) < 1) {
            // 注册失败,并显示原因
            btn_login.setProgress(0);
            CustomToast.showToast(getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
        } else {
            // 注册成功,解析用户信息
            btn_login.setProgress(100);
            UserInfoInCache.clear();
            JsonUtils.getUserInfo(getApplicationContext(), JsonUtils.getResultMsgString(json));
            startMainActivity();
        }
    }

    // 设置按键是否可点击
    private void setClickAbled(boolean b) {
        if (b) {
            // 可点击
            btn_login.setClickable(true);
            tv_or.setClickable(true);
        } else {
            //不可点击
            btn_login.setClickable(false);
            tv_or.setClickable(false);
        }
    }

    // 延时跳转主activity
    private void startMainActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }).start();
    }

}

