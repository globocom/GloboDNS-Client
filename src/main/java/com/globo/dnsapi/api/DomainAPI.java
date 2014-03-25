package com.globo.dnsapi.api;

import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.RequestProcessor;
import com.globo.dnsapi.model.Domain;
import com.google.api.client.http.HttpHeaders;

public class DomainAPI extends BaseAPI {

	private String token;
	
	public DomainAPI(RequestProcessor transport, String token) {
		super(transport);
		this.token = token;
	}
	
	public Domain listByName(String domainName) throws DNSAPIException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", this.token);
		Domain domain = this.get("/domains.json?query=" + domainName, Domain.class, headers);
		return domain;
	}
	
}
