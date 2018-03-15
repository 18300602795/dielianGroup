package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GuildAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.view.header_view.GuildHeaderView;
import com.game.sdk.log.L;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\3\15 0015.
 */

public class GuildFragment extends AutoLazyFragment {
    @BindView(R.id.fragment_recycle)
    XRecyclerView fragment_recycle;
    GuildHeaderView headerView;
    private int currentPage = 1;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;
    GuildAdapter adapter;
    private boolean requestTopSplit = false;//是否需要顶部分割线
    private int isnew = 0;//	否	INT	是否新游 2 新游 1 普通 0 所有 20170113新增
    private int remd = 0;//	否	INT	是否新游 2 推荐 1 普通 0 所有 20170113新增
    private int server = 0;//	否	INT	是否新游 2 开服游戏 1 普通 0 所有 20170113新增
    private int test = 0;//	否	INT	是否新游 2 开测游戏 1 普通 0 所有 20170113新增
    private int hot = 0;
    private int category = 0;   //INT	是否单机 2 网游 1 单机 0 所有 20170113新增
    private String type;//分类id
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    fragment_recycle.refreshComplete();
                    Log.i("333", "加载数据");
                    adapter = new GuildAdapter((ArrayList<GameBean>) msg.obj, mContext);
                    headerView = new GuildHeaderView(getActivity());
                    headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
                    headerAndFooterWrapper.addHeaderView(headerView);
                    mLoadMoreWrapper = new LoadMoreWrapper(headerAndFooterWrapper);
//        headerFooterAdapter.addHeaderView();
                    fragment_recycle.setAdapter(mLoadMoreWrapper);
                    break;
                case 2:
                    fragment_recycle.refreshComplete();
                    if (currentPage == 1) {
                        adapter.upDate((ArrayList<GameBean>) msg.obj);
                    } else {
                        adapter.addDate((ArrayList<GameBean>) msg.obj);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_recycle);
        setupUI();
    }

    private void setupUI() {
        fragment_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragment_recycle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                getPageData();
            }

            @Override
            public void onLoadMore() {
                currentPage += 1;
                getPageData();
            }
        });
        getPageData();
    }

    public void getPageData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.gameListApi);
        httpParams.put("hot", hot);
        httpParams.put("isnew", isnew);
        httpParams.put("remd", remd);
        httpParams.put("server", server);
        httpParams.put("test", test);
        httpParams.put("category", category);
        if (type != null) {
            httpParams.put("type", type);
        }
        httpParams.put("page", currentPage);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi), new HttpJsonCallBackDialog<GameBeanList>() {
            @Override
            public void onDataSuccess(GameBeanList data) {
                Message message = new Message();
                Log.i("333", "获取到数据");
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    Log.i("333", "size：" + data.getData().getList().size());
                    message.obj = data.getData().getList();
                } else {
                    message.obj = new ArrayList<GameBean>();
                }
                if (adapter == null) {
                    message.what = 1;
                } else {
                    message.what = 2;
                }
                handler.sendMessage(message);
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.i("333", "code：" + code);
                L.i("333", "msg：" + msg);
                L.i("333", "data：" + data);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                Message message = new Message();
                message.obj = new ArrayList<GameBean>();
                message.what = 1;
                handler.sendMessage(message);
            }
        });
    }


    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }

}
