package com.cells.companyapp.been;

import com.cells.companyapp.utils.HttpUtils;

public class Windows {

	/** ID */
	private int id;
	/** 名称 */
	private String name;
	/** 英文名 */
	private String en_name;
	/** 介绍 */
	private String content;
	/** LOGO */
	private String logo;
	/** 当前用户点赞状态 */
	private boolean like_status;
	/***/
	private String image;
	/** 评论数量 */
	private int comment_count;
	/** 点赞数量 */
	private int vote_count;

	@Override
	public String toString() {
		return "Culture [id=" + id + ", name=" + name + ", content=" + content
				+ ", logo=" + logo + ", like_status=" + like_status
				+ ", en_name=" + en_name + ", image=" + image
				+ ", comment_count=" + comment_count + ", vote_count="
				+ vote_count + "]";
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLogo() {
		return (HttpUtils.ROOT_URL + logo);
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public boolean isLike_status() {
		return like_status;
	}

	public void setLike_status(boolean like_status) {
		this.like_status = like_status;
	}

	public String getEn_name() {
		return en_name;
	}

	public void setEn_name(String en_name) {
		this.en_name = en_name;
	}

	public String getImage() {
		return (HttpUtils.ROOT_URL + image);
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public int getVote_count() {
		return vote_count;
	}

	public void setVote_count(int vote_count) {
		this.vote_count = vote_count;
	}
	
}
