package com.example.kys_8.easyforest.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.utils.SpUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;
import com.example.kys_8.easyforest.weight.MaterialSearchView;

public class SettingsActivity extends PreferenceActivity {

    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getDelegate().getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("设置");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addPreferencesFromResource(R.xml.preferences_settings);

        initPreference();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void initPreference(){
        findPreference("check_v").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtil.showToast(SettingsActivity.this,"当前已是最新版本");
                return false;
            }
        });

        findPreference("version_info").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(SettingsActivity.this,VInfoActivity.class));
                return false;
            }
        });

        findPreference("user_info").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (GlobalVariable.userInfo == null){
                    new AlertDialog.Builder(SettingsActivity.this).setMessage("您还未登录，是否跳转到登录界面 ?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(SettingsActivity.this,MDLoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null).show();
                }else {
                    startActivity(new Intent(SettingsActivity.this,MDUserInfoActivity.class));
                }
                return false;
            }
        });

        findPreference("modify_pwd").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (GlobalVariable.userInfo == null){
                    new AlertDialog.Builder(SettingsActivity.this).setMessage("您还未登录，是否跳转到登录界面 ?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(SettingsActivity.this,MDLoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null).show();
                }else {
                    startActivity(new Intent(SettingsActivity.this,ChangePWActivity.class));
                }
                return false;
            }
        });

        findPreference("byo_background").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(SettingsActivity.this,BgActivity.class));
                return false;
            }
        });

        findPreference("opinion").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (GlobalVariable.userInfo == null){
                    new AlertDialog.Builder(SettingsActivity.this).setMessage("您还未登录，是否跳转到登录界面 ?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(SettingsActivity.this,MDLoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null).show();
                }else {
                    startActivity(new Intent(SettingsActivity.this,OpinionActivity.class));
                }
                return false;
            }
        });

        findPreference("about_us").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(SettingsActivity.this,AboutUsActivity.class));
                return false;
            }
        });

        findPreference("help").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(SettingsActivity.this,HelpActivity.class));
                return false;
            }
        });

        findPreference("exit").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                GlobalVariable.userInfo = null;
                SpUtil.remove("userInfo");
                onBackPressed();
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalVariable.CHANGE_PWD){
            GlobalVariable.CHANGE_PWD = false;
            onBackPressed();
        }
    }
}
