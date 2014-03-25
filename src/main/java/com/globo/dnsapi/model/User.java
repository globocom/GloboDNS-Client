package com.globo.dnsapi.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class User extends GenericJson {

	@Key
	private String email;
	
	@Key
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
