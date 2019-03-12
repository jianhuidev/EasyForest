package com.example.kys_8.easyforest.ui.activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.ui.adapter.GuidePagerAdapter;

public class GuideActivity extends AppCompatActivity {

    private Button mButtonFinish;
    private ImageButton mButtonNext,mButtonPre;
    private ViewPager mViewPager;
    private ImageView[] indicators;
    private int bgColors[];
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initView();

        initData();
        setViewPager();
        setBtnListener();
    }

    private void initView(){
        GuidePagerAdapter pagerAdapter = new GuidePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.view_pager_guide);
        mViewPager.setAdapter(pagerAdapter);
        mButtonFinish = findViewById(R.id.button_finish);
        mButtonFinish.setText("完成");
        mButtonNext = findViewById(R.id.image_button_next);
        mButtonPre = findViewById(R.id.img_btn_pre);
        indicators = new ImageView[] {
                findViewById(R.id.img_indicator0),
                findViewById(R.id.img_indicator1),
                findViewById(R.id.img_indicator2) };
    }

    private void setViewPager(){
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int colorUpdate = (Integer) new ArgbEvaluator().evaluate(positionOffset, bgColors[position], bgColors[position == 2 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                updateIndicators(position);
                mViewPager.setBackgroundColor(bgColors[position]);
                mButtonPre.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                mButtonNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                mButtonFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                View section_label = page.findViewById(R.id.section_label);
                View section_intro = page.findViewById(R.id.section_intro);
                View section_img = page.findViewById(R.id.section_img);

                handleView(section_label,0,position);
                handleView(section_intro,1,position);
                handleView(section_img,2,position);
            }
        });
    }

    /**
     * @param view
     * @param i     可以控制速度，0 为同步
     * @param position
     */
    private void handleView(View view,int i,float position){

        float weight;
        if (position > 0){ // 进入
            weight = 0.3f * i;  // 速度0.3
        }else {  // 出去
            weight = 0.3f * Math.abs(4 - i);
        }
        trans(view, position, weight);
    }
    private void trans(View view, float position, float offset){
        float value = 0;
        if (view != null){
            float width = view.getWidth();
            if (position != 0){
                value = (position * width * offset);
                /*设置item在X方向上位移*/
                view.setTranslationX(value);  // 向有比正常的要偏offset
//                view.setOffset(position * offset);
            }else {
                view.setTranslationX(0);
//                viewTestItem.setOffset(0);
            }
        }
    }

    private void setBtnListener(){
        mButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition += 1;
                mViewPager.setCurrentItem(currentPosition, true);
            }
        });

        mButtonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition -= 1;
                mViewPager.setCurrentItem(currentPosition, true);
            }
        });
    }

    private void initData() {
        bgColors = new int[]{ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.guide1),
                ContextCompat.getColor(this, R.color.guide3)};
    }
    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }
}
