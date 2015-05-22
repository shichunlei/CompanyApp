package com.cells.companyapp.view;

import java.util.ArrayList;
import java.util.List;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import scl.leo.library.utils.other.JsonUtil;
import scl.leo.library.utils.other.StringUtil;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.adapter.SearchYPAdapter;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.YellowPage;
import com.cells.companyapp.customview.refresh.XListView;
import com.cells.companyapp.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;

public class SearchYellowPageActivity extends BaseActivity {

	private static final String TAG = "SearchYellowPageActivity";

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView back;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	@ViewInject(id = R.id.button_search, click = "search")
	private ImageView search;
	@ViewInject(id = R.id.search_edittext)
	private EditText etName;

	@ViewInject(id = R.id.xlistview, itemClick = "itemClick")
	private XListView listview;

	private List<YellowPage> yellowpage;
	private List<YellowPage> yellowpageList = new ArrayList<YellowPage>();

	private SearchYPAdapter adapter;

	private int page = 1;

	private CircularProgressDialog loading;

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

		loading = CircularProgressDialog.show(context);

		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);
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

		params.put("page", page + "");
		params.put("name", name);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.TIME_OUT);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.SEARCH_YELOW_PAGE, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						Log.i(TAG, str);
						loading.dismiss();

						yellowpage = (List<YellowPage>) JsonUtil.fromJson(str,
								new TypeToken<List<YellowPage>>() {
								});

						if (yellowpageList != null) {
							yellowpageList.clear();
						}
						if (yellowpage.size() > 0) {
							yellowpageList.addAll(yellowpage);
							yellowpage.clear();
						} else {
							showToast("没有搜索结果！");
						}

						adapter = new SearchYPAdapter(context);
						adapter.replaceWith(yellowpageList);
						listview.setAdapter(adapter);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						listview.stopLoadMore();
						loading.dismiss();
						if (t != null) {
							showToast("加载失败，请稍后再试！");
						}
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	public void itemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int _id = yellowpageList.get(position - 1).getId();
		String name = yellowpageList.get(position - 1).getName();
		Bundle bundle = new Bundle();
		bundle.putInt("id", _id);
		bundle.putString("name", name);
		openActivity(YellowPageInfoActivity.class, bundle, false);
	}
}
