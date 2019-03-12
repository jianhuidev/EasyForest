package com.example.kys_8.easyforest.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.KeBean;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.ui.adapter.KeAdapter;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class KeActivity extends TreeActivity {

    private KeAdapter mAdapter;
    private boolean isFirst = true;
    private String type;

    @Override
    protected void dealIntent() {
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected String getTitleText() {
        String title = "";
        switch (type){
            case "a":
                title = "被子植物";
                break;
            case "g":
                title = "裸子植物";
                break;
            case "p":
                title = "蕨类植物";
                break;
        }
        return title;
    }

    @Override
    protected IPresenter loadPresenter() {
        return null;
    }

    @Override
    protected void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new KeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData();
            }
        });
        mRefreshLayout.setRefreshing(true);
        queryData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 查询相应科
     */
    private void queryData(){
        BmobQuery<KeBean> query = new BmobQuery<>();
        query.addWhereEqualTo("type",type);
        query.findObjects(new FindListener<KeBean>() {
            @Override
            public void done(List<KeBean> list, BmobException e) {
                mRefreshLayout.setRefreshing(false);
                if (e == null){
                    mAdapter.setData(list);
                }else {
                    LogUtil.e("","查询失败"+e.getMessage()+" "+e.getErrorCode());
                }
            }
        });
    }

}