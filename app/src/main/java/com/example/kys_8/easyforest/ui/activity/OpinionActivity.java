package com.example.kys_8.easyforest.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.Opinion;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.utils.ToastUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class OpinionActivity extends BaseActivity {

    private EditText mEt;
    private ProgressBar progressBar;

    @Override
    protected int getContentView() {
        return R.layout.activity_opinion;
    }

    @Override
    protected void initView() {
        LinearLayout etLayout = findViewById(R.id.et_layout_opinion);
        mEt = findViewById(R.id.et_opinion);
        progressBar = findViewById(R.id.progress_opinion);
        etLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEt.setSelected(true);
                mEt.setFocusable(true);
                mEt.setFocusableInTouchMode(true);
                mEt.requestFocus();
                InputMethodManager imm1 = (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.showSoftInput(mEt,0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.search_tree:
                mSearchView.display();
                break;
            case R.id.submit:
//                if (!TextUtils.isEmpty(mEt.getText().toString().trim())){
//                    progressBar.setVisibility(View.VISIBLE);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setVisibility(View.INVISIBLE);
//                            ToastUtil.showToast(OpinionActivity.this,"提交成功");
//                        }
//                    },1000);
//                }else {
//                    ToastUtil.showToast(OpinionActivity.this,"请填写信息");
//                }
                uploadOpinion();
                break;
        }
        return true;
    }

    private void uploadOpinion(){
        if (GlobalVariable.userInfo == null)
            return;
        if (TextUtils.isEmpty(mEt.getText().toString().trim())){
            ToastUtil.showToast(OpinionActivity.this,"请填写信息");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Opinion opinion = new Opinion();
        opinion.setObjectId(GlobalVariable.userInfo.getObjectId());
        opinion.setUsername(GlobalVariable.userInfo.getUsername());
        if (!TextUtils.isEmpty(GlobalVariable.userInfo.getEmail()))
            opinion.setEmail(GlobalVariable.userInfo.getEmail());
        opinion.setContent(mEt.getText().toString().trim());
        opinion.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                progressBar.setVisibility(View.INVISIBLE);
                if (e == null){
                    ToastUtil.showToast(OpinionActivity.this,"提交成功，感谢您的反馈！");
                }else {
                    ToastUtil.showToast(OpinionActivity.this,"提交失败，请检查网络！");
                }
            }
        });
    }

    @Override
    protected String getTitleText() {
        return "意见反馈";
    }

    @Override
    protected IPresenter loadPresenter() {
        return null;
    }
}
