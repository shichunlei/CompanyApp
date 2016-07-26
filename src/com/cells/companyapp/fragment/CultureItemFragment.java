package com.cells.companyapp.fragment;

import java.util.List;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cells.companyapp.utils.JsonUtil;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.BaseFragment;

import com.cells.companyapp.base.CommonRecyclerAdapter;
import com.cells.companyapp.been.Culture;
import com.cells.companyapp.enums.Enum;

import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.CultrueInfoActivity;
import com.cells.companyapp.widget.CircularProgressDialog;
import com.google.gson.reflect.TypeToken;

public class CultureItemFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

	@ViewInject(id = R.id.swipe_target)
	private RecyclerView mRecyclerView;
	@ViewInject(id = R.id.swipeToLoadLayout)
	private SwipeToLoadLayout swipeToLoadLayout;

	private CommonRecyclerAdapter<Culture> adapter;

	private List<Culture> culture;

	private View view;

	private String CULTURE = "";

	private int page = 1;
	private boolean isfirst = false;
	int culture_type;

	private CircularProgressDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_culture_items, container, false);
		FinalActivity.initInjectedView(this, view);
		init();
		return view;
	}

	private void init() {
		swipeToLoadLayout.setOnRefreshListener(this);
		swipeToLoadLayout.setOnLoadMoreListener(this);

		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		loading = CircularProgressDialog.show(getActivity());
	}

	private void getCultureList(int page, String url, final Enum.Refresh type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + url, params, new AjaxCallBack<Object>() {

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
				culture = (List<Culture>) JsonUtil.fromJson(str, new TypeToken<List<Culture>>() {
				});

				if (type == Enum.Refresh.REFRESH) {
					adapter.clear();
					adapter.addAll(culture);
					swipeToLoadLayout.setRefreshing(false);
				} else if (type == Enum.Refresh.LOAD_MORE) {
					adapter.addAll(culture);
					swipeToLoadLayout.setLoadingMore(false);
				} else if (type == Enum.Refresh.DEFAULT) {
					if (culture.size() > 0) {
						adapter = new CommonRecyclerAdapter<Culture>(context, R.layout.item_culture, culture) {

							@Override
							public void onUpdate(BaseAdapterHelper helper, final Culture item, int position) {
								helper.setText(R.id.tv_name, item.getName());
								helper.setText(R.id.tv_content, item.getContent());
								helper.setImageUrl(R.id.img_culture, item.getLogo());
								helper.setOnClickListener(R.id.layout_culture, new OnClickListener() {

									@Override
									public void onClick(View v) {
										Bundle bundle = new Bundle();
										bundle.putInt("id", item.getId());
										bundle.putInt("type", culture_type);

										openActivity(CultrueInfoActivity.class, bundle, false);
									}
								});
							}
						};
						mRecyclerView.setAdapter(adapter);
					}
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (t != null) {
					showToast("加载失败，请稍后再试！");
					loading.dismiss();
					if (type == Enum.Refresh.LOAD_MORE) {
						swipeToLoadLayout.setLoadingMore(false);
					} else if (type == Enum.Refresh.REFRESH) {
						swipeToLoadLayout.setRefreshing(false);
					}
				}
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		if (swipeToLoadLayout.isRefreshing()) {
			swipeToLoadLayout.setRefreshing(false);
		}
		if (swipeToLoadLayout.isLoadingMore()) {
			swipeToLoadLayout.setLoadingMore(false);
		}
	}

	@Override
	public void onRefresh() {
		page = 1;
		getCultureList(page, CULTURE, Enum.Refresh.REFRESH);
	}

	@Override
	public void onLoadMore() {
		page++;
		getCultureList(page, CULTURE, Enum.Refresh.LOAD_MORE);
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
			loading.show();
			getCultureList(page, CULTURE, Enum.Refresh.DEFAULT);
		}
		isfirst = true;
	}
}
