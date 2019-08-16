package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.MyPhotoAlbumListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.MyPhotoAlbumPresenter;
import com.allintask.lingdao.ui.activity.BaseSwipeRefreshActivity;
import com.allintask.lingdao.ui.activity.service.UploadAlbumActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.adapter.user.MyPhotoAlbumAdapter;
import com.allintask.lingdao.utils.TemporaryDataCachePool;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IMyPhotoAlbumView;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/2/14.
 */

public class MyPhotoAlbumActivity extends BaseSwipeRefreshActivity<IMyPhotoAlbumView, MyPhotoAlbumPresenter> implements IMyPhotoAlbumView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_right_first)
    TextView rightFirstTv;
    @BindView(R.id.ll_header)
    LinearLayout headerLL;
    @BindView(R.id.tv_date)
    TextView dateTv;

    private int userId;

    private MyPhotoAlbumAdapter myPhotoAlbumAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_photo_album;
    }

    @Override
    protected MyPhotoAlbumPresenter CreatePresenter() {
        return new MyPhotoAlbumPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            userId = intent.getIntExtra(CommonConstant.EXTRA_USER_ID, -1);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.my_photo_album));

        int myUserId = UserPreferences.getInstance().getUserId();

        if (userId == myUserId) {
            rightFirstTv.setText(getString(R.string.add));
            rightFirstTv.setVisibility(View.VISIBLE);
        } else {
            rightFirstTv.setVisibility(View.GONE);
        }

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        if (null != mSwipeRefreshStatusLayout) {
            View emptyView = mSwipeRefreshStatusLayout.getEmptyView();

            TextView contentTv = (TextView) emptyView.findViewById(R.id.tv_content);
            contentTv.setText(getString(R.string.my_photo_album_no_data));

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

        myPhotoAlbumAdapter = new MyPhotoAlbumAdapter(getParentContext());
        recycler_view.setAdapter(myPhotoAlbumAdapter);
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recycler_view.getLayoutManager();

                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (null != myPhotoAlbumAdapter) {
                        MyPhotoAlbumListBean.MyPhotoAlbumBean myPhotoAlbumBean = (MyPhotoAlbumListBean.MyPhotoAlbumBean) myPhotoAlbumAdapter.getItem(firstVisibleItemPosition);

                        if (null != myPhotoAlbumBean) {
                            Date createAt = myPhotoAlbumBean.createAt;

                            if (null != createAt) {
                                String createAtStr = CommonConstant.commonDateFormat.format(createAt);
                                dateTv.setText(createAtStr);
                            }
                        }
                    }
                }
            }
        });

        myPhotoAlbumAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyPhotoAlbumListBean.MyPhotoAlbumBean myPhotoAlbumBean = (MyPhotoAlbumListBean.MyPhotoAlbumBean) myPhotoAlbumAdapter.getItem(position);

                if (null != myPhotoAlbumBean) {
                    int albumId = TypeUtils.getInteger(myPhotoAlbumBean.albumId, -1);
                    List<MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean> imageList = myPhotoAlbumBean.images;
                    TemporaryDataCachePool.getInstance().setImageList(imageList);

                    Intent intent = new Intent(getParentContext(), PhotoAlbumActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                    intent.putExtra(CommonConstant.EXTRA_ALBUM_ID, albumId);
                    startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                }
            }
        });
    }

    private void initData() {
        mPresenter.refresh(userId);
    }

    @OnClick({R.id.tv_right_first})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right_first:
                Intent intent = new Intent(getParentContext(), UploadAlbumActivity.class);
                intent.putExtra(CommonConstant.EXTRA_UPLOAD_ALBUM_TYPE, CommonConstant.UPLOAD_ALBUM_PERSONAL);
                startActivityForResult(intent, CommonConstant.REQUEST_CODE);
                break;
        }
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
    public void onShowMyPhotoAlbumList(List<MyPhotoAlbumListBean.MyPhotoAlbumBean> myPhotoAlbumList) {
        if (null != myPhotoAlbumAdapter) {
            if (null != myPhotoAlbumList && myPhotoAlbumList.size() > 0) {
                myPhotoAlbumAdapter.setDateList(myPhotoAlbumList);

                MyPhotoAlbumListBean.MyPhotoAlbumBean myPhotoAlbumBean = (MyPhotoAlbumListBean.MyPhotoAlbumBean) myPhotoAlbumAdapter.getItem(0);

                if (null != myPhotoAlbumBean) {
                    Date createAt = myPhotoAlbumBean.createAt;

                    if (null != createAt) {
                        String createAtStr = CommonConstant.commonDateFormat.format(createAt);
                        dateTv.setText(createAtStr);

                        headerLL.setVisibility(View.VISIBLE);
                    }
                }

                showContentView();
            } else {
                headerLL.setVisibility(View.GONE);
                showEmptyView();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.REFRESH_RESULT_CODE) {
            mPresenter.refresh(userId);
        }
    }

}
