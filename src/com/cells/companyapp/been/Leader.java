package com.cells.companyapp.been;

public class Leader {

	private int gallery_id;
	private String image;

	public int getGallery_id() {
		return gallery_id;
	}

	public void setGallery_id(int gallery_id) {
		this.gallery_id = gallery_id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "{\"gallery_id\":" + gallery_id + ", \"image\":\"" + image + "\"}";
	}

}
