package com.example.kys_8.easyforest.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.SortBean;
import com.example.kys_8.easyforest.bean.TreeBean;
import com.example.kys_8.easyforest.ui.activity.TreeDetaActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kys-8 on 2018/9/10.
 */

public class TreeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<TreeBean> mItems = new ArrayList<>();

    private final int TYPE_NORMAL = 1;
    private final int TYPE_FOOTER = 2;

    public TreeAdapter(Context context) {
        this.mContext = context;

    }

    public void setItems(List<TreeBean> data) {
        this.mItems = data;
        notifyDataSetChanged();
    }

    public void addItems(List<TreeBean> treeBeans) {
        mItems.addAll(treeBeans);
        if (mItems.size() != 0)
            notifyItemInserted(mItems.size() - 1);
        else
            notifyDataSetChanged();
    }
    public void addFooter() {
        TreeBean t = new TreeBean();
        t.setType("end");
        mItems.add(t);
        notifyItemInserted(mItems.size() - 1);
        notifyItemRangeChanged(mItems.size() - 1,mItems.size());
    }
    public void removeFooter() {
        mItems.remove(mItems.size() - 1);
        notifyItemRemoved(mItems.size());
        notifyItemRangeChanged(mItems.size() - 1,mItems.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tree, parent, false);
            return new RViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_footer, parent, false);
            return new FViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof RViewHolder) {
            RViewHolder rViewHolder = (RViewHolder) holder;
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_recycler_item_show);
            rViewHolder.view.startAnimation(animation);
            AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
            aa1.setDuration(400);
            rViewHolder.img.startAnimation(aa1);
            AlphaAnimation aa2 = new AlphaAnimation(0.1f, 1.0f);
            aa2.setDuration(400);

        Glide.with(mContext).load(mItems.get(position).getImgUrl())
                .error(R.mipmap.card_image_1)
                .crossFade()
                .into(rViewHolder.img);
            rViewHolder.name.setText(mItems.get(position).getName());
            rViewHolder.abc.setText(mItems.get(position).getNameLatin().trim());
            if (mItems.get(position).getBhjb().equals("Ⅰ")){
                rViewHolder.rank.setText("保护级别：一级保护植物");
            }else if (mItems.get(position).getBhjb().equals("Ⅱ")){
                rViewHolder.rank.setText("保护级别：二级保护植物");
            }

            rViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TreeDetaActivity.class);
                    intent.putExtra("mark","tree_bean");
                    intent.putExtra("tree",mItems.get(position));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                                (Activity) mContext,((RViewHolder) holder).img,"tree").toBundle());
                    } else {
                        mContext.startActivity(intent);
                    }
                }
            });

            rViewHolder.img.startAnimation(aa2);
        }

    }

    @Override
    public int getItemCount() {
//        Log.e("hahahah",mItems.size()+"");
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        String s = mItems.get(position).getType();
        if (s.equals("end")) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    class RViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name,abc,rank;
        View view;

        public RViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            img = (ImageView)itemView.findViewById(R.id.img_tree);
            name = (TextView) itemView.findViewById(R.id.name_tree);
            abc = (TextView) itemView.findViewById(R.id.abc_tree);
            rank = (TextView) itemView.findViewById(R.id.rank_tree);
        }
    }
    private class FViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progress_bar_load_more;

        private FViewHolder(View itemView) {
            super(itemView);
            progress_bar_load_more = itemView.findViewById(R.id.progress_bar_load_more);
        }
    }

}
