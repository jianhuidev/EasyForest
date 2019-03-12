package com.example.kys_8.easyforest.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by kys-8 on 2018/4/19.
 */

public class User extends BmobUser {

    /**
     * 已有字段
     * 用户名，密码，邮箱，手机号
     */

    private BmobFile avatar;
    private BmobFile userBackground;
    /**
     * 默认壁纸
     */
    private String userBg;
    private String nickName;
    private String gender;

    private String birthday;

    private String qq;
    private String phone;

    private Integer leaves;


    private String prof;

    /**
     * 个性签名
     */
    private String intro;

    private String inAWord;

    private Integer byoBg;

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public BmobFile getUserBackground() {
        return userBackground;
    }

    public void setUserBackground(BmobFile userBackground) {
        this.userBackground = userBackground;
    }

    public String getUserBg() {
        return userBg;
    }

    public void setUserBg(String userBg) {
        this.userBg = userBg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLeaves() {
        return leaves;
    }

    public void setLeaves(Integer leaves) {
        this.leaves = leaves;
    }

    public String getInAWord() {
        return inAWord;
    }

    public void setInAWord(String inAWord) {
        this.inAWord = inAWord;
    }

    public Integer getByoBg() {
        return byoBg;
    }

    public void setByoBg(Integer byoBg) {
        this.byoBg = byoBg;
    }
}
