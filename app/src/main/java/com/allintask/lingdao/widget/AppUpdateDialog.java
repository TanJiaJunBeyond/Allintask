package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.utils.WindowUtils;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2018/4/2.
 */

public class AppUpdateDialog extends Dialog implements View.OnClickListener {

    private String mContent;
    private boolean mIsForcedUpdate;
    private String mVersionName;
    private String mUpdateContent;
    private String mUrl;
    private boolean isDownloadingAPK;

    private Button contentBtn;

    private OnClickListener onClickListener;

    public AppUpdateDialog(@NonNull Context context, String content, boolean isForcedUpdate, String versionName, String updateContent, String url, boolean isDownloadingAPK) {
        super(context, R.style.AppUpdateDialogStyle);

        this.mContent = content;
        this.mIsForcedUpdate = isForcedUpdate;
        this.mVersionName = versionName;
        this.mUpdateContent = updateContent;
        this.mUrl = url;
        this.isDownloadingAPK = isDownloadingAPK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_app_update, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DensityUtils.dip2px(getContext(), 287) + DensityUtils.dip2px(getContext(), 23) / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(contentView, layoutParams);

        ImageView closeIv = (ImageView) contentView.findViewById(R.id.iv_close);
        TextView versionNameTv = (TextView) contentView.findViewById(R.id.tv_version_name);
        TextView updateContentTv = (TextView) contentView.findViewById(R.id.tv_update_content);
        contentBtn = (Button) contentView.findViewById(R.id.btn_content);

        if (!mIsForcedUpdate) {
            closeIv.setVisibility(View.VISIBLE);
        } else {
            closeIv.setVisibility(View.GONE);
        }

        versionNameTv.setText(mVersionName);

        String updateContent = mUpdateContent.replace("{newline}", "\n");
        updateContentTv.setText(updateContent);

        if (isDownloadingAPK) {
            contentBtn.setText(getContext().getString(R.string.downloading));
        } else {
            contentBtn.setText(mContent);
        }

        contentBtn.setEnabled(!isDownloadingAPK);
        contentBtn.setClickable(!isDownloadingAPK);

        closeIv.setOnClickListener(this);
        contentBtn.setOnClickListener(this);
    }

    public interface OnClickListener {

        void onClick(String content, String url);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setDownloadingAPK(boolean isDownloadingAPK) {
        this.isDownloadingAPK = isDownloadingAPK;

        if (null != contentBtn) {
            contentBtn.setText(getContext().getString(R.string.downloading));
            contentBtn.setEnabled(!isDownloadingAPK);
            contentBtn.setClickable(!isDownloadingAPK);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (isShowing()) {
                    dismiss();
                }
                break;

            case R.id.btn_content:
                if (null != onClickListener) {
                    onClickListener.onClick(mContent, mUrl);
                }
                break;
        }
    }

}
