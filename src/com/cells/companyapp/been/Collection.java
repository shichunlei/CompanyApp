package com.cells.companyapp.been;

public class Collection {

	private int id;
	/** id */
	private int collection_id;
	/** 名称 */
	private String name;
	/** 图片地址URL */
	private String image;
	/** 内容 */
	private String content;
	/**
	 * type2 = 0 "新闻"
	 * 
	 * type2 = 1 "愿景";
	 * 
	 * type2 = 2 "使命";
	 * 
	 * type2 = 3 "精神";
	 * 
	 * type2 = 4 "价值观";
	 * 
	 * type2 = 5 "经营方针";
	 * 
	 * type2 = 6 "标志释义";
	 */
	private int type;
	/** 赞的数量 */
	private int like_count;
	/** 评论的数量 */
	private int comment_count;
	/** 创建时间 */
	private String created_at;

	@Override
	public String toString() {
		return "Collection [id=" + id + ", collection_id=" + collection_id
				+ ", name=" + name + ", image=" + image + ", content="
				+ content + ", type=" + type + ", like_count=" + like_count
				+ ", comment_count=" + comment_count + ", created_at="
				+ created_at + "]";
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
		return image;
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
