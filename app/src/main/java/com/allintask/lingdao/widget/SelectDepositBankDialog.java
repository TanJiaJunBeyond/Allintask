package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.DepositBankBean;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.adapter.user.SelectDepositBankAdapter;
import com.allintask.lingdao.utils.WindowUtils;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/31.
 */

public class SelectDepositBankDialog extends Dialog {

    private List<DepositBankBean> depositBankList;

    private LinearLayout selectDepositBankLL;
    private CommonRecyclerView recyclerView;

    private OnClickListener onClickListener;

    public SelectDepositBankDialog(@NonNull Context context, List<DepositBankBean> depositBankList) {
        super(context, R.style.SelectDepositBankDialogStyle);
        this.depositBankList = depositBankList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()), -2);
        View contentView = View.inflate(getContext(), R.layout.dialog_select_deposit_bank, null);
        setContentView(contentView, layoutParams);

        selectDepositBankLL = (LinearLayout) contentView.findViewById(R.id.ll_select_deposit_bank);
        recyclerView = (CommonRecyclerView) contentView.findViewById(R.id.recycler_view);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, 1);
            }
        });

        final SelectDepositBankAdapter selectDepositBankAdapter = new SelectDepositBankAdapter(getContext());
        recyclerView.setAdapter(selectDepositBankAdapter);

        selectDepositBankAdapter.setDateList(depositBankList);
        setSelectDepositBankLinearLayoutHeight();

        selectDepositBankAdapter.setOnClickListener(new SelectDepositBankAdapter.OnClickListener() {
            @Override
            public void onItemClickView(int position) {
                DepositBankBean depositBankBean = (DepositBankBean) selectDepositBankAdapter.getItem(position);

                if (null != depositBankBean) {
                    String code = TypeUtils.getString(depositBankBean.code, "");
                    String value = TypeUtils.getString(depositBankBean.value, "");

                    if (null != onClickListener) {
                        onClickListener.onItemClick(code, value);
                    }
                }
            }
        });
    }

    private void setSelectDepositBankLinearLayoutHeight() {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(WindowUtils.getScreenWidth(getContext()), View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        recyclerView.measure(widthMeasureSpec, heightMeasureSpec);

        int recyclerViewHeight = recyclerView.getMeasuredHeight();

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) selectDepositBankLL.getLayoutParams();

        if (recyclerViewHeight > WindowUtils.getScreenHeight(getContext()) / 2) {
            layoutParams.height = WindowUtils.getScreenHeight(getContext()) / 2;
        } else {
            layoutParams.height = recyclerViewHeight + DensityUtils.dip2px(getContext(), 50);
        }

        selectDepositBankLL.setLayoutParams(layoutParams);
    }

    public interface OnClickListener {

        void onItemClick(String code, String value);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
