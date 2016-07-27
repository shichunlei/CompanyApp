package com.cells.companyapp.been;

import com.cells.companyapp.utils.ApiUtils;

public class Picture {

	private int id;
	private String name;
	private String image;
	private int image_height;
	private int image_width;

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
		return (ApiUtils.ROOT_URL + image);
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getHeight() {
		return image_height;
	}

	public void setHeight(int image_height) {
		this.image_height = image_height;
	}

	public int getWidth() {
		return image_width;
	}

	public void setWidth(int image_width) {
		this.image_width = image_width;
	}

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"name\":" + name + "\", \"image\":\"" + image + "\"}";
	}

}
