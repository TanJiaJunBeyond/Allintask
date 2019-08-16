package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.allintask.lingdao.R;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class AboutUsAdapter extends CommonRecyclerViewAdapter {

    private static final int ITEM_ABOUT_US_HEADER = 0;
    private static final int ITEM_ABOUT_US = 1;

    private Context context;

    public AboutUsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = null;

        switch (viewType) {
            case ITEM_ABOUT_US_HEADER:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_about_us_header, parent, false));
                break;

            case ITEM_ABOUT_US:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_about_us, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        switch (getItemViewType(position)) {
            case ITEM_ABOUT_US_HEADER:
                onBindAboutUsHeaderItemView(holder);
                break;

            case ITEM_ABOUT_US:
                onBindAboutUsItemView(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_ABOUT_US_HEADER;
        } else {
            return ITEM_ABOUT_US;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private void onBindAboutUsHeaderItemView(CommonRecyclerViewHolder holder) {
        String versionName = getVersionName();

        if (!TextUtils.isEmpty(versionName)) {
            holder.setTextView(R.id.tv_version_name, "版本号：" + versionName);
        }
    }

    private void onBindAboutUsItemView(CommonRecyclerViewHolder holder, int position) {

    }

    private String getVersionName() {
        String versionName = null;
        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

}
