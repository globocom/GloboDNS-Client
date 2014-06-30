/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.globo.dnsapi.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.dnsapi.AbstractAPI;
import com.globo.dnsapi.DNSAPIException;
import com.globo.dnsapi.DNSAPI;
import com.globo.dnsapi.model.DNSAPIRoot;
import com.globo.dnsapi.model.Record;

public class RecordAPI extends AbstractAPI<Record> {

	public RecordAPI(DNSAPI transport) {
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
