package com.cells.companyapp.been;

import java.util.ArrayList;

public class Windows {

	private ArrayList<Slides> slides;
	private News news;
	private ArrayList<Illustrate> illustrate_id;
	private Column special_column;

	public void setSlides(ArrayList<Slides> slides) {
		this.slides = slides;
	}

	public void setIllustrate_id(ArrayList<Illustrate> illustrate_id) {
		this.illustrate_id = illustrate_id;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Column getSpecial_column() {
		return special_column;
	}

	public void setSpecial_column(Column special_column) {
		this.special_column = special_column;
	}

	@Override
	public String toString() {
		return "{\"slides\":" + slides + ", \"news\":" + news
				+ ", \"illustrate_id\":" + illustrate_id
				+ ", \"special_column\":" + special_column + "}";
	}
}
