package com.cells.companyapp.fragment;

import java.util.List;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cells.companyapp.utils.JsonUtil;

import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.base.CommonRecyclerAdapter;
import com.cells.companyapp.been.YellowPage;
import com.cells.companyapp.enums.Enum;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.SearchYellowPageActivity;
import com.cells.companyapp.view.YellowPageInfoActivity;
import com.cells.companyapp.widget.CircularProgressDialog;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 
 * <p>
 * Title: YellowPageFragment
 * </p>
 * <p>
 * Description: 企业黄页
 * </p>
 * <p>
 * Company: 北京清软时代科技有限公司
 * </p>
 * 
 * @author 师春雷
 * 
 * @update 2016-7-22 修改刷新样式（SwipeToLoadLayout）以及瀑布流展示形式（StaggeredGridLayout）
 */
public class YellowPageFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

	@ViewInject(id = R.id.swipe_target)
	private RecyclerView mRecyclerView;

	@ViewInject(id = R.id.swipeToLoadLayout)
	private SwipeToLoadLayout swipeToLoadLayout;

	@ViewInject(id = R.id.img_search, click = "search")
	private ImageView search;

	private View view;

	private int page;

	private CircularProgressDialog loading;

	private CommonRecyclerAdapter<YellowPage> adapter;

	private List<YellowPage> yellowpage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_yellow_page, container, false);
		FinalActivity.initInjectedView(this, view);
		init();

		return view;
	}

	private void init() {
		swipeToLoadLayout.setOnRefreshListener(this);
		swipeToLoadLayout.setOnLoadMoreListener(this);

		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));// 这里用线性宫格显示类似于瀑布流
		mRecyclerView.setHasFixedSize(true);

		page = 1;
		loading = CircularProgressDialog.show(getActivity());
		loading.show();
		getRelicsList(page, Enum.Refresh.DEFAULT);
	}

	private void getRelicsList(int page, final Enum.Refresh type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.YELOW_PAGE, params, new AjaxCallBack<Object>() {

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
				yellowpage = (List<YellowPage>) JsonUtil.fromJson(str, new TypeToken<List<YellowPage>>() {
				});

				if (type == Enum.Refresh.REFRESH) {
					adapter.clear();
					adapter.addAll(yellowpage);
					swipeToLoadLayout.setRefreshing(false);
				} else if (type == Enum.Refresh.LOAD_MORE) {
					adapter.addAll(yellowpage);
					swipeToLoadLayout.setLoadingMore(false);
				} else if (type == Enum.Refresh.DEFAULT) {
					adapter = new CommonRecyclerAdapter<YellowPage>(context, R.layout.item_yellow_page,
							yellowpage) {

						@Override
						public void onUpdate(BaseAdapterHelper helper, final YellowPage item, int position) {
							helper.setText(R.id.tv_yellow_page_name, item.getName());
							helper.setImageViewHeight(context, R.id.image_yellow_page, item.getWidth(),
									item.getHeight());
							helper.setImageUrl(context, R.id.image_yellow_page, item.getUrl());
							helper.setOnClickListener(R.id.layout_yellow_page, new OnClickListener() {

								@Override
								public void onClick(View v) {
									Bundle bundle = new Bundle();
									bundle.putInt("id", item.getId());
									bundle.putString("name", item.getName());

									openActivity(YellowPageInfoActivity.class, bundle, false);
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
	public void onRefresh() {
		page = 1;
		getRelicsList(page, Enum.Refresh.REFRESH);
	}

	@Override
	public void onLoadMore() {
		page++;
		getRelicsList(page, Enum.Refresh.LOAD_MORE);
	}

	public void search(View v) {
		openActivity(SearchYellowPageActivity.class, false);
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
