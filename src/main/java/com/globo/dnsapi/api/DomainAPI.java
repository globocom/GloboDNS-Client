package com.globo.dnsapi.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.RequestProcessor;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.globo.dnsapi.model.Domain;

public class DomainAPI extends BaseAPI<Domain> {
	
	public DomainAPI(RequestProcessor transport) {
		super(transport);
	}

	@Override
	protected Type getType() {
		return new TypeReference<Domain>() {}.getType();
	}

	@Override
	protected Type getListType() {
		return new TypeReference<List<Domain>>() {}.getType();
	}
	
	//////////////////
	// Domain calls //
	//////////////////
	public Domain createDomain(String name, Long templateId, String authorityType) throws DNSAPIException {
		return this.createDomain(name, templateId, authorityType, false);
	}
	
	public List<Domain> listAll() throws DNSAPIException {
		return this.listAll(false);
	}
	
	public List<Domain> listByQuery(String query) throws DNSAPIException {
		return this.listByQuery(query, false);
	}
	
	public Domain getById(Long domainId) throws DNSAPIException {
		return this.getById(domainId, false);
	}
	
	public void updateDomain(Long domainId, String name, String authorityType, String ttl) throws DNSAPIException {
		this.updateDomain(domainId, name, authorityType, ttl, false);
	}
	
	public void removeDomain(Long domainId) throws DNSAPIException {
		this.removeDomain(domainId, false);
	}
	
			
	//////////////////////////
	// Reverse domain calls //
	//////////////////////////
	public Domain createReverseDomain(String name, Long templateId, String authorityType) throws DNSAPIException {
		return this.createDomain(name, templateId, authorityType, true);
	}
	
	public List<Domain> listAllReverse() throws DNSAPIException {
		return this.listAll(true);
	}
	
	public List<Domain> listReverseByQuery(String query) throws DNSAPIException {
		return this.listByQuery(query, true);
	}
	
	public Domain getReverseById(Long domainId) throws DNSAPIException {
		return this.getById(domainId, true);
	}
	
	public void updateReverseDomain(Long domainId, String name, String authorityType, String ttl) throws DNSAPIException {
		this.updateDomain(domainId, name, authorityType, ttl, true);
	}
	
	public void removeReverseDomain(Long domainId) throws DNSAPIException {
		this.removeDomain(domainId, true);
	}
	
	
	////////////////////
	// Implementation //
	////////////////////
	/* 
	 * Creating features 
	 */
	private Domain createDomain(String name, Long templateId, String authorityType, boolean reverse) throws DNSAPIException {
		Domain domain = new Domain();
		domain.getDomainAttributes().setName(name);
		domain.getDomainAttributes().setTemplateId(templateId);
		domain.getDomainAttributes().setAuthorityType(authorityType);
		
		DNSAPIRoot<Domain> dnsAPIRoot = this.post("/domains.json" + (reverse ? "?reverse=true" : ""), domain, false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getFirstObject();
	}
	
	/* 
	 * Recovering features
	 */
	private List<Domain> listAll(boolean reverse) throws DNSAPIException {
		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains.json" + (reverse ? "?reverse=true" : ""), true);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getObjectList();
	}
	
	private List<Domain> listByQuery(String query, boolean reverse) throws DNSAPIException {
		if (query == null) {
			throw new DNSAPIException("Query cannot be null");
		}
		
		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains.json?query=" + query + (reverse ? "&reverse=true" : ""), true);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getObjectList();
	}
	
	private Domain getById(Long domainId, boolean reverse) throws DNSAPIException {
		if (domainId == null) {
			throw new DNSAPIException("Domain id cannot be null");
		}
		
		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains/" + domainId + ".json" + (reverse ? "?reverse=true" : ""), false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getFirstObject();
	}
	
	/* 
	 * Updating features
	 */
	private void updateDomain(Long domainId, String name, String authorityType, String ttl, boolean reverse) throws DNSAPIException {
		if (domainId == null) {
			throw new DNSAPIException("Domain id cannot be null");
		}
		
		Domain domain = new Domain();
		if (name != null)
			domain.getDomainAttributes().setName(name);
		if (authorityType != null)
			domain.getDomainAttributes().setAuthorityType(authorityType);
		if (ttl != null)
			domain.getDomainAttributes().setTTL(ttl);
		
		DNSAPIRoot<Domain> dnsAPIRoot = this.put("/domains/" + domainId + ".json" + (reverse ? "?reverse=true" : ""), domain, false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return;
	}
	
	/* 
	 * Removing features
	 */
	private void removeDomain(Long domainId, boolean reverse) throws DNSAPIException {
		if (domainId == null) {
			throw new DNSAPIException("Domain id cannot be null");
		}
		
		DNSAPIRoot<Domain> dnsAPIRoot = this.delete("/domains/" + domainId + ".json" + (reverse ? "?reverse=true" : ""), false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return;
	}
}
