package com.example.kys_8.easyforest.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by kys-8 on 2018/11/23.
 */

public class Opinion extends BmobObject {

    private String userObjId;
    private String username;
    private String email;
    private String content;

    public String getUserObjId() {
        return userObjId;
    }

    public void setUserObjId(String userObjId) {
        this.userObjId = userObjId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
