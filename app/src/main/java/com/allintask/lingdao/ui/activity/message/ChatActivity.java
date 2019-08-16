package com.allintask.lingdao.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.BaseFragmentActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.fragment.message.ChatFragment;
import com.allintask.lingdao.utils.UserPreferences;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EasyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/11.
 */

public class ChatActivity extends BaseFragmentActivity {

    public static ChatActivity activityInstance;

    @BindView(R.id.ll_content)
    LinearLayout contentLL;

    private String userId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_chat;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init() {
        initData();
        initUI();
    }

    private void initData() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getStringExtra(CommonConstant.EXTRA_USER_ID);
        }

        activityInstance = this;
    }

    private void initUI() {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.EXTRA_CHAT_TYPE, CommonConstant.CHATTYPE_SINGLE);
        bundle.putString(CommonConstant.EXTRA_USER_ID, userId);
        openFragment(ChatFragment.class.getName(), bundle);
    }

    @Override
    public void onBackPressed() {
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(getParentContext(), MainActivity.class);
            startActivity(intent);

            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra(CommonConstant.EXTRA_USER_ID);

        if (userId.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

}
