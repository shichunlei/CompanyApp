package com.cells.companyapp.view;

import java.io.File;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import com.cells.companyapp.utils.*;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.utils.AppConfig;

public class WelcomeActivity extends BaseActivity {

	@ViewInject(id = R.id.welcome)
	LinearLayout welcome;

	boolean isFirstOpen = false;

	File mFolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		mFolder = FileUtils.createFolders(AppConfig.SCLUI_FOLDER_NAME
				+ AppConfig.IMAGE_FOLDER_NAME);
		mFolder = FileUtils.createFolders(AppConfig.SCLUI_FOLDER_NAME
				+ AppConfig.DOWNLOAD_FOLDER_NAME);
		mFolder = FileUtils.createFolders(AppConfig.SCLUI_FOLDER_NAME
				+ AppConfig.SAVE_FOLDER_NAME);

		isFirstOpen = (Boolean) SPUtils.get(context, "isFirstOpen", false,
				"FirstOpen");

		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.logo_alpha);
		animation.setFillAfter(true);
		welcome.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {

				if (!isFirstOpen) {
					SPUtils.put(context, "isFirstOpen", true, "FirstOpen");
					openActivity(GuideScreenActivity.class, "flog", "2", true);
				} else {
					openActivity(MainActivity.class, true);
				}
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});
	}

}
