package com.example.kys_8.easyforest.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.ui.activity.MDRegisterActivity;
import com.example.kys_8.easyforest.utils.ToastUtil;
import com.example.kys_8.easyforest.weight.VerifyCode;

import java.util.Random;

/**
 * Created by kys-8 on 2018/9/8.
 */

public class SecondRFragment extends Fragment implements View.OnClickListener{

    private VerifyCode verifyCode;
    private ProgressBar progressBar;
    private TextInputEditText verifyEt,aPwdEt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secondr, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        verifyEt = view.findViewById(R.id.verify_et_r);
        aPwdEt = view.findViewById(R.id.a_pwd_et);
        verifyCode = view.findViewById(R.id.verifyCode_r);
        progressBar = view.findViewById(R.id.code_progressbar);
        updateVerifyCode();
        verifyCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verifyCode_r:
                updateVerifyCode();
                break;
        }
    }

    public boolean isVerifyOk(){
        String inputVerifyCode = verifyEt.getText().toString().trim().toLowerCase();
        String aPwd = aPwdEt.getText().toString().trim();
        if (TextUtils.isEmpty(inputVerifyCode)){
            ToastUtil.showToast(getContext(),"请填写验证码");
            return false;
        }
        if(TextUtils.isEmpty(aPwd)){
            ToastUtil.showToast(getContext(),"请确认密码");
            return false;
        }

        if (!inputVerifyCode.equals(verifyCode.getVCode().toLowerCase())){
            ToastUtil.showToast(getContext(),"验证码填写错误");
            return false;
        } else if(!aPwd.equals(((MDRegisterActivity)getActivity()).mPwd)){
            ToastUtil.showToast(getContext(),"确认密码错误");
            return false;
        }else {
            return true;
        }
    }

    public void updateVerifyCode(){
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                verifyCode.setvCode(getCharAndNumr());
            }
        },220);
    }

    /**
     * java生成随机数字和字母组合
     * @return 随机验证码
     */
    public String getCharAndNumr() {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
