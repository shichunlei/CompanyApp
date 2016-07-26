package com.cells.companyapp.fragment;

import java.util.ArrayList;
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
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.base.CommonAdapter;
import com.cells.companyapp.base.CommonRecyclerAdapter;
import com.cells.companyapp.been.Illustrate;
import com.cells.companyapp.been.Windows;
import com.cells.companyapp.enums.Enum;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.NewsInfoActivity;
import com.cells.companyapp.view.VideoInfoActivity;
import com.cells.companyapp.widget.CircularProgressDialog;
import com.google.gson.reflect.TypeToken;

public class WindowsItemFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

	@ViewInject(id = R.id.swipe_target)
	private RecyclerView mRecyclerView;
	@ViewInject(id = R.id.swipeToLoadLayout)
	private SwipeToLoadLayout swipeToLoadLayout;

	private MultipleLayoutAdapter adapter;

	private List<Windows> window;

	private View view;

	private String WINDOWS = "";

	private int page = 1;
	private boolean isfirst = false;
	int window_type;

	private CircularProgressDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_windows_items, container, false);
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

	@Override
	public void onRefresh() {
		page = 1;
		getCultureList(page, WINDOWS, Enum.Refresh.REFRESH);
	}

	@Override
	public void onLoadMore() {
		page++;
		getCultureList(page, WINDOWS, Enum.Refresh.LOAD_MORE);
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
			loading.show();
			getCultureList(page, WINDOWS, Enum.Refresh.DEFAULT);
		}
		isfirst = true;
	}

	private void getCultureList(int page, String windows, final Enum.Refresh type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + windows, params, new AjaxCallBack<Object>() {

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
				window = (List<Windows>) JsonUtil.fromJson(str, new TypeToken<List<Windows>>() {
				});

				if (type == Enum.Refresh.REFRESH) {
					adapter.clear();
					adapter.addAll(window);
					swipeToLoadLayout.setRefreshing(false);
				} else if (type == Enum.Refresh.LOAD_MORE) {
					adapter.addAll(window);
					swipeToLoadLayout.setLoadingMore(false);
				} else if (type == Enum.Refresh.DEFAULT) {
					adapter = new MultipleLayoutAdapter(context, R.layout.item_news, window);
					mRecyclerView.setAdapter(adapter);
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

	private final class MultipleLayoutAdapter extends CommonRecyclerAdapter<Windows> {

		public MultipleLayoutAdapter(Context context, int layoutResId, List<Windows> windows) {
			super(context, layoutResId, windows);
		}

		@Override
		public int getLayoutResId(Windows item) {
			int layoutResId = -1;

			if (null != item.getNews()) {// 文章
				layoutResId = R.layout.item_news;
			} else if (null != item.getSpecial_column()) {// 专栏
				layoutResId = R.layout.item_spec;
			} else if (null != item.getIllustrate_id()) {// 书籍画刊
				layoutResId = R.layout.item_gridview;
			} else if (null != item.getSlides()) {// 幻灯片
				layoutResId = R.layout.item_slide;
			}

			return layoutResId;
		}

		@Override
		public void onUpdate(BaseAdapterHelper helper, final Windows item, int position) {
			if (null != item.getNews()) {// 文章
				helper.setText(R.id.tv_news_name, item.getNews().getTitle());
				helper.setText(R.id.tv_news_content, item.getNews().getSub_title());
				helper.setText(R.id.tv_news_content_count, item.getNews().getComment_count() + "");

				helper.setImageUrl(R.id.img_news, item.getNews().getImage());

				helper.setOnClickListener(R.id.ll_news, new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString("name", item.getNews().getTitle());
						bundle.putInt("id", item.getNews().getId());
						openActivity(NewsInfoActivity.class, bundle, false);
					}
				});
			} else if (null != item.getSpecial_column()) {// 专栏

			} else if (null != item.getIllustrate_id()) {// 书籍画刊
				helper.setAdapter(R.id.gridview, new CommonAdapter<Illustrate>(context,
						R.layout.item_illustrate, item.getIllustrate_id()) {

					@Override
					public void onUpdate(BaseAdapterHelper helper, final Illustrate item, int position) {
						helper.setImageViewHeight(R.id.image_illustrate);
						helper.setImageUrl(R.id.image_illustrate, item.getImage(),
								R.drawable.icon_book_loading);
						helper.setOnClickListener(R.id.layout_illustrate, new OnClickListener() {

							@Override
							public void onClick(View v) {
								Bundle bundle = new Bundle();
								bundle.putInt("id", item.getId());
								bundle.putString("name", item.getName());
								openActivity(VideoInfoActivity.class, bundle, false);
							}
						});
					}
				});
			} else if (null != item.getSlides()) {// 幻灯片
				List<String> imgesUrl = new ArrayList<>();
				for (int i = 0; i < item.getSlides().size(); i++) {
					imgesUrl.add(item.getSlides().get(i).getImage());
				}
				helper.setImagesUrl(R.id.banner, imgesUrl);
			}
		}
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
}
