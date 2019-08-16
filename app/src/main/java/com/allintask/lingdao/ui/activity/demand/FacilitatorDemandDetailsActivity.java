package com.allintask.lingdao.ui.activity.demand;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.ServiceCompletedListBean;
import com.allintask.lingdao.bean.service.ServiceWaitBidListBean;
import com.allintask.lingdao.bean.service.ShowFacilitatorDemandDetailsBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.demand.FacilitatorDemandDetailsPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.activity.user.EvaluateActivity;
import com.allintask.lingdao.ui.activity.user.EvaluateDetailsActivity;
import com.allintask.lingdao.ui.adapter.demand.IFacilitatorDemandDetailsView;
import com.allintask.lingdao.ui.adapter.service.FacilitatorDemandDetailsAdapter;
import com.allintask.lingdao.utils.AutoRefundDateFormatUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.widget.BidToMakeMoneyAtOnceDialog;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.allintask.lingdao.widget.ModifyServicePriceDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/17.
 */

public class FacilitatorDemandDetailsActivity extends BaseActivity<IFacilitatorDemandDetailsView, FacilitatorDemandDetailsPresenter> implements IFacilitatorDemandDetailsView {

    private static final int MESSAGE_CODE_AUTO_REFUND_DATE = 100;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;
    @BindView(R.id.ll_first)
    LinearLayout firstLL;
    @BindView(R.id.tv_first)
    TextView firstTv;
    @BindView(R.id.ll_second)
    LinearLayout secondLL;
    @BindView(R.id.tv_second)
    TextView secondTv;

    private String userHeadPortraitUrl;
    private String name;
    private int gender;
    private int age = -1;
    private String time;
    private int isBook;
    private int serviceStatus;
    private int userId = -1;
    private int demandId = -1;
    private int orderId = -1;
    private String firstContent;
    private String secondContent;
    private ServiceWaitBidListBean.ServiceWaitBidBean serviceWaitBidBean;
    private int bidId = -1;
    private String originalPrice;
    private ServiceCompletedListBean.ServiceCompletedBean serviceCompletedBean;
    private String advantage;

    private FacilitatorDemandDetailsAdapter facilitatorDemandDetailsAdapter;

    private long autoRefundDate;
    private Timer timer;
    private TimerTask timerTask;

    private BidToMakeMoneyAtOnceDialog bidToMakeMoneyAtOnceDialog;
    private ModifyServicePriceDialog modifyServicePriceDialog;

    private boolean isRefreshServiceStatusUnderway = false;

    private final FacilitatorDemandDetailsHandler facilitatorDemandDetailsHandler = new FacilitatorDemandDetailsHandler(this);

    private static class FacilitatorDemandDetailsHandler extends Handler {

        private final WeakReference<Activity> activityWeakReference;

        public FacilitatorDemandDetailsHandler(Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FacilitatorDemandDetailsActivity facilitatorDemandDetailsActivity = (FacilitatorDemandDetailsActivity) activityWeakReference.get();

            if (null != facilitatorDemandDetailsActivity) {
                if (msg.what == MESSAGE_CODE_AUTO_REFUND_DATE) {
                    facilitatorDemandDetailsActivity.autoRefundDate = facilitatorDemandDetailsActivity.autoRefundDate - 1000;
                    String autoRefundDateString = AutoRefundDateFormatUtils.format(facilitatorDemandDetailsActivity.autoRefundDate);

                    if (null != facilitatorDemandDetailsActivity.facilitatorDemandDetailsAdapter) {
                        if (facilitatorDemandDetailsActivity.autoRefundDate == 0L) {
                            facilitatorDemandDetailsActivity.facilitatorDemandDetailsAdapter.setAutoRefundDateString(null);
                        } else {
                            facilitatorDemandDetailsActivity.facilitatorDemandDetailsAdapter.setAutoRefundDateString(autoRefundDateString);
                        }
                    }
                }
            }
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_facilitator_demand_details;
    }

    @Override
    protected FacilitatorDemandDetailsPresenter CreatePresenter() {
        return new FacilitatorDemandDetailsPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            userHeadPortraitUrl = intent.getStringExtra(CommonConstant.EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL);
            name = intent.getStringExtra(CommonConstant.EXTRA_NAME);
            gender = intent.getIntExtra(CommonConstant.EXTRA_GENDER, -1);
            age = intent.getIntExtra(CommonConstant.EXTRA_AGE, -1);
            time = intent.getStringExtra(CommonConstant.EXTRA_TIME);
            isBook = intent.getIntExtra(CommonConstant.EXTRA_IS_BOOK, -1);
            serviceStatus = intent.getIntExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_WAIT_BID);
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
            demandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
            orderId = intent.getIntExtra(CommonConstant.EXTRA_ORDER_ID, -1);
            firstContent = intent.getStringExtra(CommonConstant.EXTRA_FIRST_CONTENT);
            secondContent = intent.getStringExtra(CommonConstant.EXTRA_SECOND_CONTENT);
            serviceWaitBidBean = (ServiceWaitBidListBean.ServiceWaitBidBean) intent.getSerializableExtra(CommonConstant.EXTRA_SERVICE_WAIT_BID_BEAN);
            bidId = intent.getIntExtra(CommonConstant.EXTRA_BID_ID, -1);
            originalPrice = intent.getStringExtra(CommonConstant.EXTRA_ORIGINAL_PRICE);
            serviceCompletedBean = (ServiceCompletedListBean.ServiceCompletedBean) intent.getSerializableExtra(CommonConstant.EXTRA_SERVICE_COMPLETED_BEAN);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRefreshServiceStatusUnderway) {
                    Intent intent = new Intent(getParentContext(), MainActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.SERVICE_MANAGEMENT_FRAGMENT);
                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
                    startActivity(intent);
                }

                finish();
            }
        });

        toolbar.setTitle("");

        titleTv.setText(getString(R.string.demand_details));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        facilitatorDemandDetailsAdapter = new FacilitatorDemandDetailsAdapter(getParentContext());
        recyclerView.setAdapter(facilitatorDemandDetailsAdapter);

        if (!TextUtils.isEmpty(userHeadPortraitUrl)) {
            userHeadPortraitUrl = "https:" + userHeadPortraitUrl;
        }

        facilitatorDemandDetailsAdapter.setFacilitatorDemandDetailsPersonalInformation(userHeadPortraitUrl, name, gender, age, time, isBook);
        facilitatorDemandDetailsAdapter.setOnClickListener(new FacilitatorDemandDetailsAdapter.OnClickListener() {
            @Override
            public void onAgreeRefundClick(int orderId) {
                if (orderId != -1) {
                    mPresenter.acceptApplyRefundRequest(orderId);
                }
            }

            @Override
            public void onRejectRefundClick(int orderId) {
                if (orderId != -1) {
                    mPresenter.refuseApplyRefundRequest(orderId);
                }
            }
        });

        if (!TextUtils.isEmpty(firstContent)) {
            firstTv.setText(firstContent);
            firstLL.setVisibility(View.VISIBLE);
        } else {
            firstLL.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(secondContent)) {
            secondTv.setText(secondContent);
            secondLL.setVisibility(View.VISIBLE);
        } else {
            secondLL.setVisibility(View.GONE);
        }
    }

    private void initData() {
        if (demandId != -1) {
            mPresenter.fetchFacilitatorDemandDetailsRequest(demandId);
        }
    }

    private void setAutoRefundDateTimer(long autoRefundDate) {
        this.autoRefundDate = autoRefundDate;

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = facilitatorDemandDetailsHandler.obtainMessage();
                message.what = MESSAGE_CODE_AUTO_REFUND_DATE;
                facilitatorDemandDetailsHandler.sendMessage(message);
            }
        };

        timer.schedule(timerTask, 1000, 1000);
    }

    private void showBidToMakeMoneyAtOnceDialog(String advantage) {
        bidToMakeMoneyAtOnceDialog = new BidToMakeMoneyAtOnceDialog(getParentContext(), advantage);

        Window window = bidToMakeMoneyAtOnceDialog.getWindow();

        if (null != window) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
        }

        bidToMakeMoneyAtOnceDialog.show();
        bidToMakeMoneyAtOnceDialog.setOnClickListener(new BidToMakeMoneyAtOnceDialog.OnClickListener() {
            @Override
            public void onCloseClick() {
                if (null != bidToMakeMoneyAtOnceDialog && bidToMakeMoneyAtOnceDialog.isShowing()) {
                    bidToMakeMoneyAtOnceDialog.dismiss();
                }
            }

            @Override
            public void onBidAtOnceClick(String myOffer, String myAdvantage) {
                if (null != bidToMakeMoneyAtOnceDialog && bidToMakeMoneyAtOnceDialog.isShowing()) {
                    bidToMakeMoneyAtOnceDialog.dismiss();
                }

                if (null != serviceWaitBidBean) {
                    int buyerUserId = TypeUtils.getInteger(serviceWaitBidBean.buyerUserId, -1);
                    int serveId = TypeUtils.getInteger(serviceWaitBidBean.serveId, -1);
                    int demandId = TypeUtils.getInteger(serviceWaitBidBean.demandId, -1);
                    int isBook = TypeUtils.getInteger(serviceWaitBidBean.isBook, -1);

                    mPresenter.bidRequest(demandId, myOffer, myAdvantage, isBook);
                }
            }
        });
    }

    private void showModifyServicePriceDialog(final int bidId, String originalPrice) {
        modifyServicePriceDialog = new ModifyServicePriceDialog(getParentContext(), originalPrice);

        Window window = modifyServicePriceDialog.getWindow();

        if (null != window) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
        }

        modifyServicePriceDialog.show();
        modifyServicePriceDialog.setOnClickListener(new ModifyServicePriceDialog.OnClickListener() {
            @Override
            public void onConfirmModifyClick(String modifyPrice) {
                if (!TextUtils.isEmpty(modifyPrice)) {
                    mPresenter.changePriceRequest(bidId, modifyPrice);
                } else {
                    showToast("修改的价格不能为空");
                }
            }
        });
    }

    private void sendBidSuccessMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_bid_success_content), String.valueOf(sendUserId));

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
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_BID_SUCCESS);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_bid_success_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_bid_success_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_bid_success_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_select_bidder));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_HAS_BID);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_FACILITATOR);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    private void sendModifyPriceMessage(int sendUserId, int demandId, String price) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_modify_price_content), String.valueOf(sendUserId));

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
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_MODIFY_PRICE);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_modify_price_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_modify_price_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_modify_price_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_select_bidder));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_HAS_BID);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CHANGED_PRICE, price);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    private void sendAgreeRefundMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_agree_refund_content), String.valueOf(sendUserId));

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
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_AGREE_REFUND);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_agree_refund_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_agree_refund_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_agree_refund_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_COMPLETED);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_FACILITATOR);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    private void sendRejectRefundMessage(int sendUserId, int demandId) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage emMessage = EMMessage.createTxtSendMessage(getString(R.string.message_sender_reject_refund_content), String.valueOf(sendUserId));

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
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE, CommonConstant.MESSAGE_STATUS_REJECT_REFUND);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, String.valueOf(demandId));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE, getString(R.string.message_reject_refund_title));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER, getString(R.string.message_sender_reject_refund_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE, getString(R.string.message_receiver_reject_refund_content));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE, getString(R.string.message_details));
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_FACILITATOR);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @OnClick({R.id.ll_first, R.id.ll_second})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_first:
                if (firstContent.equals(getString(R.string.chat))) {
                    if (userId != -1) {
                        Intent intent = new Intent(getParentContext(), ChatActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(userId));
                        startActivity(intent);
                    }
                }
                break;

            case R.id.ll_second:
                if (secondContent.equals(getString(R.string.bid_to_make_money))) {
                    showBidToMakeMoneyAtOnceDialog(advantage);
                } else if (secondContent.equals(getString(R.string.modify_price))) {
                    showModifyServicePriceDialog(bidId, originalPrice);
                } else if (secondContent.equals(getString(R.string.evaluate))) {
                    if (null != serviceCompletedBean) {
                        int orderId = TypeUtils.getInteger(serviceCompletedBean.orderId, -1);
                        int serviceId = TypeUtils.getInteger(serviceCompletedBean.serveId, -1);
                        int buyerUserId = TypeUtils.getInteger(serviceCompletedBean.buyerUserId, -1);
                        int sellerUserId = TypeUtils.getInteger(serviceCompletedBean.sellerUserId, -1);

                        Intent intent = new Intent(getParentContext(), EvaluateActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_EVALUATE_TYPE, CommonConstant.EVALUATE_TYPE_EMPLOYER);
                        intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                        intent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                        intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                        intent.putExtra(CommonConstant.EXTRA_BUYER_USER_ID, buyerUserId);
                        intent.putExtra(CommonConstant.EXTRA_SELLER_USER_ID, sellerUserId);
                        startActivityForResult(intent, CommonConstant.EVALUATE_REQUEST_CODE);
                    }
                } else if (secondContent.equals(getString(R.string.see_evaluate))) {
                    Intent intent = new Intent(getParentContext(), EvaluateDetailsActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_EVALUATE_DETAILS_TYPE, CommonConstant.EVALUATE_DETAILS_TYPE_EMPLOYER);
                    intent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onShowFacilitatorDemandDetailsOrderInformation(int orderId, int orderPrice, String priceTip, String refundTip, String refundReason, Date refundCreateAt, int refundStatus, long refundAutoAcceptDuration, String arbitrationTip) {
        if (null != facilitatorDemandDetailsAdapter) {
            facilitatorDemandDetailsAdapter.setFacilitatorDemandDetailsOrderInformation(orderId, orderPrice, priceTip, refundTip, refundReason, refundCreateAt, refundStatus, arbitrationTip);

            if (refundAutoAcceptDuration > 0L) {
                setAutoRefundDateTimer(refundAutoAcceptDuration);
            } else {
                facilitatorDemandDetailsAdapter.setAutoRefundDateString(null);

                if (null != timer) {
                    timer.cancel();
                }

                if (null != timerTask) {
                    timerTask.cancel();
                }
            }
        }
    }

    @Override
    public void onShowCategoryId(int categoryId) {
        mPresenter.fetchBidInformationRequest(categoryId);
    }

    @Override
    public void onShowShowFacilitatorDemandDetailsList(List<ShowFacilitatorDemandDetailsBean> showFacilitatorDemandDetailsList) {
        if (null != facilitatorDemandDetailsAdapter) {
            facilitatorDemandDetailsAdapter.setDateList(showFacilitatorDemandDetailsList);

            if (orderId != -1 && serviceStatus == CommonConstant.SERVICE_STATUS_UNDERWAY | serviceStatus == CommonConstant.SERVICE_STATUS_COMPLETED) {
                mPresenter.fetchFacilitatorDemandOrderInformationRequest(orderId);
            }
        }
    }

    @Override
    public void onShowAdvantage(String advantage) {
        this.advantage = advantage;
    }

    @Override
    public void onBidSuccess() {
        secondContent = getString(R.string.modify_price);
        secondTv.setText(secondContent);

        showToast("投标赚钱成功");
        sendBidSuccessMessage(userId, demandId);

        if (null != bidToMakeMoneyAtOnceDialog && bidToMakeMoneyAtOnceDialog.isShowing()) {
            bidToMakeMoneyAtOnceDialog.dismiss();
        }

        if (demandId != -1) {
            mPresenter.fetchFacilitatorDemandDetailsRequest(demandId);
        }

        Intent intent = new Intent();
        intent.setAction(CommonConstant.ACTION_SERVICE_STATUS);
        intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_HAS_BID);
        getParentContext().sendBroadcast(intent);
    }

    @Override
    public void onChangePriceSuccess(String price) {
        showToast("修改价格成功");
        sendModifyPriceMessage(userId, demandId, price);

        if (null != modifyServicePriceDialog && modifyServicePriceDialog.isShowing()) {
            modifyServicePriceDialog.dismiss();
        }

        if (demandId != -1) {
            mPresenter.fetchFacilitatorDemandDetailsRequest(demandId);
        }

        Intent intent = new Intent();
        intent.setAction(CommonConstant.ACTION_SERVICE_STATUS);
        intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_HAS_BID);
        getParentContext().sendBroadcast(intent);
    }

    @Override
    public void onAgreeRefundSuccess() {
        isRefreshServiceStatusUnderway = true;
        showToast("同意退款成功");
        sendAgreeRefundMessage(userId, demandId);

        if (demandId != -1) {
            mPresenter.fetchFacilitatorDemandDetailsRequest(demandId);
        }
    }

    @Override
    public void onRejectRefundSuccess() {
        isRefreshServiceStatusUnderway = true;
        showToast("驳回退款成功");
        sendRejectRefundMessage(userId, demandId);

        if (demandId != -1) {
            mPresenter.fetchFacilitatorDemandDetailsRequest(demandId);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.EVALUATE_REQUEST_CODE && resultCode == CommonConstant.REFRESH_RESULT_CODE) {
            secondContent = getString(R.string.see_evaluate);
            secondTv.setText(secondContent);

            if (demandId != -1) {
                mPresenter.fetchFacilitatorDemandDetailsRequest(demandId);
            }

            Intent intent = new Intent();
            intent.setAction(CommonConstant.ACTION_SERVICE_STATUS);
            intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_COMPLETED);
            getParentContext().sendBroadcast(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (isRefreshServiceStatusUnderway) {
            Intent intent = new Intent(getParentContext(), MainActivity.class);
            intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.SERVICE_MANAGEMENT_FRAGMENT);
            intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != timer) {
            timer.cancel();
        }

        if (null != timerTask) {
            timerTask.cancel();
        }

        super.onDestroy();
    }

}
