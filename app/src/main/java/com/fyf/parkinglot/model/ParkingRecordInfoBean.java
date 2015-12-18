package com.fyf.parkinglot.model;

/**
 * @author fengyifei
 * @category 停车记录模型
 */
public class ParkingRecordInfoBean {

    private int record_id; // 记录id
    private int car_id; // 车辆id
    private String record_startTime;// 开始时间
    private String record_endTime;// 结束时间
    private double record_fee;// 总费用

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getRecord_startTime() {
        return record_startTime;
    }

    public void setRecord_startTime(String record_startTime) {
        this.record_startTime = record_startTime;
    }

    public String getRecord_endTime() {
        return record_endTime;
    }

    public void setRecord_endTime(String record_endTime) {
        this.record_endTime = record_endTime;
    }

    public double getRecord_fee() {
        return record_fee;
    }

    public void setRecord_fee(double record_fee) {
        this.record_fee = record_fee;
    }

    @Override
    public String toString() {
        return "ParkingRecordInfoBean [record_id=" + record_id + ", car_id="
                + car_id + ", record_startTime=" + record_startTime
                + ", record_endTime=" + record_endTime + ", record_fee="
                + record_fee + "]";
    }

}
