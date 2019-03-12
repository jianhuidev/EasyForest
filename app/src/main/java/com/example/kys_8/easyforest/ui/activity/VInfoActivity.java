package com.example.kys_8.easyforest.ui.activity;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.presenter.IPresenter;

public class VInfoActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_v_info;
    }

    @Override
    protected void initView() {}

    @Override
    protected String getTitleText() {
        return "版本信息";
    }

    @Override
    protected IPresenter loadPresenter() {
        return null;
    }
}
