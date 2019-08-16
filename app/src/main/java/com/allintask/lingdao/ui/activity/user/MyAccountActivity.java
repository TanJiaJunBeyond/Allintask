package com.allintask.lingdao.ui.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.TransactionRecordListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.MyAccountPresenter;
import com.allintask.lingdao.ui.activity.BaseSwipeRefreshActivity;
import com.allintask.lingdao.ui.adapter.user.MyAccountAdapter;
import com.allintask.lingdao.utils.SystemBarTintManager;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IMyAccountView;

import java.util.List;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.component.util.DialogUtils;

/**
 * Created by TanJiaJun on 2018/2/24.
 */

public class MyAccountActivity extends BaseSwipeRefreshActivity<IMyAccountView, MyAccountPresenter> implements IMyAccountView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.iv_right_second)
    ImageView rightIv;

    private int bankCardSettingType = CommonConstant.BANK_CARD_SETTING_TYPE_SET_BANK_CARD;
    private int paymentPasswordType = CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD;

    private MyAccountAdapter myAccountAdapter;
    private PopupWindow myAccountMorePopupWindow;

    private boolean isExistRealName = false;
    private boolean isExistBankCard = false;
    private boolean isExistPayPwd = false;

    private int mMobileCountryCodeId;
    private int mBankId;
    private String mBankCardName;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_account;
    }

    @Override
    protected MyAccountPresenter CreatePresenter() {
        return new MyAccountPresenter();
    }

    @Override
    protected void init() {
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.theme_orange));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(MyAccountActivity.this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.color.theme_orange);

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        MIUISetStatusBarLightMode(window, false);
        FlymeSetStatusBarLightMode(window, false);

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.theme_orange));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.my_account));
        titleTv.setTextColor(getResources().getColor(R.color.white));

        rightIv.setImageResource(R.mipmap.ic_more_white);
        rightIv.setVisibility(View.VISIBLE);
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyAccountMorePopupWindow();
            }
        });

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        myAccountAdapter = new MyAccountAdapter(getParentContext());
        recycler_view.setAdapter(myAccountAdapter);
        recycler_view.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, DensityUtils.dip2px(getParentContext(), 1));
            }
        });

        myAccountAdapter.setOnClickListener(new MyAccountAdapter.OnClickListener() {
            @Override
            public void onWithdrawDepositClick() {
                if (!isExistRealName) {
                    DialogUtils.showAlertDialog(getParentContext(), "温馨提示", "实名认证通过后才能提现哦！", getString(R.string.go_to_verify), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent(getParentContext(), IdentifyAuthenticationActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_IS_ZHI_MA_CREDIT_AUTHENTICATION_SUCCESS, false);
                            startActivity(intent);
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else if (!isExistBankCard) {
                    DialogUtils.showAlertDialog(getParentContext(), "温馨提示", "你还没有填写银行卡，请填写完再提现", getString(R.string.go_to_fill_in), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent(getParentContext(), BankCardSettingActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_BANK_CARD_SETTING_TYPE, CommonConstant.BANK_CARD_SETTING_TYPE_SET_BANK_CARD);
                            startActivity(intent);
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else if (!isExistPayPwd) {
                    DialogUtils.showAlertDialog(getParentContext(), "温馨提示", "你还没有设置支付密码，设置完再提现", getString(R.string.go_to_set), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            int mobileCountryCodeId = UserPreferences.getInstance().getMobileCountryCodeId();

                            Intent intent = new Intent(getParentContext(), PaymentPasswordActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD);
                            intent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
                            startActivity(intent);
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    Intent intent = new Intent(getParentContext(), ApplyForWithdrawDepositActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        mPresenter.fetchSettingRequest();
        mPresenter.fetchAccountBalanceRequest();
    }

    private void showMyAccountMorePopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_my_account_more, null);
        myAccountMorePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        myAccountMorePopupWindow.setContentView(contentView);
        myAccountMorePopupWindow.showAtLocation(LayoutInflater.from(getParentContext()).inflate(getLayoutResId(), null), Gravity.NO_GRAVITY, 0, 0);

        RelativeLayout myAccountMoreRL = contentView.findViewById(R.id.rl_my_account_more);
        TextView bankCardTv = contentView.findViewById(R.id.tv_bank_card);
        TextView paymentPasswordTv = contentView.findViewById(R.id.tv_payment_password);

        myAccountMoreRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != myAccountMorePopupWindow && myAccountMorePopupWindow.isShowing()) {
                    myAccountMorePopupWindow.dismiss();
                }
            }
        });

        bankCardTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != myAccountMorePopupWindow && myAccountMorePopupWindow.isShowing()) {
                    myAccountMorePopupWindow.dismiss();
                }

                Intent intent = new Intent(getParentContext(), BankCardSettingActivity.class);

                if (!isExistBankCard) {
                    intent.putExtra(CommonConstant.EXTRA_BANK_CARD_SETTING_TYPE, CommonConstant.BANK_CARD_SETTING_TYPE_SET_BANK_CARD);
                } else {
                    intent.putExtra(CommonConstant.EXTRA_BANK_CARD_SETTING_TYPE, CommonConstant.BANK_CARD_SETTING_TYPE_MODIFY_BANK_CARD);
                    intent.putExtra(CommonConstant.EXTRA_BANK_ID, mBankId);
                    intent.putExtra(CommonConstant.EXTRA_BANK_CARD_NAME, mBankCardName);
                }

                startActivity(intent);
            }
        });

        paymentPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != myAccountMorePopupWindow && myAccountMorePopupWindow.isShowing()) {
                    myAccountMorePopupWindow.dismiss();
                }

                if (!isExistPayPwd) {
                    Intent intent = new Intent(getParentContext(), PaymentPasswordActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD);
                    intent.putExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mMobileCountryCodeId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getParentContext(), SecurityManagementActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onLoadMore() {
        mPresenter.loadMore();
    }

    @Override
    protected void onSwipeRefresh() {
        initData();
    }

    @Override
    public void onBackPressed() {
        if (null != myAccountMorePopupWindow && myAccountMorePopupWindow.isShowing()) {
            myAccountMorePopupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onShowMobileCountryCodeId(int mobileCountryCodeId) {
        mMobileCountryCodeId = mobileCountryCodeId;
    }

    @Override
    public void onShowBankId(Integer bankId) {
        mBankId = bankId;
    }

    @Override
    public void onShowBankCardName(String bankCardName) {
        mBankCardName = bankCardName;
    }

    @Override
    public void onShowAccountData(double accountBalance, double canWithdraw, double advanceRecharge, boolean isShowRechargeButton, boolean isShowWithdrawButton, double withdrawLowPrice, double withdrawRate) {
        if (null != myAccountAdapter) {
            myAccountAdapter.setAccountData(accountBalance, canWithdraw, advanceRecharge, isShowRechargeButton, isShowWithdrawButton, withdrawLowPrice, withdrawRate);
        }

        mPresenter.checkCanWithdrawRequest();
    }

    @Override
    public void onShowCheckCanWithdrawData(boolean isExistBankCard, boolean isExistPayPwd, boolean isExistRealName) {
        this.isExistRealName = isExistRealName;
        this.isExistBankCard = isExistBankCard;
        this.isExistPayPwd = isExistPayPwd;

        mPresenter.refresh();
    }

    @Override
    public void onShowTransactionRecordList(List<TransactionRecordListBean.TransactionRecordBean> transactionRecordList) {
        if (null != transactionRecordList && transactionRecordList.size() > 0) {
            myAccountAdapter.setDateList(transactionRecordList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

}
