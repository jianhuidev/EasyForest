package com.example.kys_8.easyforest.ui.activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.plant.IdentifyActivity;
import com.example.kys_8.easyforest.plant.IdentifyHistoryActivity;
import com.example.kys_8.easyforest.ui.adapter.MyViewPagerAdapter;
import com.example.kys_8.easyforest.ui.fragment.FindFragment;
import com.example.kys_8.easyforest.ui.fragment.SortFragment;
import com.example.kys_8.easyforest.utils.SpUtil;
import com.example.kys_8.easyforest.weight.Fab;
import com.example.kys_8.easyforest.weight.MaterialSearchView;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener{

    public final String TAG = "MainActivity";
    private DrawerLayout mDrawer;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView mAvatar,mImgBg;

    private FloatingActionButton fab;
    private TextView nicknameTv,mailTv;

    private Toolbar mToolbar;
    private MaterialSheetFab materialSheetFab;
    private AppBarLayout mAppbar;
    private FindFragment mFindFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTitleView();
        initView();
        setupFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserInfo();
    }

    private void initTitleView() {
        mAppbar = findViewById(R.id.appbar_main);
        mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("重点林木查询平台");
        mDrawer = findViewById(R.id.drawer_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawer,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        FrameLayout nav_header = headerView.findViewById(R.id.nav_layout_header);
        nav_header.setOnClickListener(this);
        mAvatar = (ImageView)nav_header.findViewById(R.id.avatar_nav_main);
        mImgBg = (ImageView)nav_header.findViewById(R.id.img_bg_nav_header);
        nicknameTv = (TextView)nav_header.findViewById(R.id.nickname_nav_header);
        mailTv = (TextView)nav_header.findViewById(R.id.mail_nav_header);
        fab = findViewById(R.id.fab_main);
    }

    private void initView() {
        mTabLayout = findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.view_pager_main);
        List<String> titles = new ArrayList<>();
        titles.add("重点林木");
        titles.add("发现");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SortFragment());
        mFindFragment = new FindFragment();
        fragments.add(mFindFragment);
        mViewPager.setOffscreenPageLimit(1);
        MyViewPagerAdapter pageAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(pageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * 设置昵称，邮箱，背景
     */
    private void setUserInfo(){
        if (GlobalVariable.userInfo != null){
            if (GlobalVariable.userInfo.getAvatar() != null){
                Glide.with(this).load(GlobalVariable.userInfo.getAvatar().getUrl())
                        .error(R.mipmap.avatar_off)
                        .crossFade().into(mAvatar);
            }
            if (TextUtils.isEmpty(GlobalVariable.userInfo.getNickName()))
                nicknameTv.setText("用户"+GlobalVariable.userInfo.getUsername());
            else
                nicknameTv.setText(GlobalVariable.userInfo.getNickName());

            if (TextUtils.isEmpty(GlobalVariable.userInfo.getEmail()))
                mailTv.setText("还未填写邮箱哦！");// mailTv.setText("还未填写邮箱，填写邮箱可以方便找回密码哦！");
            else
                mailTv.setText(GlobalVariable.userInfo.getEmail());

            // -1 表示上传的
            if (GlobalVariable.userInfo.getByoBg() == -1 && GlobalVariable.userInfo.getUserBackground() != null){
                Glide.with(this).load(GlobalVariable.userInfo.getUserBackground().getUrl())
                        .error(R.mipmap.card_image_1)
                        .crossFade().into(mImgBg);
            }else if (GlobalVariable.userInfo.getByoBg() != null){
                if (GlobalVariable.userInfo.getByoBg() == 7){
                    Glide.with(this).load(R.mipmap.card_img_2)
                            .error(R.mipmap.card_image_1)
                            .crossFade().into(mImgBg);
                }else {
                    Glide.with(this).load(GlobalVariable.drawArr[GlobalVariable.userInfo.getByoBg()])
                            .error(R.mipmap.card_image_1)
                            .crossFade().into(mImgBg);
                }
            }
        }else {
            mAvatar.setImageResource(R.mipmap.avatar_off);
            mImgBg.setImageResource(R.mipmap.card_img_2);
            nicknameTv.setText("未登录");
            mailTv.setText("暂无");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(this, SearchActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                }else {
                    startActivity(new Intent(this, SearchActivity.class));
                }
                break;
            case R.id.upload:
                if (GlobalVariable.userInfo == null){
                    toLogin("登录后将享有上传分享的权限，是否跳转到登录界面 ?");
                    break;
                }
                startActivity(new Intent(this,UploadActivity.class));
                break;
            case R.id.identify:
                new AlertDialog.Builder(this).setMessage("您可以拍照或从相册选择树木图片")
                        .setPositiveButton("相册选择", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GlobalVariable.type = 1;
                                startActivity(new Intent(MainActivity.this,IdentifyActivity.class));
                            }
                        })
                        .setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GlobalVariable.type = 0;
                                startActivity(new Intent(MainActivity.this,IdentifyActivity.class));
                            }
                        })
                        .setNeutralButton("不了",null).show();
                break;
            case R.id.about:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.d_wallpaper:
                startActivity(new Intent(MainActivity.this,BgActivity.class));
                break;
            case R.id.my_share:
                if (GlobalVariable.userInfo == null){
                    toLogin("您还未登录,没有分享，是否跳转到登录界面 ?");
                    break;
                }
                intent.setClass(this,MyShareActivity.class);
                intent.putExtra("title","我的分享");
                startActivity(intent);
                break;
            case R.id.collection:
                if (GlobalVariable.userInfo == null){
                    toLogin("您还未登录,没有收藏，是否跳转到登录界面 ?");
                    break;
                }
                intent.setClass(this,CollectActivity.class);
                intent.putExtra("title","我的收藏");
                startActivity(intent);
                break;
            case R.id.identify_tree:
                new AlertDialog.Builder(this).setMessage("您可以拍照或从相册选择树木图片")
                        .setPositiveButton("相册选择", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GlobalVariable.type = 1;
                                startActivity(new Intent(MainActivity.this,IdentifyActivity.class));
                            }
                        })
                        .setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GlobalVariable.type = 0;
                                startActivity(new Intent(MainActivity.this,IdentifyActivity.class));
                            }
                        })
                        .setNeutralButton("不了",null).show();
                break;
            case R.id.map:
                startActivity(new Intent(this,MapActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
            case R.id.opinion:
                if (GlobalVariable.userInfo == null){
                    toLogin("您还未登录，是否跳转到登录界面 ?");
                    break;
                }
                startActivity(new Intent(this,OpinionActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this,SettingsActivity.class));
                break;
            case R.id.day_night:
                mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {}

                    @Override
                    public void onDrawerOpened(View drawerView) {}

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        if (GlobalVariable.isNight){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            GlobalVariable.isNight = false;
                        } else{
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            GlobalVariable.isNight = true;
                        }
                        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                        recreate();

                        SpUtil.put("night_mode",GlobalVariable.isNight);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {}
                });
                mDrawer.closeDrawers();
                break;
            case R.id.identify_history:
                if (GlobalVariable.userInfo == null){
                    toLogin("您还未登录，所以没有智能识别历史，是否跳转到登录界面 ?");
                    break;
                }
                startActivity(new Intent(this,IdentifyHistoryActivity.class));
                break;
            case R.id.version:
                startActivity(new Intent(this,VInfoActivity.class));
                break;
        }
        return true;
    }

    private void toLogin(String str){
        new AlertDialog.Builder(this).setMessage(str)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this,MDLoginActivity.class));
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_sheet_collect:
                Intent intent = new Intent();
                if (GlobalVariable.userInfo == null){
                    toLogin("您还未登录,没有收藏，是否跳转到登录界面 ?");
                    break;
                }
                intent.setClass(this,CollectActivity.class);
                intent.putExtra("title","我的收藏");
                startActivity(intent);
                break;
            case R.id.fab_sheet_map:
                startActivity(new Intent(this,MapActivity.class));
                break;
            case R.id.fab_sheet_identify:
                new AlertDialog.Builder(this).setMessage("您可以拍照或从相册选择树木图片")
                        .setPositiveButton("相册选择", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GlobalVariable.type = 1;
                                startActivity(new Intent(MainActivity.this,IdentifyActivity.class));
                            }
                        })
                        .setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GlobalVariable.type = 0;
                                startActivity(new Intent(MainActivity.this,IdentifyActivity.class));
                            }
                        })
                        .setNeutralButton("不了",null).show();
                break;
            case R.id.fab_sheet_item_note:
                if (GlobalVariable.userInfo == null){
                    toLogin("登录后将享有上传分享的权限，是否跳转到登录界面 ?");
                    break;
                }
                startActivity(new Intent(MainActivity.this,UploadActivity.class));
                break;

            case R.id.nav_layout_header:
                if (GlobalVariable.userInfo == null){
                    startActivity(new Intent(MainActivity.this,MDLoginActivity.class));
                }else {
                    startActivity(new Intent(this,MDUserInfoActivity.class));
                }
                break;
        }
    }

    private void setupFab(){
        Fab fab = (Fab) findViewById(R.id.fab_main);
        View sheetView = findViewById(R.id.fab_sheet_main);
        View overlay = findViewById(R.id.overlay_main);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.colorAccent);
        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);
//        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
//            @Override
//            public void onShowSheet() {
//                super.onShowSheet();
//            }
//
//            @Override
//            public void onHideSheet() {
//                super.onHideSheet();
//            }
//        });
        // Set material sheet item click listeners
        findViewById(R.id.fab_sheet_collect).setOnClickListener(this);
        findViewById(R.id.fab_sheet_map).setOnClickListener(this);
        findViewById(R.id.fab_sheet_identify).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_note).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            Snackbar.make(fab,"退出程序吗？",Snackbar.LENGTH_SHORT)
                    .setAction("是的", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SpUtil.put("userInfo", JSON.toJSONString(GlobalVariable.userInfo));
                            MainActivity.super.onBackPressed();
                        }
                    }).show();
//            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
