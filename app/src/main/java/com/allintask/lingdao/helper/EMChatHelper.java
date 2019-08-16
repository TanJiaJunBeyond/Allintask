package com.allintask.lingdao.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.message.EaseAtMessageHelper;
import com.allintask.lingdao.bean.message.EaseNotifier;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.domain.EaseAvatarOptions;
import com.allintask.lingdao.domain.EaseEmojicon;
import com.allintask.lingdao.domain.EaseUser;
import com.allintask.lingdao.utils.EaseSmileUtils;
import com.allintask.lingdao.utils.EaseUI;
import com.allintask.lingdao.model.message.EMChatModel;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;

import com.allintask.lingdao.utils.EaseCommonUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessage.Status;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.allintask.lingdao.database.EMChatDatabaseManager;
import com.allintask.lingdao.database.InviteMessgeDao;
import com.allintask.lingdao.database.UserDao;
import com.allintask.lingdao.domain.InviteMessage;
import com.allintask.lingdao.domain.InviteMessage.InviteMessageStatus;
import com.allintask.lingdao.domain.RobotUser;
import com.allintask.lingdao.receiver.CallReceiver;

import com.allintask.lingdao.ui.activity.message.VideoCallActivity;
import com.allintask.lingdao.ui.activity.message.VoiceCallActivity;
import com.allintask.lingdao.utils.PreferenceManager;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EMChatHelper {
    /**
     * data sync listener
     */
    public interface DataSyncListener {
        /**
         * sync complete
         *
         * @param success true：data sync successful，false: failed to sync data
         */
        void onSyncComplete(boolean success);
    }

    protected static final String TAG = "EMChatHelper";

    private EaseUI easeUI;

    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private Map<String, EaseUser> contactList;

    private Map<String, RobotUser> robotList;

    private static EMChatHelper instance = null;

    private com.allintask.lingdao.model.message.EMChatModel EMChatModel = null;

    /**
     * sync groups status listener
     */
    private List<DataSyncListener> syncGroupsListeners;
    /**
     * sync contacts status listener
     */
    private List<DataSyncListener> syncContactsListeners;
    /**
     * sync blacklist status listener
     */
    private List<DataSyncListener> syncBlackListListeners;


    private boolean isSyncingGroupsWithServer = false;
    private boolean isSyncingContactsWithServer = false;
    private boolean isSyncingBlackListWithServer = false;
    private boolean isGroupsSyncedWithServer = false;
    private boolean isContactsSyncedWithServer = false;
    private boolean isBlackListSyncedWithServer = false;

    public boolean isVoiceCalling;
    public boolean isVideoCalling;

    private String username;

    private Context appContext;

    private CallReceiver callReceiver;

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    private LocalBroadcastManager localBroadcastManager;

    private boolean isGroupAndContactListenerRegisted;

    private ExecutorService executor;

    protected android.os.Handler handler;

    Queue<String> msgQueue = new ConcurrentLinkedQueue<>();

    private EMChatHelper() {
        executor = Executors.newCachedThreadPool();
    }

    public synchronized static EMChatHelper getInstance() {
        if (instance == null) {
            instance = new EMChatHelper();
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context) {
        EMChatModel = new EMChatModel(context);
        EMOptions options = initChatOptions();
//        options.setRestServer("118.193.28.212:31080");
//        options.setIMServer("118.193.28.212");
//        options.setImPort(31097);

        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;

            //debug mode, you'd better set it to false, if you want release your App officially.
            EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //to set user's profile and avatar
            setEaseUIProviders();
            //initialize preference manager
            PreferenceManager.init(context);
            //initialize profile manager
//			getUserProfileManager().init(context);
            //set Call options
            setCallOptions();

            // TODO: set Call options
            // min video kbps
            int minBitRate = PreferenceManager.getInstance().getCallMinVideoKbps();
            if (minBitRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(minBitRate);
            }

            // max video kbps
            int maxBitRate = PreferenceManager.getInstance().getCallMaxVideoKbps();
            if (maxBitRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(maxBitRate);
            }

            // max frame rate
            int maxFrameRate = PreferenceManager.getInstance().getCallMaxFrameRate();
            if (maxFrameRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(maxFrameRate);
            }

            // audio sample rate
            int audioSampleRate = PreferenceManager.getInstance().getCallAudioSampleRate();
            if (audioSampleRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(audioSampleRate);
            }

            /**
             * This function is only meaningful when your app need recording
             * If not, remove it.
             * This function need be called before the video stream started, so we set it in onCreate function.
             * This method will set the preferred video record encoding codec.
             * Using default encoding format, recorded file may not be played by mobile player.
             */
            //EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);

            // resolution
            String resolution = PreferenceManager.getInstance().getCallBackCameraResolution();
            if (resolution.equals("")) {
                resolution = PreferenceManager.getInstance().getCallFrontCameraResolution();
            }
            String[] wh = resolution.split("x");
            if (wh.length == 2) {
                try {
                    EMClient.getInstance().callManager().getCallOptions().setVideoResolution(new Integer(wh[0]).intValue(), new Integer(wh[1]).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // enabled fixed sample rate
            boolean enableFixSampleRate = PreferenceManager.getInstance().isCallFixedVideoResolution();
            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(enableFixSampleRate);

            // Offline call push
            EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(getModel().isPushCall());

            setGlobalListeners();
            localBroadcastManager = LocalBroadcastManager.getInstance(appContext);
            initDbDao();
        }
    }


    private EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();
        options.setAppKey(CommonConstant.EMCHAT_APP_KEY);

        // set if accept the invitation automatically 是否自动加好友 true
        options.setAcceptInvitationAlways(true);

        // set if you need read ack  是否要求消息的接收方发送已读回执
        options.setRequireAck(true);

        // set if you need delivery ack 是否需要送达回执
        options.setRequireDeliveryAck(false);

        /**
         * NOTE:你需要设置自己申请的Sender ID来使用Google推送功能，详见集成文档
         */
//        options.setFCMNumber("921300338324");
        //you need apply & set your own id if you want to use Mi push notification
        //设置用于小米推送的appid和appkey
        //options.setMipushConfig("2882303761517426801", "5381742660801");

        //set custom servers, commonly used in private deployment
        if (EMChatModel.isCustomServerEnable() && EMChatModel.getRestServer() != null && EMChatModel.getIMServer() != null) {
            options.setRestServer(EMChatModel.getRestServer());
            options.setIMServer(EMChatModel.getIMServer());
            if (EMChatModel.getIMServer().contains(":")) {
                options.setIMServer(EMChatModel.getIMServer().split(":")[0]);
                options.setImPort(Integer.valueOf(EMChatModel.getIMServer().split(":")[1]));
            }
        }

        //设置是否允许聊天室owner离开并删除会话记录，意味着owner再不会受到任何消息
        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
        //设置退出(主动和被动退出)群组时是否删除聊天消息
        options.setDeleteMessagesAsExitGroup(getModel().isDeleteMessagesAsExitGroup());
        //获取是否自动接受加群邀请
        options.setAutoAcceptGroupInvitation(getModel().isAutoAcceptGroupInvitation());

        return options;
    }

    private void setCallOptions() {
        // min video kbps
        int minBitRate = PreferenceManager.getInstance().getCallMinVideoKbps();
        if (minBitRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(minBitRate);
        }

        // max video kbps
        int maxBitRate = PreferenceManager.getInstance().getCallMaxVideoKbps();
        if (maxBitRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(maxBitRate);
        }

        // max frame rate
        int maxFrameRate = PreferenceManager.getInstance().getCallMaxFrameRate();
        if (maxFrameRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(maxFrameRate);
        }

        // audio sample rate
        int audioSampleRate = PreferenceManager.getInstance().getCallAudioSampleRate();
        if (audioSampleRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(audioSampleRate);
        }

        /**
         * This function is only meaningful when your app need recording
         * If not, remove it.
         * This function need be called before the video stream started, so we set it in onCreate function.
         * This method will set the preferred video record encoding codec.
         * Using default encoding format, recorded file may not be played by mobile player.
         */
        //EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);

        // resolution
        String resolution = PreferenceManager.getInstance().getCallBackCameraResolution();
        if (resolution.equals("")) {
            resolution = PreferenceManager.getInstance().getCallFrontCameraResolution();
        }
        String[] wh = resolution.split("x");
        if (wh.length == 2) {
            try {
                EMClient.getInstance().callManager().getCallOptions().setVideoResolution(new Integer(wh[0]).intValue(), new Integer(wh[1]).intValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // enabled fixed sample rate
        boolean enableFixSampleRate = PreferenceManager.getInstance().isCallFixedVideoResolution();
        EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(enableFixSampleRate);

        // Offline call push
        EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(getModel().isPushCall());
    }

    protected void setEaseUIProviders() {
        //set user avatar to circle shape
        EaseAvatarOptions avatarOptions = new EaseAvatarOptions();
        avatarOptions.setAvatarShape(1);
        easeUI.setAvatarOptions(avatarOptions);

        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        //set options 
        easeUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return EMChatModel.getSettingMsgSpeaker();
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return EMChatModel.getSettingMsgVibrate();
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return EMChatModel.getSettingMsgSound();
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if (message == null) {
                    return EMChatModel.getSettingMsgNotification();
                }
                if (!EMChatModel.getSettingMsgNotification()) {
                    return false;
                } else {
                    String chatUsename = null;
                    List<String> notNotifyIds = null;
                    // get user or group id which was blocked to show message notifications
                    if (message.getChatType() == ChatType.Chat) {
                        chatUsename = message.getFrom();
                        notNotifyIds = EMChatModel.getDisabledIds();
                    } else {
                        chatUsename = message.getTo();
                        notNotifyIds = EMChatModel.getDisabledGroups();
                    }

                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });
        //set emoji icon provider
        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
//                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
//                for(EaseEmojicon emojicon : data.getEmojiconList()){
//                    if(emojicon.getIdentityCode().equals(emojiconIdentityCode)){
//                        return emojicon;
//                    }
//                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return message.getStringAttribute(CommonConstant.EMCHAT_NICKNAME, "");
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String accessToken = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == Type.TXT) {
                    accessToken = accessToken.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + accessToken;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + accessToken;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                int demandType = message.getIntAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, -1);

                String notificationText = null;

                if (demandType != -1) {
                    switch (demandType) {
                        case CommonConstant.MESSAGE_STATUS_BID_SUCCESS:
                            notificationText = appContext.getString(R.string.message_receiver_bid_success_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_MODIFY_PRICE:
                            notificationText = appContext.getString(R.string.message_receiver_modify_price_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_PAY_SUCCESS:
                            notificationText = appContext.getString(R.string.message_receiver_pay_success_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_REFUNDED:
                            notificationText = appContext.getString(R.string.message_receiver_refunded_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_START_WORK:
                            notificationText = appContext.getString(R.string.message_receiver_start_work_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_APPLY_FOR_REFUND:
                            notificationText = appContext.getString(R.string.message_receiver_apply_for_refund_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_AGREE_REFUND:
                            notificationText = appContext.getString(R.string.message_receiver_agree_refund_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_REJECT_REFUND:
                            notificationText = appContext.getString(R.string.message_receiver_reject_refund_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_APPLY_FOR_ARBITRAMENT:
                            notificationText = appContext.getString(R.string.message_receiver_apply_for_arbitrament_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_DELAY:
                            notificationText = appContext.getString(R.string.message_receiver_delay_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_ORDER_FINISHED:
                            notificationText = appContext.getString(R.string.message_receiver_order_finished_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_EVALUATED_EMPLOYER:
                            notificationText = appContext.getString(R.string.message_receiver_employer_evaluated_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_EVALUATED_FACILITATOR:
                            notificationText = appContext.getString(R.string.message_receiver_service_evaluated_content);
                            break;

                        case CommonConstant.MESSAGE_STATUS_RECOMMEND:
                            notificationText = appContext.getString(R.string.message_receiver_recommend_content);
                            break;
                    }
                } else {
                    if (message.getBody() instanceof EMTextMessageBody) {
                        EMTextMessageBody emTextMessageBody = (EMTextMessageBody) message.getBody();
                        Spannable spannable = EaseSmileUtils.getSmiledText(appContext, emTextMessageBody.getMessage());
                        notificationText = spannable.toString();
                    } else if (message.getBody() instanceof EMCmdMessageBody) {
                        EMCmdMessageBody emCmdMessageBody = (EMCmdMessageBody) message.getBody();
                        Spannable spannable = EaseSmileUtils.getSmiledText(appContext, emCmdMessageBody.action());
                        notificationText = spannable.toString();
                    }
                }
                return notificationText;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, ChatActivity.class);
                // open calling activity if there is call
                if (isVideoCalling) {
                    intent = new Intent(appContext, VideoCallActivity.class);
                } else if (isVoiceCalling) {
                    intent = new Intent(appContext, VoiceCallActivity.class);
                } else {
                    ChatType chatType = message.getChatType();
                    if (chatType == ChatType.Chat) { // single chat message
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", CommonConstant.CHATTYPE_SINGLE);
                    } else { // group chat message
                        // message.getTo() is the group id
                        intent.putExtra("userId", message.getTo());
                        if (chatType == ChatType.GroupChat) {
                            intent.putExtra("chatType", CommonConstant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra("chatType", CommonConstant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                return intent;
            }
        });
    }

    EMConnectionListener connectionListener;

    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        syncGroupsListeners = new ArrayList<>();
        syncContactsListeners = new ArrayList<>();
        syncBlackListListeners = new ArrayList<>();

        isGroupsSyncedWithServer = EMChatModel.isGroupsSynced();
        isContactsSyncedWithServer = EMChatModel.isContactSynced();
        isBlackListSyncedWithServer = EMChatModel.isBacklistSynced();

        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {
                    onUserException(CommonConstant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    onUserException(CommonConstant.ACCOUNT_CONFLICT);
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                    onUserException(CommonConstant.ACCOUNT_FORBIDDEN);
                } else if (error == EMError.USER_KICKED_BY_CHANGE_PASSWORD) {
                    onUserException(CommonConstant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD);
                } else if (error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                    onUserException(CommonConstant.ACCOUNT_KICKED_BY_OTHER_DEVICE);
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
                if (isGroupsSyncedWithServer && isContactsSyncedWithServer) {
                    EMLog.d(TAG, "group and contact already synced with servre");
                } else {
                    if (!isGroupsSyncedWithServer) {
                        asyncFetchGroupsFromServer(null);
                    }

                    if (!isContactsSyncedWithServer) {
                        asyncFetchContactsFromServer(null);
                    }

                    if (!isBlackListSyncedWithServer) {
                        asyncFetchBlackListFromServer(null);
                    }
                }
            }
        };

        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }

        //register incoming call receiver
        appContext.registerReceiver(callReceiver, callFilter);
        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);
        //register group and contact event listener
        registerGroupAndContactListener();
        //register message event listener
        registerMessageListener();

    }

    private void initDbDao() {
        inviteMessgeDao = new InviteMessgeDao(appContext);
        userDao = new UserDao(appContext);
    }

    /**
     * register group and contact listener, you need register when login
     */
    public void registerGroupAndContactListener() {
        if (!isGroupAndContactListenerRegisted) {
            EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());
            EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
            EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
            isGroupAndContactListenerRegisted = true;
        }

    }

    /**
     * group change listener
     */
    class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

            new InviteMessgeDao(appContext).deleteMessage(groupId);

            // user invite you to join group
            InviteMessage msg = new InviteMessage();
            msg.setFrom(groupId);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(groupName);
            msg.setReason(reason);
            msg.setGroupInviter(inviter);
            showToast("receive invitation to join the group：" + groupName);
            msg.setStatus(InviteMessageStatus.GROUPINVITATION);
            notifyNewInviteMessage(msg);
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onInvitationAccepted(String groupId, String invitee, String reason) {

            new InviteMessgeDao(appContext).deleteMessage(groupId);

            //user accept your invitation
            boolean hasGroup = false;
            EMGroup _group = null;
            for (EMGroup group : EMClient.getInstance().groupManager().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    _group = group;
                    break;
                }
            }
            if (!hasGroup)
                return;

            InviteMessage msg = new InviteMessage();
            msg.setFrom(groupId);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(_group == null ? groupId : _group.getGroupName());
            msg.setReason(reason);
            msg.setGroupInviter(invitee);
            showToast(invitee + "Accept to join the group：" + _group == null ? groupId : _group.getGroupName());
            msg.setStatus(InviteMessageStatus.GROUPINVITATION_ACCEPTED);
            notifyNewInviteMessage(msg);
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

            new InviteMessgeDao(appContext).deleteMessage(groupId);

            //user declined your invitation
            EMGroup group = null;
            for (EMGroup _group : EMClient.getInstance().groupManager().getAllGroups()) {
                if (_group.getGroupId().equals(groupId)) {
                    group = _group;
                    break;
                }
            }
            if (group == null)
                return;

            InviteMessage msg = new InviteMessage();
            msg.setFrom(groupId);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(group.getGroupName());
            msg.setReason(reason);
            msg.setGroupInviter(invitee);
            showToast(invitee + "Declined to join the group：" + group.getGroupName());
            msg.setStatus(InviteMessageStatus.GROUPINVITATION_DECLINED);
            notifyNewInviteMessage(msg);
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            //user is removed from group
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
            showToast("current user removed, groupId:" + groupId);
        }

        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
            // group is dismissed,
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
            showToast("group destroyed, groupId:" + groupId);
        }

        @Override
        public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {

            // user apply to join group
            InviteMessage msg = new InviteMessage();
            msg.setFrom(applyer);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(groupName);
            msg.setReason(reason);
            showToast(applyer + " Apply to join group：" + groupId);
            msg.setStatus(InviteMessageStatus.BEAPPLYED);
            notifyNewInviteMessage(msg);
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {

            String st4 = appContext.getString(R.string.Agreed_to_your_group_chat_application);
            // your application was accepted
            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
            msg.setChatType(ChatType.GroupChat);
            msg.setFrom(accepter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new EMTextMessageBody(accepter + " " + st4));
            msg.setStatus(Status.SUCCESS);
            // save accept message
            EMClient.getInstance().chatManager().saveMessage(msg);
            // notify the accept message
            getNotifier().vibrateAndPlayTone(msg);

            showToast("request to join accepted, groupId:" + groupId);
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
            // your application was declined, we do nothing here in demo
            showToast("request to join declined, groupId:" + groupId);
        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            // got an invitation
            String st3 = appContext.getString(R.string.Invite_you_to_join_a_group_chat);
            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
            msg.setChatType(ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new EMTextMessageBody(inviter + " " + st3));
            msg.setStatus(EMMessage.Status.SUCCESS);
            // save invitation as messages
            EMClient.getInstance().chatManager().saveMessage(msg);
            // notify invitation message
            getNotifier().vibrateAndPlayTone(msg);
            showToast("auto accept invitation from groupId:" + groupId);
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
        }

        // ============================= group_reform new add api begin
        @Override
        public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
            StringBuilder sb = new StringBuilder();
            for (String member : mutes) {
                sb.append(member).append(",");
            }
            showToast("onMuterListAdded: " + sb.toString());
        }


        @Override
        public void onMuteListRemoved(String groupId, final List<String> mutes) {
            StringBuilder sb = new StringBuilder();
            for (String member : mutes) {
                sb.append(member).append(",");
            }
            showToast("onMuterListRemoved: " + sb.toString());
        }


        @Override
        public void onAdminAdded(String groupId, String administrator) {
            showToast("onAdminAdded: " + administrator);
        }

        @Override
        public void onAdminRemoved(String groupId, String administrator) {
            showToast("onAdminRemoved: " + administrator);
        }

        @Override
        public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
            showToast("onOwnerChanged new:" + newOwner + " old:" + oldOwner);
        }

        @Override
        public void onMemberJoined(String groupId, String member) {
            showToast("onMemberJoined: " + member);
        }

        @Override
        public void onMemberExited(String groupId, String member) {
            showToast("onMemberExited: " + member);
        }

        @Override
        public void onAnnouncementChanged(String groupId, String announcement) {
            showToast("onAnnouncementChanged, groupId" + groupId);
        }

        @Override
        public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
            showToast("onSharedFileAdded, groupId" + groupId);
        }

        @Override
        public void onSharedFileDeleted(String groupId, String fileId) {
            showToast("onSharedFileDeleted, groupId" + groupId);
        }
        // ============================= group_reform new add api end
    }

    void showToast(final String message) {
        Log.d(TAG, "receive invitation to join the group：" + message);
        if (handler != null) {
            Message msg = Message.obtain(handler, 0, message);
            handler.sendMessage(msg);
        } else {
            msgQueue.add(message);
        }
    }

    public void initHandler(Looper looper) {
        handler = new android.os.Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                String str = (String) msg.obj;
                Toast.makeText(appContext, str, Toast.LENGTH_LONG).show();
            }
        };
        while (!msgQueue.isEmpty()) {
            showToast(msgQueue.remove());
        }
    }

    /***
     * 好友变化listener
     *
     */
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(String username) {
            // save contact
            Map<String, EaseUser> localUsers = getContactList();
            Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();
            EaseUser user = new EaseUser(username);

            if (!localUsers.containsKey(username)) {
                userDao.saveContact(user);
            }
            toAddUsers.put(username, user);
            localUsers.putAll(toAddUsers);

            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_CONTACT_CHANAGED));
            showToast("onContactAdded:" + username);
        }

        @Override
        public void onContactDeleted(String username) {
            Map<String, EaseUser> localUsers = EMChatHelper.getInstance().getContactList();
            localUsers.remove(username);
            userDao.deleteContact(username);
            inviteMessgeDao.deleteMessage(username);

            EMClient.getInstance().chatManager().deleteConversation(username, false);

            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_CONTACT_CHANAGED));
            showToast("onContactDeleted:" + username);
        }

        @Override
        public void onContactInvited(String username, String reason) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // save invitation as message
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            showToast(username + "apply to be your friend,reason: " + reason);
            // set invitation status
            msg.setStatus(InviteMessageStatus.BEINVITEED);
            notifyNewInviteMessage(msg);
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onFriendRequestAccepted(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // save invitation as message
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            showToast(username + " accept your to be friend");
            msg.setStatus(InviteMessageStatus.BEAGREED);
            notifyNewInviteMessage(msg);
            localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onFriendRequestDeclined(String username) {
            // your request was refused
            showToast(username + " refused to be your friend");
        }
    }

    public class MyMultiDeviceListener implements EMMultiDeviceListener {

        @Override
        public void onContactEvent(int event, String target, String ext) {
            switch (event) {
                case EMMultiDeviceListener.CONTACT_REMOVE: {
                    Map<String, EaseUser> localUsers = getContactList();
                    localUsers.remove(target);
                    userDao.deleteContact(target);
                    inviteMessgeDao.deleteMessage(target);

                    EMClient.getInstance().chatManager().deleteConversation(username, false);
                    localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_CONTACT_CHANAGED));
                    showToast("CONTACT_REMOVE");
                }
                break;
                case EMMultiDeviceListener.CONTACT_ACCEPT: {
                    Map<String, EaseUser> localUsers = getContactList();
                    EaseUser user = new EaseUser(target);
                    if (!localUsers.containsKey(target)) {
                        userDao.saveContact(user);
                    }
                    localUsers.put(target, user);
                    updateContactNotificationStatus(target, "", InviteMessageStatus.MULTI_DEVICE_CONTACT_ACCEPT);
                    localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_CONTACT_CHANAGED));
                    showToast("CONTACT_ACCEPT");
                }
                break;
                case EMMultiDeviceListener.CONTACT_DECLINE:
                    updateContactNotificationStatus(target, "", InviteMessageStatus.MULTI_DEVICE_CONTACT_DECLINE);
                    showToast("CONTACT_DECLINE");
                    break;
//                case CONTACT_ADD:
//                    updateContactNotificationStatus(target, "", InviteMessageStatus.MULTI_DEVICE_CONTACT_ADD);
//                    showToast("CONTACT_ADD");
//                break;
                case CONTACT_BAN:
                    updateContactNotificationStatus(target, "", InviteMessageStatus.MULTI_DEVICE_CONTACT_BAN);
                    showToast("CONTACT_BAN");

                    Map<String, EaseUser> localUsers = EMChatHelper.getInstance().getContactList();
                    localUsers.remove(username);
                    userDao.deleteContact(username);
                    inviteMessgeDao.deleteMessage(username);
                    EMClient.getInstance().chatManager().deleteConversation(username, false);
                    localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_CONTACT_CHANAGED));
                    break;
                case CONTACT_ALLOW:
                    updateContactNotificationStatus(target, "", InviteMessageStatus.MULTI_DEVICE_CONTACT_ALLOW);
                    showToast("CONTACT_ALLOW");
                    break;
                default:
                    break;
            }
        }

        private void updateContactNotificationStatus(String from, String reason, InviteMessageStatus status) {
            InviteMessage msg = null;
            for (InviteMessage _msg : inviteMessgeDao.getMessagesList()) {
                if (_msg.getFrom().equals(from)) {
                    msg = _msg;
                    break;
                }
            }
            if (msg != null) {
                ContentValues values = new ContentValues();
                msg.setStatus(status);
                values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                inviteMessgeDao.updateMessage(msg.getId(), values);
            } else {
                // save invitation as message
                msg = new InviteMessage();
                msg.setFrom(username);
                msg.setTime(System.currentTimeMillis());
                msg.setReason(reason);
                msg.setStatus(status);
                notifyNewInviteMessage(msg);
            }
        }

        @Override
        public void onGroupEvent(final int event, final String target, final List<String> usernames) {
            execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String groupId = target;
                        switch (event) {
                            case GROUP_CREATE:
                                showToast("GROUP_CREATE");
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_CREATE);
                                break;
                            case GROUP_DESTROY:
                                showToast("GROUP_DESTROY");
                                inviteMessgeDao.deleteGroupMessage(groupId);
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_DESTROY);
                                localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
                                break;
                            case GROUP_JOIN:
                                showToast("GROUP_JOIN");
                                localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_JOIN);
                                break;
                            case GROUP_LEAVE:
                                showToast("GROUP_LEAVE");
                                inviteMessgeDao.deleteGroupMessage(groupId);
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_LEAVE);
                                localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
                                break;
                            case GROUP_APPLY:
                                showToast("GROUP_APPLY");
                                inviteMessgeDao.deleteGroupMessage(groupId);
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_APPLY);
                                break;
                            case GROUP_APPLY_ACCEPT:
                                showToast("GROUP_ACCEPT");
                                inviteMessgeDao.deleteGroupMessage(groupId, usernames.get(0));
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_APPLY_ACCEPT);
                                break;
                            case GROUP_APPLY_DECLINE:
                                showToast("GROUP_APPLY_DECLINE");
                                inviteMessgeDao.deleteGroupMessage(groupId, usernames.get(0));
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_APPLY_DECLINE);
                                break;
                            case GROUP_INVITE:
                                showToast("GROUP_INVITE");
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_INVITE);
                                break;
                            case GROUP_INVITE_ACCEPT:
                                showToast("GROUP_INVITE_ACCEPT");
                                String st3 = appContext.getString(R.string.Invite_you_to_join_a_group_chat);
                                EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
                                msg.setChatType(ChatType.GroupChat);
                                // TODO: person, reason from ext
                                String from = "";
                                if (usernames != null && usernames.size() > 0) {
                                    msg.setFrom(usernames.get(0));
                                }
                                msg.setTo(groupId);
                                msg.setMsgId(UUID.randomUUID().toString());
                                msg.addBody(new EMTextMessageBody(msg.getFrom() + " " + st3));
                                msg.setStatus(EMMessage.Status.SUCCESS);
                                // save invitation as messages
                                EMClient.getInstance().chatManager().saveMessage(msg);

                                inviteMessgeDao.deleteMessage(groupId);
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_INVITE_ACCEPT);
                                localBroadcastManager.sendBroadcast(new Intent(CommonConstant.ACTION_GROUP_CHANAGED));
                                break;
                            case GROUP_INVITE_DECLINE:
                                showToast("GROUP_INVITE_DECLINE");
                                inviteMessgeDao.deleteMessage(groupId);
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_INVITE_DECLINE);
                                break;
                            case GROUP_KICK:
                                showToast("GROUP_KICK");
                                // TODO: person, reason from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_INVITE_DECLINE);
                                break;
                            case GROUP_BAN:
                                showToast("GROUP_BAN");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_BAN);
                                break;
                            case GROUP_ALLOW:
                                showToast("GROUP_ALLOW");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_ALLOW);
                                break;
                            case GROUP_BLOCK:
                                showToast("GROUP_BLOCK");
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_BLOCK);
                                break;
                            case GROUP_UNBLOCK:
                                showToast("GROUP_UNBLOCK");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/"", /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_UNBLOCK);
                                break;
                            case GROUP_ASSIGN_OWNER:
                                showToast("GROUP_ASSIGN_OWNER");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_ASSIGN_OWNER);
                                break;
                            case GROUP_ADD_ADMIN:
                                showToast("GROUP_ADD_ADMIN");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_ADD_ADMIN);
                                break;
                            case GROUP_REMOVE_ADMIN:
                                showToast("GROUP_REMOVE_ADMIN");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_REMOVE_ADMIN);
                                break;
                            case GROUP_ADD_MUTE:
                                showToast("GROUP_ADD_MUTE");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_ADD_MUTE);
                                break;
                            case GROUP_REMOVE_MUTE:
                                showToast("GROUP_REMOVE_MUTE");
                                // TODO: person from ext
                                saveGroupNotification(groupId, /*groupName*/"",  /*person*/usernames.get(0), /*reason*/"", InviteMessageStatus.MULTI_DEVICE_GROUP_REMOVE_MUTE);
                                break;
                            default:
                                break;
                        }

                        if (false) { // keep the try catch structure
                            throw new HyphenateException("");
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        private void saveGroupNotification(String groupId, String groupName, String inviter, String reason, InviteMessageStatus status) {
            InviteMessage msg = new InviteMessage();
            msg.setFrom(groupId);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(groupName);
            msg.setReason(reason);
            msg.setGroupInviter(inviter);
            Log.d(TAG, "receive invitation to join the group：" + groupName);
            msg.setStatus(status);
            notifyNewInviteMessage(msg);
        }

        private void updateGroupNotificationStatus(String groupId, String groupName, String inviter, String reason, InviteMessageStatus status) {
            InviteMessage msg = null;
            for (InviteMessage _msg : inviteMessgeDao.getMessagesList()) {
                if (_msg.getGroupId().equals(groupId)) {
                    msg = _msg;
                    break;
                }
            }
            if (msg != null) {
                ContentValues values = new ContentValues();
                msg.setStatus(status);
                values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                inviteMessgeDao.updateMessage(msg.getId(), values);
            }
        }
    }

    /**
     * save and notify invitation message
     *
     * @param msg
     */
    private void notifyNewInviteMessage(InviteMessage msg) {
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(appContext);
        }
        inviteMessgeDao.saveMessage(msg);
        //increase the unread message count
        inviteMessgeDao.saveUnreadMessageCount(1);
        // notify there is new message
        getNotifier().vibrateAndPlayTone(null);
    }

    /**
     * user met some exception: conflict, removed or forbidden
     */
    protected void onUserException(String exception) {
        EMLog.e(TAG, "onUserException: " + exception);

        if (!getRunningActivityName().equals("com.allintask.lingdao.ui.activity.user.LoginActivity") && !exception.equals(CommonConstant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD)) {
            UserPreferences.getInstance().setUserBean(null);
            EaseUI.getInstance().getNotifier().reset();
            EMClient.getInstance().logout(true);

            Intent intent = new Intent(appContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(intent);

//            Intent intent = new Intent(appContext, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            intent.putExtra(CommonConstant.ACTION_LOGOUT, true);
//            intent.putExtra(exception, true);
//            appContext.startActivity(intent);
        }

        showToast(exception);
    }

    private String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivityName = null;

        if (null != activityManager) {
            runningActivityName = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        }
        return runningActivityName;
    }

    private EaseUser getUserInfo(String EMChatUserId) {
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        String currentUserId = EMClient.getInstance().getCurrentUser();

        EaseUser easeUser;

        if (!TextUtils.isEmpty(currentUserId) && !TextUtils.isEmpty(EMChatUserId) && EMChatUserId.equals(currentUserId)) {
            String nickname = UserPreferences.getInstance().getNickname();
            String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

            easeUser = new EaseUser(EMChatUserId);
            easeUser.setNickname(nickname);
            easeUser.setAvatar(headPortraitUrl);
        } else {
            if (null == contactList || contactList.size() <= 0 || !contactList.containsKey(EMChatUserId)) {
                contactList = getContactList();
            }

            easeUser = contactList.get(EMChatUserId);

            if (null == easeUser) {
                easeUser = new EaseUser(EMChatUserId);
            }
        }
        return easeUser;
    }

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    // in background, do not refresh UI, notify it in notification bar
                    String emChatUserId = message.getFrom();
                    String nickname = message.getStringAttribute(CommonConstant.EMCHAT_NICKNAME, "");
                    String tempHeadPortraitUrl = message.getStringAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, "");
                    String price = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CHANGED_PRICE, "");

                    EaseUser easeUser = new EaseUser(emChatUserId);
                    easeUser.setNickname(nickname);

                    String headPortraitUrl = null;

                    if (!TextUtils.isEmpty(tempHeadPortraitUrl)) {
                        headPortraitUrl = "https:" + tempHeadPortraitUrl;
                    }

                    easeUser.setAvatar(headPortraitUrl);

                    getContactList();
                    contactList.put(emChatUserId, easeUser);

                    UserDao userDao = new UserDao(AllintaskApplication.getInstance());
                    userDao.saveContact(easeUser);

                    getModel().setContactSynced(true);
                    notifyContactsSyncListener(true);

                    Intent intent = new Intent();
                    intent.setAction(CommonConstant.ACTION_REFRESH_FRAGMENT);
                    intent.putExtra(CommonConstant.EXTRA_REFRESH_FRAGMENT, CommonConstant.MESSAGE_FRAGMENT);
                    AllintaskApplication.getInstance().sendBroadcast(intent);

                    if (!TextUtils.isEmpty(price)) {
                        Intent payIntent = new Intent();
                        payIntent.setAction(CommonConstant.ACTION_REFRESH);
                        payIntent.putExtra(CommonConstant.EXTRA_PRICE, price);
                        localBroadcastManager.sendBroadcast(payIntent);
                    }

//                    if (!easeUI.hasForegroundActivies()) {
                    getNotifier().onNewMsg(message);
//                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action
                    //red packet code : 处理红包回执透传消息
//                    if(!easeUI.hasForegroundActivies()){
//                        if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                            RedPacketUtil.receiveRedPacketAckMessage(message);
//                            localBroadcastManager.sendBroadcast(new Intent(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION));
//                        }
//                    }
//
//                    if (action.equals("__Call_ReqP2P_ConferencePattern")) {
//                        String title = message.getStringAttribute("em_apns_ext", "conference call");
//                        Toast.makeText(appContext, title, Toast.LENGTH_LONG).show();
//                    }
                    //end of red packet code
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
                    getNotifier().onNewMsg(message);
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                for (EMMessage msg : messages) {
                    if (msg.getChatType() == ChatType.GroupChat && EaseAtMessageHelper.get().isAtMeMsg(msg)) {
                        EaseAtMessageHelper.get().removeAtMeGroup(msg.getTo());
                    }
                    EMMessage msgNotification = EMMessage.createReceiveMessage(Type.TXT);
                    EMTextMessageBody txtBody = new EMTextMessageBody(String.format(appContext.getString(R.string.msg_recall_by_user), msg.getFrom()));
                    msgNotification.addBody(txtBody);
                    msgNotification.setFrom(msg.getFrom());
                    msgNotification.setTo(msg.getTo());
                    msgNotification.setUnread(false);
                    msgNotification.setMsgTime(msg.getMsgTime());
                    msgNotification.setLocalTime(msg.getMsgTime());
                    msgNotification.setChatType(msg.getChatType());
                    msgNotification.setAttribute(CommonConstant.MESSAGE_TYPE_RECALL, true);
                    EMClient.getInstance().chatManager().saveMessage(msgNotification);
                }
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                EMLog.d(TAG, "change:");
                EMLog.d(TAG, "change:" + change);
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        Log.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    public EMChatModel getModel() {
        return (EMChatModel) EMChatModel;
    }

    /**
     * update contact list
     *
     * @param aContactList
     */
    public void setContactList(Map<String, EaseUser> aContactList) {
        if (aContactList == null) {
            if (contactList != null) {
                contactList.clear();
            }
            return;
        }

        contactList = aContactList;
    }

    /**
     * save single contact
     */
    public void saveContact(EaseUser user) {
        contactList.put(user.getUsername(), user);
        EMChatModel.saveContact(user);
    }

    /**
     * get contact list
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && contactList == null) {
            contactList = EMChatModel.getContactList();
        }

        // return a empty non-null object to avoid app crash
        if (contactList == null) {
            return new Hashtable<String, EaseUser>();
        }

        return contactList;
    }

    /**
     * set current username
     *
     * @param username
     */
    public void setCurrentUserName(String username) {
        this.username = username;
        EMChatModel.setCurrentUserName(username);
    }

    /**
     * get current user's id
     */
    public String getCurrentUsernName() {
        if (username == null) {
            username = EMChatModel.getCurrentUsernName();
        }
        return username;
    }

    public void setRobotList(Map<String, RobotUser> robotList) {
        this.robotList = robotList;
    }

    public Map<String, RobotUser> getRobotList() {
        if (isLoggedIn() && robotList == null) {
            robotList = EMChatModel.getRobotList();
        }
        return robotList;
    }

    /**
     * update user list to cache and database
     *
     * @param contactInfoList
     */
    public void updateContactList(List<EaseUser> contactInfoList) {
        for (EaseUser u : contactInfoList) {
            contactList.put(u.getUsername(), u);
        }
        ArrayList<EaseUser> mList = new ArrayList<EaseUser>();
        mList.addAll(contactList.values());
        EMChatModel.saveContactList(mList);
    }

    void endCall() {
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.add(listener);
        }
    }

    public void removeSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.remove(listener);
        }
    }

    public void addSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncContactsListeners.contains(listener)) {
            syncContactsListeners.add(listener);
        }
    }

    public void removeSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncContactsListeners.contains(listener)) {
            syncContactsListeners.remove(listener);
        }
    }

    public void addSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.add(listener);
        }
    }

    public void removeSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.remove(listener);
        }
    }

    /**
     * Get group list from server
     * This method will save the sync state
     *
     * @throws HyphenateException
     */
    public synchronized void asyncFetchGroupsFromServer(final EMCallBack callback) {
        if (isSyncingGroupsWithServer) {
            return;
        }

        isSyncingGroupsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    List<EMGroup> groups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isGroupsSyncedWithServer = false;
                        isSyncingGroupsWithServer = false;
                        noitifyGroupSyncListeners(false);
                        return;
                    }

                    EMChatModel.setGroupsSynced(true);

                    isGroupsSyncedWithServer = true;
                    isSyncingGroupsWithServer = false;

                    //notify sync group list success
                    noitifyGroupSyncListeners(true);

                    if (callback != null) {
                        callback.onSuccess();
                    }
                } catch (HyphenateException e) {
                    EMChatModel.setGroupsSynced(false);
                    isGroupsSyncedWithServer = false;
                    isSyncingGroupsWithServer = false;
                    noitifyGroupSyncListeners(false);
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void noitifyGroupSyncListeners(boolean success) {
        for (DataSyncListener listener : syncGroupsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchContactsFromServer(final EMValueCallBack<List<String>> callback) {
        if (isSyncingContactsWithServer) {
            return;
        }

        isSyncingContactsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                List<String> usernames = null;
                List<String> selfIds = null;
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    selfIds = EMClient.getInstance().contactManager().getSelfIdsOnOtherPlatform();
                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isContactsSyncedWithServer = false;
                        isSyncingContactsWithServer = false;
                        notifyContactsSyncListener(false);
                        return;
                    }
                    if (selfIds.size() > 0) {
                        usernames.addAll(selfIds);
                    }
                    Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
                    for (String username : usernames) {
                        EaseUser user = new EaseUser(username);
                        EaseCommonUtils.setUserInitialLetter(user);
                        userlist.put(username, user);
                    }
                    // save the contact list to cache
                    getContactList().clear();
                    getContactList().putAll(userlist);
                    // save the contact list to database
                    UserDao dao = new UserDao(appContext);
                    List<EaseUser> users = new ArrayList<EaseUser>(userlist.values());
                    dao.saveContactList(users);

                    EMChatModel.setContactSynced(true);
                    EMLog.d(TAG, "set contact syn status to true");

                    isContactsSyncedWithServer = true;
                    isSyncingContactsWithServer = false;

                    //notify sync success
                    notifyContactsSyncListener(true);

//                   getUserProfileManager().asyncFetchContactInfosFromServer(usernames,new EMValueCallBack<List<EaseUser>>() {
//
//                       @Override
//                       public void onSuccess(List<EaseUser> uList) {
//                           updateContactList(uList);
//                           getUserProfileManager().notifyContactInfosSyncListener(true);
//                       }
//
//                       @Override
//                       public void onError(int error, String errorMsg) {
//                       }
//                   });
                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    EMChatModel.setContactSynced(false);
                    isContactsSyncedWithServer = false;
                    isSyncingContactsWithServer = false;
                    notifyContactsSyncListener(false);
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyContactsSyncListener(boolean success) {
        for (DataSyncListener listener : syncContactsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchBlackListFromServer(final EMValueCallBack<List<String>> callback) {

        if (isSyncingBlackListWithServer) {
            return;
        }

        isSyncingBlackListWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getBlackListFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isBlackListSyncedWithServer = false;
                        isSyncingBlackListWithServer = false;
                        notifyBlackListSyncListener(false);
                        return;
                    }

                    EMChatModel.setBlacklistSynced(true);

                    isBlackListSyncedWithServer = true;
                    isSyncingBlackListWithServer = false;

                    notifyBlackListSyncListener(true);
                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    EMChatModel.setBlacklistSynced(false);

                    isBlackListSyncedWithServer = false;
                    isSyncingBlackListWithServer = true;
                    e.printStackTrace();

                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyBlackListSyncListener(boolean success) {
        for (DataSyncListener listener : syncBlackListListeners) {
            listener.onSyncComplete(success);
        }
    }

    public boolean isSyncingGroupsWithServer() {
        return isSyncingGroupsWithServer;
    }

    public boolean isSyncingContactsWithServer() {
        return isSyncingContactsWithServer;
    }

    public boolean isSyncingBlackListWithServer() {
        return isSyncingBlackListWithServer;
    }

    public boolean isGroupsSyncedWithServer() {
        return isGroupsSyncedWithServer;
    }

    public boolean isContactsSyncedWithServer() {
        return isContactsSyncedWithServer;
    }

    public boolean isBlackListSyncedWithServer() {
        return isBlackListSyncedWithServer;
    }

    synchronized void reset() {
        isSyncingGroupsWithServer = false;
        isSyncingContactsWithServer = false;
        isSyncingBlackListWithServer = false;

        EMChatModel.setGroupsSynced(false);
        EMChatModel.setContactSynced(false);
        EMChatModel.setBlacklistSynced(false);

        isGroupsSyncedWithServer = false;
        isContactsSyncedWithServer = false;
        isBlackListSyncedWithServer = false;

        isGroupAndContactListenerRegisted = false;

        setContactList(null);
        setRobotList(null);
//        getUserProfileManager().reset();
        EMChatDatabaseManager.getInstance().closeDB();
    }

    public void pushActivity(Activity activity) {
        easeUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.popActivity(activity);
    }

}
