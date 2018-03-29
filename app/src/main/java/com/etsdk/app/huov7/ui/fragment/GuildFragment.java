package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.GuildHeader;
import com.etsdk.app.huov7.provider.GameItemViewProvider2;
import com.etsdk.app.huov7.provider.GuildHeaderViewProvider2;
import com.etsdk.app.huov7.util.JsonUtil;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * Created by Administrator on 2018\3\15 0015.
 */

public class GuildFragment extends AutoLazyFragment implements AdvRefreshListener {
    @BindView(R.id.ll_dots)
    LinearLayout ll_dots;
    @BindView(R.id.hunter_pager)
    ViewPager hunter_pager;
    @BindView(R.id.recyclerView)
    RecyclerView fragment_recycle;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    //    private MultiTypeAdapter multiTypeAdapter;
    Items items = new Items();
    BaseRefreshLayout baseRefreshLayout;
    private int isnew = 0;//	否	INT	是否新游 2 新游 1 普通 0 所有 20170113新增
    private int remd = 0;//	否	INT	是否新游 2 推荐 1 普通 0 所有 20170113新增
    private int server = 0;//	否	INT	是否新游 2 开服游戏 1 普通 0 所有 20170113新增
    private int test = 0;//	否	INT	是否新游 2 开测游戏 1 普通 0 所有 20170113新增
    private int hot = 0;
    private int category = 3;   //INT	是否单机 2 网游 1 GM 3 BT 4 折扣 5 精品 0 所有 20170113新增
    private String type;//分类id
    private List<Integer> imgs;
    private List<View> mDots = new ArrayList<>();// 存放圆点视图的集合
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_guild);
        setupUI();
    }

    private void setupUI() {
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(GuildHeader.class, new GuildHeaderViewProvider2());
        multiTypeAdapter.register(GameBean.class, new GameItemViewProvider2());
        fragment_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
        hunter_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int count = mDots.size();
                for (int i = 0; i < count; i++) {
                    if (position % imgs.size() == i) {
                        mDots.get(i).setBackgroundResource(
                                R.mipmap.dot_focus);
                    } else {
                        mDots.get(i).setBackgroundResource(
                                R.mipmap.dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                abc.removeCallbacksAndMessages(null);
                abc.sendEmptyMessageDelayed(0, 4000);
            }
        });
    }

    Handler abc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = hunter_pager.getCurrentItem();
            hunter_pager.setCurrentItem(currentItem + 1);
        }
    };


    /**
     * 开始轮播图片
     */
    private void startScrollView() {
        if (pagerAdapter == null) {
            pagerAdapter = new ViewPagerAdapter(imgs, getActivity());
            hunter_pager.setAdapter(pagerAdapter);
            hunter_pager.setCurrentItem(10000 * imgs.size());
        } else {
            pagerAdapter.notifyDataSetChanged();
        }
        // 实现轮播效果
        abc.sendEmptyMessageDelayed(0, 4000);
    }

    /**
     * 显示广告条
     */
    private void showBanner() {
        // 创建ViewPager对应的点
        ll_dots.removeAllViews();
        mDots.clear();
        for (int i = 0; i < imgs.size(); i++) {
            View dot = new View(getActivity());
            int size = StringUtils.dip2px(getActivity(), 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    size, size);
            params.leftMargin = size;
            if (i == 0) {
                dot.setBackgroundResource(R.mipmap.dot_focus);// 默认选择第1个点
            } else {
                dot.setBackgroundResource(R.mipmap.dot_normal);
            }
            dot.setLayoutParams(params);
            ll_dots.addView(dot);
            mDots.add(dot);
        }
    }


    public void getPageData(final int requestPageNo, final Items resultItems) {
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
    public void getPageData(final int requestPageNo) {
        imgs = new ArrayList<>();
        imgs.add(R.mipmap.item1);
        imgs.add(R.mipmap.item2);
        imgs.add(R.mipmap.item3);
        showBanner();
        startScrollView();
        if (requestPageNo != 1) {
            Items resultItems = new Items();
            getPageData(requestPageNo, resultItems);
            return;
        }
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.union);
        //成功，失败，null数据
        L.e("333", "url：" + AppApi.getUrl(AppApi.union));
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.union), new HttpJsonCallBackDialog<String>() {
            @Override
            public void onDataSuccess(String data) {
                L.e("333", "data：" + data);
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int code2 = jsonObject.getInt("code");
                    String data2 = jsonObject.getString("data");
                    L.e("333", "data2：" + data2);
                    GuildHeader header = JsonUtil.parse(data2, GuildHeader.class);
                    L.e("333", "name：" + header.getGuild().get(0).getName());
                    AileApplication.groupId = header.getGuild().get(0).getId();
                    if (code2 == 200) {
                        Items resultItems = new Items();
                        resultItems.add(header);
                        baseRefreshLayout.resultLoadData(items, resultItems, 1);
                        getPageData(requestPageNo, resultItems);
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Items resultItems = new Items();
                    resultItems.add(new GuildHeader());
                    baseRefreshLayout.resultLoadData(items, resultItems, 1);
                    getPageData(requestPageNo, resultItems);
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                L.e("333", "code：" + errorNo + "msg：" + strMsg + "data：" + completionInfo);
            }

        });
    }


    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }

}
