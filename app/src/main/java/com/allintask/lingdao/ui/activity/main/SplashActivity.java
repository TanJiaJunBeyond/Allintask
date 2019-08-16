package com.allintask.lingdao.ui.activity.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.UserPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by TanJiaJun on 2018/3/30.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            Uri uri = intent.getData();

            if (null != uri) {
                String host = uri.getHost();
                String encodedQuery = uri.getEncodedQuery();

                if (!TextUtils.isEmpty(host)) {
                    String zhiMaCreditAuthenticationType = host.replaceFirst("type=", "");
                    UserPreferences.getInstance().setZhiMaCreditAuthenticationType(zhiMaCreditAuthenticationType);
                }

                if (!TextUtils.isEmpty(encodedQuery)) {
                    String[] queryArray = encodedQuery.split("&");

                    if (queryArray.length > 0) {
                        String params = queryArray[0];
                        String sign = queryArray[1];

                        if (!TextUtils.isEmpty(params)) {
                            String zhiMaCreditAuthenticationParams = params.replaceFirst("params=", "");
                            UserPreferences.getInstance().setZhiMaCreditAuthenticationParams(zhiMaCreditAuthenticationParams);
                        }

                        if (!TextUtils.isEmpty(sign)) {
                            String zhiMaCreditAuthenticationSign = sign.replaceFirst("sign=", "");
                            UserPreferences.getInstance().setZhiMaCreditAuthenticationSign(zhiMaCreditAuthenticationSign);
                        }
                    }
                }
            }
        }

        initUI();
    }

    private void initUI() {
        final boolean isFirst = UserPreferences.getInstance().getIsFirst();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirst) {
                    Set<String> searchSet = new HashSet<>();
                    String searchSetJsonString = JSONObject.toJSONString(searchSet, SerializerFeature.DisableCircularReferenceDetect);
                    UserPreferences.getInstance().setSearchSetJsonString(searchSetJsonString);

                    if (!AllintaskApplication.IS_STARTED) {
                        Intent guideIntent = new Intent(getParentContext(), GuideActivity.class);
                        startActivity(guideIntent);
                    }

                    finish();
                } else {
                    if (!AllintaskApplication.IS_STARTED) {
                        Intent mainIntent = new Intent(getParentContext(), MainActivity.class);
                        startActivity(mainIntent);

                        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                    }

                    finish();
                }
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {

    }

}
