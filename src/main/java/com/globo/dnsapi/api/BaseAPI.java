package com.globo.dnsapi.api;

import java.lang.reflect.Type;

import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.RequestProcessor;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.google.api.client.http.HttpHeaders;

public abstract class BaseAPI<T> {
	
	private final RequestProcessor transport;
	protected String token;
	
	public BaseAPI(RequestProcessor transport) {
		this.transport = transport;
	}
	
	protected RequestProcessor getTransport() {
		if (this.transport == null) {
			throw new RuntimeException("No transport configured");
		}
		return this.transport;
	}
	
	protected HttpHeaders getHttpHeaders() {
		if (this.token == null) {
			return null;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", this.token);
		return headers;
	}
	
	protected abstract Type getType();
	
	protected abstract Type getListType();
	
	protected DNSAPIRoot<T> get(String suffixUrl, boolean isList) throws DNSAPIException {
		DNSAPIRoot<T> answer;
		if (isList) {
			answer = this.getTransport().get(suffixUrl, getHttpHeaders(), getListType());
		} else {
			answer = this.getTransport().get(suffixUrl, getHttpHeaders(), getType());
		}
		return answer;
	}
	
	protected DNSAPIRoot<T> post(String suffixUrl, Object payload , boolean isList) throws DNSAPIException {
		DNSAPIRoot<T> answer;
		if (isList) {
			answer = this.getTransport().post(suffixUrl, payload, getHttpHeaders(), getListType());	
		} else {
			answer = this.getTransport().post(suffixUrl, payload, getHttpHeaders(), getType());
		}
		return answer;
	}
	
	protected DNSAPIRoot<T> put(String suffixUrl, Object payload, boolean isList) throws DNSAPIException {
		DNSAPIRoot<T> answer;
		if (isList) {
			answer = this.getTransport().put(suffixUrl, payload, getHttpHeaders(), getListType());
		} else {
			answer = this.getTransport().put(suffixUrl, payload, getHttpHeaders(), getType());
		}
		return answer;
	}
	
	protected DNSAPIRoot<T> delete(String suffixUrl, boolean isList) throws DNSAPIException {
		DNSAPIRoot<T> answer;
		if (isList) {
			answer = this.getTransport().delete(suffixUrl, getHttpHeaders(), getListType());
		} else {
			answer = this.getTransport().delete(suffixUrl, getHttpHeaders(), getType());
		}
		return answer;
	}
	
}