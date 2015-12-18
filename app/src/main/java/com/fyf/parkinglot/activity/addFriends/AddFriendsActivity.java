package com.fyf.parkinglot.activity.addFriends;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.easemob.chat.EMContactManager;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.utils.Utils;
import com.fyf.parkinglot.view.CustomToast;

/**
 * Created by fengyifei on 15/12/16.
 */
public class AddFriendsActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;

    private TextInputLayout warpper_imAccount;
    private EditText et_imAccount; // 即时通信账号(手机号)
    private EditText et_reason;
    private CircularProgressButton btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        findView();
        setListener();
        init();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);

        warpper_imAccount = (TextInputLayout) findViewById(R.id.activity_add_friends_warpper_imAccount);
        et_imAccount = (EditText) findViewById(R.id.activity_add_friends_et_imAccount);
        et_reason = (EditText) findViewById(R.id.activity_add_friends_et_reason);
        btn_submit = (CircularProgressButton) findViewById(R.id.activity_add_friends_btn_submit);
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
                if (!Utils.isMobileNum(et_imAccount.getText().toString())) {
                    warpper_imAccount.setError("手机号码不正确");
                    return;
                }
                warpper_imAccount.setError("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMContactManager.getInstance().addContact(et_imAccount.getText().toString()
                                    , et_reason.getText().toString());//需异步处理
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                CustomToast.showToast(getApplicationContext(), "提交成功,等待好友确认", 1000);
                finish();
            }
        });
    }

    private void init() {
        btn_next.setVisibility(View.INVISIBLE);
        tv_title.setText(R.string.activity_add_friends_add);
    }

}