package com.allintask.lingdao.ui.activity.user;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.user.AboutUsPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.user.AboutUsAdapter;
import com.allintask.lingdao.view.user.IAboutUsView;
import com.allintask.lingdao.widget.CommonRecyclerView;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class AboutUsActivity extends BaseActivity<IAboutUsView, AboutUsPresenter> implements IAboutUsView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;

    private AboutUsAdapter aboutUsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected AboutUsPresenter CreatePresenter() {
        return new AboutUsPresenter();
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

        titleTv.setText(getString(R.string.about_us));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, DensityUtils.dip2px(getParentContext(), 1));
            }
        });

        aboutUsAdapter = new AboutUsAdapter(getParentContext());
        recyclerView.setAdapter(aboutUsAdapter);

        showContentView();
    }

}
