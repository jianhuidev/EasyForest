package com.example.kys_8.easyforest.ui.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.User;

import com.example.kys_8.easyforest.ui.activity.MDLoginActivity;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.SpUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by kys-8 on 2018/10/28.
 */

public class LoginFragment extends Fragment {

    public Button forgotPwdBtn;
    public TextInputEditText usernameEt,passwordEt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        usernameEt = view.findViewById(R.id.username_et_login);
        passwordEt = view.findViewById(R.id.password_et_login);
        forgotPwdBtn = view.findViewById(R.id.btn_forgot_pwd);
        forgotPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MDLoginActivity)getActivity()).toRegisterBtn.setText(R.string.back);
                ((MDLoginActivity)getActivity()).mHintTv.setText(R.string.title2_login);
                ((MDLoginActivity)getActivity()).loginBtn.setText(R.string.submit);
                if (((MDLoginActivity)getActivity()).cViewPager == null) return;
                ((MDLoginActivity)getActivity()).currentPosition += 1;
                ((MDLoginActivity)getActivity()).cViewPager.setCurrentItem(
                        ((MDLoginActivity)getActivity()).currentPosition,true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((MDLoginActivity)getActivity()).isToRegister){
            forgetPwdAnim();
            ((MDLoginActivity)getActivity()).isToRegister = false;
        }
    }

    private void forgetPwdAnim(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.forget_pwd_in);
        forgotPwdBtn.startAnimation(animation);
        forgotPwdBtn.setVisibility(View.VISIBLE);
    }

    public void login(){
        String username = usernameEt.getText().toString().trim();
        final String pwd = passwordEt.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            ToastUtil.showToast(getContext(),"请填写用户名");
            return;
        }
        if (TextUtils.isEmpty(pwd)){
            ToastUtil.showToast(getContext(),"请填写密码");
            return;
        }
        if (pwd.length()<6 || pwd.length()>18){
            ToastUtil.showToast(getContext(),"密码长度为6-18位");
            return;
        }

        ((MDLoginActivity)getActivity()).progressBar.setVisibility(View.VISIBLE);
        User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                ((MDLoginActivity)getActivity()).progressBar.setVisibility(View.INVISIBLE);
                if (e == null){
                    GlobalVariable.userInfo = user;
                    GlobalVariable.curUserPd = pwd;
                    SpUtil.put("userInfo", JSON.toJSONString(user));
                    SpUtil.put("pd", pwd);
                    getActivity().finish();
                }else {
                    ToastUtil.showToast(getContext(),"请检查用户名或密码");
                    LogUtil.e("LoginFragment","查询失败："+e.getMessage()+" "+e.getErrorCode());
                }
            }
        });
    }

}