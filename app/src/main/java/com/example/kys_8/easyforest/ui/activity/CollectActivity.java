package com.example.kys_8.easyforest.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.ui.adapter.CollectAdapter;
import com.example.kys_8.easyforest.ui.adapter.ItemTouchHelperCallback;
import com.example.kys_8.easyforest.ui.adapter.MyShareAdapter;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CollectActivity extends TreeActivity {

    private CollectAdapter mAdapter;

    @Override
    protected void dealIntent() {}

    @Override
    protected String getTitleText() {return "我的收藏";}

    @Override
    protected IPresenter loadPresenter() {return null;}

    @Override
    protected void setRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CollectAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryCollect();
            }
        });
        queryCollect();
    }

    private void queryCollect(){
        BmobQuery<UploadData> query = new BmobQuery<>();
        query.addWhereContainsAll("collectObjIds",
                Collections.singletonList(GlobalVariable.userInfo == null ? "" : GlobalVariable.userInfo.getObjectId()));
        query.findObjects(new FindListener<UploadData>() {
            @Override
            public void done(List<UploadData> list, BmobException e) {
                mRefreshLayout.setRefreshing(false);
                if (e == null){
                    mAdapter.setData(list);
                }else {
                    LogUtil.e("MyShareActivity","查询失败" + e.getMessage()+" "+e.getErrorCode());
                }
            }
        });
    }
}
