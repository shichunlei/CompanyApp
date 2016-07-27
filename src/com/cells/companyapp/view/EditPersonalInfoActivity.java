package com.cells.companyapp.view;

import com.cells.companyapp.utils.*;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Result;
import com.cells.companyapp.utils.AppConfig;
import com.cells.companyapp.utils.ApiUtils;
import com.cells.companyapp.widget.ActionSheetDialog;
import com.cells.companyapp.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.cells.companyapp.widget.ActionSheetDialog.SheetItemColor;

public class EditPersonalInfoActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.et_update_personal_name)
	private EditText etName;
	@ViewInject(id = R.id.et_update_personal_pwd)
	private EditText etPwd;
	@ViewInject(id = R.id.et_update_personal_repwd)
	private EditText etRePwd;
	@ViewInject(id = R.id.tv_update_personal_gender, click = "selectGender")
	private TextView tvGender;

	@ViewInject(id = R.id.btn_submit, click = "submit")
	private Button submit;

	private String gender = "";

	Result result = new Result();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_edit);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		tvTitle.setText("修改个人信息");
		ivTitleLeft.setImageResource(R.drawable.icon_back);

		String name = (String) SPUtils.get(context, "name", "",
				AppConfig.LOGIN_INFO_DATA);
		String strgender = (String) SPUtils.get(context, "gender", "",
				AppConfig.LOGIN_INFO_DATA);
		if (null != name) {
			etName.setText(name);
		}
		if (null != strgender) {
			tvGender.setText(strgender);
			if (strgender.equals("男")) {
				gender = "0";
			} else if (strgender.equals("女")) {
				gender = "1";
			}
		}
	}

	public void back(View v) {
		finish();
	}

	public void submit(View v) {
		String name = etName.getText().toString().trim();
		String pwd = etPwd.getText().toString().trim();
		String repwd = etRePwd.getText().toString().trim();

		if (StringUtil.isEmpty(name)) {
			showToast("用户名不能为空");
		} else if (StringUtil.isEmpty(pwd)) {
			showToast("密码不能为空");
		} else if (!StringUtil.pwdVal(pwd, repwd)) {
			showToast("两次密码不一致");
		} else if (StringUtil.isEmpty(gender)) {
			showToast("请选择性别");
		} else {
			loading.show();
			submitPersonalInfo(name, pwd, gender);
		}
	}

	private void submitPersonalInfo(String name, String pwd, String gender) {
		AjaxParams params = new AjaxParams();

		int id = (Integer) SPUtils.get(context, "id", 0,
				AppConfig.LOGIN_INFO_DATA);

		params.put("id", id + "");
		params.put("name", name);
		params.put("password", pwd);
		params.put("gender", gender);

		FinalHttp fh = new FinalHttp();
		fh.put(ApiUtils.ROOT_URL + ApiUtils.UPDATE_PROFILE, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						loading.dismiss();

						result = (Result) JsonUtil.fromJson(str, Result.class);
						if (result.isStatus()) {
							showToast("修改成功");
							finish();
						} else {
							showToast("修改失败");
						}
					}

					@Override
					public void onFailure(Throwable t, int errorCode,
							String strMsg) {
						super.onFailure(t, errorCode, strMsg);
						if (t != null) {
							loading.dismiss();
							showToast("网络环境不稳定，请稍后再试");
						}
					}
				});
	}

	/**
	 * 选择性别
	 * 
	 * @param v
	 */
	public void selectGender(View v) {
		new ActionSheetDialog(context)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(true)
				.addSheetItem(getString(R.string.man), SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								gender = "0";
								tvGender.setText(getString(R.string.man));
							}
						})
				.addSheetItem(getString(R.string.women), SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								gender = "1";
								tvGender.setText(getString(R.string.women));
							}
						}).show();
	}
}
