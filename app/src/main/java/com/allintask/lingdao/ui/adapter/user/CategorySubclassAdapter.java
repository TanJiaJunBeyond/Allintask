package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.CategoryBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class CategorySubclassAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public CategorySubclassAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_subclass, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        CategoryBean.FirstSubBean firstSubBean = (CategoryBean.FirstSubBean) getItem(position);

        if (null != firstSubBean) {
            TagFlowLayout tagFlowLayout = holder.getChildView(R.id.tag_flow_layout);

            String firstName = TypeUtils.getString(firstSubBean.name, "");
            final List<CategoryBean.FirstSubBean.SecondSubBean> secondSubList = firstSubBean.sub;
            final Set<Integer> isSelectedSet = firstSubBean.isSelectedSet;
            final List<Integer> isSelectedCategoryIdList = firstSubBean.isSelectedCategoryIdList;

            holder.setTextView(R.id.tv_title, firstName, true);

            List<String> subclassNameList = new ArrayList<>();

            if (null != secondSubList && secondSubList.size() > 0) {
                for (int i = 0; i < secondSubList.size(); i++) {
                    CategoryBean.FirstSubBean.SecondSubBean secondSubBean = secondSubList.get(i);

                    if (null != secondSubBean) {
                        String name = TypeUtils.getString(secondSubBean.name, "");
                        subclassNameList.add(name);
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
            tagFlowLayout.setAdapter(tagAdapter);
            tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                    boolean isSelected = isSelectedSet.contains(i);

                    if (isSelected) {
                        isSelectedSet.remove(i);

                        if (null != secondSubList && secondSubList.size() > 0) {
                            CategoryBean.FirstSubBean.SecondSubBean secondSubBean = secondSubList.get(i);

                            if (null != secondSubBean) {
                                int code = TypeUtils.getInteger(secondSubBean.code, -1);

                                if (null != isSelectedCategoryIdList && isSelectedCategoryIdList.size() > 0 && code != -1) {
                                    for (int j = 0; j < isSelectedCategoryIdList.size(); j++) {
                                        Integer isSelectedCategoryIdInteger = isSelectedCategoryIdList.get(j);

                                        if (null != isSelectedCategoryIdInteger && isSelectedCategoryIdInteger == code) {
                                            isSelectedCategoryIdList.remove(j);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        isSelectedSet.add(i);

                        if (null != secondSubList && secondSubList.size() > 0) {
                            CategoryBean.FirstSubBean.SecondSubBean secondSubBean = secondSubList.get(i);

                            if (null != secondSubBean) {
                                int code = TypeUtils.getInteger(secondSubBean.code, -1);

                                if (code != -1) {
                                    isSelectedCategoryIdList.add(code);
                                }
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

}
