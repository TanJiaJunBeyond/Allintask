package cn.tanjiajun.sdk.component.custom.listview.xlistview;

/**
 * 默认的XLisView监听器
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class DefaultXListViewListener implements XListView.IXListViewListener {

//	private CityInfo cityInfo;
//
//	private MyCityListAdapter myCityListAdapter;
//
//	private XListView xListView;
//
//	List<WeatherInfo> cityWeatherListForAdapter;
//
//	private int index = 1;
//
//	public DefaultXListViewListener(Context context, CityInfo cityInfo,
//			XListView xListView) {
//		this.cityInfo = cityInfo;
//
//		this.xListView = xListView;
//		CityInfo cityInfoForAdapter = new CityInfo();
//		cityInfoForAdapter.setCityId(cityInfo.getCityId());
//		cityInfoForAdapter.setCityIsSelected(cityInfo.isCityIsSelected());
//		cityInfoForAdapter.setCityName(cityInfo.getCityName());
//		cityWeatherListForAdapter = new ArrayList<WeatherInfo>();
//		List<WeatherInfo> cityWeatherList = cityInfo.getCityWeathers();
//		if (null != cityWeatherList && 0 != cityWeatherList.size()) {
//			cityWeatherListForAdapter.add(cityWeatherList.get(0));
//		}
//		cityInfoForAdapter
//				.setCityWeathers((ArrayList<WeatherInfo>) cityWeatherListForAdapter);
//
//		myCityListAdapter = new MyCityListAdapter(context, cityInfoForAdapter);
//		xListView.setAdapter(myCityListAdapter);
//
//	}

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }

    private void geneItems() {
//		List<WeatherInfo> cityWeatherList = cityInfo.getCityWeathers();
//
//		if (null != cityWeatherList && index < cityWeatherList.size()) {
//			cityWeatherListForAdapter.add(cityWeatherList.get(index));
//			++index;
//		}
    }

    @Override
    public void onLoadMore() {

//		List<WeatherInfo> cityWeatherList = cityInfo.getCityWeathers();
//
//		if (null != cityWeatherList && index < cityWeatherList.size()) {
//			geneItems();
//			myCityListAdapter.notifyDataSetChanged();
//		}
//		xListView.stopLoadMore();
    }

}
