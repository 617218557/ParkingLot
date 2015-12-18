package com.fyf.parkinglot.fragment.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.edmodo.cropper.CropImageView;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.utils.DataOperateUtils;
import com.fyf.parkinglot.utils.NotifyDataChangeUtils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;

public class CropperUserActivity extends Activity {

    private static final int ON_TOUCH = 1;

    private CropImageView cropImageView;
    private Button btn_confirm;
    private Bitmap bitmap;
    private CustomPrgressDailog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            dialog.dismiss();
            switch (msg.what) {
                case 0x001:
                    NotifyDataChangeUtils.REFRESH_USER_HEAD_IMG = 1;
                    finish();
                    break;
                case 0x002:
                    CustomToast.showToast(getApplicationContext(), "不支持该类型的图片",
                            1000);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cropper);
        findView();
        initView();
        setListener();
    }

    private void findView() {
        cropImageView = (CropImageView) findViewById(R.id.activity_cropper_iv_cropImageView);
        btn_confirm = (Button) findViewById(R.id.activity_cropper_btn_confirm);
    }

    private void initView() {
        cropImageView.setFixedAspectRatio(false);
        cropImageView.setGuidelines(ON_TOUCH);
        cropImageView.setAspectRatio(100, 100);
        Intent intent = getIntent();
        Bitmap bf = BitmapFactory
                .decodeFile(intent.getStringExtra("imagePath"));
        cropImageView.setImageBitmap(bf);
    }

    private void setListener() {
        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog = new CustomPrgressDailog(CropperUserActivity.this, R.style.DialogNormal);
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            bitmap = cropImageView.getCroppedImage();
                            DataOperateUtils.savaHead(CropperUserActivity.this, bitmap);
                            handler.sendEmptyMessage(0x001);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                            handler.sendEmptyMessage(0x002);
                        }
                    }
                }).start();
            }
        });
    }

}
