package com.globo.dnsapi.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	private MockDNSAPI rp;
	
	@Before
	public void setUp() {
		this.rp = new MockDNSAPI();
		this.authAPI = this.rp.getAuthAPI();
	}
	
	@Test
	public void testSignIn() throws DNSAPIException {
		
		this.rp.registerFakeRequest(HttpMethod.POST, "/users/sign_in.json", 
				"{\"authentication_token\":\"Xjn5GEsYsQySAsr7APqj\",\"id\":1}");
		Authentication auth = this.authAPI.signIn("admin@domain.com", "password");
		assertNotNull(auth);
		assertEquals(Long.valueOf(1), auth.getId());
		assertEquals("Xjn5GEsYsQySAsr7APqj", auth.getToken());
	}
	
}
