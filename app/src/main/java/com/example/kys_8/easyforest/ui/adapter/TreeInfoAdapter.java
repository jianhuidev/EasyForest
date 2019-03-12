package com.example.kys_8.easyforest.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.InfoBean;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.ui.activity.InfoActivity;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.List;


/**
 * Created by kys-8 on 2018/9/10.
 */

public class TreeInfoAdapter extends RecyclerView.Adapter<TreeInfoAdapter.ViewHolder>{

    private Context mContext;

    private List<InfoBean.DataBean> mItems;

    public TreeInfoAdapter(Context context,List<InfoBean.DataBean> datas) {
        this.mContext = context;
        this.mItems = datas;
    }
    public void setItems(List<InfoBean.DataBean> datas) {
        this.mItems = datas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tree_info,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
        aa1.setDuration(400);
        holder.img.startAnimation(aa1);
        AlphaAnimation aa2 = new AlphaAnimation(0.1f, 1.0f);
        aa2.setDuration(400);

        if (mItems != null){
            String title = mItems.get(position).getTitle();//.replaceAll();
            if (title != null)
                title = title.replaceAll("&quot","");
            holder.title.setText(title);
            holder.time.setText("时间："+mItems.get(position).getPublishDateStr());
            holder.author.setText("作者："+mItems.get(position).getPosterScreenName());
            if (mItems.get(position).getImageUrls() != null){
                Glide.with(mContext).load(mItems.get(position).getImageUrls().size() != 0
                        ? mItems.get(position).getImageUrls().get(0) : "0")
                        .error(R.mipmap.card_image_1)
                        .crossFade()
                        .into(holder.img);
            }
            holder.line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, InfoActivity.class);
                    intent.putExtra("info",mItems.get(position).getUrl());
                    mContext.startActivity(intent);
                }
            });
        }else {
            LogUtil.e("TreeInfoAdapter","   空");
        }
        holder.img.startAnimation(aa2);
    }

    @Override
    public int getItemCount() {
//        return 20;
        return mItems != null ? mItems.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView title,time,author;
        LinearLayout line;

        public ViewHolder(View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.line_info);
            img = itemView.findViewById(R.id.img_tree_info);
            title = itemView.findViewById(R.id.title_info);
            time = itemView.findViewById(R.id.time_info);
            author = itemView.findViewById(R.id.author_info);
        }
    }
}
