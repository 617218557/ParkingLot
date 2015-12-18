package com.fyf.parkinglot.activity.updateUserPassword;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.DataOperateUtils;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

/**
 * Created by fengyifei on 15/11/30.
 */
public class UpdateUserPasswordActivity extends AppCompatActivity {
    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;

    private TextInputLayout warpper_old, warpper_new;
    private EditText et_old, et_new;
    private CircularProgressButton btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_password);
        findView();
        setListener();
        init();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);

        warpper_old = (TextInputLayout) findViewById(R.id.activity_changeUserPassword_warpper_old);
        warpper_new = (TextInputLayout) findViewById(R.id.activity_changeUserPassword_warpper_new);
        et_old = (EditText) findViewById(R.id.activity_changeUserPassword_et_old);
        et_new = (EditText) findViewById(R.id.activity_changeUserPassword_et_new);
        btn_submit = (CircularProgressButton) findViewById(R.id.activity_changeUserPassword_btn_submit);

    }

    private void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm()){
                    UpdatePasswordTask task = new UpdatePasswordTask();
                    task.execute();
                }
            }
        });
    }
    private void init(){
        tv_title.setText(R.string.actionBar_updatePassword);
        btn_next.setVisibility(View.INVISIBLE);
    }

    private boolean checkForm() {
        // 旧密码
        if (et_old.getText().toString().equals("")) {
            warpper_old.setError("密码不能为空");
            return false;
        }
        warpper_old.setError("");
        if (et_old.getText().toString().length() < 6) {
            warpper_old.setError("密码长度需大于6位");
            return false;
        }
        warpper_old.setError("");
        // 新密码
        if (et_new.getText().toString().equals("")) {
            warpper_new.setError("密码不能为空");
            return false;
        }
        warpper_new.setError("");
        if (et_new.getText().toString().length() < 6) {
            warpper_new.setError("密码长度需大于6位");
            return false;
        }
        warpper_new.setError("");
        return true;
    }

    class UpdatePasswordTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(UpdateUserPasswordActivity.this, R.style.DialogNormal);
        private String old_password, new_password;

        @Override
        protected void onPreExecute() {
            old_password = et_old.getText().toString();
            new_password = et_new.getText().toString();
            btn_submit.setClickable(false);
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder()
                    .add(SQLWord.USER_ID, UserInfoInCache.user_id + "").add("old_password", old_password)
                    .add("new_password", new_password).build();
            return HttpUtils.httpPost(URLAddress.updateUserPasswordURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            handleUpdatePassword(o.toString(), new_password);
            btn_submit.setClickable(true);
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }

    private void handleUpdatePassword(String json, String new_password) {
        if (json == null || json.equals("")) {
            btn_submit.setProgress(0);
            CustomToast.showToast(getApplicationContext(), "修改失败", 1000);
            return;
        }
        if (JsonUtils.getResultCode(json) < 1) {
            // 修改失败,并显示原因
            btn_submit.setProgress(0);
            CustomToast.showToast(getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
        } else {
            // 修改成功,解析用户信息
            btn_submit.setProgress(100);
            UserInfoInCache.user_password = new_password;
            DataOperateUtils.saveLoginInfo(UpdateUserPasswordActivity.this, UserInfoInCache.user_phoneNum, new_password);
            finishDelay();
        }
    }

    // 延时finish
    private void finishDelay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                finish();
            }
        }).start();
    }
}
