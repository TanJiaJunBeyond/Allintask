package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.utils.WindowUtils;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public class SeeExampleDialog extends Dialog implements View.OnClickListener {

    private String content;

    public SeeExampleDialog(@NonNull Context context, String content) {
        super(context, R.style.SeeExampleDialogStyle);
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = View.inflate(getContext(), R.layout.dialog_see_example, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()) - DensityUtils.dip2px(getContext(), 32), ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(contentView, layoutParams);

        ImageView closeIv = contentView.findViewById(R.id.iv_close);
        final TextView contentTv = contentView.findViewById(R.id.tv_content);
        Button confirmBtn = contentView.findViewById(R.id.btn_confirm);

        content = content.replace(CommonConstant.NEWLINE, "\n");
        contentTv.setText(content);

        closeIv.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        contentTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String content = contentTv.getText().toString().trim();

                if (!TextUtils.isEmpty(content)) {
                    ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(content);
                    Toast.makeText(getContext(), "已复制到剪切板", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (isShowing()) {
                    dismiss();
                }
                break;

            case R.id.btn_confirm:
                if (isShowing()) {
                    dismiss();
                }
                break;
        }
    }

}
