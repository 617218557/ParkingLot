package com.fyf.parkinglot.activity.updateUserInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.utils.NotifyDataChangeUtils;
import com.fyf.parkinglot.view.CustomToast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import it.sauronsoftware.base64.Base64;

/**
 * Created by fengyifei on 15/11/29.
 */
public class UpdateUserInfoActivity extends AppCompatActivity {
    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;

    private EditText et_name, et_age;
    private RadioGroup rg_gender;
    private CircularProgressButton btn_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        findView();
        setListener();
        init();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);

        et_name = (EditText) findViewById(R.id.activity_changeUserInfo_et_name);
        et_age = (EditText) findViewById(R.id.activity_changeUserInfo_et_age);
        rg_gender = (RadioGroup) findViewById(R.id.activity_changeUserInfo_rg_gender);
        btn_submit = (CircularProgressButton) findViewById(R.id.activity_changeUserInfo_btn_submit);
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
                if (checkForm()) {
                    SubmitTask submitTask = new SubmitTask();
                    submitTask.execute();
                }
            }
        });
    }

    private void init() {
        tv_title.setText(R.string.actionBar_updateUserInfo);
        btn_next.setVisibility(View.INVISIBLE);
        et_name.setText(UserInfoInCache.user_name);
        et_age.setText(UserInfoInCache.user_age + "");
        btn_submit.setIndeterminateProgressMode(true);
    }

    // 检查表单
    private boolean checkForm() {
        if (et_name.getText().toString().equals("")) {
            CustomToast.showToast(getApplicationContext(), "姓名不能为空", 1000);
            return false;
        }
        if (et_age.getText().toString().equals("")) {
            CustomToast.showToast(getApplicationContext(), "年龄不能为空", 1000);
            return false;
        }
        return true;
    }


    class SubmitTask extends AsyncTask {
        private String name, age;
        private int gender;

        @Override
        protected void onPreExecute() {
            btn_submit.setProgress(50);
            name = et_name.getText().toString();
            age = et_age.getText().toString();
            gender = rg_gender.getCheckedRadioButtonId() == R.id.activity_changeUserInfo_rb_female ? 0 : 1;
            btn_submit.setClickable(false);
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder()
                    .add(SQLWord.USER_ID, UserInfoInCache.user_id + "").add(SQLWord.USER_NAME, Base64.encode(name))
                    .add(SQLWord.USER_AGE, age).add(SQLWord.USER_GENDER, gender + "").build();
            return HttpUtils.httpPost(URLAddress.updateUserInfoURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            setUpdateInfo(o.toString());
            btn_submit.setClickable(true);
            super.onPostExecute(o);
        }
    }

    // 解析修改结果,并处理
    private void setUpdateInfo(String json) {
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
            NotifyDataChangeUtils.REFRESH_USER_INFO = 1;
            JsonUtils.getUserInfo(UpdateUserInfoActivity.this, JsonUtils.getResultMsgString(json));
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
