package com.fyf.parkinglot.activity.addCar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.fyf.parkinglot.R;
import com.fyf.parkinglot.common.GlobalDefine;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.common.URLAddress;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.utils.HttpUtils;
import com.fyf.parkinglot.utils.JsonUtils;
import com.fyf.parkinglot.utils.Utils;
import com.fyf.parkinglot.view.CustomPrgressDailog;
import com.fyf.parkinglot.view.CustomToast;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import it.sauronsoftware.base64.Base64;

/**
 * Created by fengyifei on 15/12/1.
 */
public class AddCarActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private Button btn_next;

    private AppCompatImageView iv_car;
    private TextInputLayout warpper_type, warpper_licenseNum;
    private AppCompatEditText et_type, et_licenseNum;
    private CircularProgressButton btn_submit;

    private String imgPath;
    private int RESULT_PHOTO = 10, RESULT_CAMERA = 20;// activityForResult状态码
    private CustomPrgressDailog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        findView();
        setListener();
        init();
    }

    private void findView() {
        btn_back = (Button) findViewById(R.id.layout_actionBar_btn_back);
        tv_title = (TextView) findViewById(R.id.layout_actionBar_tv_title);
        btn_next = (Button) findViewById(R.id.layout_actionBar_btn_next);

        iv_car = (AppCompatImageView) findViewById(R.id.activity_add_car_iv_car);
        warpper_type = (TextInputLayout) findViewById(R.id.activity_add_car_warpper_type);
        warpper_licenseNum = (TextInputLayout) findViewById(R.id.activity_add_car_warpper_licenseNum);
        et_type = (AppCompatEditText) findViewById(R.id.activity_add_car_et_type);
        et_licenseNum = (AppCompatEditText) findViewById(R.id.activity_add_car_et_licenseNum);
        btn_submit = (CircularProgressButton) findViewById(R.id.activity_add_car_btn_submit);
    }

    private void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddCarActivity.this).setTitle("提示")
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
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    uploadImage();
                }
            }
        });
    }

    private void init() {
        tv_title.setText(R.string.actionBar_add_car);
        btn_next.setVisibility(View.INVISIBLE);
    }

    private boolean checkForm() {
        if (et_type.getText().toString().equals("")) {
            warpper_type.setError("车辆类型不能为空");
            return false;
        }
        warpper_type.setError("");
        if (et_licenseNum.getText().toString().equals("")) {
            warpper_licenseNum.setError("车牌号不能为空");
            return false;
        }
        warpper_licenseNum.setError("");
        return true;
    }

    // 上传车辆图片
    private void uploadImage() {
        dialog = new CustomPrgressDailog(AddCarActivity.this, R.style.DialogNormal);
        dialog.show();
        if (imgPath == null || imgPath.equals("")) {
            AddCarTask addCarTask = new AddCarTask(URLAddress.QINIU_URL + "/" + GlobalDefine.QINIU_CAR_DEFAULT);
            addCarTask.execute();
        } else {
            UploadManager uploadManager = new UploadManager();
            File data = new File(imgPath);
            String key = UserInfoInCache.user_id + "-" + imgPath.split("/")[imgPath.split("/").length - 1];
            String token = Utils.getUploadToken();
            uploadManager.put(data, key, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
                            AddCarTask addCarTask = new AddCarTask(URLAddress.QINIU_URL + "/" + key);
                            addCarTask.execute();
                        }
                    }, null);
        }
    }

    // 添加车辆的Task
    class AddCarTask extends AsyncTask {
        private String car_type, car_licenseNum, car_image;

        public AddCarTask(String car_image) {
            this.car_image = car_image;
        }

        @Override
        protected void onPreExecute() {
            car_type = et_type.getText().toString();
            car_licenseNum = et_licenseNum.getText().toString();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            RequestBody body = new FormEncodingBuilder()
                    .add(SQLWord.USER_ID, UserInfoInCache.user_id + "")
                    .add(SQLWord.CAR_TYPE, Base64.encode(car_type))
                    .add(SQLWord.CAR_LICENSENUM, Base64.encode(car_licenseNum))
                    .add(SQLWord.CAR_IMG, car_image).build();
            return HttpUtils.httpPost(URLAddress.addCarURL, body);
        }

        @Override
        protected void onPostExecute(Object o) {
            String json = o.toString();
            if (json == null || json.equals("")) {
                dialog.dismiss();
                CustomToast.showToast(getApplicationContext(), "加载失败", 1000);
                return;
            }
            if (JsonUtils.getResultCode(json) < 1) {
                // 显示失败原因
                dialog.dismiss();
                CustomToast.showToast(getApplicationContext(), JsonUtils.getResultMsgString(json), 1000);
            } else {
                // 成功时相关处理
                finish();
            }
            super.onPostExecute(o);
        }
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
        } else {
            imgPath = "";
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showCameraImage(int requestCode, Intent data) {
        Bitmap bmp = BitmapFactory.decodeFile(imgPath);
        iv_car.setImageBitmap(bmp);
    }

    /**
     * @param requestCode
     * @param data        显示图片
     */
    private void ShowImage(int requestCode, Intent data) {
        try {
            Uri imageUri = data.getData();
            imgPath = uri2filePath(imageUri, AddCarActivity.this);
            Bitmap bmp = BitmapFactory.decodeFile(imgPath);
            iv_car.setImageBitmap(bmp);
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
