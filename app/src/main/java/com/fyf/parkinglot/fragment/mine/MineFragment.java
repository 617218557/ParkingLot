package com.fyf.parkinglot.fragment.mine;

/**
 * Created by fengyifei on 15/11/24.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.activity.login.LoginActivity;
import com.fyf.parkinglot.activity.myCar.MyCarActivity;
import com.fyf.parkinglot.activity.record.RecordActivity;
import com.fyf.parkinglot.activity.updateUserInfo.UpdateUserInfoActivity;
import com.fyf.parkinglot.activity.updateUserPassword.UpdateUserPasswordActivity;
import com.fyf.parkinglot.common.GlobalDefine;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.push.MyPushMessageReceiver;
import com.fyf.parkinglot.utils.DataOperateUtils;
import com.fyf.parkinglot.utils.NotifyDataChangeUtils;
import com.fyf.parkinglot.view.CustomToast;
import com.fyf.parkinglot.view.HaloView;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MineFragment extends Fragment {

    private CircleImageView iv_user;
    private TextView tv_name, tv_gender, tv_age, tv_phoneNum;
    private Button btn_edit;
    private HaloView haloView;
    private RelativeLayout ll_car, ll_record, ll_changePassword, ll_logout;

    private String imgPath;

    private int RESULT_PHOTO = 10, RESULT_CAMERA = 20;// activityForResult状态码

    private View rootView;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            haloView.inva();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
            findView(rootView);
            setListener();
            init();
            creatHalo();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }


    @Override
    public void onResume() {
        if (NotifyDataChangeUtils.REFRESH_USER_HEAD_IMG == 1) {
            if (DataOperateUtils.getHead(getActivity()) != null) {
                iv_user.setImageBitmap(DataOperateUtils.getHead(getActivity()));
            }
            NotifyDataChangeUtils.REFRESH_USER_HEAD_IMG = 0;
        }
        if (NotifyDataChangeUtils.REFRESH_USER_INFO == 1) {
            tv_name.setText(UserInfoInCache.user_name);
            tv_gender.setText(UserInfoInCache.user_age == 0 ? "女" : "男");
            tv_age.setText(UserInfoInCache.user_age + "");
            tv_phoneNum.setText(UserInfoInCache.user_phoneNum + "");
            NotifyDataChangeUtils.REFRESH_USER_INFO = 0;
        }
        super.onResume();
    }

    private void findView(View v) {
        haloView = (HaloView) v.findViewById(R.id.fragment_mine_view_haloView);
        iv_user = (CircleImageView) v.findViewById(R.id.fragment_mine_iv_user);
        tv_name = (TextView) v.findViewById(R.id.fragment_mine_tv_name);
        tv_gender = (TextView) v.findViewById(R.id.fragment_mine_tv_gender);
        tv_age = (TextView) v.findViewById(R.id.fragment_mine_tv_age);
        tv_phoneNum = (TextView) v.findViewById(R.id.fragment_mine_tv_phoneNum);
        btn_edit = (Button) v.findViewById(R.id.fragment_mine_btn_edit);
        ll_car = (RelativeLayout) v.findViewById(R.id.fragment_mine_ll_car);
        ll_record = (RelativeLayout) v.findViewById(R.id.fragment_mine_ll_record);
        ll_changePassword = (RelativeLayout) v.findViewById(R.id.fragment_mine_ll_changePassword);
        ll_logout = (RelativeLayout) v.findViewById(R.id.fragment_mine_ll_logout);
    }

    private void setListener() {
        // 编辑用户信息
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateUserInfoActivity.class));
            }
        });
        // 设置头像
        iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("提示")
                        .setPositiveButton("从相册选取图片", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getImage();
                            }
                        }).setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCamera();
                    }
                }).create().show();
            }
        });
        //我的车辆
        ll_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyCarActivity.class));
            }
        });
        // 停车记录
        ll_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RecordActivity.class));
            }
        });
        // 修改密码
        ll_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateUserPasswordActivity.class));
            }
        });
        // 退出登录
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setMessage("退出登录?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataOperateUtils.clearAllData(getActivity());
                                EMChatManager.getInstance().logout();
                                MyPushMessageReceiver.unregistPushTag(getActivity(), UserInfoInCache.user_id + "");
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });
    }

    private void init() {
        tv_name.setText(UserInfoInCache.user_name);
        tv_gender.setText(UserInfoInCache.user_age == 0 ? "女" : "男");
        tv_age.setText(UserInfoInCache.user_age + "");
        tv_phoneNum.setText(UserInfoInCache.user_phoneNum + "");
        if (DataOperateUtils.getHead(getActivity()) != null) {
            iv_user.setImageBitmap(DataOperateUtils.getHead(getActivity()));
        }
    }


    // 生成光晕
    private void creatHalo() {
        haloView.loadFlower();
        haloView.addRect();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0x999;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(timerTask, 0, 85);
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
                ShowImage(requestCode, data);
            if (requestCode == RESULT_CAMERA) {
                showCameraImage(requestCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showCameraImage(int requestCode, Intent data) {
        Intent intent = new Intent();
        intent.putExtra("imagePath", imgPath);
        intent.setClass(getActivity(), CropperUserActivity.class);
        startActivity(intent);
    }

    /**
     * @param requestCode
     * @param data        显示图片
     */
    private void ShowImage(int requestCode, Intent data) {
        Uri imageUri = data.getData();
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(imageUri, pojo, null, null, null);
        String imagePath = null;
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            imagePath = cursor.getString(columnIndex);
            cursor.close();

            Intent intent = new Intent();
            intent.putExtra("imagePath", imagePath);
            intent.setClass(getActivity(), CropperUserActivity.class);
            startActivity(intent);
        } else {
            CustomToast.showToast(getActivity().getApplicationContext(), "选取图片错误", 1000);
        }

    }
}