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
import com.cells.companyapp.been.Gallery;

public class GalleryAdapter extends BaseAdapter {

	private LinkedList<Gallery> linklist;
	private Context context = null;

	private ViewHolder holder;

	public GalleryAdapter(Context context) {
		linklist = new LinkedList<Gallery>();
		this.context = context;
	}

	@Override
	public int getCount() {
		return linklist.size();
	}

	public LinkedList<Gallery> getmInfos() {
		return linklist;
	}

	public void setmInfos(LinkedList<Gallery> list) {
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

	public void addItemLast(List<Gallery> datas) {
		for (int i = 0; i < datas.size(); i++) {
			linklist.addLast(datas.get(i));
		}
	}

	public void addItemTop(List<Gallery> datas) {
		for (int i = datas.size() - 1; i >= 0; i--) {
			linklist.addFirst(datas.get(i));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Gallery duitangInfo = linklist.get(position);
		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
			convertView = layoutInflator.inflate(R.layout.item_gallery, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.news_pic);
			holder.name = (TextView) convertView.findViewById(R.id.news_title);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.name.setText(duitangInfo.getName());
		FinalBitmap fb = FinalBitmap.create(context);
		fb.configLoadfailImage(R.drawable.loading_icon);
		fb.configLoadingImage(R.drawable.loading_icon);
		fb.display(holder.imageView, duitangInfo.getPicture().getImage());

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
