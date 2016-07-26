package com.cells.companyapp.view;

import java.util.List;

import com.cells.companyapp.utils.*;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.base.BaseAdapterHelper;
import com.cells.companyapp.base.CommonAdapter;
import com.cells.companyapp.been.YellowPage;
import com.cells.companyapp.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;

public class SearchYellowPageActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView back;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.button_search, click = "search")
	private ImageView search;
	@ViewInject(id = R.id.search_edittext)
	private EditText etName;

	@ViewInject(id = R.id.listview)
	private ListView listview;

	private List<YellowPage> yellowpage;

	private int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		tvTitle.setText("搜索");
		back.setImageResource(R.drawable.icon_back);
	}

	public void back(View v) {
		finish();
	}

	public void search(View v) {
		String name = etName.getText().toString().trim();
		if (StringUtil.isEmpty(name)) {
			showToast("请输入要查询的公司名称");
		} else {
			loading.show();
			searchYellowPage(page, name);
		}
	}

	private void searchYellowPage(int page, String name) {
		AjaxParams params = new AjaxParams();

		params.put("page", page);
		params.put("name", name);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.SEARCH_YELOW_PAGE, params, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String str = t.toString();
				loading.dismiss();

				yellowpage = (List<YellowPage>) JsonUtil.fromJson(str, new TypeToken<List<YellowPage>>() {
				});

				if (yellowpage.size() > 0) {
					listview.setAdapter(new CommonAdapter<YellowPage>(context, R.layout.item_search,
							yellowpage) {

						@Override
						public void onUpdate(BaseAdapterHelper helper, final YellowPage item, int position) {
							helper.setText(R.id.tv_name, item.getName());
							helper.setImageUrl(R.id.image, HttpUtils.ROOT_URL + item.getUrl());
							helper.setOnClickListener(R.id.layout_search, new OnClickListener() {

								@Override
								public void onClick(View v) {
									Bundle bundle = new Bundle();
									bundle.putInt("id", item.getId());
									bundle.putString("name", item.getName());
									openActivity(YellowPageInfoActivity.class, bundle, false);
								}
							});
						}
					});
				} else {
					showToast("没有搜索结果！");
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				loading.dismiss();
				if (t != null) {
					showToast("加载失败，请稍后再试！");
				}
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
}
