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
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.globo.globodns.client.model.ErrorMessage;
import com.globo.globodns.client.model.GloboDnsRoot;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseInterceptor;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

public abstract class AbstractAPI<T> {
	
	static final Logger LOGGER = LoggerFactory.getLogger(AbstractAPI.class);
	static final JsonFactory JSON_FACTORY = new JacksonFactory();
	static final JsonObjectParser parser = new JsonObjectParser(JSON_FACTORY);

	private final GloboDns globoDns;

	private HttpRequestFactory requestFactory;
	
	protected AbstractAPI(GloboDns globoDns) {
		if (globoDns == null) {
			throw new IllegalArgumentException("No GloboDNS configured");
		}
		this.globoDns = globoDns;
		this.requestFactory = this.buildHttpRequestFactory();
	}
	
	protected GloboDns getGloboDns() {
		return this.globoDns;
	}
	
	protected abstract Type getType();
	
	protected abstract Type getListType();
	
	/**
	 * Customize HttpRequestFactory with authentication and error handling.
	 * @return new instance of HttpRequestFactory
	 */
	protected HttpRequestFactory buildHttpRequestFactory() {
		HttpRequestFactory request = this.getGloboDns().getHttpTransport().createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {
				request.setNumberOfRetries(1);
				request.setThrowExceptionOnExecuteError(false);
				request.setParser(parser);
				request.setLoggingEnabled(true);
				request.getHeaders().setUserAgent("GloboDNS-Client");
				request.setCurlLoggingEnabled(true);
				request.setUnsuccessfulResponseHandler(new HttpUnsuccessfulResponseHandler() {
					
					@Override
					public boolean handleResponse(HttpRequest request, HttpResponse response,
							boolean supportsRetry) throws IOException {
						if (response.getStatusCode() == 401) {
							getGloboDns().clearToken();
							
							if (supportsRetry) {
								// only if supports retry I will prepare request with a new token, and ask to retry
								insertAuthenticationHeaders(request);
								return true;
							}
						}
						return false;
					}
				});
				request.setResponseInterceptor(new HttpResponseInterceptor() {
					@Override
					public void interceptResponse(HttpResponse response) throws IOException {
						LOGGER.debug("Response from {} {} is {} {}", 
								response.getRequest().getRequestMethod(),
								response.getRequest().getUrl(),
								response.getStatusCode(),
								response.getStatusMessage());
						AbstractAPI.this.interceptResponse(response);
					}
				});
				interceptRequest(request);
			}
		});
		return request;
	}
	
	/**
	 * Run before each request runs.
	 * @param request
	 */
	protected void interceptRequest(HttpRequest request) {
		insertAuthenticationHeaders(request);
	}

	protected void insertAuthenticationHeaders(HttpRequest request) {
		request.getHeaders().set("X-Auth-Token", this.getGloboDns().requestToken());
	}

	/**
	 * Run after each request complete (with or without success).
	 * @param response
	 * @throws GloboDnsException
	 * @throws IOException
	 */
	protected void interceptResponse(HttpResponse response) throws GloboDnsException, IOException {
		handleExceptionIfNeeded(response);
	}
	
	/**
	 * Exception treatment for generic calls
	 * @param statusCode
	 * @param responseAsString
	 * @throws IOException
	 * @throws GloboDnsException
	 */
	protected void handleExceptionIfNeeded(HttpResponse response) throws GloboDnsException, IOException {
		int statusCode = response.getStatusCode();
		if (statusCode/100 == 2) {
			// 200 family code
			// Successful return, do nothing
			return;
		} else if (statusCode/100 == 4 || statusCode/100 == 5) {
			// 400 and 500 family codes
			// Something was wrong, produce an error result
			String responseAsString = response.parseAsString();
			if (responseAsString == null || !responseAsString.startsWith("{")) {
				// Response not well formed!
				throw new GloboDnsException("Unknown error in GloboDNS: " + responseAsString);
			}
			
			GloboDnsRoot<ErrorMessage> responseObj = this.parse(responseAsString, ErrorMessage.class);
			ErrorMessage errorMsg = responseObj.getFirstObject();
			if (errorMsg != null && errorMsg.getMsg() != null) {
				throw new GloboDnsException(errorMsg.getMsg());
			} else {
				throw new GloboDnsException(responseAsString);
			}
		} else {
			// Unknown error code, return generic exception with description
			throw new GloboDnsException(response.parseAsString());
		}
	}
	
	/**
	 * Convert an HttpResponse object to GloboDnsRoot of <b>E</b> object.
	 * @param response
	 * @param type
	 * @return
	 * @throws GloboDnsException
	 */
	@SuppressWarnings("unchecked")
	protected <E> GloboDnsRoot<E> parse(String responseAsString, Type type) throws GloboDnsException {
		try {			
			GloboDnsRoot<E> globoDnsRoot = new GloboDnsRoot<E>();
			
			if ("".equalsIgnoreCase(responseAsString)) {
				// Empty response, just return empty object
				return globoDnsRoot;
			}
			
			boolean isList = false;
			if (responseAsString.startsWith("[") && responseAsString.endsWith("]")) {
				// This means it's a list
				isList = true;
			}
			
			Reader in = new StringReader(responseAsString);
		
			if (isList) {
				List<E> retList = (List<E>) parser.parseAndClose(in, type);
				globoDnsRoot.setObjectList(retList);
			} else {
				E retObj = (E) parser.parseAndClose(in, type);
				globoDnsRoot.getObjectList().add(retObj);
			}
			
			return globoDnsRoot;

		} catch (IOException e) {
			throw new GloboDnsException("IOError: " + e.getMessage(), e);
		}
	}
	
	protected <E> GloboDnsRoot<E> parse(HttpResponse response, Type type) throws GloboDnsException {
		try {
			return this.parse(response.parseAsString(), type);
		} catch (IOException e) {
			throw new GloboDnsException("IOError: " + e.getMessage(), e);
		}
	}
	
	protected GenericUrl buildUrl(String suffixUrl) {
		return new GenericUrl(this.globoDns.getBaseUrl() + suffixUrl);
	}
	
	protected GloboDnsRoot<T> get(String suffixUrl, boolean returnsList) throws GloboDnsException {
		try {
			Type type = returnsList ? getListType() : getType();
			GenericUrl url = this.buildUrl(suffixUrl);
			HttpRequest request = this.requestFactory.buildGetRequest(url);
			HttpResponse response = request.execute();
			return this.parse(response, type); 
		} catch (IOException e) {
			throw new GloboDnsException("IOError: " + e, e);
		}
	}
	
	protected GloboDnsRoot<T> post(String suffixUrl, Object payload, boolean returnsList) throws GloboDnsException {
		try {
			Type type = returnsList ? getListType() : getType();
			GenericUrl url = this.buildUrl(suffixUrl);
			JsonHttpContent content = null;
			if (payload != null) {
				content = new JsonHttpContent(JSON_FACTORY, payload);
			}
			HttpRequest request = this.requestFactory.buildPostRequest(url, content);
			HttpResponse response = request.execute();

			return this.parse(response, type);
		} catch (IOException e) {
			throw new GloboDnsException("IOError: " + e, e);
		}	
	}

	protected GloboDnsRoot<T> put(String suffixUrl, Object payload, boolean returnsList) throws GloboDnsException {
		try {
			Type type = returnsList ? getListType() : getType();
			GenericUrl url = this.buildUrl(suffixUrl);
			JsonHttpContent content = null;
			if (payload != null) {
				content = new JsonHttpContent(JSON_FACTORY, payload);
			}
			HttpRequest request = this.requestFactory.buildPutRequest(url, content);
			HttpResponse response = request.execute();

			return this.parse(response, type);
		} catch (IOException e) {
			throw new GloboDnsException("IOError: " + e, e);
		}	
	}

	protected GloboDnsRoot<T> delete(String suffixUrl, boolean returnsList) throws GloboDnsException {
		try {
			Type type = returnsList ? getListType() : getType();
			GenericUrl url = this.buildUrl(suffixUrl);
			HttpRequest request = this.requestFactory.buildDeleteRequest(url);
			HttpResponse response = request.execute();
			return this.parse(response, type); 
		} catch (IOException e) {
			throw new GloboDnsException("IOError: " + e, e);
		}
	}
	
	
}