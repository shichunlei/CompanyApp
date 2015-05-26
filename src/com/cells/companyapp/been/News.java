package com.cells.companyapp.been;

public class News {

	private int id;
	private String image;
	private String title;
	private String image_url;
	private int image_width;
	private int image_height;
	private int comment_count;
	private String sub_title;

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"image\":\"" + image + "\", \"title\":\""
				+ title + "\", \"image_url\":\"" + image_url
				+ "\", \"image_width\":" + image_width + ", \"image_height\":"
				+ image_height + ", \"comment_count\":" + comment_count
				+ ", \"sub_title\":\"" + sub_title + "\"}";
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public String getSub_title() {
		return sub_title;
	}

	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}

}
