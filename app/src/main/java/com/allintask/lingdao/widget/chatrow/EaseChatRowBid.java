package com.allintask.lingdao.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.allintask.lingdao.R;
import com.allintask.lingdao.utils.EaseSmileUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

public class EaseChatRowBid extends EaseChatRow {

    private TextView messageTv;

    public EaseChatRowBid(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_bid : R.layout.ease_row_sent_bid, this);
    }

    @Override
    protected void onFindViewById() {
        messageTv = (TextView) findViewById(R.id.tv_message);
    }

    @Override
    public void onSetUpView() {
        EMTextMessageBody emTextMessageBody = (EMTextMessageBody) message.getBody();
        Spannable spannable = EaseSmileUtils.getSmiledText(context, emTextMessageBody.getMessage());
        messageTv.setText(spannable, BufferType.SPANNABLE);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        switch (msg.status()) {
            case CREATE:
                onMessageCreate();
                break;
            case SUCCESS:
                onMessageSuccess();
                break;
            case FAIL:
                onMessageError();
                break;
            case INPROGRESS:
                onMessageInProgress();
                break;
        }
    }

    private void onMessageCreate() {
        deliveredView.setVisibility(GONE);
        ackedView.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }

    private void onMessageSuccess() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.GONE);
    }

    private void onMessageError() {
        deliveredView.setVisibility(GONE);
        ackedView.setVisibility(GONE);
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.VISIBLE);
    }

    private void onMessageInProgress() {
        deliveredView.setVisibility(GONE);
        ackedView.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }

}
