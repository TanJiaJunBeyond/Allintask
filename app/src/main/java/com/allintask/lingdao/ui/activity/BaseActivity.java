package com.allintask.lingdao.ui.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.demand.SelectServiceActivity;
import com.allintask.lingdao.ui.activity.main.GuideActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.main.SplashActivity;
import com.allintask.lingdao.ui.activity.user.MyAccountActivity;
import com.allintask.lingdao.ui.activity.user.SelectSkillsActivity;
import com.allintask.lingdao.utils.SystemBarTintManager;
import com.allintask.lingdao.view.IBaseView;
import com.allintask.lingdao.widget.MultiStatusView;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 父类Activity
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public abstract class BaseActivity<V extends IBaseView, T extends BasePresenter<V>> extends AppCompatActivity implements IBaseView {

    @Nullable
    @BindView(R.id.swipe_refresh_status_layout)
    protected MultiStatusView mSwipeRefreshStatusLayout;

    protected ProgressDialog baseProgressDialog;

    protected T mPresenter;
    private boolean isStatusBarFloat = false;
    private SystemBarTintManager systemBarTintManager;

    private Unbinder unbinder;
    private Toast toast;

    private boolean isActive = true;
    private boolean isPrepareCreateNewProcess = false;

    private boolean activityStatus = true;

    private boolean isOpenGesturePassword = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(getParentContext() instanceof SplashActivity)) {
            AllintaskApplication.IS_STARTED = true;
        }

        setContentView(this.getLayoutResId());
        unbinder = ButterKnife.bind(this);
        mPresenter = CreatePresenter();

        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }

        if (!(getParentContext() instanceof GuideActivity) || !(getParentContext() instanceof MyAccountActivity) || !(getParentContext() instanceof SelectSkillsActivity) || !(getParentContext() instanceof SelectServiceActivity)) {
            setStatusBarFloatable(true);
            setStatusBarColor(android.R.color.white);

            MIUISetStatusBarLightMode(getWindow(), true);
            FlymeSetStatusBarLightMode(getWindow(), true);
        }

//        if (CommonConstant.IS_OPEN_UMENG) {
//            MobclickAgent.setDebugMode(true);
//            // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//            // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
//            // MobclickAgent.openActivityDurationTrack(false);
//            // MobclickAgent.setAutoLocation(true);
//            // MobclickAgent.setSessionContinueMillis(1000);
//        }

        this.init();

        if (null != baseProgressDialog) {
            baseProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (null != mPresenter) {
                        mPresenter.cancelCall();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CommonConstant.IS_OPEN_UMENG) {
            MobclickAgent.onResume(this);
        }

//        if (activityStatus) {
//            isOpenGesturePassword = UserPreferences.getInstance().isOpenGesturePassword();
//
//            if (!isActive) {
//                // 从后台唤醒
//                isActive = true;
//
//                if (isOpenGesturePassword) {
//                    Intent intent = new Intent(this, UseGesturePasswordLoginActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (CommonConstant.IS_OPEN_UMENG) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (activityStatus) {
//            if (!isAppOnForeground()) {
//                isActive = false;
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    public void setActivityStatus(boolean activityStatus) {
        this.activityStatus = activityStatus;
    }

    public void setPrepareCreateNewProcess(boolean isPrepareCreateNewProcess) {
        this.isPrepareCreateNewProcess = isPrepareCreateNewProcess;
    }

    /**
     * 是否在后台
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();

        if (null != runningAppProcessInfoList) {
            String curPackageName = getApplicationContext().getPackageName();

            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfoList) {
                if (!isPrepareCreateNewProcess) {
                    if (runningAppProcessInfo.processName.equals(curPackageName) && runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    }
                } else {
                    if (runningAppProcessInfo.processName.equals(curPackageName) | runningAppProcessInfo.processName.equals(curPackageName + ":camera") && runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (activityStatus) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获得布局文件的Resource_ID
     *
     * @return
     */
    protected abstract int getLayoutResId();

    protected abstract T CreatePresenter();

    /**
     * 初始化数据、布局等
     */
    protected abstract void init();

    @Override
    public void showContentView() {
        showStatusView(MultiStatusView.STATUS_NORMAL);
    }

    @Override
    public void showLoadingView() {
        showStatusView(MultiStatusView.STATUS_LOADING);
    }

    @Override
    public void showErrorView() {
        showStatusView(MultiStatusView.STATUS_ERROR);
    }

    @Override
    public void showEmptyView() {
        showStatusView(MultiStatusView.STATUS_EMPTY);
    }

    @Override
    public void showNoNetworkView() {
        showStatusView(MultiStatusView.STATUS_NO_NETWORK);
    }

    private void showStatusView(int status) {
        if (null != mSwipeRefreshStatusLayout) {
            mSwipeRefreshStatusLayout.switchStatusView(status);
        }
    }

    @Override
    public void showProgressDialog(String msg) {
        if (null == baseProgressDialog) {
            baseProgressDialog = new ProgressDialog(this);
        }

        if (null != baseProgressDialog && !baseProgressDialog.isShowing()) {
            if (!TextUtils.isEmpty(msg)) {
                baseProgressDialog.setMessage(msg);
            } else {
                baseProgressDialog.setMessage("加载中");
            }

            baseProgressDialog.show();
        }
    }

    @Override
    public void showProgressDialog() {
        if (null == baseProgressDialog) {
            baseProgressDialog = new ProgressDialog(this);
        }

        if (null != baseProgressDialog && !baseProgressDialog.isShowing()) {
            baseProgressDialog.setMessage("加载中");
            baseProgressDialog.show();
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (null != baseProgressDialog && baseProgressDialog.isShowing()) {
            baseProgressDialog.dismiss();
        }
    }

    public ProgressDialog getProgressDialog() {
        return baseProgressDialog;
    }

    /*****
     * showToast
     *****/

    protected void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showLongToast(int resId) {
        showLongToast(getString(resId));
    }

    @Override
    public void showLongToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    private void showToast(CharSequence msg, int duration) {
        if (!TextUtils.isEmpty(msg)) {
            if (toast != null) {
                toast.cancel();
            }

            toast = Toast.makeText(this, msg, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    protected void setStatusBarFloatable(boolean isStatusBarFloat) {
        this.isStatusBarFloat = isStatusBarFloat;
        if (isStatusBarFloat) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //设置为悬浮透明
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    protected void setStatusBarColor(int color) {
        if (isStatusBarFloat) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //设置背景色
                if (systemBarTintManager == null) {
                    systemBarTintManager = new SystemBarTintManager(this);
                }

                systemBarTintManager.setStatusBarTintEnabled(true);
                systemBarTintManager.setStatusBarTintResource(color);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getResources().getColor(color));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(getResources().getColor(color));
            }
        }
    }

    /**
     * 小米手机状态栏颜色适配
     *
     * @param window
     * @param dark
     * @return
     */
    public boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;

        if (null != window) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);// 状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);// 清除黑色字体
                }
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 魅族手机状态栏颜色适配
     *
     * @param window
     * @param dark
     * @return
     */
    public boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Context getParentContext() {
        return this;
    }

}
