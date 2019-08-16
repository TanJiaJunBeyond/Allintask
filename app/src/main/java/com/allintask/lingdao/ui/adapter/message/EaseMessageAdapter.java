/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.allintask.lingdao.ui.adapter.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.message.style.EaseMessageListItemStyle;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.message.EaseChatBidPresenter;
import com.allintask.lingdao.presenter.message.EaseChatBigExpressionPresenter;
import com.allintask.lingdao.presenter.message.EaseChatMessagePresenter;
import com.allintask.lingdao.presenter.message.EaseChatServicePresenter;
import com.allintask.lingdao.utils.EaseCommonUtils;
import com.allintask.lingdao.widget.EaseChatMessageList;
import com.allintask.lingdao.widget.chatrow.EaseCustomChatRowProvider;
import com.allintask.lingdao.presenter.message.EaseChatFilePresenter;
import com.allintask.lingdao.presenter.message.EaseChatImagePresenter;
import com.allintask.lingdao.presenter.message.EaseChatLocationPresenter;
import com.allintask.lingdao.presenter.message.EaseChatRowPresenter;
import com.allintask.lingdao.presenter.message.EaseChatTextPresenter;
import com.allintask.lingdao.presenter.message.EaseChatVideoPresenter;
import com.allintask.lingdao.presenter.message.EaseChatVoicePresenter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/2/28.
 */

public class EaseMessageAdapter extends BaseAdapter {

    private final static String TAG = "msg";

    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_EXPRESSION = 12;
    private static final int MESSAGE_TYPE_RECV_EXPRESSION = 13;
    private static final int MESSAGE_TYPE_SERVICE = 14;
    private static final int MESSAGE_TYPE_SENT_BID = 15;
    private static final int MESSAGE_TYPE_RECV_BID = 16;
    private static final int MESSAGE_TYPE_MESSAGE = 17;

    private Context context;

    public int itemTypeCount;

    // reference to conversation object in chatsdk
    private EMConversation conversation;
    EMMessage[] messages = null;

    private String toChatUsername;

    private EaseChatMessageList.MessageListItemClickListener itemClickListener;
    private EaseCustomChatRowProvider customRowProvider;

    private boolean showUserNick;
    private boolean showAvatar;
    private Drawable myBubbleBg;
    private Drawable otherBuddleBg;

    private ListView listView;
    private EaseMessageListItemStyle itemStyle;

    public EaseMessageAdapter(Context context, String username, int chatType, ListView listView) {
        this.context = context;
        this.listView = listView;
        toChatUsername = username;
        this.conversation = EMClient.getInstance().chatManager().getConversation(username, EaseCommonUtils.getConversationType(chatType), true);
    }

    Handler handler = new Handler() {
        private void refreshList() {
            // you should not call getAllMessages() in UI thread
            // otherwise there is problem when refreshing UI and there is new message arrive
            java.util.List<EMMessage> var = conversation.getAllMessages();
            Log.i("TanJiaJun", JSONArray.toJSONString(var));
            messages = var.toArray(new EMMessage[var.size()]);
            conversation.markAllMessagesAsRead();
            notifyDataSetChanged();
        }

        @Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case HANDLER_MESSAGE_REFRESH_LIST:
                    refreshList();
                    break;
                case HANDLER_MESSAGE_SELECT_LAST:
                    if (messages != null && messages.length > 0) {
                        listView.setSelection(messages.length - 1);
                    }
                    break;
                case HANDLER_MESSAGE_SEEK_TO:
                    int position = message.arg1;
                    listView.setSelection(position);
                    break;
                default:
                    break;
            }
        }
    };

    public void refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return;
        }
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    /**
     * refresh and select the last
     */
    public void refreshSelectLast() {
        final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
        handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
        handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);
    }

    /**
     * refresh and seek to the position
     */
    public void refreshSeekTo(int position) {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
    }

    public EMMessage getItem(int position) {
        if (messages != null && position < messages.length) {
            return messages[position];
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * get count of messages
     */
    public int getCount() {
        return messages == null ? 0 : messages.length;
    }

    /**
     * get number of message type, here 14 = (EMMessage.Type) * 2
     */
    public int getViewTypeCount() {
        return 18;
    }


    /**
     * get type of item
     */
    public int getItemViewType(int position) {
        EMMessage message = getItem(position);

        if (message == null) {
            return -1;
        }

        if (message.getType() == EMMessage.Type.TXT) {
            String type = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, "");

            if (type.equals(CommonConstant.MESSAGE_ATTRIBUTE_TYPE_SERVICE)) {
                return MESSAGE_TYPE_SERVICE;
            } else if (type.equals(CommonConstant.MESSAGE_ATTRIBUTE_TYPE_BID)) {
                return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_BID : MESSAGE_TYPE_SENT_BID;
            } else if (type.equals(CommonConstant.MESSAGE_ATTRIBUTE_TYPE_MESSAGE)) {
                return MESSAGE_TYPE_MESSAGE;
            } else if (message.getBooleanAttribute(CommonConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_EXPRESSION : MESSAGE_TYPE_SENT_EXPRESSION;
            } else {
                return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
            }
        }

        if (message.getType() == EMMessage.Type.IMAGE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;
        }

        if (message.getType() == EMMessage.Type.LOCATION) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
        }

        if (message.getType() == EMMessage.Type.VOICE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
        }

        if (message.getType() == EMMessage.Type.VIDEO) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
        }

        if (message.getType() == EMMessage.Type.FILE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
        }
        return -1;
    }

    protected EaseChatRowPresenter createChatRowPresenter(EMMessage message, int position) {
        if (customRowProvider != null && customRowProvider.getCustomChatRow(message, position, this) != null) {
            return customRowProvider.getCustomChatRow(message, position, this);
        }

        EaseChatRowPresenter presenter = null;

        switch (message.getType()) {
            case TXT:
                String type = TypeUtils.getString(message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, ""));

                if (type.equals(CommonConstant.MESSAGE_ATTRIBUTE_TYPE_SERVICE)) {
                    presenter = new EaseChatServicePresenter();
                } else if (type.equals(CommonConstant.MESSAGE_ATTRIBUTE_TYPE_BID)) {
                    presenter = new EaseChatBidPresenter();
                } else if (type.equals(CommonConstant.MESSAGE_ATTRIBUTE_TYPE_MESSAGE)) {
                    presenter = new EaseChatMessagePresenter();
                } else if (message.getBooleanAttribute(CommonConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    presenter = new EaseChatBigExpressionPresenter();
                } else {
                    presenter = new EaseChatTextPresenter();
                }
                break;

            case LOCATION:
                presenter = new EaseChatLocationPresenter();
                break;

            case FILE:
                presenter = new EaseChatFilePresenter();
                break;

            case IMAGE:
                presenter = new EaseChatImagePresenter();
                break;

            case VOICE:
                presenter = new EaseChatVoicePresenter();
                break;

            case VIDEO:
                presenter = new EaseChatVideoPresenter();
                break;
        }
        return presenter;
    }


    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        EMMessage message = getItem(position);

        EaseChatRowPresenter presenter;

        if (convertView == null) {
            presenter = createChatRowPresenter(message, position);
            convertView = presenter.createChatRow(context, message, position, this);
            convertView.setTag(presenter);
        } else {
            presenter = (EaseChatRowPresenter) convertView.getTag();
        }

        presenter.setup(message, position, itemClickListener, itemStyle);
        return convertView;
    }


    public void setItemStyle(EaseMessageListItemStyle itemStyle) {
        this.itemStyle = itemStyle;
    }

    public void setItemClickListener(EaseChatMessageList.MessageListItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider) {
        customRowProvider = rowProvider;
    }

    public boolean isShowUserNick() {
        return showUserNick;
    }

    public boolean isShowAvatar() {
        return showAvatar;
    }

    public Drawable getMyBubbleBg() {
        return myBubbleBg;
    }

    public Drawable getOtherBubbleBg() {
        return otherBuddleBg;
    }

}
