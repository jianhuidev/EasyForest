package com.example.kys_8.easyforest.plant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.IdentifyHistory;
import com.example.kys_8.easyforest.presenter.IdentifyPresenter;
import com.example.kys_8.easyforest.ui.activity.BaseActivity;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.SearchDialog;
import com.example.kys_8.easyforest.weight.MaterialSearchView;
import com.example.kys_8.easyforest.weight.onSearchListener;
import com.example.kys_8.easyforest.weight.onSimpleSearchActionsListener;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.text.DecimalFormat;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by kys-8 on 2018/11/1.
 */

public class IdentifyActivity extends AppCompatActivity {

    private RecyclerView mRv;
    private IdentifyAdapter mAdapter;
    private String filePath;
    private IdentifyPresenter mPresenter;
    private boolean isFirst = true;
    private LinearLayout mProgress;
    private DecimalFormat mDf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);
        Toolbar toolbar = findViewById(R.id.toolbar_identify);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("拍照识树");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mPresenter = new IdentifyPresenter();
        initView();
        if (GlobalVariable.type == 0) camera();
        else album();
    }

    private void initView(){
        mDf = new DecimalFormat("0.00");
        mProgress = findViewById(R.id.progress_layout_identify);
        mProgress.setVisibility(View.GONE);
        mRv = findViewById(R.id.rv_identify);
        mRv.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRv.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.identify, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.camera:
                select();
                break;
        }
        return true;
    }

    private void select(){
        new AlertDialog.Builder(this).setMessage("您可以拍照或从相册选择树木图片")
                .setPositiveButton("相册选择", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        album();
                    }
                })
                .setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        camera();
                    }
                })
                .setNeutralButton("不了", null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            if (requestCode == 1){
                filePath = data.getStringExtra("result");
            }else {
                List<String> pathList = data.getStringArrayListExtra("result");
                LogUtil.e("IdentifyActivity",pathList.size()+"");
                for (String path : pathList) {
                    filePath = path;
                }
            }
            handleImg();
        }else {
            if (isFirst) onBackPressed();
            else select();
        }
    }

    public void camera() {
        ISCameraConfig config = new ISCameraConfig.Builder()
                .needCrop(true)
                .cropSize(1, 1, 200, 200)
                .build();
        ISNav.getInstance().toCameraActivity(this, config, 1);
    }

    private void album(){
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选
                .multiSelect(false)
                .btnText("Confirm")
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(getResources().getColor(R.color.colorPrimary))
                .backResId(R.mipmap.back)
                .title("请选择一个图片")
                .titleColor(Color.WHITE)
                .titleBgColor(getResources().getColor(R.color.colorPrimary))
                .allImagesText("所有图片")
                .needCrop(true)
//                .cropSize(1, 1, 200, 200)
                // 第一个是否显示相机
                .needCamera(false)
                // 最大选择图片数量
                .maxNum(9)
                .build();
        ISNav.getInstance().toListActivity(this, config, 2);
    }

    private void handleImg(){
        mRv.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
        mPresenter.identifyTree(filePath, new IdentifyView() {
            @Override
            public void plantResult(IdentifyResult result) {
                final List<IdentifyResult.ResultBean> resultList = result.getResult();
//                JSON.toJSONString(user)
                saveHistory(result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRv.setVisibility(View.VISIBLE);
                        mProgress.setVisibility(View.GONE);
                        mAdapter = new IdentifyAdapter(IdentifyActivity.this,resultList);
                        mRv.setAdapter(mAdapter);
                        isFirst = false;
                    }
                });
            }

            @Override
            public void reGetToken() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.setVisibility(View.GONE);
                        tokenDialog();
                    }
                });
            }
        });
    }

    private void tokenDialog(){
        new AlertDialog.Builder(IdentifyActivity.this).setMessage("您的识别令牌由于过期，以为您重新获取令牌，是否重新进行树木识别？")
                .setCancelable(false)
                .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select();
                    }
                }).setNegativeButton("不了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        }).show();
    }

    private void saveHistory(IdentifyResult result){
        if (GlobalVariable.userInfo == null)
            return;
        IdentifyHistory history = new IdentifyHistory();
        history.setUserObjId(GlobalVariable.userInfo.getObjectId());
        history.setResult(JSON.toJSONString(result));
        if (result.getResult() != null && result.getResult().size() > 0){
            history.setMajorTree(result.getResult().get(0).getName());
            String con = mDf.format(result.getResult().get(0).getScore()*100)+"%";
            history.setMajorCon(con);
            history.setMainImg(result.getResult().get(0).getBaike_info().getImage_url());
        } else {
            history.setMajorTree("非树木");
            history.setMajorCon("无");
            history.setMainImg("0");
        }

        history.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    LogUtil.e("IdentifyActivity","更新成功");
                }else {
                    LogUtil.e("IdentifyActivity","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

}
