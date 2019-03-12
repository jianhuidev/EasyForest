package com.example.kys_8.easyforest.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by kys-8 on 2018/9/14.
 */

public class TreeBean extends BmobObject {

    private String name;
    private String nameLatin;
    private String shu;
    private String shuLatin;
    private String ke;
    private String keLatin;
    /**
     * 形态特征
     */
    private String xttz;
    /**
     * 分布范围
     */
    private String fbfw;
    /**
     * 保护级别
     */
    private String bhjb;
    /**
     * 被子植物 a
     * 裸子植物 g
     * 蕨类植物 p
     */
    private String type;
    private String imgUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLatin() {
        return nameLatin;
    }

    public void setNameLatin(String nameLatin) {
        this.nameLatin = nameLatin;
    }

    public String getShu() {
        return shu;
    }

    public void setShu(String shu) {
        this.shu = shu;
    }

    public String getShuLatin() {
        return shuLatin;
    }

    public void setShuLatin(String shuLatin) {
        this.shuLatin = shuLatin;
    }

    public String getKe() {
        return ke;
    }

    public void setKe(String ke) {
        this.ke = ke;
    }

    public String getKeLatin() {
        return keLatin;
    }

    public void setKeLatin(String keLatin) {
        this.keLatin = keLatin;
    }

    public String getXttz() {
        return xttz;
    }

    public void setXttz(String xttz) {
        this.xttz = xttz;
    }

    public String getFbfw() {
        return fbfw;
    }

    public void setFbfw(String fbfw) {
        this.fbfw = fbfw;
    }

    public String getBhjb() {
        return bhjb;
    }

    public void setBhjb(String bhjb) {
        this.bhjb = bhjb;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
