package com.fyf.parkinglot.utils;

import android.content.Context;

import com.fyf.parkinglot.common.SQLWord;
import com.fyf.parkinglot.model.CarInfoBean;
import com.fyf.parkinglot.model.IMInfoBean;
import com.fyf.parkinglot.model.ParkingRecordInfoBean;
import com.fyf.parkinglot.model.ParkinglotInfoBean;
import com.fyf.parkinglot.model.ResultInfoBean;
import com.fyf.parkinglot.model.UserInfoBean;
import com.fyf.parkinglot.model.UserInfoInCache;
import com.fyf.parkinglot.push.MyPushMessageReceiver;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.sauronsoftware.base64.Base64;

/**
 * Created by fengyifei on 15/11/23.
 * j解析json
 */
public class JsonUtils {
    private static Gson gson = new Gson();

    // 解析结果
    public static ResultInfoBean getResult(String json) {
        return gson.fromJson(json, ResultInfoBean.class);
    }

    //  获取结果状态码
    public static int getResultCode(String json) {
        int code = -1;
        try {
            JSONObject obj = new JSONObject(json);
            code = obj.getInt("code");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return code;
    }

    // 获取结果内容
    public static String getResultMsgString(String json) {
        String str = "";
        try {
            JSONObject obj = new JSONObject(json);
            str = obj.getString("msg");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }

    // 解析并设置用户信息
    public static boolean getUserInfo(Context context, String json) {
        UserInfoBean userInfoBean = gson.fromJson(json, UserInfoBean.class);
        UserInfoInCache.user_age = userInfoBean.getUser_age();
        UserInfoInCache.user_gender = userInfoBean.getUser_gender();
        UserInfoInCache.user_id = userInfoBean.getUser_id();
        UserInfoInCache.user_img = userInfoBean.getUser_img();
        try {
            UserInfoInCache.user_name = Base64.decode(userInfoBean.getUser_name());
        } catch (Exception e) {
            e.printStackTrace();
            UserInfoInCache.user_name = userInfoBean.getUser_name();
        }

        UserInfoInCache.user_password = userInfoBean.getUser_password();
        UserInfoInCache.user_phoneNum = userInfoBean.getUser_phoneNum();
        MyPushMessageReceiver.registPushTag(context, UserInfoInCache.user_id + "");
        return true;
    }

    // 解析停车场信息
    public static ArrayList<ParkinglotInfoBean> getParkinglot(String json) {
        ArrayList<ParkinglotInfoBean> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            int len = array.length();
            for (int i = 0; i < len; i++) {
                list.add(gson.fromJson(array.getJSONObject(i).toString(), ParkinglotInfoBean.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 解析车辆信息
    public static ArrayList<CarInfoBean> getCarlist(String json) {
        ArrayList<CarInfoBean> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            int len = array.length();
            for (int i = 0; i < len; i++) {
                JSONObject obj = array.getJSONObject(i);
                CarInfoBean carInfoBean = new CarInfoBean();
                carInfoBean.setCar_id(obj.getInt(SQLWord.CAR_ID));
                carInfoBean.setCar_img(obj.getString(SQLWord.CAR_IMG));
                try {
                    carInfoBean.setCar_licenseNum(Base64.decode(obj.getString(SQLWord.CAR_LICENSENUM)));
                } catch (Exception e) {
                    e.printStackTrace();
                    carInfoBean.setCar_licenseNum(obj.getString(SQLWord.CAR_LICENSENUM));
                }
                try {
                    carInfoBean.setCar_type(Base64.decode(obj.getString(SQLWord.CAR_TYPE)));
                } catch (Exception e) {
                    e.printStackTrace();
                    carInfoBean.setCar_type(obj.getString(SQLWord.CAR_TYPE));
                }
                carInfoBean.setUser_id(obj.getInt(SQLWord.USER_ID));
                list.add(carInfoBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 解析停车记录信息
    public static ArrayList<ParkingRecordInfoBean> getRecordList(String json) {
        ArrayList<ParkingRecordInfoBean> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            int len = array.length();
            for (int i = 0; i < len; i++) {
                list.add(gson.fromJson(array.getJSONObject(i).toString(), ParkingRecordInfoBean.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 解析即时通信用户信息
    public static IMInfoBean getImInfo(String json){
        return gson.fromJson(json,IMInfoBean.class);
    }
}
