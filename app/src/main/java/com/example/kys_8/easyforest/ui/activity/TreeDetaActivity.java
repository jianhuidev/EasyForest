package com.example.kys_8.easyforest.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TreeDetaActivity extends AppCompatActivity {

    private TreeBean tree;
    private TextView typeTv,nameTv,nameLatinTv, shu,
            shu_latin,ke,ke_latin,xttz,fbfw,bhjb;
    private ImageView img;
    private String mark="";
    FloatingActionButton mFab;

    private String bhjbShare,typeShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_deta);

        initView();
        mark = getIntent().getStringExtra("mark");
        if (mark.equals("tree_bean")){
            tree = (TreeBean) getIntent().getSerializableExtra("tree");
            initData(tree);
        } else{
            query(mark);
        }
    }
    private void query(String name){
        BmobQuery<TreeBean> query = new BmobQuery<>();
        query.addWhereEqualTo("name",mark);
        query.findObjects(new FindListener<TreeBean>() {
            @Override
            public void done(List<TreeBean> list, BmobException e) {
                if (e == null){
                    if (list.size() == 1)
                        initData(list.get(0));
                }
            }
        });
    }

    private void initView(){
        mFab = findViewById(R.id.det_fab);
        shu = findViewById(R.id.shu_td);
        shu_latin = findViewById(R.id.shu_latin_td);
        ke = findViewById(R.id.ke_td);
        ke_latin = findViewById(R.id.ke_latin_td);
        xttz = findViewById(R.id.xttz_td);
        fbfw = findViewById(R.id.fbfw_td);
        bhjb = findViewById(R.id.bhjb_td);
        typeTv = findViewById(R.id.type_tree_deta);
        nameTv = findViewById(R.id.name_tree_deta);
        nameLatinTv = findViewById(R.id.name_latin_tree_deta);
        img = findViewById(R.id.img_td);

    }
    private void initData(final TreeBean data){
        LogUtil.e("TreeDetaActivity",data.getName());
        Toolbar toolbar = findViewById(R.id.toolbar_deta);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(data.getName());
        Glide.with(this).load(data.getImgUrl())
                .placeholder(R.mipmap.card_image_1)
                .error(R.mipmap.card_image_1) // 可能会换
                .crossFade()
                .into(img);
        nameTv.setText(data.getName());
        nameLatinTv.setText(data.getNameLatin());
        shu.setText(data.getShu());
        shu_latin.setText(data.getShuLatin());
        ke.setText(data.getKe());
        ke_latin.setText(data.getKeLatin());
        xttz.setText("\t\t\t\t"+data.getXttz());
        fbfw.setText("\t\t\t\t"+data.getFbfw());
        if (data.getType().equals("a")){ // 蕨类植物
            typeTv.setText("被子植物（Angiospermae）");
            typeShare = "被子植物（Angiospermae）";
        }else if (data.getType().equals("g")){
            typeTv.setText("裸子植物（Gymnospermae）");
            typeShare = "裸子植物（Gymnospermae）";
        }else if (data.getType().equals("p")){
            typeTv.setText("蕨类植物（Pteridophytes）");
            typeShare = "蕨类植物（Pteridophytes）";
        }
        if (data.getBhjb().trim().equals("Ⅰ")){
            bhjb.setText("国家一级保护植物");
            bhjbShare = "国家一级保护植物";
        }else if (data.getBhjb().trim().equals("Ⅱ")){
            bhjb.setText("国家二级保护植物");
            bhjbShare = "国家二级保护植物";
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText(TreeDetaActivity.this,data.getName()+" "+
                bhjbShare+" "+typeShare +" "+data.getImgUrl());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public void shareText(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "分享给他人吧"));
    }
}
