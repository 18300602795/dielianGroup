package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.etsdk.app.huov7.R;


/**
 * Created by Administrator on 2018\3\5 0005.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private String[] imgs;

    public ImageAdapter(Context context, String[] imgs) {
        this.context = context;
        if (imgs == null) {
            imgs = new String[0];
        }
        this.imgs = imgs;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
//        Picasso.with(context).load(imgs[position]).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgs.length;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_img);
        }
    }

}
