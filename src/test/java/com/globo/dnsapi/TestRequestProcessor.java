package com.globo.dnsapi;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.RequestProcessor;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.google.api.client.http.HttpHeaders;

public class TestRequestProcessor extends RequestProcessor {
	
	public enum HttpMethod {
		GET, POST, PUT, DELETE
	}
	
	public final Map<String, FakeResponse> urlVsResponse = new HashMap<String, FakeResponse>();

	public void registerFakeRequest(HttpMethod method, String url, String expectedResult) {
		registerFakeRequest(method, url, 200, expectedResult);
	}

	public void registerFakeRequest(HttpMethod method, String url, int statusCode, String expectedResult) {
		String key = method.name() + " " + url;
		if (this.urlVsResponse.containsKey(key)) {
			throw new IllegalArgumentException("Request " + key + " already exists.");
		}
		
		FakeResponse fakeResponse = new FakeResponse(statusCode, expectedResult);
		this.urlVsResponse.put(key, fakeResponse);
	}
	
	private FakeResponse fakeResponse(HttpMethod method, String url) {
		String key = method.name() + " " + url;
		return this.urlVsResponse.get(key);
	}
	
	private <T> DNSAPIRoot<T> parseResponse(FakeResponse response, Type type) throws DNSAPIException {
		if (response == null) {
			throw new RuntimeException("Invalid url");
		}

		int httpStatusCode = response.getStatusCode();
		String content = response.getContent();
		handleExceptionIfNeeded(httpStatusCode, content);
		return this.parseJson(content, type);
	}

	public <T> DNSAPIRoot<T> get(String baseurl, HttpHeaders headers, Type type) throws DNSAPIException {
		FakeResponse response = this.fakeResponse(HttpMethod.GET, baseurl);
		return this.parseResponse(response, type);
	}

	public <T> DNSAPIRoot<T> post(String baseurl, Object payload, HttpHeaders headers, Type type) throws DNSAPIException {
		FakeResponse response = this.fakeResponse(HttpMethod.POST, baseurl);
		return this.parseResponse(response, type);
	}

	public <T> DNSAPIRoot<T> put(String baseurl, Object payload, HttpHeaders headers, Type type) throws DNSAPIException {
		FakeResponse response = this.fakeResponse(HttpMethod.PUT, baseurl);
		return this.parseResponse(response, type);
	}

	public <T> DNSAPIRoot<T> delete(String baseurl, HttpHeaders headers, Type type) throws DNSAPIException {
		FakeResponse response = this.fakeResponse(HttpMethod.DELETE, baseurl);
		return this.parseResponse(response, type);
	}
	
	private static class FakeResponse {
		
		private final int statusCode;
		
		private final String content;
		
		FakeResponse(int statusCode, String content) {
			this.statusCode = statusCode;
			this.content = content;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public String getContent() {
			return content;
		}
	}

}
