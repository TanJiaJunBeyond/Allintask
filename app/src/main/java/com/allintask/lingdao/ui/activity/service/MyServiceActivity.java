package com.allintask.lingdao.ui.activity.service;

import android.content.Intent;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.MyServiceListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.service.MyServicePresenter;
import com.allintask.lingdao.ui.activity.BaseSwipeRefreshActivity;
import com.allintask.lingdao.ui.activity.main.PublishServiceActivity;
import com.allintask.lingdao.ui.activity.recommend.RecommendDetailsActivity;
import com.allintask.lingdao.ui.activity.user.PersonalInformationActivity;
import com.allintask.lingdao.ui.adapter.service.MyServiceAdapter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.service.IMyServiceView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public class MyServiceActivity extends BaseSwipeRefreshActivity<IMyServiceView, MyServicePresenter> implements IMyServiceView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_right_first)
    TextView rightFirstTv;
    @BindView(R.id.rl_add_photos)
    RelativeLayout addPhotosRL;
    @BindView(R.id.view_white_divider_line)
    View whiteDividerLineView;
    @BindView(R.id.rl_resume_complete)
    RelativeLayout resumeCompleteRL;
    @BindView(R.id.tv_resume_complete_rate)
    TextView resumeCompleteRateTv;
    @BindView(R.id.btn_publish_service)
    Button publishServiceBtn;
    @BindView(R.id.btn_add_photos)
    Button addPhotosBtn;

    private int mUserId;
    private MyServiceAdapter myServiceAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_service;
    }

    @Override
    protected MyServicePresenter CreatePresenter() {
        return new MyServicePresenter();
    }

    @Override
    protected void init() {
        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.my_service));
        rightFirstTv.setText(getString(R.string.preview));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        mUserId = UserPreferences.getInstance().getUserId();

        if (null != mSwipeRefreshStatusLayout) {
            View emptyView = mSwipeRefreshStatusLayout.getEmptyView();

            TextView contentTv = (TextView) emptyView.findViewById(R.id.tv_content);
            contentTv.setText(getString(R.string.my_service_no_data));

            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadingView();
                    mPresenter.refresh();
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

        boolean isFirstShowAddPhotosLayout = UserPreferences.getInstance().getIsFirstShowAddPhotosLayout();

        if (isFirstShowAddPhotosLayout) {
            whiteDividerLineView.setVisibility(View.VISIBLE);
            addPhotosRL.setVisibility(View.VISIBLE);
        } else {
            whiteDividerLineView.setVisibility(View.GONE);
            addPhotosRL.setVisibility(View.GONE);
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getParentContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getParentContext(), R.drawable.shape_common_recycler_view_divider));
        recycler_view.addItemDecoration(dividerItemDecoration);

        myServiceAdapter = new MyServiceAdapter(getParentContext());
        recycler_view.setAdapter(myServiceAdapter);

        myServiceAdapter.setOnClickListener(new MyServiceAdapter.OnClickListener() {
            @Override
            public void onDeleteClickListener(int position, int serviceId) {
                mPresenter.deleteServiceRequest(position, serviceId);
            }

            @Override
            public void onGoOnlineClickListener(int position, int serviceId) {
                mPresenter.goOnlineServiceRequest(position, serviceId);
            }

            @Override
            public void onGoOfflineClickListener(int position, int serviceId) {
                mPresenter.goOfflineServiceRequest(position, serviceId);
            }

            @Override
            public void onCompileClickListener(int position, int serviceId, int categoryId) {
                Intent intent = new Intent(getParentContext(), PublishServiceActivity.class);
                intent.putExtra(CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE, CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE);
                intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                intent.putExtra(CommonConstant.EXTRA_CATEGORY_ID, categoryId);
                startActivityForResult(intent, CommonConstant.REQUEST_CODE);
            }
        });
    }

    private void initData() {
        mPresenter.getResumeCompleteRateRequest();
        mPresenter.getIdToChineseRequest();
    }

    @OnClick({R.id.tv_right_first, R.id.rl_add_photos, R.id.btn_complete, R.id.btn_publish_service, R.id.btn_add_photos})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right_first:
                Intent recommendDetailsIntent = new Intent(getParentContext(), RecommendDetailsActivity.class);
                recommendDetailsIntent.putExtra(CommonConstant.EXTRA_USER_ID, mUserId);
                startActivity(recommendDetailsIntent);
                break;

            case R.id.rl_add_photos:
                UserPreferences.getInstance().setIsFirstShowAddPhotosLayout(false);
                whiteDividerLineView.setVisibility(View.GONE);
                addPhotosRL.setVisibility(View.GONE);
                break;

            case R.id.btn_complete:
                Intent personalInformationIntent = new Intent(getParentContext(), PersonalInformationActivity.class);
                startActivity(personalInformationIntent);
                break;

            case R.id.btn_publish_service:
                Intent publishServiceIntent = new Intent(getParentContext(), PublishServiceActivity.class);
                publishServiceIntent.putExtra(CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE, CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD);
                startActivity(publishServiceIntent);
                break;

            case R.id.btn_add_photos:
                Intent intent = new Intent(getParentContext(), UploadAlbumActivity.class);
                intent.putExtra(CommonConstant.EXTRA_UPLOAD_ALBUM_TYPE, CommonConstant.UPLOAD_ALBUM_PERSONAL);
                startActivity(intent);
                break;
        }
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
    public void onShowResumeCompleteRate(int resumeCompleteRate) {
        StringBuilder stringBuilder = new StringBuilder(getString(R.string.resume_complete_rate)).append(String.valueOf(resumeCompleteRate)).append("%");
        resumeCompleteRateTv.setText(stringBuilder);
        resumeCompleteRL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowGetIdToChineseListBean(GetIdToChineseListBean getIdToChineseListBean) {
        List<GetIdToChineseListBean.GetIdToChineseBean> categoryList = getIdToChineseListBean.category;
        List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList = getIdToChineseListBean.serveWay;
        List<GetIdToChineseListBean.GetIdToChineseBean> priceUnitList = getIdToChineseListBean.priceUnit;

        if (null != myServiceAdapter) {
            myServiceAdapter.setCategoryList(categoryList);
            myServiceAdapter.setServiceModeList(serviceModeList);
            myServiceAdapter.setPriceUnitList(priceUnitList);

            mPresenter.refresh();
        }
    }

    @Override
    public void onShowMyServiceList(List<MyServiceListBean.MyServiceBean> myServiceListList) {
        if (null != myServiceAdapter) {
            if (null != myServiceListList && myServiceListList.size() > 0) {
                myServiceAdapter.setDateList(myServiceListList);
                showContentView();
            } else {
                showEmptyView();
            }
        }
    }

    @Override
    public void onDeleteServiceSuccess(int position) {
        mPresenter.refresh();
    }

    @Override
    public void onGoOnlineSuccess(int position) {
        if (null != myServiceAdapter) {
            myServiceAdapter.goOnlineService(position);
        }
    }

    @Override
    public void onGoOfflineSuccess(int position) {
        if (null != myServiceAdapter) {
            myServiceAdapter.goOfflineService(position);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.REFRESH_RESULT_CODE) {
//            mPresenter.refresh();
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

}
