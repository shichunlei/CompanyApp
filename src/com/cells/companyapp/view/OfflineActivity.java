package com.cells.companyapp.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import com.cells.companyapp.utils.*;
import com.cells.companyapp.widget.UISwitchButton;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;

public class OfflineActivity extends BaseActivity {

	public static final String SETTING_DATA = "setting_data";

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.tvTitleRigh, click = "down")
	private TextView down;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.toggle_btn_wifi)
	private UISwitchButton wifi;

	@ViewInject(id = R.id.btn_all_open, click = "open")
	private Button open;
	@ViewInject(id = R.id.btn_all_close, click = "close")
	private Button close;

	@ViewInject(id = R.id.toggle_btn_headline)
	private UISwitchButton headline;
	@ViewInject(id = R.id.toggle_btn_activity)
	private UISwitchButton activity;
	@ViewInject(id = R.id.toggle_btn_video)
	private UISwitchButton video;
	@ViewInject(id = R.id.toggle_btn_book)
	private UISwitchButton book;
	@ViewInject(id = R.id.toggle_btn_travel)
	private UISwitchButton travel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline);
		FinalActivity.initInjectedView(this);
		init();
		setToggleChanged();
	}

	private void init() {
		ivTitleLeft.setImageResource(R.drawable.icon_back);
		down.setText("开始缓存");
		tvTitle.setText("离线缓存设置");

		if ((Boolean) SPUtils.get(context, "wifi", true, SETTING_DATA)) {
			wifi.setChecked(true);
		} else {
			wifi.setChecked(false);
		}

		if ((Boolean) SPUtils.get(context, "headline", false, SETTING_DATA)) {
			headline.setChecked(true);
		} else {
			headline.setChecked(false);
		}

		if ((Boolean) SPUtils.get(context, "activity", false, SETTING_DATA)) {
			activity.setChecked(true);
		} else {
			activity.setChecked(false);
		}

		if ((Boolean) SPUtils.get(context, "video", false, SETTING_DATA)) {
			video.setChecked(true);
		} else {
			video.setChecked(false);
		}

		if ((Boolean) SPUtils.get(context, "book", false, SETTING_DATA)) {
			book.setChecked(true);
		} else {
			book.setChecked(false);
		}

		if ((Boolean) SPUtils.get(context, "travel", false, SETTING_DATA)) {
			travel.setChecked(true);
		} else {
			travel.setChecked(false);
		}

	}

	private void setToggleChanged() {
		wifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "wifi", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "wifi", false, SETTING_DATA);
				}
			}
		});

		headline.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "headline", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "headline", false, SETTING_DATA);
				}
			}
		});

		activity.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "activity", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "activity", false, SETTING_DATA);
				}
			}
		});

		video.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "video", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "video", false, SETTING_DATA);
				}
			}
		});

		book.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "book", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "book", false, SETTING_DATA);
				}
			}
		});

		travel.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "travel", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "travel", false, SETTING_DATA);
				}
			}
		});
	}

	public void back(View view) {
		finish();
	}

	public void open(View view) {
		headline.setChecked(true);
		SPUtils.put(context, "headline", true, SETTING_DATA);
		activity.setChecked(true);
		SPUtils.put(context, "activity", true, SETTING_DATA);
		video.setChecked(true);
		SPUtils.put(context, "video", true, SETTING_DATA);
		book.setChecked(true);
		SPUtils.put(context, "book", true, SETTING_DATA);
		travel.setChecked(true);
		SPUtils.put(context, "travel", true, SETTING_DATA);
	}

	public void close(View view) {
		headline.setChecked(false);
		SPUtils.put(context, "headline", false, SETTING_DATA);
		activity.setChecked(false);
		SPUtils.put(context, "activity", false, SETTING_DATA);
		video.setChecked(false);
		SPUtils.put(context, "video", false, SETTING_DATA);
		book.setChecked(false);
		SPUtils.put(context, "book", false, SETTING_DATA);
		travel.setChecked(false);
		SPUtils.put(context, "travel", false, SETTING_DATA);
	}

}
