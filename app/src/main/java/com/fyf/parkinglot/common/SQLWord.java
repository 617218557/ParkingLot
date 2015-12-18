package com.fyf.parkinglot.common;

/**
 * Created by fengyifei on 15/11/23.
 */
public class SQLWord {

    // 表名、字段名
    /**
     * //用户信息表
     */
    public static String USER_TABLE_NAME = "t_user";
    public static String USER_ID = "user_id";
    public static String USER_NAME = "user_name";
    public static String USER_PHONENUM = "user_phoneNum";
    public static String USER_PASSWORD = "user_password";
    public static String USER_AGE = "user_age";
    public static String USER_GENDER = "user_gender";
    public static String USER_IMG = "user_img";

    /**
     * 车辆信息表
     */
    public static String CAR_TABLE_NAME = "t_car";
    public static String CAR_ID = "car_id";
    public static String CAR_LICENSENUM = "car_licenseNum";
    public static String CAR_TYPE = "car_type";
    public static String CAR_IMG = "car_img";

    /**
     * 停车场表
     */
    public static String PARKINGLOT_TABLE_NAME = "t_parkinglot";
    public static String PARK_ID = "park_id";
    public static String PARK_FEE = "park_fee";
    public static String PARK_ISUSE = "park_isUse";
    public static String PARK_CAR = "park_car";
    public static String PARK_STARTTIME = "park_startTime";

    /**
     * 停车记录表
     */
    public static String PARKINGRECORD_TABLE_NAME = "t_parkingRecord";
    public static String RECORD_ID = "record_id";
    public static String RECORD_STARTTIME = "record_strartTime";
    public static String RECORD_ENDTIME = "record_endTime";
    public static String RECORD_FEE = "record_fee";

    public static String ADMIN_TABLE_NAME = "t_admin"; // 管理员表
    public static String ADMIN_ID = "admin_id";
    public static String ADMIN_USER = "admin_user";
    public static String ADMIN_PASSWORD = "admin_password";

    /**
     * 即时通信表
     */
    public static String IM_TABLE_NAME = "t_im";
    public static String IM_ID  = "im_id";
    public static String IM_ACCOUNT = "im_account";
    public static String IM_PASSWORD = "im_password";

}
