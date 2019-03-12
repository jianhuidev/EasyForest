package com.example.kys_8.easyforest.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kys_8.easyforest.R;

/**
 * Created by kys-8 on 2018/11/12.
 */

public class MyShareViewHolder extends RecyclerView.ViewHolder {

    TextView contentTv,timeTv,starTv;
    View view;
    RecyclerView rv;

    public MyShareViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        contentTv = itemView.findViewById(R.id.content_my_share);
        rv = itemView.findViewById(R.id.rv_img_my_share);
        starTv = itemView.findViewById(R.id.star_my_share);
        timeTv = itemView.findViewById(R.id.time_my_share);
    }
}
