package com.cells.companyapp.been;

public class Gallery {

	private int company_id;
	private String name;
	private Picture picture;

	private String year;
	private Leader leader;
	private Activity activity;

	@Override
	public String toString() {
		return "{\"company_id\":" + company_id + ",\"year\":\"" + year
				+ "\",\"leader\":" + leader + ",\"activity\":" + activity
				+ ", \"name\":\"" + name + "\", \"picture\":" + picture + "}";
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Leader getLeader() {
		return leader;
	}

	public void setLeader(Leader leader) {
		this.leader = leader;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
