/**
 * Copyright 2013 Joan Zapata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cells.companyapp.base;

import java.util.List;

import com.bumptech.glide.Glide;
import com.cells.companyapp.R;
import com.cells.companyapp.widget.slide.FlyBanner;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Joan Zapata Allows an abstraction of the ViewHolder pattern.
 */
public class BaseAdapterHelper {

	/** Views indexed with their IDs */
	private final SparseArray<View> mViews;

	private View mConvertView;
	private int mPosition;
	private int mLayoutId;
	/**
	 * Package private field to retain the associated user object and detect a
	 * change
	 */
	Object mAssociatedObject;

	protected BaseAdapterHelper(Context context, ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mLayoutId = layoutId;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	/**
	 * This method is the only entry point to get a BaseAdapterHelper.
	 * 
	 * @param context
	 *            The current context.
	 * @param convertView
	 *            The convertView arg passed to the getView() method.
	 * @param parent
	 *            The parent arg passed to the getView() method.
	 * @return A BaseAdapterHelper instance.
	 */
	public static BaseAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId) {
		return get(context, convertView, parent, layoutId, -1);
	}

	public static BaseAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId,
			int position) {
		if (convertView == null) {
			return new BaseAdapterHelper(context, parent, layoutId, position);
		}

		// Retrieve the existing helper and update its position
		BaseAdapterHelper existingHelper = (BaseAdapterHelper) convertView.getTag();

		if (existingHelper.mLayoutId != layoutId) {
			return new BaseAdapterHelper(context, parent, layoutId, position);
		}

		existingHelper.mPosition = position;
		return existingHelper;
	}

	/**
	 * This method allows you to retrieve a view and perform custom operations
	 * on it, not covered by the BaseAdapterHelper. If you think it's a common
	 * use case, please consider creating a new issue at
	 * https://github.com/JoanZapata/base-adapter-helper/issues.
	 * 
	 * @param viewId
	 *            The id of the view you want to retrieve.
	 */
	public <T extends View> T getView(int viewId) {
		return retrieveView(viewId);
	}

	/**
	 * Will set the text of a TextView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param value
	 *            The text to put in the text view.
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setText(int viewId, String value) {
		TextView view = retrieveView(viewId);
		view.setText(value);
		return this;
	}

	/**
	 * Will set background of a view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param backgroundRes
	 *            A resource to use as a background.
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setBackgroundRes(int viewId, int backgroundRes) {
		View view = retrieveView(viewId);
		view.setBackgroundResource(backgroundRes);
		return this;
	}

	/**
	 * 
	 * @param context
	 *            上下文
	 * @param viewId
	 *            The view id.
	 * @param backgroundUrl
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setBackgroundUrl(Context context, int viewId, String backgroundUrl) {
		View view = retrieveView(viewId);
		FinalBitmap.create(context).configLoadingImage(R.drawable.icon_loading)
				.configLoadfailImage(R.drawable.icon_loading).display(view, backgroundUrl);
		return this;
	}

	/**
	 * 
	 * @param viewId
	 *            The view id.
	 * @param value
	 *            The ProgressBar value
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setProgress(int viewId, int value) {
		ProgressBar view = retrieveView(viewId);
		view.setProgress(value);
		return this;
	}

	/**
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imageRes
	 *            The image drawable.
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setImageRes(int viewId, int imageRes) {
		ImageView view = retrieveView(viewId);
		view.setImageResource(imageRes);
		return this;
	}

	/**
	 * Will set the image of an ImageView from a drawable.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param drawable
	 *            The image drawable.
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setImageDrawable(int viewId, Drawable drawable) {
		ImageView view = retrieveView(viewId);
		view.setImageDrawable(drawable);
		return this;
	}

	/**
	 * Will download an image from a URL and put it in an ImageView.<br/>
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imageUrl
	 *            The image URL.
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setImageUrl(Context context, int viewId, String imageUrl) {
		ImageView imageview = retrieveView(viewId);
		Glide.with(context).load(imageUrl).centerCrop().crossFade().error(R.drawable.icon_loading)
				.placeholder(R.drawable.icon_loading).into(imageview);
		return this;
	}

	/**
	 * Will download images from imagesUrl and put it in an FlyBanner.<br/>
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imagesUrl
	 *            The images URL.
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setImagesUrl(int viewId, List<String> imagesUrl) {
		FlyBanner flyBanner = retrieveView(viewId);
		flyBanner.setImagesUrl(imagesUrl);
		return this;
	}

	/**
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imageUrl
	 *            The image URL.
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setHeadPicUrl(Context context, int viewId, String imageUrl) {
		ImageView view = retrieveView(viewId);
		Glide.with(context).load(imageUrl).centerCrop().crossFade().error(R.drawable.icon_personal)
				.into(view);
		return this;
	}

	/**
	 * Add an action to set the image of an image view. Can be called multiple
	 * times.
	 */
	public BaseAdapterHelper setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = retrieveView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}

	/**
	 * Add an action to set the alpha of a view. Can be called multiple times.
	 * Alpha between 0-1.
	 */
	public BaseAdapterHelper setAlpha(int viewId, float value) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			retrieveView(viewId).setAlpha(value);
		} else {
			// Pre-honeycomb hack to set Alpha value
			AlphaAnimation alpha = new AlphaAnimation(value, value);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			retrieveView(viewId).startAnimation(alpha);
		}
		return this;
	}

	/**
	 * Set a view visibility to VISIBLE (true) or GONE (false).
	 * 
	 * @param viewId
	 *            The view id.
	 * @param visible
	 *            True for VISIBLE, false for GONE.
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setVisible(int viewId, boolean visible) {
		View view = retrieveView(viewId);
		view.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	/**
	 * Set a view visibility
	 * 
	 * @param viewId
	 *            The view id.
	 * @param visible
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setVisible(int viewId, int visible) {
		View view = retrieveView(viewId);
		view.setVisibility(visible);
		return this;
	}

	/**
	 * Add links into a TextView.
	 * 
	 * @param viewId
	 *            The id of the TextView to linkify.
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper linkify(int viewId) {
		TextView view = retrieveView(viewId);
		Linkify.addLinks(view, Linkify.ALL);
		return this;
	}

	/** Apply the typeface to the given viewId, and enable subpixel rendering. */
	public BaseAdapterHelper setTypeface(int viewId, Typeface typeface) {
		TextView view = retrieveView(viewId);
		view.setTypeface(typeface);
		view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		return this;
	}

	/**
	 * Apply the typeface to all the given viewIds, and enable subpixel
	 * rendering.
	 */
	public BaseAdapterHelper setTypeface(Typeface typeface, int... viewIds) {
		for (int viewId : viewIds) {
			TextView view = retrieveView(viewId);
			view.setTypeface(typeface);
			view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
		return this;
	}

	/**
	 * 
	 * @param viewId
	 *            The view id.
	 * @param enabled
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setEnabled(int viewId, boolean enabled) {
		View view = retrieveView(viewId);
		view.setEnabled(enabled);
		return this;
	}

	/**
	 * 
	 * @param viewId
	 *            The view id.
	 * @param enabled
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setSelected(int viewId, boolean enabled) {
		View view = retrieveView(viewId);
		view.setSelected(enabled);
		return this;
	}

	/**
	 * Sets the checked status of a checkable.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param checked
	 *            The checked status;
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setChecked(int viewId, boolean checked) {
		Checkable view = (Checkable) retrieveView(viewId);
		view.setChecked(checked);
		return this;
	}

	/**
	 * Sets the adapter of a adapter view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param adapter
	 *            The adapter;
	 * @return The BaseAdapterHelper for chaining.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BaseAdapterHelper setAdapter(int viewId, Adapter adapter) {
		AdapterView view = retrieveView(viewId);
		view.setAdapter(adapter);
		return this;
	}

	/**
	 * Sets the on click listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on click listener;
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setOnClickListener(int viewId, View.OnClickListener listener) {
		View view = retrieveView(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	/**
	 * Sets the on touch listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on touch listener;
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setOnTouchListener(int viewId, View.OnTouchListener listener) {
		View view = retrieveView(viewId);
		view.setOnTouchListener(listener);
		return this;
	}

	/**
	 * Sets the on long click listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on long click listener;
	 * @return The BaseAdapterHelper for chaining.
	 */
	public BaseAdapterHelper setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
		View view = retrieveView(viewId);
		view.setOnLongClickListener(listener);
		return this;
	}

	/**
	 * Sets the listview or gridview's item click listener of the view
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The item on click listener;
	 * @return The BaseAdapterHelper for chaining.
	 */
	@SuppressWarnings({ "rawtypes" })
	public BaseAdapterHelper setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
		AdapterView view = retrieveView(viewId);
		view.setOnItemClickListener(listener);
		return this;
	}

	/**
	 * Sets the listview or gridview's item long click listener of the view
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The item long click listener;
	 * @return The BaseAdapterHelper for chaining.
	 */
	@SuppressWarnings({ "rawtypes" })
	public BaseAdapterHelper setOnItemLongClickListener(int viewId,
			AdapterView.OnItemLongClickListener listener) {
		AdapterView view = retrieveView(viewId);
		view.setOnItemLongClickListener(listener);
		return this;
	}

	/**
	 * Sets the listview or gridview's item selected click listener of the view
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The item selected click listener;
	 * @return The BaseAdapterHelper for chaining.
	 */
	@SuppressWarnings({ "rawtypes" })
	public BaseAdapterHelper setOnItemSelectedClickListener(int viewId,
			AdapterView.OnItemSelectedListener listener) {
		AdapterView view = retrieveView(viewId);
		view.setOnItemSelectedListener(listener);
		return this;
	}

	/** Retrieve the mConvertView */
	public View getView() {
		return mConvertView;
	}

	/**
	 * Retrieve the overall mPosition of the mData in the list.
	 * 
	 * @throws IllegalArgumentException
	 *             If the mPosition hasn't been set at the construction of the
	 *             this helper.
	 */
	public int getPosition() {
		if (mPosition == -1)
			throw new IllegalStateException("Use BaseAdapterHelper constructor "
					+ "with mPosition if you need to retrieve the mPosition.");
		return mPosition;
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T retrieveView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/** Retrieves the last converted object on this view. */
	public Object getAssociatedObject() {
		return mAssociatedObject;
	}

	/** Should be called during convert */
	public void setAssociatedObject(Object associatedObject) {
		this.mAssociatedObject = associatedObject;
	}
}
