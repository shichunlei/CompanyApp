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
import com.cells.companyapp.been.Video;
import com.cells.companyapp.customview.refresh.XListView;

public class VideoAdapter extends BaseAdapter {

	private LinkedList<Video> relic;
	private Context context = null;

	private ViewHolder holder;

	public VideoAdapter(Context context, XListView xListView) {
		relic = new LinkedList<Video>();
		this.context = context;
	}

	@Override
	public int getCount() {
		return relic.size();
	}

	public LinkedList<Video> getmInfos() {
		return relic;
	}

	public void setmInfos(LinkedList<Video> relic) {
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

	public void addItemLast(List<Video> datas) {
		for (int i = 0; i < datas.size(); i++) {
			relic.addLast(datas.get(i));
		}
	}

	public void addItemTop(List<Video> datas) {
		for (int i = datas.size() - 1; i >= 0; i--) {
			relic.addFirst(datas.get(i));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Video duitangInfo = relic.get(position);

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(parent
					.getContext());
			convertView = layoutInflator.inflate(R.layout.item_video, null);
			holder = new ViewHolder();

			holder.imageView = (ImageView) convertView
					.findViewById(R.id.img_video);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);

			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		holder.name.setText(duitangInfo.getName());

		FinalBitmap fb = FinalBitmap.create(context);
		fb.configLoadfailImage(R.drawable.icon_book_loading);
		fb.configLoadingImage(R.drawable.icon_book_loading);
		fb.display(holder.imageView, duitangInfo.getImage());

		return convertView;
	}

	public void clear() {
		relic.clear();
	}

	class ViewHolder {
		ImageView imageView;
		TextView name;
	}
}
