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
import com.globo.dnsapi.model.Domain;

@RunWith(JUnit4.class)
public class DomainAPITest {

	private DomainAPI domainAPI;
	private MockDNSAPI rp;
	
	@Before
	public void setUp() {
		this.rp = new MockDNSAPI();
		this.domainAPI = this.rp.getDomainAPI();
	}
	
	@Test(expected=DNSAPIException.class)
	public void testMissingToken() throws DNSAPIException {
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json", 401,
				"{\"error\":\"You need to sign in or sign up before continuing.\"}");
		
		this.domainAPI.listAll();
	}
	
	@Test(expected=DNSAPIException.class)
	public void testInvalidToken() throws DNSAPIException {
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json", 401,
				"{\"error\":\"Invalid authentication token.\"}");

		this.domainAPI.listAll();
	}
	
	@Test
	public void testGetById() throws DNSAPIException {
		Long domainId = 10L;
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains/" + domainId + ".json", 
				"{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-11T17:31:58Z\",\"id\":10,\"last_check\":null,\"master\":null,\"name\":\"anydomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-11T17:38:40Z\",\"user_id\":null,\"view_id\":null}}");
		
		Domain domain = this.domainAPI.getById(domainId);
		assertNotNull(domain);
		assertEquals(domainId, domain.getId());
	}
	
	@Test
	public void testListByName() throws DNSAPIException {
		
		String domainName = "anydomain.com";
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json?query=" + domainName, 
				"[{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-11T17:31:58Z\",\"id\":0,\"last_check\":null,\"master\":null,\"name\":\"anydomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-11T17:38:40Z\",\"user_id\":null,\"view_id\":null}}]");
		
		List<Domain> domainList = this.domainAPI.listByQuery(domainName);
		assertNotNull(domainList);
		assertEquals(1, domainList.size());
		
		Domain domain = domainList.get(0);
		assertEquals(Long.valueOf(0), domain.getId());
		assertEquals(domainName, domain.getName());
	}
	
	@Test
	public void testListReverseByName() throws DNSAPIException {
		
		String reverseDomain = "10.10.10.in-addr.arpa";
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json?query=" + reverseDomain + "&reverse=true", 
				"[{\"domain\":{\"account\":null,\"addressing_type\":\"R\",\"authority_type\":\"M\",\"created_at\":\"2014-03-11T17:31:58Z\",\"id\":0,\"last_check\":null,\"master\":null,\"name\":\"10.10.10.in-addr.arpa\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-11T17:38:40Z\",\"user_id\":null,\"view_id\":null}}]");
		
		List<Domain> domainList = this.domainAPI.listReverseByQuery(reverseDomain);
		assertNotNull(domainList);
		assertEquals(1, domainList.size());
		
		Domain domain = domainList.get(0);
		assertEquals(Long.valueOf(0), domain.getId());
		assertEquals(reverseDomain, domain.getName());
	}
	
	@Test
	public void testListByNameDomainDoesntExist() throws DNSAPIException {
		
		String domainName = "unexistant.dev.globoi.com";
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json?query=" + domainName, 
				"[]");
		
		List<Domain> domainList = this.domainAPI.listByQuery(domainName);
		assertNotNull(domainList);
		assertEquals(0, domainList.size());
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
		assertEquals(4, domainList.size());
		
		Domain domain1 = domainList.get(0);
		assertEquals(Long.valueOf(1), domain1.getId());
		assertEquals("N", domain1.getAddressType());
		assertEquals("firstdomain.com", domain1.getName());
		
		Domain domain2 = domainList.get(1);
		assertEquals(Long.valueOf(2), domain2.getId());
		assertEquals("N", domain2.getAddressType());
		assertEquals("seconddomain.com", domain2.getName());
		
		Domain domain3 = domainList.get(2);
		assertEquals(Long.valueOf(3), domain3.getId());
		assertEquals("N", domain3.getAddressType());
		assertEquals("thirddomain.com", domain3.getName());
		
		Domain domain4 = domainList.get(3);
		assertEquals(Long.valueOf(4), domain4.getId());
		assertEquals("N", domain4.getAddressType());
		assertEquals("fourthdomain.com", domain4.getName());
	}
	
	@Test
	public void testListAllReverse() throws DNSAPIException {
		
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json?reverse=true", 
				"[{\"domain\":{\"account\":null,\"addressing_type\":\"R\",\"authority_type\":\"M\",\"created_at\":\"2013-08-13T18:46:25Z\",\"id\":1,\"last_check\":null,\"master\":null,\"name\":\"0.11.10.in-addr.arpa\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"3H\",\"updated_at\":\"2013-08-13T18:46:25Z\",\"user_id\":null,\"view_id\":null}},"
				+ "{\"domain\":{\"account\":null,\"addressing_type\":\"R\",\"authority_type\":\"M\",\"created_at\":\"2013-08-13T18:46:25Z\",\"id\":2,\"last_check\":null,\"master\":null,\"name\":\"0.17.10.in-addr.arpa\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"3H\",\"updated_at\":\"2013-08-13T18:46:25Z\",\"user_id\":null,\"view_id\":null}},"
				+ "{\"domain\":{\"account\":null,\"addressing_type\":\"R\",\"authority_type\":\"M\",\"created_at\":\"2013-08-13T18:46:25Z\",\"id\":3,\"last_check\":null,\"master\":null,\"name\":\"0.170.10.in-addr.arpa\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"3H\",\"updated_at\":\"2013-08-13T18:46:25Z\",\"user_id\":null,\"view_id\":null}}]");
				
		List<Domain> domainList = this.domainAPI.listAllReverse();
		assertNotNull(domainList);
		assertEquals(3, domainList.size());
		
		Domain domain1 = domainList.get(0);
		assertEquals(Long.valueOf(1), domain1.getId());
		assertEquals("R", domain1.getAddressType());
		assertEquals("0.11.10.in-addr.arpa", domain1.getName());
		
		Domain domain2 = domainList.get(1);
		assertEquals(Long.valueOf(2), domain2.getId());
		assertEquals("R", domain2.getAddressType());
		assertEquals("0.17.10.in-addr.arpa", domain2.getName());
		
		Domain domain3 = domainList.get(2);
		assertEquals(Long.valueOf(3), domain3.getId());
		assertEquals("R", domain3.getAddressType());
		assertEquals("0.170.10.in-addr.arpa", domain3.getName());
	}
	
	@Test
	public void testCreateDomain() throws DNSAPIException {
		String newDomainName = "newdomain.com";
		String newAuthType = "M";
		this.rp.registerFakeRequest(HttpMethod.POST, "/domains.json", 
				"{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-25T01:04:05Z\",\"id\":100,\"last_check\":null,\"master\":null,\"name\":\"newdomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-25T01:04:05Z\",\"user_id\":null,\"view_id\":null}}");
		
		Domain createdDomain = this.domainAPI.createDomain(newDomainName, 1L, newAuthType);
		assertNotNull(createdDomain);
		assertEquals(newDomainName, createdDomain.getName());
		assertEquals(newAuthType, createdDomain.getAuthorityType());
	}
	
	@Test(expected=DNSAPIException.class)
	public void testCreateDomainAlreadyExists() throws DNSAPIException {
		String newDomainName = "newdomain.com";
		String newAuthType = "M";
		this.rp.registerFakeRequest(HttpMethod.POST, "/domains.json", 422,
				"{\"errors\":{\"name\":[\"has already been taken\"]}}");
		
		this.domainAPI.createDomain(newDomainName, 1L, newAuthType);
	}
	
	@Test
	public void testCreateReverseDomain() throws DNSAPIException {
		String newReverseDomainName = "0.10.10.in-addr.arpa";
		String newReverseAuthType = "M";
		this.rp.registerFakeRequest(HttpMethod.POST, "/domains.json?reverse=true", 
				"{\"domain\":{\"account\":null,\"addressing_type\":\"R\",\"authority_type\":\"M\",\"created_at\":\"2014-03-28T20:40:34Z\",\"id\":20,\"last_check\":null,\"master\":null,\"name\":\"0.10.10.in-addr.arpa\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-28T20:40:34Z\",\"user_id\":null,\"view_id\":null}}");
		
		Domain createdReverseDomain = this.domainAPI.createReverseDomain(newReverseDomainName, 1L, newReverseAuthType);
		assertNotNull(createdReverseDomain);
		assertEquals(newReverseDomainName, createdReverseDomain.getName());
		assertEquals(newReverseAuthType, createdReverseDomain.getAuthorityType());
	}
}
