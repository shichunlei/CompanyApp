package com.cells.companyapp.adapter;

import java.util.ArrayList;
import java.util.Collection;

import scl.leo.library.image.CircleImageView;
import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.been.Comment;

public class CommentAdapter extends BaseAdapter {

	private ArrayList<Comment> list;
	private LayoutInflater inflater;
	private Context context;

	public CommentAdapter(Context context) {
		this.context = context;
		this.list = new ArrayList<Comment>();
		this.inflater = LayoutInflater.from(context);
	}

	public void replaceWith(Collection<Comment> newArticle) {
		this.list.clear();
		this.list.addAll(newArticle);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Comment getItem(int position) {
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

	private void bind(Comment item, View convertView) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (null != item.getUser_name()) {
			holder.name.setText(item.getUser_name());
			FinalBitmap fb = FinalBitmap.create(context);
			fb.configLoadfailImage(R.drawable.icon_loading);
			fb.configLoadingImage(R.drawable.icon_loading);
			fb.display(holder.image, item.getUser_avatar());
		} else {
			holder.name.setText(item.getManager_name());
			FinalBitmap fb = FinalBitmap.create(context);
			fb.configLoadfailImage(R.drawable.icon_loading);
			fb.configLoadingImage(R.drawable.icon_loading);
			fb.display(holder.image, item.getManager_avatar());
		}
		holder.body.setText(item.getBody());
		holder.created_at.setText(item.getCreated_at());
	}

	private View inflateIfRequired(View view, int position, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.item_comment, null);
			view.setTag(new ViewHolder(view));
		}
		return view;
	}

	class ViewHolder {

		private TextView name;
		private CircleImageView image;
		private TextView body;
		private TextView created_at;

		public ViewHolder(View view) {
			name = (TextView) view.findViewWithTag("name");
			body = (TextView) view.findViewWithTag("body");
			image = (CircleImageView) view.findViewWithTag("image");
			created_at = (TextView) view.findViewWithTag("created_at");
		}
	}

}
