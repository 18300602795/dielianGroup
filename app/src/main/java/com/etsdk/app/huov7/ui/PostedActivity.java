package com.etsdk.app.huov7.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.PostPhotoAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.L;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Administrator on 2018\2\28 0028.
 */

public class PostedActivity extends ImmerseActivity implements PostPhotoAdapter.PostPhotoCallback {
    private static final int REQUEST_IMAGE = 2;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    @BindView(R.id.tv_titleName)
    TextView tv_titleName;
    @BindView(R.id.tv_titleRight)
    TextView tv_titleRight;
    @BindView(R.id.photo_recycle)
    RecyclerView photo_recycle;
    List<String> bitmaps;
    ArrayList<String> mSelectPath;
    PostPhotoAdapter photoAdapter;
    @BindView(R.id.title_et)
    EditText title_et;
    @BindView(R.id.con_et)
    EditText con_et;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted);
        ButterKnife.bind(this);
        initDate();
    }

    private void initDate() {
        tv_titleName.setText("发帖");
        tv_titleRight.setVisibility(View.VISIBLE);
        tv_titleRight.setText("发布");
        photo_recycle.setLayoutManager(new GridLayoutManager(mContext, 5));
        bitmaps = new ArrayList<>();
        photoAdapter = new PostPhotoAdapter(bitmaps, mContext);
        photoAdapter.setPhotoCallback(this);
        photo_recycle.setAdapter(photoAdapter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.addArticleApi);
                httpParams.put("article_title", title_et.getText().toString());
                httpParams.put("contents", con_et.getText().toString());
                L.e("333", "图片：" + bitmaps.size());
                for (String path : bitmaps) {//耗时操作
                    L.e("333", "图片：" + path);
                    httpParams.put("image[]", new File(path));
                }
                //成功，失败，null数据
                NetRequest.request(this).setParams(httpParams).showDialog(true).post(AppApi.getUrl(AppApi.addArticleApi), new HttpJsonCallBackDialog<String>() {
                    @Override
                    public void onDataSuccess(String data) {
                        L.e("333", "data：" + data);
                    }

                    @Override
                    public void onJsonSuccess(int code, String msg, String data) {
                        L.e("333", "code：" + code + "msg：" + msg + "data：" + data);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg, String completionInfo) {
                        L.e("333", "errorNo：" + errorNo + "strMsg：" + strMsg + "completionInfo：" + completionInfo);
                    }
                });
                break;
        }

    }

    @Override
    public void close(int position) {
        bitmaps.remove(position);
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void add() {
        pickImage();
    }

    @Override
    public void show(int position) {
    }

    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            int maxNum = 9;
            MultiImageSelector selector = MultiImageSelector.create(mContext);
            selector.showCamera(true);
            selector.count(maxNum);
            selector.multi();
            selector.origin(mSelectPath);
            selector.start((Activity) mContext, REQUEST_IMAGE);
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                bitmaps.clear();
                for (String p : mSelectPath) {
                    bitmaps.add(p);
                }
                photoAdapter.notifyDataSetChanged();
            }
        }
    }

}
