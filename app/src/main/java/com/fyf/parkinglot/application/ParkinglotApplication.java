package com.fyf.parkinglot.application;

import android.app.Application;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.easemob.chat.EMChat;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.fyf.parkinglot.push.Utils;

/**
 * Created by fengyifei on 15/11/28.
 */
public class ParkinglotApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
        initIM();
        initFresco();
    }

    private void initFresco() {
        Fresco.initialize(getApplicationContext());
    }

    // 即时通信
    private void initIM() {
        EMChat.getInstance().init(getApplicationContext());
        EMChat.getInstance().setDebugMode(true);
    }

    private void initPush() {
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(getApplicationContext(), "BAIDU_PUSH_API_KEY"));
        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
        PushManager.enableLbs(getApplicationContext());
    }
}
