package com.example.kys_8.easyforest.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by kys-8 on 2018/9/21.
 */

public class MapBean extends BmobObject{
    private String name;
    private String province;
    private Double longitude;//经度
    private Double latitude;//纬度
    private String mark;
    private String fbfw;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getFbfw() {
        return fbfw;
    }

    public void setFbfw(String fbfw) {
        this.fbfw = fbfw;
    }
}
