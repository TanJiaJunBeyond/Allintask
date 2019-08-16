package com.allintask.lingdao.widget.chatrow;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.domain.EaseEmojicon;
import com.allintask.lingdao.utils.EaseUI;
import com.hyphenate.chat.EMMessage;

import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * big emoji icons
 *
 */
public class EaseChatRowBigExpression extends EaseChatRowText {

    private ImageView imageView;


    public EaseChatRowBigExpression(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }
    
    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_bigexpression : R.layout.ease_row_sent_bigexpression, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
    }


    @Override
    public void onSetUpView() {
        String emojiconId = message.getStringAttribute(CommonConstant.MESSAGE_ATTR_EXPRESSION_ID, null);
        EaseEmojicon emojicon = null;
        if(EaseUI.getInstance().getEmojiconInfoProvider() != null){
            emojicon =  EaseUI.getInstance().getEmojiconInfoProvider().getEmojiconInfo(emojiconId);
        }
        if(emojicon != null){
            if(emojicon.getBigIcon() != 0){
                ImageViewUtil.setImageView(context,imageView,String.valueOf(emojicon.getBigIcon()),R.drawable.ease_default_expression);
            }else if(emojicon.getBigIconPath() != null){
                ImageViewUtil.setImageView(context,imageView,String.valueOf(emojicon.getBigIconPath()),R.drawable.ease_default_expression);
            }else{
                imageView.setImageResource(R.drawable.ease_default_expression);
            }
        }
    }
}
