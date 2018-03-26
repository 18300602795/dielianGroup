package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.MineAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\3\16 0016.
 */

public class MineFragment extends AutoLazyFragment {
    @BindView(R.id.fragment_recycle)
    RecyclerView fragment_recycle;
    MineAdapter adapter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_recycle2);
        setupUI();
    }

    private void setupUI() {
        adapter = new MineAdapter(getActivity());
        fragment_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragment_recycle.setAdapter(adapter);
        getUserInfoData();
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<UserInfoResultBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (adapter != null) {
                    adapter.setData(data);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                if (adapter != null)
                    adapter.setData(null);
                if (CODE_SESSION_ERROR.equals(code)) {
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi2), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        updateData();
    }

    /**
     * 更新数据
     */
    public void updateData() {
        getUserInfoData();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
    }
}
