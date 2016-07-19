package com.cells.companyapp.fragment;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.cells.companyapp.utils.JsonUtil;
import com.cells.companyapp.customview.CircularProgressDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.base.CommonAdapter;
import com.cells.companyapp.been.Video;
import com.cells.companyapp.customview.refresh.XListView;
import com.cells.companyapp.customview.refresh.XListView.FooterListener;
import com.cells.companyapp.customview.refresh.XListView.HeaderListener;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.VideoInfoActivity;
import com.google.gson.reflect.TypeToken;

public class VideoItemFragment extends BaseFragment implements FooterListener, HeaderListener {

	@ViewInject(id = R.id.xlistview)
	private XListView listview;

	private CommonAdapter<Video> adapter;

	private List<Video> culture;

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
		listview.setPullLoadEnable(true);
		listview.setXHeaderListener(this);
		listview.setXFooterListener(this);
		listview.setSelector(new ColorDrawable(Color.TRANSPARENT));

		loading = CircularProgressDialog.show(getActivity());
	}

	private void getCultureList(int page, int video_type, final int type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page);
		params.put("v_type", video_type);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.VIDEOS, params, new AjaxCallBack<Object>() {

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
				culture = (List<Video>) JsonUtil.fromJson(str, new TypeToken<List<Video>>() {
				});

				if (type == 1) {
					adapter.clear();
					adapter.addAll(culture);
					listview.stopRefresh();
					listview.setRefreshTime("刚刚");
				} else if (type == 2) {
					adapter.addAll(culture);
					listview.stopLoadMore();
				} else if (type == 0) {
					adapter = new CommonAdapter<Video>(context, R.layout.item_video, culture) {

						@Override
						public void onUpdate(BaseAdapterHelper helper, final Video item, int position) {
							helper.setText(R.id.tv_name, item.getName());
							helper.setImageUrl(context, R.id.img_video, item.getImage());
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
					listview.setAdapter(adapter);
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
			loading.show();
			getCultureList(page, video_type, 0);
		}
		isfirst = true;
	}
}
