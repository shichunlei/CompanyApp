package com.cells.companyapp.adapter;

import java.util.ArrayList;
import java.util.Collection;

import net.tsz.afinal.FinalBitmap;

import com.cells.companyapp.R;
import com.cells.companyapp.been.YellowPage;
import com.cells.companyapp.utils.HttpUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchYPAdapter extends BaseAdapter {

	private ArrayList<YellowPage> list;
	private LayoutInflater inflater;
	private Context context;

	public SearchYPAdapter(Context context) {
		this.context = context;
		this.list = new ArrayList<YellowPage>();
		this.inflater = LayoutInflater.from(context);
	}

	public void replaceWith(Collection<YellowPage> newArticle) {
		this.list.clear();
		this.list.addAll(newArticle);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public YellowPage getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflateIfRequired(convertView, position, parent);
		bind(getItem(position), convertView);
		return convertView;
	}

	private void bind(YellowPage item, View convertView) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.name.setVisibility(View.VISIBLE);
		holder.name.setText(item.getName());

		FinalBitmap fb = FinalBitmap.create(context);
		fb.configLoadfailImage(R.drawable.icon_loading);
		fb.configLoadingImage(R.drawable.icon_loading);
		fb.display(holder.image, HttpUtils.ROOT_URL + item.getUrl());
	}

	private View inflateIfRequired(View view, int position, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.item_search, null);
			view.setTag(new ViewHolder(view));
		}
		return view;
	}

	class ViewHolder {

		private TextView name;
		private ImageView image;

		public ViewHolder(View view) {
			name = (TextView) view.findViewWithTag("name");
			image = (ImageView) view.findViewWithTag("image");
		}
	}

}
