package com.globo.dnsapi;


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
import com.globo.dnsapi.TestDNSAPIFactory.HttpMethod;
import com.globo.dnsapi.model.DNSAPIRoot;

@RunWith(JUnit4.class)
public class AbstractAPITest {

	private TestAPI testApi;
	private TestDNSAPIFactory rp;
	
	@Before
	public void setUp() {
		this.rp = spy(new TestDNSAPIFactory());
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

		protected TestAPI(DNSAPIFactory apiFactory) {
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
			DNSAPIRoot<String> root = this.post("/request/test", null, false);
			return root.getFirstObject();
		}
	}
}
