package com.etsdk.app.huov7.chat.utils.keyboard.interfaces;

import android.view.ViewGroup;

import com.etsdk.app.huov7.chat.utils.keyboard.adapter.EmoticonsAdapter;


public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}
