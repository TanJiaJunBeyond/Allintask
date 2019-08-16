package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.utils.WindowUtils;

/**
 * Created by TanJiaJun on 2018/3/10.
 */

public class SelectWorkStatusDialog extends Dialog implements View.OnClickListener {

    private OnClickListener onClickListener;

    public SelectWorkStatusDialog(@NonNull Context context) {
        super(context, R.style.SelectWorkStatusDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()), -2);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_work_status, null);
        setContentView(contentView, layoutParams);

        TextView unfinishedTv = (TextView) contentView.findViewById(R.id.tv_unfinished);
        TextView completedTv = (TextView) contentView.findViewById(R.id.tv_completed);
        TextView cancelTv = (TextView) contentView.findViewById(R.id.tv_cancel);

        unfinishedTv.setOnClickListener(this);
        completedTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_unfinished:
                if (null != onClickListener) {
                    onClickListener.onWorkStatusClick(CommonConstant.WORK_STATUS_UNFINISHED);
                }
                break;

            case R.id.tv_completed:
                if (null != onClickListener) {
                    onClickListener.onWorkStatusClick(CommonConstant.WORK_STATUS_COMPLETED);
                }
                break;

            case R.id.tv_cancel:
                if (isShowing()) {
                    dismiss();
                }
                break;
        }
    }

    public interface OnClickListener {

        void onWorkStatusClick(int selectedWorkStatus);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
