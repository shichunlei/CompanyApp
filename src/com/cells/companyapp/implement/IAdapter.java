package com.cells.companyapp.implement;

import com.cells.companyapp.base.BaseAdapterHelper;

/**
 * 项目名称: 谷雨合伙人
 * 
 * 包 名 称: com.chingtech.microbusiness.implement
 * 
 * 类 描 述: 扩展的Adapter接口规范
 * 
 * 创 建 人: 师春雷
 * 
 * 创建时间: 2016/6/27
 */
public interface IAdapter<T> {

	void onUpdate(BaseAdapterHelper helper, T item, int position);

	int getLayoutResId(T item);
}
