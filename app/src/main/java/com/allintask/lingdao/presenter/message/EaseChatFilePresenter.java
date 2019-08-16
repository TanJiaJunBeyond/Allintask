package com.allintask.lingdao.presenter.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.allintask.lingdao.ui.activity.message.EaseShowNormalFileActivity;
import com.allintask.lingdao.widget.chatrow.EaseChatRow;
import com.allintask.lingdao.widget.chatrow.EaseChatRowFile;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.FileUtils;

import java.io.File;

public class EaseChatFilePresenter extends EaseChatRowPresenter {

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new EaseChatRowFile(cxt, message, position, adapter);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        EMNormalFileMessageBody fileMessageBody = (EMNormalFileMessageBody) message.getBody();
        String filePath = fileMessageBody.getLocalUrl();
        File file = new File(filePath);
        if (file.exists()) {
            // open files if it exist
            FileUtils.openFile(file, (Activity) getContext());
        } else {
            // download the file
            getContext().startActivity(new Intent(getContext(), EaseShowNormalFileActivity.class).putExtra("msg", message));
        }
        if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
