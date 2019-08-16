package com.allintask.lingdao.ui.activity.demand;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.PublishServiceBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.demand.CompileDemandPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.pay.TrusteeshipPayActivity;
import com.allintask.lingdao.ui.adapter.demand.CompileDemandAdapter;
import com.allintask.lingdao.ui.adapter.user.CityAdapter;
import com.allintask.lingdao.ui.adapter.user.ProvinceAdapter;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.view.demand.ICompileDemandView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.allintask.lingdao.widget.SelectServiceCityDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/6/1.
 */

public class CompileDemandActivity extends BaseActivity<ICompileDemandView, CompileDemandPresenter> implements ICompileDemandView {

    private static final int MESSAGE_CODE_EMPLOYMENT_PERIOD = 100;
    private static final int MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_PERIOD = 200;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_demand_category)
    TextView demandCategoryTv;
    @BindView(R.id.ll_employment_way)
    LinearLayout employmentWayLL;
    @BindView(R.id.tfl_employment_way)
    TagFlowLayout employmentWayTFL;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;
    @BindView(R.id.ll_all_employment_city)
    LinearLayout allEmploymentCityLL;
    @BindView(R.id.ll_employment_city)
    LinearLayout employmentCityLL;
    @BindView(R.id.tv_employment_city)
    TextView employmentCityTv;
    @BindView(R.id.rl_subscribe_start_time)
    RelativeLayout subscribeStartTimeRL;
    @BindView(R.id.tv_subscribe_start_time)
    TextView subscribeStartTimeTv;
    @BindView(R.id.ll_employment_period)
    LinearLayout employmentPeriodLL;
    @BindView(R.id.tv_decrease)
    TextView decreaseTv;
    @BindView(R.id.ll_employment_period_subclass)
    LinearLayout employmentPeriodSubclassLL;
    @BindView(R.id.et_employment_period)
    EditText employmentPeriodEt;
    @BindView(R.id.tv_employment_period_unit)
    TextView employmentPeriodUnitTv;
    @BindView(R.id.tv_increase)
    TextView increaseTv;
    @BindView(R.id.rl_employment_price)
    RelativeLayout employmentPriceRL;
    @BindView(R.id.et_employment_price)
    EditText employmentPriceEt;
    @BindView(R.id.tv_see_example)
    TextView seeExampleTv;
    @BindView(R.id.et_demand_introduction)
    EditText demandIntroductionEt;
    @BindView(R.id.tv_demand_introduction_number_of_words)
    TextView demandIntroductionNumberOfWordsTv;
    @BindView(R.id.btn_compile_demand)
    Button compileDemandBtn;

    private int mDemandId = -1;

    private InputMethodManager inputMethodManager;

    private CompileDemandAdapter compileDemandAdapter;

    private List<GetIdToChineseListBean.GetIdToChineseBean> categoryList;

    private List<ServiceCategoryListBean> mServiceCategoryList;
    private List<PublishServiceBean> publishServiceList;

    private int mCategoryId = -1;
    private int employmentWayIndex = -1;
    private int mServiceWayId = -1;
    private List<ServiceModeAndPriceModeBean> mServiceModeAndPriceModeList;

    private Set<Integer> isSelectedSet;

    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    private boolean mIsNeedAddress = false;
    private List<IsAllBean> mAddressList;

    private String mProvinceCode;
    private String mCityCode;
    private String mAddress;

    private SelectServiceCityDialog selectServiceCityDialog;

    private TimePickerBuilder timePickerBuilder;

    private String mSubscribeStartTime;
    private String mCompileDemandSubscribeStartTime;
    private int mEmploymentPeriod = 0;
    private int mPriceUnitId = -1;
    private String mPriceUnit;
    private volatile boolean mEmploymentPeriodIsStart = true;
    private int mMinEmploymentPeriod;
    private int mMaxEmploymentPeriod;
    private boolean mIsTrusteeship = false;
    private int mEmploymentPrice;
    private String mDemandIntroduction;
    private int mShowEmploymentTimes = 0;
    private int mDemandMaxBudget = 0;

    private final CompileDemandHandler compileDemandHandler = new CompileDemandHandler(this);

    private static class CompileDemandHandler extends Handler {

        private final WeakReference<Activity> activityWeakReference;

        public CompileDemandHandler(Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CompileDemandActivity compileDemandActivity = (CompileDemandActivity) activityWeakReference.get();

            if (msg.what == MESSAGE_CODE_EMPLOYMENT_PERIOD) {
                if (compileDemandActivity.mEmploymentPeriod < compileDemandActivity.mMinEmploymentPeriod) {
                    compileDemandActivity.showToast("小于最小雇佣周期");
                    compileDemandActivity.employmentPeriodEt.setText(String.valueOf(compileDemandActivity.mMinEmploymentPeriod));
                } else if (compileDemandActivity.mEmploymentPeriod > compileDemandActivity.mMaxEmploymentPeriod) {
                    compileDemandActivity.showToast("大于最大雇佣周期");
                    compileDemandActivity.employmentPeriodEt.setText(String.valueOf(compileDemandActivity.mMaxEmploymentPeriod));
                }
            } else if (msg.what == MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_PERIOD) {
                compileDemandActivity.employmentPeriodEt.setText(String.valueOf(compileDemandActivity.mEmploymentPeriod));
            }
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_compile_demand;
    }

    @Override
    protected CompileDemandPresenter CreatePresenter() {
        return new CompileDemandPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            mDemandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.compile_demand));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        if (null != mSwipeRefreshStatusLayout) {
            View emptyView = mSwipeRefreshStatusLayout.getEmptyView();

            TextView contentTv = emptyView.findViewById(R.id.tv_content);
            contentTv.setText(getString(R.string.no_data));

            View noNetworkView = mSwipeRefreshStatusLayout.getNoNetworkView();

            Button checkNetworkBtn = noNetworkView.findViewById(R.id.btn_check_network);
            checkNetworkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            });
        }

        recyclerView.setFocusableInTouchMode(false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getParentContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getParentContext(), R.drawable.shape_common_recycler_view_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        compileDemandAdapter = new CompileDemandAdapter(getParentContext());
        recyclerView.setAdapter(compileDemandAdapter);

        employmentPeriodEt.setFocusable(false);
        employmentPriceEt.setFocusable(false);
        demandIntroductionEt.setFocusable(false);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (null != inputMethodManager && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(employmentPeriodEt.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(employmentPriceEt.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(demandIntroductionEt.getWindowToken(), 0);
        }

        employmentWayTFL.setMaxSelectCount(1);
        employmentWayTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                if (null != mServiceModeAndPriceModeList && mServiceModeAndPriceModeList.size() > 0) {
                    boolean isSelected = isSelectedSet.contains(i);

                    if (isSelected) {
                        isSelectedSet.clear();

                        employmentWayIndex = -1;
                        mServiceWayId = -1;
                        mIsNeedAddress = false;

                        mMinEmploymentPeriod = 0;
                        mMaxEmploymentPeriod = 0;

                        mPriceUnitId = -1;
                        mPriceUnit = null;
                    } else {
                        isSelectedSet.clear();
                        isSelectedSet.add(i);

                        employmentWayIndex = i;
                        ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = mServiceModeAndPriceModeList.get(employmentWayIndex);
                        mServiceWayId = TypeUtils.getInteger(serviceModeAndPriceModeBean.serveWayId);
                        Integer isNeedAddress = serviceModeAndPriceModeBean.isNeedAddress;
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
                    }

                    if (mIsNeedAddress) {
                        allEmploymentCityLL.setVisibility(View.VISIBLE);
                    } else {
                        allEmploymentCityLL.setVisibility(View.GONE);
                    }

                    if (mPriceUnitId != -1) {
                        employmentPeriodUnitTv.setText(mPriceUnit);
                        employmentPeriodLL.setVisibility(View.VISIBLE);
                    } else {
                        employmentPeriodLL.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });

        provinceAdapter = new ProvinceAdapter(getParentContext());
        cityAdapter = new CityAdapter(getParentContext());

        decreaseTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String employmentPeriodString = employmentPeriodEt.getText().toString().trim();

                        if (!TextUtils.isEmpty(employmentPeriodString)) {
                            int employmentPeriod = Integer.valueOf(employmentPeriodString);

                            mEmploymentPeriodIsStart = true;
                            mEmploymentPeriod = employmentPeriod;

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (mEmploymentPeriodIsStart) {
                                        if (mEmploymentPeriod > mMinEmploymentPeriod) {
                                            try {
                                                mEmploymentPeriod--;

                                                Message message = Message.obtain();
                                                message.what = MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_PERIOD;
                                                compileDemandHandler.sendMessage(message);

                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            mEmploymentPeriodIsStart = false;
                                        }
                                    }
                                }
                            });

                            thread.start();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mEmploymentPeriodIsStart = false;
                        break;
                }
                return true;
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

                if (compileDemandHandler.hasMessages(MESSAGE_CODE_EMPLOYMENT_PERIOD)) {
                    compileDemandHandler.removeMessages(MESSAGE_CODE_EMPLOYMENT_PERIOD);
                }

                compileDemandHandler.sendEmptyMessageDelayed(MESSAGE_CODE_EMPLOYMENT_PERIOD, 500);
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

                            mEmploymentPeriodIsStart = true;
                            mEmploymentPeriod = employmentPeriod;

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (mEmploymentPeriodIsStart) {
                                        if (mEmploymentPeriod < mMaxEmploymentPeriod) {
                                            try {
                                                mEmploymentPeriod++;

                                                Message message = Message.obtain();
                                                message.what = MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_PERIOD;
                                                compileDemandHandler.sendMessage(message);

                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            mEmploymentPeriodIsStart = false;
                                        }
                                    }
                                }
                            });

                            thread.start();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mEmploymentPeriodIsStart = false;
                        break;
                }
                return true;
            }
        });

        employmentPriceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String employmentPriceString = employmentPriceEt.getText().toString().trim();

                if (!TextUtils.isEmpty(employmentPriceString)) {
                    mEmploymentPrice = Integer.valueOf(employmentPriceString);
                } else {
                    mEmploymentPrice = 0;
                }
            }
        });

        demandIntroductionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mDemandIntroduction = demandIntroductionEt.getText().toString().trim();
                int index = demandIntroductionEt.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(mDemandIntroduction)) {
                        Editable editable = demandIntroductionEt.getText();
                        editable.delete(index, index + 1);
                    }
                }

                int demandIntroductionLength = mDemandIntroduction.length();
                demandIntroductionNumberOfWordsTv.setText(String.valueOf(demandIntroductionLength) + "/500");
            }
        });
    }

    private void initData() {
        publishServiceList = new ArrayList<>();
        isSelectedSet = new HashSet<>();

        mPresenter.getIdToChineseRequest();
    }

    private void showSelectServiceCityDialog(List<IsAllBean> addressList) {
        selectServiceCityDialog = new SelectServiceCityDialog(getParentContext(), provinceAdapter, cityAdapter, mAddressList);

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

                    employmentCityTv.setText(address);
                }
            }
        });
    }

    @OnClick({R.id.tv_employment_city, R.id.rl_subscribe_start_time, R.id.ll_employment_period_subclass, R.id.et_employment_period, R.id.rl_employment_price, R.id.et_employment_price, R.id.et_demand_introduction, R.id.btn_compile_demand})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_employment_city:
                if (null != mAddressList && mAddressList.size() > 0) {
                    showSelectServiceCityDialog(mAddressList);
                } else {
                    showToast("暂无服务城市数据");
                }
                break;

            case R.id.rl_subscribe_start_time:
                employmentPeriodEt.setFocusable(false);
                employmentPriceEt.setFocusable(false);
                demandIntroductionEt.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(employmentPeriodEt.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(employmentPriceEt.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(demandIntroductionEt.getWindowToken(), 0);
                }

                timePickerBuilder = new TimePickerBuilder(getParentContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View view) {
                        try {
                            Date tempDate = new Date();
                            String now = CommonConstant.commonDateFormat.format(tempDate);
                            Date todayDate = CommonConstant.commonDateFormat.parse(now);
                            long todayLong = todayDate.getTime();
                            long selectedLong = date.getTime();

                            if (selectedLong >= todayLong) {
                                mSubscribeStartTime = CommonConstant.subscribeTimeFormat.format(date);
                                mCompileDemandSubscribeStartTime = CommonConstant.refreshTimeFormat.format(date);

                                subscribeStartTimeTv.setText(mSubscribeStartTime);
                            } else {
                                showToast("请选择正确的工作开始时间");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                timePickerBuilder.setType(new boolean[]{true, true, true, true, true, false})
                        .setSubmitText(getString(R.string.confirm))// 确认按钮文字
                        .setCancelText(getString(R.string.cancel))//取消按钮文字
                        .setTitleSize(16)// 标题文字大小
                        .setSubCalSize(14)// 按钮文字大小
                        .setContentTextSize(16)// 滚轮文字大小
                        .setTitleText(getString(R.string.subscribe_start_time))// 标题文字
                        .setOutSideCancelable(true)// 点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setTitleColor(getResources().getColor(R.color.text_dark_black))// 标题文字颜色
                        .setSubmitColor(getResources().getColor(R.color.theme_orange))// 确定按钮文字颜色
                        .setCancelColor(getResources().getColor(R.color.text_dark_black))// 取消按钮文字颜色
                        .setTitleBgColor(getResources().getColor(R.color.white))// 标题背景颜色
                        .setBgColor(getResources().getColor(R.color.white))// 滚轮背景颜色
                        .setLabel("年", "月", "日", "时", "分", "秒")
                        .build().show();
                break;

            case R.id.ll_employment_period_subclass:
                employmentPeriodEt.setFocusable(true);
                employmentPeriodEt.setFocusableInTouchMode(true);
                employmentPeriodEt.requestFocus();
                employmentPeriodEt.findFocus();

                String employmentPeriod = employmentPeriodEt.getText().toString().trim();
                employmentPeriodEt.setSelection(employmentPeriod.length());

                inputMethodManager.showSoftInput(employmentPeriodEt, InputMethodManager.SHOW_FORCED);
                break;

            case R.id.et_employment_period:
                employmentPeriodEt.setFocusable(true);
                employmentPeriodEt.setFocusableInTouchMode(true);
                employmentPeriodEt.requestFocus();
                employmentPeriodEt.findFocus();

                inputMethodManager.showSoftInput(employmentPeriodEt, InputMethodManager.SHOW_FORCED);
                break;

            case R.id.rl_employment_price:
                if (!mIsTrusteeship) {
                    employmentPriceEt.setFocusable(true);
                    employmentPriceEt.setFocusableInTouchMode(true);
                    employmentPriceEt.requestFocus();
                    employmentPriceEt.findFocus();

                    inputMethodManager.showSoftInput(employmentPriceEt, InputMethodManager.SHOW_FORCED);
                }
                break;

            case R.id.et_employment_price:
                if (!mIsTrusteeship) {
                    employmentPriceEt.setFocusable(true);
                    employmentPriceEt.setFocusableInTouchMode(true);
                    employmentPriceEt.requestFocus();
                    employmentPriceEt.findFocus();

                    inputMethodManager.showSoftInput(employmentPriceEt, InputMethodManager.SHOW_FORCED);
                }
                break;

            case R.id.et_demand_introduction:
                demandIntroductionEt.setFocusable(true);
                demandIntroductionEt.setFocusableInTouchMode(true);
                demandIntroductionEt.requestFocus();
                demandIntroductionEt.findFocus();

                inputMethodManager.showSoftInput(demandIntroductionEt, InputMethodManager.SHOW_FORCED);
                break;

            case R.id.btn_compile_demand:
                List<Integer> mustSelectedCategoryIdList = compileDemandAdapter.getMustSelectedCategoryIdList();
                String serviceCategoryListString = compileDemandAdapter.getServiceCategoryListString();

                if (mCategoryId != -1) {
                    if (null != mustSelectedCategoryIdList && mustSelectedCategoryIdList.size() > 0) {
                        if (!TextUtils.isEmpty(serviceCategoryListString)) {
                            boolean isServiceCategoryWhole = true;

                            for (int i = 0; i < mustSelectedCategoryIdList.size(); i++) {
                                int mustSelectedCategoryId = mustSelectedCategoryIdList.get(i);

                                if (!serviceCategoryListString.contains(String.valueOf(mustSelectedCategoryId))) {
                                    isServiceCategoryWhole = false;
                                }
                            }

                            if (isServiceCategoryWhole) {
                                if (mServiceWayId != -1) {
                                    if (mIsNeedAddress) {
                                        if (!TextUtils.isEmpty(mProvinceCode)) {
                                            if (!TextUtils.isEmpty(mCompileDemandSubscribeStartTime)) {
                                                if (mEmploymentPeriod > 0) {
                                                    if (mEmploymentPrice > 0) {
                                                        if (mEmploymentPrice < mDemandMaxBudget) {
                                                            if (!TextUtils.isEmpty(mDemandIntroduction)) {
                                                                if (mDemandIntroduction.length() >= 30) {
                                                                    mPresenter.updateDemandRequest(mDemandId, mCategoryId, serviceCategoryListString, mServiceWayId, mProvinceCode, mCityCode, mCompileDemandSubscribeStartTime, mEmploymentPeriod, mEmploymentPeriod, mPriceUnitId, mEmploymentPrice, mDemandIntroduction, mShowEmploymentTimes);
                                                                } else {
                                                                    showToast("需求介绍不够字数");
                                                                }
                                                            } else {
                                                                showToast("请填写需求介绍");
                                                            }
                                                        } else {
                                                            showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                        }
                                                    } else {
                                                        showToast("请填写正确的雇用价格");
                                                    }
                                                } else {
                                                    showToast("请输入正确的雇佣周期");
                                                }
                                            } else {
                                                showToast("请选择预约工作时间");
                                            }
                                        } else {
                                            showToast("请选择雇佣城市");
                                        }
                                    } else {
                                        if (!TextUtils.isEmpty(mCompileDemandSubscribeStartTime)) {
                                            if (mEmploymentPeriod > 0) {
                                                if (mEmploymentPrice > 0) {
                                                    if (mEmploymentPrice < mDemandMaxBudget) {
                                                        if (!TextUtils.isEmpty(mDemandIntroduction)) {
                                                            if (mDemandIntroduction.length() >= 30) {
                                                                mPresenter.updateDemandRequest(mDemandId, mCategoryId, serviceCategoryListString, mServiceWayId, null, null, mCompileDemandSubscribeStartTime, mEmploymentPeriod, mEmploymentPeriod, mPriceUnitId, mEmploymentPrice, mDemandIntroduction, mShowEmploymentTimes);
                                                            } else {
                                                                showToast("需求介绍不够字数");
                                                            }
                                                        } else {
                                                            showToast("请填写需求介绍");
                                                        }
                                                    } else {
                                                        showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                    }
                                                } else {
                                                    showToast("请填写正确的需求总预算");
                                                }
                                            } else {
                                                showToast("请输入正确的雇佣周期");
                                            }
                                        } else {
                                            showToast("请选择预约工作时间");
                                        }
                                    }
                                } else {
                                    showToast("请选择雇佣方式");
                                }
                            } else {
                                showToast("还有必选项没选择");
                            }
                        } else {
                            showToast("请选择需求品类分类");
                        }
                    } else {
                        if (mServiceWayId != -1) {
                            if (mIsNeedAddress) {
                                if (!TextUtils.isEmpty(mProvinceCode)) {
                                    if (!TextUtils.isEmpty(mCompileDemandSubscribeStartTime)) {
                                        if (mEmploymentPeriod > 0) {
                                            if (mEmploymentPrice > 0) {
                                                if (mEmploymentPrice < mDemandMaxBudget) {
                                                    if (!TextUtils.isEmpty(mDemandIntroduction)) {
                                                        if (mDemandIntroduction.length() >= 30) {
                                                            mPresenter.updateDemandRequest(mDemandId, mCategoryId, serviceCategoryListString, mServiceWayId, mProvinceCode, mCityCode, mCompileDemandSubscribeStartTime, mEmploymentPeriod, mEmploymentPeriod, mPriceUnitId, mEmploymentPrice, mDemandIntroduction, mShowEmploymentTimes);
                                                        } else {
                                                            showToast("需求介绍不够字数");
                                                        }
                                                    } else {
                                                        showToast("请填写需求介绍");
                                                    }
                                                } else {
                                                    showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                }
                                            } else {
                                                showToast("请填写正确的雇用价格");
                                            }
                                        } else {
                                            showToast("请输入正确的雇佣周期");
                                        }
                                    } else {
                                        showToast("请选择预约工作时间");
                                    }
                                } else {
                                    showToast("请选择雇佣城市");
                                }
                            } else {
                                if (!TextUtils.isEmpty(mCompileDemandSubscribeStartTime)) {
                                    if (mEmploymentPeriod > 0) {
                                        if (mEmploymentPrice > 0) {
                                            if (mEmploymentPrice < mDemandMaxBudget) {
                                                if (!TextUtils.isEmpty(mDemandIntroduction)) {
                                                    if (mDemandIntroduction.length() >= 30) {
                                                        mPresenter.updateDemandRequest(mDemandId, mCategoryId, serviceCategoryListString, mServiceWayId, null, null, mCompileDemandSubscribeStartTime, mEmploymentPeriod, mEmploymentPeriod, mPriceUnitId, mEmploymentPrice, mDemandIntroduction, mShowEmploymentTimes);
                                                    } else {
                                                        showToast("需求介绍不够字数");
                                                    }
                                                } else {
                                                    showToast("请填写需求介绍");
                                                }
                                            } else {
                                                showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                            }
                                        } else {
                                            showToast("请填写正确的雇用价格");
                                        }
                                    } else {
                                        showToast("请输入正确的雇佣周期");
                                    }
                                } else {
                                    showToast("请选择预约工作时间");
                                }
                            }
                        } else {
                            showToast("请选择雇佣方式");
                        }
                    }
                } else {
                    showToast("请选择需求品类");
                }
                break;
        }
    }

    @Override
    public void onShowDemandData(int categoryId, String categoryPropertyList, String categoryName, int serveWayId, String provinceCode, String cityCode, Date bookingDate, int employmentCycle, String employmentCycleUnit, boolean isTrusteeship, int budget, String introduce, int showEmploymentTimes) {
        mCategoryId = categoryId;
        mServiceWayId = serveWayId;
        mProvinceCode = provinceCode;
        mCityCode = cityCode;
        mEmploymentPeriod = employmentCycle;
        mIsTrusteeship = isTrusteeship;
        mEmploymentPrice = budget;
        mDemandIntroduction = introduce;
        mShowEmploymentTimes = showEmploymentTimes;

        demandCategoryTv.setText(categoryName);

        if (null != bookingDate) {
            mCompileDemandSubscribeStartTime = CommonConstant.refreshTimeFormat.format(bookingDate);
            mSubscribeStartTime = CommonConstant.commonTimeFormat.format(bookingDate);
            subscribeStartTimeTv.setText(mSubscribeStartTime);
        }

        employmentPeriodEt.setText(String.valueOf(mEmploymentPeriod));

        if (mIsTrusteeship) {
            employmentPriceEt.setFocusable(false);
        } else {
            employmentPriceEt.setFocusable(true);
        }

        if (mEmploymentPrice == 0) {
            mEmploymentPrice = mMinEmploymentPeriod;
        }

        employmentPriceEt.setText(String.valueOf(mEmploymentPeriod));
        demandIntroductionEt.setText(mDemandIntroduction);

        int firstServiceCategoryIndex = -1;

        for (int i = 0; i < categoryList.size(); i++) {
            GetIdToChineseListBean.GetIdToChineseBean getIdToChineseBean = categoryList.get(i);

            if (null != getIdToChineseBean) {
                int code = TypeUtils.getInteger(getIdToChineseBean.code, -1);

                if (code == categoryId) {
                    for (int j = 0; j < mServiceCategoryList.size(); j++) {
                        ServiceCategoryListBean serviceCategoryListBean = mServiceCategoryList.get(j);

                        if (null != serviceCategoryListBean) {
                            int firstCode = TypeUtils.getInteger(serviceCategoryListBean.code, -1);

                            if (firstCode == categoryId) {
                                compileDemandAdapter.setFirstServiceCategoryIndex(j);
                                firstServiceCategoryIndex = j;
                            }
                        }
                    }
                }
            }
        }

        if (firstServiceCategoryIndex != -1) {
            ServiceCategoryListBean serviceCategoryListBean = mServiceCategoryList.get(firstServiceCategoryIndex);
            List<ServiceCategoryListBean.ServiceCategoryFirstBean> serviceCategoryFirstList = serviceCategoryListBean.sub;

            if (null != serviceCategoryFirstList && serviceCategoryFirstList.size() > 0) {
                for (int k = 0; k < serviceCategoryFirstList.size(); k++) {
                    ServiceCategoryListBean.ServiceCategoryFirstBean serviceCategoryFirstBean = serviceCategoryFirstList.get(k);
                    int subclassCategoryId = TypeUtils.getInteger(serviceCategoryFirstBean.code, -1);
                    int mustSelect = TypeUtils.getInteger(serviceCategoryFirstBean.mustSelect, 0);
                    String firstName = TypeUtils.getString(serviceCategoryFirstBean.name, "");
                    int maxSelectLen = TypeUtils.getInteger(serviceCategoryFirstBean.maxSelectLen, 0);
                    List<ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean> serviceCategorySecondList = serviceCategoryFirstList.get(k).sub;

                    if (null != serviceCategorySecondList && serviceCategorySecondList.size() > 0) {
                        List<String> subclassSubclassNameList = new ArrayList<>();
                        Set<Integer> subclassIsSelectedSet = new HashSet<>();
                        List<Integer> subclassIsSelectedCategoryIdList = new ArrayList<>();

                        for (int l = 0; l < serviceCategorySecondList.size(); l++) {
                            ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean serviceCategorySecondBean = serviceCategorySecondList.get(l);
                            String secondName = TypeUtils.getString(serviceCategorySecondBean.name, "");
                            subclassSubclassNameList.add(secondName);
                        }

                        String[] firstArray = categoryPropertyList.split(";");

                        for (String firstStr : firstArray) {
                            String[] secondArray = firstStr.split(":");

                            int j = 0;
                            int key = -1;

                            for (String secondStr : secondArray) {
                                if (!TextUtils.isEmpty(secondStr)) {
                                    if (j % 2 == 0) {
                                        key = Integer.valueOf(secondStr);
                                    } else {
                                        if (key == subclassCategoryId) {
                                            String[] array = secondStr.split(",");

                                            for (String string : array) {
                                                int thirdCategoryId = Integer.valueOf(string);

                                                for (int l = 0; l < serviceCategorySecondList.size(); l++) {
                                                    ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean serviceCategorySecondBean = serviceCategorySecondList.get(l);

                                                    if (null != serviceCategorySecondBean) {
                                                        int code = TypeUtils.getInteger(serviceCategorySecondBean.code, -1);

                                                        if (code == thirdCategoryId) {
                                                            subclassIsSelectedSet.add(l);
                                                        }
                                                    }
                                                }

                                                subclassIsSelectedCategoryIdList.add(thirdCategoryId);
                                            }
                                        }
                                    }

                                    j++;
                                }
                            }
                        }

                        PublishServiceBean tempPublishServiceBean = new PublishServiceBean();
                        tempPublishServiceBean.isShow = true;

                        if (mustSelect == 0) {
                            tempPublishServiceBean.isRequired = false;
                        } else {
                            tempPublishServiceBean.isRequired = true;

                            if (null != compileDemandAdapter) {
                                compileDemandAdapter.addMustSelectedCategoryId(subclassCategoryId);
                            }
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
                        tempPublishServiceBean.subclassNameList = subclassSubclassNameList;
                        tempPublishServiceBean.isSelectedSet = subclassIsSelectedSet;
                        tempPublishServiceBean.isSelectedCategoryIdList = subclassIsSelectedCategoryIdList;
                        publishServiceList.add(tempPublishServiceBean);
                    }
                }

                compileDemandAdapter.setDateList(publishServiceList);
            }
        }

        mPresenter.fetchServiceModeAndPriceModeRequest(mCategoryId);
        mPresenter.fetchAddressListRequest();
    }

    @Override
    public void onShowServiceModeAndPriceModeList(int demandMaxBudget, int maxEmploymentTimes, List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList) {
        mDemandMaxBudget = demandMaxBudget;

        if (null != serviceModeAndPriceModeList && serviceModeAndPriceModeList.size() > 0) {
            int serviceModeAndPriceModeListSize = serviceModeAndPriceModeList.size();
            mServiceModeAndPriceModeList = serviceModeAndPriceModeList;

            if (serviceModeAndPriceModeListSize == 1) {
                employmentWayLL.setVisibility(View.GONE);

                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(0);

                if (null != serviceModeAndPriceModeBean) {
                    int serveWayId = TypeUtils.getInteger(serviceModeAndPriceModeBean.serveWayId, -1);
                    String serveWayName = TypeUtils.getString(serviceModeAndPriceModeBean.serveWayName, "");
                    Integer isNeedAddress = serviceModeAndPriceModeBean.isNeedAddress;
                    int priceUnitId = TypeUtils.getInteger(serviceModeAndPriceModeBean.employmentCycleUnitId, -1);
                    String priceUnit = TypeUtils.getString(serviceModeAndPriceModeBean.employmentCycleUnit, "");
                    Integer employmentCycleMinValue = serviceModeAndPriceModeBean.employmentCycleMinValue;
                    Integer employmentCycleMaxValue = serviceModeAndPriceModeBean.employmentCycleMaxValue;

                    if (serveWayId != -1 && serveWayId == mServiceWayId) {
                        mServiceWayId = serveWayId;

                        if (null == isNeedAddress || isNeedAddress == 0) {
                            mIsNeedAddress = false;
                        } else if (isNeedAddress == 1) {
                            mIsNeedAddress = true;
                        }

                        mPriceUnitId = priceUnitId;
                        mPriceUnit = priceUnit;

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

                        if (mIsNeedAddress) {
                            allEmploymentCityLL.setVisibility(View.VISIBLE);
                        } else {
                            allEmploymentCityLL.setVisibility(View.GONE);
                        }

                        if (mPriceUnitId != -1) {
                            employmentPeriodUnitTv.setText(mPriceUnit);
                            employmentPeriodLL.setVisibility(View.VISIBLE);
                        } else {
                            employmentPeriodLL.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                List<String> subclassNameList = new ArrayList<>();

                for (int i = 0; i < serviceModeAndPriceModeList.size(); i++) {
                    ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(i);

                    if (null != serviceModeAndPriceModeBean) {
                        int serveWayId = TypeUtils.getInteger(serviceModeAndPriceModeBean.serveWayId, -1);
                        String serveWayName = TypeUtils.getString(serviceModeAndPriceModeBean.serveWayName, "");
                        Integer isNeedAddress = serviceModeAndPriceModeBean.isNeedAddress;
                        int priceUnitId = TypeUtils.getInteger(serviceModeAndPriceModeBean.employmentCycleUnitId, -1);
                        String priceUnit = TypeUtils.getString(serviceModeAndPriceModeBean.employmentCycleUnit, "");
                        Integer employmentCycleMinValue = serviceModeAndPriceModeBean.employmentCycleMinValue;
                        Integer employmentCycleMaxValue = serviceModeAndPriceModeBean.employmentCycleMaxValue;

                        if (serveWayId != -1 && serveWayId == mServiceWayId) {
                            mServiceWayId = serveWayId;

                            if (null == isNeedAddress || isNeedAddress == 0) {
                                mIsNeedAddress = false;
                            } else if (isNeedAddress == 1) {
                                mIsNeedAddress = true;
                            }

                            mPriceUnitId = priceUnitId;
                            mPriceUnit = priceUnit;

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

                            isSelectedSet.add(i);

                            if (mIsNeedAddress) {
                                allEmploymentCityLL.setVisibility(View.VISIBLE);
                            } else {
                                allEmploymentCityLL.setVisibility(View.GONE);
                            }

                            if (mPriceUnitId != -1) {
                                employmentPeriodUnitTv.setText(mPriceUnit);
                                employmentPeriodLL.setVisibility(View.VISIBLE);
                            } else {
                                employmentPeriodLL.setVisibility(View.GONE);
                            }
                        }

                        subclassNameList.add(serveWayName);
                    }
                }

                TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                    @Override
                    public View getView(FlowLayout flowLayout, int i, Object o) {
                        TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                        tagTv.setText(String.valueOf(o));
                        return tagTv;
                    }
                };

                tagAdapter.setSelectedList(isSelectedSet);
                employmentWayTFL.setAdapter(tagAdapter);
                employmentWayLL.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCompileDemandSuccess() {
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(employmentPeriodEt.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(employmentPriceEt.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(demandIntroductionEt.getWindowToken(), 0);
        }

        if (mIsTrusteeship) {
            setResult(CommonConstant.REFRESH_RESULT_CODE);
            finish();
        } else {
            Intent intent = new Intent(getParentContext(), TrusteeshipPayActivity.class);
            intent.putExtra(CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE, CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON);
            intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, mDemandId);
            intent.putExtra(CommonConstant.EXTRA_RECOMMEND_MORE_STATUS, CommonConstant.DEMAND_IS_SHARE);
            intent.putExtra(CommonConstant.EXTRA_TRUSTEESHIP_AMOUNT, mEmploymentPrice);
            intent.putExtra(CommonConstant.EXTRA_IS_UPDATE_DEMAND, true);
            startActivity(intent);

            finish();
        }
    }

    @Override
    public void onShowAddressList(List<IsAllBean> addressList) {
        if (null != addressList && addressList.size() > 0) {
            mAddressList = addressList;

            for (int i = 0; i < addressList.size(); i++) {
                IsAllBean isAllBean = addressList.get(i);

                if (null != isAllBean) {
                    String provinceCode = TypeUtils.getString(isAllBean.code, "");
                    String province = TypeUtils.getString(isAllBean.name, "");
                    List<AddressSubBean> cityList = isAllBean.sub;

                    if (!TextUtils.isEmpty(mProvinceCode) && !TextUtils.isEmpty(provinceCode) && provinceCode.equals(mProvinceCode)) {
                        if (null != cityList && cityList.size() > 0) {
                            for (int j = 0; j < cityList.size(); j++) {
                                AddressSubBean addressSubBean = cityList.get(j);

                                if (null != addressSubBean) {
                                    String cityCode = TypeUtils.getString(addressSubBean.code, "");
                                    String city = TypeUtils.getString(addressSubBean.name, "");

                                    if (!TextUtils.isEmpty(mCityCode) && !TextUtils.isEmpty(cityCode) && cityCode.equals(mCityCode)) {
                                        if (TextUtils.isEmpty(city)) {
                                            mAddress = province;
                                        } else {
                                            if (!TextUtils.isEmpty(province) && province.equals(city)) {
                                                mAddress = province;
                                            } else {
                                                mAddress = province + "\r\r" + city;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(mAddress)) {
            employmentCityTv.setText(mAddress);
        } else {
            employmentCityTv.setText(getString(R.string.please_select));
        }
    }

    @Override
    public void onShowGetIdToChineseListBean(GetIdToChineseListBean getIdToChineseListBean) {
        categoryList = getIdToChineseListBean.category;
        mPresenter.fetchDemandCategoryListRequest();
    }

    @Override
    public void onShowServiceCategoryList(List<ServiceCategoryListBean> serviceCategoryList) {
        mServiceCategoryList = serviceCategoryList;
        compileDemandAdapter.setServiceCategoryList(mServiceCategoryList);

        if (mDemandId != -1) {
            mPresenter.fetchDemandRequest(mDemandId);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
