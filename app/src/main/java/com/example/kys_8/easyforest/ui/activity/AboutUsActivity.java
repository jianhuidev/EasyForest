package com.example.kys_8.easyforest.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.presenter.IPresenter;

public class AboutUsActivity extends BaseActivity {

    private TextView explain;

    @Override
    protected int getContentView() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initView() {
        explain = (TextView)findViewById(R.id.explain_about_us);

        explain.setText("\t\t\t\t"+"重点林木查询平台是一款针对国家重点保护林木的APP，方便林木工作者，科研人员，以及社会上保护重点林木的爱心人士，对林木感兴趣的人员，查询林木信息，" +
                "浏览，分析，以及发表保护林木意见的应用。本软件主要功能有林木分类，智能搜索，林木位置分布，陌生林木智能识别。用户可快速的对目标林木进行搜索查询，" +
                "查看重点保护林木在全国的分布，以及对陌生林木进行拍照上传，进行智能识别，即可返回该林木的详细信息，方便用户进行了解林木相关信息。" +
                "本软件主要在于为用户提供便捷移动智能终端的“植物保护名录”，让用户通过手机即可进行查询与浏览，提高人们对重要植物资源的鉴别能力，提高大家的保护意识。");
    }

    @Override
    protected String getTitleText() {
        return "关于我们";
    }

    @Override
    protected IPresenter loadPresenter() {
        return null;
    }
}
