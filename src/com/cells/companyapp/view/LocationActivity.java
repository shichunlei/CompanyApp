package com.cells.companyapp.view;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;

/**
 * 企业地图板块
 * 
 * @author leo
 *
 */
public class LocationActivity extends BaseActivity {

	private static final String TAG = "LocationActivity";

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView back;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		Bundle bundle = getIntent().getExtras();
		String name = bundle.getString("name");
		String addr = bundle.getString("addr");
		Log.i(TAG, addr);

		tvTitle.setText(name);
		back.setImageResource(R.drawable.icon_back);
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		finish();
	}
}
