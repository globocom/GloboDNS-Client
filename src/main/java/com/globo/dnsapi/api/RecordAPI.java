package com.globo.dnsapi.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.http.RequestProcessor;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.globo.dnsapi.model.Record;

public class RecordAPI extends BaseAPI<Record> {

	public RecordAPI(RequestProcessor transport) {
		super(transport);
	}
	
	@Override
	protected Type getType() {
		return new TypeReference<Record>() {}.getType();
	}
	
	@Override
	protected Type getListType() {
		return new TypeReference<List<Record>>() {}.getType();
	}

	
	////////////////////
	// Implementation //
	////////////////////
	/* 
	 * Creating features 
	 */
	public Record createRecord(Long domainId, String name, String content, String type) throws DNSAPIException {
		if (domainId == null) {
			throw new DNSAPIException("Domain id cannot be null");
		}
		
		Record record = new Record();
		record.getGenericRecordAttributes().setDomainId(domainId);
		record.getGenericRecordAttributes().setName(name);
		record.getGenericRecordAttributes().setContent(content);
		record.getGenericRecordAttributes().setType(type.toUpperCase());
		
		DNSAPIRoot<Record> dnsAPIRoot = this.post("/domains/" + domainId + "/records.json", record, false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getFirstObject();
	}
	
	/* 
	 * Recovering features
	 */
	public List<Record> listAll(Long domainId) throws DNSAPIException {
		if (domainId == null) {
			throw new DNSAPIException("Domain id cannot be null");
		}
		
		DNSAPIRoot<Record> dnsAPIRoot = this.get("/domains/" + domainId + "/records.json", true);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getObjectList();
	}
	
	public List<Record> listByQuery(Long domainId, String query) throws DNSAPIException {
		if (domainId == null) {
			throw new DNSAPIException("Domain id cannot be null");
		}
		
		if (query == null) {
			throw new DNSAPIException("Query cannot be null");
		}
		
		DNSAPIRoot<Record> dnsAPIRoot = this.get("/domains/" + domainId + "/records.json?query=" + query, true);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getObjectList();
	}
	
	public Record getById(Long recordId) throws DNSAPIException {
		if (recordId == null) {
			throw new DNSAPIException("Record id cannot be null");
		}
		
		DNSAPIRoot<Record> dnsAPIRoot = this.get("/records/" + recordId + ".json", false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return dnsAPIRoot.getFirstObject();
	}
	
	/* 
	 * Updating features
	 */
	public void updateRecord(Long recordId, Long domainId, String name, String content) throws DNSAPIException {
		if (recordId == null) {
			throw new DNSAPIException("Record id cannot be null");
		}
		
		Record record = new Record();
		if (domainId != null)
			record.getGenericRecordAttributes().setDomainId(domainId);
		if (name != null)
			record.getGenericRecordAttributes().setName(name);
		if (content != null)
			record.getGenericRecordAttributes().setContent(content);
		
		DNSAPIRoot<Record> dnsAPIRoot = this.put("/records/" + recordId + ".json", record, false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return;
	}
	
	/* 
	 * Removing features
	 */
	public void removeRecord(Long recordId) throws DNSAPIException {
		if (recordId == null) {
			throw new DNSAPIException("Record id cannot be null");
		}
		
		DNSAPIRoot<Record> dnsAPIRoot = this.delete("/records/" + recordId + ".json", false);
		if (dnsAPIRoot == null) {
			throw new DNSAPIException("Invalid response");
		}
		return;	
	}
}
