package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.SearchInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.SearchInformationPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.adapter.user.SearchInformationAdapter;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.utils.EnglishUtils;
import com.allintask.lingdao.utils.TemporaryDataCachePool;
import com.allintask.lingdao.view.user.ISearchInformationView;
import com.allintask.lingdao.widget.CommonRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class SearchInformationActivity extends BaseActivity<ISearchInformationView, SearchInformationPresenter> implements ISearchInformationView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.iv_right_second)
    ImageView rightIv;
    @BindView(R.id.et_search_information)
    EditText searchInformationEt;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;

    private int searchInformationPosition;
    private int searchInformationType = CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION;

    private SearchInformationAdapter searchInformationAdapter;
    private List<SearchInformationBean> searchInformationList;
    private int educationalBackgroundId = -1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_information;
    }

    @Override
    protected SearchInformationPresenter CreatePresenter() {
        return new SearchInformationPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            searchInformationPosition = intent.getIntExtra(CommonConstant.EXTRA_POSITION, 0);
            searchInformationType = intent.getIntExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_cross);
        toolbar.setTitle("");

        switch (searchInformationType) {
            case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION:
                titleTv.setText(getString(R.string.educational_institution));
                break;

            case CommonConstant.SEARCH_INFORMATION_TYPE_MAJOR:
                titleTv.setText(getString(R.string.major));
                break;

            case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND:
                titleTv.setText(getString(R.string.education_background));
                break;
        }

        if (searchInformationType == CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND) {
            rightIv.setVisibility(View.GONE);
        } else {
            rightIv.setImageResource(R.mipmap.ic_tick);
            rightIv.setVisibility(View.VISIBLE);
            rightIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String searchInformation = searchInformationEt.getText().toString().trim();

                    if (!TextUtils.isEmpty(searchInformation)) {
                        Intent intent = new Intent();
                        intent.putExtra(CommonConstant.EXTRA_POSITION, searchInformationPosition);
                        intent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, searchInformationType);

                        if (!TextUtils.isEmpty(searchInformation)) {
                            intent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION, searchInformation);
                        }

                        setResult(CommonConstant.RESULT_CODE, intent);

                        finish();
                    } else {
                        if (searchInformationType == CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION) {
                            showToast("教育机构不能为空");
                        } else if (searchInformationType == CommonConstant.SEARCH_INFORMATION_TYPE_MAJOR) {
                            showToast("专业不能为空");
                        }
                    }
                }
            });
        }

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        switch (searchInformationType) {
            case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION:
                searchInformationEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
                searchInformationEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        recyclerView.smoothScrollToPosition(0);

                        String searchInformation = searchInformationEt.getText().toString().trim();

                        if (EnglishUtils.isEnglishCharacterString(searchInformation)) {
                            searchInformation = searchInformation.toUpperCase();
                        }

                        int index = searchInformationEt.getSelectionStart() - 1;

                        if (index >= 0) {
                            if (EmojiUtils.noEmoji(searchInformation)) {
                                Editable editable = searchInformationEt.getText();
                                editable.delete(index, index + 1);
                            }
                        }

                        searchInformationAdapter.setKeyword(searchInformation);
                        List<SearchInformationBean> tempSearchInformationList = TemporaryDataCachePool.getInstance().getSearchInformationList();

                        if (!TextUtils.isEmpty(searchInformation)) {
                            mPresenter.filtrateEducationInstitution(searchInformation, tempSearchInformationList);
                        } else {
                            searchInformationList.clear();
                            searchInformationList.addAll(tempSearchInformationList);
                            searchInformationAdapter.setDateList(searchInformationList);
                        }
                    }
                });
                break;

            case CommonConstant.SEARCH_INFORMATION_TYPE_MAJOR:
                searchInformationEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                searchInformationEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        recyclerView.smoothScrollToPosition(0);

                        String searchInformation = searchInformationEt.getText().toString().trim();

                        if (EnglishUtils.isEnglishCharacterString(searchInformation)) {
                            searchInformation = searchInformation.toUpperCase();
                        }

                        int index = searchInformationEt.getSelectionStart() - 1;

                        if (index >= 0) {
                            if (EmojiUtils.noEmoji(searchInformation)) {
                                Editable editable = searchInformationEt.getText();
                                editable.delete(index, index + 1);
                            }
                        }

                        searchInformationAdapter.setKeyword(searchInformation);
                        List<SearchInformationBean> tempSearchInformationList = TemporaryDataCachePool.getInstance().getSearchInformationList();

                        if (!TextUtils.isEmpty(searchInformation)) {
                            mPresenter.filtrateEducationInstitution(searchInformation, tempSearchInformationList);
                        } else {
                            searchInformationList.clear();
                            searchInformationList.addAll(tempSearchInformationList);
                            searchInformationAdapter.setDateList(searchInformationList);
                        }
                    }
                });
                break;

            case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND:
                searchInformationEt.setVisibility(View.GONE);
                break;
        }

        searchInformationAdapter = new SearchInformationAdapter(getParentContext());
        recyclerView.setAdapter(searchInformationAdapter);

        searchInformationAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchInformationBean searchInformationBean = (SearchInformationBean) searchInformationAdapter.getItem(position);

                if (null != searchInformationBean) {
                    String name = TypeUtils.getString(searchInformationBean.name);

                    if (searchInformationType == CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND) {
                        educationalBackgroundId = Integer.valueOf(searchInformationBean.code);

                        Intent intent = new Intent();
                        intent.putExtra(CommonConstant.EXTRA_POSITION, searchInformationPosition);
                        intent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION_TYPE, searchInformationType);
                        intent.putExtra(CommonConstant.EXTRA_SEARCH_INFORMATION, name);
                        intent.putExtra(CommonConstant.EXTRA_EDUCATIONAL_BACKGROUND_ID, educationalBackgroundId);
                        setResult(CommonConstant.RESULT_CODE, intent);

                        finish();
                    } else {
                        searchInformationEt.setText(name);
                    }
                }
            }
        });
    }

    private void initData() {
        searchInformationList = new ArrayList<>();

        switch (searchInformationType) {
            case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION:
                mPresenter.fetchEducationalInstitutionListRequest();
                break;

            case CommonConstant.SEARCH_INFORMATION_TYPE_MAJOR:
                mPresenter.fetchMajorListRequest();
                break;

            case CommonConstant.SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND:
                mPresenter.fetchEducationalBackgroundListRequest();
                break;
        }
    }

    @Override
    public void onShowSearchInformationList(List<SearchInformationBean> searchInformationList) {
        TemporaryDataCachePool.getInstance().setSearchInformationList(searchInformationList);
        this.searchInformationList.addAll(searchInformationList);

        if (null != this.searchInformationList && this.searchInformationList.size() > 0) {
            searchInformationAdapter.setDateList(this.searchInformationList);
        }
    }

    @Override
    public void onShowFiltrateList(List<SearchInformationBean> filtrateList) {
        searchInformationList.clear();
        searchInformationList.addAll(filtrateList);
        searchInformationAdapter.setDateList(searchInformationList);
    }

    @Override
    protected void onDestroy() {
        TemporaryDataCachePool.getInstance().setSearchInformationList(null);
        super.onDestroy();
    }

}
