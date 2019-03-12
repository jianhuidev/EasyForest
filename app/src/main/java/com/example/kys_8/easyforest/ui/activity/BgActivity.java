package com.example.kys_8.easyforest.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.User;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by kys-8 on 2018/10/2.
 */

public class BgActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected int getContentView() {
        return R.layout.activity_bg;
    }

    @Override
    protected void initView() {
        CardView beijing_bg = (CardView)findViewById(R.id.beijing_bg);
        CardView shanghai_bg = (CardView)findViewById(R.id.shanghai_bg);
        CardView suzhou_bg = (CardView)findViewById(R.id.suzhou_bg);

        CardView design1 = (CardView)findViewById(R.id.design1);
        CardView design2 = (CardView)findViewById(R.id.design2);
        CardView design3 = (CardView)findViewById(R.id.design3);
        CardView design4 = (CardView)findViewById(R.id.design4);
        CardView design5 = (CardView)findViewById(R.id.design5);

        CardView scenery1 = (CardView)findViewById(R.id.scenery1);
        CardView scenery2 = (CardView)findViewById(R.id.scenery2);

        beijing_bg.setOnClickListener(this);
        shanghai_bg.setOnClickListener(this);
        suzhou_bg.setOnClickListener(this);

        design1.setOnClickListener(this);
        design2.setOnClickListener(this);
        design3.setOnClickListener(this);
        design4.setOnClickListener(this);
        design5.setOnClickListener(this);

        scenery1.setOnClickListener(this);
        scenery2.setOnClickListener(this);
    }

    @Override
    protected String getTitleText() {
        return "默认背景";
    }

    @Override
    protected IPresenter loadPresenter() {
        return null;
    }

    private void uploadByoBg(int bg){
        GlobalVariable.userInfo.setByoBg(bg);
        final ProgressDialog dialog = ProgressDialog.show(this, null, "正在提交。。。");
        GlobalVariable.userInfo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dialog.cancel();
                if (e == null){
                    ToastUtil.showToast(BgActivity.this,"成功更换");
                }else {
//                    ToastUtil.showToast(BgActivity.this,"更换失败，请检查网络");
                    LogUtil.e("BgActivity","更换失败"+e.getMessage()+" "+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.beijing_bg:
                uploadByoBg(0);
                break;
            case R.id.shanghai_bg:
                uploadByoBg(1);
                break;
            case R.id.suzhou_bg:
                uploadByoBg(2);
                break;
            case R.id.design1:
                uploadByoBg(3);
                break;
            case R.id.design2:
                uploadByoBg(4);
                break;
            case R.id.design3:
                uploadByoBg(5);
                break;
            case R.id.design4:
                uploadByoBg(6);
                break;
            case R.id.design5:
                uploadByoBg(7);
                break;
            case R.id.scenery1:
                uploadByoBg(8);
                break;
            case R.id.scenery2:
                uploadByoBg(9);
                break;
        }
    }
}
