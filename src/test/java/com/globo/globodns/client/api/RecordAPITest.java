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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.globodns.client.GloboDnsException;
import com.globo.globodns.client.MockGloboDns;
import com.globo.globodns.client.MockGloboDns.HttpMethod;
import com.globo.globodns.client.api.RecordAPI;
import com.globo.globodns.client.model.Record;

@RunWith(JUnit4.class)
public class RecordAPITest {

	private RecordAPI recordAPI;
	private MockGloboDns rp;
	private Long domainId = 2000L; 
	
	@Before
	public void setUp() {
		this.rp = new MockGloboDns();
		this.recordAPI = this.rp.getRecordAPI();
	}
		
	@Test
	public void testGetById() throws GloboDnsException {
		Long recordId = 10L;
		this.rp.registerFakeRequest(HttpMethod.GET, "/records/" + recordId + ".json", 
				"{\"ns\":{\"content\":\"ns1.globoi.com.\",\"created_at\":\"2013-08-09T22:41:19Z\",\"domain_id\":2000,\"id\":10,\"name\":\"@\",\"prio\":null,\"ttl\":\"\",\"updated_at\":\"2013-08-09T22:41:19Z\"}}");
		
		Record record = this.recordAPI.getById(recordId);
		assertNotNull(record);
		assertEquals(recordId, record.getId());
	}
	
	@Test
	public void testListByQuery() throws GloboDnsException {
		
		String recordContent = "ns1.domain.com.";
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains/" + domainId + "/records.json?query=" + recordContent, 
				"[{\"ns\":{\"content\":\"ns1.domain.com.\",\"created_at\":\"2013-08-09T22:41:19Z\",\"domain_id\":2000,\"id\":20,\"name\":\"@\",\"prio\":null,\"ttl\":\"\",\"updated_at\":\"2013-08-09T22:41:19Z\"}}]");
		
		List<Record> recordList = this.recordAPI.listByQuery(domainId, recordContent);
		assertNotNull(recordList);
		assertEquals(1, recordList.size());
		
		Record record = recordList.get(0);
		assertEquals(Long.valueOf(20), record.getId());
		assertEquals(recordContent, record.getContent());
	}
	
	@Test
	public void testListByQueryRecordDoesntExist() throws GloboDnsException {
		
		String recordContent = "unexistant.domain.com";
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains/" + domainId + "/records.json?query=" + recordContent, 
				"[]");
		
		List<Record> recordList = this.recordAPI.listByQuery(domainId, recordContent);
		assertNotNull(recordList);
		assertEquals(0, recordList.size());
	}
	
	@Test
	public void testListAll() throws GloboDnsException {
		
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains/" + domainId + "/records.json", 
				"[{\"ns\":{\"content\":\"ns1.domain.com.\",\"created_at\":\"2013-08-13T18:46:15Z\",\"domain_id\":2000,\"id\":10,\"name\":\"@\",\"prio\":null,\"ttl\":null,\"updated_at\":\"2013-08-13T18:46:15Z\"}},"
				+ "{\"mx\":{\"content\":\"mx\",\"created_at\":\"2013-08-13T18:46:15Z\",\"domain_id\":2000,\"id\":20,\"name\":\"@\",\"prio\":0,\"ttl\":null,\"updated_at\":\"2013-08-13T18:46:15Z\"}},"
				+ "{\"a\":{\"content\":\"10.20.30.40\",\"created_at\":\"2013-08-13T18:46:15Z\",\"domain_id\":2000,\"id\":30,\"name\":\"@\",\"prio\":null,\"ttl\":null,\"updated_at\":\"2013-08-13T18:46:15Z\"}},"
				+ "{\"a\":{\"content\":\"50.60.70.80\",\"created_at\":\"2013-08-13T18:46:15Z\",\"domain_id\":2000,\"id\":40,\"name\":\"admin.pfc\",\"prio\":null,\"ttl\":null,\"updated_at\":\"2013-08-13T18:46:15Z\"}}]");
		List<Record> recordList = this.recordAPI.listAll(domainId);
		assertNotNull(recordList);
		assertEquals(4, recordList.size());
		
		Record record1 = recordList.get(0);
		assertEquals(Long.valueOf(10), record1.getId());
		assertEquals("ns1.domain.com.", record1.getContent());
		
		Record record2 = recordList.get(1);
		assertEquals(Long.valueOf(20), record2.getId());
		assertEquals("mx", record2.getContent());
		
		Record record3 = recordList.get(2);
		assertEquals(Long.valueOf(30), record3.getId());
		assertEquals("10.20.30.40", record3.getContent());
		
		Record record4 = recordList.get(3);
		assertEquals(Long.valueOf(40), record4.getId());
		assertEquals("50.60.70.80", record4.getContent());
	}

	@Test
	public void testCreateRecord() throws GloboDnsException {
		String newRecordName = "@";
		String newRecordContent = "ns1.domain.com.";
		String newRecordType = "NS";
		this.rp.registerFakeRequest(HttpMethod.POST, "/domains/" + domainId + "/records.json", 
				"{\"record\":{\"content\":\"ns1.domain.com.\",\"created_at\":\"2014-03-28T19:38:03Z\",\"domain_id\":2000,\"id\":10,\"name\":\"@\",\"prio\":null,\"ttl\":null,\"updated_at\":\"2014-03-28T19:38:03Z\"}}");
		
		Record createdRecord = this.recordAPI.createRecord(domainId, newRecordName, newRecordContent, newRecordType);
		assertNotNull(createdRecord);
		assertEquals(newRecordName, createdRecord.getName());
		assertEquals(newRecordContent, createdRecord.getContent());
	}	
	
	@Test
	public void testRemoveRecordNotFound() throws GloboDnsException {
		Long recordId = 1L;
		this.rp.registerFakeRequest(HttpMethod.DELETE, "/records/" + recordId + ".json", 404, "{\"error\":\"NOT FOUND\"}");
		
		try {
			this.recordAPI.removeRecord(recordId);
			fail();
		} catch (GloboDnsException ex) {
			assertEquals("NOT FOUND", ex.getMessage());
		}
	}

	@Test
	public void testGetTxtRecordById() throws GloboDnsException {
		Long recordId = 188564L;
		this.rp.registerFakeRequest(HttpMethod.GET, "/records/" + recordId + ".json", 
				"{\"txt\":{\"content\":\"n7sbs83-fd83d7-d-d734-dds-3e3d-33\",\"created_at\":\"2014-04-16T22:27:46Z\",\"domain_id\":5101,\"id\":188564,\"name\":\"cloudstack-network\",\"prio\":null,\"ttl\":\"\",\"updated_at\":\"2014-04-16T22:27:46Z\"}}");
		
		Record record = this.recordAPI.getById(recordId);
		assertNotNull(record);
		assertEquals(recordId, record.getId());
		assertEquals("cloudstack-network", record.getName());
		assertEquals("n7sbs83-fd83d7-d-d734-dds-3e3d-33", record.getContent());
	}
	

}
