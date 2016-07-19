package com.cells.companyapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;

/**
 * 时间辅助类
 * 
 * @author 师春雷
 * 
 * @time 2014-4-20
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

	private static String nowTime;

	/**
	 * 获取系统当前时间
	 * 
	 * 没有些格式化参数，我们使用默认的日期格式。
	 * 
	 * @return
	 */
	public static String nowtime() {
		SimpleDateFormat formatter = new SimpleDateFormat();
		String nowTime = formatter.format(new Date());
		return nowTime;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * yyyy-MM-dd HH:mm:ss 格式
	 * 
	 * @return
	 */
	public static String nowTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = new Date();// 获取当前时间
		nowTime = formatter.format(time);

		return nowTime;
	}

	/**
	 * 获取当前时间
	 * 
	 * MM-dd HH:mm 格式
	 * 
	 * @return
	 */
	public static String upDataTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
		Date curDate = new Date();// 获取当前时间
		nowTime = formatter.format(curDate);
		return nowTime;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * yyyy-MM-dd HH:mm 格式
	 * 
	 * @return
	 */
	public static String nowTime2() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date();// 获取当前时间
		nowTime = formatter.format(curDate);
		return nowTime;
	}

	/**
	 * 获取系统当前年份
	 * 
	 * yyyy 格式
	 * 
	 * @return
	 */
	public static String nowYear() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date curDate = new Date();// 获取当前时间
		String nowYear = formatter.format(curDate);
		return nowYear;
	}

	/**
	 * 获取系统当前日期
	 * 
	 * yyyy-MM-dd 格式
	 * 
	 * @return
	 */
	public static String nowDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date();// 获取当前时间
		String nowYear = formatter.format(curDate);
		return nowYear;
	}

	/**
	 * 获取系统当前日期
	 * 
	 * MM月dd日 格式
	 * 
	 * @return
	 */
	public static String nowDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
		Date date = new Date();// 获取当前时间
		String nowDay = formatter.format(date);
		return nowDay;
	}

	/**
	 * 得到系统当前日为一周的第几天
	 * 
	 * @return
	 */
	public static int nowWeek() {
		Calendar calendar = Calendar.getInstance();
		// 获取当前时间为本周的第几天
		int day = (calendar.get(Calendar.DAY_OF_WEEK) - 1);
		return day;
	}

	public static String TIME_12_24(Context context) {
		ContentResolver cv = context.getContentResolver();
		String timeFormat = android.provider.Settings.System.getString(cv,
				android.provider.Settings.System.TIME_12_24);
		return timeFormat;
	}

	/**
	 * 将时间字符串转换成 MM月dd日 格式的日期串
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDate(Date date) {
		String convertDate = "";

		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");

		convertDate = formatter.format(date);

		return convertDate;
	}

	/**
	 * 得到服务器时间与当前时间的时间差
	 * 
	 * @param time
	 * @return
	 */
	public static String GetTimeDifference(String time) {
		String result = "";
		String string1 = time.substring(0, 10);
		String string2 = time.substring(11, 19);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		try {
			long createTime = format.parse(string1 + " " + string2).getTime();
			long nowTime = new Date().getTime();

			long timeInterval = (nowTime - createTime) / 1000 - 8 * 60 * 60;
			long temp = 0;

			if (timeInterval < 60) {
				result = "刚刚";
			} else if ((temp = timeInterval / 60) < 60) {
				result = temp + "分钟前";
			} else if ((temp = temp / 60) < 24) {
				result = temp + "小时前";
			} else if ((temp = temp / 24) < 30) {
				result = temp + "天前";
			} else if ((temp = temp / 30) < 12) {
				result = temp + "月前";
			} else {
				temp = temp / 12;
				result = temp + "年前";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 比较两个时间的大小
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Boolean compare_date(String startDate, String endDate) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			Date start = df.parse(startDate);
			Date end = df.parse(endDate);
			if (start.getTime() <= end.getTime()) {
				return true;
			} else if (start.getTime() > end.getTime()) {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	/**
	 * 将时间字符串转换成 MM月dd日EEEE HH:mm 格式的时间串
	 * 
	 * @param date
	 * @return
	 */
	public static String convertTime(Date date) {
		String convertTime = "";
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日EEEE HH:mm");
		convertTime = formatter.format(date);
		return convertTime;
	}

	/**
	 * 把毫秒转化成日期
	 * 
	 * @param dateFormat
	 *            (日期格式，例如：MM/dd/yyyy HH:mm:ss)
	 * @param millSec
	 *            (毫秒数)
	 * @return
	 */
	public static String transferLongToDate(String dateFormat, Long millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(millSec);
		return sdf.format(date);
	}
}
