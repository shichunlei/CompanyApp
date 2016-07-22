package com.cells.companyapp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DisplayUtil {

	private static WindowManager wm;

	/**
	 * 根据dip值转化成px值
	 *
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dp2px(Context context, int dip) {
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources()
				.getDisplayMetrics());
		return size;
	}

	/**
	 * 得到屏幕的宽度
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getWindowsWidth(Context context) {
		wm = ((Activity) context).getWindowManager();
		return wm.getDefaultDisplay().getWidth();
	}

	/**
	 * 设置View的大小
	 * 
	 * @param context
	 * @param view
	 * @param width
	 * @param height
	 */
	public static void setViewSize(Context context, ImageView view, int width, int height) {
		int windows_width = getWindowsWidth(context);
		int view_height = 0;
		int view_width = windows_width / 2;
		view_height = (int) ((height * view_width) / width);

		/** 取控件View当前的布局参数 */
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams();
		/** 控件的高强制设成屏幕宽度 */
		linearParams.height = view_height;
		/** 控件的宽强制设成屏幕宽度 */
		linearParams.width = view_width;
		/** 使设置好的布局参数应用到控件 */
		view.setLayoutParams(linearParams);
	}
}
