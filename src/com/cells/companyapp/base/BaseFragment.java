package com.cells.companyapp.base;

import java.io.Serializable;

import com.cells.companyapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {

	public Context context;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
	}

	/**
	 * 接收前一个页面传递的String值
	 * 
	 * @param key
	 * @return
	 */
	protected String getStringExtra(String key) {
		Intent receive = ((Activity) context).getIntent();
		String flag;

		flag = receive.getStringExtra(key);

		return flag;
	}

	/**
	 * 接收前一个页面传递的Integer值
	 * 
	 * @param key
	 * @return
	 */
	protected Integer getIntExtra(String key) {
		Intent receive = ((Activity) context).getIntent();
		int flag;

		flag = receive.getIntExtra(key, 0);

		return flag;
	}

	/**
	 * 接收前一个页面传递的Boolean值
	 * 
	 * @param key
	 * @return
	 */
	protected boolean getBooleanExtra(String key) {
		Intent receive = ((Activity) context).getIntent();
		boolean flag;

		flag = receive.getBooleanExtra(key, false);

		return flag;
	}

	/**
	 * 接收前一个页面传递的Float值
	 * 
	 * @param key
	 * @return
	 */
	protected Float getFloatExtra(String key) {
		Intent receive = ((Activity) context).getIntent();
		Float flag;

		flag = receive.getFloatExtra(key, 0f);

		return flag;
	}

	/**
	 * 接收前一个页面传递的Long值
	 * 
	 * @param key
	 * @return
	 */
	protected Long getLongExtra(String key) {
		Intent receive = ((Activity) context).getIntent();
		Long flag;

		flag = receive.getLongExtra(key, 0L);

		return flag;
	}

	/**
	 * 接收前一个页面传递的Double值
	 * 
	 * @param key
	 * @return
	 */
	protected Double getDoubleExtra(String key) {
		Intent receive = ((Activity) context).getIntent();
		Double flag;

		flag = receive.getDoubleExtra(key, 0.0);

		return flag;
	}

	/**
	 * 通过类名启动Activity，是否结束本页面
	 * 
	 * @param pClass
	 * @param isfinish
	 */
	protected void openActivity(Class<?> pClass, boolean isfinish) {
		showActivity(pClass, null, null, null, null, isfinish);
	}

	/**
	 * 通过类名启动Activity，并且携带数据
	 * 
	 * @param pClass
	 * @param key
	 * @param value
	 */
	protected void openActivity(Class<?> pClass, String key, Serializable value) {
		showActivity(pClass, null, key, value, null, false);
	}

	/**
	 * 通过类名启动Activity，并且携带单个数据
	 * 
	 * @param pClass
	 * @param key
	 * @param value
	 * @param isfinish
	 */
	protected void openActivity(Class<?> pClass, String key,
			Serializable value, boolean isfinish) {
		showActivity(pClass, null, key, value, null, isfinish);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 * @param isfinish
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle,
			boolean isfinish) {
		showActivity(pClass, pBundle, null, null, null, isfinish);
	}

	/**
	 * 通过className启动Activity
	 * 
	 * @param className
	 * @param isfinish
	 */
	protected void openActivity(String className, boolean isfinish) {
		showActivity(null, null, null, null, className, isfinish);
	}

	protected void showActivity(Class<?> pClass, Bundle bundle, String key,
			Serializable value, String className, boolean isfinish) {

		Intent intent = new Intent(context, pClass);

		if (null != className) {
			intent.setClassName(((Activity) context).getApplicationContext()
					.getPackageName(), ((Activity) context)
					.getApplicationContext().getPackageName() + "." + className);
		} else {
			if (null != key) {
				intent.putExtra(key, value);
			}

			if (null != bundle) {
				intent.putExtras(bundle);
			}
		}

		startActivity(intent);

		((Activity) context).overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
		if (isfinish) {
			((Activity) context).finish();
		}
	}

	/**
	 * Toast提示
	 * 
	 * @param message
	 */
	protected void showToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast提示
	 * 
	 * @param id
	 */
	protected void showToast(int id) {
		Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
	}
}
