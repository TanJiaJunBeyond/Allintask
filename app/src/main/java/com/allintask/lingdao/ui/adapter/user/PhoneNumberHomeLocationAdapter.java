package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationBean;
import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.user.PhoneNumberHomeLocationActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/22.
 */

public class PhoneNumberHomeLocationAdapter extends CommonRecyclerViewAdapter implements StickyRecyclerHeadersAdapter {

    private Context context;

    public PhoneNumberHomeLocationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_phone_number_home_location_header, parent, false));
        return holder;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_phone_number_home_location, parent, false));
        return holder;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommonRecyclerViewHolder phoneNumberHomeLocationHolder = (CommonRecyclerViewHolder) holder;
        PhoneNumberHomeLocationBean phoneNumberHomeLocationBean = (PhoneNumberHomeLocationBean) getItem(position);

        if (null != phoneNumberHomeLocationBean) {
            String initial = TypeUtils.getString(phoneNumberHomeLocationBean.initial, "");
            phoneNumberHomeLocationHolder.setTextView(R.id.tv_initial, initial);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommonRecyclerViewHolder phoneNumberHomeLocationHolder = (CommonRecyclerViewHolder) holder;
        PhoneNumberHomeLocationBean phoneNumberHomeLocationBean = (PhoneNumberHomeLocationBean) getItem(position);

        if (null != phoneNumberHomeLocationBean) {
            LinearLayout phoneNumberHomeLocationLL = phoneNumberHomeLocationHolder.getChildView(R.id.ll_phone_number_home_location);

            final int mobileCountryCodeId = TypeUtils.getInteger(phoneNumberHomeLocationBean.mobileCountryCodeId, 0);
            String name = TypeUtils.getString(phoneNumberHomeLocationBean.name, "");
            final String value = TypeUtils.getString(phoneNumberHomeLocationBean.value, "");
            final String regularEx = TypeUtils.getString(phoneNumberHomeLocationBean.regularEx, "");

            phoneNumberHomeLocationHolder.setTextView(R.id.tv_name, name);
            phoneNumberHomeLocationHolder.setTextView(R.id.tv_number, value);

            phoneNumberHomeLocationLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(CommonConstant.EXTRA_PHONE_NUMBER_HOME_LOCATION_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
                    intent.putExtra(CommonConstant.EXTRA_PHONE_NUMBER_HOME_LOCATION_VALUE, value);
                    intent.putExtra(CommonConstant.EXTRA_PHONE_NUMBER_HOME_LOCATION_REGULAR_EX, regularEx);
                    ((PhoneNumberHomeLocationActivity) context).setResult(CommonConstant.RESULT_CODE, intent);

                    ((PhoneNumberHomeLocationActivity) context).finish();
                }
            });
        }
    }

    @Override
    public long getHeaderId(int position) {
        long headerId = 0L;
        PhoneNumberHomeLocationBean phoneNumberHomeLocationBean = (PhoneNumberHomeLocationBean) getItem(position);

        if (null != phoneNumberHomeLocationBean) {
            String initial = TypeUtils.getString(phoneNumberHomeLocationBean.initial, "");
            headerId = initial.charAt(0);
        }
        return headerId;
    }

}
