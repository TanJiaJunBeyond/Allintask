package com.allintask.lingdao.presenter.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendDataBean;
import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.bean.recommend.RecommendDemandListBean;
import com.allintask.lingdao.bean.recommend.RecommendGridBean;
import com.allintask.lingdao.bean.recommend.RecommendGridListBean;
import com.allintask.lingdao.bean.recommend.RecommendListBean;
import com.allintask.lingdao.bean.user.AddressListBean;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.AdvancedFilterBean;
import com.allintask.lingdao.bean.user.BannerBean;
import com.allintask.lingdao.bean.user.CategoryBean;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.bean.user.DemandAdvancedFilterBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.main.IRecommendView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/3.
 */

public class RecommendPresenter extends BasePresenter<IRecommendView> {

    private IUserModel userModel;
    private List<RecommendListBean.RecommendBean> recommendList;
    private List<RecommendDemandBean> recommendDemandList;

    private int mLobbyStatus;
    private int pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
    private int recommendFiltrateStatus;

    public RecommendPresenter() {
        userModel = new UserModel();
        recommendList = new ArrayList<>();
        recommendDemandList = new ArrayList<>();
    }

    public void checkBasicPersonalInformationWholeRequest() {
        OkHttpRequest checkBasicPersonalInformationWholeRequest = userModel.checkBasicPersonalInformationWholeRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, checkBasicPersonalInformationWholeRequest);
    }

    public void fetchRecommendDataRequest(int lobbyStatus) {
        mLobbyStatus = lobbyStatus;

        OkHttpRequest fetchRecommendDataRequest = userModel.fetchRecommendDataRequest();
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchRecommendDataRequest);
    }

    public void fetchBannerListRequest(int width, int height) {
        OkHttpRequest fetchBannerListRequest = userModel.fetchBannerListRequest(width, height);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchBannerListRequest);
    }

    public void fetchAddressListRequest() {
        OkHttpRequest fetchAddressListRequest = userModel.fetchAddressListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchAddressListRequest);
    }

    public void fetchCategoryListRequest() {
        OkHttpRequest fetchCategoryListRequest = userModel.fetchCategoryListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchCategoryListRequest);
    }

    public void fetchDemandCategoryListRequest() {
        OkHttpRequest fetchDemandCategoryListRequest = userModel.fetchDemandCategoryListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchDemandCategoryListRequest);
    }

    public void fetchAdvancedFilterRequest() {
        OkHttpRequest fetchAdvancedFilterRequest = userModel.fetchAdvancedFilterRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchAdvancedFilterRequest);
    }

    public void fetchDemandAdvancedFilterRequest() {
        OkHttpRequest fetchDemandAdvancedFilterRequest = userModel.fetchDemandAdvancedFilterRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchDemandAdvancedFilterRequest);
    }

    public void fetchRecommendGridListRequest() {
        OkHttpRequest fetchRecommendGridListRequest = userModel.fetchRecommendGridListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchRecommendGridListRequest);
    }

    private void fetchRecommendListRequest(boolean isRefresh, int serviceWayId, int unitId, int minPrice, int maxPrice, int sortType) {
        if (isRefresh) {
            recommendList.clear();
        }

        OkHttpRequest fetchRecommendListRequest = userModel.fetchRecommendListRequest(pageNumber, serviceWayId, maxPrice, minPrice, unitId, sortType);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, fetchRecommendListRequest);
    }

    private void fetchRecommendDemandListRequest(boolean isRefresh, int serviceWayId, int isTrusteeship, int minPrice, int maxPrice, int deliverCycleId, int sortType) {
        if (isRefresh) {
            recommendDemandList.clear();
        }

        OkHttpRequest fetchRecommendDemandListRequest = userModel.fetchRecommendDemandListRequest(pageNumber, serviceWayId, isTrusteeship, minPrice, maxPrice, deliverCycleId, sortType);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, fetchRecommendDemandListRequest);
    }

    public void refresh(int lobbyStatus, int recommendFiltrateStatus, int serviceWayId, int unitId, int minPrice, int maxPrice, int sortType, int demandServiceWayId, int isTrusteeship, int demandMinPrice, int demandMaxPrice, int deliverCycleId, int demandSortType) {
        if (!getView().isLoadingMore()) {
            pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
            this.recommendFiltrateStatus = recommendFiltrateStatus;
            getView().setRefresh(true);

            if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                fetchRecommendListRequest(true, serviceWayId, unitId, minPrice, maxPrice, sortType);
            } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
                fetchRecommendDemandListRequest(true, demandServiceWayId, isTrusteeship, demandMinPrice, demandMaxPrice, deliverCycleId, demandSortType);
            }
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore(int lobbyStatus, int recommendFiltrateStatus, int serviceWayId, int unitId, int minPrice, int maxPrice, int sortType, int demandServiceWayId, int isTrusteeship, int demandMinPrice, int demandMaxPrice, int deliverCycleId, int demandSortType) {
        if (!getView().isRefreshing()) {
            this.recommendFiltrateStatus = recommendFiltrateStatus;
            getView().setLoadMore(true);

            if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                fetchRecommendListRequest(false, serviceWayId, unitId, minPrice, maxPrice, sortType);
            } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
                fetchRecommendDemandListRequest(false, demandServiceWayId, isTrusteeship, demandMinPrice, demandMaxPrice, deliverCycleId, demandSortType);
            }
        } else {
            getView().setLoadMore(false);
        }
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_CHECK_BASIC_MSG)) {
            if (success) {
                CheckBasicPersonalInformationBean checkBasicPersonalInformationBean = JSONObject.parseObject(data, CheckBasicPersonalInformationBean.class);

                if (null != checkBasicPersonalInformationBean) {
                    boolean isComplete = TypeUtils.getBoolean(checkBasicPersonalInformationBean.isComplete, false);

                    if (null != getView()) {
                        if (isComplete) {
                            getView().onShowCompletedBasicPersonalInformation();
                        } else {
                            getView().onShowBasicPersonalInformationBean(checkBasicPersonalInformationBean);
                        }
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast("请求失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_NAVIGATION_DATA)) {
            if (success) {
                RecommendDataBean recommendDataBean = JSONObject.parseObject(data, RecommendDataBean.class);

                if (null != recommendDataBean) {
                    List<BannerBean> bannerList = recommendDataBean.bannerResponseList;
                    RecommendGridListBean recommendGridListBean = recommendDataBean.homePageIconVo;
                    AdvancedFilterBean advancedFilterBean = recommendDataBean.advancedFilterServeVo;
                    DemandAdvancedFilterBean demandAdvancedFilterBean = recommendDataBean.advancedFilterDemandVo;

                    if (null != getView()) {
                        getView().onShowBannerList(bannerList);
                    }

                    if (null != recommendGridListBean) {
                        List<RecommendGridBean> serveHomeIconsList = recommendGridListBean.serveHomeIcons;
                        List<RecommendGridBean> demandHomeIconsList = recommendGridListBean.demandHomeIcons;

                        if (null != getView()) {
                            getView().onShowRecommendGridList(serveHomeIconsList, demandHomeIconsList);
                        }
                    }

                    if (mLobbyStatus == CommonConstant.SERVICE_LOBBY) {
                        if (null != getView()) {
                            getView().onShowAdvancedFilterBean(advancedFilterBean);
                        }
                    } else if (mLobbyStatus == CommonConstant.DEMAND_LOBBY) {
                        if (null != getView()) {
                            getView().onShowDemandAdvancedFilterBean(demandAdvancedFilterBean);
                        }
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_BANNER_LIST)) {
            if (success) {
                List<BannerBean> bannerList = JSONArray.parseArray(data, BannerBean.class);

                if (null != getView()) {
                    getView().onShowBannerList(bannerList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast("请求失败");
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_HOME_PAGE)) {
            if (success) {
                RecommendGridListBean recommendGridListBean = JSONObject.parseObject(data, RecommendGridListBean.class);

                if (null != recommendGridListBean) {
                    List<RecommendGridBean> serveHomeIconsList = recommendGridListBean.serveHomeIcons;
                    List<RecommendGridBean> demandHomeIconsList = recommendGridListBean.demandHomeIcons;

                    if (null != getView()) {
                        getView().onShowRecommendGridList(serveHomeIconsList, demandHomeIconsList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_SERVE) || requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_DEMAND)) {
            if (success) {
                List<CategoryBean> categoryList = JSONObject.parseArray(data, CategoryBean.class);

                if (null != getView()) {
                    getView().onShowCategoryList(categoryList);
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SERVE_RECOMMEND)) {
            if (success) {
                RecommendListBean recommendListBean = JSONObject.parseObject(data, RecommendListBean.class);

                if (null != recommendListBean) {
                    List<RecommendListBean.RecommendBean> recommendList = recommendListBean.solrServes;

                    if (null != recommendList && recommendList.size() > 0) {
                        this.recommendList.addAll(recommendList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowRecommendList(recommendFiltrateStatus, this.recommendList, recommendList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_DEMAND_RECOMMEND)) {
            if (success) {
                RecommendDemandListBean recommendDemandListBean = JSONObject.parseObject(data, RecommendDemandListBean.class);

                if (null != recommendDemandListBean) {
                    List<RecommendDemandBean> recommendDemandList = recommendDemandListBean.solrDemands;

                    if (null != recommendDemandList && recommendDemandList.size() > 0) {
                        this.recommendDemandList.addAll(recommendDemandList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowRecommendDemandList(recommendFiltrateStatus, this.recommendDemandList, recommendDemandList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }
    }

}
