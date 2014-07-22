package com.globo.globodns.client.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.globodns.client.AbstractAPI;
import com.globo.globodns.client.GloboDns;
import com.globo.globodns.client.GloboDnsException;
import com.globo.globodns.client.model.GloboDnsRoot;
import com.globo.globodns.client.model.Domain;

public class DomainAPI extends AbstractAPI<Domain> {
	
	public DomainAPI(GloboDns transport) {
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
	public Domain createDomain(String name, Long templateId, String authorityType) throws GloboDnsException {
		return this.createDomain(name, templateId, authorityType, false);
	}
	
	public List<Domain> listAll() throws GloboDnsException {
		return this.listAll(false);
	}
	
	public List<Domain> listByQuery(String query) throws GloboDnsException {
		return this.listByQuery(query, false);
	}
	
	public Domain getById(Long domainId) throws GloboDnsException {
		return this.getById(domainId, false);
	}
	
	public void updateDomain(Long domainId, String name, String authorityType, String ttl) throws GloboDnsException {
		this.updateDomain(domainId, name, authorityType, ttl, false);
	}
	
	public void removeDomain(Long domainId) throws GloboDnsException {
		this.removeDomain(domainId, false);
	}
	
			
	//////////////////////////
	// Reverse domain calls //
	//////////////////////////
	public Domain createReverseDomain(String name, Long templateId, String authorityType) throws GloboDnsException {
		return this.createDomain(name, templateId, authorityType, true);
	}
	
	public List<Domain> listAllReverse() throws GloboDnsException {
		return this.listAll(true);
	}
	
	public List<Domain> listReverseByQuery(String query) throws GloboDnsException {
		return this.listByQuery(query, true);
	}
	
	public Domain getReverseById(Long domainId) throws GloboDnsException {
		return this.getById(domainId, true);
	}
	
	public void updateReverseDomain(Long domainId, String name, String authorityType, String ttl) throws GloboDnsException {
		this.updateDomain(domainId, name, authorityType, ttl, true);
	}
	
	public void removeReverseDomain(Long domainId) throws GloboDnsException {
		this.removeDomain(domainId, true);
	}
	
	
	////////////////////
	// Implementation //
	////////////////////
	/* 
	 * Creating features 
	 */
	private Domain createDomain(String name, Long templateId, String authorityType, boolean reverse) throws GloboDnsException {
		Domain domain = new Domain();
		domain.getDomainAttributes().setName(name);
		domain.getDomainAttributes().setTemplateId(templateId);
		domain.getDomainAttributes().setAuthorityType(authorityType);
		
		GloboDnsRoot<Domain> globoDnsRoot = this.post("/domains.json" + (reverse ? "?reverse=true" : ""), domain, false);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return globoDnsRoot.getFirstObject();
	}
	
	/* 
	 * Recovering features
	 */
	private List<Domain> listAll(boolean reverse) throws GloboDnsException {
		GloboDnsRoot<Domain> globoDnsRoot = this.get("/domains.json" + (reverse ? "?reverse=true" : ""), true);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return globoDnsRoot.getObjectList();
	}
	
	private List<Domain> listByQuery(String query, boolean reverse) throws GloboDnsException {
		if (query == null) {
			throw new GloboDnsException("Query cannot be null");
		}
		
		GloboDnsRoot<Domain> globoDnsRoot = this.get("/domains.json?query=" + query + (reverse ? "&reverse=true" : ""), true);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return globoDnsRoot.getObjectList();
	}
	
	private Domain getById(Long domainId, boolean reverse) throws GloboDnsException {
		if (domainId == null) {
			throw new GloboDnsException("Domain id cannot be null");
		}
		
		GloboDnsRoot<Domain> globoDnsRoot = this.get("/domains/" + domainId + ".json" + (reverse ? "?reverse=true" : ""), false);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return globoDnsRoot.getFirstObject();
	}
	
	/* 
	 * Updating features
	 */
	private void updateDomain(Long domainId, String name, String authorityType, String ttl, boolean reverse) throws GloboDnsException {
		if (domainId == null) {
			throw new GloboDnsException("Domain id cannot be null");
		}
		
		Domain domain = new Domain();
		if (name != null)
			domain.getDomainAttributes().setName(name);
		if (authorityType != null)
			domain.getDomainAttributes().setAuthorityType(authorityType);
		if (ttl != null)
			domain.getDomainAttributes().setTTL(ttl);
		
		GloboDnsRoot<Domain> globoDnsRoot = this.put("/domains/" + domainId + ".json" + (reverse ? "?reverse=true" : ""), domain, false);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return;
	}
	
	/* 
	 * Removing features
	 */
	private void removeDomain(Long domainId, boolean reverse) throws GloboDnsException {
		if (domainId == null) {
			throw new GloboDnsException("Domain id cannot be null");
		}
		
		GloboDnsRoot<Domain> globoDnsRoot = this.delete("/domains/" + domainId + ".json" + (reverse ? "?reverse=true" : ""), false);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return;
	}
}
