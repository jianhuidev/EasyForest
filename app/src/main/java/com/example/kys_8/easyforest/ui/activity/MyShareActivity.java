package com.example.kys_8.easyforest.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.ui.adapter.MyShareAdapter;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyShareActivity extends TreeActivity {

    private String title;
    private MyShareAdapter mAdapter;
    @Override
    protected void dealIntent() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
    }

    @Override
    protected String getTitleText() {
        return title;
    }

    @Override
    protected IPresenter loadPresenter() {
        return null;
    }

    @Override
    protected void setRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyShareAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryMyShare();
            }
        });
        queryMyShare();
    }

    /**
     * 我的分享查询
     */
    private void queryMyShare(){
        BmobQuery<UploadData> query = new BmobQuery<>();
        query.addWhereEqualTo("userObjId", GlobalVariable.userInfo == null ? "" : GlobalVariable.userInfo.getObjectId());
        query.order("-createdAt");
        query.findObjects(new FindListener<UploadData>() {
            @Override
            public void done(List<UploadData> list, BmobException e) {
                mRefreshLayout.setRefreshing(false);
                if (e == null){
                    mAdapter.setItems(list);
                }else {
                    LogUtil.e("MyShareActivity","查询失败" + e.getMessage()+" "+e.getErrorCode());
                }
            }
        });
    }

}
