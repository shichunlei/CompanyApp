package com.cells.companyapp.view;

import java.io.File;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.utils.*;
import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.User;

public class PersonalLoginActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.btn_login, click = "login")
	private Button login;
	@ViewInject(id = R.id.btn_regist, click = "regist")
	private Button regist;

	@ViewInject(id = R.id.et_login_name)
	private EditText etName;
	@ViewInject(id = R.id.et_login_pwd)
	private EditText etPwd;

	User user = new User();

	HttpHandler<File> handler;
	/** 下载的图片的储存路径 */
	private String coverName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_login);
		FinalActivity.initInjectedView(this);

		init();
	}

	private void init() {
		tvTitle.setText("个人登录");
		ivTitleLeft.setImageResource(R.drawable.icon_back);
		coverName = AppConfig.DOWNLOAD_IMAGE_NAME;
	}

	public void back(View v) {
		finish();
	}

	public void login(View v) {
		String name = etName.getText().toString().trim();
		String pwd = etPwd.getText().toString().trim();

		if (StringUtil.isEmpty(name)) {
			showToast("账户不能为空");
		} else if (StringUtil.isEmpty(pwd)) {
			showToast("密码不能为空");
		} else {
			loading.show();
			postLogin(name, pwd);
		}
	}

	private void postLogin(String name, String pwd) {
		FinalHttp fh = new FinalHttp();
		AjaxParams params = new AjaxParams();

		if (StringUtil.isEmail(name)) {
			params.put("email", name);
		} else {
			params.put("name", name);
		}
		params.put("password", pwd);
		fh.configTimeout(ApiUtils.TIME_OUT);
		fh.post(ApiUtils.ROOT_URL + ApiUtils.LOGIN, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						user = (User) JsonUtil.fromJson(str, User.class);
						if (user.isStatus()) {

							if (!(ApiUtils.ROOT_URL + "/user/avatar/thumb/missing.png")
									.equals(user.getAvatar())) {
								FileUtils.DeleteFile(coverName);
								savPicture(user.getAvatar());
							}
						} else {
							loading.dismiss();
							showToast("登录失败，请稍后再试");
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

	private void savPicture(String pictureURL) {
		// 保存到本地
		FinalHttp fh = new FinalHttp();
		handler = fh.download(pictureURL, coverName, new AjaxCallBack<File>() {
			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(File t) {
				if (t != null) {
					showToast("登录成功");
					handler.stop();
					saveData();
					loading.dismiss();
					finish();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorCode, String strMsg) {
				super.onFailure(t, errorCode, strMsg);
				loading.dismiss();
			}
		});
	}

	protected void saveData() {
		SPUtils.put(context, "login_status", 1, AppConfig.LOGIN_STATUS_DATA);
		SPUtils.put(context, "id", user.getId(), AppConfig.LOGIN_INFO_DATA);
		if (null != user.getName()) {
			SPUtils.put(context, "name", user.getName(),
					AppConfig.LOGIN_INFO_DATA);
		}
		if (null != user.getEmail()) {
			SPUtils.put(context, "email", user.getEmail(),
					AppConfig.LOGIN_INFO_DATA);
		}
		if (null != user.getAuth_token()) {
			SPUtils.put(context, "token", user.getAuth_token(),
					AppConfig.LOGIN_INFO_DATA);
		}
		String gender = user.getGender();
		SPUtils.put(context, "gender", gender, AppConfig.LOGIN_INFO_DATA);
	}

	public void regist(View v) {
		openActivity(PersonalRegistActivity.class, false);
	}
}
