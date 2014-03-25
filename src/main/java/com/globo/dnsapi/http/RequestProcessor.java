package com.globo.dnsapi.http;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import com.globo.dnsapi.api.AuthAPI;
import com.globo.dnsapi.api.DomainAPI;
import com.globo.dnsapi.exception.DNSAPIException;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

public abstract class RequestProcessor {
	
	static final JsonFactory JSON_FACTORY = new JacksonFactory();
	static final JsonObjectParser parser = new JsonObjectParser(JSON_FACTORY);

	public abstract <T> T get(String suffixUrl, Class<T> dataClass, HttpHeaders headers) throws DNSAPIException;
	
	public abstract <T> T post(String suffixUrl, Object payload, Class<T> dataClass, HttpHeaders headers) throws DNSAPIException;
	
	public abstract <T> T put(String suffixUrl, Object payload, Class<T> dataClass, HttpHeaders headers) throws DNSAPIException;
	
	public abstract <T> T delete(String suffixUrl, Class<T> dataClass, HttpHeaders headers) throws DNSAPIException;

	protected <T> T parseJson(String inputContent, Class<T> dataClass) throws DNSAPIException {
		
		if (true /* isList() */) {
			inputContent = "{\"list\":" + inputContent + "}";
		}
		Reader in = new StringReader(inputContent);
		
		try {
			ListObject<T> listObj = new ListObject<T>();
			listObj = parser.parseAndClose(in, listObj.getClass());// dataClass);
			return listObj.getList().get(0);
		} catch (IOException e) {
			throw new DNSAPIException("IOError: " + e.getMessage(), e);
		}
			
//		ArrayList<Domain> x = new ArrayList<Domain>();
//		x = httpResponse.parseAs(x.getClass());
//		System.out.println(x);
//		System.out.println(x.get(0).getClass());
//		return (T) x;
	}
	
	/**
	 * Exception treatment for generic calls
	 * @param statusCode
	 * @param responseAsString
	 * @throws IOException
	 * @throws NetworkAPIException
	 */
	protected void handleExceptionIfNeeded(int statusCode, String responseAsString) throws DNSAPIException {
		if (statusCode == 200) {
			// Successful return, do nothing
			return;
		} else if (statusCode == 400 || statusCode == 500) {
			// FIXME Update to DNSAPI
			// This assumes error is well formed and mappable to class ErrorMessage
//				NetworkAPIRoot<ErrorMessage> response = this.readXML(responseAsString, ErrorMessage.class);
//				ErrorMessage errorMsg = response.getFirstObject();
//				if (errorMsg != null) {
//					throw new NetworkAPIErrorCodeException(errorMsg.getCode(), errorMsg.getDescription());
//				} else {
//					throw new NetworkAPIException(responseAsString);	
//				}
		} else {
			// Unknown error code, return generic exception with description
			throw new DNSAPIException(responseAsString);
		}
	}
	
	public AuthAPI getAuthAPI() {
		return new AuthAPI(this);
	}
	
	public DomainAPI getDomainAPI(String token) {
		return new DomainAPI(this, token);
	}
	

	public static class ListObject<T> {
		@Key("list")
		private List<T> list;
		
		public List<T> getList() {
			return this.list;
		}
		
		public void setList(List<T> list) {
			this.list = list;
		}
	}

}
