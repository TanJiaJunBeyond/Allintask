package com.allintask.lingdao.ui.fragment.demand;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.DemandStatusListBean;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.demand.DemandStatusPresenter;
import com.allintask.lingdao.ui.activity.demand.EmployerDemandDetailsActivity;
import com.allintask.lingdao.ui.activity.main.PublishDemandActivity;
import com.allintask.lingdao.ui.activity.user.CompletePersonalInformationActivity;
import com.allintask.lingdao.ui.adapter.main.DemandStatusAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.fragment.BaseSwipeRefreshFragment;
import com.allintask.lingdao.view.demand.IDemandStatusView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public class DemandStatusFragment extends BaseSwipeRefreshFragment<IDemandStatusView, DemandStatusPresenter> implements IDemandStatusView {

    private int demandStatus = 0;

    private DemandStatusAdapter demandStatusAdapter;

    private OnDataLoadingCompletedListener onDataLoadingCompletedListener;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_demand_status;
    }

    @Override
    protected DemandStatusPresenter CreatePresenter() {
        Bundle bundle = getArguments();

        if (null != bundle) {
            demandStatus = bundle.getInt(CommonConstant.EXTRA_DEMAND_STATUS, 0);
        }

        return new DemandStatusPresenter(demandStatus);
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

            contentTv.setText(getString(R.string.demand_status_no_data));

            if (demandStatus == CommonConstant.DEMAND_STATUS_IN_THE_BIDDING) {
                contentBtn.setText(getString(R.string.publish_at_once));
                contentBtn.setVisibility(View.VISIBLE);
                contentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.checkBasicPersonalInformationWholeRequest();
                    }
                });
            }

            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadingView();
                    mPresenter.refresh();
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

        demandStatusAdapter = new DemandStatusAdapter(getParentContext(), demandStatus);
        recycler_view.setAdapter(demandStatusAdapter);

        demandStatusAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DemandStatusListBean.DemandStatusBean demandStatusBean = (DemandStatusListBean.DemandStatusBean) demandStatusAdapter.getItem(position);

                if (null != demandStatusBean) {
                    int demandId = TypeUtils.getInteger(demandStatusBean.demandId, -1);

                    if (demandId != -1) {
                        Intent intent = new Intent(getParentContext(), EmployerDemandDetailsActivity.class);

                        if (demandStatus == CommonConstant.DEMAND_STATUS_UNDERWAY) {
                            intent.putExtra(CommonConstant.EXTRA_TYPE, CommonConstant.LOOK_OVER_WIN_THE_BIDDING);
                        } else if (demandStatus == CommonConstant.DEMAND_STATUS_COMPLETED) {
                            intent.putExtra(CommonConstant.EXTRA_TYPE, CommonConstant.LOOK_OVER_COMPLETE);
                        }

                        intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void initData() {
        if (null != mPresenter) {
            mPresenter.refresh();
        }
    }

    public interface OnDataLoadingCompletedListener {

        void onShowDemandStatusListSize(int size);

    }

    public void setOnDataLoadingCompletedListener(OnDataLoadingCompletedListener onDataLoadingCompletedListener) {
        this.onDataLoadingCompletedListener = onDataLoadingCompletedListener;
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
    public void onShowDemandStatusList(List<DemandStatusListBean.DemandStatusBean> demandStatusList) {
        if (null != demandStatusAdapter) {
            if (null != demandStatusList && demandStatusList.size() > 0) {
                demandStatusAdapter.setDateList(demandStatusList);
                showContentView();

                if (null != onDataLoadingCompletedListener) {
                    onDataLoadingCompletedListener.onShowDemandStatusListSize(demandStatusList.size());
                }
            } else {
                showEmptyView();

                if (null != onDataLoadingCompletedListener) {
                    onDataLoadingCompletedListener.onShowDemandStatusListSize(0);
                }
            }
        }
    }

    @Override
    public void onShowCompletedBasicPersonalInformation() {
        Intent intent = new Intent(getParentContext(), PublishDemandActivity.class);
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

}
