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
package com.globo.dnsapi.flow;

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
import com.globo.dnsapi.api.AuthAPI;
import com.globo.dnsapi.api.DomainAPI;
import com.globo.dnsapi.model.Authentication;
import com.globo.dnsapi.model.Domain;

@RunWith(JUnit4.class)
public class DNSAPIFlowTest {
	
	private AuthAPI authAPI;
	private DomainAPI domainAPI;
	private MockDNSAPI rp;
	
	@Before
	public void setUp() {
		this.rp = new MockDNSAPI();
		this.authAPI = this.rp.getAuthAPI();
		this.domainAPI = this.rp.getDomainAPI();
	}

	@Test
	public void testDNSAPIFlow() throws DNSAPIException {
		
		// Variables
		String domainName = "anydomain.com";
		String email = "admin@domain.com";
		String password = "password";
		String newDomainName = "newdomain.com";
		String newAuthType = "M";
		
		// Step 0: Register all fake requests that will be made throughout the test
		this.rp.registerFakeRequest(HttpMethod.POST, "/users/sign_in.json", 
				"{\"authentication_token\":\"Xjn5GEsYsQySAsr7APqj\",\"id\":1}");
		
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json", 
				"[{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-25T01:04:05Z\",\"id\":1,\"last_check\":null,\"master\":null,\"name\":\"firstdomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-25T10:05:02Z\",\"user_id\":null,\"view_id\":null}},"
				+ "{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2013-08-22T19:48:54Z\",\"id\":2,\"last_check\":null,\"master\":null,\"name\":\"seconddomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2013-08-22T19:48:54Z\",\"user_id\":null,\"view_id\":null}},"
				+ "{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2013-08-13T18:38:37Z\",\"id\":3,\"last_check\":null,\"master\":null,\"name\":\"thirddomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"86400\",\"updated_at\":\"2013-08-13T18:38:37Z\",\"user_id\":null,\"view_id\":null}},"
				+ "{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-11T17:31:58Z\",\"id\":4,\"last_check\":null,\"master\":null,\"name\":\"fourthdomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-11T17:38:40Z\",\"user_id\":null,\"view_id\":null}}]");
		
		this.rp.registerFakeRequest(HttpMethod.GET, "/domains.json?query=" + domainName, 
				"[{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-11T17:31:58Z\",\"id\":0,\"last_check\":null,\"master\":null,\"name\":\"anydomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-11T17:38:40Z\",\"user_id\":null,\"view_id\":null}}]");

		this.rp.registerFakeRequest(HttpMethod.POST, "/domains.json", 
				"{\"domain\":{\"account\":null,\"addressing_type\":\"N\",\"authority_type\":\"M\",\"created_at\":\"2014-03-25T01:04:05Z\",\"id\":100,\"last_check\":null,\"master\":null,\"name\":\"newdomain.com\",\"notes\":null,\"notified_serial\":null,\"ttl\":\"10800\",\"updated_at\":\"2014-03-25T01:04:05Z\",\"user_id\":null,\"view_id\":null}}");

		
		// Step 1: Sign in
		Authentication auth = this.authAPI.signIn(email, password);
		
		assertNotNull(auth);
		assertEquals(Long.valueOf(1), auth.getId());
		assertEquals("Xjn5GEsYsQySAsr7APqj", auth.getToken());
		
		
		// Step 2: List all domains
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
		
		
		// Step 3: List the domain we want - domainName
		domainList = this.domainAPI.listByQuery(domainName);
		assertNotNull(domainList);
		assertEquals(1, domainList.size());
		
		Domain domain = domainList.get(0);
		assertEquals(Long.valueOf(0), domain.getId());
		assertEquals(domainName, domain.getName());
		
		
		// Step 4: Create a new domain
		Domain createdDomain = this.domainAPI.createDomain(newDomainName, 1L, newAuthType);
		assertNotNull(createdDomain);
		assertEquals(newDomainName, createdDomain.getName());
		assertEquals(newAuthType, createdDomain.getAuthorityType());
	}
}
