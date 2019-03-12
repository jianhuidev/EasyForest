package com.example.kys_8.easyforest.presenter;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.plant.AuthService;
import com.example.kys_8.easyforest.plant.IdentifyResult;
import com.example.kys_8.easyforest.plant.IdentifyView;
import com.example.kys_8.easyforest.plant.Plant;
import com.example.kys_8.easyforest.ui.IView;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.SpUtil;

/**
 * Created by kys-8 on 2018/11/1.
 */

public class IdentifyPresenter implements IPresenter {

//    public IdentifyPresenter() {
//    }

//    String accessToken1 = "24.a8c8b8f38b5090cad8e9f87489899604.2592000.1542790370.282335-10217811"; // 11787811
    public void identifyTree(final String filePath, final IdentifyView view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = Plant.plant(filePath, GlobalVariable.accessToken);
//                view.plantResult(result);
                IdentifyResult identifyResult = JSON.parseObject(result,IdentifyResult.class);
                if (identifyResult.getLog_id() == 0){
                    // 重新获取token
                    String token = AuthService.getAuth();
                    GlobalVariable.accessToken = token;
                    SpUtil.put("accessToken",token);
                    LogUtil.e("IdentifyPresenter","重新获取token"+token);
                    view.reGetToken();
                }else {
                    view.plantResult(identifyResult);
                }
            }
        }).start();
    }
}
