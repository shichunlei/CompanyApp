package com.cells.companyapp.fragment;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cells.companyapp.utils.JsonUtil;

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
import com.cells.companyapp.been.Video;
import com.cells.companyapp.enums.Enum;
import com.cells.companyapp.utils.ApiUtils;
import com.cells.companyapp.view.VideoInfoActivity;
import com.cells.companyapp.widget.CircularProgressDialog;
import com.google.gson.reflect.TypeToken;

public class VideoItemFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

	@ViewInject(id = R.id.swipe_target)
	private RecyclerView mRecyclerView;
	@ViewInject(id = R.id.swipeToLoadLayout)
	private SwipeToLoadLayout swipeToLoadLayout;

	private CommonRecyclerAdapter<Video> adapter;

	private List<Video> videos;

	private View view;

	private int page = 1;
	private boolean isfirst = false;
	int video_type;

	private CircularProgressDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_video_items, container, false);
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

	private void getCultureList(int page, int video_type, final Enum.Refresh type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page);
		params.put("v_type", video_type);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(ApiUtils.TIME_OUT);
		fh.get(ApiUtils.ROOT_URL + ApiUtils.VIDEOS, params, new AjaxCallBack<Object>() {

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
				videos = (List<Video>) JsonUtil.fromJson(str, new TypeToken<List<Video>>() {
				});

				if (type == Enum.Refresh.REFRESH) {
					adapter.clear();
					adapter.addAll(videos);
					swipeToLoadLayout.setRefreshing(false);
				} else if (type == Enum.Refresh.LOAD_MORE) {
					adapter.addAll(videos);
					swipeToLoadLayout.setLoadingMore(false);
				} else if (type == Enum.Refresh.DEFAULT) {
					adapter = new CommonRecyclerAdapter<Video>(context, R.layout.item_video, videos) {

						@Override
						public void onUpdate(BaseAdapterHelper helper, final Video item, int position) {
							helper.setText(R.id.tv_name, item.getName());
							helper.setImageUrl(R.id.img_video, item.getImage());
							helper.setOnClickListener(R.id.layout_video, new OnClickListener() {

								@Override
								public void onClick(View v) {
									Bundle bundle = new Bundle();
									bundle.putInt("id", item.getId());
									bundle.putString("name", item.getName());
									openActivity(VideoInfoActivity.class, bundle, false);
								}
							});
						}
					};
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
		getCultureList(page, video_type, Enum.Refresh.REFRESH);
	}

	@Override
	public void onLoadMore() {
		page++;
		getCultureList(page, video_type, Enum.Refresh.LOAD_MORE);
	}

	@Override
	public void onResume() {
		super.onResume();
		video_type = getArguments().getInt("type");
		if (!isfirst) {
			page = 1;
			loading.show();
			getCultureList(page, video_type, Enum.Refresh.DEFAULT);
		}
		isfirst = true;
	}
}
