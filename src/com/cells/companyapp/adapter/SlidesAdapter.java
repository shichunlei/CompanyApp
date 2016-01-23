package com.cells.companyapp.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.cells.companyapp.R;
import com.cells.companyapp.been.Slides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SlidesAdapter extends BaseAdapter {

	private List<Slides> imageURLs;
	private Context context;
	ViewHolder viewHodler = null;

	private int index;

	public SlidesAdapter(Context context, List<Slides> imageURLs) {
		this.imageURLs = imageURLs;
		this.context = context;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE; // 返回最大值来实现循环
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_slides, null);
			viewHodler = new ViewHolder(convertView);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHolder) convertView.getTag();
		}

		index = position % imageURLs.size();

		FinalBitmap fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.icon_book_loading);
		fb.configLoadfailImage(R.drawable.icon_book_loading);
		fb.display(viewHodler.image, imageURLs.get(index).getImage());

		return convertView;
	}

	private static class ViewHolder {
		private ImageView image;

		public ViewHolder(View view) {
			image = (ImageView) view.findViewById(R.id.img_slides);
		}
	}
}
