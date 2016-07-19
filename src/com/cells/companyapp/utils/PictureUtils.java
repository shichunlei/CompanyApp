package com.cells.companyapp.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class PictureUtils {

	/**
	 * Get a BitmapDrawable from a local file that is scaled down to fit the
	 * destination size
	 * 
	 * @param a
	 * @param path
	 * @return
	 */
	public static Bitmap getScaledBitmapFromPath(Activity a, String path) {

		float desWidth = 200;
		float desHeight = 200;

		// Read in the dimensions of the image on disk
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;

		int inSamplesSize = 1;
		if (srcHeight > desHeight || srcWidth > desWidth) {
			inSamplesSize = Math.round(srcHeight / desHeight);
		} else {
			inSamplesSize = Math.round(srcWidth / desWidth);
		}

		options = new BitmapFactory.Options();
		options.inSampleSize = inSamplesSize;

		Bitmap bitmap = BitmapFactory.decodeFile(path, options);

		return bitmap;
	}

	/**
	 * 从资源中通过缩放得到一张合适的图片
	 * 
	 * @param a
	 * @param id
	 * @return
	 */
	public static Bitmap getScaledBitmapFromResource(Activity a, int id) {

		float desWidth = 200;
		float desHeight = 200;

		// Read in the dimensions of the image on disk
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(a.getResources(), id, options);

		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;

		int inSamplesSize = 1;
		if (srcHeight > desHeight || srcWidth > desWidth) {
			inSamplesSize = Math.round(srcHeight / desHeight);
		} else {
			inSamplesSize = Math.round(srcWidth / desWidth);
		}

		options = new BitmapFactory.Options();
		options.inSampleSize = inSamplesSize;

		Bitmap bitmap = BitmapFactory.decodeResource(a.getResources(), id,
				options);

		return bitmap;
	}

	/**
	 * 获得圆形图片
	 * 
	 * @param srcBmp
	 * @return
	 */
	public static Bitmap getRoundBitmap(Bitmap srcBmp) {

		final Paint paint = new Paint();

		int width = srcBmp.getWidth();
		int height = srcBmp.getHeight();
		// 矩形的四边
		int left, top, right, bottom;
		// 选择一条较短的边
		if (width <= height) {
			left = 0;
			top = (height - width) / 2;
			right = width;
			bottom = top + width;
		} else {
			left = (width - height) / 2;
			top = 0;
			right = left + height;
			bottom = height;
		}
		// 创建矩形背景
		final Rect rect = new Rect(left, top, right, bottom);
		final RectF rectF = new RectF(rect);

		Bitmap desBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(desBmp);

		// 设置边缘光滑, 去掉锯齿
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		int radius = (int) ((width > height ? width : height) / 2);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		// 设置当两个图形相交时的模式, SRC_IN为取SRC图形相交的部分, 多余的将被去掉
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(srcBmp, rect, rect, paint);

		return desBmp;
	}

	/**
	 * 绘制带白边的圆形头像
	 * 
	 * @param activity
	 * @param filename
	 * @return
	 */
	public Bitmap drawHandBitmap(Activity activity, String path) {

		Bitmap desBmp = BitmapFactory.decodeFile(path);
		desBmp = PictureUtils.getRoundBitmap(desBmp);

		Paint paint = new Paint();
		paint.setColor(0xffffffff);
		// 设置画板宽度
		paint.setStrokeWidth(10);
		// 设置边缘光滑, 去掉锯齿
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);

		// 绘制圆环
		int width = desBmp.getWidth();
		int height = desBmp.getHeight();
		float cx = width / 2;
		float cy = height / 2;
		float radius = width > height ? width / 2 - 5 : height / 2 - 5;
		Canvas canvas = new Canvas(desBmp);
		canvas.drawCircle(cx, cy, radius, paint);

		return desBmp;
	}

	/**
	 * 将本地图片路径转换为Bitmap形式展示到ImageView内
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getLocalImage(String path) {
		Bitmap bitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 1;
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(path, options);// 获取尺寸信息

		// 获取比例大小
		int wRatio = (int) Math.ceil(options.outWidth / 500);
		int hRatio = (int) Math.ceil(options.outHeight / 500);
		// 如果超出指定大小，则缩小相应的比例
		if (wRatio > 1 && hRatio > 1) {
			if (wRatio > hRatio) {
				options.inSampleSize = wRatio;
			} else {
				options.inSampleSize = hRatio;
			}
		}
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(path, options);// 获取尺寸信息
		return bitmap;

	}
}
