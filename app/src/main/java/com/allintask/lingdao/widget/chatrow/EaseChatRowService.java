package com.allintask.lingdao.widget.chatrow;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.hyphenate.chat.EMMessage;

public class EaseChatRowService extends EaseChatRow {

    private TextView serviceCategoryTv;
    private TextView serviceWayTv;
    private TextView serviceModeAndPriceTv;
    private TextView advantageTv;

    public EaseChatRowService(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.ease_row_service, this);
    }

    @Override
    protected void onFindViewById() {
        serviceCategoryTv = (TextView) findViewById(R.id.tv_service_category);
        serviceWayTv = (TextView) findViewById(R.id.tv_service_way);
        serviceModeAndPriceTv = (TextView) findViewById(R.id.tv_service_price_and_unit);
        advantageTv = (TextView) findViewById(R.id.tv_advantage);
    }

    @Override
    public void onSetUpView() {
        String serviceCategory = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVE_TYPE, "");
        String serviceWay = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVE_SERVICE_MODE, "");
        String serviceModeAndPrice = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVE_SERVICE_PRICE, "");
        String advantage = message.getStringAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVE_INTRO, "");

        serviceCategoryTv.setText(serviceCategory);
        serviceWayTv.setText(serviceWay);
        serviceModeAndPriceTv.setText(serviceModeAndPrice);
        advantageTv.setText(advantage);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {

    }

}
