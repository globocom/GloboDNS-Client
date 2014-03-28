package com.globo.dnsapi.model;

import com.google.api.client.util.Key;

public class Export {

	@Key("output")
	private String result;
	
	public String getResult() {
		return this.result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public Export() {
	}
}
