package com.fyf.parkinglot.common;

/**
 * @category 这里约束一些常量定义
 * @author fengyifei
 */
public class GlobalDefine {
	public static String APP_NAME = "parkinglot";
	// 七牛
	public static String QINIU_AK = "XPHnXDfTqIHcGg5M1nqnaLXtmupPqSEJfKkB3Czz";
	public static String QINIU_SK = "zFC8HWzqUXVxJIT5kQ5aKO_aZVipCJa1U3LSUGUU";
	// 七牛中默认车辆图片名称
	public static String QINIU_CAR_DEFAULT = "car_default.jpg";
	// IM即时通信
	public static String IM_APPKEY= "d0044ef8d4baa85de9e393dd";
	public static String IM_USER_PASSWORD = "parkinglot";
	//百度车联网API
	public static String CAR_API_AK = "fTvBOmDjpfu5bulB06ByOToR";

	// 用户表
	public static int USER_GENDER_FEMALE = 0; // 女性
	public static int USER_GENDER_MALE = 1; // 男性

	// 停车场表
	public static int PARK_NOT_USED = 0; // 车位未被占用
	public static int PARK_USED = 1; // 车位已被占用
	public static double PARK_FEE = 5.0;// 每小时停车费用

	// 用户注册
	public static int REGIST_FAIL = -1; // 注册失败
	public static int REGIST_EXIST = -2;// 用户已存在

	// 用户登录
	public static int LOGIN_FAIL = -1;// 登录失败
	public static int LOGIN_NOT_EXIST = -2;// 用户不存在
	public static int LOGIN_PASSWORD_ERROR = -3;// 密码错误

	// 查找可用车位
	public static int PARK_FIND_FAIL = -2;// 查找失败
	public static int PARK_FIND_NO_NOT_USED = -2;// 暂无可用车位

	// 预约车位
	public static int PARK_ORDER_FAIL = -1;// 预约失败
	public static int PARK_ORDER_USED = -2;// 车位已被占用
	public static int PARK_ORDER_TIME_NOT_LEGEL = -3;// 预约时间不合法

	// 取消预约车位
	public static int PARK_CANCLE_FAIL = -1; // 失败

	// 停止停靠
	public static int PARK_END_FAIL = -1;// 失败

	// 查询车辆信息
	public static int CAR_FIND_FAIL = -1;// 失败

	// 添加车辆信息
	public static int CAR_ADD_FAIl = -1;// 失败

	// 更改车辆信息
	public static int CAR_UPDATE_FAIL = -1;// 失败

	// 删除车辆信息
	public static int CAR_DELETE_FAIL = -1;// 失败

	// 根据用户id查询车辆停靠记录信息
	public static int RECORD_FIND_USER_FAIL = -1;// 失败

	// 根据车辆id查询车辆停靠记录信息
	public static int RECORD_FIND_CAR_FAIL = -1;// 失败

	// SharedPreference 相关信息
	public static String SHAREDPRE_USER_LOGIN_INFO = "USER_LOGIN_INFO";

	// 预约提前时间
	public static final int orderForwardHour = 2;

}
