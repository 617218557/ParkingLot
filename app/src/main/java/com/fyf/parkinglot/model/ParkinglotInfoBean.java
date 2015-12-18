package com.fyf.parkinglot.model;

/**
 * @category 停车场模型
 * @author fengyifei
 *
 */
public class ParkinglotInfoBean {

	private int park_id; // id
	private double park_fee; // 每小时费用
	private int park_isUse; // 车位是否被占用
	private String park_car; // 停靠车辆信息
	private String park_startTime; // 停靠开始时间

	public int getPark_id() {
		return park_id;
	}

	public void setPark_id(int park_id) {
		this.park_id = park_id;
	}

	public double getPark_fee() {
		return park_fee;
	}

	public void setPark_fee(double park_fee) {
		this.park_fee = park_fee;
	}

	public int getPark_isUse() {
		return park_isUse;
	}

	public void setPark_isUse(int park_isUse) {
		this.park_isUse = park_isUse;
	}

	public String getPark_car() {
		return park_car;
	}

	public void setPark_car(String park_car) {
		this.park_car = park_car;
	}

	public String getPark_startTime() {
		return park_startTime;
	}

	public void setPark_startTime(String park_startTime) {
		this.park_startTime = park_startTime;
	}

	@Override
	public String toString() {
		return "ParkinglotInfoBean [park_id=" + park_id + ", park_fee="
				+ park_fee + ", park_isUse=" + park_isUse + ", park_car="
				+ park_car + ", park_startTime=" + park_startTime + "]";
	}

}
