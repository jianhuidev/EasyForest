package com.example.kys_8.easyforest.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.ui.activity.TreeDetaActivity;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kys-8 on 2018/10/18.
 */

public class BottomVerticalAdapter extends RecyclerView.Adapter<BottomVerticalAdapter.ViewHolder>{

    private Context mContext;
    private int placeCount;
    private List<TreeBean> mItems = new ArrayList<>();
    private boolean isQuery = false;
    private String queryTitle;

    public BottomVerticalAdapter(Context context,int placeCount,String title) {
        this.mContext = context;
        this.placeCount = placeCount;
        queryTitle = title;
//        mItems.add(new TreeBean());
    }

    public void setItems(List<TreeBean> data) {
        isQuery = true;
        this.mItems = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_tree_search,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (mItems.size()<1)
            return;

        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
        aa1.setDuration(400);
        holder.img.startAnimation(aa1);
        AlphaAnimation aa2 = new AlphaAnimation(0.1f, 1.0f);
        aa2.setDuration(400);

        if (isQuery){
            if (position == 0){
                holder.recommendLayout.setVisibility(View.VISIBLE);
                holder.recommendTv.setText(queryTitle);
                holder.progress.setVisibility(View.GONE);
            }else {
                holder.recommendLayout.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(mItems.get(position).getImgUrl())

                    .error(R.mipmap.card_image_1)
                    .crossFade()
                    .into(holder.img);
            holder.name.setText(mItems.get(position).getName());
            holder.abc.setText(mItems.get(position).getNameLatin());
            if (mItems.get(position).getBhjb().equals("Ⅰ")){
                holder.rank.setText("一级保护植物");
            }else if (mItems.get(position).getBhjb().equals("Ⅱ")){
                holder.rank.setText("二级保护植物");
            }

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, TreeDetaActivity.class);
                    intent.putExtra("mark","tree_bean");
                    intent.putExtra("tree",mItems.get(position));

                    mContext.startActivity(intent);

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                (Activity) mContext,(holder).img,"tree").toBundle());
//                    } else {
//                        mContext.startActivity(intent);
//                    }
                }
            });
        }else {
            if (position == 0){
                holder.recommendLayout.setVisibility(View.VISIBLE);
//                holder.recommendTv.setText(queryTitle);
            }else {
                holder.recommendLayout.setVisibility(View.GONE);
            }

        }
        holder.img.startAnimation(aa2);
    }

    @Override
    public int getItemCount() {
        return isQuery ? mItems.size():placeCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progress;
        LinearLayout recommendLayout;
        ImageView img;
        TextView name,abc,rank,recommendTv;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            progress = itemView.findViewById(R.id.progress_title);
            recommendLayout = itemView.findViewById(R.id.recommend_layout_bottom);
            recommendTv = itemView.findViewById(R.id.recommend_tv_bottom);
            img = (ImageView)itemView.findViewById(R.id.img_tree);
            name = (TextView) itemView.findViewById(R.id.name_tree);
            abc = (TextView) itemView.findViewById(R.id.abc_tree);
            rank = (TextView) itemView.findViewById(R.id.rank_tree);
        }
    }
}
