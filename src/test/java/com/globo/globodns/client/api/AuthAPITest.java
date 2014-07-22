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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.globodns.client.GloboDnsException;
import com.globo.globodns.client.MockGloboDns;
import com.globo.globodns.client.MockGloboDns.HttpMethod;
import com.globo.globodns.client.api.AuthAPI;
import com.globo.globodns.client.model.Authentication;

@RunWith(JUnit4.class)
public class AuthAPITest {

	private AuthAPI authAPI;
	private MockGloboDns globoDns;
	
	@Before
	public void setUp() {
		this.globoDns = new MockGloboDns();
		this.authAPI = this.globoDns.getAuthAPI();
	}
	
	@Test
	public void testSignIn() throws GloboDnsException {
		
		this.globoDns.registerFakeRequest(HttpMethod.POST, "/users/sign_in.json", 
				"{\"authentication_token\":\"Xjn5GEsYsQySAsr7APqj\",\"id\":1}");
		Authentication auth = this.authAPI.signIn("admin@domain.com", "password");
		assertNotNull(auth);
		assertEquals(Long.valueOf(1), auth.getId());
		assertEquals("Xjn5GEsYsQySAsr7APqj", auth.getToken());
	}
	
	@Test
	public void testSignInWithInvalidPasswordDoesntRetry() throws GloboDnsException {
		
		try {
			this.globoDns.registerFakeRequest(HttpMethod.POST, "/users/sign_in.json", 401,
					"{\"error\":\"You need to sign in or sign up before continuing.\"}");
			this.authAPI.signIn("error@error.com", "invalid");
			fail();
		} catch (GloboDnsException e) {
			assertEquals(1, this.globoDns.totalNumberOfRequests());
		}
	}
	
}
