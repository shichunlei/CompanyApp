package com.cells.companyapp.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.button.ToggleButton.ToggleButton;
import scl.leo.library.button.ToggleButton.ToggleButton.OnToggleChanged;
import scl.leo.library.utils.other.SPUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
	private ToggleButton wifi;

	@ViewInject(id = R.id.btn_all_open, click = "open")
	private Button open;
	@ViewInject(id = R.id.btn_all_close, click = "close")
	private Button close;

	@ViewInject(id = R.id.toggle_btn_headline)
	private ToggleButton headline;
	@ViewInject(id = R.id.toggle_btn_activity)
	private ToggleButton activity;
	@ViewInject(id = R.id.toggle_btn_video)
	private ToggleButton video;
	@ViewInject(id = R.id.toggle_btn_book)
	private ToggleButton book;
	@ViewInject(id = R.id.toggle_btn_travel)
	private ToggleButton travel;

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
			wifi.setToggleOn(true);
		} else {
			wifi.setToggleOff(true);
		}

		if ((Boolean) SPUtils.get(context, "headline", false, SETTING_DATA)) {
			headline.setToggleOn(true);
		} else {
			headline.setToggleOff(true);
		}

		if ((Boolean) SPUtils.get(context, "activity", false, SETTING_DATA)) {
			activity.setToggleOn(true);
		} else {
			activity.setToggleOff(true);
		}

		if ((Boolean) SPUtils.get(context, "video", false, SETTING_DATA)) {
			video.setToggleOn(true);
		} else {
			video.setToggleOff(true);
		}

		if ((Boolean) SPUtils.get(context, "book", false, SETTING_DATA)) {
			book.setToggleOn(true);
		} else {
			book.setToggleOff(true);
		}

		if ((Boolean) SPUtils.get(context, "travel", false, SETTING_DATA)) {
			travel.setToggleOn(true);
		} else {
			travel.setToggleOff(true);
		}

	}

	private void setToggleChanged() {
		wifi.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "wifi", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "wifi", false, SETTING_DATA);
				}
			}
		});

		headline.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "headline", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "headline", false, SETTING_DATA);
				}
			}
		});

		activity.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "activity", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "activity", false, SETTING_DATA);
				}
			}
		});

		video.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "video", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "video", false, SETTING_DATA);
				}
			}
		});

		book.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean isChecked) {
				if (isChecked) {
					SPUtils.put(context, "book", true, SETTING_DATA);
				} else {
					SPUtils.put(context, "book", false, SETTING_DATA);
				}
			}
		});

		travel.setOnToggleChanged(new OnToggleChanged() {
			@Override
			public void onToggle(boolean isChecked) {
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
		headline.setToggleOn(true);
		SPUtils.put(context, "headline", true, SETTING_DATA);
		activity.setToggleOn(true);
		SPUtils.put(context, "activity", true, SETTING_DATA);
		video.setToggleOn(true);
		SPUtils.put(context, "video", true, SETTING_DATA);
		book.setToggleOn(true);
		SPUtils.put(context, "book", true, SETTING_DATA);
		travel.setToggleOn(true);
		SPUtils.put(context, "travel", true, SETTING_DATA);
	}

	public void close(View view) {
		headline.setToggleOff(true);
		SPUtils.put(context, "headline", false, SETTING_DATA);
		activity.setToggleOff(true);
		SPUtils.put(context, "activity", false, SETTING_DATA);
		video.setToggleOff(true);
		SPUtils.put(context, "video", false, SETTING_DATA);
		book.setToggleOff(true);
		SPUtils.put(context, "book", false, SETTING_DATA);
		travel.setToggleOff(true);
		SPUtils.put(context, "travel", false, SETTING_DATA);
	}

}
