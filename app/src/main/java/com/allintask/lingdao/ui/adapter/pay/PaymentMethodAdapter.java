package com.allintask.lingdao.ui.adapter.pay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.pay.PaymentMethodBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/9.
 */

public class PaymentMethodAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private boolean isBind = false;
    private int checkedPosition = -1;
    private OnClickListener onClickListener;

    public PaymentMethodAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, final int position) {
        PaymentMethodBean paymentMethodBean = (PaymentMethodBean) getItem(position);

        if (null != paymentMethodBean) {
            RelativeLayout paymentMethodLL = holder.getChildView(R.id.rl_payment_method);
            ImageView paymentMethodIv = holder.getChildView(R.id.iv_payment_method);
            CheckBox selectCB = holder.getChildView(R.id.cb_select);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WindowUtils.getScreenWidth(context), LinearLayout.LayoutParams.WRAP_CONTENT);
            paymentMethodLL.setLayoutParams(layoutParams);

            final String code = TypeUtils.getString(paymentMethodBean.code, "");
            String value = TypeUtils.getString(paymentMethodBean.value, "");
            final boolean isSelected = TypeUtils.getBoolean(paymentMethodBean.isSelected, false);

            if (code.equals("ld")) {
                paymentMethodIv.setBackgroundResource(R.mipmap.ic_allintask_pay);
            } else if (code.equals("zfb")) {
                paymentMethodIv.setBackgroundResource(R.mipmap.ic_alipay);
            } else if (code.equals("wx")) {
                paymentMethodIv.setBackgroundResource(R.mipmap.ic_wechat_pay);
            } else if (code.equals("withholdTrusteeship")) {
                paymentMethodIv.setBackgroundResource(R.mipmap.ic_withhold_trusteeship);
            }

            holder.setTextView(R.id.tv_payment_method, value);

            isBind = true;
            selectCB.setChecked(isSelected);
            isBind = false;

            selectCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkedPosition = position;
                    } else if (checkedPosition == position) {
                        checkedPosition = -1;
                    }

                    for (int i = 0; i < mList.size(); i++) {
                        PaymentMethodBean tempPaymentMethodBean = (PaymentMethodBean) mList.get(i);

                        if (null != tempPaymentMethodBean) {
                            if (i == checkedPosition) {
                                tempPaymentMethodBean.isSelected = true;
                            } else {
                                tempPaymentMethodBean.isSelected = false;
                            }
                        }
                    }

                    if (!isBind) {
                        notifyDataSetChanged();
                    }

                    if (null != onClickListener) {
                        if (checkedPosition == -1) {
                            onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_DEFAULT);
                        } else {
                            PaymentMethodBean tempPaymentMethodBean = (PaymentMethodBean) mList.get(checkedPosition);

                            if (null != tempPaymentMethodBean) {
                                String code = TypeUtils.getString(tempPaymentMethodBean.code, "");

                                if (code.equals("ld")) {
                                    onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_ALLINTASK_ACCOUNT);
                                } else if (code.equals("zfb")) {
                                    onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_ALIPAY);
                                } else if (code.equals("wx")) {
                                    onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_WECHAT_PAY);
                                } else if (code.equals("withholdTrusteeship")) {
                                    onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_WITHHOLD_TRUSTEESHIP);
                                }
                            }
                        }
                    }
                }
            });

            paymentMethodLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelected) {
                        checkedPosition = -1;
                    } else {
                        checkedPosition = position;
                    }

                    for (int i = 0; i < mList.size(); i++) {
                        PaymentMethodBean tempPaymentMethodBean = (PaymentMethodBean) mList.get(i);

                        if (null != tempPaymentMethodBean) {
                            if (i == checkedPosition) {
                                tempPaymentMethodBean.isSelected = true;
                            } else {
                                tempPaymentMethodBean.isSelected = false;
                            }
                        }
                    }

                    if (!isBind) {
                        notifyDataSetChanged();
                    }

                    if (null != onClickListener) {
                        if (checkedPosition == -1) {
                            onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_DEFAULT);
                        } else {
                            PaymentMethodBean tempPaymentMethodBean = (PaymentMethodBean) mList.get(checkedPosition);

                            if (null != tempPaymentMethodBean) {
                                String code = TypeUtils.getString(tempPaymentMethodBean.code, "");

                                if (code.equals("ld")) {
                                    onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_ALLINTASK_ACCOUNT);
                                } else if (code.equals("zfb")) {
                                    onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_ALIPAY);
                                } else if (code.equals("wx")) {
                                    onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_WECHAT_PAY);
                                } else if (code.equals("withholdTrusteeship")) {
                                    onClickListener.onPaymentMethodSelected(CommonConstant.PAYMENT_METHOD_WITHHOLD_TRUSTEESHIP);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public interface OnClickListener {

        void onPaymentMethodSelected(int selectedPaymentMethod);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
