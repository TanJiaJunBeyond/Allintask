package com.allintask.lingdao.ui.activity.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationBean;
import com.allintask.lingdao.presenter.user.PhoneNumberHomeLocationPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.user.PhoneNumberHomeLocationAdapter;
import com.allintask.lingdao.utils.PinyinUtils;
import com.allintask.lingdao.view.user.IPhoneNumberHomeLocationView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.allintask.lingdao.widget.LetterBarView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/22.
 */

public class PhoneNumberHomeLocationActivity extends BaseActivity<IPhoneNumberHomeLocationView, PhoneNumberHomeLocationPresenter> implements IPhoneNumberHomeLocationView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;
    @BindView(R.id.letter_bar_view)
    LetterBarView letterBarView;

    private List<PhoneNumberHomeLocationBean> phoneNumberHomeLocationList;
    private PhoneNumberHomeLocationAdapter phoneNumberHomeLocationAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_phone_number_home_location;
    }

    @Override
    protected PhoneNumberHomeLocationPresenter CreatePresenter() {
        return new PhoneNumberHomeLocationPresenter();
    }

    @Override
    protected void init() {
        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.phone_number_home_location));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        showLoadingView();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        phoneNumberHomeLocationAdapter = new PhoneNumberHomeLocationAdapter(getParentContext());
        recyclerView.setAdapter(phoneNumberHomeLocationAdapter);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(phoneNumberHomeLocationAdapter));

        letterBarView.setOnLetterSelectListener(new LetterBarView.OnLetterSelectListener() {
            @Override
            public void onLetterSelect(String s) {
                if (s.equalsIgnoreCase("#")) {
                    linearLayoutManager.scrollToPositionWithOffset(0, 0);
                } else {
                    linearLayoutManager.scrollToPositionWithOffset(getPositionFromInitial(s), 0);
                }
            }
        });
    }

    private void initData() {
        phoneNumberHomeLocationList = new ArrayList<>();
        mPresenter.fetchPhoneNumberLocationListRequest();
    }

    private List<PhoneNumberHomeLocationBean> setInitial(List<PhoneNumberHomeLocationBean> isAllList) {
        List<PhoneNumberHomeLocationBean> tempIsAllList = new ArrayList<>();

        for (int i = 0; i < isAllList.size(); i++) {
            PhoneNumberHomeLocationBean phoneNumberHomeLocationBean = isAllList.get(i);
            String name = TypeUtils.getString(phoneNumberHomeLocationBean.name, "");
            String initial = PinyinUtils.getPinyin(name).substring(0, 1);
            phoneNumberHomeLocationBean.initial = initial;
            tempIsAllList.add(phoneNumberHomeLocationBean);
        }
        return tempIsAllList;
    }

    public class PinyinComparator implements Comparator<PhoneNumberHomeLocationBean> {
        @Override
        public int compare(PhoneNumberHomeLocationBean o1, PhoneNumberHomeLocationBean o2) {
            if (o2.initial.equals("#")) {
                return -1;
            } else if (o1.initial.equals("#")) {
                return 1;
            } else {
                return o1.initial.compareTo(o2.initial);
            }
        }
    }

    private int getPositionFromInitial(String initial) {
        for (int i = 0; i < phoneNumberHomeLocationList.size(); i++) {
            String tempInitial = TypeUtils.getString(phoneNumberHomeLocationList.get(i).initial, "");

            if (initial.equals(tempInitial)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onShowPhoneNumberHomeLocationList(List<PhoneNumberHomeLocationBean> isHotList, List<PhoneNumberHomeLocationBean> isAllList) {
        if ((null != isHotList && isHotList.size() > 0) || (null != isAllList && isAllList.size() > 0)) {
            List<PhoneNumberHomeLocationBean> tempIsAllList = setInitial(isAllList);

            PinyinComparator pinyinComparator = new PinyinComparator();
            Collections.sort(tempIsAllList, pinyinComparator);

            for (int i = 0; i < isHotList.size(); i++) {
                PhoneNumberHomeLocationBean phoneNumberHomeLocationBean = isHotList.get(i);
                phoneNumberHomeLocationBean.initial = getString(R.string.common);
                phoneNumberHomeLocationList.add(i, phoneNumberHomeLocationBean);
            }

            phoneNumberHomeLocationList.addAll(tempIsAllList);
            phoneNumberHomeLocationAdapter.setDateList(phoneNumberHomeLocationList);
            showContentView();
        } else {
            showEmptyView();
        }
    }

}
