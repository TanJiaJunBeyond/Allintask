package com.allintask.lingdao.presenter.message;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.BaseAdapter;

import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.recommend.RecommendDetailsActivity;
import com.allintask.lingdao.widget.chatrow.EaseChatRow;
import com.allintask.lingdao.widget.chatrow.EaseChatRowService;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

public class EaseChatServicePresenter extends EaseChatRowPresenter {

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new EaseChatRowService(cxt, message, position, adapter);
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
        if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        super.onBubbleClick(message);
        EMMessage.Direct direct = message.direct();
        Integer userIdInteger = Integer.valueOf(message.getTo());

        int userId;

        if (null != userIdInteger) {
            userId = userIdInteger;
        } else {
            userId = -1;
        }

        String serviceIdString = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_ID, "");

        int serviceId = -1;

        if (!TextUtils.isEmpty(serviceIdString)) {
            serviceId = Integer.valueOf(serviceIdString);
        }

        switch (direct) {
            case SEND:
                if (userId != -1 && serviceId != -1) {
                    Intent sendIntent = new Intent(getContext(), RecommendDetailsActivity.class);
                    sendIntent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                    sendIntent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                    getContext().startActivity(sendIntent);
                }
                break;

            case RECEIVE:
                if (userId != -1 && serviceId != -1) {
                    Intent receiveIntent = new Intent(getContext(), RecommendDetailsActivity.class);
                    receiveIntent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                    receiveIntent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                    getContext().startActivity(receiveIntent);
                }
                break;
        }
    }

}
