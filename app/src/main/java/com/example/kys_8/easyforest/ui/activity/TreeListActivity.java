package com.example.kys_8.easyforest.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.ui.adapter.TreeAdapter;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TreeListActivity extends TreeActivity {

    private final String TAG = "TreeActivity";
    private String ke = "";
    private int skip = 0;
    private TreeAdapter mAdapter;
    private boolean isOne = true;
    private boolean ishave = true;
    private boolean isRefresh = false;

    @Override
    protected void dealIntent() {
        ke = getIntent().getStringExtra("ke");
    }

    @Override
    protected String getTitleText() {
        return ke != null ? ke : "";
    }

    @Override
    protected IPresenter loadPresenter() {
        return null;
    }

    @Override
    protected void setRecyclerView() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                skip = 0;
                queryData();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TreeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(scrollListener);
        mRefreshLayout.setRefreshing(true);
        queryData();
    }

    private void queryData(){
        BmobQuery<TreeBean> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(skip);
        query.addWhereEqualTo("ke",ke);
        query.findObjects(new FindListener<TreeBean>() {
            @Override
            public void done(List<TreeBean> list, BmobException e) {
                mRefreshLayout.setRefreshing(false);
                if (e == null){
                    if (skip == 0){
                        skip = skip +10;
                        LogUtil.e(TAG,"测试" + list.size());
                        refreshHandle(list);
                    }else {
                        skip = skip +10;
                        queryHandle(list);
                    }
                }else {
                    LogUtil.e(TAG,"查询失败" + e.getMessage());
                }
            }
        });
    }

    private void refreshHandle(List<TreeBean> list){
        if (list == null){
            LogUtil.e(TAG, "list is null");
            return;
        }
        if (list.size() == 0){
            ToastUtil.showToast(this,"该科可能没有重点保护树木");
        }
        if (isRefresh){
            if (list.size() < 10){
                mAdapter.setItems(list);
            }else if (list.size() >= 10){
                mAdapter.setItems(list);
                mAdapter.addFooter();
            }
        }else {
            mAdapter.setItems(list);
            mAdapter.addFooter();
        }
    }

    private void queryHandle(List<TreeBean> list){
        if (list == null){
            LogUtil.e(TAG, "list is null");
            return;
        }
        if (list.size() == 0){
            ishave = false;
            mAdapter.removeFooter();
        }else if (list.size() <=10){
            mAdapter.removeFooter();
            isOne = true; // 刷新完重置
            mAdapter.addItems(list);
            mAdapter.addFooter();
        }
    }

    /**
     * 下拉刷新的处理
     */
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (isOne && linearLayoutManager.getItemCount() == (linearLayoutManager.findLastVisibleItemPosition() + 1)){
                if (ishave)
                    queryData();
                isOne = false;
//                Log.e(TAG,"1111111");
            }
        }
    };

}