package com.example.kys_8.easyforest.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.ui.adapter.BottomVerticalAdapter;
import com.example.kys_8.easyforest.weight.MaterialSearchView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by kys-8 on 2018/11/16.
 */

public class SearchDialog {

    private Activity activity;
    private MaterialSearchView mSearchView;
    private String query;
    private BottomVerticalAdapter verticalAdapter;

    public SearchDialog(Activity act,MaterialSearchView searchView,String str) {
        activity = act;
        mSearchView = searchView;
        query = str;
    }

    public void show(){
        List<String> list = SearchUtil.regexSearch(query, GlobalVariable.trees);
        String queryTitle = "为您搜索“"+query+"”，结果为";
        if (list.size() == 0){
            list = SearchUtil.recommendSearch(8);
            queryTitle = "您要搜索的“"+query+"”可能不是重点保护树木"+"\r\n"+"所以为您推荐以下搜索";
        }
        mSearchView.hideInputKey();
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_bottom_sheet_tree, null);
        RecyclerView rvVertical = view.findViewById(R.id.rv_vertical_dialog);
        rvVertical.setNestedScrollingEnabled(false);
        LinearLayoutManager verticalManager = new LinearLayoutManager(activity);
        rvVertical.setLayoutManager(verticalManager);
        verticalAdapter = new BottomVerticalAdapter(activity,list.size(),queryTitle);
        rvVertical.setAdapter(verticalAdapter);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();
        queryTree(list);
    }

    private void queryTree(List<String> list){
        BmobQuery<TreeBean> query = new BmobQuery<>();
        query.addWhereContainedIn("name",list);
        query.findObjects(new FindListener<TreeBean>() {
            @Override
            public void done(List<TreeBean> list, BmobException e) {
                if (e == null){
                    verticalAdapter.setItems(list);
                }else {
                    ToastUtil.showToast(activity,"请检查网络");
                }
            }
        });
    }

}
