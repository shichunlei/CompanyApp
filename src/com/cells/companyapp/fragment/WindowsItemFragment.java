package com.cells.companyapp.fragment;

import java.util.List;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.JsonUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cells.companyapp.R;
import com.cells.companyapp.adapter.WindowsAdapter;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.been.Windows;
import com.cells.companyapp.customview.refresh.XListView;
import com.cells.companyapp.customview.refresh.XListView.FooterListener;
import com.cells.companyapp.customview.refresh.XListView.HeaderListener;
import com.cells.companyapp.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;

public class WindowsItemFragment extends BaseFragment implements
		FooterListener, HeaderListener {

	private static final String TAG = "WindowsItemFragment";

	@ViewInject(id = R.id.xlistview)
	private XListView listview;

	private WindowsAdapter adapter;

	private List<Windows> window;

	private View view;

	private String WINDOWS = "";

	private int page = 1;
	private boolean isfirst = false;
	int window_type;

	private CircularProgressDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_windows_items, container,
				false);
		FinalActivity.initInjectedView(this, view);
		init();
		setItemClick();
		return view;
	}

	private void init() {
		listview.setPullLoadEnable(true);
		listview.setXHeaderListener(this);
		listview.setXFooterListener(this);
		listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new WindowsAdapter(getActivity());

		loading = CircularProgressDialog.show(getActivity());
	}

	@Override
	public void onRefresh() {
		page = 1;
		getCultureList(page, WINDOWS, 1);
	}

	@Override
	public void onLoadMore() {
		page++;
		getCultureList(page, WINDOWS, 2);
	}

	@Override
	public void onResume() {
		super.onResume();
		window_type = getArguments().getInt("type");

		if (window_type == 1) {
			WINDOWS = HttpUtils.HEADLINE;
		} else if (window_type == 2) {
			WINDOWS = HttpUtils.ACTIVITY;
		} else if (window_type == 3) {
			WINDOWS = HttpUtils.BRAND_STORY;
		} else if (window_type == 4) {
			WINDOWS = HttpUtils.BOOK;
		} else if (window_type == 5) {
			WINDOWS = HttpUtils.CULTURE_TRIP;
		}

		if (!isfirst) {
			page = 1;
			listview.setAdapter(adapter);
			loading.show();
			getCultureList(page, WINDOWS, 2);
		}
		isfirst = true;
	}

	private void getCultureList(int page, String windows, final int type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page + "");

		Log.i(TAG, windows);
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + windows, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						loading.dismiss();
						Log.i(TAG, str);
						window = (List<Windows>) JsonUtil.fromJson(str,
								new TypeToken<List<Windows>>() {
								});

						Log.i(TAG, "=====" + window.toString());

						if (type == 1) {
							adapter.clear();
							adapter.addItemTop(window);
							adapter.notifyDataSetChanged();
							listview.stopRefresh();
							listview.setRefreshTime("刚刚");
						} else if (type == 2) {
							adapter.addItemLast(window);
							adapter.notifyDataSetChanged();
							listview.stopLoadMore();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						if (t != null) {
							showToast("加载失败，请稍后再试！");
							loading.dismiss();
							if (type == 2) {
								listview.stopLoadMore();
							} else if (type == 1) {
								listview.stopRefresh();
								listview.setRefreshTime("刚刚");
							}
						}
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	private void setItemClick() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}
}
