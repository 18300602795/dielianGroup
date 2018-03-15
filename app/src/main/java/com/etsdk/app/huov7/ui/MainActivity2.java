package com.etsdk.app.huov7.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GroupHomeAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.ui.fragment.GuildFragment;
import com.etsdk.app.huov7.ui.fragment.NewsListFragment;
import com.etsdk.app.huov7.util.StringUtils;
import com.etsdk.app.huov7.view.StickyNavLayout;
import com.game.sdk.log.L;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\3\14 0014.
 */

public class MainActivity2 extends ImmerseActivity {
    @BindView(R.id.sticky)
    StickyNavLayout sticky;
    @BindView(R.id.id_stickynavlayout_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.group_icon)
    ImageView group_icon;
    @BindView(R.id.group_name)
    TextView group_name;
    @BindView(R.id.group_introduce)
    TextView group_introduce;
    @BindView(R.id.group_ll)
    LinearLayout group_ll;
    @BindView(R.id.group_tv)
    TextView group_tv;
    @BindView(R.id.group_bg)
    View group_bg;
    @BindView(R.id.event_ll)
    LinearLayout event_ll;
    @BindView(R.id.event_tv)
    TextView event_tv;
    @BindView(R.id.event_bg)
    View event_bg;
    @BindView(R.id.chat_ll)
    LinearLayout chat_ll;
    @BindView(R.id.chat_tv)
    TextView chat_tv;
    @BindView(R.id.chat_bg)
    View chat_bg;
    @BindView(R.id.house_ll)
    LinearLayout house_ll;
    @BindView(R.id.house_tv)
    TextView house_tv;
    @BindView(R.id.house_bg)
    View house_bg;
    @BindView(R.id.mine_ll)
    LinearLayout mine_ll;
    @BindView(R.id.mine_tv)
    TextView mine_tv;
    @BindView(R.id.mine_bg)
    View mine_bg;
    List<Fragment> fragmentList = new ArrayList<>();
    private GroupHomeAdapter mAdapter;
    private List<View> views;
    private List<TextView> textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            L.i("333", "透明状态栏");
            setTranslucentStatus(true);

        }
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        initDate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bg_blue), 255);
    }


    /**
     * 设置状态栏透明
     *
     * @param on
     */
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    private void initDate() {
        views = new ArrayList<>();
        views.add(group_bg);
        views.add(house_bg);
        views.add(chat_bg);
        views.add(event_bg);
        views.add(mine_bg);
        textViews = new ArrayList<>();
        textViews.add(group_tv);
        textViews.add(house_tv);
        textViews.add(chat_tv);
        textViews.add(event_tv);
        textViews.add(mine_tv);
        fragmentList.add(new GuildFragment());
        fragmentList.add(NewsListFragment.newInstance("2", null));
        fragmentList.add(NewsListFragment.newInstance("3", null));
        fragmentList.add(NewsListFragment.newInstance("5", null));
        fragmentList.add(NewsListFragment.newInstance("1", null));
        mAdapter = new GroupHomeAdapter(getSupportFragmentManager(), fragmentList);
        clear();
        show(0);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clear();
                show(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        sticky.setStickyCallBack(new StickyNavLayout.StickyCallBack() {
            @Override
            public void move(int dy) {
                if (dy > (StringUtils.dip2px(mContext, 100))) {
                    dy = (StringUtils.dip2px(mContext, 100));
                }
                float scale;
                if (dy != 0) {
                    scale = (float) ((StringUtils.dip2px(mContext, 100)) - dy / 4) / (float) (StringUtils.dip2px(mContext, 100));
                } else {
                    scale = 1;
                }
                WindowManager wm = (WindowManager) mContext
                        .getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                float icon_width = ((float) dy / StringUtils.dip2px(mContext, 100)) * (width / 2 - StringUtils.dip2px(mContext, 40));
                ObjectAnimator.ofFloat(group_icon, "scaleX", scale).setDuration(0).start();
                ObjectAnimator.ofFloat(group_icon, "scaleY", scale).setDuration(0).start();
                ObjectAnimator.ofFloat(group_icon, "translationY", dy / 4 * 3).setDuration(0).start();
                ObjectAnimator.ofFloat(group_icon, "translationX", -icon_width).setDuration(0).start();
                float name_width = ((float) dy / StringUtils.dip2px(mContext, 100)) * (width / 2 - StringUtils.dip2px(mContext, 110));
                ObjectAnimator.ofFloat(group_name, "translationY", dy / 8).setDuration(0).start();
                ObjectAnimator.ofFloat(group_name, "translationX", -name_width).setDuration(0).start();
                float introduce_width = ((float) dy / StringUtils.dip2px(mContext, 100)) * (width / 2 - StringUtils.dip2px(mContext, 160));
                ObjectAnimator.ofFloat(group_introduce, "translationY", dy / 8).setDuration(0).start();
                ObjectAnimator.ofFloat(group_introduce, "translationX", -introduce_width).setDuration(0).start();
            }
        });
    }

    @OnClick({R.id.group_ll, R.id.event_ll, R.id.chat_ll, R.id.house_ll, R.id.mine_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_ll:
                clear();
                show(0);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.event_ll:
                clear();
                show(3);
                mViewPager.setCurrentItem(3);
                break;
            case R.id.chat_ll:
                clear();
                show(2);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.house_ll:
                clear();
                show(1);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.mine_ll:
                clear();
                show(4);
                mViewPager.setCurrentItem(4);
                break;
            default:
                break;
        }
    }

    private void clear() {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setVisibility(View.GONE);
            textViews.get(i).setTextColor(getResources().getColor(R.color.bg_pressed));
        }
    }

    private void show(int position) {
        views.get(position).setVisibility(View.VISIBLE);
        textViews.get(position).setTextColor(getResources().getColor(R.color.white));
    }

    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    //重写onBackPressed()方法,继承自退出的方法
    @Override
    public void onBackPressed() {
        // 判断时间间隔
        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            // 退出
            finish();
        }
    }

}
