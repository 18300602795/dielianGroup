package com.etsdk.app.huov7.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ReplyAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ArticleBean;
import com.etsdk.app.huov7.model.ArticleBeans;
import com.etsdk.app.huov7.util.JsonUtil;
import com.etsdk.app.huov7.view.header_view.CommentHeaderView;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\3\5 0005.
 */

public class CommentActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tv_titleName;
    @BindView(R.id.iv_title_down)
    ImageView iv_title_down;
    @BindView(R.id.comment_et)
    EditText comment_et;
    @BindView(R.id.comment_tv)
    TextView comment_tv;
    @BindView(R.id.comment_recycle)
    RecyclerView comment_recycle;
    ReplyAdapter adapter;
    CommentHeaderView headerView;
    HeaderAndFooterWrapper headerAndFooterWrapper;
    LoadMoreWrapper mLoadMoreWrapper;
    ArticleBeans commentBeans;

    ArticleBean argumentBean;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    T.s(mContext, (String) msg.obj);
                    break;
                case 2:
                    adapter = new ReplyAdapter(mContext, commentBeans.getCom());
                    headerView = new CommentHeaderView(mContext, argumentBean);
                    headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
                    headerAndFooterWrapper.addHeaderView(headerView);
                    mLoadMoreWrapper = new LoadMoreWrapper(headerAndFooterWrapper);
                    comment_recycle.setAdapter(mLoadMoreWrapper);
                    headerView.setLikes(commentBeans.getPraise());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initDate();
        getComments();
    }


    private void initDate() {
        tv_titleName.setText("评论");
        argumentBean = (ArticleBean) getIntent().getSerializableExtra("argument");
        iv_title_down.setVisibility(View.VISIBLE);
        iv_title_down.setImageResource(R.mipmap.share);
        comment_recycle.setLayoutManager(new LinearLayoutManager(mContext));
        comment_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    comment_tv.setVisibility(View.GONE);
                } else {
                    comment_tv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getComments() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.detailsListApi);
        httpParams.put("article_id", argumentBean.getArticle_id());
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.detailsListApi), new HttpJsonCallBackDialog<String>() {
            @Override
            public void onDataSuccess(String data) {
                L.e("333", "data：" + data);
                try {
                    parseJson(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                try {
                    parseJson(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                L.e("333", "errorNo：" + errorNo + "strMsg：" + strMsg + "completionInfo：" + completionInfo);
            }
        });
    }

    private void parseJson(String res) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        int code = jsonObject.getInt("code");
        String data = jsonObject.getString("data");
        if (code == 200) {
            commentBeans = JsonUtil.parse(data, ArticleBeans.class);
            Log.i("333", "commentBeans：" + commentBeans.getArticle_id());
            Log.i("333", "commentBeans：" + commentBeans.getPraise());
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        } else {
            String msg = jsonObject.getString("msg");
            Message message = new Message();
            message.what = 1;
            message.obj = msg;
            handler.sendMessage(message);
        }
    }

    private void sendComment() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.addReplyApi);
        httpParams.put("article_id", argumentBean.getArticle_id());
        httpParams.put("contents", comment_et.getText().toString());
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.addReplyApi), new HttpJsonCallBackDialog<String>() {
            @Override
            public void onDataSuccess(String data) {
                L.e("333", "data：" + data);
                try {
                    parseJson2(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                try {
                    parseJson2(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                L.e("333", "errorNo：" + errorNo + "strMsg：" + strMsg + "completionInfo：" + completionInfo);
            }
        });
    }

    private void parseJson2(String res) throws JSONException {
        JSONObject jsonObject = new JSONObject(res);
        int code = jsonObject.getInt("code");
        if (code == 200) {
        } else {
            String msg = jsonObject.getString("msg");
            Message message = new Message();
            message.what = 1;
            message.obj = msg;
            handler.sendMessage(message);
        }
    }


    @OnClick({R.id.iv_titleLeft, R.id.iv_title_down, R.id.send_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_title_down:
                break;
            case R.id.send_tv:
                sendComment();
                break;
        }
    }
}
