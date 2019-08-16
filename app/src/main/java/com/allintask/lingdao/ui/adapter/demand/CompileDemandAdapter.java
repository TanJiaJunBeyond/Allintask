package com.allintask.lingdao.ui.adapter.demand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.PublishServiceBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
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
 * Created by TanJiaJun on 2018/6/5.
 */

public class CompileDemandAdapter extends CommonRecyclerViewAdapter {

    private Context mContext;
    private List<Integer> mustSelectedCategoryIdList;

    private List<ServiceCategoryListBean> mServiceCategoryList;
    private int mFirstServiceCategoryIndex;

    public CompileDemandAdapter(Context context) {
        mContext = context;
        mustSelectedCategoryIdList = new ArrayList<>();
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonRecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_publish_demand, parent, false));
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, final int position) {
        PublishServiceBean publishServiceBean = (PublishServiceBean) getItem(position);

        if (null != publishServiceBean) {
            ImageView starIv = holder.getChildView(R.id.iv_star);
            TagFlowLayout tagFlowLayout = holder.getChildView(R.id.tag_flow_layout);

            boolean isShow = TypeUtils.getBoolean(publishServiceBean.isShow, false);
            boolean isRequired = TypeUtils.getBoolean(publishServiceBean.isRequired, false);
            String name = TypeUtils.getString(publishServiceBean.name, "");
            final int maxSelectCount = TypeUtils.getInteger(publishServiceBean.maxSelectCount, 0);
            final List<String> subclassNameList = publishServiceBean.subclassNameList;
            final Set<Integer> isSelectedSet = publishServiceBean.isSelectedSet;
            final List<Integer> isSelectedCategoryIdList = publishServiceBean.isSelectedCategoryIdList;

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.getItemView().getLayoutParams();

            if (isShow) {
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

                holder.getItemView().setVisibility(View.VISIBLE);
            } else {
                layoutParams.width = 0;
                layoutParams.height = 0;

                holder.getItemView().setVisibility(View.GONE);
            }

            if (isRequired) {
                starIv.setVisibility(View.VISIBLE);
            } else {
                starIv.setVisibility(View.GONE);
            }

            holder.setTextView(R.id.tv_title, name);

            TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                @Override
                public View getView(FlowLayout flowLayout, int i, Object o) {
                    TextView tagTv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                    tagTv.setText(String.valueOf(o));
                    return tagTv;
                }
            };

            tagAdapter.setSelectedList(publishServiceBean.isSelectedSet);

            tagFlowLayout.setMaxSelectCount(maxSelectCount);
            tagFlowLayout.setAdapter(tagAdapter);

            final int isSelectedSetSize = isSelectedSet.size();

            tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                    boolean isSelected = isSelectedSet.contains(i);

                    if (maxSelectCount != 1 && isSelectedSetSize >= maxSelectCount) {
                        isSelectedSet.remove(i);
                    } else {
                        if (isSelected) {
                            isSelectedSet.remove(i);
                        } else {
                            isSelectedSet.add(i);
                        }
                    }

                    ServiceCategoryListBean tempServiceCategoryListBean = mServiceCategoryList.get(mFirstServiceCategoryIndex);

                    if (null != tempServiceCategoryListBean) {
                        List<ServiceCategoryListBean.ServiceCategoryFirstBean> tempServiceCategoryFirstList = tempServiceCategoryListBean.sub;

                        if (null != tempServiceCategoryFirstList && tempServiceCategoryFirstList.size() > 0) {
                            ServiceCategoryListBean.ServiceCategoryFirstBean serviceCategoryFirstBean = tempServiceCategoryFirstList.get(position);

                            if (null != serviceCategoryFirstBean) {
                                List<ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean> serviceCategorySecondBeanList = serviceCategoryFirstBean.sub;

                                if (null != serviceCategorySecondBeanList && serviceCategorySecondBeanList.size() > 0) {
                                    ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean serviceCategorySecondBean = serviceCategorySecondBeanList.get(i);

                                    if (null != serviceCategorySecondBean) {
                                        int code = TypeUtils.getInteger(serviceCategorySecondBean.code, -1);

                                        if (maxSelectCount != 1 && isSelectedSetSize >= maxSelectCount) {
                                            if (null != isSelectedCategoryIdList && isSelectedCategoryIdList.size() > 0) {
                                                for (int k = 0; k < isSelectedCategoryIdList.size(); k++) {
                                                    int isSelectedCategory = TypeUtils.getInteger(isSelectedCategoryIdList.get(k), -1);

                                                    if (isSelectedCategory == code) {
                                                        isSelectedCategoryIdList.remove(k);
                                                    }
                                                }
                                            }
                                        } else {
                                            if (isSelected) {
                                                if (null != isSelectedCategoryIdList && isSelectedCategoryIdList.size() > 0) {
                                                    for (int k = 0; k < isSelectedCategoryIdList.size(); k++) {
                                                        int isSelectedCategory = TypeUtils.getInteger(isSelectedCategoryIdList.get(k), -1);

                                                        if (isSelectedCategory == code) {
                                                            isSelectedCategoryIdList.remove(k);
                                                        }
                                                    }
                                                }
                                            } else {
                                                isSelectedCategoryIdList.add(code);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    notifyItemChanged(position);
                    return false;
                }
            });
        }
    }

    public void setServiceCategoryList(List<ServiceCategoryListBean> serviceCategoryList) {
        mServiceCategoryList = serviceCategoryList;
    }

    public void setFirstServiceCategoryIndex(int firstServiceCategoryIndex) {
        mFirstServiceCategoryIndex = firstServiceCategoryIndex;
    }

    public List<Integer> getMustSelectedCategoryIdList() {
        return mustSelectedCategoryIdList;
    }

    public void addMustSelectedCategoryId(int mustSelectedCategoryId) {
        mustSelectedCategoryIdList.add(mustSelectedCategoryId);
    }

    public String getServiceCategoryListString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (null != mList && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                PublishServiceBean publishServiceBean = (PublishServiceBean) mList.get(i);

                if (null != publishServiceBean) {
                    int categoryId = TypeUtils.getInteger(publishServiceBean.categoryId, -1);
                    List<Integer> isSelectedCategoryIdList = publishServiceBean.isSelectedCategoryIdList;

                    if (null != isSelectedCategoryIdList && isSelectedCategoryIdList.size() > 0) {
                        stringBuilder.append(categoryId).append(":");

                        for (int j = 0; j < isSelectedCategoryIdList.size(); j++) {
                            int subclassCategoryId = TypeUtils.getInteger(isSelectedCategoryIdList.get(j), -1);
                            stringBuilder.append(subclassCategoryId);

                            if (j < isSelectedCategoryIdList.size() - 1) {
                                stringBuilder.append(",");
                            }

                            if (j == isSelectedCategoryIdList.size() - 1) {
                                stringBuilder.append(";");
                            }
                        }
                    }
                }

                if (!TextUtils.isEmpty(stringBuilder) && i == mList.size() - 1) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
            }
        }
        return stringBuilder.toString();
    }

}
