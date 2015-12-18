package com.fyf.parkinglot.model;

/**
 * @category 汽车模型
 * @author fengyifei
 *
 */
public class CarInfoBean {

	private int car_id; // id
	private String car_licenseNum; // 车牌号
	private int user_id;// 用户id
	private String car_type; // 车辆类型
	private String car_img;// 车辆图片

	public int getCar_id() {
		return car_id;
	}

	public void setCar_id(int car_id) {
		this.car_id = car_id;
	}

	public String getCar_licenseNum() {
		return car_licenseNum;
	}

	public void setCar_licenseNum(String car_licenseNum) {
		this.car_licenseNum = car_licenseNum;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getCar_type() {
		return car_type;
	}

	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}

	public String getCar_img() {
		return car_img;
	}

	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}

	@Override
	public String toString() {
		return "CarInfoBean [car_id=" + car_id + ", car_licenseNum="
				+ car_licenseNum + ", user_id=" + user_id + ", car_type="
				+ car_type + ", car_img=" + car_img + "]";
	}

}
