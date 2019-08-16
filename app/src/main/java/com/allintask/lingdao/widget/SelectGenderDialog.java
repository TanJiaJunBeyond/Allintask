package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.allintask.lingdao.R;
import com.allintask.lingdao.utils.WindowUtils;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2018/3/23.
 */

public class SelectGenderDialog extends Dialog implements View.OnClickListener {

    private OnClickListener onClickListener;

    public SelectGenderDialog(@NonNull Context context) {
        super(context, R.style.SelectGenderDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = View.inflate(getContext(), R.layout.dialog_select_gender, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()) - DensityUtils.dip2px(getContext(), 32), ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(contentView, layoutParams);

        LinearLayout maleLL = (LinearLayout) contentView.findViewById(R.id.ll_male);
        LinearLayout femaleLL = (LinearLayout) contentView.findViewById(R.id.ll_female);

        maleLL.setOnClickListener(this);
        femaleLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_male:
                if (null != onClickListener) {
                    onClickListener.onMaleClick();
                }
                break;

            case R.id.ll_female:
                if (null != onClickListener) {
                    onClickListener.onFemaleClick();
                }
                break;
        }
    }

    public interface OnClickListener {

        void onMaleClick();

        void onFemaleClick();

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
