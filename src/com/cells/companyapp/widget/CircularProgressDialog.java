package com.cells.companyapp.widget;

import com.cells.companyapp.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class CircularProgressDialog extends Dialog {

	private static CircularProgressDialog progressDialog;

	private CircularProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param cancelable
	 * 
	 * @return
	 */
	public static CircularProgressDialog show(Context context, String title, String message,
			boolean cancelable) {
		progressDialog = new CircularProgressDialog(context, R.style.MyCustomDialog);
		progressDialog.setContentView(R.layout.view_progress_dialog);
		progressDialog.setCancelable(cancelable);
		progressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		TextView tv_message = (TextView) progressDialog.findViewById(R.id.tv_message);
		if (null == message || "".equals(message))
			tv_message.setVisibility(View.GONE);
		else
			tv_message.setText(message);
		return progressDialog;
	}

	public static CircularProgressDialog show(Context context, String title, String message) {
		return show(context, title, message, false);
	}

	public static CircularProgressDialog show(Context context, String message) {
		return show(context, "", message, false);
	}

	public static CircularProgressDialog show(Context context) {
		return show(context, "");
	}

	@Override
	public void cancel() {
		super.cancel();
		progressDialog = null;
	}

	@Override
	public void dismiss() {
		super.dismiss();
		progressDialog = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
