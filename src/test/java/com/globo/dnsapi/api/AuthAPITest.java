package com.globo.dnsapi.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.globo.dnsapi.TestRequestProcessor;
import com.globo.dnsapi.TestRequestProcessor.HttpMethod;
import com.globo.dnsapi.exception.DNSAPIException;
import com.globo.dnsapi.model.Authentication;

@RunWith(JUnit4.class)
public class AuthAPITest {

	private AuthAPI authAPI;
	private TestRequestProcessor rp;
	
	@Before
	public void setUp() {
		this.rp = new TestRequestProcessor();
		this.authAPI = this.rp.getAuthAPI();
	}
	
	@Test
	public void testSignIn() throws DNSAPIException {
		
		this.rp.registerFakeRequest(HttpMethod.POST, "/users/sign_in.json", 
				"{\"authentication_token\":\"Xjn5GEsYsQySAsr7APqj\",\"id\":1}");
		Authentication auth = this.authAPI.signIn("admin@globoi.com", "password");
		assertNotNull(auth);
		assertEquals(auth.getId(), Long.valueOf(1));
		assertEquals(auth.getToken(), "Xjn5GEsYsQySAsr7APqj");
	}

}
