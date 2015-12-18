package com.fyf.parkinglot.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.fyf.parkinglot.common.GlobalDefine;
import com.qiniu.util.Auth;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengyifei on 15/11/23.
 */
public class Utils {

    // 手机号验证
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|177)\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    // base64解码
    public static String base64Decode(String str) {
        return new String((Base64.decode(str, Base64.NO_WRAP)));
    }


    // 获取上传token
    public static String getUploadToken() {
        Auth auth = Auth.create(GlobalDefine.QINIU_AK, GlobalDefine.QINIU_SK);
        String uploadToken = auth.uploadToken("fyfparkinglot");
        Log.e("七牛上传token", uploadToken);
        return uploadToken;
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    // 与当前时间比较,现在时间小于比较时间,返回true
    public static boolean comparePointTime(String time) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            long timeNow = df.parse(dateFormat.format(now)).getTime();
            long timeT = df.parse(time).getTime();
            if (timeNow < timeT)
                return true;
            else
                return false;
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static String saveBitmapFile(Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/parkinglot";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + "/" + fileName + ".jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile.getAbsolutePath();
    }
}
