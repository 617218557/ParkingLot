package com.fyf.parkinglot.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.fyf.parkinglot.common.GlobalDefine;
import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.model.UserInfoInCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengyifei on 15/11/23.
 * 对数据操作
 */
public class DataOperateUtils {

    // 存储登录信息
    public static void saveLoginInfo(Context context, String user_phoneNum, String user_password) {
        SharedPreferences sp = context.getSharedPreferences
                (GlobalDefine.SHAREDPRE_USER_LOGIN_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(SQLWord.USER_PHONENUM, user_phoneNum);
        e.putString(SQLWord.USER_PASSWORD, user_password);
        e.commit();
    }

    //  获取登录信息
    public static Map<String, String> readLoginInfo(Context context) {
        Map<String, String> map = new HashMap<String, String>();
        SharedPreferences sp = context.getSharedPreferences
                (GlobalDefine.SHAREDPRE_USER_LOGIN_INFO, Context.MODE_PRIVATE);
        String user_phoneNum = sp.getString(SQLWord.USER_PHONENUM, "");
        String user_password = sp.getString(SQLWord.USER_PASSWORD, "");
        if (!user_phoneNum.equals("")) {
            map.put(SQLWord.USER_PHONENUM, user_phoneNum);
        }
        if (!user_password.equals("")) {
            map.put(SQLWord.USER_PASSWORD, user_password);
        }
        return map;
    }

    // 存储用户的头像
    public static void savaHead(Context context, Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("head.png", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // 得到用户的头像
    public static Bitmap getHead(Context context) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new Options(); // 解析位图的附加条件
        opts.inJustDecodeBounds = false;
        File dir = context.getFilesDir();
        File f = new File(dir.getPath() + "/head.png");
        if (f.exists())
            bitmap = BitmapFactory
                    .decodeFile(dir.getPath() + "/head.png", opts);
        return bitmap;
    }

    // 删除用户头像
    public static void delHead(Context context) {
        File dir = context.getFilesDir();
        File file = new File(dir.getPath() + "/head.png");
        DeleteFile(file);
    }


    public static void clearAllData(Context context) {
        // DeleteFile(getAppPackagePath(context));
        SharedPreferences sp = context.getSharedPreferences(GlobalDefine.SHAREDPRE_USER_LOGIN_INFO,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        UserInfoInCache.clear();
        new File(context.getFilesDir(), "/head.png").delete();
    }

    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

}
