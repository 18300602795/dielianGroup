package com.etsdk.app.huov7.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.chat.ui.ChatActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.ChatListActivity;
import com.etsdk.app.huov7.ui.LoginActivityV1;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.app.huov7.util.TimeUtils;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;


/**
 * Created by Administrator on 2018\2\22 0022.
 */

public class ChatFragment extends AutoLazyFragment {

    @BindView(R.id.login_ll)
    LinearLayout login_ll;
    @BindView(R.id.group_ll)
    LinearLayout group_ll;
    @BindView(R.id.chat_ll)
    LinearLayout chat_ll;
    @BindView(R.id.head_img)
    RoundedImageView head_img;
    @BindView(R.id.chat_img)
    RoundedImageView chat_img;
    @BindView(R.id.name_tv)
    public TextView name_tv;
    @BindView(R.id.time_tv)
    public TextView time_tv;
    @BindView(R.id.count_tv)
    public TextView count_tv;
    @BindView(R.id.chat_name_tv)
    TextView chat_name_tv;
    @BindView(R.id.chat_time_tv)
    TextView chat_time_tv;
    @BindView(R.id.chat_count_tv)
    public TextView chat_count_tv;
    @BindView(R.id.has_img)
    public ImageView has_img;


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        getUserInfoData();
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<UserInfoResultBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    initDate();
                } else {
                    login_ll.setVisibility(View.VISIBLE);
                    group_ll.setVisibility(View.GONE);
                    chat_ll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void initDate() {
        login_ll.setVisibility(View.GONE);
        group_ll.setVisibility(View.VISIBLE);
        chat_ll.setVisibility(View.VISIBLE);
        Conversation conversation = JMessageClient.getGroupConversation(25680755);
        Message message = conversation.getLatestMessage();
        time_tv.setText(TimeUtils.getTime(message.getCreateTime() / 1000));
        count_tv.setText(StringUtils.getCont(message));
    }

    @OnClick({R.id.group_ll, R.id.chat_ll, R.id.login_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_ll:
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(AileApplication.GROUP_ID, Long.valueOf(25680755));
                intent.putExtra(AileApplication.CONV_TITLE, "蝶恋公会");
                startActivity(intent);
                break;
            case R.id.chat_ll:
                has_img.setVisibility(View.GONE);
                chat_count_tv.setVisibility(View.GONE);
                ChatListActivity.start(getActivity());
                break;
            case R.id.login_ll:
                LoginActivityV1.start(getActivity());
                break;
        }
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        getUserInfoData();
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }
}
