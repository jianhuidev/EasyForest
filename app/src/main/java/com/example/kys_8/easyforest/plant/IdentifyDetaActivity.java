package com.example.kys_8.easyforest.plant;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.User;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.ui.activity.BaseActivity;

public class IdentifyDetaActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_identify_deta;
    }

    @Override
    protected void initView() {
        IdentifyResult result = JSON.parseObject(getIntent().getStringExtra("identify"), IdentifyResult.class);
        RecyclerView mRv = findViewById(R.id.rv_identify);
        mRv.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRv.setLayoutManager(layoutManager);
        IdentifyAdapter adapter = new IdentifyAdapter(this,result.getResult());
        mRv.setAdapter(adapter);

    }

    @Override
    protected String getTitleText() {
        return getIntent().getStringExtra("title");
    }

    @Override
    protected IPresenter loadPresenter() {
        return null;
    }
}
