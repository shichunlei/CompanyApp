package com.cells.companyapp.fragment;

import java.io.File;

import scl.leo.library.image.HeaderImageView;
import scl.leo.library.utils.other.*;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.utils.AppConfig;
import com.cells.companyapp.view.*;

public class RightMenuFragment extends BaseFragment {

	private View view;

	@ViewInject(id = R.id.ll_login)
	private LinearLayout layoutLogin;
	@ViewInject(id = R.id.ll_company_login, click = "companyLogin")
	private LinearLayout companyLogin;
	@ViewInject(id = R.id.ll_personal_login, click = "personalLogin")
	private LinearLayout personalLogin;

	@ViewInject(id = R.id.ll_layout_info, click = "checkInfo")
	private LinearLayout checkInfo;
	@ViewInject(id = R.id.img_head_pic)
	private HeaderImageView headpic;
	@ViewInject(id = R.id.tv_name)
	private TextView name;

	@ViewInject(id = R.id.tv_offline, click = "offline")
	private TextView offline;
	@ViewInject(id = R.id.tv_collection, click = "collection")
	private TextView collection;
	@ViewInject(id = R.id.tv_mode, click = "mode")
	private TextView mode;
	@ViewInject(id = R.id.tv_clear, click = "clear")
	private TextView clear;
	@ViewInject(id = R.id.tv_download, click = "download")
	private TextView download;
	@ViewInject(id = R.id.tv_introduce, click = "introduce")
	private TextView introduce;

	@ViewInject(id = R.id.ll_logout, click = "logout")
	private LinearLayout logout;

	private String coverName;

	/**
	 * status用来判断登录状态
	 * 
	 * status = 0：未登录状态
	 * 
	 * status = 1：登录用户为个人用户
	 * 
	 * status = 2：登录用户为企业用户
	 */
	private int status = 0;

	private int id;
	private String path = null;
	private String Name;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_right_menu, container, false);
		FinalActivity.initInjectedView(this, view);
		init();
		return view;
	}

	private void init() {
		coverName = AppConfig.DOWNLOAD_IMAGE_NAME;

		File file = new File(coverName);
		if (!file.exists()) {
			path = "";
		} else {
			path = coverName;
		}

		status = (Integer) SPUtils.get(getActivity(), "login_status", 0,
				AppConfig.LOGIN_STATUS_DATA);
		id = (Integer) SPUtils.get(getActivity(), "id", 0,
				AppConfig.LOGIN_INFO_DATA);
		Name = (String) SPUtils.get(getActivity(), "name", "",
				AppConfig.LOGIN_INFO_DATA);

		if (status == 0) {
			layoutLogin.setVisibility(View.VISIBLE);
			checkInfo.setVisibility(View.GONE);
			logout.setVisibility(View.GONE);
		} else {
			layoutLogin.setVisibility(View.GONE);
			checkInfo.setVisibility(View.VISIBLE);
			logout.setVisibility(View.VISIBLE);

			if (null == path || "".equals(path)) {
				if (status == 1) {
					headpic.setImageResource(R.drawable.icon_personal);
				} else if (status == 2) {
					headpic.setImageResource(R.drawable.icon_company);
				}
			} else {
				headpic.setImageBitmap(PictureUtils.getLocalImage(path));
			}
			name.setText(Name);
		}
	}

	/**
	 * 离线下载
	 * 
	 * @param v
	 */
	public void offline(View v) {
		openActivity(OfflineActivity.class, false);
	}

	/**
	 * 收藏
	 * 
	 * @param v
	 */
	public void collection(View v) {
		openActivity(CollectionActivity.class, false);
	}

	/**
	 * 夜间模式
	 * 
	 * @param v
	 */
	public void mode(View v) {

	}

	/**
	 * 视刊下载
	 * 
	 * @param v
	 */
	public void download(View v) {
		openActivity(CompanyLoginActivity.class, false);
	}

	/**
	 * 清理缓存
	 * 
	 * @param v
	 */
	public void clear(View v) {

	}

	/**
	 * 企业用户登录
	 * 
	 * @param v
	 */
	public void companyLogin(View v) {
		openActivity(CompanyLoginActivity.class, false);
	}

	/**
	 * 个人用户登录
	 * 
	 * @param v
	 */
	public void personalLogin(View v) {
		openActivity(PersonalLoginActivity.class, false);
	}

	/**
	 * 查看个人（企业）信息
	 * 
	 * @param v
	 */
	public void checkInfo(View v) {
		if (status == 1) {
			openActivity(PersonalInfoActivity.class, "id", id, false);
		} else if (status == 2) {
			openActivity(CompanyInfoActivity.class, "id", id, false);
		}
	}

	/**
	 * 注销账号
	 * 
	 * @param v
	 */
	public void logout(View v) {
		SPUtils.put(context, "login_status", 0, AppConfig.LOGIN_STATUS_DATA);
		// 清空本地保存的用户登录信息
		SPUtils.clear(context, AppConfig.LOGIN_INFO_DATA);
		FileUtils.DeleteFile(coverName);
		init();
	}

	/**
	 * 功能介绍
	 * 
	 * @param v
	 */
	public void introduce(View v) {
		openActivity(GuideScreenActivity.class, "flog", "1", false);
	}

	@Override
	public void onResume() {
		init();
		super.onResume();
	}
}
