package com.example.kys_8.easyforest.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.CommentData;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by kys-8 on 2018/9/10.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private static final String TAG = "CommentAdapter";
    private Context mContext;

    private List<CommentData> mList;
    private UploadData uploadData;
    public CommentAdapter(Context context,UploadData uploadData) {
        this.mContext = context;
        this.uploadData = uploadData;

    }
    public void setDatas(List<CommentData> list){
        mList = list;
        notifyDataSetChanged();
    }

    public void addItem(CommentData data) {
        mList.add(0, data);
        notifyItemInserted(0);
        notifyItemRangeChanged(0,mList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (mList != null){
            Glide.with(mContext).load(mList.get(position).getUserAvatarUrl())
                    .error(R.mipmap.card_image_1)
                    .crossFade()
                    .into(holder.avatar);
            holder.name.setText(mList.get(position).getUsername());
            holder.content.setText(mList.get(position).getContent());
            holder.time.setText(mList.get(position).getCreatedAt());

        }else {
            LogUtil.e(TAG,"   空");
        }
        if (GlobalVariable.userInfo != null){
            if (GlobalVariable.userInfo.getObjectId().equals(mList.get(position).getUserId())){
                holder.lineComment.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(mContext).setMessage("是否删除该评论 ?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        CommentData current = mList.get(position);
                                        mList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,mList.size());

                                        // 更新云数据库
                                        delComment(current);

                                    }
                                })
                                .setNegativeButton("取消", null).show();
                        return true;
                    }
                });
            }
        }

//        holder.lineComment.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                new AlertDialog.Builder(mContext).setMessage("是否删除该评论 ?")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                CommentData current = mList.get(position);
//                                mList.remove(position);
//                                notifyItemRemoved(position);
//                                notifyItemRangeChanged(position,mList.size());
//
//                                // 更新云数据库
//                                delComment(current);
//
//                            }
//                        })
//                        .setNegativeButton("取消", null).show();
//                return true;
//            }
//        });

    }

    @Override
    public int getItemCount() {
//        return 10;
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        ImageView avatar;
        TextView name,content,time;

        LinearLayout lineComment;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            lineComment = itemView.findViewById(R.id.line_comment);
            avatar = itemView.findViewById(R.id.avatar_comment);
            name = itemView.findViewById(R.id.name_comment);
            content = itemView.findViewById(R.id.content_comment);
            time = itemView.findViewById(R.id.time_comment);
        }
    }

    private void delComment(CommentData current){
        current.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    ToastUtil.showToast(mContext,"删除成功");
                    uploadData.increment("commentCount",-1);
                    uploadData.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){

                            }else {
                                LogUtil.e(TAG,"评论删除了，可是commentCount ，-1 失败"+e.getMessage());
                            }
                        }
                    });
                }else {
                    ToastUtil.showToast(mContext,"删除失败，请检查网络");
                }
            }
        });
    }

}
