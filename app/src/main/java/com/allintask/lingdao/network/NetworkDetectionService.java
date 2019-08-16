package com.allintask.lingdao.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.allintask.lingdao.R;

import java.text.DecimalFormat;

public class NetworkDetectionService extends Service {

    private LayoutParams windowManagerLayoutParams;

    private WindowManager windowManager;

    private View panelView;

    private TextView panel_sending_speed_tv;
    private TextView panel_receiving_speed_tv;

    private TextView panel_total_time_tv;
    private TextView panel_idle_time_tv;
    private TextView panel_network_use_ratio_tv;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManagerLayoutParams = new LayoutParams();
        windowManagerLayoutParams.type = LayoutParams.TYPE_PHONE; // 设置window type
        windowManagerLayoutParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         */
        windowManagerLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;

        windowManagerLayoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        windowManagerLayoutParams.x = 0;
        windowManagerLayoutParams.y = 0;

        windowManagerLayoutParams.width = LayoutParams.WRAP_CONTENT;
        windowManagerLayoutParams.height = LayoutParams.WRAP_CONTENT;

        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        panelView = View.inflate(this, R.layout.panel, null);

        panel_sending_speed_tv = (TextView) panelView.findViewById(R.id.panel_sending_speed_tv);
        panel_receiving_speed_tv = (TextView) panelView.findViewById(R.id.panel_receiving_speed_tv);
        panel_total_time_tv = (TextView) panelView.findViewById(R.id.panel_total_time_tv);
        panel_idle_time_tv = (TextView) panelView.findViewById(R.id.panel_idle_time_tv);
        panel_network_use_ratio_tv = (TextView) panelView.findViewById(R.id.panel_network_use_ratio_tv);

        windowManager.addView(panelView, windowManagerLayoutParams);

        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        NetworkTrafficDetector.getInstance().setNetworkTrafficStatusListener(new NetworkTrafficDetector.INetworkTrafficStatusListener() {

            @Override
            public void onDetectSpeed(int receivingSpeed, int sendingSpeed) {
                panel_receiving_speed_tv.setText(Integer.toString(receivingSpeed));
                panel_sending_speed_tv.setText(Integer.toString(sendingSpeed));
            }

            @Override
            public void onDetectTime(int totalTime, int idleTime) {
                panel_total_time_tv.setText(Integer.toString(totalTime));
                panel_idle_time_tv.setText(Integer.toString(idleTime));
                panel_network_use_ratio_tv.setText(df.format((totalTime - idleTime) * 100.00 / totalTime));
            }
        });
        NetworkTrafficDetector.getInstance().detect();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != windowManager && null != panelView) {
            windowManager.removeView(panelView);
        }
        NetworkTrafficDetector.getInstance().terminate();
    }

}
