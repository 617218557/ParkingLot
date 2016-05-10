package com.fyf.parkinglot.activity.createGroupChat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.List;

/**
 * Created by fengyifei on 15/12/21.
 */
public class CreateGroupChatActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;

    private TextInputLayout warpper_groupName, warpper_desc;
    private EditText et_groupName, et_desc;
    private JazzyListView lv_friends;

    private CreateGroupChatListAdapter adapter;

    private List<String> usernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chat);
        findView();
        setListener();
        init();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);


        warpper_groupName = (TextInputLayout) findViewById(R.id.activity_create_group_chat_warpper_groupName);
        warpper_desc = (TextInputLayout) findViewById(R.id.activity_create_group_chat_warpper_desc);
        et_groupName = (EditText) findViewById(R.id.activity_create_group_chat_et_groupName);
        et_desc = (EditText) findViewById(R.id.activity_create_group_chat_et_groupName);
        lv_friends = (JazzyListView) findViewById(R.id.activity_create_group_chat_lv_friends);
    }

    private void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_groupName.getText().toString().equals("")) {
                    warpper_groupName.setError("名称不能为空");
                    return;
                }
                warpper_groupName.setError("");
                if (adapter.getCheckedAccount() == null) {
                    CustomToast.showToast(getApplicationContext(), "请选取群聊用户", 1000);
                } else {
                    CreateGroupChatTask createGroupChatTask = new CreateGroupChatTask
                            (et_groupName.getText().toString(), et_desc.getText().toString()
                                    , adapter.getCheckedAccount());
                    createGroupChatTask.execute();
                }
            }
        });
    }

    private void init() {
        btn_next.setText(R.string.ativity_create_group_chat_create);
        tv_title.setText(R.string.ativity_create_group_chat_createGroup);
        GetFriendsAsyncTask getFriendsAsyncTask = new GetFriendsAsyncTask();
        getFriendsAsyncTask.execute();
    }

    // 查询用户好友列表task
    class GetFriendsAsyncTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(CreateGroupChatActivity.this, R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                usernames = EMContactManager.getInstance().getContactUserNames();//需异步执行
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            dailog.dismiss();
            adapter = new CreateGroupChatListAdapter(CreateGroupChatActivity.this, usernames);
            lv_friends.setAdapter(adapter);
            super.onPostExecute(o);
        }
    }

    // 创建群聊的Task
    class CreateGroupChatTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(CreateGroupChatActivity.this, R.style.DialogNormal);
        private String groupName, desc;
        private String[] members;

        public CreateGroupChatTask(String groupName, String desc, String[] members) {
            this.groupName = groupName;
            this.desc = desc;
            this.members = members;
        }

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                //groupName：要创建的群聊的名称
                //desc：群聊简介
                //members：群聊成员,为空时这个创建的群组只包含自己
                //allowInvite:是否允许群成员邀请人进群
                EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members, true);//需异步执行
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            dailog.dismiss();
            finish();
            super.onPostExecute(o);
        }
    }
}
