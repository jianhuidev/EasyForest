package com.example.kys_8.easyforest.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.presenter.IPresenter;

public class HelpActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_help;
    }

    @Override
    protected void initView() {
        TextView smfl = findViewById(R.id.smfl);
        smfl.setText("\t\t\t\t"+"将国家重点保护林木分为三种，分别是被子植物、裸子植物、蕨类植物，每个分类里面又细分为不同的科，每个科" +
                "里面包含该科的树种。用户可以通过选择林木所在的类别，查找到相应的植物。");
        TextView wzfb = findViewById(R.id.wzfb);
        wzfb.setText("\t\t\t\t"+"我们将国家重点林木在地图上精确的进行位置标注，并且进行标注使用的是该树木的圆形图片，清晰明了的展示了重点林木" +
                "的位置分布。点击标注可查看国家重点林木的详细信息，我们通过百度SDK进行定位显示。");
        TextView sscx = findViewById(R.id.sscx);
        sscx.setText("\t\t\t\t"+" 我们提供三种查询方式其中包含搜索历史，推荐搜索，模糊查询功能。" +
                "\n模糊查询：在主界面的输入框中当你输入一个松字我们会提示用户所有和松有关的树木。" +
                "\n推荐查询：用户在进入搜索界面的时候 我们会随机产生15种树木。\n搜索历史：我们提供搜索历史功能保留用户搜索过的树木");
        TextView scfx = findViewById(R.id.scfx);
        scfx.setText("\t\t\t\t"+"用户可以对发现的重点保护林木，进行拍照上传分享，在配上想发表的话即感想，来将自己保护林木的意识传达给其他用户。" +
                "每条上传分享将在主界面的发现模块展示。点击进入分享详细信息界面。用户可以进行点赞、评论和收藏。");
        TextView znss = findViewById(R.id.znss);
        znss.setText("\t\t\t\t"+"用户可以在主界面点击悬浮按钮出现拍照识树功能，然后用户可以通过拍照和相册两种方式来进行树木识别，" +
                "通过图片识别会返回和这个图片当中相似的树木，每个卡片包括一种可能性，每一种都有一个相似度显示。");
        TextView dn = findViewById(R.id.dn);
        dn.setText("\t\t\t\t"+"在主界面的侧滑界面我们支持日夜间转换模式，用户点击切换模式。");

    }

    @Override
    protected String getTitleText() {
        return "帮助（功能介绍）";
    }

    @Override
    protected IPresenter loadPresenter() {return null;}
}
