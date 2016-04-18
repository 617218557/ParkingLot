package com.fyf.parkinglot.activity.main;

import android.app.Activity;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.common.GlobalDefine;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.ScreenInfo;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp_viewPager;
    private TabLayout tabLayout;

    private MainPagerAdapter mMainPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setListener();
        setScreenInfo();
        init();
    }

    private void findView() {
        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        vp_viewPager = (ViewPager) findViewById(R.id.activity_main_vp_container);
        vp_viewPager.setAdapter(mMainPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.activity_main_tabs);
        tabLayout.setupWithViewPager(vp_viewPager);
    }

    private void setListener() {

    }

    private void init() {
        GetCarAsyncTask getCarAsyncTask = new GetCarAsyncTask();
        getCarAsyncTask.execute();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + GlobalDefine.APP_NAME + "/");
        if (!file.exists())
            file.mkdirs();// 创建文件夹
    }

    // 获取屏幕信息
    private void setScreenInfo() {
        int screenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        int statusBarHeight = getStatusHeight(this);
        screenHeight = screenHeight - statusBarHeight;

        DisplayMetrics dis = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dis);
        // 密度
        float de = dis.density;
        ScreenInfo.setDe(de);
        ScreenInfo.setScreenHeight(screenHeight);
        ScreenInfo.setScreenWidth(screenWidth);
        ScreenInfo.setStatusBarHeight(statusBarHeight);
    }

    // 获取状态栏高度
    private int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    // 查询用户车辆
    class GetCarAsyncTask extends AsyncTask {
        CustomPrgressDailog dailog = new CustomPrgressDailog(MainActivity.this, R.style.DialogNormal);

        @Override
        protected void onPreExecute() {
            dailog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder()
                    .add(SQLWord.USER_ID, UserInfoInCache.user_id + "").build();
            return HttpUtils.httpPost(URLAddress.findUserCarURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            handleCarResult(o.toString());
            dailog.dismiss();
            super.onPostExecute(o);
        }
    }

    // 处理用户车辆信息
    private void handleCarResult(String json) {
        if (json == null || json.equals("")) {
            return;
        }
        if (JsonUtils.getResultCode(json) < 1) {

        } else {
            // 成功时相关处理
            UserInfoInCache.myCarList = JsonUtils.getCarlist(JsonUtils.getResultMsgString(json));
        }
    }

    @Override
    protected void onDestroy() {
        UserInfoInCache.clear();
        // 退出聊天
        EMChatManager.getInstance().logout(new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });
        super.onDestroy();
    }
}
