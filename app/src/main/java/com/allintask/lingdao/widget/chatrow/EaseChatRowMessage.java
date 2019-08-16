package com.allintask.lingdao.widget.chatrow;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.hyphenate.chat.EMMessage;

/**
 * Created by TanJiaJun on 2018/5/8.
 */

public class EaseChatRowMessage extends EaseChatRow {

    private TextView titleTv;
    private TextView contentTv;
    private TextView statusTv;

    public EaseChatRowMessage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.ease_row_message, this);
    }

    @Override
    protected void onFindViewById() {
        titleTv = (TextView) findViewById(R.id.tv_title);
        contentTv = (TextView) findViewById(R.id.tv_content);
        statusTv = (TextView) findViewById(R.id.tv_status);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {

    }

    @Override
    protected void onSetUpView() {
        EMMessage.Direct direct = message.direct();

        String title = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, "");

        String content = null;
        String status = null;

        if (direct == EMMessage.Direct.SEND) {
            content = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, "");
            status = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, "");
        } else if (direct == EMMessage.Direct.RECEIVE) {
            content = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, "");
            status = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, "");
        }

        titleTv.setText(title);
        contentTv.setText(content);
        statusTv.setText(status);
    }

}
