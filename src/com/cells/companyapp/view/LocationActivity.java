package com.cells.companyapp.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;

/**
 * 企业地图板块
 * 
 * @author leo
 *
 */
public class LocationActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView back;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.baidumap_view)
	private MapView mMapView;

	private MapStatus mMapStatus = null;

	private BaiduMap mBaiduMap;

	/** 地图联系人位置图标 */
	private BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		String name = getStringExtra("name");
		String addr = getStringExtra("addr");

		tvTitle.setText(name);
		back.setImageResource(R.drawable.icon_back);

		mBaiduMap = mMapView.getMap();

		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
		mBaiduMap.setMapStatus(msu);

		Log.i("==========", name + "\naddr = " + addr);

		showMapPoint(addr);
	}

	/**
	 * 在地图上展示点
	 * 
	 * @param addr
	 */
	private void showMapPoint(String addr) {
		double latitude = 0.0;
		double longitude = 0.0;
		LatLng point = new LatLng(latitude, longitude);

		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point)// 设置marker的位置
				.icon(bd)// 设置marker图标
				.zIndex(9) // 设置marker所在层级
				.draggable(true); // 设置手势拖拽
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);

		// 定义地图状态
		mMapStatus = new MapStatus.Builder().target(point).zoom(18.0f).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		finish();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bd.recycle();
		mMapView.onDestroy();
		mMapView = null;
	}
}
