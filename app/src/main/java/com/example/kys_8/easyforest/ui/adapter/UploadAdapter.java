package com.example.kys_8.easyforest.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TakePhotoVariable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.MyViewHolder> implements MyRecyclerItemTouchCallback.ItemTouchAdapter {

    private Context context;
    private Activity activity;
    private int src;
    private List<String> results;
    private int resultsSize;

    public UploadAdapter(Activity activity, int src, List<String> results){
        this.activity=activity;
        this.src = src;

        syncResultList(results);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(src, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Glide.with(activity).load(new File(results.get(position))).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (resultsSize != TakePhotoVariable.MAX_SELECT_PHOTO_COUNT && (fromPosition==results.size()-1 || toPosition==results.size()-1)){
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(results, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(results, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        results.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth()-wm.getDefaultDisplay().getWidth()/5;
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = width/4;
            layoutParams.width = width/4;
            itemView.setLayoutParams(layoutParams);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }

    public void syncResultList(List<String> results){
        this.results = new ArrayList<>();
        for (String re:results){
            this.results.add(re);
        }
        resultsSize = results.size();
        if (this.results.size() != TakePhotoVariable.MAX_SELECT_PHOTO_COUNT){
            this.results.add(TakePhotoVariable.getAddBitmapPath(activity));
        }
    }
}
