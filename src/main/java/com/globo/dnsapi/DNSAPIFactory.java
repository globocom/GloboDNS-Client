package com.globo.dnsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.globo.dnsapi.api.AuthAPI;
import com.globo.dnsapi.api.DomainAPI;
import com.globo.dnsapi.api.ExportAPI;
import com.globo.dnsapi.api.RecordAPI;
import com.globo.dnsapi.model.Authentication;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class DNSAPIFactory {

	static final Logger LOGGER = LoggerFactory.getLogger(DNSAPIFactory.class);

	private final HttpTransport httpTransport;
	private String baseUrl;
	private String userName;
	private String password;
	private String token;

	protected DNSAPIFactory(HttpTransport httpTransport) {
		this.httpTransport = httpTransport;
	}

	public static DNSAPIFactory buildHttpApi(String baseUrl, String userName, String password) {
		DNSAPIFactory apiFactory = new DNSAPIFactory(new NetHttpTransport());
		apiFactory.setBaseUrl(baseUrl);
		apiFactory.setUserName(userName);
		apiFactory.setPassword(password);
		return apiFactory;
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

	public ExportAPI getExportAPI() {
		return new ExportAPI(this);
	}

	public String getToken() {
		return this.token;
	}

	public synchronized String requestToken() {
		if (this.getToken() == null) {
			this.token = this.buildToken();
		}
		return this.token;
	}
	
	protected String buildToken() {
		LOGGER.info("Requesting new authentication token");
		AuthAPI authAPI = getAuthAPI();
		Authentication auth = authAPI.signIn(this.getUserName(), this.getPassword());
		return auth.getToken();
	}

	public synchronized void clearToken() {
		if (this.token != null) {
			this.token = null;
			LOGGER.info("Authentication token cleared.");
		}
	}

	protected HttpTransport getHttpTransport() {
		return this.httpTransport;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		this.clearToken();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		this.clearToken();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.clearToken();
	}

}
