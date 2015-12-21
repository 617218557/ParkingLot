package com.fyf.parkinglot.fragment.myGroup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.singleChat.SingleChatActivity;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.List;

/**
 * Created by fengyifei on 15/12/21.
 */
public class MyGroupFragment extends Fragment {

    private View rootView;

    private JazzyListView lv_groups;
    private MyGroupListAdapter adapter;
    private List<EMGroup> groupList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_group, container, false);
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
        lv_groups = (JazzyListView) v.findViewById(R.id.fragment_my_group_lv_groups);
    }

    private void setListener() {
    }

    private void init() {
        GetGroupsAsyncTask getFriendsAsyncTask = new GetGroupsAsyncTask();
        getFriendsAsyncTask.execute();
    }

    // 查询用户好友列表task
    class GetGroupsAsyncTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(getActivity(), R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                groupList = EMGroupManager.getInstance().getGroupsFromServer();//需异步处理
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            dailog.dismiss();
            adapter = new MyGroupListAdapter(getActivity(), groupList);
            lv_groups.setAdapter(adapter);
            lv_groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), SingleChatActivity.class);
                    intent.putExtra("groupId", groupList.get(position));
                    startActivity(intent);
                }
            });
            super.onPostExecute(o);
        }
    }

    @Override
    public void onResume() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }

}
