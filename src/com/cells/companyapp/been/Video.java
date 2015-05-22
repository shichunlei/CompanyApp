package com.cells.companyapp.been;

import com.cells.companyapp.utils.HttpUtils;

public class Video {

	private int id;
	private String name;
	private String image;
	private String price;
	private String issue;
	private String issue_date;
	private String document;

	@Override
	public String toString() {
		return "Video [id=" + id + ", name=" + name + ", image=" + image
				+ ", price=" + price + ", issue=" + issue + ", issue_date="
				+ issue_date + ", document=" + document + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getIssue_date() {
		return issue_date;
	}

	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
