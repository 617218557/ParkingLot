package com.fyf.parkinglot.activity.myFriends;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easemob.chat.EMContactManager;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.List;

/**
 * Created by fengyifei on 15/12/17.
 */
public class MyFriendsActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;

    private JazzyListView lv_friends;
    private MyFriendsListAdapter adapter;
    private List<String> usernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        findView();
        setListener();
        init();
    }


    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);
        lv_friends = (JazzyListView) findViewById(R.id.activity_my_friends_lv_friends);
    }

    private void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        tv_title.setText(R.string.activity_my_friends_friends);
        btn_next.setVisibility(View.INVISIBLE);
        GetFriendsAsyncTask GetFriendsAsyncTask = new GetFriendsAsyncTask();
        GetFriendsAsyncTask.execute();
    }

    // 查询用户好友列表task
    class GetFriendsAsyncTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(MyFriendsActivity.this, R.style.DialogNormal);

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
            adapter = new MyFriendsListAdapter(MyFriendsActivity.this, usernames);
            lv_friends.setAdapter(adapter);
            super.onPostExecute(o);
        }
    }

}
