package com.cells.companyapp.fragment;

import java.util.List;

import scl.leo.library.dialog.circularprogress.CircularProgressDialog;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cells.companyapp.R;
import com.cells.companyapp.adapter.WindowsAdapter;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.been.Windows;
import com.cells.companyapp.customview.refresh.XListView;
import com.cells.companyapp.customview.refresh.XListView.FooterListener;
import com.cells.companyapp.customview.refresh.XListView.HeaderListener;

public class WindowsItemFragment extends BaseFragment implements
		FooterListener, HeaderListener {

	private static final String TAG = "WindowsItemFragment";

	@ViewInject(id = R.id.xlistview)
	private XListView listview;

	private WindowsAdapter adapter;

	private List<Windows> window;

	private View view;

	private String WINDOWS = "";

	private int page = 1;
	private boolean isfirst = false;
	int culture_type;

	private CircularProgressDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_windows_items, container,
				false);
		FinalActivity.initInjectedView(this, view);
		init();
		setItemClick();
		return view;
	}

	private void init() {

	}

	@Override
	public void onRefresh() {
		page = 1;
		getCultureList(page, WINDOWS, 1);
	}

	@Override
	public void onLoadMore() {
		page++;
		getCultureList(page, WINDOWS, 2);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!isfirst) {
			page = 1;
			listview.setAdapter(adapter);
			loading.show();
			getCultureList(page, WINDOWS, 2);
		}
		isfirst = true;
	}

	private void getCultureList(int page, String windows, int type) {

	}

	private void setItemClick() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int _id = ((Windows) adapter.getItem(position - 1)).getId();
				Bundle bundle = new Bundle();
				bundle.putInt("id", _id);
			}
		});
	}
}
