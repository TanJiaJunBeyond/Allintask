package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.bean.recommend.RecommendListBean;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.AdvancedFilterBean;
import com.allintask.lingdao.bean.user.CategoryBean;
import com.allintask.lingdao.bean.user.DemandAdvancedFilterBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.bean.user.WayBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.SearchPresenter;
import com.allintask.lingdao.ui.activity.BaseSwipeRefreshActivity;
import com.allintask.lingdao.ui.activity.demand.DemandDetailsActivity;
import com.allintask.lingdao.ui.activity.recommend.RecommendDetailsActivity;
import com.allintask.lingdao.ui.adapter.main.RecommendAdapter;
import com.allintask.lingdao.ui.adapter.main.RecommendDemandAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.adapter.user.CategoryAdapter;
import com.allintask.lingdao.ui.adapter.user.CategorySubclassAdapter;
import com.allintask.lingdao.ui.adapter.user.CityAdapter;
import com.allintask.lingdao.ui.adapter.user.ProvinceAdapter;
import com.allintask.lingdao.ui.adapter.user.WayAdapter;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.utils.PopupWindowUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.view.user.ISearchView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.allintask.lingdao.widget.SearchView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/1.
 */

public class SearchActivity extends BaseSwipeRefreshActivity<ISearchView, SearchPresenter> implements ISearchView {

    @BindView(R.id.ll_search_title)
    LinearLayout searchTitleLL;
    @BindView(R.id.ll_search_status)
    LinearLayout searchStatusLL;
    @BindView(R.id.tv_search_status)
    TextView searchStatusTv;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.tv_cancel)
    TextView cancelTv;
    @BindView(R.id.view_divider_line)
    View dividerLineView;
    @BindView(R.id.ll_service)
    LinearLayout serviceLL;
    @BindView(R.id.tv_service)
    TextView serviceTv;
    @BindView(R.id.iv_service_arrow)
    ImageView serviceArrowIv;
    @BindView(R.id.ll_city)
    LinearLayout cityLL;
    @BindView(R.id.tv_city)
    TextView cityTv;
    @BindView(R.id.iv_city_arrow)
    ImageView cityArrowIv;
    @BindView(R.id.ll_mode)
    LinearLayout modeLL;
    @BindView(R.id.tv_mode)
    TextView modeTv;
    @BindView(R.id.iv_mode_arrow)
    ImageView modeArrowIv;
    @BindView(R.id.ll_more)
    LinearLayout moreLL;
    @BindView(R.id.tv_more)
    TextView moreTv;
    @BindView(R.id.iv_more_arrow)
    ImageView moreArrowIv;

    private int searchStatus;
    private String keywords;
    private String provinceCode;
    private String cityCode;
    private int categoryId = -1;
    private List<Integer> categoryPropertyValueIdList;

    private Set<String> searchSet;

    private int categoryPosition;
    private int serviceWayId = -1;
    private int minPrice = -1;
    private int maxPrice = -1;
    private int unitId = -1;
    private int sortType = -1;

    private int demandServiceWayId = -1;
    private int payEscrowId = -1;
    private int demandMinPrice = -1;
    private int demandMaxPrice = -1;
    private int paymentCycleId = -1;
    private int demandSortType = -1;

    private InputMethodManager inputMethodManager;

    private int widthMeasureSpec;
    private int heightMeasureSpec;

    private PopupWindow searchStatusPopupWindow;

    private PopupWindow servicePopupWindow;
    private CategoryAdapter categoryAdapter;
    private CategorySubclassAdapter categorySubclassAdapter;

    private PopupWindow cityPopupWindow;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    private RecommendAdapter recommendAdapter;
    private RecommendDemandAdapter recommendDemandAdapter;

    private PopupWindow wayPopupWindow;
    private WayAdapter wayAdapter;

    private PopupWindow morePopupWindow;

    private List<IsAllBean> addressList;
    private List<AddressSubBean> cityList;
    private List<CategoryBean> categoryList;
    private List<CategoryBean.FirstSubBean> categorySubclassList;

    private AdvancedFilterBean advancedFilterBean;
    private DemandAdvancedFilterBean demandAdvancedFilterBean;

    private List<WayBean> wayList;

    private List<AdvancedFilterBean.ServeWayCodeAndValuesBean> serveWayCodeAndValuesList;
    private List<AdvancedFilterBean.UnitCodeAndValuesBean> unitCodeAndValuesList;
    private List<AdvancedFilterBean.UnitCodeAndValuesBean.PriceRangeVosBean> priceRangeVosList;
    private List<AdvancedFilterBean.SortCodeAndValuesBean> sortCodeAndValuesList;

    private List<DemandAdvancedFilterBean.ServeWayCodeAndValuesBean> demandServeWayCodeAndValuesList;
    private List<DemandAdvancedFilterBean.TrusteeshipCodeAndValuesBean> trusteeshipCodeAndValuesList;
    private List<DemandAdvancedFilterBean.BudgetRangeVosBean> budgetRangeVosList;
    private List<DemandAdvancedFilterBean.DeliverCycleCodeAndValuesBean> deliverCycleCodeAndValuesList;
    private List<DemandAdvancedFilterBean.SortCodeAndValuesBean> demandSortCodeAndValuesList;

    private TagAdapter priceUnitTagAdapter;
    private TagAdapter servicePriceTagAdapter;
    private TagAdapter sortTypeTagAdapter;

    private TagAdapter payEscrowTagAdapter;
    private TagAdapter rangeOfBudgetTagAdapter;
    private TagAdapter paymentCycleTagAdapter;
    private TagAdapter demandSortTypeTagAdapter;

    //    private Set<Integer> serviceWaySet;
    private Set<Integer> priceUnitSet;
    private Set<Integer> servicePriceSet;
    private Set<Integer> sortTypeSet;

    //    private Set<Integer> demandServiceWaySet;
    private Set<Integer> payEscrowSet;
    private Set<Integer> rangeOfBudgetSet;
    private Set<Integer> paymentCycleSet;
    private Set<Integer> demandSortTypeSet;

    private boolean isShowAddressPopupWindow = false;
    private boolean isShowCategoryPopupWindow = false;
    private boolean isShowModePopupWindow = false;
    private boolean isShowMorePopupWindow = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchPresenter CreatePresenter() {
        return new SearchPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            searchStatus = intent.getIntExtra(CommonConstant.EXTRA_SEARCH_STATUS, CommonConstant.SEARCH_STATUS_SERVICE);
            keywords = intent.getStringExtra(CommonConstant.EXTRA_KEYWORDS);
            provinceCode = intent.getStringExtra(CommonConstant.EXTRA_PROVINCE_CODE);
            cityCode = intent.getStringExtra(CommonConstant.EXTRA_CITY_CODE);
            categoryId = intent.getIntExtra(CommonConstant.EXTRA_CATEGORY_ID, -1);
            categoryPropertyValueIdList = intent.getIntegerArrayListExtra(CommonConstant.EXTRA_CATEGORY_PROPERTY_VALUE_ID_LIST);
        }

        initUI();
        initData();
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(WindowUtils.getScreenWidth(getParentContext()), View.MeasureSpec.EXACTLY);
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        if (null != mSwipeRefreshStatusLayout) {
            View emptyView = mSwipeRefreshStatusLayout.getEmptyView();

            TextView contentTv = (TextView) emptyView.findViewById(R.id.tv_content);
            contentTv.setText(getString(R.string.search_no_data));

            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSearchList(keywords);
                    mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                }
            });
        }

        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
            searchStatusTv.setText(getString(R.string.service));
        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
            searchStatusTv.setText(getString(R.string.demand));
        }

        if (!TextUtils.isEmpty(keywords)) {
            searchView.setText(keywords);
        }

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    unSelected();

                    if (null != servicePopupWindow && servicePopupWindow.isShowing()) {
                        servicePopupWindow.dismiss();
                    }

                    if (null != cityPopupWindow && cityPopupWindow.isShowing()) {
                        cityPopupWindow.dismiss();
                    }

                    if (null != wayPopupWindow && wayPopupWindow.isShowing()) {
                        wayPopupWindow.dismiss();
                    }

                    if (null != morePopupWindow && morePopupWindow.isShowing()) {
                        morePopupWindow.dismiss();
                    }
                }
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keywords = searchView.getText().toString().trim();
                int index = searchView.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(keywords)) {
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

                    keywords = searchView.getText().toString().trim();

                    if (!TextUtils.isEmpty(keywords)) {
                        mPresenter.saveKeyWordsSearchLogRequest(searchStatus, keywords);
                    } else {
                        showToast("服务内容不能为空");
                    }
                }
                return false;
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getParentContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getParentContext(), R.drawable.shape_common_recycler_view_divider));
        recycler_view.addItemDecoration(dividerItemDecoration);

        recommendAdapter = new RecommendAdapter(getParentContext());
        recommendAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RecommendListBean.RecommendBean recommendBean = (RecommendListBean.RecommendBean) recommendAdapter.getItem(position);

                if (null != recommendBean) {
                    int userId = TypeUtils.getInteger(recommendBean.userId, -1);
                    int serveId = TypeUtils.getInteger(recommendBean.serveId, -1);

                    Intent intent = new Intent(getParentContext(), RecommendDetailsActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_USER_ID, userId);
                    intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serveId);
                    startActivity(intent);
                }
            }
        });

        recommendDemandAdapter = new RecommendDemandAdapter(getParentContext());
        recommendDemandAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RecommendDemandBean recommendDemandBean = (RecommendDemandBean) recommendDemandAdapter.getItem(position);

                if (null != recommendDemandBean) {
                    int demandId = TypeUtils.getInteger(recommendDemandBean.demandId, -1);

                    if (demandId != -1) {
                        Intent intent = new Intent(getParentContext(), DemandDetailsActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                        startActivity(intent);
                    }
                }
            }
        });

        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
            recycler_view.setAdapter(recommendAdapter);
        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
            recycler_view.setAdapter(recommendDemandAdapter);
        }
    }

    private void initData() {
        String searchSetJsonString = UserPreferences.getInstance().getSearchSetJsonString();

        if (!TextUtils.isEmpty(searchSetJsonString)) {
            searchSet = JSON.parseObject(searchSetJsonString, new TypeReference<Set<String>>() {
            });
        } else {
            searchSet = new HashSet<>();
        }

        categoryAdapter = new CategoryAdapter(getParentContext());
        categorySubclassAdapter = new CategorySubclassAdapter(getParentContext());

        if (null == categoryPropertyValueIdList) {
            categoryPropertyValueIdList = new ArrayList<>();
        }

        provinceAdapter = new ProvinceAdapter(getParentContext());
        cityAdapter = new CityAdapter(getParentContext());

        wayAdapter = new WayAdapter(getParentContext());
        wayList = new ArrayList<>();

//        serviceWaySet = new HashSet<>();
        priceUnitSet = new HashSet<>();
        servicePriceSet = new HashSet<>();
        sortTypeSet = new HashSet<>();

//        demandServiceWaySet = new HashSet<>();
        payEscrowSet = new HashSet<>();
        rangeOfBudgetSet = new HashSet<>();
        paymentCycleSet = new HashSet<>();
        demandSortTypeSet = new HashSet<>();

        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
            mPresenter.fetchCategoryListRequest();
            mPresenter.fetchAdvancedFilterRequest();
        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
            mPresenter.fetchDemandCategoryListRequest();
            mPresenter.fetchDemandAdvancedFilterRequest();
        }

        mPresenter.fetchAddressListRequest();
        mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
    }

    private void selectService() {
        serviceTv.setTextColor(getResources().getColor(R.color.text_orange));
        serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);

//        cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
//
//        modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
//
//        moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);

        showServicePopupWindow();
    }

    private void selectCity() {
//        serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);

        cityTv.setTextColor(getResources().getColor(R.color.text_orange));
        cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);

//        modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
//
//        moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);

        showCityPopupWindow();
    }

    private void selectMode() {
//        serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
//
//        cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);

        modeTv.setTextColor(getResources().getColor(R.color.text_orange));
        modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);

//        moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);

        showWayPopupWindow();
    }

    private void selectMore() {
//        serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
//
//        cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
//
//        modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
//        modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);

        moreTv.setTextColor(getResources().getColor(R.color.text_orange));
        moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);

        showMorePopupWindow();
    }

    private void unSelected() {
        serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
        serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);

        cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
        cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);

        modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
        modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);

        moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
        moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
    }

    private void showSearchStatusPopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_search_status, null);

        LinearLayout searchStatusLL = (LinearLayout) contentView.findViewById(R.id.ll_search_status);
        final TextView serviceTv = (TextView) contentView.findViewById(R.id.tv_service);
        final TextView demandTv = (TextView) contentView.findViewById(R.id.tv_demand);

        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
            serviceTv.setBackgroundColor(getResources().getColor(R.color.text_orange));
            serviceTv.setTextColor(getResources().getColor(R.color.white));

            demandTv.setBackgroundColor(getResources().getColor(R.color.search_status_unselected_background_color));
            demandTv.setTextColor(getResources().getColor(R.color.text_dark_black));
        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
            serviceTv.setBackgroundColor(getResources().getColor(R.color.search_status_unselected_background_color));
            serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));

            demandTv.setBackgroundColor(getResources().getColor(R.color.text_orange));
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

                checkSearchFiltrateConditionStatus();
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

                recycler_view.setAdapter(recommendAdapter);

                if (null != categoryPropertyValueIdList && categoryPropertyValueIdList.size() > 0) {
                    categoryPropertyValueIdList.clear();
                }

                if (null != categoryList && categoryList.size() > 0) {
                    categoryList.clear();
                }

                if (null != categorySubclassList && categorySubclassList.size() > 0) {
                    categorySubclassList.clear();
                }

                if (null != addressList && addressList.size() > 0) {
                    addressList.clear();
                }

                if (null != cityList && cityList.size() > 0) {
                    cityList.clear();
                }

                categoryId = -1;
                provinceCode = null;
                cityCode = null;

//            if (null != serviceWaySet) {
//                serviceWaySet.clear();
//                serviceWaySet.add(0);
//            }

                if (null != priceUnitSet) {
                    priceUnitSet.clear();
                    priceUnitSet.add(0);
                }

                if (null != servicePriceSet) {
                    servicePriceSet.clear();
                    servicePriceSet.add(0);
                }

                if (null != sortTypeSet) {
                    sortTypeSet.clear();
                    sortTypeSet.add(0);
                }

                serviceWayId = -1;
                unitId = -1;
                minPrice = -1;
                maxPrice = -1;
                sortType = -1;

//            if (null != demandServiceWaySet) {
//                demandServiceWaySet.clear();
//                demandServiceWaySet.add(0);
//            }

                if (null != payEscrowSet) {
                    payEscrowSet.clear();
                    payEscrowSet.add(0);
                }

                if (null != rangeOfBudgetSet) {
                    rangeOfBudgetSet.clear();
                    rangeOfBudgetSet.add(0);
                }

                if (null != paymentCycleSet) {
                    paymentCycleSet.clear();
                    paymentCycleSet.add(0);
                }

                if (null != demandSortTypeSet) {
                    demandSortTypeSet.clear();
                    demandSortTypeSet.add(0);
                }

                demandServiceWayId = -1;
                payEscrowId = -1;
                demandMinPrice = -1;
                demandMaxPrice = -1;
                paymentCycleId = -1;
                demandSortType = -1;

                checkSearchFiltrateConditionStatus();

                mPresenter.fetchCategoryListRequest();
                mPresenter.fetchAddressListRequest();
                mPresenter.fetchAdvancedFilterRequest();
                mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
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

                recycler_view.setAdapter(recommendDemandAdapter);

                if (null != categoryPropertyValueIdList && categoryPropertyValueIdList.size() > 0) {
                    categoryPropertyValueIdList.clear();
                }

                if (null != categoryList && categoryList.size() > 0) {
                    categoryList.clear();
                }

                if (null != categorySubclassList && categorySubclassList.size() > 0) {
                    categorySubclassList.clear();
                }

                if (null != addressList && addressList.size() > 0) {
                    addressList.clear();
                }

                if (null != cityList && cityList.size() > 0) {
                    cityList.clear();
                }

                categoryId = -1;
                provinceCode = null;
                cityCode = null;

//            if (null != serviceWaySet) {
//                serviceWaySet.clear();
//                serviceWaySet.add(0);
//            }

                if (null != priceUnitSet) {
                    priceUnitSet.clear();
                    priceUnitSet.add(0);
                }

                if (null != servicePriceSet) {
                    servicePriceSet.clear();
                    servicePriceSet.add(0);
                }

                if (null != sortTypeSet) {
                    sortTypeSet.clear();
                    sortTypeSet.add(0);
                }

                serviceWayId = -1;
                unitId = -1;
                minPrice = -1;
                maxPrice = -1;
                sortType = -1;

//            if (null != demandServiceWaySet) {
//                demandServiceWaySet.clear();
//                demandServiceWaySet.add(0);
//            }

                if (null != payEscrowSet) {
                    payEscrowSet.clear();
                    payEscrowSet.add(0);
                }

                if (null != rangeOfBudgetSet) {
                    rangeOfBudgetSet.clear();
                    rangeOfBudgetSet.add(0);
                }

                if (null != paymentCycleSet) {
                    paymentCycleSet.clear();
                    paymentCycleSet.add(0);
                }

                if (null != demandSortTypeSet) {
                    demandSortTypeSet.clear();
                    demandSortTypeSet.add(0);
                }

                demandServiceWayId = -1;
                payEscrowId = -1;
                demandMinPrice = -1;
                demandMaxPrice = -1;
                paymentCycleId = -1;
                demandSortType = -1;

                mPresenter.fetchDemandCategoryListRequest();
                mPresenter.fetchAddressListRequest();
                mPresenter.fetchDemandAdvancedFilterRequest();
                mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
            }
        });
    }

    private void setSearchList(String searchText) {
        if (!TextUtils.isEmpty(searchText)) {
            searchSet.add(searchText);
            String searchSetJsonString = JSONArray.toJSONString(searchSet, SerializerFeature.DisableCircularReferenceDetect);
            UserPreferences.getInstance().setSearchSetJsonString(searchSetJsonString);
        }
    }

    private void showServicePopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_service, null);

        LinearLayout serviceLL = (LinearLayout) contentView.findViewById(R.id.ll_service);
        final LinearLayout contentLL = (LinearLayout) contentView.findViewById(R.id.ll_content);
        final CommonRecyclerView categoryCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_category);
        final CommonRecyclerView categorySubclassCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_category_subclass);
        final LinearLayout bottomLL = (LinearLayout) contentView.findViewById(R.id.ll_bottom);
        Button confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);

        categoryCRV.setAdapter(categoryAdapter);

        categoryAdapter.setDateList(categoryList);
        categoryAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                categoryPosition = position;
                categoryPropertyValueIdList.clear();
                int itemCount = categoryAdapter.getItemCount();

                for (int i = 0; i < itemCount; i++) {
                    CategoryBean categoryBean = (CategoryBean) categoryAdapter.getItem(i);

                    if (null != categoryBean && i == position) {
                        categoryBean.isSelected = true;
                    } else {
                        categoryBean.isSelected = false;
                    }
                }

                if (null != categoryAdapter) {
                    categoryAdapter.notifyDataSetChanged();
                }

                CategoryBean categoryBean = (CategoryBean) categoryAdapter.getItem(position);

                if (null != categoryBean) {
                    categoryId = TypeUtils.getInteger(categoryBean.code, -1);
                    categorySubclassList = categoryBean.sub;

                    if (null != categorySubclassList && categorySubclassList.size() > 0) {
                        for (int i = 0; i < categorySubclassList.size(); i++) {
                            Set<Integer> isSelectedSet = new HashSet<>();
                            List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                            CategoryBean.FirstSubBean firstSubBean = categorySubclassList.get(i);
                            firstSubBean.isSelectedSet = isSelectedSet;
                            firstSubBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                        }
                    }

                    if (null != categorySubclassAdapter) {
                        categorySubclassAdapter.setDateList(categorySubclassList);

                        categoryCRV.measure(widthMeasureSpec, heightMeasureSpec);
//                        bottomLL.measure(widthMeasureSpec, heightMeasureSpec);

                        int categoryRecyclerViewHeight = categoryCRV.getMeasuredHeight();
//                        int bottomHeight = bottomLL.getMeasuredHeight();

                        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 85.5F);
                        int tempHeight = height * 2 / 3;
//                        int tempPopupWindowHeight = categoryRecyclerViewHeight + bottomHeight;
                        int resultHeight;

                        if (categoryRecyclerViewHeight < tempHeight) {
                            resultHeight = categoryRecyclerViewHeight;
                        } else {
                            resultHeight = tempHeight;
                        }

                        LinearLayout.LayoutParams categoryCRVLayoutParams = (LinearLayout.LayoutParams) categoryCRV.getLayoutParams();
//                        categoryCRVLayoutParams.height = resultHeight - bottomHeight;
                        categoryCRVLayoutParams.height = resultHeight;
                        categoryCRV.setLayoutParams(categoryCRVLayoutParams);

//                        LinearLayout.LayoutParams categorySubclassCRVLayoutParams = (LinearLayout.LayoutParams) categorySubclassCRV.getLayoutParams();
//                        categorySubclassCRVLayoutParams.height = resultHeight - bottomHeight;
//                        categorySubclassCRV.setLayoutParams(categorySubclassCRVLayoutParams);

                        ViewGroup.LayoutParams layoutParams = contentLL.getLayoutParams();
                        layoutParams.height = resultHeight;
                        contentLL.setLayoutParams(layoutParams);

                        if (null != servicePopupWindow && servicePopupWindow.isShowing()) {
                            servicePopupWindow.dismiss();
                        }

//                        unSelected();
                        checkSearchFiltrateConditionStatus();
                        setSearchList(keywords);
                        mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                    }
                }
            }
        });

        categorySubclassCRV.setAdapter(categorySubclassAdapter);

        categorySubclassAdapter.setDateList(categorySubclassList);

        categoryCRV.measure(widthMeasureSpec, heightMeasureSpec);
//        bottomLL.measure(widthMeasureSpec, heightMeasureSpec);

        int categoryRecyclerViewHeight = categoryCRV.getMeasuredHeight();
//        int bottomHeight = bottomLL.getMeasuredHeight();

        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 85.5F);
        int tempHeight = height * 2 / 3;
//        int tempPopupWindowHeight = categoryRecyclerViewHeight + bottomHeight;
        int resultHeight;

        if (categoryRecyclerViewHeight < tempHeight) {
            resultHeight = categoryRecyclerViewHeight;
        } else {
            resultHeight = tempHeight;
        }

        LinearLayout.LayoutParams categoryCRVLayoutParams = (LinearLayout.LayoutParams) categoryCRV.getLayoutParams();
//        categoryCRVLayoutParams.height = resultHeight - bottomHeight;
        categoryCRVLayoutParams.height = resultHeight;
        categoryCRV.setLayoutParams(categoryCRVLayoutParams);

//        LinearLayout.LayoutParams categorySubclassCRVLayoutParams = (LinearLayout.LayoutParams) categorySubclassCRV.getLayoutParams();
//        categorySubclassCRVLayoutParams.height = resultHeight - bottomHeight;
//        categorySubclassCRV.setLayoutParams(categorySubclassCRVLayoutParams);

        ViewGroup.LayoutParams layoutParams = contentLL.getLayoutParams();
        layoutParams.height = resultHeight;
        contentLL.setLayoutParams(layoutParams);

        servicePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PopupWindowUtils.showAsDropDown(servicePopupWindow, dividerLineView);

        serviceLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != servicePopupWindow && servicePopupWindow.isShowing()) {
                    servicePopupWindow.dismiss();
//                    unSelected();
                    checkSearchFiltrateConditionStatus();
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != servicePopupWindow && servicePopupWindow.isShowing()) {
                    servicePopupWindow.dismiss();
//                    unSelected();

                    if (categoryPosition != -1) {
                        CategoryBean categoryBean = (CategoryBean) categoryAdapter.getItem(categoryPosition);

                        if (null != categoryBean) {
                            List<CategoryBean.FirstSubBean> firstSubList = categoryBean.sub;

                            if (null != firstSubList && firstSubList.size() > 0) {
                                for (int i = 0; i < firstSubList.size(); i++) {
                                    CategoryBean.FirstSubBean firstSubBean = firstSubList.get(i);

                                    if (null != firstSubBean) {
                                        List<Integer> isSelectedCategoryIdList = firstSubBean.isSelectedCategoryIdList;

                                        if (null != isSelectedCategoryIdList && isSelectedCategoryIdList.size() > 0) {
                                            categoryPropertyValueIdList.addAll(isSelectedCategoryIdList);
                                        }
                                    }
                                }
                            }

                            checkSearchFiltrateConditionStatus();
                            setSearchList(keywords);
                            mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                        }
                    }
                }
            }
        });
    }

    private void showCityPopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_city, null);

        LinearLayout cityLL = (LinearLayout) contentView.findViewById(R.id.ll_city);
        final LinearLayout contentLL = (LinearLayout) contentView.findViewById(R.id.ll_content);
        final CommonRecyclerView provinceCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_province);
        final CommonRecyclerView cityCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_city);

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
                    provinceCode = isAllBean.code;
                    cityList = isAllBean.sub;

                    if (null != cityList && cityList.size() > 0) {
                        if (null != cityAdapter) {
                            cityAdapter.setDateList(cityList);
                        }
                    } else {
                        if (null != cityPopupWindow && cityPopupWindow.isShowing()) {
                            cityPopupWindow.dismiss();

                            if (null != cityAdapter) {
                                cityAdapter.setDateList(null);
                            }

//                            unSelected();
                            cityCode = null;
                            checkSearchFiltrateConditionStatus();
                            setSearchList(keywords);
                            mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                        }
                    }
                }

                provinceCRV.measure(widthMeasureSpec, heightMeasureSpec);
                cityCRV.measure(widthMeasureSpec, heightMeasureSpec);

                int provinceRecyclerViewHeight = provinceCRV.getMeasuredHeight();
                int cityRecyclerViewHeight = cityCRV.getMeasuredHeight();

                int resultHeight;
                int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 85.5F);
                int tempHeight = height * 2 / 3;

                if (provinceRecyclerViewHeight < cityRecyclerViewHeight) {
                    if (cityRecyclerViewHeight < tempHeight) {
                        resultHeight = cityRecyclerViewHeight;
                    } else {
                        resultHeight = tempHeight;
                    }
                } else {
                    if (provinceRecyclerViewHeight < tempHeight) {
                        resultHeight = provinceRecyclerViewHeight;
                    } else {
                        resultHeight = tempHeight;
                    }
                }

                ViewGroup.LayoutParams layoutParams = contentLL.getLayoutParams();
                layoutParams.height = resultHeight;
                contentLL.setLayoutParams(layoutParams);
            }
        });

        cityCRV.setAdapter(cityAdapter);

        cityAdapter.setDateList(cityList);
        cityAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int itemCount = cityAdapter.getItemCount();

                if (null != cityList && cityList.size() > 0) {
                    for (int i = 0; i < itemCount; i++) {
                        AddressSubBean subBean = (AddressSubBean) cityAdapter.getItem(i);

                        if (null != subBean) {
                            if (i == position) {
                                subBean.isSelected = true;
                            } else {
                                subBean.isSelected = false;
                            }
                        }
                    }

                    AddressSubBean subBean = cityList.get(position);

                    if (null != subBean) {
                        cityCode = TypeUtils.getString(subBean.code, "");

                        if (null != cityPopupWindow && cityPopupWindow.isShowing()) {
                            cityPopupWindow.dismiss();
//                            unSelected();
                            checkSearchFiltrateConditionStatus();
                            setSearchList(keywords);
                            mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                        }
                    }
                }
            }
        });

        provinceCRV.measure(widthMeasureSpec, heightMeasureSpec);
        cityCRV.measure(widthMeasureSpec, heightMeasureSpec);

        int provinceRecyclerViewHeight = provinceCRV.getMeasuredHeight();
        int cityRecyclerViewHeight = cityCRV.getMeasuredHeight();

        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 85.5F);
        int tempHeight = height * 2 / 3;
        int resultHeight;

        if (provinceRecyclerViewHeight < cityRecyclerViewHeight) {
            if (cityRecyclerViewHeight < tempHeight) {
                resultHeight = cityRecyclerViewHeight;
            } else {
                resultHeight = tempHeight;
            }
        } else {
            if (provinceRecyclerViewHeight < tempHeight) {
                resultHeight = provinceRecyclerViewHeight;
            } else {
                resultHeight = tempHeight;
            }
        }

        ViewGroup.LayoutParams layoutParams = contentLL.getLayoutParams();
        layoutParams.height = resultHeight;
        contentLL.setLayoutParams(layoutParams);

        cityPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PopupWindowUtils.showAsDropDown(cityPopupWindow, dividerLineView);

        cityLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != cityPopupWindow && cityPopupWindow.isShowing()) {
                    cityPopupWindow.dismiss();
//                    unSelected();
                    checkSearchFiltrateConditionStatus();
                }
            }
        });
    }

    private void showWayPopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_way, null);

        LinearLayout wayLL = (LinearLayout) contentView.findViewById(R.id.ll_way);
        CommonRecyclerView wayCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_way);

        wayCRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, DensityUtils.dip2px(getParentContext(), 0.5F));
            }
        });

        wayCRV.setAdapter(wayAdapter);

        wayAdapter.setDateList(wayList);
        wayAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int itemCount = wayAdapter.getItemCount();

                for (int i = 0; i < itemCount; i++) {
                    WayBean wayBean = (WayBean) wayAdapter.getItem(i);

                    if (null != wayBean) {
                        if (i == position) {
                            wayBean.isSelected = true;
                        } else {
                            wayBean.isSelected = false;
                        }
                    }
                }

                if (null != wayPopupWindow && wayPopupWindow.isShowing()) {
                    WayBean wayBean = (WayBean) wayAdapter.getItem(position);

                    if (null != wayBean) {
                        wayPopupWindow.dismiss();
//                        unSelected();

                        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                            serviceWayId = TypeUtils.getInteger(wayBean.code, -1);
                        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                            demandServiceWayId = TypeUtils.getInteger(wayBean.code, -1);
                        }

                        checkSearchFiltrateConditionStatus();
                        setSearchList(keywords);
                        mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                    }
                }
            }
        });

        wayCRV.measure(widthMeasureSpec, heightMeasureSpec);

        int wayRecyclerViewHeight = wayCRV.getMeasuredHeight();

        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 85.5F);
        int tempHeight = height * 2 / 3;
        int resultHeight;

        if (wayRecyclerViewHeight > tempHeight) {
            resultHeight = tempHeight;

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) wayCRV.getLayoutParams();
            layoutParams.height = resultHeight;
            wayCRV.setLayoutParams(layoutParams);
        }

        wayPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PopupWindowUtils.showAsDropDown(wayPopupWindow, dividerLineView);

        wayLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != wayPopupWindow && wayPopupWindow.isShowing()) {
                    wayPopupWindow.dismiss();
//                    unSelected();
                    checkSearchFiltrateConditionStatus();
                }
            }
        });
    }

    private void showMorePopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_more, null);

        LinearLayout moreLL = (LinearLayout) contentView.findViewById(R.id.ll_more);
        final LinearLayout contentLL = (LinearLayout) contentView.findViewById(R.id.ll_content);
        final ScrollView moreSV = (ScrollView) contentView.findViewById(R.id.sv_more);
        LinearLayout serviceLL = (LinearLayout) contentView.findViewById(R.id.ll_service);
        LinearLayout serviceWayLL = (LinearLayout) contentView.findViewById(R.id.ll_service_way);
        TagFlowLayout serviceWayTFL = (TagFlowLayout) contentView.findViewById(R.id.tfl_service_way);
        LinearLayout priceUnitLL = (LinearLayout) contentView.findViewById(R.id.ll_price_unit);
        TagFlowLayout priceUnitTFL = (TagFlowLayout) contentView.findViewById(R.id.tfl_price_unit);
        final LinearLayout servicePriceLL = (LinearLayout) contentView.findViewById(R.id.ll_service_price);
        final TagFlowLayout servicePriceTFL = (TagFlowLayout) contentView.findViewById(R.id.tfl_service_price);
        final LinearLayout sortLL = (LinearLayout) contentView.findViewById(R.id.ll_sort);
        TagFlowLayout sortTFL = (TagFlowLayout) contentView.findViewById(R.id.tfl_sort);
        LinearLayout demandLL = (LinearLayout) contentView.findViewById(R.id.ll_demand);
        LinearLayout demandServiceWayLL = (LinearLayout) contentView.findViewById(R.id.ll_demand_service_way);
        TagFlowLayout demandServiceWayTFL = (TagFlowLayout) contentView.findViewById(R.id.tfl_demand_service_way);
        LinearLayout payEscrowLL = (LinearLayout) contentView.findViewById(R.id.ll_pay_escrow);
        TagFlowLayout payEscrowTFL = (TagFlowLayout) contentView.findViewById(R.id.tfl_pay_escrow);
        final LinearLayout rangeOfBudgetLL = (LinearLayout) contentView.findViewById(R.id.ll_range_of_budget);
        TagFlowLayout rangeOfBudgetTFL = (TagFlowLayout) contentView.findViewById(R.id.tfl_range_of_budget);
        LinearLayout paymentCycleLL = (LinearLayout) contentView.findViewById(R.id.ll_payment_cycle);
        TagFlowLayout paymentCycleTFL = (TagFlowLayout) contentView.findViewById(R.id.tfl_payment_cycle);
        LinearLayout demandSortLL = (LinearLayout) contentView.findViewById(R.id.ll_demand_sort);
        TagFlowLayout demandSortTFL = (TagFlowLayout) contentView.findViewById(R.id.tfl_demand_sort);
        final LinearLayout bottomLL = (LinearLayout) contentView.findViewById(R.id.ll_bottom);
        Button resetBtn = (Button) contentView.findViewById(R.id.btn_reset);
        Button confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);

        switch (searchStatus) {
            case CommonConstant.SEARCH_STATUS_SERVICE:
                //        if (null != serveWayCodeAndValuesList && serveWayCodeAndValuesList.size() > 0) {
//            serviceWayLL.setVisibility(View.VISIBLE);
//
//            List<String> subclassNameList = new ArrayList<>();
//            subclassNameList.add(getString(R.string.unlimited));
//
//            if (serviceWayId == -1) {
//                serviceWaySet.clear();
//                serviceWaySet.add(0);
//            }
//
//            for (int i = 0; i < serveWayCodeAndValuesList.size(); i++) {
//                AdvancedFilterBean.ServeWayCodeAndValuesBean serveWayCodeAndValuesBean = serveWayCodeAndValuesList.get(i);
//
//                if (null != serveWayCodeAndValuesBean) {
//                    String value = TypeUtils.getString(serveWayCodeAndValuesBean.value, "");
//                    subclassNameList.add(value);
//                }
//            }
//
//            TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
//                @Override
//                public View getView(FlowLayout flowLayout, int i, Object o) {
//                    TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
//                    tagTv.setText(String.valueOf(o));
//                    return tagTv;
//                }
//            };
//
//            tagAdapter.setSelectedList(serviceWaySet);
//
//            serviceWayTFL.setMaxSelectCount(1);
//            serviceWayTFL.setAdapter(tagAdapter);
//            serviceWayTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//                @Override
//                public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
//                    boolean isSelected = serviceWaySet.contains(i);
//                    serviceWaySet.clear();
//
//                    if (isSelected) {
//                        serviceWayId = -1;
//                    } else {
//                        serviceWaySet.add(i);
//
//                        if (i == 0) {
//                            serviceWayId = -1;
//                        } else {
//                            AdvancedFilterBean.ServeWayCodeAndValuesBean serveWayCodeAndValuesBean = serveWayCodeAndValuesList.get(i - 1);
//
//                            if (null != serveWayCodeAndValuesBean) {
//                                serviceWayId = TypeUtils.getInteger(serveWayCodeAndValuesBean.code, -1);
//                            }
//                        }
//                    }
//                    return false;
//                }
//            });
//        } else {
//            serviceWayLL.setVisibility(View.GONE);
//        }

                if (null != unitCodeAndValuesList && unitCodeAndValuesList.size() > 0) {
                    priceUnitLL.setVisibility(View.VISIBLE);

                    final List<String> subclassNameList = new ArrayList<>();
                    subclassNameList.add(getString(R.string.unlimited));

                    if (unitId == -1) {
                        priceUnitSet.clear();
                        priceUnitSet.add(0);
                    }

                    for (int i = 0; i < unitCodeAndValuesList.size(); i++) {
                        AdvancedFilterBean.UnitCodeAndValuesBean unitCodeAndValuesBean = unitCodeAndValuesList.get(i);

                        if (null != unitCodeAndValuesBean) {
                            String value = TypeUtils.getString(unitCodeAndValuesBean.value, "");
                            subclassNameList.add(value);
                        }
                    }

                    priceUnitTagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    priceUnitTagAdapter.setSelectedList(priceUnitSet);

                    priceUnitTFL.setMaxSelectCount(1);
                    priceUnitTFL.setAdapter(priceUnitTagAdapter);
                    priceUnitTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                            boolean isSelected = priceUnitSet.contains(i);
                            priceUnitSet.clear();
                            servicePriceSet.clear();

                            minPrice = -1;
                            maxPrice = -1;

                            if (isSelected) {
                                unitId = -1;
                                servicePriceLL.setVisibility(View.GONE);
                            } else {
                                priceUnitSet.add(i);

                                if (i == 0) {
                                    unitId = -1;
                                    servicePriceLL.setVisibility(View.GONE);
                                } else {
                                    AdvancedFilterBean.UnitCodeAndValuesBean unitCodeAndValuesBean = unitCodeAndValuesList.get(i - 1);

                                    if (null != unitCodeAndValuesBean) {
                                        unitId = TypeUtils.getInteger(unitCodeAndValuesBean.code, -1);
                                        priceRangeVosList = unitCodeAndValuesBean.priceRangeVos;

                                        if (null != priceRangeVosList && priceRangeVosList.size() > 0) {
                                            servicePriceLL.setVisibility(View.VISIBLE);

                                            List<String> servicePriceSubclassNameList = new ArrayList<>();
                                            servicePriceSubclassNameList.add(getString(R.string.unlimited));

                                            if (minPrice == -1 && maxPrice == -1) {
                                                servicePriceSet.clear();
                                                servicePriceSet.add(0);
                                            }

                                            for (int j = 0; j < priceRangeVosList.size(); j++) {
                                                AdvancedFilterBean.UnitCodeAndValuesBean.PriceRangeVosBean priceRangeVosBean = priceRangeVosList.get(j);

                                                if (null != priceRangeVosBean) {
                                                    String value = TypeUtils.getString(priceRangeVosBean.value, "");
                                                    servicePriceSubclassNameList.add(value);
                                                }
                                            }

                                            TagAdapter servicePriceTagAdapter = new TagAdapter(servicePriceSubclassNameList) {
                                                @Override
                                                public View getView(FlowLayout flowLayout, int i, Object o) {
                                                    TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                                                    tagTv.setText(String.valueOf(o));
                                                    return tagTv;
                                                }
                                            };

                                            servicePriceTagAdapter.setSelectedList(servicePriceSet);

                                            servicePriceTFL.setMaxSelectCount(1);
                                            servicePriceTFL.setAdapter(servicePriceTagAdapter);
                                            servicePriceTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                                @Override
                                                public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                                                    boolean isSelected = servicePriceSet.contains(i);
                                                    servicePriceSet.clear();

                                                    if (isSelected) {
                                                        minPrice = -1;
                                                        maxPrice = -1;

                                                        servicePriceLL.setVisibility(View.GONE);
                                                    } else {
                                                        servicePriceSet.add(i);

                                                        if (i == 0) {
                                                            minPrice = -1;
                                                            maxPrice = -1;
                                                        } else {
                                                            AdvancedFilterBean.UnitCodeAndValuesBean.PriceRangeVosBean priceRangeVosBean = priceRangeVosList.get(i - 1);

                                                            if (null != priceRangeVosBean) {
                                                                minPrice = TypeUtils.getInteger(priceRangeVosBean.minPrice, -1);
                                                                maxPrice = TypeUtils.getInteger(priceRangeVosBean.maxPrice, -1);
                                                            }
                                                        }
                                                    }
                                                    return false;
                                                }
                                            });
                                        } else {
                                            servicePriceLL.setVisibility(View.GONE);
                                        }

                                        moreSV.measure(widthMeasureSpec, heightMeasureSpec);
                                        bottomLL.measure(widthMeasureSpec, heightMeasureSpec);

                                        int advancedFilterRecyclerViewHeight = moreSV.getMeasuredHeight();
                                        int bottomHeight = bottomLL.getMeasuredHeight();

                                        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 80);
                                        int tempHeight = height * 2 / 3;
                                        int tempPopupWindowHeight = advancedFilterRecyclerViewHeight + bottomHeight;
                                        int resultHeight;

                                        if (tempPopupWindowHeight < tempHeight) {
                                            resultHeight = tempPopupWindowHeight;
                                        } else {
                                            resultHeight = tempHeight;

                                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) moreSV.getLayoutParams();
                                            layoutParams.height = resultHeight - bottomHeight;
                                            moreSV.setLayoutParams(layoutParams);
                                        }

                                        ViewGroup.LayoutParams layoutParams = contentLL.getLayoutParams();
                                        layoutParams.height = resultHeight;
                                        contentLL.setLayoutParams(layoutParams);
                                    }
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    priceUnitLL.setVisibility(View.GONE);
                }

                if (unitId != -1 && null != priceRangeVosList && priceRangeVosList.size() > 0) {
                    servicePriceLL.setVisibility(View.VISIBLE);

                    List<String> servicePriceSubclassNameList = new ArrayList<>();
                    servicePriceSubclassNameList.add(getString(R.string.unlimited));

                    if (minPrice == -1 && maxPrice == -1) {
                        servicePriceSet.clear();
                        servicePriceSet.add(0);
                    }

                    for (int j = 0; j < priceRangeVosList.size(); j++) {
                        AdvancedFilterBean.UnitCodeAndValuesBean.PriceRangeVosBean priceRangeVosBean = priceRangeVosList.get(j);

                        if (null != priceRangeVosBean) {
                            String value = TypeUtils.getString(priceRangeVosBean.value, "");
                            servicePriceSubclassNameList.add(value);
                        }
                    }

                    servicePriceTagAdapter = new TagAdapter(servicePriceSubclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    servicePriceTagAdapter.setSelectedList(servicePriceSet);

                    servicePriceTFL.setMaxSelectCount(1);
                    servicePriceTFL.setAdapter(servicePriceTagAdapter);
                    servicePriceTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                            boolean isSelected = servicePriceSet.contains(i);
                            servicePriceSet.clear();

                            if (isSelected) {
                                minPrice = -1;
                                maxPrice = -1;

                                servicePriceLL.setVisibility(View.GONE);
                            } else {
                                servicePriceSet.add(i);

                                if (i == 0) {
                                    minPrice = -1;
                                    maxPrice = -1;
                                } else {
                                    AdvancedFilterBean.UnitCodeAndValuesBean.PriceRangeVosBean priceRangeVosBean = priceRangeVosList.get(i - 1);

                                    if (null != priceRangeVosBean) {
                                        minPrice = TypeUtils.getInteger(priceRangeVosBean.minPrice, -1);
                                        maxPrice = TypeUtils.getInteger(priceRangeVosBean.maxPrice, -1);
                                    }
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    servicePriceLL.setVisibility(View.GONE);
                }

                if (null != sortCodeAndValuesList && sortCodeAndValuesList.size() > 0) {
                    sortLL.setVisibility(View.VISIBLE);

                    List<String> subclassNameList = new ArrayList<>();

                    if (sortType == -1) {
                        sortTypeSet.clear();
                        sortTypeSet.add(0);
                    }

                    for (int i = 0; i < sortCodeAndValuesList.size(); i++) {
                        AdvancedFilterBean.SortCodeAndValuesBean sortCodeAndValuesBean = sortCodeAndValuesList.get(i);

                        if (null != sortCodeAndValuesBean) {
                            String value = TypeUtils.getString(sortCodeAndValuesBean.value, "");
                            subclassNameList.add(value);
                        }
                    }

                    sortTypeTagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    sortTypeTagAdapter.setSelectedList(sortTypeSet);

                    sortTFL.setMaxSelectCount(1);
                    sortTFL.setAdapter(sortTypeTagAdapter);
                    sortTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                            boolean isSelected = sortTypeSet.contains(i);
                            sortTypeSet.clear();

                            if (isSelected) {
                                sortType = -1;
                            } else {
                                sortTypeSet.add(i);

                                AdvancedFilterBean.SortCodeAndValuesBean sortCodeAndValuesBean = sortCodeAndValuesList.get(i);

                                if (null != sortCodeAndValuesBean) {
                                    sortType = TypeUtils.getInteger(sortCodeAndValuesBean.code, -1);
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    sortLL.setVisibility(View.GONE);
                }

                serviceLL.setVisibility(View.VISIBLE);
                demandLL.setVisibility(View.GONE);
                break;

            case CommonConstant.SEARCH_STATUS_DEMAND:
                if (null != trusteeshipCodeAndValuesList && trusteeshipCodeAndValuesList.size() > 0) {
                    payEscrowLL.setVisibility(View.VISIBLE);

                    List<String> subclassNameList = new ArrayList<>();
                    subclassNameList.add(getString(R.string.unlimited));

                    if (payEscrowId == -1) {
                        payEscrowSet.clear();
                        payEscrowSet.add(0);
                    }

                    for (int i = 0; i < trusteeshipCodeAndValuesList.size(); i++) {
                        DemandAdvancedFilterBean.TrusteeshipCodeAndValuesBean trusteeshipCodeAndValuesBean = trusteeshipCodeAndValuesList.get(i);

                        if (null != trusteeshipCodeAndValuesBean) {
                            String value = TypeUtils.getString(trusteeshipCodeAndValuesBean.value, "");
                            subclassNameList.add(value);
                        }
                    }

                    payEscrowTagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    payEscrowTagAdapter.setSelectedList(payEscrowSet);

                    payEscrowTFL.setMaxSelectCount(1);
                    payEscrowTFL.setAdapter(payEscrowTagAdapter);
                    payEscrowTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                            boolean isSelected = payEscrowSet.contains(i);
                            payEscrowSet.clear();

                            if (isSelected) {
                                payEscrowId = -1;
                            } else {
                                payEscrowSet.add(i);

                                if (i == 0) {
                                    payEscrowId = -1;
                                } else {
                                    DemandAdvancedFilterBean.TrusteeshipCodeAndValuesBean trusteeshipCodeAndValuesBean = trusteeshipCodeAndValuesList.get(i - 1);

                                    if (null != trusteeshipCodeAndValuesBean) {
                                        payEscrowId = TypeUtils.getInteger(trusteeshipCodeAndValuesBean.code, -1);
                                    }
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    payEscrowLL.setVisibility(View.GONE);
                }

                if (null != budgetRangeVosList && budgetRangeVosList.size() > 0) {
                    rangeOfBudgetLL.setVisibility(View.VISIBLE);

                    List<String> subclassNameList = new ArrayList<>();
                    subclassNameList.add(getString(R.string.unlimited));

                    if (demandMinPrice == -1 && demandMaxPrice == -1) {
                        rangeOfBudgetSet.clear();
                        rangeOfBudgetSet.add(0);
                    }

                    for (int j = 0; j < budgetRangeVosList.size(); j++) {
                        DemandAdvancedFilterBean.BudgetRangeVosBean budgetRangeVosBean = budgetRangeVosList.get(j);

                        if (null != budgetRangeVosBean) {
                            String value = TypeUtils.getString(budgetRangeVosBean.value, "");
                            subclassNameList.add(value);
                        }
                    }

                    rangeOfBudgetTagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    rangeOfBudgetTagAdapter.setSelectedList(rangeOfBudgetSet);

                    rangeOfBudgetTFL.setMaxSelectCount(1);
                    rangeOfBudgetTFL.setAdapter(rangeOfBudgetTagAdapter);
                    rangeOfBudgetTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                            boolean isSelected = rangeOfBudgetSet.contains(i);
                            rangeOfBudgetSet.clear();

                            if (isSelected) {
                                demandMinPrice = -1;
                                demandMaxPrice = -1;

                                rangeOfBudgetLL.setVisibility(View.GONE);
                            } else {
                                rangeOfBudgetSet.add(i);

                                if (i == 0) {
                                    demandMinPrice = -1;
                                    demandMaxPrice = -1;
                                } else {
                                    DemandAdvancedFilterBean.BudgetRangeVosBean budgetRangeVosBean = budgetRangeVosList.get(i - 1);

                                    if (null != budgetRangeVosBean) {
                                        demandMinPrice = TypeUtils.getInteger(budgetRangeVosBean.minPrice, -1);
                                        demandMaxPrice = TypeUtils.getInteger(budgetRangeVosBean.maxPrice, -1);
                                    }
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    rangeOfBudgetLL.setVisibility(View.GONE);
                }

                if (null != deliverCycleCodeAndValuesList && deliverCycleCodeAndValuesList.size() > 0) {
                    paymentCycleLL.setVisibility(View.VISIBLE);

                    List<String> subclassNameList = new ArrayList<>();
                    subclassNameList.add(getString(R.string.unlimited));

                    if (paymentCycleId == -1) {
                        paymentCycleSet.clear();
                        paymentCycleSet.add(0);
                    }

                    for (int i = 0; i < deliverCycleCodeAndValuesList.size(); i++) {
                        DemandAdvancedFilterBean.DeliverCycleCodeAndValuesBean deliverCycleCodeAndValuesBean = deliverCycleCodeAndValuesList.get(i);

                        if (null != deliverCycleCodeAndValuesBean) {
                            String value = TypeUtils.getString(deliverCycleCodeAndValuesBean.value, "");
                            subclassNameList.add(value);
                        }
                    }

                    paymentCycleTagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    paymentCycleTagAdapter.setSelectedList(paymentCycleSet);

                    paymentCycleTFL.setMaxSelectCount(1);
                    paymentCycleTFL.setAdapter(paymentCycleTagAdapter);
                    paymentCycleTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                            boolean isSelected = paymentCycleSet.contains(i);
                            paymentCycleSet.clear();

                            if (isSelected) {
                                paymentCycleId = -1;
                            } else {
                                paymentCycleSet.add(i);

                                if (i == 0) {
                                    payEscrowId = -1;
                                } else {
                                    DemandAdvancedFilterBean.DeliverCycleCodeAndValuesBean deliverCycleCodeAndValuesBean = deliverCycleCodeAndValuesList.get(i - 1);

                                    if (null != deliverCycleCodeAndValuesBean) {
                                        paymentCycleId = TypeUtils.getInteger(deliverCycleCodeAndValuesBean.code, -1);
                                    }
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    paymentCycleLL.setVisibility(View.GONE);
                }

                if (null != demandSortCodeAndValuesList && demandSortCodeAndValuesList.size() > 0) {
                    demandSortLL.setVisibility(View.VISIBLE);

                    List<String> subclassNameList = new ArrayList<>();

                    if (demandSortType == -1) {
                        demandSortTypeSet.clear();
                        demandSortTypeSet.add(0);
                    }

                    for (int i = 0; i < demandSortCodeAndValuesList.size(); i++) {
                        DemandAdvancedFilterBean.SortCodeAndValuesBean sortCodeAndValuesBean = demandSortCodeAndValuesList.get(i);

                        if (null != sortCodeAndValuesBean) {
                            String value = TypeUtils.getString(sortCodeAndValuesBean.value, "");
                            subclassNameList.add(value);
                        }
                    }

                    demandSortTypeTagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    demandSortTypeTagAdapter.setSelectedList(demandSortTypeSet);

                    demandSortTFL.setMaxSelectCount(1);
                    demandSortTFL.setAdapter(demandSortTypeTagAdapter);
                    demandSortTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                            boolean isSelected = demandSortTypeSet.contains(i);
                            demandSortTypeSet.clear();

                            if (isSelected) {
                                demandSortType = -1;
                            } else {
                                demandSortTypeSet.add(i);

                                DemandAdvancedFilterBean.SortCodeAndValuesBean sortCodeAndValuesBean = demandSortCodeAndValuesList.get(i);

                                if (null != sortCodeAndValuesBean) {
                                    demandSortType = TypeUtils.getInteger(sortCodeAndValuesBean.code, -1);
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    demandSortLL.setVisibility(View.GONE);
                }

                serviceLL.setVisibility(View.GONE);
                demandLL.setVisibility(View.VISIBLE);
                break;
        }

        moreSV.measure(widthMeasureSpec, heightMeasureSpec);
        bottomLL.measure(widthMeasureSpec, heightMeasureSpec);

        int advancedFilterRecyclerViewHeight = moreSV.getMeasuredHeight();
        int bottomHeight = bottomLL.getMeasuredHeight();

        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 80);
        int tempHeight = height * 2 / 3;
        int tempPopupWindowHeight = advancedFilterRecyclerViewHeight + bottomHeight;
        int resultHeight;

        if (tempPopupWindowHeight < tempHeight) {
            resultHeight = tempPopupWindowHeight;
        } else {
            resultHeight = tempHeight;

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) moreSV.getLayoutParams();
            layoutParams.height = resultHeight - bottomHeight;
            moreSV.setLayoutParams(layoutParams);
        }

        ViewGroup.LayoutParams layoutParams = contentLL.getLayoutParams();
        layoutParams.height = resultHeight;
        contentLL.setLayoutParams(layoutParams);

        morePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PopupWindowUtils.showAsDropDown(morePopupWindow, dividerLineView);

        moreLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != morePopupWindow && morePopupWindow.isShowing()) {
                    morePopupWindow.dismiss();
//                    unSelected();
                    checkSearchFiltrateConditionStatus();
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                    servicePriceLL.setVisibility(View.GONE);

                    unitId = -1;
                    minPrice = -1;
                    maxPrice = -1;

                    if (null != sortCodeAndValuesList && sortCodeAndValuesList.size() > 0) {
                        AdvancedFilterBean.SortCodeAndValuesBean sortCodeAndValuesBean = sortCodeAndValuesList.get(0);

                        if (null != sortCodeAndValuesBean) {
                            sortType = TypeUtils.getInteger(sortCodeAndValuesBean.value, -1);
                        }
                    }

                    priceUnitSet.clear();
                    priceUnitSet.add(0);

                    servicePriceSet.clear();
                    servicePriceSet.add(0);

                    sortTypeSet.clear();
                    sortTypeSet.add(0);

                    if (null != priceUnitTagAdapter) {
                        priceUnitTagAdapter.setSelectedList(priceUnitSet);
                    }

                    if (null != servicePriceTagAdapter) {
                        servicePriceTagAdapter.setSelectedList(servicePriceSet);
                    }

                    if (null != sortTypeTagAdapter) {
                        sortTypeTagAdapter.setSelectedList(sortTypeSet);
                    }
                } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                    payEscrowId = -1;
                    demandMinPrice = -1;
                    demandMaxPrice = -1;
                    paymentCycleId = -1;
                    demandSortType = -1;

                    payEscrowSet.clear();
                    payEscrowSet.add(0);

                    rangeOfBudgetSet.clear();
                    rangeOfBudgetSet.add(0);

                    paymentCycleSet.clear();
                    paymentCycleSet.add(0);

                    demandSortTypeSet.clear();
                    demandSortTypeSet.add(0);

                    if (null != payEscrowTagAdapter) {
                        payEscrowTagAdapter.setSelectedList(payEscrowSet);
                    }

                    if (null != rangeOfBudgetTagAdapter) {
                        rangeOfBudgetTagAdapter.setSelectedList(rangeOfBudgetSet);
                    }

                    if (null != paymentCycleTagAdapter) {
                        paymentCycleTagAdapter.setSelectedList(paymentCycleSet);
                    }

                    if (null != demandSortTypeTagAdapter) {
                        demandSortTypeTagAdapter.setSelectedList(demandSortTypeSet);
                    }
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != morePopupWindow && morePopupWindow.isShowing()) {
                    morePopupWindow.dismiss();
//                    unSelected();
                    checkSearchFiltrateConditionStatus();
                    setSearchList(keywords);
                    mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                }
            }
        });
    }

    private void checkSearchFiltrateConditionStatus() {
        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
            if (categoryId == -1) {
                serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
            } else {
                serviceTv.setTextColor(getResources().getColor(R.color.text_orange));
                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
            }

            if (serviceWayId == -1) {
                modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
            } else {
                modeTv.setTextColor(getResources().getColor(R.color.text_orange));
                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
            }

            if (minPrice == -1 && maxPrice == -1 && unitId == -1 && sortType == -1) {
                moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
            } else {
                moreTv.setTextColor(getResources().getColor(R.color.text_orange));
                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
            }
        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
            if (categoryId == -1) {
                serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
            } else {
                serviceTv.setTextColor(getResources().getColor(R.color.text_orange));
                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
            }

            if (demandServiceWayId == -1) {
                modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
            } else {
                modeTv.setTextColor(getResources().getColor(R.color.text_orange));
                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
            }

            if (payEscrowId == -1 && demandMinPrice == -1 && demandMaxPrice == -1 && paymentCycleId == -1 && demandSortType == -1) {
                moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
            } else {
                moreTv.setTextColor(getResources().getColor(R.color.text_orange));
                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
            }
        }

        if (TextUtils.isEmpty(provinceCode)) {
            cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
            cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
        } else {
            cityTv.setTextColor(getResources().getColor(R.color.text_orange));
            cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
        }
    }

    @OnClick({R.id.ll_search_status, R.id.search_view, R.id.tv_cancel, R.id.ll_service, R.id.ll_city, R.id.ll_mode, R.id.ll_more})
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

                if (null != servicePopupWindow && servicePopupWindow.isShowing()) {
                    servicePopupWindow.dismiss();
                }

                if (null != cityPopupWindow && cityPopupWindow.isShowing()) {
                    cityPopupWindow.dismiss();
                }

                if (null != wayPopupWindow && wayPopupWindow.isShowing()) {
                    wayPopupWindow.dismiss();
                }

                if (null != morePopupWindow && morePopupWindow.isShowing()) {
                    morePopupWindow.dismiss();
                }

                unSelected();
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

            case R.id.ll_service:
                searchView.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

                if (null != categoryList && categoryList.size() > 0) {
                    isShowCategoryPopupWindow = false;

                    if (null != servicePopupWindow && servicePopupWindow.isShowing()) {
                        servicePopupWindow.dismiss();
//                        unSelected();
                        checkSearchFiltrateConditionStatus();
                    } else {
                        selectService();

                        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                            if (serviceWayId == -1) {
                                modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                modeTv.setTextColor(getResources().getColor(R.color.text_orange));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (minPrice == -1 && maxPrice == -1 && unitId == -1 && sortType == -1) {
                                moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                moreTv.setTextColor(getResources().getColor(R.color.text_orange));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }
                        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                            if (demandServiceWayId == -1) {
                                modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                modeTv.setTextColor(getResources().getColor(R.color.text_orange));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (payEscrowId == -1 && demandMinPrice == -1 && demandMaxPrice == -1 && paymentCycleId == -1 && demandSortType == -1) {
                                moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                moreTv.setTextColor(getResources().getColor(R.color.text_orange));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }
                        }

                        if (TextUtils.isEmpty(provinceCode)) {
                            cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                            cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                        } else {
                            cityTv.setTextColor(getResources().getColor(R.color.text_orange));
                            cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                        }
                    }
                } else {
                    isShowCategoryPopupWindow = true;

                    if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                        mPresenter.fetchCategoryListRequest();
                    } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                        mPresenter.fetchDemandCategoryListRequest();
                    }
                }

                if (null != cityPopupWindow && cityPopupWindow.isShowing()) {
                    cityPopupWindow.dismiss();
                }

                if (null != wayPopupWindow && wayPopupWindow.isShowing()) {
                    wayPopupWindow.dismiss();
                }

                if (null != morePopupWindow && morePopupWindow.isShowing()) {
                    morePopupWindow.dismiss();
                }
                break;

            case R.id.ll_city:
                searchView.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

                if (null != addressList && addressList.size() > 0) {
                    isShowAddressPopupWindow = false;

                    if (null != cityPopupWindow && cityPopupWindow.isShowing()) {
                        cityPopupWindow.dismiss();
//                        unSelected();
                        checkSearchFiltrateConditionStatus();
                    } else {
                        selectCity();

                        if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                            if (categoryId == -1) {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_orange));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (serviceWayId == -1) {
                                modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                modeTv.setTextColor(getResources().getColor(R.color.text_orange));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (minPrice == -1 && maxPrice == -1 && unitId == -1 && sortType == -1) {
                                moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                moreTv.setTextColor(getResources().getColor(R.color.text_orange));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }
                        } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                            if (categoryId == -1) {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_orange));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (demandServiceWayId == -1) {
                                modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                modeTv.setTextColor(getResources().getColor(R.color.text_orange));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (payEscrowId == -1 && demandMinPrice == -1 && demandMaxPrice == -1 && paymentCycleId == -1 && demandSortType == -1) {
                                moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                moreTv.setTextColor(getResources().getColor(R.color.text_orange));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }
                        }
                    }
                } else {
                    isShowAddressPopupWindow = true;
                    mPresenter.fetchAddressListRequest();
                }

                if (null != servicePopupWindow && servicePopupWindow.isShowing()) {
                    servicePopupWindow.dismiss();
                }

                if (null != wayPopupWindow && wayPopupWindow.isShowing()) {
                    wayPopupWindow.dismiss();
                }

                if (null != morePopupWindow && morePopupWindow.isShowing()) {
                    morePopupWindow.dismiss();
                }
                break;

            case R.id.ll_mode:
                isShowMorePopupWindow = false;
                searchView.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

                if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                    if (null != advancedFilterBean) {
                        isShowModePopupWindow = false;

                        if (null != wayPopupWindow && wayPopupWindow.isShowing()) {
                            wayPopupWindow.dismiss();
//                            unSelected();
                            checkSearchFiltrateConditionStatus();
                        } else {
                            selectMode();

                            if (categoryId == -1) {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_orange));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (TextUtils.isEmpty(provinceCode)) {
                                cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                cityTv.setTextColor(getResources().getColor(R.color.text_orange));
                                cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (minPrice == -1 && maxPrice == -1 && unitId == -1 && sortType == -1) {
                                moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                moreTv.setTextColor(getResources().getColor(R.color.text_orange));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }
                        }
                    } else {
                        isShowModePopupWindow = true;
                        mPresenter.fetchAdvancedFilterRequest();
                    }
                } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                    if (null != demandAdvancedFilterBean) {
                        isShowModePopupWindow = false;

                        if (null != wayPopupWindow && wayPopupWindow.isShowing()) {
                            wayPopupWindow.dismiss();
//                            unSelected();
                            checkSearchFiltrateConditionStatus();
                        } else {
                            selectMode();

                            if (categoryId == -1) {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_orange));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (TextUtils.isEmpty(provinceCode)) {
                                cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                cityTv.setTextColor(getResources().getColor(R.color.text_orange));
                                cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (payEscrowId == -1 && demandMinPrice == -1 && demandMaxPrice == -1 && paymentCycleId == -1 && demandSortType == -1) {
                                moreTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                moreTv.setTextColor(getResources().getColor(R.color.text_orange));
                                moreArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }
                        }
                    } else {
                        isShowModePopupWindow = true;
                        mPresenter.fetchDemandAdvancedFilterRequest();
                    }
                }

                if (null != servicePopupWindow && servicePopupWindow.isShowing()) {
                    servicePopupWindow.dismiss();
                }

                if (null != cityPopupWindow && cityPopupWindow.isShowing()) {
                    cityPopupWindow.dismiss();
                }

                if (null != morePopupWindow && morePopupWindow.isShowing()) {
                    morePopupWindow.dismiss();
                }
                break;

            case R.id.ll_more:
                isShowModePopupWindow = false;
                searchView.setFocusable(false);

                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }

                if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                    if (null != advancedFilterBean) {
                        isShowMorePopupWindow = false;

                        if (null != morePopupWindow && morePopupWindow.isShowing()) {
                            morePopupWindow.dismiss();
//                            unSelected();
                            checkSearchFiltrateConditionStatus();
                        } else {
                            selectMore();

                            if (categoryId == -1) {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_orange));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (TextUtils.isEmpty(provinceCode)) {
                                cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                cityTv.setTextColor(getResources().getColor(R.color.text_orange));
                                cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (serviceWayId == -1) {
                                modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                modeTv.setTextColor(getResources().getColor(R.color.text_orange));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }
                        }
                    } else {
                        isShowMorePopupWindow = true;
                        mPresenter.fetchAdvancedFilterRequest();
                    }
                } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                    if (null != demandAdvancedFilterBean) {
                        isShowMorePopupWindow = false;

                        if (null != morePopupWindow && morePopupWindow.isShowing()) {
                            morePopupWindow.dismiss();
//                            unSelected();
                            checkSearchFiltrateConditionStatus();
                        } else {
                            selectMore();

                            if (categoryId == -1) {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                serviceTv.setTextColor(getResources().getColor(R.color.text_orange));
                                serviceArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (TextUtils.isEmpty(provinceCode)) {
                                cityTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                cityTv.setTextColor(getResources().getColor(R.color.text_orange));
                                cityArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }

                            if (demandServiceWayId == -1) {
                                modeTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_down);
                            } else {
                                modeTv.setTextColor(getResources().getColor(R.color.text_orange));
                                modeArrowIv.setBackgroundResource(R.mipmap.ic_search_arrow_up_blue);
                            }
                        }
                    } else {
                        isShowMorePopupWindow = true;
                        mPresenter.fetchDemandAdvancedFilterRequest();
                    }
                }

                if (null != servicePopupWindow && servicePopupWindow.isShowing()) {
                    servicePopupWindow.dismiss();
                }

                if (null != cityPopupWindow && cityPopupWindow.isShowing()) {
                    cityPopupWindow.dismiss();
                }

                if (null != wayPopupWindow && wayPopupWindow.isShowing()) {
                    wayPopupWindow.dismiss();
                }
                break;
        }
    }

    @Override
    protected void onLoadMore() {
        mPresenter.loadMore(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
    }

    @Override
    protected void onSwipeRefresh() {
        setSearchList(keywords);
        mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
    }

    @Override
    public void onShowAddressList(List<IsAllBean> addressList) {
        if (null != addressList && addressList.size() > 0) {
            if (!TextUtils.isEmpty(provinceCode)) {
                for (int i = 0; i < addressList.size(); i++) {
                    IsAllBean isAllBean = addressList.get(i);

                    if (null != isAllBean) {
                        String provinceCode = TypeUtils.getString(isAllBean.code, "");
                        List<AddressSubBean> subList = isAllBean.sub;

                        if (provinceCode.equals(this.provinceCode)) {
                            isAllBean.isSelected = true;

                            if (!TextUtils.isEmpty(cityCode)) {
                                if (null != subList && subList.size() > 0) {
                                    for (int j = 0; j < subList.size(); j++) {
                                        AddressSubBean addressSubBean = subList.get(j);

                                        if (null != addressSubBean) {
                                            String cityCode = TypeUtils.getString(addressSubBean.code, "");

                                            if (!TextUtils.isEmpty(cityCode) && !TextUtils.isEmpty(this.cityCode) && cityCode.equals(this.cityCode)) {
                                                addressSubBean.isSelected = true;
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (null != subList && subList.size() > 0) {
                                    AddressSubBean addressSubBean = subList.get(0);

                                    if (null != addressSubBean) {
                                        addressSubBean.isSelected = true;
                                    }
                                }
                            }

                            cityList = subList;
                        }
                    }
                }
            }

            this.addressList = addressList;

            if (isShowAddressPopupWindow) {
                selectCity();
            }
        } else {
            showToast(getString(R.string.no_data));
        }

        checkSearchFiltrateConditionStatus();
    }

    @Override
    public void onShowAdvancedFilterBean(AdvancedFilterBean advancedFilterBean) {
        if (null != advancedFilterBean) {
            this.advancedFilterBean = advancedFilterBean;

            serveWayCodeAndValuesList = advancedFilterBean.serveWayCodeAndValues;
            unitCodeAndValuesList = advancedFilterBean.unitCodeAndValues;
            sortCodeAndValuesList = advancedFilterBean.sortCodeAndValues;

//            if (null != serviceWaySet) {
//                serviceWaySet.clear();
//                serviceWaySet.add(0);
//            }

            if (null != priceUnitSet) {
                priceUnitSet.clear();
                priceUnitSet.add(0);
            }

            if (null != servicePriceSet) {
                servicePriceSet.clear();
                servicePriceSet.add(0);
            }

            if (null != sortTypeSet) {
                sortTypeSet.clear();
                sortTypeSet.add(0);
            }

            serviceWayId = -1;
            unitId = -1;
            minPrice = -1;
            maxPrice = -1;
            sortType = -1;

            if (null != wayList && wayList.size() > 0) {
                wayList.clear();
            }

            WayBean wayBean = new WayBean();
            wayBean.code = -1;
            wayBean.value = getString(R.string.unlimited);
            wayBean.isSelected = true;
            wayList.add(wayBean);

            if (null != serveWayCodeAndValuesList && serveWayCodeAndValuesList.size() > 0) {
                for (int i = 0; i < serveWayCodeAndValuesList.size(); i++) {
                    AdvancedFilterBean.ServeWayCodeAndValuesBean serveWayCodeAndValuesBean = serveWayCodeAndValuesList.get(i);

                    if (null != serveWayCodeAndValuesBean) {
                        int code = TypeUtils.getInteger(serveWayCodeAndValuesBean.code, -1);
                        String value = TypeUtils.getString(serveWayCodeAndValuesBean.value, "");

                        WayBean tempWayBean = new WayBean();
                        tempWayBean.code = code;
                        tempWayBean.value = value;
                        tempWayBean.isSelected = false;
                        wayList.add(tempWayBean);
                    }
                }
            }

            if (isShowModePopupWindow) {
                selectMode();
            } else if (isShowMorePopupWindow) {
                selectMore();
            }
        } else {
            showToast(getString(R.string.no_data));
        }

        checkSearchFiltrateConditionStatus();
    }

    @Override
    public void onShowDemandAdvancedFilterBean(DemandAdvancedFilterBean demandAdvancedFilterBean) {
        if (null != demandAdvancedFilterBean) {
            this.demandAdvancedFilterBean = demandAdvancedFilterBean;

            demandServeWayCodeAndValuesList = demandAdvancedFilterBean.serveWayCodeAndValues;
            trusteeshipCodeAndValuesList = demandAdvancedFilterBean.trusteeshipCodeAndValues;
            budgetRangeVosList = demandAdvancedFilterBean.budgetRangeVos;
            deliverCycleCodeAndValuesList = demandAdvancedFilterBean.deliverCycleCodeAndValues;
            demandSortCodeAndValuesList = demandAdvancedFilterBean.sortCodeAndValues;

//            if (null != demandServiceWaySet) {
//                demandServiceWaySet.clear();
//                demandServiceWaySet.add(0);
//            }

            if (null != payEscrowSet) {
                payEscrowSet.clear();
                payEscrowSet.add(0);
            }

            if (null != rangeOfBudgetSet) {
                rangeOfBudgetSet.clear();
                rangeOfBudgetSet.add(0);
            }

            if (null != paymentCycleSet) {
                paymentCycleSet.clear();
                paymentCycleSet.add(0);
            }

            if (null != demandSortTypeSet) {
                demandSortTypeSet.clear();
                demandSortTypeSet.add(0);
            }

            demandServiceWayId = -1;
            payEscrowId = -1;
            demandMinPrice = -1;
            demandMaxPrice = -1;
            paymentCycleId = -1;
            demandSortType = -1;

            if (null != wayList && wayList.size() > 0) {
                wayList.clear();
            }

            WayBean wayBean = new WayBean();
            wayBean.code = -1;
            wayBean.value = getString(R.string.unlimited);
            wayBean.isSelected = true;
            wayList.add(wayBean);

            if (null != demandServeWayCodeAndValuesList && demandServeWayCodeAndValuesList.size() > 0) {
                for (int i = 0; i < demandServeWayCodeAndValuesList.size(); i++) {
                    DemandAdvancedFilterBean.ServeWayCodeAndValuesBean serveWayCodeAndValuesBean = demandServeWayCodeAndValuesList.get(i);

                    if (null != serveWayCodeAndValuesBean) {
                        int code = TypeUtils.getInteger(serveWayCodeAndValuesBean.code, -1);
                        String value = TypeUtils.getString(serveWayCodeAndValuesBean.value, "");

                        WayBean tempWayBean = new WayBean();
                        tempWayBean.code = code;
                        tempWayBean.value = value;
                        tempWayBean.isSelected = false;
                        wayList.add(tempWayBean);
                    }
                }
            }

            if (isShowModePopupWindow) {
                selectMode();
            } else if (isShowMorePopupWindow) {
                selectMore();
            }
        } else {
            showToast(getString(R.string.no_data));
        }

        checkSearchFiltrateConditionStatus();
    }

    @Override
    public void onShowCategoryList(List<CategoryBean> categoryList) {
        if (null != categoryList && categoryList.size() > 0) {
            if (categoryId != -1) {
                for (int i = 0; i < categoryList.size(); i++) {
                    CategoryBean categoryBean = categoryList.get(i);

                    if (null != categoryBean) {
                        int code = TypeUtils.getInteger(categoryBean.code, -1);

                        if (code == categoryId) {
                            List<CategoryBean.FirstSubBean> firstSubList = categoryBean.sub;
                            categoryBean.isSelected = true;

                            if (null != firstSubList && firstSubList.size() > 0) {
                                for (int j = 0; j < firstSubList.size(); j++) {
                                    CategoryBean.FirstSubBean firstSubBean = firstSubList.get(j);

                                    if (null != firstSubBean) {
                                        List<CategoryBean.FirstSubBean.SecondSubBean> secondSubList = firstSubBean.sub;
                                        Set<Integer> isSelectedSet = new HashSet<>();
                                        List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                                        if (null != secondSubList && secondSubList.size() > 0) {
                                            for (int k = 0; k < secondSubList.size(); k++) {
                                                CategoryBean.FirstSubBean.SecondSubBean secondSubBean = secondSubList.get(k);

                                                if (null != secondSubBean) {
                                                    int categorySubclassId = TypeUtils.getInteger(secondSubBean.code, -1);

                                                    if (null != categoryPropertyValueIdList && categoryPropertyValueIdList.size() > 0) {
                                                        for (int l = 0; l < categoryPropertyValueIdList.size(); l++) {
                                                            int categoryPropertyValueId = categoryPropertyValueIdList.get(l);

                                                            if (categoryPropertyValueId == categorySubclassId) {
                                                                isSelectedSet.add(k);
                                                                isSelectedCategoryIdList.add(categorySubclassId);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        firstSubBean.isSelectedSet = isSelectedSet;
                                        firstSubBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                                    }
                                }
                            }

                            categorySubclassList = firstSubList;
                        }
                    }
                }
            }

            this.categoryList = categoryList;

            if (isShowCategoryPopupWindow) {
                selectService();
            }
        } else {
            showToast(getString(R.string.no_data));
        }

        checkSearchFiltrateConditionStatus();
    }

    @Override
    public void onShowRecommendList(List<RecommendListBean.RecommendBean> recommendList) {
        if (null != recommendList && recommendList.size() > 0) {
            recommendAdapter.setDateList(recommendList);
            showContentView();
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onShowRecommendDemandList(List<RecommendDemandBean> recommendDemandList) {
        if (null != recommendDemandList && recommendDemandList.size() > 0) {
            recommendDemandAdapter.setDateList(recommendDemandList);
            showContentView();
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onSaveKeywordsSearchLog() {
        searchView.setFocusable(false);

        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }

        setSearchList(keywords);
        mPresenter.refresh(searchStatus, categoryPropertyValueIdList, keywords, categoryId, provinceCode, cityCode, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
    }

}
