package com.globo.dnsapi.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.dnsapi.DNSAPIException;
import com.globo.dnsapi.MockDNSAPI;
import com.globo.dnsapi.MockDNSAPI.HttpMethod;
import com.globo.dnsapi.model.Record;

@RunWith(JUnit4.class)
public class RecordAPITest {

	private RecordAPI recordAPI;
	private MockDNSAPI rp;
	private Long domainId = 2000L; 
	
	@Before
	public void setUp() {
		this.rp = new MockDNSAPI();
		this.recordAPI = this.rp.getRecordAPI();
	}
		
	@Test
	public void testGetById() throws DNSAPIException {
		Long recordId = 10L;
		this.rp.registerFakeRequest(HttpMethod.GET, "/records/" + recordId + ".json", 
				"{\"ns\":{\"content\":\"ns1.globoi.com.\",\"created_at\":\"2013-08-09T22:41:19Z\",\"domain_id\":2000,\"id\":10,\"name\":\"@\",\"prio\":null,\"ttl\":\"\",\"updated_at\":\"2013-08-09T22:41:19Z\"}}");
		
		Record record = this.recordAPI.getById(recordId);
		assertNotNull(record);
		assertEquals(recordId, record.getId());
	}
	
	@Test
	public void testListByQuery() throws DNSAPIException {
		
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
	public void testListByQueryRecordDoesntExist() throws DNSAPIException {
		
		String recordContent = "unexistant.domain.com";
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains/" + domainId + "/records.json?query=" + recordContent, 
				"[]");
		
		List<Record> recordList = this.recordAPI.listByQuery(domainId, recordContent);
		assertNotNull(recordList);
		assertEquals(0, recordList.size());
	}
	
	@Test
	public void testListAll() throws DNSAPIException {
		
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
	public void testCreateRecord() throws DNSAPIException {
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
}
