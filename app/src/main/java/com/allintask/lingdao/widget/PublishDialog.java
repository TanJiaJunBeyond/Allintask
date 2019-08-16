package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.ui.activity.main.PublishDemandActivity;
import com.allintask.lingdao.ui.activity.main.PublishServiceActivity;

/**
 * Created by TanJiaJun on 2018/1/5.
 */

public class PublishDialog extends Dialog implements View.OnClickListener {

    public OnClickListener onClickListener;

    public PublishDialog(@NonNull Context context) {
        super(context, R.style.PublishDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_publish, null);
        setContentView(view);

        ImageView publishServiceIv = (ImageView) view.findViewById(R.id.iv_publish_service);
        ImageView publishDemandIv = (ImageView) view.findViewById(R.id.iv_publish_demand);

        publishServiceIv.setOnClickListener(this);
        publishDemandIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_publish_service:
                if (isShowing()) {
                    dismiss();
                }

                if (null != onClickListener) {
                    onClickListener.onPublishServiceClick();
                }
                break;

            case R.id.iv_publish_demand:
                if (isShowing()) {
                    dismiss();
                }

                if (null != onClickListener) {
                    onClickListener.onPublishDemandClick();
                }
                break;
        }
    }

    public interface OnClickListener {

        void onPublishServiceClick();

        void onPublishDemandClick();

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
