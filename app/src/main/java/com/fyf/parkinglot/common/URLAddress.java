package com.fyf.parkinglot.common;

/**
 * Created by fengyifei on 15/11/23.
 */
public class URLAddress {

    public static final String publicURL = "";
    public static final String localURL = "http://115.28.111.133:8080/Parkinglot";
    public static final String URL = localURL;
    public static final String QINIU_URL = "http://www.7xoqdw.com1.z0.glb.clouddn.com";

    public static final String loginURL = URL + "/userlogincl";// 登录
    public static final String registURL = URL + "/userregistcl"; // 注册
    public static final String allSpaceInfoURl = URL + "/findallparkingspacecl"; // 查询所有车位信息
    public static final String findUserCarURL = URL + "/findcarinfocl"; // 查询用户车辆
    public static final String orderSPaceURl = URL + "/orderparkingspacecl"; // 预约车位
    public static final String findTodayOrderURl = URL + "/findtodayorderbyuseridcl";// 查询用户今日预约
    public static final String updateUserInfoURL = URL + "/updateuserinfocl";// 更新用户信息
    public static final String updateUserPasswordURL = URL + "/updateuserpasswordcl";// 查询用户密码
    public static final String findRecordByUserIdURL = URL + "/findparkingrecordbyuseridcl";//根据用户id查询停车记录
    public static final String cancleOrderURL = URL + "/cancleorderparkingspacecl"; //  取消预约
    public static final String endParkingURL = URL + "/endorderparkingspacecl";// 结束停车
    public static final String addCarURL = URL + "/addcarinfocl";//  添加车辆
    public static final String updateCarURL = URL + "updatecarinfocl";// 更新车辆
    public static final String deleteCarURL = URL + "/deletecarinfocl";// 删除车辆
    public static final String findImaccountURl = URL + "/findimaccountcl";// 查找即时通信账户
    public static final String weatherForcastURL = "http://api.map.baidu.com/telematics/v3/weather";// 天气预报
}
