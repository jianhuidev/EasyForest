package com.example.kys_8.easyforest.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by kys-8 on 2018/9/9.
 */

public class UploadData extends BmobObject{

    private String content;
    private List<BmobFile> imgs;
    private String userNmae;
    private String userAvatarUrl;
    private String userObjId;

    /**
     * 赞
     */
    private List<String> likeObjIds;
    /**
     * 不喜欢
     */
    private List<String> dislikeObjIds;
    /**
     * 有多少人收藏
     */
    private List<String> collectObjIds;


    /**
     * 匿名1 ，不匿名0
     */
    private String isAnon;

    private Integer commentCount;

    /**
     * 分享数量
     */
    private Integer shares;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<BmobFile> getImgs() {
        return imgs;
    }

    public void setImgs(List<BmobFile> imgs) {
        this.imgs = imgs;
    }

    public String getUserNmae() {
        return userNmae;
    }

    public void setUserNmae(String userNmae) {
        this.userNmae = userNmae;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserObjId() {
        return userObjId;
    }

    public void setUserObjId(String userObjId) {
        this.userObjId = userObjId;
    }

    public List<String> getLikeObjIds() {
        return likeObjIds;
    }

    public void setLikeObjIds(List<String> likeObjIds) {
        this.likeObjIds = likeObjIds;
    }

    public List<String> getDislikeObjIds() {
        return dislikeObjIds;
    }

    public void setDislikeObjIds(List<String> dislikeObjIds) {
        this.dislikeObjIds = dislikeObjIds;
    }

    public List<String> getCollectObjIds() {
        return collectObjIds;
    }

    public void setCollectObjIds(List<String> collectObjIds) {
        this.collectObjIds = collectObjIds;
    }

    public String getIsAnon() {
        return isAnon;
    }

    public void setIsAnon(String isAnon) {
        this.isAnon = isAnon;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }
}
