package com.cells.companyapp.fragment;

import java.util.List;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.JsonUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cells.companyapp.R;
import com.cells.companyapp.adapter.YellowPageAdapter;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.been.YellowPage;
import com.cells.companyapp.customview.refresh.YListView;
import com.cells.companyapp.customview.refresh.YListView.IYListViewListener;
import com.cells.companyapp.customview.waterfall.PLA_AdapterView;
import com.cells.companyapp.customview.waterfall.PLA_AdapterView.OnItemClickListener;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.SearchYellowPageActivity;
import com.cells.companyapp.view.YellowPageInfoActivity;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class YellowPageFragment extends BaseFragment implements
		IYListViewListener {

	@ViewInject(id = R.id.ylistview)
	private YListView listview;

	@ViewInject(id = R.id.img_search, click = "search")
	private ImageView search;

	private View view;

	private int currentPage;

	private CircularProgressDialog loading;

	private YellowPageAdapter adapter;

	private List<YellowPage> yellowpage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater
				.inflate(R.layout.fragment_yellow_page, container, false);
		FinalActivity.initInjectedView(this, view);
		init();

		setItemClick();
		return view;
	}

	private void init() {
		listview.setPullLoadEnable(true);
		listview.setYListViewListener(this);
		listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new YellowPageAdapter(getActivity(), listview);

		currentPage = 1;
		listview.setAdapter(adapter);
		loading = CircularProgressDialog.show(getActivity());
		loading.show();
		getRelicsList(currentPage, 2);
	}

	private void setItemClick() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				int _id = ((YellowPage) adapter.getItem(position - 1)).getId();
				String name = ((YellowPage) adapter.getItem(position - 1))
						.getName();

				Bundle bundle = new Bundle();
				bundle.putInt("id", _id);
				bundle.putString("name", name);

				openActivity(YellowPageInfoActivity.class, bundle, false);
			}
		});
	}

	private void getRelicsList(int page, final int type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page + "");

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.YELOW_PAGE, params,
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
						yellowpage = (List<YellowPage>) JsonUtil.fromJson(str,
								new TypeToken<List<YellowPage>>() {
								});

						if (type == 1) {
							adapter.clear();
							adapter.addItemTop(yellowpage);
							adapter.notifyDataSetChanged();
							listview.stopRefresh();
							listview.setRefreshTime("刚刚");
						} else if (type == 2) {
							adapter.addItemLast(yellowpage);
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

	@Override
	public void onRefresh() {
		currentPage = 1;
		getRelicsList(currentPage, 1);
	}

	@Override
	public void onLoadMore() {
		getRelicsList(++currentPage, 2);
	}

	public void search(View v) {
		openActivity(SearchYellowPageActivity.class, false);
	}
}
