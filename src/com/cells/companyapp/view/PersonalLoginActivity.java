package com.cells.companyapp.view;

import java.io.File;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.image.RoundImageView;
import scl.leo.library.utils.other.*;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.User;
import com.cells.companyapp.utils.AppConfig;
import com.cells.companyapp.utils.HttpUtils;

public class PersonalLoginActivity extends BaseActivity {

	private static final String TAG = "PersonalLoginActivity";

	@ViewInject(id = R.id.image)
	private RoundImageView image;

	private CircularProgressDialog loading;

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

		loading = CircularProgressDialog.show(context);

		Animation operatingAnim = AnimationUtils.loadAnimation(this,
				R.anim.rotating);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			image.startAnimation(operatingAnim);
		}
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
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.post(HttpUtils.ROOT_URL + HttpUtils.LOGIN, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						Log.i(TAG, "个人登录返回结果：" + str);
						user = (User) JsonUtil.fromJson(str, User.class);
						if (user.isStatus()) {
							Log.i(TAG, user.getAvatar());

							if (!(HttpUtils.ROOT_URL + "/user/avatar/thumb/missing.png")
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
				Log.i(TAG, current + "/" + count);
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
				if (t != null) {
					Log.i(TAG, t.toString());
				}
				if (strMsg != null) {
					Log.i(TAG, strMsg);
				}
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
			Log.i(TAG, "==============" + user.getAuth_token());
			SPUtils.put(context, "token", user.getAuth_token(),
					AppConfig.LOGIN_INFO_DATA);
		}
		String gender = "男";
		if (user.getGender() == 0) {
			gender = "男";
		} else if (user.getGender() == 1) {
			gender = "女";
		} else {
			gender = "未知";
		}
		SPUtils.put(context, "gender", gender, AppConfig.LOGIN_INFO_DATA);
	}

	public void regist(View v) {
		openActivity(PersonalRegistActivity.class, false);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		image.clearAnimation();
	}
}
