package com.allintask.lingdao.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.BasePresenter;

import butterknife.BindView;

/**
 * WebViewActivity加载模板类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class SimpleWebViewActivity extends BaseActivity {

    public static final String WEB_URL_KEY = "WEB_URL_KEY";

    public static final String TOOLBAR_TITLE_KEY = "ACTION_BAR_TITLE_KEY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.simple_web_view_browser_webview)
    WebView simple_web_view_browser_webview;
    @BindView(R.id.simple_web_view_browser_progress_bar_pb)
    ProgressBar simple_web_view_browser_progress_bar_pb;

    private boolean isHistoryClearable = false;

    private String webURL;
    private String toolbarTitle;

    @Override
    protected int getLayoutResId() {
        return R.layout.simple_web_view_activity;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init() {
        AllintaskApplication.IS_STARTED = true;

        /**
         * 获得传递的数据
         */
        Intent intent = getIntent();
        if (null != intent) {
            this.webURL = intent.getStringExtra(WEB_URL_KEY);
            this.toolbarTitle = intent.getStringExtra(TOOLBAR_TITLE_KEY);
        }

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        /**
         * 设置标题
         */
        this.toolbarTitle = TextUtils.isEmpty(toolbarTitle) ? "" : toolbarTitle;
        titleTv.setText(toolbarTitle);

        setSupportActionBar(toolbar);

        /**
         * 配置WebSetting
         */
        WebSettings webSettings = simple_web_view_browser_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        simple_web_view_browser_webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                if (isHistoryClearable) {
                    isHistoryClearable = false;
                    view.clearHistory();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

        });

        simple_web_view_browser_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbarTitle = title;
                titleTv.setText(toolbarTitle);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    simple_web_view_browser_progress_bar_pb.setVisibility(View.GONE);
                } else {
                    if (simple_web_view_browser_progress_bar_pb.getVisibility() == View.GONE) {
                        simple_web_view_browser_progress_bar_pb.setVisibility(View.VISIBLE);
                    }
                    simple_web_view_browser_progress_bar_pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        //        simple_web_view_browser_webview.loadUrl(DEFAULT_ACCOUNT_SERVICE_INTRO_URL);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        if (!TextUtils.isEmpty(webURL)) {
            simple_web_view_browser_webview.loadUrl(webURL);
            isHistoryClearable = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (null != simple_web_view_browser_webview) {
            if (simple_web_view_browser_webview.canGoBack()) {
                simple_web_view_browser_webview.goBack();
            } else {
                simple_web_view_browser_webview.clearHistory();
                simple_web_view_browser_webview.clearFormData();
                simple_web_view_browser_webview.clearCache(true);
                //            deleteDatabase("webview.db");
                //            deleteDatabase("webviewCache.db");
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (null != simple_web_view_browser_webview) {
            simple_web_view_browser_webview.setWebChromeClient(null);
            simple_web_view_browser_webview = null;
        }

        super.onDestroy();
    }

}
