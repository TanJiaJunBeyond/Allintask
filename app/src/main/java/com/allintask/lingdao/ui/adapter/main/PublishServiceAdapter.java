package com.allintask.lingdao.ui.adapter.main;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.PublishServiceBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.main.PublishServiceActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.ui.adapter.user.CityAdapter;
import com.allintask.lingdao.ui.adapter.user.ProvinceAdapter;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.utils.ImageUtils;
import com.allintask.lingdao.utils.MediaRecordPlayerUtils;
import com.allintask.lingdao.utils.MediaRecordUtils;
import com.allintask.lingdao.widget.SeeExampleDialog;
import com.allintask.lingdao.widget.SelectServiceCityDialog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/5.
 */

public class PublishServiceAdapter extends CommonRecyclerViewAdapter {

    private static final int ITEM_PUBLISH_SERVICE = 0;
    private static final int ITEM_PUBLISH_SERVICE_MODE = 1;
    private static final int ITEM_PUBLISH_SERVICE_BOTTOM = 2;

    private Context context;
    private int publishServiceType = CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD;

    private InputMethodManager inputMethodManager;

    private List<ServiceModeAndPriceModeBean> addServiceModeAndPriceModeList;
    private List<Integer> mustSelectedCategoryIdList;

    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    private List<ServiceCategoryListBean> serviceCategoryList;
    private List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList;
    private int firstServiceCategoryIndex = -1;
    private int mCategoryId = -1;

    private int mPriceMinValue = 0;
    private int mPriceMaxValue = 0;

    private boolean mIsNeedAddress = false;

    private List<IsAllBean> mAddressList;

    private String mProvinceCode;
    private String mCityCode;

    private int voiceStatus;
    private int voiceDuration = -1;

    private LinearLayout voiceDemoLL;
    private LinearLayout voiceIntroduceLL;
    private TextView voiceTimeTv;
    private TextView voiceLoadingTv;

    private String downloadVoiceDemoFilePath;
    private int voiceDemoDuration = -1;

    private String downloadVoiceFilePath;
    private String myMerit;
    private String serviceIntroduction;

    private SelectServiceCityDialog selectServiceCityDialog;
    private SeeExampleDialog seeExampleDialog;

    private OnClickListener onClickListener;
    private OnTouchListener onTouchListener;

    public PublishServiceAdapter(Context context, int publishServiceType) {
        this.context = context;
        this.publishServiceType = publishServiceType;

        inputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);

        addServiceModeAndPriceModeList = new ArrayList<>();
        mustSelectedCategoryIdList = new ArrayList<>();

        provinceAdapter = new ProvinceAdapter(context);
        cityAdapter = new CityAdapter(context);
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = null;

        switch (viewType) {
            case ITEM_PUBLISH_SERVICE:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_publish_service, parent, false));
                break;

            case ITEM_PUBLISH_SERVICE_MODE:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_publish_service_mode, parent, false));
                break;

            case ITEM_PUBLISH_SERVICE_BOTTOM:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_publish_service_bottom, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        switch (getItemViewType(position)) {
            case ITEM_PUBLISH_SERVICE:
                onBindPublishServiceItemView(holder, position);
                break;

            case ITEM_PUBLISH_SERVICE_MODE:
                onBindPublishServiceModeItemView(holder, position);
                break;

            case ITEM_PUBLISH_SERVICE_BOTTOM:
                onBindPublishServiceBottomView(holder);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < mList.size()) {
            return ITEM_PUBLISH_SERVICE;
        } else if (position >= mList.size() && position < mList.size() + addServiceModeAndPriceModeList.size()) {
            return ITEM_PUBLISH_SERVICE_MODE;
        } else {
            return ITEM_PUBLISH_SERVICE_BOTTOM;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + addServiceModeAndPriceModeList.size() + 1;
    }

    private void onBindPublishServiceItemView(CommonRecyclerViewHolder holder, final int position) {
        final PublishServiceBean publishServiceBean = (PublishServiceBean) getItem(position);

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

                    switch (publishServiceType) {
                        case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD:
                            if (position == 0) {
                                if (isSelected) {
                                    notifyItemRangeRemoved(position + 1, mList.size() - 1 + addServiceModeAndPriceModeList.size());

                                    for (int j = mList.size() - 1; j > 0; j--) {
                                        mList.remove(j);
                                    }

                                    addServiceModeAndPriceModeList.clear();
                                    mustSelectedCategoryIdList.clear();

                                    for (int j = 0; j < serviceCategoryList.size(); j++) {
                                        isSelectedSet.clear();
                                    }

                                    firstServiceCategoryIndex = -1;
                                    mCategoryId = -1;
                                    mIsNeedAddress = false;

                                    notifyItemChanged(position);
                                    notifyItemChanged(mList.size() + addServiceModeAndPriceModeList.size());
                                } else {
                                    notifyItemRangeRemoved(position + 1, mList.size() - 1 + addServiceModeAndPriceModeList.size());

                                    for (int j = mList.size() - 1; j > 0; j--) {
                                        mList.remove(j);
                                    }

                                    addServiceModeAndPriceModeList.clear();
                                    mustSelectedCategoryIdList.clear();

                                    for (int j = 0; j < serviceCategoryList.size(); j++) {
                                        isSelectedSet.clear();
                                    }

                                    isSelectedSet.add(i);
                                    mIsNeedAddress = false;

                                    notifyItemChanged(position);

                                    if (null != onClickListener) {
                                        onClickListener.onCheckIsPublished(position, isSelectedSet, i);
                                    }
                                }
                            } else if (position == mList.size() - 1) {
                                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(i);
                                String serviceModeName = serviceModeAndPriceModeBean.serveWayName;

                                if (isSelected) {
                                    for (int m = 0; m < addServiceModeAndPriceModeList.size(); m++) {
                                        String tempServiceModeName = addServiceModeAndPriceModeList.get(m).serveWayName;

                                        if (tempServiceModeName.equals(serviceModeName)) {
                                            isSelectedSet.remove(i);
                                            addServiceModeAndPriceModeList.remove(m);
                                            notifyItemRemoved(mList.size() + m);
                                        }
                                    }
                                } else {
                                    isSelectedSet.add(i);
                                    addServiceModeAndPriceModeList.add(serviceModeAndPriceModeBean);
                                    notifyItemInserted(mList.size() + addServiceModeAndPriceModeList.size() + 1);
                                }

                                for (int j = 0; j < addServiceModeAndPriceModeList.size(); j++) {
                                    ServiceModeAndPriceModeBean addServiceModeAndPriceModeBean = addServiceModeAndPriceModeList.get(j);

                                    if (null != addServiceModeAndPriceModeBean) {
                                        Integer addIsNeedAddress = addServiceModeAndPriceModeBean.isNeedAddress;

                                        if (null == addIsNeedAddress || addIsNeedAddress == 0) {
                                            mIsNeedAddress = false;
                                        } else if (addIsNeedAddress == 1) {
                                            mIsNeedAddress = true;
                                            break;
                                        }
                                    }
                                }

                                notifyItemChanged(mList.size() + addServiceModeAndPriceModeList.size());
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
                            break;

                        case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE:
                            if (position == mList.size() - 1) {
                                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(i);
                                String serviceModeName = serviceModeAndPriceModeBean.serveWayName;

                                if (isSelected) {
                                    for (int m = 0; m < addServiceModeAndPriceModeList.size(); m++) {
                                        String tempServiceModeName = addServiceModeAndPriceModeList.get(m).serveWayName;

                                        if (tempServiceModeName.equals(serviceModeName)) {
                                            isSelectedSet.remove(i);
                                            addServiceModeAndPriceModeList.remove(m);
                                            notifyItemRemoved(mList.size() + m);
                                        }
                                    }
                                } else {
                                    isSelectedSet.add(i);
                                    addServiceModeAndPriceModeList.add(serviceModeAndPriceModeBean);
                                    notifyItemInserted(mList.size() + addServiceModeAndPriceModeList.size() + 1);
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
                            }
                            break;
                    }
                    return false;
                }
            });
        }
    }

    private void onBindPublishServiceModeItemView(CommonRecyclerViewHolder holder, int position) {
        if (null != addServiceModeAndPriceModeList && addServiceModeAndPriceModeList.size() > 0) {
            final ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = addServiceModeAndPriceModeList.get(position - mList.size());

            if (null != serviceModeAndPriceModeBean) {
                final EditText contentEt = holder.getChildView(R.id.et_content);

                String priceWayName = TypeUtils.getString(serviceModeAndPriceModeBean.priceWayName, "");
                String price = TypeUtils.getString(serviceModeAndPriceModeBean.price, "");
                String priceUnit = TypeUtils.getString(serviceModeAndPriceModeBean.priceUnit, "");
                Integer priceMinValue = serviceModeAndPriceModeBean.priceMinValue;
                Integer priceMaxValue = serviceModeAndPriceModeBean.priceMaxValue;

                if (null == priceMinValue) {
                    mPriceMinValue = 0;
                } else {
                    mPriceMinValue = priceMinValue;
                }

                if (null == priceMaxValue) {
                    mPriceMaxValue = 0;
                } else {
                    mPriceMaxValue = priceMaxValue;
                }

                holder.setTextView(R.id.tv_title, priceWayName);
                holder.setTextView(R.id.et_content, price);
                holder.setTextView(R.id.tv_unit, "元/" + priceUnit);

                if (contentEt.getTag() instanceof TextWatcher) {
                    contentEt.removeTextChangedListener((TextWatcher) contentEt.getTag());
                }

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String priceString = contentEt.getText().toString().trim();
                        int price = 0;

                        if (!TextUtils.isEmpty(priceString)) {
                            price = Integer.valueOf(priceString);
                        }

                        if (price == 0) {
                            price = mPriceMinValue;
                        }

                        if (price < mPriceMinValue) {
                            Toast.makeText(context, "不能小于最小价格", Toast.LENGTH_SHORT).show();
                            contentEt.setText(String.valueOf(mPriceMinValue));
                        } else if (price > mPriceMaxValue) {
                            Toast.makeText(context, "不能大于最大价格", Toast.LENGTH_SHORT).show();
                            contentEt.setText(String.valueOf(mPriceMaxValue));
                        } else {
                            serviceModeAndPriceModeBean.price = priceString;
                        }
                    }
                };

                contentEt.addTextChangedListener(textWatcher);
                contentEt.setTag(textWatcher);
            }
        }
    }

    private void onBindPublishServiceBottomView(final CommonRecyclerViewHolder holder) {
        LinearLayout allServiceCityLL = holder.getChildView(R.id.ll_all_service_city);
        LinearLayout serviceCityLL = holder.getChildView(R.id.ll_service_city);
        TextView serviceCityTv = holder.getChildView(R.id.tv_service_city);
        Button holdToTalkBtn = holder.getChildView(R.id.btn_hold_to_talk);
        voiceDemoLL = holder.getChildView(R.id.ll_voice_demo);
        final LinearLayout voiceIntroduceDemoAllLL = holder.getChildView(R.id.ll_voice_introduce_demo_all);
        LinearLayout voiceIntroduceDemoSubclassLL = holder.getChildView(R.id.ll_voice_introduce_demo_subclass);
        LinearLayout voiceIntroduceDemoLL = holder.getChildView(R.id.ll_voice_introduce_demo);
        final ImageView voiceDemoIv = holder.getChildView(R.id.iv_voice_demo);
        TextView voiceDemoTimeTv = holder.getChildView(R.id.tv_voice_demo_time);
        TextView voiceDemoLoadingTv = holder.getChildView(R.id.tv_voice_demo_loading);
        voiceIntroduceLL = holder.getChildView(R.id.ll_voice_introduce);
        LinearLayout voiceLL = holder.getChildView(R.id.ll_voice);
        final ImageView voiceIv = holder.getChildView(R.id.iv_voice);
        voiceTimeTv = holder.getChildView(R.id.tv_voice_time);
        TextView deleteTv = holder.getChildView(R.id.tv_delete);
        voiceLoadingTv = holder.getChildView(R.id.tv_voice_loading);
        TextView seeExampleTv = holder.getChildView(R.id.tv_see_example);
        final EditText myMeritEt = holder.getChildView(R.id.et_my_merit);
        final EditText serviceIntroductionEt = holder.getChildView(R.id.et_service_introduction);

        myMeritEt.setFocusable(false);
        serviceIntroductionEt.setFocusable(false);

        if (null != inputMethodManager && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(myMeritEt.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(serviceIntroductionEt.getWindowToken(), 0);
        }

        if (mIsNeedAddress) {
            allServiceCityLL.setVisibility(View.VISIBLE);
        } else {
            allServiceCityLL.setVisibility(View.GONE);
        }

        if (publishServiceType == CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD) {
            switch (voiceStatus) {
                case CommonConstant.VOICE_DEMO_DOWNLOADING:
                    voiceIntroduceDemoAllLL.setVisibility(View.VISIBLE);
                    voiceIntroduceDemoSubclassLL.setVisibility(View.GONE);
                    voiceDemoLoadingTv.setVisibility(View.VISIBLE);
                    break;

                case CommonConstant.VOICE_DEMO_DOWNLOAD_SUCCESS:
                    voiceIntroduceDemoAllLL.setVisibility(View.VISIBLE);
                    voiceIntroduceDemoSubclassLL.setVisibility(View.VISIBLE);
                    voiceDemoLoadingTv.setVisibility(View.GONE);
                    break;

                case CommonConstant.NO_VOICE_DEMO:
                case CommonConstant.VOICE_DEMO_DOWNLOAD_FAIL:
                    voiceIntroduceDemoAllLL.setVisibility(View.GONE);
                    break;

                case CommonConstant.VOICE_DOWNLOAD_SUCCESS:
                    voiceDemoLL.setVisibility(View.GONE);
                    voiceIntroduceLL.setVisibility(View.VISIBLE);
                    voiceLoadingTv.setVisibility(View.GONE);

                    String voiceDurationString = String.valueOf(voiceDuration);
                    voiceTimeTv.setText(voiceDurationString + "''");
                    break;

                case CommonConstant.NO_VOICE:
                    voiceDemoLL.setVisibility(View.VISIBLE);
                    voiceIntroduceLL.setVisibility(View.VISIBLE);
                    voiceIntroduceDemoAllLL.setVisibility(View.VISIBLE);
                    voiceIntroduceDemoSubclassLL.setVisibility(View.VISIBLE);
                    voiceLoadingTv.setVisibility(View.GONE);
                    voiceIntroduceLL.setVisibility(View.GONE);
                    break;

            }
        } else if (publishServiceType == CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE) {
            switch (voiceStatus) {
                case CommonConstant.VOICE_DEMO_DOWNLOADING:
                    voiceIntroduceDemoAllLL.setVisibility(View.VISIBLE);
                    voiceDemoLoadingTv.setVisibility(View.VISIBLE);
                    break;

                case CommonConstant.VOICE_DEMO_DOWNLOAD_SUCCESS:
                    voiceIntroduceDemoAllLL.setVisibility(View.VISIBLE);
                    voiceDemoLoadingTv.setVisibility(View.GONE);
                    break;

                case CommonConstant.NO_VOICE_DEMO:
                case CommonConstant.VOICE_DEMO_DOWNLOAD_FAIL:
                    voiceIntroduceDemoAllLL.setVisibility(View.GONE);
                    break;

                case CommonConstant.VOICE_DOWNLOADING:
                    voiceDemoLL.setVisibility(View.GONE);
                    voiceIntroduceLL.setVisibility(View.GONE);
                    voiceLoadingTv.setVisibility(View.VISIBLE);
                    break;

                case CommonConstant.VOICE_DOWNLOAD_SUCCESS:
                    voiceDemoLL.setVisibility(View.GONE);
                    voiceIntroduceLL.setVisibility(View.VISIBLE);
                    voiceLoadingTv.setVisibility(View.GONE);

                    String voiceDurationString = String.valueOf(voiceDuration);
                    voiceTimeTv.setText(voiceDurationString + "''");
                    break;

                case CommonConstant.NO_VOICE:
                case CommonConstant.VOICE_DOWNLOAD_FAIL:
                    voiceDemoLL.setVisibility(View.VISIBLE);
                    voiceIntroduceLL.setVisibility(View.GONE);
                    voiceLoadingTv.setVisibility(View.GONE);
                    break;
            }
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
            } else if (!TextUtils.isEmpty(province) && province.equals(city)) {
                address = province;
            } else {
                address = province + "\r\r" + city;
            }

            if (!TextUtils.isEmpty(address)) {
                serviceCityTv.setText(address);
            } else {
                serviceCityTv.setText(context.getString(R.string.please_select));
            }
        }

        String voiceDemoDurationString = String.valueOf(voiceDemoDuration);
        voiceDemoTimeTv.setText(voiceDemoDurationString + "''");

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

        holdToTalkBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (null != onTouchListener) {
                    onTouchListener.onRecordImageViewTouch(v, event);
                }
                return false;
            }
        });

        voiceIntroduceDemoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AnimationDrawable animationDrawable = (AnimationDrawable) voiceDemoIv.getBackground();

                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();

                    MediaRecordPlayerUtils.getInstance().pause();
                    MediaRecordPlayerUtils.getInstance().release();

                    Drawable drawable = context.getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                    voiceDemoIv.setBackground(drawable);
                } else {
                    animationDrawable.start();

                    MediaRecordPlayerUtils.playSound(downloadVoiceDemoFilePath, true);
                    MediaRecordPlayerUtils.getInstance().set0nMediaRecordPlayerListener(new MediaRecordPlayerUtils.OnMediaRecordPlayerListener() {
                        @Override
                        public void setOnCompletionListener() {
                            animationDrawable.stop();

                            MediaRecordPlayerUtils.getInstance().release();

                            Drawable drawable = context.getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                            voiceDemoIv.setBackground(drawable);
                        }
                    });
                }
            }
        });

        voiceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AnimationDrawable animationDrawable = (AnimationDrawable) voiceIv.getBackground();

                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();

                    MediaRecordPlayerUtils.getInstance().pause();
                    MediaRecordPlayerUtils.getInstance().release();

                    Drawable drawable = context.getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                    voiceIv.setBackground(drawable);
                } else {
                    animationDrawable.start();

                    if (TextUtils.isEmpty(downloadVoiceFilePath)) {
                        String fileAddress = AllintaskApplication.getInstance().getVoiceFilePath();
                        String fileName = MediaRecordUtils.getInstance().getFileName();
                        String fileStr = fileAddress + fileName;

                        if (!TextUtils.isEmpty(fileName)) {
                            File file = new File(fileAddress, fileName);

                            if (file.exists()) {
                                MediaRecordPlayerUtils.playSound(fileStr, true);
                            } else {
                                Toast.makeText(context, context.getString(R.string.error_voice_file_destroy), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        MediaRecordPlayerUtils.playSound(downloadVoiceFilePath, true);
                    }

                    MediaRecordPlayerUtils.getInstance().set0nMediaRecordPlayerListener(new MediaRecordPlayerUtils.OnMediaRecordPlayerListener() {
                        @Override
                        public void setOnCompletionListener() {
                            animationDrawable.stop();

                            MediaRecordPlayerUtils.getInstance().release();

                            Drawable drawable = context.getResources().getDrawable(R.drawable.anim_publish_service_voice_demo);
                            voiceIv.setBackground(drawable);
                        }
                    });
                }
            }
        });

        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVoiceStatus(CommonConstant.NO_VOICE);

                if (null != onClickListener) {
                    onClickListener.onDeleteVoice();
                }
            }
        });

        seeExampleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstServiceCategoryIndex != -1) {
                    ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(firstServiceCategoryIndex);

                    if (null != serviceCategoryListBean) {
                        String example = TypeUtils.getString(serviceCategoryListBean.exampleContext, "").replace("{newline}", "\n");
                        showSeeExampleDialog(example);
                    }
                } else {
                    Toast.makeText(context, "请选择服务品类", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.setTextView(R.id.et_my_merit, myMerit);

        if (myMeritEt.getTag() instanceof TextWatcher) {
            myMeritEt.removeTextChangedListener((TextWatcher) myMeritEt.getTag());
        }

        TextWatcher myMeritTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                myMerit = myMeritEt.getText().toString().trim();
                int index = myMeritEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(myMerit)) {
                        Editable editable = myMeritEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                int myMeritNumberOfWords = myMerit.length();
                holder.setTextView(R.id.tv_my_merit_number_of_words, String.valueOf(myMeritNumberOfWords) + "/100");
            }
        };

        myMeritEt.addTextChangedListener(myMeritTextWatcher);
        myMeritEt.setTag(myMeritTextWatcher);
        myMeritEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMeritEt.setFocusable(true);
                myMeritEt.setFocusableInTouchMode(true);
                myMeritEt.requestFocus();
                myMeritEt.findFocus();

                inputMethodManager.showSoftInput(myMeritEt, InputMethodManager.SHOW_FORCED);
            }
        });

        holder.setTextView(R.id.et_service_introduction, serviceIntroduction);

        if (serviceIntroductionEt.getTag() instanceof TextWatcher) {
            serviceIntroductionEt.removeTextChangedListener((TextWatcher) serviceIntroductionEt.getTag());
        }

        TextWatcher serviceIntroductionTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serviceIntroduction = serviceIntroductionEt.getText().toString().trim();
                int index = serviceIntroductionEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(serviceIntroduction)) {
                        Editable editable = serviceIntroductionEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                int serviceIntroductionNumberOfWords = serviceIntroduction.length();
                holder.setTextView(R.id.tv_service_introduction_number_of_words, String.valueOf(serviceIntroductionNumberOfWords) + "/500");
            }
        };

        serviceIntroductionEt.addTextChangedListener(serviceIntroductionTextWatcher);
        serviceIntroductionEt.setTag(serviceIntroductionEt);
        serviceIntroductionEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceIntroductionEt.setFocusable(true);
                serviceIntroductionEt.setFocusableInTouchMode(true);
                serviceIntroductionEt.requestFocus();
                serviceIntroductionEt.findFocus();

                inputMethodManager.showSoftInput(serviceIntroductionEt, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    public void expandService(int position, Set<Integer> isSelectedSet, int i) {
        ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(i);
        int categoryId = TypeUtils.getInteger(serviceCategoryListBean.code, 0);
        List<ServiceCategoryListBean.ServiceCategoryFirstBean> serviceCategoryFirstList = serviceCategoryListBean.sub;

        firstServiceCategoryIndex = i;
        mCategoryId = categoryId;

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
                    Set<Integer> tempIsSelectedSet = new HashSet<>();
                    List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                    for (int k = 0; k < serviceCategorySecondList.size(); k++) {
                        ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean serviceCategorySecondBean = serviceCategorySecondList.get(k);
                        String secondName = TypeUtils.getString(serviceCategorySecondBean.name, "");
                        subclassNameList.add(secondName);
                    }

                    PublishServiceBean tempPublishServiceBean = new PublishServiceBean();
                    tempPublishServiceBean.isShow = true;

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
                    tempPublishServiceBean.isSelectedSet = tempIsSelectedSet;
                    tempPublishServiceBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                    mList.add(tempPublishServiceBean);
                }
            }

            isSelectedSet.add(i);
            notifyItemRangeInserted(position + 1, serviceCategoryFirstList.size());
        }
    }

    public void unexpandedService(int position) {
        PublishServiceBean publishServiceBean = (PublishServiceBean) getItem(position);

        if (null != publishServiceBean) {
            Set<Integer> isSelectedSet = publishServiceBean.isSelectedSet;

            if (null != isSelectedSet) {
                isSelectedSet.clear();
                notifyItemChanged(position);
            }
        }

        notifyItemChanged(mList.size() + addServiceModeAndPriceModeList.size());
    }

    public void setVoiceStatus(int voiceStatus) {
        this.voiceStatus = voiceStatus;
        notifyItemChanged(mList.size() + addServiceModeAndPriceModeList.size());
    }

    public void setVoiceDemoDuration(int voiceDemoDuration) {
        this.voiceDemoDuration = voiceDemoDuration;
    }

    public void setVoiceDuration(int voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    public void setServiceCategoryList(List<ServiceCategoryListBean> serviceCategoryList) {
        this.serviceCategoryList = serviceCategoryList;
    }

    public void setServiceModeAndPriceModeList(List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList) {
        this.serviceModeAndPriceModeList = serviceModeAndPriceModeList;

        if (null != this.serviceModeAndPriceModeList && this.serviceModeAndPriceModeList.size() > 0) {
            List<String> subclassNameList = new ArrayList<>();
            Set<Integer> isSelectedSet = new HashSet<>();
            List<Integer> isSelectedCategoryIdList = new ArrayList<>();

            for (int l = 0; l < this.serviceModeAndPriceModeList.size(); l++) {
                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = this.serviceModeAndPriceModeList.get(l);
                String serviceModeName = TypeUtils.getString(serviceModeAndPriceModeBean.serveWayName, "");
                subclassNameList.add(serviceModeName);
            }

            PublishServiceBean tempPublishServiceBean = new PublishServiceBean();

            if (serviceModeAndPriceModeList.size() > 1) {
                tempPublishServiceBean.isShow = true;
            } else {
                tempPublishServiceBean.isShow = false;
            }

            tempPublishServiceBean.isRequired = true;
            tempPublishServiceBean.name = "服务方式";
            tempPublishServiceBean.maxSelectCount = 100;
            tempPublishServiceBean.subclassNameList = subclassNameList;
            tempPublishServiceBean.isSelectedSet = isSelectedSet;
            tempPublishServiceBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
            mList.add(tempPublishServiceBean);

            notifyItemInserted(mList.size());

            if (this.serviceModeAndPriceModeList.size() == 1) {
                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = this.serviceModeAndPriceModeList.get(0);

                if (null != serviceModeAndPriceModeBean) {
                    addServiceModeAndPriceModeList.add(serviceModeAndPriceModeBean);

                    for (int j = 0; j < addServiceModeAndPriceModeList.size(); j++) {
                        ServiceModeAndPriceModeBean addServiceModeAndPriceModeBean = addServiceModeAndPriceModeList.get(j);

                        if (null != addServiceModeAndPriceModeBean) {
                            Integer addIsNeedAddress = addServiceModeAndPriceModeBean.isNeedAddress;

                            if (null == addIsNeedAddress || addIsNeedAddress == 0) {
                                mIsNeedAddress = false;
                            } else if (addIsNeedAddress == 1) {
                                mIsNeedAddress = true;
                                break;
                            }
                        }
                    }

                    notifyItemInserted(mList.size() + addServiceModeAndPriceModeList.size() + 1);
                    notifyItemChanged(mList.size() + addServiceModeAndPriceModeList.size());
                }
            }
        }
    }

    private void showSelectServiceCityDialog(List<IsAllBean> addressList) {
        selectServiceCityDialog = new SelectServiceCityDialog(context, provinceAdapter, cityAdapter, addressList);

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

                    notifyItemChanged(mList.size() + addServiceModeAndPriceModeList.size());
                }
            }
        });
    }

    private void showSeeExampleDialog(String content) {
        seeExampleDialog = new SeeExampleDialog(context, content);

        Window window = seeExampleDialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        seeExampleDialog.show();
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public String getServiceCategoryListString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (null != mList && mList.size() > 0) {
            switch (publishServiceType) {
                case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD:
                    if (null != serviceModeAndPriceModeList && serviceModeAndPriceModeList.size() > 0) {
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
                    break;

                case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE:
                    for (int i = 0; i < mList.size() - 1; i++) {
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

    public String getServiceModeAndPriceModeString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (null != addServiceModeAndPriceModeList && addServiceModeAndPriceModeList.size() > 0) {
            for (int i = 0; i < addServiceModeAndPriceModeList.size(); i++) {
                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = addServiceModeAndPriceModeList.get(i);

                if (null != serviceModeAndPriceModeBean) {
                    int serviceModeId = TypeUtils.getInteger(serviceModeAndPriceModeBean.serveWayId, -1);
                    int priceUnitId = TypeUtils.getInteger(serviceModeAndPriceModeBean.priceUnitId, -1);
                    String price = TypeUtils.getString(serviceModeAndPriceModeBean.price, "0");

                    if (!TextUtils.isEmpty(price) && !price.equals("0")) {
                        stringBuilder.append(serviceModeId).append(":").append(priceUnitId).append(":").append(price);
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("0");
                        break;
                    }
                }

                if (i < addServiceModeAndPriceModeList.size() - 1) {
                    stringBuilder.append(";");
                }
            }
        }
        return stringBuilder.toString();
    }

    public void setAddServiceModeAndPriceModeBean(ServiceModeAndPriceModeBean serviceModeAndPriceModeBean) {
        this.addServiceModeAndPriceModeList.add(serviceModeAndPriceModeBean);
    }

    public void setFirstServiceCategoryIndex(int firstServiceCategoryIndex) {
        this.firstServiceCategoryIndex = firstServiceCategoryIndex;
    }

    public boolean getIsNeedAddress() {
        return mIsNeedAddress;
    }

    public void setAddressList(List<IsAllBean> addressList) {
        this.mAddressList = addressList;
    }

    public String getProvinceCode() {
        return mProvinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        mProvinceCode = provinceCode;
    }

    public String getCityCode() {
        return mCityCode;
    }

    public void setCityCode(String cityCode) {
        mCityCode = cityCode;
    }

    public void setDownloadVoiceDemoFilePath(String downloadVoiceDemoFilePath) {
        this.downloadVoiceDemoFilePath = downloadVoiceDemoFilePath;
    }

    public void setDownloadVoiceFilePath(String downloadVoiceFilePath) {
        this.downloadVoiceFilePath = downloadVoiceFilePath;
    }

    public String getMyMerit() {
        return myMerit;
    }

    public void setMyMerit(String myMerit) {
        this.myMerit = myMerit;
    }

    public String getServiceIntroduction() {
        return serviceIntroduction;
    }

    public void setServiceIntroduction(String serviceIntroduction) {
        this.serviceIntroduction = serviceIntroduction;
    }

    public interface OnClickListener {

        void onCheckIsPublished(int position, Set<Integer> isSelectedSet, int i);

        void onTagClickListener(int tempCategoryId);

        void onDeleteVoice();

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnTouchListener {

        void onRecordImageViewTouch(View view, MotionEvent event);

    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

}