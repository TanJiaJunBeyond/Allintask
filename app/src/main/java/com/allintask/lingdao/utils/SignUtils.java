package com.allintask.lingdao.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.allintask.lingdao.constant.CommonConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/22.
 */

public class SignUtils {

    /**
     * 加签
     *
     * @param requestFormParamMap
     * @param isWithdrawDeposit
     * @return
     */
    public static String getSign(Map<String, List<String>> requestFormParamMap, boolean isWithdrawDeposit) {
        String result = "";

        if (null != requestFormParamMap && requestFormParamMap.size() > 0) {
            List<String> keysList = new ArrayList<>();
            Map<String, List<String>> values = new HashMap<>();

            for (Map.Entry<String, List<String>> entry : requestFormParamMap.entrySet()) {
                keysList.add(entry.getKey());
                values.put(entry.getKey(), entry.getValue());
            }

            // ASCII排序
            Collections.sort(keysList);
            Collections.reverse(keysList);

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < keysList.size(); i++) {
                String paramKey = keysList.get(i);
                List<String> paramValues = values.get(paramKey);

                String value = null;

                if (null != paramValues && paramValues.size() > 0) {
                    if (paramValues.size() == 1) {
                        if (isWithdrawDeposit) {
                            try {
                                value = URLEncoder.encode(TypeUtils.getString(paramValues.get(0), ""), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            value = TypeUtils.getString(paramValues.get(0), "");
                        }
                    } else {
                        value = JSONArray.toJSONString(paramValues, SerializerFeature.DisableCircularReferenceDetect).trim();
                    }

                    if (!TextUtils.isEmpty(value)) {
                        stringBuilder.append(paramKey).append("=").append(value).append("&");
                    }
                }
            }

            stringBuilder.append("key=").append(CommonConstant.AES_KEY);

            try {
                if (isWithdrawDeposit) {
                    result = AESUtils.encrypt(stringBuilder.toString(), CommonConstant.WITHDRAW_DEPOSIT_AES_KEY);
                } else {
                    result = AESUtils.encrypt(stringBuilder.toString(), CommonConstant.AES_KEY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
