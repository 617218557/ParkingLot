package com.fyf.parkinglot.fragment.myGroup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.groupChat.GroupChatActivity;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.google.gson.Gson;
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

    public void init() {
        GetGroupsAsyncTask getFriendsAsyncTask = new GetGroupsAsyncTask();
        getFriendsAsyncTask.execute();
    }

    public void updateMyGroup() {
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
                    Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                    intent.putExtra("group", new Gson().toJson(groupList.get(position)));
                    startActivity(intent);
                }
            });
            lv_groups.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    if(groupList.get(position).getOwner().equals(UserInfoInCache.user_phoneNum)){
                        // 是群主
                        new AlertDialog.Builder(getActivity()).setTitle("确认解散群("
                                + groupList.get(position).getGroupName() + ")")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ExitAndDeleteGroupTask exitAndDeleteGroupTask = new ExitAndDeleteGroupTask(groupList.get(position).getGroupId());
                                exitAndDeleteGroupTask.execute();
                                dialog.dismiss();
                            }
                        }).create().show();
                    }else{
                        // 不是群主
                        new AlertDialog.Builder(getActivity()).setTitle("确认退出群("
                                + groupList.get(position).getGroupName() + ")")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ExitGroupTask exitGroupTask = new ExitGroupTask(groupList.get(position).getGroupId());
                                exitGroupTask.execute();
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                    return false;
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

    // 退出群聊
    class ExitGroupTask extends AsyncTask {
        private CustomPrgressDailog dailog = new CustomPrgressDailog(getActivity(), R.style.DialogNormal);
        private String groupId;

        public ExitGroupTask(String groupId) {
            this.groupId = groupId;
        }

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                EMGroupManager.getInstance().exitFromGroup(groupId);//需异步处理
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            init();
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }

    // 解散群聊
    class ExitAndDeleteGroupTask extends AsyncTask {
        private CustomPrgressDailog dailog = new CustomPrgressDailog(getActivity(), R.style.DialogNormal);
        private String groupId;

        public ExitAndDeleteGroupTask(String groupId) {
            this.groupId = groupId;
        }

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                EMGroupManager.getInstance().exitAndDeleteGroup(groupId);//需异步处理
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            init();
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }

}
