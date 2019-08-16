package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allintask.lingdao.R;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.utils.WindowUtils;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2018/3/5.
 */

public class BidToMakeMoneyAtOnceDialog extends Dialog implements View.OnClickListener {

    private String advantage;

    private EditText myOfferEt;
    private EditText myAdvantageEt;

    private OnClickListener onClickListener;

    public BidToMakeMoneyAtOnceDialog(@NonNull Context context, String advantage) {
        super(context, R.style.BidToMakeMoneyAtOnceDialogStyle);
        this.advantage = advantage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bid_to_make_money_at_once, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()) - DensityUtils.dip2px(getContext(), 32), ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(contentView, layoutParams);

        ImageView closeIv = contentView.findViewById(R.id.iv_close);
        myOfferEt = contentView.findViewById(R.id.et_my_offer);
        myAdvantageEt = contentView.findViewById(R.id.et_my_advantage);
        final TextView numberOfWordsTv = contentView.findViewById(R.id.tv_number_of_words);
        Button bidAtOnceBtn = contentView.findViewById(R.id.btn_bid_at_once);

        myOfferEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String myOffer = myOfferEt.getText().toString().trim();

                if (!TextUtils.isEmpty(myOffer) && myOffer.equals("0")) {
                    myOfferEt.setText("");
                }
            }
        });

        myAdvantageEt.setText(advantage);

        if (!TextUtils.isEmpty(advantage)) {
            int myAdvantageLength = advantage.length();
            numberOfWordsTv.setText(String.valueOf(myAdvantageLength) + "/100");
        }

        myAdvantageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String myAdvantage = myAdvantageEt.getText().toString().trim();

                if (!TextUtils.isEmpty(myAdvantage)) {
                    int index = myAdvantageEt.getSelectionStart() - 1;

                    if (index >= 0) {
                        if (EmojiUtils.noEmoji(myAdvantage)) {
                            Editable editable = myAdvantageEt.getText();
                            editable.delete(index, index + 1);
                        }
                    }

                    int myAdvantageLength = myAdvantage.length();
                    numberOfWordsTv.setText(String.valueOf(myAdvantageLength) + "/100");
                }
            }
        });

        closeIv.setOnClickListener(this);
        bidAtOnceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (null != onClickListener) {
                    onClickListener.onCloseClick();
                }
                break;

            case R.id.btn_bid_at_once:
                String myOffer = myOfferEt.getText().toString().trim();
                String myAdvantage = myAdvantageEt.getText().toString().trim();

                if (!TextUtils.isEmpty(myOffer)) {
                    if (!TextUtils.isEmpty(myAdvantage)) {
                        if (null != onClickListener) {
                            onClickListener.onBidAtOnceClick(myOffer, myAdvantage);
                        }
                    } else {
                        Toast.makeText(getContext(), "我的优势不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "我的报价不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public interface OnClickListener {

        void onCloseClick();

        void onBidAtOnceClick(String myOffer, String myAdvantage);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
