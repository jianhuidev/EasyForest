package com.example.kys_8.easyforest.plant;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.ui.activity.BaseActivity;
import com.example.kys_8.easyforest.utils.LogUtil;

public class IdentifyWebActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_web);
        progressBar = findViewById(R.id.progressBar_web);
        String url = getIntent().getStringExtra("url_web");
        mWebView = findViewById(R.id.web_view);
        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        };
        mWebView.setWebViewClient (webViewClient);
        mWebView.loadUrl(url);
    }
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()){
            mWebView.goBack();
            LogUtil.e("IdentifyWebActivity","back");
            return;
        }
        super.onBackPressed();
    }

}
