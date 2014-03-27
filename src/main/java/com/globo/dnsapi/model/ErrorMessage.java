package com.globo.dnsapi.model;

import com.google.api.client.util.Key;

public class ErrorMessage {

	@Key("error")
	private String msg;
	
	public ErrorMessage() {
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
