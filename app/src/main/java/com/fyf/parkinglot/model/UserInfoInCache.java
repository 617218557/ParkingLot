package com.fyf.parkinglot.model;

import java.util.ArrayList;

/**
 * Created by fengyifei on 15/11/24.
 */
public class UserInfoInCache {

    public static  int user_id; // id
    public static  String user_name;// 姓名
    public static  String user_phoneNum;// 电话
    public static  String user_password;// 密码
    public static  int user_age;// 年龄
    public static  int user_gender;// 性别(0女 1男)
    public static  String user_img;// 用户头像地址
    public static ArrayList<CarInfoBean> myCarList; //我的车辆信息

    public static void clear(){
        user_id = 0;
        user_name = "";
        user_phoneNum = "";
        user_password = "";
        user_age = 0;
        user_gender = 0;
        user_img = "";
        myCarList = null;
    }

}
