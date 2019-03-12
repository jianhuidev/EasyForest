package com.example.kys_8.easyforest.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.utils.LogUtil;

import java.util.List;


/**
 * Created by kys-8 on 2018/9/10.
 */

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.ViewHolder>{

    private final String TAG = "FindAdapter";
    private Context mContext;

    private List<UploadData> mItems;
    ClickCallBack mCallBack;

    public FindAdapter(Context context,ClickCallBack callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
    }
    public void setItems(List<UploadData> data) {
        this.mItems = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_find,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mItems != null){
            holder.collect.setText(mItems.get(position).getCollectObjIds() == null? "0"
                    :mItems.get(position).getCollectObjIds().size()+"");
            holder.contentTv.setText(mItems.get(position).getContent());
            holder.timeTv.setText(mItems.get(position).getCreatedAt());
            Glide.with(mContext).load(mItems.get(position).getUserAvatarUrl())
                    .error(R.mipmap.avatar_off)
                    .crossFade()
                    .into(holder.avatar);
            Glide.with(mContext).load(mItems.get(position).getImgs().get(0).getUrl())
                    .error(R.mipmap.card_image_1)
                    .crossFade()
                    .into(holder.img);
        }else {
            LogUtil.e(TAG,"   空");
        }
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClick(mItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar,img;
        TextView collect,contentTv,timeTv;
        LinearLayout line;

        public ViewHolder(View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.line_find);
            img = itemView.findViewById(R.id.img_find_item);
            contentTv = itemView.findViewById(R.id.content_my_data);
            avatar = itemView.findViewById(R.id.avatar_find_item);
            timeTv = itemView.findViewById(R.id.time_my_data);
            collect = itemView.findViewById(R.id.collect_find_item);
        }
    }

    public interface ClickCallBack{
        void onClick(UploadData data);
    }

}
