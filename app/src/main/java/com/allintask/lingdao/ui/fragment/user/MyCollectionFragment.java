package com.allintask.lingdao.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.bean.user.MyCollectListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.MyCollectionPresenter;
import com.allintask.lingdao.ui.activity.demand.DemandDetailsActivity;
import com.allintask.lingdao.ui.activity.recommend.RecommendDetailsActivity;
import com.allintask.lingdao.ui.adapter.main.RecommendDemandAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.adapter.user.MyCollectionAdapter;
import com.allintask.lingdao.ui.fragment.BaseSwipeRefreshFragment;
import com.allintask.lingdao.view.user.IMyCollectionView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/6/19.
 */

public class MyCollectionFragment extends BaseSwipeRefreshFragment<IMyCollectionView, MyCollectionPresenter> implements IMyCollectionView {

    private int myCollectionStatus = CommonConstant.MY_COLLECTION_SERVICE;

    private MyCollectionAdapter myCollectionAdapter;
    private RecommendDemandAdapter recommendDemandAdapter;

    private OnDataLoadingCompletedListener mOnDataLoadingCompletedListener;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_collection;
    }

    @Override
    protected MyCollectionPresenter CreatePresenter() {
        Bundle bundle = getArguments();

        if (null != bundle) {
            myCollectionStatus = bundle.getInt(CommonConstant.EXTRA_MY_COLLECTION_STATUS, CommonConstant.MY_COLLECTION_SERVICE);
        }
        return new MyCollectionPresenter(myCollectionStatus);
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
            contentTv.setText(getString(R.string.my_collection_no_data));

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

        if (myCollectionStatus == CommonConstant.MY_COLLECTION_SERVICE) {
            myCollectionAdapter = new MyCollectionAdapter(getParentContext());
            recycler_view.setAdapter(myCollectionAdapter);

            myCollectionAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MyCollectListBean.MyCollectBean myCollectBean = (MyCollectListBean.MyCollectBean) myCollectionAdapter.getItem(position);

                    if (null != myCollectBean) {
                        int beUserId = TypeUtils.getInteger(myCollectBean.beUserId, -1);

                        Intent intent = new Intent(getParentContext(), RecommendDetailsActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_USER_ID, beUserId);
                        startActivity(intent);
                    }
                }
            });
        } else if (myCollectionStatus == CommonConstant.MY_COLLECTION_DEMAND) {
            recommendDemandAdapter = new RecommendDemandAdapter(getParentContext());
            recycler_view.setAdapter(recommendDemandAdapter);

            recommendDemandAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    RecommendDemandBean recommendDemandBean = (RecommendDemandBean) recommendDemandAdapter.getItem(position);

                    if (null != recommendDemandBean) {
                        int demandId = TypeUtils.getInteger(recommendDemandBean.demandId, -1);

                        if (demandId != -1) {
                            Intent intent = new Intent(getParentContext(), DemandDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    public void initData() {
        if (null != mPresenter) {
            mPresenter.refresh();
        }
    }

    public interface OnDataLoadingCompletedListener {

        void onShowMyCollectionListSize(int size);

    }

    public void setOnDataLoadingCompletedListener(OnDataLoadingCompletedListener onDataLoadingCompletedListener) {
        mOnDataLoadingCompletedListener = onDataLoadingCompletedListener;
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
    public boolean isLoadingMore() {
        return false;
    }

    @Override
    public void onShowMyCollectList(List<MyCollectListBean.MyCollectBean> myCollectList) {
        if (null != myCollectList && myCollectList.size() > 0) {
            myCollectionAdapter.setDateList(myCollectList);
            showContentView();

            if (null != mOnDataLoadingCompletedListener) {
                mOnDataLoadingCompletedListener.onShowMyCollectionListSize(myCollectList.size());
            }
        } else {
            showEmptyView();

            if (null != mOnDataLoadingCompletedListener) {
                mOnDataLoadingCompletedListener.onShowMyCollectionListSize(0);
            }
        }
    }

    @Override
    public void onShowMyCollectionDemandList(List<RecommendDemandBean> recommendDemandList) {
        if (null != recommendDemandList && recommendDemandList.size() > 0) {
            recommendDemandAdapter.setDateList(recommendDemandList);
            showContentView();

            if (null != mOnDataLoadingCompletedListener) {
                mOnDataLoadingCompletedListener.onShowMyCollectionListSize(recommendDemandList.size());
            }
        } else {
            showEmptyView();

            if (null != mOnDataLoadingCompletedListener) {
                mOnDataLoadingCompletedListener.onShowMyCollectionListSize(0);
            }
        }
    }

}
