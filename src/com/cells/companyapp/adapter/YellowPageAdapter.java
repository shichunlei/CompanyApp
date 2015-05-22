package com.cells.companyapp.adapter;

import java.util.LinkedList;
import java.util.List;

import com.cells.companyapp.R;
import com.cells.companyapp.been.YellowPage;
import com.cells.companyapp.customview.refresh.YListView;
import com.cells.companyapp.utils.HttpUtils;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YellowPageAdapter extends BaseAdapter {

	private LinkedList<YellowPage> linklist;
	private Context context = null;

	private ViewHolder holder;

	public YellowPageAdapter(Context context, YListView xListView) {
		linklist = new LinkedList<YellowPage>();
		this.context = context;
	}

	@Override
	public int getCount() {
		return linklist.size();
	}

	public LinkedList<YellowPage> getmInfos() {
		return linklist;
	}

	public void setmInfos(LinkedList<YellowPage> list) {
		this.linklist = list;
	}

	@Override
	public Object getItem(int position) {
		return linklist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addItemLast(List<YellowPage> datas) {
		for (int i = 0; i < datas.size(); i++) {
			linklist.addLast(datas.get(i));
		}
	}

	public void addItemTop(List<YellowPage> datas) {
		for (int i = datas.size() - 1; i >= 0; i--) {
			linklist.addFirst(datas.get(i));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		YellowPage duitangInfo = linklist.get(position);

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(parent
					.getContext());
			convertView = layoutInflator.inflate(R.layout.item_yellow_page,
					null);
			holder = new ViewHolder();

			holder.imageView = (ImageView) convertView
					.findViewById(R.id.news_pic);

			holder.name = (TextView) convertView.findViewById(R.id.news_title2);

			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		holder.name.setText(duitangInfo.getName());

		FinalBitmap fb = FinalBitmap.create(context);
		fb.configLoadfailImage(R.drawable.loading_icon);
		fb.configLoadingImage(R.drawable.loading_icon);
		fb.display(holder.imageView, HttpUtils.ROOT_URL + duitangInfo.getUrl());

		return convertView;
	}

	public void clear() {
		linklist.clear();
	}

	class ViewHolder {
		ImageView imageView;
		TextView name;
	}

}
