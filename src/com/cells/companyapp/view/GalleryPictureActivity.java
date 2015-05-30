package com.cells.companyapp.view;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.image.touchgallery.GalleryWidget.GalleryViewPager;
import scl.leo.library.image.touchgallery.GalleryWidget.UrlPagerAdapter;
import scl.leo.library.image.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import scl.leo.library.utils.other.JsonUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Picture;
import com.cells.companyapp.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;

public class GalleryPictureActivity extends BaseActivity {

	private static final String TAG = "GalleryPictureActivity";

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;
	@ViewInject(id = R.id.tv_img)
	private TextView tv;

	List<Picture> image = new ArrayList<Picture>();

	private GalleryViewPager mViewPager;

	private int gallery_id;
	private String type;

	private CircularProgressDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_picture);
		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		Bundle bundle = getIntent().getExtras();
		gallery_id = bundle.getInt("gallery_id");
		type = bundle.getString("type");

		ivTitleLeft.setImageResource(R.drawable.icon_back);
		if (type.equals("activity")) {
			tvTitle.setText("企业活动");
		} else if (type.equals("leader")) {
			tvTitle.setText("企业领导");
		}
		loading = CircularProgressDialog.show(context);
		loading.show();
		getPictures(gallery_id);
	}

	private void getPictures(int gallery_id) {
		AjaxParams params = new AjaxParams();
		params.put("gallery_id", gallery_id + "");

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.GET_GALLERY, params,
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

						Log.i(TAG, str);

						loading.dismiss();
						image = (List<Picture>) JsonUtil.fromJson(str,
								new TypeToken<List<Picture>>() {
								});

						List<String> items = new ArrayList<String>();
						for (int i = 0; i < image.size(); i++) {
							items.add(image.get(i).getImage());
						}

						UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(
								GalleryPictureActivity.this, items);
						pagerAdapter
								.setOnItemChangeListener(new OnItemChangeListener() {
									@Override
									public void onItemChange(int currentPosition) {
										tv.setText((currentPosition + 1) + "/"
												+ image.size());
									}
								});

						mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
						mViewPager.setOffscreenPageLimit(3);
						mViewPager.setAdapter(pagerAdapter);
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

	public void back(View v) {
		finish();
	}

}
