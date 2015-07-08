package com.cells.companyapp.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.cells.companyapp.R;
import com.cells.companyapp.been.Illustrate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IllustrateAdapter extends BaseAdapter {

	private List<Illustrate> illustrate;
	private Context context;

	public IllustrateAdapter(Context context, List<Illustrate> illustrate) {
		this.context = context;
		this.illustrate = illustrate;
	}

	@Override
	public int getCount() {
		return illustrate.size();
	}

	@Override
	public Object getItem(int position) {
		return illustrate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_illustrate, null);
			viewHodler = new ViewHodler();
			viewHodler.imgIllustrate = (ImageView) convertView
					.findViewById(R.id.image_illustrate);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}

		FinalBitmap fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.icon_book_loading);
		fb.configLoadfailImage(R.drawable.icon_book_loading);
		fb.display(viewHodler.imgIllustrate, illustrate.get(position)
				.getImage());
		return convertView;
	}

	private class ViewHodler {
		private ImageView imgIllustrate;
	}

}
