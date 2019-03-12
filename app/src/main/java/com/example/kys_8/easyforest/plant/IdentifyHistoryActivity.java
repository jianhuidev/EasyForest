package com.example.kys_8.easyforest.plant;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.IdentifyHistory;
import com.example.kys_8.easyforest.ui.adapter.ItemTouchHelperCallback;
import com.example.kys_8.easyforest.utils.SearchDialog;
import com.example.kys_8.easyforest.weight.MaterialSearchView;
import com.example.kys_8.easyforest.weight.onSearchListener;
import com.example.kys_8.easyforest.weight.onSimpleSearchActionsListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class IdentifyHistoryActivity extends AppCompatActivity
        implements onSimpleSearchActionsListener,onSearchListener {

    private RecyclerView mRv;
    private IdentifyHistoryAdapter mAdapter;
    private SwipeRefreshLayout mRefresh;

    public MaterialSearchView mSearchView;
    private WindowManager mWindowManager;
    private Toolbar mToolbar;
    private boolean mSearchViewAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_history);
        mToolbar = findViewById(R.id.toolbar_identify);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("你的智能识树历史记录");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initMaterialSearchView();
        initView();
        mRefresh.setRefreshing(true);
        query();
    }

    private void initView(){
        mRefresh = findViewById(R.id.refresh_identify);
        mRefresh.setColorSchemeResources(R.color.google_blue,
                R.color.google_yellow, R.color.google_red, R.color.google_green);
        mRv = findViewById(R.id.rv_identify);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRv.setLayoutManager(layoutManager);
        mAdapter = new IdentifyHistoryAdapter(this);
        mRv.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
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
                                MaterialSearchView.getSearchViewLayoutParams(IdentifyHistoryActivity.this));
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

    private void query(){
        if (GlobalVariable.userInfo == null) return;
        BmobQuery<IdentifyHistory> query = new BmobQuery<>();
        query.addWhereEqualTo("userObjId", GlobalVariable.userInfo.getObjectId());
        query.findObjects(new FindListener<IdentifyHistory>() {
            @Override
            public void done(List<IdentifyHistory> list, BmobException e) {
                mRefresh.setRefreshing(false);
                if (e == null){
                    mAdapter.setData(list);
                }
            }
        });
    }

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
