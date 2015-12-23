package com.fyf.parkinglot.activity.groupInfo;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.common.ContextManager;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.ListViewUtils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.MyListView;
import com.google.gson.Gson;

/**
 * Created by fengyifei on 15/12/21.
 * 群聊群组详细信息
 */
public class GroupInfoActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;
    private RelativeLayout ll_groupName, ll_groupDesc;
    private TextView tv_groupName, tv_groupDesc;
    private MyListView lv_members;
    private ImageView iv_groupName;

    private GroupMembersListAdapter adapter;
    private EMGroup toChatGroup;// 群信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        getDataFromIntent();
        findView();
        setListener();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);
        ll_groupName = (RelativeLayout) findViewById(R.id.activity_group_info_ll_groupName);
        ll_groupDesc = (RelativeLayout) findViewById(R.id.activity_group_info_ll_groupDesc);

        tv_groupName = (TextView) findViewById(R.id.activity_group_info_tv_groupName);
        tv_groupDesc = (TextView) findViewById(R.id.activity_group_info_tv_groupDesc);
        lv_members = (MyListView) findViewById(R.id.activity_group_info_lv_members);

        iv_groupName = (ImageView) findViewById(R.id.activity_group_info_iv_groupName);
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

            }
        });
        if (toChatGroup.getOwner().equals(UserInfoInCache.user_phoneNum)) {
            // 该用户是群主
            ll_groupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText et = new EditText(GroupInfoActivity.this);
                    new AlertDialog.Builder(GroupInfoActivity.this).setView(et)
                            .setTitle("输入新群聊名称")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        EMGroupManager.getInstance().changeGroupName
                                                (toChatGroup.getGroupId(), et.getText().toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            // 更新相关信息
                            tv_groupName.setText(et.getText().toString());
                            if (ContextManager.friendsFragment != null) {
                                ContextManager.friendsFragment.updateGroupList();
                            }
                            if (ContextManager.groupChatActivity != null) {
                                ContextManager.groupChatActivity.updateTitle(et.getText().toString());
                            }
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            });
        }
        ll_groupDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getDataFromIntent() {
        toChatGroup = new Gson().fromJson(getIntent().getStringExtra("group"), EMGroup.class);
        GetGroupInfoTask getGroupInfoTask = new GetGroupInfoTask();
        getGroupInfoTask.execute();
    }

    private void init() {
        tv_title.setText(getString(R.string.activity_group_info_info)
                + "(" + toChatGroup.getMembers().size() + "人)");
        btn_next.setText(R.string.activity_group_info_addMember);
        tv_groupName.setText(toChatGroup.getGroupName());
        if (toChatGroup.getOwner().equals(UserInfoInCache.user_phoneNum)) {
            // 该用户是群主
            iv_groupName.setVisibility(View.VISIBLE);
        } else {
            iv_groupName.setVisibility(View.INVISIBLE);
        }
        tv_groupDesc.setText(toChatGroup.getDescription());
        adapter = new GroupMembersListAdapter(GroupInfoActivity.this, toChatGroup.getMembers());
        lv_members.setAdapter(adapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_members);
    }

    class GetGroupInfoTask extends AsyncTask {
        CustomPrgressDailog dialog = new CustomPrgressDailog(GroupInfoActivity.this, R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                //根据群聊ID从服务器获取群聊基本信息
                toChatGroup = EMGroupManager.getInstance().getGroupFromServer(toChatGroup.getGroupId());
                //保存获取下来的群聊基本信息
                EMGroupManager.getInstance().createOrUpdateLocalGroup(toChatGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            init();
            dialog.dismiss();
            super.onPostExecute(o);
        }
    }

    private void updateData() {
        GetGroupInfoTask getGroupInfoTask = new GetGroupInfoTask();
        getGroupInfoTask.execute();
    }
}
