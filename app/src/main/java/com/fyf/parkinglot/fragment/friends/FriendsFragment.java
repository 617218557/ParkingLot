package com.fyf.parkinglot.fragment.friends;

/**
 * Created by fengyifei on 15/11/24.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.addFriends.AddFriendsActivity;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.IMInfoBean;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendsFragment extends Fragment {

    private View rootView;
    private ViewPager vp_viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fa_fab;// 悬浮按钮

    private FriendsPagerAdapter friendsPagerAdapter;

    private IMInfoBean imInfoBean;

    private final int MSG_DIALOG = 0x000, MSG_TOAST = 0x001;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DIALOG:
                    final String ImAccount = msg.obj.toString().substring(0, 11);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("好友请求")
                            .setMessage("账号:" + msg.obj.toString())
                            .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                EMChatManager.getInstance().acceptInvitation
                                                        (ImAccount);//需异步处理
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        EMChatManager.getInstance().refuseInvitation
                                                (ImAccount);//需异步处理
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            dialog.dismiss();
                        }
                    });
                    dialog.create().show();
                    break;
                case 0x001:
                    CustomToast.showToast(getActivity().getApplicationContext()
                            , msg.obj.toString(), 1000);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_friends, container, false);
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

    private void findView(View rootView) {
        vp_viewPager = (ViewPager) rootView.findViewById(R.id.fragment_friends_vp_container);
        tabLayout = (TabLayout) rootView.findViewById(R.id.fragment_friends_tabs);
        fa_fab = (FloatingActionButton) rootView.findViewById(R.id.fragment_friends_fa_fab);
    }

    private void setListener() {
        fa_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String items[] = {"添加好友", "发起群聊"};
                new AlertDialog.Builder(getActivity()).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                startActivity(new Intent(getActivity(), AddFriendsActivity.class));
                                dialog.dismiss();
                                break;
                            case 1:

                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();
            }
        });
    }

    private void init() {

        FindImAccountTask findImAccountTask = new FindImAccountTask();
        findImAccountTask.execute();
    }

    /**
     * 有服务器查询用户是否注册过聊天,如果注册过,则返回账号密码直接登录,否则返回服务器生成的账号密码本地注册登录
     */
    class FindImAccountTask extends AsyncTask {
        CustomPrgressDailog dialog = new CustomPrgressDailog(getActivity(), R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder().add(SQLWord.USER_ID, UserInfoInCache.user_id + "")
                    .add(SQLWord.IM_ACCOUNT, UserInfoInCache.user_phoneNum).build();
            return HttpUtils.httpPost(URLAddress.findImaccountURl, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            String json = o.toString();
            if (json == null || json.equals("")) {
                CustomToast.showToast(getActivity().getApplicationContext(), "初始化失败", 1000);
                return;
            }
            if (JsonUtils.getResultCode(json) < 1) {
                // 初始化失败,并显示原因
                CustomToast.showToast(getActivity().getApplicationContext()
                        , JsonUtils.getResultMsgString(json), 1000);
            } else if (JsonUtils.getResultCode(json) == 2) {
                // 用户未注册即时通信
                imInfoBean = JsonUtils.getImInfo(JsonUtils.getResultMsgString(json));
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            // 调用sdk注册方法
                            EMChatManager.getInstance().createAccountOnServer
                                    (imInfoBean.getIm_account(), imInfoBean.getIm_password());
                        } catch (final EaseMobException e) {
                            //注册失败
                        }
                    }
                }).start();
            } else {
                // 用户已注册即时通信
                imInfoBean = JsonUtils.getImInfo(JsonUtils.getResultMsgString(json));
            }
            EMChatManager.getInstance().login(imInfoBean.getIm_account(), imInfoBean.getIm_password()
                    , new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                            regReceiver();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            friendsPagerAdapter = new FriendsPagerAdapter(getActivity().getSupportFragmentManager());
                                            vp_viewPager.setAdapter(friendsPagerAdapter);
                                            tabLayout.setupWithViewPager(vp_viewPager);
                                        }
                                    });
                                }
                            }).start();
                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    CustomToast.showToast(getActivity().getApplicationContext(), "登录聊天服务器失败", 1000);
                }
            });
            dialog.dismiss();
            super.onPostExecute(o);
        }
    }


    private void regReceiver() {
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);// 使用好友体系
        EMChatManager.getInstance().getChatOptions().setAcceptInvitationAlways(false);// 需要确认好友请求
        // 设置接收消息监听
        EMChatManager.getInstance().registerEventListener
                (new EMEventListener() {
                    @Override
                    public void onEvent(EMNotifierEvent event) {
                        // TODO Auto-generated method stub
                        EMMessage message = (EMMessage) event.getData();
                        if (message.getChatType() == EMMessage.ChatType.Chat) {
                            // 单聊
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            friendsPagerAdapter.myFriendsFragment.onResume();
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                });
        EMContactManager.getInstance().setContactListener(new EMContactListener() {
            @Override
            public void onContactAgreed(String username) {
                //好友请求被同意
                Message msg = new Message();
                msg.what = MSG_TOAST;
                msg.obj = username + "同意成为您的好友";
                handler.sendMessage(msg);
            }

            @Override
            public void onContactRefused(String username) {
                //好友请求被拒绝
                Message msg = new Message();
                msg.what = MSG_TOAST;
                msg.obj = username + "拒绝成为您的好友";
                handler.sendMessage(msg);
            }

            @Override
            public void onContactInvited(final String username, String reason) {
                //收到好友邀请
                Message msg = new Message();
                msg.what = MSG_DIALOG;
                msg.obj = username + "\n" + "理由:" + reason;
                handler.sendMessage(msg);
            }

            @Override
            public void onContactDeleted(List<String> usernameList) {
                //被删除时回调此方法
                for (String str : usernameList) {
                    Message msg = new Message();
                    msg.what = MSG_TOAST;
                    msg.obj = str + "已从好友列表中删除您";
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onContactAdded(List<String> usernameList) {
                //增加了联系人时回调此方法
                for (String str : usernameList) {
                    Message msg = new Message();
                    msg.what = MSG_TOAST;
                    msg.obj = str + "已成为您的好友";
                    handler.sendMessage(msg);
                }
            }
        });
        // 最后要通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了,需要放到最后
        EMChat.getInstance().setAppInited();
    }

}