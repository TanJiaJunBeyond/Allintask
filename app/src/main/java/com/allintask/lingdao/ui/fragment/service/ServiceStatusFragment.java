package com.allintask.lingdao.ui.fragment.service;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.ServiceCompletedListBean;
import com.allintask.lingdao.bean.service.ServiceExpiredListBean;
import com.allintask.lingdao.bean.service.ServiceHasBidListBean;
import com.allintask.lingdao.bean.service.ServiceUnderwayListBean;
import com.allintask.lingdao.bean.service.ServiceWaitBidListBean;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.service.ServiceStatusPresenter;
import com.allintask.lingdao.ui.activity.main.PublishServiceActivity;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.activity.demand.FacilitatorDemandDetailsActivity;
import com.allintask.lingdao.ui.activity.service.UploadAlbumActivity;
import com.allintask.lingdao.ui.activity.user.CompletePersonalInformationActivity;
import com.allintask.lingdao.ui.activity.user.EvaluateActivity;
import com.allintask.lingdao.ui.activity.user.EvaluateDetailsActivity;
import com.allintask.lingdao.ui.activity.user.IdentifyAuthenticationActivity;
import com.allintask.lingdao.ui.activity.user.PersonalInformationActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.adapter.service.ServiceStatusCompletedAdapter;
import com.allintask.lingdao.ui.adapter.service.ServiceStatusExpiredAdapter;
import com.allintask.lingdao.ui.adapter.service.ServiceStatusHasBidAdapter;
import com.allintask.lingdao.ui.adapter.service.ServiceStatusUnderwayAdapter;
import com.allintask.lingdao.ui.adapter.service.ServiceStatusWaitBidAdapter;
import com.allintask.lingdao.ui.fragment.BaseSwipeRefreshFragment;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.service.IServiceStatusView;
import com.allintask.lingdao.widget.BidToMakeMoneyAtOnceDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.AgeUtils;
import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public class ServiceStatusFragment extends BaseSwipeRefreshFragment<IServiceStatusView, ServiceStatusPresenter> implements IServiceStatusView {

    private int serviceStatus = 0;

    private ServiceStatusWaitBidAdapter serviceStatusWaitBidAdapter;
    private ServiceStatusHasBidAdapter serviceStatusHasBidAdapter;
    private ServiceStatusUnderwayAdapter serviceStatusUnderwayAdapter;
    private ServiceStatusCompletedAdapter serviceStatusCompletedAdapter;
    private ServiceStatusExpiredAdapter serviceStatusExpiredAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_service_status;
    }

    @Override
    protected ServiceStatusPresenter CreatePresenter() {
        Bundle bundle = getArguments();

        if (null != bundle) {
            serviceStatus = bundle.getInt(CommonConstant.EXTRA_SERVICE_STATUS, 0);
        }
        return new ServiceStatusPresenter(serviceStatus);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        initUI();
        initData();
    }

    private void initUI() {
        if (null != mSwipeRefreshStatusLayout) {
            View emptyView = mSwipeRefreshStatusLayout.getEmptyView();

            TextView contentTv = emptyView.findViewById(R.id.tv_content);
            Button contentBtn = emptyView.findViewById(R.id.btn_content);
            RelativeLayout serviceManagementRL = emptyView.findViewById(R.id.rl_service_management);
            Button publishServiceBtn = emptyView.findViewById(R.id.btn_publish_service);
            Button completeResumeBtn = emptyView.findViewById(R.id.btn_complete_resume);
            Button addPhotosBtn = emptyView.findViewById(R.id.btn_add_photos);
            Button identifyVerificationBtn = emptyView.findViewById(R.id.btn_identity_verification);

            serviceManagementRL.setVisibility(View.VISIBLE);

            contentTv.setText(getString(R.string.service_status_no_data));

//            if (serviceStatus == CommonConstant.SERVICE_STATUS_WAIT_BID) {
//                contentBtn.setText(getString(R.string.publish_at_once));
//                contentBtn.setVisibility(View.VISIBLE);
//                contentBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mPresenter.checkBasicPersonalInformationWholeRequest();
//                    }
//                });
//            }

            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadingView();
                    mPresenter.refresh();
                }
            });

            publishServiceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getParentContext(), PublishServiceActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE, CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD);
                    startActivity(intent);
                }
            });

            completeResumeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent personalInformationIntent = new Intent(getParentContext(), PersonalInformationActivity.class);
                    startActivity(personalInformationIntent);
                }
            });

            addPhotosBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getParentContext(), UploadAlbumActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_UPLOAD_ALBUM_TYPE, CommonConstant.UPLOAD_ALBUM_PERSONAL);
                    startActivity(intent);
                }
            });

            identifyVerificationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.fetchMyDataRequest();
                }
            });

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

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getParentContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getParentContext(), R.drawable.shape_common_recycler_view_divider));
        recycler_view.addItemDecoration(dividerItemDecoration);

        switch (serviceStatus) {
            case CommonConstant.SERVICE_STATUS_WAIT_BID:
                serviceStatusWaitBidAdapter = new ServiceStatusWaitBidAdapter(getParentContext());
                recycler_view.setAdapter(serviceStatusWaitBidAdapter);

                serviceStatusWaitBidAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ServiceWaitBidListBean.ServiceWaitBidBean serviceWaitBidBean = (ServiceWaitBidListBean.ServiceWaitBidBean) serviceStatusWaitBidAdapter.getItem(position);

                        if (null != serviceWaitBidBean) {
                            String buyerAvatarUrl = TypeUtils.getString(serviceWaitBidBean.buyerAvatarUrl, "");
                            String buyerName = TypeUtils.getString(serviceWaitBidBean.buyerName, "");
                            int gender = TypeUtils.getInteger(serviceWaitBidBean.gender, -1);
                            Date birthday = serviceWaitBidBean.birthday;
                            String loginTimeTip = TypeUtils.getString(serviceWaitBidBean.loginTimeTip, "");
                            int isBook = TypeUtils.getInteger(serviceWaitBidBean.isBook, -1);
                            int buyerUserId = TypeUtils.getInteger(serviceWaitBidBean.buyerUserId, -1);
                            int demandId = TypeUtils.getInteger(serviceWaitBidBean.demandId, -1);

                            int age = 0;

                            if (null != birthday) {
                                age = AgeUtils.getAge(birthday);
                            }

                            Intent intent = new Intent(getParentContext(), FacilitatorDemandDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_WAIT_BID);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL, buyerAvatarUrl);
                            intent.putExtra(CommonConstant.EXTRA_NAME, buyerName);
                            intent.putExtra(CommonConstant.EXTRA_GENDER, gender);
                            intent.putExtra(CommonConstant.EXTRA_AGE, age);
                            intent.putExtra(CommonConstant.EXTRA_TIME, loginTimeTip);
                            intent.putExtra(CommonConstant.EXTRA_IS_BOOK, isBook);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, buyerUserId);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                            intent.putExtra(CommonConstant.EXTRA_FIRST_CONTENT, getString(R.string.chat));
                            intent.putExtra(CommonConstant.EXTRA_SECOND_CONTENT, getString(R.string.bid_to_make_money));
                            intent.putExtra(CommonConstant.EXTRA_SERVICE_WAIT_BID_BEAN, serviceWaitBidBean);
                            startActivity(intent);
                        }
                    }
                });

                serviceStatusWaitBidAdapter.setOnClickListener(new ServiceStatusWaitBidAdapter.OnClickListener() {
                    @Override
                    public void onBidToMakeMoney(int position, int categoryId) {
                        mPresenter.fetchBidInformationRequest(position, categoryId);
                    }

                    @Override
                    public void onBidAtOnceClick(BidToMakeMoneyAtOnceDialog bidToMakeMoneyAtOnceDialog, int buyerUserId, int serveId, int demandId, String myOffer, String myAdvantage, int isBook) {
                        int myAdvantageLength = myAdvantage.length();

                        if (myAdvantageLength >= 20) {
                            if (null != bidToMakeMoneyAtOnceDialog && bidToMakeMoneyAtOnceDialog.isShowing()) {
                                bidToMakeMoneyAtOnceDialog.dismiss();
                            }

                            mPresenter.bidRequest(buyerUserId, demandId, myOffer, myAdvantage, isBook);
                        } else {
                            showToast("我的优势字数不够");
                        }
                    }
                });
                break;

            case CommonConstant.SERVICE_STATUS_HAS_BID:
                serviceStatusHasBidAdapter = new ServiceStatusHasBidAdapter(getParentContext());
                recycler_view.setAdapter(serviceStatusHasBidAdapter);

                serviceStatusHasBidAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ServiceHasBidListBean.ServiceHasBidBean serviceHasBidBean = (ServiceHasBidListBean.ServiceHasBidBean) serviceStatusHasBidAdapter.getItem(position);

                        if (null != serviceHasBidBean) {
                            String avatarUrl = TypeUtils.getString(serviceHasBidBean.avatarUrl, "");
                            String name = TypeUtils.getString(serviceHasBidBean.name, "");
                            int gender = TypeUtils.getInteger(serviceHasBidBean.gender, -1);
                            Date birthday = serviceHasBidBean.birthday;
                            String loginTimeTip = TypeUtils.getString(serviceHasBidBean.loginTimeTip, "");
                            int isBook = TypeUtils.getInteger(serviceHasBidBean.isBook, -1);
                            int buyerUserId = TypeUtils.getInteger(serviceHasBidBean.buyerUserId, -1);
                            int demandId = TypeUtils.getInteger(serviceHasBidBean.demandId, -1);
                            int bidId = TypeUtils.getInteger(serviceHasBidBean.bidId, -1);
                            String price = TypeUtils.getString(serviceHasBidBean.price, "");

                            int age = 0;

                            if (null != birthday) {
                                age = AgeUtils.getAge(birthday);
                            }

                            Intent intent = new Intent(getParentContext(), FacilitatorDemandDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_HAS_BID);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL, avatarUrl);
                            intent.putExtra(CommonConstant.EXTRA_NAME, name);
                            intent.putExtra(CommonConstant.EXTRA_GENDER, gender);
                            intent.putExtra(CommonConstant.EXTRA_AGE, age);
                            intent.putExtra(CommonConstant.EXTRA_TIME, loginTimeTip);
                            intent.putExtra(CommonConstant.EXTRA_IS_BOOK, isBook);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, buyerUserId);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                            intent.putExtra(CommonConstant.EXTRA_FIRST_CONTENT, getString(R.string.chat));
                            intent.putExtra(CommonConstant.EXTRA_SECOND_CONTENT, getString(R.string.modify_price));
                            intent.putExtra(CommonConstant.EXTRA_BID_ID, bidId);
                            intent.putExtra(CommonConstant.EXTRA_ORIGINAL_PRICE, price);
                            startActivity(intent);
                        }
                    }
                });

                serviceStatusHasBidAdapter.setOnClickListener(new ServiceStatusHasBidAdapter.OnClickListener() {
                    @Override
                    public void onClickListener(int userId, int demandId, int bidId, String price) {
                        mPresenter.changePriceRequest(userId, demandId, bidId, price);
                    }
                });
                break;

            case CommonConstant.SERVICE_STATUS_UNDERWAY:
                serviceStatusUnderwayAdapter = new ServiceStatusUnderwayAdapter(getParentContext());
                recycler_view.setAdapter(serviceStatusUnderwayAdapter);

                serviceStatusUnderwayAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ServiceUnderwayListBean.ServiceUnderwayBean serviceUnderwayBean = (ServiceUnderwayListBean.ServiceUnderwayBean) serviceStatusUnderwayAdapter.getItem(position);

                        if (null != serviceUnderwayBean) {
                            String avatarUrl = TypeUtils.getString(serviceUnderwayBean.avatarUrl, "");
                            String name = TypeUtils.getString(serviceUnderwayBean.name, "");
                            int gender = TypeUtils.getInteger(serviceUnderwayBean.gender, -1);
                            Date birthday = serviceUnderwayBean.birthday;
                            String loginTimeTip = TypeUtils.getString(serviceUnderwayBean.loginTimeTip, "");
                            int isBook = TypeUtils.getInteger(serviceUnderwayBean.isBook, -1);
                            int buyerUserId = TypeUtils.getInteger(serviceUnderwayBean.buyerUserId, -1);
                            int demandId = TypeUtils.getInteger(serviceUnderwayBean.demandId, -1);
                            int orderId = TypeUtils.getInteger(serviceUnderwayBean.orderId, -1);

                            int age = 0;

                            if (null != birthday) {
                                age = AgeUtils.getAge(birthday);
                            }

                            Intent intent = new Intent(getParentContext(), FacilitatorDemandDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL, avatarUrl);
                            intent.putExtra(CommonConstant.EXTRA_NAME, name);
                            intent.putExtra(CommonConstant.EXTRA_GENDER, gender);
                            intent.putExtra(CommonConstant.EXTRA_AGE, age);
                            intent.putExtra(CommonConstant.EXTRA_TIME, loginTimeTip);
                            intent.putExtra(CommonConstant.EXTRA_IS_BOOK, isBook);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, buyerUserId);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                            intent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                            intent.putExtra(CommonConstant.EXTRA_FIRST_CONTENT, getString(R.string.chat));
                            startActivity(intent);
                        }
                    }
                });
                break;

            case CommonConstant.SERVICE_STATUS_COMPLETED:
                serviceStatusCompletedAdapter = new ServiceStatusCompletedAdapter(getParentContext());
                recycler_view.setAdapter(serviceStatusCompletedAdapter);

                serviceStatusCompletedAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ServiceCompletedListBean.ServiceCompletedBean serviceCompletedBean = (ServiceCompletedListBean.ServiceCompletedBean) serviceStatusCompletedAdapter.getItem(position);

                        if (null != serviceCompletedBean) {
                            String avatarUrl = TypeUtils.getString(serviceCompletedBean.avatarUrl, "");
                            String name = TypeUtils.getString(serviceCompletedBean.name, "");
                            int gender = TypeUtils.getInteger(serviceCompletedBean.gender, -1);
                            Date birthday = serviceCompletedBean.birthday;
                            String loginTimeTip = TypeUtils.getString(serviceCompletedBean.loginTimeTip, "");
                            int isBook = TypeUtils.getInteger(serviceCompletedBean.isBook, -1);
                            int buyUserId = TypeUtils.getInteger(serviceCompletedBean.buyerUserId, -1);
                            int demandId = TypeUtils.getInteger(serviceCompletedBean.demandId, -1);
                            int orderId = TypeUtils.getInteger(serviceCompletedBean.orderId, -1);
                            boolean showEvaluationButton = TypeUtils.getBoolean(serviceCompletedBean.showEvaluationButton, false);
                            boolean showEvaluationDetailsButton = TypeUtils.getBoolean(serviceCompletedBean.showEvaluationDetailsButton, false);

                            int age = 0;

                            if (null != birthday) {
                                age = AgeUtils.getAge(birthday);
                            }

                            Intent intent = new Intent(getParentContext(), FacilitatorDemandDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_COMPLETED);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL, avatarUrl);
                            intent.putExtra(CommonConstant.EXTRA_NAME, name);
                            intent.putExtra(CommonConstant.EXTRA_GENDER, gender);
                            intent.putExtra(CommonConstant.EXTRA_AGE, age);
                            intent.putExtra(CommonConstant.EXTRA_TIME, loginTimeTip);
                            intent.putExtra(CommonConstant.EXTRA_IS_BOOK, isBook);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, buyUserId);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                            intent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                            intent.putExtra(CommonConstant.EXTRA_FIRST_CONTENT, getString(R.string.chat));
                            intent.putExtra(CommonConstant.EXTRA_SERVICE_COMPLETED_BEAN, serviceCompletedBean);

                            if (showEvaluationButton) {
                                intent.putExtra(CommonConstant.EXTRA_SECOND_CONTENT, getString(R.string.evaluate));
                            } else if (showEvaluationDetailsButton) {
                                intent.putExtra(CommonConstant.EXTRA_SECOND_CONTENT, getString(R.string.see_evaluate));
                            }

                            startActivity(intent);
                        }
                    }
                });

                serviceStatusCompletedAdapter.setOnClickListener(new ServiceStatusCompletedAdapter.OnClickListener() {
                    @Override
                    public void onChatClick(int buyerUserId) {
                        if (buyerUserId != -1) {
                            Intent intent = new Intent(getParentContext(), ChatActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(buyerUserId));
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onEvaluateClick(int userId, int orderId, int serviceId, int buyerUserId, int sellerUserId) {
                        Intent intent = new Intent(getParentContext(), EvaluateActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_EVALUATE_TYPE, CommonConstant.EVALUATE_TYPE_EMPLOYER);
                        intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                        intent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                        intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                        intent.putExtra(CommonConstant.EXTRA_BUYER_USER_ID, buyerUserId);
                        intent.putExtra(CommonConstant.EXTRA_SELLER_USER_ID, sellerUserId);
                        startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                    }

                    @Override
                    public void onSeeEvaluateClick(int orderId) {
                        Intent intent = new Intent(getParentContext(), EvaluateDetailsActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_EVALUATE_DETAILS_TYPE, CommonConstant.EVALUATE_DETAILS_TYPE_EMPLOYER);
                        intent.putExtra(CommonConstant.EXTRA_ORDER_ID, orderId);
                        startActivity(intent);
                    }
                });
                break;

            case CommonConstant.SERVICE_STATUS_EXPIRED:
                serviceStatusExpiredAdapter = new ServiceStatusExpiredAdapter(getParentContext());
                recycler_view.setAdapter(serviceStatusExpiredAdapter);

                serviceStatusExpiredAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ServiceExpiredListBean.ServiceExpiredBean serviceExpiredBean = (ServiceExpiredListBean.ServiceExpiredBean) serviceStatusExpiredAdapter.getItem(position);

                        if (null != serviceExpiredBean) {
                            String avatarUrl = TypeUtils.getString(serviceExpiredBean.avatarUrl, "");
                            String name = TypeUtils.getString(serviceExpiredBean.name, "");
                            int gender = TypeUtils.getInteger(serviceExpiredBean.gender, -1);
                            Date birthday = serviceExpiredBean.birthday;
                            String loginTimeTip = TypeUtils.getString(serviceExpiredBean.loginTimeTip, "");
                            int isBook = TypeUtils.getInteger(serviceExpiredBean.isBook, -1);
                            int buyerUserId = TypeUtils.getInteger(serviceExpiredBean.buyerUserId, -1);
                            int demandId = TypeUtils.getInteger(serviceExpiredBean.demandId, -1);

                            int age = 0;

                            if (null != birthday) {
                                age = AgeUtils.getAge(birthday);
                            }

                            Intent intent = new Intent(getParentContext(), FacilitatorDemandDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_EXPIRED);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL, avatarUrl);
                            intent.putExtra(CommonConstant.EXTRA_NAME, name);
                            intent.putExtra(CommonConstant.EXTRA_GENDER, gender);
                            intent.putExtra(CommonConstant.EXTRA_AGE, age);
                            intent.putExtra(CommonConstant.EXTRA_TIME, loginTimeTip);
                            intent.putExtra(CommonConstant.EXTRA_IS_BOOK, isBook);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, buyerUserId);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                            intent.putExtra(CommonConstant.EXTRA_FIRST_CONTENT, getString(R.string.chat));
                            startActivity(intent);
                        }
                    }
                });
                break;
        }
    }

    public void initData() {
        if (null != mPresenter) {
            mPresenter.getIdToChineseRequest();
        }
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
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_USER_TYPE, CommonConstant.MESSAGE_STATUS_USER_TYPE_FACILITATOR);
        emMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_CHANGED_PRICE, price);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    @Override
    public boolean isLoadingMore() {
        return false;
    }

    @Override
    protected void onLoadMore() {
        mPresenter.loadMore();
    }

    @Override
    protected void onSwipeRefresh() {
        mPresenter.refresh();
    }

    @Override
    public void onShowGetIdToChineseListBean(GetIdToChineseListBean getIdToChineseListBean) {
        List<GetIdToChineseListBean.GetIdToChineseBean> categoryList = getIdToChineseListBean.category;
        List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList = getIdToChineseListBean.serveWay;

        switch (serviceStatus) {
            case CommonConstant.SERVICE_STATUS_WAIT_BID:
                if (null != serviceStatusWaitBidAdapter) {
                    serviceStatusWaitBidAdapter.setCategoryList(categoryList);
                    serviceStatusWaitBidAdapter.setServiceModeList(serviceModeList);
                }
                break;

            case CommonConstant.SERVICE_STATUS_HAS_BID:
                if (null != serviceStatusHasBidAdapter) {
                    serviceStatusHasBidAdapter.setCategoryList(categoryList);
                    serviceStatusHasBidAdapter.setServiceModeList(serviceModeList);
                }
                break;

            case CommonConstant.SERVICE_STATUS_UNDERWAY:
                if (null != serviceStatusUnderwayAdapter) {
                    serviceStatusUnderwayAdapter.setCategoryList(categoryList);
                    serviceStatusUnderwayAdapter.setServiceModeList(serviceModeList);
                }
                break;

            case CommonConstant.SERVICE_STATUS_COMPLETED:
                if (null != serviceStatusCompletedAdapter) {
                    serviceStatusCompletedAdapter.setCategoryList(categoryList);
                    serviceStatusCompletedAdapter.setServiceModeList(serviceModeList);
                }
                break;

            case CommonConstant.SERVICE_STATUS_EXPIRED:
                if (null != serviceStatusExpiredAdapter) {
                    serviceStatusExpiredAdapter.setCategoryList(categoryList);
                    serviceStatusExpiredAdapter.setServiceModeList(serviceModeList);
                }
                break;
        }

        mPresenter.refresh();
    }

    @Override
    public void onShowServiceWaitBidList(List<ServiceWaitBidListBean.ServiceWaitBidBean> serviceWaitBidList) {
        if (null != serviceWaitBidList && serviceWaitBidList.size() > 0) {
            if (null != serviceStatusWaitBidAdapter) {
                serviceStatusWaitBidAdapter.setDateList(serviceWaitBidList);
                showContentView();
            }
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onShowServiceHasBidList(List<ServiceHasBidListBean.ServiceHasBidBean> serviceHasBidList) {
        if (null != serviceHasBidList && serviceHasBidList.size() > 0) {
            if (null != serviceStatusHasBidAdapter) {
                serviceStatusHasBidAdapter.setDateList(serviceHasBidList);
                showContentView();
            }
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onShowServiceUnderwayList(List<ServiceUnderwayListBean.ServiceUnderwayBean> serviceUnderwayList) {
        if (null != serviceUnderwayList && serviceUnderwayList.size() > 0) {
            if (null != serviceStatusUnderwayAdapter) {
                serviceStatusUnderwayAdapter.setDateList(serviceUnderwayList);
                showContentView();
            }
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onShowServiceCompletedList(List<ServiceCompletedListBean.ServiceCompletedBean> serviceCompletedList) {
        if (null != serviceCompletedList && serviceCompletedList.size() > 0) {
            if (null != serviceStatusCompletedAdapter) {
                serviceStatusCompletedAdapter.setDateList(serviceCompletedList);
                showContentView();
            }
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onShowServiceExpiredList(List<ServiceExpiredListBean.ServiceExpiredBean> serviceExpiredList) {
        if (null != serviceExpiredList && serviceExpiredList.size() > 0) {
            if (null != serviceStatusExpiredAdapter) {
                serviceStatusExpiredAdapter.setDateList(serviceExpiredList);
                showContentView();
            }
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onShowAdvantage(int position, String advantage) {
        if (null != serviceStatusWaitBidAdapter) {
            serviceStatusWaitBidAdapter.showBidToMakeMoneyAtOnceDialog(position, advantage);
        }
    }

    @Override
    public void onBidSuccess(int userId, int demandId) {
        showToast("投标赚钱成功");

        if (null != serviceStatusWaitBidAdapter) {
            serviceStatusWaitBidAdapter.dismissBidToMakeMoneyAtOnceDialog();
        }

        sendBidSuccessMessage(userId, demandId);
        mPresenter.refresh();

        Intent intent = new Intent();
        intent.setAction(CommonConstant.ACTION_SERVICE_STATUS);
        intent.putExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_HAS_BID);
        getParentContext().sendBroadcast(intent);
    }

    @Override
    public void onChangePriceSuccess(int userId, int demandId, String price) {
        showToast("修改价格成功");

        if (null != serviceStatusHasBidAdapter) {
            serviceStatusHasBidAdapter.dismissModifyServicePriceDialog();
        }

        sendModifyPriceMessage(userId, demandId, price);
        mPresenter.refresh();
    }

    @Override
    public void onShowCompletedBasicPersonalInformation() {
        Intent intent = new Intent(getParentContext(), PublishServiceActivity.class);
        intent.putExtra(CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE, CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD);
        startActivity(intent);
    }

    @Override
    public void onShowBasicPersonalInformationBean(CheckBasicPersonalInformationBean checkBasicPersonalInformationBean) {
        Intent intent = new Intent(getParentContext(), CompletePersonalInformationActivity.class);
        intent.putExtra(CommonConstant.EXTRA_COMPLETE_PERSONAL_INFORMATION_TYPE, CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_COMPLETE);
        intent.putExtra(CommonConstant.EXTRA_PUBLISH_TYPE, CommonConstant.PUBLISH_TYPE_PUBLISH_DEMAND);
        intent.putExtra(CommonConstant.EXTRA_CHECK_BASIC_PERSONAL_INFORMATION_BEAN, checkBasicPersonalInformationBean);
        startActivity(intent);
    }

    @Override
    public void showIsZmrzAuthSuccess(boolean isZmrzAuthSuccess) {
        Intent intent = new Intent(getParentContext(), IdentifyAuthenticationActivity.class);
        intent.putExtra(CommonConstant.EXTRA_IS_ZHI_MA_CREDIT_AUTHENTICATION_SUCCESS, isZmrzAuthSuccess);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.REFRESH_RESULT_CODE) {
            mPresenter.refresh();
        }
    }

}
