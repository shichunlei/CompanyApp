package com.cells.companyapp.been;

import com.cells.companyapp.utils.ApiUtils;

public class Slides {

	private int id;
	private String image;
	private String name;
	private String image_url;
	private int image_width;
	private int image_height;

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"image\":\"" + image + "\", \"name\":\""
				+ name + "\", \"image_url\":\"" + image_url
				+ "\", \"image_width\":" + image_width + ", \"image_height\":"
				+ image_height + "}";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return (ApiUtils.ROOT_URL + image);
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public int getImage_width() {
		return image_width;
	}

	public void setImage_width(int image_width) {
		this.image_width = image_width;
	}

	public int getImage_height() {
		return image_height;
	}

	public void setImage_height(int image_height) {
		this.image_height = image_height;
	}

}
