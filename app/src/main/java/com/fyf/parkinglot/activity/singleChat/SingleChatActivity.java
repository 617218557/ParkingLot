package com.fyf.parkinglot.activity.singleChat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.view.CustomToast;

/**
 * Created by fengyifei on 15/12/18.
 */
public class SingleChatActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;
    private ListView lv_message;
    private EditText et_message;
    private Button btn_send;

    private SingleChatListAdapter singleChatListAdapter;

    private String toChatUsername = "";// 对话人账号
    private EMConversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        findView();
        setListener();
        getDataFromIntent();
        init();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);
        lv_message = (ListView) findViewById(R.id.activity_single_chat_lv_message);
        et_message = (EditText) findViewById(R.id.activity_single_chat_et_message);
        btn_send = (Button) findViewById(R.id.activity_single_chat_btn_send);
    }

    private void setListener() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_message.getText().toString().equals("")) {
                    CustomToast.showToast(getApplicationContext(), "不能发送空消息", 1000);
                } else {
                    sendTextMessage(et_message.getText().toString());
                }
            }
        });
    }

    private void getDataFromIntent() {
        toChatUsername = getIntent().getStringExtra("imAccount");
    }

    private void init() {
        tv_title.setText(toChatUsername);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 获取当前conversation对象
        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();
        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        singleChatListAdapter = new SingleChatListAdapter(SingleChatActivity.this,conversation.getAllMessages());
        lv_message.setAdapter(singleChatListAdapter);
    }


    //发送消息方法
    //==========================================================================
    protected void sendTextMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message) {
        //发送消息
        EMChatManager.getInstance().sendMessage(message, null);
        //刷新ui
        singleChatListAdapter.updateData(conversation.getAllMessages());
        et_message.setText("");
    }
    //===================================================================================

    @Override
    protected void onDestroy() {
        // 解除监听
        EMChatManager.getInstance().unregisterEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub

            }
        });
        super.onDestroy();
    }
}
