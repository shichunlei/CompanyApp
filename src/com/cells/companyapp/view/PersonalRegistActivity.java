package com.cells.companyapp.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cells.companyapp.utils.*;
import com.cells.companyapp.widget.*;
import com.cells.companyapp.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.cells.companyapp.widget.ActionSheetDialog.SheetItemColor;
import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.User;

public class PersonalRegistActivity extends BaseActivity {

	private static final int ALBUM = 1;
	private static final int CAMERA = 2;
	private static final int ZOOM = 3;

	@ViewInject(id = R.id.img_regist_add_photo, click = "addPhone")
	private CircularImageView phone;

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.et_regist_name)
	private EditText etName;
	@ViewInject(id = R.id.et_regist_email)
	private EditText etEmail;
	@ViewInject(id = R.id.et_regist_pwd)
	private EditText etPwd;
	@ViewInject(id = R.id.et_regist_repwd)
	private EditText etRePwd;
	@ViewInject(id = R.id.tv_regist_gender, click = "selectGender")
	private TextView tvGender;
	@ViewInject(id = R.id.btn_regist, click = "regist")
	private Button regist;

	@ViewInject(id = R.id.cb_agreement, click = "agreed")
	private CheckBox agreed;
	@ViewInject(id = R.id.tv_agreement, click = "agreement")
	private TextView agreement;

	private String gender = "2";

	/** 是否选择头像 */
	private Boolean is_addpicture = false;

	private PopupWindow poppWindow;
	private View popview;
	/** 相册 */
	private TextView tvAlbum;
	/** 拍照 */
	private TextView tvCamera;
	/** 取消 */
	private TextView tvCacel;

	/** 封面图片名称（包括全路径） */
	private String coverName = "";
	/** 调用相机拍摄照片的名字(临时) */
	private String takePicturePath;

	User user = new User();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_regist);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		tvTitle.setText("个人注册");
		ivTitleLeft.setImageResource(R.drawable.icon_back);

		popview = LayoutInflater.from(context).inflate(R.layout.pop_add_phone,
				null);

		tvCacel = (TextView) popview.findViewById(R.id.app_cancle);
		tvAlbum = (TextView) popview.findViewById(R.id.app_album);
		tvCamera = (TextView) popview.findViewById(R.id.app_camera);

		poppWindow = new PopupWindow(popview, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		/**
		 * 相册选择
		 */
		tvAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				takePicturePath = getIntent().getStringExtra("data");
				startActivityForResult(intent, ALBUM);
				poppWindow.dismiss();
			}
		});

		/**
		 * 拍照
		 */
		tvCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				takePicturePath = AppConfig.TEMPORARY_IMAGE_NAME;

				File image = new File(takePicturePath);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
				startActivityForResult(intent, CAMERA);
				poppWindow.dismiss();

			}
		});

		/**
		 * 取消
		 */
		tvCacel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				poppWindow.dismiss();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				// 如果是直接从相册获取
				case ALBUM:
					startPhotoZoom(data.getData());
					break;
				// 如果是调用相机拍照
				case CAMERA:
					File temp = new File(takePicturePath);
					startPhotoZoom(Uri.fromFile(temp));
					break;
				// 取得裁剪后的图片
				case ZOOM:
					if (data != null) {
						setPicToView(data);
					}
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 360);
		intent.putExtra("outputY", 360);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			FileOutputStream b = null;
			coverName = AppConfig.TEMP_IMAGE_NAME;
			try {
				b = new FileOutputStream(coverName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				is_addpicture = true;
				phone.setImageBitmap(bitmap);
			}
		}
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		finish();
	}

	/**
	 * 验证注册信息
	 * 
	 * @param v
	 */
	public void regist(View v) {
		String name = etName.getText().toString().trim();
		String email = etEmail.getText().toString().trim();
		String pwd = etPwd.getText().toString().trim();
		String repwd = etRePwd.getText().toString().trim();

		if (!is_addpicture) {
			showToast("请选择头像");
		} else if (StringUtil.isEmpty(name)) {
			showToast("用户名不能为空");
		} else if (StringUtil.isEmpty(email)) {
			showToast("邮箱不能为空");
		} else if (StringUtil.isEmpty(pwd)) {
			showToast("密码不能为空");
		} else if (!StringUtil.pwdVal(pwd, repwd)) {
			showToast("两次密码不一致");
		} else if (!agreed.isChecked()) {
			showToast("请同意注册协议");
		} else {
			loading.show();
			postRegist(name, email, pwd, gender);
		}
	}

	/**
	 * 提交注册信息
	 * 
	 * @param name
	 * @param email
	 * @param pwd
	 * @param gender
	 */
	private void postRegist(String name, String email, String pwd, String gender) {
		AjaxParams params = new AjaxParams();
		params.put("name", name);
		params.put("email", email);
		params.put("password", pwd);
		params.put("password_confirmation", pwd);
		params.put("gender", gender);

		try {
			params.put("avatar", new File(coverName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		FinalHttp fh = new FinalHttp();
		fh.post(ApiUtils.ROOT_URL + ApiUtils.REGISTER, params,
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
							showToast("注册成功");

							FileUtils.renameToFile(coverName,
									AppConfig.DOWNLOAD_IMAGE_NAME);

							saveData();
							finish();
						} else {
							showToast(user.getError());
						}
						loading.dismiss();
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

	protected void saveData() {
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

	/**
	 * 添加头像
	 * 
	 * @param v
	 */
	public void addPhone(View v) {
		poppWindow.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#b0000000")));
		poppWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		poppWindow.setAnimationStyle(R.style.app_pop);
		poppWindow.setOutsideTouchable(true);
		poppWindow.setFocusable(true);
		poppWindow.update();
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

	public void agreement(View v) {
		openActivity(AgreementActivity.class, false);
	}
}
