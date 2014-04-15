package com.globo.dnsapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;

/**
 * DNSAPIFactory like with mock simulated responses. Useful for tests.
 * @author snbuback
 *
 */
public class MockDNSAPI extends DNSAPI {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MockDNSAPI.class);
	
	public MockDNSAPI() {
		super(new SimulatedHttpTransport());
		this.setBaseUrl("http://example.com");
	}

	public enum HttpMethod {
		GET, POST, PUT, DELETE
	}

	@Override
	public String buildToken() {
		LOGGER.info("Generating new authentication token");
		return "fake-auth-token";
	}
	
	public void registerFakeRequest(HttpMethod method, String url, String expectedResult) {
		registerFakeRequest(method, url, 200, expectedResult);
	}

	public void registerFakeRequest(HttpMethod method, String url, int statusCode, String expectedResult) {
		((SimulatedHttpTransport)this.getHttpTransport()).registerFakeRequest(method, this.getBaseUrl() + url, statusCode, expectedResult);
	}
	
	private static class SimulatedHttpTransport extends MockHttpTransport {
		final Map<String, MockLowLevelHttpResponse> urlVsResponse = new HashMap<String, MockLowLevelHttpResponse>();
		
		public void registerFakeRequest(HttpMethod method, String url, int statusCode, String expectedResult) {
			String key = method.name() + " " + url;
//			if (this.urlVsResponse.containsKey(key)) {
//				throw new IllegalArgumentException("Request " + key + " already exists.");
//			}
			MockLowLevelHttpResponse fakeResponse = new MockLowLevelHttpResponse();
			fakeResponse.setStatusCode(statusCode);
			fakeResponse.setContent(expectedResult);
			this.urlVsResponse.put(key, fakeResponse);
		}

		@Override
		public LowLevelHttpRequest buildRequest(String method, String url)
				throws IOException {
			MockLowLevelHttpRequest request = (MockLowLevelHttpRequest) super.buildRequest(method, url);
			MockLowLevelHttpResponse response = this.urlVsResponse.get(method.toUpperCase() + " " + url);
			if (response == null) {
				throw new RuntimeException("Invalid url: " + url);
			}
			request.setResponse(response);
			return request;
		}
		
	}
}
