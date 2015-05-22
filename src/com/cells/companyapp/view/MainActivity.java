package com.cells.companyapp.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import scl.leo.library.slidingmenu.SlidingMenu;
import scl.leo.library.slidingmenu.app.SlidingFragmentActivity;

import com.cells.companyapp.R;
import com.cells.companyapp.fragment.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SlidingFragmentActivity {

	protected SlidingMenu mLeftMenu, mRightMenu;

	private long mExitTime;

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "left")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleBtnRigh, click = "right")
	private ImageView ivTitleRight;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	private Fragment content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLeftmenu();
		setContentView(R.layout.activity_main);

		FinalActivity.initInjectedView(this);
		init();

		initRightmenu();
	}

	private void init() {
		ivTitleLeft.setImageResource(R.drawable.icon_menu);
		ivTitleRight.setImageResource(R.drawable.icon_menu_setting);
		tvTitle.setText("企业视窗");
	}

	private void initLeftmenu() {
		content = new WindowsFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, content).commit();

		setBehindContentView(R.layout.left_menu_layout);// 设置左边的菜单布局

		FragmentTransaction mFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment fm = new LeftMenuFragment();
		mFragementTransaction.replace(R.id.main_left_fragment, fm);
		mFragementTransaction.commit();

		mLeftMenu = getSlidingMenu();
		mLeftMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
		mLeftMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置菜单宽度
		mLeftMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		mLeftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);// 设置手势模式
		mLeftMenu.setShadowDrawable(R.drawable.shadow);// 设置左菜单阴影图片
		mLeftMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		mLeftMenu.setBehindScrollScale(0.0f);// 设置滑动时拖拽效果
		mLeftMenu.setBehindWidth(360);
	}

	private void initRightmenu() {
		mRightMenu = new SlidingMenu(this);
		mRightMenu.setMode(SlidingMenu.RIGHT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
		mRightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);// 设置手势模式
		mRightMenu.setBehindOffset(220);// 前面的视图剩下多少
		mRightMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		mRightMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		mRightMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		mRightMenu.setMenu(R.layout.right_menu_layout); // 设置menu容器

		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction()
				.replace(R.id.right_menu_fragment, new RightMenuFragment())
				.commit();
	}

	public void left(View v) {
		if (mLeftMenu != null && mLeftMenu.isMenuShowing()) {
			mLeftMenu.showContent();
		} else {
			mLeftMenu.toggle();
		}
	}

	public void right(View v) {
		if (mRightMenu != null && mRightMenu.isMenuShowing()) {
			mRightMenu.showContent();
		} else {
			mRightMenu.toggle();
		}
	}

	// 按下菜单键时
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mLeftMenu != null && mLeftMenu.isMenuShowing()) {
				mLeftMenu.showContent();
			} else if (mRightMenu != null && mRightMenu.isMenuShowing()) {
				mRightMenu.showContent();
			} else {
				// 判断两次点击的时间间隔（默认设置为2秒）
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					System.exit(0);
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 左侧菜单点击切换首页的内容
	 */
	public void switchContent(Fragment fragment) {
		content = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}
}
