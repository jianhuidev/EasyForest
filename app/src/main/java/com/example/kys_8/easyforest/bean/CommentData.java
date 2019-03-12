package com.example.kys_8.easyforest.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by kys-8 on 2018/9/18.
 */

public class CommentData extends BmobObject {
    private String username;
    private String content;

    /**
     * 用于删除评论的核对，因为只能删除自己的评论
     */
    private String userId;
    private String userAvatarUrl;
    /**
     * 用来查询是那条分享的评论
     */
    private String uploadObjId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUploadObjId() {
        return uploadObjId;
    }

    public void setUploadObjId(String uploadObjId) {
        this.uploadObjId = uploadObjId;
    }
}
