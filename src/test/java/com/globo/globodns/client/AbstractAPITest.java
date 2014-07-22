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
package com.globo.globodns.client;


import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.globo.globodns.client.AbstractAPI;
import com.globo.globodns.client.GloboDns;
import com.globo.globodns.client.MockGloboDns;
import com.globo.globodns.client.MockGloboDns.HttpMethod;
import com.globo.globodns.client.model.GloboDnsRoot;

@RunWith(JUnit4.class)
public class AbstractAPITest {

	private TestAPI testApi;
	private MockGloboDns rp;
	
	@Before
	public void setUp() {
		this.rp = spy(new MockGloboDns());
		this.testApi = new TestAPI(this.rp);
	}

	@Test
	public void whenTokenIsInvalidRetryWithAnotherToken() {
		this.rp.registerFakeRequest(HttpMethod.POST, "/request/test", 401,
				"{\"error\":\"You need to sign in or sign up before continuing.\"}");
		
		this.rp.requestToken();
		reset(this.rp);
		
		when(this.rp.buildToken()).then(new Answer<String>() {
		    public String answer(InvocationOnMock invocation) {
				rp.registerFakeRequest(HttpMethod.POST, "/request/test", 200,
						"\"valid answer\"");
		        return "new-token-on-test";
		    }
		});
		this.testApi.testRequest();
		// buildToken was called only one time
		verify(this.rp, times(1)).buildToken();
	}
	
	public static class TestAPI extends AbstractAPI<String> {

		protected TestAPI(GloboDns apiFactory) {
			super(apiFactory);
		}

		@Override
		protected Type getType() {
			return new TypeReference<String>() {}.getType();
		}

		@Override
		protected Type getListType() {
			return new TypeReference<List<String>>() {}.getType();
		}
		
		public String testRequest() {
			GloboDnsRoot<String> root = this.post("/request/test", null, false);
			return root.getFirstObject();
		}
	}
}
