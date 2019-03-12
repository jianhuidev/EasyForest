package com.example.kys_8.easyforest.plant;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.CommentData;
import com.example.kys_8.easyforest.bean.IdentifyHistory;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.ui.adapter.TreeAdapter;
import com.example.kys_8.easyforest.ui.adapter.onMoveAndSwipedListener;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by kys-8 on 2018/9/14.
 */

public class IdentifyHistoryAdapter extends RecyclerView.Adapter<IdentifyHistoryAdapter.ViewHolder> {

    private final String TAG = "IdentifyHistoryAdapter";
    private Context mContext;

    private List<IdentifyHistory> mList;

    public IdentifyHistoryAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<IdentifyHistory> list){
        mList = list;
        notifyDataSetChanged();
    }

    private void addItem(int position, IdentifyHistory data) {
        mList.add(position, data);
        notifyItemInserted(position);
        notifyItemRangeChanged(position,mList.size());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_identify_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.anim_show);
        holder.card.startAnimation(animation);
        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
        aa1.setDuration(400);
        holder.treeImg.startAnimation(aa1);
        AlphaAnimation aa2 = new AlphaAnimation(0.1f, 1.0f);
        aa2.setDuration(400);
        if (mList != null){
            holder.name.setText(mList.get(position).getMajorTree());
            holder.con.setText(mList.get(position).getMajorCon());
//            LogUtil.e("IdentifyHistoryAdapter",mList.get(position).getMainImg());
            Glide.with(mContext).load(mList.get(position).getMainImg())
                    .error(R.mipmap.card_image_1)
                    .crossFade()
                    .into(holder.treeImg);
        }else {
            LogUtil.e(TAG,"   空");
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, IdentifyDetaActivity.class);
                intent.putExtra("title",mList.get(position).getMajorTree()+"  "+mList.get(position).getMajorCon());
                intent.putExtra("identify",mList.get(position).getResult());
                mContext.startActivity(intent);
            }
        });
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(mContext).setMessage("你确定要删除该记录吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IdentifyHistory current = mList.get(position);
                                mList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,mList.size());
                                affirmDelData(current);
                            }
                        })
                        .setNegativeButton("取消",null).show();
                return true;
            }
        });
        holder.treeImg.startAnimation(aa2);
    }

    @Override
    public int getItemCount() {
//        return mList != null?mList.size():0;
        return mList != null ? mList.size() : 0;
    }

    /**
     * 将数据库的删除了
     */
    public void affirmDelData(IdentifyHistory data){
        data.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUtil.showToast(mContext,"已删除该数据");
                }else{
                    ToastUtil.showToast(mContext,"删除失败"+e.getMessage()+e.getErrorCode());
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView treeImg;
        TextView name,con;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_identify);
            treeImg = itemView.findViewById(R.id.tree_identify);
            name = itemView.findViewById(R.id.name_identify);
            con = itemView.findViewById(R.id.con_identify);
        }
    }

}