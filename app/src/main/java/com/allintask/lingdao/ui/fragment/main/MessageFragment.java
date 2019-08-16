package com.allintask.lingdao.ui.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.message.UserInfoListRequestBean;
import com.allintask.lingdao.bean.message.UserInfoResponseBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.domain.EaseUser;
import com.allintask.lingdao.helper.EMChatHelper;
import com.allintask.lingdao.presenter.main.MessagePresenter;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.utils.EaseUserUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.main.IMessageView;
import com.allintask.lingdao.widget.EaseConversationList;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/9.
 */

public class MessageFragment extends BaseFragment<IMessageView, MessagePresenter> implements IMessageView {

    private final static int MSG_REFRESH = 2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.ease_conversation_list)
    EaseConversationList easeConversationList;
    @BindView(R.id.rl_status)
    RelativeLayout statusRL;

    protected InputMethodManager inputMethodManager;

    protected List<EMConversation> conversationList = new ArrayList<>();
    protected boolean isConflict;

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    easeConversationList.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_message;
    }

    @Override
    protected MessagePresenter CreatePresenter() {
        return new MessagePresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Window window = ((MainActivity) getParentContext()).getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(getResources().getColor(R.color.white));

            ((MainActivity) getParentContext()).MIUISetStatusBarLightMode(((MainActivity) getParentContext()).getWindow(), true);
            ((MainActivity) getParentContext()).FlymeSetStatusBarLightMode(((MainActivity) getParentContext()).getWindow(), true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getParentContext().getResources().getColor(R.color.white));
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.message_notification));

        ((MainActivity) getParentContext()).setSupportActionBar(toolbar);
    }

    private void initUI() {
        initEaseConversationList();
    }

    private void initData() {
        EMClient.getInstance().addConnectionListener(connectionListener);
    }

    private void initEaseConversationList() {
        conversationList.addAll(loadConversationList());
        easeConversationList.init(conversationList);
        easeConversationList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

        easeConversationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation emConversation = easeConversationList.getItem(position);

                if (null != emConversation) {
                    String userId = emConversation.conversationId();
                    Intent intent = new Intent(getParentContext(), ChatActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                    getParentContext().startActivity(intent);
                }
            }
        });
    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
                    || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };

    /**
     * connected to server
     */
    protected void onConnectionConnected() {
        statusRL.setVisibility(View.GONE);
    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected() {
        statusRL.setVisibility(View.VISIBLE);
    }


    /**
     * refresh ui
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * load conversation list 会话列表加载
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onShowUserInfoList(List<UserInfoResponseBean> userInfoList) {
        if (null != userInfoList && userInfoList.size() > 0) {
            for (int i = 0; i < userInfoList.size(); i++) {
                UserInfoResponseBean userInfoResponseBean = userInfoList.get(i);

                if (null != userInfoResponseBean) {
                    int userId = TypeUtils.getInteger(userInfoResponseBean.userId, -1);
                    String name = TypeUtils.getString(userInfoResponseBean.name, "");
                    String avatarUrl = TypeUtils.getString(userInfoResponseBean.avatarUrl, "");

                    if (userId != -1) {
                        EaseUser easeUser = new EaseUser(String.valueOf(userId));
                        easeUser.setNickname(name);

                        String headPortraitUrl = null;

                        if (!TextUtils.isEmpty(avatarUrl)) {
                            headPortraitUrl = "https:" + avatarUrl;
                        }

                        easeUser.setAvatar(headPortraitUrl);

                        EMChatHelper.getInstance().saveContact(easeUser);
                        EMChatHelper.getInstance().getModel().setContactSynced(true);
                        EMChatHelper.getInstance().notifyContactsSyncListener(true);

                        refresh();
                    }
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            Window window = ((MainActivity) getParentContext()).getWindow();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(getResources().getColor(R.color.white));

                ((MainActivity) getParentContext()).MIUISetStatusBarLightMode(((MainActivity) getParentContext()).getWindow(), true);
                ((MainActivity) getParentContext()).FlymeSetStatusBarLightMode(((MainActivity) getParentContext()).getWindow(), true);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.white));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            int myUserId = UserPreferences.getInstance().getUserId();
            List<Integer> userIdList = new ArrayList<>();

            for (int i = 0; i < conversationList.size(); i++) {
                EMConversation emConversation = conversationList.get(i);

                if (null != emConversation) {
                    List<EMMessage> emMessageList = emConversation.getAllMessages();

                    if (null != emMessageList && emMessageList.size() > 0) {
                        for (int j = 0; j < emMessageList.size(); j++) {
                            EMMessage emMessage = emMessageList.get(j);

                            if (null != emMessage) {
                                String userId = emMessage.getTo();

                                if (!TextUtils.isEmpty(userId)) {
                                    int sendUserId = Integer.valueOf(userId);

                                    if (sendUserId != myUserId) {
                                        EaseUser easeUser = EaseUserUtils.getUserInfo(userId);

                                        if (null != easeUser) {
                                            String nickname = TypeUtils.getString(easeUser.getNickname(), "");
                                            String headPortraitUrl = TypeUtils.getString(easeUser.getAvatar(), "");

//                                            if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(headPortraitUrl)) {
                                            userIdList.add(sendUserId);
//                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (userIdList.size() > 0) {
                UserInfoListRequestBean userInfoListRequestBean = new UserInfoListRequestBean();
                userInfoListRequestBean.userIds = userIdList;
                mPresenter.fetchUserInfoListRequest(userInfoListRequestBean);
            } else {
                refresh();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (null != savedInstanceState && savedInstanceState.getBoolean("isConflict", false)) {
            return;
        }

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        int myUserId = UserPreferences.getInstance().getUserId();
        List<Integer> userIdList = new ArrayList<>();

        for (int i = 0; i < conversationList.size(); i++) {
            EMConversation emConversation = conversationList.get(i);

            if (null != emConversation) {
                List<EMMessage> emMessageList = emConversation.getAllMessages();

                if (null != emMessageList && emMessageList.size() > 0) {
                    for (int j = 0; j < emMessageList.size(); j++) {
                        EMMessage emMessage = emMessageList.get(j);

                        if (null != emMessage) {
                            String userId = emMessage.getTo();

                            if (!TextUtils.isEmpty(userId)) {
                                int sendUserId = Integer.valueOf(userId);

                                if (sendUserId != myUserId) {
                                    EaseUser easeUser = EaseUserUtils.getUserInfo(userId);

                                    if (null != easeUser) {
                                        String nickname = TypeUtils.getString(easeUser.getNickname(), "");
                                        String headPortraitUrl = TypeUtils.getString(easeUser.getAvatar(), "");

//                                        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(headPortraitUrl)) {
                                        userIdList.add(sendUserId);
//                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (userIdList.size() > 0) {
            UserInfoListRequestBean userInfoListRequestBean = new UserInfoListRequestBean();
            userInfoListRequestBean.userIds = userIdList;
            mPresenter.fetchUserInfoListRequest(userInfoListRequestBean);
        } else {
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        if (null != connectionListener) {
            EMClient.getInstance().removeConnectionListener(connectionListener);
        }

        super.onDestroy();
    }

}
