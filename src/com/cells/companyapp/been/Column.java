package com.cells.companyapp.been;

public class Column {

	private int id;
	private String image;
	private String name;
	private int image_width;
	private int image_height;

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"image\":\"" + image + "\", \"name\":\""
				+ name + "\", \"image_width\":" + image_width
				+ ", \"image_height\":" + image_height + "}";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
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
