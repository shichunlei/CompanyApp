package com.cells.companyapp.view;

import java.util.ArrayList;
import java.util.List;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.JsonUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.adapter.GalleryListAdapter;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Gallery;
import com.cells.companyapp.customview.refresh.XListView;
import com.cells.companyapp.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;

public class GalleryListActivity extends BaseActivity {

	private static final String TAG = "GalleryListActivity";

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.xlistview)
	private XListView listview;

	private String name;
	private int company_id;

	int gallery_id;

	private CircularProgressDialog loading;

	private GalleryListAdapter adapter;

	List<Gallery> gallery;
	List<Gallery> galleryList = new ArrayList<Gallery>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_list);
		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		Bundle bundle = getIntent().getExtras();
		company_id = bundle.getInt("company_id");
		name = bundle.getString("name");

		ivTitleLeft.setImageResource(R.drawable.icon_back);

		tvTitle.setText(name + "画廊");

		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);

		loading = CircularProgressDialog.show(context);
		loading.show();
		getGalleryList(company_id);
	}

	public void back(View v) {
		finish();
	}

	private void getGalleryList(int company_id) {
		AjaxParams params = new AjaxParams();
		params.put("company_id", company_id + "");

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.YEAR_OF_GALLERY, params,
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

						gallery = (List<Gallery>) JsonUtil.fromJson(str,
								new TypeToken<List<Gallery>>() {
								});

						Log.i(TAG, gallery.toString());

						if (galleryList != null) {
							galleryList.clear();
						}
						if (gallery.size() > 0) {
							galleryList.addAll(gallery);
							gallery.clear();
						}

						adapter = new GalleryListAdapter(context);
						adapter.replaceWith(galleryList);
						listview.setAdapter(adapter);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						if (t != null) {
							showToast("加载失败，请稍后再试！");
							loading.dismiss();
						}
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

}
