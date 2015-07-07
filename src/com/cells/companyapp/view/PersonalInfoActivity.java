package com.cells.companyapp.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.image.HeaderImageView;
import scl.leo.library.utils.other.FileUtils;
import scl.leo.library.utils.other.JsonUtil;
import scl.leo.library.utils.other.PictureUtils;
import scl.leo.library.utils.other.SPUtils;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Result;
import com.cells.companyapp.utils.AppConfig;
import com.cells.companyapp.utils.HttpUtils;

public class PersonalInfoActivity extends BaseActivity {

	private static final String TAG = "PersonalInfoActivity";

	private static final int ALBUM = 1;
	private static final int CAMERA = 2;
	private static final int ZOOM = 3;

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.tvTitleRigh, click = "edit")
	private TextView tvTitleRight;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.tv_personal_info_name)
	private TextView tvName;
	@ViewInject(id = R.id.tv_personal_info_email)
	private TextView tvEmail;
	@ViewInject(id = R.id.tv_personal_info_gender)
	private TextView tvGender;

	@ViewInject(id = R.id.img_regist_add_photo, click = "updateHeadPic")
	private HeaderImageView imgHeadPic;

	private String pictureName;
	private String path = null;

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

	private CircularProgressDialog loading;

	int id;

	Result result = new Result();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		FinalActivity.initInjectedView(this);
		tvTitleRight.setText("编辑");
		init();
	}

	private void init() {
		loading = CircularProgressDialog.show(context);

		pictureName = AppConfig.DOWNLOAD_IMAGE_NAME;
		File file = new File(pictureName);
		if (!file.exists()) {
			path = "";
		} else {
			path = pictureName;
		}

		id = getIntExtra("id");
		Log.i(TAG, id + "");
		tvTitle.setText("个人用户");
		ivTitleLeft.setImageResource(R.drawable.icon_back);

		String name = (String) SPUtils.get(context, "name", "",
				AppConfig.LOGIN_INFO_DATA);
		String email = (String) SPUtils.get(context, "email", "",
				AppConfig.LOGIN_INFO_DATA);
		String gender = (String) SPUtils.get(context, "gender", "未知",
				AppConfig.LOGIN_INFO_DATA);

		if (null != name) {
			tvName.setText(name);
		}
		if (null != email) {
			tvEmail.setText(email);
		}
		tvGender.setText(gender);

		if (null == path || "".equals(path)) {
			imgHeadPic.setImageResource(R.drawable.add_photo);
		} else {
			imgHeadPic.setImageBitmap(PictureUtils.getLocalImage(path));
		}

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

				Log.i(TAG, "takePicturePath = " + takePicturePath);

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
				tvTitleRight.setText("编辑");
			}
		});
	}

	public void back(View v) {
		finish();
	}

	public void edit(View v) {
		if ("完成".equals(tvTitleRight.getText().toString().trim())) {
			if (is_addpicture) {
				loading.show();
				postUpdateHeadPic(coverName);
			}
		} else if ("编辑".equals(tvTitleRight.getText().toString().trim())) {
			openActivity(EditPersonalInfoActivity.class, false);
		}
	}

	private void postUpdateHeadPic(final String coverName) {
		AjaxParams params = new AjaxParams();

		String token = (String) SPUtils.get(context, "token", "",
				AppConfig.LOGIN_INFO_DATA);

		params.put("id", id + "");
		params.put("auth_token", token);
		try {
			params.put("avatar", new File(coverName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		FinalHttp fh = new FinalHttp();
		fh.put(HttpUtils.ROOT_URL + HttpUtils.UPLOAD_AVATAR, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						Log.i(TAG, "修改头像返回结果： " + str);

						result = (Result) JsonUtil.fromJson(str, Result.class);
						if (result.isStatus()) {
							showToast("修改成功");

							FileUtils.renameToFile(coverName, AppConfig.DOWNLOAD_IMAGE_NAME);
						} else {
							showToast("失败");
						}

						tvTitleRight.setText("编辑");
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

	public void updateHeadPic(View v) {
		tvTitleRight.setText("完成");

		poppWindow.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#b0000000")));
		poppWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		poppWindow.setAnimationStyle(R.style.app_pop);
		poppWindow.setOutsideTouchable(true);
		poppWindow.setFocusable(true);
		poppWindow.update();
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
				imgHeadPic.setImageBitmap(bitmap);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
