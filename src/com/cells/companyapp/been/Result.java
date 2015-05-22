package com.cells.companyapp.been;

public class Result {

	private boolean status;
	private String error;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "Result [status=" + status + ", error=" + error + "]";
	}

}
