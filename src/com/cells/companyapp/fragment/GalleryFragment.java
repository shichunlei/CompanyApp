package com.cells.companyapp.fragment;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cells.companyapp.R;
import com.cells.companyapp.adapter.GalleryAdapter;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.been.*;
import com.cells.companyapp.widget.refresh.YListView;
import com.cells.companyapp.widget.refresh.YListView.IYListViewListener;
import com.cells.companyapp.widget.waterfall.PLA_AdapterView;
import com.cells.companyapp.widget.waterfall.PLA_AdapterView.OnItemClickListener;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.GalleryListActivity;
import com.cells.companyapp.widget.CircularProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.cells.companyapp.utils.JsonUtil;

public class GalleryFragment extends BaseFragment implements IYListViewListener {

	private View view;
	@ViewInject(id = R.id.ylistview)
	private YListView listview;

	private int page;

	private CircularProgressDialog loading;

	private GalleryAdapter adapter;

	private List<Gallery> gallery;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_gallery, container, false);
		FinalActivity.initInjectedView(this, view);
		init();
		setItemClick();
		return view;
	}

	private void setItemClick() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
				int company_id = ((Gallery) adapter.getItem(position - 1)).getCompany_id();
				String name = ((Gallery) adapter.getItem(position - 1)).getName();
				Bundle bundle = new Bundle();
				bundle.putInt("company_id", company_id);
				bundle.putString("name", name);
				openActivity(GalleryListActivity.class, bundle, false);
			}
		});
	}

	private void init() {
		listview.setPullLoadEnable(true);
		listview.setYListViewListener(this);
		listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GalleryAdapter(getActivity());

		page = 1;
		listview.setAdapter(adapter);
		loading = CircularProgressDialog.show(getActivity());
		loading.show();
		getGalleryList(page, 2);
	}

	private void getGalleryList(int page, final int type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.GALLERY, params, new AjaxCallBack<Object>() {

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
				gallery = (List<Gallery>) JsonUtil.fromJson(str, new TypeToken<List<Gallery>>() {
				});
				if (type == 1) {
					adapter.clear();
					adapter.addItemTop(gallery);
					adapter.notifyDataSetChanged();
					listview.stopRefresh();
					listview.setRefreshTime("刚刚");
				} else if (type == 2) {
					adapter.addItemLast(gallery);
					adapter.notifyDataSetChanged();
					listview.stopLoadMore();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
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

	@Override
	public void onRefresh() {
		page = 1;
		getGalleryList(page, 1);
	}

	@Override
	public void onLoadMore() {
		getGalleryList(++page, 2);
	}
}
