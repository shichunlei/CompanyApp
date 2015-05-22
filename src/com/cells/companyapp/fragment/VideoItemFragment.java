package com.cells.companyapp.fragment;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.JsonUtil;
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
import com.cells.companyapp.adapter.VideoAdapter;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.been.Video;
import com.cells.companyapp.customview.refresh.XListView;
import com.cells.companyapp.customview.refresh.XListView.FooterListener;
import com.cells.companyapp.customview.refresh.XListView.HeaderListener;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.VideoInfoActivity;
import com.google.gson.reflect.TypeToken;

public class VideoItemFragment extends BaseFragment implements FooterListener,
		HeaderListener {

	private static final String TAG = "VideoItemFragment";

	@ViewInject(id = R.id.xlistview)
	private XListView listview;

	private VideoAdapter adapter;

	private List<Video> culture;

	private View view;

	private int page = 1;
	private boolean isfirst = false;
	int video_type;

	private CircularProgressDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater
				.inflate(R.layout.fragment_video_items, container, false);
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
		adapter = new VideoAdapter(getActivity(), listview);

		loading = CircularProgressDialog.show(getActivity());
	}

	private void getCultureList(int page, int video_type, final int type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page + "");
		params.put("v_type", video_type + "");

		Log.i(TAG, video_type + "\n" + page);
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.VIDEOS, params,
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
						culture = (List<Video>) JsonUtil.fromJson(str,
								new TypeToken<List<Video>>() {
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
		getCultureList(page, video_type, 1);
	}

	@Override
	public void onLoadMore() {
		page++;
		getCultureList(page, video_type, 2);
	}

	@Override
	public void onResume() {
		super.onResume();
		video_type = getArguments().getInt("type");
		if (!isfirst) {
			page = 1;
			listview.setAdapter(adapter);
			loading.show();
			getCultureList(page, video_type, 2);
		}
		isfirst = true;
	}

	private void setItemClick() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int _id = ((Video) adapter.getItem(position - 1)).getId();
				String name = ((Video) adapter.getItem(position - 1)).getName();

				Bundle bundle = new Bundle();
				bundle.putInt("id", _id);
				bundle.putString("name", name);

				openActivity(VideoInfoActivity.class, bundle, false);
			}
		});
	}
}
