package com.allintask.lingdao.ui.adapter.service;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.MyServiceListBean;
import com.allintask.lingdao.bean.service.MyServiceModeAndPriceUnitBean;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public class MyServiceAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private List<GetIdToChineseListBean.GetIdToChineseBean> categoryList;
    private List<GetIdToChineseListBean.GetIdToChineseBean> priceUnitList;
    private List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList;

    private OnClickListener onClickListener;

    public MyServiceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_service, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, final int position) {
        MyServiceListBean.MyServiceBean myServiceBean = (MyServiceListBean.MyServiceBean) getItem(position);

        if (null != myServiceBean) {
            LinearLayout serviceModeAndPriceUnitLL = holder.getChildView(R.id.ll_service_mode_and_price_unit);
            TextView statusTv = holder.getChildView(R.id.tv_status);
            Button deleteBtn = holder.getChildView(R.id.btn_delete);
            Button onlineBtn = holder.getChildView(R.id.btn_online);
            Button offlineBtn = holder.getChildView(R.id.btn_offline);
            Button compileBtn = holder.getChildView(R.id.btn_compile);

            final int serviceId = TypeUtils.getInteger(myServiceBean.id, -1);
            final int categoryId = TypeUtils.getInteger(myServiceBean.categoryId, -1);
            String serveWayPriceUnitListStr = TypeUtils.getString(myServiceBean.serveWayPriceUnitList, "");
            int auditCode = TypeUtils.getInteger(myServiceBean.auditCode, 0);
            int onOffLine = TypeUtils.getInteger(myServiceBean.onOffLine, 0);
            Date changeAt = myServiceBean.changeAt;
            int consummate = TypeUtils.getInteger(myServiceBean.consummate, -1);
            Integer serveAlbumAmount = myServiceBean.serveAlbumAmount;

            if (null != categoryList && categoryList.size() > 0) {
                for (int i = 0; i < categoryList.size(); i++) {
                    GetIdToChineseListBean.GetIdToChineseBean serviceGetIdToChineseBean = categoryList.get(i);

                    if (null != serviceGetIdToChineseBean) {
                        int code = TypeUtils.getInteger(serviceGetIdToChineseBean.code, 0);
                        String value = TypeUtils.getString(serviceGetIdToChineseBean.value, "");

                        if (code == categoryId) {
                            holder.setTextView(R.id.tv_category, value);
                        }
                    }
                }
            }

            if (!TextUtils.isEmpty(serveWayPriceUnitListStr)) {
                List<MyServiceModeAndPriceUnitBean> myServiceModeAndPriceUnitList = new ArrayList<>();
                String[] serviceModeAndPriceUnitArray = serveWayPriceUnitListStr.split(";");

                for (int i = 0; i < serviceModeAndPriceUnitArray.length; i++) {
                    String firstStr = serviceModeAndPriceUnitArray[i];
                    String[] firstArray = firstStr.split(":");

                    for (int j = 0; j < firstArray.length; j++) {
                        String secondStr = firstArray[j];
                        int second = Integer.valueOf(secondStr);

                        if (j == 0) {
                            MyServiceModeAndPriceUnitBean myServiceModeAndPriceUnitBean = new MyServiceModeAndPriceUnitBean();

                            for (int k = 0; k < serviceModeList.size(); k++) {
                                GetIdToChineseListBean.GetIdToChineseBean serviceGetIdToChineseBean = serviceModeList.get(k);

                                if (null != serviceGetIdToChineseBean) {
                                    int code = TypeUtils.getInteger(serviceGetIdToChineseBean.code, -1);
                                    String value = TypeUtils.getString(serviceGetIdToChineseBean.value, "");

                                    if (code == second) {
                                        myServiceModeAndPriceUnitBean.name = value;
                                        myServiceModeAndPriceUnitList.add(myServiceModeAndPriceUnitBean);
                                    }
                                }
                            }
                        } else if (j == 1) {
                            MyServiceModeAndPriceUnitBean myServiceModeAndPriceUnitBean = myServiceModeAndPriceUnitList.get(i);

                            for (int k = 0; k < priceUnitList.size(); k++) {
                                GetIdToChineseListBean.GetIdToChineseBean serviceGetIdToChineseBean = priceUnitList.get(k);

                                if (null != serviceGetIdToChineseBean) {
                                    int code = TypeUtils.getInteger(serviceGetIdToChineseBean.code, -1);
                                    String value = TypeUtils.getString(serviceGetIdToChineseBean.value, "");

                                    if (code == second) {
                                        myServiceModeAndPriceUnitBean.unit = value;
                                    }
                                }
                            }
                        } else if (j == 2) {
                            MyServiceModeAndPriceUnitBean myServiceModeAndPriceUnitBean = myServiceModeAndPriceUnitList.get(i);
                            myServiceModeAndPriceUnitBean.price = secondStr;
                        }
                    }
                }

                serviceModeAndPriceUnitLL.removeAllViews();

                for (int i = 0; i < myServiceModeAndPriceUnitList.size(); i++) {
                    MyServiceModeAndPriceUnitBean myServiceModeAndPriceUnitBean = myServiceModeAndPriceUnitList.get(i);

                    if (null != myServiceModeAndPriceUnitBean) {
                        String name = TypeUtils.getString(myServiceModeAndPriceUnitBean.name, "");
                        String price = TypeUtils.getString(myServiceModeAndPriceUnitBean.price, "");
                        String unit = TypeUtils.getString(myServiceModeAndPriceUnitBean.unit, "");

                        View view = LayoutInflater.from(context).inflate(R.layout.item_my_service_service_mode_and_price_unit, null);

                        TextView titleTv = (TextView) view.findViewById(R.id.tv_title);
                        TextView contentTv = (TextView) view.findViewById(R.id.tv_content);

                        titleTv.setText(name);

                        String content = price + unit;
                        contentTv.setText(content);

                        serviceModeAndPriceUnitLL.addView(view);
                    }
                }
            }

            if (null == serveAlbumAmount) {
                holder.setTextView(R.id.tv_service_pictures, "0张");
            } else {
                holder.setTextView(R.id.tv_service_pictures, String.valueOf(serveAlbumAmount) + "张");
            }

            if (auditCode == 2) {
                statusTv.setTextColor(context.getResources().getColor(R.color.text_red));
                statusTv.setText(context.getString(R.string.audit_failure));

                onlineBtn.setVisibility(View.GONE);
                offlineBtn.setVisibility(View.GONE);
            } else {
                if (consummate == 0) {
                    statusTv.setTextColor(context.getResources().getColor(R.color.text_red));
                    statusTv.setText(context.getString(R.string.remain_to_be_improved));

                    onlineBtn.setVisibility(View.GONE);
                    offlineBtn.setVisibility(View.GONE);
                } else {
                    if (onOffLine == 0) {
                        statusTv.setTextColor(context.getResources().getColor(R.color.text_dark_gray));
                        statusTv.setText(context.getString(R.string.offline));

                        onlineBtn.setVisibility(View.VISIBLE);
                        offlineBtn.setVisibility(View.GONE);
                    } else {
                        statusTv.setTextColor(context.getResources().getColor(R.color.text_green));
                        statusTv.setText(context.getString(R.string.online));

                        onlineBtn.setVisibility(View.GONE);
                        offlineBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            String date = CommonConstant.commonTimeFormat.format(changeAt);
            holder.setTextView(R.id.tv_date, "更新：" + date);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onDeleteClickListener(position, serviceId);
                    }
                }
            });

            onlineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onGoOnlineClickListener(position, serviceId);
                    }
                }
            });

            offlineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onGoOfflineClickListener(position, serviceId);
                    }
                }
            });

            compileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onCompileClickListener(position, serviceId, categoryId);
                    }
                }
            });
        }
    }

    public void setCategoryList(List<GetIdToChineseListBean.GetIdToChineseBean> categoryList) {
        this.categoryList = categoryList;
    }

    public void setPriceUnitList(List<GetIdToChineseListBean.GetIdToChineseBean> priceUnitList) {
        this.priceUnitList = priceUnitList;
    }

    public void setServiceModeList(List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList) {
        this.serviceModeList = serviceModeList;
    }

    public void goOnlineService(int position) {
        MyServiceListBean.MyServiceBean myServiceBean = (MyServiceListBean.MyServiceBean) getItem(position);
        myServiceBean.onOffLine = 1;
        notifyItemChanged(position);
    }

    public void goOfflineService(int position) {
        MyServiceListBean.MyServiceBean myServiceBean = (MyServiceListBean.MyServiceBean) getItem(position);
        myServiceBean.onOffLine = 0;
        notifyItemChanged(position);
    }

    public interface OnClickListener {

        void onDeleteClickListener(int position, int serviceId);

        void onGoOnlineClickListener(int position, int serviceId);

        void onGoOfflineClickListener(int position, int serviceId);

        void onCompileClickListener(int position, int serviceId, int categoryId);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
