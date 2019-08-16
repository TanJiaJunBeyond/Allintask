package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.ArbitramentReasonBean;
import com.allintask.lingdao.ui.adapter.demand.ArbitramentReasonAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.utils.WindowUtils;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2018/3/20.
 */

public class SelectArbitramentReasonDialog extends Dialog implements View.OnClickListener {

    private List<ArbitramentReasonBean> arbitramentReasonList;

    private OnClickListener onClickListener;

    public SelectArbitramentReasonDialog(Context context, List<ArbitramentReasonBean> arbitramentReasonList) {
        super(context, R.style.SelectArbitramentDialogStyle);
        this.arbitramentReasonList = arbitramentReasonList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()), -2);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_arbitrament_reason, null);
        setContentView(view, layoutParams);

        ImageView closeIv = (ImageView) view.findViewById(R.id.iv_close);
        CommonRecyclerView recyclerView = (CommonRecyclerView) view.findViewById(R.id.recycler_view);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, DensityUtils.dip2px(getContext(), 0.5F));
            }
        });

        ArbitramentReasonAdapter arbitramentReasonAdapter = new ArbitramentReasonAdapter(getContext());
        recyclerView.setAdapter(arbitramentReasonAdapter);

        if (null != arbitramentReasonList && arbitramentReasonList.size() > 0) {
            arbitramentReasonAdapter.setDateList(arbitramentReasonList);
        }

        arbitramentReasonAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (null != onClickListener) {
                    onClickListener.onItemClick(position);
                }
            }
        });

        closeIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (isShowing()) {
                    dismiss();
                }
                break;
        }
    }

    public interface OnClickListener {

        void onItemClick(int position);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
