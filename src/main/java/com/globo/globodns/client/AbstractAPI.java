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

import com.globo.globodns.client.exception.GloboDnsHttpException;
import com.globo.globodns.client.exception.GloboDnsIOException;
import com.globo.globodns.client.exception.GloboDnsParseException;
import com.globo.globodns.client.http.HttpUtil;
import com.globo.globodns.client.http.ResponseWrapper;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMethods;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Date;
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
				throw new GloboDnsHttpException("Unknown error in GloboDNS: " + responseAsString, statusCode);
			}
			
			GloboDnsRoot<ErrorMessage> responseObj = this.parse(responseAsString, ErrorMessage.class);
			ErrorMessage errorMsg = responseObj.getFirstObject();
			if (errorMsg != null && errorMsg.getMsg() != null) {
				throw new GloboDnsHttpException(errorMsg.getMsg(), statusCode);
			} else {
				throw new GloboDnsHttpException(responseAsString, statusCode);
			}
		} else {
			// Unknown error code, return generic exception with description
			throw new GloboDnsHttpException(response.parseAsString(), statusCode);
		}
	}
	
	/**
	 * Convert an HttpResponse object to GloboDnsRoot of <b>E</b> object.
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
			throw new GloboDnsParseException("IOError Parse: " + e.getMessage(), e);
		}
	}
	
	protected <E> GloboDnsRoot<E> parse(HttpResponse response, Type type) throws GloboDnsException {
		try {
			return this.parse(response.parseAsString(), type);
		} catch (IOException e) {
			throw new GloboDnsParseException("IOError: " + e.getMessage(), e);
		}
	}
	
	protected GenericUrl buildUrl(String suffixUrl) {
		return new GenericUrl(this.globoDns.getBaseUrl() + suffixUrl);
	}
	
	protected GloboDnsRoot<T> get(String suffixUrl, boolean returnsList) throws GloboDnsException {
		ResponseWrapper response = this.performRequest(buildUrl(suffixUrl), HttpMethods.GET, null);

		Type type = returnsList ? getListType() : getType();
		return parse(response.getContent(), type);
	}
	
	protected GloboDnsRoot<T> post(String suffixUrl, Object payload, boolean returnsList) throws GloboDnsException {
		ResponseWrapper response = this.performRequest(buildUrl(suffixUrl), HttpMethods.POST, payload);

		Type type = returnsList ? getListType() : getType();
		return parse(response.getContent(), type);
	}

	protected GloboDnsRoot<T> put(String suffixUrl, Object payload, boolean returnsList) throws GloboDnsException {
		ResponseWrapper response = this.performRequest(buildUrl(suffixUrl), HttpMethods.PUT, payload);

		Type type = returnsList ? getListType() : getType();
		return parse(response.getContent(), type);
	}

	protected GloboDnsRoot<T> delete(String suffixUrl, boolean returnsList) throws GloboDnsException {
		ResponseWrapper response = this.performRequest(buildUrl(suffixUrl), HttpMethods.DELETE, null);

		Type type = returnsList ? getListType() : getType();
		return parse(response.getContent(), type);
	}

	protected ResponseWrapper performRequest(GenericUrl url, String method, Object payload) throws GloboDnsException {
		Long startTime = new Date().getTime();

		try {
			HttpRequest request = this.buildRequest(method, url, payload);
			HttpUtil.loggingRequest(request);

			HttpResponse response = request.execute();

			ResponseWrapper helper = new ResponseWrapper(response);
			HttpUtil.loggingResponse(startTime, request, helper);

			return helper;
		} catch (IOException e) {
			LOGGER.error("IOException trying request: " +  url.toString() + " method: " + method + " error: " + e.toString(), e);
			throw new GloboDnsIOException("IOException: " + e.getMessage(), e );
		}
	}
	protected HttpRequest buildRequest(String method, GenericUrl url, Object payload) throws IOException {
		HttpRequest request;

		// Preparing content for POST and PUT
		HttpContent content = null;
		if (payload != null) {
			content = new JsonHttpContent(new JacksonFactory(), payload);
		}

		request = this.requestFactory.buildRequest(method, url, content);
		request.setLoggingEnabled(true);

		return request;
	}
}