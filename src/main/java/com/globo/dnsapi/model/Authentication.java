package com.globo.dnsapi.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Authentication extends GenericJson {

	@Key
	private Long id;
	
	@Key("authentication_token")
	private String token;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
