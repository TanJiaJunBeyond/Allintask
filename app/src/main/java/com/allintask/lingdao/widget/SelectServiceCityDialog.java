package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.adapter.user.CityAdapter;
import com.allintask.lingdao.ui.adapter.user.ProvinceAdapter;
import com.allintask.lingdao.utils.WindowUtils;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/4/11.
 */

public class SelectServiceCityDialog extends Dialog implements View.OnClickListener {

    private List<IsAllBean> addressList;

    private LinearLayout selectServiceCityLL;
    private CommonRecyclerView provinceCRV;
    private CommonRecyclerView cityCRV;

    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    private OnClickListener onClickListener;

    public SelectServiceCityDialog(@NonNull Context context, ProvinceAdapter provinceAdapter, CityAdapter cityAdapter, List<IsAllBean> addressList) {
        super(context, R.style.SelectServiceCityDialogStyle);
        this.provinceAdapter = provinceAdapter;
        this.cityAdapter = cityAdapter;
        this.addressList = addressList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()), -2);
        View contentView = View.inflate(getContext(), R.layout.dialog_select_service_city, null);
        setContentView(contentView, layoutParams);

        selectServiceCityLL = (LinearLayout) contentView.findViewById(R.id.ll_select_service_city);
        ImageView closeIv = (ImageView) contentView.findViewById(R.id.iv_close);
        TextView confirmTv = (TextView) contentView.findViewById(R.id.tv_confirm);
        provinceCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_province);
        cityCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_city);

        provinceCRV.setAdapter(provinceAdapter);

        provinceAdapter.setDateList(addressList);
        provinceAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int itemCount = provinceAdapter.getItemCount();

                for (int i = 0; i < itemCount; i++) {
                    IsAllBean isAllBean = (IsAllBean) provinceAdapter.getItem(i);

                    if (null != isAllBean && i == position) {
                        isAllBean.isSelected = true;
                    } else {
                        isAllBean.isSelected = false;
                    }
                }

                if (null != provinceAdapter) {
                    provinceAdapter.notifyDataSetChanged();
                }

                IsAllBean isAllBean = (IsAllBean) provinceAdapter.getItem(position);

                if (null != isAllBean) {
                    List<AddressSubBean> cityList = isAllBean.sub;

                    if (null != cityList && cityList.size() > 0) {
                        if (null != cityAdapter) {
                            cityAdapter.setDateList(cityList);
                        }
                    }
                }
            }
        });

        cityCRV.setAdapter(cityAdapter);

        cityAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int itemCount = cityAdapter.getItemCount();

                for (int i = 0; i < itemCount; i++) {
                    AddressSubBean addressSubBean = (AddressSubBean) cityAdapter.getItem(i);

                    if (null != addressSubBean && i == position) {
                        addressSubBean.isSelected = true;
                    } else {
                        addressSubBean.isSelected = false;
                    }
                }

                if (null != cityAdapter) {
                    cityAdapter.notifyDataSetChanged();
                }
            }
        });

        setSelectServiceCityLinearLayoutHeight();

        closeIv.setOnClickListener(this);
        confirmTv.setOnClickListener(this);
    }

    private void setSelectServiceCityLinearLayoutHeight() {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(WindowUtils.getScreenWidth(getContext()), View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        provinceCRV.measure(widthMeasureSpec, heightMeasureSpec);

        int provinceRecyclerViewHeight = provinceCRV.getMeasuredHeight();

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) selectServiceCityLL.getLayoutParams();

        if (provinceRecyclerViewHeight > WindowUtils.getScreenHeight(getContext()) / 2) {
            layoutParams.height = WindowUtils.getScreenHeight(getContext()) / 2;
        } else {
            layoutParams.height = provinceRecyclerViewHeight + DensityUtils.dip2px(getContext(), 50);
        }

        selectServiceCityLL.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (isShowing()) {
                    dismiss();
                }
                break;

            case R.id.tv_confirm:
                if (null != onClickListener) {
                    if (null != addressList && addressList.size() > 0) {
                        String provinceCode = null;
                        String province = null;
                        String cityCode = null;
                        String city = null;

                        for (int i = 0; i < addressList.size(); i++) {
                            IsAllBean isAllBean = addressList.get(i);

                            if (null != isAllBean) {
                                String tempProvinceCode = TypeUtils.getString(isAllBean.code, "");
                                String tempProvince = TypeUtils.getString(isAllBean.name, "");
                                List<AddressSubBean> cityList = isAllBean.sub;
                                boolean isProvinceSelected = TypeUtils.getBoolean(isAllBean.isSelected, false);

                                if (isProvinceSelected) {
                                    provinceCode = tempProvinceCode;
                                    province = tempProvince;

                                    if (null != cityList && cityList.size() > 0) {
                                        for (int j = 0; j < cityList.size(); j++) {
                                            AddressSubBean addressSubBean = cityList.get(j);

                                            if (null != addressSubBean) {
                                                String tempCityCode = TypeUtils.getString(addressSubBean.code, "");
                                                String tempCity = TypeUtils.getString(addressSubBean.name, "");
                                                boolean isCitySelected = TypeUtils.getBoolean(addressSubBean.isSelected, false);

                                                if (isCitySelected) {
                                                    cityCode = tempCityCode;
                                                    city = tempCity;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!TextUtils.isEmpty(province)) {
                            if (!TextUtils.isEmpty(city)) {
                                String address = province + "  " + city;
                                onClickListener.onConfirmClick(addressList, provinceCode, cityCode, address);
                            } else {
                                Toast.makeText(getContext(), "请选择城市", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "请选择省份", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    public interface OnClickListener {

        void onConfirmClick(List<IsAllBean> addressList, String provinceCode, String cityCode, String address);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
