package com.allintask.lingdao.presenter.message;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.BaseAdapter;

import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.demand.EmployerDemandDetailsActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.widget.chatrow.EaseChatRow;
import com.allintask.lingdao.widget.chatrow.EaseChatRowBid;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

public class EaseChatBidPresenter extends EaseChatRowPresenter {

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new EaseChatRowBid(cxt, message, position, adapter);
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

        if (null != message) {
            EMMessage.Direct direct = message.direct();
            String demandIdString = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, "");

            int demandId = -1;

            if (!TextUtils.isEmpty(demandIdString)) {
                demandId = Integer.valueOf(demandIdString);
            }

            switch (direct) {
                case RECEIVE:
                    Intent mainIntent = new Intent(getContext(), MainActivity.class);
                    mainIntent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.SERVICE_MANAGEMENT_FRAGMENT);
                    mainIntent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.SERVICE_STATUS_WAIT_BID);
                    getContext().startActivity(mainIntent);
                    break;

                case SEND:
                    if (demandId != -1) {
                        Intent employerDemandDetailsIntent = new Intent(getContext(), EmployerDemandDetailsActivity.class);
                        employerDemandDetailsIntent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                        getContext().startActivity(employerDemandDetailsIntent);
                    }
                    break;
            }
        }
    }

}
