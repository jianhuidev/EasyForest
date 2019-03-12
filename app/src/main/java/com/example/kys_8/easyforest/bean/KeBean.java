package com.example.kys_8.easyforest.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by kys-8 on 2018/9/13.
 */

public class KeBean extends BmobObject {

    private String name;
    private String imgUrl;
    /**
     * 被子植物 a
     * 裸子植物 g
     * 蕨类植物 p
     */
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
