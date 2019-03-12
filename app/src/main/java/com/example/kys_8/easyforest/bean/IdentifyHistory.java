package com.example.kys_8.easyforest.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by kys-8 on 2018/11/6.
 */

public class IdentifyHistory extends BmobObject {

    private String userObjId;
    private String result;
    private String majorTree;
    private String majorCon;
    private String mainImg;

    public String getUserObjId() {
        return userObjId;
    }

    public void setUserObjId(String userObjId) {
        this.userObjId = userObjId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMajorTree() {
        return majorTree;
    }

    public void setMajorTree(String majorTree) {
        this.majorTree = majorTree;
    }

    public String getMajorCon() {
        return majorCon;
    }

    public void setMajorCon(String majorCon) {
        this.majorCon = majorCon;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }
}
