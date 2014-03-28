package com.globo.dnsapi.http;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

import com.globo.dnsapi.api.AuthAPI;
import com.globo.dnsapi.api.DomainAPI;
import com.globo.dnsapi.api.RecordAPI;
import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.globo.dnsapi.model.ErrorMessage;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

public abstract class RequestProcessor {
	
	private String token;
	
	static final JsonFactory JSON_FACTORY = new JacksonFactory();
	static final JsonObjectParser parser = new JsonObjectParser(JSON_FACTORY);

	public abstract <T> DNSAPIRoot<T> get(String suffixUrl, HttpHeaders headers, Type type) throws DNSAPIException;
	
	public abstract <T> DNSAPIRoot<T> post(String suffixUrl, Object payload, HttpHeaders headers, Type type) throws DNSAPIException;
	
	public abstract <T> DNSAPIRoot<T> put(String suffixUrl, Object payload, HttpHeaders headers, Type type) throws DNSAPIException;
	
	public abstract <T> DNSAPIRoot<T> delete(String suffixUrl, HttpHeaders headers, Type type) throws DNSAPIException;

	protected <T> DNSAPIRoot<T> parseJson(String inputContent, Type type) throws DNSAPIException {
		
		DNSAPIRoot<T> dnsAPIRoot = new DNSAPIRoot<T>();
		
		if ("".equalsIgnoreCase(inputContent)) {
			// Empty response, just return empty object
			return dnsAPIRoot;
		}
		
		boolean isList = false;
		if (inputContent.startsWith("[") && inputContent.endsWith("]")) {
			// This means it's a list
			isList = true;
		}
		
		Reader in = new StringReader(inputContent);
		
		try {			
			if (isList) {
				List<T> retList = (List<T>) parser.parseAndClose(in, type);
				dnsAPIRoot.setObjectList(retList);
			} else {
				T retObj = (T) parser.parseAndClose(in, type);
				dnsAPIRoot.getObjectList().add(retObj);
			}
			
			return dnsAPIRoot;

		} catch (IOException e) {
			throw new DNSAPIException("IOError: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Exception treatment for generic calls
	 * @param statusCode
	 * @param responseAsString
	 * @throws IOException
	 * @throws NetworkAPIException
	 */
	protected void handleExceptionIfNeeded(int statusCode, String responseAsString) throws DNSAPIException {
		if (statusCode/100 == 2) {
			// 200 family code
			// Successful return, do nothing
			return;
		} else if (statusCode/100 == 4 || statusCode/100 == 5) {
			// 400 and 500 family codes
			// Something was wrong, produce an error result
			// This assumes error is well formed and mappable to class ErrorMessage
			DNSAPIRoot<ErrorMessage> response = this.parseJson(responseAsString, ErrorMessage.class);
			ErrorMessage errorMsg = response.getFirstObject();
			if (errorMsg != null && errorMsg.getMsg() != null) {
				throw new DNSAPIException(errorMsg.getMsg());
			} else {
				throw new DNSAPIException(responseAsString);
			}
		} else {
			// Unknown error code, return generic exception with description
			throw new DNSAPIException(responseAsString);
		}
	}
	
	public AuthAPI getAuthAPI() {
		return new AuthAPI(this);
	}
	
	public DomainAPI getDomainAPI() {
		return new DomainAPI(this);
	}
	
	public RecordAPI getRecordAPI() {
		return new RecordAPI(this);
	}
	
	public String getToken() {
		return this.token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
