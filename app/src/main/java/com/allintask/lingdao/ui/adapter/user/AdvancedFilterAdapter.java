package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.ShowAdvancedFilterBean;
import com.allintask.lingdao.bean.user.ShowAdvancedSubclassBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class AdvancedFilterAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private OnClickListener onClickListener;

    public AdvancedFilterAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_advanced_filter, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        ShowAdvancedFilterBean showAdvancedFilterBean = (ShowAdvancedFilterBean) getItem(position);

        if (null != showAdvancedFilterBean) {
            TagFlowLayout tagFlowLayout = holder.getChildView(R.id.tag_flow_layout);

            final String title = TypeUtils.getString(showAdvancedFilterBean.title, "");
            List<ShowAdvancedSubclassBean> showAdvancedSubclassList = showAdvancedFilterBean.subclassList;

            holder.setTextView(R.id.tv_title, title, true);

            List<String> subclassNameList = new ArrayList<>();
            final Set<Integer> isSelectedSet = new HashSet<>();

            for (int i = 0; i < showAdvancedSubclassList.size(); i++) {
                ShowAdvancedSubclassBean showAdvancedSubclassBean = showAdvancedSubclassList.get(i);

                if (null != showAdvancedSubclassBean) {
                    String content = TypeUtils.getString(showAdvancedSubclassBean.content, "");
                    boolean isSelected = TypeUtils.getBoolean(showAdvancedSubclassBean.isSelected, false);

                    subclassNameList.add(content);

                    if (isSelected) {
                        isSelectedSet.add(i);
                    }
                }
            }

            TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                @Override
                public View getView(FlowLayout flowLayout, int i, Object o) {
                    TextView tagTv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                    tagTv.setText(String.valueOf(o));
                    return tagTv;
                }
            };

            tagAdapter.setSelectedList(isSelectedSet);

            tagFlowLayout.setMaxSelectCount(1);
            tagFlowLayout.setAdapter(tagAdapter);
            tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                    boolean isSelected = isSelectedSet.contains(i);

                    if (null != onClickListener) {
                        onClickListener.onTagClickListener(title, i, isSelected);
                    }
                    return false;
                }
            });
        }
    }

    public interface OnClickListener {

        void onTagClickListener(String type, int position, boolean isSelected);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
