package com.globo.globodns.client.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.globodns.client.AbstractAPI;
import com.globo.globodns.client.GloboDns;
import com.globo.globodns.client.GloboDnsException;
import com.globo.globodns.client.model.GloboDnsRoot;
import com.globo.globodns.client.model.Domain;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

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
	@Trace(dispatcher = true)
	public Domain createDomain(String name, Long templateId, String authorityType) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/createDomain");
		return this.createDomain(name, templateId, authorityType, false);
	}

	@Trace(dispatcher = true)
	public List<Domain> listAll() throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/listDomains");
		return this.listAll(false);
	}

	@Trace(dispatcher = true)
	public List<Domain> listByQuery(String query) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/listDomainsByQuery");
		return this.listByQuery(query, false);
	}

	@Trace(dispatcher = true)
	public Domain getById(Long domainId) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/getDomainById");
		return this.getById(domainId, false);
	}

	@Trace(dispatcher = true)
	public void updateDomain(Long domainId, String name, String authorityType, String ttl) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/updateDomain");
		this.updateDomain(domainId, name, authorityType, ttl, false);
	}

	@Trace(dispatcher = true)
	public void removeDomain(Long domainId) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/removeDomain");
		this.removeDomain(domainId, false);
	}
	
			
	//////////////////////////
	// Reverse domain calls //
	//////////////////////////
	@Trace(dispatcher = true)
	public Domain createReverseDomain(String name, Long templateId, String authorityType) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/createReverseDomain");
		return this.createDomain(name, templateId, authorityType, true);
	}

	@Trace(dispatcher = true)
	public List<Domain> listAllReverse() throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/listAllReverseDomains");
		return this.listAll(true);
	}

	@Trace(dispatcher = true)
	public List<Domain> listReverseByQuery(String query) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/listReverseDomainsByQuery");
		return this.listByQuery(query, true);
	}

	@Trace(dispatcher = true)
	public Domain getReverseById(Long domainId) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/getReverseDomainById");
		return this.getById(domainId, true);
	}

	@Trace(dispatcher = true)
	public void updateReverseDomain(Long domainId, String name, String authorityType, String ttl) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/updateReverseDomain");
		this.updateDomain(domainId, name, authorityType, ttl, true);
	}

	@Trace(dispatcher = true)
	public void removeReverseDomain(Long domainId) throws GloboDnsException {
		NewRelic.setTransactionName(null, "/globodns/removeReverseDomain");
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
