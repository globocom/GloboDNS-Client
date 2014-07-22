/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.globo.globodns.client;

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
 * GloboDnsFactory like with mock simulated responses. Useful for tests.
 * @author snbuback
 *
 */
public class MockGloboDns extends GloboDns {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MockGloboDns.class);
	
	public MockGloboDns() {
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
	
	public int numberOfRequests(HttpMethod method, String url) {
		return ((SimulatedHttpTransport)this.getHttpTransport()).numberOfRequests(method.name(), url);
	}
	
	public int totalNumberOfRequests() {
		return ((SimulatedHttpTransport)this.getHttpTransport()).totalNumberOfRequests();
	}
	
	private static class SimulatedHttpTransport extends MockHttpTransport {
		private int totalNumberOfRequests = 0;
		final Map<String, CounterMockLowLevelHttpResponse> urlVsResponse = new HashMap<String, CounterMockLowLevelHttpResponse>();
		
		public void registerFakeRequest(HttpMethod method, String url, int statusCode, String expectedResult) {
			String key = method.name() + " " + url;
//			if (this.urlVsResponse.containsKey(key)) {
//				throw new IllegalArgumentException("Request " + key + " already exists.");
//			}
			CounterMockLowLevelHttpResponse fakeResponse = new CounterMockLowLevelHttpResponse();
			fakeResponse.setStatusCode(statusCode);
			fakeResponse.setContent(expectedResult);
			this.urlVsResponse.put(key, fakeResponse);
		}

		@Override
		public LowLevelHttpRequest buildRequest(String method, String url)
				throws IOException {
			MockLowLevelHttpRequest request = (MockLowLevelHttpRequest) super.buildRequest(method, url);
			CounterMockLowLevelHttpResponse response = this.urlVsResponse.get(method.toUpperCase() + " " + url);
			if (response == null) {
				throw new RuntimeException("Invalid url: " + url);
			}
			request.setResponse(response);
			response.numberOfRequests++;
			totalNumberOfRequests++;
			return request;
		}
		
		public int totalNumberOfRequests() {
			return this.totalNumberOfRequests;
		}

		public int numberOfRequests(String method, String url) {
			CounterMockLowLevelHttpResponse response = this.urlVsResponse.get(method.toUpperCase() + " " + url);
			if (response == null) {
				throw new RuntimeException("Invalid url: " + url);
			}
			return response.numberOfRequests;
		}

	}
	
	public static class CounterMockLowLevelHttpResponse extends MockLowLevelHttpResponse {
		int numberOfRequests = 0;
	}
}
