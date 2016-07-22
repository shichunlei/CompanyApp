package com.cells.companyapp.been;

import com.cells.companyapp.utils.HttpUtils;

public class YellowPage {

	private int id;
	private String name;
	private String en_name;
	private String abbrev_name;
	private String full_name;
	private String address;
	private String postcode;
	private String mobile;
	private String desc;
	private String logo;
	private String fax;
	private String url;
	private int height;
	private int width;

	@Override
	public String toString() {
		return "YellowPage [id=" + id + ", name=" + name + ", en_name=" + en_name + ", abbrev_name="
				+ abbrev_name + ", full_name=" + full_name + ", address=" + address + ", postcode="
				+ postcode + ", mobile=" + mobile + ", desc=" + desc + ", logo=" + logo + ", fax=" + fax
				+ ", url=" + url + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEn_name() {
		return en_name;
	}

	public void setEn_name(String en_name) {
		this.en_name = en_name;
	}

	public String getAbbrev_name() {
		return abbrev_name;
	}

	public void setAbbrev_name(String abbrev_name) {
		this.abbrev_name = abbrev_name;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLogo() {
		return (HttpUtils.ROOT_URL + logo);
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getUrl() {
		return (HttpUtils.ROOT_URL + url);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
