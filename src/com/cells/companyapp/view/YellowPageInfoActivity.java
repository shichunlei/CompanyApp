package com.cells.companyapp.view;

import com.cells.companyapp.utils.*;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.YellowPage;

public class YellowPageInfoActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleBtnRigh, click = "location")
	private ImageView ivTitleRight;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.img_yellow_page_logo)
	private ImageView logo;

	@ViewInject(id = R.id.ll_yellow_page_name)
	private LinearLayout ll_name;
	@ViewInject(id = R.id.ll_yellow_page_ename)
	private LinearLayout ll_ename;
	@ViewInject(id = R.id.ll_yellow_page_fname)
	private LinearLayout ll_fname;

	@ViewInject(id = R.id.tv_yellow_page_name)
	private TextView cname;
	@ViewInject(id = R.id.tv_yellow_page_ename)
	private TextView ename;
	@ViewInject(id = R.id.tv_yellow_page_fname)
	private TextView fname;
	@ViewInject(id = R.id.tv_yellow_page_addr)
	private TextView addr;
	@ViewInject(id = R.id.tv_yellow_page_postcode)
	private TextView postcode;
	@ViewInject(id = R.id.tv_yellow_page_mobile)
	private TextView mobile;
	@ViewInject(id = R.id.tv_yellow_page_fax)
	private TextView fax;
	@ViewInject(id = R.id.tv_yellow_page_web)
	private TextView web;
	@ViewInject(id = R.id.tv_yellow_page_desc)
	private TextView desc;

	int id;

	YellowPage yellowpage = new YellowPage();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yelllowpage_info);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		Bundle bundle = getIntent().getExtras();
		String name = bundle.getString("name");
		id = bundle.getInt("id");

		tvTitle.setText(name);
		ivTitleLeft.setImageResource(R.drawable.icon_back);
		ivTitleRight.setImageResource(R.drawable.icon_location);

		loading.show();
		getCompanyInfo(id);
	}

	private void getCompanyInfo(int id) {
		AjaxParams params = new AjaxParams();
		params.put("id", id);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.PAGE_DETAIL, params, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String str = t.toString();

				yellowpage = (YellowPage) JsonUtil.fromJson(str, YellowPage.class);

				loading.dismiss();

				if (null != yellowpage.getName()) {
					cname.setText(yellowpage.getName());
				}
				if (null != yellowpage.getEn_name()) {
					ename.setText(yellowpage.getEn_name());
				}
				if (null != yellowpage.getFull_name()) {
					fname.setText(yellowpage.getFull_name());
				}
				if (null != yellowpage.getAddress()) {
					addr.setText(yellowpage.getAddress());
				}
				if (null != yellowpage.getPostcode()) {
					postcode.setText(yellowpage.getPostcode());
				}
				if (null != yellowpage.getMobile()) {
					mobile.setText(yellowpage.getMobile());
				}
				if (null != yellowpage.getUrl()) {
					web.setText(yellowpage.getUrl());
				}
				if (null != yellowpage.getFax()) {
					fax.setText(yellowpage.getFax());
				}
				FinalBitmap fb = FinalBitmap.create(context);
				fb.display(logo, yellowpage.getLogo());

				if (null != yellowpage.getDesc()) {
					desc.setText(yellowpage.getDesc());
				}
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

	public void back(View v) {
		finish();
	}

	public void location(View v) {
		Bundle bundle = new Bundle();
		bundle.putString("name", yellowpage.getName());
		bundle.putString("addr", yellowpage.getAddress());
		openActivity(LocationActivity.class, bundle, false);
	}
}