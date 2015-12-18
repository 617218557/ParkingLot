package com.fyf.parkinglot.model;

/**
 * @author fengyifei
 * @category 用户信息
 */
public class UserInfoBean {

    private int user_id; // id
    private String user_name;// 姓名
    private String user_phoneNum;// 电话
    private String user_password;// 密码
    private int user_age;// 年龄
    private int user_gender;// 性别(0女 1男)
    private String user_img;// 用户头像地址

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phoneNum() {
        return user_phoneNum;
    }

    public void setUser_phoneNum(String user_phoneNum) {
        this.user_phoneNum = user_phoneNum;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    @Override
    public String toString() {
        return "UserInfoBean [user_id=" + user_id + ", user_name=" + user_name
                + ", user_phoneNum=" + user_phoneNum + ", user_password="
                + user_password + ", user_age=" + user_age + ", user_gender="
                + user_gender + ", user_img=" + user_img + "]";
    }

}
