package com.example.kys_8.easyforest.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.SpUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePWActivity extends BaseActivity {

    private TextInputEditText old_password_et,new_password_et;

    private ProgressBar progressBar;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_pw;
    }

    @Override
    protected void initView() {
        old_password_et = (TextInputEditText)findViewById(R.id.old_password_change);
        new_password_et = (TextInputEditText)findViewById(R.id.new_password_change);
        progressBar = findViewById(R.id.progress_change);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.submit:
                if (TextUtils.isEmpty(old_password_et.getText().toString().trim()) ||
                        TextUtils.isEmpty(new_password_et.getText().toString().trim())){
                    ToastUtil.showToast(ChangePWActivity.this,"新旧密码不能为空");
                    return true;
                }
                if (old_password_et.getText().toString().trim().equals(GlobalVariable.curUserPd)){
                    progressBar.setVisibility(View.VISIBLE);
                    GlobalVariable.userInfo.setValue("password",new_password_et.getText().toString().trim());
                    GlobalVariable.userInfo.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if (e == null){
                                ToastUtil.showToast(ChangePWActivity.this,"修改密码成功，请重新登录");
                                GlobalVariable.userInfo = null;
                                SpUtil.remove("userInfo");
                                GlobalVariable.CHANGE_PWD = true;
                                onBackPressed();
                            }else {
                                ToastUtil.showToast(ChangePWActivity.this,"修改密码失败");
                                LogUtil.e("ChangePWActivity",e.getMessage());
                            }
                        }
                    });
                }else {
                    ToastUtil.showToast(ChangePWActivity.this,"请检查旧密码是否填写正确");
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.search_tree:
                mSearchView.display();
                break;
        }
        return true;
    }

    @Override
    protected String getTitleText() {
        return "修改密码";
    }

    @Override
    protected IPresenter loadPresenter() {
        return null;
    }
}
