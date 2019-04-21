package com.example.kys_8.easyforest.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.ui.activity.FindDetaActivity;
import com.example.kys_8.easyforest.ui.adapter.FindAdapter;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by kys-8 on 2018/9/15.
 */

public class FindFragment extends Fragment {

    private final String TAG = "FindFragment";
    private RecyclerView mRv;
    private FindAdapter adapter;
    private SwipeRefreshLayout mRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        LogUtil.e("Fragment","xxxxxxxxx");
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e("xxxFindFragment","onResume 会执行了？？？");
        if (GlobalVariable.isNeedRefresh){
            queryData();
            GlobalVariable.isNeedRefresh = false;
        }

    }

    private void initView(View view){
        mRefresh = view.findViewById(R.id.refresh_find);
        mRefresh.setColorSchemeResources(R.color.google_blue,
                R.color.google_yellow, R.color.google_red, R.color.google_green);
        LogUtil.e("xxxFindFragment","执行了？？？");
        mRv = view.findViewById(R.id.rv_find);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(linearLayoutManager);
        adapter = new FindAdapter(getContext(), new FindAdapter.ClickCallBack() {
            @Override
            public void onClick(UploadData data) {
                Intent intent = new Intent(getActivity(), FindDetaActivity.class);
                intent.putExtra("upload",data);
                getActivity().startActivity(intent);
            }
        });
        mRv.setAdapter(adapter);

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                LogUtil.e(TAG,FindFragment.this.toString());
                queryData();
            }
        });
        queryData();
    }

    /**
     * 查询他人分享的数据
     */
    public void queryData() {
        LogUtil.e(TAG,this.toString());
        BmobQuery<UploadData> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<UploadData>() {
            @Override
            public void done(List<UploadData> list, BmobException e) {
                if (mRefresh != null){
                    mRefresh.setRefreshing(false);
                }
                if (e == null){
                    LogUtil.e(TAG,"查询成功");
                    refreshHandle(list);
                }else {
                    LogUtil.e(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void refreshHandle(List<UploadData> list){
        adapter.setItems(list);
    }

}
