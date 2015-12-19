package com.fyf.parkinglot.fragment.myFriends;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.easemob.chat.EMContactManager;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.singleChat.SingleChatActivity;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.List;

/**
 * Created by fengyifei on 15/12/17.
 */
public class MyFriendsFragment extends Fragment {

    private View rootView;

    private JazzyListView lv_friends;
    private MyFriendsListAdapter adapter;
    private List<String> usernames;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_friends, container, false);
            findView(rootView);
            setListener();
            init();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void findView(View v) {
        lv_friends = (JazzyListView) v.findViewById(R.id.fragment_my_friends_lv_friends);
    }

    private void setListener() {
    }

    private void init() {
        GetFriendsAsyncTask GetFriendsAsyncTask = new GetFriendsAsyncTask();
        GetFriendsAsyncTask.execute();
    }

    // 查询用户好友列表task
    class GetFriendsAsyncTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(getActivity(), R.style.DialogNormal);

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
            adapter = new MyFriendsListAdapter(getActivity(), usernames);
            lv_friends.setAdapter(adapter);
            lv_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), SingleChatActivity.class);
                    intent.putExtra("imAccount", usernames.get(position));
                    startActivity(intent);
                }
            });
            super.onPostExecute(o);
        }
    }

}
