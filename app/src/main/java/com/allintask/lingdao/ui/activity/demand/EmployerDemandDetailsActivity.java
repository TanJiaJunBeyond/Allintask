package com.allintask.lingdao.ui.activity.demand;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.ApplyForRefundReasonBean;
import com.allintask.lingdao.bean.demand.DemandCompletedListBean;
import com.allintask.lingdao.bean.demand.DemandExpiredListBean;
import com.allintask.lingdao.bean.demand.DemandInTheBiddingListBean;
import com.allintask.lingdao.bean.demand.DemandUnderwayListBean;
import com.allintask.lingdao.bean.demand.ShowDemandDetailsBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.demand.EmployerDemandDetailsPresenter;
import com.allintask.lingdao.ui.activity.BaseSwipeRefreshActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.pay.PayActivity;
import com.allintask.lingdao.ui.activity.user.EvaluateActivity;
import com.allintask.lingdao.ui.activity.user.EvaluateDetailsActivity;
import com.allintask.lingdao.ui.adapter.demand.EmployerDemandDetailsAdapter;
import com.allintask.lingdao.ui.adapter.demand.EmployerDemandDetailsHeaderAdapter;
import com.allintask.lingdao.ui.adapter.recommend.DemandDetailsAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.view.demand.IEmployerDemandDetailsView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.allintask.lingdao.widget.SelectApplyForRefundReasonDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.DialogUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/16.
 */

public class EmployerDemandDetailsActivity extends BaseSwipeRefreshActivity<IEmployerDemandDetailsView, EmployerDemandDetailsPresenter> implements IEmployerDemandDetailsView, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_right_first)
    TextView rightFirstTv;
    @BindView(R.id.tv_right_second)
    TextView rightSecondTv;
    @BindView(R.id.iv_right_second)
    ImageView rightSecondIv;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.ll_header)
    LinearLayout headerLL;
    @BindView(R.id.iv_head_portrait)
    ImageView headPortraitIV;
    @BindView(R.id.tv_category_name)
    TextView categoryNameTv;
    @BindView(R.id.tv_time)
    TextView timeTv;
    @BindView(R.id.tv_demand_status)
    TextView demandStatusTv;
    @BindView(R.id.crv_employer_demand_details)
    CommonRecyclerView employerDemandDetailsCRV;
    @BindView(R.id.rl_demand_details_title)
    RelativeLayout demandDetailsTitleRL;
    @BindView(R.id.tv_demand_details_title)
    TextView demandDetailsTitleTv;
    @BindView(R.id.iv_funnel)
    ImageView funnelIv;
    @BindView(R.id.btn_employer_demand_details)
    Button employerDemandDetailsBtn;
    @BindView(R.id.header_recycler_view)
    RecyclerView headerRecyclerView;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    private int demandId;
    private int type = -1;

    private EmployerDemandDetailsActivityBroadcastReceiver employerDemandDetailsActivityBroadcastReceiver;

    private int demandStatus = CommonConstant.DEMAND_STATUS_IN_THE_BIDDING;
    private boolean isPaid = false;

    private DemandDetailsAdapter demandDetailsAdapter;
    private EmployerDemandDetailsHeaderAdapter employerDemandDetailsHeaderAdapter;
    private EmployerDemandDetailsAdapter employerDemandDetailsAdapter;

    private List<ShowDemandDetailsBean> showDemandDetailsList;
    private List<ShowDemandDetailsBean> showingShowDemandDetailsList;

    private ObjectAnimator funnelObjectAnimator;

    private int mOrderId;
    private SelectApplyForRefundReasonDialog selectApplyForRefundReasonDialog;

    private int mIsShare;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_employer_demand_details;
    }

    @Override
    protected EmployerDemandDetailsPresenter CreatePresenter() {
        return new EmployerDemandDetailsPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            demandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
            type = intent.getIntExtra(CommonConstant.EXTRA_TYPE, -1);
        }

        registerEmployerDemandDetailsActivityReceiver();

        initToolbar();
        initUI();
        initData();
    }

    private void registerEmployerDemandDetailsActivityReceiver() {
        employerDemandDetailsActivityBroadcastReceiver = new EmployerDemandDetailsActivityBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConstant.ACTION_REFRESH);
        intentFilter.addAction(CommonConstant.ACTION_REFRESH_COMPILE_DEMAND);
        AllintaskApplication.getInstance().getLocalBroadcastManager().registerReceiver(employerDemandDetailsActivityBroadcastReceiver, intentFilter);
    }

    private class EmployerDemandDetailsActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();

                if (!TextUtils.isEmpty(action)) {
                    if (action.equals(CommonConstant.ACTION_REFRESH)) {
                        String price = intent.getStringExtra(CommonConstant.EXTRA_PRICE);

                        if (!TextUtils.isEmpty(price)) {
                            type = CommonConstant.LOOK_OVER_WIN_THE_BIDDING;
                            initData();
                        }
                    }

                    if (action.equals(CommonConstant.ACTION_REFRESH_COMPILE_DEMAND)) {
                        initData();
                    }
                }
            }
        }

    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPaid) {
                    Intent intent = new Intent(getParentContext(), MainActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.DEMAND_MANAGEMENT_FRAGMENT);
                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_UNDERWAY);
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        });

        toolbar.setTitle("");

        titleTv.setText(getString(R.string.demand_details));

        rightFirstTv.setVisibility(View.GONE);

        rightSecondTv.setText(getString(R.string.compile));
        rightSecondTv.setVisibility(View.GONE);

        rightSecondIv.setImageResource(R.mipmap.ic_delete);
        rightSecondIv.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    swipe_refresh_layout.setEnabled(true);
                } else {
                    swipe_refresh_layout.setEnabled(false);
                }
            }
        });

        showDemandDetailsList = new ArrayList<>();
        showingShowDemandDetailsList = new ArrayList<>();

        AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(WindowUtils.getScreenWidth(getParentContext()), LinearLayout.LayoutParams.WRAP_CONTENT);
        employerDemandDetailsCRV.setLayoutParams(layoutParams);

        demandDetailsAdapter = new DemandDetailsAdapter(getParentContext());
        employerDemandDetailsCRV.setAdapter(demandDetailsAdapter);

        initHeaderRecyclerView();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getParentContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getParentContext(), R.drawable.shape_common_recycler_view_divider));
        recycler_view.addItemDecoration(dividerItemDecoration);

        employerDemandDetailsAdapter = new EmployerDemandDetailsAdapter(getParentContext(), 0);
        recycler_view.setAdapter(employerDemandDetailsAdapter);

        employerDemandDetailsAdapter.setOnClickListener(new EmployerDemandDetailsAdapter.OnClickListener() {
            @Override
            public void onPayClick(int userId, int serviceId, String sellerName, int paymentMoney, int earnestMoney) {
                Intent intent = new Intent(getParentContext(), PayActivity.class);
                intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                intent.putExtra(CommonConstant.EXTRA_SELLER_NAME, sellerName);
                intent.putExtra(CommonConstant.EXTRA_PAYMENT_MONEY, paymentMoney);
                intent.putExtra(CommonConstant.EXTRA_EARNEST_MONEY, earnestMoney);
                startActivityForResult(intent, CommonConstant.REQUEST_CODE);
            }

            @Override
            public void onApplyForRefundClick(int userId, int orderStatus, int orderId, int orderPrice) {
                mOrderId = orderId;

                if (orderStatus == CommonConstant.ORDER_STATUS_WAIT_FOR_START) {
                    mPresenter.fetchRefundReasonListRequest(userId, demandId);
                } else {
                    Intent intent = new Intent(getParentContext(), ApplyForRefundActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                    intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                    intent.putExtra(CommonConstant.EXTRA_ORDER_ID, mOrderId);
                    intent.putExtra(CommonConstant.EXTRA_ORDER_PRICE, orderPrice);
                    startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                }
            }

            @Override
            public void onApplyForArbitramentClick(int userId, int orderId) {
                Intent intent = new Intent(getParentContext(), ApplyForArbitramentActivity.class);
                intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                intent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                startActivityForResult(intent, CommonConstant.REQUEST_CODE);
            }

            @Override
            public void onConfirmStartWorkClick(int userId, int orderId, Date startWorkDate) {
                mPresenter.buyerConfirmStartWorkRequest(userId, demandId, orderId, startWorkDate);
            }

            @Override
            public void onConfirmCompleteWorkClick(int userId, int orderId) {
                mPresenter.buyerConfirmCompleteWorkRequest(userId, demandId, orderId);
            }

            @Override
            public void onDelayCompleteWorkClick(int userId, int orderId) {
                mPresenter.buyerDelayCompleteWorkRequest(userId, demandId, orderId);
            }

            @Override
            public void onEvaluateClick(int userId, int orderId, int serviceId, int buyerUserId, int sellerUserId) {
                Intent intent = new Intent(getParentContext(), EvaluateActivity.class);
                intent.putExtra(CommonConstant.EXTRA_EVALUATE_TYPE, CommonConstant.EVALUATE_TYPE_SERVICE);
                intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                intent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                intent.putExtra(CommonConstant.EXTRA_BUYER_USER_ID, buyerUserId);
                intent.putExtra(CommonConstant.EXTRA_SELLER_USER_ID, sellerUserId);
                startActivityForResult(intent, CommonConstant.REQUEST_CODE);
            }

            @Override
            public void onSeeEvaluateClick(int userId, int orderId) {
                Intent intent = new Intent(getParentContext(), EvaluateDetailsActivity.class);
                intent.putExtra(CommonConstant.EXTRA_EVALUATE_DETAILS_TYPE, CommonConstant.EVALUATE_DETAILS_TYPE_SERVICE);
                intent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mPresenter.fetchDemandDetailsRequest(demandId);
    }

    private void initHeaderRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getParentContext(), LinearLayoutManager.HORIZONTAL, false);
        headerRecyclerView.setLayoutManager(linearLayoutManager);

        AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(WindowUtils.getScreenWidth(getParentContext()), LinearLayout.LayoutParams.WRAP_CONTENT);
        headerRecyclerView.setLayoutParams(layoutParams);

        employerDemandDetailsHeaderAdapter = new EmployerDemandDetailsHeaderAdapter(getParentContext());
        headerRecyclerView.setAdapter(employerDemandDetailsHeaderAdapter);

        employerDemandDetailsHeaderAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int itemCount = employerDemandDetailsHeaderAdapter.getItemCount();

                for (int i = 0; i < itemCount; i++) {
                    switch (demandStatus) {
                        case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                            DemandInTheBiddingListBean.DemandInTheBiddingBean demandInTheBiddingBean = (DemandInTheBiddingListBean.DemandInTheBiddingBean) employerDemandDetailsHeaderAdapter.getItem(i);

                            if (null != demandInTheBiddingBean && i == position) {
                                demandInTheBiddingBean.isSelected = true;
                            } else {
                                demandInTheBiddingBean.isSelected = false;
                            }
                            break;

                        case CommonConstant.DEMAND_STATUS_UNDERWAY:
                            DemandUnderwayListBean.DemandUnderwayBean demandUnderwayBean = (DemandUnderwayListBean.DemandUnderwayBean) employerDemandDetailsAdapter.getItem(i);

                            if (null != demandUnderwayBean && i == position) {
                                demandUnderwayBean.isSelected = true;
                            } else {
                                demandUnderwayBean.isSelected = false;
                            }
                            break;

                        case CommonConstant.DEMAND_STATUS_COMPLETED:
                            DemandCompletedListBean.DemandCompletedBean demandCompletedBean = (DemandCompletedListBean.DemandCompletedBean) employerDemandDetailsAdapter.getItem(i);

                            if (null != demandCompletedBean && i == position) {
                                demandCompletedBean.isSelected = true;
                            } else {
                                demandCompletedBean.isSelected = false;
                            }
                            break;

                        case CommonConstant.DEMAND_STATUS_EXPIRED:
                            DemandExpiredListBean.DemandExpiredBean demandExpiredBean = (DemandExpiredListBean.DemandExpiredBean) employerDemandDetailsAdapter.getItem(i);

                            if (null != demandExpiredBean && i == position) {
                                demandExpiredBean.isSelected = true;
                            } else {
                                demandExpiredBean.isSelected = false;
                            }
                            break;
                    }
                }

                if (null != employerDemandDetailsHeaderAdapter) {
                    employerDemandDetailsHeaderAdapter.notifyDataSetChanged();
                }

                if (null != recycler_view) {
                    recycler_view.smoothScrollToPosition(position);
                }
            }
        });
    }

    private void startFunnelObjectAnimator() {
        funnelObjectAnimator = ObjectAnimator.ofFloat(funnelIv, "rotation", 0F, 360F);
        funnelObjectAnimator.setDuration(1000);
        funnelObjectAnimator.setRepeatCount(-1);
        funnelObjectAnimator.start();
    }

    private void showSelectApplyForRefundReasonDialog(final int userId, final int demandId, final int orderId, final List<ApplyForRefundReasonBean> applyForRefundReasonList) {
        selectApplyForRefundReasonDialog = new SelectApplyForRefundReasonDialog(getParentContext(), applyForRefundReasonList);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = selectApplyForRefundReasonDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        selectApplyForRefundReasonDialog.show();
        selectApplyForRefundReasonDialog.setOnClickListener(new SelectApplyForRefundReasonDialog.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                if (null != applyForRefundReasonList && applyForRefundReasonList.size() > 0) {
                    ApplyForRefundReasonBean applyForRefundReasonBean = applyForRefundReasonList.get(position);

                    if (null != applyForRefundReasonBean) {
                        int id = TypeUtils.getInteger(applyForRefundReasonBean.id, -1);
                        String tag = TypeUtils.getString(applyForRefundReasonBean.tag, "");

                        if (id != -1 && !TextUtils.isEmpty(tag)) {
                            mPresenter.applyRefundRequest(userId, demandId, orderId, id, tag);
                        }
                    }
                }
            }
        });
    }

    private void sendRefundedMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_refunded_content), String.valueOf(sendUserId));

        if (!TextUtils.isEmpty(nickname)) {
            emMessage.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                emMessage.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(headPortraitUrl)) {
            String tempHeadPortrait = headPortraitUrl.replace("https:", "");
            emMessage.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
        }

        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, CommonConstant.MESSAGE_ATTRIBUTE_TYPE_MESSAGE);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_REFUNDED);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_refunded_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_refunded_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_refunded_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    private void sendStartWorkMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_start_work_content), String.valueOf(sendUserId));

        if (!TextUtils.isEmpty(nickname)) {
            emMessage.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                emMessage.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(headPortraitUrl)) {
            String tempHeadPortrait = headPortraitUrl.replace("https:", "");
            emMessage.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
        }

        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, CommonConstant.MESSAGE_ATTRIBUTE_TYPE_MESSAGE);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_START_WORK);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_start_work_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_start_work_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_start_work_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    private void sendOrderFinishedMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_order_finished_content), String.valueOf(sendUserId));

        if (!TextUtils.isEmpty(nickname)) {
            emMessage.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                emMessage.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(headPortraitUrl)) {
            String tempHeadPortrait = headPortraitUrl.replace("https:", "");
            emMessage.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
        }

        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, CommonConstant.MESSAGE_ATTRIBUTE_TYPE_MESSAGE);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_ORDER_FINISHED);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_order_finished_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_order_finished_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_order_finished_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_evaluate));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_evaluate));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_COMPLETED);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    private void sendDelayMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_delay_content), String.valueOf(sendUserId));

        if (!TextUtils.isEmpty(nickname)) {
            emMessage.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                emMessage.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(headPortraitUrl)) {
            String tempHeadPortrait = headPortraitUrl.replace("https:", "");
            emMessage.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
        }

        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, CommonConstant.MESSAGE_ATTRIBUTE_TYPE_MESSAGE);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_DELAY);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_delay_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_delay_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_delay_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_EMPLOYER);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @OnClick({R.id.tv_right_first, R.id.tv_right_second, R.id.iv_right_second, R.id.btn_employer_demand_details})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right_first:
                rightFirstTv.setOnClickListener(null);

                if (demandId != -1) {
                    String onlineStatus = rightFirstTv.getText().toString().trim();

                    if (onlineStatus.equals(getString(R.string.offline))) {
                        DialogUtils.showAlertDialog(getParentContext(), "需求下线", "需求下线后平台停止推荐和显示，但不影响订单的进行，请确认是否下线。", "确认下线", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mPresenter.goOfflineDemandRequest(demandId);
                            }
                        }, "再想想", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    } else if (onlineStatus.equals(getString(R.string.online))) {
                        mPresenter.goOnlineDemandRequest(demandId);
                    }
                }
                break;

            case R.id.tv_right_second:
                Intent compileDemandIntent = new Intent(getParentContext(), CompileDemandActivity.class);
                compileDemandIntent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                startActivityForResult(compileDemandIntent, CommonConstant.REQUEST_CODE);
                break;

            case R.id.iv_right_second:
                DialogUtils.showAlertDialog(getParentContext(), "需求删除", "需求删除后，所有投标记录将会删除，操作不可逆，请确认是否删除。", "确认删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (demandId != -1) {
                            mPresenter.deleteDemandRequest(demandId);
                        }
                    }
                }, "再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.btn_employer_demand_details:
                employerDemandDetailsBtn.setOnClickListener(null);

                if (demandStatus == CommonConstant.DEMAND_STATUS_UNDERWAY) {
                    if (type == CommonConstant.LOOK_OVER_WIN_THE_BIDDING) {
                        type = CommonConstant.SELECT_MORE_BIDDING;

                        employerDemandDetailsBtn.setText("查看中标的");

                        employerDemandDetailsHeaderAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);
                        employerDemandDetailsAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);

                        mPresenter.refresh(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING, demandId);
                    } else if (type == CommonConstant.SELECT_MORE_BIDDING) {
                        type = CommonConstant.LOOK_OVER_WIN_THE_BIDDING;

                        employerDemandDetailsBtn.setText("选更多标");

                        employerDemandDetailsHeaderAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_UNDERWAY);
                        employerDemandDetailsAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_UNDERWAY);

                        mPresenter.refresh(CommonConstant.DEMAND_STATUS_UNDERWAY, demandId);
                    }
                } else if (demandStatus == CommonConstant.DEMAND_STATUS_COMPLETED) {
                    if (type == CommonConstant.LOOK_OVER_COMPLETE) {
                        type = CommonConstant.LOOK_OVER_BIDDER;

                        employerDemandDetailsBtn.setText("查看完成的");

                        employerDemandDetailsHeaderAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_EXPIRED);
                        employerDemandDetailsAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_EXPIRED);

                        mPresenter.refresh(CommonConstant.DEMAND_STATUS_EXPIRED, demandId);
                    } else if (type == CommonConstant.LOOK_OVER_BIDDER) {
                        type = CommonConstant.LOOK_OVER_COMPLETE;

                        employerDemandDetailsBtn.setText("查看投标者");

                        employerDemandDetailsHeaderAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_COMPLETED);
                        employerDemandDetailsAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_COMPLETED);

                        mPresenter.refresh(CommonConstant.DEMAND_STATUS_COMPLETED, demandId);
                    }
                }
                break;
        }
    }

    @Override
    protected void onLoadMore() {
        if (demandStatus == CommonConstant.DEMAND_STATUS_UNDERWAY) {
            if (type == CommonConstant.LOOK_OVER_WIN_THE_BIDDING) {
                mPresenter.loadMore(CommonConstant.DEMAND_STATUS_UNDERWAY, demandId);
            } else if (type == CommonConstant.SELECT_MORE_BIDDING) {
                mPresenter.loadMore(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING, demandId);
            }
        } else if (demandStatus == CommonConstant.DEMAND_STATUS_COMPLETED) {
            if (type == CommonConstant.LOOK_OVER_COMPLETE) {
                mPresenter.loadMore(CommonConstant.DEMAND_STATUS_COMPLETED, demandId);
            } else if (type == CommonConstant.LOOK_OVER_BIDDER) {
                mPresenter.loadMore(CommonConstant.DEMAND_STATUS_EXPIRED, demandId);
            }
        } else {
            mPresenter.loadMore(demandStatus, demandId);
        }
    }

    @Override
    protected void onSwipeRefresh() {
        mPresenter.fetchDemandDetailsRequest(demandId);
    }

    @Override
    public void onShowDemandInTheBiddingList(List<DemandInTheBiddingListBean.DemandInTheBiddingBean> demandInTheBiddingList) {
        if (null != demandInTheBiddingList && demandInTheBiddingList.size() > 0) {
            if (null != employerDemandDetailsHeaderAdapter && null != employerDemandDetailsAdapter) {
                StringBuilder stringBuilder = new StringBuilder();

                if (type == CommonConstant.SELECT_MORE_BIDDING) {
                    stringBuilder.append("已有").append(String.valueOf(demandInTheBiddingList.size())).append("位服务商投标");
                } else {
                    stringBuilder.append("已有").append(String.valueOf(demandInTheBiddingList.size())).append("位服务商投标，等待支付中");
                }

                demandDetailsTitleTv.setText(stringBuilder);

                DemandInTheBiddingListBean.DemandInTheBiddingBean demandInTheBiddingBean = demandInTheBiddingList.get(0);
                demandInTheBiddingBean.isSelected = true;

                employerDemandDetailsHeaderAdapter.setDateList(demandInTheBiddingList);
                employerDemandDetailsAdapter.setDateList(demandInTheBiddingList);

                if (mIsShare == 0) {
                    headerRecyclerView.setVisibility(View.GONE);
                } else if (mIsShare == 1) {
                    headerRecyclerView.setVisibility(View.VISIBLE);
                }

                nestedScrollView.setVisibility(View.GONE);
            }
        } else {
            headerRecyclerView.setVisibility(View.GONE);
            recycler_view.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }

        employerDemandDetailsBtn.setOnClickListener(this);
    }

    @Override
    public void onShowDemandUnderwayList(List<DemandUnderwayListBean.DemandUnderwayBean> demandUnderwayList) {
        if (null != demandUnderwayList && demandUnderwayList.size() > 0) {
            if (null != employerDemandDetailsHeaderAdapter && null != employerDemandDetailsAdapter) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("已支付").append(String.valueOf(demandUnderwayList.size())).append("位服务商，请安排工作");
                demandDetailsTitleTv.setText(stringBuilder);

                DemandUnderwayListBean.DemandUnderwayBean demandUnderwayBean = demandUnderwayList.get(0);
                demandUnderwayBean.isSelected = true;

                employerDemandDetailsHeaderAdapter.setDateList(demandUnderwayList);
                employerDemandDetailsAdapter.setDateList(demandUnderwayList);

                if (mIsShare == 0) {
                    headerRecyclerView.setVisibility(View.GONE);
                } else if (mIsShare == 1) {
                    headerRecyclerView.setVisibility(View.VISIBLE);
                }

                recycler_view.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
            }
        } else {
            headerRecyclerView.setVisibility(View.GONE);
            recycler_view.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }

        employerDemandDetailsBtn.setOnClickListener(this);
    }

    @Override
    public void onShowDemandCompletedList(List<DemandCompletedListBean.DemandCompletedBean> demandCompletedList) {
        if (null != demandCompletedList && demandCompletedList.size() > 0) {
            if (null != employerDemandDetailsHeaderAdapter && null != employerDemandDetailsAdapter) {
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(demandCompletedList.size())).append("位服务商已完成工作！");
                demandDetailsTitleTv.setText(stringBuilder);

                DemandCompletedListBean.DemandCompletedBean demandCompletedBean = demandCompletedList.get(0);
                demandCompletedBean.isSelected = true;

                employerDemandDetailsHeaderAdapter.setDateList(demandCompletedList);
                employerDemandDetailsAdapter.setDateList(demandCompletedList);

                if (mIsShare == 0) {
                    headerRecyclerView.setVisibility(View.GONE);
                } else if (mIsShare == 1) {
                    headerRecyclerView.setVisibility(View.VISIBLE);
                }

                recycler_view.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
            }
        } else {
            headerRecyclerView.setVisibility(View.GONE);
            recycler_view.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }

        employerDemandDetailsBtn.setOnClickListener(this);
    }

    @Override
    public void onShowDemandExpiredList(List<DemandExpiredListBean.DemandExpiredBean> demandExpiredList) {
        if (null != demandExpiredList && demandExpiredList.size() > 0) {
            if (null != employerDemandDetailsHeaderAdapter && null != employerDemandDetailsAdapter) {
                if (type == CommonConstant.LOOK_OVER_BIDDER) {
                    StringBuilder stringBuilder = new StringBuilder("已有").append(String.valueOf(demandExpiredList.size())).append("位服务商投标");
                    demandDetailsTitleTv.setText(stringBuilder);
                } else {
                    demandDetailsTitleTv.setText("需求已过期，自动拒绝所有服务商");
                }

                DemandExpiredListBean.DemandExpiredBean demandExpiredBean = demandExpiredList.get(0);
                demandExpiredBean.isSelected = true;

                employerDemandDetailsHeaderAdapter.setDateList(demandExpiredList);
                employerDemandDetailsAdapter.setDateList(demandExpiredList);

                if (mIsShare == 0) {
                    headerRecyclerView.setVisibility(View.GONE);
                } else if (mIsShare == 1) {
                    headerRecyclerView.setVisibility(View.VISIBLE);
                }

                recycler_view.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
            }
        } else {
            headerRecyclerView.setVisibility(View.GONE);
            recycler_view.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }

        employerDemandDetailsBtn.setOnClickListener(this);
    }

    @Override
    public void onConfirmStartWorkSuccess(int userId, int demandId) {
        showToast("确认开始工作成功");
        sendStartWorkMessage(userId, demandId);

        if (demandStatus == CommonConstant.DEMAND_STATUS_UNDERWAY) {
            if (type == CommonConstant.LOOK_OVER_WIN_THE_BIDDING) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_UNDERWAY, demandId);
            } else if (type == CommonConstant.SELECT_MORE_BIDDING) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING, demandId);
            }
        } else if (demandStatus == CommonConstant.DEMAND_STATUS_COMPLETED) {
            if (type == CommonConstant.LOOK_OVER_COMPLETE) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_COMPLETED, demandId);
            } else if (type == CommonConstant.LOOK_OVER_BIDDER) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_EXPIRED, demandId);
            }
        } else {
            mPresenter.refresh(demandStatus, demandId);
        }
    }

    @Override
    public void onConfirmCompleteWorkSuccess(int userId, int demandId) {
        showToast("确认完成工作成功");
        sendOrderFinishedMessage(userId, demandId);

        if (demandStatus == CommonConstant.DEMAND_STATUS_UNDERWAY) {
            if (type == CommonConstant.LOOK_OVER_WIN_THE_BIDDING) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_UNDERWAY, demandId);
            } else if (type == CommonConstant.SELECT_MORE_BIDDING) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING, demandId);
            }
        } else if (demandStatus == CommonConstant.DEMAND_STATUS_COMPLETED) {
            if (type == CommonConstant.LOOK_OVER_COMPLETE) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_COMPLETED, demandId);
            } else if (type == CommonConstant.LOOK_OVER_BIDDER) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_EXPIRED, demandId);
            }
        } else {
            mPresenter.refresh(demandStatus, demandId);
        }
    }

    @Override
    public void onDelayCompleteWorkSuccess(int userId, int demandId) {
        showToast("延长完成时间成功");
        sendDelayMessage(userId, demandId);

        if (demandStatus == CommonConstant.DEMAND_STATUS_UNDERWAY) {
            if (type == CommonConstant.LOOK_OVER_WIN_THE_BIDDING) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_UNDERWAY, demandId);
            } else if (type == CommonConstant.SELECT_MORE_BIDDING) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING, demandId);
            }
        } else if (demandStatus == CommonConstant.DEMAND_STATUS_COMPLETED) {
            if (type == CommonConstant.LOOK_OVER_COMPLETE) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_COMPLETED, demandId);
            } else if (type == CommonConstant.LOOK_OVER_BIDDER) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_EXPIRED, demandId);
            }
        } else {
            mPresenter.refresh(demandStatus, demandId);
        }
    }

    @Override
    public void onShowDemandStatus(int demandStatus) {
        switch (demandStatus) {
            case CommonConstant.DEMAND_DETAILS_STATUS_IN_THE_BIDDING:
                this.demandStatus = CommonConstant.DEMAND_STATUS_IN_THE_BIDDING;

                rightSecondTv.setVisibility(View.VISIBLE);
                rightSecondIv.setVisibility(View.VISIBLE);

                demandStatusTv.setText(getString(R.string.demand_status_in_the_bidding));
                funnelIv.setVisibility(View.VISIBLE);
                employerDemandDetailsBtn.setVisibility(View.GONE);

                demandDetailsTitleTv.setText("已有0位服务商投标，等待支付中");

                startFunnelObjectAnimator();

                if (null != employerDemandDetailsHeaderAdapter) {
                    employerDemandDetailsHeaderAdapter.setDemandStatus(this.demandStatus);
                }

                if (null != employerDemandDetailsAdapter) {
                    employerDemandDetailsAdapter.setDemandStatus(this.demandStatus);
                }

                mPresenter.refresh(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING, demandId);
                break;

            case CommonConstant.DEMAND_DETAILS_STATUS_UNDERWAY:
                this.demandStatus = CommonConstant.DEMAND_STATUS_UNDERWAY;

                rightSecondTv.setVisibility(View.GONE);
                rightSecondIv.setVisibility(View.GONE);

                demandStatusTv.setText(getString(R.string.demand_status_underway));
                funnelIv.setVisibility(View.GONE);

                if (type == -1) {
                    type = CommonConstant.LOOK_OVER_WIN_THE_BIDDING;
                }

                if (type == CommonConstant.LOOK_OVER_WIN_THE_BIDDING) {
                    employerDemandDetailsBtn.setText("选更多标");

                    employerDemandDetailsHeaderAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_UNDERWAY);
                    employerDemandDetailsAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_UNDERWAY);

                    mPresenter.refresh(CommonConstant.DEMAND_STATUS_UNDERWAY, demandId);
                } else if (type == CommonConstant.SELECT_MORE_BIDDING) {
                    employerDemandDetailsBtn.setText("查看中标的");

                    employerDemandDetailsHeaderAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);
                    employerDemandDetailsAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);

                    mPresenter.refresh(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING, demandId);
                }
                break;

            case CommonConstant.DEMAND_DETAILS_STATUS_COMPLETED:
                this.demandStatus = CommonConstant.DEMAND_STATUS_COMPLETED;

                rightSecondTv.setVisibility(View.GONE);
                rightSecondIv.setVisibility(View.GONE);

                demandStatusTv.setText(getString(R.string.demand_status_completed));
                funnelIv.setVisibility(View.GONE);

                if (type == -1) {
                    type = CommonConstant.LOOK_OVER_COMPLETE;
                }

                if (type == CommonConstant.LOOK_OVER_COMPLETE) {
                    employerDemandDetailsBtn.setText("查看投标者");

                    employerDemandDetailsHeaderAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_COMPLETED);
                    employerDemandDetailsAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_COMPLETED);

                    mPresenter.refresh(CommonConstant.DEMAND_STATUS_COMPLETED, demandId);
                } else if (type == CommonConstant.LOOK_OVER_BIDDER) {
                    employerDemandDetailsBtn.setText("查看完成的");

                    employerDemandDetailsHeaderAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_EXPIRED);
                    employerDemandDetailsAdapter.setDemandStatus(CommonConstant.DEMAND_STATUS_EXPIRED);

                    mPresenter.refresh(CommonConstant.DEMAND_STATUS_EXPIRED, demandId);
                }
                break;

            case CommonConstant.DEMAND_DETAILS_STATUS_EXPIRED:
                this.demandStatus = CommonConstant.DEMAND_STATUS_EXPIRED;

                rightSecondTv.setVisibility(View.GONE);
                rightSecondIv.setVisibility(View.GONE);

                demandStatusTv.setText(getString(R.string.demand_status_expired));
                funnelIv.setVisibility(View.GONE);

                employerDemandDetailsBtn.setVisibility(View.GONE);
                demandDetailsTitleTv.setText("需求已过期，自动拒绝所有服务商");

                if (null != employerDemandDetailsHeaderAdapter) {
                    employerDemandDetailsHeaderAdapter.setDemandStatus(this.demandStatus);
                }

                if (null != employerDemandDetailsAdapter) {
                    employerDemandDetailsAdapter.setDemandStatus(this.demandStatus);
                }

                mPresenter.refresh(CommonConstant.DEMAND_STATUS_EXPIRED, demandId);
                break;
        }
    }

    @Override
    public void onShowUserHeadPortraitUrl(String userHeadPortraitUrl) {
        ImageViewUtil.setImageView(getParentContext(), headPortraitIV, userHeadPortraitUrl, R.mipmap.ic_default_avatar);
    }

    @Override
    public void onShowCategoryName(String categoryName) {
        categoryNameTv.setText(categoryName);
    }

    @Override
    public void onShowTime(String time) {
        timeTv.setText(time);
    }

    @Override
    public void onShowShowDemandDetailsList(List<ShowDemandDetailsBean> showDemandDetailsList) {
        if (null != showDemandDetailsList && showDemandDetailsList.size() > 0) {
            this.showDemandDetailsList.clear();
            this.showingShowDemandDetailsList.clear();

            this.showDemandDetailsList.addAll(showDemandDetailsList);

            for (int i = 0; i < this.showDemandDetailsList.size(); i++) {
                if (i > 2) {
                    break;
                }

                ShowDemandDetailsBean showDemandDetailsBean = this.showDemandDetailsList.get(i);
                showingShowDemandDetailsList.add(showDemandDetailsBean);
            }

            demandDetailsAdapter.setShowDemandDetailsList(this.showDemandDetailsList);
            demandDetailsAdapter.setIsShowMore(false);
            demandDetailsAdapter.setDateList(showingShowDemandDetailsList);
        }
    }

    @Override
    public void onShowAuditCodeAndOnOffLine(int auditCode, int onOffLine) {
        if (demandStatus == CommonConstant.DEMAND_STATUS_IN_THE_BIDDING) {
            if (auditCode == 2) {
                rightFirstTv.setVisibility(View.GONE);
            } else {
                if (onOffLine == 0) {
                    rightFirstTv.setText(getString(R.string.online));
                } else {
                    rightFirstTv.setText(getString(R.string.offline));
                }

                rightFirstTv.setVisibility(View.VISIBLE);
            }
        } else {
            rightFirstTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowIsShare(int isShare) {
        mIsShare = isShare;

        if (mIsShare == 0) {
            demandDetailsTitleRL.setVisibility(View.GONE);
            headerRecyclerView.setVisibility(View.GONE);
        } else if (mIsShare == 1) {
            demandDetailsTitleRL.setVisibility(View.VISIBLE);
            headerRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShowNoServiceProvider() {
        switch (demandStatus) {
            case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                if (type == CommonConstant.SELECT_MORE_BIDDING) {
                    demandDetailsTitleTv.setText("已有0位服务商投标");
                } else {
                    demandDetailsTitleTv.setText("已有0位服务商投标，等待支付中");
                }
                break;

            case CommonConstant.DEMAND_STATUS_UNDERWAY:
                demandDetailsTitleTv.setText("已有0位服务商投标");
                break;

            case CommonConstant.DEMAND_STATUS_COMPLETED:
                demandDetailsTitleTv.setText("已支付0位服务商，请安排工作");
                break;

            case CommonConstant.DEMAND_STATUS_EXPIRED:
                if (type == CommonConstant.LOOK_OVER_BIDDER) {
                    demandDetailsTitleTv.setText("已有0位服务商投标");
                } else {
                    demandDetailsTitleTv.setText("需求已过期，自动拒绝所有服务商");
                }
                break;
        }

        headerRecyclerView.setVisibility(View.GONE);
        recycler_view.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);

        employerDemandDetailsBtn.setOnClickListener(this);
    }

    @Override
    public void onShowApplyForRefundReasonList(int userId, int demandId, List<ApplyForRefundReasonBean> applyForRefundReasonList) {
        if (null != applyForRefundReasonList && applyForRefundReasonList.size() > 0) {
            showSelectApplyForRefundReasonDialog(userId, demandId, mOrderId, applyForRefundReasonList);
        } else {
            showToast("没有申请退款理由");
        }
    }

    @Override
    public void onApplyForRefundSuccess(int userId, int demandId) {
        if (null != selectApplyForRefundReasonDialog && selectApplyForRefundReasonDialog.isShowing()) {
            selectApplyForRefundReasonDialog.dismiss();
        }

        showToast("退款成功");
        sendRefundedMessage(userId, demandId);

        if (demandStatus == CommonConstant.DEMAND_STATUS_UNDERWAY) {
            if (type == CommonConstant.LOOK_OVER_WIN_THE_BIDDING) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_UNDERWAY, demandId);
            } else if (type == CommonConstant.SELECT_MORE_BIDDING) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING, demandId);
            }
        } else if (demandStatus == CommonConstant.DEMAND_STATUS_COMPLETED) {
            if (type == CommonConstant.LOOK_OVER_COMPLETE) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_COMPLETED, demandId);
            } else if (type == CommonConstant.LOOK_OVER_BIDDER) {
                mPresenter.refresh(CommonConstant.DEMAND_STATUS_EXPIRED, demandId);
            }
        } else {
            mPresenter.refresh(demandStatus, demandId);
        }
    }

    @Override
    public void onGoOnlineDemandSuccess() {
        showToast("恭喜，你的需求已上线。");
        rightFirstTv.setText(getString(R.string.offline));
        rightFirstTv.setOnClickListener(this);
    }

    @Override
    public void onGoOnlineDemandFail() {
        showToast("上线失败");
        rightFirstTv.setOnClickListener(this);
    }

    @Override
    public void onGoOfflineDemandSuccess() {
        rightFirstTv.setText(getString(R.string.online));
        rightFirstTv.setOnClickListener(this);
    }

    @Override
    public void onGoOfflineDemandFail() {
        showToast("下线失败");
        rightFirstTv.setOnClickListener(this);
    }

    @Override
    public void onDeleteDemandSuccess() {
        showToast("删除成功");

        Intent intent = new Intent(getParentContext(), MainActivity.class);
        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.DEMAND_MANAGEMENT_FRAGMENT);
        intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, demandStatus);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (isPaid) {
            Intent intent = new Intent(getParentContext(), MainActivity.class);
            intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.DEMAND_MANAGEMENT_FRAGMENT);
            intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_STATUS_UNDERWAY);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.REFRESH_RESULT_CODE) {
//            if (demandStatus == CommonConstant.DEMAND_STATUS_UNDERWAY) {
//                if (type == CommonConstant.LOOK_OVER_WIN_THE_BIDDING) {
//                    mPresenter.refresh(CommonConstant.DEMAND_STATUS_UNDERWAY, demandId);
//                } else if (type == CommonConstant.SELECT_MORE_BIDDING) {
//                    mPresenter.refresh(CommonConstant.DEMAND_STATUS_IN_THE_BIDDING, demandId);
//                }
//            } else if (demandStatus == CommonConstant.DEMAND_STATUS_COMPLETED) {
//                if (type == CommonConstant.LOOK_OVER_COMPLETE) {
//                    mPresenter.refresh(CommonConstant.DEMAND_STATUS_COMPLETED, demandId);
//                } else if (type == CommonConstant.LOOK_OVER_BIDDER) {
//                    mPresenter.refresh(CommonConstant.DEMAND_STATUS_EXPIRED, demandId);
//                }
//            } else {
//                mPresenter.refresh(demandStatus, demandId);
//            }

            initData();
        }

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.RESULT_CODE) {
            finish();
        }

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.PAID_RESULT_CODE) {
            isPaid = true;
            type = CommonConstant.LOOK_OVER_WIN_THE_BIDDING;
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != employerDemandDetailsActivityBroadcastReceiver) {
            AllintaskApplication.getInstance().getLocalBroadcastManager().unregisterReceiver(employerDemandDetailsActivityBroadcastReceiver);
        }

        if (null != funnelObjectAnimator) {
            funnelObjectAnimator.cancel();
            funnelObjectAnimator = null;
        }

        super.onDestroy();
    }

}
