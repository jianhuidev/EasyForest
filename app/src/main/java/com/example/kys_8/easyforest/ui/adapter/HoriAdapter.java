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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.ui.activity.TreeDetaActivity;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.List;

/**
 * Created by kys-8 on 2018/10/18.
 */

public class HoriAdapter extends RecyclerView.Adapter<HoriAdapter.ViewHolder>{

    private Context mContext;

    private List<TreeBean> mList;
    private static final String TAG = "HoriAdapter";

    public HoriAdapter(Context context,List<TreeBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setList(List<TreeBean> list){
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hori,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (mList != null){
            holder.name.setText(mList.get(position).getName());
            holder.number.setText(position+1+"/"+mList.size());
            if ("0".equals(mList.get(position).getImgUrl())){
                holder.image.setImageResource(R.mipmap.card_image_1);
            }else {
                AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
                aa1.setDuration(400);
                holder.image.startAnimation(aa1);
                AlphaAnimation aa2 = new AlphaAnimation(0.1f, 1.0f);
                aa2.setDuration(400);
                Glide.with(mContext).load(mList.get(position).getImgUrl())
                        .error(R.mipmap.card_image_1)
                        .crossFade()
                        .into(holder.image);
                if (mList.get(position).getBhjb().equals("Ⅰ")){
                    holder.bhjb.setText("一级保护植物");
                }else if (mList.get(position).getBhjb().equals("Ⅱ")){
                    holder.bhjb.setText("二级保护植物");
                }
                holder.latin.setText(mList.get(position).getNameLatin().trim());
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, TreeDetaActivity.class);
                        intent.putExtra("mark","tree_bean");
                        intent.putExtra("tree",mList.get(position));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    (Activity) mContext,holder.image,"tree").toBundle());
                        } else {
                            mContext.startActivity(intent);
                        }
                    }
                });
                holder.image.startAnimation(aa2);
            }
        }else {
            LogUtil.e(TAG,"   空");
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView bhjb,number,name,latin;

        public ViewHolder(View itemView) {
            super(itemView);
            bhjb = itemView.findViewById(R.id.bhjb_hori);
            image = itemView.findViewById(R.id.image_hori);
            number = itemView.findViewById(R.id.tv_number_hori);
            name = itemView.findViewById(R.id.name_hori);
            latin = itemView.findViewById(R.id.latin_hori);
        }
    }
}
