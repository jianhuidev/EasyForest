package com.example.kys_8.easyforest.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.ui.SearchClickListener;
import com.example.kys_8.easyforest.ui.adapter.SearchLikeAdapter;
import com.example.kys_8.easyforest.ui.adapter.SearchTreeAdapter;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.SearchUtil;
import com.example.kys_8.easyforest.utils.SpUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;
import com.example.kys_8.easyforest.weight.FlowLayout;
import com.yuyh.library.imgsel.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchActivity extends AppCompatActivity{

    LinearLayout mLikeLine,mRecommendLine;
    FlowLayout mFlow;
    SearchView mSearchView;

    private String TAG = "SearchActivity";

    private int[] tvBg = {R.drawable.tv_bg_y,R.drawable.tv_bg_b,R.drawable.tv_bg_g,R.drawable.tv_bg_r,};
    RecyclerView mLikeRv,recommendRv;
    SearchLikeAdapter likeAdapter;
    SearchTreeAdapter treeAdapter;
    RelativeLayout historyLayout;
    /**
     * 历史记录数据
     */
    private String[] history;
    private TextView clearTv,noFindTv;
//    private List<String> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }

        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("发现");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ininHistory();
        initRecyclerView();
        initView();
    }


    private void initView(){
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setIconified(false);//..

        mFlow = (FlowLayout) findViewById(R.id.flow_search);
        mRecommendLine = (LinearLayout) findViewById(R.id.recommend_line);
        mLikeLine =(LinearLayout) findViewById(R.id.search_line);
        historyLayout = (RelativeLayout) findViewById(R.id.history_layout);
        clearTv = findViewById(R.id.clear_history);
        noFindTv = findViewById(R.id.no_find_tv);


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mLikeLine.setVisibility(View.VISIBLE);
                    likeAdapter.setData(SearchUtil.regexSearch(newText, GlobalVariable.trees));
                    if (likeAdapter.getItemCount() == 0){
                        noFindTv.setVisibility(View.VISIBLE);
                        noFindTv.setText("您要搜索的“"+newText+"”可能不是重点保护树木"+"\r\n"+"所以为您推荐以下搜索");
                        likeAdapter.setData(SearchUtil.recommendSearch(15));
                    }else {
                        noFindTv.setVisibility(View.GONE);
                    }
                }else {
                    mLikeLine.setVisibility(View.GONE);
                }
                return true;
            }
        });
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtils.d(TAG, "hasFocus " + hasFocus);
                if (!hasFocus) {
                    onBackPressed();
                }
            }
        });


        setHistoryLayout(); // 设置历史搜索
        clearTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 只有有历史搜索才能调用到清除方法
                removeAllViews(); // 视图清除
                history = new String[]{}; // 数组清除
                SpUtil.remove("history"); // 记录清除
            }
        });
    }


    private void initRecyclerView(){
//        List<String> searchList = Arrays.asList(arr);
        mLikeRv = findViewById(R.id.like_rv_search);
        LinearLayoutManager likeLayoutManager = new LinearLayoutManager(this);
        mLikeRv.setLayoutManager(likeLayoutManager);

        likeAdapter = new SearchLikeAdapter(this, new SearchClickListener() {
            @Override
            public void onAddHistory(String s) {
                addHistory(s);
            }
        });
        mLikeRv.setAdapter(likeAdapter);

        //..
        recommendRv = findViewById(R.id.recommend_rv_search);

        LinearLayoutManager recommendLayoutManager = new LinearLayoutManager(this);
        recommendRv.setLayoutManager(recommendLayoutManager);
        treeAdapter = new SearchTreeAdapter(this, new SearchClickListener() {
            @Override
            public void onAddHistory(String s) {
                addHistory(s);
            }
        });
        recommendRv.setAdapter(treeAdapter);
        query();
    }


    /**
     * 推荐查询
     */
    private void query(){
        int skip = new Random().nextInt(85);
        BmobQuery<TreeBean> query = new BmobQuery<>();
        query.setLimit(7);
        query.setSkip(skip);
        query.findObjects(new FindListener<TreeBean>() {
            @Override
            public void done(List<TreeBean> list, BmobException e) {
                if (e == null){
                    treeAdapter.setItems(list);
                }else {
                    LogUtil.e(TAG,e.getMessage()+" - "+e.getErrorCode());
                }
            }
        });
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

    /**
     * 初始化历史记录数据
     */
    private void ininHistory(){
        history = ((String)SpUtil.get("history","")).split(",");
//        LogUtil.e(TAG,history.toString());
    }

    /**
     * 搜索历史流式布局设置
     */
    private void setHistoryLayout(){
        LogUtil.e(TAG,"haha - history - >"+history.length);
        if (history.length == 1 && TextUtils.isEmpty(history[0])){
            historyLayout.setVisibility(View.GONE);
            return;
        }
        for (String s : history){
            if (!TextUtils.isEmpty(s))
                addTextView(s);
        }
    }

    /**
     * 更新历史数据，将新的数据添加到数组
     * 将视图添加
     * @param tag
     */
    private void addHistory(String tag){
        List<String> list = new ArrayList<>();
        for (String h : history)
            list.add(h);
        if (!list.contains(tag)){
            list.add(tag); // 不包含才添加
            if (historyLayout.getVisibility() == View.GONE)
                historyLayout.setVisibility(View.VISIBLE);
            addTextView(tag);
        }
        history = new String[list.size()];
        for (int i=0;i<list.size();i++) {
            history[i] = list.get(i);
        }
    }

    private void saveHistory(){
        StringBuilder s = new StringBuilder("");
        for (int i=0;i<history.length;i++){
            if (i!=history.length-1) {
                s.append(history[i]+",");
            }else {
                s.append(history[i]);
            }
        }
        SpUtil.put("history",s.toString());
    }

    /**
     * 移除flowGroupView 的所有数据
     */
    private void removeAllViews() {
        mFlow.removeAllViews();
        historyLayout.setVisibility(View.GONE);
    }
    /**
     * 为flowGroupView 添加TextView
     * @param str TextView 的字符串
     */
    private void addTextView(String str) {
        TextView child = new TextView(this);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        child.setLayoutParams(params);
        child.setBackgroundResource(randomTvBg());
        child.setText(str);
        child.setTextSize(14);
        child.setTextColor(Color.WHITE);
        initEvents(child);//监听

        /**给flowGroupView 添加动画*/
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.flow_item_anim);
        //得到一个LayoutAnimationController对象
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.2f);
        mFlow.setLayoutAnimation(controller);
        mFlow.addView(child);
    }
    private int randomTvBg(){
        Random random = new Random();
        int index = random.nextInt(tvBg.length);
        return tvBg[index];
    }

    /**
     * 添加监听
     * @param tv textView
     */
    private void initEvents(final TextView tv) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,TreeDetaActivity.class);
                intent.putExtra("mark",tv.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveHistory();
    }

    @Override
    public void onBackPressed() {
        mRecommendLine.setVisibility(View.GONE);
        super.onBackPressed();
    }
}
