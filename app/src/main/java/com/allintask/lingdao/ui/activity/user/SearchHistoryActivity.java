package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.main.SearchHistoryPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.utils.PopupWindowUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.main.ISearchHistoryView;
import com.allintask.lingdao.widget.SearchView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.component.custom.textview.TagCloudView;


/**
 * 搜索历史页面
 * Created by zping on 2017/12/1.
 * Rebuild by TanJiaJun on 2018/1/10.
 */

public class SearchHistoryActivity extends BaseActivity<ISearchHistoryView, SearchHistoryPresenter> implements ISearchHistoryView {

    @BindView(R.id.ll_search_title)
    LinearLayout searchTitleLL;
    @BindView(R.id.ll_search_status)
    LinearLayout searchStatusLL;
    @BindView(R.id.tv_search_status)
    TextView searchStatusTv;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.tcv_everybody_search)
    TagCloudView everybodySearchTCV;
    @BindView(R.id.tv_cancel)
    TextView cancelTv;
    @BindView(R.id.tv_clear)
    TextView clearTv;
    @BindView(R.id.tcv_search_history)
    TagCloudView searchHistoryTCV;

    private int searchStatus;

    private InputMethodManager inputMethodManager;
    private PopupWindow searchStatusPopupWindow;
    private Set<String> searchSet;
    private String searchText;

    private List<String> mSearchServeRecommendVos;
    private List<String> mSearchDemandRecommendVos;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_history;
    }

    @Override
    protected SearchHistoryPresenter CreatePresenter() {
        return new SearchHistoryPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            searchStatus = intent.getIntExtra(CommonConstant.EXTRA_SEARCH_STATUS, CommonConstant.SEARCH_STATUS_SERVICE);
        }

        initUI();
        initData();
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
            searchStatusTv.setText(getString(R.string.service));
        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
            searchStatusTv.setText(getString(R.string.demand));
        }

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = searchView.getText().toString().trim();
                int index = searchView.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(searchText)) {
                        Editable editable = searchView.getText();
                        editable.delete(index, index + 1);
                    }
                }
            }
        });

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchView.setFocusable(false);

                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    }

                    searchText = searchView.getText().toString().trim();

                    if (!TextUtils.isEmpty(searchText)) {
                        mPresenter.saveKeyWordsSearchLogRequest(searchStatus, searchText);
                    } else {
                        showToast("服务内容不能为空");
                    }
                }
                return false;
            }
        });

        everybodySearchTCV.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                searchView.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

                String searchText = null;

                if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                    if (null != mSearchServeRecommendVos && mSearchDemandRecommendVos.size() > 0) {
                        searchText = mSearchServeRecommendVos.get(position);
                    }
                } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                    if (null != mSearchDemandRecommendVos && mSearchDemandRecommendVos.size() > 0) {
                        searchText = mSearchDemandRecommendVos.get(position);
                    }
                }

                if (!TextUtils.isEmpty(searchText)) {
                    searchView.setText("");

                    Intent intent = new Intent(getParentContext(), SearchActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_SEARCH_STATUS, searchStatus);
                    intent.putExtra(CommonConstant.EXTRA_KEYWORDS, searchText);
                    startActivity(intent);
                } else {
                    showToast("服务内容不能为空");
                }
            }
        });

        searchHistoryTCV.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                searchView.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

                if (null != searchSet) {
                    List<String> searchList = new ArrayList<>(searchSet);
                    String searchText = searchList.get(position);

                    if (!TextUtils.isEmpty(searchText)) {
                        searchView.setText("");

                        Intent intent = new Intent(getParentContext(), SearchActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_SEARCH_STATUS, searchStatus);
                        intent.putExtra(CommonConstant.EXTRA_KEYWORDS, searchText);
                        startActivity(intent);
                    } else {
                        showToast("服务内容不能为空");
                    }
                }
            }
        });
    }

    private void initData() {
        mPresenter.fetchRecommendDataRequest();
    }

    private void showSearchStatusPopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_search_status, null);

        LinearLayout searchStatusLL = (LinearLayout) contentView.findViewById(R.id.ll_search_status);
        final TextView serviceTv = (TextView) contentView.findViewById(R.id.tv_service);
        final TextView demandTv = (TextView) contentView.findViewById(R.id.tv_demand);

        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
            serviceTv.setBackgroundColor(getResources().getColor(R.color.theme_orange));
            serviceTv.setTextColor(getResources().getColor(R.color.white));

            demandTv.setBackgroundColor(getResources().getColor(R.color.search_status_unselected_background_color));
            demandTv.setTextColor(getResources().getColor(R.color.text_dark_black));
        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
            serviceTv.setBackgroundColor(getResources().getColor(R.color.search_status_unselected_background_color));
            serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));

            demandTv.setBackgroundColor(getResources().getColor(R.color.theme_orange));
            demandTv.setTextColor(getResources().getColor(R.color.white));
        }

        searchStatusPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PopupWindowUtils.showAsDropDown(searchStatusPopupWindow, searchTitleLL);

        searchStatusLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != searchStatusPopupWindow && searchStatusPopupWindow.isShowing()) {
                    searchStatusPopupWindow.dismiss();
                }
            }
        });

        serviceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStatus = CommonConstant.SEARCH_STATUS_SERVICE;
                searchStatusTv.setText(getString(R.string.service));

                if (null != searchStatusPopupWindow && searchStatusPopupWindow.isShowing()) {
                    searchStatusPopupWindow.dismiss();
                }

                mPresenter.fetchRecommendDataRequest();
            }
        });

        demandTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStatus = CommonConstant.SEARCH_STATUS_DEMAND;
                searchStatusTv.setText(getString(R.string.demand));

                if (null != searchStatusPopupWindow && searchStatusPopupWindow.isShowing()) {
                    searchStatusPopupWindow.dismiss();
                }

                mPresenter.fetchRecommendDataRequest();
            }
        });
    }

    private void setSearchList(String searchText) {
        searchSet.add(searchText);
        String searchSetJsonString = JSONArray.toJSONString(searchSet, SerializerFeature.DisableCircularReferenceDetect);
        UserPreferences.getInstance().setSearchSetJsonString(searchSetJsonString);
    }

    @OnClick({R.id.ll_search_status, R.id.search_view, R.id.tv_cancel, R.id.tv_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search_status:
                searchView.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

                if (null != searchStatusPopupWindow && searchStatusPopupWindow.isShowing()) {
                    searchStatusPopupWindow.dismiss();
                } else {
                    showSearchStatusPopupWindow();
                }
                break;

            case R.id.search_view:
                searchView.setFocusable(true);
                searchView.setFocusableInTouchMode(true);
                searchView.requestFocus();
                searchView.findFocus();

                inputMethodManager.showSoftInput(searchView, InputMethodManager.SHOW_FORCED);
                break;

            case R.id.tv_cancel:
                searchView.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

                finish();
                break;

            case R.id.tv_clear:
                UserPreferences.getInstance().setSearchSetJsonString(null);
                searchHistoryTCV.setTags(null);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String searchSetJsonString = UserPreferences.getInstance().getSearchSetJsonString();

        if (!TextUtils.isEmpty(searchSetJsonString)) {
            searchSet = JSON.parseObject(searchSetJsonString, new TypeReference<Set<String>>() {
            });
        } else {
            searchSet = new HashSet<>();
        }

        List<String> tempSearchList = new ArrayList<>(searchSet);
        List<String> searchList = new ArrayList<>();

        if (null != tempSearchList && tempSearchList.size() > 0) {
            if (tempSearchList.size() > 20) {
                for (int i = 0; i < 20; i++) {
                    String searchText = tempSearchList.get(i);
                    searchList.add(searchText);
                }

                searchHistoryTCV.setTags(searchList);
            } else {
                searchHistoryTCV.setTags(tempSearchList);
            }
        }
    }

    @Override
    public void onShowEverybodySearchData(List<String> searchServeRecommendVos, List<String> searchDemandRecommendVos) {
        mSearchServeRecommendVos = searchServeRecommendVos;
        mSearchDemandRecommendVos = searchDemandRecommendVos;

        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
            everybodySearchTCV.setTags(mSearchServeRecommendVos);
        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
            everybodySearchTCV.setTags(mSearchDemandRecommendVos);
        }
    }

    @Override
    public void onSaveKeywordsSearchLog() {
        setSearchList(searchText);

        if (null != searchView) {
            searchView.setText("");
        }

        Intent intent = new Intent(getParentContext(), SearchActivity.class);
        intent.putExtra(CommonConstant.EXTRA_SEARCH_STATUS, searchStatus);
        intent.putExtra(CommonConstant.EXTRA_KEYWORDS, searchText);
        startActivity(intent);
    }

}
