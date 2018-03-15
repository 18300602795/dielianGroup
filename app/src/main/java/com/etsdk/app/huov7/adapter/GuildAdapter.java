package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameGiftItem;
import com.etsdk.app.huov7.view.GameTagView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_game_list_item, parent, false);
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

    public void setGameBean(GuildViewHolder holder, GameBean gameBean) {
        if (gameBean instanceof GameGiftItem && !"0".equals(gameBean.getGiftcnt())) {
            holder.tvSize.setTextColor(mContext.getResources().getColor(R.color.text_red));
            holder.tvSize.setText("礼包");
        } else {
            holder.tvSize.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            holder.tvSize.setText(gameBean.getSize());
        }
        holder.tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        holder.pbDown.setProgress(100);
        holder.tvGameName.setText(gameBean.getGamename());
        holder.tvOneword.setText(gameBean.getOneword());
//        GlideDisplay.display(ivGameImg, gameBean.getIcon(), R.mipmap.icon_load);
        Glide.with(mContext).load(gameBean.getIcon()).placeholder(R.mipmap.icon_load).into(holder.ivGameImg);
        holder.gameTagView.setGameType(gameBean.getType());

        //返利、打折
        if (gameBean.getDiscounttype() != 0) {
            holder.tvRate.setVisibility(VISIBLE);
            if (gameBean.getDiscounttype() == 1) {
//                tvRate.setText((gameBean.getDiscount() * 10) + "折");
                if (gameBean.getFirst_discount() > 0 && gameBean.getFirst_discount() < 1) {
                    holder.tvRate.setText(Math.round(gameBean.getFirst_discount() * 1000) / 100f + "折");
                } else if (gameBean.getDiscount() > 0 && gameBean.getDiscount() < 1) {
                    holder.tvRate.setText(Math.round(gameBean.getDiscount() * 1000) / 100f + "折");
                } else {
                    holder.tvRate.setVisibility(GONE);
                }
            } else {
                if (gameBean.getDiscount() > 0 && gameBean.getDiscount() < 1) {
                    holder.tvRate.setText("赠送" + Math.round(gameBean.getDiscount() * 1000) / 10f + "%");
                } else {
                    holder.tvRate.setVisibility(GONE);
                }
            }
        } else {
            holder.tvRate.setVisibility(GONE);
        }
        if ("2".equals(gameBean.getGive_first())) {
            holder.tvSendFirst.setVisibility(VISIBLE);
        } else {
            holder.tvSendFirst.setVisibility(GONE);
        }
    }

    class GuildViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.v_line)
        View vLine;
        @BindView(R.id.tv_hot_rank)
        TextView tvHotRank;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.pb_down)
        ProgressBar pbDown;
        @BindView(R.id.tv_down_status)
        TextView tvDownStatus;
        @BindView(R.id.game_list_item)
        RelativeLayout gameListItem;
        @BindView(R.id.iv_game_img)
        RoundedImageView ivGameImg;
        @BindView(R.id.tv_oneword)
        TextView tvOneword;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.gameTagView)
        GameTagView gameTagView;
        @BindView(R.id.btn_gift)
        Button btnGift;
        @BindView(R.id.ll_time_line)
        LinearLayout leftTimeLine;
        @BindView(R.id.v_time_line)
        View bottomTimeLine;
        @BindView(R.id.ll_game_name)
        LinearLayout llGameName;
        @BindView(R.id.tv_rate)
        TextView tvRate;
        @BindView(R.id.tv_gift)
        TextView tvGift;
        @BindView(R.id.tv_send_first)
        TextView tvSendFirst;

        public GuildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
