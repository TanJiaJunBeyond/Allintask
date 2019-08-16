package com.allintask.lingdao.ui.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.bean.recommend.RecommendGridBean;
import com.allintask.lingdao.bean.recommend.RecommendListBean;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.AdvancedFilterBean;
import com.allintask.lingdao.bean.user.BannerBean;
import com.allintask.lingdao.bean.user.CategoryBean;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.bean.user.DemandAdvancedFilterBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.main.RecommendPresenter;
import com.allintask.lingdao.ui.activity.SimpleWebViewActivity;
import com.allintask.lingdao.ui.activity.demand.DemandDetailsActivity;
import com.allintask.lingdao.ui.activity.user.SearchActivity;
import com.allintask.lingdao.ui.activity.main.PublishDemandActivity;
import com.allintask.lingdao.ui.activity.main.PublishServiceActivity;
import com.allintask.lingdao.ui.activity.recommend.RecommendDetailsActivity;
import com.allintask.lingdao.ui.activity.user.SearchHistoryActivity;
import com.allintask.lingdao.R;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.user.CompletePersonalInformationActivity;
import com.allintask.lingdao.ui.adapter.CommonFragmentStatePagerAdapter;
import com.allintask.lingdao.ui.adapter.main.MainRecommendAdapter;
import com.allintask.lingdao.ui.adapter.main.MainRecommendDemandAdapter;
import com.allintask.lingdao.ui.adapter.main.RecommendAdapter;
import com.allintask.lingdao.ui.adapter.main.RecommendDemandAdapter;
import com.allintask.lingdao.ui.adapter.recommend.RecommendGridAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.BaseRecyclerViewItemClickListener;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.adapter.user.CategoryAdapter;
import com.allintask.lingdao.ui.adapter.user.CategorySubclassAdapter;
import com.allintask.lingdao.ui.adapter.user.CityAdapter;
import com.allintask.lingdao.ui.adapter.user.ProvinceAdapter;
import com.allintask.lingdao.ui.fragment.BaseSwipeRefreshFragment;
import com.allintask.lingdao.utils.PopupWindowUtils;
import com.allintask.lingdao.utils.SystemBarTintManager;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.view.main.IRecommendView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.allintask.lingdao.widget.CommonViewPager;
import com.allintask.lingdao.widget.PublishDialog;
import com.allintask.lingdao.widget.SearchView;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * 推荐
 * Created by Administrator on 2017/11/2.
 * Rebuild by TanJiaJun on 2018/1/3.
 */

public class RecommendFragment extends BaseSwipeRefreshFragment<IRecommendView, RecommendPresenter> implements IRecommendView {

    @BindView(R.id.ll_header)
    LinearLayout headerLL;
    @BindView(R.id.tv_location)
    TextView locationTv;
    @BindView(R.id.sv_service_content)
    SearchView serviceContentSV;
    @BindView(R.id.tv_category)
    TextView categoryTv;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.bga_banner)
    BGABanner bgaBanner;
    //    @BindView(R.id.rv_recommend_grid)
//    RecyclerView recommendGridRV;
    @BindView(R.id.view_pager)
    CommonViewPager viewPager;
    @BindView(R.id.ll_recommend_header)
    LinearLayout recommendHeaderLL;
    @BindView(R.id.ll_lobby)
    LinearLayout lobbyLL;
    @BindView(R.id.tv_lobby_title)
    TextView lobbyTitleTv;
    @BindView(R.id.tv_change_lobby)
    TextView changeLobbyTv;
    @BindView(R.id.iv_change_lobby)
    ImageView changeLobbyIv;
    @BindView(R.id.rl_recommend)
    RelativeLayout recommendRL;
    @BindView(R.id.tv_intelligent_recommendation)
    TextView intelligentRecommendationTv;
    @BindView(R.id.tv_publish_recently)
    TextView publishRecentlyTv;
    @BindView(R.id.tv_advanced_filter)
    TextView advancedFilterTv;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.iv_publish)
    ImageView publishIv;

    private SystemBarTintManager systemBarTintManager;

    private int lobbyStatus = CommonConstant.DEMAND_LOBBY;

    private boolean mIsFirstGuide = true;
    private boolean mIsFinishGuide = false;

    private int widthMeasureSpec;
    private int heightMeasureSpec;

    private int bgaBannerWidth;
    private int bgaBannerHeight;

    private List<BannerBean> mBannerList;

//    private RecommendGridAdapter recommendGridAdapter;

    private GestureDetector gestureDetector;
    private GestureDetector intelligentRecommendationGestureDetector;
    private GestureDetector publishRecentlyGestureDetector;
    private GestureDetector advancedFilterGestureDetector;

    private PopupWindow guidePopupWindow;
    private RelativeLayout guideRL;
    private Button guideChangeBtn;
    private LinearLayout guideLobbyLL;
    private TextView guideChangeLobbyTv;

    private PopupWindow locationPopupWindow;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    private PopupWindow categoryPopupWindow;
    private CategoryAdapter categoryAdapter;
    private CategorySubclassAdapter categorySubclassAdapter;

    private float mPreX;
    private float mPreY;

    private List<RecommendGridBean> mServeHomeIconsList;
    private List<RecommendGridBean> mDemandHomeIconsList;

    private PopupWindow advancedFilterPopupWindow;

    private MainRecommendAdapter mainRecommendAdapter;
    private MainRecommendDemandAdapter mainRecommendDemandAdapter;

    private PublishDialog publishDialog;

    private int advancedFilterType = -1;

    private List<IsAllBean> addressList;
    private List<AddressSubBean> cityList;
    private List<CategoryBean> categoryList;

    private AdvancedFilterBean advancedFilterBean;
    private List<AdvancedFilterBean.ServeWayCodeAndValuesBean> serveWayCodeAndValuesList;
    private List<AdvancedFilterBean.UnitCodeAndValuesBean> unitCodeAndValuesList;
    private List<AdvancedFilterBean.UnitCodeAndValuesBean.PriceRangeVosBean> priceRangeVosList;
    private List<AdvancedFilterBean.SortCodeAndValuesBean> sortCodeAndValuesList;

    private DemandAdvancedFilterBean demandAdvancedFilterBean;
    private List<DemandAdvancedFilterBean.ServeWayCodeAndValuesBean> demandServeWayCodeAndValuesList;
    private List<DemandAdvancedFilterBean.TrusteeshipCodeAndValuesBean> trusteeshipCodeAndValuesList;
    private List<DemandAdvancedFilterBean.BudgetRangeVosBean> budgetRangeVosList;
    private List<DemandAdvancedFilterBean.DeliverCycleCodeAndValuesBean> deliverCycleCodeAndValuesList;
    private List<DemandAdvancedFilterBean.SortCodeAndValuesBean> demandSortCodeAndValuesList;

    private Set<Integer> serviceWaySet;
    private Set<Integer> priceUnitSet;
    private Set<Integer> servicePriceSet;
    private Set<Integer> sortTypeSet;

    private Set<Integer> demandServiceWaySet;
    private Set<Integer> payEscrowSet;
    private Set<Integer> rangeOfBudgetSet;
    private Set<Integer> paymentCycleSet;
    private Set<Integer> demandSortTypeSet;

    private String provinceCode;
    private String cityCode;

    private int categoryId = -1;
    private int categoryPosition = -1;
    private ArrayList<Integer> categoryPropertyValueIdList;

    private int serviceWayId = -1;
    private int unitId = -1;
    private int minPrice = -1;
    private int maxPrice = -1;
    private int sortType = -1;

    private int demandServiceWayId = -1;
    private int payEscrowId = -1;
    private int demandMinPrice = -1;
    private int demandMaxPrice = -1;
    private int paymentCycleId = -1;
    private int demandSortType = -1;

    private int recommendFiltrateStatus = CommonConstant.PUBLISH_RECENTLY;
    private int demandRecommendFiltrateStatus = CommonConstant.PUBLISH_RECENTLY;

    private boolean isShowAddressPopupWindow = false;
    private boolean isShowCategoryPopupWindow = false;
    private boolean isShowAdvancedFilterPopupWindow = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected RecommendPresenter CreatePresenter() {
        return new RecommendPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Window window = ((MainActivity) getParentContext()).getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.theme_orange));

            ((MainActivity) getParentContext()).MIUISetStatusBarLightMode(window, false);
            ((MainActivity) getParentContext()).FlymeSetStatusBarLightMode(window, false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemBarTintManager = new SystemBarTintManager(((MainActivity) getParentContext()));
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.color.theme_orange);

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        super.init(savedInstanceState);

        initUI();
        initData();
    }

    private void initUI() {
        mIsFirstGuide = UserPreferences.getInstance().getIsFirstGuide();

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    viewPager.setOnCommonViewPagerListener(new CommonViewPager.OnCommonViewPagerListener() {
                        @Override
                        public void onViewPagerDispatchTouchEvent(MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    mPreX = event.getX();
                                    mPreY = event.getY();
                                    break;

                                case MotionEvent.ACTION_MOVE:
                                    float currentX = event.getX();
                                    float currentY = event.getY();

                                    if (Math.abs(currentX - mPreX) > Math.abs(currentY - mPreY)) {
                                        swipe_refresh_layout.setEnabled(false);
                                    } else {
                                        swipe_refresh_layout.setEnabled(true);
                                    }
                                    break;

                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    swipe_refresh_layout.setEnabled(true);
                                    break;
                            }
                        }
                    });
                } else {
                    swipe_refresh_layout.setEnabled(false);
                }
            }
        });

        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(WindowUtils.getScreenWidth(getParentContext()), View.MeasureSpec.EXACTLY);
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        initBGABanner();
//        initRecommendGrid();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                viewPager.resetHeight(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        gestureDetector = new GestureDetector(getParentContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                recycler_view.smoothScrollToPosition(0);
                return super.onDoubleTap(e);
            }
        });

        intelligentRecommendationGestureDetector = new GestureDetector(getParentContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                serviceWayId = -1;
                unitId = -1;
                minPrice = -1;
                maxPrice = -1;
                sortType = -1;
                demandServiceWayId = -1;
                payEscrowId = -1;
                demandMinPrice = -1;
                demandMaxPrice = -1;
                paymentCycleId = -1;
                demandSortType = -1;

                intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.theme_orange));
                publishRecentlyTv.setTextColor(getResources().getColor(R.color.text_light_black));
                advancedFilterTv.setTextColor(getResources().getColor(R.color.text_light_black));

                if (null != advancedFilterPopupWindow && advancedFilterPopupWindow.isShowing()) {
                    advancedFilterPopupWindow.dismiss();
                }

                setRefresh(true);
                mPresenter.refresh(lobbyStatus, CommonConstant.INTELLIGENT_RECOMMENDATION, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                recycler_view.smoothScrollToPosition(0);
                return super.onDoubleTap(e);
            }
        });

        publishRecentlyGestureDetector = new GestureDetector(getParentContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                    serviceWayId = -1;
                    unitId = -1;
                    minPrice = -1;
                    maxPrice = -1;
                    sortType = CommonConstant.NEWEST;
                    demandServiceWayId = -1;
                    payEscrowId = -1;
                    demandMinPrice = -1;
                    demandMaxPrice = -1;
                    paymentCycleId = -1;
                    demandSortType = -1;
                } else {
                    serviceWayId = -1;
                    unitId = -1;
                    minPrice = -1;
                    maxPrice = -1;
                    sortType = -1;
                    demandServiceWayId = -1;
                    payEscrowId = -1;
                    demandMinPrice = -1;
                    demandMaxPrice = -1;
                    paymentCycleId = -1;
                    demandSortType = CommonConstant.RECENTLY_VISITED;
                }

                intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.text_light_black));
                publishRecentlyTv.setTextColor(getResources().getColor(R.color.theme_orange));
                advancedFilterTv.setTextColor(getResources().getColor(R.color.text_light_black));

                if (null != advancedFilterPopupWindow && advancedFilterPopupWindow.isShowing()) {
                    advancedFilterPopupWindow.dismiss();
                }

                setRefresh(true);
                mPresenter.refresh(lobbyStatus, CommonConstant.PUBLISH_RECENTLY, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                recycler_view.smoothScrollToPosition(0);
                return super.onDoubleTap(e);
            }
        });

        advancedFilterGestureDetector = new GestureDetector(getParentContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
                behavior.onNestedPreScroll(coordinatorLayout, appBarLayout, nestedScrollView, 0, WindowUtils.getScreenHeight(getParentContext()), new int[]{0, 0}, ViewCompat.TYPE_TOUCH);

                if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                    if (null != advancedFilterBean) {
                        isShowAdvancedFilterPopupWindow = false;

                        intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        publishRecentlyTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        advancedFilterTv.setTextColor(getResources().getColor(R.color.theme_orange));

                        if (null != advancedFilterPopupWindow) {
                            if (advancedFilterPopupWindow.isShowing()) {
                                advancedFilterPopupWindow.dismiss();
                            } else {
                                showAdvancedFilterPopupWindow();
                            }
                        } else {
                            showAdvancedFilterPopupWindow();
                        }
                    } else {
                        isShowAdvancedFilterPopupWindow = true;
                        mPresenter.fetchAdvancedFilterRequest();
                    }
                } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
                    if (null != demandAdvancedFilterBean) {
                        isShowAdvancedFilterPopupWindow = false;

                        intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        publishRecentlyTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        advancedFilterTv.setTextColor(getResources().getColor(R.color.theme_orange));

                        if (null != advancedFilterPopupWindow) {
                            if (advancedFilterPopupWindow.isShowing()) {
                                advancedFilterPopupWindow.dismiss();
                            } else {
                                showAdvancedFilterPopupWindow();
                            }
                        } else {
                            showAdvancedFilterPopupWindow();
                        }
                    } else {
                        isShowAdvancedFilterPopupWindow = true;
                        mPresenter.fetchDemandAdvancedFilterRequest();
                    }
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                recycler_view.smoothScrollToPosition(0);
                return super.onDoubleTap(e);
            }
        });

        recommendHeaderLL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        intelligentRecommendationTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return intelligentRecommendationGestureDetector.onTouchEvent(motionEvent);
            }
        });

        publishRecentlyTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return publishRecentlyGestureDetector.onTouchEvent(motionEvent);
            }
        });

        advancedFilterTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return advancedFilterGestureDetector.onTouchEvent(motionEvent);
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getParentContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getParentContext(), R.drawable.shape_common_recycler_view_divider));
        recycler_view.addItemDecoration(dividerItemDecoration);

        mainRecommendAdapter = new MainRecommendAdapter(getParentContext());
        mainRecommendAdapter.setState(BaseRecyclerViewAdapter.STATE_LOADING);
        mainRecommendAdapter.setOnItemClickListener(new BaseRecyclerViewItemClickListener() {
            @Override
            public void setOnItemClick(RecyclerView.ViewHolder holder, int position) {
                RecommendListBean.RecommendBean recommendBean = mainRecommendAdapter.getItem(position);

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

        mainRecommendDemandAdapter = new MainRecommendDemandAdapter(getParentContext());
        mainRecommendDemandAdapter.setState(BaseRecyclerViewAdapter.STATE_LOADING);
        recycler_view.setAdapter(mainRecommendDemandAdapter);

        mainRecommendDemandAdapter.setOnItemClickListener(new BaseRecyclerViewItemClickListener() {
            @Override
            public void setOnItemClick(RecyclerView.ViewHolder holder, int position) {
                RecommendDemandBean recommendDemandBean = mainRecommendDemandAdapter.getItem(position);

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

        serviceWaySet = new HashSet<>();
        priceUnitSet = new HashSet<>();
        servicePriceSet = new HashSet<>();
        sortTypeSet = new HashSet<>();

        demandServiceWaySet = new HashSet<>();
        payEscrowSet = new HashSet<>();
        rangeOfBudgetSet = new HashSet<>();
        paymentCycleSet = new HashSet<>();
        demandSortTypeSet = new HashSet<>();

        categoryPropertyValueIdList = new ArrayList<>();
    }

    private void initData() {
        mServeHomeIconsList = new ArrayList<>();
        mDemandHomeIconsList = new ArrayList<>();

        boolean isLogin = UserPreferences.getInstance().isLogin();

        if (isLogin) {
//            mPresenter.fetchBannerListRequest(bgaBannerWidth, bgaBannerHeight);
//            mPresenter.fetchAddressListRequest();
//            mPresenter.fetchDemandCategoryListRequest();
//            mPresenter.fetchRecommendGridListRequest();
//            mPresenter.fetchAdvancedFilterRequest();

            mPresenter.fetchRecommendDataRequest(lobbyStatus);

            if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                mPresenter.refresh(lobbyStatus, recommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
            } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
                mPresenter.refresh(lobbyStatus, demandRecommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
            }
        }
    }

    private void initBGABanner() {
        // 动态计算图片的长宽
        bgaBannerWidth = WindowUtils.getScreenWidth(getContext());
        bgaBannerHeight = bgaBannerWidth / 3;

        ViewGroup.LayoutParams layoutParams = bgaBanner.getLayoutParams();
        layoutParams.width = bgaBannerWidth;
        layoutParams.height = bgaBannerHeight;
        bgaBanner.setLayoutParams(layoutParams);
        bgaBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner bgaBanner, View view, Object o, int i) {
                if (null != mBannerList && mBannerList.size() > 0) {
                    BannerBean bannerBean = mBannerList.get(i);

                    if (null != bannerBean) {
                        String resizeUrl = TypeUtils.getString(bannerBean.resizeUrl, "");
                        String imageUrl = null;

                        if (!TextUtils.isEmpty(resizeUrl)) {
                            imageUrl = "https:" + resizeUrl;
                            imageUrl = imageUrl.replace(CommonConstant.WIDTH, String.valueOf(bgaBannerWidth));
                            imageUrl = imageUrl.replace(CommonConstant.HEIGHT, String.valueOf(bgaBannerHeight));
                        }

                        ImageViewUtil.setImageView(getParentContext(), (ImageView) view, imageUrl, R.drawable.bg_banner_default);
                    }
                }
            }
        });

        bgaBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner bgaBanner, View view, Object o, int i) {
//                Intent intent = new Intent(getParentContext(), SimpleWebViewActivity.class);
//                intent.putExtra(SimpleWebViewActivity.TOOLBAR_TITLE_KEY, getString(R.string.strategies_to_make_money));
//                intent.putExtra(SimpleWebViewActivity.WEB_URL_KEY, ServiceAPIConstant.STRATEGIES_TO_MAKE_MONEY_WEB_URL);
//                startActivity(intent);

                if (null != mBannerList && mBannerList.size() > 0) {
                    BannerBean bannerBean = mBannerList.get(i);

                    if (null != bannerBean) {
                        String url = TypeUtils.getString(bannerBean.h5Url, "");

                        if (!TextUtils.isEmpty(url)) {
                            Intent intent = new Intent(getParentContext(), SimpleWebViewActivity.class);
                            intent.putExtra(SimpleWebViewActivity.WEB_URL_KEY, url);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

//    private void initRecommendGrid() {
//        recommendGridRV.setLayoutManager(new GridLayoutManager(getParentContext(), 4));
//
//        recommendGridAdapter = new RecommendGridAdapter(getParentContext());
//        recommendGridRV.setAdapter(recommendGridAdapter);
//
//        recommendGridAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                RecommendGridBean recommendGridBean = (RecommendGridBean) recommendGridAdapter.getItem(position);
//
//                if (null != recommendGridBean) {
//                    int categoryId = TypeUtils.getInteger(recommendGridBean.categoryId, -1);
//
//                    Intent intent = new Intent(getParentContext(), SearchActivity.class);
//                    intent.putExtra(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);
//                    intent.putExtra(CommonConstant.EXTRA_CATEGORY_ID, categoryId);
//                    startActivity(intent);
//                }
//            }
//        });
//    }

    private void showGuidePopupWindow(int topMargin) {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_guide, null);

        guideRL = contentView.findViewById(R.id.rl_guide);
        guideChangeBtn = contentView.findViewById(R.id.btn_guide_change);
        guideLobbyLL = contentView.findViewById(R.id.ll_guide_lobby);
        guideChangeLobbyTv = contentView.findViewById(R.id.tv_guide_change_lobby);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) guideRL.getLayoutParams();
        layoutParams.topMargin = topMargin;
        guideRL.setLayoutParams(layoutParams);

        guideChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                    if (null != guidePopupWindow && guidePopupWindow.isShowing()) {
                        guidePopupWindow.dismiss();
                    }

                    selectLobby(CommonConstant.DEMAND_LOBBY);
                } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
                    guideChangeBtn.setText(getString(R.string.change_to_find_demand));
                    guideChangeLobbyTv.setText(getString(R.string.change_demand_lobby));

                    selectLobby(CommonConstant.SERVICE_LOBBY);
                }
            }
        });

        guideLobbyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                    if (null != guidePopupWindow && guidePopupWindow.isShowing()) {
                        guidePopupWindow.dismiss();
                    }

                    selectLobby(CommonConstant.DEMAND_LOBBY);
                } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
                    guideChangeBtn.setText(getString(R.string.change_to_find_demand));
                    guideChangeLobbyTv.setText(getString(R.string.change_demand_lobby));

                    selectLobby(CommonConstant.SERVICE_LOBBY);
                }
            }
        });

        guidePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        guidePopupWindow.showAtLocation(getContentView(), Gravity.NO_GRAVITY, WindowUtils.getScreenWidth(getParentContext()), WindowUtils.getScreenHeight(getParentContext()));

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    guidePopupWindow.dismiss();
                }
                return false;
            }
        });
    }

    private void showLocationPopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_location, null);

        LinearLayout locationLL = (LinearLayout) contentView.findViewById(R.id.ll_location);
        final LinearLayout contentLL = (LinearLayout) contentView.findViewById(R.id.ll_content);
        final CommonRecyclerView provinceCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_province);
        final TextView selectLocationTv = (TextView) contentView.findViewById(R.id.tv_select_location);
        final CommonRecyclerView cityCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_city);

        selectLocationTv.setVisibility(View.VISIBLE);
        cityCRV.setVisibility(View.GONE);

        provinceCRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, DensityUtils.dip2px(getParentContext(), 1));
            }
        });

        cityCRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, DensityUtils.dip2px(getParentContext(), 1));
            }
        });

        provinceAdapter = new ProvinceAdapter(getParentContext());
        provinceCRV.setAdapter(provinceAdapter);

        if (null != addressList && addressList.size() > 0) {
            for (int i = 0; i < addressList.size(); i++) {
                IsAllBean isAllBean = addressList.get(i);

                if (null != isAllBean) {
                    List<AddressSubBean> cityList = isAllBean.sub;

                    isAllBean.isSelected = false;

                    if (null != cityList && cityList.size() > 0) {
                        for (int j = 0; j < cityList.size(); j++) {
                            AddressSubBean addressSubBean = cityList.get(j);

                            if (null != addressSubBean) {
                                addressSubBean.isSelected = false;
                            }
                        }
                    }
                }
            }
        }

        provinceAdapter.setDateList(addressList);
        provinceAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (selectLocationTv.getVisibility() == View.VISIBLE) {
                    selectLocationTv.setVisibility(View.GONE);
                    cityCRV.setVisibility(View.VISIBLE);
                }

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
                        if (null != locationPopupWindow && locationPopupWindow.isShowing()) {
                            locationPopupWindow.dismiss();
                            cityCode = null;

                            Intent intent = new Intent(getParentContext(), SearchActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);
                            intent.putExtra(CommonConstant.EXTRA_PROVINCE_CODE, provinceCode);
                            startActivity(intent);
                        }
                    }
                }

                provinceCRV.measure(widthMeasureSpec, heightMeasureSpec);
                cityCRV.measure(widthMeasureSpec, heightMeasureSpec);

                int provinceRecyclerViewHeight = provinceCRV.getMeasuredHeight();
                int cityRecyclerViewHeight = cityCRV.getMeasuredHeight();

                int resultHeight;
                int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 45);
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

        cityAdapter = new CityAdapter(getParentContext());
        cityCRV.setAdapter(cityAdapter);

        cityAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (null != cityList && cityList.size() > 0) {
                    AddressSubBean subBean = cityList.get(position);

                    if (null != subBean) {
                        cityCode = TypeUtils.getString(subBean.code, "");

                        if (null != locationPopupWindow && locationPopupWindow.isShowing()) {
                            locationPopupWindow.dismiss();

                            Intent intent = new Intent(getParentContext(), SearchActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);
                            intent.putExtra(CommonConstant.EXTRA_PROVINCE_CODE, provinceCode);
                            intent.putExtra(CommonConstant.EXTRA_CITY_CODE, cityCode);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        provinceCRV.measure(widthMeasureSpec, heightMeasureSpec);
        cityCRV.measure(widthMeasureSpec, heightMeasureSpec);

        int provinceRecyclerViewHeight = provinceCRV.getMeasuredHeight();
        int cityRecyclerViewHeight = cityCRV.getMeasuredHeight();

        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 45);
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

        locationPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PopupWindowUtils.showAsDropDown(locationPopupWindow, headerLL);

        locationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != locationPopupWindow && locationPopupWindow.isShowing()) {
                    locationPopupWindow.dismiss();
                }
            }
        });
    }

    private void showCategoryPopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_category, null);

        LinearLayout categoryLL = (LinearLayout) contentView.findViewById(R.id.ll_category);
        final LinearLayout contentLL = (LinearLayout) contentView.findViewById(R.id.ll_content);
        final CommonRecyclerView categoryCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_category);
        final TextView selectCategoryTv = (TextView) contentView.findViewById(R.id.tv_select_category);
        final CommonRecyclerView categorySubclassCRV = (CommonRecyclerView) contentView.findViewById(R.id.crv_category_subclass);
        final LinearLayout bottomLL = (LinearLayout) contentView.findViewById(R.id.ll_bottom);
        Button confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);

        selectCategoryTv.setVisibility(View.VISIBLE);
        categorySubclassCRV.setVisibility(View.GONE);

        categoryAdapter = new CategoryAdapter(getParentContext());
        categoryCRV.setAdapter(categoryAdapter);

        if (null != categoryList && categoryList.size() > 0) {
            for (int i = 0; i < categoryList.size(); i++) {
                CategoryBean categoryBean = categoryList.get(i);

                if (null != categoryBean) {
                    categoryBean.isSelected = false;
                }
            }
        }

        categoryAdapter.setDateList(categoryList);
        categoryAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (selectCategoryTv.getVisibility() == View.VISIBLE) {
                    selectCategoryTv.setVisibility(View.GONE);
                    categorySubclassCRV.setVisibility(View.VISIBLE);
                }

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

                CategoryBean categoryBean = (CategoryBean) categoryAdapter.getItem(categoryPosition);

                if (null != categoryBean) {
                    categoryId = TypeUtils.getInteger(categoryBean.code, -1);
                    List<CategoryBean.FirstSubBean> firstSubList = categoryBean.sub;

                    if (null != firstSubList && firstSubList.size() > 0) {
                        for (int i = 0; i < firstSubList.size(); i++) {
                            Set<Integer> isSelectedSet = new HashSet<>();
                            List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                            CategoryBean.FirstSubBean firstSubBean = firstSubList.get(i);
                            firstSubBean.isSelectedSet = isSelectedSet;
                            firstSubBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                        }
                    }

                    if (null != categorySubclassAdapter) {
                        categorySubclassAdapter.setDateList(firstSubList);

                        categoryCRV.measure(widthMeasureSpec, heightMeasureSpec);
                        bottomLL.measure(widthMeasureSpec, heightMeasureSpec);

                        int categoryRecyclerViewHeight = categoryCRV.getMeasuredHeight();
                        int bottomHeight = bottomLL.getMeasuredHeight();

                        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 45);
                        int tempHeight = height * 2 / 3;
                        int tempPopupWindowHeight = categoryRecyclerViewHeight + bottomHeight;
                        int resultHeight;

                        if (tempPopupWindowHeight < tempHeight) {
                            resultHeight = tempPopupWindowHeight;
                        } else {
                            resultHeight = tempHeight;
                        }

                        LinearLayout.LayoutParams categoryCRVLayoutParams = (LinearLayout.LayoutParams) categoryCRV.getLayoutParams();
                        categoryCRVLayoutParams.height = resultHeight - bottomHeight;
                        categoryCRV.setLayoutParams(categoryCRVLayoutParams);

                        LinearLayout.LayoutParams categorySubclassCRVLayoutParams = (LinearLayout.LayoutParams) categorySubclassCRV.getLayoutParams();
                        categorySubclassCRVLayoutParams.height = resultHeight - bottomHeight;
                        categorySubclassCRV.setLayoutParams(categorySubclassCRVLayoutParams);

                        ViewGroup.LayoutParams layoutParams = contentLL.getLayoutParams();
                        layoutParams.height = resultHeight;
                        contentLL.setLayoutParams(layoutParams);
                    }
                }
            }
        });

        categorySubclassAdapter = new CategorySubclassAdapter(getParentContext());
        categorySubclassCRV.setAdapter(categorySubclassAdapter);

        categoryCRV.measure(widthMeasureSpec, heightMeasureSpec);
        bottomLL.measure(widthMeasureSpec, heightMeasureSpec);

        int categoryRecyclerViewHeight = categoryCRV.getMeasuredHeight();
        int bottomHeight = bottomLL.getMeasuredHeight();

        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - DensityUtils.dip2px(getParentContext(), 45);
        int tempHeight = height * 2 / 3;
        int tempPopupWindowHeight = categoryRecyclerViewHeight + bottomHeight;
        int resultHeight;

        if (tempPopupWindowHeight < tempHeight) {
            resultHeight = tempPopupWindowHeight;
        } else {
            resultHeight = tempHeight;
        }

        LinearLayout.LayoutParams categoryCRVLayoutParams = (LinearLayout.LayoutParams) categoryCRV.getLayoutParams();
        categoryCRVLayoutParams.height = resultHeight - bottomHeight;
        categoryCRV.setLayoutParams(categoryCRVLayoutParams);

        LinearLayout.LayoutParams categorySubclassCRVLayoutParams = (LinearLayout.LayoutParams) categorySubclassCRV.getLayoutParams();
        categorySubclassCRVLayoutParams.height = resultHeight - bottomHeight;
        categorySubclassCRV.setLayoutParams(categorySubclassCRVLayoutParams);

        ViewGroup.LayoutParams layoutParams = contentLL.getLayoutParams();
        layoutParams.height = resultHeight;
        contentLL.setLayoutParams(layoutParams);

        categoryPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PopupWindowUtils.showAsDropDown(categoryPopupWindow, headerLL);

        categoryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != categoryPopupWindow && categoryPopupWindow.isShowing()) {
                    categoryPopupWindow.dismiss();
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != categoryPopupWindow && categoryPopupWindow.isShowing()) {
                    categoryPopupWindow.dismiss();

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

                            Intent intent = new Intent(getParentContext(), SearchActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);
                            intent.putExtra(CommonConstant.EXTRA_CATEGORY_ID, categoryId);
                            intent.putExtra(CommonConstant.EXTRA_CATEGORY_PROPERTY_VALUE_ID_LIST, categoryPropertyValueIdList);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    private void showAdvancedFilterPopupWindow() {
        View contentView = LayoutInflater.from(getParentContext()).inflate(R.layout.popup_window_advanced_filter, null);

        LinearLayout advancedFilterLL = (LinearLayout) contentView.findViewById(R.id.ll_advanced_filter);
        ImageView triangleIv = (ImageView) contentView.findViewById(R.id.iv_triangle);
        final LinearLayout contentLL = (LinearLayout) contentView.findViewById(R.id.ll_content);
        final ScrollView advancedFilterSV = (ScrollView) contentView.findViewById(R.id.sv_advanced_filter);
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
        Button confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);

        LinearLayout.LayoutParams triangleIvLayoutParams = (LinearLayout.LayoutParams) triangleIv.getLayoutParams();
        triangleIvLayoutParams.rightMargin = WindowUtils.getScreenWidth(getParentContext()) / 6;
        triangleIv.setLayoutParams(triangleIvLayoutParams);

        switch (lobbyStatus) {
            case CommonConstant.SERVICE_LOBBY:
                if (null != serveWayCodeAndValuesList && serveWayCodeAndValuesList.size() > 0) {
                    serviceWayLL.setVisibility(View.VISIBLE);

                    List<String> subclassNameList = new ArrayList<>();
                    subclassNameList.add(getString(R.string.unlimited));

                    if (serviceWayId == -1) {
                        serviceWaySet.clear();
                        serviceWaySet.add(0);
                    }

                    for (int i = 0; i < serveWayCodeAndValuesList.size(); i++) {
                        AdvancedFilterBean.ServeWayCodeAndValuesBean serveWayCodeAndValuesBean = serveWayCodeAndValuesList.get(i);

                        if (null != serveWayCodeAndValuesBean) {
                            String value = TypeUtils.getString(serveWayCodeAndValuesBean.value, "");
                            subclassNameList.add(value);
                        }
                    }

                    TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    tagAdapter.setSelectedList(serviceWaySet);

                    serviceWayTFL.setMaxSelectCount(1);
                    serviceWayTFL.setAdapter(tagAdapter);
                    serviceWayTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                            boolean isSelected = serviceWaySet.contains(i);
                            serviceWaySet.clear();

                            if (isSelected) {
                                serviceWayId = -1;
                            } else {
                                serviceWaySet.add(i);

                                if (i == 0) {
                                    serviceWayId = -1;
                                } else {
                                    AdvancedFilterBean.ServeWayCodeAndValuesBean serveWayCodeAndValuesBean = serveWayCodeAndValuesList.get(i - 1);

                                    if (null != serveWayCodeAndValuesBean) {
                                        serviceWayId = TypeUtils.getInteger(serveWayCodeAndValuesBean.code, -1);
                                    }
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    serviceWayLL.setVisibility(View.GONE);
                }

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

                    TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    tagAdapter.setSelectedList(priceUnitSet);

                    priceUnitTFL.setMaxSelectCount(1);
                    priceUnitTFL.setAdapter(tagAdapter);
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

                                        advancedFilterSV.measure(widthMeasureSpec, heightMeasureSpec);
                                        bottomLL.measure(widthMeasureSpec, heightMeasureSpec);

                                        int advancedFilterRecyclerViewHeight = advancedFilterSV.getMeasuredHeight();
                                        int bottomHeight = bottomLL.getMeasuredHeight();

                                        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - bgaBannerHeight - DensityUtils.dip2px(getParentContext(), 85);
                                        int tempHeight = height * 2 / 3;
                                        int tempPopupWindowHeight = advancedFilterRecyclerViewHeight + bottomHeight;
                                        int resultHeight;

                                        if (tempPopupWindowHeight < tempHeight) {
                                            resultHeight = tempPopupWindowHeight;
                                        } else {
                                            resultHeight = tempHeight;

                                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) advancedFilterSV.getLayoutParams();
                                            layoutParams.height = resultHeight - bottomHeight;
                                            advancedFilterSV.setLayoutParams(layoutParams);
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

                        TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                            @Override
                            public View getView(FlowLayout flowLayout, int i, Object o) {
                                TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                                tagTv.setText(String.valueOf(o));
                                return tagTv;
                            }
                        };

                        tagAdapter.setSelectedList(sortTypeSet);

                        sortTFL.setMaxSelectCount(1);
                        sortTFL.setAdapter(tagAdapter);
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
                    }
                } else {
                    sortLL.setVisibility(View.GONE);
                }

                serviceLL.setVisibility(View.VISIBLE);
                demandLL.setVisibility(View.GONE);
                break;

            case CommonConstant.DEMAND_LOBBY:
                if (null != demandServeWayCodeAndValuesList && demandServeWayCodeAndValuesList.size() > 0) {
                    demandServiceWayLL.setVisibility(View.VISIBLE);

                    List<String> subclassNameList = new ArrayList<>();
                    subclassNameList.add(getString(R.string.unlimited));

                    if (demandServiceWayId == -1) {
                        demandServiceWaySet.clear();
                        demandServiceWaySet.add(0);
                    }

                    for (int i = 0; i < demandServeWayCodeAndValuesList.size(); i++) {
                        DemandAdvancedFilterBean.ServeWayCodeAndValuesBean serveWayCodeAndValuesBean = demandServeWayCodeAndValuesList.get(i);

                        if (null != serveWayCodeAndValuesBean) {
                            String value = TypeUtils.getString(serveWayCodeAndValuesBean.value, "");
                            subclassNameList.add(value);
                        }
                    }

                    TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    tagAdapter.setSelectedList(demandServiceWaySet);

                    demandServiceWayTFL.setMaxSelectCount(1);
                    demandServiceWayTFL.setAdapter(tagAdapter);
                    demandServiceWayTFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                            boolean isSelected = demandServiceWaySet.contains(i);
                            demandServiceWaySet.clear();

                            if (isSelected) {
                                demandServiceWayId = -1;
                            } else {
                                demandServiceWaySet.add(i);

                                if (i == 0) {
                                    demandServiceWayId = -1;
                                } else {
                                    DemandAdvancedFilterBean.ServeWayCodeAndValuesBean serveWayCodeAndValuesBean = demandServeWayCodeAndValuesList.get(i - 1);

                                    if (null != serveWayCodeAndValuesBean) {
                                        demandServiceWayId = TypeUtils.getInteger(serveWayCodeAndValuesBean.code, -1);
                                    }
                                }
                            }
                            return false;
                        }
                    });
                } else {
                    demandServiceWayLL.setVisibility(View.GONE);
                }

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

                    TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    tagAdapter.setSelectedList(payEscrowSet);

                    payEscrowTFL.setMaxSelectCount(1);
                    payEscrowTFL.setAdapter(tagAdapter);
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

                    TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    tagAdapter.setSelectedList(rangeOfBudgetSet);

                    rangeOfBudgetTFL.setMaxSelectCount(1);
                    rangeOfBudgetTFL.setAdapter(tagAdapter);
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

                    TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, Object o) {
                            TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                            tagTv.setText(String.valueOf(o));
                            return tagTv;
                        }
                    };

                    tagAdapter.setSelectedList(paymentCycleSet);

                    paymentCycleTFL.setMaxSelectCount(1);
                    paymentCycleTFL.setAdapter(tagAdapter);
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
                                    paymentCycleId = -1;
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

                        TagAdapter tagAdapter = new TagAdapter(subclassNameList) {
                            @Override
                            public View getView(FlowLayout flowLayout, int i, Object o) {
                                TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_common_tag_flow_layout, flowLayout, false);
                                tagTv.setText(String.valueOf(o));
                                return tagTv;
                            }
                        };

                        tagAdapter.setSelectedList(demandSortTypeSet);

                        demandSortTFL.setMaxSelectCount(1);
                        demandSortTFL.setAdapter(tagAdapter);
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
                    }
                } else {
                    demandSortLL.setVisibility(View.GONE);
                }

                serviceLL.setVisibility(View.GONE);
                demandLL.setVisibility(View.VISIBLE);
                break;
        }

        advancedFilterSV.measure(widthMeasureSpec, heightMeasureSpec);
        bottomLL.measure(widthMeasureSpec, heightMeasureSpec);

        int advancedFilterRecyclerViewHeight = advancedFilterSV.getMeasuredHeight();
        int bottomHeight = bottomLL.getMeasuredHeight();

        int height = WindowUtils.getScreenHeight(getParentContext()) - WindowUtils.getStatusBarHeight(getParentContext()) - bgaBannerHeight - DensityUtils.dip2px(getParentContext(), 85);
        int tempHeight = height * 2 / 3;
        int tempPopupWindowHeight = advancedFilterRecyclerViewHeight + bottomHeight;
        int resultHeight;

        if (tempPopupWindowHeight < tempHeight) {
            resultHeight = tempPopupWindowHeight;
        } else {
            resultHeight = tempHeight;

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) advancedFilterSV.getLayoutParams();
            layoutParams.height = resultHeight - bottomHeight;
            advancedFilterSV.setLayoutParams(layoutParams);
        }

        ViewGroup.LayoutParams layoutParams = contentLL.getLayoutParams();
        layoutParams.height = resultHeight;
        contentLL.setLayoutParams(layoutParams);

        advancedFilterPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        PopupWindowUtils.showAsDropDown(advancedFilterPopupWindow, recommendRL);

        advancedFilterLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != advancedFilterPopupWindow && advancedFilterPopupWindow.isShowing()) {
                    advancedFilterPopupWindow.dismiss();
                }

                switch (recommendFiltrateStatus) {
                    case CommonConstant.INTELLIGENT_RECOMMENDATION:
                        intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.theme_orange));
                        publishRecentlyTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        advancedFilterTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        break;

                    case CommonConstant.PUBLISH_RECENTLY:
                        intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        publishRecentlyTv.setTextColor(getResources().getColor(R.color.theme_orange));
                        advancedFilterTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        break;

                    case CommonConstant.ADVANCED_FILTER:
                        intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        publishRecentlyTv.setTextColor(getResources().getColor(R.color.text_light_black));
                        advancedFilterTv.setTextColor(getResources().getColor(R.color.theme_orange));
                        break;
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != advancedFilterPopupWindow && advancedFilterPopupWindow.isShowing()) {
                    advancedFilterPopupWindow.dismiss();
                    setRefresh(true);
                    mPresenter.refresh(lobbyStatus, CommonConstant.ADVANCED_FILTER, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                }
            }
        });
    }

    private void showPublishDialog() {
        publishDialog = new PublishDialog(getParentContext());
        publishDialog.show();
        publishDialog.setOnClickListener(new PublishDialog.OnClickListener() {
            @Override
            public void onPublishServiceClick() {
                advancedFilterType = CommonConstant.PUBLISH_TYPE_PUBLISH_SERVICE;
                mPresenter.checkBasicPersonalInformationWholeRequest();
            }

            @Override
            public void onPublishDemandClick() {
                advancedFilterType = CommonConstant.PUBLISH_TYPE_PUBLISH_DEMAND;
                mPresenter.checkBasicPersonalInformationWholeRequest();
            }
        });
    }

    public void selectLobby(int lobbyStatus) {
        this.lobbyStatus = lobbyStatus;

        mPresenter.fetchRecommendGridListRequest();

        if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
            lobbyTitleTv.setText(getString(R.string.service_lobby_title));
            changeLobbyTv.setText(getString(R.string.change_demand_lobby));
            changeLobbyIv.setImageResource(R.mipmap.ic_arrow_down_orange);

            recycler_view.setAdapter(mainRecommendAdapter);

            mPresenter.fetchAdvancedFilterRequest();
        } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
            lobbyTitleTv.setText(getString(R.string.demand_lobby_title));
            changeLobbyTv.setText(getString(R.string.change_service_lobby));
            changeLobbyIv.setImageResource(R.mipmap.ic_arrow_up_orange);

            recycler_view.setAdapter(mainRecommendDemandAdapter);

            mPresenter.fetchDemandAdvancedFilterRequest();
        }

        if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
            mPresenter.refresh(lobbyStatus, recommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
        } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
            mPresenter.refresh(lobbyStatus, demandRecommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
        }
    }

    @OnClick({R.id.tv_location, R.id.sv_service_content, R.id.tv_category, R.id.ll_lobby, R.id.iv_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                if (null != categoryPopupWindow && categoryPopupWindow.isShowing()) {
                    categoryPopupWindow.dismiss();
                }

                if (null != advancedFilterPopupWindow && advancedFilterPopupWindow.isShowing()) {
                    advancedFilterPopupWindow.dismiss();
                }

                if (null != addressList && addressList.size() > 0) {
                    isShowAddressPopupWindow = false;

                    if (null != locationPopupWindow && locationPopupWindow.isShowing()) {
                        locationPopupWindow.dismiss();
                    } else {
                        showLocationPopupWindow();
                    }
                } else {
                    isShowAddressPopupWindow = true;
                    mPresenter.fetchAddressListRequest();
                }
                break;

            case R.id.sv_service_content:
                Intent serviceContentIntent = new Intent(getParentContext(), SearchHistoryActivity.class);
                serviceContentIntent.putExtra(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity) getParentContext(), serviceContentSV, "service_content_search_view");
                ActivityCompat.startActivity(getParentContext(), serviceContentIntent, activityOptionsCompat.toBundle());
                break;

            case R.id.tv_category:
                if (null != locationPopupWindow && locationPopupWindow.isShowing()) {
                    locationPopupWindow.dismiss();
                }

                if (null != advancedFilterPopupWindow && advancedFilterPopupWindow.isShowing()) {
                    advancedFilterPopupWindow.dismiss();
                }

                if (null != categoryList && categoryList.size() > 0) {
                    isShowCategoryPopupWindow = false;

                    if (null != categoryPopupWindow && categoryPopupWindow.isShowing()) {
                        categoryPopupWindow.dismiss();
                    } else {
                        showCategoryPopupWindow();
                    }
                } else {
                    isShowCategoryPopupWindow = true;

                    if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                        mPresenter.fetchCategoryListRequest();
                    } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
                        mPresenter.fetchDemandCategoryListRequest();
                    }
                }
                break;

            case R.id.ll_lobby:
                if (null != advancedFilterPopupWindow && advancedFilterPopupWindow.isShowing()) {
                    advancedFilterPopupWindow.dismiss();
                }

                if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                    lobbyStatus = CommonConstant.DEMAND_LOBBY;

                    lobbyTitleTv.setText(getString(R.string.demand_lobby_title));
                    changeLobbyTv.setText(getString(R.string.change_service_lobby));
                    changeLobbyIv.setImageResource(R.mipmap.ic_arrow_up_orange);

                    recycler_view.setAdapter(mainRecommendDemandAdapter);

//                    mPresenter.fetchDemandCategoryListRequest();
//                    mPresenter.fetchRecommendGridListRequest();
//                    mPresenter.fetchDemandAdvancedFilterRequest();
                } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
                    lobbyStatus = CommonConstant.SERVICE_LOBBY;

                    lobbyTitleTv.setText(getString(R.string.service_lobby_title));
                    changeLobbyTv.setText(getString(R.string.change_demand_lobby));
                    changeLobbyIv.setImageResource(R.mipmap.ic_arrow_down_orange);

                    recycler_view.setAdapter(mainRecommendAdapter);

//                    mPresenter.fetchCategoryListRequest();
//                    mPresenter.fetchRecommendGridListRequest();
//                    mPresenter.fetchAdvancedFilterRequest();
                }

                mPresenter.fetchRecommendDataRequest(lobbyStatus);
                recycler_view.stopScroll();

                if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
                    switch (recommendFiltrateStatus) {
                        case CommonConstant.INTELLIGENT_RECOMMENDATION:
                            intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.theme_orange));
                            publishRecentlyTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            advancedFilterTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            break;

                        case CommonConstant.PUBLISH_RECENTLY:
                            intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            publishRecentlyTv.setTextColor(getResources().getColor(R.color.theme_orange));
                            advancedFilterTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            break;

                        case CommonConstant.ADVANCED_FILTER:
                            intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            publishRecentlyTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            advancedFilterTv.setTextColor(getResources().getColor(R.color.theme_orange));
                            break;
                    }

                    mainRecommendAdapter.clear();
                    mainRecommendAdapter.setState(BaseRecyclerViewAdapter.STATE_LOADING);

                    mPresenter.refresh(lobbyStatus, recommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
                    switch (demandRecommendFiltrateStatus) {
                        case CommonConstant.INTELLIGENT_RECOMMENDATION:
                            intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.theme_orange));
                            publishRecentlyTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            advancedFilterTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            break;

                        case CommonConstant.PUBLISH_RECENTLY:
                            intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            publishRecentlyTv.setTextColor(getResources().getColor(R.color.theme_orange));
                            advancedFilterTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            break;

                        case CommonConstant.ADVANCED_FILTER:
                            intelligentRecommendationTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            publishRecentlyTv.setTextColor(getResources().getColor(R.color.text_light_black));
                            advancedFilterTv.setTextColor(getResources().getColor(R.color.theme_orange));
                            break;
                    }

                    mainRecommendDemandAdapter.clear();
                    mainRecommendDemandAdapter.setState(BaseRecyclerViewAdapter.STATE_LOADING);

                    mPresenter.refresh(lobbyStatus, demandRecommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
                }
                break;

            case R.id.iv_publish:
                showPublishDialog();
                break;
        }
    }

    @Override
    public boolean isLoadingMore() {
        return false;
    }

    @Override
    protected void onLoadMore() {
        if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
            mPresenter.loadMore(lobbyStatus, recommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
        } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
            mPresenter.loadMore(lobbyStatus, demandRecommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
        }
    }

    @Override
    protected void onSwipeRefresh() {
//        mPresenter.fetchBannerListRequest(bgaBannerWidth, bgaBannerHeight);
//        mPresenter.fetchRecommendGridListRequest();

        mPresenter.fetchRecommendDataRequest(lobbyStatus);

        if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
            mainRecommendAdapter.clear();
            mainRecommendAdapter.setState(BaseRecyclerViewAdapter.STATE_LOADING);

            mPresenter.refresh(lobbyStatus, recommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
        } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
            mainRecommendDemandAdapter.clear();
            mainRecommendDemandAdapter.setState(BaseRecyclerViewAdapter.STATE_LOADING);

            mPresenter.refresh(lobbyStatus, demandRecommendFiltrateStatus, serviceWayId, unitId, minPrice, maxPrice, sortType, demandServiceWayId, payEscrowId, demandMinPrice, demandMaxPrice, paymentCycleId, demandSortType);
        }
    }

    @Override
    public void onShowCompletedBasicPersonalInformation() {
        if (advancedFilterType == CommonConstant.PUBLISH_TYPE_PUBLISH_SERVICE) {
            Intent intent = new Intent(getParentContext(), PublishServiceActivity.class);
            startActivity(intent);
        } else if (advancedFilterType == CommonConstant.PUBLISH_TYPE_PUBLISH_DEMAND) {
            Intent intent = new Intent(getParentContext(), PublishDemandActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onShowBasicPersonalInformationBean(CheckBasicPersonalInformationBean checkBasicPersonalInformationBean) {
        Intent intent = new Intent(getParentContext(), CompletePersonalInformationActivity.class);
        intent.putExtra(CommonConstant.EXTRA_COMPLETE_PERSONAL_INFORMATION_TYPE, CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_COMPLETE);
        intent.putExtra(CommonConstant.EXTRA_PUBLISH_TYPE, advancedFilterType);
        intent.putExtra(CommonConstant.EXTRA_CHECK_BASIC_PERSONAL_INFORMATION_BEAN, checkBasicPersonalInformationBean);
        startActivity(intent);
    }

    @Override
    public void onShowBannerList(List<BannerBean> bannerList) {
        mBannerList = bannerList;

        if (null != bgaBanner) {
            if (null != mBannerList && mBannerList.size() > 0) {
                bgaBanner.setData(bannerList, null);
            } else {
                bgaBanner.setData(Arrays.asList(ImageDownloader.Scheme.DRAWABLE.wrap(String.valueOf(R.drawable.bg_banner_default))), null);
            }
        }
    }

    @Override
    public void onShowAddressList(List<IsAllBean> addressList) {
        if (null != addressList && addressList.size() > 0) {
            this.addressList = addressList;

            if (isShowAddressPopupWindow) {
                showLocationPopupWindow();
            }
        } else {
            showToast(getString(R.string.no_data));
        }
    }

    @Override
    public void onShowCategoryList(List<CategoryBean> categoryList) {
        if (null != categoryList && categoryList.size() > 0) {
            this.categoryList = categoryList;

            if (isShowCategoryPopupWindow) {
                showCategoryPopupWindow();
            }
        } else {
            showToast(getString(R.string.no_data));
        }
    }

    @Override
    public void onShowRecommendGridList(List<RecommendGridBean> serveHomeIconsList, List<RecommendGridBean> demandHomeIconsList) {
        if (null != mServeHomeIconsList && mServeHomeIconsList.size() > 0) {
            mServeHomeIconsList.clear();
        }

        if (null != mDemandHomeIconsList && mDemandHomeIconsList.size() > 0) {
            mDemandHomeIconsList.clear();
        }
//
//        if (null != serveHomeIconsList && serveHomeIconsList.size() > 0) {
//            int serveHomeIconsListSize = serveHomeIconsList.size();
//
//            if (serveHomeIconsListSize > 8) {
//                for (int i = 0; i < 8; i++) {
//                    RecommendGridBean recommendGridBean = serveHomeIconsList.get(i);
//                    mServeHomeIconsList.add(recommendGridBean);
//                }
//            } else {
//                mServeHomeIconsList.addAll(serveHomeIconsList);
//            }
//        }
//
//        if (null != demandHomeIconsList && demandHomeIconsList.size() > 0) {
//            int demandHomeIconsListSize = demandHomeIconsList.size();
//
//            if (demandHomeIconsListSize > 8) {
//                for (int i = 0; i < 8; i++) {
//                    RecommendGridBean recommendGridBean = demandHomeIconsList.get(i);
//                    mDemandHomeIconsList.add(recommendGridBean);
//                }
//            } else {
//                mDemandHomeIconsList.addAll(demandHomeIconsList);
//            }
//        }
//
//        recommendGridAdapter.setDateList(mServeHomeIconsList);

        CommonFragmentStatePagerAdapter fragmentStatePagerAdapter = new CommonFragmentStatePagerAdapter(getChildFragmentManager());
        List<Fragment> recommendGridFragmentList = new ArrayList<>();

        if (lobbyStatus == CommonConstant.SERVICE_LOBBY) {
            if (null != serveHomeIconsList && serveHomeIconsList.size() > 0) {
                int serviceRecommendGridFragmentAmount = serveHomeIconsList.size() / 8;
                int serviceRecommendGridFragmentSurplusAmount = serveHomeIconsList.size() % 8;

                if (serveHomeIconsList.size() >= 8) {
                    for (int i = 0; i < serviceRecommendGridFragmentAmount; i++) {
                        RecommendGridFragment recommendGridFragment = new RecommendGridFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);

                        List<RecommendGridBean> recommendGridList = new ArrayList<>();

                        for (int j = i * 8; j < (i + 1) * 8; j++) {
                            RecommendGridBean recommendGridBean = serveHomeIconsList.get(j);
                            recommendGridList.add(recommendGridBean);
                        }

                        bundle.putParcelableArrayList(CommonConstant.EXTRA_RECOMMEND_GRID_LIST, (ArrayList<? extends Parcelable>) recommendGridList);
                        recommendGridFragment.setArguments(bundle);
                        recommendGridFragmentList.add(recommendGridFragment);

                        viewPager.addHeight(i, DensityUtils.dip2px(getParentContext(), 175));

                        if (i == 0) {
                            mServeHomeIconsList.addAll(recommendGridList);
                        }
                    }
                }

                if (serviceRecommendGridFragmentSurplusAmount > 0) {
                    RecommendGridFragment recommendGridFragment = new RecommendGridFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);

                    List<RecommendGridBean> recommendGridList = new ArrayList<>();

                    for (int i = serveHomeIconsList.size() - serviceRecommendGridFragmentSurplusAmount; i < serveHomeIconsList.size(); i++) {
                        RecommendGridBean recommendGridBean = serveHomeIconsList.get(i);
                        recommendGridList.add(recommendGridBean);
                    }

                    bundle.putParcelableArrayList(CommonConstant.EXTRA_RECOMMEND_GRID_LIST, (ArrayList<? extends Parcelable>) recommendGridList);
                    recommendGridFragment.setArguments(bundle);
                    recommendGridFragmentList.add(recommendGridFragment);

                    if (serviceRecommendGridFragmentSurplusAmount > 4) {
                        viewPager.addHeight(recommendGridFragmentList.size() - 1, DensityUtils.dip2px(getParentContext(), 175));
                    } else {
                        viewPager.addHeight(recommendGridFragmentList.size() - 1, DensityUtils.dip2px(getParentContext(), 87.5F));
                    }

                    if (serviceRecommendGridFragmentAmount < 8) {
                        mServeHomeIconsList.addAll(recommendGridList);
                    }
                }
            }

            if (mIsFirstGuide && null != guideRL && null != mServeHomeIconsList && mServeHomeIconsList.size() > 0) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) guideRL.getLayoutParams();

                if (mServeHomeIconsList.size() > 4) {
                    layoutParams.topMargin = DensityUtils.dip2px(getParentContext(), 45 + 99) + bgaBannerHeight;
                } else {
                    layoutParams.topMargin = DensityUtils.dip2px(getParentContext(), 45 + 7) + bgaBannerHeight;
                }

                guideRL.setLayoutParams(layoutParams);

                mIsFirstGuide = false;
                mIsFinishGuide = true;
            }
        } else if (lobbyStatus == CommonConstant.DEMAND_LOBBY) {
//            recommendGridAdapter.setDateList(mDemandHomeIconsList);

            if (null != demandHomeIconsList && demandHomeIconsList.size() > 0) {
                int demandRecommendGridFragmentAmount = demandHomeIconsList.size() / 8;
                int demandRecommendGridFragmentSurplusAmount = demandHomeIconsList.size() % 8;

                if (demandHomeIconsList.size() >= 8) {
                    for (int i = 0; i < demandRecommendGridFragmentAmount; i++) {
                        RecommendGridFragment recommendGridFragment = new RecommendGridFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);

                        List<RecommendGridBean> recommendGridList = new ArrayList<>();

                        for (int j = i * 8; j < (i + 1) * 8; j++) {
                            RecommendGridBean recommendGridBean = demandHomeIconsList.get(j);
                            recommendGridList.add(recommendGridBean);
                        }

                        bundle.putParcelableArrayList(CommonConstant.EXTRA_RECOMMEND_GRID_LIST, (ArrayList<? extends Parcelable>) recommendGridList);
                        recommendGridFragment.setArguments(bundle);
                        recommendGridFragmentList.add(recommendGridFragment);

                        viewPager.addHeight(i, DensityUtils.dip2px(getParentContext(), 175));

                        if (i == 0) {
                            mDemandHomeIconsList.addAll(recommendGridList);
                        }
                    }
                }

                if (demandRecommendGridFragmentSurplusAmount > 0) {
                    RecommendGridFragment recommendGridFragment = new RecommendGridFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(CommonConstant.EXTRA_SEARCH_STATUS, lobbyStatus);

                    List<RecommendGridBean> recommendGridList = new ArrayList<>();

                    for (int i = demandHomeIconsList.size() - demandRecommendGridFragmentSurplusAmount; i < demandHomeIconsList.size(); i++) {
                        RecommendGridBean recommendGridBean = demandHomeIconsList.get(i);
                        recommendGridList.add(recommendGridBean);
                    }

                    bundle.putParcelableArrayList(CommonConstant.EXTRA_RECOMMEND_GRID_LIST, (ArrayList<? extends Parcelable>) recommendGridList);
                    recommendGridFragment.setArguments(bundle);
                    recommendGridFragmentList.add(recommendGridFragment);

                    if (demandRecommendGridFragmentSurplusAmount > 4) {
                        viewPager.addHeight(recommendGridFragmentList.size() - 1, DensityUtils.dip2px(getParentContext(), 175));
                    } else {
                        viewPager.addHeight(recommendGridFragmentList.size() - 1, DensityUtils.dip2px(getParentContext(), 87.5F));
                    }

                    if (demandRecommendGridFragmentAmount < 8) {
                        mDemandHomeIconsList.addAll(recommendGridList);
                    }
                }
            }

            if (mIsFirstGuide && !mIsFinishGuide && null != mDemandHomeIconsList && mDemandHomeIconsList.size() > 0) {
                UserPreferences.getInstance().setIsFirstGuide(false);

                if (mDemandHomeIconsList.size() > 4) {
                    showGuidePopupWindow(DensityUtils.dip2px(getParentContext(), 45 + 99) + bgaBannerHeight);
                } else {
                    showGuidePopupWindow(DensityUtils.dip2px(getParentContext(), 45 + 7) + bgaBannerHeight);
                }
            }
        }

        fragmentStatePagerAdapter.setFragmentList(recommendGridFragmentList);
        viewPager.setAdapter(fragmentStatePagerAdapter);
        viewPager.resetHeight(0);
    }

    @Override
    public void onShowAdvancedFilterBean(AdvancedFilterBean advancedFilterBean) {
        if (null != advancedFilterBean) {
            this.advancedFilterBean = advancedFilterBean;

            serveWayCodeAndValuesList = advancedFilterBean.serveWayCodeAndValues;
            unitCodeAndValuesList = advancedFilterBean.unitCodeAndValues;
            sortCodeAndValuesList = advancedFilterBean.sortCodeAndValues;

            if (isShowAdvancedFilterPopupWindow) {
                showAdvancedFilterPopupWindow();
            }
        } else {
            showToast(getString(R.string.no_data));
        }
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

            if (isShowAdvancedFilterPopupWindow) {
                showAdvancedFilterPopupWindow();
            }
        } else {
            showToast(getString(R.string.no_data));
        }
    }

    @Override
    public void onShowRecommendList(int recommendFiltrateStatus, List<RecommendListBean.RecommendBean> allRecommendList, List<RecommendListBean.RecommendBean> recommendList) {
        this.recommendFiltrateStatus = recommendFiltrateStatus;

        if (null != allRecommendList && allRecommendList.size() > 0) {
            if (null != recommendList && recommendList.size() > 0) {
                mainRecommendAdapter.addAll(recommendList);

                recycler_view.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
            } else {
                mainRecommendAdapter.setState(BaseRecyclerViewAdapter.STATE_NO_MORE);
            }
        } else {
            recycler_view.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShowRecommendDemandList(int recommendFiltrateStatus, List<RecommendDemandBean> allRecommendDemandList, List<RecommendDemandBean> recommendDemandList) {
        demandRecommendFiltrateStatus = recommendFiltrateStatus;

        if (null != allRecommendDemandList && allRecommendDemandList.size() > 0) {
            if (null != recommendDemandList && recommendDemandList.size() > 0) {
                mainRecommendDemandAdapter.addAll(recommendDemandList);

                recycler_view.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
            } else {
                mainRecommendDemandAdapter.setState(BaseRecyclerViewAdapter.STATE_NO_MORE);
            }
        } else {
            recycler_view.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        Window window = ((MainActivity) getParentContext()).getWindow();

        if (!hidden) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.theme_orange));

                ((MainActivity) getParentContext()).MIUISetStatusBarLightMode(window, false);
                ((MainActivity) getParentContext()).FlymeSetStatusBarLightMode(window, false);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                systemBarTintManager = new SystemBarTintManager(((MainActivity) getParentContext()));
                systemBarTintManager.setStatusBarTintEnabled(true);
                systemBarTintManager.setStatusBarTintResource(R.color.theme_orange);

                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }

            initData();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                if (null != systemBarTintManager) {
                    systemBarTintManager.setStatusBarTintEnabled(false);
                    systemBarTintManager.setStatusBarTintResource(Color.TRANSPARENT);
                }
            }
        }
    }

}
