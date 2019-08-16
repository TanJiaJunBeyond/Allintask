package com.allintask.lingdao.presenter.message;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.BaseAdapter;

import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.demand.EmployerDemandDetailsActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.user.EvaluateDetailsActivity;
import com.allintask.lingdao.widget.chatrow.EaseChatRow;
import com.allintask.lingdao.widget.chatrow.EaseChatRowMessage;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by TanJiaJun on 2018/5/8.
 */

public class EaseChatMessagePresenter extends EaseChatRowPresenter {

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new EaseChatRowMessage(cxt, message, position, adapter);
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
        super.handleReceiveMessage(message);

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
            int demandType = message.getIntAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, -1);
            String demandIdString = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, "");
            int serviceStatus = message.getIntAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, -1);
            String orderIdString = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_ORDER_ID, "");
            int userType = message.getIntAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, -1);

            int demandId = -1;

            if (!TextUtils.isEmpty(demandIdString)) {
                demandId = Integer.valueOf(demandIdString);
            }

            int orderId = -1;

            if (!TextUtils.isEmpty(orderIdString)) {
                orderId = Integer.valueOf(orderIdString);
            }

            if (direct == EMMessage.Direct.SEND) {
                if (userType == CommonConstant.MESSAGE_STATUS_USER_TYPE_FACILITATOR) {
                    if (demandType == CommonConstant.MESSAGE_STATUS_EVALUATED_EMPLOYER) {
                        if (orderId != -1) {
                            Intent evaluateDetailsIntent = new Intent(getContext(), EvaluateDetailsActivity.class);
                            evaluateDetailsIntent.putExtra(CommonConstant.EXTRA_EVALUATE_DETAILS_TYPE, CommonConstant.EVALUATE_DETAILS_TYPE_EMPLOYER);
                            evaluateDetailsIntent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                            getContext().startActivity(evaluateDetailsIntent);
                        }
                    } else {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.SERVICE_MANAGEMENT_FRAGMENT);
                        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, serviceStatus);
                        getContext().startActivity(intent);
                    }
                } else if (userType == CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER) {
                    if (demandType == CommonConstant.MESSAGE_STATUS_EVALUATED_FACILITATOR) {
                        if (orderId != -1) {
                            Intent evaluateDetailsIntent = new Intent(getContext(), EvaluateDetailsActivity.class);
                            evaluateDetailsIntent.putExtra(CommonConstant.EXTRA_EVALUATE_DETAILS_TYPE, CommonConstant.EVALUATE_DETAILS_TYPE_SERVICE);
                            evaluateDetailsIntent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                            getContext().startActivity(evaluateDetailsIntent);
                        }
                    } else if (demandType == CommonConstant.MESSAGE_STATUS_BID_SUCCESS || demandType == CommonConstant.MESSAGE_STATUS_MODIFY_PRICE) {
                        Intent intent = new Intent(getContext(), EmployerDemandDetailsActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                        intent.putExtra(CommonConstant.EXTRA_TYPE, CommonConstant.SELECT_MORE_BIDDING);
                        getContext().startActivity(intent);
                    } else if (demandType == CommonConstant.MESSAGE_STATUS_EMPLOY_SUCCESS) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.DEMAND_MANAGEMENT_FRAGMENT);
                        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_UNDERWAY);
                        getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), EmployerDemandDetailsActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                        getContext().startActivity(intent);
                    }
                }
            } else if (direct == EMMessage.Direct.RECEIVE) {
                if (userType == CommonConstant.MESSAGE_STATUS_USER_TYPE_FACILITATOR) {
                    if (demandType == CommonConstant.MESSAGE_STATUS_EVALUATED_EMPLOYER) {
                        if (orderId != -1) {
                            Intent evaluateDetailsIntent = new Intent(getContext(), EvaluateDetailsActivity.class);
                            evaluateDetailsIntent.putExtra(CommonConstant.EXTRA_EVALUATE_DETAILS_TYPE, CommonConstant.EVALUATE_DETAILS_TYPE_SERVICE);
                            evaluateDetailsIntent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                            getContext().startActivity(evaluateDetailsIntent);
                        }
                    } else if (demandType == CommonConstant.MESSAGE_STATUS_BID_SUCCESS || demandType == CommonConstant.MESSAGE_STATUS_MODIFY_PRICE) {
                        Intent intent = new Intent(getContext(), EmployerDemandDetailsActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                        intent.putExtra(CommonConstant.EXTRA_TYPE, CommonConstant.SELECT_MORE_BIDDING);
                        getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), EmployerDemandDetailsActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                        getContext().startActivity(intent);
                    }
                } else if (userType == CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER) {
                    if (demandType == CommonConstant.MESSAGE_STATUS_EVALUATED_FACILITATOR) {
                        if (orderId != -1) {
                            Intent evaluateDetailsIntent = new Intent(getContext(), EvaluateDetailsActivity.class);
                            evaluateDetailsIntent.putExtra(CommonConstant.EXTRA_EVALUATE_DETAILS_TYPE, CommonConstant.EVALUATE_DETAILS_TYPE_EMPLOYER);
                            evaluateDetailsIntent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                            getContext().startActivity(evaluateDetailsIntent);
                        }
                    } else {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.SERVICE_MANAGEMENT_FRAGMENT);
                        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, serviceStatus);
                        getContext().startActivity(intent);
                    }
                }
            }
        }
    }

}
