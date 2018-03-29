package com.etsdk.app.huov7.ui.fragment;

/**
 * Created by Administrator on 2017/11/14.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * head1图片轮播适配器
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<Integer> listbean;// 装载着轮播图的信息
    private Context context;

    public ViewPagerAdapter(List<Integer> listbean, Context context) {
        this.listbean = listbean;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listbean == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container,
                                  final int position) {
        ImageView img = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                -1, -1);
        img.setLayoutParams(layoutParams);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        final int listBean = listbean.get(position % listbean.size());
        img.setImageResource(listBean);

        container.addView(img);
        return img;
    }


}
