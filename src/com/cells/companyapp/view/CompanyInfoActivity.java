package com.cells.companyapp.view;

import java.io.File;

import com.cells.companyapp.utils.*;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.utils.AppConfig;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.widget.CircularImageView;

public class CompanyInfoActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.tv_company_info_name)
	private TextView name;

	@ViewInject(id = R.id.tv_company_info_type)
	private TextView type;
	@ViewInject(id = R.id.tv_company_info_scope)
	private TextView scope;
	@ViewInject(id = R.id.tv_company_info_addr)
	private TextView addr;

	@ViewInject(id = R.id.tv_company_info_establishes)
	private TextView establishes;
	@ViewInject(id = R.id.tv_company_info_expire)
	private TextView expire;
	@ViewInject(id = R.id.tv_company_info_license)
	private TextView license;
	@ViewInject(id = R.id.tv_company_info_verify)
	private TextView verify;

	@ViewInject(id = R.id.img_company_info_head_pic)
	private CircularImageView phone;

	private HttpHandler<File> handler;
	/** 下载的图片的储存路径 */
	private String logoName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_info);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		logoName = AppConfig.DOWNLOAD_LOGO_NAME;

		// int id = getIntExtra("id");
		tvTitle.setText("企业用户");
		ivTitleLeft.setImageResource(R.drawable.icon_back);

		String strName = (String) SPUtils.get(context, "c_name", "", AppConfig.LOGIN_INFO_DATA);
		if (null != strName) {
			name.setText(strName);
		}
		String strType = (String) SPUtils.get(context, "c_type", "", AppConfig.LOGIN_INFO_DATA);
		if (null != strType) {
			type.setText(strType);
		}
		String strScope = (String) SPUtils.get(context, "scopes", "", AppConfig.LOGIN_INFO_DATA);
		if (null != strScope) {
			scope.setText(strScope);
		}
		String strAddr = (String) SPUtils.get(context, "address", "", AppConfig.LOGIN_INFO_DATA);
		if (null != strAddr) {
			addr.setText(strAddr);
		}
		String strVerify = (String) SPUtils.get(context, "verify", "", AppConfig.LOGIN_INFO_DATA);
		if (null != strVerify) {
			verify.setText(strVerify);
		}
		String strLicense = (String) SPUtils.get(context, "license", "", AppConfig.LOGIN_INFO_DATA);
		if (null != strLicense) {
			license.setText(strLicense);
		}
		String strExpire = (String) SPUtils.get(context, "expire", "", AppConfig.LOGIN_INFO_DATA);
		if (null != strExpire) {
			expire.setText(strExpire + "年");
		}
		String strEstablishes = (String) SPUtils.get(context, "establishes", "", AppConfig.LOGIN_INFO_DATA);
		if (null != strEstablishes) {
			establishes.setText(strEstablishes);
		}

		String logoURL = (String) SPUtils.get(context, "logo_url", "", AppConfig.LOGIN_INFO_DATA);
		if (HttpUtils.ROOT_URL + "/companys/logo/thumb/missing.png" != logoURL) {
			FileUtils.DeleteFile(logoName);
			savPicture(logoURL);
		} else {
			phone.setImageResource(R.drawable.icon_company);
		}
	}

	private void savPicture(String logoURL) {
		// 保存到本地
		FinalHttp fh = new FinalHttp();
		handler = fh.download(logoURL, logoName, new AjaxCallBack<File>() {
			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(File t) {
				if (t != null) {
					handler.stop();
					phone.setImageBitmap(PictureUtils.getLocalImage(logoName));
				}
			}

			@Override
			public void onFailure(Throwable t, int errorCode, String strMsg) {
				super.onFailure(t, errorCode, strMsg);
				phone.setImageResource(R.drawable.icon_company);
			}
		});
	}

	public void back(View v) {
		finish();
	}
}
