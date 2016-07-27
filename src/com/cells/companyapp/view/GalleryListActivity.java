package com.cells.companyapp.view;

import java.util.ArrayList;
import java.util.List;

import com.cells.companyapp.utils.*;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.adapter.GalleryListAdapter;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Gallery;
import com.google.gson.reflect.TypeToken;

public class GalleryListActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.listview)
	private ListView listview;

	private String name;
	private int company_id;

	private GalleryListAdapter adapter;

	private List<Gallery> gallery;
	private List<Gallery> galleryList = new ArrayList<Gallery>();

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

		loading.show();
		getGalleryList(company_id);
	}

	public void back(View v) {
		finish();
	}

	private void getGalleryList(int company_id) {
		AjaxParams params = new AjaxParams();
		params.put("company_id", company_id);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(ApiUtils.TIME_OUT);
		fh.get(ApiUtils.ROOT_URL + ApiUtils.YEAR_OF_GALLERY, params, new AjaxCallBack<Object>() {

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
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (t != null) {
					showToast("加载失败，请稍后再试！");
					loading.dismiss();
				}
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}

}
