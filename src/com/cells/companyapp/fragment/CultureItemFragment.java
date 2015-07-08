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
import com.cells.companyapp.adapter.CultureAdapter;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.been.Culture;
import com.cells.companyapp.customview.refresh.XListView;
import com.cells.companyapp.customview.refresh.XListView.FooterListener;
import com.cells.companyapp.customview.refresh.XListView.HeaderListener;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.CultrueInfoActivity;
import com.google.gson.reflect.TypeToken;

public class CultureItemFragment extends BaseFragment implements
		FooterListener, HeaderListener {

	private static final String TAG = "CultreItemFragment";

	@ViewInject(id = R.id.xlistview)
	private XListView listview;

	private CultureAdapter adapter;

	private List<Culture> culture;

	private View view;

	private String CULTURE = "";

	private int page = 1;
	private boolean isfirst = false;
	int culture_type;

	private CircularProgressDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_culture_items, container,
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
		adapter = new CultureAdapter(getActivity());

		loading = CircularProgressDialog.show(getActivity());
	}

	private void getCultureList(int page, String culture_type, final int type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page + "");

		Log.i(TAG, culture_type + "\n" + page);
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + culture_type, params,
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
						culture = (List<Culture>) JsonUtil.fromJson(str,
								new TypeToken<List<Culture>>() {
								});

						if (type == 1) {
							adapter.clear();
							adapter.addItemTop(culture);
							adapter.notifyDataSetChanged();
							listview.stopRefresh();
							listview.setRefreshTime("刚刚");
						} else if (type == 2) {
							adapter.addItemLast(culture);
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
		page = 1;
		getCultureList(page, CULTURE, 1);
	}

	@Override
	public void onLoadMore() {
		page++;
		getCultureList(page, CULTURE, 2);
	}

	@Override
	public void onResume() {
		super.onResume();
		culture_type = getArguments().getInt("type");

		if (culture_type == 1) {
			CULTURE = HttpUtils.HOPES;
		} else if (culture_type == 2) {
			CULTURE = HttpUtils.MISSIONS;
		} else if (culture_type == 3) {
			CULTURE = HttpUtils.SPIRITS;
		} else if (culture_type == 4) {
			CULTURE = HttpUtils.VALUES;
		} else if (culture_type == 5) {
			CULTURE = HttpUtils.MANAGEMENTS;
		} else if (culture_type == 6) {
			CULTURE = HttpUtils.BRAND_EXPLAINS;
		}

		if (!isfirst) {
			page = 1;
			listview.setAdapter(adapter);
			loading.show();
			getCultureList(page, CULTURE, 2);
		}
		isfirst = true;
	}

	private void setItemClick() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int _id = ((Culture) adapter.getItem(position - 1)).getId();

				Bundle bundle = new Bundle();
				bundle.putInt("id", _id);
				bundle.putInt("type", culture_type);

				openActivity(CultrueInfoActivity.class, bundle, false);
			}
		});
	}
}
