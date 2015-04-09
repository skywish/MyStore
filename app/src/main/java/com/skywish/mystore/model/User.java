package com.skywish.mystore.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by skywish on 2015/4/5.
 */
public class User extends BmobUser {

    private String nickName;
    private String phone;
    private String qq;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
