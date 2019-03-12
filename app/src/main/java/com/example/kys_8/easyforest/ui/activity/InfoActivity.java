package com.example.kys_8.easyforest.ui.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.utils.LogUtil;

public class InfoActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        String url = getIntent().getStringExtra("info");
        progressBar = findViewById(R.id.progressBar_info);
        mWebView = findViewById(R.id.web_view_info);
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
            LogUtil.e("InfoActivity","back");
            return;
        }
        super.onBackPressed();
    }
}
