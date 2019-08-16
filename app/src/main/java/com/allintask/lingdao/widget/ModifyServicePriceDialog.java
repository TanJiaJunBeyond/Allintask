package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.utils.WindowUtils;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2018/3/6.
 */

public class ModifyServicePriceDialog extends Dialog implements View.OnClickListener {

    private String originalPrice;

    private EditText modifyPriceEt;
    private OnClickListener onClickListener;

    public ModifyServicePriceDialog(@NonNull Context context, String originalPrice) {
        super(context, R.style.ModifyServicePriceDialogStyle);
        this.originalPrice = originalPrice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_modify_service_price, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()) - DensityUtils.dip2px(getContext(), 32), ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(contentView, layoutParams);

        ImageView closeIv = (ImageView) contentView.findViewById(R.id.iv_close);
        TextView originalPriceTv = (TextView) contentView.findViewById(R.id.tv_original_price);
        modifyPriceEt = (EditText) contentView.findViewById(R.id.et_modify_price);
        Button confirmModifyBtn = (Button) contentView.findViewById(R.id.btn_confirm_modify);

        originalPriceTv.setText(originalPrice);

        closeIv.setOnClickListener(this);
        confirmModifyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (isShowing()) {
                    dismiss();
                }
                break;

            case R.id.btn_confirm_modify:
                if (null != onClickListener) {
                    String modifyPrice = modifyPriceEt.getText().toString().trim();
                    onClickListener.onConfirmModifyClick(modifyPrice);
                }
                break;
        }
    }

    public interface OnClickListener {

        void onConfirmModifyClick(String modifyPrice);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
