package com.allintask.lingdao.presenter.user;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.bean.recommend.RecommendDemandListBean;
import com.allintask.lingdao.bean.recommend.RecommendListBean;
import com.allintask.lingdao.bean.user.AddressListBean;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.AdvancedFilterBean;
import com.allintask.lingdao.bean.user.CategoryBean;
import com.allintask.lingdao.bean.user.DemandAdvancedFilterBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ISearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/1.
 */

public class SearchPresenter extends BasePresenter<ISearchView> {

    private IUserModel userModel;
    private List<RecommendListBean.RecommendBean> recommendList;
    private List<RecommendDemandBean> recommendDemandList;

    private int pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;

    public SearchPresenter() {
        userModel = new UserModel();
        recommendList = new ArrayList<>();
        recommendDemandList = new ArrayList<>();
    }

    public void fetchCategoryListRequest() {
        OkHttpRequest fetchCategoryListRequest = userModel.fetchCategoryListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchCategoryListRequest);
    }

    public void fetchDemandCategoryListRequest() {
        OkHttpRequest fetchDemandCategoryListRequest = userModel.fetchDemandCategoryListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchDemandCategoryListRequest);
    }

    public void fetchAddressListRequest() {
        OkHttpRequest fetchAddressListRequest = userModel.fetchAddressListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchAddressListRequest);
    }

    public void fetchAdvancedFilterRequest() {
        OkHttpRequest fetchAdvancedFilterRequest = userModel.fetchAdvancedFilterRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchAdvancedFilterRequest);
    }

    public void fetchDemandAdvancedFilterRequest() {
        OkHttpRequest fetchDemandAdvancedFilterRequest = userModel.fetchDemandAdvancedFilterRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchDemandAdvancedFilterRequest);
    }

    public void refresh(int searchStatus, List<Integer> categoryPropertyValueIds, String keywords, int categoryId, String provinceCode, String cityCode, int serveWayId, int unitId, int minPrice, int maxPrice, int sortType, int demandServiceWayId, int isTrusteeship, int demandMinPrice, int demandMaxPrice, int deliverCycleId, int demandSortType) {
        if (!getView().isLoadingMore()) {
            pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
            getView().setRefresh(true);

            if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                searchRequest(true, categoryPropertyValueIds, keywords, categoryId, provinceCode, cityCode, serveWayId, unitId, maxPrice, minPrice, sortType);
            } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                searchDemandRequest(true, categoryPropertyValueIds, keywords, categoryId, provinceCode, cityCode, demandServiceWayId, isTrusteeship, demandMinPrice, demandMaxPrice, deliverCycleId, demandSortType);
            }
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore(int searchStatus, List<Integer> categoryPropertyValueIds, String keywords, int categoryId, String provinceCode, String cityCode, int serveWayId, int unitId, int minPrice, int maxPrice, int sortType, int demandServiceWayId, int isTrusteeship, int demandMinPrice, int demandMaxPrice, int deliverCycleId, int demandSortType) {
        if (!getView().isRefreshing()) {
            getView().setLoadMore(true);

            if (searchStatus == CommonConstant.SEARCH_STATUS_SERVICE) {
                searchRequest(false, categoryPropertyValueIds, keywords, categoryId, provinceCode, cityCode, serveWayId, unitId, minPrice, maxPrice, sortType);
            } else if (searchStatus == CommonConstant.SEARCH_STATUS_DEMAND) {
                searchDemandRequest(false, categoryPropertyValueIds, keywords, categoryId, provinceCode, cityCode, demandServiceWayId, isTrusteeship, demandMinPrice, demandMaxPrice, deliverCycleId, demandSortType);
            }
        } else {
            getView().setLoadMore(false);
        }
    }

    private void searchRequest(boolean isRefresh, List<Integer> categoryPropertyValueIds, String keywords, int categoryId, String provinceCode, String cityCode, int serveWayId, int unitId, int minPrice, int maxPrice, int sortType) {
        if (isRefresh) {
            recommendList.clear();
        }

        Map<String, Object> searchMap = new HashMap<>();

        if (pageNumber != -1) {
            searchMap.put(ApiKey.COMMON_PAGE_NUM, pageNumber);
        }

        if (null != categoryPropertyValueIds && categoryPropertyValueIds.size() > 0) {
            searchMap.put(ApiKey.USER_CATEGORY_PROPERTY_VALUE_IDS, categoryPropertyValueIds);
        }

        if (!TextUtils.isEmpty(keywords)) {
            searchMap.put(ApiKey.USER_KEYWORDS, keywords);
        }

        if (categoryId != -1) {
            searchMap.put(ApiKey.USER_CATEGORY_ID, categoryId);
        }

        if (!TextUtils.isEmpty(provinceCode)) {
            searchMap.put(ApiKey.USER_PROVINCE_CODE, provinceCode);
        }

        if (!TextUtils.isEmpty(cityCode)) {
            searchMap.put(ApiKey.USER_CITY_CODE, cityCode);
        }

        if (serveWayId != -1) {
            searchMap.put(ApiKey.USER_SERVE_WAY_ID, serveWayId);
        }

        if (unitId != -1) {
            searchMap.put(ApiKey.USER_UNIT_ID, unitId);
        }

        if (minPrice != -1) {
            searchMap.put(ApiKey.USER_PRICE_MIN, minPrice);
        }

        if (maxPrice != -1) {
            searchMap.put(ApiKey.USER_PRICE_MAX, maxPrice);
        }

        if (sortType != -1) {
            searchMap.put(ApiKey.USER_ORDER, sortType);
        }

        OkHttpRequest searchRequest = userModel.searchRequest(searchMap);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, searchRequest);
    }

    private void searchDemandRequest(boolean isRefresh, List<Integer> categoryPropertyValueIds, String keywords, int categoryId, String provinceCode, String cityCode, int demandServiceWayId, int isTrusteeship, int demandMinPrice, int demandMaxPrice, int deliverCycleId, int demandSortType) {
        if (isRefresh) {
            recommendDemandList.clear();
        }

        Map<String, Object> searchMap = new HashMap<>();

        if (pageNumber != -1) {
            searchMap.put(ApiKey.COMMON_PAGE_NUM, pageNumber);
        }

        if (null != categoryPropertyValueIds && categoryPropertyValueIds.size() > 0) {
            searchMap.put(ApiKey.USER_CATEGORY_PROPERTY_VALUE_IDS, categoryPropertyValueIds);
        }

        if (!TextUtils.isEmpty(keywords)) {
            searchMap.put(ApiKey.USER_KEYWORDS, keywords);
        }

        if (categoryId != -1) {
            searchMap.put(ApiKey.USER_CATEGORY_ID, categoryId);
        }

        if (!TextUtils.isEmpty(provinceCode)) {
            searchMap.put(ApiKey.USER_PROVINCE_CODE, provinceCode);
        }

        if (!TextUtils.isEmpty(cityCode)) {
            searchMap.put(ApiKey.USER_CITY_CODE, cityCode);
        }

        if (demandServiceWayId != -1) {
            searchMap.put(ApiKey.USER_SERVE_WAY_ID, demandServiceWayId);
        }

        if (isTrusteeship != -1) {
            searchMap.put(ApiKey.USER_IS_TRUSTEESHIP, isTrusteeship);
        }

        if (demandMinPrice != -1) {
            searchMap.put(ApiKey.USER_PRICE_MIN, demandMinPrice);
        }

        if (demandMaxPrice != -1) {
            searchMap.put(ApiKey.USER_PRICE_MAX, demandMaxPrice);
        }

        if (deliverCycleId != -1) {
            searchMap.put(ApiKey.USER_DELIVER_CYCLE_ID, deliverCycleId);
        }

        if (demandSortType != -1) {
            searchMap.put(ApiKey.USER_ORDER, demandSortType);
        }

        OkHttpRequest searchDemandRequest = userModel.searchDemandRequest(searchMap);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, searchDemandRequest);
    }

    public void saveKeyWordsSearchLogRequest(int type, String keywords) {
        OkHttpRequest saveKeyWordsSearchLogRequest = userModel.saveKeyWordsSearchLogRequest(type, keywords);
        addJsonStringRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, saveKeyWordsSearchLogRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_SERVE) || requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_DEMAND)) {
            if (success) {
                if (null != getView()) {
                    List<CategoryBean> categoryList = new ArrayList<>();
                    CategoryBean categoryBean = new CategoryBean();
                    categoryBean.isSelected = false;
                    categoryBean.code = -1;
                    categoryBean.name = getView().getParentContext().getString(R.string.all_category);
                    categoryList.add(categoryBean);

                    List<CategoryBean> tempCategoryList = JSONObject.parseArray(data, CategoryBean.class);
                    categoryList.addAll(tempCategoryList);
                    getView().onShowCategoryList(categoryList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_ADDRESS_LIST)) {
            if (success) {
                AddressListBean addressListBean = JSONObject.parseObject(data, AddressListBean.class);

                if (null != addressListBean) {
                    List<IsAllBean> tempIsAllList = addressListBean.isAll;
                    List<IsAllBean> isAllList = new ArrayList<>();

                    if (null != getView()) {
//                        IsAllBean isAllBean = new IsAllBean();
//                        isAllBean.name = getView().getParentContext().getString(R.string.nationwide);
//                        isAllList.add(isAllBean);

                        if (null != tempIsAllList && tempIsAllList.size() > 0) {
                            for (int i = 0; i < tempIsAllList.size(); i++) {
                                IsAllBean tempIsAllBean = tempIsAllList.get(i);

                                if (null != tempIsAllBean) {
                                    List<AddressSubBean> tempAddressSubList = tempIsAllBean.sub;
                                    List<AddressSubBean> addressSubList = new ArrayList<>();

                                    AddressSubBean addressSubBean = new AddressSubBean();
                                    addressSubBean.name = getView().getParentContext().getString(R.string.all);
                                    addressSubList.add(addressSubBean);
                                    addressSubList.addAll(tempAddressSubList);

                                    tempIsAllBean.sub = addressSubList;
                                }
                            }

                            isAllList.addAll(tempIsAllList);
                        }

                        getView().onShowAddressList(isAllList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SERVE_GET_ADVANCED_FILTER)) {
            if (success) {
                AdvancedFilterBean advancedFilterBean = JSONObject.parseObject(data, AdvancedFilterBean.class);

                if (null != getView()) {
                    getView().onShowAdvancedFilterBean(advancedFilterBean);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_DEMAND_GET_ADVANCED_FILTER)) {
            if (success) {
                DemandAdvancedFilterBean demandAdvancedFilterBean = JSONObject.parseObject(data, DemandAdvancedFilterBean.class);

                if (null != getView()) {
                    getView().onShowDemandAdvancedFilterBean(demandAdvancedFilterBean);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SERVE_SEARCH)) {
            if (success) {
                RecommendListBean recommendListBean = JSONObject.parseObject(data, RecommendListBean.class);

                if (null != recommendListBean) {
                    List<RecommendListBean.RecommendBean> recommendList = recommendListBean.solrServes;

                    if (null != recommendList && recommendList.size() > 0) {
                        this.recommendList.addAll(recommendList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowRecommendList(this.recommendList);
                    }
                } else if (null == this.recommendList || this.recommendList.size() <= 0) {
                    if (null != getView()) {
                        getView().showEmptyView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                    getView().showEmptyView();
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_DEMAND_SEARCH)) {
            if (success) {
                RecommendDemandListBean recommendDemandListBean = JSONObject.parseObject(data, RecommendDemandListBean.class);

                if (null != recommendDemandListBean) {
                    List<RecommendDemandBean> recommendDemandList = recommendDemandListBean.solrDemands;

                    if (null != recommendDemandList && recommendDemandList.size() > 0) {
                        this.recommendDemandList.addAll(recommendDemandList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowRecommendDemandList(this.recommendDemandList);
                    }
                } else if (null == this.recommendDemandList || this.recommendDemandList.size() <= 0) {
                    if (null != getView()) {
                        getView().showEmptyView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                    getView().showEmptyView();
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SAVE_KEYWORDS_SEARCH_LOG)) {
            if (null != getView()) {
                getView().onSaveKeywordsSearchLog();
            }
        }
    }

}
