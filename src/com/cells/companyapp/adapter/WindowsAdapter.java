package com.cells.companyapp.adapter;

import java.util.LinkedList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.been.Windows;
import com.cells.companyapp.customview.refresh.XListView;

public class WindowsAdapter extends BaseAdapter {

	private LinkedList<Windows> relic;
	private Context context = null;

	private ViewHolder holder;

	public WindowsAdapter(Context context, XListView xListView) {
		relic = new LinkedList<Windows>();
		this.context = context;
	}

	@Override
	public int getCount() {
		return relic.size();
	}

	public LinkedList<Windows> getmInfos() {
		return relic;
	}

	public void setmInfos(LinkedList<Windows> relic) {
		this.relic = relic;
	}

	@Override
	public Object getItem(int position) {
		return relic.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addItemLast(List<Windows> datas) {
		for (int i = 0; i < datas.size(); i++) {
			relic.addLast(datas.get(i));
		}
	}

	public void addItemTop(List<Windows> datas) {
		for (int i = datas.size() - 1; i >= 0; i--) {
			relic.addFirst(datas.get(i));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Windows duitangInfo = relic.get(position);

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(parent
					.getContext());
			convertView = layoutInflator.inflate(R.layout.item_windows, null);
			holder = new ViewHolder();

			holder.imageView = (ImageView) convertView
					.findViewById(R.id.img_culture);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.content = (TextView) convertView
					.findViewById(R.id.tv_content);

			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		holder.name.setText(duitangInfo.getName());
		holder.content.setText(duitangInfo.getContent());

		FinalBitmap fb = FinalBitmap.create(context);
		fb.configLoadfailImage(R.drawable.icon_loading);
		fb.configLoadingImage(R.drawable.icon_loading);
		fb.display(holder.imageView, duitangInfo.getLogo());

		return convertView;
	}

	public void clear() {
		relic.clear();
	}

	class ViewHolder {
		ImageView imageView;
		TextView name;
		TextView content;
	}

}
