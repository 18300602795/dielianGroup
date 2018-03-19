package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.GuildHeader;
import com.etsdk.app.huov7.provider.GameItemViewProvider2;
import com.etsdk.app.huov7.provider.GuildHeaderViewProvider;
import com.etsdk.app.huov7.view.header_view.GuildHeaderView;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * Created by Administrator on 2018\3\15 0015.
 */

public class GuildFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView fragment_recycle;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    GuildHeaderView headerView;
    private int currentPage = 1;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;
    private MultiTypeAdapter multiTypeAdapter;
    Items items = new Items();
    BaseRefreshLayout baseRefreshLayout;
    private boolean requestTopSplit = true;//是否需要顶部分割线
    private int isnew = 0;//	否	INT	是否新游 2 新游 1 普通 0 所有 20170113新增
    private int remd = 0;//	否	INT	是否新游 2 推荐 1 普通 0 所有 20170113新增
    private int server = 0;//	否	INT	是否新游 2 开服游戏 1 普通 0 所有 20170113新增
    private int test = 0;//	否	INT	是否新游 2 开测游戏 1 普通 0 所有 20170113新增
    private int hot = 0;
    private int category = 3;   //INT	是否单机 2 网游 1 GM 3 BT 4 折扣 5 精品 0 所有 20170113新增
    private String type;//分类id

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_rec);
        setupUI();
    }

    private void setupUI() {
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.applyGlobalMultiTypePool();
        multiTypeAdapter.register(GuildHeader.class, new GuildHeaderViewProvider());
        multiTypeAdapter.register(GameBean.class, new GameItemViewProvider2());
        multiTypeAdapter.notifyDataSetChanged();
        fragment_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(final int requestPageNo) {
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
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.gameListApi), new HttpJsonCallBackDialog<GameBeanList>() {
            @Override
            public void onDataSuccess(GameBeanList data) {
                if (data != null && data.getData() != null && data.getData().getList() != null) {
                    int maxPage = (int) Math.ceil(data.getData().getCount() / 20.);
                    if (isnew == 1 || hot == 1) {//热门和新游只要20个
                        maxPage = 1;
                    }
                    Items resultItems = new Items();
                    if (requestTopSplit && requestPageNo == 1) {//新游热门第一页第一个顶部有分割线
                        resultItems.add(new GuildHeader());
                    }
                    resultItems.addAll(data.getData().getList());
                    baseRefreshLayout.resultLoadData(items, resultItems, maxPage);
                } else {
                    baseRefreshLayout.resultLoadData(items, new ArrayList(), requestPageNo - 1);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                baseRefreshLayout.resultLoadData(items, null, null);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(items, null, null);
            }
        });
    }


    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }

}
