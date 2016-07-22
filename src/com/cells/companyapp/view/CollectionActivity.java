package com.cells.companyapp.view;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.baoyz.swipemenulistview.*;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.cells.companyapp.R;
import com.cells.companyapp.adapter.CollectionAdapter;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Collection;
import com.cells.companyapp.utils.DBUtils;
import com.cells.companyapp.utils.DisplayUtil;
import com.cells.companyapp.widget.AlertDialog;

public class CollectionActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	private List<Collection> collection;
	private List<Collection> collectionList = new ArrayList<Collection>();
	private List<Collection> list = new ArrayList<Collection>();

	private CollectionAdapter adapter;

	@ViewInject(id = R.id.listview)
	private SwipeMenuListView listview;

	private DBUtils dbUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		FinalActivity.initInjectedView(this);
		init();
	}

	private void init() {
		tvTitle.setText("我的收藏");
		ivTitleLeft.setImageResource(R.drawable.icon_back);

		dbUtil = new DBUtils(context);

		getCollectionList();
	}

	private void initSwipeMenu() {
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(DisplayUtil.dp2px(context, 90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		listview.setMenuCreator(creator);

		// step 2. listener item click event
		listview.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					new AlertDialog(context).builder().setTitle("").setMsg("").setNegativeButton("", null)
							.setPositiveButton("", new OnClickListener() {

								@Override
								public void onClick(View v) {
									delete(position);
								}

							}).show();
					break;
				}

				return false;
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (collectionList.get(position).getType() == 0) {
					Bundle bundle = new Bundle();
					bundle.putInt("id", collectionList.get(position).getCollection_id());
					bundle.putString("name", collectionList.get(position).getName());
					openActivity(NewsInfoActivity.class, bundle, false);
				} else {
					Bundle bundle = new Bundle();
					bundle.putInt("id", collectionList.get(position).getCollection_id());
					bundle.putInt("type", collectionList.get(position).getType());
					openActivity(CultrueInfoActivity.class, bundle, false);
				}
			}
		});
	}

	private void delete(int position) {
		// other setting
		listview.setCloseInterpolator(new BounceInterpolator());
		if (list != null) {
			list.clear();
		}
		list.addAll(collectionList);

		dbUtil.deleteById(list.get(position).getId());
		adapter.removeItem(position);
		listview.setAdapter(adapter);
	}

	private void getCollectionList() {
		collection = dbUtil.queryAll();

		if (collection.size() > 0) {
			if (collectionList != null) {
				collectionList.clear();
			}
			collectionList.addAll(collection);
			collection.clear();

			adapter = new CollectionAdapter(context, collectionList);
			listview.setAdapter(adapter);

			initSwipeMenu();
		}
	}

	public void back(View v) {
		finish();
	}
}
