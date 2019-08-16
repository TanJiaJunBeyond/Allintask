package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.ReportReasonBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/4/9.
 */

public class ReportReasonAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public ReportReasonAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_report_reason, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        final ReportReasonBean reportReasonBean = (ReportReasonBean) getItem(position);

        if (null != reportReasonBean) {
            final CheckBox selectCB = holder.getChildView(R.id.cb_select);

            String name = TypeUtils.getString(reportReasonBean.name, "");
            boolean isChecked = TypeUtils.getBoolean(reportReasonBean.isChecked, false);

            holder.setTextView(R.id.tv_report_reason, name);

            selectCB.setChecked(isChecked);
            selectCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    reportReasonBean.isChecked = isChecked;
                }
            });

            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reportReasonBean.isChecked) {
                        reportReasonBean.isChecked = false;
                        selectCB.setChecked(false);
                    } else {
                        reportReasonBean.isChecked = true;
                        selectCB.setChecked(true);
                    }
                }
            });
        }
    }

}
