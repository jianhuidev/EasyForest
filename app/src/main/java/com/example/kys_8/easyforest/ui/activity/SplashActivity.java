package com.example.kys_8.easyforest.ui.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.alibaba.fastjson.JSON;
import com.example.kys_8.easyforest.Config;
import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.PrefConstants;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.User;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.SAppUtil;
import com.example.kys_8.easyforest.utils.SpUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDeviceDensity();
        checkShowTutorial();
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
    }

    /**
     * 根据版本来决定是引导页还是欢迎界面
     */
    private void checkShowTutorial(){
        //没有数据，取出-1
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = SAppUtil.getAppVersionCode(this);

        if(currentVersionCode>oldVersionCode){
            startActivity(new Intent(SplashActivity.this,GuideActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this,"version_code", currentVersionCode);
            finish();
        }else {
            startWelcome();
        }

    }

    /**
     * 欢迎界面
     */
    public void startWelcome(){
        setContentView(R.layout.activity_splash);
        autoLogin();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        },800);

    }

    /**
     * 之前登录过的用户，将为其自动登录
     */
    private void autoLogin(){
        if (GlobalVariable.userInfo == null)
            return; // 没有登录过，则不进行自动登录
        User user = new User();
        user.setUsername(GlobalVariable.userInfo.getUsername());
        user.setPassword(GlobalVariable.curUserPd);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    LogUtil.e("SplashActivity","已自动登录");
                }else {
                    LogUtil.e("SplashActivity","请检查用户名或密码："+e.getMessage()+" "+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 获取当前设备的屏幕密度等基本参数
     */
    protected void getDeviceDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Config.EXACT_SCREEN_HEIGHT = metrics.heightPixels;
        Config.EXACT_SCREEN_WIDTH = metrics.widthPixels;
    }
}
