package com.allintask.lingdao.ui.activity.recommend;

import android.content.Intent;
import android.graphics.Rect;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.EvaluationListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.recommend.AllEvaluatePresenter;
import com.allintask.lingdao.ui.activity.BaseSwipeRefreshActivity;
import com.allintask.lingdao.ui.adapter.recommend.EvaluateAdapter;
import com.allintask.lingdao.view.recommend.IAllEvaluateView;

import java.util.List;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2018/3/3.
 */

public class AllEvaluateActivity extends BaseSwipeRefreshActivity<IAllEvaluateView, AllEvaluatePresenter> implements IAllEvaluateView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;

    private int userId = -1;

    private EvaluateAdapter evaluateAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_all_evaluate;
    }

    @Override
    protected AllEvaluatePresenter CreatePresenter() {
        return new AllEvaluatePresenter();
    }

    @Override
    protected void init() {
        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
        }

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.all_evaluate));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        if (null != mSwipeRefreshStatusLayout) {
            View emptyView = mSwipeRefreshStatusLayout.getEmptyView();

            TextView contentTv = (TextView) emptyView.findViewById(R.id.tv_content);
            contentTv.setText(getString(R.string.all_evaluate_no_data));

            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadingView();
                    mPresenter.refresh(userId);
                }
            });

            View noNetworkView = mSwipeRefreshStatusLayout.getNoNetworkView();

            Button checkNetworkBtn = (Button) noNetworkView.findViewById(R.id.btn_check_network);
            checkNetworkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            });
        }

        recycler_view.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, DensityUtils.dip2px(getParentContext(), 1));
            }
        });

        evaluateAdapter = new EvaluateAdapter(getParentContext());
        recycler_view.setAdapter(evaluateAdapter);
    }

    private void initData() {
        mPresenter.refresh(userId);
    }

    @Override
    protected void onLoadMore() {
        mPresenter.loadMore(userId);
    }

    @Override
    protected void onSwipeRefresh() {
        mPresenter.refresh(userId);
    }

    @Override
    public void onShowEvaluateList(List<EvaluationListBean.EvaluationBean> evaluationList) {
        if (null != evaluationList && evaluationList.size() > 0) {
            evaluateAdapter.setDateList(evaluationList);
            showContentView();
        } else {
            showEmptyView();
        }
    }

}
