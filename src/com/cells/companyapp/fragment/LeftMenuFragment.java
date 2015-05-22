package com.cells.companyapp.fragment;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.view.*;

public class LeftMenuFragment extends BaseFragment implements OnClickListener {

	private View view;

	@ViewInject(id = R.id.windows)
	private TextView windows;
	@ViewInject(id = R.id.culture)
	private TextView culture;
	@ViewInject(id = R.id.gallery)
	private TextView gallery;
	@ViewInject(id = R.id.yellowPage)
	private TextView yellowPage;
	@ViewInject(id = R.id.video)
	private TextView video;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_left_menu, container, false);
		FinalActivity.initInjectedView(this, view);
		init();

		return view;
	}

	private void init() {
		windows.setOnClickListener(this);
		culture.setOnClickListener(this);
		gallery.setOnClickListener(this);
		yellowPage.setOnClickListener(this);
		video.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Fragment content = null;
		windows.setSelected(false);
		culture.setSelected(false);
		gallery.setSelected(false);
		yellowPage.setSelected(false);
		video.setSelected(false);

		switch (v.getId()) {
		case R.id.windows:
			windows.setSelected(true);
			content = new WindowsFragment();
			break;

		case R.id.culture:
			culture.setSelected(true);
			content = new CultureFragment();
			break;

		case R.id.gallery:
			gallery.setSelected(true);
			content = new GalleryFragment();
			break;

		case R.id.yellowPage:
			yellowPage.setSelected(true);
			content = new YellowPageFragment();
			break;

		case R.id.video:
			video.setSelected(true);
			content = new VideoFragment();
			break;

		default:
			break;
		}

		if (content != null)
			switchFragment(content);
	}

	/*
	 * 切换到不同的功能内容
	 */
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		MainActivity ra = (MainActivity) getActivity();
		ra.switchContent(fragment);
	}
}
