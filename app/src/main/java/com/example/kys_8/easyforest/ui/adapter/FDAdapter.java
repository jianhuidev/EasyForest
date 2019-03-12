package com.example.kys_8.easyforest.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.weight.ShowImagesDialog;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by kys-8 on 2018/9/10.
 */

public class FDAdapter extends RecyclerView.Adapter<FDAdapter.ViewHolder>{

    private static final String TAG = "FDAdapter";
    private Context mContext;
//    private int mSize;

    private List<String> mUrls;
    private int mRvWithdp;
//    private int mark;
    public FDAdapter(Context context, int rvWithdp) {
        this.mContext = context;

//        this.mark = mark;
        mRvWithdp = rvWithdp;
    }
    public void setDatas(List<String> imgs){
        mUrls = imgs;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fd,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (mUrls != null){

            Glide.with(mContext).load(mUrls.get(position))
                    .error(R.mipmap.card_image_1)
                    .crossFade()
                    .into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ShowImagesDialog(mContext, mUrls,position).show();
                }
            });
        }else {
            LogUtil.e(TAG,"   空");
        }
    }

    @Override
    public int getItemCount() {
        return mUrls == null ? 0 : mUrls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
//        TextView name_member;
//        CardView item_member;

        public ViewHolder(View itemView) {
            super(itemView);

            // 动态设置ImageView 的高度
            img = itemView.findViewById(R.id.img_item);
            ViewGroup.LayoutParams lp = img.getLayoutParams();
//            if (mark == 1){
//                lp.width = mRvWithdp;
//            }
            lp.height = mRvWithdp;
            itemView.setLayoutParams(lp);
//            item_member = (CardView)itemView.findViewById(R.id.item_member);
//            avatar_member = (ImageView)itemView.findViewById(R.id.avater_member);
//            name_member = (TextView) itemView.findViewById(R.id.name_member);

        }
    }

}
