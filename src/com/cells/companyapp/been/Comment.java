package com.cells.companyapp.been;

import com.cells.companyapp.utils.ApiUtils;

public class Comment {

	private int id;
	private String body;
	private String user_name;
	private String user_avatar;
	private String manager_name;
	private String manager_avatar;
	private String created_at;

	@Override
	public String toString() {
		return "Comment [id=" + id + ", body=" + body + ", user_name="
				+ user_name + ", user_avatar=" + user_avatar
				+ ", manager_name=" + manager_name + ", manager_avatar="
				+ manager_avatar + ", created_at=" + created_at + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_avatar() {
		return (ApiUtils.ROOT_URL + user_avatar);
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public String getManager_name() {
		return manager_name;
	}

	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}

	public String getManager_avatar() {
		return (ApiUtils.ROOT_URL + manager_avatar);
	}

	public void setManager_avatar(String manager_avatar) {
		this.manager_avatar = manager_avatar;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

}
