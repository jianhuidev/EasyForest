package com.example.kys_8.easyforest.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.ui.activity.FindDetaActivity;
import com.example.kys_8.easyforest.utils.DpUtil;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by kys-8 on 2018/9/10.
 */

public class MyShareAdapter extends RecyclerView.Adapter<MyShareAdapter.ViewHolder>{

    private final String TAG = "FindAdapter";
    private Context mContext;

    private List<UploadData> mItems;

    private FDAdapter imgAdapter;

    private int mRvWidth;

    public MyShareAdapter(Context context) {
        this.mContext = context;
        mRvWidth = DpUtil.getRvWidth(context);
    }

    public void setItems(List<UploadData> data) {
        this.mItems = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_share,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mItems != null){
            if (mItems.get(position).getCollectObjIds() != null)
                holder.starTv.setText("目前已有"+mItems.get(position).getCollectObjIds().size()+"个");
            holder.contentTv.setText(mItems.get(position).getContent());
            holder.timeTv.setText("我在"+mItems.get(position).getCreatedAt()+"上传的分享");
            holder.rv.setLayoutManager(setRv(mItems.get(position).getImgs().size()));
            setAdapter(position);
            holder.rv.setAdapter(imgAdapter);
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FindDetaActivity.class);
                    intent.putExtra("upload",mItems.get(position));
                    mContext.startActivity(intent);
                }
            });
            holder.card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(mContext).setMessage("是否删除该分享?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UploadData current = mItems.get(position);
                                    mItems.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,mItems.size());
                                    delUploadData(current);
                                }
                            }).setNegativeButton("取消", null).show();
                    return true;
                }
            });
        }else {
            LogUtil.e(TAG,"   空");
        }
    }

    private void delUploadData(UploadData current){
        current.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    ToastUtil.showToast(mContext,"删除成功");
                else
                    ToastUtil.showToast(mContext,"删除失败，请检查网络");
            }
        });
    }

    @Override
    public int getItemCount() {
//        return 20;
        return mItems != null ? mItems.size():0;
    }

    private GridLayoutManager setRv(int size){
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,3);
        if (size == 1){
            layoutManager.setSpanCount(1);
            imgAdapter = new FDAdapter(mContext,mRvWidth*2/3);
        }else if (size < 5){
            layoutManager.setSpanCount(2);
            imgAdapter = new FDAdapter(mContext,mRvWidth/2);
        }else if (size >= 5){
            layoutManager.setSpanCount(3);
            imgAdapter = new FDAdapter(mContext,mRvWidth/3);
        }
        return layoutManager;
    }

    private void setAdapter(int position){
        List<String> urlList = new ArrayList<>();
        if (mItems.get(position).getImgs() != null) {
            LogUtil.e("MyShareAdapter",mItems.get(position).getImgs().size()+"");
            for (BmobFile file : mItems.get(position).getImgs()){
                urlList.add(file.getUrl());
            }
            imgAdapter.setDatas(urlList);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView contentTv,timeTv,starTv;
        CardView card;
        RecyclerView rv;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cv_share);
            contentTv = itemView.findViewById(R.id.content_my_share);
            rv = itemView.findViewById(R.id.rv_img_my_share);
            starTv = itemView.findViewById(R.id.star_my_share);
            timeTv = itemView.findViewById(R.id.time_my_share);

        }

    }


}
