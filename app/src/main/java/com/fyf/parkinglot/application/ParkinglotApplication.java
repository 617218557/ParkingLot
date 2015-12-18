package com.fyf.parkinglot.application;

import android.app.Application;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.easemob.chat.EMChat;
import com.fyf.parkinglot.push.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by fengyifei on 15/11/28.
 */
public class ParkinglotApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        initPush();
        initIM();
    }

    // 即时通信
    private void initIM(){
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

    private void initImageLoader() {
        // ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }
}
