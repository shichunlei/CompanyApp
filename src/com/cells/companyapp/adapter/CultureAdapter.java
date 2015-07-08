package com.cells.companyapp.adapter;

import java.util.LinkedList;
import java.util.List;

import com.cells.companyapp.R;
import com.cells.companyapp.been.Culture;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CultureAdapter extends BaseAdapter {

	private LinkedList<Culture> relic;
	private Context context = null;

	private ViewHolder holder;

	public CultureAdapter(Context context) {
		relic = new LinkedList<Culture>();
		this.context = context;
	}

	@Override
	public int getCount() {
		return relic.size();
	}

	public LinkedList<Culture> getmInfos() {
		return relic;
	}

	public void setmInfos(LinkedList<Culture> relic) {
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

	public void addItemLast(List<Culture> datas) {
		for (int i = 0; i < datas.size(); i++) {
			relic.addLast(datas.get(i));
		}
	}

	public void addItemTop(List<Culture> datas) {
		for (int i = datas.size() - 1; i >= 0; i--) {
			relic.addFirst(datas.get(i));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Culture duitangInfo = relic.get(position);

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(parent
					.getContext());
			convertView = layoutInflator.inflate(R.layout.item_culture, null);
			holder = new ViewHolder();

			holder.imageView = (ImageView) convertView
					.findViewById(R.id.img_culture);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.content = (TextView) convertView
					.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

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
