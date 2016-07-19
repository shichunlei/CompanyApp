package com.cells.companyapp.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * json解析 辅助类
 * 
 * @author 师春雷
 * 
 */
public class JsonUtil {

	/**
	 * 
	 * 描述：将对象转化为json.
	 * 
	 * @param list
	 * @return
	 */
	public static String toJson(Object src) {
		String json = null;
		try {
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			json = gson.toJson(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 
	 * 描述：将列表转化为json.
	 * 
	 * @param list
	 * @return
	 */
	public static String toJson(List<?> list) {
		String json = null;
		try {
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			json = gson.toJson(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 
	 * 描述：将json转化为列表.
	 * 
	 * @param json
	 * @param typeToken
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<?> fromJson(String json, TypeToken typeToken) {
		List<?> list = null;
		try {
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			Type type = typeToken.getType();
			list = gson.fromJson(json, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * 描述：将json转化为对象.
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static Object fromJson(String json, Class<?> clazz) {
		Object obj = null;
		try {
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			obj = gson.fromJson(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
