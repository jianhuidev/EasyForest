package com.example.kys_8.easyforest.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.presenter.IPresenter;
import com.example.kys_8.easyforest.ui.adapter.BottomVerticalAdapter;
import com.example.kys_8.easyforest.utils.SearchDialog;
import com.example.kys_8.easyforest.weight.MaterialSearchView;
import com.example.kys_8.easyforest.weight.onSearchListener;
import com.example.kys_8.easyforest.weight.onSimpleSearchActionsListener;

/**
 * Created by kys-8 on 2018/10/15.
 */

public abstract class TreeActivity<P extends IPresenter> extends AppCompatActivity
        implements onSimpleSearchActionsListener,onSearchListener {

    protected P mPresenter;
    public SwipeRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public MaterialSearchView mSearchView;
    private WindowManager mWindowManager;
    private Toolbar mToolbar;
    private boolean mSearchViewAdded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
        dealIntent();
        initView();
    }

    private void initView(){
        mToolbar = findViewById(R.id.toolbar_tree);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getTitleText());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initMaterialSearchView();
//        mProgressLayout = findViewById(R.id.progress_layout_tree);
        mRefreshLayout = findViewById(R.id.refresh_tree);
        mRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_yellow, R.color.google_red, R.color.google_green);
        mRecyclerView = findViewById(R.id.rv_tree);
        setRecyclerView();

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
                                MaterialSearchView.getSearchViewLayoutParams(TreeActivity.this));
                        mSearchViewAdded = true;
                    }
                }
            });
        }
    }

    protected abstract void dealIntent();

    protected abstract String getTitleText();

    protected abstract P loadPresenter();

    protected abstract void setRecyclerView();

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

    @Override
    public void onSearch(String query) {
        new SearchDialog(this,mSearchView,query).show();
    }

//    private boolean keRegex(String str){
//        String regex = "[\\u4e00-\\u9fa5]+科";
//        return str.matches(regex);
//    }
//    private boolean treeRegex(String str){
//        String regex = "[\\u4e00-\\u9fa5]+";
//        return str.matches(regex);
//    }

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
