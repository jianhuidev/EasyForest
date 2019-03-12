package com.example.kys_8.easyforest.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.ui.adapter.BottomVerticalAdapter;
import com.example.kys_8.easyforest.utils.SearchDialog;
import com.example.kys_8.easyforest.utils.SearchUtil;
import com.example.kys_8.easyforest.weight.MaterialSearchView;
import com.example.kys_8.easyforest.weight.onSearchListener;
import com.example.kys_8.easyforest.weight.onSimpleSearchActionsListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by kys-8 on 2018/10/15.
 */

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity
        implements onSimpleSearchActionsListener,onSearchListener {

    protected P mPresenter;
    protected FrameLayout mContent;
    public MaterialSearchView mSearchView;
    private WindowManager mWindowManager;
    private Toolbar mToolbar;
    private boolean mSearchViewAdded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mToolbar = findViewById(R.id.toolbar_base);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getTitleText());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initMaterialSearchView();
        mContent = findViewById(R.id.content_base);
        mContent.addView(View.inflate(this,getContentView(),null));

        initView();
    }

    private void initMaterialSearchView(){
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mSearchView = new MaterialSearchView(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setHintText("重点林木查询");

        if (mToolbar != null) {
            // Delay adding SearchView until Toolbar has finished loading
            mToolbar.post(new Runnable() {
                @Override
                public void run() {
                    if (!mSearchViewAdded && mWindowManager != null) {
                        mWindowManager.addView(mSearchView,
                                MaterialSearchView.getSearchViewLayoutParams(BaseActivity.this));
                        mSearchViewAdded = true;
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tree, menu);
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
        }
        return true;
    }

    /**
     * 获取中间内容显示区
     *
     * @return 布局
     */
    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract String getTitleText();

    protected abstract P loadPresenter();

    @Override
    public void onSearch(String query) {
        new SearchDialog(this,mSearchView,query).show();
    }

    @Override
    public void searchViewOpened() {}

    @Override
    public void searchViewClosed() {}

    @Override
    public void onCancelSearch() {mSearchView.hide();}

    @Override
    public void onItemClicked(String item) {}

    @Override
    public void onScroll() {}

    @Override
    public void error(String localizedMessage) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWindowManager != null)
            mWindowManager.removeViewImmediate(mSearchView);
    }

}
