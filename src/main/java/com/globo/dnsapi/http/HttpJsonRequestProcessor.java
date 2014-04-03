package com.globo.dnsapi.http;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;

public class HttpJsonRequestProcessor extends RequestProcessor {

	static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);
	
	private String baseUrl;
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private volatile HttpRequestFactory requestFactory;
	
	public HttpJsonRequestProcessor(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	private HttpRequestFactory getRequestFactory() {
		if (this.requestFactory == null) {
			synchronized (this) {
				this.requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
					@Override
					public void initialize(HttpRequest request) throws IOException {
						request.setParser(parser);
					}
				});
			}
		}
		return this.requestFactory;
	}
	
	protected HttpRequest buildRequest(String method, GenericUrl url, Object payload, HttpHeaders headers) throws IOException {
		HttpRequest request;
		
		// Preparing content for POST and PUT
		JsonHttpContent content = null;
		if (payload != null) {
			content = new JsonHttpContent(JSON_FACTORY, payload);
		}
		
		if ("GET".equalsIgnoreCase(method)) {
			request = this.getRequestFactory().buildGetRequest(url);
		} else if ("POST".equalsIgnoreCase(method)) {
			request = this.getRequestFactory().buildPostRequest(url, content);
		} else if ("PUT".equalsIgnoreCase(method)) {
			request = this.getRequestFactory().buildPutRequest(url, content);
		} else if ("DELETE".equalsIgnoreCase(method)) {
			request = this.getRequestFactory().buildDeleteRequest(url);
		} else {
			throw new InvalidParameterException("Invalid HTTP method.");
		}

		if (headers != null) {
			request.setHeaders(headers);
		}
		request.setLoggingEnabled(true);
		LOGGER.debug(method + " %s", url);
		
		return request;
	}
	
	protected HttpResponse performHttpRequest(HttpRequest request) throws IOException {
		LOGGER.debug("Calling DNSAPI: " + request.getRequestMethod() + " " + request.getUrl() + " " + request.getContent());
		HttpResponse response = request.execute();
		LOGGER.debug("Response from DNSAPI: " + response.getStatusCode() + " " + response.getStatusMessage());
		return response;
	}
	
	protected String performRequest(HttpRequest request) throws DNSAPIException, IOException {
		int httpStatusCode;
		String responseAsString;
		try {
			HttpResponse httpResponse = this.performHttpRequest(request);
			httpStatusCode = httpResponse.getStatusCode();
			responseAsString = httpResponse.parseAsString();
		} catch (HttpResponseException e) {
			httpStatusCode = e.getStatusCode();
			responseAsString = e.getMessage();
		}
		handleExceptionIfNeeded(httpStatusCode, responseAsString);
		return responseAsString;
	}
	
	protected GenericUrl buildUrl(String suffixUrl) {
		return new GenericUrl(this.baseUrl + suffixUrl);
	}
	
	@Override
	public <T> DNSAPIRoot<T> get(String suffixUrl, HttpHeaders headers, Type type)
			throws DNSAPIException {
		try {
			GenericUrl url = this.buildUrl(suffixUrl);
			HttpRequest request = this.buildRequest("GET", url, null, headers);
			String response = this.performRequest(request);
			return this.parseJson(response, type); 
		} catch (IOException e) {
			throw new DNSAPIException("IOError: " + e, e);
		}
	}

	@Override
	public <T> DNSAPIRoot<T> post(String suffixUrl, Object payload,
			HttpHeaders headers, Type type) throws DNSAPIException {
		try {
			GenericUrl url = this.buildUrl(suffixUrl);
			HttpRequest request = this.buildRequest("POST", url, payload, headers);
			String response = this.performRequest(request);

			return this.parseJson(response, type);
		} catch (IOException e) {
			throw new DNSAPIException("IOError: " + e, e);
		}	
	}

	@Override
	public <T> DNSAPIRoot<T> put(String suffixUrl, Object payload,
			HttpHeaders headers, Type type) throws DNSAPIException {
		try {
			GenericUrl url = this.buildUrl(suffixUrl);
			HttpRequest request = this.buildRequest("PUT", url, payload, headers);
			String response = this.performRequest(request);
				
			return this.parseJson(response, type);
		} catch (IOException e) {
			throw new DNSAPIException("IOError: " + e, e);
		}
	}

	@Override
	public <T> DNSAPIRoot<T> delete(String suffixUrl, HttpHeaders headers, Type type)
			throws DNSAPIException {
		try {
			GenericUrl url = this.buildUrl(suffixUrl);
			HttpRequest request = this.buildRequest("DELETE", url, null, headers);
			String response = this.performRequest(request);

			return this.parseJson(response, type);
		} catch (IOException e) {
			throw new DNSAPIException("IOError: " + e, e);
		}
	}

}
