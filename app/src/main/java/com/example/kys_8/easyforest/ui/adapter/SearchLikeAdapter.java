package com.example.kys_8.easyforest.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.ui.SearchClickListener;
import com.example.kys_8.easyforest.ui.activity.TreeDetaActivity;

import java.util.List;

/**
 * Created by kys-8 on 2018/9/21.
 */

public class SearchLikeAdapter extends RecyclerView.Adapter<SearchLikeAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mList;
    SearchClickListener mListener;
    public SearchLikeAdapter(Context context,SearchClickListener listener) {
        this.mContext = context;
        mListener = listener;
    }
    public void setData(List<String> list){
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

//        holder.img.setImageResource(R.drawable.nav_header1);
        holder.name.setText(mList.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddHistory(mList.get(position));
                Intent intent = new Intent(mContext, TreeDetaActivity.class);
                intent.putExtra("mark",mList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 :mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;
//        LinearLayout line;

        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
//            line = (LinearLayout) itemView.findViewById(R.id.line_sort);
            img = (ImageView)itemView.findViewById(R.id.img_search);
            name = (TextView) itemView.findViewById(R.id.name_search);

        }
    }
}
