package com.cells.companyapp.fragment;

import java.util.List;

import com.cells.companyapp.utils.JsonUtil;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.base.CommonAdapter;
import com.cells.companyapp.been.Culture;
import com.cells.companyapp.widget.refresh.XListView;
import com.cells.companyapp.widget.refresh.XListView.FooterListener;
import com.cells.companyapp.widget.refresh.XListView.HeaderListener;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.CultrueInfoActivity;
import com.cells.companyapp.widget.CircularProgressDialog;
import com.google.gson.reflect.TypeToken;

public class CultureItemFragment extends BaseFragment implements FooterListener, HeaderListener {

	@ViewInject(id = R.id.xlistview)
	private XListView listview;

	private CommonAdapter<Culture> adapter;

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
		listview.setPullLoadEnable(true);
		listview.setXHeaderListener(this);
		listview.setXFooterListener(this);
		listview.setSelector(new ColorDrawable(Color.TRANSPARENT));

		loading = CircularProgressDialog.show(getActivity());
	}

	private void getCultureList(int page, String url, final int type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page + "");

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

				if (type == 1) {
					adapter.clear();
					adapter.addAll(culture);
					listview.stopRefresh();
					listview.setRefreshTime("刚刚");
				} else if (type == 2) {
					adapter.addAll(culture);
					listview.stopLoadMore();
				} else if (type == 0) {
					if (culture.size() > 0) {
						adapter = new CommonAdapter<Culture>(context, R.layout.item_culture, culture) {

							@Override
							public void onUpdate(BaseAdapterHelper helper, final Culture item, int position) {
								helper.setText(R.id.tv_name, item.getName());
								helper.setText(R.id.tv_content, item.getContent());
								helper.setImageUrl(context, R.id.img_culture, item.getLogo());
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
						listview.setAdapter(adapter);
					}
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
			loading.show();
			getCultureList(page, CULTURE, 0);
		}
		isfirst = true;
	}
}
