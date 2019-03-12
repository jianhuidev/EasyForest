package com.example.kys_8.easyforest.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.ui.adapter.MyViewPagerAdapter;
import com.example.kys_8.easyforest.ui.fragment.ForgetPwdFragment;
import com.example.kys_8.easyforest.ui.fragment.LoginFragment;
import com.example.kys_8.easyforest.weight.CViewPager;

import java.util.ArrayList;
import java.util.List;

public class MDLoginActivity extends AppCompatActivity implements View.OnClickListener{
    private AppBarLayout appbar;
    private CardView card;
    private FrameLayout frame;
    public Button loginBtn,toRegisterBtn;
    public CViewPager cViewPager;
    private LoginFragment mLoginFragment;
    private ForgetPwdFragment mForgetPwdFragment;
    private final int TO_REGISTER = 1;
    public boolean isToRegister = false;
    public int currentPosition = 0;
    public ProgressBar progressBar;
    public TextView mHintTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdlogin);
        initView();
    }

    private void initView(){
        mHintTv = findViewById(R.id.hint_tv);
        appbar = findViewById(R.id.appbar_login);
        card = findViewById(R.id.card_login);
        frame = findViewById(R.id.frame_login);
        loginBtn = findViewById(R.id.btn_login);
        cViewPager = findViewById(R.id.cvp_login);
        progressBar = findViewById(R.id.progress_bar_login);
        List<Fragment> fragments = new ArrayList<>();
        mLoginFragment = new LoginFragment();
        mForgetPwdFragment = new ForgetPwdFragment();
        fragments.add(mLoginFragment);
        fragments.add(mForgetPwdFragment);
        MyViewPagerAdapter pageAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),fragments,null);
        cViewPager.setAdapter(pageAdapter);
        setViewPager();
        toRegisterBtn = findViewById(R.id.btn_to_register);
        toRegisterBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    private void setViewPager(){
        cViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {currentPosition = position;}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_to_register:
                if ("返回".equals(toRegisterBtn.getText())){
                    currentPosition -= 1;
                    cViewPager.setCurrentItem(currentPosition, true);
                    toRegisterBtn.setText(R.string.to_register);
                    loginBtn.setText("登录");
                    mHintTv.setText(R.string.title1_login);
                    return;
                }
                isToRegister = true;
                Intent intent1 = new Intent(MDLoginActivity.this,MDRegisterActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Pair<AppBarLayout, String> appbarPair = Pair.create(appbar, "appbar");
                    Pair<CardView, String> cardPair = Pair.create(card,"card" );
                    Pair<FrameLayout, String> framePair = Pair.create(frame,"frame" );
                    Pair<CViewPager, String> linePair = Pair.create(cViewPager,"cvp" );
                    Pair[] pairs = {appbarPair,cardPair,framePair,linePair};
                    ActivityOptionsCompat compat = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(MDLoginActivity.this,pairs);
                    startActivityForResult(intent1,TO_REGISTER,compat.toBundle());
                    mLoginFragment.forgotPwdBtn.setVisibility(View.INVISIBLE);
                } else {
                    startActivityForResult(intent1,1);
                }
                break;
            case R.id.btn_login:
                if ("登录".equals(loginBtn.getText())){
                    mLoginFragment.login();
                }else {
                    mForgetPwdFragment.modifyPwd();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                mLoginFragment.usernameEt.setText(data.getStringExtra("username"));
                mLoginFragment.passwordEt.setText(data.getStringExtra("password"));
            }
        }
    }

//    private void hintBtnCircular(){
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Animator animator = ViewAnimationUtils.createCircularReveal(hintBtn, // 动画
//                    hintBtn.getWidth()/4,
//                    0,
//                    0,
//                    hintBtn.getWidth());
//            animator.setDuration(400);
//            animator.start();
//            hintBtn.setVisibility(View.VISIBLE);
//        }else {
//            hintBtn.setVisibility(View.VISIBLE);
//        }
//    }

}
