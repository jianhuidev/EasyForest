package com.example.kys_8.easyforest.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.User;
import com.example.kys_8.easyforest.ui.activity.MDRegisterActivity;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kys-8 on 2018/9/8.
 */

public class ThirdRFragment extends Fragment {

    private TextInputEditText nicknameEt,mailEt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        nicknameEt = view.findViewById(R.id.nickname_et_r);
        mailEt = view.findViewById(R.id.mail_et_r);
    }

    public void register(final String username, final String pwd){
        String nickname = nicknameEt.getText().toString().trim();
        String mail = mailEt.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)){
            ToastUtil.showToast(getContext(),"请填写昵称");
            return;
        }
        if (TextUtils.isEmpty(mail)){
            ToastUtil.showToast(getContext(),"请填写邮箱");
            return;
        }
        if (!checkMail(mail)){
            ToastUtil.showToast(getContext(),"请检查邮箱是否正确");
            return;
        }
        ((MDRegisterActivity)getActivity()).progressBar.setVisibility(View.VISIBLE);
        User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);
        user.setNickName(nicknameEt.getText().toString().trim());
        user.setEmail(mailEt.getText().toString().trim());
        user.setBirthday("请填写你的生日");
        user.setLeaves(0);
        user.setByoBg(7);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                ((MDRegisterActivity)getActivity()).progressBar.setVisibility(View.INVISIBLE);
                if (e == null){
                    ToastUtil.showToast(getContext(),"注册成功");
                    Intent intent = new Intent();
                    intent.putExtra("username",username);
                    intent.putExtra("password",pwd);
                    getActivity().setResult(RESULT_OK,intent);
                    getActivity().onBackPressed();
                }else {
                    ToastUtil.showToast(getContext(),"注册失败");
                    LogUtil.e("ThirdRFragment","注册失败："+e.getMessage());
                }
            }
        });
    }

    /**
     * 对邮箱地址校验
     */
    public boolean checkMail(String mail){
        String regex = "[a-zA-Z0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{1,3}){1,3}";
        return mail.matches(regex);
    }
}
