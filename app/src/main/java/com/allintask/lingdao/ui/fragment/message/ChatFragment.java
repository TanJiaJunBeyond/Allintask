package com.allintask.lingdao.ui.fragment.message;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.allintask.lingdao.helper.EMChatHelper;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.main.MainActivity;

import com.allintask.lingdao.ui.activity.message.ImageGridActivity;
import com.allintask.lingdao.domain.RobotUser;
import com.allintask.lingdao.ui.activity.message.ContextMenuActivity;
import com.allintask.lingdao.ui.activity.message.ForwardMessageActivity;
import com.allintask.lingdao.ui.activity.message.VideoCallActivity;
import com.allintask.lingdao.ui.activity.message.VoiceCallActivity;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.widget.EaseChatRecallPresenter;

import com.allintask.lingdao.widget.EaseChatVoiceCallPresenter;
import com.allintask.lingdao.R;
import com.allintask.lingdao.widget.chatrow.EaseCustomChatRowProvider;
import com.allintask.lingdao.presenter.message.EaseChatRowPresenter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.PathUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/3.
 */

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;
//    //red packet code : 红包功能使用的常量
//    private static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;
//    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
//    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
//    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
//    private static final int MESSAGE_TYPE_RECV_RANDOM = 11;
//    private static final int MESSAGE_TYPE_SEND_RANDOM = 12;
//    private static final int ITEM_RED_PACKET = 16;

    /**
     * if it is chatBot
     */
    private boolean isRobot;

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
//        easeChatInputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
//        easeChatInputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
//
//        if (chatType == CommonConstant.CHATTYPE_SINGLE) {
//            easeChatInputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//            easeChatInputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
//        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState,
//                EMChatHelper.getInstance().getModel().isMsgRoaming() && (chatType != CommonConstant.CHATTYPE_CHATROOM));
//    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setChatFragmentHelper(this);

        if (chatType == CommonConstant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = EMChatHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyUtils.isSingleActivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }

                onBackPressed();
            }
        });

//        sendMessage(100005);
    }

    private void sendMessage(int userId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();
        EMMessage emMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);
        emMessage.setTo(String.valueOf(userId));

        EMCmdMessageBody emCmdMessageBody = new EMCmdMessageBody("很好很好");
        emMessage.addBody(emCmdMessageBody);

        if (!TextUtils.isEmpty(nickname)) {
            emMessage.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                emMessage.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(headPortraitUrl)) {
                String tempHeadPortrait = headPortraitUrl.replace("https:", "");
                emMessage.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
            }
        }

        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (isRobot) {
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
            message.setAttribute("test", true);
        }

        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        if (!TextUtils.isEmpty(nickname)) {
            message.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                message.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(headPortraitUrl)) {
            String tempHeadPortrait = headPortraitUrl.replace("https:", "");
            message.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
        }
    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {
        //头像点击事件
    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    easeChatMessageList.refresh();
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                    startActivity(intent);
                    break;
                case ContextMenuActivity.RESULT_CODE_RECALL://recall
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMMessage msgNotification = EMMessage.createTxtSendMessage(" ", contextMenuMessage.getTo());
                                EMTextMessageBody txtBody = new EMTextMessageBody(getResources().getString(R.string.msg_recall_by_self));
                                msgNotification.addBody(txtBody);
                                msgNotification.setMsgTime(contextMenuMessage.getMsgTime());
                                msgNotification.setLocalTime(contextMenuMessage.getMsgTime());
                                msgNotification.setAttribute(CommonConstant.MESSAGE_TYPE_RECALL, true);
                                EMClient.getInstance().chatManager().recallMessage(contextMenuMessage);
                                EMClient.getInstance().chatManager().saveMessage(msgNotification);
                                easeChatMessageList.refresh();
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                    break;

                default:
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
//            // voiceCallBtn.setEnabled(false);
            easeChatInputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // videoCallBtn.setEnabled(false);
            easeChatInputMenu.hideExtendMenuContainer();
        }
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO:
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;

        }
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row  消息的类型数量
            return 6;//13
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(CommonConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(CommonConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //messagee recall
                else if (message.getBooleanAttribute(CommonConstant.MESSAGE_TYPE_RECALL, false)) {
                    return MESSAGE_TYPE_RECALL;
                }
                //red packet code : 红包消息、红包回执消息以及转账消息的chatrow type
//                else if (RedPacketUtil.isRandomRedPacket(message)) {
//                    //小额随机红包
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RANDOM : MESSAGE_TYPE_SEND_RANDOM;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {
//                    //发送红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET : MESSAGE_TYPE_SEND_RED_PACKET;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    //领取红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET_ACK : MESSAGE_TYPE_SEND_RED_PACKET_ACK;
//                }
                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // voice call or video call
                if (message.getBooleanAttribute(CommonConstant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(CommonConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    EaseChatRowPresenter presenter = new EaseChatVoiceCallPresenter();
                    return presenter;
                }
                //recall message
                else if (message.getBooleanAttribute(CommonConstant.MESSAGE_TYPE_RECALL, false)) {
//                    return new EaseChatRowRecall(getActivity(), message, position, adapter);
                    EaseChatRowPresenter presenter = new EaseChatRecallPresenter();
                    return presenter;
                }
                //red packet code : 红包消息、红包回执消息以及转账消息的chat row
//                else if (RedPacketUtil.isRandomRedPacket(message)) {//小额随机红包
//                    return new ChatRowRandomPacket(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {//红包消息00
//                    return new ChatRowRedPacket(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {//红包回执消息
//                    return new ChatRowRedPacketAck(getActivity(), message, position, adapter);
//                }
                //end of red packet code
            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //调用该方法可防止红包SDK引起的内存泄漏
//        RPRedPacketUtil.getInstance().detachView();
    }

}
