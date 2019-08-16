package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.DepositBankBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/31.
 */

public class SelectDepositBankAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private OnClickListener onClickListener;

    public SelectDepositBankAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_deposit_bank, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, final int position) {
        DepositBankBean depositBankBean = (DepositBankBean) getItem(position);

        if (null != depositBankBean) {
            RelativeLayout selectDepositBankRL = holder.getChildView(R.id.rl_select_deposit_bank);

            String value = TypeUtils.getString(depositBankBean.value, "");
            holder.setTextView(R.id.tv_deposit_bank, value, true);

            selectDepositBankRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onItemClickView(position);
                    }
                }
            });
        }
    }

    public interface OnClickListener {

        void onItemClickView(int position);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
