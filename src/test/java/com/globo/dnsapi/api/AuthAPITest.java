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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.dnsapi.DNSAPIException;
import com.globo.dnsapi.MockDNSAPI;
import com.globo.dnsapi.MockDNSAPI.HttpMethod;
import com.globo.dnsapi.model.Authentication;

@RunWith(JUnit4.class)
public class AuthAPITest {

	private AuthAPI authAPI;
	private MockDNSAPI dnsapi;
	
	@Before
	public void setUp() {
		this.dnsapi = new MockDNSAPI();
		this.authAPI = this.dnsapi.getAuthAPI();
	}
	
	@Test
	public void testSignIn() throws DNSAPIException {
		
		this.dnsapi.registerFakeRequest(HttpMethod.POST, "/users/sign_in.json", 
				"{\"authentication_token\":\"Xjn5GEsYsQySAsr7APqj\",\"id\":1}");
		Authentication auth = this.authAPI.signIn("admin@domain.com", "password");
		assertNotNull(auth);
		assertEquals(Long.valueOf(1), auth.getId());
		assertEquals("Xjn5GEsYsQySAsr7APqj", auth.getToken());
	}
	
	@Test
	public void testSignInWithInvalidPasswordDoesntRetry() throws DNSAPIException {
		
		try {
			this.dnsapi.registerFakeRequest(HttpMethod.POST, "/users/sign_in.json", 401,
					"{\"error\":\"You need to sign in or sign up before continuing.\"}");
			this.authAPI.signIn("error@error.com", "invalid");
			fail();
		} catch (DNSAPIException e) {
			assertEquals(1, this.dnsapi.totalNumberOfRequests());
		}
	}
	
}
