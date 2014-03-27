package com.globo.dnsapi.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.RequestProcessor;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.globo.dnsapi.model.Domain;
import com.google.api.client.http.HttpHeaders;

public class DomainAPI extends BaseAPI<Domain> {

	private HttpHeaders headerWithToken;
	
	public DomainAPI(RequestProcessor transport, String token) {
		super(transport);
		this.headerWithToken = new HttpHeaders();
		this.headerWithToken.set("X-Auth-Token", token);
	}
	
	public List<Domain> listByName(String domainName) throws DNSAPIException {
		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains.json?query=" + domainName, this.headerWithToken, true);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getObjectList();
	}
	
	public List<Domain> listAll() throws DNSAPIException {
		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains.json", this.headerWithToken, true);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getObjectList();
	}

	@Override
	protected Type getType() {
		return new TypeReference<Domain>() {}.getType();
	}

	@Override
	protected Type getListType() {
		return new TypeReference<List<Domain>>() {}.getType();
	}
	
}
