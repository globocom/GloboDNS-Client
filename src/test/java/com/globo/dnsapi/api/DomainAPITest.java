package com.globo.dnsapi.api;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.dnsapi.TestRequestProcessor;
import com.globo.dnsapi.TestRequestProcessor.HttpMethod;
import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.model.Domain;

@RunWith(JUnit4.class)
public class DomainAPITest {

	private DomainAPI domainAPI;
	private TestRequestProcessor rp;
	
	@Before
	public void setUp() {
		this.rp = new TestRequestProcessor();
		this.domainAPI = this.rp.getDomainAPI();
	}
	
	@Test
	public void testListByName() throws DNSAPIException {
		
		String domainName = "anydomain.com";
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json?query=" + domainName, 
				"[{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-11T17:31:58Z\",\"id\":0,\"last_check\":null,\"master\":null,\"name\":\"anydomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-11T17:38:40Z\",\"user_id\":null,\"view_id\":null}}]");
		
		List<Domain> domainList = this.domainAPI.listByName(domainName);
		assertNotNull(domainList);
		assertEquals(domainList.size(), 1);
		
		Domain domain = domainList.get(0);
		assertEquals(domain.getId(), Long.valueOf(0));
		assertEquals(domain.getName(), domainName);
	}
	
	@Test
	public void testListByNameDomainDoesntExist() throws DNSAPIException {
		
		String domainName = "unexistant.dev.globoi.com";
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json?query=" + domainName, 
				"[]");
		
		List<Domain> domainList = this.domainAPI.listByName(domainName);
		assertNotNull(domainList);
		assertEquals(domainList.size(), 0);
	}
	
	@Test
	public void testListAll() throws DNSAPIException {
		
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json", 
				"[{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-25T01:04:05Z\",\"id\":1,\"last_check\":null,\"master\":null,\"name\":\"firstdomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-25T10:05:02Z\",\"user_id\":null,\"view_id\":null}},"
				+ "{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2013-08-22T19:48:54Z\",\"id\":2,\"last_check\":null,\"master\":null,\"name\":\"seconddomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2013-08-22T19:48:54Z\",\"user_id\":null,\"view_id\":null}},"
				+ "{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2013-08-13T18:38:37Z\",\"id\":3,\"last_check\":null,\"master\":null,\"name\":\"thirddomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"86400\",\"updated_at\":\"2013-08-13T18:38:37Z\",\"user_id\":null,\"view_id\":null}},"
				+ "{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-11T17:31:58Z\",\"id\":4,\"last_check\":null,\"master\":null,\"name\":\"fourthdomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-11T17:38:40Z\",\"user_id\":null,\"view_id\":null}}]");
		List<Domain> domainList = this.domainAPI.listAll();
		assertNotNull(domainList);
		assertEquals(domainList.size(), 4);
		
		Domain domain1 = domainList.get(0);
		assertEquals(domain1.getId(), Long.valueOf(1));
		assertEquals(domain1.getName(), "firstdomain.com");
		
		Domain domain2 = domainList.get(1);
		assertEquals(domain2.getId(), Long.valueOf(2));
		assertEquals(domain2.getName(), "seconddomain.com");
		
		Domain domain3 = domainList.get(2);
		assertEquals(domain3.getId(), Long.valueOf(3));
		assertEquals(domain3.getName(), "thirddomain.com");
		
		Domain domain4 = domainList.get(3);
		assertEquals(domain4.getId(), Long.valueOf(4));
		assertEquals(domain4.getName(), "fourthdomain.com");
	}
}
