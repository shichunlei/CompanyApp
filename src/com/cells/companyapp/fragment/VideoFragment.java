package com.cells.companyapp.fragment;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cells.companyapp.R;
import com.cells.companyapp.base.BaseFragment;
import com.cells.companyapp.utils.DisplayUtil;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

public class VideoFragment extends BaseFragment {

	private View view;

	@ViewInject(id = R.id.id_viewpager)
	private ViewPager viewPager;

	@ViewInject(id = R.id.moretab_indicator)
	private ScrollIndicatorView indicator;

	private IndicatorViewPager indicatorViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_video, container, false);
		FinalActivity.initInjectedView(this, view);
		init();
		return view;
	}

	private void init() {
		indicator.setScrollBar(new ColorBar(getActivity(), Color.RED, 5));
		// 设置滚动监听
		indicator.setOnTransitionListener(new OnTransitionTextListener().setColorId(getActivity(),
				R.color.selected, R.color.nor));

		viewPager.setOffscreenPageLimit(2);
		indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
		indicatorViewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager()));

		indicatorViewPager.getAdapter().notifyDataSetChanged();
	}

	private class MyAdapter extends IndicatorFragmentPagerAdapter {

		private String[] types = { "最新", "企业精选" };

		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return types.length;
		}

		@Override
		public View getViewForTab(int position, View convertView, ViewGroup container) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.tab_top, container, false);
			}
			TextView textView = (TextView) convertView;
			textView.setText(types[position % types.length]);
			int padding = DisplayUtil.dipToPix(getActivity(), 10);
			textView.setPadding(padding, 0, padding, 0);
			return convertView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			VideoItemFragment fragment = new VideoItemFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("type", position);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			// 这是ViewPager适配器的特点,有两个值
			// POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
			// 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
			return PagerAdapter.POSITION_NONE;
		}
	};
}
