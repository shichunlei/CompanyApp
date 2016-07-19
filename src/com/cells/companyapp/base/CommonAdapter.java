package com.cells.companyapp.base;

import java.util.ArrayList;
import java.util.List;

import com.cells.companyapp.implement.IAdapter;
import com.cells.companyapp.implement.IData;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import static com.cells.companyapp.base.BaseAdapterHelper.get;

public abstract class CommonAdapter<T>  extends BaseAdapter implements IData<T>, IAdapter<T> {

	protected final Context mContext;
	protected final int mLayoutResId;
	protected final List<T> mData;

	public CommonAdapter(Context context, int layoutResId) {
		this(context, layoutResId, null);
	}

	public CommonAdapter(Context context, int layoutResId, List<T> data) {
		this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
		this.mContext = context;
		this.mLayoutResId = layoutResId;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return position >= mData.size() ? null : mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getLayoutResId(T item) {
		return this.mLayoutResId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final T item = getItem(position);
		final BaseAdapterHelper helper = get(mContext, convertView, parent, getLayoutResId(item), position);
		onUpdate(helper, item, position);
		helper.setAssociatedObject(item);
		return helper.getView();
	}

	@Override
	public boolean isEnabled(int position) {
		return position < mData.size();
	}

	@Override
	public void add(T elem) {
		mData.add(elem);
		notifyDataSetChanged();
	}

	@Override
	public void addAll(List<T> elem) {
		mData.addAll(elem);
		notifyDataSetChanged();
	}

	@Override
	public void set(T oldElem, T newElem) {
		set(mData.indexOf(oldElem), newElem);
	}

	@Override
	public void set(int index, T elem) {
		mData.set(index, elem);
		notifyDataSetChanged();
	}

	@Override
	public void remove(T elem) {
		mData.remove(elem);
		notifyDataSetChanged();
	}

	@Override
	public void remove(int index) {
		mData.remove(index);
		notifyDataSetChanged();
	}

	@Override
	public void replaceAll(List<T> elem) {
		mData.clear();
		mData.addAll(elem);
		notifyDataSetChanged();
	}

	@Override
	public boolean contains(T elem) {
		return mData.contains(elem);
	}

	@Override
	public void clear() {
		mData.clear();
		notifyDataSetChanged();
	}
}
