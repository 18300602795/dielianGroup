package com.etsdk.app.huov7.view.header_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018\3\15 0015.
 */

public class GuildHeaderView extends LinearLayout {
    public GuildHeaderView(Context context) {
        super(context);
        initView();
        setData();
    }

    private void initView() {
        ButterKnife.bind(this);
        LayoutInflater.from(getContext()).inflate(R.layout.header_guild, this);
    }

    public void setData() {

    }
}
