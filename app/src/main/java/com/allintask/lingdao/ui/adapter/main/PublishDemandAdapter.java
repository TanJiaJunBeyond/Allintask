package com.allintask.lingdao.ui.adapter.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.DemandPeriodListBean;
import com.allintask.lingdao.bean.service.PublishServiceBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeListBean;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.ui.adapter.user.CityAdapter;
import com.allintask.lingdao.ui.adapter.user.ProvinceAdapter;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.widget.CircleImageView;
import com.allintask.lingdao.widget.SeeExampleDialog;
import com.allintask.lingdao.widget.SelectServiceCityDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/8.
 */

public class PublishDemandAdapter extends CommonRecyclerViewAdapter {

    private static final int ITEM_PUBLISH_DEMAND_HEADER = 0;
    private static final int ITEM_PUBLISH_DEMAND = 1;
    private static final int ITEM_PUBLISH_DEMAND_BOTTOM = 2;

    private Context context;
    private int publishDemandType = CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON;

    private InputMethodManager inputMethodManager;

    private String userHeadPortraitUrl;
    private String name;
    private int gender;
    private String age;
    private String serviceCategory;
    private String distance;
    private String serviceWayPriceUnitString;

    private List<ServiceModeAndPriceModeBean> addServiceModeAndPriceModeList;
    private List<Integer> mustSelectedCategoryIdList;
    private Calendar calendar;

    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    private List<ServiceCategoryListBean> serviceCategoryList;
    private List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList;
    private ArrayList<Integer> mServiceWayIdArrayList;
    private ArrayList<String> mServicePriceArrayList;
    private List<DemandPeriodListBean> demandPeriodList;

    private int firstServiceCategoryIndex = -1;
    private int mCategoryId = -1;
    private String mCategoryName;

    private int serviceModeId = -1;

    private boolean mIsNeedAddress = false;
    private List<IsAllBean> mAddressList;

    private String mProvinceCode;
    private String mCityCode;

    private SelectServiceCityDialog selectServiceCityDialog;
    private SeeExampleDialog seeExampleDialog;

    private String subscribeStartTime;
    private String publishDemandSubscribeStartTime;
    private int mEmploymentTime = 0;
    private int mEmploymentPeriod = 0;
    private int mEmploymentTimeServicePrice = 0;
    private int mServicePrice = 0;
    private int mServicePayment = 0;
    private int mShowEmploymentTimes = 0;
    private int mMinEmploymentTime = 0;
    private int mMaxEmploymentTime = 0;
    private int mDemandMaxBudget = 0;
    private int mEmploymentTimeServicePayment = 0;
    private String mEmploymentTimeUnit;
    private int mPriceUnitId = -1;
    private String mPriceUnit;
    private int mMinEmploymentPeriod;
    private int mMaxEmploymentPeriod;
    private int aggregateDemandBudget;
    private String demandIntroduction;
    private int recommendMoreStatus = CommonConstant.DEMAND_IS_NOT_SHARE;

    private EditText employmentTimeEt;
    private TextView employmentTimeServicePaymentTv;

    private EditText employmentPeriodEt;
    private TextView servicePaymentTv;

    private TimePickerBuilder timePickerBuilder;

    private OnClickListener onClickListener;

    public PublishDemandAdapter(Context context, int publishDemandType) {
        this.context = context;
        this.publishDemandType = publishDemandType;

        inputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);

        addServiceModeAndPriceModeList = new ArrayList<>();
        mustSelectedCategoryIdList = new ArrayList<>();
        calendar = Calendar.getInstance();

        provinceAdapter = new ProvinceAdapter(context);
        cityAdapter = new CityAdapter(context);
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = null;

        switch (viewType) {
            case ITEM_PUBLISH_DEMAND_HEADER:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_publish_demand_header, parent, false));
                break;

            case ITEM_PUBLISH_DEMAND:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_publish_demand, parent, false));
                break;

            case ITEM_PUBLISH_DEMAND_BOTTOM:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_publish_demand_bottom, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        switch (getItemViewType(position)) {
            case ITEM_PUBLISH_DEMAND_HEADER:
                onBindHeaderItemView(holder);
                break;

            case ITEM_PUBLISH_DEMAND:
                onBindItemView(holder, position);
                break;

            case ITEM_PUBLISH_DEMAND_BOTTOM:
                onBindBottomItemView(holder);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (publishDemandType) {
            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON:
                if (position >= 0 && position < mList.size()) {
                    return ITEM_PUBLISH_DEMAND;
                } else {
                    return ITEM_PUBLISH_DEMAND_BOTTOM;
                }

            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE:
                if (position == 0) {
                    return ITEM_PUBLISH_DEMAND_HEADER;
                } else if (position < mList.size() + 1) {
                    return ITEM_PUBLISH_DEMAND;
                } else {
                    return ITEM_PUBLISH_DEMAND_BOTTOM;
                }

            default:
                return ITEM_PUBLISH_DEMAND_BOTTOM;
        }
    }

    @Override
    public int getItemCount() {
        switch (publishDemandType) {
            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON:
                return mList.size() + 1;

            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE:
                return mList.size() + 2;

            default:
                return 0;
        }
    }

    private void onBindHeaderItemView(CommonRecyclerViewHolder holder) {
        CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);
        ImageView genderIv = holder.getChildView(R.id.iv_gender);
        SwitchCompat switchCompat = holder.getChildView(R.id.sc_recommend_more);

        if (!TextUtils.isEmpty(userHeadPortraitUrl)) {
            ImageViewUtil.setImageView(context, headPortraitCIV, userHeadPortraitUrl, R.mipmap.ic_default_avatar);
            headPortraitCIV.setVisibility(View.VISIBLE);
        } else {
            headPortraitCIV.setVisibility(View.GONE);
        }

        holder.setTextView(R.id.tv_name, name, true);

        if (gender == CommonConstant.MALE) {
            genderIv.setImageResource(R.mipmap.ic_male);
            genderIv.setVisibility(View.VISIBLE);
        } else if (gender == CommonConstant.FEMALE) {
            genderIv.setImageResource(R.mipmap.ic_female);
            genderIv.setVisibility(View.VISIBLE);
        } else {
            genderIv.setVisibility(View.GONE);
        }

        holder.setTextView(R.id.tv_age, age, true);
        holder.setTextView(R.id.tv_service_category_title, serviceCategory, true);

        if (!TextUtils.isEmpty(distance)) {
            holder.setTextView(R.id.tv_distance, distance);
        } else {
            holder.setTextView(R.id.tv_distance, "0m");
        }

        holder.setTextView(R.id.tv_service_offer, serviceWayPriceUnitString);
        holder.setTextView(R.id.tv_demand_category, serviceCategory);

        if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
            switchCompat.setChecked(false);
        } else if (recommendMoreStatus == CommonConstant.DEMAND_IS_SHARE) {
            switchCompat.setChecked(true);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recommendMoreStatus = CommonConstant.DEMAND_IS_SHARE;

                    if (null != mList) {
                        for (int i = 0; i < mList.size(); i++) {
                            PublishServiceBean publishServiceBean = (PublishServiceBean) mList.get(i);

                            if (null != publishServiceBean) {
                                if (i == mList.size() - 1) {
                                    publishServiceBean.isShow = false;
                                } else {
                                    publishServiceBean.isShow = true;
                                }
                            }
                        }

                        notifyItemRangeChanged(1, mList.size() + 2);
                    }
                } else {
                    recommendMoreStatus = CommonConstant.DEMAND_IS_NOT_SHARE;

                    if (null != mList) {
                        for (int i = 0; i < mList.size(); i++) {
                            PublishServiceBean publishServiceBean = (PublishServiceBean) mList.get(i);

                            if (null != publishServiceBean) {
//                                    if (i == mList.size() - 1) {
//                                        publishServiceBean.isShow = true;
//                                    } else {
                                publishServiceBean.isShow = false;
//                                    }
                            }
                        }

                        notifyItemRangeChanged(1, mList.size() + 2);
                    }
                }

                if (null != onClickListener) {
                    onClickListener.onSwitchCompatCheckedChange(isChecked);
                }
            }
        });
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, final int position) {
        PublishServiceBean publishServiceBean;

        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
            publishServiceBean = (PublishServiceBean) getItem(position);
        } else {
            publishServiceBean = (PublishServiceBean) getItem(position - 1);
        }

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
                    TextView tagTv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
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

                    if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON && position == 0) {
                        if (isSelected) {
                            firstServiceCategoryIndex = -1;
                            mCategoryId = -1;
                            mCategoryName = null;
                            notifyItemRangeRemoved(position + 1, mList.size() - 1);

                            for (int j = mList.size() - 1; j > 0; j--) {
                                mList.remove(j);
                            }

                            addServiceModeAndPriceModeList.clear();
                            mustSelectedCategoryIdList.clear();

                            for (int j = 0; j < serviceCategoryList.size(); j++) {
                                isSelectedSet.clear();
                            }

                            mIsNeedAddress = false;

                            mShowEmploymentTimes = 0;

                            mEmploymentTime = 0;
                            mMinEmploymentTime = 0;
                            mMaxEmploymentTime = 0;
                            mEmploymentTimeUnit = null;

                            mEmploymentPeriod = 0;
                            mMinEmploymentPeriod = 0;
                            mMaxEmploymentPeriod = 0;

                            mPriceUnitId = -1;
                            mPriceUnit = null;

                            notifyItemChanged(position);

                            if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                                notifyItemChanged(mList.size());
                            } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                                notifyItemChanged(mList.size() + 1);
                            }
                        } else {
                            firstServiceCategoryIndex = -1;
                            mCategoryId = -1;
                            mCategoryName = null;
                            notifyItemRangeRemoved(position + 1, mList.size() - 1);

                            for (int j = mList.size() - 1; j > 0; j--) {
                                mList.remove(j);
                            }

                            addServiceModeAndPriceModeList.clear();
                            mustSelectedCategoryIdList.clear();

                            for (int j = 0; j < serviceCategoryList.size(); j++) {
                                isSelectedSet.clear();
                            }

                            mIsNeedAddress = false;

                            mShowEmploymentTimes = 0;

                            mEmploymentTime = 0;
                            mMinEmploymentTime = 0;
                            mMaxEmploymentTime = 0;
                            mEmploymentTimeUnit = null;

                            mEmploymentPeriod = 0;
                            mMinEmploymentPeriod = 0;
                            mMaxEmploymentPeriod = 0;

                            mPriceUnitId = -1;
                            mPriceUnit = null;

                            notifyItemChanged(position);

                            if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                                notifyItemChanged(mList.size());
                            } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                                notifyItemChanged(mList.size() + 1);
                            }

                            ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(i);
                            int categoryId = TypeUtils.getInteger(serviceCategoryListBean.code, 0);
                            String name = TypeUtils.getString(serviceCategoryListBean.name, "");
                            List<ServiceCategoryListBean.ServiceCategoryFirstBean> serviceCategoryFirstList = serviceCategoryListBean.sub;

                            isSelectedSet.add(i);
                            firstServiceCategoryIndex = i;
                            mCategoryId = categoryId;
                            mCategoryName = name;

                            if (null != onClickListener) {
                                onClickListener.onTagClickListener(mCategoryId);
                            }

                            if (null != serviceCategoryFirstList && serviceCategoryFirstList.size() > 0) {
                                for (int j = 0; j < serviceCategoryFirstList.size(); j++) {
                                    ServiceCategoryListBean.ServiceCategoryFirstBean serviceCategoryFirstBean = serviceCategoryFirstList.get(j);
                                    int subclassCategoryId = TypeUtils.getInteger(serviceCategoryFirstBean.code, -1);
                                    int mustSelect = TypeUtils.getInteger(serviceCategoryFirstBean.mustSelect, 0);
                                    String firstName = TypeUtils.getString(serviceCategoryFirstBean.name, "");
                                    int maxSelectLen = TypeUtils.getInteger(serviceCategoryFirstBean.maxSelectLen, 0);
                                    List<ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean> serviceCategorySecondList = serviceCategoryFirstList.get(j).sub;

                                    if (null != serviceCategorySecondList && serviceCategorySecondList.size() > 0) {
                                        List<String> subclassNameList = new ArrayList<>();
                                        Set<Integer> isSelectedSet = new HashSet<>();
                                        List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                                        for (int k = 0; k < serviceCategorySecondList.size(); k++) {
                                            ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean serviceCategorySecondBean = serviceCategorySecondList.get(k);
                                            String secondName = TypeUtils.getString(serviceCategorySecondBean.name, "");
                                            subclassNameList.add(secondName);
                                        }

                                        PublishServiceBean tempPublishServiceBean = new PublishServiceBean();

                                        switch (publishDemandType) {
                                            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON:
                                                tempPublishServiceBean.isShow = true;
                                                break;

                                            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE:
                                                if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
                                                    tempPublishServiceBean.isShow = false;
                                                } else if (recommendMoreStatus == CommonConstant.DEMAND_IS_SHARE) {
                                                    tempPublishServiceBean.isShow = true;
                                                }
                                                break;
                                        }

                                        if (mustSelect == 0) {
                                            tempPublishServiceBean.isRequired = false;
                                        } else {
                                            tempPublishServiceBean.isRequired = true;
                                            mustSelectedCategoryIdList.add(subclassCategoryId);
                                        }

                                        tempPublishServiceBean.categoryId = subclassCategoryId;

                                        if (maxSelectLen == 1) {
                                            tempPublishServiceBean.name = firstName + "（单选）";
                                        } else if (maxSelectLen == 100) {
                                            tempPublishServiceBean.name = firstName + "（多选）";
                                        } else {
                                            tempPublishServiceBean.name = firstName + "（" + maxSelectLen + "个）";
                                        }

                                        tempPublishServiceBean.maxSelectCount = maxSelectLen;
                                        tempPublishServiceBean.subclassNameList = subclassNameList;
                                        tempPublishServiceBean.isSelectedSet = isSelectedSet;
                                        tempPublishServiceBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                                        mList.add(tempPublishServiceBean);
                                    }
                                }

                                notifyItemRangeInserted(position + 1, serviceCategoryFirstList.size());
                            }
                        }
                    } else if (serviceModeAndPriceModeList.size() > 1 && publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON ? position == mList.size() - 1 : position == mList.size()) {
                        ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(i);
                        String serviceModeName = serviceModeAndPriceModeBean.serveWayName;
                        Integer isNeedAddress = serviceModeAndPriceModeBean.isNeedAddress;
                        mShowEmploymentTimes = TypeUtils.getInteger(serviceModeAndPriceModeBean.showEmploymentTimes, 0);
                        mEmploymentTimeUnit = TypeUtils.getString(serviceModeAndPriceModeBean.priceUnit);
                        mPriceUnitId = TypeUtils.getInteger(serviceModeAndPriceModeBean.employmentCycleUnitId, -1);
                        mPriceUnit = TypeUtils.getString(serviceModeAndPriceModeBean.employmentCycleUnit, "");
                        Integer minValue = serviceModeAndPriceModeBean.employmentCycleMinValue;
                        Integer maxValue = serviceModeAndPriceModeBean.employmentCycleMaxValue;

                        if (null == isNeedAddress || isNeedAddress == 0) {
                            mIsNeedAddress = false;
                        } else if (isNeedAddress == 1) {
                            mIsNeedAddress = true;
                        }

                        if (null == minValue) {
                            mMinEmploymentPeriod = 0;
                        } else {
                            mMinEmploymentPeriod = minValue;
                        }

                        if (null == maxValue) {
                            mMaxEmploymentPeriod = 0;
                        } else {
                            mMaxEmploymentPeriod = maxValue;
                        }

                        mEmploymentPeriod = mMinEmploymentPeriod;

                        if (null != mServicePriceArrayList && mServicePriceArrayList.size() > 0) {
                            String servicePriceString = mServicePriceArrayList.get(i);

                            if (!TextUtils.isEmpty(servicePriceString)) {
                                mServicePrice = Integer.valueOf(servicePriceString);
                            }
                        }

                        if (isSelected) {
                            for (int m = 0; m < addServiceModeAndPriceModeList.size(); m++) {
                                String tempServiceModeName = addServiceModeAndPriceModeList.get(m).serveWayName;

                                if (tempServiceModeName.equals(serviceModeName)) {
                                    isSelectedSet.remove(i);
                                    addServiceModeAndPriceModeList.remove(m);
                                }
                            }

                            mIsNeedAddress = false;

                            mShowEmploymentTimes = 0;

                            mEmploymentTime = 0;
                            mMinEmploymentTime = 0;
                            mMaxEmploymentTime = 0;
                            mEmploymentTimeUnit = null;

                            mEmploymentPeriod = 0;
                            mMinEmploymentPeriod = 0;
                            mMaxEmploymentPeriod = 0;

                            mPriceUnitId = -1;
                            mPriceUnit = null;

                            if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                                notifyItemChanged(mList.size());
                            } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                                notifyItemChanged(mList.size() + 1);
                            }
                        } else {
                            isSelectedSet.clear();
                            isSelectedSet.add(i);

                            addServiceModeAndPriceModeList.add(serviceModeAndPriceModeBean);

                            if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                                notifyItemChanged(mList.size());
                            } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                                notifyItemChanged(mList.size() + 1);
                            }
                        }
                    } else {
                        if (maxSelectCount != 1 && isSelectedSetSize >= maxSelectCount) {
                            isSelectedSet.remove(i);
                        } else {
                            if (isSelected) {
                                isSelectedSet.remove(i);
                            } else {
                                isSelectedSet.add(i);
                            }
                        }

                        ServiceCategoryListBean tempServiceCategoryListBean = serviceCategoryList.get(firstServiceCategoryIndex);

                        if (null != tempServiceCategoryListBean) {
                            List<ServiceCategoryListBean.ServiceCategoryFirstBean> tempServiceCategoryFirstList = tempServiceCategoryListBean.sub;

                            if (null != tempServiceCategoryFirstList && tempServiceCategoryFirstList.size() > 0) {
                                ServiceCategoryListBean.ServiceCategoryFirstBean serviceCategoryFirstBean = tempServiceCategoryFirstList.get(position - 1);

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
                    }
                    return false;
                }
            });
        }
    }

    private void onBindBottomItemView(final CommonRecyclerViewHolder holder) {
        LinearLayout allServiceCityLL = holder.getChildView(R.id.ll_all_service_city);
        LinearLayout serviceCityLL = holder.getChildView(R.id.ll_service_city);
        TextView serviceCityTv = holder.getChildView(R.id.tv_service_city);
        LinearLayout allSubscribeStartTimeLL = holder.getChildView(R.id.ll_all_subscribe_start_time);
        RelativeLayout workStartTimeRL = holder.getChildView(R.id.rl_work_start_time);
        final TextView workStartTimeTv = holder.getChildView(R.id.tv_work_start_time);
        TagFlowLayout tagFlowLayout = holder.getChildView(R.id.tag_flow_layout);
        LinearLayout allEmploymentTimeLL = holder.getChildView(R.id.ll_all_employment_time);
        TextView employmentTimeDecreaseTv = holder.getChildView(R.id.tv_employment_time_decrease);
        LinearLayout employmentTimeSubclassLL = holder.getChildView(R.id.ll_employment_time_subclass);
        employmentTimeEt = holder.getChildView(R.id.et_employment_time);
        TextView employmentTimeUnitTv = holder.getChildView(R.id.tv_employment_time_unit);
        TextView employmentTimeIncreaseTv = holder.getChildView(R.id.tv_employment_time_increase);
        employmentTimeServicePaymentTv = holder.getChildView(R.id.tv_employment_time_service_payment);
        LinearLayout allEmploymentPeriodLL = holder.getChildView(R.id.ll_all_employment_period);
        TextView decreaseTv = holder.getChildView(R.id.tv_decrease);
        LinearLayout employmentPeriodSubclassLL = holder.getChildView(R.id.ll_employment_period_subclass);
        employmentPeriodEt = holder.getChildView(R.id.et_employment_period);
        TextView employmentPeriodUnitTv = holder.getChildView(R.id.tv_employment_period_unit);
        TextView increaseTv = holder.getChildView(R.id.tv_increase);
        LinearLayout servicePaymentLL = holder.getChildView(R.id.ll_service_payment);
        servicePaymentTv = holder.getChildView(R.id.tv_service_payment);
        LinearLayout employmentPriceLL = holder.getChildView(R.id.ll_employment_price);
        RelativeLayout aggregateDemandBudgetRL = holder.getChildView(R.id.rl_aggregate_demand_budget);
        final EditText aggregateDemandBudgetEt = holder.getChildView(R.id.et_aggregate_demand_budget);
        LinearLayout demandIntroductionLL = holder.getChildView(R.id.ll_demand_introduction);
        TextView seeExampleTv = holder.getChildView(R.id.tv_see_example);
        ImageView demandIntroductionStarIv = holder.getChildView(R.id.iv_demand_introduction_star);
        final EditText demandIntroductionEt = holder.getChildView(R.id.et_demand_introduction);

        employmentPeriodEt.setFocusable(false);
        aggregateDemandBudgetEt.setFocusable(false);
        demandIntroductionEt.setFocusable(false);

        if (null != inputMethodManager && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(employmentPeriodEt.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(aggregateDemandBudgetEt.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(demandIntroductionEt.getWindowToken(), 0);
        }

        employmentTimeUnitTv.setText(mEmploymentTimeUnit);

        if (mPriceUnitId != -1) {
            employmentPeriodUnitTv.setText(mPriceUnit);
            allEmploymentPeriodLL.setVisibility(View.VISIBLE);
        } else {
            allEmploymentPeriodLL.setVisibility(View.GONE);
        }

        switch (publishDemandType) {
            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON:
                if (mIsNeedAddress) {
                    allServiceCityLL.setVisibility(View.VISIBLE);
                } else {
                    allServiceCityLL.setVisibility(View.GONE);
                }

                allEmploymentTimeLL.setVisibility(View.GONE);
                servicePaymentLL.setVisibility(View.GONE);
                break;


            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE:
                if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
                    allServiceCityLL.setVisibility(View.GONE);
//                demandIntroductionStarIv.setVisibility(View.GONE);

                    if (mShowEmploymentTimes == 0) {
                        servicePaymentLL.setVisibility(View.VISIBLE);
                        allEmploymentTimeLL.setVisibility(View.GONE);
                    } else {
                        employmentPeriodEt.setText(String.valueOf(mEmploymentPeriod));

                        servicePaymentLL.setVisibility(View.GONE);
                        allEmploymentTimeLL.setVisibility(View.VISIBLE);
                    }

                    allSubscribeStartTimeLL.setVisibility(View.GONE);
                    employmentPriceLL.setVisibility(View.GONE);
                    demandIntroductionLL.setVisibility(View.GONE);
                } else if (recommendMoreStatus == CommonConstant.DEMAND_IS_SHARE) {
//                allServiceCityLL.setVisibility(View.VISIBLE);
//                demandIntroductionStarIv.setVisibility(View.VISIBLE);

                    if (mIsNeedAddress) {
                        allServiceCityLL.setVisibility(View.VISIBLE);
                    } else {
                        allServiceCityLL.setVisibility(View.GONE);
                    }

                    allEmploymentTimeLL.setVisibility(View.GONE);
                    allSubscribeStartTimeLL.setVisibility(View.VISIBLE);
                    servicePaymentLL.setVisibility(View.GONE);
                    employmentPriceLL.setVisibility(View.VISIBLE);
                    demandIntroductionLL.setVisibility(View.VISIBLE);
                }
                break;
        }

        if (null != mAddressList && mAddressList.size() > 0) {
            String province = null;
            String city = null;

            for (int i = 0; i < mAddressList.size(); i++) {
                IsAllBean isAllBean = mAddressList.get(i);

                if (null != isAllBean) {
                    String tempProvince = TypeUtils.getString(isAllBean.name, "");
                    List<AddressSubBean> cityList = isAllBean.sub;
                    boolean isProvinceSelected = TypeUtils.getBoolean(isAllBean.isSelected, false);

                    if (isProvinceSelected) {
                        province = tempProvince;

                        if (null != cityList && cityList.size() > 0) {
                            for (int j = 0; j < cityList.size(); j++) {
                                AddressSubBean addressSubBean = cityList.get(j);

                                if (null != addressSubBean) {
                                    String tempCity = TypeUtils.getString(addressSubBean.name, "");
                                    boolean isCitySelected = TypeUtils.getBoolean(addressSubBean.isSelected, false);

                                    if (isCitySelected) {
                                        city = tempCity;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            String address;

            if (TextUtils.isEmpty(city)) {
                address = province;
            } else {
                if (!TextUtils.isEmpty(province) && province.equals(city)) {
                    address = province;
                } else {
                    address = province + "\r\r" + city;
                }
            }

            if (!TextUtils.isEmpty(address)) {
                serviceCityTv.setText(address);
            } else {
                serviceCityTv.setText(context.getString(R.string.please_select));
            }

            if (!TextUtils.isEmpty(subscribeStartTime)) {
                workStartTimeTv.setText(subscribeStartTime);
            }
        }

        int isSelectedPosition = -1;
        List<String> demandPeriodNameList = new ArrayList<>();

        if (null != demandPeriodList && demandPeriodList.size() > 0) {
            for (int i = 0; i < demandPeriodList.size(); i++) {
                DemandPeriodListBean demandPeriodListBean = demandPeriodList.get(i);

                if (null != demandPeriodListBean) {
                    String value = TypeUtils.getString(demandPeriodListBean.value, "");
                    boolean isSelected = TypeUtils.getBoolean(demandPeriodListBean.isSelected, false);

                    demandPeriodNameList.add(value);

                    if (isSelected) {
                        isSelectedPosition = i;
                    }
                }
            }
        }

        tagFlowLayout.setMaxSelectCount(1);

        TagAdapter tagAdapter = new TagAdapter(demandPeriodNameList) {
            @Override
            public View getView(FlowLayout flowLayout, int i, Object o) {
                TextView tagTv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                tagTv.setText(String.valueOf(o));
                return tagTv;
            }
        };

        if (isSelectedPosition != -1) {
            tagAdapter.setSelectedList(isSelectedPosition);
        }

        tagFlowLayout.setAdapter(tagAdapter);

        decreaseTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String employmentPeriodString = employmentPeriodEt.getText().toString().trim();

                        if (!TextUtils.isEmpty(employmentPeriodString)) {
                            int employmentPeriod = Integer.valueOf(employmentPeriodString);

                            if (null != onClickListener) {
                                onClickListener.onDecreaseTouch(mMinEmploymentPeriod, mMaxEmploymentPeriod, employmentPeriod);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (null != onClickListener) {
                            onClickListener.onEmploymentPeriodActionUp();
                        }
                        break;
                }
                return true;
            }
        });

        employmentPeriodSubclassLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employmentPeriodEt.setFocusable(true);
                employmentPeriodEt.setFocusableInTouchMode(true);
                employmentPeriodEt.requestFocus();
                employmentPeriodEt.findFocus();

                String employmentPeriod = employmentPeriodEt.getText().toString().trim();
                employmentPeriodEt.setSelection(employmentPeriod.length());

                inputMethodManager.showSoftInput(employmentPeriodEt, InputMethodManager.SHOW_FORCED);
            }
        });

        employmentPeriodEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String employmentPeriodString = employmentPeriodEt.getText().toString().trim();

                if (!TextUtils.isEmpty(employmentPeriodString)) {
                    mEmploymentPeriod = Integer.valueOf(employmentPeriodString);
                } else {
                    mEmploymentPeriod = 0;
                }

                mServicePayment = mServicePrice * mEmploymentPeriod;
                servicePaymentTv.setText("￥" + String.valueOf(mServicePayment));

                if (null != onClickListener) {
                    onClickListener.onCheckEmploymentPeriod(mMinEmploymentPeriod, mMaxEmploymentPeriod, mEmploymentPeriod);
                }
            }
        });

        employmentPeriodEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employmentPeriodEt.setFocusable(true);
                employmentPeriodEt.setFocusableInTouchMode(true);
                employmentPeriodEt.requestFocus();
                employmentPeriodEt.findFocus();

                inputMethodManager.showSoftInput(employmentPeriodEt, InputMethodManager.SHOW_FORCED);
            }
        });

        increaseTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String employmentPeriodString = employmentPeriodEt.getText().toString().trim();

                        if (!TextUtils.isEmpty(employmentPeriodString)) {
                            int employmentPeriod = Integer.valueOf(employmentPeriodString);

                            if (null != onClickListener) {
                                onClickListener.onIncreaseTouch(mMinEmploymentPeriod, mMaxEmploymentPeriod, employmentPeriod);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (null != onClickListener) {
                            onClickListener.onEmploymentPeriodActionUp();
                        }
                        break;
                }
                return true;
            }
        });

        decreaseTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String employmentPeriodString = employmentPeriodEt.getText().toString().trim();

                        if (!TextUtils.isEmpty(employmentPeriodString)) {
                            int employmentPeriod = Integer.valueOf(employmentPeriodString);

                            if (null != onClickListener) {
                                onClickListener.onDecreaseTouch(mMinEmploymentPeriod, mMaxEmploymentPeriod, employmentPeriod);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (null != onClickListener) {
                            onClickListener.onEmploymentPeriodActionUp();
                        }
                        break;
                }
                return true;
            }
        });

        employmentPeriodSubclassLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employmentPeriodEt.setFocusable(true);
                employmentPeriodEt.setFocusableInTouchMode(true);
                employmentPeriodEt.requestFocus();
                employmentPeriodEt.findFocus();

                String employmentPeriod = employmentPeriodEt.getText().toString().trim();
                employmentPeriodEt.setSelection(employmentPeriod.length());

                inputMethodManager.showSoftInput(employmentPeriodEt, InputMethodManager.SHOW_FORCED);
            }
        });

        employmentPeriodEt.setText(String.valueOf(mEmploymentPeriod));
        employmentPeriodEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String employmentPeriodString = employmentPeriodEt.getText().toString().trim();

                if (!TextUtils.isEmpty(employmentPeriodString)) {
                    mEmploymentPeriod = Integer.valueOf(employmentPeriodString);
                } else {
                    mEmploymentPeriod = 0;
                }

                mServicePayment = mServicePrice * mEmploymentPeriod;
                servicePaymentTv.setText("￥" + String.valueOf(mServicePayment));

                if (null != onClickListener) {
                    onClickListener.onCheckEmploymentPeriod(mMinEmploymentPeriod, mMaxEmploymentPeriod, mEmploymentPeriod);
                }
            }
        });

        employmentPeriodEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employmentPeriodEt.setFocusable(true);
                employmentPeriodEt.setFocusableInTouchMode(true);
                employmentPeriodEt.requestFocus();
                employmentPeriodEt.findFocus();

                inputMethodManager.showSoftInput(employmentPeriodEt, InputMethodManager.SHOW_FORCED);
            }
        });

        increaseTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String employmentPeriodString = employmentPeriodEt.getText().toString().trim();

                        if (!TextUtils.isEmpty(employmentPeriodString)) {
                            int employmentPeriod = Integer.valueOf(employmentPeriodString);

                            if (null != onClickListener) {
                                onClickListener.onIncreaseTouch(mMinEmploymentPeriod, mMaxEmploymentPeriod, employmentPeriod);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (null != onClickListener) {
                            onClickListener.onEmploymentPeriodActionUp();
                        }
                        break;
                }
                return true;
            }
        });

        servicePaymentTv.setText("￥" + String.valueOf(mServicePayment));

        employmentTimeDecreaseTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String employmentPeriodString = employmentTimeEt.getText().toString().trim();

                        if (!TextUtils.isEmpty(employmentPeriodString)) {
                            int employmentPeriod = Integer.valueOf(employmentPeriodString);

                            if (null != onClickListener) {
                                onClickListener.onEmploymentTimeDecreaseTouch(mMinEmploymentTime, mMaxEmploymentTime, employmentPeriod);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (null != onClickListener) {
                            onClickListener.onEmploymentTimeActionUp();
                        }
                        break;
                }
                return true;
            }
        });

        employmentTimeSubclassLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employmentTimeEt.setFocusable(true);
                employmentTimeEt.setFocusableInTouchMode(true);
                employmentTimeEt.requestFocus();
                employmentTimeEt.findFocus();

                String employmentPeriod = employmentTimeEt.getText().toString().trim();
                employmentTimeEt.setSelection(employmentPeriod.length());

                inputMethodManager.showSoftInput(employmentTimeEt, InputMethodManager.SHOW_FORCED);
            }
        });

        employmentTimeEt.setText(String.valueOf(mEmploymentTime));
        employmentTimeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String employmentPeriodString = employmentTimeEt.getText().toString().trim();

                if (!TextUtils.isEmpty(employmentPeriodString)) {
                    mEmploymentTime = Integer.valueOf(employmentPeriodString);
                } else {
                    mEmploymentTime = 0;
                }

                mEmploymentTimeServicePayment = mServicePrice * mEmploymentTime;
                employmentTimeServicePaymentTv.setText("￥" + String.valueOf(mEmploymentTimeServicePayment));

                if (null != onClickListener) {
                    onClickListener.onCheckEmploymentTime(mMinEmploymentTime, mMaxEmploymentTime, mEmploymentTime);
                }
            }
        });

        employmentTimeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employmentTimeEt.setFocusable(true);
                employmentTimeEt.setFocusableInTouchMode(true);
                employmentTimeEt.requestFocus();
                employmentTimeEt.findFocus();

                inputMethodManager.showSoftInput(employmentTimeEt, InputMethodManager.SHOW_FORCED);
            }
        });

        mEmploymentTimeServicePayment = mServicePrice * mEmploymentTime;
        employmentTimeServicePaymentTv.setText("￥" + String.valueOf(mEmploymentTimeServicePayment));

        employmentTimeIncreaseTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String employmentPeriodString = employmentTimeEt.getText().toString().trim();

                        if (!TextUtils.isEmpty(employmentPeriodString)) {
                            int employmentPeriod = Integer.valueOf(employmentPeriodString);

                            if (null != onClickListener) {
                                onClickListener.onEmploymentTimeIncreaseTouch(mMinEmploymentTime, mMaxEmploymentTime, employmentPeriod);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (null != onClickListener) {
                            onClickListener.onEmploymentTimeActionUp();
                        }
                        break;
                }
                return true;
            }
        });

        aggregateDemandBudgetRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggregateDemandBudgetEt.setFocusable(true);
                aggregateDemandBudgetEt.setFocusableInTouchMode(true);
                aggregateDemandBudgetEt.requestFocus();
                aggregateDemandBudgetEt.findFocus();

                inputMethodManager.showSoftInput(aggregateDemandBudgetEt, InputMethodManager.SHOW_FORCED);
            }
        });

        if (aggregateDemandBudgetEt.getTag() instanceof TextWatcher) {
            aggregateDemandBudgetEt.removeTextChangedListener((TextWatcher) aggregateDemandBudgetEt.getTag());
        }

        TextWatcher aggregateDemandBudgetTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String aggregateDemandBudgetStr = aggregateDemandBudgetEt.getText().toString().trim();

                if (!TextUtils.isEmpty(aggregateDemandBudgetStr)) {
                    aggregateDemandBudget = Integer.valueOf(aggregateDemandBudgetStr);
                } else {
                    aggregateDemandBudget = 0;
                }
            }
        };

        aggregateDemandBudgetEt.addTextChangedListener(aggregateDemandBudgetTextWatcher);
        aggregateDemandBudgetEt.setTag(aggregateDemandBudgetTextWatcher);
        aggregateDemandBudgetEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggregateDemandBudgetEt.setFocusable(true);
                aggregateDemandBudgetEt.setFocusableInTouchMode(true);
                aggregateDemandBudgetEt.requestFocus();
                aggregateDemandBudgetEt.findFocus();

                inputMethodManager.showSoftInput(aggregateDemandBudgetEt, InputMethodManager.SHOW_FORCED);
            }
        });

        if (demandIntroductionEt.getTag() instanceof TextWatcher) {
            demandIntroductionEt.removeTextChangedListener((TextWatcher) demandIntroductionEt.getTag());
        }

        TextWatcher demandIntroductionTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                demandIntroduction = demandIntroductionEt.getText().toString().trim();
                int index = demandIntroductionEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(demandIntroduction)) {
                        Editable editable = demandIntroductionEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                int demandIntroductionLength = demandIntroduction.length();
                holder.setTextView(R.id.tv_demand_introduction_number_of_words, String.valueOf(demandIntroductionLength) + "/500");
            }
        };

        demandIntroductionEt.addTextChangedListener(demandIntroductionTextWatcher);
        demandIntroductionEt.setTag(demandIntroductionTextWatcher);

        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                DemandPeriodListBean demandPeriodListBean = demandPeriodList.get(i);

                if (null != demandPeriodListBean) {
                    boolean isSelected = TypeUtils.getBoolean(demandPeriodListBean.isSelected, false);

                    if (isSelected) {
                        demandPeriodListBean.isSelected = false;
                    } else {
                        demandPeriodListBean.isSelected = true;
                    }
                }
                return false;
            }
        });

        serviceCityLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAddressList && mAddressList.size() > 0) {
                    showSelectServiceCityDialog(mAddressList);
                } else {
                    Toast.makeText(context, "暂无服务城市数据", Toast.LENGTH_SHORT).show();
                }
            }
        });

        workStartTimeRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showWorkStartTimeDatePickerDialog();

                employmentPeriodEt.setFocusable(false);
                aggregateDemandBudgetEt.setFocusable(false);
                demandIntroductionEt.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(employmentPeriodEt.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(aggregateDemandBudgetEt.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(demandIntroductionEt.getWindowToken(), 0);
                }

                timePickerBuilder = new TimePickerBuilder(context, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View view) {
                        try {
                            Date tempDate = new Date();
                            String now = CommonConstant.commonDateFormat.format(tempDate);
                            Date todayDate = CommonConstant.commonDateFormat.parse(now);
                            long todayLong = todayDate.getTime();
                            long selectedLong = date.getTime();

                            if (selectedLong >= todayLong) {
                                subscribeStartTime = CommonConstant.subscribeTimeFormat.format(date);
                                publishDemandSubscribeStartTime = CommonConstant.refreshTimeFormat.format(date);

                                if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                                    notifyItemChanged(mList.size());
                                } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                                    notifyItemChanged(mList.size() + 1);
                                }
                            } else {
                                Toast.makeText(context, "请选择正确的工作开始时间", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                timePickerBuilder.setType(new boolean[]{true, true, true, true, true, false})
                        .setSubmitText(context.getString(R.string.confirm))// 确认按钮文字
                        .setCancelText(context.getString(R.string.cancel))//取消按钮文字
                        .setTitleSize(16)// 标题文字大小
                        .setSubCalSize(14)// 按钮文字大小
                        .setContentTextSize(16)// 滚轮文字大小
                        .setTitleText(context.getString(R.string.subscribe_start_time))// 标题文字
                        .setOutSideCancelable(true)// 点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setTitleColor(context.getResources().getColor(R.color.text_dark_black))// 标题文字颜色
                        .setSubmitColor(context.getResources().getColor(R.color.theme_orange))// 确定按钮文字颜色
                        .setCancelColor(context.getResources().getColor(R.color.text_dark_black))// 取消按钮文字颜色
                        .setTitleBgColor(context.getResources().getColor(R.color.white))// 标题背景颜色
                        .setBgColor(context.getResources().getColor(R.color.white))// 滚轮背景颜色
                        .setLabel("年", "月", "日", "时", "分", "秒")
                        .build().show();
            }
        });

        seeExampleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstServiceCategoryIndex != -1) {
                    ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(firstServiceCategoryIndex);

                    if (null != serviceCategoryListBean) {
                        String example = TypeUtils.getString(serviceCategoryListBean.exampleContext, "");
                        showSeeExampleDialog(example);
                    }
                } else {
                    Toast.makeText(context, "请选择需求品类", Toast.LENGTH_SHORT).show();
                }
            }
        });

        demandIntroductionEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demandIntroductionEt.setFocusable(true);
                demandIntroductionEt.setFocusableInTouchMode(true);
                demandIntroductionEt.requestFocus();
                demandIntroductionEt.findFocus();

                inputMethodManager.showSoftInput(demandIntroductionEt, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    public void setServiceCategoryList(List<ServiceCategoryListBean> serviceCategoryList) {
        this.serviceCategoryList = serviceCategoryList;
    }

    public void setServiceModeAndPriceModeList(List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList, ArrayList<Integer> serviceWayIdArrayList, ArrayList<String> servicePriceArrayList, int demandMaxBudget, int maxEmploymentTimes) {
        this.serviceModeAndPriceModeList = serviceModeAndPriceModeList;
        mServiceWayIdArrayList = serviceWayIdArrayList;
        mServicePriceArrayList = servicePriceArrayList;
        mMaxEmploymentTime = maxEmploymentTimes;
        mDemandMaxBudget = demandMaxBudget;

        switch (publishDemandType) {
            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON:
                if (null != serviceModeAndPriceModeList && serviceModeAndPriceModeList.size() > 0) {
                    int serviceModeAndPriceModeListSize = serviceModeAndPriceModeList.size();

                    if (serviceModeAndPriceModeListSize == 1) {
                        ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(0);
                        Integer isNeedAddress = serviceModeAndPriceModeBean.isNeedAddress;
                        mShowEmploymentTimes = TypeUtils.getInteger(serviceModeAndPriceModeBean.showEmploymentTimes, 0);
                        mEmploymentTimeUnit = TypeUtils.getString(serviceModeAndPriceModeBean.priceUnit);
                        mPriceUnitId = TypeUtils.getInteger(serviceModeAndPriceModeBean.employmentCycleUnitId, -1);
                        mPriceUnit = TypeUtils.getString(serviceModeAndPriceModeBean.employmentCycleUnit, "");
                        Integer employmentCycleMinValue = serviceModeAndPriceModeBean.employmentCycleMinValue;
                        Integer employmentCycleMaxValue = serviceModeAndPriceModeBean.employmentCycleMaxValue;

                        if (null == isNeedAddress || isNeedAddress == 0) {
                            mIsNeedAddress = false;
                        } else if (isNeedAddress == 1) {
                            mIsNeedAddress = true;
                        }

                        if (null == employmentCycleMinValue) {
                            mMinEmploymentPeriod = 0;
                        } else {
                            mMinEmploymentPeriod = employmentCycleMinValue;
                        }

                        if (null == employmentCycleMaxValue) {
                            mMaxEmploymentPeriod = 0;
                        } else {
                            mMaxEmploymentPeriod = employmentCycleMaxValue;
                        }

                        mEmploymentPeriod = mMinEmploymentPeriod;
                        addServiceModeAndPriceModeList.add(serviceModeAndPriceModeBean);
                        notifyItemChanged(mList.size());
                    } else {
                        if (null != this.serviceModeAndPriceModeList && this.serviceModeAndPriceModeList.size() > 0) {
                            List<String> subclassNameList = new ArrayList<>();
                            Set<Integer> isSelectedSet = new HashSet<>();
                            List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                            for (int i = 0; i < this.serviceModeAndPriceModeList.size(); i++) {
                                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = this.serviceModeAndPriceModeList.get(i);
                                String serviceModeName = TypeUtils.getString(serviceModeAndPriceModeBean.serveWayName, "");
                                subclassNameList.add(serviceModeName);
                            }

                            PublishServiceBean tempPublishServiceBean = new PublishServiceBean();
                            tempPublishServiceBean.isShow = true;
                            tempPublishServiceBean.isRequired = true;
                            tempPublishServiceBean.name = context.getString(R.string.employment_way);
                            tempPublishServiceBean.maxSelectCount = 1;
                            tempPublishServiceBean.subclassNameList = subclassNameList;
                            tempPublishServiceBean.isSelectedSet = isSelectedSet;
                            tempPublishServiceBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                            mList.add(tempPublishServiceBean);

                            notifyItemInserted(mList.size());
                        }
                    }
                }
                break;

            case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE:
                if (null != serviceWayIdArrayList && serviceWayIdArrayList.size() > 0 && null != mServicePriceArrayList && mServicePriceArrayList.size() > 0) {
                    int serviceWayIdArrayListSize = mServiceWayIdArrayList.size();
                    int mServicePriceArrayListSize = mServicePriceArrayList.size();

                    if (serviceWayIdArrayListSize == 1 && mServicePriceArrayListSize == 1) {
                        if (null != serviceModeAndPriceModeList && serviceModeAndPriceModeList.size() > 0) {
                            ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(0);
                            Integer isNeedAddress = serviceModeAndPriceModeBean.isNeedAddress;
                            mShowEmploymentTimes = TypeUtils.getInteger(serviceModeAndPriceModeBean.showEmploymentTimes, 0);
                            mEmploymentTimeUnit = TypeUtils.getString(serviceModeAndPriceModeBean.priceUnit);
                            mPriceUnitId = TypeUtils.getInteger(serviceModeAndPriceModeBean.employmentCycleUnitId, -1);
                            mPriceUnit = TypeUtils.getString(serviceModeAndPriceModeBean.employmentCycleUnit, "");
                            Integer employmentCycleMinValue = serviceModeAndPriceModeBean.employmentCycleMinValue;
                            Integer employmentCycleMaxValue = serviceModeAndPriceModeBean.employmentCycleMaxValue;

                            if (null == isNeedAddress || isNeedAddress == 0) {
                                mIsNeedAddress = false;
                            } else if (isNeedAddress == 1) {
                                mIsNeedAddress = true;
                            }

                            if (null == employmentCycleMinValue) {
                                mMinEmploymentPeriod = 0;
                            } else {
                                mMinEmploymentPeriod = employmentCycleMinValue;
                            }

                            if (null == employmentCycleMaxValue) {
                                mMaxEmploymentPeriod = 0;
                            } else {
                                mMaxEmploymentPeriod = employmentCycleMaxValue;
                            }

                            mEmploymentPeriod = mMinEmploymentPeriod;
                            addServiceModeAndPriceModeList.add(serviceModeAndPriceModeBean);

                            String servicePriceString = mServicePriceArrayList.get(0);

                            if (!TextUtils.isEmpty(servicePriceString)) {
                                mServicePrice = Integer.valueOf(servicePriceString);
                            }

                            notifyItemChanged(mList.size() + 1);
                        }

                        if (null != this.serviceModeAndPriceModeList && this.serviceModeAndPriceModeList.size() > 0) {
                            List<String> subclassNameList = new ArrayList<>();
                            Set<Integer> isSelectedSet = new HashSet<>();
                            List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                            for (int i = 0; i < this.serviceModeAndPriceModeList.size(); i++) {
                                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = this.serviceModeAndPriceModeList.get(i);
                                String serviceModeName = TypeUtils.getString(serviceModeAndPriceModeBean.serveWayName, "");
                                subclassNameList.add(serviceModeName);
                            }

                            PublishServiceBean tempPublishServiceBean = new PublishServiceBean();
                            tempPublishServiceBean.isShow = false;
                            tempPublishServiceBean.isRequired = true;
                            tempPublishServiceBean.name = context.getString(R.string.employment_way);
                            tempPublishServiceBean.maxSelectCount = 1;
                            tempPublishServiceBean.subclassNameList = subclassNameList;
                            tempPublishServiceBean.isSelectedSet = isSelectedSet;
                            tempPublishServiceBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                            mList.add(tempPublishServiceBean);

                            notifyItemInserted(mList.size() + 1);
                        }
                    } else {
                        if (null != this.serviceModeAndPriceModeList && this.serviceModeAndPriceModeList.size() > 0) {
                            List<String> subclassNameList = new ArrayList<>();
                            Set<Integer> isSelectedSet = new HashSet<>();
                            List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                            for (int i = 0; i < this.serviceModeAndPriceModeList.size(); i++) {
                                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = this.serviceModeAndPriceModeList.get(i);
                                String serviceModeName = TypeUtils.getString(serviceModeAndPriceModeBean.serveWayName, "");
                                subclassNameList.add(serviceModeName);
                            }

                            PublishServiceBean tempPublishServiceBean = new PublishServiceBean();
                            tempPublishServiceBean.isShow = true;
                            tempPublishServiceBean.isRequired = true;
                            tempPublishServiceBean.name = context.getString(R.string.employment_way);
                            tempPublishServiceBean.maxSelectCount = 1;
                            tempPublishServiceBean.subclassNameList = subclassNameList;
                            tempPublishServiceBean.isSelectedSet = isSelectedSet;
                            tempPublishServiceBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                            mList.add(tempPublishServiceBean);

                            notifyItemInserted(mList.size() + 1);
                        }
                    }
                }
                break;
        }
    }

    private void showSelectServiceCityDialog(List<IsAllBean> addressList) {
        selectServiceCityDialog = new SelectServiceCityDialog(context, provinceAdapter, cityAdapter, mAddressList);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = selectServiceCityDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        selectServiceCityDialog.show();
        selectServiceCityDialog.setOnClickListener(new SelectServiceCityDialog.OnClickListener() {
            @Override
            public void onConfirmClick(List<IsAllBean> addressList, String provinceCode, String cityCode, String address) {
                mAddressList = addressList;

                if (null != selectServiceCityDialog && selectServiceCityDialog.isShowing()) {
                    selectServiceCityDialog.dismiss();

                    mProvinceCode = provinceCode;
                    mCityCode = cityCode;

                    if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                        notifyItemChanged(mList.size());
                    } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                        notifyItemChanged(mList.size() + 1);
                    }
                }
            }
        });
    }

    private void showWorkStartTimeDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String yearStr = String.valueOf(year);
                String monthStr = String.valueOf(month + 1);
                String dateStr = String.valueOf(dayOfMonth);

                if (monthStr.length() == 1) {
                    monthStr = "0" + monthStr;
                }

                if (dateStr.length() == 1) {
                    dateStr = "0" + dateStr;
                }

                String startTimeStr = yearStr + "-" + monthStr + "-" + dateStr;

                try {
                    Date date = new Date();
                    String now = CommonConstant.commonDateFormat.format(date);
                    Date todayDate = CommonConstant.commonDateFormat.parse(now);
                    long todayLong = todayDate.getTime();

                    Date selectedDate = CommonConstant.commonDateFormat.parse(startTimeStr);
                    long selectedLong = selectedDate.getTime();

                    if (selectedLong >= todayLong) {
                        subscribeStartTime = startTimeStr;

                        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                            notifyItemChanged(mList.size());
                        } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                            notifyItemChanged(mList.size() + 1);
                        }
                    } else {
                        Toast.makeText(context, "请选择正确的工作开始时间", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.show();
    }

    private void showSeeExampleDialog(String content) {
        seeExampleDialog = new SeeExampleDialog(context, content);

        Window window = seeExampleDialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;

        if (null != window) {
            window.setAttributes(layoutParams);
        }


        seeExampleDialog.show();
    }

    public void setDemandPeriodList(List<DemandPeriodListBean> demandPeriodList) {
        this.demandPeriodList = demandPeriodList;
    }

    public void setFirstServiceCategoryIndex(int firstServiceCategoryIndex) {
        this.firstServiceCategoryIndex = firstServiceCategoryIndex;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public String getServiceCategoryListString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (null != mList && mList.size() > 0) {
            switch (publishDemandType) {
                case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON:
                    if (null != serviceModeAndPriceModeList && serviceModeAndPriceModeList.size() > 0) {
                        int serviceModeAndPriceModeListSize = serviceModeAndPriceModeList.size();

                        if (serviceModeAndPriceModeListSize == 1) {
                            for (int i = 1; i < mList.size(); i++) {
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
                        } else {
                            for (int i = 1; i < mList.size() - 1; i++) {
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

                                if (!TextUtils.isEmpty(stringBuilder) && i == mList.size() - 2) {
                                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                                }
                            }
                        }
                    }
                    break;

                case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE:
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
                    break;
            }
        }
        return stringBuilder.toString();
    }

    public List<Integer> getMustSelectedCategoryIdList() {
        return mustSelectedCategoryIdList;
    }

    public void addMustSelectedCategoryId(int mustSelectedCategoryId) {
        mustSelectedCategoryIdList.add(mustSelectedCategoryId);
    }

    public int getServiceModeId() {
        if (null != addServiceModeAndPriceModeList && addServiceModeAndPriceModeList.size() > 0) {
            for (int i = 0; i < addServiceModeAndPriceModeList.size(); i++) {
                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = addServiceModeAndPriceModeList.get(i);

                if (null != serviceModeAndPriceModeBean) {
                    serviceModeId = TypeUtils.getInteger(serviceModeAndPriceModeBean.serveWayId, -1);
                }
            }
        }
        return serviceModeId;
    }

    public void setAddressList(List<IsAllBean> mAddressList) {
        this.mAddressList = mAddressList;
    }

    public boolean getIsNeedAddress() {
        return mIsNeedAddress;
    }

    public String getProvinceCode() {
        return mProvinceCode;
    }

    public String getCityCode() {
        return mCityCode;
    }

    public String getWorkStartTime() {
        return publishDemandSubscribeStartTime;
    }

    public int getDemandPeriodId() {
        int demandPeriodId = -1;

        if (null != demandPeriodList && demandPeriodList.size() > 0) {
            for (int i = 0; i < demandPeriodList.size(); i++) {
                DemandPeriodListBean demandPeriodListBean = demandPeriodList.get(i);

                if (null != demandPeriodListBean) {
                    int code = TypeUtils.getInteger(demandPeriodListBean.code, -1);
                    boolean isSelected = TypeUtils.getBoolean(demandPeriodListBean.isSelected, false);

                    if (isSelected) {
                        demandPeriodId = code;
                    }
                }
            }
        }
        return demandPeriodId;
    }

    public void setEmploymentTime(int employmentTime) {
        mEmploymentTime = employmentTime;
        mEmploymentTimeServicePayment = mEmploymentTimeServicePrice * mEmploymentTime;

        if (null != employmentTimeEt) {
            employmentTimeEt.setText(String.valueOf(mEmploymentTime));
        }

        if (null != employmentTimeServicePaymentTv) {
            employmentTimeServicePaymentTv.setText("￥" + String.valueOf(mEmploymentTimeServicePayment));
        }
    }

    public void setMinAndMaxEmploymentTime(int employmentTime) {
        mEmploymentTime = employmentTime;
        mEmploymentTimeServicePayment = mEmploymentTimeServicePrice * mEmploymentTime;

        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
            notifyItemChanged(mList.size());
        } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
            notifyItemChanged(mList.size() + 1);
        }
    }

    public void setEmploymentPeriod(int employmentPeriod) {
        mEmploymentPeriod = employmentPeriod;
        mServicePayment = mServicePrice * mEmploymentPeriod;

        if (null != employmentPeriodEt) {
            employmentPeriodEt.setText(String.valueOf(mEmploymentPeriod));
        }

        if (null != servicePaymentTv) {
            servicePaymentTv.setText("￥" + String.valueOf(mServicePayment));
        }
    }

    public void setMinAndMaxEmploymentPeriod(int employmentPeriod) {
        mEmploymentPeriod = employmentPeriod;
        mServicePayment = mServicePrice * mEmploymentPeriod;

        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
            notifyItemChanged(mList.size());
        } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
            notifyItemChanged(mList.size() + 1);
        }
    }

    public int getShowEmploymentTimes() {
        return mShowEmploymentTimes;
    }

    public int getEmploymentTime() {
        return mEmploymentTime;
    }

    public int getEmploymentPeriod() {
        return mEmploymentPeriod;
    }

    public int getEmploymentPeriodUnitId() {
        return mPriceUnitId;
    }

    public int getAggregateDemandBudget() {
        return aggregateDemandBudget;
    }

    public int getEmploymentTimeServicePayment() {
        return mEmploymentTimeServicePayment;
    }

    public int getServicePayment() {
        return mServicePayment;
    }

    public String getDemandIntroduction() {
        return demandIntroduction;
    }

    public int getRecommendMoreStatus() {
        return recommendMoreStatus;
    }

    public void setPersonalInformation(String userHeadPortraitUrl, String name, int gender, String age, String serviceCategory, String distance, String serviceWayPriceUnitString) {
        this.userHeadPortraitUrl = userHeadPortraitUrl;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.serviceCategory = serviceCategory;
        this.distance = distance;
        this.serviceWayPriceUnitString = serviceWayPriceUnitString;

        notifyItemChanged(0);
    }

    public interface OnClickListener {

        void onSwitchCompatCheckedChange(boolean isChecked);

        void onTagClickListener(int tempCategoryId);

        void onIncreaseTouch(int minEmployPeriod, int maxEmployPeriod, int employmentPeriod);

        void onDecreaseTouch(int minEmployPeriod, int maxEmployPeriod, int employmentPeriod);

        void onEmploymentPeriodActionUp();

        void onCheckEmploymentPeriod(int minEmployPeriod, int maxEmployPeriod, int employmentPeriod);

        void onEmploymentTimeIncreaseTouch(int minEmployTime, int maxEmployTime, int employmentTime);

        void onEmploymentTimeDecreaseTouch(int minEmployTime, int maxEmployTime, int employmentTime);

        void onEmploymentTimeActionUp();

        void onCheckEmploymentTime(int minEmployTime, int maxEmployTime, int employmentTime);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
