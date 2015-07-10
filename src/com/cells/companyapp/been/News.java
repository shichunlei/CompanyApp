package com.cells.companyapp.been;

import com.cells.companyapp.utils.HttpUtils;

public class News {

	private int id;
	private String title;
	private String image;
	private int image_width;
	private int image_height;
	private int comment_count;
	private String sub_title;
	private int vote_count;
	private String content;
	private String author;
	private String created_at;
	private String updated_at;
	private boolean like_status;

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"image\":\"" + image + "\", \"title\":\""
				+ title + "\", \"image_width\":" + image_width
				+ ", \"image_height\":" + image_height + ", \"vote_count\":"
				+ vote_count + ", \"comment_count\":" + comment_count
				+ ", \"sub_title\":\"" + sub_title + "\"}";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return (HttpUtils.ROOT_URL + image);
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

	public int getVote_count() {
		return vote_count;
	}

	public void setVote_count(int vote_count) {
		this.vote_count = vote_count;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public boolean isLike_status() {
		return like_status;
	}

	public void setLike_status(boolean like_status) {
		this.like_status = like_status;
	}

}
