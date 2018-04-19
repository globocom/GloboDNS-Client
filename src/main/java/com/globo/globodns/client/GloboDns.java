package com.globo.globodns.client;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.globo.globodns.client.api.AuthAPI;
import com.globo.globodns.client.api.DomainAPI;
import com.globo.globodns.client.api.ExportAPI;
import com.globo.globodns.client.api.RecordAPI;
import com.globo.globodns.client.model.Authentication;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import java.net.ProxySelector;

public class GloboDns {

	static final Logger LOGGER = LoggerFactory.getLogger(GloboDns.class);

	private final HttpTransport httpTransport;
	private String baseUrl;
	private String userName;
	private String password;
	private String token;
	private Integer numberOfRetries = 1;
	private Integer connectTimeout = 120000;
	private Integer readTimeout = 120000;

	protected GloboDns(HttpTransport httpTransport) {
		this.httpTransport = httpTransport;
	}

	public static GloboDns buildHttpApi(String baseUrl, String userName, String password
										) {

		GloboDns apiFactory = new GloboDns(getTransport());
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

	//Not using default HttpClient created by ApacheHttpTransport.Builder().build() as it turns off stale connection check
	private static ApacheHttpTransport getTransport(){
		return new ApacheHttpTransport(newDefaultHttpClient(SSLSocketFactory.getSocketFactory(), getHttpParams(), ProxySelector.getDefault()));
	}

	private static HttpParams getHttpParams() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		ConnManagerParams.setMaxTotalConnections(params, 200);
		ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(20));
		return params;
	}

	private static HttpClient newDefaultHttpClient(SSLSocketFactory socketFactory, HttpParams params, ProxySelector proxySelector) {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", socketFactory, 443));
		ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(params, registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager, params);
		httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
		httpClient.setRoutePlanner(new ProxySelectorRoutePlanner(registry, proxySelector));
		return httpClient;
	}

	public int getNumberOfRetries() {
		return numberOfRetries;
	}

	public int getConnectionTimeout() {
		return connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setNumberOfRetries(Integer numberOfRetries) {
		this.numberOfRetries = numberOfRetries;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}
}