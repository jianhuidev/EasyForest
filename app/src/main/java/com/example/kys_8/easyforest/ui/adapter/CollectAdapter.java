package com.example.kys_8.easyforest.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.ui.activity.FindDetaActivity;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by kys-8 on 2018/9/10.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> implements onMoveAndSwipedListener{

    private final String TAG = "CollectAdapter";
    private Context mContext;
    private List<UploadData> mList;

    public CollectAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<UploadData> list){
        this.mList = list;
        notifyDataSetChanged();

    }

    public void addItem(int position, UploadData insertData) {
        mList.add(position, insertData);
        notifyItemInserted(position);
        notifyItemRangeChanged(position,mList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_collect,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_recycler_item_show);
        holder.view.startAnimation(animation);
        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
        aa1.setDuration(400);
        holder.image.startAnimation(aa1);
        AlphaAnimation aa2 = new AlphaAnimation(0.1f, 1.0f);
        aa2.setDuration(400);

        if (mList != null){
            holder.contentTv.setText(mList.get(position).getContent());
            holder.collectTv.setText(mList.get(position).getCollectObjIds() == null? "0"
                    :mList.get(position).getCollectObjIds().size()+"");
            holder.timeTv.setText(mList.get(position).getCreatedAt());
            Glide.with(mContext).load(mList.get(position).getUserAvatarUrl())
                    .error(R.mipmap.avatar_off)
                    .crossFade()
                    .into(holder.avatar);
            Glide.with(mContext).load(mList.get(position).getImgs().get(0).getUrl())
                    .error(R.mipmap.card_image_1)
                    .crossFade()
                    .into(holder.image);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FindDetaActivity.class);
                    intent.putExtra("upload",mList.get(position));
                    mContext.startActivity(intent);
                }
            });
        }else {
            LogUtil.e(TAG,"   空");
        }
        holder.image.startAnimation(aa2);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemChanged(fromPosition);
        notifyItemChanged(toPosition);
        return true;
    }

    @Override
    public void onItemRemove(final int position) {
        final UploadData current = mList.get(position);
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mList.size());

        new AlertDialog.Builder(mContext).setMessage("确定取消该条收藏吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelCollect(current);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addItem(position,current);
                    }
                }).show();
    }

    /**
     * 取消收藏
     */
    private void cancelCollect(UploadData data){
        data.removeAll("collectObjIds", Collections.singletonList(GlobalVariable.userInfo.getObjectId()));
        data.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUtil.showToast(mContext,"取消收藏");
                }else{
                    LogUtil.e(TAG,"  失败："+e.getMessage());
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView contentTv,collectTv,timeTv;
        ImageView image,avatar;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = itemView.findViewById(R.id.img_collect);
            avatar = itemView.findViewById(R.id.avatar_collect);
            contentTv = itemView.findViewById(R.id.content_collect);
            timeTv = itemView.findViewById(R.id.time_collect);
            collectTv = itemView.findViewById(R.id.collect_tv);
        }
    }
}
