package com.allintask.lingdao.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.domain.EaseUser;

import cn.tanjiajun.sdk.component.util.ImageViewUtil;

public class EaseUserUtils {

    static EaseUI.EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EaseUser user = getUserInfo(username);

        if (null != user) {
            ImageViewUtil.setImageView(context, imageView, user.getAvatar(), R.mipmap.ic_default_avatar);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {
                textView.setText(username);
            }
        }
    }

}
