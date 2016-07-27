package com.cells.companyapp.fragment;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.base.CommonRecyclerAdapter;
import com.cells.companyapp.been.*;
import com.cells.companyapp.enums.Enum;
import com.cells.companyapp.utils.ApiUtils;
import com.cells.companyapp.view.GalleryListActivity;
import com.cells.companyapp.widget.CircularProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.cells.companyapp.utils.JsonUtil;

/**
 * 
 * <p>
 * Title: GalleryFragment
 * </p>
 * <p>
 * Description: 企业文化画廊
 * </p>
 * <p>
 * Company: 北京清软时代科技有限公司
 * </p>
 * 
 * @author 师春雷
 * @update 2016年7月22日上午11:37:43
 *         修改刷新样式（SwipeToLoadLayout）以及瀑布流展示形式（StaggeredGridLayout）
 */
public class GalleryFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

	private View view;

	@ViewInject(id = R.id.swipe_target)
	private RecyclerView mRecyclerView;

	@ViewInject(id = R.id.swipeToLoadLayout)
	private SwipeToLoadLayout swipeToLoadLayout;

	private int page;

	private CircularProgressDialog loading;

	private CommonRecyclerAdapter<Gallery> adapter;

	private List<Gallery> gallery;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_gallery, container, false);
		FinalActivity.initInjectedView(this, view);
		init();
		return view;
	}

	private void init() {
		swipeToLoadLayout.setOnRefreshListener(this);
		swipeToLoadLayout.setOnLoadMoreListener(this);

		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));// 这里用线性宫格显示类似于瀑布流
		mRecyclerView.setHasFixedSize(true);

		loading = CircularProgressDialog.show(getActivity());
		loading.show();
		page = 1;
		getGalleryList(page, Enum.Refresh.DEFAULT);
	}

	private void getGalleryList(int page, final Enum.Refresh type) {
		AjaxParams params = new AjaxParams();
		params.put("page", page);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(ApiUtils.TIME_OUT);
		fh.get(ApiUtils.ROOT_URL + ApiUtils.GALLERY, params, new AjaxCallBack<Object>() {

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
				gallery = (List<Gallery>) JsonUtil.fromJson(str, new TypeToken<List<Gallery>>() {
				});
				if (type == Enum.Refresh.REFRESH) {
					adapter.clear();
					adapter.addAll(gallery);
					swipeToLoadLayout.setRefreshing(false);
				} else if (type == Enum.Refresh.LOAD_MORE) {
					adapter.addAll(gallery);
					swipeToLoadLayout.setLoadingMore(false);
				} else if (type == Enum.Refresh.DEFAULT) {
					adapter = new CommonRecyclerAdapter<Gallery>(context, R.layout.item_gallery, gallery) {

						@Override
						public void onUpdate(BaseAdapterHelper helper, final Gallery item, int position) {
							helper.setText(R.id.tv_gallery_name, item.getName());
							helper.setImageViewSize(R.id.image_gallery, item.getPicture().getWidth(), item
									.getPicture().getHeight());
							helper.setImageUrl(R.id.image_gallery, item.getPicture().getImage(),
									R.drawable.loading_icon);
							helper.setOnClickListener(R.id.layout_gallery, new OnClickListener() {

								@Override
								public void onClick(View v) {
									Bundle bundle = new Bundle();
									bundle.putInt("company_id", item.getCompany_id());
									bundle.putString("name", item.getName());
									openActivity(GalleryListActivity.class, bundle, false);
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
		getGalleryList(page, Enum.Refresh.REFRESH);
	}

	@Override
	public void onLoadMore() {
		page++;
		getGalleryList(page, Enum.Refresh.LOAD_MORE);
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
