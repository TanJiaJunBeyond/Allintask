package com.allintask.lingdao.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类：不使用MVP模式的常规界面
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public abstract class BaseNoPresenterActivity extends AppCompatActivity {

    private ProgressDialog baseProgressDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(this.getLayoutResId());
        unbinder = ButterKnife.bind(this);
        this.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    protected void showProgressDialog(String msg) {
        if (null == baseProgressDialog) {
            baseProgressDialog = new ProgressDialog(this);
        }

        if (null != baseProgressDialog && !baseProgressDialog.isShowing()) {
            if (!TextUtils.isEmpty(msg)) {
                baseProgressDialog.setMessage(msg);
            }
            baseProgressDialog.show();
        }
    }

    protected void dimissProgressDialog() {
        if (null != baseProgressDialog && baseProgressDialog.isShowing()) {
            baseProgressDialog.dismiss();
        }
    }

    /*****
     * showToast
     *****/

    protected void showToast(int resId) {
        showToast(getString(resId));
    }

    protected void showToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showLongToast(int resId) {
        showLongToast(getString(resId));
    }

    protected void showLongToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    private void showToast(CharSequence msg, int duration) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, duration).show();
        }
    }

    /**
     * 获得布局文件的Resource_ID
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化数据、布局等
     */
    protected abstract void init();

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


}
