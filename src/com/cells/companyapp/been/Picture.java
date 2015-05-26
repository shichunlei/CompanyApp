package com.cells.companyapp.been;

import com.cells.companyapp.utils.HttpUtils;

public class Picture {

	private int id;
	private String name;
	private String image;

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

	public String getImage() {
		return (HttpUtils.ROOT_URL + image);
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"name\":" + name + "\", \"image\":\""
				+ image + "\"}";
	}

}
