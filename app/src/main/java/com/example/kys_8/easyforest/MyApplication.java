package com.example.kys_8.easyforest;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;


import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.bean.User;
import com.example.kys_8.easyforest.plant.AuthService;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.SpUtil;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import cn.bmob.v3.Bmob;


/**
 * Created by kys-8 on 2018/4/11.
 */

public class MyApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        initBmob();

        initImageSwitcher();

        initVariable();

        preference();

        initToken();
    }
    /**
     * @return
     * 全局的上下文
     */
    public static Context getAppContext() {
        return appContext;
    }

    /**
     * 初始化Bmob
     */
    private void initBmob(){
        Bmob.initialize(this,"323c102f605da949cecb99fa415fd6d6"); // 初始化bmob 云
    }

    /**
     * 初始化图片选择器
     */
    private void initImageSwitcher(){
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }

    private void initVariable(){
        String userData = (String) SpUtil.get("userInfo","");
        LogUtil.e("MyApplication","输出：userData："+userData);
        if (!TextUtils.isEmpty(userData)){
            GlobalVariable.userInfo = JSON.parseObject(userData, User.class);
        }

        String pd = (String) SpUtil.get("pd","");
        if (!TextUtils.isEmpty(pd)){
            GlobalVariable.curUserPd = pd;
        }
    }
    private void preference(){
        GlobalVariable.isNight = (boolean) SpUtil.get("night_mode",false);
        if (GlobalVariable.isNight)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * 获取过直接取，没有才请求
     */
    private void initToken(){
        String accessToken = (String) SpUtil.get("accessToken","");
        if (!TextUtils.isEmpty(accessToken)){
            GlobalVariable.accessToken = accessToken;
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String token = AuthService.getAuth();
                    GlobalVariable.accessToken = token;
                    SpUtil.put("accessToken",token);
                    LogUtil.e("MyApplication",token);
                }
            }).start();
        }
    }

}
