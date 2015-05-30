package com.cells.companyapp.fragment;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseFragment;
import com.shizhefei.view.indicator.FragmentListPageAdapter;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

public class WindowsFragment extends BaseFragment {

	private View view;

	@ViewInject(id = R.id.id_viewpager)
	private ViewPager viewPager;

	@ViewInject(id = R.id.moretab_indicator)
	private ScrollIndicatorView indicator;

	private IndicatorViewPager indicatorViewPager;

	private String[] types = { "企业头条", "企业活动", "企业微视", "企业书籍", "文化之旅" };

	private LayoutInflater inflate;

	private int size;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_windows, container, false);
		FinalActivity.initInjectedView(this, view);
		init();
		return view;
	}

	private void init() {
		indicator.setScrollBar(new ColorBar(getActivity(), Color.RED, 5));
		// 设置滚动监听
		int selectColorId = R.color.selected;
		int unSelectColorId = R.color.nor;
		indicator.setOnTransitionListener(new OnTransitionTextListener()
				.setColorId(getActivity(), selectColorId, unSelectColorId));

		viewPager.setOffscreenPageLimit(5);
		indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
		inflate = LayoutInflater.from(getActivity().getApplicationContext());
		indicatorViewPager.setAdapter(new MyAdapter(getActivity()
				.getSupportFragmentManager()));

		size = 6;

		indicatorViewPager.getAdapter().notifyDataSetChanged();
	}

	private class MyAdapter extends IndicatorFragmentPagerAdapter {

		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return size;
		}

		@Override
		public View getViewForTab(int position, View convertView,
				ViewGroup container) {
			if (convertView == null) {
				convertView = inflate.inflate(R.layout.tab_top, container,
						false);
			}
			TextView textView = (TextView) convertView;
			textView.setText(types[position % types.length]);
			textView.setPadding(20, 0, 20, 0);
			return convertView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			WindowsItemFragment fragment = new WindowsItemFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("type", position + 1);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			return FragmentListPageAdapter.POSITION_NONE;
		}

	};
}
