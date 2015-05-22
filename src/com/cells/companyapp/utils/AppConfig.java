package com.cells.companyapp.utils;

import scl.leo.library.utils.other.TimeUtils;
import android.os.Environment;

public class AppConfig {

	/** SDcard根目录 */
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/** 根文件夹 */
	public static final String SCLUI_FOLDER_NAME = "/SCLUI";
	/** 根文件夹路径 */
	public static final String SCLUI_FOLDER_PATH = SDCARD_PATH + "/SCLUI/";

	/** 拍照存储图片文件夹 */
	public static final String IMAGE_FOLDER_NAME = "/image/";
	/** 拍照存储图片文件夹路径 */
	public static final String IMAGE_FOLDER_PATH = SCLUI_FOLDER_PATH
			+ IMAGE_FOLDER_NAME;

	/** 下载的图片的文件夹 */
	public static final String DOWNLOAD_FOLDER_NAME = "/download/";
	/** 下载的图片的文件夹路径 */
	public static final String DOWNLOAD_FOLDER_PATH = SCLUI_FOLDER_PATH
			+ DOWNLOAD_FOLDER_NAME;

	/** 下载的图片的文件夹 */
	public static final String DOWNLOAD_VIDEO_FOLDER_NAME = "/video/";
	/** 下载的图片的文件夹路径 */
	public static final String DOWNLOAD_VIDEO_FOLDER_PATH = DOWNLOAD_FOLDER_PATH
			+ DOWNLOAD_VIDEO_FOLDER_NAME;

	/** 保存的图片的文件夹 */
	public static final String SAVE_FOLDER_NAME = "/save/";
	/** 保存的图片的文件夹路径 */
	public static final String SAVE_FOLDER_PATH = SCLUI_FOLDER_PATH
			+ SAVE_FOLDER_NAME;

	/** 拍照临时图片名称 */
	public static final String TEMPORARY_IMAGE_NAME = SCLUI_FOLDER_PATH
			+ "/temp.jpg";

	/** 裁剪后保存的图片的文件名称 */
	public static final String SAVE_IMAGE_NAME = "scl_" + TimeUtils.nowTime()
			+ ".jpg";

	/** 下载的图片的储存路径 */
	public static final String DOWNLOAD_IMAGE_NAME = DOWNLOAD_FOLDER_PATH
			+ "head_pic.jpg";

	/** 下载的个人头像的储存路径 */
	public static final String TEMP_IMAGE_NAME = DOWNLOAD_FOLDER_PATH
			+ "temp.jpg";

	/** 下载的公司LOGO的储存路径 */
	public static final String DOWNLOAD_LOGO_NAME = DOWNLOAD_FOLDER_PATH
			+ "logo_pic.jpg";

	/** 下载的视频画刊的储存路径 */
	public static final String DOWNLOAD_BOOK_PATH = DOWNLOAD_FOLDER_PATH;

	public static final String LOGIN_STATUS_DATA = "scl_login_status";
	public static final String LOGIN_INFO_DATA = "scl_login_info";
	public static final String HEAD_PIC_DATA = "scl_headpic";
}
