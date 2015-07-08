package com.cells.companyapp.been;

import com.cells.companyapp.utils.HttpUtils;

public class Collection {

	private int id;
	private int collection_id;
	private String name;
	private String image;
	private String content;
	private int type;
	private int type2;
	private int like_count;
	private int comment_count;
	private String created_at;

	@Override
	public String toString() {
		return "Collection [id=" + id + ", collection_id=" + collection_id
				+ ", name=" + name + ", image=" + image + ", content="
				+ content + ", type=" + type + ", type2=" + type2
				+ ", like_count=" + like_count + ", comment_count="
				+ comment_count + ", created_at=" + created_at + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCollection_id() {
		return collection_id;
	}

	public void setCollection_id(int collection_id) {
		this.collection_id = collection_id;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType2() {
		return type2;
	}

	public void setType2(int type2) {
		this.type2 = type2;
	}

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

}
