package com.globo.dnsapi.model;

import com.google.api.client.json.GenericJson;

public class DNSAPIRoot<T> extends GenericJson {

	private T obj;
	
	public T getObj() {
		return this.obj;
	}
	
	public DNSAPIRoot() {
	}
}
