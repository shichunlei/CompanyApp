package com.cells.companyapp.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;

public class VideoDownloadActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_download);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		tvTitle.setText("视频画刊下载");
		ivTitleLeft.setImageResource(R.drawable.icon_back);
	}

	public void back(View v) {
		finish();
	}
}
