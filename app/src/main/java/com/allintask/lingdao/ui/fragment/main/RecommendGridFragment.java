package com.allintask.lingdao.ui.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendGridBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.user.SearchActivity;
import com.allintask.lingdao.ui.adapter.recommend.RecommendGridAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/7/2.
 */

public class RecommendGridFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private int lobbyStatus = CommonConstant.SERVICE_LOBBY;
    private List<RecommendGridBean> recommendGridList;

    private RecommendGridAdapter recommendGridAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_recommend_grid;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            lobbyStatus = bundle.getInt(CommonConstant.EXTRA_SEARCH_STATUS, CommonConstant.SERVICE_LOBBY);
            recommendGridList = bundle.getParcelableArrayList(CommonConstant.EXTRA_RECOMMEND_GRID_LIST);
        }

        initUI();
    }

    private void initUI() {
        recyclerView.setLayoutManager(new GridLayoutManager(getParentContext(), 4));

        recommendGridAdapter = new RecommendGridAdapter(getParentContext());
        recyclerView.setAdapter(recommendGridAdapter);

        recommendGridAdapter.setDateList(recommendGridList);
        recommendGridAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RecommendGridBean recommendGridBean = (RecommendGridBean) recommendGridAdapter.getItem(position);

                if (null != recommendGridBean) {
                    int categoryId = TypeUtils.getInteger(recommendGridBean.categoryId, -1);

                    Intent intent = new Intent(getParentContext(), SearchActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);
                    intent.putExtra(CommonConstant.EXTRA_CATEGORY_ID, categoryId);
                    startActivity(intent);
                }
            }
        });
    }

}
