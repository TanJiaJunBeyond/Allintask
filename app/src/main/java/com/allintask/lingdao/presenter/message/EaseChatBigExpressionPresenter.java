package com.allintask.lingdao.presenter.message;

import android.content.Context;
import android.widget.BaseAdapter;

import com.allintask.lingdao.widget.chatrow.EaseChatRow;
import com.allintask.lingdao.widget.chatrow.EaseChatRowBigExpression;
import com.hyphenate.chat.EMMessage;

public class EaseChatBigExpressionPresenter extends EaseChatTextPresenter {
    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new EaseChatRowBigExpression(cxt, message, position, adapter);
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
    }
}
