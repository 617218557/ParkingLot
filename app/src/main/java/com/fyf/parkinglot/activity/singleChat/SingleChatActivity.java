package com.fyf.parkinglot.activity.singleChat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
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
import com.fyf.parkinglot.common.GlobalDefine;
import com.fyf.parkinglot.view.CustomToast;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by fengyifei on 15/12/18.
 */
public class SingleChatActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;
    private ListView lv_message;
    private EditText et_message;
    private Button btn_sendMore;
    private Button btn_send;

    private SingleChatListAdapter singleChatListAdapter;

    private String toChatUsername = "";// 对话人账号
    private EMConversation conversation;
    private String imgPath;
    private int RESULT_PHOTO = 10, RESULT_CAMERA = 20;// activityForResult状态码

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
        btn_sendMore = (Button) findViewById(R.id.activity_single_chat_btn_sendMore);
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
        btn_sendMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送更多类型的消息
                final String[] items = {"拍照", "从相册选取图片"};
                new AlertDialog.Builder(SingleChatActivity.this).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                getCamera();
                                dialog.dismiss();
                                break;
                            case 1:
                                getImage();
                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();
            }
        });
        // 设置新消息监听
        EMChatManager.getInstance().registerEventListener
                (new EMEventListener() {
                    @Override
                    public void onEvent(EMNotifierEvent event) {
                        // TODO Auto-generated method stub
                        //刷新ui
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 在UI线程中更新ui
                                        conversation.markAllMessagesAsRead();
                                        singleChatListAdapter.updateData(conversation.getAllMessages());
                                        et_message.setText("");
                                        lv_message.setSelection(ListView.FOCUS_DOWN);
                                    }
                                });
                            }
                        }).start();
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
        singleChatListAdapter = new SingleChatListAdapter(SingleChatActivity.this, conversation.getAllMessages());
        lv_message.setAdapter(singleChatListAdapter);
        lv_message.setSelection(ListView.FOCUS_DOWN);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在UI线程中更新ui
                        singleChatListAdapter.updateData(conversation.getAllMessages());
                        et_message.setText("");
                        lv_message.setSelection(ListView.FOCUS_DOWN);
                    }
                });

            }
        }).start();

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


    /**
     * @category 从相册里获取图片
     */
    public void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RESULT_PHOTO);
    }

    /**
     * @category 调用相机获取图片
     */
    public void getCamera() {
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.getDefault())) + ".jpg";
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(path + "/" + GlobalDefine.APP_NAME + "/");
            if (!file.exists())
                file.mkdirs();// 创建文件夹
            imgPath = path + "/" + GlobalDefine.APP_NAME + "/" + name;
            File tempFile = new File(Environment.getExternalStorageDirectory(), "/" + GlobalDefine.APP_NAME + "/" + name);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempFile.getAbsolutePath())));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, RESULT_CAMERA);
        }
    }

    // 得到用户选择的图片并显示
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_PHOTO)
                sendImage(requestCode, data);
            if (requestCode == RESULT_CAMERA) {
                sendCameraImage(requestCode, data);
            }
        } else {
            imgPath = "";
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 发送拍照图片
    private void sendCameraImage(int requestCode, Intent data) {
        sendImageMessage(imgPath);
    }

    /**
     * @param requestCode
     * @param data        发送相册图片
     */
    private void sendImage(int requestCode, Intent data) {
        try {
            Uri imageUri = data.getData();
            imgPath = uri2filePath(imageUri, SingleChatActivity.this);
            sendImageMessage(imgPath);
        } catch (Exception e) {
            e.printStackTrace();
            CustomToast.showToast(getApplicationContext(), "图片选取错误", 1000);
        }
    }

    // android4.4会出现从相册选取图片查询路径为空的情况,所以有以下方法
    private String uri2filePath(Uri uri, Activity activity) {
        String path = "";
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(activity, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = activity.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                    new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex);
            }
            cursor.close();
        } else {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();
            path = cursor.getString(column_index);
        }
        return path;
    }
}
