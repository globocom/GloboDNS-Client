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
package com.globo.globodns.client.api;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.globodns.client.AbstractAPI;
import com.globo.globodns.client.GloboDns;
import com.globo.globodns.client.GloboDnsException;
import com.globo.globodns.client.model.GloboDnsRoot;
import com.globo.globodns.client.model.Record;

public class RecordAPI extends AbstractAPI<Record> {

	public RecordAPI(GloboDns transport) {
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
	public Record createRecord(Long domainId, String name, String content, String type) throws GloboDnsException {
		if (domainId == null) {
			throw new GloboDnsException("Domain id cannot be null");
		}
		
		Record record = new Record();
		record.getGenericRecordAttributes().setDomainId(domainId);
		record.getGenericRecordAttributes().setName(name);
		record.getGenericRecordAttributes().setContent(content);
		record.getGenericRecordAttributes().setType(type.toUpperCase());
		
		GloboDnsRoot<Record> globoDnsRoot = this.post("/domains/" + domainId + "/records.json", record, false);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return globoDnsRoot.getFirstObject();
	}
	
	/* 
	 * Recovering features
	 */
	public List<Record> listAll(Long domainId) throws GloboDnsException {
		if (domainId == null) {
			throw new GloboDnsException("Domain id cannot be null");
		}
		
		GloboDnsRoot<Record> globoDnsRoot = this.get("/domains/" + domainId + "/records.json", true);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return globoDnsRoot.getObjectList();
	}
	
	public List<Record> listByQuery(Long domainId, String query) throws GloboDnsException {
		if (domainId == null) {
			throw new GloboDnsException("Domain id cannot be null");
		}
		
		if (query == null) {
			throw new GloboDnsException("Query cannot be null");
		}
		
		GloboDnsRoot<Record> globoDnsRoot = this.get("/domains/" + domainId + "/records.json?query=" + query, true);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return globoDnsRoot.getObjectList();
	}
	
	public Record getById(Long recordId) throws GloboDnsException {
		if (recordId == null) {
			throw new GloboDnsException("Record id cannot be null");
		}
		
		GloboDnsRoot<Record> globoDnsRoot = this.get("/records/" + recordId + ".json", false);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return globoDnsRoot.getFirstObject();
	}
	
	/* 
	 * Updating features
	 */
	public void updateRecord(Long recordId, Long domainId, String name, String content) throws GloboDnsException {
		if (recordId == null) {
			throw new GloboDnsException("Record id cannot be null");
		}
		
		Record record = new Record();
		if (domainId != null)
			record.getGenericRecordAttributes().setDomainId(domainId);
		if (name != null)
			record.getGenericRecordAttributes().setName(name);
		if (content != null)
			record.getGenericRecordAttributes().setContent(content);
		
		GloboDnsRoot<Record> globoDnsRoot = this.put("/records/" + recordId + ".json", record, false);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return;
	}
	
	/* 
	 * Removing features
	 */
	public void removeRecord(Long recordId) throws GloboDnsException {
		if (recordId == null) {
			throw new GloboDnsException("Record id cannot be null");
		}
		
		GloboDnsRoot<Record> globoDnsRoot = this.delete("/records/" + recordId + ".json", false);
		if (globoDnsRoot == null) {
			throw new GloboDnsException("Invalid response");
		}
		return;	
	}
}
