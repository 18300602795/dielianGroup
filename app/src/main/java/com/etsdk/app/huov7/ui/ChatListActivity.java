package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ChatListAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by Administrator on 2018\3\23 0023.
 */

public class ChatListActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tv_titleName;
    @BindView(R.id.xrecycle)
    XRecyclerView xrecycle;
    ChatListAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            xrecycle.refreshComplete();
            if (adapter != null) {
                List<Conversation> conversations = new ArrayList<>();
                for (Conversation conversation : JMessageClient.getConversationList()) {
                    if (conversation.getType() == ConversationType.single) {
                        conversations.add(conversation);
                    }
                }
                adapter.upDate(conversations);
            } else {
                List<Conversation> conversations = new ArrayList<>();
                for (Conversation conversation : JMessageClient.getConversationList()) {
                    if (conversation.getType() == ConversationType.single) {
                        conversations.add(conversation);
                    }
                }
                adapter = new ChatListAdapter(mContext, conversations);
                xrecycle.setAdapter(adapter);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.bind(this);
        tv_titleName.setText("聊天列表");
        List<Conversation> conversations = new ArrayList<>();
        for (Conversation conversation : JMessageClient.getConversationList()) {
            if (conversation.getType() == ConversationType.single) {
                conversations.add(conversation);
            }
        }
        adapter = new ChatListAdapter(mContext, conversations);
        xrecycle.setLayoutManager(new LinearLayoutManager(mContext));
        xrecycle.setAdapter(adapter);
        xrecycle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ChatListActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }
}
