package com.example.kys_8.easyforest.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.SortBean;
import com.example.kys_8.easyforest.ui.activity.TreeListActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kys-8 on 2018/9/10.
 */

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder>{

    private Context mContext;
    private List<SortBean> mList = new ArrayList<>();

    public SortAdapter(Context context,List<SortBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_a,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.img.setImageResource(mList.get(position).getRes());
        holder.tv.setText(mList.get(position).getName());


        holder.line.setOnClickListener(new View.OnClickListener() {
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
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tv;
        LinearLayout line;

        public ViewHolder(View itemView) {
            super(itemView);
            line = (LinearLayout) itemView.findViewById(R.id.line_sort);
            img = (ImageView)itemView.findViewById(R.id.img_sort);
            tv = (TextView) itemView.findViewById(R.id.ke_tv);

        }
    }

}
