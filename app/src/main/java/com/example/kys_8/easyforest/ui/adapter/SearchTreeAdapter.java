package com.example.kys_8.easyforest.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.ui.SearchClickListener;
import com.example.kys_8.easyforest.ui.activity.KeActivity;
import com.example.kys_8.easyforest.ui.activity.TreeDetaActivity;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kys-8 on 2018/9/10.
 */

public class SearchTreeAdapter extends RecyclerView.Adapter<SearchTreeAdapter.ViewHolder>{

    private static final String TAG = "SearchTreeAdapter";
    private Activity mContext;
    private List<TreeBean> mItems = new ArrayList<>();

    private final int TYPE_NORMAL = 1;
    private final int TYPE_FOOTER = 2;

    private SearchClickListener mListener;

    public SearchTreeAdapter(Activity context,SearchClickListener listener) {
        this.mContext = context;
        mListener = listener;
    }

    public void setItems(List<TreeBean> data) {
        this.mItems = data;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tree_search,parent,false);
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
                    mListener.onAddHistory(mItems.get(position).getName());
                    Intent intent = new Intent(mContext, TreeDetaActivity.class);
                    intent.putExtra("mark","tree_bean");
                    intent.putExtra("tree",mItems.get(position));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                                (Activity) mContext,(holder).img,"tree").toBundle());
                    } else {
                        mContext.startActivity(intent);
                    }
                }
            });
        }else {
            LogUtil.e(TAG,"  空");
        }
        holder.img.startAnimation(aa2);

    }

    @Override
    public int getItemCount() {
//        Log.e("hahahah",mItems.size()+"");
        return mItems == null ? 0 : mItems.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name,abc,rank;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            img = (ImageView)itemView.findViewById(R.id.img_tree);
            name = (TextView) itemView.findViewById(R.id.name_tree);
            abc = (TextView) itemView.findViewById(R.id.abc_tree);
            rank = (TextView) itemView.findViewById(R.id.rank_tree);
        }
    }

    public interface ClickCallBack{
        void onAddHistory(String s);
    }
}
