package com.allintask.lingdao.presenter.user;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.ReportReasonBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.user.ReportReasonAdapter;
import com.allintask.lingdao.view.user.IReportReasonView;
import com.allintask.lingdao.widget.CommonRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/4/9.
 */

public class ReportReasonActivity extends BaseActivity<IReportReasonView, ReportReasonPresenter> implements IReportReasonView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.iv_right_second)
    ImageView rightIv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;

    private ReportReasonAdapter reportReasonAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_report_reason;
    }

    @Override
    protected ReportReasonPresenter CreatePresenter() {
        return new ReportReasonPresenter();
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

        titleTv.setText(getString(R.string.report_reason));
        rightIv.setImageResource(R.mipmap.ic_tick);
        rightIv.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, 1);
            }
        });

        reportReasonAdapter = new ReportReasonAdapter(getParentContext());
        recyclerView.setAdapter(reportReasonAdapter);
    }

    private void initData() {
        mPresenter.fetchReportReasonListRequest();
    }

    @OnClick({R.id.iv_right_second})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_right_second:
                StringBuilder reportReasonIdListSB = new StringBuilder();
                StringBuilder reportReasonSB = new StringBuilder();

                int itemCount = reportReasonAdapter.getItemCount();

                for (int i = 0; i < itemCount; i++) {
                    ReportReasonBean reportReasonBean = (ReportReasonBean) reportReasonAdapter.getItem(i);

                    if (null != reportReasonBean) {
                        int id = TypeUtils.getInteger(reportReasonBean.id, -1);
                        String name = TypeUtils.getString(reportReasonBean.name, "");
                        boolean isChecked = TypeUtils.getBoolean(reportReasonBean.isChecked, false);

                        if (isChecked) {
                            reportReasonIdListSB.append(id).append(":");
                            reportReasonSB.append(name).append("、");
                        }

                        if (i == itemCount - 1) {
                            if (!TextUtils.isEmpty(reportReasonIdListSB)) {
                                reportReasonIdListSB.deleteCharAt(reportReasonIdListSB.length() - 1);
                            }

                            if (!TextUtils.isEmpty(reportReasonSB)) {
                                reportReasonSB.deleteCharAt(reportReasonSB.length() - 1);
                            }
                        }
                    }
                }

                String reportReasonIdList = reportReasonIdListSB.toString();
                String reportReasonString = reportReasonSB.toString();

                Intent intent = new Intent();
                intent.putExtra(CommonConstant.EXTRA_REPORT_REASON_ID_LIST_STRING, reportReasonIdList);
                intent.putExtra(CommonConstant.EXTRA_REPORT_REASON_STRING, reportReasonString);
                setResult(CommonConstant.RESULT_CODE, intent);

                finish();
                break;
        }
    }

    @Override
    public void onShowReportReasonList(List<ReportReasonBean> reportReasonList) {
        if (null != reportReasonList && reportReasonList.size() > 0) {
            reportReasonAdapter.setDateList(reportReasonList);
        }
    }

}
