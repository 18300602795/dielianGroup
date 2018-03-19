package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.ApklDownloadListener;
import com.etsdk.app.huov7.down.DownloadHelper;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameDownRequestBean;
import com.etsdk.app.huov7.model.GameDownResult;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.FuliGiftActivity;
import com.etsdk.app.huov7.ui.SettingActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.ui.dialog.DownAddressSelectDialogUtil;
import com.etsdk.app.huov7.ui.dialog.Open4gDownHintDialog;
import com.etsdk.app.huov7.view.DrawableTextView;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018\3\15 0015.
 */

public class GuildAdapter extends XRecyclerView.Adapter<GuildAdapter.GuildViewHolder> {

    private List<GameBean> gameBeens;
    private Context mContext;

    public GuildAdapter(List<GameBean> gameBeens, Context mContext) {
        this.gameBeens = gameBeens;
        this.mContext = mContext;
    }

    public void upDate(List<GameBean> gameBeens) {
        this.gameBeens.clear();
        this.gameBeens.addAll(gameBeens);
        notifyDataSetChanged();
    }

    public void addDate(List<GameBean> gameBeens) {
        this.gameBeens.addAll(gameBeens);
        notifyDataSetChanged();
    }


    @Override
    public GuildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_game_list_item2, parent, false);
        return new GuildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuildViewHolder holder, int position) {
        GameBean gameBean = gameBeens.get(position);
        setGameBean(holder, gameBean);
    }

    @Override
    public int getItemCount() {
        return gameBeens.size();
    }

    public void setGameBean(final GuildViewHolder holder, final GameBean gameBean) {
        if (holder.listener == null){
            holder.listener = new ApklDownloadListener() {
                @Override
                public void pending(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
                    L.i("333", "pending：" + TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                    holder.down_tv.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                }

                @Override
                public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
                    L.i("333", "progress：" + TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                    holder.down_tv.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                }

                @Override
                public void completed(TasksManagerModel tasksManagerModel) {
                    L.i("333", "completed：" + TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                    holder.down_tv.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                }

                @Override
                public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
                    L.i("333", "paused：" + TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                    holder.down_tv.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                }

                @Override
                public void error(TasksManagerModel tasksManagerModel, Throwable e) {
                    L.i("333", "error：" + TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                    holder.down_tv.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                }

                @Override
                public void prepareDown(TasksManagerModel tasksManagerModel, boolean noWifiHint) {
                    L.i("333", "prepareDown：" + TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                    holder.down_tv.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
                    if (noWifiHint) {//需要提示跳转到设置去打开非wifi下载
                        new Open4gDownHintDialog().showDialog(mContext, new Open4gDownHintDialog.ConfirmDialogListener() {
                            @Override
                            public void ok() {
                                SettingActivity.start(mContext);
                            }

                            @Override
                            public void cancel() {

                            }
                        });
                        return;
                    }
                    if (tasksManagerModel == null) {
                        tasksManagerModel = new TasksManagerModel();
                        tasksManagerModel.setGameId(gameBean.getGameid());
                        tasksManagerModel.setGameIcon(gameBean.getIcon());
                        tasksManagerModel.setGameName(gameBean.getGamename());
                        tasksManagerModel.setOnlyWifi(noWifiHint == true ? 0 : 1);
                        tasksManagerModel.setGameType(gameBean.getType());
//            tasksManagerModel.setUrl(gameBean.getDownlink());
                        getDownUrl(tasksManagerModel);
                    } else {
                        DownloadHelper.start(tasksManagerModel);
                    }
                }

                @Override
                public void netOff() {

                }

                @Override
                public void delete() {

                }

                @Override
                public void netRecover() {

                }

                @Override
                public void installSuccess() {

                }
            };
        }
        holder.tvGameName.setText(gameBean.getGamename());
        holder.tvOneword.setText(gameBean.getOneword());
        L.i("333", "状态：" + TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        holder.down_tv.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        Glide.with(mContext).load(gameBean.getIcon()).placeholder(R.mipmap.icon_load).into(holder.ivGameImg);
        holder.tvAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.down_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.down_tv.getText().equals("下载")) {
                    DownloadHelper.onClick(gameBean.getGameid(), holder.listener);
                } else {
                    DownloadManagerActivity.start(mContext);
                }

            }
        });

        holder.gift_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FuliGiftActivity.start(mContext, gameBean.getGameid());
            }
        });

        holder.share_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getDownUrl(final TasksManagerModel tasksManagerModel) {
        final GameDownRequestBean gameDownRequestBean = new GameDownRequestBean();
        gameDownRequestBean.setGameid(tasksManagerModel.getGameId());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(gameDownRequestBean));
        L.i("333", httpParamsBuild.getAuthkey());
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GameDownResult>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final GameDownResult data) {
                if (data != null) {
                    if (data.getList() != null && data.getList().size() != 0) {
                        if (data.getList().size() == 1) {//只有一个直接下载
                            GameDownResult.GameDown gameDown = data.getList().get(0);
                            if ("1".equals(gameDown.getType())) {//可以直接下载
                                if (!TextUtils.isEmpty(gameDown.getUrl())) {
                                    tasksManagerModel.setUrl(gameDown.getUrl());
                                    L.i("333", "" + gameDown.getUrl());
                                    tasksManagerModel.setDowncnt(data.getDowncnt() + "");
                                    DownloadHelper.start(tasksManagerModel);
                                } else {
                                    T.s(mContext, "暂无下载地址");
                                }
                            } else {//跳转到网页下载
                                WebViewActivity.start(mContext, "游戏下载", gameDown.getUrl());
                            }
                        } else {//多个下载地址，弹出选择
                            //弹出对话框，进行地址选择
                            DownAddressSelectDialogUtil.showAddressSelectDialog(mContext, data.getList(), new DownAddressSelectDialogUtil.SelectAddressListener() {
                                @Override
                                public void downAddress(String url) {
                                    tasksManagerModel.setUrl(url);
                                    tasksManagerModel.setDowncnt(data.getDowncnt() + "");
                                    DownloadHelper.start(tasksManagerModel);
                                }
                            });
                        }
                    } else {
                        T.s(mContext, "暂无下载地址");
                    }
                } else {
                    T.s(mContext, "暂无下载地址");
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        L.i("333", "url:" + AppApi.getUrl(AppApi.gameDownApi));
        RxVolley.post(AppApi.getUrl(AppApi.gameDownApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    class GuildViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.game_list_item)
        RelativeLayout gameListItem;
        @BindView(R.id.iv_game_img)
        RoundedImageView ivGameImg;
        @BindView(R.id.tv_oneword)
        TextView tvOneword;
        @BindView(R.id.tv_att)
        TextView tvAtt;
        @BindView(R.id.num_ll)
        LinearLayout num_ll;
        @BindView(R.id.down_tv)
        DrawableTextView down_tv;
        @BindView(R.id.gift_tv)
        DrawableTextView gift_tv;
        @BindView(R.id.share_tv)
        DrawableTextView share_tv;
        ApklDownloadListener listener;

        public GuildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
