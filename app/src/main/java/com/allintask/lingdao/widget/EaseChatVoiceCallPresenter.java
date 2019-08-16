package com.allintask.lingdao.widget;

import android.content.Context;
import android.widget.BaseAdapter;

import com.allintask.lingdao.widget.chatrow.EaseChatRow;
import com.allintask.lingdao.presenter.message.EaseChatRowPresenter;
import com.hyphenate.chat.EMMessage;

/**
 * Created by zhangsong on 17-10-12.
 */

public class EaseChatVoiceCallPresenter extends EaseChatRowPresenter {
    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new ChatRowVoiceCall(cxt, message, position, adapter);
    }
}
