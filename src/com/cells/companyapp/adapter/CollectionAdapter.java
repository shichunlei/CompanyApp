package com.cells.companyapp.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.cells.companyapp.R;
import com.cells.companyapp.been.Collection;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CollectionAdapter extends BaseSwipListAdapter {

	private List<Collection> list;
	private Context context;

	public CollectionAdapter(Context context, List<Collection> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Collection getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void removeItem(int position) {
		list.remove(position);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_list_collection, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		Collection item = getItem(position);

		holder.rlComment.setVisibility(View.VISIBLE);
		holder.comment_count.setText(item.getComment_count() + "");
		holder.content.setText(item.getContent());
		holder.name.setText(item.getName());
		FinalBitmap fb = FinalBitmap.create(context);
		fb.configLoadfailImage(R.drawable.icon_loading);
		fb.configLoadingImage(R.drawable.icon_loading);
		fb.display(holder.imageView, item.getImage());

		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView name;
		TextView comment_count;
		TextView content;
		ImageView imgComment;
		RelativeLayout rlComment;

		public ViewHolder(View view) {
			imageView = (ImageView) view.findViewById(R.id.iv_icon);
			imgComment = (ImageView) view.findViewById(R.id.img_comment);
			name = (TextView) view.findViewById(R.id.tv_name);
			comment_count = (TextView) view.findViewById(R.id.tv_comment_num);
			content = (TextView) view.findViewById(R.id.tv_content);
			rlComment = (RelativeLayout) view.findViewById(R.id.rl_comment);
			view.setTag(this);
		}
	}
}
