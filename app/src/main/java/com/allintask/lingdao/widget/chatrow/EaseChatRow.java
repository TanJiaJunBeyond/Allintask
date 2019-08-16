package com.allintask.lingdao.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.message.style.EaseMessageListItemStyle;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.domain.EaseAvatarOptions;
import com.allintask.lingdao.utils.EaseUI;
import com.allintask.lingdao.ui.adapter.message.EaseMessageAdapter;
import com.allintask.lingdao.utils.EaseUserUtils;
import com.allintask.lingdao.widget.EaseChatMessageList;
import com.allintask.lingdao.widget.EaseImageView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.util.DateUtils;

import java.util.Date;

/**
 * Created by TanJiaJun on 2018/2/28.
 */

public abstract class EaseChatRow extends LinearLayout {

    protected static final String TAG = EaseChatRow.class.getSimpleName();

    protected Activity activity;

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    @Nullable
    protected ImageView userAvatarView;
    @Nullable
    protected View bubbleLayout;
    @Nullable
    protected TextView usernickView;
    @Nullable
    protected TextView percentageView;
    @Nullable
    protected ProgressBar progressBar;
    @Nullable
    protected ImageView statusView;
    @Nullable
    protected TextView deliveredView;
    @Nullable
    protected TextView ackedView;

    protected EaseChatMessageList.MessageListItemClickListener itemClickListener;
    protected EaseMessageListItemStyle itemStyle;

    private EaseChatRowActionCallback itemActionCallback;

    public EaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);

        this.context = context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        this.activity = (Activity) context;
        inflater = LayoutInflater.from(context);

        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        itemActionCallback.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    public void updateView(final EMMessage msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onViewUpdate(msg);
            }
        });
    }

    private void initView() {
        onInflateView();
        timeStampView = (TextView) findViewById(R.id.timestamp);
        userAvatarView = (ImageView) findViewById(R.id.eiv_head_portrait);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
        deliveredView = (TextView) findViewById(R.id.tv_delivered);
        ackedView = (TextView) findViewById(R.id.tv_ack);

        onFindViewById();
    }

    /**
     * set property according message and postion
     *
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, int position, EaseChatMessageList.MessageListItemClickListener itemClickListener, EaseChatRowActionCallback itemActionCallback, EaseMessageListItemStyle itemStyle) {
        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;
        this.itemActionCallback = itemActionCallback;
        this.itemStyle = itemStyle;

        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

    private void setUpBaseView() {
        // set nickname, avatar and background of bubble
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // show time stamp if interval with last message is > 30 seconds
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }
        if (userAvatarView != null) {
            //set nickname and avatar
            if (message.direct() == Direct.SEND) {
                EaseUserUtils.setUserAvatar(context, EMClient.getInstance().getCurrentUser(), userAvatarView);
            } else {
                EaseUserUtils.setUserNick(message.getFrom(), usernickView);
                EaseUserUtils.setUserAvatar(context, message.getFrom(), userAvatarView);
            }
        }
        if (EMClient.getInstance().getOptions().getRequireDeliveryAck()) {
            if (deliveredView != null) {
                if (message.isDelivered()) {
                    deliveredView.setVisibility(View.VISIBLE);
                } else {
                    deliveredView.setVisibility(View.GONE);
                }
            }
        }
        if (EMClient.getInstance().getOptions().getRequireAck()) {
            if (ackedView != null) {
                if (message.isAcked()) {
                    if (null != deliveredView) {
                        deliveredView.setVisibility(View.GONE);
                    }

                    ackedView.setVisibility(View.VISIBLE);
                } else {
                    if (null != deliveredView) {
                        deliveredView.setVisibility(VISIBLE);
                    }

                    ackedView.setVisibility(View.GONE);
                }
            }
        }

        if (itemStyle != null) {
            if (userAvatarView != null) {
                if (itemStyle.isShowAvatar()) {
                    userAvatarView.setVisibility(View.VISIBLE);
                    EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
                    if (avatarOptions != null && userAvatarView instanceof EaseImageView) {
                        EaseImageView avatarView = ((EaseImageView) userAvatarView);
                        if (avatarOptions.getAvatarShape() != 0)
                            avatarView.setShapeType(avatarOptions.getAvatarShape());
                        if (avatarOptions.getAvatarBorderWidth() != 0)
                            avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
                        if (avatarOptions.getAvatarBorderColor() != 0)
                            avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
                        if (avatarOptions.getAvatarRadius() != 0)
                            avatarView.setRadius(avatarOptions.getAvatarRadius());
                    }
                } else {
                    userAvatarView.setVisibility(View.GONE);
                }
            }
            if (usernickView != null) {
                if (itemStyle.isShowUserNick())
                    usernickView.setVisibility(View.VISIBLE);
                else
                    usernickView.setVisibility(View.GONE);
            }
            if (bubbleLayout != null) {
                if (message.direct() == Direct.SEND) {
                    if (itemStyle.getMyBubbleBg() != null) {
                        bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getMyBubbleBg());
                    }
                } else if (message.direct() == Direct.RECEIVE) {
                    if (itemStyle.getOtherBubbleBg() != null) {
                        bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getOtherBubbleBg());
                    }
                }
            }
        }

    }

    private void setClickListener() {
        if (bubbleLayout != null) {
            bubbleLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null && itemClickListener.onBubbleClick(message)) {
                        return;
                    }
                    if (itemActionCallback != null) {
                        itemActionCallback.onBubbleClick(message);
                    }
                }
            });

            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemActionCallback != null) {
                        itemActionCallback.onResendClick(message);
                    }
                }
            });
        }

        if (userAvatarView != null) {
            userAvatarView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (message.direct() == Direct.SEND) {
                            itemClickListener.onUserAvatarClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarClick(message.getFrom());
                        }
                    }
                }
            });
            userAvatarView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        if (message.direct() == Direct.SEND) {
                            itemClickListener.onUserAvatarLongClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarLongClick(message.getFrom());
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    protected abstract void onInflateView();

    /**
     * find view by id
     */
    protected abstract void onFindViewById();

    /**
     * refresh view when message status change
     */
    protected abstract void onViewUpdate(EMMessage msg);

    /**
     * setup view
     */
    protected abstract void onSetUpView();

    public interface EaseChatRowActionCallback {

        void onResendClick(EMMessage message);

        void onBubbleClick(EMMessage message);

        void onDetachedFromWindow();

    }

}
