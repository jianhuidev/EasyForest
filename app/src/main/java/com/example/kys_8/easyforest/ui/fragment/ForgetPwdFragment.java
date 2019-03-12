package com.example.kys_8.easyforest.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.ui.activity.MDLoginActivity;
import com.example.kys_8.easyforest.utils.ToastUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by kys-8 on 2018/10/28.
 */

public class ForgetPwdFragment extends Fragment {

    public TextInputEditText mailEt;

//    private ImageView mail,security;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_pwd, container, false);
        mailEt = view.findViewById(R.id.mail_et_forget_pwd);
//        mail = view.findViewById(R.id.forget_mail);
//        security = view.findViewById(R.id.forget_security);
//        Glide.with(getActivity()).load(R.drawable.forget_mail).into(mail);
//        Glide.with(getActivity()).load(R.drawable.forget_security).into(security);
        return view;
    }


    public void modifyPwd(){
        final String mail = mailEt.getText().toString().trim();
        if (TextUtils.isEmpty(mail)){
            ToastUtil.showToast(getActivity(),"请输入邮箱");
            return;
        }
        ((MDLoginActivity)getActivity()).progressBar.setVisibility(View.VISIBLE);
        BmobUser.resetPasswordByEmail(mail, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                ((MDLoginActivity)getActivity()).progressBar.setVisibility(View.INVISIBLE);
                if (e == null){
                    ToastUtil.showToast(getActivity(),"请到"+mail+"，进行密码修改");
                }else {
                    ToastUtil.showToast(getActivity(),"发送邮件失败，请检查网络");
                }
            }
        });
    }

}