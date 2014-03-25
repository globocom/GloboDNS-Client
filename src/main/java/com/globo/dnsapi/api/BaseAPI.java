package com.globo.dnsapi.api;

import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.RequestProcessor;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.GenericJson;

public class BaseAPI {
	
	private final RequestProcessor transport;
	
	public BaseAPI(RequestProcessor transport) {
		this.transport = transport;
	}
	
	protected RequestProcessor getTransport() {
		if (this.transport == null) {
			throw new RuntimeException("No transport configured");
		}
		return this.transport;
	}
	
	protected <T> T get(String suffixUrl, Class<T> dataClass, HttpHeaders headers) throws DNSAPIException {
		T answer = (T) this.getTransport().get(suffixUrl, dataClass, headers);
		return answer;
	}
	
	protected <T extends GenericJson> T post(String suffixUrl, Object payload, Class<T> dataClass, HttpHeaders headers) throws DNSAPIException {
		T answer = (T) this.getTransport().post(suffixUrl, payload, dataClass, headers);
		return answer;
	}
	
	protected <T extends GenericJson> T put(String suffixUrl, Object payload, Class<T> dataClass, HttpHeaders headers) throws DNSAPIException {
		T answer = (T) this.getTransport().put(suffixUrl, payload, dataClass, headers);
		return answer;
	}
	
	protected <T extends GenericJson> T delete(String suffixUrl, Class<T> dataClass, HttpHeaders headers) throws DNSAPIException {
		T answer = (T) this.getTransport().delete(suffixUrl, dataClass, headers);
		return answer;
	}
	
}