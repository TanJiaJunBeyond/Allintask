package com.allintask.lingdao.utils;

import android.os.Handler;
import android.os.Message;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by TanJiaJun on 2016/10/27.
 */

public class TimerMaker {

    private long delay = 1000;
    private long period = 1000;
    private long duration = 60 * 1000;

    private int what;
    private Timer timer;
    private OnTimerCallback callback;

    private Handler timerHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (what == msg.what) {
                long obj = (long) msg.obj;
                callback.onReceived(obj);
            }
        }
    };

    /**
     * 定时器构造函数
     *
     * @param callback 回调函数
     */
    public TimerMaker(OnTimerCallback callback) {
        this.callback = callback;

        Random random = new Random();
        what = random.nextInt(1000);
    }

    /**
     * 定时器构造函数
     *
     * @param period   间隔时间 (单位: 毫秒)
     * @param duration 总时间 (单位: 毫秒)
     * @param callback 回调函数
     */
    public TimerMaker(long period, long duration, OnTimerCallback callback) {
        this.period = period;
        this.duration = duration;
        this.callback = callback;

        Random random = new Random();
        what = random.nextInt(1000);
    }

    /**
     * 定时器构造函数
     *
     * @param delay    启动延迟时间 (单位: 毫秒)
     * @param period   间隔时间 (单位: 毫秒)
     * @param duration 总时间 (单位: 毫秒)
     * @param callback 回调函数
     */
    public TimerMaker(long delay, long period, long duration, OnTimerCallback callback) {
        this.delay = delay;
        this.period = period;
        this.duration = duration;
        this.callback = callback;

        Random random = new Random();
        what = random.nextInt(1000);
    }

    /**
     * 启动
     */
    public void start() {
        if (delay < 0) {
            callback.onError("启动的延迟时间为负值，请检查是否正确");
            return;
        }

        if (period < 0) {
            callback.onError("间隔时间为负值，请检查是否正确");
            return;
        }

        if (duration < 0) {
            callback.onError("总时间为负值，请检查是否正确");
            return;
        }

        if (null == timer) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {

            long remainingMillis = duration;

            @Override
            public void run() {
                remainingMillis -= period;

                Message msg = Message.obtain();
                msg.what = what;
                msg.obj = remainingMillis;

                if (null != timerHandler) {
                    timerHandler.sendMessage(msg);
                }
            }
        }, delay, period);
    }

    public void stop() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 回收
     */
    public void recycle() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }

        if (timerHandler.hasMessages(what)) {
            timerHandler.removeMessages(what);
            timerHandler = null;
        }
    }


    public interface OnTimerCallback {

        void onReceived(long remainingMillis);

        void onError(String error);

    }

}
