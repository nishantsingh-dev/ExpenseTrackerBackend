package com.tracker.expensetracker.user;

public class UserResponse {

	private String token;
	private User user;

	public UserResponse() {
	}

	public UserResponse(String token, User user) {
		this.token = token;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
