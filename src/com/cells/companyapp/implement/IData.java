package com.cells.companyapp.implement;

import java.util.List;

/**
 * 项目名称: 谷雨合伙人
 * 
 * 包 名 称: com.chingtech.microbusiness.implement
 * 
 * 类 描 述: 数据操作接口规范
 * 
 * 创 建 人: 师春雷
 * 
 * 创建时间: 2016/6/27
 */
public interface IData<T> {

	void add(T elem);

	void addAll(List<T> elem);

	void set(T oldElem, T newElem);

	void set(int index, T elem);

	void remove(T elem);

	void remove(int index);

	void replaceAll(List<T> elem);

	boolean contains(T elem);

	void clear();

}
