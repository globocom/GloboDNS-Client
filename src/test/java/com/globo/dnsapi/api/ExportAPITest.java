package com.globo.dnsapi.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.dnsapi.TestRequestProcessor;
import com.globo.dnsapi.TestRequestProcessor.HttpMethod;
import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.model.Export;

@RunWith(JUnit4.class)
public class ExportAPITest {

	private ExportAPI exportAPI;
	private TestRequestProcessor rp;
	
	@Before
	public void setUp() {
		this.rp = new TestRequestProcessor();
		this.exportAPI = this.rp.getExportAPI();
	}
	
	@Test
	public void testScheduleExport() throws DNSAPIException {
		this.rp.registerFakeRequest(HttpMethod.POST, "/bind9/schedule_export.json", 
				"{\"output\":\"BIND export scheduled for 2012-07-14 02:35:00 -0300\"}");
		
		Export export = this.exportAPI.scheduleExport();
		assertNotNull(export);
	}
	
	@Test
	public void testExportNoExportScheduled() throws DNSAPIException {
		this.rp.registerFakeRequest(HttpMethod.POST, "/bind9/export.json", 
				"{\"output\":\"No BIND export scheduled.\"}");
		
		Export export = this.exportAPI.export(false, false);
		assertNotNull(export);
	}
	
	@Test
	public void testExportWithExportScheduled() throws DNSAPIException {
		this.rp.registerFakeRequest(HttpMethod.POST, "/bind9/export.json", 
				"{\"output\":\"BIND export scheduled for 2012-07-14 02:35:00 -0300\"}");
		
		Export export = this.exportAPI.export(false, false);
		assertNotNull(export);
	}
	
	@Test
	public void testExportWithNowParameter() throws DNSAPIException {
		this.rp.registerFakeRequest(HttpMethod.POST, "/bind9/export.json", 
				"{\"output\":\"Successful\"}");
		
		Export export = this.exportAPI.export(true, false);
		assertNotNull(export);
	}
}
