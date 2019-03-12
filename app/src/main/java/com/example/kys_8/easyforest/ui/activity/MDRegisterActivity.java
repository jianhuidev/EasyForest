package com.example.kys_8.easyforest.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.ui.adapter.MyViewPagerAdapter;
import com.example.kys_8.easyforest.ui.fragment.FirstRFragment;
import com.example.kys_8.easyforest.ui.fragment.SecondRFragment;
import com.example.kys_8.easyforest.ui.fragment.ThirdRFragment;
import com.example.kys_8.easyforest.utils.ToastUtil;
import com.example.kys_8.easyforest.weight.CViewPager;

import java.util.ArrayList;
import java.util.List;

public class MDRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout line;
    private CViewPager cViewPager;
    private TextView title;
    private FirstRFragment mFirstRFragment;
    private SecondRFragment mSecondRFragment;
    private ThirdRFragment mThirdRFragment;
    private Button nextBtn,toLoginBtn;
    public ProgressBar progressBar;
    private int currentPosition = 0;

//    private String mNickname,mMail;
    public String mUsername,mPwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdregister);
        initView();
    }

    private void initView(){
        title = findViewById(R.id.title_register);
        line = findViewById(R.id.line_register);
        nextBtn = findViewById(R.id.btn_next_register);
        cViewPager = findViewById(R.id.cvp_register);
        toLoginBtn = findViewById(R.id.btn_to_login);
        progressBar = findViewById(R.id.progress_bar_r);
        toLoginBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        List<Fragment> fragments = new ArrayList<>();
        mFirstRFragment = new FirstRFragment();
        mSecondRFragment = new SecondRFragment();
        mThirdRFragment = new ThirdRFragment();
        fragments.add(mFirstRFragment);
        fragments.add(mSecondRFragment);
        fragments.add(mThirdRFragment);
        MyViewPagerAdapter pageAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),fragments,null);
        cViewPager.setAdapter(pageAdapter);
        setViewPager();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_to_login:
                if ("返回".equals(toLoginBtn.getText())){
                    nextBtn.setText(R.string.next);
                    currentPosition -= 1;
                    cViewPager.setCurrentItem(currentPosition, true);
                    if (currentPosition == 0){
                        title.setText(R.string.title1_register);
                        toLoginBtn.setText(R.string.to_login);
                    }else if (currentPosition == 1){
                        mSecondRFragment.updateVerifyCode();
                        title.setText(R.string.title2_register);
                    }
                }else {onBackPressed();}
                break;
            case R.id.btn_next_register:
                if ("下一步".equals(nextBtn.getText())){
//                    currentPosition += 1;
//                    cViewPager.setCurrentItem(currentPosition, true);
                    if (currentPosition == 0){
                        if (!mFirstRFragment.isWrite()){
                            ToastUtil.showToast(this,"请完善信息");
                            return;
                        }
                        mUsername = mFirstRFragment.username;
                        mPwd = mFirstRFragment.pwd;
                        if (mPwd.length()<6 || mPwd.length()>18){
                            ToastUtil.showToast(this,"密码长度为6-18位");
                            return;
                        }
                        toLoginBtn.setText(R.string.back);
                        currentPosition += 1;
                        cViewPager.setCurrentItem(currentPosition, true);
                        title.setText(R.string.title2_register);
                    } else if (currentPosition == 1){
                        if (mSecondRFragment.isVerifyOk()){
//                            toLoginBtn.setText(R.string.back);
                            currentPosition += 1;
                            cViewPager.setCurrentItem(currentPosition, true);
                            title.setText(R.string.title3_register);
                            nextBtn.setText(R.string.finish);
                        }
                    }
                }else {
                    mThirdRFragment.register(mUsername,mPwd);
                }
                break;
        }
    }
//    private void viewPagerCircular(){
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Animator animator = ViewAnimationUtils.createCircularReveal(cViewPager, // 动画
//                    cViewPager.getWidth()/4,
//                    0,
//                    0,
//                    cViewPager.getWidth());
//            animator.setDuration(600);
//            animator.start();
//        }
//    }
}
