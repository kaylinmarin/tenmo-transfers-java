package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class AuthenticatedUser {

    public BigDecimal getBalance;
    private String token;
	private User user;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
