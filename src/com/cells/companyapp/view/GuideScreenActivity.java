package com.cells.companyapp.view;

import scl.leo.library.guidescreen.GuideContoler;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class GuideScreenActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_page);
		initViewPager();
	}

	private void initViewPager() {

		GuideContoler contoler = new GuideContoler(this);
		int[] imgIds = { R.drawable.guide_1, R.drawable.guide_2,
				R.drawable.guide_3, R.drawable.guide_4, R.drawable.guide_5,
				R.drawable.guide_6 };
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_guide_pager_four, null);

		contoler.init(imgIds, view);
		view.findViewById(R.id.bt_login).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if ("1".equals(getStringExtra("flog"))) {
							finish();
						} else if ("2".equals(getStringExtra("flog"))) {
							openActivity(MainActivity.class, true);
						}
					}
				});
	}
}
