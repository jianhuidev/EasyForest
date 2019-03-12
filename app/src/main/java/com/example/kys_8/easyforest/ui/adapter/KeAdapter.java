package com.example.kys_8.easyforest.ui.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.kys_8.easyforest.bean.KeBean;
import com.example.kys_8.easyforest.ui.activity.TreeListActivity;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.List;

/**
 * Created by kys-8 on 2018/9/14.
 */

public class KeAdapter extends RecyclerView.Adapter<KeAdapter.ViewHolder>{

    private final String TAG = "KeAdapter";
    private Context mContext;

    private List<KeBean> mList;
//    private final int TYPE_NORMAL = 1;
//    private final int TYPE_FOOTER = 2;
    public KeAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<KeBean> list){
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ke_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.anim_show);
        holder.view.startAnimation(animation);
        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
        aa1.setDuration(400);
        holder.img.startAnimation(aa1);
        AlphaAnimation aa2 = new AlphaAnimation(0.1f, 1.0f);
        aa2.setDuration(400);
        if (mList != null){
            holder.tv.setText(mList.get(position).getName());
            Glide.with(mContext).load(mList.get(position).getImgUrl())
                    .error(R.mipmap.card_image_1)
                    .crossFade()
                    .into(holder.img);
        }else {
            LogUtil.e(TAG,"   空");
        }
        holder.img.startAnimation(aa2);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TreeListActivity.class);
                intent.putExtra("ke",mList.get(position).getName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList != null?mList.size():0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tv;
        CardView cardView;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            cardView = itemView.findViewById(R.id.ke_card_view);
            img = itemView.findViewById(R.id.ke_img);
            tv = itemView.findViewById(R.id.ke_tv);

        }
    }

}