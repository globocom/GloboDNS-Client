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
	
	////////////
	// Domain //
	////////////
	
	/* 
	 * Creating features 
	 */
	public Domain createDomain(String name, Long templateId, String authorityType) throws DNSAPIException {
		Domain domain = new Domain();
		domain.getDomainAttributes().setName(name);
		domain.getDomainAttributes().setTemplateId(templateId);
		domain.getDomainAttributes().setAuthorityType(authorityType);
		
		DNSAPIRoot<Domain> dnsAPIRoot = this.post("/domains.json", domain, false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getFirstObject();
	}
	
	/* 
	 * Recovering features
	 */
	public List<Domain> listAll() throws DNSAPIException {
		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains.json", true);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getObjectList();
	}
	
	public List<Domain> listByName(String domainName) throws DNSAPIException {
		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains.json?query=" + domainName, true);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getObjectList();
	}
	
	public Domain getById(Long domainId) throws DNSAPIException {
		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains/" + domainId + ".json", false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getFirstObject();
	}
	
	/* 
	 * Updating features
	 */
	public void updateDomain(Long domainId, String name, String authorityType, String ttl) throws DNSAPIException {
		Domain domain = new Domain();
		domain.getDomainAttributes().setId(domainId);
		domain.getDomainAttributes().setName(name);
		domain.getDomainAttributes().setAuthorityType(authorityType);
		domain.getDomainAttributes().setTTL(ttl);
		
		DNSAPIRoot<Domain> dnsAPIRoot = this.put("/domains/" + domainId + ".json", domain, false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return;
	}
	
	/* 
	 * Removing features
	 */
	public void removeDomain(Long domainId) throws DNSAPIException {
		DNSAPIRoot<Domain> dnsAPIRoot = this.delete("/domains/" + domainId + ".json", false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return;
	}
	
	
	////////////////////
	// Reverse domain //
	////////////////////
	
	// FIXME Reverse not working! Need to fix the API to accept a reverse parameter with GET method
	
	/* 
	 * Recovering features
	 */
//	public List<Domain> listAllReverse() throws DNSAPIException {
//		// FIXME Reverse flag is being passed in the header,
//		// but DNS API accepts only in the body, even with GET method
//		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains.json", true);
//		if (dnsAPIRoot == null) {
//			throw new DNSAPIException("Invalid response");
//		}
//		return dnsAPIRoot.getObjectList();
//	}
//	
//	public List<Domain> listReverseByName(String reverseDomain) throws DNSAPIException {
//		// FIXME Reverse flag is being passed in the header,
//		// but DNS API accepts only in the body, even with GET method
//		DNSAPIRoot<Domain> dnsAPIRoot = this.get("/domains.json?query=" + reverseDomain, true);
//		if (dnsAPIRoot == null) {
//			throw new DNSAPIException("Invalid response");
//		}
//		return dnsAPIRoot.getObjectList();
//	}
}
