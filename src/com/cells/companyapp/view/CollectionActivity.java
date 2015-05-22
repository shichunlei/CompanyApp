package com.cells.companyapp.view;

import java.util.ArrayList;
import java.util.List;

import scl.leo.library.listview.swipemenulistview.SwipeMenu;
import scl.leo.library.listview.swipemenulistview.SwipeMenuCreator;
import scl.leo.library.listview.swipemenulistview.SwipeMenuItem;
import scl.leo.library.listview.swipemenulistview.SwipeMenuListView;
import scl.leo.library.listview.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cells.companyapp.R;
import com.cells.companyapp.adapter.CollectionAdapter;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Collection;
import com.cells.companyapp.utils.DBUtils;

public class CollectionActivity extends BaseActivity {

	@ViewInject(id = R.id.ivTitleBtnLeft, click = "back")
	private ImageView ivTitleLeft;
	@ViewInject(id = R.id.ivTitleName)
	private TextView tvTitle;

	private List<Collection> collection;
	private List<Collection> collectionList = new ArrayList<Collection>();

	private CollectionAdapter adapter;

	@ViewInject(id = R.id.listview)
	private SwipeMenuListView listview;

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

		getCollectionList();

		initSwipeMenu();
	}

	private void initSwipeMenu() {
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
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
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {

				switch (index) {
				case 0:
					showToast(collectionList.get(position).getName());
					break;
				case 1:
					collectionList.remove(position);
					DBUtils dbUtil = new DBUtils(context);
					dbUtil.deleteById(collectionList.get(position).getId());
					adapter.notifyDataSetChanged();
					break;
				}
				return false;
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showToast(collectionList.get(position).getName());
			}
		});

		// other setting
		listview.setCloseInterpolator(new BounceInterpolator());
	}

	private void getCollectionList() {
		DBUtils dbUtil = new DBUtils(context);
		collection = dbUtil.queryAll();

		if (collection.size() > 0) {
			if (collectionList != null) {
				collectionList.clear();
			}
			collectionList.addAll(collection);
			collection.clear();

			adapter = new CollectionAdapter(context, collectionList);
			listview.setAdapter(adapter);
		}
	}

	public void back(View v) {
		finish();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}