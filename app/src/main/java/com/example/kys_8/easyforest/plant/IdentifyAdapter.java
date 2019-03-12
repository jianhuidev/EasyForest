package com.example.kys_8.easyforest.plant;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by kys-8 on 2018/9/14.
 */

public class IdentifyAdapter extends RecyclerView.Adapter<IdentifyAdapter.ViewHolder>{

    private final String TAG = "IdentifyAdapter";
    private Context mContext;
    private List<IdentifyResult.ResultBean> mResultList;
    private DecimalFormat mDf;//= new DecimalFormat("0.00");

    public IdentifyAdapter(Context context, List<IdentifyResult.ResultBean> resultList) {
        this.mContext = context;
        mResultList = resultList;
        mDf = new DecimalFormat("0.00");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_identify,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(mResultList.get(position).getName());
        String con = mDf.format(mResultList.get(position).getScore()*100)+"%";
        holder.con.setText(con);
        holder.describe.setText(mResultList.get(position).getBaike_info().getDescription());
        Glide.with(mContext).load(mResultList.get(position).getBaike_info().getImage_url())
                .error(R.mipmap.card_image_1)
                .crossFade()
                .into(holder.img);
        holder.baike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext.));
                Intent intent = new Intent(mContext, IdentifyWebActivity.class);
//                intent.putExtra("name",mResultList.get(position).getName());
                intent.putExtra("url_web",mResultList.get(position).getBaike_info().getBaike_url());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
//        return mList != null?mList.size():0;
        return mResultList == null ? 0 : mResultList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView baike,name,con,describe;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            baike = itemView.findViewById(R.id.baike_identify);
            name = itemView.findViewById(R.id.name_identify);
            con = itemView.findViewById(R.id.con_identify);
            describe = itemView.findViewById(R.id.describe_identify);
            img = itemView.findViewById(R.id.img_identify);
        }
    }

}