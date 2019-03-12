package com.example.kys_8.easyforest.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.InfoBean;
import com.example.kys_8.easyforest.bean.SortBean;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.ui.activity.KeActivity;
import com.example.kys_8.easyforest.ui.adapter.HoriAdapter;
import com.example.kys_8.easyforest.ui.adapter.SortAdapter;
import com.example.kys_8.easyforest.ui.adapter.TreeInfoAdapter;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.RequestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by kys-8 on 2018/9/13.
 */

public class SortFragment extends Fragment implements View.OnClickListener{

    private RecyclerView treeInfoRv;
    private HoriAdapter horiAdapter;
    private ProgressBar mProgress;
    private FrameLayout mRefreshLayout;
    private TreeInfoAdapter treeInfoAdapter;
    private List<SortBean> aList = new ArrayList<>();
    private List<SortBean> gList = new ArrayList<>();
    private List<SortBean> pList = new ArrayList<>();
    private String[] mKeyword = {"%E6%A4%8D%E6%A0%91","%E5%A4%A7%E6%A0%91","%E6%A0%91%E6%9E%9D", // 植树，大树，树枝
            "%E6%9E%97%E6%9C%A8","%E6%A0%91%E6%9C%A8","%E6%A0%91%E6%9E%97", // 林木，树木，树林
            "%E7%BB%BF%E5%8C%96","%E9%A3%8E%E6%99%AF"}; // 绿化，风景
    private int keyIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort, container, false);
        initData();
        initView(view);
        initRecyclerView(view);
        return view;
    }

    private void initView(View view){
        mProgress = view.findViewById(R.id.progressBar_sort);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        TextView mAmore = view.findViewById(R.id.a_more);
        TextView mGmore = view.findViewById(R.id.g_more);
        TextView mPmore = view.findViewById(R.id.p_more);
        mAmore.setOnClickListener(this);
        mGmore.setOnClickListener(this);
        mPmore.setOnClickListener(this);
    }

    private void initRecyclerView(View view){
        RecyclerView horiRv = view.findViewById(R.id.rv_hori);
        LinearLayoutManager horiManager = new LinearLayoutManager(getContext());
        horiManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horiRv.setLayoutManager(horiManager);
        List<TreeBean> list = new ArrayList<>();
        TreeBean treeBean = new TreeBean();
        treeBean.setName("林木推荐");
        treeBean.setImgUrl("0");
        for (int i = 0; i<5; i++){
            list.add(treeBean);
        }
        horiAdapter = new HoriAdapter(getContext(),list);
        horiRv.setAdapter(horiAdapter);
        query();

        RecyclerView mArv = view.findViewById(R.id.a_rv_sort);
        mArv.setNestedScrollingEnabled(false);
        LinearLayoutManager alayoutManager = new LinearLayoutManager(getContext());
        alayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mArv.setLayoutManager(alayoutManager);
        SortAdapter mAdapter = new SortAdapter(getContext(), aList);
        mArv.setAdapter(mAdapter);

        RecyclerView mGrv = view.findViewById(R.id.g_rv_sort);
        mGrv.setNestedScrollingEnabled(false);
        LinearLayoutManager glayoutManager = new LinearLayoutManager(getContext());
        glayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGrv.setLayoutManager(glayoutManager);
        mAdapter = new SortAdapter(getContext(),gList);
        mGrv.setAdapter(mAdapter);

        RecyclerView mPrv = view.findViewById(R.id.p_rv_sort);
        mPrv.setNestedScrollingEnabled(false);
        LinearLayoutManager playoutManager = new LinearLayoutManager(getContext());
        playoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPrv.setLayoutManager(playoutManager);
        mAdapter = new SortAdapter(getContext(),pList);
        mPrv.setAdapter(mAdapter);

        treeInfoRv = view.findViewById(R.id.tree_info_rv);
        treeInfoRv.setNestedScrollingEnabled(false);
        List<InfoBean.DataBean> dataList = new ArrayList<>();
        InfoBean.DataBean data = new InfoBean.DataBean();
        data.setPosterScreenName("在加载");
        data.setTitle("在加载");
        data.setPublishDateStr("在加载");
        for (int i = 0; i<5; i++)
            dataList.add(data);
        treeInfoAdapter = new TreeInfoAdapter(getContext(),dataList);
        LinearLayoutManager treeInfoManager = new LinearLayoutManager(getContext());
        treeInfoRv.setLayoutManager(treeInfoManager);
        treeInfoRv.setAdapter(treeInfoAdapter);
        requestInfo();
        mRefreshLayout.setOnClickListener(this);
    }

    /**
     * 推荐查询
     */
    private void query(){
        int skip = new Random().nextInt(85);
        BmobQuery<TreeBean> query = new BmobQuery<>();
        query.setLimit(5);
        query.setSkip(skip);
        query.findObjects(new FindListener<TreeBean>() {
            @Override
            public void done(List<TreeBean> list, BmobException e) {
                if (e == null){
                    horiAdapter.setList(list);
                }else {LogUtil.e("SortFragment",e.getMessage());}
            }
        });
    }

    private void initData(){
        aList.add(new SortBean("连香树科",R.mipmap.lianxiangshu));
        aList.add(new SortBean("桦木科",R.mipmap.huamushu));
        aList.add(new SortBean("樟科",R.mipmap.zke));
        aList.add(new SortBean("龙脑香科",R.mipmap.longnaoxiang));
        aList.add(new SortBean("木兰科",R.mipmap.mulanke));
        aList.add(new SortBean("金缕梅科",R.mipmap.jinlvhai));

        gList.add(new SortBean("柏科",R.mipmap.bai));
        gList.add(new SortBean("三尖杉科 ",R.mipmap.sanjianshan));
        gList.add(new SortBean("苏铁科",R.mipmap.sutie));
        gList.add(new SortBean("银杏科",R.mipmap.yinxing));
        gList.add(new SortBean("松科",R.mipmap.song));
        gList.add(new SortBean("红豆杉科",R.mipmap.hongdoushan));

        pList.add(new SortBean("观音座莲科",R.mipmap.guanyinzuolian));
        pList.add(new SortBean("七指蕨科",R.mipmap.qizhijue));
        pList.add(new SortBean("蹄盖蕨科",R.mipmap.tigai));
        pList.add(new SortBean("乌毛蕨科",R.mipmap.wumao));
        pList.add(new SortBean("天星蕨科",R.mipmap.tianxing));
        pList.add(new SortBean("鳞毛蕨科",R.mipmap.linmao));
    }

    private void requestInfo(){
        mRefreshLayout.setVisibility(View.GONE); // 进行请求隐藏刷新键
        mProgress.setVisibility(View.VISIBLE); // 进行请求显示刷新进度条
        RequestUtil.httpRequest(getInfoUrl(), new InfoCallBack() {
            @Override
            public void infoResult(final InfoBean info) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (info != null)
                            treeInfoAdapter.setItems(info.getData());
                        mRefreshLayout.setVisibility(View.VISIBLE); // 请求完毕显示刷新键
                        mProgress.setVisibility(View.GONE); // 进行请求隐藏刷新进度条
                        keyIndex++;
                    }
                });
            }
        });
    }

    private String getInfoUrl(){
        int akey = keyIndex%(mKeyword.length);
        String kw = mKeyword[akey];
        return "http://api01.idataapi.cn:8000/news/qihoo?kw="+kw+"&site=qq.com&apikey=7Ww1pOYorGSNYrWZf6usFVMYOjtuwF0IuDPTmq0NkW2VcgreiSj0Xk9bKVD5ODAf";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.a_more:
                Intent a = new Intent(getContext(), KeActivity.class);
                a.putExtra("type","a");
                startActivity(a);
                break;
            case R.id.g_more:
                Intent g = new Intent(getContext(), KeActivity.class);
                g.putExtra("type","g");
                startActivity(g);
                break;
            case R.id.p_more:
                Intent p = new Intent(getContext(), KeActivity.class);
                p.putExtra("type","p");
                startActivity(p);
                break;
            case R.id.refresh_layout:
                requestInfo();
                break;
        }
    }
    public interface InfoCallBack{
        void infoResult(InfoBean info);
    }
}
