package com.cells.companyapp.fragment;

import java.util.ArrayList;
import java.util.List;

import com.cells.companyapp.utils.JsonUtil;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.base.CommonAdapter;
import com.cells.companyapp.been.Illustrate;
import com.cells.companyapp.been.Windows;
import com.cells.companyapp.widget.refresh.XListView;
import com.cells.companyapp.widget.refresh.XListView.FooterListener;
import com.cells.companyapp.widget.refresh.XListView.HeaderListener;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.NewsInfoActivity;
import com.cells.companyapp.view.VideoInfoActivity;
import com.cells.companyapp.widget.CircularProgressDialog;
import com.google.gson.reflect.TypeToken;

public class WindowsItemFragment extends BaseFragment implements FooterListener, HeaderListener {

	@ViewInject(id = R.id.xlistview)
	private XListView listview;

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
		listview.setPullLoadEnable(true);
		listview.setXHeaderListener(this);
		listview.setXFooterListener(this);
		listview.setSelector(new ColorDrawable(Color.TRANSPARENT));

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
			loading.show();
			getCultureList(page, WINDOWS, 0);
		}
		isfirst = true;
	}

	private void getCultureList(int page, String windows, final int type) {
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

				if (type == 1) {
					adapter.clear();
					adapter.addAll(window);
					listview.stopRefresh();
					listview.setRefreshTime("刚刚");
				} else if (type == 2) {
					adapter.addAll(window);
					listview.stopLoadMore();
				} else if (type == 0) {
					adapter = new MultipleLayoutAdapter(context, R.layout.item_news, window);
//					adapter = new CommonAdapter<Windows>(context, R.layout.item_windows, window) {
//
//						@Override
//						public void onUpdate(BaseAdapterHelper helper, final Windows item, int position) {
//							if (null != item.getNews()) {// 文章
//								helper.setVisible(R.id.ll_news, true);
//								helper.setVisible(R.id.fl_slides, false);
//								helper.setVisible(R.id.gridview, false);
//
//								helper.setText(R.id.tv_news_name, item.getNews().getTitle());
//								helper.setText(R.id.tv_news_content, item.getNews().getSub_title());
//								helper.setText(R.id.tv_news_content_count, item.getNews().getComment_count()
//										+ "");
//
//								helper.setImageUrl(context, R.id.img_news, item.getNews().getImage());
//
//								helper.setOnClickListener(R.id.ll_news, new OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										Intent intent = new Intent();
//										intent.putExtra("name", item.getNews().getTitle());
//										intent.putExtra("id", item.getNews().getId());
//										intent.setClass(context, NewsInfoActivity.class);
//										context.startActivity(intent);
//									}
//								});
//							} else if (null != item.getSpecial_column()) {// 专栏
//								helper.setVisible(R.id.ll_news, false);
//								helper.setVisible(R.id.fl_slides, false);
//								helper.setVisible(R.id.gridview, false);
//							} else if (null != item.getIllustrate_id()) {// 书籍画刊
//								helper.setVisible(R.id.ll_news, false);
//								helper.setVisible(R.id.fl_slides, false);
//								helper.setVisible(R.id.gridview, true);
//
//								helper.setAdapter(R.id.gridview, new CommonAdapter<Illustrate>(context,
//										R.layout.item_illustrate, item.getIllustrate_id()) {
//
//									@Override
//									public void onUpdate(BaseAdapterHelper helper, final Illustrate item,
//											int position) {
//										helper.setImageUrl(context, R.id.image_illustrate, item.getImage());
//										helper.setOnClickListener(R.id.layout_illustrate,
//												new OnClickListener() {
//
//													@Override
//													public void onClick(View v) {
//														Bundle bundle = new Bundle();
//														bundle.putInt("id", item.getId());
//														bundle.putString("name", item.getName());
//														openActivity(VideoInfoActivity.class, bundle, false);
//													}
//												});
//									}
//								});
//							} else if (null != item.getSlides()) {// 幻灯片
//								helper.setVisible(R.id.ll_news, false);
//								helper.setVisible(R.id.fl_slides, true);
//								helper.setVisible(R.id.gridview, false);
//
//								List<String> imgesUrl = new ArrayList<String>();
//								for (int i = 0; i < item.getSlides().size(); i++) {
//									imgesUrl.add(item.getSlides().get(i).getImage());
//								}
//								helper.setImagesUrl(R.id.banner, imgesUrl);
//							}
//						}
//					};
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

	private final class MultipleLayoutAdapter extends CommonAdapter<Windows> {

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

				helper.setImageUrl(context, R.id.img_news, item.getNews().getImage());

				helper.setOnClickListener(R.id.ll_news, new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("name", item.getNews().getTitle());
						intent.putExtra("id", item.getNews().getId());
						intent.setClass(context, NewsInfoActivity.class);
						context.startActivity(intent);
					}
				});
			} else if (null != item.getSpecial_column()) {// 专栏

			} else if (null != item.getIllustrate_id()) {// 书籍画刊
				helper.setAdapter(R.id.gridview, new CommonAdapter<Illustrate>(context,
						R.layout.item_illustrate, item.getIllustrate_id()) {

					@Override
					public void onUpdate(BaseAdapterHelper helper, final Illustrate item, int position) {
						helper.setImageUrl(context, R.id.image_illustrate, item.getImage());
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
				List<String> imgesUrl = new ArrayList<String>();
				for (int i = 0; i < item.getSlides().size(); i++) {
					imgesUrl.add(item.getSlides().get(i).getImage());
				}
				helper.setImagesUrl(R.id.banner, imgesUrl);
			}
		}
	}
}
