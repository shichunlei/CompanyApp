package com.cells.companyapp.adapter;

import java.util.ArrayList;
import java.util.Collection;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseActivity;
import com.cells.companyapp.been.Gallery;
import com.cells.companyapp.utils.HttpUtils;
import com.cells.companyapp.view.GalleryPictureActivity;

public class GalleryListAdapter extends BaseAdapter {

	private ArrayList<Gallery> list;
	private LayoutInflater inflater;
	private Context context;
	ViewHolder holder;
	int gallery_id;
	String type;

	public GalleryListAdapter(Context context) {
		this.context = context;
		this.list = new ArrayList<Gallery>();
		this.inflater = LayoutInflater.from(context);
	}

	public void replaceWith(Collection<Gallery> newArticle) {
		this.list.clear();
		this.list.addAll(newArticle);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Gallery getItem(int position) {
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

	private void bind(final Gallery item, View convertView) {
		holder = (ViewHolder) convertView.getTag();
		holder.year.setText(item.getYear());

		FinalBitmap fb = FinalBitmap.create(context);
		fb.configLoadfailImage(R.drawable.icon_loading);
		fb.configLoadingImage(R.drawable.icon_loading);
		if (null != item.getActivity() && null == item.getLeader()) {
			holder.activity.setText("企业活动");
			holder.rlLeader.setVisibility(View.INVISIBLE);
			fb.display(holder.imgActivity, HttpUtils.ROOT_URL
					+ item.getActivity().getImage());
		} else if (null == item.getActivity() && null != item.getLeader()) {
			holder.activity.setText("企业领导");
			holder.rlLeader.setVisibility(View.INVISIBLE);
			fb.display(holder.imgActivity, HttpUtils.ROOT_URL
					+ item.getLeader().getImage());
		} else if (null != item.getActivity() && null != item.getLeader()) {
			holder.activity.setText("企业活动");
			holder.leader.setText("企业领导");
			fb.display(holder.imgLeader, HttpUtils.ROOT_URL
					+ item.getLeader().getImage());
			fb.display(holder.imgActivity, HttpUtils.ROOT_URL
					+ item.getActivity().getImage());
		}

		holder.rlLeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gallery_id = item.getLeader().getGallery_id();
				type = "leader";
				goToGalleryPicture();
			}
		});

		holder.rlActivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != item.getActivity() && null == item.getLeader()) {
					gallery_id = item.getActivity().getGallery_id();
					type = "activity";
				} else if (null == item.getActivity()
						&& null != item.getLeader()) {
					gallery_id = item.getLeader().getGallery_id();
					type = "leader";
				}

				goToGalleryPicture();
			}
		});

	}

	private void goToGalleryPicture() {
		Bundle bundle = new Bundle();
		bundle.putInt("gallery_id", gallery_id);
		bundle.putString("type", type);
		((BaseActivity) context).openActivity(GalleryPictureActivity.class,
				bundle, false);
	}

	private View inflateIfRequired(View view, int position, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.item_gallery_list, null);
			view.setTag(new ViewHolder(view));
		}
		return view;
	}

	class ViewHolder {

		private TextView year;
		private ImageView imgLeader;
		private TextView leader;
		private ImageView imgActivity;
		private TextView activity;
		private RelativeLayout rlLeader;
		private RelativeLayout rlActivity;

		public ViewHolder(View view) {
			year = (TextView) view.findViewWithTag("year");
			imgLeader = (ImageView) view.findViewWithTag("img_leader");
			leader = (TextView) view.findViewWithTag("tv_leader");
			imgActivity = (ImageView) view.findViewWithTag("img_activity");
			activity = (TextView) view.findViewWithTag("tv_activity");
			rlLeader = (RelativeLayout) view.findViewWithTag("rl_leader");
			rlActivity = (RelativeLayout) view.findViewWithTag("rl_activity");
		}
	}

}
