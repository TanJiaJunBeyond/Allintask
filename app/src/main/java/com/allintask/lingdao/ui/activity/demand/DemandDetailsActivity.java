package com.allintask.lingdao.ui.activity.demand;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.demand.DemandDetailsPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.main.PublishServiceActivity;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.activity.user.ReportActivity;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.demand.IDemandDetailsView;
import com.allintask.lingdao.widget.BidToMakeMoneyAtOnceDialog;
import com.allintask.lingdao.widget.CircleImageView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.component.util.DialogUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/5/3.
 */

public class DemandDetailsActivity extends BaseActivity<IDemandDetailsView, DemandDetailsPresenter> implements SwipeRefreshLayout.OnRefreshListener, IDemandDetailsView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.iv_right_first)
    ImageView rightFirstIv;
    @BindView(R.id.iv_right_second)
    ImageView rightSecondIv;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_category_name)
    TextView categoryNameTv;
    @BindView(R.id.tv_demand_status)
    TextView demandStatusTv;
    @BindView(R.id.ll_has_bid)
    LinearLayout hasBidLL;
    @BindView(R.id.tv_has_bid)
    TextView hasBidTv;
    @BindView(R.id.ll_has_pay)
    LinearLayout hasPayLL;
    @BindView(R.id.tv_has_pay)
    TextView hasPayTv;
    @BindView(R.id.ll_has_browse)
    LinearLayout hasBrowseLL;
    @BindView(R.id.tv_has_browse)
    TextView hasBrowseTv;
    @BindView(R.id.tv_publish_time)
    TextView publishTimeTv;
    @BindView(R.id.tv_expired_time)
    TextView expiredTimeTv;
    @BindView(R.id.tv_service_way)
    TextView serviceWayTv;
    @BindView(R.id.tv_budget)
    TextView budgetTv;
    @BindView(R.id.iv_trusteeship)
    ImageView trusteeshipIv;
    @BindView(R.id.civ_head_portrait)
    CircleImageView headPortraitCIV;
    @BindView(R.id.tv_name)
    TextView nameTv;
    @BindView(R.id.tv_time)
    TextView timeTv;
    @BindView(R.id.ll_demand_description)
    LinearLayout demandDescriptionLL;
    @BindView(R.id.tv_demand_description)
    TextView demandDescriptionTv;
    @BindView(R.id.ll_service_site)
    LinearLayout serviceSiteLL;
    @BindView(R.id.tv_service_site)
    TextView serviceSiteTv;
    @BindView(R.id.ll_subscribe_start_time)
    LinearLayout subscribeStartTimeLL;
    @BindView(R.id.tv_subscribe_start_time)
    TextView subscribeStartTimeTv;
    @BindView(R.id.ll_delivery_cycle)
    LinearLayout deliveryCycleLL;
    @BindView(R.id.tv_delivery_cycle)
    TextView deliveryCycleTv;
    @BindView(R.id.ll_demand_introduction)
    LinearLayout demandIntroductionLL;
    @BindView(R.id.tv_demand_introduction)
    TextView demandIntroductionTv;
    @BindView(R.id.btn_status)
    Button statusBtn;

    private int demandId = -1;

    private int myUserId = -1;
    private int userId = -1;
    private boolean isCollected = false;
    private String mCategoryName;
    private int demandStatus = CommonConstant.DEMAND_STATUS_IN_THE_BIDDING;
    private int mBudget;
    private String advantage;
    private int isBook = 0;
    private boolean mIsBidServe;
    private BidToMakeMoneyAtOnceDialog bidToMakeMoneyAtOnceDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_demand_details;
    }

    @Override
    protected DemandDetailsPresenter CreatePresenter() {
        return new DemandDetailsPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            demandId = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_ID, -1);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.demand_details));
        rightSecondIv.setImageResource(R.mipmap.ic_more);
        rightSecondIv.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        if (null != mSwipeRefreshStatusLayout) {
            View emptyView = mSwipeRefreshStatusLayout.getEmptyView();

            TextView contentTv = emptyView.findViewById(R.id.tv_content);
            contentTv.setText(getString(R.string.demand_details_no_data));

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

        initSwipeRefreshLayout();
    }

    private void initData() {
        myUserId = UserPreferences.getInstance().getUserId();

        if (demandId != -1) {
            mPresenter.fetchRecommendDemandDetailsRequest(demandId);
        }
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_orange);
        swipeRefreshLayout.setOnRefreshListener(this);
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

                mPresenter.bidRequest(userId, demandId, myOffer, myAdvantage, isBook);
            }
        });
    }

    private void showSharePanel() {
        ShareAction shareAction = new ShareAction(DemandDetailsActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN).addButton("report", "report", "ic_report", "ic_report").setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (null == share_media) {
                    if (snsPlatform.mKeyword.equals("report")) {
                        if (userId == myUserId) {
                            showToast("不能举报自己");
                        } else {
                            Intent intent = new Intent(getParentContext(), ReportActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                            startActivity(intent);
                        }
                    }
                } else if (share_media == SHARE_MEDIA.WEIXIN) {
                    UMImage umImage = new UMImage(getParentContext(), "https://www.allintask.com/static/mobile/img/activity/local/bidMoney.png");

                    UMMin umMin = new UMMin("http://sj.qq.com/myapp/detail.htm?apkName=com.allintask.lingdao");
                    umMin.setUserName(CommonConstant.WECHAT_MINI_APPS_USERNAME);
                    umMin.setPath(CommonConstant.WECHAT_MINI_APPS_DEMAND_PATH.replace("{userId}", String.valueOf(userId)).replace("{demandId}", String.valueOf(demandId)));
                    umMin.setTitle(getString(R.string.wechat_mini_apps_demand_description).replace("{category}", mCategoryName).replace("{price}", String.valueOf(mBudget)));
                    umMin.setDescription(getString(R.string.wechat_mini_apps_demand_description).replace("{category}", mCategoryName).replace("{price}", String.valueOf(mBudget)));
                    umMin.setThumb(umImage);
                    new ShareAction(DemandDetailsActivity.this).withMedia(umMin).setPlatform(share_media).share();
                }
            }
        });

        ShareBoardConfig shareBoardConfig = new ShareBoardConfig();
        shareBoardConfig.setCancelButtonText(getString(R.string.cancel));
        shareBoardConfig.setTitleVisibility(false);
        shareAction.open(shareBoardConfig);
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

    @OnClick({R.id.iv_right_first, R.id.iv_right_second, R.id.btn_status})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right_first:
                if (userId == myUserId) {
                    showToast("不能收藏自己");
                } else {
                    if (!isCollected) {
                        mPresenter.collectDemandRequest(demandId);

                    } else {
                        mPresenter.cancelCollectDemandRequest(demandId);
                    }
                }
                break;

            case R.id.iv_right_second:
                showSharePanel();
                break;

            case R.id.btn_status:
                String status = statusBtn.getText().toString().trim();

                if (status.equals(getString(R.string.manage_demand))) {
                    if (demandId != -1) {
                        Intent intent = new Intent(getParentContext(), EmployerDemandDetailsActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                        startActivity(intent);
                    }
                } else if (status.equals(getString(R.string.bid_to_make_money))) {
                    if (mIsBidServe) {
                        showBidToMakeMoneyAtOnceDialog(advantage);
                    } else {
                        DialogUtils.showAlertDialog(getParentContext(), "你还没有发布服务", "发布对应的服务，才能投标赚钱哦！发布完服务记得回来投标。", getString(R.string.go_to_publish), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                Intent intent = new Intent(getParentContext(), PublishServiceActivity.class);
                                intent.putExtra(CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE, CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD);
                                startActivity(intent);
                            }
                        }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                } else if (status.equals(getString(R.string.has_bid_and_chat))) {
                    if (userId != -1) {
                        Intent intent = new Intent(getParentContext(), ChatActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(userId));
                        startActivity(intent);
                    }
                } else if (status.equals(getString(R.string.completed_and_find_more_demand))) {
                    Intent intent = new Intent(getParentContext(), MainActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.RECOMMEND_FRAGMENT);
                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_LOBBY);
                    startActivity(intent);
                } else if (status.equals(getString(R.string.expired_and_find_more_demand))) {
                    Intent intent = new Intent(getParentContext(), MainActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_NAME, CommonConstant.RECOMMEND_FRAGMENT);
                    intent.putExtra(CommonConstant.EXTRA_FRAGMENT_STATUS, CommonConstant.DEMAND_LOBBY);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (demandId != -1) {
            mPresenter.fetchRecommendDemandDetailsRequest(demandId);
        }
    }

    @Override
    public void setRefresh(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
    }

    @Override
    public void onShowUserId(int userId) {
        this.userId = userId;

        if (this.userId != -1) {
            mPresenter.fetchRecommendDemandDetailsUserInformationRequest(this.userId);
        }
    }

    @Override
    public void onShowCategoryId(int categoryId) {
        mPresenter.fetchBidInformationRequest(categoryId);
    }

    @Override
    public void onShowIsCollected(boolean isCollected) {
        this.isCollected = isCollected;

        if (!this.isCollected) {
            rightFirstIv.setImageResource(R.mipmap.ic_not_collect);
        } else {
            rightFirstIv.setImageResource(R.mipmap.ic_collected);
        }

        rightFirstIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowCategoryName(String categoryName) {
        mCategoryName = categoryName;

        if (!TextUtils.isEmpty(mCategoryName)) {
            categoryNameTv.setText(mCategoryName);
            categoryNameTv.setVisibility(View.VISIBLE);
        } else {
            categoryNameTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowDemandStatus(int demandStatus) {
        switch (demandStatus) {
            case CommonConstant.DEMAND_DETAILS_STATUS_IN_THE_BIDDING:
                this.demandStatus = CommonConstant.DEMAND_STATUS_IN_THE_BIDDING;
                demandStatusTv.setText(getString(R.string.demand_status_in_the_bidding));
                break;

            case CommonConstant.DEMAND_DETAILS_STATUS_UNDERWAY:
                this.demandStatus = CommonConstant.DEMAND_STATUS_UNDERWAY;
                demandStatusTv.setText(getString(R.string.demand_status_underway));
                break;

            case CommonConstant.DEMAND_DETAILS_STATUS_COMPLETED:
                this.demandStatus = CommonConstant.DEMAND_STATUS_COMPLETED;
                demandStatusTv.setText(getString(R.string.demand_status_completed));
                break;

            case CommonConstant.DEMAND_DETAILS_STATUS_EXPIRED:
                this.demandStatus = CommonConstant.DEMAND_STATUS_EXPIRED;
                demandStatusTv.setText(getString(R.string.demand_status_expired));
                break;
        }
    }

    @Override
    public void onShowHasBidAmount(int hasBidAmount) {
        hasBidTv.setText(String.valueOf(hasBidAmount));
        hasBidLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowHasPayAmount(int hasPayAmount) {
        hasPayTv.setText(String.valueOf(hasPayAmount));
        hasPayLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowHasBrowseAmount(int hasBrowseAmount) {
        hasBrowseTv.setText(String.valueOf(hasBrowseAmount));
        hasBrowseLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowPublishTip(String publishTip) {
        if (!TextUtils.isEmpty(publishTip)) {
            publishTimeTv.setText(publishTip);
            publishTimeTv.setVisibility(View.VISIBLE);
        } else {
            publishTimeTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowExpireTip(String expireTip) {
        if (!TextUtils.isEmpty(expireTip)) {
            expiredTimeTv.setText(expireTip);
            expiredTimeTv.setVisibility(View.VISIBLE);
        } else {
            expiredTimeTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowServiceWay(String serviceWay) {
        if (!TextUtils.isEmpty(serviceWay)) {
            serviceWayTv.setText(serviceWay);
            serviceWayTv.setVisibility(View.VISIBLE);
        } else {
            serviceWayTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowBudget(int budget) {
        mBudget = budget;

        if (mBudget != 0) {
            StringBuilder stringBuilder = new StringBuilder("￥").append(String.valueOf(mBudget)).append("预算");
            budgetTv.setText(stringBuilder);
            budgetTv.setVisibility(View.VISIBLE);
        } else {
            budgetTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowIsTrusteeship(int isTrusteeship) {
        if (isTrusteeship == 1) {
            trusteeshipIv.setVisibility(View.VISIBLE);
        } else {
            trusteeshipIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowCategoryPropertyChineseString(String categoryPropertyChineseString) {
        if (!TextUtils.isEmpty(categoryPropertyChineseString)) {
            demandDescriptionTv.setText(categoryPropertyChineseString);
            demandDescriptionLL.setVisibility(View.VISIBLE);
        } else {
            demandDescriptionLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowAddress(String address) {
        if (!TextUtils.isEmpty(address)) {
            serviceSiteTv.setText(address);
            serviceSiteLL.setVisibility(View.VISIBLE);
        } else {
            serviceSiteLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowSubscribeStartTime(String subscribeStartTime) {
        if (!TextUtils.isEmpty(subscribeStartTime)) {
            subscribeStartTimeTv.setText(subscribeStartTime);
            subscribeStartTimeLL.setVisibility(View.VISIBLE);
        } else {
            subscribeStartTimeLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowDeliveryCycle(String deliveryCycle) {
        if (!TextUtils.isEmpty(deliveryCycle)) {
            deliveryCycleTv.setText(deliveryCycle);
            deliveryCycleLL.setVisibility(View.VISIBLE);
        } else {
            deliveryCycleLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowDemandIntroduction(String demandIntroduction) {
        if (!TextUtils.isEmpty(demandIntroduction)) {
            demandIntroductionTv.setText(demandIntroduction);
            demandIntroductionLL.setVisibility(View.VISIBLE);
        } else {
            demandIntroductionLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowIsHasBid(boolean isHasBid) {
        if (myUserId != -1 && this.userId != -1) {
            if (myUserId == this.userId) {
                statusBtn.setText(getString(R.string.manage_demand));
            } else {
                switch (demandStatus) {
                    case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                    case CommonConstant.DEMAND_STATUS_UNDERWAY:
                        if (isHasBid) {
                            statusBtn.setText(getString(R.string.has_bid_and_chat));
                        } else {
                            statusBtn.setText(getString(R.string.bid_to_make_money));
                        }
                        break;

                    case CommonConstant.DEMAND_STATUS_COMPLETED:
                        statusBtn.setText(getString(R.string.completed_and_find_more_demand));
                        break;

                    case CommonConstant.DEMAND_STATUS_EXPIRED:
                        if (isHasBid) {
                            statusBtn.setText(getString(R.string.has_bid_and_chat));
                        } else {
                            statusBtn.setText(getString(R.string.expired_and_find_more_demand));
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void onShowIsBidServe(boolean isBidServe) {
        mIsBidServe = isBidServe;
    }

    @Override
    public void onShowUserHeadPortraitUrl(String userHeadPortraitUrl) {
        ImageViewUtil.setImageView(getParentContext(), headPortraitCIV, userHeadPortraitUrl, R.mipmap.ic_default_avatar, true);
    }

    @Override
    public void onShowName(String name) {
        if (!TextUtils.isEmpty(name)) {
            nameTv.setText(name);
            nameTv.setVisibility(View.VISIBLE);
        } else {
            nameTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowTime(String time) {
        if (!TextUtils.isEmpty(time)) {
            timeTv.setText(time);
            timeTv.setVisibility(View.VISIBLE);
        } else {
            timeTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowAdvantageAndIsBook(String advantage, int isBook) {
        this.advantage = advantage;
        this.isBook = isBook;
    }

    @Override
    public void onBidSuccess(int userId, int demandId) {
        if (null != bidToMakeMoneyAtOnceDialog && bidToMakeMoneyAtOnceDialog.isShowing()) {
            bidToMakeMoneyAtOnceDialog.dismiss();
        }

        showToast("投标赚钱成功");
        sendBidSuccessMessage(userId, demandId);

        Intent intent = new Intent();
        intent.setAction(CommonConstant.ACTION_SERVICE_STATUS);
        intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_HAS_BID);
        getParentContext().sendBroadcast(intent);

        Intent chatIntent = new Intent(getParentContext(), ChatActivity.class);
        chatIntent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(userId));
        startActivity(chatIntent);

        finish();
    }

    @Override
    public void onCollectDemandSuccess() {
        isCollected = true;
        rightFirstIv.setImageResource(R.mipmap.ic_collected);
    }

    @Override
    public void onCancelCollectDemandSuccess() {
        isCollected = false;
        rightFirstIv.setImageResource(R.mipmap.ic_not_collect);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

}
