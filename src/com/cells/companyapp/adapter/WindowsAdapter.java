package com.cells.companyapp.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import scl.leo.library.image.ad.*;

import com.cells.companyapp.R;
import com.cells.companyapp.been.*;
import com.cells.companyapp.customview.MyGridView;
import com.cells.companyapp.view.VideoInfoActivity;
import com.cells.companyapp.view.NewsInfoActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WindowsAdapter extends BaseAdapter implements OnItemClickListener {

	private Context context = null;
	private LinkedList<Windows> windows;

	private List<Slides> slidesList;
	private List<Illustrate> illustrateList;

	private ViewHolder holder;

	SlidesAdapter slides_adapter;
	IllustrateAdapter illustrate_adapter;

	public WindowsAdapter(Context context) {
		this.context = context;
		windows = new LinkedList<Windows>();
	}

	@Override
	public int getCount() {
		return windows.size();
	}

	public LinkedList<Windows> getmInfos() {
		return windows;
	}

	public void setmInfos(LinkedList<Windows> windows) {
		this.windows = windows;
	}

	@Override
	public Object getItem(int position) {
		return windows.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addItemLast(List<Windows> datas) {
		for (int i = 0; i < datas.size(); i++) {
			windows.addLast(datas.get(i));
		}
	}

	public void addItemTop(List<Windows> datas) {
		for (int i = datas.size() - 1; i >= 0; i--) {
			windows.addFirst(datas.get(i));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Windows windowsInfo = windows.get(position);

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(parent
					.getContext());
			convertView = layoutInflator.inflate(R.layout.item_windows, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (null != windowsInfo.getNews()) {// 文章
			holder.news.setVisibility(View.VISIBLE);
			holder.slides.setVisibility(View.GONE);
			holder.gridview.setVisibility(View.GONE);

			holder.news_name.setText(windowsInfo.getNews().getTitle());
			holder.news_content.setText(windowsInfo.getNews().getSub_title());
			holder.news_content_count.setText(windowsInfo.getNews()
					.getComment_count() + "");

			FinalBitmap fb = FinalBitmap.create(context);
			fb.configLoadfailImage(R.drawable.icon_loading);
			fb.configLoadingImage(R.drawable.icon_loading);
			fb.display(holder.img_news, windowsInfo.getNews().getImage());

			holder.news.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("name", windowsInfo.getNews().getTitle());
					intent.putExtra("id", windowsInfo.getNews().getId());
					intent.setClass(context, NewsInfoActivity.class);
					context.startActivity(intent);
				}
			});
		} else if (null != windowsInfo.getSpecial_column()) {// 专栏
			holder.news.setVisibility(View.GONE);
			holder.slides.setVisibility(View.GONE);
			holder.gridview.setVisibility(View.GONE);
		} else if (null != windowsInfo.getIllustrate_id()) {// 书籍画刊
			holder.news.setVisibility(View.GONE);
			holder.slides.setVisibility(View.GONE);
			holder.gridview.setVisibility(View.VISIBLE);
			illustrateList = new ArrayList<Illustrate>();
			if (null != illustrateList) {
				illustrateList.clear();
			}
			for (int i = 0; i < windowsInfo.getIllustrate_id().size(); i++) {
				illustrateList.add(windowsInfo.getIllustrate_id().get(i));
			}
			holder.gridview.setAdapter(new IllustrateAdapter(context,
					illustrateList));
			holder.gridview.setOnItemClickListener(this);
		} else if (null != windowsInfo.getSlides()) {// 幻灯片
			holder.news.setVisibility(View.GONE);
			holder.slides.setVisibility(View.VISIBLE);
			holder.gridview.setVisibility(View.GONE);
			slidesList = new ArrayList<Slides>();
			if (null != slidesList) {
				slidesList.clear();
			}
			for (int i = 0; i < windowsInfo.getSlides().size(); i++) {
				slidesList.add(windowsInfo.getSlides().get(i));
			}
			holder.viewFlow.setAdapter(new SlidesAdapter(context, slidesList));
			holder.viewFlow.setmSideBuffer(3);
			holder.viewFlow.setFlowIndicator(holder.viewflowindic);
			holder.viewFlow.setTimeSpan(3000);
			// 设置初始位置
			holder.viewFlow.setSelection(3 * 1000);
			// 启动自动播放
			holder.viewFlow.startAutoFlowTimer();
		}

		return convertView;
	}

	public void clear() {
		windows.clear();
	}

	private static class ViewHolder {
		LinearLayout news;
		ImageView img_news;
		TextView news_name;
		TextView news_content;
		TextView news_content_count;

		FrameLayout slides;
		ViewFlow viewFlow;
		CircleFlowIndicator viewflowindic;

		MyGridView gridview;

		public ViewHolder(View view) {
			news = (LinearLayout) view.findViewById(R.id.ll_news);
			img_news = (ImageView) view.findViewById(R.id.img_news);
			news_name = (TextView) view.findViewById(R.id.tv_news_name);
			news_content = (TextView) view.findViewById(R.id.tv_news_content);
			news_content_count = (TextView) view
					.findViewById(R.id.tv_news_content_count);

			slides = (FrameLayout) view.findViewById(R.id.fl_slides);
			viewFlow = (ViewFlow) view.findViewById(R.id.viewflow);
			viewflowindic = (CircleFlowIndicator) view
					.findViewById(R.id.viewflowindic);

			gridview = (MyGridView) view.findViewById(R.id.gridview);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.putExtra("name", illustrateList.get(position).getName());
		intent.putExtra("id", illustrateList.get(position).getId());
		intent.setClass(context, VideoInfoActivity.class);
		context.startActivity(intent);
	}

}
