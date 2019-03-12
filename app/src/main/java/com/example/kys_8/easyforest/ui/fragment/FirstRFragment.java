package com.example.kys_8.easyforest.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kys_8.easyforest.R;


/**
 * Created by kys-8 on 2018/9/8.
 */

public class FirstRFragment extends Fragment {

    private TextInputEditText usernameEt,pwdEt;
    public String username,pwd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firstr, container, false);
        initView(view);
        return view;
    }
    private void initView(View view){
        usernameEt = view.findViewById(R.id.username_et_r);
        pwdEt = view.findViewById(R.id.pwd_et_r);
    }

    public boolean isWrite(){
        username = usernameEt.getText().toString().trim();
        pwd = pwdEt.getText().toString().trim();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd))
            return true;
        else
            return false;
    }
}
