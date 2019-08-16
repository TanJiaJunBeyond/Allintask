package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.BaseFragmentActivity;
import com.allintask.lingdao.ui.fragment.user.ModifyBankCardFragment;
import com.allintask.lingdao.ui.fragment.user.SetBankCardFragment;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class BankCardSettingActivity extends BaseFragmentActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;

    private int bankCardSettingType = CommonConstant.BANK_CARD_SETTING_TYPE_SET_BANK_CARD;
    private int bankId;
    private String bankCardName;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bank_card_setting;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            bankCardSettingType = intent.getIntExtra(CommonConstant.EXTRA_BANK_CARD_SETTING_TYPE, CommonConstant.BANK_CARD_SETTING_TYPE_SET_BANK_CARD);
            bankId = intent.getIntExtra(CommonConstant.EXTRA_BANK_ID, -1);
            bankCardName = intent.getStringExtra(CommonConstant.EXTRA_BANK_CARD_NAME);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.bank_card_setting));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        switch (bankCardSettingType) {
            case CommonConstant.BANK_CARD_SETTING_TYPE_SET_BANK_CARD:
                openFragment(SetBankCardFragment.class.getName(), false, null);
                break;

            case CommonConstant.BANK_CARD_SETTING_TYPE_MODIFY_BANK_CARD:
                Bundle bundle = new Bundle();
                bundle.putInt(CommonConstant.EXTRA_BANK_ID, bankId);
                bundle.putString(CommonConstant.EXTRA_BANK_CARD_NAME, bankCardName);
                openFragment(ModifyBankCardFragment.class.getName(), false, bundle);
                break;
        }
    }

}
