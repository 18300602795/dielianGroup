package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.chat.ui.ChatActivity;
import com.etsdk.app.huov7.util.TimeUtils;
import com.liang530.log.L;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by Administrator on 2018\3\23 0023.
 */

public class ChatListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Conversation> conversations;

    public ChatListAdapter(Context context, List<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
    }

    public void upDate(List<Conversation> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
            return new ChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_no, parent, false);
            return new NoChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatViewHolder) {
            try {
                final Conversation conversation = conversations.get(position);
                Message message = conversation.getLatestMessage();
                ((ChatViewHolder) holder).name_tv.setText(message.getFromUser().getUserName());
                ((ChatViewHolder) holder).time_tv.setText(TimeUtils.getTime(message.getCreateTime() / 1000));
                String cont = "";
                switch (message.getContentType()) {
                    case file:
                        cont = "文件消息";
                        break;
                    case image:
                        cont = "图片消息";
                        break;
                    case location:
                        cont = "位置消息";
                        break;
                    case text:
                        TextContent textContent = (TextContent) message.getContent();
                        cont = textContent.getText();
                        break;
                    case video:
                        cont = "视频消息";
                        break;
                    case voice:
                        cont = "语音消息";
                        break;
                    default: {
                        cont = "未知消息";
                    }
                }
                ((ChatViewHolder) holder).count_tv.setText(message.getFromUser().getUserName() + "：" + cont);
                ((ChatViewHolder) holder).item_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        switch (conversation.getType()) {
                            case group:
                                L.i("333", "groupId：" + ((GroupInfo) conversation.getTargetInfo()).getGroupID());
                                intent.putExtra(AileApplication.GROUP_ID, ((GroupInfo) conversation.getTargetInfo()).getGroupID());
                                intent.putExtra(AileApplication.CONV_TITLE, conversation.getTitle());
                                break;
                            case single:
                                intent.putExtra(AileApplication.TARGET_ID, conversation.getTargetId());
                                intent.putExtra(AileApplication.CONV_TITLE, conversation.getTitle());
                                break;
                        }
                        context.startActivity(intent);
                    }
                });
            }catch (Exception e){

            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (conversations.size() == 0) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class NoChatViewHolder extends RecyclerView.ViewHolder {
        public NoChatViewHolder(View itemView) {
            super(itemView);
        }
    }


    class ChatViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_ll)
        LinearLayout item_ll;
        @BindView(R.id.head_img)
        RoundedImageView head_img;
        @BindView(R.id.name_tv)
        TextView name_tv;
        @BindView(R.id.time_tv)
        TextView time_tv;
        @BindView(R.id.count_tv)
        TextView count_tv;

        ChatViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
