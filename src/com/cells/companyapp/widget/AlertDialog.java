package com.cells.companyapp.widget;

import com.cells.companyapp.R;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 自定义 AlertDialog 仿IOS
 * 
 * @author 师春雷
 * 
 */
public class AlertDialog {

	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	/** 标头 */
	private TextView txt_title;
	/** 提示信息 */
	private TextView txt_msg;
	/** 输入框信息 */
	private EditText edittxt_result;
	private LinearLayout dialog_Group;
	private View dialog_marBottom;
	/** 确定按钮 */
	private Button btn_neg;
	/** 取消按钮 */
	private Button btn_pos;
	private View img_line;
	private Display display;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showEditText = false;
	private boolean showLayout = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;

	public AlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	@SuppressWarnings("deprecation")
	public AlertDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_alertdialog, null);

		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		txt_msg.setVisibility(View.GONE);
		edittxt_result = (EditText) view.findViewById(R.id.edittxt_result);
		edittxt_result.setVisibility(View.GONE);
		dialog_Group = (LinearLayout) view.findViewById(R.id.dialog_Group);
		dialog_Group.setVisibility(View.GONE);
		dialog_marBottom = (View) view.findViewById(R.id.dialog_marBottom);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_neg.setVisibility(View.GONE);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);
		img_line = (View) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		return this;
	}

	public AlertDialog setTitle(String title) {
		showTitle = true;
		if (null == title || "".equals(title)) {
			txt_title.setText("温馨提示");
		} else {
			txt_title.setText(title);
		}
		return this;
	}

	public AlertDialog setHidden(boolean isHidden) {
		if (isHidden) {
			// 隐藏密码
			edittxt_result
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							6) });
			edittxt_result.setInputType(EditorInfo.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
		} else {
			// 显示密码
			edittxt_result.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			edittxt_result
					.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
		}

		return this;
	}

	public AlertDialog setEditText(String hint) {
		showEditText = true;
		if (null == hint || "".equals(hint)) {
			edittxt_result.setHint("请输入内容");
		} else {
			edittxt_result.setHint(hint);
		}
		return this;
	}

	public String getResult() {
		return edittxt_result.getText().toString().trim();
	}

	public AlertDialog setMsg(String msg) {
		showMsg = true;
		if (null == msg || "".equals(msg)) {
			txt_msg.setText("内容");
		} else {
			txt_msg.setText(msg);
		}
		return this;
	}

	public AlertDialog setView(View view) {
		showLayout = true;
		if (view == null) {
			showLayout = false;
		} else
			dialog_Group.addView(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		return this;
	}

	public AlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public AlertDialog setPositiveButton(String text,
			final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_pos.setText("确定");
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public AlertDialog setNegativeButton(String text,
			final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			btn_neg.setText("取消");
		} else {
			btn_neg.setText(text);
		}

		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != listener) {
					listener.onClick(v);
				}
				dialog.dismiss();
			}
		});

		return this;
	}

	private void setLayout() {
		if (!showTitle && !showMsg) {
			txt_title.setText("温馨提示");
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showEditText) {
			edittxt_result.setVisibility(View.VISIBLE);
		}

		if (showMsg) {
			txt_msg.setVisibility(View.VISIBLE);
		}

		if (showLayout) {
			dialog_Group.setVisibility(View.VISIBLE);
			dialog_marBottom.setVisibility(View.GONE);
		}

		if (!showPosBtn && !showNegBtn) {
			btn_pos.setText("确定");
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			btn_pos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}

		if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}

		if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (!showPosBtn && showNegBtn) {
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
	}

	public void show() {
		setLayout();
		// 调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
		dialog.setCanceledOnTouchOutside(false);
		// 调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
		// dialog.setCancelable(false);
		dialog.show();
	}
}
